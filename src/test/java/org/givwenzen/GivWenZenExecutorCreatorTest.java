package org.givwenzen;

import static org.fest.assertions.Assertions.*;

import org.junit.Test;

public class GivWenZenExecutorCreatorTest {
   @Test
   public void shouldBeAbleToCreateGivWenZenExecutorWithDifferentStepPackage() throws Exception {
      String basePackage = "test.package.name";
      GivWenZenExecutorCreator creator = GivWenZenExecutorCreator.instance();
      creator.stepClassBasePackage(basePackage);
      GivWenZenExecutor executor = creator.create();
      assertThat(executor.getBaseStepClassPackge()).isEqualTo(basePackage);
   }
}
