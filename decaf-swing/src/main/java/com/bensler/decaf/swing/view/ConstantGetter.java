package com.bensler.decaf.swing.view;

import java.util.Comparator;

public class ConstantGetter<P> extends PropertyGetter<Object, P> {

  private   final P constant_;

  public ConstantGetter(P constant, Comparator<P> comparator) {
    super(comparator);
    constant_ = constant;
  }

  @Override
  public P getProperty(Object viewable) {
    return constant_;
  }

}