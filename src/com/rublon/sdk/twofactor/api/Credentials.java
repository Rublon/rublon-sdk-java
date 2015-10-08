package com.rublon.sdk.twofactor.api;


import org.json.JSONObject;

import com.rublon.sdk.core.APIMethod;
import com.rublon.sdk.core.RublonAuthParams;
import com.rublon.sdk.twofactor.Rublon2Factor;

/**
 * API method to get the authentication credentials for the 2-factor service.
 */
public class Credentials extends APIMethod {

	/**
	 * API request URI path.
	 */
	protected static final String REQUEST_URI_PATH = "/api/v3/credentials";
	
	/**
	 * Field name for the user's device ID.
	 */
	public static final String FIELD_DEVICE_ID = "deviceId";
	
	/**
	 * Field name for the confirmation result.
	 */
	public static final String FIELD_CONFIRM_RESULT = "answer";
	
	/**
	 * Positive confirmation result value.
	 */
	public static final String CONFIRM_RESULT_YES = "true";

	/**
	 * Nagative confirmation result value.
	 */
	public static final String CONFIRM_RESULT_NO = "false";
	
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
	public Credentials(Rublon2Factor rublon, String accessToken) {
		super(rublon);
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
	 * Get the user's local ID from the response.
	 */
	public String getUserId() {
		return responseResult.optString(RublonAuthParams.FIELD_USER_ID, null);
	}


	/**
	 * Get the user's profile ID from the response.
	 */
	public String getProfileId() {
		return responseResult.optString(RublonAuthParams.FIELD_PROFILE_ID, null);
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

}
