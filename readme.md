# Android SDK for Castle

**[Castle](https://castle.io) adds real-time monitoring of your authentication stack, instantly notifying you and your users on potential account hijacks.**

[![Download](https://api.bintray.com/packages/castleintelligence/castle-android/castle/images/download.svg) ](https://bintray.com/castleintelligence/castle-android/castle/_latestVersion)
[![CircleCI](https://circleci.com/gh/castle/castle-android/tree/master.svg?style=svg)](https://circleci.com/gh/castle/castle-android/tree/master)
[![codecov](https://codecov.io/gh/castle/castle-android/branch/master/graph/badge.svg)](https://codecov.io/gh/castle/castle-android)

## Requirements

- Android 4.0

## Installation

Castle is available through Gradle.

### Gradle

Add the following line to your projects `build.gradle`

```ruby
compile 'io.castle.android:castle:latest-version'
```

Castle uses Tape2 for queuing events and is currently only available in the Sonatype snapshot repository. Add the following maven repository to your `build.gradle`.

```ruby
maven {
	url "https://oss.sonatype.org/content/repositories/snapshots"
}
```

### Usage

#### Configuration

##### Alt. 1: onCreate

Add the following snippet to your application's `onCreate` method.

```java
// Create configuration
Configuration configuration = new Configuration(this);

// Enable the desired functionality
configuration.publishableKey("pk_123sdawggkdk2lk123");
configuration.screenTrackingEnabled(true); // Default: true
configuration.debugLoggingEnabled(true); // Default: false
```

Include the Client ID in all calls to your API backend. This lets us tie a specific transaction in your backend to it’s origin.

```java
List<String> whitelist = Arrays.asList(new String[] { "https://api.example.com/" });
configuration.baseURLWhiteList(whitelist);
```

Then setup Castle with the by providing the configuration

```java
Castle.setupWithConfiguration(this, configuration);
```

##### Alt. 2: Manifest

The Castle Publishable Key for your application can also be provided as meta-data in your applications Android Manifest

```xml
<meta-data android:name="castle_publishable_key"
           android:value="@string/castle_publishable_key"/>
```

Then simply setup Castle with the default configuration:

```java
Castle.setupWithDefaultConfiguration(this); // Reads appId from manifest meta tag
```

#### Client ID forwarding

The `client_id` needs to be forwarded as the HTTP header `X-Castle-Client-Id`. This will automatically be picked up by the Castle server-side SDK. The simplest way of doing this is by using OkHttp and adding an interceptor to your client:

```java
OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(Castle.castleInterceptor())
                    .build();
```

For other HTTP clients you can add the headers manually by getting a list based on the whitelist by using

```java
Castle.headers(url);
```

##### OkHttp

```java
String url = "https://api.example.com/v1/auth";
OkHttpClient client = new OkHttpClient();
Request.Builder requestBuilder = new Request.Builder()
		.url(url);

requestBuilder.header(Castle.X_CASTLE_CLIENT_ID, Castle.deviceIdentifier());

Request request = requestBuilder.build();

// Flush if request to whitelisted url
Castle.flushIfNeeded(url);

Response response = client.newCall(request).execute();
```

##### HttpURLConnection

```java
URL url = new URL("https://api.example.com/v1/auth");
HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

urlConnection.setRequestProperty(Castle.X_CASTLE_CLIENT_ID, Castle.deviceIdentifier());

// Flush if request to whitelisted url
Castle.flushIfNeeded(url.toString());

try {
	InputStream in = new BufferedInputStream(urlConnection.getInputStream());
} finally {
	urlConnection.disconnect();
}
```

##### Volley

```java
// Instantiate the RequestQueue.
RequestQueue queue = Volley.newRequestQueue(context);
String url = "https://api.example.com/v1/auth";

// Request a string response from the provided URL.
StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
	new Response.Listener<String>() {
		@Override
    	public void onResponse(String response) {
       	}
    }, new Response.ErrorListener() {
    	@Override
     	public void onErrorResponse(VolleyError error) {
    	}
	}) {
    	@Override
    	public Map<String, String> getHeaders() throws AuthFailureError {
	   		Map<String, String>  headers = super.getHeaders();

			headers.put(Castle.X_CASTLE_CLIENT_ID, Castle.deviceIdentifier());

			return headers;
		}
};

// Flush if request to whitelisted url
Castle.flushIfNeeded(url);

// Add the request to the RequestQueue.
queue.add(stringRequest);
```
#### Identify

The identify call lets you tie a user to their action and should be called right after the user logged in successfully. The `user_id` will be persisted locally so subsequent calls `screen` will automatically be tied to that user.

```java
// Identify user with a unique identifier
Castle.identify("1234");
```

#### Track screen views

The default behavior is to let the SDK automatically track screen views, but you can always disable `screenTrackingEnabled` and instead call `screen` for each screen view.

Track screen view and include some properties (optional):

```java
// Track screen view and include some properties
Map<String, String> properties = new HashMap<>();
properties.put("role", "Admin");

Castle.screen("Menu", properties);
```
