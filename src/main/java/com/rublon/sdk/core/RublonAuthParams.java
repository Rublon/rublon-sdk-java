package com.rublon.sdk.core;

import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Parameters wrapper of the Rublon authentication process.
 * <p>
 * This class is used to prepare the parameters for the authentication
 * process. This includes both the parameters used for the authentication
 * itself as well as any additional parameters that would be used by the
 * integrated website in the callback.
 *
 * @author Rublon Developers
 */
public class RublonAuthParams {

	/**
	 * Rublon service instance.
	 * <p>
	 * An istance of the RublonService class. Necessary for
	 * the class to work.
	 */
	protected RublonConsumer rublon = null;

	/**
	 * Field name for access token parameter
	 */
	public static final String FIELD_ACCESS_TOKEN = "accessToken";
	
	/**
	 * Field name for the "systemToken" parameter
	 */
	public static final String FIELD_SYSTEM_TOKEN = "systemToken";

	/**
	 * Field name for the application version.
	 */
	public static final String FIELD_APP_VER = "appVer";

	/**
	 * Field name for the SDK version.
	 */
	public static final String FIELD_SDK_VER = "sdkVer";

	/**
	 * Field name for optional params object.
	 */
	public static final String FIELD_PARAMS = "params";

	/**
	 * Field name for language parameter
	 */
	public static final String FIELD_LANG = "lang";
	
	/**
	 * Field name for the callback URL parameter.
	 */
	public static final String FIELD_CALLBACK_URL = "callbackUrl";

	/**
	 * Field name for the local user email parameter.
	 */
	public static final String FIELD_USER_EMAIL = "userEmail";

	/**
	 * Field name for the local user name parameter.
	 */
	public static final String FIELD_USER_NAME = "username";

	/**
	 * Initialize object with RublonService instance.
	 * <p>
	 * A RublonService class instance is required for
	 * the object to work.
	 *
	 * @param rublon An instance of the RublonService class
	 */
	public RublonAuthParams(RublonConsumer rublon) {
		this.rublon = rublon;
	}

	/**
	 * Get Rublon Consumer instance.
	 * <p>
	 * Returns the RublonConsumer class instance used in the creation
	 * of this class' RublonService class instance.
	 *
	 * @return RublonConsumer
	 */
	public RublonConsumer getRublon() {
		return rublon;
	}
	
	
	/**
	 * Merge many JSON objects.
	 * @param arr Array of JSON objects.
	 */
	static public JSONObject mergeJSONObjects(JSONObject[] arr) {
		if (arr.length == 0) {
			return new JSONObject();
		} else {
			JSONObject result = arr[0];
			for (int i=1; i<arr.length; i++) {
				JSONObject obj = arr[i];
				for (Iterator<String> a=obj.keys(); a.hasNext(); ) {
					String key = a.next();
					if (result.has(key)) {
						Object val1 = result.get(key);
						Object val2 = obj.get(key);
						if (val1 instanceof JSONObject && val2 instanceof JSONObject) {
							result.put(key, mergeJSONObjects(new JSONObject[] {
									(JSONObject)val1,
									(JSONObject)val2
							}));
						}
						else if (val1 instanceof JSONArray && val2 instanceof JSONArray) {
							result.accumulate(key, val2);
						} else {
							result.put(key, obj.get(key));
						}
					} else {
						result.put(key, obj.get(key));
					}
				}
			}
			return result;
		}
	}
}
