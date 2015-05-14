package com.bensler.decaf.swing.view;

import java.util.Comparator;

import com.bensler.decaf.util.cmp.NopComparator;
import com.bensler.decaf.util.cmp.NullPolicy;


public class EntityComparator<E, P> extends Object {

  private   final         NullPolicy<? super E>       entityComparator_;

  private   final         NullPolicy<? super P>       propertyComparator_;

  public EntityComparator(Comparator<P> propertyComparator) {
    this(new NopComparator(), propertyComparator);
  }

  public EntityComparator(Comparator<? super E> entityComparator, Comparator<? super P> propertyComparator) {
    super();
    entityComparator_ = new NullPolicy<>(entityComparator);
    propertyComparator_ = new NullPolicy<>(propertyComparator);
  }

  public int compare(PropertyGetter<E, P> getter, E v1, E v2) {
    int cmpValue = compareEntity(v1, v2);

    if (cmpValue == 0) {
      cmpValue = compareProperty(getter.getProperty(v1), getter.getProperty(v2));
    }
    return cmpValue;
  }

  public int compareEntity(E v1, E v2) {
    return entityComparator_.compare(v1, v2);
  }

  public int compareProperty(P p1, P p2) {
    return propertyComparator_.compare(p1, p2);
  }

}
