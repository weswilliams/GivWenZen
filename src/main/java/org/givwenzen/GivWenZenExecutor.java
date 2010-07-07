package org.givwenzen;


import org.givwenzen.annotations.InstantiationStrategy;
import org.givwenzen.annotations.MarkedClass;

import java.util.List;
import java.util.Set;

public class GivWenZenExecutor implements GivWenZen {
    private Object[] stepState;
    private DomainStepMethodLocator methodLocator;
    IDomainStepFinder domainStepFinder;
    private DomainStepFactory domainStepFactory;

    //deprecate
    public GivWenZenExecutor() {
        this(new DomainStepFinder(), new DomainStepFactory());
    }

    //deprecate
    public GivWenZenExecutor(DomainStepFinder domainStepFinder, DomainStepFactory factory, Object... stepState) {
        this((IDomainStepFinder) domainStepFinder, factory, stepState);
    }

    //GivWenZenExecutorSettings
    GivWenZenExecutor(IDomainStepFinder domainStepFinder, DomainStepFactory factory, Object... stepState) {
        super();
        this.domainStepFinder = domainStepFinder;
        this.domainStepFactory = factory;
//      this.stepState = stepState == null ? this : stepState;
//      Object[] adapters = new Object[]{this.stepState, this};

        this.stepState = stepState == null || stepState.length == 0 ? new Object[]{this} : stepState;
        int adaptersLength = this.stepState.length + 1;
        Object[] adapters = new Object[adaptersLength];
        System.arraycopy(this.stepState, 0, adapters, 0, adaptersLength - 1);
        adapters[adaptersLength - 1] = this;

        Set<MarkedClass> classes = domainStepFinder.findStepDefinitions();
        List<Object> stepDefinitions = factory.createStepDefinitions(classes, adapters);
        stepDefinitions.add(0, this.stepState);

        //inject DomainStepMethodLocator 
        methodLocator = new DomainStepMethodLocator(stepDefinitions);
    }

    public GivWenZenExecutor(DomainStepFinder domainStepFinder, Object... stepState) {
        this(domainStepFinder, new DomainStepFactory(), stepState);
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
        return returnValue == null ? stepState[0] : returnValue;
    }

    public Object[] getCustomStepState() {
        return stepState;
    }

    public List<InstantiationStrategy> getInstantiationStrategies() {
        return domainStepFactory.getInstantiationStrategies();
    }
}
