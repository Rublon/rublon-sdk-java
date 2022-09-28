package com.rublon.sdk.core;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Hex;
import org.json.JSONObject;


/**
 * Custom codecs for communication with the Rublon API.
 * 
 * @abstract
 * @author Rublon Developers
 */
public abstract class Codec {

	/**
	 * Get string that represents an object encoded to JSON
	 * 
	 * @param input object
	 * @return JSON string
	 */
	static public String json_encode(Object input) {
		return JSONObject.valueToString(input);
	}
	
	
	/**
	 * Get object encoded as JSON string
	 * 
	 * @param input JSON string
	 * @return Decoded object
	 */
	static public JSONObject json_decode(String input) {
		return new JSONObject(input);
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
