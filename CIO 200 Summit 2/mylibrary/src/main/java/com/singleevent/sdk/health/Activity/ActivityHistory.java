package com.singleevent.sdk.health.Activity;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.singleevent.sdk.ApiList;
import com.singleevent.sdk.App;
import com.singleevent.sdk.Custom_View.Error_Dialog;
import com.singleevent.sdk.Custom_View.VolleyErrorLis;
import com.singleevent.sdk.health.Adapter.MyAdapter;
import com.singleevent.sdk.health.Fragment.ActiveminsFragment;
import com.singleevent.sdk.health.Fragment.DistanceFragment;
import com.singleevent.sdk.health.Fragment.StepsFragment;
import com.singleevent.sdk.R;
import com.singleevent.sdk.model.AppDetails;
import com.singleevent.sdk.model.User_Details;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

import static java.text.DateFormat.getDateInstance;

public class ActivityHistory extends AppCompatActivity {


    TabLayout tabLayout;
    ViewPager viewPager;
    AppDetails appDetails;
    FloatingActionButton floatingActionButton;
    CircleImageView healthprofilepic;
    TextView healthuname,healthudetails;
    User_Details user_details;
    Context context;
    long total;
    double totaldist, km;
    String totalSteps;
    TextView tsteps,tmiles,tmins;
    Task<DataSet> result;
    long active;
    private ArrayList<String> step = new ArrayList<>();
    private  ArrayList<String> dist = new ArrayList<>();
    private  ArrayList<String> mins = new ArrayList<>();
    private static DecimalFormat df = new DecimalFormat("0.00");
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
         context = getApplicationContext();
         tsteps=(TextView)findViewById(R.id.tsteps);
        tmiles=(TextView)findViewById(R.id.tmiles);
        tmins=(TextView)findViewById(R.id.tmins);
        //setprofile();
        appDetails= Paper.book().read("Appdetails");
        user_details=Paper.book().read("UserDetailsOffline");
        /*floatingActionButton = (FloatingActionButton)findViewById(R.id.addFab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Features coming soon",Toast.LENGTH_SHORT).show();
            }
        });*/
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        Menu menu = bottomNav.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);


     //   getSupportActionBar().setDisplayHomeAsUpEnabled(true);
     ///  getSupportActionBar().setDisplayShowHomeEnabled(true);
    //    getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_chevron_left_black_24dp);

        //tabLayout

        tabLayout = findViewById(R.id.tabLayoutInfo);
        viewPager = findViewById(R.id.viewPagerInfo);
        healthuname=findViewById(R.id.healthuname);
        healthudetails=findViewById(R.id.healthudetails);
        healthprofilepic=findViewById(R.id.healthprofilepic);
        Getprofile();
        step=Paper.book().read("Weekly_Steps",null);
        mins=Paper.book().read("Weekly_Mins",null);
        dist=Paper.book().read("Weekly_Miles",null);
        if(step!=null) {
            int p = getday(LocalDate.now().getDayOfWeek().name());
            tsteps.setText(step.get(p));
        }
        if(mins!=null) {
            int p = getday(LocalDate.now().getDayOfWeek().name());
            tmins.setText(mins.get(p) + " mins");
        }
        if(dist!=null) {
            int p = getday(LocalDate.now().getDayOfWeek().name());
            Double d=Double.parseDouble(dist.get(p));
            String str = String.format("%.1f", d);
            tmiles.setText(str+" km");

        }
      //  readData();
      //  readDistanceData();
      //  readTotlMins();


       try {
           healthuname.setText(Paper.book().read("username").toString());
           healthudetails.setText(user_details.getEmail());

       }catch (Exception e)
       {

       }


        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#000000"));
        MyAdapter tabadapter = new MyAdapter(getSupportFragmentManager());
        tabadapter.AddFragment(new StepsFragment(),"Steps");
        tabadapter.AddFragment(new DistanceFragment(),"Distance");
        tabadapter.AddFragment(new ActiveminsFragment(),"Active Minutes");
        viewPager.setAdapter(tabadapter);
        tabLayout.setupWithViewPager(viewPager);

    }
    public int getday(String day){

        if(day.equalsIgnoreCase("MONDAY")){
            return 0;
        }
        if(day.equalsIgnoreCase("TUESDAY")){
            return 1;
        }
        if(day.equalsIgnoreCase("WEDNESDAY")){
            return 2;
        }
        if(day.equalsIgnoreCase("THURSDAY")){
            return 3;
        }
        if(day.equalsIgnoreCase("FRIDAY")){
            return 4;
        }
        if(day.equalsIgnoreCase("SATURDAY")){
            return 5;
        }
        if(day.equalsIgnoreCase("SUNDAY")){
            return 6;
        }
        return  0;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_health,menu);
        return true;

    }
    public  void setprofile(User_Details user_details)
    {
       // User_Details user_details1 = Paper.book().read("UserDetailsOffline");
        String s=user_details.getProfile_pic();//Paper.book().read("ProfilePIC", null);
        try{
        if(s!=null) {
            Glide.with(context.getApplicationContext())
                    .load(s)
                    .placeholder(R.drawable.round_user)
                    .error(R.drawable.round_user)
                    .into((healthprofilepic) );
        }}catch (Exception e)
        {

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent= new Intent(this, ChallengeActivity.class);

            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {


                       /* if(item.getItemId()== R.id.nav_home) {
                            Intent in = new Intent(getApplicationContext(), HomeActivity.class);
                            startActivity(in);
                        }*/
                         if(item.getItemId()== R.id.nav_friends) {
                            Intent in1 = new Intent(getApplicationContext(), LeaderBoardActivity.class);
                            startActivity(in1);
                            finish();
                        }


                        else if(item.getItemId()== R.id.nav_challenge) {
                             Intent inte = new Intent(getApplicationContext(), ChallengeActivity.class);
                             startActivity(inte);
                             finish();
                        }


//                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                            selectedFragment).commit();
                    return true;
                }
            };



    private void Getprofile() {


        final ProgressDialog pDialog = new ProgressDialog(ActivityHistory.this, R.style.MyAlertDialogStyle);
        pDialog.setMessage("Loading ...");
        pDialog.show();


        String tag_string_req = "Login";
        String url = ApiList.GetProfile + Paper.book().read("userId");
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
                        setprofile(user_details);
                    } else {
                        if (jObj.getString("responseString").equalsIgnoreCase("Invalid token")) {
                            Intent i = new Intent();
                            i.putExtra("keyMessage", "Session Expired, Please Login ");
                            i.putExtra("keyAlert", "Session Expired");
                            i.setClassName("com.webmobi.eventsapp", "com.webmobi.eventsapp.Views.TokenExpireAlertReceived");
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                        } else
                            Error_Dialog.show(jObj.getString("responseString"), ActivityHistory.this);
                    }
                } catch (JSONException e) {

                }


            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                pDialog.dismiss();
                if (error instanceof TimeoutError) {
                    Error_Dialog.show("Timeout", ActivityHistory.this);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, ActivityHistory.this), ActivityHistory.this);
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show("Please Check Your Internet Connection", ActivityHistory.this);
                    User_Details user_details = Paper.book().read("UserDetailsOffline");
                    /*if (user_details != null)
                        setdataOffline(user_details);*/
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


    private void readData() {

        try {
            Fitness.getHistoryClient(this, GoogleSignIn.getLastSignedInAccount(this))
                    .readDailyTotal(DataType.TYPE_STEP_COUNT_DELTA)
                    .addOnSuccessListener(
                            new OnSuccessListener<DataSet>() {
                                @Override
                                public void onSuccess(DataSet dataSet) {
                                    if (dataSet.isEmpty()) {
                                        Toast.makeText(getApplicationContext(), "There was a problem getting the step count.", Toast.LENGTH_SHORT).show();

                                    } else {
                                        total =
                                                dataSet.isEmpty()
                                                        ? 0
                                                        : dataSet.getDataPoints().get(0).getValue(Field.FIELD_STEPS).asInt();
//                                    Log.i(TAG, "Total steps: " + total);
//                                    Toast.makeText(getApplicationContext(), "Total Steps" + total, Toast.LENGTH_SHORT).show();
                                        totalSteps = String.valueOf(total);

                                        Paper.book().write("total_steps", totalSteps);
                                       // Log.i(TAG, "totalStep" + total);


                                    }
                                }
                            })
                    .addOnFailureListener(
                            new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                   // Log.w(TAG, "There was a problem getting the step count.", e);
                                }
                            });
        }catch (Exception e)
        {

        }
    }


    private void readDistanceData() {
        try {
            result = Fitness.getHistoryClient(this, GoogleSignIn.getLastSignedInAccount(this))
                    .readDailyTotal((DataType.TYPE_DISTANCE_DELTA))
                    .addOnSuccessListener(
                            new OnSuccessListener<DataSet>() {
                                @Override
                                public void onSuccess(DataSet dataSet) {
                                    totaldist =
                                            dataSet.isEmpty()
                                                    ? 0
                                                    : dataSet.getDataPoints().get(0).getValue(Field.FIELD_DISTANCE).asFloat();
//                                Log.i(TAG, "Total Distance: " + totaldist);
//                                Toast.makeText(getApplicationContext(),"Total Calories"+total,Toast.LENGTH_SHORT).show();
                                    //converting meters to km
                                    km = totaldist / 1000;
                                    String str = String.format("%.1f", km);

                                    System.out.println("distance" + str);
                                   // tmiles.setText(str + " km");


                                }
                            })
                    .addOnFailureListener(
                            new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                  //  Log.w(TAG, "There was a problem getting the step count.", e);
                                }
                            });
        }catch (Exception e)
        {

        }
    }

    private void readTotlMins ()
    {
        try {
            Fitness.getHistoryClient(this, GoogleSignIn.getLastSignedInAccount(this))
                    .readDailyTotal(DataType.AGGREGATE_STEP_COUNT_DELTA)
                    .addOnSuccessListener(
                            new OnSuccessListener<DataSet>() {
                                @Override
                                public void onSuccess(DataSet dataSet) {


                                    Calendar cal = Calendar.getInstance();
                                    Date now = new Date();
                                    cal.setTime(now);
                                    long endTime = cal.getTimeInMillis();
                                    cal.set(Calendar.HOUR_OF_DAY, 0);
                                    cal.set(Calendar.MINUTE, 0);
                                    cal.set(Calendar.SECOND, 0);

                                    long startTime = cal.getTimeInMillis();

                                    java.text.DateFormat dateFormat = getDateInstance();
                                   //    Log.i(TAG, "Range StartT: " + (startTime));
                                   // Log.i(TAG, "Range EndT: " + (endTime));

                                  //  Log.i(TAG, "Range Start: " + dateFormat.format(startTime));
                                  //  Log.i(TAG, "Range End: " + dateFormat.format(endTime));

                                    System.out.println();
                                    long i = dataSet.isEmpty() ? 0 : dataSet.getDataPoints().get(0).getEndTime(TimeUnit.MINUTES);
                                    long s = dataSet.isEmpty() ? 0 : dataSet.getDataPoints().get(0).getStartTime(TimeUnit.MINUTES);
                                    active = i - s;

                                    System.out.println("starttime" + dataSet.getDataPoints().get(0).getStartTime(TimeUnit.MILLISECONDS));
                                    System.out.println("endtime" + dataSet.getDataPoints().get(0).getEndTime(TimeUnit.MILLISECONDS));
                                    System.out.println("checking active " + active);
                                    double totalTim = active / 60000;
                                    Math.round(totalTim);
                                    System.out.println("Total Time " + totalTim);
                                    String in = String.valueOf(active);
                                  //  tmins.setText(in + " mins");


                                }
                            })
                    .addOnFailureListener(
                            new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                   // Log.w(TAG, "There was a problem getting the step count.", e);
                                }
                            });
        }catch (Exception e)
        {

        }
    }
}
