package org.givwenzen;


import org.givwenzen.annotations.DomainStep;
import org.givwenzen.parse.StringToObjectParser;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MethodAndInvocationTarget {
    //inject StringToObjectParser (with correct CustomParserFinder)
    private StringToObjectParser converter = new StringToObjectParser();
    private Method method;
    private Object target;

    public MethodAndInvocationTarget(Method method, Object target) {
        this.method = method;
        this.target = target;
    }

    public Object invoke(String methodString) throws Exception {
        try {
            return method.invoke(target, getParametersFromMethodString(methodString));
        } catch (InvocationTargetException e) {
            throw new GivWenZenExecutionException(buildExceptionMessage("Error while executing step", methodString), e);
        } catch (Exception e) {
            throw new InvalidDomainStepParameterException(buildExceptionMessage("Invalid step parameters in method pattern", methodString), e);
        }
    }

    private String buildExceptionMessage(String messageHeader, String methodString) throws NoSuchMethodException, InvocationTargetException,
            IllegalAccessException {
        return "\n" + messageHeader + ": " + methodString + "\n" +
                "  found matching method annotated with: " + getMethodDescriptionPattern().pattern() + "\n" +
                "  method signature is: " + getMethodAsString() + "\n" +
                "  step class is: " + getTargetAsString();
    }

    public String getMethodAsString() {
        return method.toString();
    }

    public Pattern getMethodDescriptionPattern()
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        return Pattern.compile(getDomainStepPattern());
    }

    public String getDomainStepPattern() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Annotation annotation = method.getAnnotation(DomainStep.class);
        Method patternMethod = annotation.getClass().getMethod("value");
        return String.valueOf(patternMethod.invoke(annotation));
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

    public String getTargetAsString() {
        return target.getClass().getName();
    }
}
