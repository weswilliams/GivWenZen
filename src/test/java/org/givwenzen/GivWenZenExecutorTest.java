package org.givwenzen;

import junit.framework.TestCase;

import java.beans.PropertyEditorSupport;
import java.lang.reflect.Method;
import java.util.ArrayList;

import org.givwenzen.GivWenZenException;
import org.givwenzen.GivWenZenExecutor;
import org.givwenzen.DomainStepFinder;
import org.givwenzen.GivWenZen;
import org.givwenzen.DomainStepNotFoundException;
import org.givwenzen.InvalidDomainStepParameterException;
import org.givwenzen.annotations.DomainStep;
import org.givwenzen.annotations.DomainSteps;

public class GivWenZenExecutorTest extends TestCase implements GivWenZen {
   public static final
   String
      METHOD_WITHOUT_PARAMETERS_AND_WITH_RETURN_VALUE =
      "method without parameters and with return value";
   public static final
   String
      METHOD_WITHOUT_PARAMETERS_AND_WITHOUT_RETURN_VALUE =
      "method without parameters and without return value";
   public static final
   String
      METHOD_WITH_INTEGER_PARAMETER_AND_WITH_RETURN_VALUE =
      "method with int of 1 parameter and with return value";
   public static final
   String
      METHOD_WITH_MYSIMPLECLASS_PARAMETER_AND_WITH_RETURN_VALUE =
      "method with MySimpleCass parameter and with return value";
   public static final
   String
      METHOD_WITH_MY_SIMPLE_CLASS3_PARAMETER_USING_OBJECT_PARSE_AND_WITH_RETURN_VALUE =
      "method with MySimpleCass3 parameter using object parse and with return value";

   private GivWenZenExecutor executor;
   boolean methodWithOutParametersAndWithoutReturnValueCalled;
   int intParamValue;
   private static final String GIVEN = "given";
   private static final String WHEN = "when";
   private static final String THEN = "then";
   private static final String AND = "and";
   private static final String[] bddMethods = new String[4];
   MySimpleClass mySimpleClass;
   MySimpleClass3 mySimpleClass3;

   static {
      bddMethods[0] = GIVEN;
      bddMethods[1] = WHEN;
      bddMethods[2] = THEN;
      bddMethods[3] = AND;
   }

   protected void setUp() throws Exception {
      super.setUp();
      executor = new GivWenZenExecutor(this, new DomainStepFinder("org.givwenzen."));
   }

   public void testFindAppropriateStepDefinitionWhenDuplicationIsNeeded() throws Exception {
      assertCorrectStepDefinitionIsUsed("org.givwenzen.steps.gui.", "gui");
      assertCorrectStepDefinitionIsUsed("org.givwenzen.steps.businesslogic.", "business");
   }

   public void testDuplicateStepDefinitionCauseException() throws Exception {
      ArrayList<String> list = new ArrayList<String>();
      executor = new GivWenZenExecutor(list, new DomainStepFinder("steps.gui.,steps.businesslogic."));
      try {
         executor.given("a step can be handled either through the gui or through the business layer");
         fail("should throw exception");
      } catch (GivWenZenException e) {
      }

   }

   private void assertCorrectStepDefinitionIsUsed(String basePackageForSteps, String expected) throws Exception {
      ArrayList<String> list = new ArrayList<String>();
      executor = new GivWenZenExecutor(list, new DomainStepFinder(basePackageForSteps));
      executor.given("a step can be handled either through the gui or through the business layer");
      assertEquals(1, list.size());
      assertEquals(expected, list.get(0));
   }

   public void testShouldScreamWhenAnnotationContainsInvalidParameters()
      throws Exception {
      try {
         executor.given("method with int of " + Integer.MAX_VALUE
                        + "0 parameter and with return value");
         fail("Should scream when annotaion contains invalid parameters");
      } catch (InvalidDomainStepParameterException e) {
      }
   }

   public void testInvalidStepParameterExceptionShouldDisplayExpectedParameters()
      throws Exception {
      try {
         executor.given("method with int of " + Integer.MAX_VALUE
                        + "0 parameter and with return value");
      } catch (InvalidDomainStepParameterException e) {

      }
   }

