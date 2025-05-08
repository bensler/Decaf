package com.bensler.decaf.swing.view;

import java.util.Comparator;
import java.util.function.Function;

import com.bensler.decaf.util.cmp.NullSafeComparator;

public class EntityComparator<E> implements Comparator<E> {

  private final Comparator<E> comparator_;

  public <P> EntityComparator(Function<E, P> getter, Comparator<? super P> propertyComparator) {
    final Comparator<P> nullSafePropCmp = new NullSafeComparator<>(propertyComparator);

    comparator_ = new NullSafeComparator<>(
      (e1, e2) -> nullSafePropCmp.compare(getter.apply(e1), getter.apply(e2))
    );
  }

  @Override
  public int compare(E e1, E e2) {
    return comparator_.compare(e1, e2);
  }

}
