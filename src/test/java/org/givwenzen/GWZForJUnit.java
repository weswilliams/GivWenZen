package org.givwenzen;

/**
 * This class is not safe for running multiple tests in the same JVM simultaneously.
 */
public final class GWZForJUnit {

  private static Object unitTest;

  public static void setUp(Object unitTest) {
    GWZForJUnit.unitTest = unitTest;
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
    Class<?> unitTestClass = sun.reflect.Reflection.getCallerClass(3);
    if (unitTest == null || !unitTestClass.equals(unitTest.getClass()))
      throw new IllegalStateException("you must call setup passing in the unit test instance");
    return new GivWenZenExecutor(unitTest, new DomainStepFinder(unitTest.getClass().getPackage().getName() + "."));
  }

}
