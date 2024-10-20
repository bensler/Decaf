package com.bensler.decaf.swing.view;

import static com.bensler.decaf.util.cmp.CollatorComparator.COLLATOR_COMPARATOR;

import java.util.Comparator;
import java.util.function.Function;

import com.bensler.decaf.util.cmp.ComparableComparator;

public class SimplePropertyGetter<E, P> extends PropertyGetter<E, P> {

  public static <E1> SimplePropertyGetter<E1, String> createStringPropertyGetter(Function<E1, String> getter) {
    return new SimplePropertyGetter<>(getter, COLLATOR_COMPARATOR);
  }

  public static <E1, P1  extends Comparable<P1>> SimplePropertyGetter<E1, P1> createComparablePropertyGetter(Function<E1, P1> getter) {
    return new SimplePropertyGetter<>(getter, new ComparableComparator<>());
  }

  protected final Function<E, P> getter_;

  public SimplePropertyGetter(Function<E, P> getter, Comparator<? super P> comparatorDelegate) {
    super(comparatorDelegate);
    getter_ = getter;
  }

  @Override
  public P getProperty(E viewable) {
    return getter_.apply(viewable);
  }

}
