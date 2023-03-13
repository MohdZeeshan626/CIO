package com.singleevent.sdk.View.RightActivity.admin.feeds.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.SparseArray;
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
import com.google.gson.reflect.TypeToken;
import com.singleevent.sdk.ApiList;
import com.singleevent.sdk.App;
import com.singleevent.sdk.Custom_View.Error_Dialog;
import com.singleevent.sdk.Custom_View.Util;
import com.singleevent.sdk.Custom_View.VolleyErrorLis;
import com.singleevent.sdk.feeds_class.OnItemClickListener;
import com.singleevent.sdk.gallery_camera.ScrollDetection.ScrollDirectionDetector;
import com.singleevent.sdk.Left_Adapter.FeedAdapter_co;
import com.singleevent.sdk.Left_Adapter.FeedItemAnimator;
import com.singleevent.sdk.model.AppDetails;
import com.singleevent.sdk.model.Feed;
import com.singleevent.sdk.R;
import com.singleevent.sdk.View.Fragment.Left_Fragment.CommentFragment;
import com.singleevent.sdk.service.UploadResultReceiver;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.paperdb.Paper;
import io.socket.emitter.Emitter;

import static com.singleevent.sdk.Left_Adapter.FeedAdapter_co.ACTION_LIKE_BUTTON_CLICKED;


public class FeedFragment extends Fragment implements UploadResultReceiver.Receiver,
        View.OnClickListener, ScrollDirectionDetector.OnDetectScrollListener, OnItemClickListener {

    AppDetails appDetails;
    private float dpWidth;
    FeedAdapter_co adapter;
    SparseArray<Feed> feeds_co = new SparseArray<>();

    ArrayList<Feed> feeds = new ArrayList<>();
    private UploadResultReceiver mReceiver;
    private double margintop, iwidth;

    private static final OvershootInterpolator OVERSHOOT_INTERPOLATOR = new OvershootInterpolator(4);
    LinearLayoutManager linearLayoutManager;

    private int mScrollState = AbsListView.OnScrollListener.SCROLL_STATE_IDLE;

    /**
     * Initial scroll direction should be UP in order to set as active most top item if no active item yet
     */
    private ScrollDirectionDetector.ScrollDirection mScrollDirection = ScrollDirectionDetector.ScrollDirection.UP;
    ScrollDirectionDetector mScrollDirectionDetector;

    private int preLast;
    private io.socket.client.Socket mSocket;

    private RecyclerView products_recycler;
    private LinearLayout morepost;
    private TextView moreitems,
            noitems;
    protected ProgressBar loading;


    @Override
    public void onStart() {
        super.onStart();
        mReceiver = new UploadResultReceiver(new Handler());
        mReceiver.setReceiver(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feeds, container, false);

        Paper.init(getContext());
        appDetails = Paper.book().read("Appdetails");

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        dpWidth = displayMetrics.widthPixels;
        iwidth = displayMetrics.widthPixels * 0.30;
        feeds = new ArrayList<>();

        loading = (ProgressBar) view.findViewById(R.id.loading);
        noitems = (TextView) view.findViewById(R.id.noitems);
        morepost = (LinearLayout) view.findViewById(R.id.morepost);
        moreitems = (TextView) view.findViewById(R.id.moreitems);
        products_recycler = (RecyclerView) view.findViewById(R.id.products_recycler);
        linearLayoutManager = new LinearLayoutManager(getContext()) {
            @Override
            protected int getExtraLayoutSpace(RecyclerView.State state) {
                return 300;
            }
        };
        products_recycler.setHasFixedSize(true);
        products_recycler.setLayoutManager(linearLayoutManager);

        adapter = new FeedAdapter_co(feeds, getContext(), dpWidth, this, "Admin_Panel",appDetails);
        products_recycler.setAdapter(adapter);
        products_recycler.setItemAnimator(new FeedItemAnimator());
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


        mScrollDirectionDetector = new ScrollDirectionDetector(this);

        products_recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

        loading.setVisibility(View.VISIBLE);
        loading.getIndeterminateDrawable().setColorFilter(Color.parseColor(appDetails.getTheme_color()),
                android.graphics.PorterDuff.Mode.MULTIPLY);


        return view;


    }

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

    public void LoadFeeds(String lastposttime) {

        String tag_string_req = "Get_Feed";
        String url = ApiList.Get_Feed + appDetails.getAppId() + "&userid=" + Paper.book().read("userId", "") + "&last_post_time=" + lastposttime;

        loading.setVisibility(View.VISIBLE);
        StringRequest strReq = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                loading.setVisibility(View.GONE);
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
                loading.setVisibility(View.GONE);
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


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.morepost) {
            products_recycler.smoothScrollToPosition(0);
            visiblenewpost(false);


        }
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {

        if (resultCode == 0) {
           feeds.clear();
            Toast.makeText(getContext().getApplicationContext(), "Posted Successfully", Toast.LENGTH_LONG).show();
            LoadFeeds("0");

        }

    }

    @Override
    public void onScrollDirectionChanged(ScrollDirectionDetector.ScrollDirection scrollDirection) {

        this.mScrollDirection = scrollDirection;

    }


    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
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
                        products_recycler.smoothScrollToPosition(0);
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


    @Override
    public void onLike(Feed feed, int islike, int pos) {
        Type type = new TypeToken<Feed>() {
        }.getType();
        String selected_post = new Gson().toJson(feed, type);
        mSocket.emit("post_like", appDetails.getAppId(), feed.getId(), Paper.book().read("userId", ""), decodeStringValue(Paper.book().read("user", "")), Paper.book().read("profile_pic", ""), "like", System.currentTimeMillis(), islike, selected_post);
    }

    @Override
    public void onComment(Feed feed, int pos) {
        CommentFragment.newInstance(mSocket, feed, feed.getId()).show(getActivity().getSupportFragmentManager(), null);
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

    private void showview(boolean flag) {

        if (flag) {
            products_recycler.setVisibility(View.VISIBLE);
            noitems.setVisibility(View.GONE);
        } else {
            products_recycler.setVisibility(View.GONE);
            noitems.setVisibility(View.VISIBLE);
        }

    }

    private String decodeStringValue(String decodestr) {
        String result = null;

        try {
            result = new String(Base64.decode(decodestr, Base64.NO_WRAP), "UTF-16");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        return result;
    }


}
