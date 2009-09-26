package org.givwenzen.annotations;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ObjectInterfaceInstantiationStrategy implements InstantiationStrategy {
  private Class<?> interfaceClass;

  ObjectInterfaceInstantiationStrategy(Class<?> interfaceClass) {
    this.interfaceClass = interfaceClass;
  }

  public InstantiationState instantiate(Class<?> markedClass, Object parameter) throws InvocationTargetException,
      InstantiationException, IllegalAccessException {
    Constructor<?> constructor;
    try {
      constructor = markedClass.getConstructor(interfaceClass);
    } catch (Exception e) {
      constructor = null;
    }
    return constructor == null ? 
        DefaultInstantiationState.UNINSTANTIATED : 
        new DefaultInstantiationState(true, constructor.newInstance(parameter));
  }
}
