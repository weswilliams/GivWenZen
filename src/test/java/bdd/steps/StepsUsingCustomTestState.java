package bdd.steps;

import org.givwenzen.annotations.DomainStep;
import org.givwenzen.annotations.DomainSteps;

@DomainSteps
public class StepsUsingCustomTestState {
  private CustomState state;

  public StepsUsingCustomTestState(CustomState state) {
    this.state = state;
  }

  @DomainStep("set value (.*) in first custom state")
  public void setStateValue(String value) {
    state.setValue(value);
  }
  
  @DomainStep("retrieve first custom state")
  public String getStateValue() {
    return state.getValue();
  }
}