   public void testShouldScreamWhenAnnotatedMethodIsNotFound()
      throws Exception {
      String method = "method with int of " + Integer.MAX_VALUE
                      + "0 parameter and with return value";
      try {
         executor.given(method);
         fail("Should Scream When Annotated Method Is Not Found");
      } catch (InvalidDomainStepParameterException e) {
         assertEquals(
            "Invalid step parameters in method pattern: " + method, e
               .getMessage().split("\\n")[1]);
         assertEquals(
            "  found matching method annotated with: method with int of (\\d+) parameter and with return value",
            e.getMessage().split("\\n")[2]);
         assertEquals(
            "  method signature is: public java.lang.String org.givwenzen.GivWenZenExecutorTest$FakeSteps2.methodWithIntParameterAndWithReturnValue(int)",
            e.getMessage().split("\\n")[3]);
      }
   }

   public void testStepNotFoundExceptionShouldHaveAClearStackTrace()
      throws Exception {
      try {
         executor.given("not found");
      } catch (DomainStepNotFoundException e) {
         assertEquals(0, e.getStackTrace().length);
      }
   }

   public void testStepNotFoundExceptionShouldGiveAnExampleMethodSignature()
      throws Exception {
      try {
         executor.given("not found");
      } catch (DomainStepNotFoundException e) {
         assertTrue(e.getMessage().contains("@DomainStep(\"not found\")"));
      } catch (Exception e) {
         System.out.println("error " + e);
      }
   }

   public void testMethodWithOutParametersAndWithReturnValueIsCalled()
      throws Exception {
      for (String bddMethod : bddMethods) {
         assertEquals(METHOD_WITHOUT_PARAMETERS_AND_WITH_RETURN_VALUE,
                      executeStringMethodOnAdapter(
                         METHOD_WITHOUT_PARAMETERS_AND_WITH_RETURN_VALUE,
                         bddMethod));
      }
   }

   public void testMethodWithOutParametersAndWithoutReturnValueIsCalled()
      throws Exception {
      for (String bddMethod : bddMethods) {
         methodWithOutParametersAndWithoutReturnValueCalled = false;
         assertEquals(this, executeStringMethodOnAdapter(
            METHOD_WITHOUT_PARAMETERS_AND_WITHOUT_RETURN_VALUE,
            bddMethod));
         assertTrue(methodWithOutParametersAndWithoutReturnValueCalled);
      }
   }

   public void testMethodWithIntegerParameterAndReturnValueIsCalled()
      throws Exception {
      for (String bddMethod : bddMethods) {
         intParamValue = 0;
         assertEquals(
            METHOD_WITH_INTEGER_PARAMETER_AND_WITH_RETURN_VALUE,
            executeStringMethodOnAdapter(
               METHOD_WITH_INTEGER_PARAMETER_AND_WITH_RETURN_VALUE,
               bddMethod));
         assertEquals(1, intParamValue);
      }
   }

   public void testMethodWithMySimpleClassParameterAndReturnValueIsCalled()
      throws Exception {
      for (String bddMethod : bddMethods) {
         mySimpleClass = null;
         assertEquals(
            METHOD_WITH_MYSIMPLECLASS_PARAMETER_AND_WITH_RETURN_VALUE,
            executeStringMethodOnAdapter(
               METHOD_WITH_MYSIMPLECLASS_PARAMETER_AND_WITH_RETURN_VALUE,
               bddMethod));
         assertEquals("MySimpleCass", mySimpleClass.getValue());
      }
   }

   public void testMethodWithMySimpleClass3ParameterWithAObjectParseAndReturnValueIsCalled()
      throws Exception {
      for (String bddMethod : bddMethods) {
         mySimpleClass3 = null;
         assertEquals(
            METHOD_WITH_MY_SIMPLE_CLASS3_PARAMETER_USING_OBJECT_PARSE_AND_WITH_RETURN_VALUE,
            executeStringMethodOnAdapter(
               METHOD_WITH_MY_SIMPLE_CLASS3_PARAMETER_USING_OBJECT_PARSE_AND_WITH_RETURN_VALUE,
               bddMethod));
         assertEquals("MySimpleCass3", mySimpleClass3.getValue());
      }
   }

