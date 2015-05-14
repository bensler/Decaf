package com.bensler.decaf.swing.view;

import java.util.Comparator;

import com.bensler.decaf.swing.MethodCache;

public class NamePropertyGetter<P> extends PropertyGetter<Object, P> {

  public    final static  Object[]    NO_ARGS   = new Object[0];

  @SuppressWarnings("unchecked")
  public <E> P getProperty(E viewable, String propertyName) {
    final Class<?> bizClass = (viewable.getClass());

    try {
      return (P)MethodCache.INSTANCE.getGetter(
        bizClass, propertyName
      ).invoke(viewable, NO_ARGS);
    } catch (Exception e) {
      throw new RuntimeException(
        "trouble accessing getter for \"" + propertyName + "\" on \"" + bizClass.getName() + "\" using reflection ", e
      );
    }
  }

  protected final String   propertyName_;

  public NamePropertyGetter(String propertyName, Comparator<P> comparatorDelegate) {
    super(comparatorDelegate);
    propertyName_ = propertyName;
  }

  @Override
  public P getProperty(Object viewable) {
    return getProperty(viewable, propertyName_);
  }

}
