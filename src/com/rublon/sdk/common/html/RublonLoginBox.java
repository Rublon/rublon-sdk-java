package com.rublon.sdk.common.html;

import java.util.HashMap;
import java.util.Map;

/**
 * Widget to display the Rublon Login Box.
 */
public class RublonLoginBox extends RublonWidget {
	
	/**
	 * Value for the id HTML attribute.
	 */
	protected static final String ATTR_ID = "RublonLoginBoxWidget";
	
	/**
	 * Simple widget format (only the login button).
	 */
	public static final String WIDGET_SIMPLE = "small";
	
	/**
	 * Normal widget format (with the background and call-to-action text).
	 */
	public static final String WIDGET_NORMAL = "large";
	
	/**
	 * URL address to begin the login transaction.
	 */
	protected String loginUrl;
	
	/**
	 * Widget format to use.
	 */
	protected String widgetFormat;
	
	
	/**
	 * Construct the Rublon Login Box.
	 * 
	 * @param loginUrl URL address to begin the login transaction.
	 * @param widgetFormat Widget format name.
	 */
	public RublonLoginBox(String loginUrl, String widgetFormat) {
		this.loginUrl = loginUrl;
		this.widgetFormat = widgetFormat;
	}
	
	/**
	 * Construct the widget instance.
	 * 
	 * @param loginUrl URL address to begin the Rublon native login transaction.
	 */
	public RublonLoginBox(String loginUrl) {
		this(loginUrl, WIDGET_SIMPLE);
	}
	

	/**
	 * Device Widget HTML iframe attributes.
	 */
	@SuppressWarnings("serial")
	protected Map<String, String> getWidgetAttributes() {
		return new HashMap<String, String>(){{
			put("id", ATTR_ID);
			put("data-login-url", loginUrl);
			put("data-size", widgetFormat);
		}};
	}
	
}
