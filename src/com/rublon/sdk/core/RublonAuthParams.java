package com.rublon.sdk.core;

import java.io.UnsupportedEncodingException;

import java.net.URLEncoder;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;

import com.rublon.sdk.core.RublonConsumer;
import com.rublon.sdk.core.message.RublonMessage;
import com.rublon.sdk.core.message.RublonSignature;

/**
 * Parameters wrapper of the Rublon authentication process.
 * <p>
 * This class is used to prepare the parameters for the authentication
 * process. This includes both the parameters used for the authentication
 * itself as well as any additional parameters that would be used by the
 * integrated website in the callback. An object of this class can also
 * be used to embed the authentication parameters in a Rublon button. 
 * 
 * @see RublonHTMLButton
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
	 * Consumer parameters store.
	 * <p>
	 * These optional parameters can be set by the integrated website.
	 * They will be signed with the Signature Wrapper (RublonSignatureWrapper class)
	 * using the website's secret key and can be retrieved in the callback via
	 * the getConsumerParams() method of the RublonResponse class. 
	 */
	protected JSONObject consumerParams = new JSONObject();
	
	/**
	 * Outer parameters (not documented).
	 */
	protected JSONObject outerParams = new JSONObject();
	
	/**
	 * URL of the origin window.
	 * <p>
	 * Note: the originUrl parameter is needed by JavaScript postMessage method
	 * in Rublon Code popup window. It is not signed by the Signature Wrapper
	 * and should NOT be utilized to perform any HTTP redirects because of phishing possibility.
	 */
	protected String originUrl = "http://rublon.com/";
	
	/**
	 * Action flag.
	 * <p>
	 * The action flag determines both the text description displayed in the
	 * QR code window and sometimes may impose a certain behavior on the
	 * authentication process.
	 */
	protected String actionFlag = null;

	/**
	 * Field name for access token parameter
	 */
	public static final String FIELD_ACCESS_TOKEN = "accessToken";
	
	/**
	 * Field name for the "requireProfileId" parameter
	 */
	public static final String FIELD_REQUIRE_PROFILE_ID = "requireProfileId";
	
	/**
	 * Field name for the "profileId" parameter
	 */
	public static final String FIELD_PROFILE_ID = "profileId";

	/**
	 * Field name for the "service" parameter
	 */
	public static final String FIELD_SERVICE = "service";
	
	/**
	 * Field name for the "systemToken" parameter
	 */
	public static final String FIELD_SYSTEM_TOKEN = "systemToken";
	
	/**
	 * Field name for language parameter
	 */
	public static final String FIELD_LANG = "lang";
	
	/**
	 * Field name for origin URL address parameter
	 */
	public static final String FIELD_ORIGIN_URL = "originUrl";

	/**
	 * Field name for the return URL parameter.
	 */
	public static final String FIELD_RETURN_URL = "returnUrl";
	
	/**
	 * Field name for consumer parameters
	 */
	public static final String FIELD_CONSUMER_PARAMS = "consumerParams";

	/**
	 * Field name for action parameter
	 */
	public static final String FIELD_ACTION = "action";

	/**
	 * Field name for action flag parameter.
	 */
	public static final String FIELD_ACTION_FLAG = "actionFlag";
	
	/**
	 * Field name for the callback URL parameter.
	 */
	public static final String FIELD_CALLBACK_URL = "callbackUrl";

	/**
	 * Field name for the local user ID parameter.
	 */
	public static final String FIELD_USER_ID = "userId";

	/**
	 * Field name for the local user email hash parameter.
	 */
	public static final String FIELD_USER_EMAIL_HASH = "userEmailHash";

	/**
	 * Field name for the local user email parameter.
	 */
	public static final String FIELD_USER_EMAIL = "userEmail";

	/**
	 * Field name for the logout listener boolean flag.
	 */
	public static final String FIELD_LOGOUT_LISTENER = "logoutListener";
	
	/**
	 * Field name for version parameter
	 */
	public static final String FIELD_VERSION = "version";

	/**
	 * Field name for version date parameter
	 */
	public static final String FIELD_VERSION_DATE = "versionDate";

	/**
	 * Field name to require Rublon to authenticate
	 * by mobile app only, not using Email two-factor.
	 */
	public static final String FIELD_FORCE_MOBILE_APP = "forceMobileApp";

	/**
	 * Field name to force ignoring the existing Trusted Devices
	 * during the authentication.
	 */
	public static final String FIELD_IGNORE_TRUSTED_DEVICES = "ignoreTrustedDevices";

	/**
	 * Field name to add a custom URI query parameter to the callback URL.
	 */
	public static final String FIELD_CUSTOM_URI_PARAM = "customURIParam";

	/**
	 * Field name to define a message for a transaction.
	 */
	public static final String FIELD_CONFIRM_MESSAGE = "confirmMessage";

	/**
	 * Field name to set the time buffer in seconds from previous confirmation
	 * which allow Rublon to confirm the custom transaction
	 * without user's action.
	 */
	public static final String FIELD_CONFIRM_TIME_BUFFER = "confirmTimeBuffer";

	/**
	 * URL path to authentication code
	 */
	private static final String URL_PATH_CODE = "/code/native/";


	

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
	 * Set defaults in constructor
	 * 
	 * @param rublon RublonService instance
	 * @param actionFlag Action flag
	 * @param consumerParams Consumer parameters
	 */
	protected void setDefaults(RublonConsumer rublon, String actionFlag, JSONObject consumerParams) {
		this.rublon = rublon;
		if (consumerParams != null) {
			this.setConsumerParams(consumerParams);
		}
		if (actionFlag != null) {
			this.setActionFlag(actionFlag);
			this.setConsumerParam(FIELD_ACTION, actionFlag);
		}
		String serviceName = rublon.getServiceName();
		if (serviceName != null) {
			this.setConsumerParam(RublonAuthParams.FIELD_SERVICE, serviceName);
		}
	}
	
	
	/**
	 * Get URL of the authentication request to perform simple HTTP redirection.
	 * <p>
	 * Returns a URL address that will start the Rublon
	 * authentication process if redirected to.
	 * 
	 * @return URL address
	 */
	public String getUrl() {
		String params;
		try {
			params = URLEncoder.encode(this.getUrlParamsString(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			params = "";
		}
		
		return this.getRublon().getAPIServer()
			+ RublonAuthParams.URL_PATH_CODE
			+ params;
	}
	
	
	
	/**
	 * Get parameters string to apply in the authentication URL address
	 * <p>
	 * Returns the authentication parameters as a base64-encoded JSON string
	 * that will be passed with the URL address to the Rublon code window.
	 * 
	 * @return Base64-encoded string
	 */
	protected String getUrlParamsString() {
		return Codec.base64_encode(
			Codec.json_encode(
				this.getUrlParams()
			)
		);
	}
	
	
	/**
	 * Get ready-made authentication parameters object to apply in the authentication URL address
	 * <p>
	 * Returns the authentication process parameters as an object
	 * (including the Signature Wrapper-signed consumer parameters)
	 * that will be passed with the URL address to the Rublon code window.
	 * 
	 * @return Parameters object
	 */
	protected JSONObject getUrlParams() {
		
		JSONObject consumerParams = this.getConsumerParams();
		JSONObject outerParams = this.getOuterParams();
		JSONObject params = new JSONObject();
		
		if ((consumerParams != null && consumerParams.keySet().size() > 0)
			|| (outerParams != null && outerParams.keySet().size() > 0)) {
			
			RublonMessage message = new RublonMessage();
			message.setBody(consumerParams);
			RublonSignature wrapper = new RublonSignature(this.getRublon().getSecretKey());
			wrapper.setMessage(message);
			
			params.put(RublonAuthParams.FIELD_CONSUMER_PARAMS, wrapper.getJSONObject());
		}
		
		return params;
		
	}
	
	/**
	 * Get the consumer parameters wrapper to apply in the Rublon button.
	 * <p>
	 * Returns the Signature Wrapper-signed consumer parameters
	 * to apply in the HTML wrapper of the Rublon button.
	 *
	 * @return JSONObject instance
	 */
	public RublonSignature getConsumerParamsWrapper() {
		
		JSONObject consumerParams = this.getConsumerParams();
		JSONObject outerParams = this.getOuterParams();
		
		if ((consumerParams != null && consumerParams.keySet().size() > 0)
			|| (outerParams != null && outerParams.keySet().size() > 0)) {
			RublonMessage message = new RublonMessage();
			message.setBody(consumerParams);
			RublonSignature wrapper = new RublonSignature(this.getRublon().getSecretKey());
			wrapper.setMessage(message);
			
			return wrapper;
			
		} else {
			return null;
		}
	}
	
	
	/**
	 * Get the consumer parameters string to apply in the Rublon button.
	 * <p>
	 * Returns the Signature Wrapped-signed consumer parameters
	 * as a JSON string.
	 * 
	 * @return String
	 */
	public String getConsumerParamsWrapperString() {
		return this.getConsumerParamsWrapper().toString();
	}
	
	
	
	/**
	 * Set consumer parameters.
	 * <p>
	 * Sets the consumer parameters using the given array.
	 *
	 * @param An object with the consumer parameters
	 * @return Self instance
	 */
	public RublonAuthParams setConsumerParams(JSONObject consumerParams) {
		if (consumerParams != null) {
			this.consumerParams = consumerParams;
		}
		return this;
	}
	
	/**
	 * Set single consumer parameter.
	 * <p>
	 * Allows to add a single consumer param to the consumer parameters object.
	 * 
	 * @param Parameter name
	 * @param Parameter value
	 * @return Self instance
	 */
	public RublonAuthParams setConsumerParam(String name, Object value) {
		this.consumerParams.put(name, value);
		return this;
	}
	

	/**
	 * Get consumer parameters.
	 * <p>
	 * Returns the consumer parameters object with the addition of actionFlag if it's set.
	 *
	 * @return JSONObject
	 */
	public JSONObject getConsumerParams() {
		
		JSONObject consumerParams = this.consumerParams;
		
		// Now set some default required parameters. 
		
		// Service name:
		String serviceName = this.getRublon().getServiceName();
		if (serviceName != null) {
			consumerParams.put(RublonAuthParams.FIELD_SERVICE, serviceName);
		}
		
		// Language code:
		String lang = this.getRublon().getLang();
		if (lang != null) {
			consumerParams.put(RublonAuthParams.FIELD_LANG, lang);
		}
		
		// Set action flag
		String actionFlag = this.getActionFlag();
		if (actionFlag != null) {
			consumerParams.put(RublonAuthParams.FIELD_ACTION_FLAG, actionFlag);
		}
		
		consumerParams.put(FIELD_SYSTEM_TOKEN, this.getRublon().getSystemToken());
		consumerParams.put(FIELD_VERSION,  this.getRublon().getVersionDate());
		
		return consumerParams;
		
	}
	
	
	/**
	 * Get single consumer parameter.
	 * 
	 * @param Parameter name
	 * @return Returns a single consumer parameter from the consumer parameters object or null if the requested parameter doesn't exist.
	 */
	public Object getConsumerParam(String name) {
		consumerParams = this.getConsumerParams();
		Object value = consumerParams.get(name);
		if (value != null) {
			return value;
		} else {
			return null;
		}
	}
	

	/**
	 * Set outer parameters (not documented).
	 *
	 * @param Parameters to be set.
	 * @return Self instance
	 */
	public RublonAuthParams setOuterParams(JSONObject params) {
		this.outerParams = params;
		return this;
	}
	
	/**
	 * Get outer parameters (not documented).
	 */
	public JSONObject getOuterParams() {
		return this.outerParams;
	}
	
	
	/**
	 * Set the URL of the origin window.
	 * <p>
	 * Note: the originUrl parameter is needed by JavaScript postMessage method
	 * in Rublon Code popup window. It is not signed by the Signature Wrapper
	 * and should NOT be utilize to perform any HTTP redirects because of phishing possibility.
	 * 
	 * @param The URL to be set as originUrl. 
	 * @return Self instance
	 */
	public RublonAuthParams setOriginUrl(String originUrl) {
		this.originUrl = originUrl;
		return this;
	}
	
	
	/**
	 * Get the URL of the origin window.
	 * <p>
	 * Returns the value of the originURL property.
	 * 
	 * @return String
	 */
	public String getOriginUrl() {
		return this.originUrl;
	}
	
	

	/**
	 * Set action flag of the process
	 * <p>
	 * Get available flags from RublonAuthParams::ACTION_FLAG_... constant.
	 *
	 * @param String actionFlag One of the actionFlag constants.
	 * @return RublonAuthParams
	 */
	public RublonAuthParams setActionFlag(String actionFlag) {
		this.actionFlag = actionFlag;
		return this;
	}
	
	
	/**
	 * Get action flag of the process.
	 * <p>
	 * Returns the value of the actionFlag property.
	 *
	 * @return String
	 */
	public String getActionFlag() {
		return this.actionFlag;
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
				for (@SuppressWarnings("unchecked")
				Iterator<String> a=obj.keys(); a.hasNext(); ) {
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
