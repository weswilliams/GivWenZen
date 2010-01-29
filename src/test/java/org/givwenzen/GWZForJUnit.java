package org.givwenzen;

/**
 * This class is not safe for running multiple tests in the same JVM simultaneously.
 */
public final class GWZForJUnit {

   private static Object unitTest;
   private static GivWenZen gwz = null;

   public static void setUp(Object unitTest) {
      GWZForJUnit.unitTest = unitTest;
      gwz = null;
   }

   public static Object and(String methodString) throws Exception {
      return getGWZ().and(methodString);
   }

   public static Object given(String methodString) throws Exception {
      return getGWZ().and(methodString);
   }

   public static Object then(String methodString) throws Exception {
      return getGWZ().and(methodString);
   }

   public static Object when(String methodString) throws Exception {
      return getGWZ().and(methodString);
   }

   private static GivWenZen getGWZ() {
      String basePackageForSteps = DomainStepFinder.DEFAULT_STEP_PACKAGE;
      if (unitTest != null)
         basePackageForSteps += "," + unitTest.getClass().getPackage().getName() + ".";
      if (gwz == null)
         gwz = GivWenZenExecutorCreator.instance()
         .stepClassBasePackage(basePackageForSteps)
         .create();
      return gwz;
   }

}
