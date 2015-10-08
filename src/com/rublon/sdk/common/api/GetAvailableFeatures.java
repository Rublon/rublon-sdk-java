package com.rublon.sdk.common.api;


import org.json.JSONObject;

import com.rublon.sdk.core.APIMethod;
import com.rublon.sdk.twofactor.Rublon2Factor;

/**
 * API method to check the available features for given system token.
 */
public class GetAvailableFeatures extends APIMethod {

	/**
	 * URI path for the API request.
	 */
	protected static final String REQUEST_URI_PATH = "/api/v3/getAvailableFeatures";
	
	/**
	 * Field name for the features list.
	 */
	public static final String FIELD_FEATURES = "features";
	
	/**
	 * Force mobile app feature name.
	 */
	public static final String FEATURE_FORCE_MOBILE_APP = "forceMobileApp";
	
	/**
	 * Ignore trusted device feature name.
	 */
	public static final String FEATURE_IGNORE_TRUSTED_DEVICE = "ignoreTrustedDevice";
	
	/**
	 * Opration confirmation feature name.
	 */
	public static final String FEATURE_OPERATION_CONFIRMATION = "operationConfirmation";
	
	/**
	 * Buffered confirmation feature name.
	 */
	public static final String FEATURE_BUFFERED_CONFIRMATION = "bufferedAutoConfirmation";
	
	/**
	 * Identity providing feature name.
	 */
	public static final String FEATURE_IDENTITY_PROVIDING = "accessControlManager";
	
	/**
	 * Remote logoute feature name.
	 */
	public static final String FEATURE_REMOTE_LOGOUT = "remoteLogout";
	
	
	/**
	 * Construct the API method instance.
	 */
	public GetAvailableFeatures(Rublon2Factor rublon) {
		super(rublon);
	}
	

	/**
	 * Get the API request URL.
	 */
	protected String getUrl() {
		return rublon.getAPIServer() + REQUEST_URI_PATH;
	}
	

	/**
	 * Get the features list from the API response.
	 */
	public JSONObject getFeatures() {
		return responseResult.optJSONObject(FIELD_FEATURES);
	}
	
}
