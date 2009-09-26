package org.givwenzen.annotations;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ObjectParameterInstantiationStrategy implements InstantiationStrategy {
  public InstantiationState instantiate(Class<?> markedClass, Object parameter) throws InvocationTargetException,
      InstantiationException, IllegalAccessException {
    Constructor<?> constructor;
    try {
      constructor = markedClass.getConstructor(parameter.getClass());
    } catch (Exception e) {
      constructor = null;
    }
    return constructor == null ? DefaultInstantiationState.UNINSTANTIATED : new DefaultInstantiationState(true, constructor.newInstance(parameter));
  }
}
