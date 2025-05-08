package com.bensler.decaf.util.cmp;

import java.util.Comparator;

/**
 * Whenever you write a {@link Comparator} your code need to deal with <code>null</code> references
 * being possibly passed in. Your custom {@link Comparator} can rely on always receiving two non-<code>null</code>
 * objects to compare if you wrap a {@link NullSafeComparator} around.
 */
public class NullSafeComparator<T> extends Object implements Comparator<T> {

  /**
   * Defines the two ways of dealings with <code>null</code> values. Kind of Named Boolean pattern.
   */
  public static enum NullPolicy {

    /** <code>null</code> references are considered smaller than any other non null object. */
    NULLS_FIRST(-1),
    /** <code>null</code> references are considered greater than any other non null object. */
    NULLS_LAST(1);

    final int firstIsNull_;

    NullPolicy(int firstIsNull) {
      firstIsNull_ = firstIsNull;
    }

  }

  private final Comparator<? super T> delegate_;

  private final NullPolicy nullPolicy_;

  public NullSafeComparator(NullPolicy nullPolicy, Comparator<? super T> delegate) {
    nullPolicy_ = nullPolicy;
    delegate_ = delegate;
  }

  /**
   * Uses {@link NullPolicy#NULLS_FIRST} internally.
   */
  public NullSafeComparator(Comparator<? super T> delegate) {
    this(NullPolicy.NULLS_FIRST, delegate);
  }

  @Override
  public int compare(T v1, T v2) {
    if (v1 == null) {
      return ((v2 == null) ? 0 : nullPolicy_.firstIsNull_);
    } else {
      return ((v2 == null) ? (nullPolicy_.firstIsNull_ * -1) : delegate_.compare(v1, v2));
    }
  }

}