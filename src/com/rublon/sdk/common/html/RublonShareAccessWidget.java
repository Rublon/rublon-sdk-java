package com.rublon.sdk.common.html;

import java.util.HashMap;
import java.util.Map;

/**
 * Displays the Rublon Share Access Widget.
 */
public class RublonShareAccessWidget extends RublonWidget {
	
	/**
	 * Value for the id HTML attribute.
	 */
	protected static final String ATTR_ID = "RublonShareAccessWidget";

	/**
	 * Device Widget HTML iframe attributes.
	 */
	@SuppressWarnings("serial")
	protected Map<String, String> getWidgetAttributes() {
		return new HashMap<String, String>(){{
			put("id", ATTR_ID);
		}};
	}
	
}
