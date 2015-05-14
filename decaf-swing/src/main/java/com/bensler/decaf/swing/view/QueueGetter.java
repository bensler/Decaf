package com.bensler.decaf.swing.view;

import java.util.Comparator;



/**
 *
 */
public class QueueGetter<E, I, P> extends PropertyGetter<E, P> {

  private   final         PropertyGetter<? super E, I>    firstGetter_;

  private   final         PropertyGetter<? super I, P>    subGetter_;

//  public QueueGetter(String propertyName, PropertyGetter subGetter) {
//    this(new NamePropertyGetter(propertyName, EntityComparator.NOP), subGetter);
//  }

  public QueueGetter(PropertyGetter<? super E, I> getter, PropertyGetter<? super I, P> subGetter, Comparator<P> comparator) {
    super(comparator);
    firstGetter_ = getter;
    subGetter_ = subGetter;
  }

  @Override
  public P getProperty(E viewable) {
    final I subViewable = firstGetter_.getProperty(viewable);

    return ((subViewable != null) ? subGetter_.getProperty(subViewable) : null);
  }

}
