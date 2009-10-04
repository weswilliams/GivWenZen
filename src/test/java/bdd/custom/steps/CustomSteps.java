package bdd.custom.steps;

import org.givwenzen.annotations.DomainStep;
import org.givwenzen.annotations.DomainSteps;

@DomainSteps
public class CustomSteps {

  @DomainStep("in custom package")
  public String inCustomPackage() {
    return "in custom package";
  }
}
