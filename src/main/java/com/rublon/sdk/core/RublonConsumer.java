package com.rublon.sdk.core;

import com.rublon.sdk.core.exception.RublonException;


/**
 * Rublon Consumer abstract class.
 * <p>
 * The main class provides common methods for all Rublon services.
 * In order for the class to work properly, it must be initiated with two parameters: System Token and the Secret Key.
 * Both of parameters can be obtained from developer dashboard at admin.rublon.com.
 * 
 * @author Rublon Developers
 */
abstract public class RublonConsumer {

	/**
	 * Default API server.
	 */
	public static final String DEFAULT_API_SERVER = "https://core.rublon.net";
	
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
	 * Language code.
	 * <p>
	 * 2-letter language code compliant with <a href="https://en.wikipedia.org/wiki/List_of_ISO_639-1_codes">ISO 639-1</a>.
	 */
	protected String lang = null;

	/**
	 * Initialize RublonConsumer.
	 * 
	 * @param systemToken
	 * @param secretKey
	 */
	public RublonConsumer(String systemToken, String secretKey) {
		this(systemToken, secretKey, DEFAULT_API_SERVER);
	}
	

	/**
	 * Initialize RublonConsumer.
	 * 
	 * @param systemToken
	 * @param secretKey
	 * @param apiServer
	 */
	public RublonConsumer(String systemToken, String secretKey, String apiServer) {
		this.systemToken = systemToken;
		this.secretKey = secretKey;
		this.apiServer = apiServer;
		this.lang = DEFAULT_LANG;
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
}