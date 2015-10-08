package com.rublon.sdk.twofactor.api;


import org.json.JSONObject;

import com.rublon.sdk.core.APIMethod;
import com.rublon.sdk.core.Codec;
import com.rublon.sdk.core.RublonAuthParams;
import com.rublon.sdk.twofactor.Rublon2Factor;

/**
 * API method to begin the 2-factor authentication transaction.
 */
public class BeginTransaction extends APIMethod {


	/**
	 * Field name for the web URI in the API response.
	 */
	public static final String FIELD_WEB_URI = "webURI";
	
	/**
	 * API request URI path.
	 */
	protected static final String REQUEST_URI_PATH = "/api/v3/beginTransaction";

	/**
	 * Callback URL.
	 */
	protected String callbackUrl;
	
	/**
	 * User's email address.
	 */
	protected String userEmail;
	
	/**
	 * User's local ID.
	 */
	protected String userId;
	
	/**
	 * Additional transaction parameters.
	 */
	protected JSONObject consumerParams;
	

	/**
	 * Construct the API method instance.
	 * 
	 * @param rublon Rublon instance.
	 * @param callbackUrl URL of the callback method.
	 * @param userEmail User's email address.
	 * @param userId User's local ID.
	 * @param consumerParams Additional transaction parameters.
	 */
	public BeginTransaction(Rublon2Factor rublon, String callbackUrl,
			String userEmail, String userId, JSONObject consumerParams) {
		super(rublon);
		this.callbackUrl = callbackUrl;
		this.userEmail = userEmail;
		this.userId = userId;
		this.consumerParams = consumerParams;
	}
	

	/**
	 * Construct the API method instance.
	 * 
	 * @param rublon Rublon instance.
	 * @param callbackUrl URL of the callback method.
	 * @param userEmail User's email address.
	 * @param userId User's local ID.
	 */
	public BeginTransaction(Rublon2Factor rublon, String callbackUrl,
			String userEmail, String userId) {
		this(rublon, callbackUrl, userEmail, userId, new JSONObject());
	}
	

	/**
	 * Get the web URI from the API response and redirect the user's web browser.
	 */
	public String getWebURI() {
		return responseResult.optString(FIELD_WEB_URI, null);
	}
	

	/**
	 * Get the API request URL.
	 */
	protected String getUrl() {
		return getRublon().getAPIServer() + REQUEST_URI_PATH;
	}
	

	/**
	 * Get the API request parameters.
	 */
	protected JSONObject getParams() {
		return RublonAuthParams.mergeJSONObjects(new JSONObject[] {
			consumerParams,
			super.getParams(),
			new JSONObject() {{
				put(RublonAuthParams.FIELD_USER_ID, userId);
				put(RublonAuthParams.FIELD_USER_EMAIL_HASH, Codec.sha256(userEmail.toLowerCase()));
				put(RublonAuthParams.FIELD_CALLBACK_URL, callbackUrl);
				if (!consumerParams.optBoolean(RublonAuthParams.FIELD_FORCE_MOBILE_APP, false)) {
					put(RublonAuthParams.FIELD_USER_EMAIL, userEmail.toLowerCase());
				}
			}}
		});
	}

}
