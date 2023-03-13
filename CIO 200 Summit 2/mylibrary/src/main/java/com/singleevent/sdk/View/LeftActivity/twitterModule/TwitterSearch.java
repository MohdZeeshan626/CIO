package com.singleevent.sdk.View.LeftActivity.twitterModule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.singleevent.sdk.Left_Adapter.twitteradapters.TwitterSearchRecyclerAdapter;
import com.singleevent.sdk.R;
import com.singleevent.sdk.model.AppDetails;
import com.singleevent.sdk.model.Events;
import com.singleevent.sdk.pojo.twitterpojos.TwitterAdapterModelClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import io.paperdb.Paper;
import timber.log.Timber;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterSearch extends AppCompatActivity implements TwitterSearchRecyclerAdapter.Twitter_func{
    // Twitter Search data

    private final String TAG = "Twitter Api";
    private EditText editText;
    private String nextToken;
    private int searchCount = 0;
    private RecyclerView recyclerView;
    private String SearchCopy = "";
    private ProgressDialog dialog;
    private ProgressBar progressBar;
    private ArrayList<TwitterAdapterModelClass> arrayListCopy = new ArrayList<>();
    private TwitterSearchRecyclerAdapter twitterSearchRecyclerAdapter;

    private ArrayList<Events> events = new ArrayList<Events>();
    Events e;

    // Twitter login , post , retweet and like data

    public static final String PREF_NAME = "sample_twitter_pref";
    public static final String PREF_USER_NAME = "twitter_user_name";
    public static final int WEBVIEW_REQUEST_CODE = 100;
    private static final String PREF_KEY_OAUTH_TOKEN = "oauth_token";
    private static final String PREF_KEY_OAUTH_SECRET = "oauth_token_secret";
    private static final String PREF_KEY_TWITTER_LOGIN = "is_twitter_loggedin";

    private ProgressDialog mPostProgress = null;

    private String mConsumerKey = null;
    private String mConsumerSecret = null;
    private String mCallbackUrl = null;
    private String mAuthVerifier = null;
    private String mTwitterVerifier = null;
    private Twitter mTwitter = null;
    private RequestToken mRequestToken = null;
    private SharedPreferences mSharedPreferences = null;

    private String RetweetUserName = null;
    private String RetweetTweetId = null;
    TextView twitter_login;
    Toolbar toolbar;
    AppDetails appDetails;
    int pos;
    String hashtag="",default_msg="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_twitter_search);

        pos = getIntent().getExtras().getInt("pos");
//        title = getIntent().getExtras().getString("title");
        events = Paper.book().read("Appevents");
        e = events.get(0);
        hashtag = e.getTabs(pos).getHashtag();
        default_msg = e.getTabs(pos).getDefaultmsg();
        Log.d("check_the_tabs", "onCreate: "+default_msg+"\n"+hashtag);
//        Toast.makeText(this,hashtag+"\n"+default_msg,Toast.LENGTH_SHORT).show();
        recyclerView = findViewById(R.id.recycler_view);
        appDetails = Paper.book().read("Appdetails");
        toolbar=findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));
        progressBar = findViewById(R.id.progress_bar);
        editText = findViewById(R.id.twitter_search);
