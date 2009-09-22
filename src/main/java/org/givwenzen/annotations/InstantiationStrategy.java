package org.givwenzen.annotations;

import java.lang.reflect.InvocationTargetException;

public interface InstantiationStrategy {
   Object instantiate(Class markedClass, Object parameter)
      throws InvocationTargetException, InstantiationException, IllegalAccessException;
}
