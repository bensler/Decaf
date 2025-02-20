package com.bensler.decaf.util.stream;

import static java.util.stream.Collectors.toMap;

import java.util.LinkedHashMap;
import java.util.function.Function;
import java.util.stream.Collector;

public class Collectors {

  private Collectors() { }

  public static <T, K, U> Collector<T, ?, LinkedHashMap<K, U>> toSortedMap(
    Function<? super T, ? extends K> keyMapper, Function<? super T, ? extends U> valueMapper
  ) {
    return toMap(
      keyMapper, valueMapper,
      (u1, u2) -> {
        throw new IllegalStateException("duplicate mapping");
      },
      LinkedHashMap::new
    );
  }

}
