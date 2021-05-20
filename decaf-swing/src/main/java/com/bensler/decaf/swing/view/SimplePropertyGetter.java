package com.bensler.decaf.swing.view;

import java.util.Comparator;
import java.util.function.Function;

public class SimplePropertyGetter<E, P> extends PropertyGetter<E, P> {

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
