package com.bensler.decaf.swing.view;

import java.util.Comparator;
import java.util.Optional;

import com.bensler.decaf.util.cmp.NullSafeComparator;



public abstract class PropertyGetter<E, P> {

  private final Comparator<E> entityDelegate_;
  private final Comparator<P> propertyDelegate_;

  public PropertyGetter(Comparator<? super P> propertyComparator) {
    propertyDelegate_ = new NullSafeComparator<>(propertyComparator);
    entityDelegate_ = new NullSafeComparator<>(
      (e1, e2) -> propertyDelegate_.compare(getProperty(e1), getProperty(e2))
    );
  }

  public abstract P getProperty(E viewable);

  public Comparator<E> getEntityComparator() {
    return entityDelegate_;
  }

  public Comparator<P> getPropertyComparator() {
    return propertyDelegate_;
  }

  public String getPropertyString(E entity) {
    return Optional.of(getProperty(entity)).map(P::toString).orElse("");
  }

}
