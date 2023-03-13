package com.singleevent.sdk.View.RightActivity.admin.feeds.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.singleevent.sdk.ApiList;
import com.singleevent.sdk.App;
import com.singleevent.sdk.Custom_View.Error_Dialog;
import com.singleevent.sdk.Custom_View.Util;
import com.singleevent.sdk.Custom_View.VolleyErrorLis;
import com.singleevent.sdk.feeds_class.OnItemClickListener;
import com.singleevent.sdk.gallery_camera.ScrollDetection.ScrollDirectionDetector;
import com.singleevent.sdk.Left_Adapter.FeedAdapter_co;
import com.singleevent.sdk.model.AppDetails;
import com.singleevent.sdk.model.Feed;
import com.singleevent.sdk.R;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.paperdb.Paper;
import io.socket.emitter.Emitter;

import static com.singleevent.sdk.Left_Adapter.FeedAdapter_co.ACTION_LIKE_BUTTON_CLICKED;


public class ReportedFeedsFragment extends Fragment implements OnItemClickListener, ScrollDirectionDetector.OnDetectScrollListener, View.OnClickListener {
    protected Context context;
    protected AppDetails appDetails;
    protected RecyclerView recyclerView;
    protected ArrayList<Feed> feeds = new ArrayList<>();
    protected TextView tv_noItems;
    protected ProgressBar progressBar;
    FeedAdapter_co adapter;
    private float dpWidth;
    private double margintop, iwidth;
    private int mScrollState = AbsListView.OnScrollListener.SCROLL_STATE_IDLE;
    private ScrollDirectionDetector.ScrollDirection mScrollDirection = ScrollDirectionDetector.ScrollDirection.UP;
    ScrollDirectionDetector mScrollDirectionDetector;
    LinearLayoutManager linearLayoutManager;
    private int preLast;
    private io.socket.client.Socket mSocket;
    private LinearLayout morepost;
    private TextView moreitems;
    private static final OvershootInterpolator OVERSHOOT_INTERPOLATOR = new OvershootInterpolator(4);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Paper.init(getContext());
        appDetails = Paper.book().read("Appdetails");
        View view = inflater.inflate(R.layout.fragment_feeds, container, false);
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        dpWidth = displayMetrics.widthPixels;
        iwidth = displayMetrics.widthPixels * 0.30;
        feeds = new ArrayList<>();

        linearLayoutManager = new LinearLayoutManager(getContext()) {
            @Override
            protected int getExtraLayoutSpace(RecyclerView.State state) {
                return 300;
            }
        };
        morepost = (LinearLayout) view.findViewById(R.id.morepost);
        moreitems = (TextView) view.findViewById(R.id.moreitems);
        recyclerView = (RecyclerView) view.findViewById(R.id.products_recycler);
        tv_noItems = (TextView) view.findViewById(R.id.noitems);
        progressBar = (ProgressBar) view.findViewById(R.id.loading);
        margintop = getResources().getDimensionPixelSize(R.dimen.abc_action_bar_default_height_material) + getResources().getDimensionPixelSize(R.dimen.abc_action_bar_default_height_material) * 0.50;
        morepost.setBackground(Util.setrounded(Color.parseColor(appDetails.getTheme_color())));
        CoordinatorLayout.LayoutParams param = (CoordinatorLayout.LayoutParams) morepost.getLayoutParams();
        param.width = (int) iwidth;
        param.height = CoordinatorLayout.LayoutParams.WRAP_CONTENT;
        param.setMargins(0, (int) margintop, 0, 0);
        morepost.setLayoutParams(param);

        moreitems.setTypeface(Util.boldtypeface(getContext()));

        morepost.setVisibility(View.GONE);
        morepost.setOnClickListener(this);

        recyclerView.setHasFixedSize(true);

        /*recyclerView.setLayoutManager(new LinearLayoutManager(context,
                LinearLayoutManager.VERTICAL, false));*/
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        adapter = new FeedAdapter_co(feeds, getContext(), dpWidth, this, "Reported_Feeds", appDetails);

        recyclerView.setAdapter(adapter);

       /* new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressBar.setIndeterminate(false);
                progressBar.setVisibility(View.GONE);
                tv_noItems.setVisibility(View.VISIBLE);
                tv_noItems.setText("NO FEEDS");
            }
        }, 3000);
*/

//        LoadFeeds("0");
        mScrollDirectionDetector = new ScrollDirectionDetector(this);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

