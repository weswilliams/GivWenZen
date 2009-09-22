package org.givwenzen;


import java.io.Serializable;

import org.givwenzen.annotations.DomainSteps;

@DomainSteps
public class StepClassWithInterfaceConstructorForUnitTesting {
   Serializable test;

   public StepClassWithInterfaceConstructorForUnitTesting(Serializable test) {
      this.test = test;
   }
}
