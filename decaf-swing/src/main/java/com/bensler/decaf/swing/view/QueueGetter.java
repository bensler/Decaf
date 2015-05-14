package com.bensler.decaf.swing.view;



/**
 *
 */
public class QueueGetter<E, I, P> extends Object implements PropertyGetter<E, P> {

  private   final         PropertyGetter<E, I>    firstGetter_;

  private   final         PropertyGetter<I, P>    subGetter_;

//  public QueueGetter(String propertyName, PropertyGetter subGetter) {
//    this(new NamePropertyGetter(propertyName, EntityComparator.NOP), subGetter);
//  }

  public QueueGetter(PropertyGetter<E, I> getter, PropertyGetter<I, P> subGetter) {
    firstGetter_ = getter;
    subGetter_ = subGetter;
  }

  @Override
  public int compare(E o1, E o2) {
    int cmpValue = firstGetter_.compare(o1, o2);

    if (cmpValue == 0) {
      cmpValue = subGetter_.compare(
        firstGetter_.getProperty(o1), firstGetter_.getProperty(o2)
      );
    }
    return cmpValue;
  }

  @Override
  public P getProperty(E viewable) {
    final I subViewable = firstGetter_.getProperty(viewable);

    return ((subViewable != null) ? subGetter_.getProperty(subViewable) : null);
  }

}
