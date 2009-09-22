package org.givwenzen;

import org.givwenzen.GivWenZen;
import org.givwenzen.annotations.DomainSteps;


@DomainSteps
public class StepsClassWithDomainStepNamesConstructor {
   private GivWenZen givWenZen;

   public StepsClassWithDomainStepNamesConstructor(GivWenZen givWenZen) {
      this.givWenZen = givWenZen;
   }

   public GivWenZen getDomainStepNames() {
      return givWenZen;
   }
}