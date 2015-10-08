package com.rublon.sdk.core.exception;

/**
 * Connection error exception
 *
 */
public class ConnectionException extends RublonException {

	/**
	 * UID
	 */
	private static final long serialVersionUID = -4130434156159068769L;

		
	/**
	 * Constructor
	 *
	 * @param Message
	 * @param Code
	 * @param Previous exception
	 */
	public ConnectionException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructor
	 * 
	 * @param Message
	 */
	public ConnectionException(String message) {
		super(message);
	}



}
