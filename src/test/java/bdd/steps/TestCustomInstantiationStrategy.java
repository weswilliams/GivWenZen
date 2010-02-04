package bdd.steps;

import org.givwenzen.annotations.InstantiationState;
import org.givwenzen.annotations.InstantiationStateCreator;
import org.givwenzen.annotations.InstantiationStrategy;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;

public class TestCustomInstantiationStrategy implements InstantiationStrategy {

   public InstantiationState instantiate(Class<?> markedClass, Object parameter) throws InvocationTargetException,
      InstantiationException, IllegalAccessException {
      InstantiationStateCreator creator = new InstantiationStateCreator();
      if (markedClass.equals(CustomInstantiationSteps.class)) {
         return creator.didInstantiate(new CustomInstantiationSteps(new Date()));
      }
      return creator.didNotInstantiate();
   }

}
