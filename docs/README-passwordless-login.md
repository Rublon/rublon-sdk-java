Passwordless Login by Rublon
=============================


Table of Contents
-----------------

1. [Introduction](#intro)
	* [Principles of operation](#intro-how-it-works)
	* [First steps](#intro-first-steps)
	* [Examples' assumptions](#intro-examples)
	* [Modifying the library](#intro-mods)
2. [Library initialization](#initialize)
3. [Rublon Login Box widget](#login-box)
	* [Example code](#login-box-example)
4. [Initialize the authentication process](#auth)
	* [Example code](#auth-example)	
5. [Authentication finalization](#callback)
	* [Input params](#callback-input)
	* [Authentication verification](#callback-verification)
	* [Example code](#callback-example)
6. [Authentication strategies](#strategies)
	* [By profile ID](#strategies-profile-id)
	* [By email address](#strategies-email)

<a id="intro"></a>

Introduction
------------

The *Rublon Passwordless Login* is an implementation of the authentication process
based on the Rublon SDK library where the user's login or password inputs are not
required at all.

Rublon provides an authentication system based on the Rublon Mobile App
which works as the authentication token.

The end-user should install the Rublon mobile app, available on all leading
smartphone systems.

<a id="intro-how-it-works"></a>

### Principles of operation

#### Displaying the Rublon Login Box

Instead of a login form you need to display the Rublon Login Box widget
which will display a login button.

An URL address pointing to Rublon servers will be generated from the SDK library.
The user's web browser must be then redirected
to that URL in order to carry out the authentication process.

#### Return to the integrated system

After a successful authentication, the web browser will be redirected to
a callback URL address which points to the integrated system servers.
The integrated system should intercept that URL, retrieve its params and finalize
the authentication using this library.

<a id="intro-first-steps"></a>

### First steps

To start using the Rublon Passwordless Login you should:

*	install the Rublon mobile app on your smartphone,
	create a new account and confirm your email address,
*	visit the Rublon [Developer Area](https://developers.rublon.com)
	at [developers.rublon.com](https://developers.rublon.com)
	and log in by clicking the "Developer Dashboard" button,
	and scanning the QR code that will appear using the Rublon mobile app,
*	go to the "Add website" form (Dashboard -&gt; Add website)
	and fill in the required fields,
*	copy the provided **system token** and **secret key**,
	which will be used to identify the integrated system and verify
	the authenticity and integrity of the messages exchanged with Rublon API.

<a id="intro-examples"></a>

### Examples' assumptions

In the following examples we assume the existence of:
*	the session handler class `Session`, which has access to
	an object storing the currently logged in user data,
*	the `HttpServer` class which is a simple HTTP server instance,
*	the `Database` class which is a database interface.


<a id="intro-mods"></a>

### Modifying the library

The `RublonLogin` class implements a few public methods, which, when needed,
can be overriden with inheritance.

We strongly discourage you from modifying any part of the library, as it usually
leads to difficulties during future library updates. If you need to change the flow
or internal structure of the `RublonLogin`, `RublonLoginGUI` or `RublonLoginCallback`
classes, don't hesitate to subclass them according to your needs.


<a id="initialize"></a>

Library initialization
----------------------

To initialize the library you need to instantiate a `RublonLogin` class object.
Its constructor takes three arguments.

<table>
	<caption><code>RublonLogin</code> class constructor arguments</caption>
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

		import com.rublon.sdk.login.RublonLogin;
		
		...
		
		RublonLogin rublon = new RublonLogin(
			// system token:
			"A69FC450848B4B94A040416DC4421523",
			// secret key:
			"bLS6NDP7pGjg346S4IHqTHgQQjjSLw3CyApvz5iRjYzgIPN4e9EOi1cQJLrTlvLoHY8zeqg4ILrItYidKJ6JjEUZaA6pR1tZMwSZ"
		);

<a id="login-box"></a>

Rublon Login Box widget
------------------------

Developer has to embed on his website the Rublon Login Box widget which
allows a user to choose the authentication by the existing Trusted Device(s)
or login as a new user. Developer must provide to the Login Box an URL address
to his website's endpoint which will be responsible for initialize
the authentication transaction using the Rublon SDK library and
redirect the user's web browser to the Rublon server in order
to authenticate himself.

<a id="login-box-example"></a>

### Example code

Following code shows how to embed the Rublon Login Box widget.

	// URL of the authentication endpoint:
	String authUrl = "http://example.com/auth/rublon/init";
	
	// Construct the Rublon instance:
	RublonLogin rublon = new RublonLogin(
		// systemToken (please store in a config):
		"A69FC450848B4B94A040416DC4421523",
		// secretKey (please store in a safe config):
		"bLS6NDP7pGjg346S4IHqTHgQQjjSLw3CyApvz5iRjYzgIPN4e9EOi1cQJLrTlvLoHY8zeqg4ILrItYidKJ6JjEUZaA6pR1tZMwSZ"
	);
	
	// Create the GUI instance:
	RublonLoginGUI gui = new RublonLoginGUI(rublon);
	
	// Embed the Consumer Script:
	HttpServer.sendOutput(gui.getConsumerScript());
	
	// Embed the Login Box widget:
	HttpServer.sendOutput(new RublonLoginBox(authUrl));
	


<a id="auth"></a>

Initialize the authentication process
--------------------------------------

The `RublonLogin.auth()` method will check the Rublon server's status
and return an URL address for the web browser to be redirected to.

<table>
	<caption><code>RublonLogin.auth()</code> method arguments</caption>
	<thead><tr>
		<th>Name</th>
		<th>Type</th>
		<th>Description</th>
	</tr></thead>
	<tbody>
		<tr><td><code>callbackUrl</code></td><td>String</td><td>The integrated system's callback URL</td></tr>
		<tr><td><code>consumerParams</code></td><td>JSONObject</td><td>Additional transaction parameters (optional)</td></tr>
	</tbody>
</table>

<a id="auth-example"></a>

### Example code

An example of logging in a user on an integrated system:

	/**
	 * An example method used to log the user in.
	 */
	void rublonLogin() {
	
		RublonLogin rublon = new RublonLogin(
			// systemToken (please store in a config):
			"A69FC450848B4B94A040416DC4421523",
			// secretKey (please store in a safe config):
			"bLS6NDP7pGjg346S4IHqTHgQQjjSLw3CyApvz5iRjYzgIPN4e9EOi1cQJLrTlvLoHY8zeqg4ILrItYidKJ6JjEUZaA6pR1tZMwSZ"
		);
		
		try { // Initiate a Rublon authentication transaction
		
			String url = rublon.auth(
				"http://example.com/auth/rublon/callback" // callback URL
			);
			
			if (url != null) {
			
				// Log the user out:
				Session.setUser(null);
				
				// Redirect the user's web browser to Rublon servers
				// to authenticate the user by Rublon:
				HttpServer.sendHeader("Location", url);
				
			} else {
				// Rublon may be not available.
			}
			
		} catch (RublonException e) {
			// An error occurred
			Session.setUser(null);
			HttpServer.setStatus(500);
			HttpServer.setResponse("There was an error, please try again later.");
		}
	
	}

Calling the `RublonLogin.auth()`
method will return a URL address pointing to Rublon servers, which the user's
browser should redirect to in order to verify a Trusted Device or display
a QR code to be scanned by the Rublon mobile app.


<a id="callback"></a>

Authentication finalization
---------------------------

After a successful authentication Rublon will redirect the user's browser
to the callback URL. The callback flow continues the authentication process,
i.e. the finalization of the authentication (logging in).

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
		<tr><td><code>state</code></td><td>String</td><td>Authentication result: <code>ok</code>, <code>error</code> or <code>cancel</code></td></tr>
		<tr><td><code>token</code></td><td>String</td><td>Access token (100 alphanumeric characters, upper- and lowercase), which allows authentication's verification using a background Rublon API connection</td></tr>
	</tbody>
</table>

<div class="block">
Notice: If the callback URL has been set to e.g. <code>http://example.com/auth/rublon/callback/</code>,
the params will be appended to the URL address:
<pre><code>http://example.com/auth/rublon/callback/?state=ok&token=Kmad4hAS...d</code></pre>
If your callback URL should be formed differently (e.g. when using an URL rewrite),
you can set the callback URL's template using the meta-tags:
<code>%token%</code> and <code>%state%</code>, like so:<br />
<pre><code>http://example.com/auth/rublon/callback/%state%/%token%</code></pre>
</div>

<a id="callback-verification"></a>
### Authentication verification

After the callback is invoked, for proper finalization of the authentication
process you need to create a `RublonLoginCallback` subclass instance.
Because the `RublonLoginCallback` class in abstract you need to create a subclass
that implement needed methods which depend on your system details.

<table>
	<caption><code>RublonLoginCallback</code> class constructor method arguments</caption>
	<thead><tr>
		<th>Name</th>
		<th>Type</th>
		<th>Description</th>
	</tr></thead>
	<tbody>
		<tr><td><code>rublon</code></td><td>RublonLogin</td><td>An instance of the <code>RublonLogin</code> class.</td></tr>
	</tbody>
</table>

Next, the `RublonLoginCallback.call()` method should be called.

The following abstract methods should be implemented in a subclass.

- `String getState()` - returns the "state" parameter from the HTTP GET request.
- `String getAccessToken()` - returns the "token" parameter from the HTTP GET request.
- `void handleCancel()` - called when the state parameter is not "ok" nor "error".
- `void handleError()` - called when the state parameter value is "error".
- `void userAuthenticated(String profileId)` - handle the authenticated user.



<a id="callback-example"></a>
### Example code

An example implementation of the `RublonLoginCallback` class and usage in the callback:

	class Callback extends RublonLoginCallback {
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
		protected void userAuthenticated(String profileId) {
			User user = User.getByField("rublon_profile_id", profileId);
			if (user != null) {
				Session.setUser(user);
				HttpServer.sendHeader("Location", "/dashboard");
			} else {
				HttpServer.sendHeader("Location", "/login?msg=rublon-error");
			}
		}
	}
	
	...

	RublonLogin rublon = new RublonLogin(
		"A69FC450848B4B94A040416DC4421523",
		"bLS6NDP7pGjg346S4IHqTHgQQjjSLw3CyApvz5iRjYzgIPN4e9EOi1cQJLrTlvLoHY8zeqg4ILrItYidKJ6JjEUZaA6pR1tZMwSZ"
	);
		
	try {
	
		RublonLoginCallback callback = new RublonLoginCallback(rublon);
		callback.call();
		
	} catch (CallbackException e) {
		// Please handle this error in the better way:
		HttpServer.setStatus(500);
		HttpServer.setResponse("There was an error, please try again later. " + e.getMessage());
	}



<a id="strategies"></a>

Authentication strategies
-----------------------------

There are two general authentication strategies that can be used
by the developer to login the user in his system
after the user has been authenticated by Rublon.

<a id="strategies-profile-id"></a>

### By profile ID

First, user has to login to his existing account in the normal way e.g. by
using login and password. Then the Rublon authentication process should be
invoken, for example after user clicks on a "Link with Rublon" button.
After the user will authenticate by Rublon, his Rublon user's profile ID
will be available in the callback method. Then your system should
save this profile ID linked with your user's local ID in your database.

The following code shows an example implementation of
the RublonLoginCallback.userAuthenticated() abstract method
which implements the profile-ID-match strategy.

	protected void userAuthenticated(String profileId) {
		
		User user = User.getByField("rublon_profile_id", profileId);
		if (user != null) {
		
			// User has been recognized.
			Session.setUser(user);
			HttpServer.sendHeader("Location", "/dashboard");
			
		} else {
		
			// There's no user linked with this profile ID.
			
			// Check if user wants to link his account with Rublon:
			User currentUser = Session.getUser();
			if (currentUser != null) {
				currentUser.setMetaField("rublon_profile_id", profileId);
			} else {
				HttpServer.sendHeader("Location", "/login?msg=rublon-error");
			}
			
		}
	}

When the user's account has been linked with the Rublon profile ID
in your database, you will be able to recognize the local user's
account after the user will authenticate by Rublon next time
as shown in the example above.

<a id="strategies-email"></a>

### By email address

The second strategy is to match a local user by his email address
from Rublon. In the callback method the Rublon SDK library returns
the `Credentials` instance which provides the user's
email addresses list which has been entered by user to Rublon.
Each email address is not given as a plain-text, but it is the SHA-256 hash
of the lower-case email address.

In order to optimize matching you should create an indexed database column,
next to the user's email column, which stores the actual SHA-256
hash of the user's current email address. Then you will be able
to select users that matches with the provided hash list after
the user has been authenticated by Rublon.

The following code shows an example implementation of
the RublonLoginCallback.userAuthenticated() abstract method
which implements the email-match strategy.

	protected void userAuthenticated(String profileId) {
		
		String[] emails = getUserEmailHashArray();
		User[] users = User.selectByMetaField("email_sha256", emails);
		if (users.length == 0) {
			HttpServer.sendHeader("Location", "/login?msg=missing-user");
		}
		else if (users.length == 1) {
			Session.setUser(users[0]);
			HttpServer.sendHeader("Location", "/dashboard");
		} else {
			// More users have been matched, you have to implement the solution.
		}
		
	}


