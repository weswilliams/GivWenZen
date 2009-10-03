package org.givwenzen;

import org.givwenzen.GivWenZen;
import org.givwenzen.annotations.DomainSteps;


@DomainSteps
public class StepsClassWithGivWenZenConstructor {
   private GivWenZen givWenZen;

   public StepsClassWithGivWenZenConstructor(GivWenZen givWenZen) {
      this.givWenZen = givWenZen;
   }

   public GivWenZen getDomainStepNames() {
      return givWenZen;
   }
}