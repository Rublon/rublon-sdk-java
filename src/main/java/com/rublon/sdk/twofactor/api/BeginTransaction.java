package com.rublon.sdk.twofactor.api;


import com.rublon.sdk.core.rest.RESTClient;
import com.rublon.sdk.twofactor.Rublon;
import org.json.JSONObject;

import com.rublon.sdk.core.APIMethod;
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
	 * Additional transaction parameters.
	 */
	protected JSONObject params;

	/**
	 * User's name address.
	 */
	protected String userName;

	/**
	 * Construct the API method instance.
	 * 
	 * @param rublon Rublon instance.
	 * @param callbackUrl URL of the callback method.
	 * @param userName User's name.
	 * @param userEmail User's email address.
	 * @param params Additional transaction parameters.
	 * @param restClient Rest client
	 */
	public BeginTransaction(Rublon rublon, String callbackUrl, String userName,
							String userEmail, JSONObject params, RESTClient restClient) {
		super(rublon, restClient);
		this.callbackUrl = callbackUrl;
		this.userName = userName;
		this.userEmail = userEmail;
		this.params = params;
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
			super.getParams(),
			new JSONObject() {{
				put(RublonAuthParams.FIELD_CALLBACK_URL, callbackUrl);
				if (userEmail != null && !userEmail.isEmpty()) {
					put(RublonAuthParams.FIELD_USER_EMAIL, userEmail.toLowerCase());
				}
				put(RublonAuthParams.FIELD_USER_NAME, userName);
				if (params != null) {
					put(RublonAuthParams.FIELD_PARAMS, params);
				}
			}}
		});
	}

}
