package com.singleevent.sdk.health.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.singleevent.sdk.ApiList;
import com.singleevent.sdk.App;
import com.singleevent.sdk.Custom_View.Error_Dialog;
import com.singleevent.sdk.Custom_View.VolleyErrorLis;
import com.singleevent.sdk.health.Adapter.RecyclerViewAdapter;
import com.singleevent.sdk.health.HistoryApiActivity;
import com.singleevent.sdk.R;
import com.singleevent.sdk.health.Model.GetChallengeList;
import com.singleevent.sdk.health.Model.LeaderBoardPoints;
import com.singleevent.sdk.model.AppDetails;
import com.singleevent.sdk.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.paperdb.Paper;

import static android.view.View.GONE;

public class LeaderBoardActivity extends AppCompatActivity {
    private ArrayList<String> mImageNames = new ArrayList<>();
    private ArrayList<String> mImages = new ArrayList<>();
    private ArrayList<String> mTotalkm = new ArrayList<>();
    private ArrayList<Integer> mPosition = new ArrayList<>();
    RecyclerViewAdapter adapter;
    RecyclerView recyclerView;
    Button btn;
    ArrayList<GetChallengeList> challengeLists =new ArrayList<>();
    AppDetails appDetails;
    ArrayList<LeaderBoardPoints> leaderBoardPoints=new ArrayList<>();
    List<com.singleevent.sdk.model.User> userview;
    int totalsteps;
    List<String > list;
    String[] values;
    TextView participants;


