package org.givwenzen;

import java.util.Set;

import org.givwenzen.annotations.*;

public class DomainStepFinder {

    public static final String DEFAULT_STEP_PACKAGE = "bdd.steps.";
    private String packageForSteps;
    private MarkedClassFinder finder;

    public DomainStepFinder() {
        this(DEFAULT_STEP_PACKAGE);
    }

    public DomainStepFinder(String basePackageForSteps) {
        packageForSteps = basePackageForSteps;
        finder = new MarkedClassFinder(DomainSteps.class, packageForSteps);
    }

    public Set<MarkedClass> findStepDefinitions() {
        return finder.findMarkedClasses();
    }

    public String getPackage() {
        return packageForSteps;
    }
}
