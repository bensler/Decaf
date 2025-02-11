package com.bensler.decaf.swing.table;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.IntStream;

import javax.swing.table.AbstractTableModel;

import com.bensler.decaf.util.Pair;

/**
 */
public class TableModel<E> extends AbstractTableModel {

  private final TableView<E> view_;

  private final List<E> entityList_;

  private final ComparatorList<E> comparator_;

  TableModel(TableView<E> view) {
    super();
    view_ = view;
    entityList_ = new ArrayList<>();
    comparator_ = new ComparatorList<>();
  }

  @Override
  public Object getValueAt(int row, int col) {
    return view_.getCellValue(col, getValueAt(row));
  }

  E getValueAt(int row) {
    return entityList_.get(row);
  }

  Optional<Pair<Sorting, Integer>> getSorting(Column<?> column) {
    return comparator_.getSorting(column);
  }

  Sorting getNewSorting(Column<E> column) {
    return comparator_.getNewSorting(column);
  }

  void sortByColumn(Column<E> column, Sorting sorting) {
    try (var notifier = new SortingChangedNotifier(false)) {
      comparator_.sortByColumn(column, sorting);
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

  String getSortPrefs() {
    return comparator_.getSortPrefs();
  }

  void applySortPrefs(String sortings, Map<String, Column<E>> columnsById) {
    try (var notifier = new SortingChangedNotifier(false)) {
      comparator_.applySortPrefs(sortings, columnsById);
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
