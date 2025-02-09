package com.bensler.decaf.util.function;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

/** Behaves like {@link Function#identity()} but lets you execute a Consumer during mapping.<p>
 *
 * That allows you to chain multiple {@link Stream#forEach(Consumer)} operations after another.
 */
public class ForEachMapperAdapter<T> implements Function<T, T> {

  public static <E> ForEachMapperAdapter<E> forEachMapper(Consumer<E> consumer) {
    return new ForEachMapperAdapter<>(consumer);
  }

  private final Consumer<T> consumer_;

  public ForEachMapperAdapter(Consumer<T> consumer) {
    consumer_ = Objects.requireNonNull(consumer);
  }

  @Override
  public T apply(T t) {
    consumer_.accept(t);
    return t;
  }

}
