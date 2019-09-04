package com.rublon.sdk.core.exception;

import com.rublon.sdk.core.rest.RESTClient;

/**
 * Client exception class.
 */
public class ClientException extends RublonException {

	private static final long serialVersionUID = -4584271816101329116L;
	
	/**
	 * REST client instance.
	 */
	protected RESTClient client;

	/**
	 * Construct the client exception instance.
	 * 
	 * @param client REST client instance.
	 * @param message Error message.
	 * @param cause Throwable cause.
	 */
	public ClientException(RESTClient client, String message, Throwable cause) {
		super(message, cause);
		this.client = client;
	}
	
	/**
	 * Construct the client exception instance.
	 * 
	 * @param client REST client instance.
	 * @param message Error message.
	 */
	public ClientException(RESTClient client, String message) {
		this(client, message, null);
	}
	
	/**
	 * Construct the client exception instance.
	 * 
	 * @param client REST client instance.
	 */
	public ClientException(RESTClient client) {
		this(client, null, null);
	}
	
	/**
	 * Get the REST client instance.
	 */
	public RESTClient getClient() {
		return client;
	}
	
	
	// ---------------------------------------------------------------------------------------------
	// Nested exception classes
	// ---------------------------------------------------------------------------------------------
	
	
	static public class ResponseException extends ClientException {
		private static final long serialVersionUID = -1209138997735327403L;
		public ResponseException(RESTClient client) {
			super(client);
		}
	}
	
	
	static public class InvalidSignatureException extends ResponseException {
		private static final long serialVersionUID = -120913899773527403L;
		public InvalidSignatureException(RESTClient client) {
			super(client);
		}
	}
	
	
	static public class ErrorResponseException extends ResponseException {
		private static final long serialVersionUID = -1998935794111665471L;
		public ErrorResponseException(RESTClient client) {
			super(client);
		}
	}
	
	static public class EmptyResponseException extends ResponseException {
		private static final long serialVersionUID = -339778272363084859L;
		public EmptyResponseException(RESTClient client) {
			super(client);
		}
	}
	
	static public class InvalidResponseException extends ResponseException {
		private static final long serialVersionUID = 9146718611923917693L;
		public InvalidResponseException(RESTClient client) {
			super(client);
		}
	}
	
	static public class InvalidJSONException extends InvalidResponseException {
		private static final long serialVersionUID = -1594192671166642559L;
		public InvalidJSONException(RESTClient client) {
			super(client);
		}
	}

	static public class MissingFieldException extends ClientException {
		
		private static final long serialVersionUID = 348523979798013817L;
		String itemName;
		
		public MissingFieldException(RESTClient client, String itemName) {
			super(client, "Missing field: " + itemName);
			this.itemName = itemName;
		}
		
		public String getName() {
			return itemName;
		}

	}
	
	static public class MissingHeaderException extends MissingFieldException {
		private static final long serialVersionUID = 221750250840486741L;
		public MissingHeaderException(RESTClient client, String itemName) {
			super(client, itemName);
		}
	}

}
