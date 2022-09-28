package com.rublon.sdk.core;

import com.rublon.sdk.core.exception.RublonException;
import com.rublon.sdk.core.exception.RublonException.ConfigurationException;

/**
 * Rublon callback handler top abstract class.
 */
abstract public class RublonHttpCallback {
	
	/**
	 * State GET parameter name.
	 */
	public static final String PARAMETER_STATE = "rublonState";
	
	/**
	 * Access token GET parameter name.
	 */
	public static final String PARAMETER_ACCESS_TOKEN = "rublonToken";
	
	/**
	 * Custom URI param GET parameter name.
	 */
	public static final String PARAMETER_CUSTOM_URI_PARAM = "custom";
	
	/**
	 * Success state value.
	 */
	public static final String STATE_OK = "ok";
	
	/**
	 * Error state value.
	 */
	public static final String STATE_ERROR = "error";

	/**
	 * Instance of the Rublon Consumer.
	 */
	protected RublonConsumer rublon;

	/**
	 * Constructor.
	 * 
	 * @param rublon Rublon instance.
	 * @throws RublonException.ConfigurationException
	 */
	public RublonHttpCallback(RublonConsumer rublon) throws ConfigurationException {
		rublon.testConfiguration();
		this.rublon = rublon;
	}
	

	/**
	 * Invoke the callback.
	 * 
	 * @throws CallbackException 
	 */
	public void call() throws CallbackException {
		
		String state = getState().toLowerCase();
		
		if (state.equals(STATE_OK)) {
			finalizeTransaction();
		} else if (state.equals(STATE_ERROR)) {
			handleError();
		} else {
			handleCancel();
		}
	
	}
	
	/**
	 * Finalize transaction for state "OK".
	 *
	 * @throws CallbackException 
	 */
	protected void finalizeTransaction() throws CallbackException {
		
		String accessToken = getAccessToken();
		if (accessToken == null || accessToken.length() == 0) {
			throw new CallbackException("Missing access token.");
		}
	
	}

	
	/**
	 * Get Rublon instance.
	 */
	public RublonConsumer getRublon() {
		return rublon;
	}

	/**
	 * Handle authentication success.
	 * 
	 * @param appUserId
	 * @return void
	 */
	abstract protected void userAuthenticated(String appUserId);
	
	
	/**
	 * Handle state "error".
	 */
	abstract protected void handleError();
	
	/**
	 * Handle state "cancel".
	 */
	abstract protected void handleCancel();
	

	/**
	 * Get state from HTTP GET parameters or NULL if not present.
	 *
	 * @return string|NULL
	 */
	abstract public String getState();
	
	/**
	 * Get access token from HTTP GET parameters or NULL if not present.
	 *
	 * @return string|NULL
	 */
	abstract public String getAccessToken();
	
	

	/**
	 * Callback exception class.
	 */
	static public class CallbackException extends RublonException {

		private static final long serialVersionUID = -6331356975121874855L;

		public CallbackException(String message) {
			super(message);
		}
		
		public CallbackException(String message, Throwable cause) {
			super(message, cause);
		}
		
	}

}
