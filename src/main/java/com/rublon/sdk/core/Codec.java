package com.rublon.sdk.core;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.json.JSONObject;


/**
 * Custom codecs for communication with the Rublon API.
 * 
 * @abstract
 * @author Rublon Developers
 */
public abstract class Codec {
	
	
	/**
	 * Encode string by Base64
	 * 
	 * @param Input string
	 * @return Base64-encoded input
	 */
	static public String base64_encode(String input) {
		try {
			return Base64.encodeBase64String(
				input.getBytes("UTF-8")
			);
		} catch (UnsupportedEncodingException ignore) {}
		return "";
	}
	

	/**
	 * Decode string by Base64
	 * 
	 * @param Input base64-encoded string
	 * @return Decoded input
	 */
	static public String base64_decode(String input) {
		try {
			return new String(
				Base64.decodeBase64(input),
				"UTF-8"
			);
		} catch (UnsupportedEncodingException ignore) {}
		return "";
	}
	
	
	/**
	 * Get string that represents an object encoded to JSON
	 * 
	 * @param Input object
	 * @return JSON string
	 */
	static public String json_encode(Object input) {
		return JSONObject.valueToString(input);
	}
	
	
	/**
	 * Get object encoded as JSON string
	 * 
	 * @param Input JSON string
	 * @return Decoded object
	 */
	static public JSONObject json_decode(String input) {
		return new JSONObject(input);
	}
	
	/**
	 * Encode string as URL component
	 * 
	 * @param Input string
	 * @return URL-encoded string
	 */
	static public String url_encode(String input) {
		try {
			return URLEncoder.encode(input, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}
	
	/**
	 * Decode an URL component
	 * 
	 * @param Input URL-encoded string
	 * @return Original string
	 */
	static public String url_decode(String input) {
		try {
			return URLDecoder.decode(input, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}

	/**
	 * Get HTML-safe string
	 * 
	 * @param Input string
	 * @return HTML-safe string
	 */
	public static String htmlspecialchars(String input) {
		return input
				.replace(">", "&lt;")
				.replace("<", "&gt;")
				.replace("&", "&amp;")
				.replace("'", "&#039;")
				.replace("\"", "&quot;");
	}


	/**
	 * Get MD5 hash of the input string
	 * 
	 * @param Input string
	 * @return MD5 hash
	 */
	public static String md5(String input) {
		return DigestUtils.md5Hex(input);
	}
	
	
	public static String sha1(String input) {
		return DigestUtils.sha1Hex(input);
	}
	
	
	public static String sha256(String input) {
		return DigestUtils.sha256Hex(input);
	}


	/**
	 * Get the HMAC hash of given secret and input using specified hash method
	 * 
	 * @param hashMethod eg. SHA256
	 * @param secretKey
	 * @param input
	 */
	public static String hmac(String hashMethod, String secretKey, String input) {
		String algo = "Hmac"+ hashMethod;
		try {
			Mac sha256_HMAC = Mac.getInstance(algo);
			SecretKeySpec secret_key = new SecretKeySpec(secretKey.getBytes(), algo);
			sha256_HMAC.init(secret_key);
			return Hex.encodeHexString(sha256_HMAC.doFinal(input.getBytes()));
		} catch (NoSuchAlgorithmException e) {
			
		} catch (InvalidKeyException e) {
			
		}
		return null;
	}
	
}
