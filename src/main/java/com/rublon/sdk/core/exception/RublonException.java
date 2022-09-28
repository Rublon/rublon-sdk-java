package com.rublon.sdk.core.exception;

/**
 * Exception class
 * 
 * @author Rublon Developers
 */
public class RublonException extends Exception {

	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = 6413320177993119L;

	/**
	 * Constructor
	 *
	 * @param message
	 * @param cause
	 */
	public RublonException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructor
	 * 
	 * @param message
	 */
	public RublonException(String message) {
		super(message);
	}
	
	/**
	 * Create string from the exception data.
	 */
	public String toString() {
		String result = this.getClass() +": "+ this.getMessage();
		Throwable cause = this.getCause();
		if (cause != null && cause instanceof Exception) {
			result += "\nCause: "+ ((Exception)cause).toString();
		}
		return result;
	}

	static public class ConfigurationException extends RublonException {
		private static final long serialVersionUID = -12091387735327403L;
		public ConfigurationException() {
			super("System token or/and secret key must be provided to the RublonConsumer instance before calling this method.");
		}
	}
}
