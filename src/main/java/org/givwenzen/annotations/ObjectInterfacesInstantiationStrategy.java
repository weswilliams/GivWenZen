package org.givwenzen.annotations;

public class ObjectInterfacesInstantiationStrategy implements InstantiationStrategy {
   public Object instantiate(Class markedClass, Object parameter) {
      try {
         for (Class interfaceClass : parameter.getClass().getInterfaces()) {
            Object
               object =
               new ObjectInterfaceInstantiationStrategy(interfaceClass).instantiate(markedClass, parameter);
            if (object != null) return object;
         }
      } catch (Exception e) {
         return null;
      }
      return null;
   }
}
