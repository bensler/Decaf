package com.bensler.decaf.util;

import java.util.function.BiFunction;
import java.util.function.Function;

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

  public <LP, RP> Pair<LP, RP> map(
    Function<? super L, ? extends LP> leftMapper,
    Function<? super R, ? extends RP> rightMapper
  ) {
    return new Pair<>(leftMapper.apply(left_), rightMapper.apply(right_));
  }

  public <RESULT> RESULT map(BiFunction<? super L, ? super R, RESULT> mapper) {
    return mapper.apply(left_, right_);
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
