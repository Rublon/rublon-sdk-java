package com.rublon.sdk.twofactor.api;

import com.rublon.sdk.core.APIMethod;
import com.rublon.sdk.core.RublonAuthParams;
import com.rublon.sdk.core.RublonConsumer;
import com.rublon.sdk.core.rest.RESTClient;
import org.json.JSONObject;

public class Init extends APIMethod {

	/**
	 * API request URI path.
	 */
	protected static final String REQUEST_URI_PATH = "/api/app/init";

	/**
	 * Application version
	 */
	private final String appVer;

	/**
	 * Optional params
	 */
	private final JSONObject params;

	/**
	 * Construct the API method instance.
	 *
	 * @param rublon
	 * @param restClient
	 */
	public Init(RublonConsumer rublon, RESTClient restClient) {
		this(rublon, restClient, null);
	}

	/**
	 * Construct the API method instance.
	 *
	 * @param rublon
	 * @param restClient
	 * @param appVer
	 */
	public Init(RublonConsumer rublon, RESTClient restClient, String appVer) {
		this(rublon, restClient, appVer, null);
	}

	/**
	 * Construct the API method instance.
	 *
	 * @param rublon
	 * @param restClient
	 * @param appVer
	 * @param params
	 */
	public Init(RublonConsumer rublon, RESTClient restClient, String appVer, JSONObject params) {
		super(rublon, restClient);
		this.appVer = appVer;
		this.params = params;
	}

	@Override protected String getUrl() {
		return getRublon().getAPIServer() + REQUEST_URI_PATH;
	}

	protected JSONObject getParams() {
		return RublonAuthParams.mergeJSONObjects(new JSONObject[] {
				super.getParams(),
				new JSONObject() {{
					put(RublonAuthParams.FIELD_APP_VER, appVer);
					if (params != null) {
						put(RublonAuthParams.FIELD_PARAMS, params);
					}
				}}
		});
	}
}
