package com.bensler.decaf.swing.view;

import java.util.Comparator;
import java.util.Optional;
import java.util.function.Function;

import com.bensler.decaf.util.cmp.ComparableComparator;
import com.bensler.decaf.util.cmp.NullSafeComparator;

public class SimplePropertyGetter<E, P> implements PropertyGetter<E, P> {

  public static <T, IR, R> Function<T, R> chain(Function<T, IR> first, Function<IR, R> second) {
    return t -> Optional.ofNullable(first.apply(t)).map(second).orElse(null);
  }

  public static <E1, P1  extends Comparable<P1>> SimplePropertyGetter<E1, P1> createComparableGetter(Function<E1, P1> getter) {
    return new SimplePropertyGetter<>(getter, new ComparableComparator<>());
  }

  private final Function<E, P> getter_;

  private final Comparator<E> comparator_;

  public SimplePropertyGetter(Function<E, P> getter, EntityComparator<? super E> comparator) {
    getter_ = getter;
    comparator_ = new NullSafeComparator<>(comparator);
  }

  public SimplePropertyGetter(Function<E, P> getter, Comparator<? super P> propertyComparator) {
    getter_ = getter;
    comparator_ = new EntityComparator<>(getter, propertyComparator);
  }

  @Override
  public P getProperty(E viewable) {
    return getter_.apply(viewable);
  }

  @Override
  public int compare(E e1, E e2) {
    return comparator_.compare(e1, e2);
  }

}
