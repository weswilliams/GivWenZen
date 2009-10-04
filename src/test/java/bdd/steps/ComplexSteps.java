package bdd.steps;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.givwenzen.*;
import org.givwenzen.annotations.*;

@DomainSteps
public class ComplexSteps {
  
  private GivWenZenExecutor gwz;
  private List<Object> calledSteps = new ArrayList<Object>();

  @DomainStep("a custom package for step classes")
  public void createGivWenZenWithCustomPackage() {
    gwz = new GivWenZenExecutor(this, new DomainStepFinder("bdd.custom.steps."));
  }
 
  @DomainStep("a step '(.*)' defined in a class in the custom package")
  public void verifyCustomStepIsFound(String customStep) throws Exception {
    gwz.given(customStep);
  }
  
  @DomainStep("the step in the custom package '(.*)' is called")
  public void callCustomStep(String customStep) throws Exception {
    calledSteps.add(gwz.when(customStep));
  }
  
  @DomainStep("the step in the custom package '(.*)' is found and executed successfully")
  public boolean stepExecutedSuccessfully(String customStep) {
    return calledSteps.contains(customStep);
  }
  
  @DomainStep("a custom state object is used")
  public void createGWZWithCustomState() {
    gwz = new GivWenZenExecutor(new TestCustomState(), new DomainStepFinder());
  } 
  
  @DomainStep("calling a step that sets (.*) in the custom state")
  public void callAStepAndSetStateValue(String value) throws Exception {
    gwz.when("set value " + value + " in custom state");
  }
  
  @DomainStep("another step has access to (.*) in the custom state")
  public boolean callAStepThatRetrieveStateValue(String value) throws Exception {
    return gwz.then("retrieve custom state").equals(value);
  }
  
  @DomainStep("a custom instantiation strategy for creating custom step classes")
  public void createGWZWithCustomInstantiationStrategy() {
    InstantiationStrategy customStrategy = new TestCustomInstantiationStrategy();
    gwz = new GivWenZenExecutor(this, new DomainStepFinder(), new DomainStepFactory(customStrategy));
  }
  
  @DomainStep("a step class that is created by the custom instantiation strategy")
  public void stepClassExistsAndCanBeInstantiatedByCustomInstantiationStrategy() throws Exception {
    assertTrue(new TestCustomInstantiationStrategy().instantiate(CustomInstantiationSteps.class, null).couldInstantiate());
  }
  
  @DomainStep("a step is called that exists in the step class created by the custom instantiation strategy")
  public void callStepInCustomInstantiationSteps() throws Exception {
    calledSteps.add(gwz.when("custom instantion step"));
  }
  
  @DomainStep("the step in the step class created by the custom instantiation strategy is executed successfully")
  public boolean verifyCustomInstantionStepWasCalled() {
    return calledSteps.contains(CustomInstantiationSteps.CUSTOM_INSTANTION_STEP);
  }
}
