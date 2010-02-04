package org.givwenzen.annotations;

public class InstantiationStateCreator {
   public InstantiationState didNotInstantiate() {
      return DefaultInstantiationState.UNINSTANTIATED;
   }

   public InstantiationState didInstantiate(Object instantiatedObject) {
      return new DefaultInstantiationState(true, instantiatedObject);
   }
}
