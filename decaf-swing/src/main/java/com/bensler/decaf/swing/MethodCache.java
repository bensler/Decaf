package com.bensler.decaf.swing;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableList;

/**
 * Caches getter {@link Method}s of property names for fast lookup.
 */
public class MethodCache extends Object {

  public    final static  MethodCache                       INSTANCE = new MethodCache();

  public    final static  List<String>                      GETTER_PREFIXES  = ImmutableList.of("get", "is", "has");

  public    final static  Object[]                          NO_ARGS   = new Object[0];

  private   final         Map<Class<?>, List<Method>>       getters_;

  private   final         Map<Class<?>, Map<String, Method>> properties_;

  public MethodCache() {
    super();
    getters_ = new HashMap<Class<?>, List<Method>>();
    properties_ = new HashMap<Class<?>, Map<String, Method>>();
  }

  private List<Method> getGetters(Class<?> bizClass) {
    if (!getters_.containsKey(bizClass)) {
      getters_.put(bizClass, findGetters(bizClass));
    }
    return getters_.get(bizClass);
  }

  public List<String> getProperties(Class<?> bizClass) {
    return new ArrayList<String>(getProperties_(bizClass).keySet());
  }

  private Map<String, Method> getProperties_(Class<?> bizClass) {
    if (!properties_.containsKey(bizClass)) {
      properties_.put(bizClass, findProperties(bizClass));
    }
    return properties_.get(bizClass);
  }

  private List<Method> findGetters(Class<?> bizClass) {
    final List<Method>  methods     = new ArrayList<Method>();
    final List<Method>  allMethods  = new ArrayList<Method>(Arrays.asList(bizClass.getMethods()));

    allMethods.removeAll(Arrays.asList(Object.class.getMethods()));
    for (Method method : allMethods) {
      if (method.getParameterTypes().length < 1) {
        methods.add(method);
      }
    }
    Collections.sort(methods, new MethodSorter());
    return methods;
  }

  private Map<String, Method> findProperties(Class<?> bizClass) {
    final List<Method>          methods     = getGetters(bizClass);
    final Map<String, Method>   properties  = new LinkedHashMap<String, Method>(3);

    for (Method method : methods) {
      final String name = method.getName().toLowerCase();

      for (String prefix : GETTER_PREFIXES) {
        if (name.startsWith(prefix)) {
          properties.put(
            name.substring(prefix.length(), name.length()), method
          );
        }
      }
    }
    return properties;
  }

  public int getMethodIndex(Class<?> bizClass, Method method) {
    return getGetters(bizClass).indexOf(method);
  }

  public boolean hasBizMethod(Class<?> bizClass, Method method) {
    return getGetters(bizClass).contains(method);
  }

  public int getPropertyIndex(Class<?> bizClass, String propertyName) {
    final Map<String, Method> properties  = getProperties_(bizClass);
          int                 index       = 0;

    for (String name : properties.keySet()) {
      if (name.equals(propertyName.toLowerCase())) {
        return index;
      }
      index++;
    }
    return -1;
  }

  /** Ensures a reproducable method order. Methods are used as index in EntityData.  */
  private final static class MethodSorter extends Object implements Comparator<Method> {

    @Override
    public int compare(Method m1, Method m2) {
      int returnValue = m1.getClass().getName().compareTo(m2.getClass().getName());

      if (returnValue == 0) {
        returnValue = m1.getName().compareTo(m2.getName());
      }
      return returnValue;
    }

  }

  public Method getGetter(Class<?> bizClass, String propertyName) {
    return getProperties_(bizClass).get(propertyName.toLowerCase());
  }

}
