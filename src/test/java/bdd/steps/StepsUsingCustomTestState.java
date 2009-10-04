package bdd.steps;

import org.givwenzen.annotations.DomainStep;
import org.givwenzen.annotations.DomainSteps;

@DomainSteps
public class StepsUsingCustomTestState {
  private TestCustomState state;

  public StepsUsingCustomTestState(TestCustomState state) {
    this.state = state;
  }

  @DomainStep("set value (.*) in custom state")
  public void setStateValue(String value) {
    state.setValue(value);
  }
  
  @DomainStep("retrieve custom state")
  public String getStateValue() {
    return state.getValue();
  }
}
