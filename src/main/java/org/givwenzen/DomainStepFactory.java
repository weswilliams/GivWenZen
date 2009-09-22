package org.givwenzen;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.givwenzen.annotations.InstantiationStrategy;
import org.givwenzen.annotations.MarkedClass;
import org.givwenzen.annotations.NoParameterInstantiationStrategy;
import org.givwenzen.annotations.ObjectInterfacesInstantiationStrategy;
import org.givwenzen.annotations.ObjectParameterInstantiationStrategy;

public class DomainStepFactory {
   private List<InstantiationStrategy> instantiationStrategies = new ArrayList<InstantiationStrategy>();

   public DomainStepFactory(InstantiationStrategy... strategies) {
      for (InstantiationStrategy strategy : strategies) {
         add(strategy);
      }
      addDefaultInstantiationStrategies();
   }

   public DomainStepFactory() {
      addDefaultInstantiationStrategies();
   }

   public List<Object> createStepDefinitions(Set<MarkedClass> classes, Object... adapters) {
      List<Object> stepDefinitions = new ArrayList<Object>();
      for (MarkedClass markedClass : classes) {
         try {
            stepDefinitions.add(create(markedClass, adapters));
         } catch (Exception e) {
            throw new RuntimeException("Error creating step: " + markedClass.getName(), e);
         }
      }
      return stepDefinitions;
   }

   public Object create(MarkedClass markedClass, Object... adapters) {
      try {
         for (Object adapter : adapters) {
            for (InstantiationStrategy instantiationStrategy : instantiationStrategies) {
               Object stepDef = instantiationStrategy.instantiate(markedClass.getTargetClass(), adapter);
               if (stepDef != null) return stepDef;
            }
         }
      } catch (Exception e) {
         System.err.println("Unable to instantiate class " + markedClass.getName() + " due to " + e);
         return MarkedClass.DUMMY_MARKED_CLASS;
      }
      return MarkedClass.DUMMY_MARKED_CLASS;
   }

   private void addDefaultInstantiationStrategies() {
      add(new ObjectInterfacesInstantiationStrategy());
      add(new ObjectParameterInstantiationStrategy());
      add(new NoParameterInstantiationStrategy());
   }

   private void add(InstantiationStrategy strategy) {
      instantiationStrategies.add(strategy);
   }

}