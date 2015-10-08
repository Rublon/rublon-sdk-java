package com.rublon.sdk.login;


import org.json.JSONObject;



import com.rublon.sdk.core.RublonAuthParams;
import com.rublon.sdk.core.RublonConsumer;
import com.rublon.sdk.core.exception.APIException;
import com.rublon.sdk.core.exception.ConnectionException;
import com.rublon.sdk.core.exception.RublonException;
import com.rublon.sdk.login.api.BeginTransaction;
import com.rublon.sdk.login.api.Credentials;

/**
 * Rublon native login service provider class.
 * 
 * Create a subclass to override the specific methods.
 */
public class RublonLogin extends RublonConsumer {
	
	/**
	 * Service name.
	 */
	private static final String SERVICE_NAME = "native";

	/**
	 * Construct an instance.
	 * 
	 * @param systemToken
	 * @param secretKey
	 */
	public RublonLogin(String systemToken, String secretKey) {
		this(systemToken, secretKey, DEFAULT_API_SERVER);
		setServiceName(SERVICE_NAME);
	}
	

	/**
	 * Construct an instance with non-default API server URI.
	 * 
	 * @param systemToken
	 * @param secretKey
	 * @param apiServer Non-default API server URI for the development purposes.
	 */
	public RublonLogin(String systemToken, String secretKey, String apiServer) {
		super(systemToken, secretKey, apiServer);
	}


	/**
	 * Initializes the Rublon login authentication transaction
	 * and returns the URL address to redirect user's browser.
	 * 
	 * Notice: to use this method the configurations values (system token and secret key)
	 * must be provided to the constructor. If not, function will throw an exception.
	 * 
	 * @param callbackUrl Callback URL address.
	 * @param consumerParams Additional transaction parameters.
	 * @return URL address to redirect.
	 * @throws APIException
	 * @throws RublonException
	 */
	public String auth(String callbackUrl, JSONObject consumerParams) throws APIException, RublonException {
		
		if (!isConfigured()) {
			throw new RublonException("Missing system token and secret key.");
		}
		
		String lang = getLang();
		if (lang != null) {
			consumerParams.put(RublonAuthParams.FIELD_LANG, lang);
		}
		
		try {
			BeginTransaction beginTransaction = new BeginTransaction(this, callbackUrl, consumerParams);
			beginTransaction.perform();
			return beginTransaction.getWebURI();
		} catch (RublonException e) {
			throw e;
		}
		
	}
	

	/**
	 * Initializes the Rublon login authentication transaction
	 * and returns the URL address to redirect user's browser.
	 * 
	 * Notice: to use this method the configurations values (system token and secret key)
	 * must be provided to the constructor. If not, function will throw an exception.
	 * 
	 * @param callbackUrl Callback URL address.
	 * @return URL address to redirect.
	 * @throws APIException
	 * @throws RublonException
	 */
	public String auth(String callbackUrl) throws APIException, RublonException {
		return auth(callbackUrl, new JSONObject());
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
