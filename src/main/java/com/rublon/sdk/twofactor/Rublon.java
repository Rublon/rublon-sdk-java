package com.rublon.sdk.twofactor;


import com.rublon.sdk.core.RublonAuthParams;
import com.rublon.sdk.core.RublonConsumer;
import com.rublon.sdk.core.exception.APIException;
import com.rublon.sdk.core.exception.ConnectionException;
import com.rublon.sdk.core.exception.RublonException;
import com.rublon.sdk.core.rest.RESTClient;
import com.rublon.sdk.twofactor.api.BeginTransaction;
import com.rublon.sdk.twofactor.api.Credentials;
import com.rublon.sdk.twofactor.api.CheckApplication;
import org.json.JSONObject;

/**
 * Rublon 2-factor service provider class.
 * 
 * Create a subclass to override the specific methods.
 */
public class Rublon extends RublonConsumer {

	private final RESTClient restClient;


	/**
	 * Construct an instance.
	 * 
	 * @param systemToken
	 * @param secretKey
	 */
	public Rublon(String systemToken, String secretKey) {
		this(systemToken, secretKey, DEFAULT_API_SERVER, null);
	}
	

	/**
	 * Construct an instance with non-default API server URI.
	 * 
	 * @param systemToken
	 * @param secretKey
	 * @param apiServer Non-default API server URI for the development purposes.
	 */
	public Rublon(String systemToken, String secretKey, String apiServer) {
		this(systemToken, secretKey, apiServer, null);
	}

	/**
	 * Construct an instance with non-default API server URI.
	 *
	 * @param systemToken
	 * @param secretKey
	 * @param apiServer Non-default API server URI for the development purposes.
	 */
	public Rublon(String systemToken, String secretKey, String apiServer, RESTClient client) {
		super(systemToken, secretKey, apiServer);
		if (client != null){
			this.restClient = client;
		}else{
			this.restClient = new RESTClient(this);
		}
	}

	/**
	 * Validate Rublon configuration
	 *
	 * @param appVer
	 * @throws APIException
	 * @throws RublonException
	 */
	public void checkApplication(String appVer) throws APIException, RublonException {

		if (!isConfigured()) {
			throw new RublonException("Missing system token and secret key.");
		}

		try {
			CheckApplication checkApplication = new CheckApplication(this, restClient, appVer);
			checkApplication.perform();
		} catch (RublonException e) {
			throw e;
		}

	}

	/**
	 * Validate Rublon configuration
	 *
	 * @param appVer
	 * @param params
	 * @throws APIException
	 * @throws RublonException
	 */
	public void checkApplication(String appVer, JSONObject params) throws APIException, RublonException {

		if (!isConfigured()) {
			throw new RublonException("Missing system token and secret key.");
		}

		try {
			CheckApplication checkApplication = new CheckApplication(this, restClient, appVer, params);
			checkApplication.perform();
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
	 * @param userName User's name.
	 * @param userEmail User's email address.
	 * @param params Additional transaction parameters.
	 * @return URL address to redirect or NULL if user is not protected.
	 * @throws APIException
	 * @throws RublonException
	 */
	public String auth(String callbackUrl, String userName, String userEmail, JSONObject params) throws APIException, RublonException {
		
		if (!isConfigured()) {
			throw new RublonException("Missing system token and secret key.");
		}
		
		String lang = getLang();
		if (lang != null) {
			params.put(RublonAuthParams.FIELD_LANG, lang);
		}
		
		try {
			BeginTransaction beginTransaction = new BeginTransaction(this, callbackUrl, userName, userEmail, params, restClient);
			beginTransaction.perform();
			return beginTransaction.getWebURI();
		} catch (RublonException e) {
			throw e;
		}
		
	}

	/**
	 *
	 * @param callbackUrl Callback URL address.
	 * @param userName User's name.
	 * @param userEmail User's email address.
	 * @return URL address to redirect or NULL if user is not protected.
	 * @throws APIException
	 * @throws RublonException
	 */
	public String auth(String callbackUrl, String userName, String userEmail) throws APIException, RublonException {
		return auth(callbackUrl, userName, userEmail, new JSONObject());
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
	public Credentials getCredentials(String accessToken) throws RublonException {
		Credentials credentials = new Credentials(this, accessToken, restClient);
		credentials.perform();
		return credentials;
	}
}
