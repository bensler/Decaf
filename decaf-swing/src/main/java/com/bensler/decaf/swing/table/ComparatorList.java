package com.bensler.decaf.swing.table;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.swing.table.TableColumn;

import com.bensler.decaf.util.Pair;


/**
 */
final class ComparatorList<E> extends Object implements Comparator<E> {

  private final LinkedHashMap<TableColumn, ComparatorWrapper<E>> sorting_;

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

  Optional<Pair<Sorting, Integer>> getSorting(TableColumn column) {
    return Optional.ofNullable(sorting_.get(column)).map(cmpWrapper -> new Pair<>(
      cmpWrapper.getSorting(), List.copyOf(sorting_.sequencedKeySet()).indexOf(column)
    ));
  }

  Sorting getNewSorting(TableColumn column) {
    return getSorting(column).map(
      pair -> (pair.getRight() == 0) ? pair.getLeft().getOpposite() : pair.getLeft()
    ).orElse(Sorting.ASCENDING);
  }

  void sortByColumn(Column<E> column, Sorting sorting) {
    sorting_.putFirst(column, new ComparatorWrapper<>(column.getView(), sorting));
  }

  boolean isEmpty() {
    return sorting_.isEmpty();
  }

  void clear() {
    sorting_.clear();
  }

  String getSortPrefs() {
    return sorting_.values().stream()
      .map(wrapper -> wrapper.view_.getId() + ":" + wrapper.sorting_)
      .collect(Collectors.joining(","));
  }

  void applySortPrefs(String sortings, Map<String, Column<E>> columnsById) {
    List.of(sortings.split(",")).reversed().stream()
      .map(str -> str.split(":"))
      .filter(idSorting -> idSorting.length == 2)
      .map(idSorting -> new Pair<>(idSorting[0], idSorting[1]))
      .filter(idSorting -> columnsById.containsKey(idSorting.getLeft()))
      .forEach(idSorting -> {
        try {
          sortByColumn(columnsById.get(idSorting.getLeft()), Sorting.valueOf(idSorting.getRight()));
        } catch (IllegalArgumentException iae) { /* idSorting[1] did not match a Sorting enum value */ }
      });
  }

  final static class ComparatorWrapper<E> extends Object implements Comparator<E> {

    final TablePropertyView<E, ?> view_;
    final Sorting sorting_;

    ComparatorWrapper(TablePropertyView<E, ?> view, Sorting sorting) {
      view_ = view;
      sorting_ = sorting;
    }

    Sorting getSorting() {
      return sorting_;
    }

    @Override
    public int compare(E o1, E o2) {
      return (sorting_.getFactor() * view_.compare(o1, o2));
    }

  }

}