//        twitter_login = findViewById(R.id.twitter_login);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        editText.setText(hashtag);
        editText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ( (event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {

                    InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                    searchIt();
                    return true;
                }
                return false;
            }
        });
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        progressBar.setVisibility(View.INVISIBLE);
        arrayListCopy = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(TwitterSearch.this,
                LinearLayoutManager.VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        twitterSearchRecyclerAdapter = new TwitterSearchRecyclerAdapter(TwitterSearch.this, arrayListCopy, this);
        recyclerView.setAdapter(twitterSearchRecyclerAdapter);

        initSDK();

    }

    @Override
    protected void onStart() {
        super.onStart();
        mSharedPreferences = getSharedPreferences(PREF_NAME, 0);
        if(isAuthenticated()){
//            twitter_login.setVisibility(View.GONE);
        }else{
//            twitter_login.setVisibility(View.VISIBLE);
        }
    }
    public void back(View view) {
        finish();
    }

    public void search_Twitter(View view) {

        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        searchIt();
    }
    public String searchUrl(int a) {
        if (searchCount == 0) {
            String url1 = "https://api.twitter.com/2/tweets/search/recent?query=";
            String url_body="";
            if(editText.getText().toString().startsWith("#")){
                url_body=editText.getText().toString().substring(1,editText.getText().toString().length());
            }else{
                url_body= editText.getText().toString();

            }
            String url3 = "&max_results=10&tweet.fields=author_id,created_at";
            String url = url1 + url_body + url3;
            return url;
        } else {
            String url1 = "https://api.twitter.com/2/tweets/search/recent?query=";
            String url_body="";
            if(editText.getText().toString().startsWith("#")){
                url_body=editText.getText().toString().substring(1,editText.getText().toString().length());
            }else{
                url_body= editText.getText().toString();

            }
            String url3 = "&max_results=10&next_token=";
            String url4 = nextToken;
            String url5 = "&tweet.fields=author_id,created_at";
            String url = url1 + url_body + url3 + url4 + url5;
            return url;
        }

    }
    public String timeSet(String str) {
        String[] strings = str.split("T", 2);
        String[] days = strings[0].split("-", 3);
        String[] time = strings[1].split(":", 3);
        System.out.println(time[0]);
        int hrsT = Integer.parseInt(time[0]);
        hrsT = hrsT + 5;
        int minT = Integer.parseInt(time[1]) + 30;
        if (minT / 60 != 0) {
            hrsT = hrsT + (minT / 60);
        }
        int hrsC = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        int minC = Calendar.getInstance().get(Calendar.MINUTE);
        int dayC = Calendar.getInstance().get(Calendar.DATE);

        if (dayC - Integer.parseInt(days[2]) != 0) {
            return String.valueOf(dayC - Integer.parseInt(days[2])) + "d" + String.valueOf(hrsC - hrsT) + "h" + String.valueOf(minC - minT) + "m";
        } else if (hrsC - hrsT != 0) {
            return String.valueOf(hrsC - hrsT) + "h" + String.valueOf(minC - minT) + "m";
        } else {
            return String.valueOf(minC - minT) + "m";
        }
    }
    private void removeWorkingDialog() {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }
    public void searchIt() {
        if (!SearchCopy.equalsIgnoreCase(editText.getText().toString())) {
            searchCount = 0;
            arrayListCopy.clear();
        }

        if (searchCount == 0) {
            dialog = new ProgressDialog(this);
            dialog.setMessage("Please Wait ...");
            dialog.show();
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    removeWorkingDialog();
                }

            }, 5000);
        } else {
            progressBar.setVisibility(View.VISIBLE);
        }
        SearchCopy = editText.getText().toString();
        editText = findViewById(R.id.twitter_search);

        String url = searchUrl(searchCount);
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d(TAG, response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = (JSONArray) jsonObject.get("data");
                    nextToken = jsonObject.getJSONObject("meta").get("next_token").toString();
                    for (int i = 0; i < 10; i++) {
                        TwitterAdapterModelClass twitterAdapterModelClass = new TwitterAdapterModelClass("", "", "", "", "", "", 0, 0);
                        twitterAdapterModelClass.setTweet(jsonArray.getJSONObject(i).get("text").toString());
                        twitterAdapterModelClass.setTweet_id(jsonArray.getJSONObject(i).get("id").toString());
                        String tweet_id = jsonArray.getJSONObject(i).get("id").toString();
                        twitterAdapterModelClass.setTime(timeSet(jsonArray.getJSONObject(i).get("created_at").toString()));

                        StringRequest stringRequest2 = new StringRequest(Request.Method.GET, "https://api.twitter.com/1.1/statuses/show.json?id=" + tweet_id, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    Log.d(TAG, response);
                                    JSONObject jsonObject1 = new JSONObject(response);
                                    JSONObject jsonObject2 = jsonObject1.getJSONObject("user");
                                    twitterAdapterModelClass.setName(jsonObject2.get("name").toString());
                                    twitterAdapterModelClass.setUsername(jsonObject2.get("screen_name").toString());
                                    twitterAdapterModelClass.setProfile_pic_url(jsonObject2.get("profile_image_url_https").toString());
                                    twitterAdapterModelClass.setLikes_count(Integer.parseInt(jsonObject1.get("favorite_count").toString()));
                                    twitterAdapterModelClass.setRetweet_count(Integer.parseInt(jsonObject1.get("retweet_count").toString()));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                Log.d(TAG, "Adding array  " + twitterAdapterModelClass.toString());
                                arrayListCopy.add(twitterAdapterModelClass);
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                dialog.dismiss();
                                Log.d(TAG, "Error in calling user by id  " + error);

                            }
                        }) {
                            @Override
                            public Map<String, String> getHeaders() {
                                Map<String, String> headers = new HashMap<String, String>();
                                headers.put("Authorization", "Bearer AAAAAAAAAAAAAAAAAAAAANboPgEAAAAATK9IQhbMsa5%2B7iGagsclBcb6SuE%3Dr8LACRu7v4ZZkRjcsig3ISFnZJlLOwj4hEzMsMr1k3x3LxfZfz");
                                return headers;
                            }
                        };
                        //queue.add(stringRequest1);
                        queue.add(stringRequest2);

                    }
                    if (searchCount == 0) {
                        if (arrayListCopy.size() > 0) {
                            updateview(arrayListCopy);
                        } else {
                            searchCount++;
                            searchIt();
                        }
                    } else {
                        dialog.dismiss();
                        twitterSearchRecyclerAdapter.setItems(arrayListCopy);
                        twitterSearchRecyclerAdapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                    recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                            if (dy < 0) {
                            }
                            if (!recyclerView.canScrollVertically(1) && dy > 0 && arrayListCopy.size() > 0) {
                                // Scrolled to bottom
                                searchCount++;
                                searchIt();
                            }
                        }
                    });

                    Log.d(TAG, jsonObject.toString());
                    Log.d(TAG, jsonArray.get(9).toString());
                    Log.d(TAG, jsonArray.get(0).toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Log.d(TAG, "hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh" + error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer AAAAAAAAAAAAAAAAAAAAANboPgEAAAAATK9IQhbMsa5%2B7iGagsclBcb6SuE%3Dr8LACRu7v4ZZkRjcsig3ISFnZJlLOwj4hEzMsMr1k3x3LxfZfz");
                return headers;
            }

        };
        queue.add(stringRequest);
    }

    public void updateview(ArrayList<TwitterAdapterModelClass> arrayListCopy) {
        twitterSearchRecyclerAdapter = new TwitterSearchRecyclerAdapter(TwitterSearch.this, arrayListCopy, this);
        recyclerView.setAdapter(twitterSearchRecyclerAdapter);
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
    public void login_twitter(View view) {
        mSharedPreferences = getSharedPreferences(PREF_NAME, 0);
        if (isAuthenticated()) {
            Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
        } else {

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
    }
    protected void startWebAuthentication() {
        final Intent intent = new Intent(TwitterSearch.this, TwitterAuthentication.class);
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
            // username_tv.setText("Welcome " + username);

            saveTwitterInformation(accessToken);
        } catch (Exception e) {
        }
    }
    public void tweet(View view) {
        if (isAuthenticated()) {
            Intent i=new Intent(this,PostATweet.class);
            i.putExtra("pos",pos);
            i.putExtra("hashtag",hashtag);
            i.putExtra("default_msg",default_msg);
            startActivity(i);
            finish();
//
//            final EditText editText = new EditText(this);
//
//            AlertDialog.Builder alert = new AlertDialog.Builder(TwitterSearch.this);
//            alert.setTitle("Enter the Tweet text")
//                    .setView(editText)
//                    .setPositiveButton("Post", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            String tweetText = editText.getText().toString();
//                            new PostTweet().execute(tweetText);
//                        }
//                    })
//                    .setNegativeButton("Discard", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//
//                        }
//                    });
//
//            alert.show();
        } else {
            Toast.makeText(this, "First Authenticate", Toast.LENGTH_SHORT).show();
        }
    }
    private void closeProgress() {
        if (mPostProgress != null && mPostProgress.isShowing()) {
            mPostProgress.dismiss();
            mPostProgress = null;
        }
    }
    @Override
    public void like_on_a_tweet(int a) {

        if (isAuthenticated()) {
            new LikeTweet().execute(arrayListCopy.get(a).getTweet_id());
        } else {
            Toast.makeText(this, "First Authenticate", Toast.LENGTH_SHORT).show();
        }

    }
    private class LikeTweet extends AsyncTask<String, Integer, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mPostProgress = new ProgressDialog(TwitterSearch.this);
            mPostProgress.setMessage("Loading...");
            mPostProgress.setCancelable(false);
            mPostProgress.show();
        }

        @Override
        protected Void doInBackground(String... params) {

            String id = params[0];
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
                twitter.createFavorite(Long.parseLong(id));
            } catch (TwitterException e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void Result) {
            closeProgress();
        }
    }
    @Override
    public void retweet_on_a_tweet(int a) {


        if (isAuthenticated()) {
            final EditText editText = new EditText(this);

            AlertDialog.Builder alert = new AlertDialog.Builder(TwitterSearch.this);
            alert.setTitle("Enter the Tweet text")
                    .setView(editText)
                    .setPositiveButton("Post", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String tweetText = editText.getText().toString();
                            RetweetUserName = arrayListCopy.get(a).getUsername();
                            RetweetTweetId = arrayListCopy.get(a).getTweet_id();
                            new RetweetTweet().execute(tweetText);
                        }
                    })
                    .setNegativeButton("Discard", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

            alert.show();
        } else {
            Toast.makeText(this, "First Authenticate", Toast.LENGTH_SHORT).show();
        }

    }
    private class RetweetTweet extends AsyncTask<String, Integer, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mPostProgress = new ProgressDialog(TwitterSearch.this);
            mPostProgress.setMessage("Loading...");
            mPostProgress.setCancelable(false);
            mPostProgress.show();
        }

        @Override
        protected Void doInBackground(String... params) {

            String id = params[0];
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
                // twitter.createFavorite(Long.parseLong(id));
                //StatusUpdate statusUpdate = new StatusUpdate(id);
                //twitter4j.Status response = twitter.updateStatus(statusUpdate);
                // twitter4j.Status response2 = twitter.retweetStatus(Long.parseLong("1195197937086681089"));
                //twitter.updateStatus("Wow! This is cool! https://twitter.com/Utkarsh62881245/status/1137644186432286721");
                twitter.updateStatus(id + " https://twitter.com/" + RetweetUserName + "/status/" + RetweetTweetId);
                //twitter4j.Status response = twitter.retweetStatus();
                // Log.d("Status", response.getText());
                //Log.d("Status",response2.getText());

            } catch (TwitterException e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void Result) {
            closeProgress();
        }
    }
    @Override
    public void share_a_tweet(int a) {


        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        //sharingIntent.putExtra(Intent.EXTRA_SUBJECT, arrayListCopy.get(a).getName());
        sharingIntent.putExtra(Intent.EXTRA_TEXT, "https://twitter.com/" + arrayListCopy.get(a).getTweet_id() + "/status/" + arrayListCopy.get(a).getTweet_id());
        startActivity(Intent.createChooser(sharingIntent, "Sharing Option"));


    }

}