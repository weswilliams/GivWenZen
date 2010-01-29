package org.givwenzen;

import static org.fest.assertions.Assertions.*;

import org.junit.Before;
import org.junit.Test;

public class GivWenZenExecutorCreatorTest {
   private GivWenZenExecutorCreator creator;

   @Before
   public void setup() {
      creator = GivWenZenExecutorCreator.instance();
   }

   @Test
   public void shouldBeAbleToCreateGivWenZenExecutorWithDifferentStepPackage() throws Exception {
      String basePackage = "test.package.name.";
      GivWenZenExecutor executor = creator.stepClassBasePackage(basePackage).create();
      assertThat(executor.getBaseStepClassPackge()).isEqualTo(basePackage);
   }

   @Test
   public void shouldBeAbleToCreateGivWenZenExecutorWithStepSharedStateObject() throws Exception {
      String state = "my state object";
      GivWenZenExecutor executor = creator.customStepState(state).create();
      assertThat(executor.getCustomStepState()).isEqualTo(state);
   }
}
