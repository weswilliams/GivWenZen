package org.givwenzen.reflections;

import com.google.common.base.*;
import org.givwenzen.annotations.*;
import org.givwenzen.reflections.adapters.*;
import org.givwenzen.reflections.scanners.*;
import org.givwenzen.reflections.scanners.Scanner;
import org.givwenzen.reflections.util.*;

import java.lang.annotation.*;
import java.net.*;
import java.util.*;

public class ReflectionsBuilder {
   private String basePackage;
   private List<Scanner> scanners = new ArrayList<Scanner>();

   public ReflectionsBuilder basePackage(String basePackage) {
      this.basePackage = basePackage;
      return this;
   }

   public ReflectionsBuilder subTypeScanner() {
      scanners.add(new SubTypesScanner());
      return this;
   }

   public ReflectionsBuilder classAnnotationScanner(Class<? extends Annotation> aClass) {
      Predicate<String> annotationFilter = new FilterBuilder().include(DomainSteps.class.getName());
      scanners.add(new ClassAnnotationsScanner().filterBy(annotationFilter));
      return this;
   }

   public Reflections build() {
      Configuration configuration = new AbstractConfiguration() {
         {
            setUrls(getUrlsFromString(basePackage));
            setScanners(scanners.toArray(new Scanner[scanners.size()]));
            setParallelStrategy(new SingleThreadedParallelStrategy());
         }
      };
      return new Reflections(configuration);
   }

   private Collection<URL> getUrlsFromString(String basePackage) {
      Collection<URL> allUrls = new ArrayList<URL>();
      String[] packages = basePackage.split(",");
      for (String packageName : packages) {
         allUrls.addAll(ClasspathHelper.getUrlsForPackagePrefix(packageName.trim()));
      }
      return allUrls;
   }
}