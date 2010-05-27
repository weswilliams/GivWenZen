package bdd.steps;

import org.givwenzen.GivWenZenExecutor;
import org.givwenzen.GivWenZenExecutorCreator;
import org.givwenzen.annotations.DomainStep;
import org.givwenzen.annotations.DomainSteps;
import org.givwenzen.annotations.InstantiationStrategy;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

@DomainSteps
public class ComplexSteps {

   private GivWenZenExecutor gwz;
   private List<Object> calledSteps = new ArrayList<Object>();

   @DomainStep("a custom package for step classes")
   public void createGivWenZenWithCustomPackage() {
      gwz = GivWenZenExecutorCreator.instance()
         .customStepState(this)
         .stepClassBasePackage("bdd.custom.steps.")
         .create();
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

   @DomainStep("custom state objects are used")
   public void createGWZWithCustomState() {
      gwz = GivWenZenExecutorCreator.instance()
         .customStepState(new CustomState(), new CustomState2())
         .create();
   }

   @DomainStep("calling a step that sets (.*) in the (.*) custom state")
   public void callAStepAndSetFirstStateValue(String value, String whichState) throws Exception {
      gwz.when("set value " + value + " in " + whichState + " custom state");
   }

   @DomainStep("another step has access to (.*) in the (.*) custom state")
   public boolean callAStepThatRetrieveStateValue(String value, String whichState) throws Exception {
      return gwz.then("retrieve " + whichState + " custom state").equals(value);
   }

   @DomainStep("a custom instantiation strategy for creating custom step classes")
   public void createGWZWithCustomInstantiationStrategy() {
      InstantiationStrategy customStrategy = new CustomInstantiationStrategy();
      gwz = GivWenZenExecutorCreator.instance()
         .customStepState(this)
         .customInstantiationStrategies(customStrategy)
         .create();
   }

   @DomainStep("a step class that is created by the custom instantiation strategy")
   public void stepClassExistsAndCanBeInstantiatedByCustomInstantiationStrategy() throws Exception {
      assertTrue(new CustomInstantiationStrategy().instantiate(CustomInstantiationSteps.class, null).couldInstantiate());
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
