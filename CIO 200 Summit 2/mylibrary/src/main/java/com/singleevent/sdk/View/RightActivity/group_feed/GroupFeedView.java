package com.singleevent.sdk.View.RightActivity.group_feed;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.AbsListView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.singleevent.sdk.ApiList;
import com.singleevent.sdk.App;
import com.singleevent.sdk.Custom_View.Error_Dialog;
import com.singleevent.sdk.Custom_View.Util;
import com.singleevent.sdk.Custom_View.VolleyErrorLis;
import com.singleevent.sdk.View.LeftActivity.Feeds;
import com.singleevent.sdk.feeds_class.OnItemClickListener;
import com.singleevent.sdk.gallery_camera.ScrollDetection.ScrollDirectionDetector;
import com.singleevent.sdk.Left_Adapter.FeedAdapter_co;
import com.singleevent.sdk.Left_Adapter.FeedItemAnimator;
import com.singleevent.sdk.model.AppDetails;
import com.singleevent.sdk.model.Feed;
import com.singleevent.sdk.R;
import com.singleevent.sdk.View.Fragment.Left_Fragment.CommentFragment;
import com.singleevent.sdk.View.LeftActivity.WrapContentLinearLayoutManager;
import com.singleevent.sdk.databinding.FeedActivityBinding;
import com.singleevent.sdk.model.Polling.PollingModel;
import com.singleevent.sdk.pojo.pollingpojo.PollingAnswersPojo;
import com.singleevent.sdk.pojo.pollingpojo.PollingResultPojo;
import com.singleevent.sdk.service.Health1NetworkController;
import com.singleevent.sdk.service.UploadPost;
import com.singleevent.sdk.service.UploadResultReceiver;
import com.singleevent.sdk.Left_Adapter.Like_User_Adapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import io.paperdb.Paper;
import io.socket.emitter.Emitter;
import retrofit2.Call;
import retrofit2.Callback;

import static com.singleevent.sdk.Left_Adapter.FeedAdapter_co.ACTION_LIKE_BUTTON_CLICKED;




public class GroupFeedView extends AppCompatActivity implements UploadResultReceiver.Receiver,
        View.OnClickListener, ScrollDirectionDetector.OnDetectScrollListener, OnItemClickListener {
    FeedActivityBinding binding;
    AppDetails appDetails;
    private float dpWidth;
    FeedAdapter_co adapter;
//    Like_User_Adapter like_user_adapter;
//    RecyclerView likelist;
//    SparseArray<Feed> feeds_co = new SparseArray<>();
    ArrayList<Feed> feeds = new ArrayList<>();
    List<PollingModel.Poll> pollList = new ArrayList<>();
    boolean check;
    private String user_id;
    String group_id,group_name;
    private UploadResultReceiver mReceiver;
    private double margintop, iwidth;
    private static final OvershootInterpolator OVERSHOOT_INTERPOLATOR = new OvershootInterpolator(4);
    WrapContentLinearLayoutManager linearLayoutManager;
    private int mScrollState = AbsListView.OnScrollListener.SCROLL_STATE_IDLE;
    /**
     * Initial scroll direction should be UP in order to set as active most top item if no active item yet
     */
    private ScrollDirectionDetector.ScrollDirection mScrollDirection = ScrollDirectionDetector.ScrollDirection.UP;
    ScrollDirectionDetector mScrollDirectionDetector;

    private int preLast;
    private io.socket.client.Socket mSocket;
    ProgressDialog uploaddaialog;
    SwipeRefreshLayout feedswiperefresh;
    @Override
    protected void onStart() {
        super.onStart();
        mReceiver = new UploadResultReceiver(new Handler());
        mReceiver.setReceiver(this);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Paper.init(this);
        binding = DataBindingUtil.setContentView(this, R.layout.feed_activity);
        appDetails = Paper.book().read("Appdetails");
        user_id = Paper.book().read("userId", "");
        binding.toolbar.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));
        setSupportActionBar(binding.toolbar);
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        dpWidth = displayMetrics.widthPixels;
        iwidth = displayMetrics.widthPixels * 0.30;
        feeds = new ArrayList<>();
        feedswiperefresh=(SwipeRefreshLayout)findViewById(R.id.feedswiperefresh);


        group_id = getIntent().getExtras().getString("group_id");
        group_name = getIntent().getExtras().getString("group_name");
        try {
            linearLayoutManager = new WrapContentLinearLayoutManager(this) {
                @Override
                protected int getExtraLayoutSpace(RecyclerView.State state) {
                    return 300;
                }
            };
        }catch (Exception e){
        }