                            if (lastItem == totalItemCount) {
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

        progressBar.setVisibility(View.VISIBLE);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.parseColor(appDetails.getTheme_color()),
                android.graphics.PorterDuff.Mode.MULTIPLY);
        return view;


    }

    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            if (getActivity() == null)
                return;
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject json = new JSONObject();
                        json.put("userid", Paper.book().read("userId", ""));
                        json.put("appid", appDetails.getAppId());
                        mSocket.emit("feeds_checkin", json);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        }
    };

    private Emitter.Listener onConfirm = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!getActivity().isFinishing()) {
                        feeds.clear();
                        LoadFeeds("0");
                    }
                }
            });
        }
    };
    private Emitter.Listener onLikeConfirm = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject response = (JSONObject) args[0];
                        if (response.getBoolean("response")) {
                            JSONObject feed = new JSONObject((String) args[1]);
                            String eventString = feed.toString();
                            Feed eobj = new Gson().fromJson(eventString, Feed.class);
                            int pos = feeds.indexOf(eobj);
                            if (pos != -1)
                                adapter.notifyItemChanged(pos, ACTION_LIKE_BUTTON_CLICKED);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext().getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    };

    private Emitter.Listener onNewLike = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    try {
                        JSONObject json = new JSONObject((String) args[6]);
                        String eventString = json.toString();
                        Feed eobj = new Gson().fromJson(eventString, Feed.class);
                        int pos = feeds.indexOf(eobj);

                        if (pos != -1) {
                            if ((int) args[5] == 0)
                                feeds.get(pos).setLikes(feeds.get(pos).getLikesCount() - 1);
                            else
                                feeds.get(pos).setLikes(feeds.get(pos).getLikesCount() + 1);

                            adapter.notifyDataSetChanged();

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
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    try {
                        JSONObject json = new JSONObject((String) args[0]);
                        String eventString = json.toString();
                        Feed eobj = new Gson().fromJson(eventString, Feed.class);
                        int pos = feeds.indexOf(eobj);

                        if (pos != -1) {
                            feeds.get(pos).setComments(feeds.get(pos).getCommented() + 1);

                            adapter.notifyDataSetChanged();

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
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {


                    JSONObject data = (JSONObject) args[0];
                    Gson gson = new Gson();
                    String eventString = data.toString();
                    Feed eobj = gson.fromJson(eventString, Feed.class);
                    feeds.add(0, eobj);
                    adapter.notifyItemInserted(0);
                    if (linearLayoutManager.findFirstCompletelyVisibleItemPosition() == 0) {
                        recyclerView.smoothScrollToPosition(0);
                    } else {
                        if (morepost.getVisibility() == View.GONE)
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

    private void visiblenewpost(boolean isvisble) {
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator bounceAnimX = null, bounceAnimY = null;

        if (isvisble) {
            bounceAnimX = ObjectAnimator.ofFloat(morepost, "scaleX", 0.2f, 1f);
            bounceAnimX.setDuration(600);
            bounceAnimX.setInterpolator(OVERSHOOT_INTERPOLATOR);

            bounceAnimY = ObjectAnimator.ofFloat(morepost, "scaleY", 0.2f, 1f);
            bounceAnimY.setDuration(600);
            bounceAnimY.setInterpolator(OVERSHOOT_INTERPOLATOR);
            bounceAnimY.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    morepost.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animator animation) {

                }
            });
        } else {
            bounceAnimX = ObjectAnimator.ofFloat(morepost, "alpha", 0);
            bounceAnimX.setDuration(600);
            bounceAnimX.setInterpolator(new DecelerateInterpolator());

            bounceAnimY = ObjectAnimator.ofFloat(morepost, "alpha", 1f);
            bounceAnimY.setDuration(600);
            bounceAnimY.setInterpolator(new DecelerateInterpolator());
            bounceAnimY.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    morepost.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationEnd(Animator animation) {

                }
            });
        }

        animatorSet.play(bounceAnimX).with(bounceAnimY);
        animatorSet.start();
    }

    private void LoadFeeds(String lastposttime) {

        String tag_string_req = "Get_Feed";
        String url = ApiList.Get_Feed + appDetails.getAppId() + "&userid=" + Paper.book().read("userId", "") + "&last_post_time=" + lastposttime + "&flag=report";

        progressBar.setVisibility(View.VISIBLE);
        StringRequest strReq = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                progressBar.setVisibility(View.GONE);
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
                progressBar.setVisibility(View.GONE);
                if (error instanceof TimeoutError) {
                    Error_Dialog.show("Timeout", getActivity());
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, getActivity()), getActivity());
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show("Please Check Your Internet Connection", getActivity());
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

    private void parseresult(JSONObject args) {

        Gson gson = new Gson();

        try {
            JSONArray jaarray = args.getJSONArray("feeds");
            for (int i = 0; i < jaarray.length(); i++) {
                String eventString = jaarray.getJSONObject(i).toString();
                Feed eobj = gson.fromJson(eventString, Feed.class);
                feeds.add(eobj);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (feeds.size() > 0)
            adapter.notifyDataSetChanged();
        else
            showview(false);

    }

    private void showview(boolean flag) {

        if (flag) {
            recyclerView.setVisibility(View.VISIBLE);
            tv_noItems.setVisibility(View.GONE);
        } else {
            recyclerView.setVisibility(View.GONE);
            tv_noItems.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onLike(Feed feed, int islike, int pos) {

    }

    @Override
    public void onComment(Feed feed, int pos) {

    }

    @Override
    public void onReport(Feed feed, int pos) {

    }

    @Override
    public void onBlockUser(Feed feed, int pos) {
        onBlockUser(feed.getUserid());
    }

    @Override
    public void onRecoverUser(Feed feed, int pos) {
        onRecoverUser(pos);
    }
    private void onBlockUser(final String block_userid) {
        final ProgressDialog dialog = new ProgressDialog(getActivity(), R.style.MyAlertDialogStyle);
        dialog.setMessage("Blocking User...");
        dialog.show();
        String tag_string_req = "Blocking";
        String url = ApiList.Block_User;
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
                        Toast.makeText(getActivity(), "User Blocked", Toast.LENGTH_SHORT).show();
//                        finish();
                    } else {
                        Error_Dialog.show(jObj.getString("responseString"), getActivity());
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
                    Error_Dialog.show("Timeout", getActivity());
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, getActivity()), getActivity());
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show("Please Check Your Internet Connection", getActivity());
                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("loggedin_userid", Paper.book().read("userId", ""));
                params.put("appid", appDetails.getAppId());
                params.put("block_userid", block_userid);
                params.put("block_status", String.valueOf(1));
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
    private void onRecoverUser(final int pos) {
        final ProgressDialog dialog = new ProgressDialog(getActivity(), R.style.MyAlertDialogStyle);
        dialog.setMessage("Recovering User...");
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
                        Error_Dialog.show(jObj.getString("responseString"), getActivity());
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
                    Error_Dialog.show("Timeout", getActivity());
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, getActivity()), getActivity());
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show("Please Check Your Internet Connection", getActivity());
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userid", Paper.book().read("userId", ""));
                params.put("report_flag", String.valueOf(0));
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

    @Override
    public void onDeletePost(int feedid) {


        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext(),R.style.MyAlertDialogStyle);
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
        final ProgressDialog dialog = new ProgressDialog(getActivity(), R.style.MyAlertDialogStyle);
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
                        Error_Dialog.show(jObj.getString("responseString"), getActivity());
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
                    Error_Dialog.show("Timeout", getActivity());
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, getActivity()), getActivity());
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show("Please Check Your Internet Connection", getActivity());
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

    @Override
    public void onScrollDirectionChanged(ScrollDirectionDetector.ScrollDirection scrollDirection) {
        this.mScrollDirection = scrollDirection;
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.morepost) {
            recyclerView.smoothScrollToPosition(0);
            visiblenewpost(false);


        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSocket.off("confirm_connection", onConnect);
        mSocket.off("confirm_feeds_checkin", onConfirm);
        mSocket.off("post_like_ack", onLikeConfirm);
        mSocket.off("new_like", onNewLike);
        mSocket.off("new_comment", onNewComment);
        mSocket.off("new_feed", onNewFeed);
        mSocket.disconnect();
    }
}
