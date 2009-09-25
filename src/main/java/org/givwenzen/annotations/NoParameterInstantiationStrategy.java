package org.givwenzen.annotations;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class NoParameterInstantiationStrategy implements InstantiationStrategy {
  public Object instantiate(Class<?> markedClass, Object parameter) throws InvocationTargetException,
      InstantiationException, IllegalAccessException {
    Constructor<?> constructor;
    try {
      constructor = markedClass.getConstructor();
    } catch (NoSuchMethodException e) {
      constructor = null;
    }
    return constructor == null ? null : constructor.newInstance();
  }
}
