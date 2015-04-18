package com.bensler.decaf.swing.view;

import com.bensler.decaf.swing.EntityCache;
import com.bensler.decaf.swing.Viewable;

public class NamePropertyGetter extends Object implements PropertyGetter {

  public    final static  Object[]    NO_ARGS   = new Object[0];

  protected static Object getProperty(Viewable viewable, String propertyName) {
    final Class<?> bizClass = (viewable.getClass());

    try {
      return EntityCache.getInstance().getGetter(
        bizClass, propertyName
      ).invoke(viewable, NO_ARGS);
    } catch (Exception e) {
      throw new RuntimeException(
        "trouble accessing getter for \"" + propertyName + "\" on \"" + bizClass.getName() + "\" using reflection ", e
      );
    }
  }

  protected final         String              propertyName_;
  private   final         EntityComparator    comparator_;

  public NamePropertyGetter(String propertyName) {
    this(propertyName, new EntityComparator(new CollatorComparator()));
  }

  public NamePropertyGetter(String propertyName, EntityComparator comparator) {
    comparator_ = comparator;
    propertyName_ = propertyName;
  }

  @Override
  public Object getProperty(Viewable viewable) {
    return getProperty(viewable, propertyName_);
  }

  @Override
  public int compare(Viewable v1, Viewable v2) {
    return comparator_.compare(this, v1, v2);
  }

  @Override
  public boolean isSortable() {
    return (comparator_ != EntityComparator.NOT_SORTABLE);
  }

}
