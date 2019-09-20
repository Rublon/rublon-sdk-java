package com.rublon.sdk.core.html;

import java.util.HashMap;
import java.util.Map;

/**
 * Widget to display the Rublon Login Box.
 */
public class RublonLoginBox extends RublonWidget {
	
	/**
	 * Value for the id HTML attribute.
	 */
	protected static final String ATTR_ID = "RublonLoginBoxWidget";

	/**
	 * URL address to begin the login transaction.
	 */
	protected String apiUrl;

	/**
	 * URL path of the login box.
	 *
	 */
	protected String urlPath = "/api/sdk/passwordless";
	
	
	/**
	 * Construct the Rublon Login Box.
	 * 
	 * @param apiUrl URL address to begin the login transaction.
	 */
	public RublonLoginBox(String apiUrl) {
		this.apiUrl = apiUrl;
	}

	/**
	 * Device Widget HTML iframe attributes.
	 */
	@SuppressWarnings("serial")
	protected Map<String, String> getWidgetAttributes() {
		return new HashMap<String, String>(){{
			put("id", ATTR_ID);
			put("src", apiUrl + urlPath);
		}};
	}

	protected String getAdditionalHTML() {
		return "<script> " +
		"window.addEventListener('message', function(event) {" +
			"if (event.origin !== '" + this.apiUrl  +"')" +
				"return;" +

			"if (event.data && event.data.action) {" +
				"var form = document.createElement('form');" +
				"var actionInput = document.createElement('input');" +
				"var userEmailInput = document.createElement('input');" +

				"form.method = 'POST';" +

				"actionInput.value = event.data['action'];" +
				"actionInput.name = 'action';" +
				"actionInput.type = 'hidden';" +
				"form.appendChild(actionInput);" +

				"if (event.data['email']) {" +
					"userEmailInput.value=event.data['email'];" +
					"userEmailInput.name= 'userEmail';" +
					"userEmailInput.type = 'hidden';" +
					"form.appendChild(userEmailInput);" +
				"}" +

				"document.body.appendChild(form);" +

				"form.submit();" +
				"} else if (event.data && event.data.height) {" +
					"var iframe = document.querySelectorAll('iframe#RublonLoginBoxWidget');" +
					"for (var i = 0; i < iframe.length; i++ ) {" +
						"iframe[i].style.height = event.data['height'] + 38 + 'px';" +
					"}" +
				"}" +
			"}, true);" +
			"</script>";
	}
	
}
