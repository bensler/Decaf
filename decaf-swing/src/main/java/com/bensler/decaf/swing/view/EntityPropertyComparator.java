package com.bensler.decaf.swing.view;

import java.util.Comparator;
import java.util.function.Function;

import com.bensler.decaf.util.cmp.NullSafeComparator;

public class EntityPropertyComparator<E, P> implements PropertyGetter<E, P>, Function<E, P> {

  private final Function<E, P> getter_;
  private final Comparator<E> comparator_;

  public EntityPropertyComparator(Function<E, P> getter, Comparator<? super P> propertyComparator) {
    final Comparator<P> nullSafePropCmp = new NullSafeComparator<>(propertyComparator);

    getter_ = getter;
    comparator_ = new NullSafeComparator<>(
      (e1, e2) -> nullSafePropCmp.compare(getter_.apply(e1), getter_.apply(e2))
    );
  }

  @Override
  public int compare(E e1, E e2) {
    return comparator_.compare(e1, e2);
  }

  @Override
  public P getProperty(E entity) {
    return apply(entity);
  }

  @Override
  public P apply(E entity) {
    return getter_.apply(entity);
  }

}
