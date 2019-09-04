package com.rublon.sdk.core;

import org.json.JSONObject;

import com.rublon.sdk.core.message.RublonSignature;
import com.rublon.sdk.core.rest.RESTClient;

/**
 * Remote Logout handler abstract class.
 * <p>
 * If you want to implement the Remote Logout create a subclass.
 */
abstract public class RemoteLogoutHandler {
	
	/**
	 * Access token field on the remote logout action.
	 */
	protected static final String FIELD_LOGOUT_ACCESS_TOKEN = "accessToken";
	
	/**
	 * User ID field on the remote logout action.
	 */
	protected static final String FIELD_LOGOUT_APP_USER_ID = "appUserId";

	/**
	 * Device ID field on the remote logout action.
	 */
	protected static final String FIELD_LOGOUT_DEVICE_ID = "deviceId";
	
	/**
	 * Message field on the remote logout response.
	 */
	protected static final String RESPONSE_FIELD_MSG = "msg";

	/**
	 * Field field on the remote logout response.
	 */
	protected static final String RESPONSE_FIELD_FIELD = "field";
	
	/**
	 * Rublon instance.
	 */
	RublonConsumer rublon;

	/**
	 * Construct the instance.
	 * @param rublon
	 */
	public RemoteLogoutHandler(RublonConsumer rublon) {
		this.rublon = rublon;
	}
	
	
	/**
	 * Handle remote logout request: parse input and call logout for given user.
	 */
	public void handle() {
		
		JSONObject response = new JSONObject();
		String input = getHTTPRequestPOSTBody();
		if (input != null && input.length() > 0) {
			
			JSONObject message = new JSONObject(input);
			
			if (!RublonSignature.verifyData(input, getRublon().getSecretKey(), getHTTPRequestHeader(RESTClient.HEADER_NAME_SIGNATURE))) {
				response.put(APIMethod.FIELD_STATUS, APIMethod.STATUS_ERROR);
				response.put(RESPONSE_FIELD_MSG, "Invalid signature.");
				
			} else {
				
				String[] requiredFields = new String[] { FIELD_LOGOUT_ACCESS_TOKEN, FIELD_LOGOUT_APP_USER_ID, FIELD_LOGOUT_DEVICE_ID };
				for (int i=0; i<requiredFields.length; i++) {
					if (message.optString(requiredFields[i], "").length() == 0) {
						response.put(APIMethod.FIELD_STATUS, APIMethod.STATUS_ERROR);
						response.put(RESPONSE_FIELD_MSG, "Missing field.");
						response.put(RESPONSE_FIELD_FIELD, requiredFields[i]);
						setHTTPResponseBody(response.toString());
						return;
					}
				}
				
				// All OK
				logoutUser(message.optString(FIELD_LOGOUT_APP_USER_ID), message.optInt(FIELD_LOGOUT_DEVICE_ID));
				response.put(APIMethod.FIELD_STATUS, APIMethod.STATUS_OK);
				response.put(RESPONSE_FIELD_MSG, "Success.");
				
			}
			
		} else {
			response.put(APIMethod.FIELD_STATUS, APIMethod.STATUS_ERROR);
			response.put(RESPONSE_FIELD_MSG, "Empty POST body input.");
		}
		
		setHTTPResponseBody(response.toString());
		
	}
	
	
	/**
	 * Get Rublon instance.
	 */
	public RublonConsumer getRublon() {
		return rublon;
	}
	
	
	/**
	 * Get POST body for current HTTP request.
	 */
	abstract protected String getHTTPRequestPOSTBody();

	/**
	 * Get HTTP request first header with the given name.
	 */
	abstract protected String getHTTPRequestHeader(String name);
	

	/**
	 * Set HTTP response body.
	 */
	abstract protected void setHTTPResponseBody(String body);
	
	
	/**
	 * Handle logout in the local system: logout user by given user's ID for given deviceId.
	 */
	abstract protected void logoutUser(String appUserId, int deviceId);
	

}
