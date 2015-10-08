package com.rublon.sdk.core.exception;

/**
 * Invalid signature of the Rublon API response
 *
 */
public class RublonSignatureException extends RublonException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -41434156159068769L;

		
	/**
	 * Constructor
	 *
	 * @param Message
	 * @param Code
	 * @param Previous exception
	 */
	public RublonSignatureException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructor
	 * 
	 * @param Message
	 */
	public RublonSignatureException(String message) {
		super(message);
	}



}
