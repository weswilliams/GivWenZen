package bdd.steps;

import org.givwenzen.annotations.*;

@DomainSteps
public class StepsUsingCustomTestState2 {
   private CustomState2 state;

   public StepsUsingCustomTestState2(CustomState2 state) {
     this.state = state;
   }

   @DomainStep("set value (.*) in second custom state")
   public void setStateValue(String value) {
     state.setValue(value);
   }

   @DomainStep("retrieve second custom state")
   public String getStateValue() {
     return state.getValue();
   }
}