   public void testAnnotatedClassesAreFoundAndStepsAreAvailable()
      throws Exception {
      // this method is available in FakeSteps.java
      assertEquals(true, executor.given("my step"));
      assertEquals(true, executor
         .given("my step in class requiring access to the adapter"));
   }

   private Object executeStringMethodOnAdapter(String methodPatten,
                                               String givenWhenOrThen) throws Exception {
      try {
         Method method = executor.getClass().getMethod(givenWhenOrThen,
                                                       String.class);
         return method.invoke(executor, methodPatten);
      } catch (Exception e) {
         throw new RuntimeException("Error executing '" + methodPatten
                                    + "' using '" + givenWhenOrThen + "': " + e.getMessage());
      }
   }

   public Object given(String methodString) throws Exception {
      return null;
   }

   public Object when(String methodString) throws Exception {
      return null;
   }

   public Object then(String methodString) throws Exception {
      return null;
   }

   public Object and(String methodString) throws Exception {
      return null;
   }

   @DomainSteps
   public static class FakeSteps {

      @DomainStep("my step")
      public boolean myStep() {
         return true;
      }
   }

   @DomainSteps
   public static class FakeSteps2 {

      private GivWenZen adapter;

      public FakeSteps2(GivWenZen adapter) {
         this.adapter = adapter;
      }

      @DomainStep("my step in class requiring access to the adapter")
      public boolean myStep2() {
         return true;
      }

      @DomainStep(METHOD_WITHOUT_PARAMETERS_AND_WITH_RETURN_VALUE)
      public String methodWithOutParametersAndWithReturnValue() {
         return METHOD_WITHOUT_PARAMETERS_AND_WITH_RETURN_VALUE;
      }

      @DomainStep(METHOD_WITHOUT_PARAMETERS_AND_WITHOUT_RETURN_VALUE)
      public void methodWithOutParametersAndWithoutReturnValue() {
         getExecutor().methodWithOutParametersAndWithoutReturnValueCalled = true;
      }

      @DomainStep("method with int of (\\d+) parameter and with return value")
      public String methodWithIntParameterAndWithReturnValue(int param) {
         getExecutor().intParamValue = param;
         return METHOD_WITH_INTEGER_PARAMETER_AND_WITH_RETURN_VALUE;
      }

      @DomainStep("method with (.*) parameter and with return value")
      public String methodWithMySimpleClassParameterUsingPropertyEditorAndWithReturnValue(
         MySimpleClass param) {
         getExecutor().mySimpleClass = param;
         return METHOD_WITH_MYSIMPLECLASS_PARAMETER_AND_WITH_RETURN_VALUE;
      }

      @DomainStep("method with (.*) parameter using object parse and with return value")
      public String methodWithMySimpleClass3ParameterWithObjectParseAndWithReturnValue(
         MySimpleClass3 param) {
         getExecutor().mySimpleClass3 = param;
         return METHOD_WITH_MY_SIMPLE_CLASS3_PARAMETER_USING_OBJECT_PARSE_AND_WITH_RETURN_VALUE;
      }

      private GivWenZenExecutorTest getExecutor() {
         return (GivWenZenExecutorTest) adapter;
      }
   }

   public static class MySimpleClass3 extends MySimpleClass {
      public MySimpleClass3(String value) {
         super(value);
      }

      public static MySimpleClass3 parse(String value) {
         return new MySimpleClass3(value);
      }
   }

   public static class MySimpleClass {
      private String value;

      public String getValue() {
         return value;
      }

      public MySimpleClass(String value) {
         this.value = value;
      }

      public String toString() {
         return this.value;
      }
   }

   public static class MySimpleClassEditor extends PropertyEditorSupport {
      public String getAsText() {
         return getValue().toString();
      }

      public void setAsText(String text) throws IllegalArgumentException {
         setValue(new MySimpleClass(text));
      }
   }
}