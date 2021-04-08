package bdd.steps;

import org.givwenzen.GivWenZenException;

public class StopTestExceptionForTesting extends GivWenZenException {

    public StopTestExceptionForTesting(Throwable cause) {
        super(cause);
    }

    public StopTestExceptionForTesting(String message) {
        super(message);
    }

}
