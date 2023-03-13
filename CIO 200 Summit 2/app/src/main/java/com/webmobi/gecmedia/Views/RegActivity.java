package com.webmobi.gecmedia.Views;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.Html;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.singleevent.sdk.App;
import com.singleevent.sdk.Custom_View.Util;
import com.webmobi.gecmedia.Config.ApiList;
import com.singleevent.sdk.Custom_View.Error_Dialog;
import com.webmobi.gecmedia.CustomViews.VolleyErrorLis;
import com.webmobi.gecmedia.LinkedinSDK.APIHelper;
import com.webmobi.gecmedia.LinkedinSDK.LISessionManager;
import com.webmobi.gecmedia.LinkedinSDK.errors.LIApiError;
import com.webmobi.gecmedia.LinkedinSDK.errors.LIAuthError;
import com.webmobi.gecmedia.LinkedinSDK.listeners.ApiListener;
import com.webmobi.gecmedia.LinkedinSDK.listeners.ApiResponse;
import com.webmobi.gecmedia.LinkedinSDK.listeners.AuthListener;
import com.webmobi.gecmedia.LinkedinSDK.utils.Scope;
import com.webmobi.gecmedia.Models.MultiEvent;
import com.webmobi.gecmedia.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cyd.awesome.material.AwesomeText;
import cyd.awesome.material.FontCharacterMaps;
import io.paperdb.Paper;

public class RegActivity extends AppCompatActivity implements View.OnClickListener/* GoogleApiClient.OnConnectionFailedListener*/ {

    private static final String TAG = RegActivity.class.getSimpleName();

    RelativeLayout facebook, linkedin, create, googleplus, nextemail;
    AwesomeText close;
    TextView login, fac, link, email;
    int Facebookrequestcode;
    private CallbackManager callbackManager;
    private LoginButton loginButton;
    EditText txtgplus;
    Button sigin_in_email;
    String emailid;

    private String eid = "a5d72f633c6f8a0e964525123a500376a26d";
    String regId;
    int loginresult = 9001;
    int regresult = 9002;
    Context context;
    String txtemail = null, txtname = null, lname = null, profilePicUrl = null, facebook_id = null, linkedin_id = null, mediatype = null;
    JSONArray myevents;
    Button d1, d2;
    int pos = 0;
    private static final String host = "api.linkedin.com";
    private static final String topCardUrl = "https://" + host + "/v1/people/~:" +
            "(id,first-name,email-address,last-name,formatted-name,phone-numbers,public-profile-url,picture-url,picture-urls::(original))";
    String baseUrl = "https://webmobi.s3.amazonaws.com/nativeapps/";
    String filename = "app.json";
    ProgressDialog pdialog;

    private ProgressDialog mProgressDialog;

