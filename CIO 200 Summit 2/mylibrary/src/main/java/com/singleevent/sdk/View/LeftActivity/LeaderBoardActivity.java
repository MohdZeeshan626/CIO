package com.singleevent.sdk.View.LeftActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.SpannableString;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.gson.Gson;
import com.makeramen.roundedimageview.RoundedImageView;
import com.singleevent.sdk.Left_Adapter.LeaderBoardAdapter;
import com.singleevent.sdk.ApiList;
import com.singleevent.sdk.App;
import com.singleevent.sdk.Custom_View.Error_Dialog;
import com.singleevent.sdk.Custom_View.Util;
import com.singleevent.sdk.Custom_View.VolleyErrorLis;
import com.singleevent.sdk.model.AppDetails;
import com.singleevent.sdk.model.LeaderBoard.LeaderBoard;
import com.singleevent.sdk.model.User_Details;
import com.singleevent.sdk.R;
import com.singleevent.sdk.utils.DataBaseStorage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import io.paperdb.Paper;

/**
 * Created by webMOBI on 1/5/2018.
 */

public class LeaderBoardActivity extends AppCompatActivity {


    private static final String TAG = LeaderBoardActivity.class.getSimpleName();
    RecyclerView recyclerView;
    LeaderBoardAdapter adapter;
    Context context;
    Toolbar toolbar;
    AppDetails appDetails;
    private String checkValue,userId;
    private RoundedImageView iv_profile;
    private TextView tv_user_name, tv_no_items;
    private double CellWidth;
    private Button user_leadcount;
    private SwipeRefreshLayout swiperefresh;
    private ArrayList<LeaderBoard> leaderBoardArrayList,listexpectLoginUser;
    private LinearLayout view1;
    private String token;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Paper.init(this);
        setContentView(R.layout.activity_leaderboard);
        context = LeaderBoardActivity.this;
        appDetails = Paper.book().read("Appdetails");
        userId = Paper.book().read("userId","");
        leaderBoardArrayList = new ArrayList<>();
        listexpectLoginUser = new ArrayList<>();
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        swiperefresh = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        view1 = (LinearLayout) findViewById(R.id.view1);
        iv_profile = (RoundedImageView) findViewById(R.id.iv_profile);
        tv_user_name = (TextView) findViewById(R.id.tv_user_name);
        tv_no_items = (TextView) findViewById(R.id.tv_no_items);
        user_leadcount = (Button) findViewById(R.id.user_leadcount);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));
        user_leadcount.setBackground(Util.setdrawable(context, R.drawable.rectanglebackground,
                Color.parseColor(appDetails.getTheme_color())));

        user_leadcount.setVisibility(View.INVISIBLE);
        // dynamically setting cell size based on screen width
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        CellWidth = (displayMetrics.widthPixels * 0.20F);


        recyclerView.setLayoutManager(new LinearLayoutManager(context,
                LinearLayoutManager.VERTICAL,false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new LeaderBoardAdapter(context,leaderBoardArrayList,CellWidth);

        recyclerView.setAdapter(adapter);

        checkValue = Paper.book(appDetails.getAppId()).read("Gamification");
        //get leaderboard (calling api )
        getLeaders();
        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swiperefresh.setRefreshing(true);
                if (!DataBaseStorage.isInternetConnectivity(getBaseContext())){
                    view1.setVisibility(View.VISIBLE);
                }else {
                    view1.setVisibility(View.GONE);
                }
                getLeaders();
            }
        });

        token=Paper.book().read("token", "");

        if (token!=null && !token.equals(""))
        Getprofile();

    }

    private void setprofilepic(String bm) {

        Glide.with(getApplicationContext())

                .load(bm.equalsIgnoreCase("") ? R.drawable.default_user : bm)
                .asBitmap()
                .placeholder(R.drawable.default_user)
                .error(R.drawable.default_user)
                .into(iv_profile);
        iv_profile.setCornerRadius(12,12,12,12);
        /*new BitmapImageViewTarget(iv_profile) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(getResources(),
                                Bitmap.createScaledBitmap(resource, (int) CellWidth, (int) CellWidth, false));
                        drawable.setCircular(true);
                        iv_profile.setImageDrawable(drawable);
                    }
                });*/


    }

    //
    private void Getprofile() {


        final ProgressDialog pDialog = new ProgressDialog(LeaderBoardActivity.this,
                R.style.MyAlertDialogStyle );
        pDialog.setMessage("Loading ...");
        pDialog.show();


        String tag_string_req = "getprofilePic";
        String url = ApiList.GetProfile + userId;
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
                        setprofilepic(user_details.getProfile_pic());
                        tv_user_name.setText(user_details.getFirst_name()+" "+user_details.getLast_name());
                    } else {
                        if (jObj.getString("responseString").equalsIgnoreCase("Invalid token")) {
                            Intent i = new Intent();
                            i.putExtra("keyMessage", "Session Expired, Please Login ");
                            i.putExtra("keyAlert", "Session Expired");
                            i.setClassName(getApplicationContext().getPackageName(),
                                    getApplicationContext().getPackageName()+".SessionExpired");
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);

                        } else
                            Error_Dialog.show(jObj.getString("responseString"),
                                    LeaderBoardActivity.this);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                pDialog.dismiss();
                if (error instanceof TimeoutError) {
                    Error_Dialog.show("Timeout", LeaderBoardActivity.this);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error,
                            LeaderBoardActivity.this),
                            LeaderBoardActivity.this);
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    view1.setVisibility(View.VISIBLE);
                }
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("token", Paper.book().read("token", ""));
                System.out.println("token is "+headers);
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

    private void getLeaders() {

        final ProgressDialog pDialog = new ProgressDialog(LeaderBoardActivity.this,
                R.style.MyAlertDialogStyle );
        pDialog.setMessage("Please wait...");
        pDialog.show();


        String tag_string_req = "getLeaders";
        String url = ApiList.Get_Leader_board + "userid="+Paper.book().read("userId",
                "")+"&checkvalue="+checkValue+"&appid="+appDetails.getAppId();
        StringRequest strReq = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                pDialog.dismiss();
                System.out.println(response);
                swiperefresh.setRefreshing(false);

                Gson gson = new Gson();

                JSONObject jObj = null;
                try {
                    jObj = new JSONObject(response);
                    if (jObj.getBoolean("response")) {
                        JSONArray userList = jObj.getJSONArray("users");
                        leaderBoardArrayList.clear();
                        listexpectLoginUser.clear();
                        for (int count=0;count< userList.length();count++ ){

                            String userdetails = userList.getJSONObject(count).toString();
                            LeaderBoard user_details = gson.fromJson(userdetails, LeaderBoard.class);
                            leaderBoardArrayList.add(user_details);


                        }
                        String Email =  Paper.book().read("Email", "");
                        for (int count =0;count<leaderBoardArrayList.size();count++){
                            if (!leaderBoardArrayList.get(count).getEmail().toLowerCase().equals(Email.toLowerCase())){
                                listexpectLoginUser.add(leaderBoardArrayList.get(count));

                            }else {
                                //Setting lead points for logged-in user
                                user_leadcount.setText( String.valueOf(leaderBoardArrayList.get(count).getTotal_points()) );
                                Paper.book(appDetails.getAppId()).write("AuthUserTotalPoints",String.valueOf(leaderBoardArrayList.get(count).getTotal_points()));
                                if (Paper.book(appDetails.getAppId()).read("admin_flag","none").equals("admin"))
                                    user_leadcount.setVisibility(View.VISIBLE);
                                else {
                                    user_leadcount.setVisibility(View.VISIBLE);
                                }

                                if (leaderBoardArrayList.get(count).getTotal_points()>0 ){

                                }
                            }
                        }
                        adapter = new LeaderBoardAdapter(context,listexpectLoginUser,CellWidth);
                        recyclerView.setAdapter(adapter);

                        sortByLeads();
                        Paper.book(appDetails.getAppId()).write("OfflineLeaderBoardUserList",listexpectLoginUser);
                        if (listexpectLoginUser.size() > 0){
                            tv_no_items.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                        }else{
                            tv_no_items.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        }


                    } else {
                        Error_Dialog.show(jObj.getString("responseString"),
                                LeaderBoardActivity.this);
                    }
                } catch (JSONException e) {
                    Log.e(TAG,e.toString());

                }


            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                pDialog.dismiss();
                swiperefresh.setRefreshing(false);
                if (error instanceof TimeoutError) {
                    Error_Dialog.show("Timeout", LeaderBoardActivity.this);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error,
                            LeaderBoardActivity.this), LeaderBoardActivity.this);
                } else if (VolleyErrorLis.isNetworkProblem(error)) {

                    view1.setVisibility(View.VISIBLE);
                    listexpectLoginUser.clear();
                    user_leadcount.setText(Paper.book(appDetails.getAppId()).read("AuthUserTotalPoints",""));

                    listexpectLoginUser = Paper.book(appDetails.getAppId()).read("OfflineLeaderBoardUserList",
                            new ArrayList<LeaderBoard>());
                    if (listexpectLoginUser.size() > 0){
                        tv_no_items.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }else{
                        tv_no_items.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    }
                    adapter = new LeaderBoardAdapter(context,listexpectLoginUser,CellWidth);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
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



    private void sortByLeads() {
        Collections.sort(listexpectLoginUser, new Comparator<LeaderBoard>(){
            public int compare(LeaderBoard obj1, LeaderBoard obj2) {
                // ## Ascending order
                // return Integer.valueOf(obj1.getLeads()).compareTo(obj2.getLeads()); // To compare integer values
                // ## Descending order
                // return obj2.firstName.compareToIgnoreCase(obj1.firstName); // To compare string values
                return Integer.valueOf(obj2.getLead_points()).compareTo(obj1.getLead_points()); // To compare integer values
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();

            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onResume() {
        super.onResume();
        SpannableString s = new SpannableString("Leader Board");
        setTitle(Util.applyFontToMenuItem(this, s));
    }
}
