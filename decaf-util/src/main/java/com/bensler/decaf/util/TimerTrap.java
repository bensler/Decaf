package com.bensler.decaf.util;

import java.util.Objects;
import java.util.function.Consumer;

public class TimerTrap implements AutoCloseable {

  private long startNanoTime_;
  private final Consumer<Long> milliesCollector_;

  public TimerTrap(Consumer<Long> milliesCollector) {
    milliesCollector_ = Objects.requireNonNull(milliesCollector);
    startNanoTime_ = System.nanoTime();
  }

  @Override
  public void close() {
    milliesCollector_.accept((System.nanoTime() - startNanoTime_) / 1_000_000);
  }

}
