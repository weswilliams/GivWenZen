package org.givwenzen;

public class DomainStepNotFoundException extends GivWenZenException {
  private static final long serialVersionUID = 5740717106423132496L;

  public DomainStepNotFoundException(String message) {
    super(message);
    setStackTrace(new StackTraceElement[0]);
  }
}
