package org.givwenzen.annotations;

public class DefaultInstantiationState implements InstantiationState {

  public static final DefaultInstantiationState UNINSTANTIATED = new DefaultInstantiationState();
  
  private boolean isInstantiated = false;
  private Object instantiation;

  public DefaultInstantiationState() {
  }

  public DefaultInstantiationState(boolean isInstantiated, Object instantiation) {
    this.isInstantiated = isInstantiated;
    this.instantiation = instantiation;
  }

  public boolean couldInstantiate() {
    return isInstantiated;
  }

  public Object getInstantiation() {
    return instantiation;
  }

}
