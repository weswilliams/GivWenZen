package org.givwenzen.steps.businesslogic;


import java.util.List;

import org.givwenzen.annotations.DomainStep;
import org.givwenzen.annotations.DomainSteps;

@DomainSteps
public class BusinessLogicStepDefinition {
   private List<String> forTesting;

   public BusinessLogicStepDefinition(List<String> forTesting) {
      this.forTesting = forTesting;
   }

   @DomainStep("a step can be handled either through the gui or through the business layer")
   public void somethingAtTheBusinessLogicLayer() {
      forTesting.add("business");
   }
}
