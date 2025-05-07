package com.bensler.decaf.swing.view;

import java.util.Comparator;

public interface PropertyGetter<E, P> extends Comparator<E> {

  public abstract P getProperty(E viewable);

}
