package fr.irit.smac.shield.exceptions;

public class TooMuchVariableToRemoveException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6089475469144943974L;

	public TooMuchVariableToRemoveException() {
	}

	public TooMuchVariableToRemoveException(String message) {
		super(message);
	}

	public TooMuchVariableToRemoveException(Throwable cause) {
		super(cause);
	}

	public TooMuchVariableToRemoveException(String message, Throwable cause) {
		super(message, cause);
	}

	public TooMuchVariableToRemoveException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
