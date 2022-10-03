# Rublon Java SDK

## Table of Contents

1. [Overview](#overview)
2. [Use Cases](#use-cases)
3. [Supported Authentication Methods](#supported-authentication-methods)
4. [Before You Start](#before-you-start)
   * Create an Application in the Rublon Admin Console
   * Optional: Install Rublon Authenticator
5. [Configuration](#configuration)
   * INFO: Initial Assumptions
   * INFO: Modifying the Library
   * Initialize the Library
   * Perform Authentication
   * Finalize Authentication
6. [Troubleshooting](#troubleshooting)

<a id="overview"></a>

## Overview

The Rublon Java SDK library is a client-side implementation of the Rublon API written in Java.
It includes methods for connecting with the Rublon API and embedding the service's GUI in an HTML-based environment.
It forms a convenient Java coding language facade for Rublon API's REST interface.

<a id="use-cases"></a>

## Use Cases

Rublon adds an extra layer of security by prompting the user to authenticate using an extra authentication method such as Mobile Push.
Even if a malicious actor compromises the user's password, the hacker would not be able to log in to the user's account because the second secure factor will thwart them.

Rublon can add an extra layer of security in the following two use cases:
1. **When a user signs in to a system** (after the user enters the correct password)
2. **When a user undergoes a security-sensitive transaction** (such as changing the password or conducting a money transfer)
   
When a user signs in to a system, the second authentication factor should be initiated only after:
- the user has successfully completed the first authentication factor (e.g., entered the correct password)
- the username (and optionally, email address) have been gathered

<a id="supported-authentication-methods"></a>

## Supported Authentication Methods

- [Mobile Push](https://rublon.com/product/mobile-push/) - approve the authentication request by tapping a push notification displayed on the Rublon Authenticator mobile app.
- [Mobile Passcodes](https://rublon.com/product/mobile-passcodes/) (TOTP) - enter the TOTP code (Time-Based One Time Password) using the Rublon Authenticator mobile app.
- [SMS Passcodes](https://rublon.com/product/sms-passcodes/) - enter the verification code from the SMS sent to your mobile phone number.
- [QR Codes](https://rublon.com/product/qr-codes/) - scan a QR code using the Rublon Authenticator mobile app.
- [Email Links](https://rublon.com/product/email-link/) - Click the verification link sent to your email address.
- [WebAuthn/U2F Security Keys](https://rublon.com/product/security-keys/) - Insert the security key into the USB port of your computer and touch it.
<a id="before-you-start"></a>

## Before You Start

Before you start implementing the Rublon Java SDK library into your code, you must create an application in the Rublon Admin Console. We also recommend that you install the Rublon Authenticator mobile app.

### Create an Application in the Rublon Admin Console
1. Sign up for the Rublon Admin Console. [Here’s how](https://rublon.com/doc/admin-console/#rublon-account-registration).
2. In the Rublon Admin Console, go to the **Applications** tab and click **Add Application**.
3. Enter a name for your application and then set the type to **Custom integration using Java SDK**.
4. Click **Save** to add the new Java SDK application in the Rublon Admin Console.
5. Copy and save the values of **System Token** and **Secret Key**. You are going to need these values later.
   
### Optional: Install Rublon Authenticator

For increased security of Multi-Factor Authentication (MFA), end-users are recommended to install the [Rublon Authenticator](https://rublon.com/product/rublon-authenticator/) mobile app.
   
Download the Rublon Authenticator for:
- [Android](https://play.google.com/store/apps/details?id=com.rublon.authenticator&hl=en)
- [iOS](https://apps.apple.com/us/app/rublon-authenticator/id1434412791)

After installing the mobile app, users can authenticate using the following authentication methods:
- [Mobile Push](https://rublon.com/product/mobile-push/)
- [Mobile Passcode](https://rublon.com/product/mobile-passcodes/)
- [QR Code](https://rublon.com/product/qr-codes/)

In some cases, users may not want to install any additional apps on their phones. Also, some users own older phones that do not support modern mobile applications. These users can authenticate using one of the following authentication methods instead:
- [WebAuthn/U2F Security Keys](https://rublon.com/product/security-keys/)
- [SMS Passcode](https://rublon.com/product/sms-passcodes/)
- [Email Link](https://rublon.com/product/email-link/)

<a id="configuration"></a>

## Configuration

Follow the steps below to configure Rublon Java SDK.

### INFO: Initial Assumptions

Let’s assume there is a session handler class Session. It has access to an object that stores user data of the currently logged-in user. Also, let’s assume there is the HttpServer class which is a simple HTTP server instance.

Classes `Session` and `HttpServer` will be used in Java code examples later in this document.

### INFO: Modifying the Library

The `Rublon` class implements a few public methods, which, when needed, can be overridden with inheritance.

We strongly discourage you from modifying any part of the library, as it usually leads to difficulties during library updates. If you need to change the flow or internal structure of the `Rublon` or `RublonCallback` classes, do not hesitate to subclass them according to your needs.

### Initialize the Library
To initialize the Rublon JAVA SDK library, you need to instantiate a `Rublon` class object. Its constructor takes three arguments.

<table>
	<caption><code>Rublon</code> class constructor arguments</caption>
	<thead><tr>
		<th>Name</th>
		<th>Type</th>
		<th>Description</th>
	</tr></thead>
	<tbody>
		<tr><td><code>systemToken</code></td><td>String</td><td>The System Token value you copied from the Rublon Admin Console.</td></tr>
		<tr><td><code>secretKey</code></td><td>String</td><td>The Secret Key value you copied from the Rublon Admin Console.</td></tr>
		<tr><td><code>apiServer</code></td><td>String</td><td>Optional.<br/><br/>Rublon API Server URI.<br/><br/>Default:<br/>https://core.rublon.net</td></tr>
	</tbody>
</table>

### Example Java Code

	import com.rublon.sdk.twofactor.Rublon;

	...
	
	Rublon rublon = new Rublon(
		// system token:
		"A69FC450848B4B94A040416DC4421523",
		// secret key:
		"bLS6NDP7pGjg346S4IHqTHgQQjjSLw3CyApvz5iRjYzgIPN4e9EOi1cQJLrTlvLoHY8zeqg4ILrItYidKJ6JjEUZaA6pR1tZMwSZ"
	);

### Verify Configuration
The `Rublon.checkApplication()` method verifies the validity of the configuration. Your application should call this method every time you change or save the configuration. A configuration change can be, for example, changing the systemToken or secretKey.

<table>
	<caption><code>Rublon.checkApplication()</code> method arguments</caption>
	<thead><tr>
		<th>Name</th>
		<th>Type</th>
		<th>Description</th>
	</tr></thead>
	<tbody>
		<tr><td><code>appVer</code></td><td>String</td><td>The version of the current application.</td></tr>
		<tr><td><code>params</code></td><td>JSONObject</td><td>Optional.<br/></br>Additional transaction parameters.</td></tr>
	</tbody>
</table>

`Rublon.checkApplication()` may throw one of the following exceptions:
- **ApplicationNotFoundException** - Invalid system token
- **InvalidSignatureException** - Invalid Secret Key
- **UnsupportedVersionException** - Incorrect version of the application

### Perform Authentication

The `Rublon.auth()` method uses the username to check the user's protection status and returns a URL address the user should be redirected to in their web browser. The method returns `null` if the user's protection is not active.


<table>
	<caption><code>Rublon.auth()</code> method arguments</caption>
	<thead><tr>
		<th>Name</th>
		<th>Type</th>
		<th>Description</th>
	</tr></thead>
	<tbody>
		<tr><td><code>callbackUrl</code></td><td>String</td><td>The integrated system's callback URL.<br/><br/>Rublon will redirect the user to this URL after successful authentication</td></tr>
		<tr><td><code>username</code></td><td>String</td><td>The user's unique ID, which allows the user to sign in.<br/><br/>Required.</td></tr>
		<tr><td><code>userEmail</code></td><td>JSONObject</td><td>The user's email address.<br/><br/>Optional.<br/><br/>The user's email address. You only need to provide the email address if you want to use the Email Link authentication method.</td></tr>
		<tr><td><code>params</code></td><td>JSONObject</td><td>Optional.<br/><br/>Additional transaction parameters (optional)</td></tr>
	</tbody>
</table>

### Example Java Code

	/**
	 * An example method used to log the user in (integrated system's method)
  	 */
  	void login(String login, String password) {

  		if (loginPreListener()) {
  			User user = authenticate(login, password);
  			if (user != null) {

  				// The user has been authenticated.
  				Session.setUser(user);
  				loginPostListener();
  			}
  		}
	}


	/**
 	 * Listener (hook) invoked after a successful first factor user authentication,
	 * implemented for Rublon integration purposes.
  	 */
  	void loginPostListener() {

  		Rublon rublon = new Rublon(
  			// systemToken (please store in a config):
  			"A69FC450848B4B94A040416DC4421523",
  			// secretKey (please store in a safe config):
  			"bLS6NDP7pGjg346S4IHqTHgQQjjSLw3CyApvz5iRjYzgIPN4e9EOi1cQJLrTlvLoHY8zeqg4ILrItYidKJ6JjEUZaA6pR1tZMwSZ"
  		);

  		try { // Initiate a Rublon authentication transaction

  			String url = rublon.auth(
				"http://example.com/rublon_callback", // callback URL
  				Session.getUser().getId(), // User Id
  				Session.getUser().getEmail() // User email
  			);
  	
  			if (url != null) { // User protection is active
  	
  				// Log the user out before checking the second factor:
  				Session.setUser(null);
  		
  				// Redirect the user's web browser to Rublon servers
  				// to verify the protection:
  				HttpServer.sendHeader("Location", url);
  			}

  		} catch (RublonException e) {
  			// An error occurred
  			Session.setUser(null);
  			HttpServer.setStatus(500);
  			HttpServer.setResponse("There was an error, please try again later.");
  		}

  	/* If we're here, the user's account is not protected by Rublon.
  	The user can be authenticated. */

	}

**Note**: Make sure that your code checks that the user is not signed in. The user should be signed in only after successful Rublon authentication.

### Finalize Authentication

After successful authentication, Rublon redirects the user to the callback URL. The callback flow continues and finalizes the authentication process.

#### Input Params

The callback URL will receive its input arguments in the URL address itself (*query string*).

<table>
	<caption>Callback URL arguments</caption>
	<thead><tr>
		<th>Name</th>
		<th>Type</th>
		<th>Description</th>
	</tr></thead>
	<tbody>
		<tr><td><code>rublonState</code></td><td>String</td><td>Authentication result: <code>ok</code>, <code>error</code> or <code>cancel</code></td></tr>
		<tr><td><code>rublonToken</code></td><td>String</td><td>Access token (60 alphanumeric characters, upper- and lowercase), which allows verifying the authentication using a background Rublon API connection</td></tr>
	</tbody>
</table>

**Note**: If the callback URL has been set to, e.g., `http://example.com/twofactor/auth/`, the params will be appended to the URL address:
`http://example.com/twofactor/auth/?rublonState=ok&rublonToken=Kmad4hAS...d`

**Note**: If you want to construct the callback URL differently (e.g., by using URL Rewrite), you can set the callback URL's template using the meta-tags: `%rublonToken%` and `%rublonState%`, like so:
`http://example.com/twofactor/auth/%rublonState%/%rublonToken%`

#### Handle Authentication Result

After the callback is invoked, you need to create a `RublonCallback` subclass instance to properly finalize authentication. Since the `RublonCallback` class is abstract, you need to create a subclass that implements the methods you need. The implementation is up to you and depends on your requirements and unique system details.

<table>
	<caption><code>RublonCallback</code> class constructor method arguments</caption>
	<thead><tr>
		<th>Name</th>
		<th>Type</th>
		<th>Description</th>
	</tr></thead>
	<tbody>
		<tr><td>rublon</td><td>Rublon</td><td>An instance of the Rublon class.</td></tr>
	</tbody>
</table>

Next, call the `RublonCallback.call()` method.

You should implement the following abstract methods in a subclass.
- `String getState()` - returns the "rublonState" parameter from the HTTP GET request.
- `String getAccessToken()` - returns the "rublonToken" parameter from the HTTP GET request.
- `void handleCancel()` - called when the state parameter is not "ok" nor "error".
- `void handleError()` - called when the state parameter value is "error".
- `void userAuthenticated(String appUserId)` - handle the authenticated user with given user's local ID.

#### Example Java Code

An example implementation of the `RublonCallback` class and usage in the callback:

	class Callback extends RublonCallback {

		public String getState() {
			return HttpServer.getRequestHandler().getParam(PARAMETER_STATE);
		}

		public String getAccessToken() {
			return HttpServer.getRequestHandler().getParam(PARAMETER_ACCESS_TOKEN);
		}

		protected void handleCancel() {
			HttpServer.sendHeader("Location", "/login");
		}

		protected void handleError() {
			HttpServer.sendHeader("Location", "/login?msg=rublon-error");
		}

		protected void userAuthenticated(String appUserId) {
			Session.setUser(User.getById(appUserId));
			HttpServer.sendHeader("Location", "/dashboard");
		}
	}

	...

	Rublon rublon = new Rublon(
		"A69FC450848B4B94A040416DC4421523",
		"bLS6NDP7pGjg346S4IHqTHgQQjjSLw3CyApvz5iRjYzgIPN4e9EOi1cQJLrTlvLoHY8zeqg4ILrItYidKJ6JjEUZaA6pR1tZMwSZ"
	);

	try {
		RublonCallback callback = new Callback(rublon);
		callback.call();
	} catch (CallbackException e) {
		// Please handle this error in the better way:
		HttpServer.setStatus(500);
		HttpServer.setResponse("There was an error, please try again later. " + e.getMessage());
	}

<a id="troubleshooting"></a>

## Troubleshooting

If you encounter any issues with your Rublon integration, please contact [Rublon Support](https://rublon.com/support/).
