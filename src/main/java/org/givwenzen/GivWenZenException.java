package org.givwenzen;

public class GivWenZenException extends RuntimeException {
   public GivWenZenException(String message) {
      super(message);
   }

   public GivWenZenException(String message, Throwable cause) {
      super(message, cause);
   }
}
