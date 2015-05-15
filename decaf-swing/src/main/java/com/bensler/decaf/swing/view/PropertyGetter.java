package com.bensler.decaf.swing.view;

import java.util.Comparator;

import com.bensler.decaf.util.cmp.NullSafeComparator;



public abstract class PropertyGetter<E, P> {

  private final Comparator<E> entityDelegate_;
  private final Comparator<P> propertyDelegate_;

  public PropertyGetter(Comparator<? super P> propertyComparator) {
    super();
    propertyDelegate_ = new NullSafeComparator<>(propertyComparator);
    entityDelegate_ = new NullSafeComparator<>(new Comparator<E>() {
      @Override
      public int compare(E e1, E e2) {
        return propertyDelegate_.compare(getProperty(e1), getProperty(e2));
      }
    });
  }

  public abstract P getProperty(E viewable);

  public Comparator<E> getEntityComparator() {
    return entityDelegate_;
  }

  public Comparator<P> getPropertyComparator() {
    return propertyDelegate_;
  }

}
