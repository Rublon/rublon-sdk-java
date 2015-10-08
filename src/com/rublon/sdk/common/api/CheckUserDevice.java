package com.rublon.sdk.common.api;


import org.json.JSONObject;

import com.rublon.sdk.core.APIMethod;
import com.rublon.sdk.core.RublonAuthParams;
import com.rublon.sdk.twofactor.Rublon2Factor;

/**
 * API method to check the user's device status.
 */
public class CheckUserDevice extends APIMethod {

	/**
	 * URI path for the API request.
	 */
	protected static final String REQUEST_URI_PATH = "/api/v3/checkUserDevice";
	
	/**
	 * Field name for the device status.
	 */
	public static final String FIELD_DEVICE_STATUS = "deviceActive";
	
	/**
	 * Field name for the device ID.
	 */
	public static final String FIELD_DEVICE_ID = "deviceId";
	
	/**
	 * Device ID.
	 */
	protected int deviceId;
	
	/**
	 * Profile ID.
	 */
	protected int profileId;
	
	/**
	 * Construct the API method instance.
	 * 
	 * @param rublon Rublon instance.
	 * @param profileId Profile ID to check.
	 * @param deviceId Device ID to check.
	 */
	public CheckUserDevice(Rublon2Factor rublon, int profileId, int deviceId) {
		super(rublon);
		this.profileId = profileId;
		this.deviceId = deviceId;
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
				.put(RublonAuthParams.FIELD_PROFILE_ID, profileId)
				.put(FIELD_DEVICE_ID, deviceId);
	}
	
	
	/**
	 * Check the device's status.
	 */
	public boolean isDeviceActive() {
		return response.optBoolean(FIELD_DEVICE_STATUS, false);
	}
	
}
