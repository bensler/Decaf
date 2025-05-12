package com.bensler.decaf.util.cmp;

import java.text.Collator;
import java.util.Comparator;
import java.util.List;

/**
 * For some reason {@link Collator} implements {@link Comparator} with generic parameter {@link Object} but
 * trying to compare non-{@link String} objects results in a {@link ClassCastException} at runtime. To fix
 * this issue {@link ComparatorChain} wraps a {@link Collator} but implements {@link Comparator} with
 * generic parameter {@link String}.
 */
public final class ComparatorChain<E> extends Object implements Comparator<E> {

  private final List<Comparator<E>> comparators_;

  public ComparatorChain(List<Comparator<E>> comparators) {
    comparators_ = List.copyOf(comparators);
  }

  @Override
  public int compare(E e1, E e2) {
    return comparators_.stream()
      .mapToInt(cmp -> cmp.compare(e1, e2))
      .filter(i -> (i != 0))
      .findFirst().orElse(0);
  }

}