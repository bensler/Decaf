package com.bensler.decaf.swing.table;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import javax.swing.table.AbstractTableModel;

/**
 */
public class TableModel<E> extends AbstractTableModel {

  /** TODO rm */
            final         EntityTable<E>      boTable_;

  private final TableView<E> view_;

  private final List<E> entityList_;

  private final ComparatorList<E> comparator_;

  TableModel(TableView<E> view, EntityTable<E> boTable) {
    super();
    boTable_ = boTable;
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

  Optional<Sorting> getSorting(Column<E> column) {
    return comparator_.getSorting(column);
  }

  Sorting sortByColumn(Column<E> column) {
    try {
      return comparator_.sortByColumn(column);
    } finally {
      Collections.sort(entityList_, comparator_);
      fireTableDataChanged();
    }
  }

  void sortByColumn(Column<E> column, Sorting sorting) {
    comparator_.sortByColumn(column, sorting);
    Collections.sort(entityList_, comparator_);
    fireTableDataChanged();
  }

  void clearSorting() {
    comparator_.clear();
  }

  void setData(Collection<E> newData) {
    final int oldSize = entityList_.size();
    entityList_.clear();
    for (final E subject : newData) {
      entityList_.add(subject);
    }
    Collections.sort(entityList_, comparator_);
    fireTableDataChanged();
//    resort(oldSize);
  }

  private void resort() {
//    resort(entityList_.size());
  }

  private void resort(int oldSize) {
    final List<E> selectionBefore = boTable_.setSelectionSilent();

    try {
      if (!comparator_.isEmpty()) {
        Collections.sort(entityList_, comparator_);
        if (oldSize == entityList_.size()) {
          fireTableDataChanged();
        } else {
          fireTableStructureChanged();
        }
      }
    } finally {
      boTable_.setSelectionUnsilent(selectionBefore);
    }
  }

  int indexOf(Object subject) {
    return entityList_.indexOf(subject);
  }

  void addData(E subject) {
    addData(Collections.singleton(subject));
  }

  void addData(Collection<? extends E> data) {
    final int oldSize = entityList_.size();

    for (E subject : data) {
      final int       index   = indexOf(subject);

      if (index >= 0) {
        entityList_.remove(index);
      }
      entityList_.add(subject);
    }
    resort(oldSize);
  }

  void updateData(E subject) {
    updateData(Collections.singleton(subject));
  }

  void updateData(Collection<? extends E> subjects) {
    final int     oldSize = entityList_.size();
          boolean resort  = false;

    for (E subject : subjects) {
      final int index = indexOf(subject);

      if (index >= 0) {
        entityList_.remove(index);
        entityList_.add(index, subject);
        resort = true;
      }
    }
    if (resort) {
      resort(oldSize);
    }
  }

  void removeData(Object subject) {
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

  boolean contains(Object bo) {
    return entityList_.contains(bo);
  }

  List<String> getSortPrefs() {
    return comparator_.getSortPrefs();
  }

//  void loadSortPrefs(String[] sortings, ColumnModel colModel) {
//    if (boTable_.isSortable()) {
//      comparator_.loadSortPrefs(sortings, colModel);
//      if (!comparator_.isEmpty()) {
//        resort();
//      }
//    }
//  }

}
