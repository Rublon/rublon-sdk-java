package com.rublon.sdk.login.api;

import org.json.JSONObject;

import com.rublon.sdk.core.APIMethod;
import com.rublon.sdk.core.RublonAuthParams;
import com.rublon.sdk.login.RublonLogin;

/**
 * API method to begin the native login authentication transaction.
 */
public class BeginTransaction extends APIMethod {

	/**
	 * Field name for the web URI in the API response.
	 */
	public static final String FIELD_WEB_URI = "webURI";
	
	/**
	 * API request URI path.
	 */
	protected static final String REQUEST_URI_PATH = "/api/v3/beginLoginTransaction";

	/**
	 * Callback URL.
	 */
	protected String callbackUrl;
	
	/**
	 * Additional transaction parameters.
	 */
	protected JSONObject consumerParams;

	/**
	 * Construct the API method instance.
	 * 
	 * @param rublon Rublon instance.
	 * @param callbackUrl URL of the callback method.
	 * @param consumerParams Additional transaction parameters.
	 */
	public BeginTransaction(RublonLogin rublon, String callbackUrl,
			JSONObject consumerParams) {
		super(rublon);
		this.callbackUrl = callbackUrl;
		this.consumerParams = consumerParams;
	}

	/**
	 * Construct the API method instance.
	 * 
	 * @param rublon Rublon instance.
	 * @param callbackUrl URL of the callback method.
	 */
	public BeginTransaction(RublonLogin rublon, String callbackUrl) {
		this(rublon, callbackUrl, new JSONObject());
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
		return rublon.getAPIServer() + REQUEST_URI_PATH;
	}

	/**
	 * Get the API request parameters.
	 */
	protected JSONObject getParams() {
		return RublonAuthParams.mergeJSONObjects(new JSONObject[] {
			consumerParams,
			super.getParams(),
			new JSONObject() {{
				put(RublonAuthParams.FIELD_CALLBACK_URL, callbackUrl);
			}}
		});
	}

}
