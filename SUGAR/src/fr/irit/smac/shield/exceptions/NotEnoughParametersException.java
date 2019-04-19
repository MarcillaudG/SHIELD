package fr.irit.smac.shield.exceptions;

public class NotEnoughParametersException extends Exception {

	public NotEnoughParametersException() {
	}

	public NotEnoughParametersException(String message) {
		super(message);
	}

	public NotEnoughParametersException(Throwable cause) {
		super(cause);
	}

	public NotEnoughParametersException(String message, Throwable cause) {
		super(message, cause);
	}

	public NotEnoughParametersException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		
	}

}