    private static final int RC_SIGN_IN = 4;
    RelativeLayout remail;
    TextView emailaddress;
    RelativeLayout pwdlogin, emaillogin;
    EditText password;
    AwesomeText passwordshow;
    boolean showflag = false;
    Button sigin,login_with_email;
    String spassword;
    private ProgressDialog dialog;
    ArrayList<MultiEvent> multieventlist;
    TextView emailchange,message_for_login;
    boolean chek_islogin;
    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Paper.init(this);
        //FacebookSdk.sdkInitialize(this);
        // AppEventsLogger.activateApp(this);
        callbackManager = CallbackManager.Factory.create();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        context = this;
        regId = Paper.book().read("regId");
        chek_islogin=Paper.book().read("check_auth_for_login", true);
        loginButton = (LoginButton) findViewById(R.id.login_button);
        facebook = (RelativeLayout) findViewById(R.id.facebook);
        googleplus = (RelativeLayout) findViewById(R.id.googleplus);
        nextemail = (RelativeLayout) findViewById(R.id.nextemail);
        linkedin = (RelativeLayout) findViewById(R.id.linkedin);
        create = (RelativeLayout) findViewById(R.id.create);
        sigin_in_email = (Button) findViewById(R.id.sigin_in_email);
        close = (AwesomeText) findViewById(R.id.close);
        login = (TextView) findViewById(R.id.login);
        fac = (TextView) findViewById(R.id.txtfac);
        link = (TextView) findViewById(R.id.txtlink);
        email = (TextView) findViewById(R.id.txtemail);
        txtgplus = (EditText) findViewById(R.id.txtgplus);
        emailaddress = (TextView) findViewById(R.id.emailaddress);
        pwdlogin = (RelativeLayout) findViewById(R.id.pwdlogin);
        emaillogin = (RelativeLayout) findViewById(R.id.emaillogin);
        password = (EditText) findViewById(R.id.password);
        password.addTextChangedListener(watch);
        passwordshow = (AwesomeText) findViewById(R.id.show);
        remail = (RelativeLayout) findViewById(R.id.remail);
        emailchange = (TextView) findViewById(R.id.emailchange);
        message_for_login = (TextView) findViewById(R.id.message_for_login);
        sigin = (Button) findViewById(R.id.sigin_in);
        login_with_email = (Button) findViewById(R.id.login_with_email);
        passwordshow.setOnClickListener(this);
        password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        showflag = true;
        d1 = (Button) findViewById(R.id.d1);
        d2 = (Button) findViewById(R.id.d22);
        email.setTypeface(Util.regulartypeface(context));
        fac.setTypeface(Util.regulartypeface(context));
        link.setTypeface(Util.regulartypeface(context));
        login.setTypeface(Util.regulartypeface(context));
        login.setText(Html.fromHtml(getResources().getString(R.string.Login)));
        if(!chek_islogin){
            sigin_in_email.setVisibility(View.GONE);
            login_with_email.setVisibility(View.VISIBLE);
            message_for_login.setText("Enter email to Login into the app");
        }
        login.setVisibility(View.GONE);
        facebook.setOnClickListener(this);
        login_with_email.setOnClickListener(this);
        googleplus.setOnClickListener(this);
        sigin_in_email.setOnClickListener(this);
        nextemail.setOnClickListener(this);
        linkedin.setOnClickListener(this);
        create.setOnClickListener(this);
        close.setOnClickListener(this);
        login.setOnClickListener(this);
        facebook.setVisibility(View.GONE);
        linkedin.setVisibility(View.GONE);
        d1.setOnClickListener(this);
        d2.setOnClickListener(this);
        emailchange.setOnClickListener(this);

