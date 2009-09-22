package org.givwenzen;

public class StepClassWhoseOnlyConstructorIsUnrelatedToAnythingStepFinderKnowsAbout {
   private String name;

   public StepClassWhoseOnlyConstructorIsUnrelatedToAnythingStepFinderKnowsAbout(String name) {
      this.name = name;
   }

   public String getName() {
      return name;
   }
}
