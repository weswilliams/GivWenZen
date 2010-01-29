package org.givwenzen;

public class GivWenZenExecutorCreator {
   private String packageName;
   private Object state;

   public static GivWenZenExecutorCreator instance() {
      return new GivWenZenExecutorCreator();
   }

   public GivWenZenExecutorCreator stepClassBasePackage(String packageName) {
      this.packageName = packageName;
      return this;
   }

   public GivWenZenExecutorCreator customStepState(Object state) {
      this.state = state;
      return this;
   }

   public GivWenZenExecutor create() {
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
      return state;
   }
}
