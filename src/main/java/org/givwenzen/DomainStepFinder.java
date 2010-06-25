package org.givwenzen;

import org.givwenzen.annotations.DomainSteps;
import org.givwenzen.annotations.MarkedClass;
import org.givwenzen.annotations.MarkedClassFinder;

import java.util.Set;

public class DomainStepFinder implements IDomainStepFinder {

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
