package com.bensler.decaf.swing.view;

import java.util.Comparator;


public interface PropertyGetter<E, P> extends Comparator<E> {

  P getProperty(E viewable);

  boolean isSortable();

}
