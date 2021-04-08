package org.givwenzen;

import fitnesse.slim.SlimServer;
import fitnesse.slim.StackTraceEnricher;

public class GivWenZenException extends RuntimeException {
  private static final long serialVersionUID = 6287173811929462247L;
  private final String tag;

  public GivWenZenException(String message) {
    super(message);
    tag = "";
  }

  public GivWenZenException(String message, Throwable cause, String tag) {
    super(message, cause);
    this.tag = tag;
  }

  public GivWenZenException(Throwable cause, String tag) {
    super(cause);
    this.tag = tag;
  }

  public GivWenZenException(Throwable cause) {
    this(cause, "");
  }

  public GivWenZenException(String message, Exception e) {
    super(message, e);
    tag = "";
  }

  public GivWenZenException(String message, String tag) {
    super(message);
    this.tag = tag;
  }

  public GivWenZenException(String message, Throwable cause) {
    super(message, cause);
    tag = "";
  }

  public String getTag() {
    return tag;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    String msg = getMessage();
    if (msg != null && !msg.isEmpty()) {
      sb.append(msg);
    }

    StackTraceEnricher enricher = new StackTraceEnricher();
    if (getCause() != null) {
      sb.append(enricher.getStackTraceAsString(getCause()));
    } else {
      if (this.getStackTrace() == null || this.getStackTrace().length == 0) {
        this.fillInStackTrace();
      }
      sb.append(enricher.getStackTraceAsString(this));
    }
    return sb.toString();
  }

  public static boolean isStopTestException(Throwable t) {
    return t != null && t.getClass().toString().contains("StopTest");
  }

  public static boolean isStopSuiteException(Throwable t) {
    return t != null && t.getClass().toString().contains("StopSuite") || t instanceof InterruptedException;
  }
}
