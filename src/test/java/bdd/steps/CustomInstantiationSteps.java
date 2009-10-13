package bdd.steps;

import java.util.Date;

import org.givwenzen.annotations.DomainStep;
import org.givwenzen.annotations.DomainSteps;

@DomainSteps
public class CustomInstantiationSteps {
  public static final String CUSTOM_INSTANTION_STEP = "custom instantion step";

  public CustomInstantiationSteps(Date creationDate) {
  }
  
  @DomainStep(CUSTOM_INSTANTION_STEP)
  public String customInstantionStep() {
    return CUSTOM_INSTANTION_STEP;
  }
}
