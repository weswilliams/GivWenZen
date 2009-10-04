package org.givwenzen;


import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.regex.MatchResult;
import java.util.List;
import java.util.ArrayList;

import org.givwenzen.annotations.DomainStep;
import org.givwenzen.parse.StringToObjectParser;

public class MethodAndInvocationTarget {
   private StringToObjectParser converter = new StringToObjectParser();
   private Method method;
   private Object target;

   public MethodAndInvocationTarget(Method method, Object target, String methodString) {
      this.method = method;
      this.target = target;
   }

   public Object invoke(String methodString) throws Exception {
      return method.invoke(target, getParametersFromMethodString(methodString));
   }

   public String getMethodAsString() {
      return method.toString();
   }

   public Pattern getMethodDescriptionPattern()
      throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
      Annotation annotation = method.getAnnotation(DomainStep.class);
      Method patternMethod = annotation.getClass().getMethod("value");
      return Pattern.compile(String.valueOf(patternMethod.invoke(annotation)));
   }

   public boolean methodStringMatchesMethodPatern(String methodString)
      throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
      return matches(getMatcherFor(methodString));
   }

  private boolean matches(Matcher matcher) {
    return matcher.matches();
  }

  private Matcher getMatcherFor(String methodString) throws NoSuchMethodException, InvocationTargetException,
      IllegalAccessException {
    Pattern pattern = getMethodDescriptionPattern();
    Matcher matcher = pattern.matcher(methodString);
    return matcher;
  }

   public Object[] getParametersFromMethodString(String methodString) throws Exception {
      List<Object> params = new ArrayList<Object>();
      Matcher matcher = getMatcherFor(methodString);
      if (matches(matcher)) {
         MatchResult matchResult = matcher.toMatchResult();
         for (int index = 1; index <= matchResult.groupCount(); index++) {
            params.add(matchResult.group(index));
         }
      }
      return converter.convertParamertersToTypes(
         params.toArray(new Object[params.size()]), method.getParameterTypes());
   }

   public Annotation getAnnotation(Class<DomainStep> bddClass) {
      return method.getAnnotation(bddClass);
   }

   public Class<?>[] getParameterTypes() {
      return method.getParameterTypes();
   }

   public String getTargetAsString() {
      return target.getClass().getName();
   }
}
