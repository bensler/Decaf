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

  public static <E1, P1> SimplePropertyGetter<E1, P1> createGetterComparator(Function<E1, P1> getter, Comparator<P1> propertyComparator) {
    return new SimplePropertyGetter<>(new EntityPropertyComparator<>(getter, propertyComparator));
  }

  public static <E1, P1> SimplePropertyGetter<E1, P1> createGetter(Function<E1, P1> getter, Comparator<E1> comparator) {
    return new SimplePropertyGetter<>(getter, new NullSafeComparator<>(comparator));
  }

  public static <E1, P1  extends Comparable<P1>> SimplePropertyGetter<E1, P1> createComparableGetter(Function<E1, P1> getter) {
    return createGetterComparator(getter, new ComparableComparator<>());
  }

  private final Function<E, P> getter_;
  private final Comparator<E> comparator_;

  public SimplePropertyGetter(Function<E, P> getter, Comparator<E> comparator) {
    getter_ = getter;
    comparator_ = comparator;
  }

  public SimplePropertyGetter(EntityPropertyComparator<E, P> entityComparator) {
    getter_ = entityComparator;
    comparator_ = entityComparator;
  }

  @Override
  public P getProperty(E entity) {
    return getter_.apply(entity);
  }

  @Override
  public int compare(E e1, E e2) {
    return comparator_.compare(e1, e2);
  }

}
