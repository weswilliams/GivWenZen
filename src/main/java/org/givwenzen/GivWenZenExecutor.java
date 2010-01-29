package org.givwenzen;


import java.util.List;
import java.util.Set;

import org.givwenzen.annotations.InstantiationStrategy;
import org.givwenzen.annotations.MarkedClass;

public class GivWenZenExecutor implements GivWenZen {
   private Object stepState;
   private DomainStepMethodLocator methodLocator;
   private DomainStepFinder domainStepFinder;
   private DomainStepFactory domainStepFactory;

   public GivWenZenExecutor() {
      this(null, new DomainStepFinder(), new DomainStepFactory());
   }

   public GivWenZenExecutor(Object stepState, DomainStepFinder domainStepFinder, DomainStepFactory factory) {
      super();
      this.domainStepFinder = domainStepFinder;
      this.domainStepFactory = factory;
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
      MethodAndInvocationTarget method = methodLocator.getMethodWithAnnotatedPatternMatching(methodString);
      Object returnValue;
      try {
         returnValue = method.invoke(methodString);
      } catch (GivWenZenException e) {
        throw e;
      } catch (Exception e) {
         throw new GivWenZenException(
            "\nSomething went drastically wrong while exectuing the step for: " + methodString + "\n" +
            "  found matching method annotated with: " + method.getMethodDescriptionPattern().pattern() + "\n" +
            "  method signature is: " + method.getMethodAsString() + "\n" +
            "  step class is: " + method.getTargetAsString(), e);

      }
      return returnValue == null ? stepState : returnValue;
   }

   public String getBaseStepClassPackge() {
      return domainStepFinder.getPackage();
   }

   public Object getCustomStepState() {
      return stepState;
   }

   public List<InstantiationStrategy> getInstantiationStrategies() {
      return domainStepFactory.getInstantiationStrategies();
   }
}
