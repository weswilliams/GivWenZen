package org.givwenzen.annotations;


public class ObjectInterfacesInstantiationStrategy implements InstantiationStrategy {
  public InstantiationState instantiate(Class<?> markedClass, Object parameter) {
     InstantiationStateCreator creator = new InstantiationStateCreator();
    try {
      for (Class<?> interfaceClass : parameter.getClass().getInterfaces()) {
        InstantiationState state = new ObjectInterfaceInstantiationStrategy(interfaceClass).instantiate(markedClass, parameter);
        if (state.couldInstantiate())
          return creator.didInstantiate(state.getInstantiation());
      }
    } catch (RuntimeException e) {
      return creator.didNotInstantiate();
    } catch (Exception e) {
      return creator.didNotInstantiate();
    }
    return creator.didNotInstantiate();
  }
}
