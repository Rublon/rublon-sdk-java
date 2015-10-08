package com.rublon.sdk.login.html;

import com.rublon.sdk.common.html.RublonDeviceWidget;
import com.rublon.sdk.common.html.RublonGUI;
import com.rublon.sdk.core.RublonConsumer;

/**
 * Rublon GUI for the native login service.
 */
public class RublonLoginGUI extends RublonGUI {
	
	public RublonLoginGUI(RublonConsumer rublon) {
		super(rublon);
	}
	
	/**
	 * Display the Rublon GUI.
	 */
	public String toString() {
		return getConsumerScript()
				+ getUserBoxContainer();
	}
	
	
	/**
	 * Get container of the user box.
	 * 
	 * @param ignored
	 */
	protected String getUserBoxContainer(String ignored) {
		return new RublonDeviceWidget().toString();
	}

	
}
