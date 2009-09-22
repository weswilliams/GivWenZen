package org.givwenzen;

public class InvalidDomainStepParameterException extends GivWenZenException {
   public InvalidDomainStepParameterException(String message, Throwable cause) {
      super(message, cause);
      setStackTrace(cause.getStackTrace());
   }
}
