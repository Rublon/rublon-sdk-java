package com.rublon.sdk.core.exception;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import org.json.JSONObject;

import com.rublon.sdk.core.APIMethod;
import com.rublon.sdk.core.rest.RESTClient;

/**
 * API exception class.
 *
 */
public class APIException extends RublonException {
	
	private static final long serialVersionUID = -4584271816101329116L;
	
	/**
	 * Exception name field name.
	 */
	public static final String FIELD_EXCEPTION = "exception";
	
	/**
	 * Error message field name.
	 */
	public static final String FIELD_MESSAGE = "errorMessage";
	
	/**
	 * Field name for the invalid/missing field.
	 */
	public static final String FIELD_ITEM_NAME = "name";
	
	/**
	 * REST client instance.
	 */
	protected RESTClient client;

	/**
	 * Construct the exception instance.
	 * 
	 * @param client REST client instance.
	 * @param message Exception message.
	 * @param cause Throwable cause.
	 */
	public APIException(RESTClient client, String message, Throwable cause) {
		super(message, cause);
		this.client = client;
	}
	

	/**
	 * Construct the exception instance.
	 * 
	 * @param client REST client instance.
	 * @param message Exception message.
	 */
	public APIException(RESTClient client, String message) {
		this(client, message, null);
	}
	

	/**
	 * Construct the exception instance.
	 * 
	 * @param client REST client instance.
	 */	
	public APIException(RESTClient client) {
		this(client, "", null);
	}
	
	
	/**
	 * Get REST client instance.
	 */
	public RESTClient getClient() {
		return client;
	}
	

	
	
	// ---------------------------------------------------------------------------------------------
	// Exception Factory
	// ---------------------------------------------------------------------------------------------
	
	
	/**
	 * Create the APIException instance by the API response.
	 * 
	 * @param client REST client instance.
	 */
	static public APIException factory(RESTClient client) {
		
		APIException resultException = null;
		
		String rawResponse = client.getRawResponse();
		JSONObject response = new JSONObject(rawResponse);
		if (response != null) {
			String status = response.optString(APIMethod.FIELD_STATUS, null);
			if (status != null && status.equals(APIMethod.STATUS_ERROR)) {
				JSONObject result = response.optJSONObject(APIMethod.FIELD_RESULT);
				if (result != null) {
					String exception = result.optString(FIELD_EXCEPTION, null);
					if (exception != null) {
						String message = result.optString(FIELD_MESSAGE, null);
						String itemName = result.optString(FIELD_ITEM_NAME, null);
						if (message != null && itemName != null) {
							resultException = factory(exception, new Object[]{client, message, itemName});
						}
						else if (message != null) {
							resultException = factory(exception, new Object[]{client, message});
						} else {
							resultException = factory(exception, new Object[]{client});
						}
						
					}
				}
			}
		}
		
		if (resultException == null) {
			resultException = new APIException(client);
		}
		
		return resultException;
		
	}
	
	
	/**
	 * Get the exception instance by given name and arguments.
	 * 
	 * @param name Exception class name.
	 * @param args Arguments array.
	 * @return
	 */
	@SuppressWarnings("unchecked")
	static public APIException factory(String name, Object[] args) {
		@SuppressWarnings("rawtypes")
		Class[] typesArray = new Class[args.length];
		for (int i=0; i<args.length; i++) {
			typesArray[i] = args[i].getClass();
		}
		
		@SuppressWarnings("rawtypes")
		Constructor constructor;
		try {
			constructor = factoryGetClass(name).getConstructor(typesArray);
		} catch (SecurityException e) {
			return null;
		} catch (NoSuchMethodException e) {
			return null;
		}
		Object instance;
		try {
			instance = constructor.newInstance(args);
		} catch (IllegalArgumentException e) {
			return null;
		} catch (InstantiationException e) {
			return null;
		} catch (IllegalAccessException e) {
			return null;
		} catch (InvocationTargetException e) {
			return null;
		}
		if (instance instanceof APIException) {
			return (APIException)instance;
		} else {
			return null;
		}
	}
	

	/**
	 * Get the Class object by given name.
	 * 
	 * @param name Exception class name.
	 */
	@SuppressWarnings("rawtypes")
	static public Class factoryGetClass(String name) {
		
		HashMap<String,Class> map = new HashMap<String, Class>();
		map.put("MissingField_RublonAPIException", APIException.MissingFieldException.class);
		map.put("MissingHeader_RublonAPIException", APIException.MissingHeaderException.class);
		map.put("UnsupportedRequestMethod_RublonAPIException", APIException.UnsupportedRequestMethodException.class);
		map.put("EmptyInput_RublonAPIException", APIException.EmptyInputException.class);
		map.put("InvalidJSON_RublonAPIException", APIException.InvalidJSONException.class);
		map.put("InvalidSignature_RublonAPIException", APIException.InvalidSignatureException.class);
		map.put("ConsumerNotFound_RublonAPIException", APIException.ConsumerNotFoundException.class);
		map.put("UnsupportedVersion_RublonAPIException", APIException.UnsupportedVersionException.class);
		map.put("UserNotFound_RublonAPIException", APIException.UserNotFoundException.class);
		map.put("AccessTokenExpired_RublonAPIException", APIException.AccessTokenExpiredException.class);
		map.put("UnknownAccessToken_RublonAPIException", APIException.UnknownAccessTokenException.class);
		map.put("SessionRestore_RublonAPIException", APIException.SessionRestoreException.class);
		map.put("UnauthorizedUser_RublonAPIException", APIException.UnauthorizedUserException.class);
		map.put("ForbiddenMethod_RublonAPIException", APIException.ForbiddenMethodException.class);
		
		if (map.containsKey(name)) {
			return map.get(name);
		} else {
			return APIException.class;
		}
	}
	
	
	// ---------------------------------------------------------------------------------------------
	// Nested exception classes
	// ---------------------------------------------------------------------------------------------
	
