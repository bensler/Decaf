package com.bensler.decaf.util.cmp;

import java.util.Comparator;

/**
 * TODO
 */
public final class ComparableComparator<E extends Comparable<E>> extends Object implements Comparator<E> {

  public ComparableComparator() { }

  @Override
  public int compare(E p1, E p2) {
    return p1.compareTo(p2);
  }

}