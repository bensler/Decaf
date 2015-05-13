package com.bensler.decaf.swing.view;

import com.bensler.decaf.swing.EntityCache;

public class NamePropertyGetter extends Object implements PropertyGetter<Object, String> {

  public    final static  Object[]    NO_ARGS   = new Object[0];

  @SuppressWarnings("unchecked")
  protected static <E, P> P getProperty(E viewable, String propertyName) {
    final Class<?> bizClass = (viewable.getClass());

    try {
      return (P)EntityCache.getInstance().getGetter(
        bizClass, propertyName
      ).invoke(viewable, NO_ARGS);
    } catch (Exception e) {
      throw new RuntimeException(
        "trouble accessing getter for \"" + propertyName + "\" on \"" + bizClass.getName() + "\" using reflection ", e
      );
    }
  }

  protected final         String                        propertyName_;
  private   final         EntityComparator<Object, String>   comparator_;

  public NamePropertyGetter(String propertyName) {
    this(propertyName, new EntityComparator<Object, String>(new CollatorComparator()));
  }

  public NamePropertyGetter(String propertyName, EntityComparator<Object, String> comparator) {
    comparator_ = comparator;
    propertyName_ = propertyName;
  }

  @Override
  public String getProperty(Object viewable) {
    return getProperty(viewable, propertyName_);
  }

  @Override
  public int compare(Object v1, Object v2) {
    return comparator_.compare(this, v1, v2);
  }

  @Override
  public boolean isSortable() {
    return (comparator_ != EntityComparator.NOT_SORTABLE);
  }

}
