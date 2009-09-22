package org.givwenzen;

public class DomainStepNotFoundException extends GivWenZenException {
   public DomainStepNotFoundException(String message) {
      super(message);
      setStackTrace(new StackTraceElement[0]);
   }
}
