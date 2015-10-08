package com.rublon.sdk.common.html;


import org.json.JSONObject;

import com.rublon.sdk.core.Codec;
import com.rublon.sdk.core.RublonAuthParams;
import com.rublon.sdk.core.RublonConsumer;
import com.rublon.sdk.core.message.RublonMessage;
import com.rublon.sdk.core.message.RublonSignature;

/**
 * Class for generating the HTML script tag that embeds consumer's JavaScript library.
 * 
 * The so-called "consumer script" is an individualized JavaScript library
 * that allows the website to use Rublon JavaScript elements - usually
 * the Rublon GUI. The library searches Rublon button HTML containers
 * in the website's DOM tree and fills them with proper content.
 */
public class ConsumerScript {
	
	/**
	 * Consumer script URL (self explanatory).
	 */
	protected String URL_CONSUMER_SCRIPT = "/native/consumer_script_2factor";
	
	
	/**
	 * Rublon GUI instance.
	 */
	protected RublonGUI gui;

	/**
	 * Field name for version date parameter.
	 */
	public static final String FIELD_VERSION_DATE = "version";
	
	
	/**
	 * Initialize object with RublonGUI instance.
	 */
	public ConsumerScript(RublonGUI gui) {
		this.gui = gui;
	}
	
	/**
	 * Generate a HTML code of this object.
	 * <p>
	 * Returns a HTML script tag that will load the consumer
	 * script from the Rublon servers.
	 * 
	 * @return String
	 */
	public String toString() {
		return "<script type=\"text/javascript\" src=\""
				+ this.getConsumerScriptURL()
				+"?t="+ (long)Math.floor(Math.random()*9999998.0)
				+"\"></script>";
	}
	
	
	/**
	 * Get the RublonGUI instance.
	 */
	public RublonGUI getGUI() {
		return gui;
	}
	
	/**
	 * Get the Rublon instance.
	 */
	public RublonConsumer getRublon() {
		return getGUI().getRublon();
	}
	
	
	/**
	 * Get consumer's script URL address.
	 * <p>
	 * Returns the URL address of the consumer script on
	 * the Rublon servers.
	 *
	 * @return String
	 */
	public String getConsumerScriptURL() {
		
		String paramsString = Codec.url_encode(
			Codec.base64_encode(
				getParamsWrapper()
			)
		);
		
		return this.getRublon().getAPIServer()
			+ this.URL_CONSUMER_SCRIPT +'/'
			+ paramsString +'/'
			+ (long)Math.floor(Math.random()*99999998.0);
	
	}
	
	/**
	 * Get the consumer's script params wrapper string.
	 */
	protected String getParamsWrapper() {
		if (getRublon().isConfigured()) {
			RublonMessage message = new RublonMessage();
			message.setBody(getParams());
			RublonSignature sign = new RublonSignature(getRublon().getSecretKey());
			sign.setMessage(message);
			return sign.toString();
		} else {
			return getParams().toString();
		}
	}
	
	
	/**
	 * Get the parameters which will be send to the consumer script.
	 */
	protected JSONObject getParams() {
		JSONObject params = new JSONObject();
		params.put(RublonAuthParams.FIELD_ORIGIN_URL, getRublon().getCurrentUrl());
		params.put(RublonAuthParams.FIELD_SYSTEM_TOKEN, getRublon().getSystemToken());
		params.put(ConsumerScript.FIELD_VERSION_DATE, this.getVersionDate());
		params.put(RublonAuthParams.FIELD_SERVICE, getRublon().getServiceName());
		
		String lang = getRublon().getLang();
		if (lang != null && lang.length() > 0) {
			params.put(RublonAuthParams.FIELD_LANG, lang);
		}
		
		String userEmail = getGUI().getUserEmail();
		if (userEmail != null && userEmail.length() > 0) {
			params.put(RublonAuthParams.FIELD_USER_EMAIL_HASH, Codec.sha256(userEmail.toLowerCase()));
		}
		
		String userId = getGUI().getUserId();
		if (userId != null && userId.length() > 0) {
			params.put(RublonAuthParams.FIELD_USER_ID, userId);
		}
		
		boolean logoutListener = getGUI().getLogoutListener();
		if (logoutListener) {
			params.put(RublonAuthParams.FIELD_LOGOUT_LISTENER, logoutListener);
		}
		return params;
	}
	
	
	/**
	 * Get version date for consmer script.
	 */
	protected String getVersionDate() {
		return this.getRublon().getVersionDate().replace("-", "");
	}


}
