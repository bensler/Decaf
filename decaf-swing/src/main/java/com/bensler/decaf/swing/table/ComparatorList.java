package com.bensler.decaf.swing.table;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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

  Sorting getNewSorting(Column<E> column) {
    return getSorting(column).map(
      pair -> (pair.getRight() == 0) ? pair.getLeft().getOpposite() : pair.getLeft()
    ).orElse(Sorting.ASCENDING);
  }

  void sortByColumn(Column<E> column, Sorting sorting) {
    sorting_.putFirst(column, new ComparatorWrapper<>(column, sorting));
  }

  boolean isEmpty() {
    return sorting_.isEmpty();
  }

  void clear() {
    sorting_.clear();
  }

  String getSortPrefs() {
    return sorting_.entrySet().stream()
      .map(entry -> entry.getKey().getId() + ":" + entry.getValue().sorting_)
      .collect(Collectors.joining(","));
  }

  void applySortPrefs(String sortings, Map<String, Column<E>> columnsById) {
    List.of(sortings.split(",")).reversed().forEach(str -> {
      final String[] idSorting = str.split(":");
      final String columnId = idSorting[0];

      if ((idSorting.length == 2) && columnsById.containsKey(columnId)) {
        try {
          sortByColumn(columnsById.get(columnId), Sorting.valueOf(idSorting[1]));
        } catch (IllegalArgumentException iae) { /* idSorting[1] did not match a Sorting enum value */ }
      }
    });
  }

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