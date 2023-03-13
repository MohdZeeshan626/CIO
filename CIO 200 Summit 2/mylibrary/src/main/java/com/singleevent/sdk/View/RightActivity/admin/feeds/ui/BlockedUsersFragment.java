package com.singleevent.sdk.View.RightActivity.admin.feeds.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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
import com.singleevent.sdk.Custom_View.VolleyErrorLis;
import com.singleevent.sdk.model.AppDetails;
import com.singleevent.sdk.R;
import com.singleevent.sdk.View.RightActivity.admin.adminSurvey.UsersModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import io.paperdb.Paper;

public class BlockedUsersFragment extends Fragment implements BlockedUsersAdapter.UnBlockUser {

    protected Context context;
    protected BlockedUsersAdapter adapter;
    protected AppDetails appDetails;
    protected RecyclerView recyclerView;
    //    protected ArrayList<Feed> feeds = new ArrayList<>();
    protected TextView tv_noItems;
    protected ProgressBar progressBar;
    List<UsersModel> userview;
    List<UsersModel> blocked_userview;

    String token, theme_color;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        Paper.init(context);
        appDetails = Paper.book().read("Appdetails");
        token = Paper.book().read("token");
        theme_color = appDetails.getTheme_color();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.act_blocked_users, container, false);


        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        tv_noItems = (TextView) view.findViewById(R.id.noitems);
//        progressBar = (ProgressBar) view.findViewById(R.id.loading);
        recyclerView.setLayoutManager(new LinearLayoutManager(context,
                LinearLayoutManager.VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        userview = new ArrayList<>();
        blocked_userview = new ArrayList<>();



      /*  new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                progressBar.setVisibility(View.GONE);
                tv_noItems.setVisibility(View.VISIBLE);
                tv_noItems.setText("NO USERS");
            }
        },3000);
*/

        getuser();


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private void getuser() {

        final ProgressDialog dialog = new ProgressDialog(getActivity(), R.style.MyAlertDialogStyle);
        dialog.setMessage("Loading the User");
        dialog.show();
        String tag_string_req = "getuser";
        String url = ApiList.Users + appDetails.getAppId() + "&admin_flag=attendee";
        StringRequest strReq = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                dialog.dismiss();
//                swipeRefreshLayout.setRefreshing(false);
               /* tv_checkInternet.setVisibility(View.GONE);
                if (swiperefresh.isRefreshing()) {
                    swiperefresh.setRefreshing(false);
                }*/
                try {
                    System.out.println(response);
                    JSONObject jObj = new JSONObject(response);
                    System.out.println("Beacon User Lists : " + jObj.toString());

                    if (jObj.getBoolean("response")) {
                        parseuser(jObj.getJSONObject("responseString").getJSONArray("users"));
                    } else {
                        if (jObj.getString("responseString").equalsIgnoreCase("Invalid token")) {
                            Error_Dialog.show("Session is Expired Please Login", getActivity());


                        } else
                            Error_Dialog.show(jObj.getString("responseString"), getActivity());
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    System.out.println("Error Msg " + e.getMessage());
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
               /* if (swiperefresh.isRefreshing()) {
                    swiperefresh.setRefreshing(false);
                }*/

                if (error instanceof TimeoutError) {
                    Error_Dialog.show("Timeout", getActivity());
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, getActivity()), getActivity());
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show("Please Check Your Internet Connection", getActivity());
                   /* if (!DataBaseStorage.isInternetConnectivity(RegisteredUserList.this)) {
//                        tv_checkInternet.setVisibility(View.VISIBLE);
                        userview = Paper.book(appDetails.getAppId()).read("BulkPirntUsers", new ArrayList<UsersModel>());
                        bulkPrintingUserAdapter = new BulkPrintingUserAdapter(RegisteredUserList.this, userview, theme_color, false);
                        userlist_rv.setAdapter(bulkPrintingUserAdapter);
                        setspinner();
                    }*/
                }

            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("token", token);
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


    private void parseuser(JSONArray responseString) {

        try {
            userview.clear();
            Gson gson = new Gson();

            Random r = new Random();

            for (int i = 0; i < responseString.length(); i++) {
                String eventString = responseString.getJSONObject(i).toString();
                UsersModel obj = gson.fromJson(eventString, UsersModel.class);
                obj.setColor(Color.argb(255, r.nextInt(256), r.nextInt(256), r.nextInt(256)));

                if (!Paper.book().read("userId", "").equals(obj.getUserid()))
                    userview.add(obj);
            }

            /*Shorting based in alphabet*/
            Collections.sort(userview, new Comparator<UsersModel>() {
                @Override
                public int compare(UsersModel usersModel, UsersModel t1) {
                    return usersModel.getFirst_name().compareToIgnoreCase(t1.getFirst_name());
                }


            });

//            Paper.book(appDetails.getAppId()).write("BulkPirntUsers", userview);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        if (userview.size() > 0) {
//            adapter.notifyDataSetChanged();
            blocked_userview.clear();
            for (int i = 0; i < userview.size(); i++) {
                if (userview.get(i).getBlocked_users() == 1)
                    blocked_userview.add(userview.get(i));
            }
            if (blocked_userview.size() > 0) {
                adapter = new BlockedUsersAdapter(getContext(), blocked_userview, theme_color, (BlockedUsersAdapter.UnBlockUser) this);
                recyclerView.setAdapter(adapter);
            } else {
                showview(false);
            }
        } else
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
    public void unblockuser(final String userid) {
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
                        blocked_userview.clear();
                        getuser();
                        Toast.makeText(getActivity(), "User UnBlocked", Toast.LENGTH_SHORT).show();
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
                params.put("block_userid", userid);
                params.put("block_status", String.valueOf(0));
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
