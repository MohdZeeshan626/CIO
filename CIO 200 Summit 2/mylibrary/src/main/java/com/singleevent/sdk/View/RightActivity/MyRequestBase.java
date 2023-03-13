package com.singleevent.sdk.View.RightActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.SpannableString;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

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
import com.singleevent.sdk.feeds_class.Scheduler;
import com.singleevent.sdk.model.My_Request;
import com.singleevent.sdk.R;
import com.singleevent.sdk.View.Fragment.Left_Fragment.MyAdapter;
import com.singleevent.sdk.databinding.ActivitySchedulerBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.paperdb.Paper;

/**
 * Created by webMOBI on 10/11/2017.
 */

public class MyRequestBase extends AppCompatActivity implements Scheduler {


    float dpWidth;
    ArrayList<My_Request> requestlist;
    private MyAdapter mAdapter;
    LinearLayoutManager mLayoutmanger;
    ActivitySchedulerBinding binding;
    String appid, userid, appname;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_scheduler);

        binding.nofav.setText("No Invitation");


        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        dpWidth = displayMetrics.widthPixels * 0.18F;
        requestlist = new ArrayList<>();

        // Initialize recycler view
        mLayoutmanger = new LinearLayoutManager(this);
        binding.recyclerview.setLayoutManager(mLayoutmanger);

        mAdapter = new MyAdapter(requestlist, MyRequestBase.this, true);
        binding.recyclerview.setAdapter(mAdapter);

        binding.recyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int firstVisibleItem = mLayoutmanger.findFirstVisibleItemPosition();
                if (firstVisibleItem == 0)
                    binding.container.setEnabled(true);
                else
                    binding.container.setEnabled(false);

            }
        });


        binding.container.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()

        {
            @Override
            public void onRefresh() {
                binding.container.setRefreshing(false);
                getslots();
            }
        });


    }


    public void init_myrequest(String theme_color, String appid, String userid, String appname) {
        this.appid = appid;
        this.userid = userid;
        this.appname = appname;
        binding.toolbar.setBackgroundColor(Color.parseColor(theme_color));
        setSupportActionBar(binding.toolbar);

        binding.container.setColorSchemeColors(Color.parseColor(theme_color), Color.GREEN, Color.BLUE, Color.YELLOW);

    }

    public void getslots() {

        final ProgressDialog dialog = new ProgressDialog(MyRequestBase.this, R.style.MyAlertDialogStyle);
        dialog.setMessage("Fetching the booked timeslot....");
        dialog.show();
        String tag_string_req = "Getting_Slot";
        String url = ApiList.Get_RequestedTimeSlot + appid + "&userid=" + userid;
        Log.d("awsedrd", "getslots: "+url);
        StringRequest strReq = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                try {
                    System.out.println(response);
                    JSONObject jObj = new JSONObject(response);

                    if (jObj.getBoolean("response")) {
                        parsetimeslot(jObj.getJSONArray("pending_requests"));
                    } else {
                        if (jObj.getString("responseString").equalsIgnoreCase("Invalid token")) {
                            Intent i = new Intent();
                            i.putExtra("keyMessage", "Session Expired, Please Login ");
                            i.putExtra("keyAlert", "Session Expired");
                            i.setClassName(getPackageName(), getPackageName() + ".SessionExpired");
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                        } else {
                            Error_Dialog.show(jObj.getString("responseString"), MyRequestBase.this);
                            binding.recyclerview.setVisibility(View.GONE);
                            binding.nofav.setVisibility(View.VISIBLE);
                        }
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
                    Error_Dialog.show("Timeout", MyRequestBase.this);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, MyRequestBase.this), MyRequestBase.this);
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show("Please Check Your Internet Connection", MyRequestBase.this);
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

    private void parsetimeslot(JSONArray pending_requests) {
        try {
            requestlist.clear();
            Gson gson = new Gson();

            for (int i = 0; i < pending_requests.length(); i++) {
                String eventString = null;

                eventString = pending_requests.getJSONObject(i).toString();
                My_Request data = gson.fromJson(eventString, My_Request.class);
                requestlist.add(data);

                if (requestlist.size() > 0) {
                    mAdapter.notifyDataSetChanged();
                    binding.recyclerview.setVisibility(View.VISIBLE);
                    binding.nofav.setVisibility(View.GONE);
                } else {
                    binding.recyclerview.setVisibility(View.GONE);
                    binding.nofav.setVisibility(View.VISIBLE);
                }


            }
        } catch (JSONException e) {

        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        SpannableString s = new SpannableString("Inbox");
        setTitle(Util.applyFontToMenuItem(this, s));

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
    public void accept(My_Request mtrequest) {
        System.out.println(mtrequest.getAppid());
        int pos = requestlist.indexOf(mtrequest);
        if (pos != -1)
            SubmitDetails(mtrequest, 0, "accepted");


    }

    @Override
    public void decline(My_Request mtrequest) {
        System.out.println(mtrequest.getAppid());
        SubmitDetails(mtrequest, 0, "rejected");

    }


    private void SubmitDetails(final My_Request myrequest, final int pos, final String status) {

        final ProgressDialog dialog = new ProgressDialog(MyRequestBase.this);
        dialog.setMessage("Sending the confirmation...");
        dialog.show();
        String tag_string_req = "Book_TimeSlot";
        String url = ApiList.SendingBookingRequest;
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                try {
                    System.out.println(response);
                    JSONObject jObj = new JSONObject(response);

                    if (jObj.getBoolean("response")) {
                        requestlist.remove(pos);
                        mAdapter.notifyDataSetChanged();
                        if (requestlist.size() == 0) {
                            binding.recyclerview.setVisibility(View.GONE);
                            binding.nofav.setVisibility(View.VISIBLE);
                        }
                    } else {
                        if (jObj.getString("responseString").equalsIgnoreCase("Invalid token")) {
                            Error_Dialog.show("Session Expired, Please Login", MyRequestBase.this);
                        } else
                            Error_Dialog.show(jObj.getString("responseString"), MyRequestBase.this);
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
                    Error_Dialog.show("Timeout", MyRequestBase.this);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, MyRequestBase.this), MyRequestBase.this);
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show("Please Check Your Internet Connection", MyRequestBase.this);
                }
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("appid", appid);
                params.put("userid", myrequest.getUserid());
                params.put("client_id", userid);
                params.put("meeting_date", String.valueOf(myrequest.getMeeting_date()));
                params.put("from_time", String.valueOf(myrequest.getFrom_time()));
                params.put("status", status);
                params.put("appname", appname);
                params.put("username", Paper.book().read("username", ""));

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
}
