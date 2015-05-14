package com.bensler.decaf.util.cmp;

import java.util.Comparator;

public class NullPolicy<T> extends Object implements Comparator<T> {

  public static enum NULLS {

    FIRST(-1),
    LAST(1);

    private final int firstIsNull_;

    NULLS(int firstIsNull) {
      firstIsNull_ = firstIsNull;
    }

    public int getFirstIsNull() {
      return firstIsNull_;
    }

  }

  private final Comparator<? super T> delegate_;

  private final NULLS                 nulls_;

  public NullPolicy(NULLS nulls, Comparator<? super T> delegate) {
    super();
    nulls_ = nulls;
    delegate_ = delegate;
  }

  public NullPolicy(Comparator<? super T> delegate) {
    this(NULLS.FIRST, delegate);
  }

  @Override
  public int compare(T v1, T v2) {
    if (v1 == null) {
      return ((v2 == null) ? 0 : nulls_.getFirstIsNull());
    } else {
      return ((v2 == null) ? (nulls_.getFirstIsNull() * -1) : delegate_.compare(v1, v2));
    }
  }

}