package com.bensler.decaf.swing.table;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;


/**
 */
final class ComparatorList<E> extends Object implements Comparator<E> {

  private   final         LinkedList<ComparatorWrapper>   sorting_;

  ComparatorList() {
    sorting_ = new LinkedList<>();
  }

  private void addCompWrapper(ComparatorWrapper wrapper) {
    final int index = sorting_.indexOf(wrapper);

    if (index > 0) {
      while (sorting_.size() > index) {
        sorting_.removeLast();
      }
    }
    sorting_.addFirst(wrapper);
  }

  @Override
  public int compare(E o1, E o2) {
    int cmpVal = 0;

    for (int i = 0; i < sorting_.size(); i++) {
      cmpVal = sorting_.get(i).compare(o1, o2);
      if (cmpVal != 0) {
        break;
      }
    }
    return cmpVal;
  }

  Sorting getSorting(Column<?> column) {
    if (
      (!sorting_.isEmpty())
      && (sorting_.getFirst().column_ == column)
    ) {
      return (sorting_.getFirst()).sorting_.getOpposite();
    } else {
      return Sorting.ASCENDING;
    }
  }

  /**@return if order is just switched */
  boolean addSorting(Column<?> column, Sorting sorting) {
    if (!sorting_.isEmpty()) {
      final ComparatorWrapper first = sorting_.getFirst();

      if (first.column_ == column) {
        final Sorting oldSorting = first.sorting_;

        first.setSorting(sorting);
        return (sorting == oldSorting.getOpposite());
      }
    }
    addCompWrapper(new ComparatorWrapper(
      column, sorting
    ));
    return false;
  }

  boolean isEmpty() {
    return sorting_.isEmpty();
  }

  List<String> getSortPrefs() {
    final List<String> result = new ArrayList<>(sorting_.size() * 2);

    for (int i = 0; i < sorting_.size(); i++) {
      final ComparatorWrapper wrapper = sorting_.get(i);

      result.add(wrapper.column_.getView().getId());
      result.add(Boolean.toString(wrapper.sorting_.isAscending()));
    }
    return result;
  }

//  void loadSortPrefs(String[] sortings, ColumnModel colModel) {
//    try {
//      sorting_.clear();
//      for (int i = 0; i < sortings.length; i += 2) {
//        final boolean           asc     = Boolean.valueOf(sortings[i + 1]).booleanValue();
//        final Column            column  = colModel.resolveColumn(new StaticRef(TablePropertyView.class, sortings[i]));
//        final ComparatorWrapper wrapper;
//
//        if (column.isSortable()) {
//          wrapper = new ComparatorWrapper(
//            column, (asc ? Sorting.ASCENDING : Sorting.DESCENDING)
//          );
//
//          if (sorting_.contains(wrapper)) {
//            break;
//          } else {
//            sorting_.addLast(wrapper);
//          }
//        }
//      }
//    } catch (RuntimeException re) {
//      // ignore
//    }
//    if (!sorting_.isEmpty()) {
//      final ComparatorWrapper first = sorting_.getFirst();
//
//      colModel.setSorting(first.column_, first.sorting_);
//    }
//  }

  private final class ComparatorWrapper extends Object implements Comparator<E> {

    private   final         Column      column_;

                            Sorting     sorting_;

    ComparatorWrapper(
      Column column, Sorting sorting
    ) {
      column_ = column;
      sorting_ = sorting;
    }

    @Override
    public boolean equals(Object obj) {
      return ((obj.getClass().equals(getClass())) && (((ComparatorWrapper)obj).column_.equals(column_)));
    }

    @Override
    public int hashCode() {
      return column_.hashCode();
    }

    void setSorting(Sorting sorting) {
      sorting_ = sorting;
    }

    @Override
    public int compare(E o1, E o2) {
      return (sorting_.getFactor() * column_.getView().compare(o1, o2));
    }

  }

  void clear() {
    sorting_.clear();
  }

}