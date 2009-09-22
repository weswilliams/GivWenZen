package org.givwenzen;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.givwenzen.annotations.DomainStep;

public class DomainStepMethodLocator {
   private List<Object> stepDefinitions;
   private Map<String, MethodAndInvocationTarget> steps;

   public DomainStepMethodLocator(List<Object> stepDefinitions) {
      this.stepDefinitions = stepDefinitions;
   }

   public MethodAndInvocationTarget getMethodWithAnnotatedPatternMatching(String methodString)
      throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
      for (MethodAndInvocationTarget methodAndTarget : getSteps()) {
         if (methodAndTarget.methodStringMatchesMethodPatern(methodString)) return methodAndTarget;
      }
      return null;
   }

   private Collection<MethodAndInvocationTarget> getSteps()
      throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
      if (steps == null) {
         steps = findMethodsOnTargets();
      }
      return steps.values();
   }

   private Map<String, MethodAndInvocationTarget> findMethodsOnTargets()
      throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
      Map<String, MethodAndInvocationTarget>
         methodTargetsByDescription =
         new HashMap<String, MethodAndInvocationTarget>();
      for (Object stepDefinition : stepDefinitions) {
         for (MethodAndInvocationTarget method : searchMethodsOnTarget(stepDefinition)) {
            Pattern pattern = method.getMethodDescriptionPattern();
            if (methodTargetsByDescription.containsKey(pattern.pattern())) {
               return handleDuplicateStepDefinition(methodTargetsByDescription.get(pattern.pattern()), method, pattern);
            }
            methodTargetsByDescription.put(method.getMethodDescriptionPattern().pattern(), method);
         }
      }
      return methodTargetsByDescription;
   }

   private Map<String, MethodAndInvocationTarget> handleDuplicateStepDefinition(MethodAndInvocationTarget firstDefinition,
                                                                                MethodAndInvocationTarget secondDefinition,
                                                                                Pattern pattern
   ) {
      String message = "Duplicate step definition: " + pattern.pattern();
      message += "\nfirst definition " + firstDefinition.getMethodAsString();
      message += "\nsecond definition " + secondDefinition.getMethodAsString();
      throw new GivWenZenException(message);
   }

   private List<MethodAndInvocationTarget> searchMethodsOnTarget(Object target)
      throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
      List<MethodAndInvocationTarget> targetMethods = new ArrayList<MethodAndInvocationTarget>();
      Method[] methods = target.getClass().getMethods();
      for (Method method : methods) {
         if (method.isAnnotationPresent(DomainStep.class)) {
            targetMethods.add(new MethodAndInvocationTarget(method, target, null));
         }
      }
      return targetMethods;
   }

}
