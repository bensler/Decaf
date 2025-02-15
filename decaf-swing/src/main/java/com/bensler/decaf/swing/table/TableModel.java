package com.bensler.decaf.swing.table;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;

import com.bensler.decaf.util.Pair;
import com.bensler.decaf.util.prefs.Prefs;

/**
 */
public class TableModel<E> extends AbstractTableModel {

  private final TableView<E> view_;

  private final ColumnsController<E> colCtrl_;

  private final List<E> entityList_;

  private final ComparatorList<E> comparator_;

  TableModel(TableView<E> view) {
    view_ = view;
    entityList_ = new ArrayList<>();
    comparator_ = new ComparatorList<>();
    colCtrl_ = new ColumnsController<>(this);
  }

  TableView<E> getView() {
    return view_;
  }

  ColumnsController<E> getColumnsController() {
    return colCtrl_;
  }

  @Override
  public Object getValueAt(int row, int col) {
    return view_.getCellValue(col, getValueAt(row));
  }

  E getValueAt(int row) {
    return entityList_.get(row);
  }

  Optional<Pair<Sorting, Integer>> getSorting(TableColumn column) {
    return comparator_.getSorting(column);
  }

  Sorting getNewSorting(TableColumn column) {
    return comparator_.getNewSorting(column);
  }

  void sortByColumn(TableColumn column, Sorting sorting) {
    try (var notifier = new SortingChangedNotifier(false)) {
      comparator_.sortByColumn(column, colCtrl_.getView(column), sorting);
    }
  }

  void clearSorting() {
    try (var notifier = new SortingChangedNotifier(false)) {
      comparator_.clear();
    }
  }

  int indexOf(Object subject) {
    return entityList_.indexOf(subject);
  }

  void addOrUpdateData(Collection<? extends E> data) {
    try (var notifier = new SortingChangedNotifier(true)) {
      data.forEach(entity -> {
        final int index = entityList_.indexOf(entity);

        if (index < 0) {
          entityList_.add(entity);
        } else {
          entityList_.set(index, entity);
        }
      });
    }
  }

  private void removeData(Object subject) {
    final int index = entityList_.indexOf(subject);

    if (index >= 0) {
      entityList_.remove(index);
      fireTableRowsDeleted(index, index);
    }
  }

  void removeData(Collection<?> bos) {
    bos.forEach(this::removeData);
  }

  @Override
  public int getRowCount() {
    return entityList_.size();
  }

  @Override
  public int getColumnCount() {
    return view_.getColumnCount();
  }

  @Override
  public String getColumnName(int column) {
    return view_.getName(column);
  }

  List<E> getValues(int[] indices) {
    return IntStream.of(indices).mapToObj(entityList_::get).toList();
  }

  List<E> getValues() {
    return new ArrayList<>(entityList_);
  }

  Optional<E> contains(Object object) {
    final int index = entityList_.indexOf(object);

    return (index < 0) ? Optional.empty() : Optional.of(entityList_.get(index));
  }

  void applyColumnWidthPrefs(String widths) {
    colCtrl_.applyColumnWidthPrefs(Arrays.stream(widths.split(","))
      .map(str -> str.split(":"))
      .filter(idWidth -> idWidth.length == 2)
      .map(idWidth -> new Pair<>(idWidth[0], Prefs.tryParseInt(idWidth[1])))
      .filter(idWidth -> idWidth.getRight().isPresent())
      .map(idWidth -> idWidth.map(Function.identity(), Optional::get))
      .toList());
  }

  String getSortPrefs() {
    return comparator_.getSorting().stream()
      .map(pair -> pair.map(colCtrl_::getView, Function.identity()))
      .map(pair -> pair.getLeft().getId() + ":" + pair.getRight())
      .collect(Collectors.joining(","));
  }

  void applySortPrefs(String sortings) {
    try (var notifier = new SortingChangedNotifier(false)) {
      List.of(sortings.split(",")).reversed().stream()
        .map(str -> str.split(":"))
        .filter(idSorting -> idSorting.length == 2)
        .map(idSorting -> new Pair<>(idSorting[0], idSorting[1]))
        .map(idSorting -> idSorting.map(colCtrl_::getColumn, sortStr -> Prefs.tryParseEnum(Sorting.class, sortStr)))
        .filter(propViewSorting -> propViewSorting.getLeft().isPresent() && propViewSorting.getRight().isPresent())
        .forEach(propViewSorting -> {
          sortByColumn(propViewSorting.getLeft().get(), propViewSorting.getRight().get());
        });
    }
  }

  class SortingChangedNotifier implements AutoCloseable {

    private final boolean alwaysFireEvent_;
    private final List<E> oldEntityList_;

    SortingChangedNotifier(boolean alwaysFireEvent) {
      oldEntityList_ = (alwaysFireEvent_ = alwaysFireEvent) ? List.of() : List.copyOf(entityList_);
    }

    @Override
    public void close() {
      Collections.sort(entityList_, comparator_);
      if (alwaysFireEvent_ || (!oldEntityList_.equals(entityList_))) {
        fireTableDataChanged();
      }
    }

  }

}
