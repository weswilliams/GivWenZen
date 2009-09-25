package org.givwenzen;

public class GivWenZenException extends RuntimeException {
  private static final long serialVersionUID = 6287173811929462247L;

  public GivWenZenException(String message) {
    super(message);
  }

  public GivWenZenException(String message, Throwable cause) {
    super(message, cause);
  }
}
