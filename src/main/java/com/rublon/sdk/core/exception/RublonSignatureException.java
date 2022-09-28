package com.rublon.sdk.core.exception;

/**
 * Invalid signature of the Rublon API response
 *
 */
public class RublonSignatureException extends RublonException {

	/**
	 * UID
	 */
	private static final long serialVersionUID = -41434156159068769L;

	/**
	 * Constructor
	 *
	 * @param message
	 * @param cause
	 */
	public RublonSignatureException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructor
	 * 
	 * @param message
	 */
	public RublonSignatureException(String message) {
		super(message);
	}
}