//        binding.productsRecycler.setHasFixedSize(true);
        binding.productsRecycler.setLayoutManager(linearLayoutManager);

        try {
            adapter = new FeedAdapter_co(feeds, com.singleevent.sdk.View.RightActivity.group_feed.GroupFeedView.this, dpWidth, this, "Feeds", appDetails);
        }catch (Exception e)
        {

        }
        try{
            binding.productsRecycler.setAdapter(adapter);}
        catch (Exception e)
        {
        }
        feedswiperefresh.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        feedswiperefresh.setRefreshing(true);
                        feeds.clear();
                        adapter.notifyDataSetChanged();
                        binding.productsRecycler.setHasFixedSize(true);
                        LoadFeeds("0");
                    }
                }
        );
        binding.productsRecycler.setItemAnimator(new FeedItemAnimator());
        margintop = getResources().getDimensionPixelSize(R.dimen.abc_action_bar_default_height_material) + getResources().getDimensionPixelSize(R.dimen.abc_action_bar_default_height_material) * 0.50;
        binding.morepost.setBackground(Util.setrounded(Color.parseColor(appDetails.getTheme_color())));
        CoordinatorLayout.LayoutParams param = (CoordinatorLayout.LayoutParams) binding.morepost.getLayoutParams();
        param.width = (int) iwidth;
        param.height = CoordinatorLayout.LayoutParams.WRAP_CONTENT;
        param.setMargins(0, (int) margintop, 0, 0);
        binding.morepost.setLayoutParams(param);

        binding.moreitems.setTypeface(Util.boldtypeface(this));

        binding.morepost.setVisibility(View.GONE);
        binding.morepost.setOnClickListener(this);

        String action = getIntent().getAction();
        if (action != null && action.equals("admin_panel")) {

            binding.adminPanel.setVisibility(View.VISIBLE);
        } else {
            binding.adminPanel.setVisibility(View.GONE);
        }
        LoadFeeds("0");
        mScrollDirectionDetector = new ScrollDirectionDetector(this);

        binding.productsRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int scrollState) {
                mScrollState = scrollState;

                if (scrollState == RecyclerView.SCROLL_STATE_IDLE && !(feeds.size() == 0)) {

                    switch (mScrollDirection) {
                        case UP:
                            break;
                        case DOWN:

                            int visibleItemCount = linearLayoutManager.getChildCount();
                            int totalItemCount = linearLayoutManager.getItemCount();

                            final int lastItem = linearLayoutManager.findFirstVisibleItemPosition() + visibleItemCount;
                            System.out.println("Last item is "+lastItem);
                            System.out.println("Total item is "+totalItemCount);

                            if (lastItem == totalItemCount) {
                                System.out.print("prelast"+preLast);
                                if (preLast != lastItem) { //to avoid multiple calls for last item
                                    preLast = lastItem;
                                    LoadFeeds(feeds.get(totalItemCount - 1).getTimeStamp());

                                }
                            }

                            break;
                        default:
                            throw new RuntimeException("not handled mScrollDirection " + mScrollDirection);
                    }
                }
            }



            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                mScrollDirectionDetector.onDetectedListScroll(linearLayoutManager, linearLayoutManager.findFirstVisibleItemPosition());

            }
        });


        // getting socket objects from application class

        try {
            mSocket = App.getInstance().getSocket();
            mSocket.on("confirm_connection", onConnect);
            mSocket.on("confirm_feeds_checkin", onConfirm);
            mSocket.on("post_like_ack", onLikeConfirm);
            mSocket.on("new_like", onNewLike);
            mSocket.on("new_comment", onNewComment);
            mSocket.on("new_feed", onNewFeed);
            mSocket.connect();

        } catch (Exception e) {
            e.printStackTrace();
        }

        binding.loading.setVisibility(View.VISIBLE);
        binding.loading.getIndeterminateDrawable().setColorFilter(Color.parseColor(appDetails.getTheme_color()),
                android.graphics.PorterDuff.Mode.MULTIPLY);

    }

    private void visiblenewpost(boolean isvisble) {
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator bounceAnimX = null, bounceAnimY = null;

        if (isvisble) {
            bounceAnimX = ObjectAnimator.ofFloat(binding.morepost, "scaleX", 0.2f, 1f);
            bounceAnimX.setDuration(600);
            bounceAnimX.setInterpolator(OVERSHOOT_INTERPOLATOR);

            bounceAnimY = ObjectAnimator.ofFloat(binding.morepost, "scaleY", 0.2f, 1f);
            bounceAnimY.setDuration(600);
            bounceAnimY.setInterpolator(OVERSHOOT_INTERPOLATOR);
            bounceAnimY.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    binding.morepost.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animator animation) {

                }
            });
        } else {
            bounceAnimX = ObjectAnimator.ofFloat(binding.morepost, "alpha", 0);
            bounceAnimX.setDuration(600);
            bounceAnimX.setInterpolator(new DecelerateInterpolator());

            bounceAnimY = ObjectAnimator.ofFloat(binding.morepost, "alpha", 1f);
            bounceAnimY.setDuration(600);
            bounceAnimY.setInterpolator(new DecelerateInterpolator());
            bounceAnimY.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    binding.morepost.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationEnd(Animator animation) {

                }
            });
        }

        animatorSet.play(bounceAnimX).with(bounceAnimY);
        animatorSet.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setTitle("");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.create_post, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int i1 = item.getItemId();
        if (i1 == android.R.id.home) {
            onBackPressed();
            clearSharedPref();
            return true;
        } else if (i1 == R.id.post) {
            Intent i = new Intent(GroupFeedView.this, GroupUploadPost.class);
            i.putExtra("group_id",group_id);
            startActivityForResult(i, 1);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        clearSharedPref();
        super.onBackPressed();
    }

    private void LoadFeeds(String lastposttime) {

        String tag_string_req = "Get_Feed";
        String url = ApiList.Get_Feed + appDetails.getAppId() + "&userid=" + Paper.book().read("userId", "") + "&last_post_time=" + lastposttime +"&group_id="+group_id;

        binding.loading.setVisibility(View.VISIBLE);
        StringRequest strReq = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                try {
                    if (feedswiperefresh.isRefreshing()) {
                        feedswiperefresh.setRefreshing(false);
                    }
                }catch (Exception e){}
                binding.loading.setVisibility(View.GONE);
                JSONObject jObj = null;
                try {
                    jObj = new JSONObject(response);
                    if (jObj.getBoolean("response")) {
                        parseresult(jObj);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    if (feedswiperefresh.isRefreshing()) {
                        feedswiperefresh.setRefreshing(false);
                    }
                }catch (Exception e){
                }
                binding.loading.setVisibility(View.GONE);
                if (error instanceof TimeoutError) {
                    Error_Dialog.show("Timeout", GroupFeedView.this);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, GroupFeedView.this), GroupFeedView.this);
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show("Please Check Your Internet Connection", GroupFeedView.this);
                }

            }
        });


        App.getInstance().addToRequestQueue(strReq, tag_string_req);
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        if(uploaddaialog.isShowing()) {
            uploaddaialog.dismiss();
        }

        if (resultCode == 0) {

            feeds.clear();
            LoadFeeds("0");
            Toast.makeText(getApplicationContext(), "Posted Successfully", Toast.LENGTH_LONG).show();
            //    LoadFeeds("0");

        }else{
            Toast.makeText(getApplicationContext(), "Your account is blocked", Toast.LENGTH_LONG).show();
        }

    }


    private void parseresult(JSONObject args) {

        Gson gson = new Gson();


        try {
            JSONArray jaarray = args.getJSONArray("feeds");
            for (int i = 0; i < jaarray.length(); i++) {
                String eventString = jaarray.getJSONObject(i).toString();
                String title = jaarray.getJSONObject(i).getString("title");
                Log.d("awsedrf", "parseresult: " + title);
                Feed eobj = gson.fromJson(eventString, Feed.class);
                if (title.startsWith("polling{") && title.endsWith("}")) {
                    String poll_id = getPollId(title);
                    getPollingData(i, poll_id);

                }
                
                if(!feeds.contains(eobj)) {
                    feeds.add(eobj);
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (feeds.size() > 0)
            try {
                if(feeds.size()==1){
                    adapter.notifyDataSetChanged();
                    showview(true);}
                else{
                    adapter.notifyDataSetChanged();
                }
            }catch (Exception e)
            {
            }
        else
            showview(false);
    }
    private void getPollingData(int z, String pollId) {

        Health1NetworkController.getInstance().getService().getPolls(appDetails.getAppId()).enqueue(new Callback<PollingModel>() {
            @Override
            public void onResponse(Call<PollingModel> call, retrofit2.Response<PollingModel> response) {
                if (response.isSuccessful()) {
                    PollingModel pollingModel = response.body();
                    if (pollingModel.getResponse()) {
                        pollList = pollingModel.getPolls();
                        Log.d("polls_list", "onResponse: " + pollList.size());
                        for (int i = 0; i < pollList.size(); i++) {
                            if (pollList.get(i).getPollId().equals(pollId)) {
                                Log.d("polls_list", "onResponse: " + pollList.get(i).getPollId());
                                String answers = pollList.get(i).getVotes();
                                String participants = pollList.get(i).getVotedBy();
                                int total_vote = pollList.get(i).getTotalVotes();
                                if (participants.contains(user_id)) {
                                    check = true;
                                } else {
                                    check = false;
                                }
                                List<PollingAnswersPojo> answersPojos = new ArrayList<>();
                                try {

                                    JSONObject obj = new JSONObject(answers);
                                    Log.d("gggg", "onBindViewHolder: " + obj);
                                    Iterator<String> keysItr = obj.keys();
                                    Log.d("polls_list", "iteraor: " + keysItr.toString());

                                    while (keysItr.hasNext()) {
                                        String key = keysItr.next();
                                        answersPojos.add(new PollingAnswersPojo(key));
                                        Log.d("ggg_key", "onBindViewHolder: " + key);
                                    }
                                    Log.d("polls_list", "answer pojo: " + answersPojos.size());

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                if (!check) {
                                    feeds.get(z).setPoll_answersPojoList(answersPojos);
                                    feeds.get(z).setAnswerdPoll(check);
                                    adapter.notifyDataSetChanged();
                                    Log.d("polls_list", "check not " + check);

                                } else {
                                    List<PollingResultPojo> resultPojos = new ArrayList<>();
                                    try {
                                        JSONObject obj = new JSONObject(answers);
                                        Log.d("gggg", "onBindViewHolder: " + obj);
                                        Iterator<String> keysItr = obj.keys();
                                        while (keysItr.hasNext()) {
                                            String key = keysItr.next();
                                            Object value = obj.get(key);
                                            int count = (Integer) value;
                                            Log.d("ggg_key", "onBindViewHolder: " + key + "\n" + count);
                                            resultPojos.add(new PollingResultPojo(key, count, total_vote));
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    feeds.get(z).setPoll_resultPojos(resultPojos);
                                    feeds.get(z).setAnswerdPoll(check);
                                    adapter.notifyDataSetChanged();
                                    Log.d("polls_list", "check is " + check);

                                }
                            }
                        }
                    } else {

                        Toast.makeText(GroupFeedView.this, pollingModel.getResponseString(), Toast.LENGTH_SHORT).show();

                    }
                } else {

                    Toast.makeText(GroupFeedView.this, response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PollingModel> call, Throwable t) {

                Toast.makeText(GroupFeedView.this, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    private String getPollId(String value) {
        Pattern pattern = Pattern.compile("\\{([^}]+)\\}");
        Matcher m = pattern.matcher(value);
        String poll_id = "";
        while (m.find()) {
            System.out.println(m.group(1));
            poll_id = m.group(1);
        }
        return poll_id;
    }
    private void clearSharedPref(){
        SharedPreferences sharedPreferences = getSharedPreferences("poll", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    uploaddaialog = new ProgressDialog(GroupFeedView.this, R.style.MyAlertDialogStyle);
                    uploaddaialog.setMessage("Uploading ...");
                    uploaddaialog.show();
                    uploaddaialog.setCancelable(false);

                    Intent intent = new Intent(Intent.ACTION_SYNC, null, this, UploadPost.class);
                    intent.putExtra("receiver", mReceiver);
                    intent.putExtra("header", data.getStringExtra("header"));
                    intent.putExtra("attachment", data.getParcelableArrayListExtra("attachment"));
                    startService(intent);
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.morepost) {
            binding.productsRecycler.smoothScrollToPosition(0);
            visiblenewpost(false);


        }
    }

    @Override
    public void onScrollDirectionChanged(ScrollDirectionDetector.ScrollDirection scrollDirection) {

        this.mScrollDirection = scrollDirection;

    }


    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject json = new JSONObject();
                        // json.put("userid", Paper.book().read("userId", ""));
                        // json.put("appid", appDetails.getAppId());
                        // json.put("group_id",group_id);
                        String temp=convertStrigyfy();
                        mSocket.emit("group_checkin", temp);
                        System.out.println("T1 is calling");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
        }
    };

    private Emitter.Listener onConfirm = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!isFinishing()) {
                        int n=feeds.size();
                        System.out.print("value of feed size is "+n);

                        //  feeds.clear();
                        // adapter.notifyDataSetChanged();
                        //  System.out.print("T111 calling");
                        LoadFeeds("0");

                    }
                }
            });
        }
    };
    private Emitter.Listener onLikeConfirm = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println("T2 is calling");
                        JSONObject response = (JSONObject) args[0];
                        if (response.getBoolean("response")) {
                            JSONObject feed = new JSONObject((String) args[1]);
                            String eventString = feed.toString();
                            Feed eobj = new Gson().fromJson(eventString, Feed.class);
                            int pos = feeds.indexOf(eobj);
                            if (pos != -1)
                                try {
                                    adapter.notifyItemChanged(pos, ACTION_LIKE_BUTTON_CLICKED);
                                }catch (Exception e)
                                {
                                }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    };

    private Emitter.Listener onNewLike = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    try {
                        System.out.println("T3 is calling");
                        JSONObject json = new JSONObject((String) args[6]);
                        String eventString = json.toString();
                        Feed eobj = new Gson().fromJson(eventString, Feed.class);
                        int pos = feeds.indexOf(eobj);

                        if (pos != -1) {
                            if ((int) args[5] == 0)
                                feeds.get(pos).setLikes(feeds.get(pos).getLikesCount() - 1);
                            else
                                feeds.get(pos).setLikes(feeds.get(pos).getLikesCount() + 1);


                            try {
                                adapter.notifyDataSetChanged();
                            }catch (Exception e) {}

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        }
    };

    private Emitter.Listener onNewComment = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    try {
                        System.out.println("T4 is calling");
                        JSONObject json = new JSONObject((String) args[0]);
                        String eventString = json.toString();
                        Feed eobj = new Gson().fromJson(eventString, Feed.class);
                        int pos = feeds.indexOf(eobj);

                        if (pos != -1) {
                            feeds.get(pos).setComments(feeds.get(pos).getCommented() + 1);

                            try{ adapter.notifyDataSetChanged();}catch (Exception e)
                            {
                            }

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        }
    };


    private Emitter.Listener onNewFeed = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    try {
                        JSONObject data = (JSONObject) args[0];
                        Gson gson = new Gson();
                        String eventString = data.toString();
                        int n=feeds.size();
                        Feed eobj = gson.fromJson(eventString, Feed.class);
                        if(!feeds.contains(eobj)) {
                            feeds.add(0, eobj);
                        }

//                        adapter.notifyItemInserted(0);
                        adapter.notifyDataSetChanged();

                    }catch (Exception e)
                    {
                    }
                    if (linearLayoutManager.findFirstCompletelyVisibleItemPosition() == 0) {
                        binding.productsRecycler.smoothScrollToPosition(0);
                    } else {
                        if (binding.morepost.getVisibility() == View.GONE)
                            visiblenewpost(true);
                    }


                    if (feeds.size() > 0)
                        showview(true);
                    else
                        showview(false);

                }
            });
        }
    };


    @Override
    protected void onDestroy() {
        //LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiverss);
        super.onDestroy();
        mSocket.off("confirm_connection", onConnect);
        mSocket.off("confirm_feeds_checkin", onConfirm);
        mSocket.off("post_like_ack", onLikeConfirm);
        mSocket.off("new_like", onNewLike);
        mSocket.off("new_comment", onNewComment);
        mSocket.off("new_feed", onNewFeed);
        mSocket.disconnect();
        clearSharedPref();
    }


    public String convertStrigyfy( )
    {
        ArrayList<String> s=new ArrayList<>();

        s.add(group_id);




        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId",Paper.book().read("userId", ""));
            jsonObject.put("appid",appDetails.getAppId());
            jsonObject.put("groupIds",new JSONArray(Arrays.asList(s.toArray())));



        }catch (Exception e)
        {

        }
        String d=jsonObject.toString();

        return d;
    }
    @Override
    public void onLike(Feed feed, int islike, int pos) {
        Type type = new TypeToken<Feed>() {
        }.getType();
        String selected_post = new Gson().toJson(feed, type);
        mSocket.emit("post_like", appDetails.getAppId(), feed.getId(), Paper.book().read("userId", ""), Paper.book().read("username", ""), Paper.book().read("profile_pic", ""), "like", System.currentTimeMillis(), islike, selected_post);
    }

    @Override
    public void onComment(Feed feed, int pos) {
        CommentFragment.newInstance(mSocket, feed, feed.getId()).show(getSupportFragmentManager(), null);
    }

    @Override
    public void onReport(Feed feed, int pos) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.MyAlertDialogStyle);
        builder.setTitle("Are you sure You want to report this post ? ");
        //  builder.setMessage("Its better to update now");
        builder.setPositiveButton("Report", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Click button action
                dialog.dismiss();
                onPostReport(pos);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Cancel button action

            }
        });
        builder.setCancelable(false);
        builder.show();
