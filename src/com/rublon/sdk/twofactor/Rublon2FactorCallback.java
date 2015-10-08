package com.rublon.sdk.twofactor;

import com.rublon.sdk.core.RublonCallback;
import com.rublon.sdk.core.exception.APIException;
import com.rublon.sdk.core.exception.ConnectionException;
import com.rublon.sdk.core.exception.RublonException;
import com.rublon.sdk.core.exception.RublonException.ConfigurationException;
import com.rublon.sdk.twofactor.api.Credentials;

/**
 * Rublon callback handler abstract class.
 * 
 * You need to create a subclass and implement some methods. 
 *
 */
abstract public class Rublon2FactorCallback extends RublonCallback {
	
	/**
	 * Rublon instance.
	 */
	protected Rublon2Factor rublon;
	
	
	/**
	 * Authentication credentials instance.
	 */
	protected Credentials credentials;
	
	
	/**
	 * Constructor.
	 * 
	 * @param rublon Rublon instance.
	 * @throws RublonException.ConfigurationException
	 */
	public Rublon2FactorCallback(Rublon2Factor rublon) throws ConfigurationException {
		super(rublon);
		this.rublon = rublon;
	}
	

	/**
	 * Finalize transaction for state "OK".
	 *
	 * @throws CallbackException
	 */
	protected void finalizeTransaction() throws CallbackException {
	
		super.finalizeTransaction();
		
		try {
			credentials = getRublon().getCredentials(getAccessToken());
		} catch (ConnectionException e) {
			throw new CallbackException("Connection problem in the Rublon callback method when trying to get auth credentials.", e);
		} catch (APIException e) {
			throw new CallbackException("Rublon API error in the Rublon callback method when trying to get auth credentials.", e);
		}
		
		// Authenticate user:
		userAuthenticated(credentials.getUserId());
		
	}
	
	
	/**
	 * Get Rublon instance.
	 */
	public Rublon2Factor getRublon() {
		return rublon;
	}
	
	
	/**
	 * Get the authentication credentials instance.
	 */
	public Credentials getCredentials() {
		return credentials;
	}
	

}
