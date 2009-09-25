package org.givwenzen;

public class InvalidDomainStepParameterException extends GivWenZenException {
	private static final long serialVersionUID = -5758694984621732105L;

	public InvalidDomainStepParameterException(String message, Throwable cause) {
		super(message, cause);
		setStackTrace(cause.getStackTrace());
	}
}
