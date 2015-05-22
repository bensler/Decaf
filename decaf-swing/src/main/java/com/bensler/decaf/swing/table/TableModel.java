package com.bensler.decaf.swing.table;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.table.AbstractTableModel;

/**
 */
public class TableModel<E> extends AbstractTableModel {

  /** TODO weg */
            final         EntityTable         boTable_;

  private   final         TableView           view_;

  private   final         Set<E>              entitiesUnfiltered_;

  private   final         List<E>             entityList_;

  private   final         ComparatorList      comparator_;

  TableModel(TableView view, EntityTable boTable) {
    super();
    boTable_ = boTable;
    view_ = view;
    entitiesUnfiltered_ = new HashSet<>();
    entityList_ = new ArrayList<>();
    comparator_ = new ComparatorList();
  }

  @Override
  public Object getValueAt(int row, int col) {
    return view_.getCellValue(col, getValueAt(row));
  }

  void fireRowFilterChanged() {
    setData(new HashSet<E>(entitiesUnfiltered_));
  }

  E getValueAt(int row) {
    return entityList_.get(row);
  }

  Sorting getSorting(Column column) {
    return comparator_.getSorting(column);
  }

  void sortByColumn(Column column, Sorting sorting) {
    if (comparator_.addSorting(column, sorting)) {
      final List<E> selectionBefore = boTable_.setSelectionSilent();

      try {
        Collections.reverse(entityList_);
        fireTableDataChanged();
      } finally {
        boTable_.setSelectionUnsilent(selectionBefore);
      }
    } else {
      resort();
    }
  }

  void clearSorting() {
    comparator_.clear();
  }

  void setData(Collection<E> newData) {
    entitiesUnfiltered_.clear();
    entitiesUnfiltered_.addAll(newData);

    final int oldSize = entityList_.size();
    entityList_.clear();
    for (final E subject : newData) {
      entityList_.add(subject);
    }
    resort(oldSize);
  }

  private void resort() {
    resort(entityList_.size());
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

    entitiesUnfiltered_.removeAll(data);
    entitiesUnfiltered_.addAll(data);
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
    entitiesUnfiltered_.removeAll(subjects);
    entitiesUnfiltered_.addAll(subjects);

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
    removeData(Collections.singleton(subject));
  }

  void removeData(Collection<?> bos) {
    entitiesUnfiltered_.removeAll(bos);

    final int     oldSize = entityList_.size();
          boolean resort  = false;

    for (final Object subject : bos) {
      final int index = entityList_.indexOf(subject);

      if (index >= 0) {
        entityList_.remove(index);
        resort = true;
      }
    }
    if (resort) {
      resort(oldSize);
    }
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
    final List<E> returnValue = new ArrayList<>(indices.length);

    for (int i = 0; i < indices.length && i < entityList_.size(); i++) {
      returnValue.add(entityList_.get(indices[i]));
    }
    return returnValue;
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