//        Toast.makeText(Feeds.this, " Feeds Report ", Toast.LENGTH_LONG).show();


    }

    @Override
    public void onBlockUser(Feed feed, int pos) {

    }
    @Override
    public void onRecoverUser(Feed feed, int pos) {

    }

    @Override
    public void onDeletePost(int feedid) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.MyAlertDialogStyle);
        builder.setTitle("Are you sure You want to Delete this post ? ");
        //  builder.setMessage("Its better to update now");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Click button action
                dialog.dismiss();
                onPostDelete(feedid);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Cancel button action
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    private void onPostDelete(final int feedid) {
        final ProgressDialog dialog = new ProgressDialog(GroupFeedView.this, R.style.MyAlertDialogStyle);
        dialog.setMessage("Deleting post...");
        dialog.show();
        String tag_string_req = "Delete";
        String url = ApiList.Delete_Post;
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                try {
                    System.out.println(response);
                    JSONObject jObj = new JSONObject(response);
                    if (jObj.getBoolean("response")) {
                        feeds.clear();
                        LoadFeeds("0");
                    } else {
                        Error_Dialog.show(jObj.getString("responseString"), GroupFeedView.this);
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
                    Error_Dialog.show("Timeout", GroupFeedView.this);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, GroupFeedView.this), GroupFeedView.this);
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show("Please Check Your Internet Connection", GroupFeedView.this);
                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userid", Paper.book().read("userId", ""));
                params.put("appid", appDetails.getAppId());
                params.put("post_id", String.valueOf(feedid));
                System.out.println("Report Params : " + params);
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

    private void onPostReport(final int pos) {
        final ProgressDialog dialog = new ProgressDialog(GroupFeedView.this, R.style.MyAlertDialogStyle);
        dialog.setMessage("Reporting post...");
        dialog.show();
        String tag_string_req = "Report";
        String url = ApiList.Report_Post;
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                try {
                    System.out.println(response);
                    JSONObject jObj = new JSONObject(response);
                    if (jObj.getBoolean("response")) {
                        feeds.clear();
                        LoadFeeds("0");
                    } else {
                        Error_Dialog.show(jObj.getString("responseString"), GroupFeedView.this);
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
                    Error_Dialog.show("Timeout", GroupFeedView.this);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, GroupFeedView.this), GroupFeedView.this);
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show("Please Check Your Internet Connection", GroupFeedView.this);
                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userid", Paper.book().read("userId", ""));
                params.put("report_flag", String.valueOf(1));
                params.put("appid", appDetails.getAppId());
                params.put("post_id", String.valueOf(pos));
                System.out.println("Report Params : " + params);
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

    private void showview(boolean flag) {

        if (flag) {
            binding.productsRecycler.setVisibility(View.VISIBLE);
            binding.noitems.setVisibility(View.GONE);
        } else {
            binding.productsRecycler.setVisibility(View.GONE);
            binding.noitems.setVisibility(View.VISIBLE);
        }

    }


}
