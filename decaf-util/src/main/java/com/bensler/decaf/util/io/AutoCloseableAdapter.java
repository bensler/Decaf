package com.bensler.decaf.util.io;

import java.util.function.Consumer;

public class AutoCloseableAdapter<T> implements AutoCloseable {

  private final Consumer<T> closer_;
  public final T target_;

  public AutoCloseableAdapter(T target, Consumer<T> closer) {
    target_ = target;
    closer_ = closer;
  }

  @Override
  public void close() {
    closer_.accept(target_);
  }

}