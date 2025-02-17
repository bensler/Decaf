package com.bensler.decaf.swing.table;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

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

  void sortByColumn(TableColumn column, Comparator<E> comparator, Sorting sorting) {
    sorting_.putFirst(column, new ComparatorWrapper<>(comparator, sorting));
  }

  boolean isEmpty() {
    return sorting_.isEmpty();
  }

  void clear() {
    sorting_.clear();
  }

  List<Pair<TableColumn, Sorting>> getSorting() {
    return sorting_.entrySet().stream()
      .map(entry -> new Pair<>(entry.getKey(), entry.getValue().sorting_))
      .toList();
  }

  public void removeColumnFromSorting(TableColumn column) {
    sorting_.remove(column);
  }

  final static class ComparatorWrapper<E> extends Object implements Comparator<E> {

    final Comparator<E> comparator_;
    final Sorting sorting_;

    ComparatorWrapper(Comparator<E> comparator, Sorting sorting) {
      comparator_ = comparator;
      sorting_ = sorting;
    }

    Sorting getSorting() {
      return sorting_;
    }

    @Override
    public int compare(E o1, E o2) {
      return (sorting_.getFactor() * comparator_.compare(o1, o2));
    }

  }

}