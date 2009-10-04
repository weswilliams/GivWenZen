package bdd.steps;

import java.util.HashMap;
import java.util.Map;

import org.givwenzen.GivWenZen;
import org.givwenzen.annotations.DomainStep;
import org.givwenzen.annotations.DomainSteps;

@DomainSteps
public class SimpleTestSteps {

  private GivWenZen gwz;
  private Map<String, Boolean> stepCalls = new HashMap<String, Boolean>();
  private Object paramValue;
  
  public SimpleTestSteps(GivWenZen gwz) {
    this.gwz = gwz;
  }

  @DomainStep("a step annotated with '(.*)' (:?.*)")
  public void verifyStepExists(String step) throws Exception {
    System.out.println("step annotated " + step);
    gwz.given(step);
  }
 
  @DomainStep("the step '(.*)' is called")
  public void callStep(String stepToCall) throws Exception {
    System.out.println("step called " + stepToCall);
    gwz.when(stepToCall);
  }
  
  @DomainStep("the step '(.*)' executes successfully")
  public boolean stepExecutedSuccessfully(String executedStep) {
    return stepCalls.get(executedStep).equals(true);
  }
  
  @DomainStep("simple no parameter step")
  public void simpleNoParamStep() {
    System.out.println("no param step");
    stepCalls.put("simple no parameter step", true);
  }
  
  @DomainStep("simple step with int parameter (\\d+)")
  public void simpleIntParamTest(int intParam) {
    System.out.println("int step " + intParam);
    stepCalls.put("simple step with int parameter " + intParam, true);
    paramValue = intParam;
  }
  
  @DomainStep("the value (\\d+) is passed as a parameter")
  public boolean verifyParamValue(int paramValue) {
    return this.paramValue.equals(paramValue);
  }
}
