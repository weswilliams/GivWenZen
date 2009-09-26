package org.givwenzen;


import static org.junit.Assert.*;


import org.givwenzen.GivWenZenException;
import org.givwenzen.DomainStepMethodLocator;
import org.givwenzen.annotations.DomainStep;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class DomainStepMethodLocatorTest {
   private DomainStepMethodLocator locator;

   @Test
   public void testGetMethodWithAnnotatedPatternMatching() throws Exception {
      List<Object> stepDefinitions = new ArrayList<Object>();
      stepDefinitions.add(new StepsWithDuplicateAnnotatedMethod());
      locator = new DomainStepMethodLocator(stepDefinitions);
      try {
         locator.getMethodWithAnnotatedPatternMatching(null);
         fail("should get error");
      }  catch (GivWenZenException e) {
         assertTrue("should get this", true);
      }
   }

   static class StepsWithDuplicateAnnotatedMethod {

      @DomainStep("duplicate")
      public void method1() {}

      @DomainStep("duplicate")
      public void method2() {}
   }
}