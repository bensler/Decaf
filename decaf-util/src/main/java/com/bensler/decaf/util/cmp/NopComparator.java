package com.bensler.decaf.util.cmp;

import java.util.Comparator;

/**
 * Kind of <code>null</code> object pattern implementation of {@link Comparator}. It always
 * returns 0 whatever you drop in. Any type of object can be "compared" with any other type
 * of object, <code>null</code>s allowed.
 * <p>
 * As this class is stateless {@link #NOP_COMPARATOR} might be used instead of creating
 * new instances.
 */
public final class NopComparator extends Object implements Comparator<Object> {

  public final static NopComparator NOP_COMPARATOR = new NopComparator();

  @Override
  public int compare(Object v1, Object v2) {
    return 0;
  }

}