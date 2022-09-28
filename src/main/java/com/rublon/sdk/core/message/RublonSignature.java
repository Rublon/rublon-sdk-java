package com.rublon.sdk.core.message;

import org.json.JSONObject;


import com.rublon.sdk.core.Codec;
import com.rublon.sdk.core.exception.RublonSignatureException;


/**
 * Class for handling Rublon signed message.
 *
 */
public class RublonSignature {

	/**
	 * Rublon message life time in seconds
	 */
	public static final int MESSAGE_LIFETIME = 300;

	/**
	 * Field name for data
	 */
	public static final String FIELD_DATA = "data";

	/**
	 * Field name for signature
	 */
	public static final String FIELD_SIGNATURE = "sign";

	/**
	 * Field name for status
	 */
	public static final String FIELD_STATUS = "status";

	/**
	 * Secret key to signing messages
	 */
	protected String secretKey;
	

	/**
	 * Rublon message instance
	 */
	protected RublonMessage message;
	
	
	/**
	 * Raw string message for signing
	 */
	protected String rawData;
	
	
	/**
	 * Signature computed for current message and secret key
	 */
	protected String signature;
	
	
	/**
	 * Message status
	 */
	protected String status;
	
	
	
	
	/**
	 * Construct Rublon signature with default sign method
	 * 
	 * @param secretKey key for signing message
	 */
	public RublonSignature(String secretKey) {
		this.secretKey = secretKey;
	}
	
	
	/**
	 * Parse raw string message and create RublonMessage object
	 * 
	 * @param rawInput string message
	 * @return Self instance
	 * @throws RublonMessage
	 * @throws RublonSignatureException
	 * @throws RublonMessage.InvalidMessageTimeException 
	 */
	public RublonSignature parse(String rawInput) throws RublonMessage.InvalidMessageException, RublonSignatureException, RublonMessage.InvalidMessageTimeException {
		JSONObject response = Codec.json_decode(rawInput);
		if (response != null && response.keySet().size() > 0) {
			rawData = response.optString(RublonSignature.FIELD_DATA);
			signature = response.optString(RublonSignature.FIELD_SIGNATURE);
			status = response.optString(RublonSignature.FIELD_STATUS);
			if (rawData.length() > 0 && signature.length() > 0) {
				if (RublonSignature.verifyData(rawData, secretKey, signature)) {
					this.message = new RublonMessage(rawData);
				} else {
					throw new RublonSignatureException("Invalid signature: "+ signature +", should be: "+ RublonSignature.sign(rawData, secretKey));
				}
			} else {
				throw new RublonMessage.InvalidMessageException("Invalid response (no data or sign field)");
			}
		} else {
			throw new RublonMessage.InvalidMessageException("Empty response");
		}
		return this;
	}
	
	
	/**
	 * Set message object
	 * 
	 * @param message instance
	 * @return Self instance
	 */
	public RublonSignature setMessage(RublonMessage message) {
		this.message = message;
		return this;
	}
	
	
	/**
	 * Get object as JSON string
	 */
	public String toString() {
		return this.getJSONObject().toString();
	}
	
	
	/**
	 * Get object as JSON
	 * 
	 * @return JSONObject instance
	 */
	public JSONObject getJSONObject() {
		this.message.setHead(RublonMessage.FIELD_HEAD_TIME, RublonSignature.getTime());
		this.rawData = message.toString();
		this.signature = RublonSignature.sign(this.rawData, this.secretKey);
		JSONObject json = new JSONObject();
		json.put(RublonSignature.FIELD_DATA, rawData);
		json.put(RublonSignature.FIELD_SIGNATURE, this.getSignature());
		return json;
	}
	
	
	/**
	 * Sign given data with secret key and get the signature
	 * 
	 * @param rawData string data to sign
	 * @param secretKey key
	 * @return Signature string
	 */
	public static String sign(String rawData, String secretKey) {
		return Codec.hmac("SHA256", secretKey, rawData);
	}
	
	
	/**
	 * Get current signature
	 */
	public String getSignature() {
		return this.signature;
	}
	
	
	/**
	 * Get status
	 */
	public String getStatus() {
		return this.status;
	}
	

	/**
	 * Get current UNIX timestamp
	 * 
	 * @return UNIX timestamp in seconds
	 */
	public static long getTime() {
		return (long) (System.currentTimeMillis() / 1000L);
	}
	
	
	/**
	 * Get current message instance
	 */
	public RublonMessage getMessage() {
		return this.message;
	}
	
	

	/**
	 * Verify data by signature and secret key
	 *
	 * @param data to sign
	 * @param secretKey key used to create the signature
	 * @param sign signature to verify
	 * @return bool
	 */
	public static boolean verifyData(String data, String secretKey, String sign) {
		String dataSign = sign(data, secretKey);
		return (dataSign.equals(sign));
	}

	
	/**
	 * Verify data by signature and secret key
	 *
	 * @param data to sign
	 * @param secretKey key used to create the signature
	 * @param sign signature to verify
	 * @return bool
	 */
	public static boolean verifyData(JSONObject data, String secretKey, String sign) {
		String dataString = Codec.json_encode(data);
		return verifyData(dataString, secretKey, sign);
	}

	

}
