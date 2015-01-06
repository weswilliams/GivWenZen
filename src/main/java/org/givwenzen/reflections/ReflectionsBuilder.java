package org.givwenzen.reflections;

import com.google.common.base.Predicate;
import org.givwenzen.reflections.adapters.SingleThreadedParallelStrategy;
import org.givwenzen.reflections.scanners.ClassAnnotationsScanner;
import org.givwenzen.reflections.scanners.Scanner;
import org.givwenzen.reflections.scanners.SubTypesScanner;
import org.givwenzen.reflections.util.AbstractConfiguration;
import org.givwenzen.reflections.util.ClasspathHelper;
import org.givwenzen.reflections.util.FilterBuilder;

import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
      Predicate<String> annotationFilter = new FilterBuilder().include(aClass.getName());
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