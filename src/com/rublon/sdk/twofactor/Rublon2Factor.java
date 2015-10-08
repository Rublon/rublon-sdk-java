package com.rublon.sdk.twofactor;


import org.json.JSONObject;

import com.rublon.sdk.core.RublonAuthParams;
import com.rublon.sdk.core.RublonConsumer;
import com.rublon.sdk.core.exception.APIException;
import com.rublon.sdk.core.exception.ConnectionException;
import com.rublon.sdk.core.exception.RublonException;
import com.rublon.sdk.twofactor.api.BeginTransaction;
import com.rublon.sdk.twofactor.api.Credentials;

/**
 * Rublon 2-factor service provider class.
 * 
 * Create a subclass to override the specific methods.
 */
public class Rublon2Factor extends RublonConsumer {
	
	
	/**
	 * Service name.
	 */
	private static final String SERVICE_NAME = "2factor";


	/**
	 * Construct an instance.
	 * 
	 * @param systemToken
	 * @param secretKey
	 */
	public Rublon2Factor(String systemToken, String secretKey) {
		this(systemToken, secretKey, DEFAULT_API_SERVER);
	}
	

	/**
	 * Construct an instance with non-default API server URI.
	 * 
	 * @param systemToken
	 * @param secretKey
	 * @param apiServer Non-default API server URI for the development purposes.
	 */
	public Rublon2Factor(String systemToken, String secretKey, String apiServer) {
		super(systemToken, secretKey, apiServer);
		setServiceName(SERVICE_NAME);
	}


	
	/**
	 * Initializes the Rublon 2-factor authentication transaction
	 * and returns the URL address to redirect user's browser
	 * or NULL if user's account is not protected.
	 * 
	 * First, method checks the account's protection status in the Rublon server for current user.
	 * If user has protected this account, method returns the URL address.
	 * Redirect user's browser to this URL to start the Rublon authentication process.
	 * 
	 * If Rublon user has deleted his Rublon account or Rublon API is not available at this time,
	 * method returns null. If so, just bypass Rublon and sign in the user.
	 * 
	 * Notice: to use this method the configurations values (system token and secret key)
	 * must be provided to the constructor. If not, function will throw an exception.
	 * 
	 * @param callbackUrl Callback URL address.
	 * @param userId User's ID in local system.
	 * @param userEmail User's email address.
	 * @param consumerParams Additional transaction parameters.
	 * @return URL address to redirect or NULL if user is not protected.
	 * @throws APIException
	 * @throws RublonException
	 */
	public String auth(String callbackUrl, String userId, String userEmail, JSONObject consumerParams) throws APIException, RublonException {
		
		if (!isConfigured()) {
			throw new RublonException("Missing system token and secret key.");
		}
		
		String lang = getLang();
		if (lang != null) {
			consumerParams.put(RublonAuthParams.FIELD_LANG, lang);
		}
		
		try {
			BeginTransaction beginTransaction = new BeginTransaction(this, callbackUrl, userEmail, userId, consumerParams);
			beginTransaction.perform();
			return beginTransaction.getWebURI();
		} catch (APIException.UserNotFoundException e) {					
			// bypass Rublon
			return null;									
		} catch (RublonException e) {
			throw e;
		}
		
	}
	
	/**
	 * Initializes the Rublon 2-factor authentication transaction
	 * and returns the URL address to redirect user's browser
	 * or NULL if user's account is not protected.
	 * 
	 * First, method checks the account's protection status in the Rublon server for current user.
	 * If user has protected this account, method returns the URL address.
	 * Redirect user's browser to this URL to start the Rublon authentication process.
	 * 
	 * If Rublon user has deleted his Rublon account or Rublon API is not available at this time,
	 * method returns null. If so, just bypass Rublon and sign in the user.
	 * 
	 * Notice: to use this method the configurations values (system token and secret key)
	 * must be provided to the constructor. If not, function will throw an exception.
	 * 
	 * @param callbackUrl Callback URL address.
	 * @param userId User's ID in local system.
	 * @param userEmail User's email address.
	 * @return URL address to redirect or NULL if user is not protected.
	 * @throws APIException
	 * @throws RublonException
	 */
	public String auth(String callbackUrl, String userId, String userEmail) throws APIException, RublonException {
		return auth(callbackUrl, userId, userEmail, new JSONObject());
	}
	

