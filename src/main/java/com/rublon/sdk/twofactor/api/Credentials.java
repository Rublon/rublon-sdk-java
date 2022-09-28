package com.rublon.sdk.twofactor.api;


import com.rublon.sdk.core.exception.RublonException;
import com.rublon.sdk.core.rest.RESTClient;
import org.json.JSONObject;

import com.rublon.sdk.core.APIMethod;
import com.rublon.sdk.core.RublonAuthParams;
import com.rublon.sdk.twofactor.Rublon;

/**
 * API method to get the authentication credentials for the 2-factor service.
 */
public class Credentials extends APIMethod {

	/**
	 * API request URI path.
	 */
	protected static final String REQUEST_URI_PATH = "/api/transaction/credentials";
	
	/**
	 * Field name for the user's device ID.
	 */
	public static final String FIELD_DEVICE_ID = "deviceId";
	
	/**
	 * Field name for the confirmation result.
	 */
	public static final String FIELD_CONFIRM_RESULT = "answer";
	
	/**
	 * Access token.
	 */
	protected String accessToken;

	/**
	 * Construct the API method instance.
	 * 
	 * @param rublon Rublon instance.
	 * @param accessToken Access token.
	 */
	public Credentials(Rublon rublon, String accessToken, RESTClient client) throws RublonException {
		super(rublon, client);

		if (accessToken == null || !accessToken.matches("[a-z0-9]{60}")) {
			throw new RublonException("Invalid access token.");
		}
		this.accessToken = accessToken;
	}
	

	/**
	 * Get the API request URL.
	 */
	protected String getUrl() {
		return rublon.getAPIServer() + REQUEST_URI_PATH;
	}
	

	/**
	 * Get the API request parameters.
	 */
	protected JSONObject getParams() {
		return super.getParams()
				.put(RublonAuthParams.FIELD_ACCESS_TOKEN, accessToken);
	}

	/**
	 * Get the user's device ID from the response.
	 */
	public String getDeviceId() {
		return responseResult.optString(FIELD_DEVICE_ID, null);
	}
	
	/**
	 * Get the confirmation result.
	 */
	public String getConfirmResult() {
		return responseResult.optString(FIELD_CONFIRM_RESULT, null);
	}

	/**
	 * Get the user's name from the response.
	 */
	public String getUserName() {
		return responseResult.optString(RublonAuthParams.FIELD_USER_NAME, null);
	}

}
