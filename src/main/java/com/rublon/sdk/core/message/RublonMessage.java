package com.rublon.sdk.core.message;


import org.json.JSONObject;

import com.rublon.sdk.core.Codec;
import com.rublon.sdk.core.exception.RublonException;

/**
 * Class for handling Rublon message.
 */
public class RublonMessage {

	/**
	 * Message header
	 */
	protected JSONObject head = new JSONObject();
	
	/**
	 * Message body
	 */
	protected String body = "";
	
	/**
	 * Field name for message header
	 */
	static final String FIELD_HEAD = "head";

	/**
	 * Field name for time parameter
	 */
	static final String FIELD_HEAD_TIME = "time";

	/**
	 * Field name for size parameter
	 */
	static final String FIELD_HEAD_SIZE = "size";

	/**
	 * Field name for message body
	 */
	static final String FIELD_BODY = "body";

	/**
	 * Field name for sign method
	 */
	static final String FIELD_SIGN_METHOD = "signMethod";
	
	
	
	/**
	 * Simple constructor
	 */
	public RublonMessage() {
		super();
	}
	
	
	/**
	 * Construct Rublon Message from raw string
	 * 
	 * @param Raw string message
	 * @throws RublonMessage.InvalidMessageException 
	 * @throws RublonMessage.InvalidMessageTimeException
	 */
	public RublonMessage(String rawMessage) throws InvalidMessageTimeException, InvalidMessageException {
		JSONObject message = Codec.json_decode(rawMessage);
		JSONObject head = message.optJSONObject(RublonMessage.FIELD_HEAD);
		if (head != null && head.keySet().size() > 0) {
			long time = head.optInt(RublonMessage.FIELD_HEAD_TIME);
			long currentTime = RublonSignature.getTime();
			if (time > 0 && time < currentTime + 60 && time > currentTime - RublonSignature.MESSAGE_LIFETIME) {
				String rawBody = message.optString(RublonMessage.FIELD_BODY);
				if (rawBody.length() > 0) {
					
					this.body = rawBody;
					
				} else {
					throw new InvalidMessageException("Invalid response data (empty body)");
				}
			} else {
				throw new InvalidMessageTimeException("Invalid message time: "+ time);
			}
		} else {
			throw new InvalidMessageException("Invalid response data (empty header)");
		}
		
	}
	
	
	/**
	 * Set message body
	 * 
	 * @param Raw body string
	 * @return Self instance
	 */
	public RublonMessage setBody(String body) {
		this.body = body;
		this.setHead(RublonMessage.FIELD_HEAD_SIZE, body.length());
		return this;
	}
	
	
	/**
	 * Set single header field
	 * 
	 * @param Field name
	 * @param Value to set
	 * @return Self instance
	 */
	public RublonMessage setHead(String name, Object value) {
		this.head.put(name, value);
		return this;
	}
	
	
	/**
	 * Get the body JSON object
	 * 
	 * @return JSONObject
	 */
	public JSONObject getBodyObject() {
		return Codec.json_decode(this.body);
	}

	
	/**
	 * Set body object
	 * 
	 * @param Body object
	 * @return Self instance
	 */
	public RublonMessage setBody(JSONObject body) {
		this.setBody(body.toString());
		return this;
	}
	
	
	/**
	 * Get object as JSON string
	 */
	public String toString() {
		JSONObject json = new JSONObject();
		json.put(RublonMessage.FIELD_HEAD, this.head);
		json.put(RublonMessage.FIELD_BODY, this.body);
		return json.toString();
	}
	
	
	
	@SuppressWarnings("serial")
	public static class InvalidMessageException extends RublonException {
		public InvalidMessageException(String message, Throwable cause) {
			super(message, cause);
		}
		public InvalidMessageException(String message) {
			super(message);
		}
	}
	
	@SuppressWarnings("serial")
	public static class InvalidMessageTimeException extends RublonException {
		public InvalidMessageTimeException(String message, Throwable cause) {
			super(message, cause);
		}
		public InvalidMessageTimeException(String message) {
			super(message);
		}
	}
	

}