    FloatingActionButton floatingActionButton;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard_health);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerview);
        btn =(Button)findViewById(R.id.testBtn);
        participants=(TextView) findViewById(R.id.participants);
        appDetails = Paper.book().read("Appdetails");
        userview =new ArrayList<>();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), HistoryApiActivity.class);
                startActivity(intent);
                finish();
            }
        });


        challengeLists=Paper.book().read("challengelist",null);
        if(challengeLists!=null&& challengeLists.size()>0) {
            if (challengeLists.get(0).getJoinedChallenge() != null && !challengeLists.get(0).getJoinedChallenge().equalsIgnoreCase("")) {

                values = challengeLists.get(0).getJoinedChallenge().split(",");
                list = new ArrayList<String>(Arrays.asList(values));
            }
        }getuser();
        getLeaderBoardPoints();

        /*floatingActionButton = (FloatingActionButton)findViewById(R.id.addFab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {getget
                Toast.makeText(getApplicationContext(),"Features coming soon",Toast.LENGTH_SHORT).show();
            }
        });*/
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        Menu menu = bottomNav.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);
       // initImages();
    //    getSupportActionBar().hide();
    }
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {


                        if(item.getItemId()== R.id.nav_home) {
                            Intent in = new Intent(getApplicationContext(), ActivityHistory.class);
                            startActivity(in);
                            finish();
                        }
                         if(item.getItemId()== R.id.nav_friends)

                        {

                        }

                        else if(item.getItemId()==R.id.nav_challenge) {
                        Intent inte = new Intent(getApplicationContext(), ChallengeActivity.class);
                        startActivity(inte);
                        finish();
                    }


//                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                            selectedFragment).commit();
                    return true;
                }
            };

    private void initImages()
    {
        try {
            List<String> listing = new ArrayList<String>();


            if (leaderBoardPoints.size() > 0) {
                for (int i = 0; i < userview.size(); i++) {
                    if(userview.contains(list.get(0)))
                    for (int j = 0; j < list.size(); j++) {
                        try {
                            if (userview.get(i).getUserid().toString().equalsIgnoreCase(list.get(j).toString())) {
                                listing.add(userview.get(i).getFirst_name() + " " + userview.get(i).getLast_name());
                                mImages.add(userview.get(i).getProfile_pic());
                                mImageNames.add(userview.get(i).getFirst_name() + " " + userview.get(i).getLast_name());

                                //   mTotalkm.add(String.valueOf(leaderBoardPoints.get(i).getSteps()));
                                for (int k = 0; k < leaderBoardPoints.size(); k++) {
                                    totalsteps += leaderBoardPoints.get(k).getSteps();
                                    if (leaderBoardPoints.get(k).getUser_id().equalsIgnoreCase(list.get(j))) {
                                        mTotalkm.add(String.valueOf(leaderBoardPoints.get(k).getSteps()));
                                    }

                                }
                                //  mTotalkm.add("22 KM");
                                if(i==0){
                                mPosition.add((R.drawable.medfirst));}
                                if(i==1){
                                    mPosition.add((R.drawable.medsecond));
                                }
                                else{
                                    mPosition.add((R.drawable.medthird));
                                }

                            }
                        } catch (Exception e) {
                        }
                    }

                }
            }
            try {
                participants.setText(challengeLists.get(0).getJoinedChallenge().split(",").length + " " + "Participants");
            }catch (Exception e){

            }


            initRecyclerView();
        }catch (Exception e){

        }
    }
    private void initRecyclerView()
    {
        try {

            adapter = new RecyclerViewAdapter(mImageNames, mImages, mTotalkm, mPosition, this);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            adapter.notifyDataSetChanged();
        }
        catch (Exception e){

        }
    }

    public void getLeaderBoardPoints() {
try {
    String tag_string_req = "Checking Update";
    String url = "https://api2.webmobi.com/health/getleaderboard?app_id=" + appDetails.getAppId() + "&challenge_id=" + challengeLists.get(0).getChallenge_id();
    System.out.println(url);
    final ProgressDialog pDialog = new ProgressDialog(LeaderBoardActivity.this, R.style.MyAlertDialogStyle);
    pDialog.setMessage("Checking ...");
    pDialog.show();

    JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, new JSONObject(), new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            try {
                Gson gson = new Gson();
                pDialog.dismiss();
                System.out.println("JSON Response " + response);
                if (response.getBoolean("response")) {
                    JSONArray jsonArray = response.getJSONArray("data");
                    leaderBoardPoints.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        String temp = jsonArray.getJSONObject(i).toString();
                        LeaderBoardPoints obj = gson.fromJson(temp, LeaderBoardPoints.class);
                        leaderBoardPoints.add(obj);
                        // totalsteps+=obj.getSteps()
                    }
                    initImages();

                }
            } catch (JSONException e1) {
                e1.printStackTrace();
            }

        }
    }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            pDialog.dismiss();
            if (error instanceof TimeoutError) {
                Error_Dialog.show("Timeout", LeaderBoardActivity.this);
            } else if (VolleyErrorLis.isServerProblem(error)) {
                Error_Dialog.show(VolleyErrorLis.handleServerError(error, LeaderBoardActivity.this), LeaderBoardActivity.this);
            } else if (VolleyErrorLis.isNetworkProblem(error)) {
                Error_Dialog.show("Please Check Your Internet Connection", LeaderBoardActivity.this);
            }

        }

    }) {

    };


    App.getInstance().addToRequestQueue(jsonRequest, tag_string_req);
    jsonRequest.setRetryPolicy(new DefaultRetryPolicy(
            500000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
    ));
}catch (Exception e){}
    }
    private void getuser() {

        final ProgressDialog dialog = new ProgressDialog(LeaderBoardActivity.this, R.style.MyAlertDialogStyle);
        dialog.setMessage("Loading the User");
        dialog.show();
        String tag_string_req = "Login";
        String url = ApiList.Users + appDetails.getAppId()+"&userid="+Paper.book().read("userId", "")+"&admin_flag=attendee_option";
        StringRequest strReq = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                try {

                    JSONObject jObj = new JSONObject(response);


                    if (jObj.getBoolean("response")) {
                        userview.clear();

                        parseuser(jObj.getJSONObject("responseString").getJSONArray("users"));
                    } else {
                        if (jObj.getString("responseString").equalsIgnoreCase("Invalid token")) {
                            Intent i = new Intent();
                            i.putExtra("keyMessage", "Session Expired, Please Login ");
                            i.putExtra("keyAlert", "Session Expired");
                            i.setClassName(getPackageName(), "com.singleevent.sdk.View.TokenExpireAlertReceived");
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);

                        } else
                            Error_Dialog.show(jObj.getString("responseString"), LeaderBoardActivity.this);
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
                    Error_Dialog.show("Timeout", LeaderBoardActivity.this);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, LeaderBoardActivity.this), LeaderBoardActivity.this);
                } else if (VolleyErrorLis.isNetworkProblem(error)) {


                    userview.clear();
                    userview = Paper.book(appDetails.getAppId()).read("OfflineAttendeeList",new ArrayList<User>());

                    if (userview.size() > 0) {

                    } else{}
                    // showview(false);

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
    private void parseuser(JSONArray responseString) {
        try {
            userview.clear();
            Gson gson = new Gson();

            Random r = new Random();

            for (int i = 0; i < responseString.length(); i++) {
                String eventString = responseString.getJSONObject(i).toString();
                User obj = gson.fromJson(eventString, User.class);
                obj.setColor(Color.argb(255, r.nextInt(256), r.nextInt(256), r.nextInt(256)));

                userview.add(obj);

            }

            Collections.sort(userview, new Comparator<User>() {
                @Override
                public int compare(User o1, User o2) {
                    return o1.getFirst_name().compareToIgnoreCase(o2.getFirst_name());
                }
            });

            Paper.book(appDetails.getAppId()).write("OfflineAttendeeList",userview);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (userview.size() > 0) {

        } else{}
        // showview(false);


    }

}