        sigin.setOnClickListener(this);
        passwordshow.setOnClickListener(this);
//        signin_btn = (SignInButton) findViewById(R.id.gp_sign_in);
//        signin_btn.setOnClickListener(this);
        loginButton.setReadPermissions("public_profile", "email", "user_friends");
        //google plus
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();



    }




    @Override
    public void onClick(View view) {
        Intent i;

        switch (view.getId()) {
            case R.id.close:
                if (getIntent().getAction() != null && getIntent().getAction().equalsIgnoreCase("com.useraction")) {
                    setResult(RESULT_CANCELED);
                    finish();

                } else if (getIntent().getAction() != null && getIntent().getAction().equalsIgnoreCase("com.singleuseraction")) {
                    setResult(RESULT_CANCELED);
                    finish();
                } else {

                    Intent sending = new Intent(RegActivity.this, HomeScreen.class);
                    sending.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(sending);
                    finish();
                }
                break;
            case R.id.login:
                i = new Intent(RegActivity.this, LoginActivity.class);
                startActivityForResult(i, loginresult);
                finish();
                break;
            case R.id.emailchange:
                pwdlogin.setVisibility(View.GONE);
                emaillogin.setVisibility(View.VISIBLE);
                break;

            case R.id.create:
                i = new Intent(RegActivity.this, Signup.class);
                startActivityForResult(i, regresult);
                break;

            case R.id.facebook:

                loginButton.performClick();

                loginButton.setPressed(true);

                loginButton.invalidate();

                loginButton.registerCallback(callbackManager, mCallBack);

                Facebookrequestcode = loginButton.getRequestCode();

                loginButton.setPressed(false);

                loginButton.invalidate();


                break;

            case R.id.linkedin:
                linkedinlogin();
                break;

            case R.id.googleplus:
                signIn();
               
                break;

            case R.id.sigin_in_email:
                emailid = txtgplus.getText().toString();
                Paper.book().write("usermail", emailid);
                apitogetpwd();
               /* i = new Intent(RegActivity.this, LoginActivityDemo.class);
                startActivityForResult(i, loginresult);*/
                break;
                case R.id.login_with_email:
                emailid = txtgplus.getText().toString();
                Paper.book().write("usermail", emailid);
                ApiToLogin();
                break;
            case R.id.show:
                if (showflag) {
                    password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    showflag = false;
                    passwordshow.setMaterialDesignIcon(FontCharacterMaps.MaterialDesign.MD_VISIBILITY);

                } else {
                    password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    showflag = true;
                    passwordshow.setMaterialDesignIcon(FontCharacterMaps.MaterialDesign.MD_VISIBILITY_OFF);
                }
                break;



        }


    }

    private void ApiToLogin() {
        pdialog = new ProgressDialog(RegActivity.this);
        pdialog.setMessage("Login...");
        pdialog.show();


        String tag_string_req = "GetPwd";
        String url = ApiList.multievent_Email_login;
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pdialog.dismiss();
                try {
                    System.out.print(response);
                    JSONObject jObj = new JSONObject(response);
                    if (jObj.getBoolean("response")) {

                        if(jObj.getInt("eventStatus")==0) {

                            Paper.book().write("Email", emailid);
                            Paper.book().write("email", emailid);
                            Paper.book().write("Password", "123456");

                            Paper.book().write("Islogin", true);
                            Paper.book().write("userId", jObj.getJSONObject("responseString").getString("userId"));
                            Paper.book().write("username", jObj.getJSONObject("responseString").getString("username"));
                            Paper.book().write("token", jObj.getJSONObject("responseString").getString("token"));
                            Intent i1;
                            i1 = new Intent(context, HomeScreenMulti.class);
                            i1.putExtra("Event_ID", eid);
                            i1.putExtra("Event_Name", "GEC Media Group");
                            i1.putExtra("Event_Logo", "https://webmobi.s3.amazonaws.com/nativeapps/46699/1662555715113_2022_Conference_Lockup_LICSPA_Blue_2.png");
                            i1.putExtra("Event_Theme", "F814A7");
                            i1.putExtra("Multi_event_logo", "https://webmobi.s3.amazonaws.com/nativeapps/devwebmobitechconference2018/download__1_.jhttps://webmobi.s3.amazonaws.com/nativeapps/10105/1667577338751_Event_Logo_The_world_CIO_200_1024x1024px.pngpg");
                            startActivity(i1);
                            finish();
                        }else{
                            Error_Dialog.show("Email Sent Successfully", RegActivity.this);
                            Paper.book().write("responseforemail", "Email Sent Successfully");
                            //Toast.makeText(getApplicationContext(),"Passkey sent to your Mail Id",Toast.LENGTH_LONG);
                            emailaddress.setVisibility(View.VISIBLE);
                            emailaddress.setText(emailid);
                            emaillogin.setVisibility(View.GONE);
                            pwdlogin.setVisibility(View.VISIBLE);
                        }

                    } else {
                        Error_Dialog.show(jObj.getString("responseString"), RegActivity.this);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pdialog.dismiss();
                DeviceLog("EMAIL LOGIN Error", error.toString());

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("event_id", eid);
                params.put("email", emailid);
                params.put("action", "generate");
                params.put("passkey", "");
                params.put("deviceId", regId);
                params.put("deviceType:", "android");
                System.out.println(params);
                return params;
            }

        };

        App.getInstance().addToRequestQueue(strReq);
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));


    }


    // This method is used to make permissions to retrieve data from linkedin

    private static Scope buildScope() {
        return Scope.build(Scope.R_BASICPROFILE, Scope.R_EMAILADDRESS);
    }

    public void linkedinlogin() {


        LISessionManager.getInstance(getApplicationContext()).init(this, buildScope(), new AuthListener() {
            @Override
            public void onAuthSuccess() {

                // Toast.makeText(getApplicationContext(), "success" + LISessionManager.getInstance(getApplicationContext()).getSession().getAccessToken().toString(), Toast.LENGTH_LONG).show();

            }

            @Override
            public void onAuthError(LIAuthError error) {

                Toast.makeText(getApplicationContext(), "failed " + error.toString(),
                        Toast.LENGTH_LONG).show();
            }
        }, true);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (Facebookrequestcode == requestCode) {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        } else if (requestCode == loginresult) {

            if (resultCode == RESULT_OK)
                viewtran();

        } else if (requestCode == regresult) {

            if (resultCode == RESULT_OK)
                viewtran();


        } else {
            LISessionManager.getInstance(getApplicationContext()).onActivityResult(this, requestCode, resultCode, data);
            linkededinApiHelper();
        }
    }


    private void viewtran() {

        if (getIntent().getAction() != null && getIntent().getAction().equalsIgnoreCase("com.useraction")) {

            setResult(RESULT_OK);
            finish();

        } else if (getIntent().getAction() != null && getIntent().getAction().equalsIgnoreCase("com.singleuseraction")) {
            setResult(RESULT_OK);
            finish();
        } else {

            Intent sending = new Intent(RegActivity.this, HomeScreen.class);
            sending.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(sending);
            finish();
        }

    }


    private FacebookCallback<LoginResult> mCallBack = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {

//            progressDialog.dismiss();

            // App code
            GraphRequest request = GraphRequest.newMeRequest(
                    loginResult.getAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject object, GraphResponse response) {

                            Log.e("response: ", response.getJSONObject() + "");

                            try {
                                if (object.has("email")) {
                                    facebook_id = object.getString("id").toString();
                                    txtemail = object.getString("email").toString();
                                    txtname = object.getString("first_name").toString();
                                    lname = object.getString("last_name").toString();
                                    profilePicUrl = object.getJSONObject("picture").getJSONObject("data").getString("url");
                                    mediatype = "facebook";
                                    linkedin_id = "";
                                    SocialMediaLogin();

                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            LoginManager.getInstance().logOut();


                        }

                    });

            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,email, first_name, last_name, picture");
            request.setParameters(parameters);
            request.executeAsync();
        }

        @Override
        public void onCancel() {
//            progressDialog.dismiss();
        }

        @Override
        public void onError(FacebookException e) {
            Log.d("Tag", e.getMessage());
//            progressDialog.dismiss();
        }
    };

    private void SocialMediaLogin() {

        Loginuser();


    }

    private void Loginuser() {


        String tag_string_req = "Login";
        String url = ApiList.Social_Media_Login;
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                try {
                    System.out.println(response);
                    JSONObject jObj = new JSONObject(response);


                    if (jObj.getBoolean("response")) {
                        Paper.book().write("Islogin", true);
                        Paper.book().write("token", jObj.getJSONObject("responseString").getString("token"));
                        Paper.book().write("userId", jObj.getJSONObject("responseString").getString("userId"));
                        Paper.book().write("username", jObj.getJSONObject("responseString").getString("username"));
                        Paper.book().write("Email", txtemail);
                        Paper.book().write("email", txtemail);
                        Paper.book().write("Password", "");
                        viewtran();


                    } else {
                        Error_Dialog.show(jObj.getString("responseString"), RegActivity.this);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {


            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("media_type", mediatype);
                params.put("deviceType", "android");
                params.put("deviceId", regId);
                params.put("profile_pic_url", profilePicUrl);
                params.put("email", txtemail);
                params.put("last_name", lname);
                params.put("first_name", txtname);
                params.put("fb_id", facebook_id);
                params.put("linked_in_id", linkedin_id);
                System.out.println(params);
                return params;
            }

        };

        App.getInstance().addToRequestQueue(strReq);
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

    }

    public void apitogetpwd() {
        pdialog = new ProgressDialog(RegActivity.this);
        pdialog.setMessage("Login...");
        pdialog.show();


        String tag_string_req = "GetPwd";
//        String url = ApiList.Container_login;
        String url = ApiList.multievent_Email_login;
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pdialog.dismiss();
                try {
                    System.out.print(response);
                    JSONObject jObj = new JSONObject(response);
                    String rp = jObj.getString("responseString");
                    if (jObj.getBoolean("response")) {
                        try {
                            Error_Dialog.show("Email Sent Successfully", RegActivity.this);
                            Paper.book().write("responseforemail", "Email Sent Successfully");
                            //Toast.makeText(getApplicationContext(),"Passkey sent to your Mail Id",Toast.LENGTH_LONG);
                            emailaddress.setVisibility(View.VISIBLE);
                            emailaddress.setText(emailid);
                            emaillogin.setVisibility(View.GONE);
                            pwdlogin.setVisibility(View.VISIBLE);
                           /*Intent i;
                           i = new Intent(RegActivity.this, LoginActivity.class);
                           startActivityForResult(i, loginresult);
                           finish();*/
                        } catch (Exception e) {
                        }
                    } else {
                        Error_Dialog.show(rp, RegActivity.this);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pdialog.dismiss();
                DeviceLog("EMAIL LOGIN Error", error.toString());

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("event_id", eid);
                params.put("email", emailid);
                params.put("action", "generate");
                params.put("passkey", "");
                params.put("deviceId", regId);
                params.put("deviceType:", "android");
                System.out.println(params);
                return params;
            }

        };

        App.getInstance().addToRequestQueue(strReq);
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

    }

    /// device info
    public void DeviceLog(String info, String error) {

        final String info1, error1;
        info1 = info;
        error1 = error;


        pdialog = new ProgressDialog(RegActivity.this, R.style.MyAlertDialogStyle);
        pdialog.setMessage("checking...");
        pdialog.show();


        String tag_string_req = "Login";
        String url = ApiList.DeviceLog;
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                pdialog.dismiss();
                try {
                    System.out.println(response);
                    JSONObject jObj = new JSONObject(response);


                    if (jObj.getBoolean("response")) {

                        finish();


                    } else {
                        Error_Dialog.show(jObj.getString("responseString"), RegActivity.this);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pdialog.dismiss();
                if (error instanceof TimeoutError) {
                    Error_Dialog.show("Timeout", RegActivity.this);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, context), RegActivity.this);
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show("Please Check Your Internet Connection", RegActivity.this);
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("appid", eid);
                params.put("userid", "");
                params.put("deviceType", "android");
                params.put("deviceId", regId);
                params.put("info", info1);
                params.put("log", error1);
                params.put("device_info", "");
                params.put("device_version", "");


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


    public void linkededinApiHelper() {
        APIHelper apiHelper = APIHelper.getInstance(getApplicationContext());
        apiHelper.getRequest(RegActivity.this, topCardUrl, new ApiListener() {
            @Override
            public void onApiSuccess(ApiResponse result) {
                try {
                    System.out.println(result.getResponseDataAsJson());
                    JSONObject jobject = result.getResponseDataAsJson();
                    txtemail = jobject.getString("emailAddress");
                    txtname = jobject.getString("firstName");
                    lname = jobject.getString("lastName");
                    linkedin_id = jobject.getString("id");

                    profilePicUrl = jobject.isNull("pictureUrl") ? "" : jobject.getString("pictureUrl");
                    mediatype = "linkedin";
                    facebook_id = "";

                    SocialMediaLogin();


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onApiError(LIApiError error) {
                // ((TextView) findViewById(R.id.error)).setText(error.toString());

            }
        });
    }


    /*@Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }*/

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Logging in...");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    private void signIn() {
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }

    private void updateUI(GoogleSignInAccount account) {

    }


    ////Added from Login Acivity

    TextWatcher watch = new TextWatcher() {

        @Override
        public void afterTextChanged(Editable arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onTextChanged(CharSequence s, int a, int b, int c) {


            if (checkallfeilds()) {
                sigin.setSelected(true);
                sigin.setEnabled(true);
            } else {
                sigin.setSelected(false);
                sigin.setEnabled(false);
            }

        }
    };


    private boolean checkallfeilds() {

        boolean flag = true;

        /*semail = email.getText().toString();*/
        //  semail=Paper.book().read("usermail");

        //if (TextUtils.isEmpty(semail) || !Patterns.EMAIL_ADDRESS.matcher(semail).matches())
        //    flag = false;

        spassword = password.getText().toString().trim();
        try {
            if (spassword.length() == 6) {
                PLoginuser();
            } else {

            }
        } catch (Exception e) {
        }

/*
        if (spassword.length() < 4 ){
            flag = false;
         //   Error_Dialog.show("Password length should be minimum 4", LoginActivity.this);

        }*/


        return flag;
    }

    private void PLoginuser() {

        dialog = new ProgressDialog(RegActivity.this, R.style.MyAlertDialogStyle);
        dialog.setMessage("Login...");
        dialog. show();

        String tag_string_req = "Login";
//        String url = ApiList.Container_login;
        String url = ApiList.multievent_Email_login; //changed
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                try {
                    System.out.println(response);
                    JSONObject jObj = new JSONObject(response);
                    if (jObj.getBoolean("response")) {

                        Paper.book().write("Email", emailid);
                        Paper.book().write("email", emailid);
                        Paper.book().write("Password", spassword);
                        Paper.book().write("Islogin", true);

                        Paper.book().write("token", jObj.getJSONObject("responseString").getString("token"));
                        Paper.book().write("userId", jObj.getJSONObject("responseString").getString("userId"));
                        Paper.book().write("username", jObj.getJSONObject("responseString").getString("username"));
                        Log.d("show_data", "onResponse: " + jObj);
                        //ee4627

                        Intent i1;
                        i1 = new Intent(context, HomeScreenMulti.class);
                        i1.putExtra("Event_ID", eid);
                        i1.putExtra("Event_Name", "GEC Media Group");
                        i1.putExtra("Event_Logo", "https://webmobi.s3.amazonaws.com/nativeapps/devwebmobitechconference2018/download__1_.jhttps://webmobi.s3.amazonaws.com/nativeapps/10105/1667577338751_Event_Logo_The_world_CIO_200_1024x1024px.pngpg");
                        i1.putExtra("Event_Theme", "F814A7");
                        i1.putExtra("Multi_event_logo", "https://webmobi.s3.amazonaws.com/nativeapps/devwebmobitechconference2018/download__1_.jhttps://webmobi.s3.amazonaws.com/nativeapps/10105/1667577338751_Event_Logo_The_world_CIO_200_1024x1024px.pngpg");
                        startActivity(i1);
                        finish();

                    } else {
                        Error_Dialog.show(jObj.getString("responseString"), RegActivity.this);
                    }

                } catch (JSONException e) {
                    Error_Dialog.show(e.toString(), RegActivity.this);
                }

            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                DeviceLog("LOGINUSER", error.toString());
                if (error instanceof TimeoutError) {
                    Error_Dialog.show("Timeout", RegActivity.this);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, context), RegActivity.this);
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show("Please Check Your Internet Connection", RegActivity.this);
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                //params.put("event_id", eid);

                params.put("event_id", eid); //added change
                params.put("email", emailid);
                params.put("action", "verify");
                params.put("passkey", spassword);
                params.put("deviceId", regId);
                params.put("deviceType", "android");

                System.out.println(params);
                return params;
            }

           /* @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                // add headers <key,value>
                String credentials = Paper.book().read("usermail") + ":" + password.getText().toString();
                String auth = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                headers.put("Authorization", auth);
                return headers;
            }*/
        };


        App.getInstance().addToRequestQueue(strReq, tag_string_req);
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));


    }

    private void parsemulti(JSONArray events) {
        int n = events.length();
        multieventlist = new ArrayList<>();
        int pos = 0;
        //multieventid=new ArrayList<>();

        try {
            Gson gson = new Gson();
            for (int i = 0; i < events.length(); i++) {
                String eventString = events.getJSONObject(i).toString();

                MultiEvent obj = gson.fromJson(eventString, MultiEvent.class);
                multieventlist.add(obj);

                //   MultiEvent obj = gson.fromJson(eventString, Event.class);
            }

            String s = Paper.book().read("AppType", "");
            if (s.equalsIgnoreCase("Branded")) {

                try {
                    if (multieventlist.size() > 0 && multieventlist != null) {
                        for (int i = 0; i < multieventlist.size(); i++) {
                            if (multieventlist.get(i).getEvent_id().equalsIgnoreCase("a5d72f633c6f8a0e964525123a500376a26d")) {
                                pos = i;
                                Paper.book().write("Multieventpos", pos);
                                Paper.book().write("MultiEvents", multieventlist);
                            }
                        }
                        Intent i1;
                        i1 = new Intent(context, HomeScreenMulti.class);
                        i1.putExtra("Event_ID", multieventlist.get(pos).getEvent_id());
                        i1.putExtra("Event_Name", multieventlist.get(pos).getEvent_name());
                        i1.putExtra("Event_Logo", multieventlist.get(pos).getApp_image());
                        i1.putExtra("Event_Theme", multieventlist.get(pos).getColor_code());
//                        i1.putExtra("Multi_event_logo", "https://webmobi.s3.amazonaws.com/nativeapps/engagement2020/1595330656633_k4YFFP9v_400x400.png");
                        i1.putExtra("Multi_event_logo", multieventlist.get(pos).getMulti_event_logo());
                        startActivity(i1);
                        finish();
                    } else {
                        Intent i1;
                        i1 = new Intent(context, HomeScreenMulti.class);
                        i1.putExtra("Event_ID", "a5d72f633c6f8a0e964525123a500376a26d");
                        i1.putExtra("Event_Name", "WebMOBI CMO 2020");
                        i1.putExtra("Event_Logo", "");
                        i1.putExtra("Event_Theme", "");
                        i1.putExtra("Multi_event_logo", "");
                        startActivity(i1);
                        finish();
                    }
                } catch (Exception e) {

                }
            } else {
                setResult(RESULT_OK);
                finish();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
