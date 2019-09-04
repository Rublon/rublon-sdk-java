package com.rublon.sdk.twofactor.api;


import com.rublon.sdk.twofactor.Rublon;
import org.json.JSONObject;

import com.rublon.sdk.core.APIMethod;
import com.rublon.sdk.core.Codec;
import com.rublon.sdk.core.RublonAuthParams;

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
	protected static final String REQUEST_URI_PATH = "/api/transaction/init";

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
	protected String appUserId;
	
	/**
	 * Additional transaction parameters.
	 */
	protected JSONObject consumerParams;

	/**
	 * Is passwordless login
	 */
	protected boolean isPasswordless;
	

	/**
	 * Construct the API method instance.
	 * 
	 * @param rublon Rublon instance.
	 * @param callbackUrl URL of the callback method.
	 * @param userEmail User's email address.
	 * @param appUserId User's local ID.
	 * @param consumerParams Additional transaction parameters.
	 * @param isPasswordless Is passwordless login.
	 */
	public BeginTransaction(Rublon rublon, String callbackUrl,
                            String userEmail, String appUserId, JSONObject consumerParams, boolean isPasswordless) {
		super(rublon);
		this.callbackUrl = callbackUrl;
		this.userEmail = userEmail;
		this.appUserId = appUserId;
		this.consumerParams = consumerParams;
		this.isPasswordless = isPasswordless;
	}
	

	/**
	 * Construct the API method instance.
	 * 
	 * @param rublon Rublon instance.
	 * @param callbackUrl URL of the callback method.
	 * @param userEmail User's email address.
	 * @param appUserId User's local ID.
	 */
	public BeginTransaction(Rublon rublon, String callbackUrl,
                            String userEmail, String appUserId) {
		this(rublon, callbackUrl, userEmail, appUserId, new JSONObject(), false);
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
		JSONObject consumerParamJson = new JSONObject();
		consumerParamJson.put("consumerParams", consumerParams);
		return RublonAuthParams.mergeJSONObjects(new JSONObject[] {
			consumerParamJson,
			super.getParams(),
			new JSONObject() {{
				put(RublonAuthParams.FIELD_APP_USER_ID, appUserId);
				put(RublonAuthParams.FIELD_USER_EMAIL_HASH, Codec.sha256(userEmail.toLowerCase()));
				put(RublonAuthParams.FIELD_CALLBACK_URL, callbackUrl);
				put(RublonAuthParams.FIELD_USER_EMAIL, userEmail.toLowerCase());
				put(RublonAuthParams.FIELD_PASSWORDLESS, isPasswordless);
			}}
		});
	}

}
