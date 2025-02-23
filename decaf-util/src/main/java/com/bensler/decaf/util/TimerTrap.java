package com.bensler.decaf.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

public class TimerTrap implements AutoCloseable {

  private static final StatisticsCollector STATS = new StatisticsCollector();

  public StatisticsCollector getStats() {
    return STATS;
  }

  private long startNanoTime_;
  private final Consumer<Long> milliesCollector_;

  public TimerTrap(String collectorName) {
    this(STATS.getNamedCollector(collectorName));
  }

  public TimerTrap(Consumer<Long> milliesCollector) {
    milliesCollector_ = Objects.requireNonNull(milliesCollector);
    startNanoTime_ = System.nanoTime();
  }

  @Override
  public void close() {
    milliesCollector_.accept((System.nanoTime() - startNanoTime_) / 1_000_000);
  }

  public static class StatisticsCollector {

    private final Map<String, List<Long>> statistics;

    public StatisticsCollector(){
      statistics = new HashMap<>();
    }
    public Consumer<Long> getNamedCollector(String collectorName) {
      return statistics.computeIfAbsent(collectorName, key -> new ArrayList<>())::add;
    }

  }

}
