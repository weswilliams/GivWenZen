package bdd.steps;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import org.givwenzen.annotations.*;

public class TestCustomInstantiationStrategy implements InstantiationStrategy {

  public InstantiationState instantiate(Class<?> markedClass, Object parameter) throws InvocationTargetException,
      InstantiationException, IllegalAccessException {
    if (markedClass.equals(CustomInstantiationSteps.class)) {
      return new DefaultInstantiationState(true, new CustomInstantiationSteps(new Date()));
    }
    return DefaultInstantiationState.UNINSTANTIATED;
  }

}
