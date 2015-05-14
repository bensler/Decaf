package com.bensler.decaf.swing.view;

import java.util.Comparator;

import com.bensler.decaf.util.cmp.NopComparator;
import com.bensler.decaf.util.cmp.NullPolicy;

public class EntityComparator<E, P> extends Object {

  private   final         NullPolicy<? super P>       propertyComparator_;

  public EntityComparator(Comparator<P> propertyComparator) {
    super();
    propertyComparator_ = new NullPolicy<>(propertyComparator);
  }

  public int compare(PropertyGetter<E, P> getter, E v1, E v2) {
    int cmpValue = new NopComparator().compare(v1, v2);

    if (cmpValue == 0) {
      cmpValue = propertyComparator_.compare(getter.getProperty(v1), getter.getProperty(v2));
    }
    return cmpValue;
  }

}
