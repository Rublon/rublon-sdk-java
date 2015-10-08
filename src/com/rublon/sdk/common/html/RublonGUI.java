package com.rublon.sdk.common.html;

import com.rublon.sdk.core.RublonConsumer;

/**
 * Rublon GUI parent class.
 */
public class RublonGUI {

	
	/**
	 * Rublon instance.
	 */
	protected RublonConsumer rublon;
	
	/**
	 * Current user's local ID (if logged in).
	 */
	protected String userId;
	
	/**
	 * Current user's email address (if logged in).
	 */
	String userEmail;
	
	/**
	 * Enable or disable th logout listener.
	 */
	boolean logoutListener = false;

	/**
	 * Construct the instance.
	 * @param rublon Rublon instance.
	 */
	public RublonGUI(RublonConsumer rublon) {
		this.rublon = rublon;
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
		return new RublonDeviceWidget().toString();
	}

	
	protected String getUserBoxContainer() {
		return getUserBoxContainer("");
	}

	
	/**
	 * Set the current user's local ID.
	 * 
	 * @param userId
	 */
	public RublonGUI setUserId(String userId) {
		this.userId = userId;
		return this;
	}
	
	/**
	 * Set the current user's email address.
	 * 
	 * @param userEmail
	 */
	public RublonGUI setUserEmail(String userEmail) {
		this.userEmail = userEmail;
		return this;
	}
	
	/**
	 * Enable or disable the remote logout listener.
	 * 
	 * @param logoutListener True for enable, false for disable.
	 */
	public RublonGUI enableLogoutListener(boolean logoutListener) {
		this.logoutListener = logoutListener;
		return this;
	}
	
	/**
	 * Get Rublon instance.
	 */
	public RublonConsumer getRublon() {
		return rublon;
	}
	
	/**
	 * Get consumer script instance.
	 */
	public ConsumerScript getConsumerScript() {
		return new ConsumerScript(this);
	}


	/**
	 * Get user's email address.
	 */
	public String getUserEmail() {
		return userEmail;
	}

	/**
	 * Get user's local ID.
	 */
	public String getUserId() {
		return userId;
	}


	/**
	 * Get logout listener status.
	 */
	public boolean getLogoutListener() {
		return logoutListener;
	}

}
