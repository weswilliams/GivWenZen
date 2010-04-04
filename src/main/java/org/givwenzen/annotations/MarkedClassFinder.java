package org.givwenzen.annotations;

import org.givwenzen.reflections.*;

import java.lang.annotation.*;
import java.util.*;

public class MarkedClassFinder {
   private Class<? extends Annotation> markerAnnotation = DomainSteps.class;
   private Reflections reflections;

   public MarkedClassFinder(final Class<? extends Annotation> markerAnnotation, final String basePackage) {
      this.markerAnnotation = markerAnnotation;

      reflections = new ReflectionsBuilder()
         .basePackage(basePackage)
         .subTypeScanner()
         .classAnnotationScanner(DomainSteps.class)
         .build();
   }

   public Set<MarkedClass> findMarkedClasses() {
      return getMarkedClasses();
   }

   private Set<MarkedClass> getMarkedClasses() {
      Set<Class<?>> classes = reflections.getTypesAnnotatedWith(markerAnnotation);
      Set<MarkedClass> markedClasses = new HashSet<MarkedClass>(classes.size());
      for (Class<?> aClass : classes) {
         markedClasses.add(new MarkedClass(aClass));
      }
      return markedClasses;
   }

}
