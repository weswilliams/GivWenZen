package org.givwenzen;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.givwenzen.GWZForJUnit.*;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.givwenzen.annotations.DomainStep;
import org.givwenzen.annotations.DomainSteps;
import org.junit.Before;
import org.junit.Test;

@DomainSteps
public class DomainStepMethodLocatorTest {
  private DomainStepMethodLocator locator;
  private GivWenZenException retrieveMethodsError;

  @Before
  public void setup() {
    setUp(this);
  }
  
  @Test
  public void testGetMethodWithAnnotatedPatternMatching() throws Exception {
    given("a class exists with two methods that have duplicate DomainStep annotations");
    when("the annotated method is retrieved for 'duplicate'");
    then("a GivWenZenException is thrown");
  }

  @Test
  public void testWhenAnInstanceOfTheSameClassIsInTheListOfAnnotatatedObjectsMoreThanOnceThenTheExactDuplicatesAreIgnored() throws Exception {
    given("a method locator is created with a list containing multiple instances of the same class");
    when("the annotated method is retrieved for 'dummy step'");
    then("duplicates for the exact same class are ignored and no duplicate exception is thrown");
  }
  
  @DomainStep("a method locator is created with a list containing multiple instances of the same class")
  public void createStepLocatorWithMulitpleInstancesOfAStepClass() {
    List<Object> stepDefinitions = new ArrayList<Object>();
    stepDefinitions.add(new DomainStepMethodLocatorTest());
    stepDefinitions.add(new DomainStepMethodLocatorTest());
    createStepLocator(stepDefinitions);    
  }
  
  @DomainStep("a class exists with two methods that have duplicate DomainStep annotations")
  public void createStepLocatorWithClassContatiningDuplicateAnnotations() {
    List<Object> stepDefinitions = new ArrayList<Object>();
    stepDefinitions.add(new StepsWithDuplicateAnnotatedMethod());
    createStepLocator(stepDefinitions);    
  }
  
  @DomainStep("the annotated method is retrieved for (.*)")
  public void retrieveAnnotatedMethodAndSaveErrorIfAny(String annotationString) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    try {
      System.out.println("retrieve methods");
      locator.getMethodWithAnnotatedPatternMatching(annotationString);
      retrieveMethodsError = null;
    } catch (GivWenZenException e) {
      retrieveMethodsError = e;
    }    
  }

  @DomainStep("dummy step")
  public void dummy() {
    
  }
  
  @DomainStep("duplicates for the exact same class are ignored and no duplicate exception is thrown")
  public void verifyNoErrorOccuredRetrievingAnnotatedMethods() {
    assertNull(retrieveMethodsError);
  }
  
  @DomainStep("a GivWenZenException is thrown")
  public void verifyGivWenZenErrorWasThrown() {
    assertNotNull(retrieveMethodsError);
  }
  
  private void createStepLocator(List<Object> stepDefinitions) {
    locator = new DomainStepMethodLocator(stepDefinitions);
  }
  
  @DomainSteps
  static class StepsWithDuplicateAnnotatedMethod {

    @DomainStep("duplicate")
    public void method1() {
    }

    @DomainStep("duplicate")
    public void method2() {
    }
  }
}