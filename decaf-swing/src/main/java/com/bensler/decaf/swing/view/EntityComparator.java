package com.bensler.decaf.swing.view;

import java.util.Comparator;


public class EntityComparator<E, P> extends Object {

  public    final static  EntityComparator    NOT_SORTABLE  = new EntityComparator(new Nop());

  public    final static  EntityComparator    NOP           = new EntityComparator(new Nop());

  private   final         NullPolicy<? super E>       entityComparator_;

  private   final         NullPolicy<? super P>       propertyComparator_;

  public EntityComparator(Comparator<P> propertyComparator) {
    this(new Nop(), propertyComparator);
  }

  public EntityComparator(Comparator<? super E> entityComparator, Comparator<? super P> propertyComparator) {
    super();
    entityComparator_ = new NullPolicy<>(entityComparator);
    propertyComparator_ = new NullPolicy<>(propertyComparator);
  }

  public int compare(PropertyGetter<E, P> getter, E v1, E v2) {
    int cmpValue = compareEntity(v1, v2);

    if ((cmpValue == 0) && entityComparator_.continue_) {
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

  private final static class NullPolicy<T> extends Object implements Comparator<T> {

    private final Comparator<T> delegate_;

    private       boolean       continue_;

    public NullPolicy(Comparator<T> delegate) {
      super();
      delegate_ = delegate;
    }

    public int compare(T v1, T v2) {
      int cmpValue = 0;

      continue_ = true;
      if (cmpValue == 0) {
        if (v1 == null) {
          if (v2 == null) {
            cmpValue = 0;
            continue_ = false;
          } else {
            cmpValue = -1;
          }
        } else {
          if (v2 == null) {
            cmpValue = 1;
          } else {
            cmpValue = delegate_.compare(v1, v2);
          }
        }
      }
      return cmpValue;
    }

  }

  public final static class Nop extends Object implements Comparator<Object> {

    public int compare(Object v1, Object v2) {
      return 0;
    }

  }

}