	static public class EmptyInputException extends APIException {
		private static final long serialVersionUID = -1695990751138766697L;
		public EmptyInputException(RESTClient client, String message) {
			super(client, message);
		}
		public EmptyInputException(RESTClient client) {
			this(client, null);
		}
	}
	
	static public class InvalidJSONException extends APIException {
		private static final long serialVersionUID = -3964116030716963473L;
		public InvalidJSONException(RESTClient client, String message) {
			super(client, message);
		}
		public InvalidJSONException(RESTClient client) {
			this(client, null);
		}
	}
	
	static public class UnsupportedRequestMethodException extends APIException {
		private static final long serialVersionUID = 6251875354959232233L;
		public UnsupportedRequestMethodException(RESTClient client, String message) {
			super(client, message);
		}
		public UnsupportedRequestMethodException(RESTClient client) {
			this(client, null);
		}
	}
	
	static public class InvalidSignatureException extends APIException {
		private static final long serialVersionUID = 4051326992755590593L;
		public InvalidSignatureException(RESTClient client, String message) {
			super(client, message);
		}
		public InvalidSignatureException(RESTClient client) {
			this(client, null);
		}
	}
	
	static public class ConsumerNotFoundException extends APIException {
		private static final long serialVersionUID = -8569225607933253747L;
		public ConsumerNotFoundException(RESTClient client, String message) {
			super(client, message);
		}
		public ConsumerNotFoundException(RESTClient client) {
			this(client, null);
		}
	}
	
	static public class UnsupportedVersionException extends APIException {
		private static final long serialVersionUID = 5502571573073932717L;
		public UnsupportedVersionException(RESTClient client, String message) {
			super(client, message);
		}
		public UnsupportedVersionException(RESTClient client) {
			this(client, null);
		}
	}
	
	static public class UserNotFoundException extends APIException {
		private static final long serialVersionUID = -4978431825518985616L;
		public UserNotFoundException(RESTClient client, String message) {
			super(client, message);
		}
		public UserNotFoundException(RESTClient client) {
			this(client, null);
		}
	}
	
	static public class AccessTokenExpiredException extends APIException {
		private static final long serialVersionUID = -3193227909709878255L;
		public AccessTokenExpiredException(RESTClient client, String message) {
			super(client, message);
		}
		public AccessTokenExpiredException(RESTClient client) {
			this(client, null);
		}
	}
	
	static public class UnknownAccessTokenException extends APIException {
		private static final long serialVersionUID = 8489915071232973254L;
		public UnknownAccessTokenException(RESTClient client, String message) {
			super(client, message);
		}
		public UnknownAccessTokenException(RESTClient client) {
			this(client, null);
		}
	}
	
	static public class SessionRestoreException extends APIException {
		private static final long serialVersionUID = -1559202691903372378L;
		public SessionRestoreException(RESTClient client, String message) {
			super(client, message);
		}
		public SessionRestoreException(RESTClient client) {
			this(client, null);
		}
	}
	
	static public class UnauthorizedUserException extends APIException {
		private static final long serialVersionUID = 9042164658652337222L;
		public UnauthorizedUserException(RESTClient client, String message) {
			super(client, message);
		}
		public UnauthorizedUserException(RESTClient client) {
			this(client, null);
		}
	}
	
	static public class ForbiddenMethodException extends APIException {
		private static final long serialVersionUID = 479390271634211179L;
		public ForbiddenMethodException(RESTClient client, String message) {
			super(client, message);
		}
		public ForbiddenMethodException(RESTClient client) {
			this(client, null);
		}
	}

	static public class MissingFieldException extends APIException {
		
		private static final long serialVersionUID = 3485239679798013817L;
		String itemName;
		
		public MissingFieldException(RESTClient client, String message, String itemName) {
			super(client, "Missing field: " + itemName);
			this.itemName = itemName;
		}
		
		
		public MissingFieldException(RESTClient client, String itemName) {
			this(client, null, itemName);
		}
		
		public String getName() {
			return itemName;
		}

	}
	
	static public class MissingHeaderException extends MissingFieldException {
		private static final long serialVersionUID = 2217502508340486741L;
		public MissingHeaderException(RESTClient client, String message, String itemName) {
			super(client, message, itemName);
		}
		public MissingHeaderException(RESTClient client, String itemName) {
			this(client, null, itemName);
		}
	}
	
	
	static public class InvalidFieldException extends MissingFieldException {
		private static final long serialVersionUID = 221750250834486741L;
		public InvalidFieldException(RESTClient client, String message, String itemName) {
			super(client, message, itemName);
		}
		public InvalidFieldException(RESTClient client, String itemName) {
			this(client, null, itemName);
		}
	}


}
