package bdd.steps;

import org.givwenzen.GivWenZen;
import org.givwenzen.annotations.DomainStep;
import org.givwenzen.annotations.DomainSteps;

@DomainSteps
public class CallingStepsFromStep {

  private GivWenZen gwz;

  public CallingStepsFromStep(GivWenZen gwz) {
    this.gwz = gwz;
  }

  @DomainStep("i call the steps to add (\\d+) and (\\d+)")
  public void callTheAddStepTwoTimes(int int1, int int2) throws Exception {
    gwz.given("i have entered " + int1 + " into the calculator");
    gwz.given("i have entered " + int2 + " into the calculator");
    gwz.when("i press add");
  }
}
