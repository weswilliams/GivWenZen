package org.givwenzen;

import org.givwenzen.annotations.MarkedClass;

import java.util.List;
import java.util.Set;

public interface IDomainStepFactory {    
    public List<Object> createStepDefinitions(Set<MarkedClass> classes, Object... adapters);
}