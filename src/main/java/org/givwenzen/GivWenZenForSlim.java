package org.givwenzen;

public class GivWenZenForSlim implements GivWenZen {
   private GivWenZenExecutor executor;

   public GivWenZenForSlim() {
      this(new GivWenZenExecutor());
   }

   public GivWenZenForSlim(GivWenZenExecutor executor) {
      this.executor = executor;
   }

   public Object given(String methodString) throws Exception {
      return executor.given(methodString);
   }

   public Object when(String methodString) throws Exception {
      return executor.when(methodString);
   }

   public Object then(String methodString) throws Exception {
      return executor.then(methodString);
   }

   public Object and(String methodString) throws Exception {
      return executor.and(methodString);
   }

   public GivWenZenExecutor getExecutor() {
      return executor;
   }

   public Object Given(String methodString) throws Exception { return given(methodString); }

   public Object When(String methodString) throws Exception { return when(methodString); }

   public Object Then(String methodString) throws Exception { return then(methodString); }

   public Object And(String methodString) throws Exception { return and(methodString); }

}
