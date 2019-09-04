Rublon Java SDK
================


Table of Contents
-----------------

1. [Introduction](#intro)
	* [Use cases](#intro-use-cases)
	* [Principles of operation](#intro-how-it-works)
	* [First steps](#intro-first-steps)
	* [Examples' assumptions](#intro-examples)
	* [Modifying the library](#intro-mods)
2. [Library initialization](#initialize)
3. [Signing in](#auth)
	* [Example code](#auth-example)
4. [Authentication finalization](#callback)
	* [Input params](#callback-input)
	* [Authentication verification](#callback-verification)
	* [Example code](#callback-example)
5. [Email2FA - simplified identity verification](#email2fa)
	* [Principles of operation](#email2fa-how-it-works)
	* [Example usage](#email2fa-config)




<a id="intro"></a>

Introduction
------------

The *Rublon Java SDK* library is a client-side implementation of
the [Rublon](https://rublon.com) authentication service written in Java,
including methods for connecting with the Rublon API and embedding
the service's GUI in a HTML-based environment.
It forms a convenient Java coding language facade for the service's REST interface.

<a id="intro-use-cases"></a>

### Use cases

Rublon provides an additional secury layer:

1.	**during logging in to your system**, adding a second (or additional)
	authentication factor,
2.	**while conducting a security-sensitive transactions**,
	providing a user the means for identity confirmation before changing passwords
	or conducting a money transfer.

To be able to perform an additional authentication using Rublon,
the user must first be authenticated in a different way,
e.g. with a username and password.
It is a necessary step, because upon Rublon's initialization the service
must receive certain information about the user:

- a unique Id, stored in the system (inafter called **the integrated system**) implementing the Rublon service,
- the user's email address.

To experience the full measure of two-factor authentication, the end-user
should install the Rublon mobile app, available on all leading smartphone
systems. However, having those with older phone devices in mind or those
who do not want to install any additional apps on their phones, we prepared
a Email2FA process which does not require using an additional device of any kind.

<a id="intro-how-it-works"></a>

### Principles of operation

#### User protection

User protection is active when a user's email address in the integrated system
can be matched to a user in the Rublon service.
For this purpose, the user's email is sent to Rublon servers.

1. If the email is matched to an existing Rublon account, the user's identity
can be confirmed using Rublon.
2. Otherwise, if the user does not possess a Rublon account (the email
could not be matched), Rublon will use the Email2FA process, trying to verify
the user's identity by sending a confirmation email message to his email address.

#### Identity confirmation

If the library finds an active user protection, a URL address pointing to Rublon
servers will be generated. The user's web browser must be then redirected
to that URL in order to carry out the identity confirmation.

If the web browser is the user's Trusted Device, the authentication will be
performed automatically and invisibly. Otherwise, the user will be asked
to scan a QR code using the Rublon mobile app or to click the verification
link sent to his email address, upon which the authentication will be performed.

#### Return to the integrated system

After a successful authentication, the web browser will be redirected to
a callback URL address which points to the integrated system servers.
The integrated system should intercept that URL, retrieve its params and finalize
the authentication using this library.

<a id="intro-first-steps"></a>

### First steps

To start using the Rublon Java SDK library you should:

*	install the Rublon mobile app on your smartphone,
	create a new account and confirm your email address,
*	visit the Rublon [Rublon Admin Console](https://admin.rublon.net)
	and log in,
*	go to the "Add the application" form (Applications -> Add)
    and fill in the required fields,
*	copy the provided **system token** and **secret key**,
	which will be used to identify the integrated system and verify
	the authenticity and integrity of the messages exchanged with Rublon API.

<a id="intro-examples"></a>

### Examples' assumptions

In the following examples we assume the existence of the session handler
class `Session`, which has access to an object storing the currently logged
in user data and the `HttpServer` class which is a simple HTTP server instance.

<a id="intro-mods"></a>

### Modifying the library

The `Rublon` class implements a few public methods, which, when needed,
can be overriden with inheritance.

We strongly discourage you from modifying any part of the library, as it usually
leads to difficulties during future library updates. If you need to change the flow
or internal structure of the `Rublon` or `RublonCallback`
classes, don't hesitate to subclass them according to your needs.


<a id="initialize"></a>

Library initialization
----------------------

To initialize the library you need to instantiate a `Rublon` class object.
Its constructor takes three arguments.

<table>
	<caption><code>Rublon</code> class constructor arguments</caption>
	<thead><tr>
		<th>Name</th>
		<th>Type</th>
		<th>Description</th>
	</tr></thead>
	<tbody>
		<tr><td><code>systemToken</code></td><td>String</td><td>Your system's public Id</td></tr>
		<tr><td><code>secretKey</code></td><td>String</td><td>Secret key</td></tr>
		<tr><td><code>apiServer</code></td><td>String</td><td>(optional) API Server URI</td></tr>
	</tbody>
</table>

An example of the library's initialization:

		import com.rublon.sdk.twofactor.Rublon;
		
		...
		
		Rublon rublon = new Rublon(
			// system token:
			"A69FC450848B4B94A040416DC4421523",
			// secret key:
			"bLS6NDP7pGjg346S4IHqTHgQQjjSLw3CyApvz5iRjYzgIPN4e9EOi1cQJLrTlvLoHY8zeqg4ILrItYidKJ6JjEUZaA6pR1tZMwSZ"
		);

<a id="auth"></a>

Signing in
------------

Rublon protects users during their signing in processes. Even if a someone
lears the user's password with malicious intent, such a person would be unable
to log in to the user's account, because a physical access to the Rublon mobile app
(installed in the user's smartphone) or to his email account is needed.

<p class="notice">
Administrator can force users to authenticate using the mobile app (to avoid the Email2FA process).
</p>

Authenticating a user with the second factor should be initiated when the user
has successfully passed the first factor of authentication (e.g. the valid user
credentials have been provided) and the user's unique Id and email address are known.

The `Rublon.auth()` method will check the user's protection status (using
the email address) and return a URL address for the web browser to be redirected to
(if user protection is active) or `null` in case the user's protection is not active.

<table>
	<caption><code>Rublon.auth()</code> method arguments</caption>
	<thead><tr>
		<th>Name</th>
		<th>Type</th>
		<th>Description</th>
	</tr></thead>
	<tbody>
            <tr>
                <td><code>callbackUrl</code></td>
                <td>String</td><td>The integrated system's callback URL</td>
            </tr>
            <tr>
                <td><code>appUserId</code></td>
                <td>String</td>
                <td>The integrated system's user's unique Id which will allow to log in the user upon successful authentication</td>
            </tr>
            <tr>
                <td><code>userEmail</code></td><td>String</td>
                <td>The user's email address in the integrated system which will allow to check the user's protection status and match the user to a Rublon account</td>
            </tr>
            <tr>
                <td><code>consumerParams</code></td>
                <td>JSONObject</td>
                <td>Additional transaction parameters (optional)</td>
            </tr>
            <tr>
                <td><code>isPasswordless</code></td>
                <td>boolean</td>
                <td>Information if it is a login attempt using passwordless method (optional)</td>
            </tr>
        </tbody>
</table>

<a id="auth-example"></a>

### Example code

An example of logging in a user on an integrated system:

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

If the user's account is protected by Rublon, calling the `Rublon.auth()`
method will return a URL address pointing to Rublon servers, which the user's
browser should redirect to in order to verify a Trusted Device, display
a QR code to be scanned by the Rublon mobile app or verify identity by clicking
a confirmation link which has been sent to the user's email address.

<p class="notice">
Because the user's web browser will be redirected to Rublon servers in order
to confirm the user's identity, the user should be logged out
(if he/she was logged in before) to prevent creating a user session.
Otherwise Rublon will not protect the user effectively, because returning
to the integrated system before a proper Rublon authentication is performed
may grant the user access to an active logged in session in the system.
The user should be logged in only after a successful Rublon authentication.
</p>

<a id="callback"></a>

Authentication finalization
---------------------------

After a successful authentication Rublon will redirect the user's browser
to the callback URL. The callback flow continues the authentication process,
i.e. the finalization of the authentication (logging in or identity confirmation).

<a id="callback-input"></a>
### Input params

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
		<tr><td><code>rublonToken</code></td><td>String</td><td>Access token (100 alphanumeric characters, upper- and lowercase), which allows authentication's verification using a background Rublon API connection</td></tr>
	</tbody>
</table>

<div class="block">
Notice: If the callback URL has been set to e.g. <code>http://example.com/twofactor/auth/</code>,
the params will be appended to the URL address:
<pre><code>http://example.com/twofactor/auth/?rublonState=ok&rublonToken=Kmad4hAS...d</code></pre>
If your callback URL should be formed differently (e.g. when using an URL rewrite),
you can set the callback URL's template using the meta-tags:
<code>%rublonToken%</code> and <code>%rublonState%</code>, like so:<br />
<pre><code>http://example.com/twofactor/auth/%rublonState%/%rublonToken%</code></pre>
</div>

<a id="callback-verification"></a>
### Authentication verification

After the callback is invoked, for proper finalization of the authentication
process you need to create a `RublonCallback` subclass instance.
Because the `RublonCallback` class in abstract you need to create a subclass
that implement needed methods which depend on your system details.

<table>
	<caption><code>RublonCallback</code> class constructor method arguments</caption>
	<thead><tr>
		<th>Name</th>
		<th>Type</th>
		<th>Description</th>
	</tr></thead>
	<tbody>
		<tr><td><code>rublon</code></td><td>Rublon</td><td>An instance of the <code>Rublon</code> class.</td></tr>
	</tbody>
</table>

Next, the `RublonCallback.call()` method should be called.

The following abstract methods should be implemented in a subclass.

- `String getState()` - returns the "rublonState" parameter from the HTTP GET request.
- `String getAccessToken()` - returns the "rublonToken" parameter from the HTTP GET request.
- `void handleCancel()` - called when the state parameter is not "ok" nor "error".
- `void handleError()` - called when the state parameter value is "error".
- `void userAuthenticated(String appUserId)` - handle the authenticated user with given user's local ID.


<a id="callback-example"></a>
### Example code

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
	
		RublonCallback callback = new RublonCallback(rublon);
		callback.call();
		
	} catch (CallbackException e) {
		// Please handle this error in the better way:
		HttpServer.setStatus(500);
		HttpServer.setResponse("There was an error, please try again later. " + e.getMessage());
	}



<a id="email2fa"></a>

Email2FA - simplified identity verification
-------------------------------------------

For users of an integrated system who do not possess a Rublon account (they do
not want to sign up or don't own a smartphone), Rublon provides a simplified form
of two-factor identity verification. This feature employs an email message
with an identity confirmation link sent to the email address of the user being
authenticated, assuming that no one but that user has access to his/her email
inbox. 

<p class="notice">
This feature is enabled by default. However
developer can force users to authenticate using the mobile app, to avoid
the Email2FA process, which can increase the security.
</p>

<a id="email2fa-how-it-works"></a>

### Principles of operation

1.	Rublon looks for a Trusted Device, which will authenticate the user automatically.
2.	If a Trusted Device cannot be found, Rublon will check if a user with an email address
	provided by the integrated system is protected by Rublon. If such a user is found, the
	process involves using the mobile app.
3.	If no user is found (the user does not have a Rublon account), the Email2FA process
	is started.
4.	The user will receive an email with a identity confirmation link.
5.	After clicking the link, the user will be asked if the current browser should become a Trusted
	Device (signing in) or [a question about confirming a transaction will be displayed](#confirm).
6.	After answering the question, the user will be redirected to the integrated system's [callback URL](#callback)
	and logged in (or the transaction initiated by the user will be confirmed).


<a id="email2fa-config"></a>

### Example usage

The use of Email2FA is by default active and looks the same as the [Signing in process](#auth-example).
