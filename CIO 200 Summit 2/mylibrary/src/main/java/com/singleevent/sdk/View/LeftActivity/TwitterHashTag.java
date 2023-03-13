package com.singleevent.sdk.View.LeftActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.SpannableString;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.singleevent.sdk.Custom_View.Util;
import com.singleevent.sdk.model.AppDetails;
import com.singleevent.sdk.R;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetcomposer.ComposerActivity;
import com.twitter.sdk.android.tweetui.SearchTimeline;
import com.twitter.sdk.android.tweetui.TimelineResult;
import com.twitter.sdk.android.tweetui.TweetTimelineRecyclerViewAdapter;

import io.paperdb.Paper;


public class TwitterHashTag extends AppCompatActivity implements View.OnClickListener{
    private TwitterAuthClient mTwitterAuthClient;

    private String title;
    private TweetTimelineRecyclerViewAdapter adapter;
    private SearchTimeline searchTimeline;

    private Toolbar toolbar;
     private TextView tv_hashtag_head ;
     private ImageView ivCompose;
     private SwipeRefreshLayout swipeLayout;
     private RecyclerView recyclerView ;
    AppDetails appDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tweetui_timeline_activity);

        tv_hashtag_head = (TextView) findViewById(R.id.tv_hashtag_head);
         ivCompose = (ImageView) findViewById(R.id.ivCompose);
         swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_layout);
         recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        appDetails = Paper.book().read("Appdetails");
        toolbar.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));
        setSupportActionBar(toolbar);

        TwitterConfig config = new TwitterConfig.Builder(this)
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(new TwitterAuthConfig(getString(R.string.com_twitter_sdk_android_CONSUMER_KEY), getString(R.string.com_twitter_sdk_android_CONSUMER_SECRET)))
                .debug(true)
                .build();
        Twitter.initialize(config);



        Bundle extras= getIntent().getExtras();

        if(extras!=null){
            title = extras.getString("HashTagStrValue");
        }


        tv_hashtag_head.setText(title);
        ivCompose.setOnClickListener(this);
        ivCompose.setBackground(getResources().getDrawable(R.drawable.tweet));

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

       /* final List<String> handles = Arrays.asList("mobile", "java");
        final FilterValues filterValues = new FilterValues(null, null, handles, null); // or load from JSON, XML, etc
        final TimelineFilter timelineFilter = new BasicTimelineFilter(filterValues, Locale.ENGLISH);*/

         searchTimeline = new SearchTimeline.Builder()
                .query(title)
                .maxItemsPerRequest(10)
                .build();

       /* final UserTimeline userTimeline = new UserTimeline.Builder()
                .screenName(title)
                .build();*/

       // startProgress();
        new YourAsyncTask(TwitterHashTag.this).execute();

        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadDataOnRefresh();

            }
        });


    }

    private void loadDataOnRefresh() {
        swipeLayout.setRefreshing(true);
        adapter.refresh(new Callback<TimelineResult<Tweet>>() {
            @Override
            public void success(Result<TimelineResult<Tweet>> result) {
                swipeLayout.setRefreshing(false);
            }

            @Override
            public void failure(TwitterException exception) {
                // Toast or some other action
                swipeLayout.setRefreshing(false);
            }
        });
       recyclerView.setAdapter(adapter);
    }
    private class YourAsyncTask extends AsyncTask<Void, Void, Void> {
        private ProgressDialog dialog;

        public YourAsyncTask(TwitterHashTag activity) {
            dialog = new ProgressDialog(activity,R.style.MyAlertDialogStyle);
        }

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Please wait...");
            dialog.show();
        }

        protected Void doInBackground(Void... args) {
            // do background work here
            twitterLoadData();
            return null;
        }

        protected void onPostExecute(Void result) {
            // do UI work here
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            recyclerView.setAdapter(adapter);
        }
    }

    private void twitterLoadData() {
        adapter =
                new TweetTimelineRecyclerViewAdapter.Builder(this)
                        .setTimeline(searchTimeline)
                        .setViewStyle(R.style.tw__TweetLightWithActionsStyle)
                        .build();


    }

    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.ivCompose){
            mTwitterAuthClient= new TwitterAuthClient();

            final TwitterSession session = TwitterCore.getInstance().getSessionManager()
                    .getActiveSession();
            if(session!=null){

                final Intent intent = new ComposerActivity.Builder(TwitterHashTag.this)
                        .session(session)
                        .hashtags(title)
                        .createIntent();
                startActivity(intent);
            }else{
                mTwitterAuthClient.authorize(TwitterHashTag.this, new Callback<TwitterSession>() {

                    @Override
                    public void success(Result<TwitterSession> twitterSessionResult) {
                        // Success
                        final TwitterSession session = TwitterCore.getInstance().getSessionManager()
                                .getActiveSession();
                        final Intent intent = new ComposerActivity.Builder(TwitterHashTag.this)
                                .session(session)
                                .hashtags(title)
                                .createIntent();
                        startActivity(intent);

                    }

                    @Override
                    public void failure(TwitterException e) {
                        Toast.makeText(TwitterHashTag.this,"Try Again",Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                });
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        mTwitterAuthClient.onActivityResult(requestCode, resultCode, data);
    }

    //on menu item selected method


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case android.R.id.home:
                finish();
        }
        return true;

    }

    @Override
    protected void onResume() {
        super.onResume();
        SpannableString s = new SpannableString("Twitter Share");
        setTitle(Util.applyFontToMenuItem(this, s));
    }
}
