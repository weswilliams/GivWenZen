package org.givwenzen;

import static org.fest.assertions.Assertions.*;
import static org.mockito.Mockito.*;

import org.givwenzen.annotations.InstantiationStrategy;
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
      GivWenZenExecutor executor = creator
         .stepClassBasePackage(basePackage)
         .create();
      assertThat(executor.getBaseStepClassPackge()).isEqualTo(basePackage);
   }

   @Test
   public void shouldBeAbleToCreateGivWenZenExecutorWithStepSharedStateObject() throws Exception {
      String state = "my state object";
      GivWenZenExecutor executor = creator
         .customStepState(state)
         .create();
      assertThat(executor.getCustomStepState()[0]).isEqualTo(state);
   }

   @Test
   public void shouldBeAbleToCreateGivWenZenExecutorWithCustomStepClassInstantiationStrategy() throws Exception {
      InstantiationStrategy instantiationStrategy = mock(InstantiationStrategy.class);
      GivWenZenExecutor executor = creator
         .customInstantiationStrategies(instantiationStrategy)
         .create();
      assertThat(executor.getInstantiationStrategies()).contains(instantiationStrategy);
   }
}
