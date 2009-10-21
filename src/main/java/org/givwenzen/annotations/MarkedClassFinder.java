package org.givwenzen.annotations;

import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.*;

import org.reflections.Configuration;
import org.reflections.Reflections;
import org.reflections.adapters.SingleThreadedParallelStrategy;
import org.reflections.scanners.ClassAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.*;

import com.google.common.base.Predicate;

public class MarkedClassFinder {
  private Class<? extends Annotation> markerAnnotation = DomainSteps.class;
  private Reflections reflections;

  public MarkedClassFinder(final Class<? extends Annotation> markerAnnotation, final String basePackage) {
    this.markerAnnotation = markerAnnotation;
    Configuration configuration = new AbstractConfiguration() {
      {
        setUrls(getUrlsFromString(basePackage));
        Predicate<String> annotationFilter = new FilterBuilder().include(DomainSteps.class.getName());
        setScanners(new SubTypesScanner(), new ClassAnnotationsScanner().filterBy(annotationFilter));
        setParallelStrategy(new SingleThreadedParallelStrategy());
      }
    };
    findClasses(configuration);
  }

  private Collection<URL> getUrlsFromString(String basePackage) {
    Collection<URL> allUrls = new ArrayList<URL>();
    String[] packages = basePackage.split(",");
    for (String packageName : packages) {
      allUrls.addAll(ClasspathHelper.getUrlsForPackagePrefix(packageName.trim()));
    }
    return allUrls;
  }

  private void findClasses(Configuration configuration) {
    reflections = new Reflections(configuration);
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
