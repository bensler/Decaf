package com.bensler.decaf.util.cmp;

import java.util.Comparator;

/**
 * Comparator for {@link Comparable} objects. <b>Not</b> <code>null</code> safe.
 */
public final class ComparableComparator<E extends Comparable<E>> extends Object implements Comparator<E> {

  public ComparableComparator() { }

  @Override
  public int compare(E p1, E p2) {
    return p1.compareTo(p2);
  }

}