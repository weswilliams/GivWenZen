package org.givwenzen;

public class GivWenZenWithCustomState extends GivWenZenForSlim {

  public GivWenZenWithCustomState() {
    super(new GivWenZenExecutor(new CustomState(), new DomainStepFinder()));
  }
}
