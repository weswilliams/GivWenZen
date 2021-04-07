package org.givwenzen;

public class StopTestGivWenZenException extends GivWenZenException {
    private static final String STOP_EXCEPTION_TAG = "ABORT_SLIM_TEST";

    public StopTestGivWenZenException(String message, Throwable cause) {
        super(message, cause, STOP_EXCEPTION_TAG);
    }

    public StopTestGivWenZenException(Throwable cause) {
        super(cause, STOP_EXCEPTION_TAG);
    }

    public StopTestGivWenZenException(String message) {
        super(message, STOP_EXCEPTION_TAG);
    }
}