	/**
	 * Authenticate user and perform an additional confirmation of the transaction.
	 * 
	 * This method requires user to use the Rublon mobile app
	 * (even if the Trusted Device is available)
	 * and confirm transaction to maintain higher security level.
	 * 
	 * For users which are using the email-2-factor method, the question
	 * will be displayed in the web browser after clicking the confirmation
	 * link sent to the user's email address.
	 * 
	 * The message passed in the $customMessage argument will be displayed
	 * in the confirmation dialog.
	 * 
	 * @param callbackUrl
	 * @param userId
	 * @param userEmail
	 * @param confirmMessage Message to display (max. 255 bytes).
	 * @param consumerParams Additional transaction parameters.
	 * @return URL to redirect or NULL if user is not protected.
	 * @throws APIException 
	 * @throws RublonException
	 */
	public String confirm(String callbackUrl, String userId, String userEmail, String confirmMessage, JSONObject consumerParams) throws APIException, RublonException {
		consumerParams.put(RublonAuthParams.FIELD_CONFIRM_MESSAGE, confirmMessage);
		String lang = getLang();
		if (lang != null && lang.length() > 0) {
			consumerParams.put(RublonAuthParams.FIELD_LANG, lang);
		}
		return auth(callbackUrl, userId, userEmail, consumerParams);
	}
	

	/**
	 * Authenticate user and perform an additional confirmation of the transaction.
	 * 
	 * This method requires user to use the Rublon mobile app
	 * (even if the Trusted Device is available)
	 * and confirm transaction to maintain higher security level.
	 * 
	 * For users which are using the email-2-factor method, the question
	 * will be displayed in the web browser after clicking the confirmation
	 * link sent to the user's email address.
	 * 
	 * The message passed in the $customMessage argument will be displayed
	 * in the confirmation dialog.
	 * 
	 * @param callbackUrl
	 * @param userId
	 * @param userEmail
	 * @param confirmMessage Message to display (max. 255 bytes).
	 * @return URL to redirect or NULL if user is not protected.
	 * @throws APIException 
	 * @throws RublonException
	 */
	public String confirm(String callbackUrl, String userId, String userEmail, String confirmMessage) throws APIException, RublonException {
		return confirm(callbackUrl, userId, userEmail, confirmMessage, new JSONObject());
	}
	
	
	/**
	 * Perform a confirmation of the transaction without user's action needed
	 * if the time buffer after previous confirmation has not been reached.
	 *
	 * If the amount of seconds after the previous transaction is less than
	 * given time buffer, Rublon will confirm the transaction without user's action.
	 * In other cases, this method will behave the same as the Rublon2Factor.confirm() method.
	 *
	 * @param callbackUrl
	 * @param userId User's local ID.
	 * @param userEmail User's email address.
	 * @param confirmMessage Message to display (max. 255 bytes).
	 * @param timeBuffer Amount of seconds from last confirmation.
	 * @param consumerParams Additional transaction parameters.
	 * @return URL to redirect or NULL if user is not protected.
	 * @throws APIException 
	 * @throws RublonException
	 */
	public String confirmWithBuffer(String callbackUrl, String userId, String userEmail, String confirmMessage, int timeBuffer, JSONObject consumerParams) throws APIException, RublonException {
		consumerParams.put(RublonAuthParams.FIELD_CONFIRM_TIME_BUFFER, timeBuffer);
		return confirm(callbackUrl, userId, userEmail, confirmMessage, consumerParams);
	}
	

	/**
	 * Perform a confirmation of the transaction without user's action needed
	 * if the time buffer after previous confirmation has not been reached.
	 *
	 * If the amount of seconds after the previous transaction is less than
	 * given time buffer, Rublon will confirm the transaction without user's action.
	 * In other cases, this method will behave the same as the Rublon2Factor.confirm() method.
	 *
	 * @param callbackUrl
	 * @param userId User's local ID.
	 * @param userEmail User's email address.
	 * @param confirmMessage Message to display (max. 255 bytes).
	 * @param timeBuffer Amount of seconds from last confirmation.
	 * @return URL to redirect or NULL if user is not protected.
	 * @throws APIException 
	 * @throws RublonException
	 */
	public String confirmWithBuffer(String callbackUrl, String userId, String userEmail, String confirmMessage, int timeBuffer) throws APIException, RublonException {
		return confirmWithBuffer(callbackUrl, userId, userEmail, confirmMessage, timeBuffer, new JSONObject());
	}
	
	
	/**
	 * Authenticate user and get user's credentials using one-time use access token.
	 *
	 * One-time use access token is a session identifier which will be deleted after first usage.
	 * This method can be called only once in authentication process.
	 *
	 * @param accessToken One-time use access token.
	 * @throws ConnectionException
	 * @throws APIException 
	 */
	public Credentials getCredentials(String accessToken) throws ConnectionException, APIException {
		Credentials credentials = new Credentials(this, accessToken);
		credentials.perform();
		return credentials;
	}

	
}
