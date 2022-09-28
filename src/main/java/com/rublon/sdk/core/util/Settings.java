package com.rublon.sdk.core.util;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Settings class
 */
public class Settings {

	private final Logger logger = Logger.getLogger(String.class.getName());
	private static final Settings INSTANCE = new Settings();
	private final String settingsFileName = "/settings.properties";
	private final Properties properties = new Properties();
	private static final String PROPERTY_SDKVER = "sdkVer";

	private Settings() {
		try {
			load();
		} catch (Exception e) {
			logger.info("Couldn't read properties file.");
		}
	}

	/**
	 * Get instance of Settings class
	 * @return instance
	 */
	public static Settings getInstance() {
		return INSTANCE;
	}

	/**
	 * Get version of SDK
	 *
	 * @return sdkVer
	 */
	public String getSdkVer() {
		return properties.getProperty(PROPERTY_SDKVER);
	}

	private void load() throws IOException {
		properties.load(this.getClass().getClassLoader().getResourceAsStream(settingsFileName));
	}
}
