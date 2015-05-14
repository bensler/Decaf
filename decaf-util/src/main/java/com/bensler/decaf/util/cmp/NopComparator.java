package com.bensler.decaf.util.cmp;

import java.util.Comparator;

public final class NopComparator extends Object implements Comparator<Object> {

  public final static NopComparator NOP_COMPARATOR = new NopComparator();

  @Override
  public int compare(Object v1, Object v2) {
    return 0;
  }

}