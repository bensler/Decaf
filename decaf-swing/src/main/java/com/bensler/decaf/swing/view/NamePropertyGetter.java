package com.bensler.decaf.swing.view;

import java.lang.reflect.Proxy;

import com.bensler.decaf.swing.Viewable;

public class NamePropertyGetter extends Object implements PropertyGetter {

  public    final static  Object[]    NO_ARGS   = new Object[0];

  protected static Object getProperty(Viewable viewable, String propertyName) {
    if (Proxy.isProxyClass(viewable.getClass())) {
      return ((EntityVo)Proxy.getInvocationHandler(viewable)).getValue(propertyName);
    } else {
      final Class<?> bizClass = (
        (viewable instanceof Entity) ? ((Entity)viewable).getBusinessClass() : viewable.getClass()
      );

      try {
        return VoController.getInstance().getGetter(
          bizClass, propertyName
        ).invoke(viewable, NO_ARGS);
      } catch (Exception e) {
        throw new RuntimeException(
          "trouble accessing getter for \"" + propertyName + "\" on \"" + bizClass.getName() + "\" using reflection ", e
        );
      }
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

  public Object getProperty(Viewable viewable) {
    return getProperty(viewable, propertyName_);
  }

  public int compare(Viewable v1, Viewable v2) {
    return comparator_.compare(this, v1, v2);
  }

  public boolean isSortable() {
    return (comparator_ != EntityComparator.NOT_SORTABLE);
  }

}
