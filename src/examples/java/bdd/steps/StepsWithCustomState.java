package bdd.steps;

import org.givwenzen.CustomState;
import org.givwenzen.annotations.DomainStep;
import org.givwenzen.annotations.DomainSteps;

@DomainSteps
public class StepsWithCustomState {
  private CustomState state;

  public StepsWithCustomState(CustomState state) {
    this.state = state;
  }
  
  @DomainStep("the custom state is (.*)")
  public boolean domainStep(String customeState) {
    return customeState.equals(state.someValue());
  }  
}
