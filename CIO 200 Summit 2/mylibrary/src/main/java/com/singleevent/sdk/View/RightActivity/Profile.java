package com.singleevent.sdk.View.RightActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.StrictMode;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.gson.Gson;
import com.singleevent.sdk.ApiList;
import com.singleevent.sdk.App;
import com.singleevent.sdk.Custom_View.Error_Dialog;
import com.singleevent.sdk.Custom_View.Util;
import com.singleevent.sdk.Custom_View.VolleyErrorLis;
import com.singleevent.sdk.View.LeftActivity.linkedInModule.LinkedInLoginActivity;
import com.singleevent.sdk.View.LeftActivity.twitterModule.TwitterAuthentication;
import com.singleevent.sdk.model.AppDetails;
import com.singleevent.sdk.model.User_Details;
import com.singleevent.sdk.R;
import com.singleevent.sdk.View.LoginAndRegister.LinkedinSDK.APIHelper;
import com.singleevent.sdk.View.LoginAndRegister.LinkedinSDK.LISessionManager;
import com.singleevent.sdk.View.LoginAndRegister.LinkedinSDK.errors.LIApiError;
import com.singleevent.sdk.View.LoginAndRegister.LinkedinSDK.errors.LIAuthError;
import com.singleevent.sdk.View.LoginAndRegister.LinkedinSDK.listeners.ApiListener;
import com.singleevent.sdk.View.LoginAndRegister.LinkedinSDK.listeners.ApiResponse;
import com.singleevent.sdk.View.LoginAndRegister.LinkedinSDK.listeners.AuthListener;
import com.singleevent.sdk.View.LoginAndRegister.LinkedinSDK.utils.Scope;
import com.singleevent.sdk.utils.DataBaseStorage;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cyd.awesome.material.AwesomeText;
import io.paperdb.Paper;
import timber.log.Timber;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Created by Admin on 6/13/2017.
 */

public class Profile extends AppCompatActivity implements View.OnClickListener {

    private static final String PREF_NAME = "sample_twitter_pref";
    EditText fname, lname, mnumber, email, jpos, company, website, des, blog;
    String sfname, slname, smnumber, semail, sjpos, scompany, swebsite, UserID;
    AwesomeText  logout;
    Button done;
    TextView profession, name, Changepwd, connect_linkedin, connected_linkedin, authenticate_linkedin, connect_facebook, connected_facebook, authenticate_facebook, connect_twitter, connected_twitter, authenticate_twitter;

    RelativeLayout edit;
    LinearLayout linkedin;
    private double CellWidth;
    ImageView ProfilePic, edit_image;
    AppDetails appDetails;

