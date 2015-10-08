package com.rublon.sdk.core;

import com.rublon.sdk.core.exception.RublonException;


/**
 * Rublon Consumer abstract class.
 * <p>
 * The main class provides common methods for all Rublon services.
 * In order for the class to work properly, it must be initiated with two parameters: System Token and the Secret Key.
 * Both of parameters can be obtained from developer dashboard at developers.rublon.com.
 * 
 * @author Rublon Developers
 */
abstract public class RublonConsumer {

	/**
	 * API version number.
	 */
	public static final String VERSION = "3.8.0";
	
	/**
	 * API version date.
	 */
	public static final String VERSION_DATE = "2015-08-18";
	
	/**
	 * Default API server.
	 */
	public static final String DEFAULT_API_SERVER = "https://code.rublon.com";
	
	/**
	 * Library target platform.
	 */
	public static final String PLATFORM = "java";
	
	/**
	 * Default language code.
	 */
	public static final String DEFAULT_LANG = "en";


	/**
	 * System token.
	 */
	protected String systemToken = null;

	/**
	 * Secret key.
	 */
	protected String secretKey = null;

	/**
	 * Rublon API domain.
	 * <p>
	 * URL used to make requests to Rublon API.
	 */
	protected String apiServer = null;
	
	/**
	 * Service name.
	 */
	protected String serviceName = null;
	
	/**
	 * Language code.
	 * <p>
	 * 2-letter language code compliant with <a href="https://en.wikipedia.org/wiki/List_of_ISO_639-1_codes">ISO 639-1</a>.
	 */
	protected String lang = null;

	/**
	 * Current URL address.
	 */
	protected String currentUrl = null;

	/**
	 * Whether the current user can configure the Rublon system token and secret key.
	 */
	protected boolean userCanConfigure = false;
	

	/**
	 * Initialize RublonConsumer.
	 * 
	 * @param String systemToken
	 * @param String secretKey
	 */
	public RublonConsumer(String systemToken, String secretKey) {
		this(systemToken, secretKey, DEFAULT_API_SERVER);
	}
	

	/**
	 * Initialize RublonConsumer.
	 * 
	 * @param String systemToken
	 * @param String secretKey
	 * @param String apiServer
	 */
	public RublonConsumer(String systemToken, String secretKey, String apiServer) {
		this.systemToken = systemToken;
		this.secretKey = secretKey;
		this.apiServer = apiServer;
	}


	/**
	 * Get Rublon API server.
	 * 
	 * @return String
	 */
	public String getAPIServer() {
		return this.apiServer;
	}

	/**
	 * Set Rublon API domain.
	 * 
	 * @param domain
	 * @return RublonConsumer
	 */
	public RublonConsumer setAPIServer(String domain) {
		this.apiServer = domain;
		return this;
	}

	/**
	 * Get secret key.
	 * 
	 * @return String
	 */
	public String getSecretKey() {
		return this.secretKey;
	}

	/**
	 * Get system token.
	 * 
	 * @return String
	 */
	public String getSystemToken() {
		return this.systemToken;
	}


	/**
	 * Get version date.
	 */
	public String getVersionDate() {
		return VERSION_DATE;
	}
	
	
	/**
	 * Get version.
	 */
	public String getVersion() {
		return VERSION;
	}
	
	
	/**
	 * Get target platform name
	 */
	public String getPlatform() {
		return PLATFORM;
	}

	
	/**
	 * Set language code
	 * <p>
	 * Set 2-letter language code compliant with <a href="https://en.wikipedia.org/wiki/List_of_ISO_639-1_codes">ISO 639-1</a>.
	 * 
	 * @param lang 2-letter language code
	 */
	public RublonConsumer setLang(String lang) {
		this.lang = lang;
		return this;
	}

	/**
	 * Get language code
	 * <p>
	 * Get 2-letter language code compliant with <a href="https://en.wikipedia.org/wiki/List_of_ISO_639-1_codes">ISO 639-1</a>.
	 * 
	 * @return 2-letter language code
	 */
	public String getLang() {
		return this.lang;
	}
	
	/**
	 * Get service name.
	 */
	public String getServiceName() {
		return serviceName;
	}
	
	/**
	 * Set service name.
	 * 
	 * @param serviceName
	 */
	public RublonConsumer setServiceName(String serviceName) {
		this.serviceName = serviceName;
		return this;
	}
	
	/**
	 * Check whether the service is configured.
	 */
	public boolean isConfigured() {
		return (getSystemToken() != null && getSecretKey() != null);
	}
	
	
	/**
	 * Test whether the service is configured and throw an exception if not.
	 * 
	 * @throws RublonException.ConfigurationException
	 */
	public void testConfiguration() throws RublonException.ConfigurationException {
		if (!isConfigured()) {
			throw new RublonException.ConfigurationException();
		}
	}


	/**
	 * Get current URL address.
	 */
	public String getCurrentUrl() {
		return currentUrl;
	}
	
	
	/**
	 * Set the current URL address.
	 * @param url
	 */
	public RublonConsumer setCurrentUrl(String url) {
		currentUrl = url;
		return this;
	}

	/**
	 * Check whether the current user can configure the Rublon system token and secret key.
	 */
	public boolean userCanConfigure() {
		return userCanConfigure;
	}
	
	
	/**
	 * Set the current URL address.
	 * @param url
	 */
	public RublonConsumer setCurrentUrl(boolean can) {
		userCanConfigure = can;
		return this;
	}
	
	

}
