package com.bensler.decaf.swing.view;

import com.bensler.decaf.swing.Viewable;



public class QueueGetter extends Object implements PropertyGetter {

  private   final         PropertyGetter    firstGetter_;

  private   final         PropertyGetter    subGetter_;

  public QueueGetter(String propertyName, PropertyGetter subGetter) {
    this(new NamePropertyGetter(propertyName, EntityComparator.NOP), subGetter);
  }

  public QueueGetter(PropertyGetter getter, PropertyGetter subGetter) {
    firstGetter_ = getter;
    subGetter_ = subGetter;
  }

  public boolean isSortable() {
    return (firstGetter_.isSortable() && subGetter_.isSortable());
  }

  public int compare(Viewable o1, Viewable o2) {
    int cmpValue = firstGetter_.compare(o1, o2);

    if (cmpValue == 0) {
      cmpValue = subGetter_.compare(
        (Viewable)firstGetter_.getProperty(o1), (Viewable)firstGetter_.getProperty(o2)
      );
    }
    return cmpValue;
  }

  public Object getProperty(Viewable viewable) {
    final Viewable subViewable = (Viewable)firstGetter_.getProperty(viewable);

    return ((subViewable != null) ? subGetter_.getProperty(subViewable) : null);
  }

}
