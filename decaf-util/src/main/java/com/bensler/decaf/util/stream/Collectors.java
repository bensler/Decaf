package com.bensler.decaf.util.stream;

import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class Collectors {

  private Collectors() { }

  public static <T, K, U, M extends Map<K, U>> Collector<T, ?, M> toMap(
    Function<? super T, ? extends K> keyMapper, Function<? super T, ? extends U> valueMapper, Supplier<M> mapSupplier
  ) {
    return java.util.stream.Collectors.toMap(
      keyMapper, valueMapper,
      (_, _) -> {
        throw new IllegalStateException("duplicate mapping");
      },
      mapSupplier
    );
  }

}
