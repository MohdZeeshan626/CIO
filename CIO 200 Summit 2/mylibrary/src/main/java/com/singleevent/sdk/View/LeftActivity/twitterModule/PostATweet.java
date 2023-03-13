package com.singleevent.sdk.View.LeftActivity.twitterModule;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.singleevent.sdk.R;
import com.singleevent.sdk.model.AppDetails;

import io.paperdb.Paper;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

public class PostATweet extends AppCompatActivity {

    public static final String PREF_NAME = "sample_twitter_pref";
    public static final String PREF_USER_NAME = "twitter_user_name";
    public static final int WEBVIEW_REQUEST_CODE = 100;
    private static final String PREF_KEY_OAUTH_TOKEN = "oauth_token";
    private static final String PREF_KEY_OAUTH_SECRET = "oauth_token_secret";
    private static final String PREF_KEY_TWITTER_LOGIN = "is_twitter_loggedin";

    private EditText mEditText = null;
    private Button tweet_btn = null, twitter_login_btn = null;
    private TextView username_tv = null;

    private ProgressDialog mPostProgress = null;

    private String mConsumerKey = null;
    private String mConsumerSecret = null;
    private String mCallbackUrl = null;
    private String mAuthVerifier = null;
    private String mTwitterVerifier = null;
    private Twitter mTwitter = null;
    private RequestToken mRequestToken = null;
    private SharedPreferences mSharedPreferences = null;
    Toolbar toolbar;
    AppDetails appDetails;
    int pos;
    String hashtag = "", default_msg = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_post_a_tweet);
        initViews();
        initSDK();
    }

    private void initViews() {
        appDetails = Paper.book().read("Appdetails");
        pos = getIntent().getExtras().getInt("pos");
        hashtag = getIntent().getStringExtra("hashtag");
        default_msg = getIntent().getStringExtra("default_msg");
        toolbar = findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));
        mEditText = findViewById(R.id.post_text);
        mEditText.setText(default_msg);

    }


    private void closeProgress() {
        if (mPostProgress != null && mPostProgress.isShowing()) {
            mPostProgress.dismiss();
            mPostProgress = null;
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
                    Log.d("Failed to login ", e.getMessage());
                }
            }
        }
    }

    protected boolean isAuthenticated() {
        return mSharedPreferences.getBoolean(PREF_KEY_TWITTER_LOGIN, false);
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

    private void loginToTwitter() {
        boolean isLoggedIn = mSharedPreferences.getBoolean(PREF_KEY_TWITTER_LOGIN, false);

        if (!isLoggedIn) {
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
    }

    protected void startWebAuthentication() {
        final Intent intent = new Intent(PostATweet.this, TwitterAuthentication.class);
        intent.putExtra(TwitterAuthentication.EXTRA_URL, mRequestToken.getAuthenticationURL());
        startActivityForResult(intent, WEBVIEW_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null)
            mTwitterVerifier = data.getExtras().getString(mAuthVerifier);

        AccessToken accessToken;
        try {
            accessToken = mTwitter.getOAuthAccessToken(mRequestToken, mTwitterVerifier);

            long userID = accessToken.getUserId();
            final User user = mTwitter.showUser(userID);
            String username = user.getName();
            username_tv.setText("Welcome " + username);

            saveTwitterInformation(accessToken);
        } catch (Exception e) {
        }
    }

    public void back(View view) {
        onBackPressed();
        Intent i = new Intent(PostATweet.this, TwitterSearch.class);
        i.putExtra("pos", pos);
        startActivity(i);
        finish();
    }

    public void postToTwitter(View view) {
        if (isAuthenticated()) {
            String tweetText = mEditText.getText().toString();
            if (tweetText != null && !tweetText.isEmpty()) {
                Log.d("check_tag_twitter", "postToTwitter: "+tweetText +" "+hashtag);
                new PostTweet().execute(tweetText);
            } else {
                mEditText.setError("filed can't be Empty!");
                mEditText.requestFocus();
            }
        } else {
            Toast.makeText(this, "First Authenticate", Toast.LENGTH_SHORT).show();
        }

    }

    private class PostTweet extends AsyncTask<String, Integer, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mPostProgress = new ProgressDialog(PostATweet.this);
            mPostProgress.setMessage("Loading...");
            mPostProgress.setCancelable(false);
            mPostProgress.show();
        }

        @Override
        protected Void doInBackground(String... params) {

            String status = params[0];
            ConfigurationBuilder builder = new ConfigurationBuilder();
            builder.setOAuthConsumerKey(mConsumerKey);
            builder.setOAuthConsumerSecret(mConsumerSecret);

            SharedPreferences mSharedPreferences = null;
            mSharedPreferences = getSharedPreferences(PREF_NAME, 0);
            String access_token = mSharedPreferences.getString(PREF_KEY_OAUTH_TOKEN, "");
            String access_token_secret = mSharedPreferences.getString(PREF_KEY_OAUTH_SECRET, "");
            Log.d("Async", "Consumer Key in Post Process : " + access_token);
            Log.d("Async", "Consumer Secreat Key in post Process : " + access_token_secret);

            AccessToken accessToken = new AccessToken(access_token, access_token_secret);
            Twitter twitter = new TwitterFactory(builder.build()).getInstance(accessToken);
            try {
                if (status.length() < 139) {

                    twitter.updateStatus(mEditText.getText().toString());

                }
            } catch (TwitterException e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void Result) {
            mEditText.setText("");
            closeProgress();
            Toast.makeText(PostATweet.this, "Posted Successfully", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(PostATweet.this, TwitterSearch.class);
            i.putExtra("pos", pos);
            startActivity(i);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(PostATweet.this, TwitterSearch.class);
        i.putExtra("pos", pos);
        startActivity(i);
        finish();
    }
}