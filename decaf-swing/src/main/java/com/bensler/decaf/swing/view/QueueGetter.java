package com.bensler.decaf.swing.view;




/**
 *
 */
public class QueueGetter<E, I, P> extends PropertyGetter<E, P> {

  private   final         PropertyGetter<? super E, I>    firstGetter_;

  private   final         PropertyGetter<? super I, P>    subGetter_;

  public QueueGetter(PropertyGetter<? super E, I> getter, PropertyGetter<? super I, P> subGetter) {
    super(subGetter.getPropertyComparator());
    firstGetter_ = getter;
    subGetter_ = subGetter;
  }

  @Override
  public P getProperty(E viewable) {
    final I subViewable = firstGetter_.getProperty(viewable);

    return ((subViewable != null) ? subGetter_.getProperty(subViewable) : null);
  }

}
