package com.rublon.sdk.login;

import org.json.JSONArray;

import com.rublon.sdk.core.RublonCallback;
import com.rublon.sdk.core.exception.APIException;
import com.rublon.sdk.core.exception.ConnectionException;
import com.rublon.sdk.core.exception.RublonException;
import com.rublon.sdk.core.exception.RublonException.ConfigurationException;
import com.rublon.sdk.login.api.Credentials;

/**
 * Class to handle the Rublon callback action.
 */
abstract public class RublonLoginCallback extends RublonCallback {
	
	
	/**
	 * Instance of the RublonLogin class.
	 */
	protected RublonLogin rublon;
	
	
	/**
	 * Rublon API response instance.
	 */
	protected Credentials credentials;
	
	
	/**
	 * Constructor.
	 * 
	 * @param rublon
	 * @throws RublonException.ConfigurationException
	 */
	public RublonLoginCallback(RublonLogin rublon) throws ConfigurationException {
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
		userAuthenticated(credentials.getProfileId());
	
	}
	
	
	/**
	 * Get Rublon instance.
	 */
	public RublonLogin getRublon() {
		return rublon;
	}
	
	
	/**
	 * Get the authentication credentials instance.
	 */
	public Credentials getCredentials() {
		return this.credentials;
	}
	
	
	/**
	 * Get the Rublon user's email addresses hashes with the UIDs.
	 */
	public JSONArray getUserEmailHashList() {
		return getCredentials().getUserEmailHashEntities();
	}
	
	

	/**
	 * Handle authentication success.
	 * 
	 * @param userId
	 * @return void
	 */
	abstract protected void userAuthenticated(String profileId);
	

}
