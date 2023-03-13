package com.singleevent.sdk.zoommeet.main.java.us.zoom.sdksample.initsdk;

public interface AuthConstants {

	// TODO Change it to your web domain
	public final static String WEB_DOMAIN = "zoom.us";
	public final static String APP_KEY = "A3i4gzO8EpgMrOELjjgu4KIj5Pdfi6gdH1mU";

	// TODO Change it to your APP Secret
	public final static String APP_SECRET = "wfSPAkvUYdI6hcEKDS27pMg8bFgrYpbgkzDB";


	/**
	 * We recommend that, you can generate jwttoken on your own server instead of hardcore in the code.
	 * We hardcore it here, just to run the demo.
	 *
	 * You can generate a jwttoken on the https://jwt.io/
	 * with this payload:
	 * {
	 *     "appKey": "string", // app key
	 *     "iat": long, // access token issue timestamp
	 *     "exp": long, // access token expire time
	 *     "tokenExp": long // token expire time
	 * }
	 */
	public final static String SDK_JWTTOKEN ="eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOm51bGwsImlzcyI6InlYRW8tYTg4UVd1dUhycXpXM1JzMnciLCJleHAiOjE1OTY3MDIyMjUsImlhdCI6MTU5NjY5NjgyNX0.1ko1_4EP-b6QnIst6ICGn4OGra21tTbCDkqRTvFP6bQ";

}
