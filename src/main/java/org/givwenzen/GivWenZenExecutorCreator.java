package org.givwenzen;

public class GivWenZenExecutorCreator {
   private String packageName;

   public GivWenZenExecutorCreator stepClassBasePackage(String packageName) {
      this.packageName = packageName;
      return this;
   }

   public static GivWenZenExecutorCreator instance() {
      return new GivWenZenExecutorCreator();
   }

   public GivWenZenExecutor create() {
      // null, new DomainStepFinder(), new DomainStepFactory()
      return new GivWenZenExecutor(getStepState(), createDomainStepFinder(), createDomainStepFactory());
   }

   private DomainStepFactory createDomainStepFactory() {
      return new DomainStepFactory();
   }

   private DomainStepFinder createDomainStepFinder() {
      if (packageName != null) return new DomainStepFinder(packageName);
      return new DomainStepFinder();
   }

   private Object getStepState() {
      return null;
   }
}
