package com.rublon.sdk.core.rest;


import java.io.IOException;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import com.rublon.sdk.core.RublonConsumer;
import com.rublon.sdk.core.exception.ConnectionException;
import com.rublon.sdk.core.message.RublonSignature;


/**
 * REST client class.
 * <p>
 * Object provides an interface to perform an HTTP REST request.
 * 
 * @author Rublon Developers
 */
public class RESTClient {
	
	/**
	 * Connection timeout in seconds.
	 */
	public static final int TIMEOUT = 30;
	
	/**
	 * User agent string.
	 */
	public static final String USER_AGENT = "rublon-java-sdk";
	
	
	/**
	 * Value of the "Content-Type" HTTP header
	 */
	public static final String HEADER_VALUE_CONTENT_TYPE = "application/json; charset=UTF-8";


	/**
	 * Value of the "Accept" HTTP header
	 */
	public static final String HEADER_VALUE_ACCEPT = "application/json, text/javascript, */*; q=0.01";

	/**
	 * Name of the custom HTTP header to send the library's version
	 */
	public static final String HEADER_NAME_VERSION = "X-Rublon-API-Version";
	
	/**
	 * Name of the custom HTTP header to send the library's version date
	 */
	public static final String HEADER_NAME_VERSION_DATE = "X-Rublon-API-Version-Date";

	/**
	 * Name of the custom HTTP heaader to send the library's platform
	 */
	public static final String HEADER_NAME_PLATFORM = "Rublon-Consumer-Platform";
	
	/**
	 * Name of the custom HTTP header to send the library's technology
	 */
	public static final String HEADER_NAME_SIGNATURE = "X-Rublon-Signature";
	
	/**
	 * HTTP POST request handler.
	 */
	HttpPost httppost;
	
	/**
	 * Rublon Consumer instance
	 */
	RublonConsumer rublon;

	/**
	 * Raw response string.
	 */
	protected String rawResponse;

	/**
	 * Response handler.
	 */
	protected HttpResponse response;
	
	/**
	 * Construct REST client instance
	 * 
	 * @param rublon instance
	 */
	public RESTClient(RublonConsumer rublon) {
		this.rublon = rublon;
	}
	
	
	/**
	 * Perform the request
	 * 
	 * @param url URL address
	 * @param rawPostBody POST body
	 * @return Raw HTTP response body
	 * @throws ConnectionException
	 */
	public String performRequest(String url, String rawPostBody) throws ConnectionException {
		
		httppost = new HttpPost(url);
		httppost.setHeader("Content-Type", RESTClient.HEADER_VALUE_CONTENT_TYPE);
		httppost.setHeader("Accept", RESTClient.HEADER_VALUE_ACCEPT);
		httppost.setHeader("User-Agent", USER_AGENT);
		httppost.setHeader(HEADER_NAME_VERSION, this.rublon.getVersion());
		httppost.setHeader(HEADER_NAME_VERSION_DATE, this.rublon.getVersionDate());
		httppost.setHeader(HEADER_NAME_PLATFORM, RublonConsumer.PLATFORM);
		httppost.setHeader(HEADER_NAME_SIGNATURE, RublonSignature.sign(rawPostBody, rublon.getSecretKey()));
		
		CloseableHttpClient httpclient = HttpClientBuilder.create().build();
		try {
			RequestConfig reqConfig = RequestConfig.custom().
					setConnectionRequestTimeout(TIMEOUT * 1000).
					setSocketTimeout(TIMEOUT * 1000).build();
			StringEntity postBody = new StringEntity(rawPostBody);
			httppost.setEntity(postBody);
			httppost.setConfig(reqConfig);
			response = httpclient.execute(httppost);
			HttpEntity resEntity = response.getEntity();
            if (resEntity != null) {
            	rawResponse = convertStreamToString(resEntity.getContent());
            }
            EntityUtils.consume(resEntity);

		} catch (Exception e) {
			throw new ConnectionException(e.getLocalizedMessage(), e);
		}
		finally {
			try {
				httpclient.close();
			} catch (IOException e) {
				throw new ConnectionException("Error on closing HTTP Client connection. " + e.getLocalizedMessage(), e);
			}
		}
		
		return rawResponse;
	}
	
	/**
	 * Get the raw response body string.
	 */
	public String getRawResponse() {
		return rawResponse;
	}
	
	/**
	 * Get the HTTP POST request handler.
	 */
	public HttpPost getPostHandler() {
		return httppost;
	}
	
	/**
	 * Get the HTTP response status name.
	 */
	public String getHTTPStatus() {
		return response.getStatusLine().getReasonPhrase();
	}
	
	/**
	 * Get the HTTP response status code.
	 * @return
	 */
	public int getHTTPStatusCode() {
		return response.getStatusLine().getStatusCode();
	}
	
	
	/**
	 * Convert an InputStream into String
	 * 
	 * @param is instance
	 * @return String
	 */
	protected static String convertStreamToString(java.io.InputStream is) {
	    java.util.Scanner s = new java.util.Scanner(is);
	    s.useDelimiter("\\A");
	    String result = (s.hasNext() ? s.next() : "");
	    s.close();
	    return result;
	}


	/**
	 * Get the Rublon signature header from response.
	 */
	public String getSignature() {
		Header[] headers = response.getHeaders(HEADER_NAME_SIGNATURE);
		if (headers.length > 0) {
			return headers[0].getValue();
		} else {
			return null;
		}
	}

}
