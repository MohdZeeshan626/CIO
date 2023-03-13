package com.webmobi.gecmedia.Views;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.text.SpannableString;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.singleevent.sdk.App;
import com.singleevent.sdk.Custom_View.Error_Dialog;
import com.singleevent.sdk.Custom_View.Util;
import com.singleevent.sdk.Custom_View.VolleyErrorLis;
import com.singleevent.sdk.R;

import com.singleevent.sdk.model.AppDetails;
import com.singleevent.sdk.model.Events;
import com.singleevent.sdk.model.LinkedinUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import io.paperdb.Paper;

public class LinkedinAppProfile extends AppCompatActivity {

    AppDetails appDetails;
    int pos;
    private String title;
    String url="https://www.linkedin.com/oauth/v2/authorization?response_type=code&client_id=77s8qs6oqrmcqi&redirect_uri=https://www.webmobi.com/77s8qs6oqrmcqi/callback&state=gdfgfdhdfhgsdfg&scope=r_liteprofile";
    private ArrayList<Events> events = new ArrayList<Events>();
    // TextView tv_app_version_name;146w231Q
    WebView aboutus;
    private static final String API_KEY = "77s8qs6oqrmcqi";
    //This is the private api key of our application
    private static final String SECRET_KEY = "MtqzP0woROUIVmDu";
    //This is any string we want to use. This will be used for avoiding CSRF attacks. You can generate one here: http://strongpasswordgenerator.com/
    private static final String STATE = "gdfgfdhdfhgsdfg";
    //This is the url that LinkedIn Auth process will redirect to. We can put whatever we want that starts with http:// or https:// .
//We use a made up url that we will intercept when redirecting. Avoid Uppercases.
    private static final String REDIRECT_URI = "https://www.webmobi.com/77s8qs6oqrmcqi/callback";
    /*********************************************/

//These are constants used for build the urls
    private static final String AUTHORIZATION_URL = "https://www.linkedin.com/uas/oauth2/authorization";
    private static final String ACCESS_TOKEN_URL = "https://www.linkedin.com/uas/oauth2/accessToken";
    private static final String SECRET_KEY_PARAM = "client_secret";
    private static final String RESPONSE_TYPE_PARAM = "response_type";
    private static final String GRANT_TYPE_PARAM = "grant_type";
    private static final String GRANT_TYPE = "authorization_code";
    private static final String RESPONSE_TYPE_VALUE = "code";
    private static final String CLIENT_ID_PARAM = "client_id";
    private static final String STATE_PARAM = "state";
    private static final String REDIRECT_URI_PARAM = "redirect_uri";
    /*---------------------------------------*/
    private static final String QUESTION_MARK = "?";
    private static final String AMPERSAND = "&";
    private static final String EQUALS = "=";
    private LinkedinUser linkedInUser = new LinkedinUser();
    private static final String TAG = com.singleevent.sdk.View.LeftActivity.LinkedinProfile.class.getSimpleName();



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Paper.init(this);
        setContentView(R.layout.linkedinprofile);
        appDetails = Paper.book().read("Appdetails");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //    tv_app_version_name = (TextView) findViewById(R.id.tv_app_version_name);
        toolbar.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));
        setSupportActionBar(toolbar);


        // getting the data from previous activity

        if (getIntent().getExtras() == null)
            finish();


        title = getIntent().getExtras().getString("Value");

        aboutus = (WebView)findViewById(R.id.linkedinweb);


        aboutus.requestFocus(View.FOCUS_DOWN);
        aboutus.clearHistory();
        aboutus.clearCache(true);



        aboutus.setWebViewClient(new LinkedinAppProfile.myWebClient(){

            @Override
            public void onPageFinished(WebView view, String url) {
                //  hideProgressDialog();
            }

            //to support below Android N we need to use the deprecated method only
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String authorizationUrl) {

                // showProgressDialog();

                if (authorizationUrl.startsWith(REDIRECT_URI)) {

                    Uri uri = Uri.parse(authorizationUrl);
                    String stateToken = uri.getQueryParameter(STATE_PARAM);
                    if (stateToken == null || !stateToken.equals(STATE)) {
                        //  Log.e(LinkedInBuilder.TAG, "State token doesn't match");
                        return true;
                    }

                    //If the user doesn't allow authorization to our application, the authorizationToken Will be null.
                    String authorizationToken = uri.getQueryParameter(RESPONSE_TYPE_VALUE);
                    if (authorizationToken == null) {
                      /*  Intent intent = new Intent();
                        intent.putExtra("err_code", LinkedInBuilder.ERROR_USER_DENIED);
                        intent.putExtra("err_message", "Authorization not received. User didn't allow access to account.");
                        setResult(Activity.RESULT_CANCELED, intent);*/
                        finish();
                    }
                    String accessTokenUrl = getAccessTokenUrl(authorizationToken);
                    //We make the request in a AsyncTask
                    GetAccessToken(authorizationToken);

                    //  new RetrieveDataAsyncTask().execute(authorizationToken);


                } else {
                    //Default behaviour
                    aboutus.loadUrl(authorizationUrl);
                }
                return true;
            }
        });

        aboutus.loadUrl(url);
    }

    private class myWebClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String authorizationUrl) {
//            if (url != null && (url.startsWith("http://") || url.startsWith("https://"))) {
            if (authorizationUrl.startsWith(REDIRECT_URI)) {

                Uri uri = Uri.parse(authorizationUrl);
                String stateToken = uri.getQueryParameter(STATE_PARAM);
                if (stateToken == null || !stateToken.equals(STATE)) {
                    //  Log.e(LinkedInBuilder.TAG, "State token doesn't match");
                    return true;
                }

                //If the user doesn't allow authorization to our application, the authorizationToken Will be null.
                String authorizationToken = uri.getQueryParameter(RESPONSE_TYPE_VALUE);
                if (authorizationToken == null) {
                      /*  Intent intent = new Intent();
                        intent.putExtra("err_code", LinkedInBuilder.ERROR_USER_DENIED);
                        intent.putExtra("err_message", "Authorization not received. User didn't allow access to account.");
                        setResult(Activity.RESULT_CANCELED, intent);*/
                    finish();
                }

                //We make the request in a AsyncTask
                GetAccessToken(authorizationToken);

                //  new RetrieveDataAsyncTask().execute(authorizationToken);


            } else {
                //Default behaviour
                aboutus.loadUrl(authorizationUrl);
            }
            return true;

        }

        @Override
        public void onReceivedSslError(WebView view, final SslErrorHandler handler,
                                       SslError error) {


            final AlertDialog.Builder builder = new AlertDialog.Builder(LinkedinAppProfile.this);
            builder.setMessage("SSL Certificate Error");
            builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    handler.proceed();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    handler.cancel();
                }
            });
            final AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    private boolean validateEmail(String url) {
        boolean isValidEmail = false;
        String regex = "^(.+)@(.+)$";

        Pattern pattern = Pattern.compile(regex);


        Matcher matcher = pattern.matcher(url);
        isValidEmail = matcher.matches();

        return isValidEmail;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        SpannableString s = new SpannableString("LinkedIn");
        setTitle(Util.applyFontToMenuItem(this, s));
        //tv_app_version_name.setText("Version : " + DataBaseStorage.getAppVersionName(this));

    }
    private static String getAccessTokenUrl(String authorizationToken) {
        return ACCESS_TOKEN_URL
                + QUESTION_MARK
                + GRANT_TYPE_PARAM + EQUALS + GRANT_TYPE
                + AMPERSAND
                + RESPONSE_TYPE_VALUE + EQUALS + authorizationToken
                + AMPERSAND
                + CLIENT_ID_PARAM + EQUALS + API_KEY
                + AMPERSAND
                + REDIRECT_URI_PARAM + EQUALS + REDIRECT_URI
                + AMPERSAND
                + SECRET_KEY_PARAM + EQUALS + SECRET_KEY;
    }



    private void GetAccessToken(String accessTokenUrl) {

        final ProgressDialog dialog = new ProgressDialog(LinkedinAppProfile.this, R.style.MyAlertDialogStyle);
        dialog.setMessage("Please Wait...");
        dialog.show();
        String tag_string_req = "MyProfile";
        String url = "https://www.linkedin.com/oauth/v2/accessToken";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                try {
                    System.out.println(response);
                    JSONObject jsonObject = new JSONObject(response);
                    String token =jsonObject.getString("access_token");
                    long expiresOn = jsonObject.getLong("expires_in");
                    retrieveBasicProfile(token);




                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                if (error instanceof TimeoutError) {
                    Error_Dialog.show("Timeout", LinkedinAppProfile.this);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, LinkedinAppProfile.this), LinkedinAppProfile.this);
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show("Please Check Your Internet Connection", LinkedinAppProfile.this);
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("grant_type","authorization_code");
                params.put("code",accessTokenUrl);
                params.put("redirect_uri",REDIRECT_URI);
                params.put("client_id",API_KEY);
                params.put("client_secret",SECRET_KEY);

                System.out.println(params);
                return params;
            }


        };


        App.getInstance().addToRequestQueue(strReq, tag_string_req);
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

    }
    private void retrieveBasicProfile(String token) throws IOException, JSONException {

        String profileUrl = "https://api.linkedin.com/v2/me?projection=(id,firstName,lastName,profilePicture(displayImage~:playableStreams))";
        getData(token);

    }
    public static String sendGet(String url, String accessToken) throws IOException {
        URL obj = new URL(url);
        int responseCode = 0;
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Authorization", "Bearer" + accessToken);
        con.setRequestProperty("cache-control", "no-cache");
        con.setRequestProperty("X-Restli-Protocol-Version", "2.0.0");
        try{ responseCode = con.getResponseCode();}catch (Exception e){}
        System.out.println("Response Code :: " + responseCode);
        if (responseCode == HttpURLConnection.HTTP_OK) { // connection ok
            BufferedReader in = new BufferedReader(new InputStreamReader( con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return response.toString();
        } else {

            BufferedReader err=new BufferedReader( new InputStreamReader(con.getErrorStream()));
            StringBuilder sb = new StringBuilder();
            String line="";
            while((line = err.readLine()) != null) {
                sb.append(line);
                break;
            }
            err.close();
            Log.e(TAG, "Error Code "+con.getResponseCode());
            Log.e(TAG, "Error Message "+sb.toString());

            return null;
        }
    }

    private void getData(String token) {
        String profileUrl = "https://api.linkedin.com/v2/me?projection=(id,firstName,lastName,profilePicture(displayImage~:playableStreams))";

        final ProgressDialog dialog = new ProgressDialog(LinkedinAppProfile.this,R.style.MyAlertDialogStyle);
        dialog.setMessage("Loading data");
        dialog.show();
        String tag_string_req = "Login";
        String url =profileUrl;
        StringRequest strReq = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                try {
                    System.out.println(response);
                    if (response != null) {

                        JSONObject jsonObject = new JSONObject(response);

                        try {
                            String linkedInUserId = jsonObject.getString("id");
                            String country = jsonObject.getJSONObject("firstName").getJSONObject("preferredLocale").getString("country");
                            String language = jsonObject.getJSONObject("firstName").getJSONObject("preferredLocale").getString("language");
                            String firstNameKey = language + "_" + country;
                            String linkedInUserFirstName = jsonObject.getJSONObject("firstName").getJSONObject("localized").getString(firstNameKey);
                            String linkedInUserLastName = jsonObject.getJSONObject("lastName").getJSONObject("localized").getString(firstNameKey);
                            String linkedInUserProfile = jsonObject.getJSONObject("profilePicture").getJSONObject("displayImage~").getJSONArray("elements").getJSONObject(0).getJSONArray("identifiers").getJSONObject(0).getString("identifier");

                            linkedInUser.setId(linkedInUserId);
                            linkedInUser.setFirstName(linkedInUserFirstName);
                            linkedInUser.setLastName(linkedInUserLastName);
                            //  Error_Dialog.show(linkedInUserFirstName+""+profileUrl,LinkedinProfile.this );
                            Intent i=new Intent (LinkedinAppProfile.this,Profile.class);
                            i.putExtra("fname",linkedInUserFirstName);
                            i.putExtra("lname",linkedInUserLastName);
                            i.putExtra("image",linkedInUserProfile);
                            startActivity(i);
                            finish();
                        }catch (Exception E){

                        }
                        // linkedInUser.setProfileUrl(linkedInUserProfile);


                    } else {
                        Log.e(TAG, "Failed To Retrieve Basic Profile");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                if (error instanceof TimeoutError) {
                    Error_Dialog.show("Timeout", LinkedinAppProfile.this);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, getApplicationContext()), LinkedinAppProfile.this);
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show("Please Check Your Internet Connection", LinkedinAppProfile.this);
                }

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };


        App.getInstance().addToRequestQueue(strReq, tag_string_req);
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));


    }
}
