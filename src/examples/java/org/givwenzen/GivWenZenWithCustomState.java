package org.givwenzen;

public class GivWenZenWithCustomState extends GivWenZenForSlim {

   public GivWenZenWithCustomState() {
      super(GivWenZenExecutorCreator.instance()
         .customStepState(new CustomState())
         .create());
   }
}
