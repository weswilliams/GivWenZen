package org.givwenzen;

import fitlibrary.DoFixture;

public class GivWenZenDoFixture extends DoFixture {

   public GivWenZenDoFixture() {
      super(GivWenZenExecutorCreator.instance()
         .customStepState(new CustomState())
         .create());
   }
}
