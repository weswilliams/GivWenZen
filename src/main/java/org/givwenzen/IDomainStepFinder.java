package org.givwenzen;

import org.givwenzen.annotations.MarkedClass;

import java.util.Set;

public interface IDomainStepFinder {
    Set<MarkedClass> findStepDefinitions();
}
