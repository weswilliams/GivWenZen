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
  private Collection<Object> stepDefinitions;
  private Map<String, MethodAndInvocationTarget> steps;

  public DomainStepMethodLocator(Collection<Object> stepDefinitions) {
    this.stepDefinitions = stepDefinitions;
  }

  public MethodAndInvocationTarget getMethodWithAnnotatedPatternMatching(String methodString)
      throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

    List<MethodAndInvocationTarget> possibleAmbiguities = findPossibleAmbiguities(methodString);

    if (possibleAmbiguities.size() > 1)
      return handleAmbiguousStepDefinitions(methodString, possibleAmbiguities);

    return possibleAmbiguities.get(0);
  }

  private List<MethodAndInvocationTarget> findPossibleAmbiguities(String methodString)
      throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
    List<MethodAndInvocationTarget> possibleAmbiguities = new ArrayList<MethodAndInvocationTarget>();
    for (MethodAndInvocationTarget methodAndTarget : getSteps()) {
      if (methodAndTarget.methodStringMatchesMethodPatern(methodString))
        possibleAmbiguities.add(methodAndTarget);
    }
    if (possibleAmbiguities.isEmpty()) {
      possibleAmbiguities.add(new MissingStepMethodAndInvocationTarget());
    }
    return possibleAmbiguities;
  }

  private MethodAndInvocationTarget handleAmbiguousStepDefinitions(String methodString,
      List<MethodAndInvocationTarget> matches) throws NoSuchMethodException, IllegalAccessException,
      InvocationTargetException {
    String message = "Multiple step definitions match \"" + methodString + "\"\n";
    for (MethodAndInvocationTarget match : matches) {
      message += match.getMethodAsString() + " \"" + match.getDomainStepPattern() + "\"\n";
    }
    throw new GivWenZenException(message);
  }

  private Collection<MethodAndInvocationTarget> getSteps() throws InvocationTargetException, NoSuchMethodException,
      IllegalAccessException {
    if (steps == null) {
      steps = findMethodsOnTargets();
    }
    return steps.values();
  }

  private Map<String, MethodAndInvocationTarget> findMethodsOnTargets() throws InvocationTargetException,
      NoSuchMethodException, IllegalAccessException {
    Map<String, MethodAndInvocationTarget> methodTargetsByDescription = new HashMap<String, MethodAndInvocationTarget>();
    stepDefinitions = removeDuplicates(stepDefinitions);
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

  private List<Object> removeDuplicates(Collection<Object> values) {
    List<Object> noDuplicates = new ArrayList<Object>(values.size());
    for (Object object : values) {
      if (collectionDoesNotContainInstanceOf(noDuplicates, object)) {
        noDuplicates.add(object);
      }
    }
    return noDuplicates;
  }

  private boolean collectionDoesNotContainInstanceOf(List<Object> noDuplicates, Object object) {
    for (Object possibleDup : noDuplicates) {
      if (possibleDup.getClass().equals(object.getClass())) {
        return false;
      }
    }
    return true;
  }

  private Map<String, MethodAndInvocationTarget> handleDuplicateStepDefinition(
      MethodAndInvocationTarget firstDefinition, MethodAndInvocationTarget secondDefinition, Pattern pattern) {
    String message = "Duplicate step definition: " + pattern.pattern();
    message += "\nfirst definition " + firstDefinition.getMethodAsString();
    message += "\nsecond definition " + secondDefinition.getMethodAsString();
    throw new GivWenZenException(message);
  }

  private List<MethodAndInvocationTarget> searchMethodsOnTarget(Object target) throws NoSuchMethodException,
      InvocationTargetException, IllegalAccessException {
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
