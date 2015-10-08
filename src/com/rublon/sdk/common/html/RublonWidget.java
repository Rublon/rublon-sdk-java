package com.rublon.sdk.common.html;

import java.util.HashMap;
import java.util.Map;

import com.rublon.sdk.core.Codec;

/**
 * Rublon widget abstract class.
 */
abstract public class RublonWidget {


	/**
	 * Name of the paramter to set the CSS font color of the widget.
	 */
	public static final String WIDGET_CSS_FONT_COLOR = "font-color";

	/**
	 * Name of the paramter to set the CSS font size of the widget.
	 */
	public static final String WIDGET_CSS_FONT_SIZE = "font-size";
	
	/**
	 * Name of the paramter to set the CSS font family of the widget.
	 */
	public static final String WIDGET_CSS_FONT_FAMILY = "font-family";
	
	/**
	 * Name of the paramter to set the CSS background of the widget.
	 */
	public static final String WIDGET_CSS_BACKGROUND_COLOR = "background-color";
	
	/**
	 * HTML template.
	 */
	private static final String TEMPLATE = "<div class=\"rublon-widget\"><iframe %s></iframe></div>";
	

	/**
	 * Get iframe to load the Device Widget.
	 *
	 * @return string
	 */
	public String toString() {
		Map<String, String> attr = new HashMap<String, String>();
		attr.putAll(getWidgetAttributes());
		attr.putAll(getWidgetCSSAttribsData());
		return String.format(TEMPLATE, createAttributesString(attr));
	}
	
	
	/**
	 * Creates HTML attributes string.
	 */
	public static String createAttributesString(Map<String, String> attr) {
		String result = "";
		for (String name : attr.keySet()) {
			result += " " + Codec.htmlspecialchars(name) + "=\"" + Codec.htmlspecialchars(attr.get(name)) + "\"";
		}
		return result;
	}
	
	

	/**
	 * Creates HTML attributes array for a widget CSS attributes.
	 */
	private Map<String, String> getWidgetCSSAttribsData() {
		Map<String, String> result = new HashMap<String, String>();
		Map<String, String> attribs = getWidgetCSSAttribs();
		for (String name : attribs.keySet()) {
			result.put("data-" + name, attribs.get(name));
		}
		return result;
	}
	
	
	/**
	 * Returns CSS attributes for a widget.
	 */
	protected Map<String, String> getWidgetCSSAttribs() {
		return new HashMap<String, String>();
	}
	
	
	/**
	 * Get widget's iframe HTML attributes.
	 */
	protected Map<String, String> getWidgetAttributes() {
		return new HashMap<String, String>();
	}
	

	
}
