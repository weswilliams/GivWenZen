package org.givwenzen;

import org.givwenzen.annotations.DomainSteps;

@DomainSteps
public class StepsClassWithObjectConstructorForUnitTesting {
   DomainStepFactoryTest test;

   public StepsClassWithObjectConstructorForUnitTesting(DomainStepFactoryTest test) {
      this.test = test;
   }
}