    private static final String host = "api.linkedin.com";
    private static final String topCardUrl = "https://" + host + "/v1/people/~:" +
            "(id,first-name,email-address,last-name,formatted-name,phone-numbers,public-profile-url,picture-url,picture-urls::(original))";
    String dir;
    List<String> savedirs;
    private SharedPreferences mSharedPreferences = null;
    private String mConsumerKey = null;
    private String mConsumerSecret = null;
    private String mCallbackUrl = null;
    private Twitter mTwitter = null;
    private RequestToken mRequestToken = null;
    private static final String PREF_KEY_TWITTER_LOGIN = "is_twitter_loggedin_Authenticated";
    private String mAuthVerifier = null;
    private String mTwitterVerifier = null;
    public static final String PREF_USER_NAME = "twitter_user_name";
    public static final int WEBVIEW_REQUEST_CODE = 100;
    private static final String PREF_KEY_OAUTH_TOKEN = "oauth_token";
    private static final String PREF_KEY_OAUTH_SECRET = "oauth_token_secret";
    public CallbackManager callbackManagerFaceBook;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Paper.init(this);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.s_setting);
        UserID = Paper.book().read("userId");
        appDetails = Paper.book().read("Appdetails");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor( Color.parseColor(appDetails.getTheme_color() ) );
        setSupportActionBar(toolbar);

        // setting view
        name = (TextView) findViewById(R.id.username);
        Changepwd = (TextView) findViewById(R.id.changewd);
        profession = (TextView) findViewById(R.id.profession);

        fname = (EditText) findViewById(R.id.fname);
        lname = (EditText) findViewById(R.id.lname);
        mnumber = (EditText) findViewById(R.id.mphone);
        email = (EditText) findViewById(R.id.temail);
        jpos = (EditText) findViewById(R.id.jobpos);
        company = (EditText) findViewById(R.id.company);
        website = (EditText) findViewById(R.id.website);
        done = (Button) findViewById(R.id.done);
        des = (EditText) findViewById(R.id.des);
        blog = (EditText) findViewById(R.id.burl);
        linkedin = findViewById(R.id.linkedin);

        connect_linkedin = findViewById(R.id.connect_linkedin);
        connected_linkedin = findViewById(R.id.connected_linkedin);
        authenticate_linkedin = findViewById(R.id.authenticate_linkedin);

        connect_facebook = findViewById(R.id.connect_facebook);
        connected_facebook = findViewById(R.id.connected_facebook);
        authenticate_facebook = findViewById(R.id.authenticate_facebook);

        connect_twitter = findViewById(R.id.connect_twitter);
        connected_twitter = findViewById(R.id.connected_twitter);
        authenticate_twitter = findViewById(R.id.authenticate_twitter);

        logout = (AwesomeText) findViewById(R.id.logout);
        done.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));
        fname.addTextChangedListener(fwatch);
        lname.addTextChangedListener(lwatch);
        mnumber.addTextChangedListener(watch);
        email.addTextChangedListener(watch);
        jpos.addTextChangedListener(jwatch);
        company.addTextChangedListener(watch);
        website.addTextChangedListener(watch);

        // setting fonts

        authenticate_linkedin.setOnClickListener(this);
        authenticate_facebook.setOnClickListener(this);
        authenticate_twitter.setOnClickListener(this);
        connect_linkedin.setOnClickListener(this);
        connect_facebook.setOnClickListener(this);
        connect_twitter.setOnClickListener(this);

        fname.setTypeface(Util.regulartypeface(this));
        lname.setTypeface(Util.regulartypeface(this));
        mnumber.setTypeface(Util.regulartypeface(this));
        email.setTypeface(Util.regulartypeface(this));
        jpos.setTypeface(Util.regulartypeface(this));
        company.setTypeface(Util.regulartypeface(this));
        website.setTypeface(Util.regulartypeface(this));
        name.setTypeface(Util.boldtypeface(this));
        des.setTypeface(Util.regulartypeface(this));
        blog.setTypeface(Util.regulartypeface(this));
        profession.setTypeface(Util.lighttypeface(this));

        edit = (RelativeLayout) findViewById(R.id.edit);
        ProfilePic = (ImageView) findViewById(R.id.iv_profile);
        edit_image = (ImageView) findViewById(R.id.iv_edit_image);

        // dynamically setting cell size based on screen width
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        CellWidth = (displayMetrics.widthPixels * 0.20F);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((int) CellWidth, (int) CellWidth);
        edit.setLayoutParams(params);

        edit_image.setOnClickListener(this);
        done.setOnClickListener(this);
        Changepwd.setVisibility(Paper.book().read("normallogin", true) ? View.GONE : View.GONE);
        Changepwd.setOnClickListener(this);
        logout.setOnClickListener(this);
        linkedin.setOnClickListener(this);

        //-------------twitter-------------------------//
        initSDK();
        //-------------twitter end-------------------------//
        //------------faceBook---------------------------//
        computePakageHash();
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManagerFaceBook = CallbackManager.Factory.create();
        faceBookLoginManager();


        //-----------faceBook end---------------//

        disablebutton();
        setprofilepic("");
        Getprofile();


        //dir

        dir = getFilesDir() + File.separator + "EventsDownload" + File.separator;


    }


    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sharedPreferences_linked_in = getSharedPreferences("LinkedIn_Authenticated", MODE_PRIVATE);
        String LinkedIn_id = sharedPreferences_linked_in.getString("id", "");
        String LinkedIn_token = sharedPreferences_linked_in.getString("token", "");
        SharedPreferences sharedPreferences_linkedIn_connected = getSharedPreferences("LinkedIn", MODE_PRIVATE);
        String connected_id_linkedIn = sharedPreferences_linkedIn_connected.getString("id", "");
        String connected_token_linkedIn = sharedPreferences_linkedIn_connected.getString("token", "");
        Log.d("connected_check", "onStart: " + connected_id_linkedIn);
        if (!LinkedIn_id.isEmpty() && !LinkedIn_token.isEmpty()) {
            if (!connected_id_linkedIn.isEmpty() && !connected_token_linkedIn.isEmpty()) {
                connect_linkedin.setVisibility(View.GONE);
                authenticate_linkedin.setVisibility(View.GONE);
                connected_linkedin.setVisibility(View.VISIBLE);
            } else {
                connect_linkedin.setVisibility(View.VISIBLE);
                authenticate_linkedin.setVisibility(View.GONE);
                connected_linkedin.setVisibility(View.GONE);
            }

        } else {
            connect_linkedin.setVisibility(View.GONE);
            authenticate_linkedin.setVisibility(View.VISIBLE);
            connected_linkedin.setVisibility(View.GONE);
        }


        mSharedPreferences = getSharedPreferences(PREF_NAME, 0);
        if (isAuthenticated()) {
            if (mSharedPreferences.getBoolean("is_twitter_loggedin", false)) {
                authenticate_twitter.setVisibility(View.GONE);
                connect_twitter.setVisibility(View.GONE);
                connected_twitter.setVisibility(View.VISIBLE);
            } else {
                authenticate_twitter.setVisibility(View.GONE);
                connect_twitter.setVisibility(View.VISIBLE);
                connected_twitter.setVisibility(View.GONE);
            }
        } else {
            authenticate_twitter.setVisibility(View.VISIBLE);
            connect_twitter.setVisibility(View.GONE);
            connected_twitter.setVisibility(View.GONE);
        }

        SharedPreferences sharedPreferences_faceBook = getSharedPreferences("faceBookAuth", MODE_PRIVATE);
        String fb_user_id = sharedPreferences_faceBook.getString("id", "");
        String fb_access_token = sharedPreferences_faceBook.getString("token", "");
        boolean fb_logged_in = sharedPreferences_faceBook.getBoolean("loggedIn", false);
        SharedPreferences sharedPreferences_faceBook_connected = getSharedPreferences("faceBookConnected", MODE_PRIVATE);
        boolean fb_connected = sharedPreferences_faceBook_connected.getBoolean("connected", false);
        if (fb_logged_in) {
            if (fb_connected) {
                connected_facebook.setVisibility(View.VISIBLE);
                authenticate_facebook.setVisibility(View.GONE);
                connect_facebook.setVisibility(View.GONE);
            } else {
                authenticate_facebook.setVisibility(View.GONE);
                connect_facebook.setVisibility(View.VISIBLE);
                connected_facebook.setVisibility(View.GONE);
            }
        } else {
            authenticate_facebook.setVisibility(View.VISIBLE);
            connect_facebook.setVisibility(View.GONE);
            connected_facebook.setVisibility(View.GONE);
        }


    }

    private void Getprofile() {


        final ProgressDialog pDialog = new ProgressDialog(Profile.this,R.style.MyAlertDialogStyle);
        pDialog.setMessage("Loading ...");
        pDialog.show();


        String tag_string_req = "Login";
        String url = ApiList.GetProfile + UserID;
        StringRequest strReq = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                pDialog.dismiss();
                System.out.println(response);
                Gson gson = new Gson();

                JSONObject jObj = null;
                try {
                    jObj = new JSONObject(response);
                    if (jObj.getBoolean("response")) {
                        JSONArray chatlist = jObj.getJSONArray("Profile");
                        String userdetails = chatlist.getJSONObject(0).toString();
                        User_Details user_details = gson.fromJson(userdetails, User_Details.class);
                        setdata(user_details);
                    } else {
                        if (jObj.getString("responseString").equalsIgnoreCase("Invalid token")) {
                            Intent i = new Intent();
                            i.putExtra("keyMessage", "Session Expired, Please Login ");
                            i.putExtra("keyAlert", "Session Expired");
                            i.setClassName(Profile.this.getPackageName(), "com.singleevent.sdk.Views.TokenExpireAlertReceived");
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                        } else
                            Error_Dialog.show(jObj.getString("responseString"), Profile.this);
                    }
                } catch (JSONException e) {


                }


            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                pDialog.dismiss();
                if (error instanceof TimeoutError) {
                    Error_Dialog.show("Timeout", Profile.this);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, Profile.this), Profile.this);
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show("Please Check Your Internet Connection", Profile.this);
                }
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("token", Paper.book().read("token", ""));
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

    private void setdata(User_Details user_details) {

        // setting first name

        fname.setText(user_details.getFirst_name());

        //setting last name

        lname.setText(user_details.getLast_name());

        // setting mobile number

        mnumber.setText(user_details.getMobile());

        // setting email

        email.setText(user_details.getEmail());

        // setting company

        company.setText(user_details.getCompany());

        //designation

        jpos.setText(user_details.getDesignation());

        //website

        website.setText(user_details.getWebsite());

        // setting profilepic

        setprofilepic(user_details.getProfile_pic());


        // setting description
        des.setText(Html.fromHtml(user_details.getDescription()));

        // setting blog

        blog.setText(user_details.getUser_blog());


    }

    private void setprofilepic(String bm) {

        Glide.with(getApplicationContext())

                .load(bm.equalsIgnoreCase("") ? R.drawable.round_user : bm)
                .asBitmap()
                .placeholder(R.drawable.round_user)
                .error(R.drawable.round_user)
                .into(new BitmapImageViewTarget(ProfilePic) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(getResources(),
                                Bitmap.createScaledBitmap(resource, (int) CellWidth, (int) CellWidth, false));
                        drawable.setCircular(true);
                        ProfilePic.setImageDrawable(drawable);
                    }
                });


    }


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
                done.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));
                done.setTextColor(Color.parseColor("#ffffff"));
                done.setEnabled(true);
            } else {
                disablebutton();

            }

        }


    };


    // firstname listener

    TextWatcher fwatch = new TextWatcher() {

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

            name.setText(s + " " + lname.getText().toString());

            if (checkallfeilds()) {
                done.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));
                done.setTextColor(Color.parseColor("#ffffff"));
                done.setEnabled(true);
            } else {
                disablebutton();

            }

        }


    };


    // lastname listener

    TextWatcher lwatch = new TextWatcher() {

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

            name.setText(fname.getText().toString() + " " + s);

            if (checkallfeilds()) {
                done.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));
                done.setTextColor(Color.parseColor("#ffffff"));
                done.setEnabled(true);
            } else {
                disablebutton();

            }

        }


    };

    // lastname listener

    TextWatcher jwatch = new TextWatcher() {

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

            profession.setText(s);

            if (checkallfeilds()) {
                done.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));
                done.setTextColor(Color.parseColor("#ffffff"));
                done.setEnabled(true);
            } else {
                disablebutton();

            }

        }


    };


    private void disablebutton() {
        int res = (Color.parseColor("#0a6a99") & 0x1affffff);
        //done.setBackground(Util.setdrawable(Profile.this, R.drawable.lead_gen_btn_bckg, res));
        done.setBackgroundColor(res);
//        done.setTextColor((Color.parseColor("#ffffff") & 0x00ffffff));
        done.setEnabled(false);
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

        SpannableString s = new SpannableString("Profile");
        setTitle(Util.applyFontToMenuItem(this, s));

    }

    private boolean checkallfeilds() {

        boolean flag = true;

        sfname = fname.getText().toString();
        slname = lname.getText().toString();
        smnumber = mnumber.getText().toString();
        semail = email.getText().toString();
        sjpos = jpos.getText().toString();
        scompany = company.getText().toString();
        swebsite = website.getText().toString();


        if (TextUtils.isEmpty(semail) || !Patterns.EMAIL_ADDRESS.matcher(semail).matches())
            flag = false;

        if (TextUtils.isEmpty(sfname) || !sfname.matches("[a-zA-Z]*"))
            flag = false;

        if (TextUtils.isEmpty(slname) || !slname.matches("[a-zA-Z]*"))
            flag = false;

        if (TextUtils.isEmpty(smnumber) || !smnumber.matches("[0-9]*"))
            flag = false;

        if (TextUtils.isEmpty(sjpos) || !sjpos.matches("[a-zA-Z0-9_\\s]+")) {
            flag = false;
            jpos.setError("Enter your job position");
        } else
            jpos.setError(null);


//        if (!TextUtils.isEmpty(swebsite) || !Patterns.WEB_URL.matcher(swebsite).matches())
//            flag = false;

        if (TextUtils.isEmpty(scompany)) {
            flag = false;
            company.setError("Enter your company name");
        } else
            company.setError(null);


        return flag;

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onClick(View view) {

        int n = view.getId();
        if (n == R.id.iv_edit_image) {
            onSelectImageClick(view);

        } else if (n == R.id.done) {
            UpdatingProfile();


        } else if (n == R.id.changewd) {
            Intent i = new Intent(Profile.this, ChangePwd.class);
            startActivity(i);

        } else if (n == R.id.logout) {
            logout();

        }
        if (n == R.id.authenticate_linkedin) {
            if (DataBaseStorage.isInternetConnectivity(this))
                // linkedinlogin();
                GetAccessToken();
            else
                Error_Dialog.show("Please Check Your Internet Connection", this);
        }
        if (n == R.id.connect_linkedin) {
            connectToLinkedIn();
        }
        if (n == R.id.authenticate_twitter) {
            login_twitter();
        }
        if (n == R.id.connect_twitter) {
            connectToTwitter();
        }
        if (n == R.id.authenticate_facebook) {
            LoginManager.getInstance().logInWithReadPermissions(Profile.this, Arrays.asList("email", "public_profile"));
        }
        if (n == R.id.connect_facebook) {
            connectToFaceBook();
        }

    }


    //--------------faceBook code-----------------------------//
    public void connectToFaceBook() {
        SharedPreferences sharedPreferences_faceBook = getSharedPreferences("faceBookAuth", MODE_PRIVATE);
        String fb_user_id = sharedPreferences_faceBook.getString("id", "");
        String fb_access_token = sharedPreferences_faceBook.getString("token", "");
        boolean fb_logged_in = sharedPreferences_faceBook.getBoolean("loggedIn", false);

        SharedPreferences sharedPreferences_faceBook_connected = getSharedPreferences("faceBookConnected", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences_faceBook_connected.edit();
        editor.putString("id", fb_user_id);
        editor.putString("token", fb_access_token);
        editor.putBoolean("connected", fb_logged_in);
        editor.commit();
        connect_facebook.setVisibility(View.GONE);
        authenticate_facebook.setVisibility(View.GONE);
        connected_facebook.setVisibility(View.VISIBLE);
    }

    public void faceBookLoginManager() {
        LoginManager.getInstance().registerCallback(callbackManagerFaceBook,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Toast.makeText(Profile.this, "Successfully Authenticated", Toast.LENGTH_SHORT).show();
                        SharedPreferences sharedPreferences = getSharedPreferences("faceBookAuth", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("id", loginResult.getAccessToken().getUserId());
                        editor.putString("token", loginResult.getAccessToken().toString());
                        editor.putBoolean("loggedIn", true);
                        editor.commit();
                        authenticate_facebook.setVisibility(View.GONE);
                        connect_facebook.setVisibility(View.VISIBLE);
                        connected_facebook.setVisibility(View.GONE);

//                        handleFacebookToken(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(Profile.this, "Login Cancel", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Toast.makeText(Profile.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void computePakageHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    getPackageName(),
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (Exception e) {
            Log.e("TAG", e.getMessage());
        }
    }

    AccessTokenTracker accessToken = new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(com.facebook.AccessToken oldAccessToken, com.facebook.AccessToken currentAccessToken) {
            if (currentAccessToken != null) {

                Log.d("chcek_token", "onCurrentAccessTokenChanged: " + currentAccessToken);
                getFaceBookProfileData(currentAccessToken);

            } else {
                authenticate_facebook.setVisibility(View.VISIBLE);
                connect_facebook.setVisibility(View.GONE);
                connected_facebook.setVisibility(View.GONE);
            }
        }
    };

    private void getFaceBookProfileData(com.facebook.AccessToken currentAccessToken) {
        Log.d("getFaceBookProfileData", "onCurrentAccessTokenChanged: " + currentAccessToken);

        GraphRequest graphRequest = GraphRequest.newMeRequest(currentAccessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                if (object != null) {
                    try {
                        String userEmail = object.getString("email");
                        String faceBookUserid = object.getString("id");
                        Log.d("facebookdataerror", "onCompleted: " + object.toString());
                    } catch (Exception e) {
                        Log.d("facebookdataerror", "onCompleted: " + e.getMessage());
                    }
                } else {
                    Log.d("facebookdataerror", "else_of_oncomplete: ");
                }
            }
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "email,id");
        graphRequest.setParameters(parameters);
        graphRequest.executeAsync();

    }

    //faceBook Code end-----------------------------//
    //----------------------------Twitter codes---------------------------//
    public void connectToTwitter() {
        if (isAuthenticated()) {

            SharedPreferences mSharedPreferences;
            mSharedPreferences = getSharedPreferences(PREF_NAME, 0);
            String access_token = mSharedPreferences.getString(PREF_KEY_OAUTH_TOKEN, "");
            String access_token_secret = mSharedPreferences.getString(PREF_KEY_OAUTH_SECRET, "");
            String twitter_user_name = mSharedPreferences.getString(PREF_USER_NAME, "");

            String PREF_KEY_TWITTER_LOGIN = "is_twitter_loggedin";
            SharedPreferences.Editor e = mSharedPreferences.edit();
//            e.putString(PREF_KEY_OAUTH_TOKEN, access_token);
//            e.putString(PREF_KEY_OAUTH_SECRET, access_token_secret);
            e.putBoolean(PREF_KEY_TWITTER_LOGIN, true);
//            e.putString(PREF_USER_NAME, twitter_user_name);
            e.commit();
            connected_twitter.setVisibility(View.VISIBLE);
            connect_twitter.setVisibility(View.GONE);
            authenticate_twitter.setVisibility(View.GONE);
        }
    }

    public void initSDK() {
        mConsumerKey = getResources().getString(R.string.com_twitter_sdk_android_CONSUMER_KEY);
        mConsumerSecret = getResources().getString(R.string.com_twitter_sdk_android_CONSUMER_SECRET);
        mAuthVerifier = "oauth_verifier";

        if (TextUtils.isEmpty(mConsumerKey) || TextUtils.isEmpty(mConsumerSecret)) {
            return;
        }

        mSharedPreferences = getSharedPreferences(PREF_NAME, 0);
        if (isAuthenticated()) {
//            Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
            //hide login button here and show tweet
            //mSharedPreferences.getString(PREF_USER_NAME, "");
            // username_tv.setText("Welcome " + mSharedPreferences.getString(PREF_USER_NAME, ""));

        } else {
            Uri uri = getIntent().getData();
            if (uri != null && uri.toString().startsWith(mCallbackUrl)) {
                String verifier = uri.getQueryParameter(mAuthVerifier);
                try {
                    AccessToken accessToken = mTwitter.getOAuthAccessToken(mRequestToken, verifier);
                    saveTwitterInformation(accessToken);
                    Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                    Timber.d(e);
                }
            }
        }
    }

    private void saveTwitterInformation(AccessToken accessToken) {
        long userID = accessToken.getUserId();
        User user;
        try {
            user = mTwitter.showUser(userID);
            String username = user.getName();
            SharedPreferences.Editor e = mSharedPreferences.edit();
            e.putString(PREF_KEY_OAUTH_TOKEN, accessToken.getToken());
            e.putString(PREF_KEY_OAUTH_SECRET, accessToken.getTokenSecret());
            e.putBoolean(PREF_KEY_TWITTER_LOGIN, true);
            e.putString(PREF_USER_NAME, username);
            e.commit();

        } catch (TwitterException e1) {
            Log.d("Failed to Save", e1.getMessage());
        }
    }

    public void login_twitter() {
        mSharedPreferences = getSharedPreferences(PREF_NAME, 0);
        if (isAuthenticated()) {
            Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
        } else {

            boolean isLoggedIn = mSharedPreferences.getBoolean(PREF_KEY_TWITTER_LOGIN, false);
            if (!isLoggedIn) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final ConfigurationBuilder builder = new ConfigurationBuilder();
                        builder.setOAuthConsumerKey(mConsumerKey);
                        builder.setOAuthConsumerSecret(mConsumerSecret);

                        final Configuration configuration = builder.build();
                        final TwitterFactory factory = new TwitterFactory(configuration);
                        mTwitter = factory.getInstance();
                        try {
                            mRequestToken = mTwitter.getOAuthRequestToken(mCallbackUrl);
                            startWebAuthentication();
                        } catch (TwitterException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
        }
    }

    protected void startWebAuthentication() {
        final Intent intent = new Intent(Profile.this, TwitterAuthentication.class);
        intent.putExtra(TwitterAuthentication.EXTRA_URL, mRequestToken.getAuthenticationURL());
        startActivityForResult(intent, WEBVIEW_REQUEST_CODE);
    }

    protected boolean isAuthenticated() {
        return mSharedPreferences.getBoolean(PREF_KEY_TWITTER_LOGIN, false);
    }
    //----------------------------Twitter codes end--------------------------//

    //-------------LinkedIn code --------------------//
    public void GetAccessToken() {


        Intent i = new Intent(Profile.this, LinkedInLoginActivity.class);
//        i.putExtra("Value", response);
        i.putExtra("directed_from","AttendeeProfile");
        startActivity(i);
        finish();
    }

    public void connectToLinkedIn() {
        SharedPreferences sharedPreferences_linked_in = getSharedPreferences("LinkedIn_Authenticated", MODE_PRIVATE);
        String linkedInUserId = sharedPreferences_linked_in.getString("id", "");
        String token = sharedPreferences_linked_in.getString("token", "");
        String linkedInUserFirstName = sharedPreferences_linked_in.getString("firstName", "");
        String linkedInUserLastName = sharedPreferences_linked_in.getString("lastName", "");

        SharedPreferences sharedPreferences = getSharedPreferences("LinkedIn", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("id", linkedInUserId);
        editor.putString("token", token);
        editor.putString("firstName", linkedInUserFirstName);
        editor.putString("lastName", linkedInUserLastName);
        editor.commit();
        connected_linkedin.setVisibility(View.VISIBLE);
        connect_linkedin.setVisibility(View.GONE);
        authenticate_linkedin.setVisibility(View.GONE);
    }
//-------------LinkedIn code end--------------------//
    // This method is used to make permissions to retrieve data from linkedin

    private static Scope buildScope() {
        return Scope.build(Scope.R_BASICPROFILE, Scope.R_EMAILADDRESS);
    }

    private void linkedinlogin() {

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

    private void logout() {

        final ProgressDialog dialog = new ProgressDialog(Profile.this,R.style.MyAlertDialogStyle);
        dialog.setMessage("Logout...");
        dialog.show();
        String tag_string_req = "Logout";
        String url = ApiList.Logout;
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                try {
                    System.out.println(response);
                    JSONObject jObj = new JSONObject(response);

                    if (jObj.getBoolean("response")) {
                        Paper.book().delete("Islogin");
                        clearalllogin();
                        clearApplicationData();
                        setResult(RESULT_OK);
                        finish();

                    } else {
                        if (jObj.getString("responseString").equalsIgnoreCase("Invalid token")) {

                            Error_Dialog.show("Session Expired, Please Login", Profile.this);


                        } else
                            Error_Dialog.show(jObj.getString("responseString"), Profile.this);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                if (error instanceof TimeoutError) {
                    Error_Dialog.show("Timeout", Profile.this);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, Profile.this), Profile.this);
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show("Please Check Your Internet Connection", Profile.this);
                }
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("deviceType", "android");
                params.put("deviceId", Paper.book().read("regId", ""));
                System.out.println(params);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("token", Paper.book().read("token", ""));
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

    private void clearalllogin() {

        savedirs = new ArrayList<>();
        savedirs = getSaveDirs(dir);

        for (String tag : savedirs) {
            System.out.println("" + tag);
            Paper.book(tag).destroy();
        }
    }


    private List<String> getSaveDirs(String dir) {
        List<String> paths = new ArrayList<>();
        File directory = new File(dir);

        File[] files = directory.listFiles();

        for (File file : files) {
            if (file.isDirectory())
                paths.add(file.getName());
        }
        return paths;
    }


    private void UpdatingProfile() {

        final ProgressDialog dialog = new ProgressDialog(Profile.this,R.style.MyAlertDialogStyle);
        dialog.setMessage("Updating Profile...");
        dialog.show();
        String tag_string_req = "Profile";
        String url = ApiList.SetProfile;
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                try {
                    System.out.println(response);
                    JSONObject jObj = new JSONObject(response);


                    if (jObj.getBoolean("response")) {
                        Error_Dialog.show(jObj.getString("responseString"), Profile.this);

                    } else {
                        if (jObj.getString("responseString").equalsIgnoreCase("Invalid token")) {
                            Error_Dialog.show("Session Expired, Please Login", Profile.this);

                        } else
                            Error_Dialog.show(jObj.getString("responseString"), Profile.this);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                if (error instanceof TimeoutError) {
                    Error_Dialog.show("Timeout", Profile.this);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, Profile.this), Profile.this);
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show("Please Check Your Internet Connection", Profile.this);
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userid", Paper.book().read("userId", ""));
                params.put("first_name", sfname);
                params.put("last_name", slname);
                params.put("phone", smnumber);
                params.put("designation", sjpos);
                params.put("company", scompany);
                params.put("profile_pic_url", Paper.book().read("ProfilePIC", ""));
                params.put("website", swebsite);
                params.put("user_blog", blog.getText().toString());
                params.put("description", des.getText().toString());
                System.out.println(params);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("token", Paper.book().read("token", ""));
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

    /**
     * Start pick image activity with chooser.
     */
    public void onSelectImageClick(View view) {

        Rect square6 = new Rect();
        square6.set(20, 20, 20, 20);

        CropImage.activity(null)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setRequestedSize((int) CellWidth, (int) CellWidth)
                .setAspectRatio(100,100)
                .setMaxCropResultSize(((int) CellWidth) * 10, ((int) CellWidth) * 10)
                /* .setMinCropWindowSize((int) CellWidth, (int) CellWidth)*/
                .start(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManagerFaceBook.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (data != null)
                mTwitterVerifier = data.getExtras().getString(mAuthVerifier);
            AccessToken accessToken;
            try {
                accessToken = mTwitter.getOAuthAccessToken(mRequestToken, mTwitterVerifier);

                long userID = accessToken.getUserId();
                final User user = mTwitter.showUser(userID);
                String username = user.getName();
                // username_tv.setText("Welcome " + username);
                Log.d("twitter", "onActivityResult: " + user.getName());
                saveTwitterInformation(accessToken);
            } catch (Exception e) {
            }
        }
        // handle result of CropImageActivity
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                final InputStream imageStream;
                try {
                    imageStream = getContentResolver().openInputStream(result.getUri());
                    Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    String encodedImage = encodeImage(selectedImage);
                    updateprofilepic(encodedImage);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
            }
        } else {
            LISessionManager.getInstance(getApplicationContext()).onActivityResult(this, requestCode, resultCode, data);
            linkededinApiHelper();
        }

    }


    public void linkededinApiHelper() {
        APIHelper apiHelper = APIHelper.getInstance(getApplicationContext());
        apiHelper.getRequest(Profile.this, topCardUrl, new ApiListener() {
            @Override
            public void onApiSuccess(ApiResponse result) {
                try {
                    System.out.println(result.getResponseDataAsJson());
                    JSONObject jobject = result.getResponseDataAsJson();
                    fname.setText(jobject.getString("firstName"));
                    lname.setText(jobject.getString("lastName"));
                    Paper.book().write("ProfilePIC", jobject.getString("pictureUrl"));
                    setprofilepic(jobject.getString("pictureUrl"));
                    UpdatingProfile();

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


    private void updateprofilepic(final String encodedImage) {

        final ProgressDialog dialog = new ProgressDialog(Profile.this,R.style.MyAlertDialogStyle);
        dialog.setMessage("Uploading Profile Pic...");
        dialog.show();
        String tag_string_req = "Profile";
        String url = ApiList.UpdateProfilePic;
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                try {
                    System.out.println(response);
                    JSONObject jObj = new JSONObject(response);


                    if (jObj.getBoolean("response")) {
                        Paper.book().write("ProfilePIC", jObj.getString("responseString"));
                        setprofilepic(jObj.getString("responseString"));
                    } else {
                        if (jObj.getString("responseString").equalsIgnoreCase("Invalid token")) {
                            Error_Dialog.show("Session Expired, Please Login ", Profile.this);
                        } else
                            Error_Dialog.show(jObj.getString("responseString"), Profile.this);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                if (error instanceof TimeoutError) {
                    Error_Dialog.show("Timeout", Profile.this);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, Profile.this), Profile.this);
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show("Please Check Your Internet Connection", Profile.this);
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userid", Paper.book().read("userId", ""));
                params.put("appurl", "");
                params.put("user_type", "eventuser");
                params.put("image", encodedImage);
                System.out.println(params);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("token", Paper.book().read("token", ""));
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

    private String encodeImage(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);

        return encImage;
    }

    public void clearApplicationData() {
        File cache = getCacheDir();
        File appDir = new File(getFilesDir() + File.separator + "EventsDownload" + File.separator);
        if (appDir.exists()) {
            String[] children = appDir.list();
            for (String s : children) {
                if (!s.equals("lib")) {
                    deleteDir(new File(appDir, s));
                    Log.i("TAG", "File /data/data/APP_PACKAGE/" + s + " DELETED");
                }
            }
        }


        File dir = new File(getFilesDir()+ "/EventsDownload" );
        if (dir.isDirectory())
        {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++)
            {
                new File(dir, children[i]).delete();
            }
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        return dir.delete();
    }
}