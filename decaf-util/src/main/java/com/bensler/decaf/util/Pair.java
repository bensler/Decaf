package com.bensler.decaf.util;

public class Pair<L, R> {

  private final L left_;
  private final R right_;

  public Pair(L left, R right) {
    left_ = left;
    right_ = right;
  }

  public L getLeft() {
    return left_;
  }

  public R getRight() {
    return right_;
  }

  @Override
  public int hashCode() {
    return ((left_  != null) ?  left_.hashCode() : 0)
         + ((right_ != null) ? right_.hashCode() : 0);
  }

  @Override
  public boolean equals(Object obj) {
    return (obj != null)
        && (obj instanceof Pair pair)
        && ((left_  != null) && ( left_.equals(pair.left_)))
        && ((right_ != null) && (right_.equals(pair.right_)));
  }

  @Override
  public String toString() {
    return "%s[%s,%s]".formatted(getClass().getSimpleName(), left_, right_);
  }

}
