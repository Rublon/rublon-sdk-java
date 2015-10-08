package com.rublon.sdk.twofactor.html;

import com.rublon.sdk.common.html.RublonGUI;
import com.rublon.sdk.common.html.RublonShareAccessWidget;
import com.rublon.sdk.core.RublonConsumer;

/**
 * Rublon GUI for the 2-factor service.
 */
public class Rublon2FactorGUI extends RublonGUI {
	
	/**
	 * Template for user box container.
	 */
	static private final String TEMPLATE_BOX_CONTAINER = "<div class=\"rublon-box\" data-configured=\"%d\" data-can-activate=\"%d\">%s</div>";
	
	
	public Rublon2FactorGUI(RublonConsumer rublon) {
		super(rublon);
	}
	
	/**
	 * Create user box.
	 * 
	 * @return string
	 */
	public String toString() {
		return getConsumerScript()
				+ getUserBoxContainer();
	}
	
	
	
	/**
	 * Get container of the user box.
	 * 
	 * @param string $content HTML content
	 * @return string
	 */
	protected String getUserBoxContainer(String content) {
		return String.format(TEMPLATE_BOX_CONTAINER,
				getRublon().isConfigured() ? 1 : 0,
				getRublon().userCanConfigure() ? 1 : 0,
				content
				)
				+ super.getUserBoxContainer(content)
				+ new RublonShareAccessWidget();
	}

	
}
