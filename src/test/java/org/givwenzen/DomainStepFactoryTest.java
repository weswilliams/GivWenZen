package org.givwenzen;

import static org.givwenzen.GWZForJUnit.*;
import static org.junit.Assert.*;

import java.beans.PropertyEditorManager;
import java.beans.PropertyEditorSupport;
import java.io.Serializable;
import java.util.*;

import org.givwenzen.annotations.*;
import org.junit.Before;
import org.junit.Test;

@DomainSteps
public class DomainStepFactoryTest {
  private DomainStepFactory domainStepFactory;
  private Class<?> domainStepClass;
  private List<?> stepDefinitions;
  private MarkedClass markedClass;

  @Before
  public void setUp() throws Exception {
    GWZForJUnit.setUp(this);
    PropertyEditorManager.registerEditor(Class.class, DomainStepFactoryTest.ClassPropertyEditor.class);
  }

  @Test
  public void testFindStepsWithNoParameterConstructor() throws Exception {
    String className = StepsClassForUnitTest.class.getName();
    String constructorObjectClassName = DomainStepFactoryTest.class.getName();

    given ( "a step class " + className + " with only default construction" );
    when  ( "step definitions are created with constructor object " + constructorObjectClassName );
    then  ( "an instance of the class " + className + " is created" );
  }
  
  @Test
  public void testFindStepsWithObjectParameterConstructor() throws Exception {
    String className = StepsClassWithObjectConstructorForUnitTesting.class.getName();
    String constructorObjectClassName = DomainStepFactoryTest.class.getName();

    given ( "a step class " + className + " with constructor that takes the test as a parameter" );
    when  ( "step definitions are created with constructor object " + constructorObjectClassName);
    then  ( "an instance of the class " + className + " is created");
  }

  @Test
  public void testFindStepsWithInterfaceParameterConstructor() throws Exception {
    String stepClassName = StepClassWithInterfaceConstructorForUnitTesting.class.getName();
    String constructorObjectClassName = SerializableClassForConstructorTest.class.getName();
    
    given ( "a step class " + stepClassName + " with constructor that takes a Serializable as a parameter" );
    when  ( "step definitions are created with constructor object " + constructorObjectClassName);
    then  ( "an instance of the class " + stepClassName + " is created" );
  }

  @Test
  public void testFindStepsWithGivWenZenParameterConstructor() throws Exception {
    String stepClassName = StepsClassWithGivWenZenConstructor.class.getName();
    String constructorObjectClassName = GivWenZenDummy.class.getName();
    
    given ( "a step class " + stepClassName + " with constructor that takes a GivWenZen interface as a parameter" );
    when  ( "step definitions are created with constructor object " + constructorObjectClassName);
    then  ( "an instance of the class " + stepClassName + " is created" );
  }

  @Test
  public void testWhatHappensWhenOnlyStepClassIsUnusable() throws Exception {
    String stepClassName = StepClassWhoseOnlyConstructorIsUnrelatedToAnythingStepFinderKnowsAbout.class.getName();
    String constructorObjectClassName = DomainStepFactoryTest.class.getName();
    String dummyMarkedClassName = MarkedClass.DummyMarkedClass.class.getName();
    
    given ( "a step class " + stepClassName + " with constructor that the domain step finder cannot handle" );
    when  ( "step definitions are created with constructor object " + constructorObjectClassName);
    then  ( "an instance of the class " + dummyMarkedClassName + " is created" );
  }

  @Test
  public void testCreateDoesNotScreamWhenClassThrowsAnException() throws Exception {
    String stepClassName = TestMarkedClass.class.getName();
    String dummyMarkedClassName = MarkedClass.DummyMarkedClass.class.getName();
    
    given ("a marked class " + stepClassName + " that the constructor throws an exception");
    when  ("the marked class is created");
    then  ( "an instance of the class " + dummyMarkedClassName + " is created" );
  }

  @Test
  public void testCreateDoesNotScreamWhenClassDoesNotHaveMatchConstructor() throws Exception {
    String stepClassName = TestMarkedClassWithInvalidConstructor.class.getName();
    String dummyMarkedClassName = MarkedClass.DummyMarkedClass.class.getName();
    
    given ("a marked class " + stepClassName + " that the constructor cannot be handled");
    when  ("the marked class is created");
    then  ( "an instance of the class " + dummyMarkedClassName + " is created" );
  }

  @DomainStep("a marked class (.*) that the constructor (?:.*)")
  public void createMarkedClass(Class<?> clazz) {
    markedClass = new MarkedClass(clazz);
  }
  
  @DomainStep("the marked class is created")
  public void callCreate() {
    stepDefinitions = Collections.singletonList(getDSFactory().create(markedClass, this));
  }
  
  @DomainStep("a step class (.*) with (?:.*)")
  public void stepClassForDomainStepFactory(Class<?> clazz) {
    this.domainStepClass = clazz;
  }
  
  @DomainStep("step definitions are created with constructor object (.*)")
  public void createStepDefinitions(Class<?> constructorObjectClass) throws InstantiationException, IllegalAccessException {
    Set<MarkedClass> classes = createMarkedClasses(domainStepClass);
    Object[] adapters = new Object[] { createObject(constructorObjectClass) };
    stepDefinitions = getDSFactory().createStepDefinitions(classes, adapters);
  }

  @DomainStep("an instance of the class (.*) is created")
  public void verifyStepClassIsCreated(Class<?> aClass) {
    assertEquals(1, stepDefinitions.size());
    assertTrue(aClass.isInstance(stepDefinitions.get(0)));
    assertNotNull(stepDefinitions.get(0));
  }
  
  private Object createObject(Class<?> constructorObjectClass) {
    try {
      return constructorObjectClass.newInstance();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  private Set<MarkedClass> createMarkedClasses(Class<?> aClass) {
    Class<?>[] markedClasses = new Class[] { aClass };
    return getMarkedClasses(markedClasses);
  }

  private Set<MarkedClass> getMarkedClasses(Class<?>... markedClasses) {
    Set<MarkedClass> stepsClasses = new HashSet<MarkedClass>();
    for (Class<?> markedClass : markedClasses) {
      stepsClasses.add(new MarkedClass(markedClass));
    }
    return stepsClasses;
  }

   private DomainStepFactory getDSFactory() {
      if (domainStepFactory == null) domainStepFactory = new DomainStepFactory();
      return domainStepFactory;
   }

  public class TestMarkedClass {
    public TestMarkedClass() {
      throw new RuntimeException();
    }
  }

  class TestMarkedClassWithInvalidConstructor {
    TestMarkedClass forTheConstructor;

    TestMarkedClassWithInvalidConstructor(TestMarkedClass forTheConstructor) {
      this.forTheConstructor = forTheConstructor;
    }
  }

  public static class ClassPropertyEditor extends PropertyEditorSupport {
    @Override
    public void setAsText(String methodName) throws IllegalArgumentException {
      try {
        setValue(Class.forName(methodName));
      } catch (ClassNotFoundException e) {
        throw new IllegalStateException(e);
      }
    }
  }
  
  @DomainSteps
  public class StepsClassForUnitTest {
  }

  public static class SerializableClassForConstructorTest implements Serializable {
    private static final long serialVersionUID = -8578525041848530267L;
  }
  
  public static class GivWenZenDummy implements GivWenZen {
    public Object and(String methodString) throws Exception {
      return null;
    }

    public Object given(String methodString) throws Exception {
      return null;
    }

    public Object then(String methodString) throws Exception {
      return null;
    }

    public Object when(String methodString) throws Exception {
      return null;
    }
  }
}