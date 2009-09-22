package org.givwenzen.annotations;

import com.google.common.collect.Sets;
import org.reflections.Configuration;
import org.reflections.Reflections;
import org.reflections.filters.IncludePrefix;
import org.reflections.scanners.ClassAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.AbstractConfiguration;
import org.reflections.util.ClasspathHelper;

import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

public class MarkedClassFinder {
   private Class markerAnnotation = DomainSteps.class;
   private Reflections reflections;

   public MarkedClassFinder(final Class markerAnnotation, final String basePackage) {
      this.markerAnnotation = markerAnnotation;
      Configuration configuration = new AbstractConfiguration() {
         {
            Set<URL> allUrls = getUrlsFromString(basePackage);

            setUrls(allUrls);
            setScanners(new SubTypesScanner(), new ClassAnnotationsScanner());
            setFilter(new IncludePrefix(markerAnnotation.getName()));
         }
      };
      findClasses(configuration);
   }

   private Set<URL> getUrlsFromString(String basePackage) {
      Set<URL> allUrls = Sets.newHashSet();
      StringTokenizer tokenizer = new StringTokenizer(basePackage, ",");
      while (tokenizer.hasMoreTokens()) {
         allUrls.addAll(ClasspathHelper.getUrlsForPackagePrefix(tokenizer.nextToken().trim()));
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
