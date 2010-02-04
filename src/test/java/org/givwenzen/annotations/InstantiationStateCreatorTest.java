package org.givwenzen.annotations;

import static org.fest.assertions.Assertions.*;
import org.junit.Test;

public class InstantiationStateCreatorTest {

   @Test
   public void shouldCreateUninstantiatedState() throws Exception {
      InstantiationState state = new InstantiationStateCreator()
         .didNotInstantiate();
      assertThat(state.couldInstantiate()).isFalse();
   }

   @Test
   public void shouldCreate() throws Exception {
      Object object = "My Object";
      InstantiationState state = new InstantiationStateCreator()
         .didInstantiate(object);
      assertThat(state.couldInstantiate()).isTrue();
      assertThat(state.getInstantiation()).isEqualTo(object);
   }
}
