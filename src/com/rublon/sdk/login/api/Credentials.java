package com.rublon.sdk.login.api;


import org.json.JSONArray;
import org.json.JSONObject;

import com.rublon.sdk.core.APIMethod;
import com.rublon.sdk.core.RublonAuthParams;
import com.rublon.sdk.login.RublonLogin;

/**
 * API method to get the authentication credentials for the native login service.
 */
public class Credentials extends APIMethod {

	/**
	 * API request URI path.
	 */
	protected static final String REQUEST_URI_PATH = "/api/v3/loginCredentials";
	
	/**
	 * Field name for the device ID.
	 */
	public static final String FIELD_DEVICE_ID = "deviceId";
	
	/**
	 * Field name for the Rublon user"s email.
	 */
	public static final String FIELD_EMAIL_HASH_LIST = "emailHashList";
	
	/**
	 * Field name for the UID in the emails hash list.
	 */
	public static final String FIELD_EMAIL_HASH_LIST_UID = "uid";
	
	/**
	 * Field name for the hash string in the email hash list.
	 */
	public static final String FIELD_EMAIL_HASH_LIST_HASH = "hash";


	
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
	public Credentials(RublonLogin rublon, String accessToken) {
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
	 * Get the user's email address hash from the response.
	 */
	public JSONArray getUserEmailHashEntities() {
		return responseResult.optJSONArray(FIELD_EMAIL_HASH_LIST);
	}
	

	/**
	 * Get the Rublon user's emails hashes simple string array.
	 */
	public String[] getUserEmailHashArray() {
		JSONArray list = getUserEmailHashEntities();
		String[] result = new String[list.length()];
		for (int i=0; i<list.length(); i++) {
			result[i] = list.getJSONObject(i).getString(FIELD_EMAIL_HASH_LIST_HASH);
		}
		
		return result;
	}
	
	

}
