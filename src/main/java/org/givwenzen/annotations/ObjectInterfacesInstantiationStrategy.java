package org.givwenzen.annotations;


public class ObjectInterfacesInstantiationStrategy implements InstantiationStrategy {
  public InstantiationState instantiate(Class<?> markedClass, Object parameter) {
    try {
      for (Class<?> interfaceClass : parameter.getClass().getInterfaces()) {
        InstantiationState state = new ObjectInterfaceInstantiationStrategy(interfaceClass).instantiate(markedClass, parameter);
        if (state.couldInstantiate())
          return new DefaultInstantiationState(true, state.getInstantiation());
      }
    } catch (RuntimeException e) {
      return DefaultInstantiationState.UNINSTANTIATED;
    } catch (Exception e) {
      return DefaultInstantiationState.UNINSTANTIATED;
    }
    return DefaultInstantiationState.UNINSTANTIATED;
  }
}
