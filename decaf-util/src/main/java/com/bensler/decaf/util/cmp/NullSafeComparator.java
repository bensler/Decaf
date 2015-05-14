package com.bensler.decaf.util.cmp;

import java.util.Comparator;

public class NullSafeComparator<T> extends Object implements Comparator<T> {

  public static enum NullPolicy {

    NULLS_FIRST(-1),
    NULLS_LAST(1);

    private final int firstIsNull_;

    NullPolicy(int firstIsNull) {
      firstIsNull_ = firstIsNull;
    }

    public int getFirstIsNull() {
      return firstIsNull_;
    }

  }

  private final Comparator<? super T> delegate_;

  private final NullPolicy nullPolicy_;

  public NullSafeComparator(NullPolicy nulls, Comparator<? super T> delegate) {
    super();
    nullPolicy_ = nulls;
    delegate_ = delegate;
  }

  public NullSafeComparator(Comparator<? super T> delegate) {
    this(NullPolicy.NULLS_FIRST, delegate);
  }

  @Override
  public int compare(T v1, T v2) {
    if (v1 == null) {
      return ((v2 == null) ? 0 : nullPolicy_.getFirstIsNull());
    } else {
      return ((v2 == null) ? (nullPolicy_.getFirstIsNull() * -1) : delegate_.compare(v1, v2));
    }
  }

  public Comparator<? super T> getDelegate() {
    return delegate_;
  }

  public NullPolicy getNullPolicy() {
    return nullPolicy_;
  }

}