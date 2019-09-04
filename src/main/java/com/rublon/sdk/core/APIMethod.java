package com.rublon.sdk.core;

import org.json.JSONObject;

import com.rublon.sdk.core.exception.APIException;
import com.rublon.sdk.core.exception.ConnectionException;
import com.rublon.sdk.core.rest.RESTClient;

/**
 * API method abstract class.
 */
abstract public class APIMethod {

	/**
	 * Rublon instance.
	 */
	protected RublonConsumer rublon;
	
	/**
	 * REST client instance.
	 */
	protected RESTClient client;
	
	/**
	 * Raw response body.
	 */
	protected String rawResponseBody;
	
	/**
	 * Response JSON object.
	 */
	protected JSONObject response;
	
	/**
	 * Value of the "status" field of the response.
	 */
	protected String responseStatus;
	
	/**
	 * Value of the "result" field of the response.
	 */
	protected JSONObject responseResult;
	
	/**
	 * Success status value.
	 */
	public static final String STATUS_OK = "OK";
	
	/**
	 * Error status value.
	 */
	public static final String STATUS_ERROR = "ERROR";
	
	/**
	 * Field name for the result field.
	 */
	public static final String FIELD_RESULT = "result";
	
	/**
	 * Field name for the status field.
	 */
	public static final String FIELD_STATUS = "status";
	
	/**
	 * Hash algorithm.
	 */
	public static final String HASH_ALG = "sha256";
	
	
	/**
	 * Construct the API method instance.
	 * @param rublon
	 */
	protected APIMethod(RublonConsumer rublon) {
		this.rublon = rublon;
		client = new RESTClient(rublon);
	}
	
	

	/**
	 * Get the raw response string.
	 */
	public String getRawResponse() {
		return rawResponseBody;
	}
	
	
	/**
	 * Get the response object.
	 */
	public JSONObject getResponse() {
		return response;
	}

	/**
	 * Get the response result object.
	 */
	public JSONObject getResponseResult() {
		return responseResult;
	}
	
	
	/**
	 * Perform HTTP request
	 *
	 * @throws ConnectionException 
	 * @throws APIException 
	 */
	public void perform() throws ConnectionException, APIException {
		
		String rawPostBody = "";
		
		JSONObject params = getParams();
		
		if (params != null && params.keySet().size() > 0) {
			rawPostBody = params.toString();
		}
		
		// Execute request
		this.rawResponseBody = client.performRequest(getUrl(), rawPostBody);
		
		// Validate response
		validateResponse();
		
	}
	
	
	/**
	 * Validate the API response.
	 * @throws APIException
	 */
	protected void validateResponse() throws APIException {
		if (client.getHTTPStatusCode() == 200 || client.getHTTPStatusCode() == 400) {
			if (rawResponseBody != null) {
				response = new JSONObject(rawResponseBody);
				if (response != null && response.length() > 0) {
					responseResult = response.optJSONObject(FIELD_RESULT);
					if (responseResult != null && responseResult.length() > 0) {
						String status = response.optString(FIELD_STATUS, null);
						if (status != null) {
							if (status.equals(STATUS_OK)) {
								String signature = client.getSignature();
								if (signature != null) {
									if (validateSignature(signature, rawResponseBody)) {
										// OK
									} else throw new APIException.InvalidSignatureException(client, "Invalid response signature: "+ signature);
								} else throw new APIException.MissingHeaderException(client, RESTClient.HEADER_NAME_SIGNATURE);
							}
							else if (status.equals(STATUS_ERROR)) {
								throw APIException.factory(client);
							} else throw new APIException.InvalidFieldException(client, "Invalid status field", status);
						} else throw new APIException.MissingFieldException(client, FIELD_STATUS);
					} else throw new APIException.MissingFieldException(client, FIELD_RESULT);
				} else throw new APIException.InvalidJSONException(client);
			} else throw new APIException(client, "Empty response body.");
		} else throw new APIException(client, "Unexpected response HTTP status code: " + client.getHTTPStatusCode());
	}
	
	/**
	 * Validate given signature for given input and secret key.
	 * 
	 * @param signature
	 * @param input
	 * @param secretKey
	 * @return True if the signature is valid.
	 */
	protected boolean validateSignature(String signature, String input, String secretKey) {
		String check = signMessage(input, secretKey);
		return (check != null && signature != null && check.equals(signature));
	}

	/**
	 * Validate given signature for given input.
	 * 
	 * Get the secret key from the Rublon instance.
	 * 
	 * @param signature
	 * @param input
	 * @return True if the signature is valid.
	 */
	protected boolean validateSignature(String signature, String input) {
		return validateSignature(signature, input, rublon.getSecretKey());
	}
	
	/**
	 * Sign the given message by given secret key.
	 * 
	 * @param input
	 * @param secretKey
	 * @return Signature string.
	 */
	protected String signMessage(String input, String secretKey) {
		return Codec.hmac(HASH_ALG, secretKey, input);
	}
	
	/**
	 * Get Rublon instance.
	 */
	protected RublonConsumer getRublon() {
		return rublon;
	}
	

	/**
	 * Get the API request's parameters object.
	 */
	protected JSONObject getParams() {
		return new JSONObject(){{
			put(RublonAuthParams.FIELD_SYSTEM_TOKEN, rublon.getSystemToken());
			put(RublonAuthParams.FIELD_VERSION, getRublon().getVersion());
			put(RublonAuthParams.FIELD_VERSION_DATE, getRublon().getVersionDate());
		}};
	}
	
	/**
	 * Get the API request's URL.
	 */
	abstract protected String getUrl();

}
