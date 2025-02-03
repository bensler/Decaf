package com.bensler.decaf.swing.table;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

import com.bensler.decaf.util.Pair;


/**
 */
final class ComparatorList<E> extends Object implements Comparator<E> {

  private final LinkedHashMap<Column<E>, ComparatorWrapper<E>> sorting_;

  ComparatorList() {
    sorting_ = new LinkedHashMap<>();
  }

  @Override
  public int compare(E e1, E e2) {
    return sorting_.values().stream()
    .mapToInt(wrapper -> wrapper.compare(e1, e2))
    .filter(cmpVal -> (cmpVal != 0))
    .findFirst().orElse(0);
  }

  Optional<Pair<Sorting, Integer>> getSorting(Column<?> column) {
    return Optional.ofNullable(sorting_.get(column)).map(cmpWrapper -> new Pair<>(
      cmpWrapper.getSorting(), List.copyOf(sorting_.sequencedKeySet()).indexOf(column)
    ));
  }

  Sorting sortByColumn(Column<E> column) {
    final Sorting sorting = getSorting(column)
    .map(Pair::getLeft)
    .map(Sorting::getOpposite)
    .orElse(Sorting.ASCENDING);

    sortByColumn(column, sorting);
    return sorting;
  }

  void sortByColumn(Column<E> column, Sorting sorting) {
    sorting_.putFirst(column, new ComparatorWrapper<>(column, sorting));
  }

  boolean isEmpty() {
    return sorting_.isEmpty();
  }

  List<String> getSortPrefs() {
    return sorting_.entrySet().stream()
    .map(entry -> entry.getKey().getView().getId() + ":" + entry.getValue().sorting_)
    .toList();
  }

  void clear() {
    sorting_.clear();
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

  final static class ComparatorWrapper<E> extends Object implements Comparator<E> {

    final Column<E> column_;
    final Sorting sorting_;

    ComparatorWrapper(
      Column<E> column, Sorting sorting
    ) {
      column_ = column;
      sorting_ = sorting;
    }

    Sorting getSorting() {
      return sorting_;
    }

    @Override
    public int compare(E o1, E o2) {
      return (sorting_.getFactor() * column_.getView().compare(o1, o2));
    }

  }

}