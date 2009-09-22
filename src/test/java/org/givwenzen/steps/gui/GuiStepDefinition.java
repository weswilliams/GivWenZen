package org.givwenzen.steps.gui;


import java.util.List;

import org.givwenzen.annotations.DomainStep;
import org.givwenzen.annotations.DomainSteps;

@DomainSteps
public class GuiStepDefinition {
   private List<String> forTesting;

   public GuiStepDefinition(List<String> forTesting) {
      this.forTesting = forTesting;
   }

   @DomainStep("a step can be handled either through the gui or through the business layer")
   public void somethingAtTheGuiLayer() {
      forTesting.add("gui");
   }
}
