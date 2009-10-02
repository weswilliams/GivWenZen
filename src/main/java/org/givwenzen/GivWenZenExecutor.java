package org.givwenzen;


import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Set;

import org.givwenzen.annotations.MarkedClass;

public class GivWenZenExecutor implements GivWenZen {
   private Object stepState;
   private DomainStepMethodLocator methodLocator;
   private DomainStepFinder domainStepFinder;

   public GivWenZenExecutor() {
      this(null, new DomainStepFinder(), new DomainStepFactory());
   }

   public GivWenZenExecutor(Object stepState, DomainStepFinder domainStepFinder, DomainStepFactory factory) {
      super();
      this.domainStepFinder = domainStepFinder;
      this.stepState = stepState == null ? this : stepState;
      Object[] adapters = new Object[]{this.stepState, this};
      Set<MarkedClass> classes = domainStepFinder.findStepDefinitions();
      List<Object> stepDefinitions = factory.createStepDefinitions(classes, adapters);
      stepDefinitions.add(0, this.stepState);
      methodLocator = new DomainStepMethodLocator(stepDefinitions);
   }

   public GivWenZenExecutor(Object stepState, DomainStepFinder domainStepFinder) {
      this(stepState, domainStepFinder, new DomainStepFactory());
   }

   public Object given(String methodString) throws Exception {
      return executeMethodWithMatchingAnnotation(methodString);
   }

   public Object when(String methodString) throws Exception {
      return executeMethodWithMatchingAnnotation(methodString);
   }

   public Object then(String methodString) throws Exception {
      return executeMethodWithMatchingAnnotation(methodString);
   }

   public Object and(String methodString) throws Exception {
      return executeMethodWithMatchingAnnotation(methodString);
   }

   private Object executeMethodWithMatchingAnnotation(String methodString) throws Exception {
      MethodAndInvocationTarget method = getMethod(methodString);
      Object returnValue;
      try {
         returnValue = method.invoke(methodString);
      } catch (InvocationTargetException e) {
         System.err.println("Exception executing step " + methodString);
         e.getCause().printStackTrace(System.err);
         throw e;
      } catch (Exception e) {
         throw new InvalidDomainStepParameterException(
            "\nInvalid step parameters in method pattern: " + methodString + "\n" +
            "  found matching method annotated with: " + method.getMethodDescriptionPattern().pattern() + "\n" +
            "  method signature is: " + method.getMethodAsString() + "\n" +
            "  step class is: " + method.getTargetAsString(), e);

      }
      return returnValue == null ? stepState : returnValue;
   }

   private MethodAndInvocationTarget getMethod(String methodString) throws
                                                                    NoSuchMethodException,
                                                                    InvocationTargetException,
                                                                    IllegalAccessException {
      MethodAndInvocationTarget method = methodLocator.getMethodWithAnnotatedPatternMatching(methodString);
      if (method == null) {
         throw new DomainStepNotFoundException(
            "\nYou need a step class with an annotated method matching this pattern: '" + methodString + "'\n" +
            "The step class should be placed in the package or sub-package of " + domainStepFinder.getPackage() + "\n" +
            "Example:\n" +
            "  @DomainSteps\n" +
            "  public class StepClass {\n" +
            "    @DomainStep(\"" + methodString + "\")\n" +
            "    public void domainStep() {\n" +
            "      // TODO implement step\n" +
            "    }" +
            "  }");
      }
      return method;
   }
}
