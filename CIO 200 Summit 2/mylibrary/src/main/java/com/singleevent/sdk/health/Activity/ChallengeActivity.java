package com.singleevent.sdk.health.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptionsExtension;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.Bucket;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataSource;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.result.DataReadResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.singleevent.sdk.App;
import com.singleevent.sdk.Custom_View.Error_Dialog;
import com.singleevent.sdk.Custom_View.Util;
import com.singleevent.sdk.Custom_View.VolleyErrorLis;
import com.singleevent.sdk.health.Adapter.MyAdapter;



import com.singleevent.sdk.health.Fragment.CompleteFragment;
import com.singleevent.sdk.health.Fragment.LiveFragment;
import com.singleevent.sdk.health.Fragment.RunningFragment;
import com.singleevent.sdk.health.Fragment.UpcomingFragment;
import com.singleevent.sdk.R;
import com.singleevent.sdk.health.Model.GetChallengeList;
import com.singleevent.sdk.health.Model.Updatesteps;
import com.singleevent.sdk.model.AppDetails;
import com.singleevent.sdk.utils.DataBaseStorage;
import com.zipow.videobox.confapp.ChatMsgType;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.Interval;
import org.joda.time.Period;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.multidex.BuildConfig;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import io.paperdb.Paper;

import static com.singleevent.sdk.App.TAG;
import static com.singleevent.sdk.health.Fragment.ActivityFragment.dayStringFormat;
import static java.text.DateFormat.getDateInstance;
import static java.text.DateFormat.getTimeInstance;

public class ChallengeActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    LinearLayout linearActive,linearUpcoming,linearCompleted,linearActiveRecycler;
    Button  btncreate_join,btn_history_activity;

    FrameLayout container;
    FloatingActionButton floatingActionButton;
    TabLayout tabLayout;

    ViewPager viewPager;
    private static final int REQUEST_OAUTH_REQUEST_CODE = 0x1001;
    FitnessOptions fitnessOptions;
    RelativeLayout r1,r2;
    ProgressBar progressBar3;
    ArrayList<GetChallengeList> challengeLists =new ArrayList<>();
    HashMap<Integer,Drawable> drawables =new HashMap<>();
    //
    private ArrayList<String> mImageNames = new ArrayList<>();
    private ArrayList<String> mImages = new ArrayList<>();
    private ArrayList<String> mTotalsteps = new ArrayList<>();
    private ArrayList<String> mPosition = new ArrayList<>();
    boolean googlesignin;
    Button button6;
    ImageView sync;
    TextView textsync;
    ArrayList<Updatesteps> updatesteps=new ArrayList<>();
    private ArrayList<String> Hmstep ;
    private ArrayList<String> HmMiles ;
    private ArrayList<String> Hmmins ;
    AppDetails appDetails;
    String daily_steps,daily_mins,daily_dist;
    TextView points,points1;
    Drawable image;
    String update;
    List<String > list;
    String[] values;

    static int currentIndex = 0;
    static ArrayList<String> arr = new ArrayList<>();
    static ArrayList<Long> startweekmil = new ArrayList<>();
    static ArrayList<Long> endweekmil = new ArrayList<>();
    static HashMap<Integer,Long> startmonthmil = new HashMap<>();
    static HashMap<Integer,Long> endmonthmil = new HashMap<>();
    static HashMap<Integer,Long> startyearmil = new HashMap<>();
    static HashMap<Integer,Long> endyearmil = new HashMap<>();
    private  ArrayList<String> mDate ;
    private  ArrayList<String> mSteps ;
    private  ArrayList<String> mMins ;
    private  ArrayList<String> mDist ;
    private  ArrayList<String> mStepsmonth ;
    private  ArrayList<String> mStepsyesr ;
    private  ArrayList<String> weekday ;
    ZonedDateTime now;
    int joinedchallenge;
    String steps,distance,time ;
    String str;







    TextView txtactive,txtupcoming,txtcompleted;
    RecyclerView activeRecylerView;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge);
       googlesignin=Paper.book().read("GoogleLogin",false);
       if(!googlesignin) {
           GoogleSignin();
       }


        btn_history_activity = findViewById(R.id.btn_history);
        r1=(RelativeLayout)findViewById(R.id.r1);
        r2=(RelativeLayout)findViewById(R.id.r2);
        button6=(Button)findViewById(R.id.button6);
        points=(TextView)findViewById(R.id.points);
        points1=(TextView)findViewById(R.id.points1);
        btncreate_join = findViewById(R.id.joinbtn_create);
        progressBar3=(ProgressBar)findViewById(R.id.progressBar3);
        sync=(ImageView)findViewById(R.id.sync);
        textsync=(TextView) findViewById(R.id.textsync);
        try {
            weekbby();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        r1.setBackground(Util.setrounded(Color.parseColor("#ff8c80f8")));
        r2.setBackground(Util.setrounded(Color.parseColor("#ff8c80f8")));
        appDetails = Paper.book().read("Appdetails");
        Hmstep=new ArrayList<>();
        Hmstep= Paper.book().read("Weekly_Steps",null);
        ///
        HmMiles=Paper.book().read("Weekly_Miles",null);

        Hmmins=Paper.book().read("Weekly_Mins",null);


       // joinedchallenge=Paper.book().read("JoinedChallenge",0);

        System.out.println(Hmstep);
        if(Hmstep!=null){

            try {
                int p = getday(LocalDate.now().getDayOfWeek().name());
                daily_steps = Hmstep.get(p);
                points.setText(daily_steps);
            }catch (Exception e){}
        }
        else{
            daily_steps="0";
            points.setText(daily_steps);
        }


        mStepsyesr=new ArrayList<>();
        mStepsmonth= new ArrayList<>();
        mSteps=new ArrayList<>();
        mMins=new ArrayList<>();
        mDist=new ArrayList<>();
        mDate=new ArrayList<>();
        weekday= new ArrayList<>();



        /*floatingActionButton = (FloatingActionButton)findViewById(R.id.addFab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ChallengeActivity.this,"Features coming soon",Toast.LENGTH_SHORT).show();
            }
        });*/


        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
       // bottomNav.setSelectedItemId(R.id.nav_challenge);


        sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UpdateSteps(0);

                /*try {
                    weekbby();
                } catch (ParseException e) {
                    textsync.setText("Last synced 3 minute ago");
                    progressBar3.setVisibility(View.GONE);
                    progressBar3.setEnabled(true);
                    sync.setVisibility(View.VISIBLE);
                    e.printStackTrace();
                }*/
               /* Intent in = new Intent(getApplicationContext(),ActivityHistory.class);
                startActivity(in);*/
            }
        });

        btncreate_join.setBackground(Util.setrounded(Color.parseColor("#ff8c80f8")));
        getChallenge();

        btncreate_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getApplicationContext(),JoinChallenge.class);
                startActivity(in);

                //finish();
               /* Intent in = new Intent(getApplicationContext(),Challengemode.class);
                startActivity(in);*/
            }
        });
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent in = new Intent(getApplicationContext(),HealthFeature.class);
                startActivity(in);*/
               /* Intent i=new Intent(getApplicationContext(),ActivityHistory.class);
                startActivity(i);*/
             //  Toast.makeText("")

            }
        });


        btn_history_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in  = new Intent(getApplicationContext(), MainChallengeView.class);
                startActivity(in);
                finish();
            }
        });




        Menu menu = bottomNav.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);


        //getSupportActionBar().hide();



/*

        FragmentManager fm = getSupportFragmentManager();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        RunningFragment mActivityFragment = new RunningFragment();
        fragmentTransaction.replace(R.id.frameLayout_horizontalrecyclerview, mActivityFragment, "");
        fragmentTransaction.commit();

*/




        //tab layout


        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);



        /*Calendar timeOfDay = Calendar.getInstance();
        timeOfDay.set(Calendar.HOUR_OF_DAY, 1);
        timeOfDay.set(Calendar.MINUTE, 58);
        timeOfDay.set(Calendar.SECOND, 0);*/
        if(appDetails.getTimezone()!=null){
           String zone= checkTimeZone(appDetails.getTimezone());
             now = ZonedDateTime.now(ZoneId.of(zone));
        }
        else{
            now = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
        }

        ZonedDateTime nextRun = now.withHour(11).withMinute(58).withSecond(0);
        if(now.compareTo(nextRun) > 0)
            nextRun = nextRun.plusDays(1);

        Duration duration = Duration.between(now, nextRun);
        long initalDelay = duration.getSeconds();

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(mDelayedShow,
                initalDelay,
                TimeUnit.DAYS.toSeconds(1),
                TimeUnit.SECONDS);

/*        new DailyRunnerDaemon1(timeOfDay, new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    // call whatever your daily task is here
                    UpdateSteps();
                }
                catch(Exception e)
                {
                    Toast.makeText(getApplicationContext(),"An error occurred performing daily housekeeping",Toast.LENGTH_LONG).show();
                }
            }
        }, "daily-housekeeping");*/
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void updatepoint(){
        Hmstep= Paper.book().read("Weekly_Steps",null);
        if(Hmstep!=null){

            try {
                int p = getday(LocalDate.now().getDayOfWeek().name());
                daily_steps = Hmstep.get(p);
                points.setText(daily_steps);
            }catch (Exception e){}
        }
    }

    public String checkTimeZone(String s)
    {
        String val="";
        Map<String ,String> hm=new HashMap<String,String>();
        hm.put("ADT" ,"America/Halifax");
        hm.put("AKDT", "America/Juneau");
        hm.put("AKST", "America/Juneau");
        hm.put("ART", "America/Argentina/Buenos_Aires");
        hm.put("AST", "America/Halifax");
        hm.put("BDT", "Asia/Dhaka");
        hm.put("BRST", "America/Sao_Paulo");
        hm.put("BRT", "America/Sao_Paulo");
        hm.put("BST", "Europe/London");
        hm.put("CAT", "Africa/Harare");
        hm.put("CDT", "America/Chicago");
        hm.put("CEST", "Europe/Paris");
        hm.put("CET", "Europe/Paris");
        hm.put("CLST", "America/Santiago");
        hm.put("CLT", "America/Santiago");
        hm.put("COT", "America/Bogota");
        hm.put("CST", "America/Chicago");
        hm.put("EAT", "Africa/Addis_Ababa");
        hm.put("EDT", "America/New_York");
        hm.put("EEST", "Europe/Istanbul");
        hm.put("EET", "Europe/Istanbul");
        hm.put("EST", "America/New_York");
        hm.put("GMT", "GMT");
        hm.put("GST", "Asia/Dubai");
        hm.put("HKT", "Asia/Hong_Kong");
        hm.put("HST", "Pacific/Honolulu");
        hm.put("ICT", "Asia/Bangkok");
        hm.put("IRST", "Asia/Tehran");
        hm.put("IST", "Asia/Calcutta");
        hm.put("JST", "Asia/Tokyo");
        hm.put("KST", "Asia/Seoul");
        hm.put("MDT", "America/Denver");
        hm.put("MSD", "Europe/Moscow");
        hm.put("MSK", "Europe/Moscow");
        hm.put("MST", "America/Denver");
        hm.put("NZDT", "Pacific/Auckland");
        hm.put("NZST", "Pacific/Auckland");
        hm.put("PDT", "America/Los_Angeles");
        hm.put("PET", "America/Lima");
        hm.put("PHT", "Asia/Manila");
        hm.put("PKT", "Asia/Karachi");
        hm.put("PST", "America/Los_Angeles");
        hm.put("SGT", "Asia/Singapore");
        hm.put("UTC", "UTC");
        hm.put("WAT", "Africa/Lagos");
        hm.put("WEST", "Europe/Lisbon");
        hm.put("WET", "Europe/Lisbon");
        hm.put("WIT", "Asia/Jakarta");

        if (hm.containsKey(s)) {
            val = (String)hm.get(s);


        }
        return val;
    }


    public  Runnable mDelayedShow = new Runnable() {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void run() {
                    //getting all the users for api call
                 //   UpdateSteps(1);

                }
            });
          // sync.setVisibility(View.GONE);
         //  UpdateSteps(1);
        }
    };


    public void getChallenge() {

        String tag_string_req = "Checking Update";
        String url ="https://api2.webmobi.com/health/getallchallenges?app_id="+appDetails.getAppId()+"&user_id="+Paper.book().read("userId", "");
        System.out.println(url);
        final ProgressDialog pDialog = new ProgressDialog(ChallengeActivity.this, R.style.MyAlertDialogStyle);
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
                        JSONArray jsonArray = response.getJSONArray("challenges");
                        challengeLists.clear();

                        for(int i=0; i<jsonArray.length(); i++)
                        {
                            String temp = jsonArray.getJSONObject(i).toString();
                            GetChallengeList obj = gson.fromJson(temp, GetChallengeList.class);
                          //  if(obj.getViewChallenge().equalsIgnoreCase("public")) {

                                challengeLists.add(obj);
                            if(challengeLists.get(i).getJoinedChallenge()!=null) {
                                values = challengeLists.get(i).getJoinedChallenge().split(",");
                                list = new ArrayList<String>(Arrays.asList(values));
                                if (list.contains(Paper.book().read("userId", ""))) {
                                    joinedchallenge++;
                                }
                            }

                                 /*if(obj.getImageUrl()!=null&& obj.getImageUrl().startsWith("https://")){
                                     drawables.put(i,getBitmapFromURL(obj.getImageUrl()));
                                 }*/
                           // }


                        }

                        points1.setText(String.valueOf(joinedchallenge)+ " Joined");
                        Paper.book().write("challengelist",challengeLists);
                     //   Paper.book().write("Challengeimage",drawables);
                        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#000000"));
                        MyAdapter tabadapter = new MyAdapter(getSupportFragmentManager());


                        tabadapter.AddFragment(new RunningFragment()," Live");
                        tabadapter.AddFragment(new UpcomingFragment()," Upcoming");
                        tabadapter.AddFragment(new CompleteFragment()," Completed");

                        viewPager.setAdapter(tabadapter);
                        tabLayout.setupWithViewPager(viewPager);
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
                    Error_Dialog.show("Timeout", ChallengeActivity.this);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, ChallengeActivity.this), ChallengeActivity.this);
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show("Please Check Your Internet Connection", ChallengeActivity.this);
                }

            }

        }){

        };


        App.getInstance().addToRequestQueue(jsonRequest, tag_string_req);
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
    }

    public  Drawable getBitmapFromURL(String urls) {
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                URL url = null;
                try {
                    url = new URL(urls);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                Bitmap bitmap = null;
                try {
                    bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                image  = new BitmapDrawable(getApplicationContext().getResources(), bitmap);

            }
        });
        thread.start();
        return image;
    }
    public void GoogleSignin()
    {
     //  googlesignin=true;
       Paper.book().write("GoogleLogin",true);
        fitnessOptions =
                FitnessOptions.builder()
                        .addDataType(DataType.TYPE_STEP_COUNT_CUMULATIVE)
                        .addDataType(DataType.TYPE_STEP_COUNT_DELTA)
                        .addDataType(DataType.TYPE_CALORIES_EXPENDED)
                     //   .addDataType(DataType.TYPE_DISTANCE_CUMULATIVE)
                        .addDataType(DataType.TYPE_DISTANCE_DELTA,FitnessOptions.ACCESS_READ)
                        //.addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_WRITE)
                      //  .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_WRITE)
                        .addDataType(DataType.TYPE_MOVE_MINUTES)
                        .addDataType(DataType.TYPE_WEIGHT)
                        .build();
        if (!GoogleSignIn.hasPermissions(GoogleSignIn.getLastSignedInAccount(this), fitnessOptions)) {
            GoogleSignIn.requestPermissions(
                    this,
                    REQUEST_OAUTH_REQUEST_CODE,
                    GoogleSignIn.getLastSignedInAccount(this),
                    fitnessOptions);
        } else {
            subscribe();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_OAUTH_REQUEST_CODE) {
                subscribe();
            }
        }
    }
    public void subscribe() {
        // To create a subscription, invoke the Recording API. As soon as the subscription is
        // active, fitness data will start recording.
        Fitness.getRecordingClient(this, GoogleSignIn.getLastSignedInAccount(this))
                .subscribe(DataType.TYPE_ACTIVITY_SEGMENT)
                .addOnCompleteListener(
                        new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.i(TAG, "Successfully subscribed!");
                                    Toast.makeText(getApplicationContext(),"Successfully subscribed!",Toast.LENGTH_SHORT).show();

//                                    for (Subscription sc : subscriptions) {
//                                        DataType dt = sc.getDataType();
//                                        Log.i(TAG, "Active subscription for data type: " + dt.getName());
//                                    }
                                } else {
                                    Log.w(TAG, "There was a problem subscribing.", task.getException());
                                    Toast.makeText(getApplicationContext(),"There was a problem subscribing.",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
    }


    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {


                        if(item.getItemId()== R.id.nav_home) {
                        Intent intent = new Intent(getApplicationContext(), ActivityHistory.class);
                        startActivity(intent);
                        finish();
                    }
                         if(item.getItemId()== R.id.nav_friends) {
                            Intent in = new Intent(getApplicationContext(), LeaderBoardActivity.class);
                            startActivity(in);
                            finish();
                        }

                        else if(item.getItemId()== R.id.nav_challenge) {
                        }


//                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                            selectedFragment).commit();
                    return false;
                }
            };
    @Override
    protected void onResume() {
        super.onResume();
        SpannableString s = new SpannableString("Health");
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void UpdateSteps(int n) {
        Hmstep= Paper.book().read("Weekly_Steps",null);
        ///
        HmMiles=Paper.book().read("Weekly_Miles",null);

        Hmmins=Paper.book().read("Weekly_Mins",null);

        if(n==0){


             progressBar3.setVisibility(View.VISIBLE);
             progressBar3.setEnabled(false);
         }
        Date date = new Date();
        //This method returns the time in millis
        long timeMilli = date.getTime();
        update=gettime(timeMilli);
        System.out.println("DATE IS "+update);

        String tag_string_req = "Profile";
        String url ="https://api2.webmobi.com/health/updatesteps";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(n==0) {
                    textsync.setText("Last synced 3 minute ago");
                    progressBar3.setVisibility(View.GONE);
                    progressBar3.setEnabled(true);
                    sync.setVisibility(View.VISIBLE);
                }

                try {
                    System.out.println(response);
                    JSONObject jObj = new JSONObject(response);


                    if (jObj.getBoolean("response")) {
                        Toast.makeText(ChallengeActivity.this,jObj.getString("responseString"),Toast.LENGTH_SHORT).show();
                  //     System.out.println(response.toString());
                        Gson gson = new Gson();
                        try {
                            JSONObject object = jObj.getJSONObject("data");
                            updatesteps.clear();
                            Updatesteps user = gson.fromJson(object.toString(), Updatesteps.class);
                            updatesteps.add(user);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //setImages(jObj.getString("responseString"));
                    } else {
                        if (jObj.getString("responseString").equalsIgnoreCase("Invalid token")) {
                            Error_Dialog.show("Session Expired, Please Login ", ChallengeActivity.this);
                        } else
                            Error_Dialog.show(jObj.getString("responseString"), ChallengeActivity.this);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if(n==0) {
                    textsync.setText("Last synced 3 minute ago");
                    progressBar3.setVisibility(View.GONE);
                    progressBar3.setEnabled(true);
                    sync.setVisibility(View.VISIBLE);
                }

                if (error instanceof TimeoutError) {
                    Error_Dialog.show("Timeout", ChallengeActivity.this);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, ChallengeActivity.this), ChallengeActivity.this);
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show("Please Check Your Internet Connection", ChallengeActivity.this);
                }
            }
        }) {


            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                if(Hmstep!=null){

                    int p=  getday(LocalDate.now().getDayOfWeek().name());
                    steps=Hmstep.get(p);}
                else{
                     steps="0";
                }
                if(HmMiles!=null){

                    int p=  getday(LocalDate.now().getDayOfWeek().name());
                    distance=HmMiles.get(p);
                }
                else{
                    distance="0";
                }
                if(Hmmins!=null){

                    int p=  getday(LocalDate.now().getDayOfWeek().name());
                    time=Hmmins.get(p);
                }
                else{
                    time="0";
                }

                params.put("user_id",Paper.book().read("userId", ""));
                params.put("app_id", appDetails.getAppId());
                params.put("date", update);
                params.put("steps", steps);
                params.put("time",time);
                params.put("distance",distance);
                System.out.println("Update param"+params);
                return params;
            }

          /*@Override
          public Map<String, String> getHeaders() throws AuthFailureError {
              Map<String, String> headers = new HashMap<String, String>();
              headers.put("token", Paper.book().read("token", ""));
              return headers;
          }*/
        };


        App.getInstance().addToRequestQueue(strReq, tag_string_req);
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
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
    public String gettime(long mill){
        String dateString;

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(mill);
        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
        String days = myFormat.format(cal.getTime());
        System.out.println("DAY's IS "+days);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        String mo=getmonth(month);
        String temp=mo+" "+day;
        return days;
    }
    public String getmonth(int n){
        ArrayList<String> monthArray=new ArrayList<>();
        monthArray.add("Jan");
        monthArray.add("Feb");
        monthArray.add("Mar");
        monthArray.add("Apr");
        monthArray.add("May");
        monthArray.add("Jun");
        monthArray.add("Jul");
        monthArray.add("Aug");
        monthArray.add("Sep");
        monthArray.add("Oct");
        monthArray.add("Nov");
        monthArray.add("Dec");
        return monthArray.get(n);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Task<DataReadResponse> readHistoryData(String type, int index) throws ParseException,NullPointerException {
        // Begin by creating the query.
        String temp=type;
        DataReadRequest readRequest = queryFitnessData(type,index);

        // Invoke the History API to fetch the data with the query
        return Fitness.getHistoryClient(ChallengeActivity.this, GoogleSignIn.getLastSignedInAccount(this))
                .readData(readRequest)
                .addOnSuccessListener(
                        new OnSuccessListener<DataReadResponse>() {
                            @Override
                            public void onSuccess(DataReadResponse dataReadResponse) {
                                // For the sake of the sample, we'll print the data so we can see what we just
                                // added. In general, logging fitness information should be avoided for privacy
                                // reasons.
                                printData(dataReadResponse,type);
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                textsync.setText("Last synced 3 minute ago");
                                progressBar3.setVisibility(View.GONE);
                                progressBar3.setEnabled(true);
                                sync.setVisibility(View.VISIBLE);
                                Log.e(TAG, "There was a problem reading the data.", e);
                            }
                        });

    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public  DataReadRequest queryFitnessData(String h,int index) throws ParseException {
        // [START build_read_data_request]
        // Setting a start and end date using a range of 1 week before this moment.
        long startTime=0l;
        long endTime=0l;
        Calendar cal = Calendar.getInstance();
        Date now = new Date();
        cal.setTime(now);
        cal.setTimeInMillis(0);
        cal.set(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DATE), 23, 59, 59);
        cal.getTime();
        if(index==0) {
            endTime = cal.getTimeInMillis();
        }

        cal.set(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DATE));
//        Log.i(TAG,"EndDate"+cal.getTime());
//        cal.getTime();
        LocalDate s= LocalDate.now().with ( ChronoField.DAY_OF_MONTH , 1 );


        if (h.equalsIgnoreCase("week")) {
            cal.add(Calendar.WEEK_OF_YEAR, -1);
            if(index==0) {
                endTime = cal.getTimeInMillis();
                startTime = cal.getTimeInMillis();
            }
            else{
                startTime=startweekmil.get(index);
                endTime=endweekmil.get(index);
            }

            //  startTime = cal.getTimeInMillis();
        }//1596479400000l
        else if (h.equalsIgnoreCase("month")) {

            if(index==0) {
                cal.add(Calendar.MONTH, -1);
                /// endTime = cal.getTimeInMillis();
                startTime= datetomill(s);
            }
            else{
                // cal.add(Calendar.MONTH, -(month-index));
                endTime=endmonthmil.get(index);
                startTime=startmonthmil.get(index);
            }
            //startTime= datetomill(s);
            // startTime = cal.getTimeInMillis();
        } else if (h.equalsIgnoreCase("year")){

            if(index==0) {
                Date d = new Date();
                DateFormat dateFormat = new SimpleDateFormat("yyyy");
                String format = dateFormat.format(d);
                // System.out.println("Current date and time = " + format);
                //System.out.printf("Four-digit Year = %TY\n",d);
                int year = Integer.parseInt(format);
                Calendar calendarStart = Calendar.getInstance();
                calendarStart.set(Calendar.YEAR, year);
                calendarStart.set(Calendar.MONTH, 0);
                calendarStart.set(Calendar.DAY_OF_MONTH, 1);
                // returning the first date


                Calendar calendarEnd = Calendar.getInstance();
                calendarEnd.set(Calendar.YEAR, year);
                calendarEnd.set(Calendar.MONTH, 11);
                calendarEnd.set(Calendar.DAY_OF_MONTH, 31);
                startTime = calendarStart.getTimeInMillis();
                endTime = cal.getTimeInMillis();

                // returning the last date
                Date endDate = calendarEnd.getTime();

                cal.add(Calendar.YEAR, -1);
            }
            else{
                endTime=endyearmil.get(index);
                startTime=startyearmil.get(index);

            }
            //  startTime = cal.getTimeInMillis();
        }


//        ArrayList<Long> dates = new ArrayList<>();
//        dates.add(startTime);

        java.text.DateFormat dateFormat = getDateInstance(DateFormat.FULL);

//        Log.i(TAG, "Range : " + dateFormat.format(dates));
//        Log.i(TAG, "Size : " + dates.size());
        Log.i(TAG, "Range Start: " + dateFormat.format(startTime));
        Log.i(TAG, "Range End: " + dateFormat.format(endTime));

        DataReadRequest readRequest =
                new DataReadRequest.Builder()
                        // The data request can specify multiple data types to return, effectively
                        // combining multiple data queries into one call.
                        // In this example, it's very unlikely that the request is for several hundred
                        // datapoints each consisting of a few steps and a timestamp.  The more likely
                        // scenario is wanting to see how many steps were walked per day, for 7 days.
                        .aggregate(DataType.TYPE_STEP_COUNT_DELTA, DataType.AGGREGATE_STEP_COUNT_DELTA)
                        // Analogous to a "Group By" in SQL, defines how data should be aggregated.
                        // bucketByTime allows for a time span, whereas bucketBySession would allow
                        // bucketing by "sessions", which would need to be defined in code.
                        .bucketByTime(1, TimeUnit.DAYS)
                        .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)//1596220200000//1597084200000
                        .build();
        // [END build_read_data_request]

        return readRequest;
    }
    public  Long datetomill(LocalDate s) throws ParseException {
        String myDate = s.toString();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse(myDate);
        long start = date.getTime();
        return start;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public  void printData(DataReadResponse dataReadResult, String type) {
        // [START parse_read_data_result]
        // If the DataReadRequest object specified aggregated data, dataReadResult will be returned
        // as buckets containing DataSets, instead of just DataSets.
        mSteps.clear();

        if (dataReadResult.getBuckets().size() > 0) {
            //  Log.i(TAG, "Number of returned buckets of DataSets is: " + dataReadResult.getBuckets().size());
            for (Bucket bucket : dataReadResult.getBuckets()) {
            //    System.out.println("BUCKET FULL "+dataReadResult);
                List<DataSet> dataSets = bucket.getDataSets();
                for (DataSet dataSet : dataSets) {
                 //   System.out.println("DATASET"+dataSet);
//                    Log.i(TAG, "Data point:: size:"+dataSets.get(0));
//                    Log.i(TAG, "Size:: " + dataReadResult.getBuckets().get(2));


                    if (dataSet.getDataPoints().isEmpty()) {

                        int checZero = 0;
                        long startTime = bucket.getEndTime(TimeUnit.MILLISECONDS);
                        long endTime = bucket.getEndTime(TimeUnit.MILLISECONDS);
                        DataSource dataSource = new DataSource.Builder()
                                .setAppPackageName(BuildConfig.APPLICATION_ID)
                                .setDataType(DataType.TYPE_STEP_COUNT_DELTA)
                                .setStreamName("move mins")
                                .setType(DataSource.TYPE_RAW)
                                .build();
                        dataSet = DataSet.create(dataSource);
                        // For each data point, specify a start time, end time, and the data value -- in this case,
                        // the number of new steps.
                        DataPoint dataPoint =
                                dataSet.createDataPoint()
                                        .setTimeInterval(startTime, endTime, TimeUnit.MILLISECONDS);
                        dataPoint.getValue(Field.FIELD_STEPS).setInt(checZero);
                        dataSet.add(dataPoint);


                    }
                    dumpDataSet(dataSet,type);

                }

            }

        } else if (dataReadResult.getDataSets().size() > 0) {
            Log.i(TAG, "Number of returned DataSets is: " + dataReadResult.getDataSets().size());
            for (DataSet dataSet : dataReadResult.getDataSets()) {
                dumpDataSet(dataSet,type);
            }
        }
        textsync.setText("Last synced 3 minute ago");
        System.out.println("Total steps"+mSteps.size());
        progressBar3.setVisibility(View.GONE);
        progressBar3.setEnabled(true);
        sync.setVisibility(View.VISIBLE);
       try {
           if (mSteps != null && mSteps.size() > 0) {
               updatepoint();
           }
       }catch (Exception e){

       }
        // [END parse_read_data_result]
    }

    // [START parse_dataset]
    @RequiresApi(api = Build.VERSION_CODES.O)
    private  void dumpDataSet(DataSet dataSet, String type) {
        Log.i(TAG, "Data returned for Data type: " + dataSet.getDataType().getName());
        DateFormat dateFormat = getTimeInstance();


        java.text.DateFormat dateFormat3 = getDateInstance(DateFormat.FULL);
        for (DataPoint dp : dataSet.getDataPoints()) {

            //  Log.i(TAG, "Data point:");
            //  Log.i(TAG, "\tType: " + dp.getDataType().getName());
            //Log.i(TAG, "\tStart: Date: "+"==" +dateFormat3.format(dp.getStartTime(TimeUnit.MILLISECONDS)));
//            mDate.add(dateFormat3.format(dp.getEndTime(TimeUnit.MILLISECONDS)));
            mDate.add(dayStringFormat(dp.getEndTime(TimeUnit.MILLISECONDS)));
            //  Log.i(TAG, "\tEnd: Date: "+"==" + dateFormat3.format(dp.getEndTime(TimeUnit.MILLISECONDS)));
            for (Field field : dp.getDataType().getFields()) {
                //     Log.i(TAG, "\tField: " + field.getName() + " Value: " + dp.getValue(field));
             //   System.out.println("Checking steps ::"+dp.getValue(field));
                try {
                    if (type.equalsIgnoreCase("week")) {
                        mSteps.add(dp.getValue(field).toString());
                        System.out.println("Daily Steps"+dp.getValue(field).toString());
                        GregorianCalendar cal = new GregorianCalendar();

                        cal.setTime(new Date(dp.getStartTime(TimeUnit.MICROSECONDS)));

                        int dow = cal.get(Calendar.DAY_OF_WEEK);
                       // System.out.println("Mill to Dyas="+dow);
                    }
                    else if (type.equalsIgnoreCase("month")) {
                        mStepsmonth.add(dp.getValue(field).toString());
                    } else if (type.equalsIgnoreCase("year")) {
                        mStepsyesr.add(dp.getValue(field).toString());
                    }
                }catch (Exception e)
                {

                }
            }

        }
        if(mSteps!=null) {
            Paper.book().write("Weekly_Steps", mSteps);
        }
        if(mStepsmonth!=null) {
            Paper.book().write("Month_data", mStepsmonth);
        }
        if(mStepsyesr!=null) {
            Paper.book().write("Year_data", mStepsyesr);
        }
       // initUI();
//
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void weekbby() throws ParseException {
       // textsync.setText("Syncing");
       // progressBar3.setVisibility(View.VISIBLE);
      //  progressBar3.setEnabled(false);
      //  sync.setVisibility(View.GONE);
        SimpleDateFormat df = new SimpleDateFormat(" dd.MMM.yyyy");
        Period weekPeriod = new Period().withWeeks(1);
        DateTime startDate = new DateTime(2021, 1, 1, 0, 0, 0, 0);
        System.out.println("Check startDate:" + startDate);
        while (startDate.getDayOfWeek() != DateTimeConstants.MONDAY) {
            startDate = startDate.plusDays(1);
        }
        DateTime endDate = new DateTime(2022, 1, 1, 0, 0, 0, 0);
        while (endDate.getDayOfWeek() != DateTimeConstants.SUNDAY) {
            endDate = endDate.plusDays(1);
        }
        Interval i = new Interval(startDate, weekPeriod);
        int index = 0;
        while (i.getStart().isBefore(endDate)) {
            long currentDateInMilli = new DateTime().getMillis();
            System.out.println("checking for the current" + i.getStart().getMillis());
            if (i.getStart().getMillis() <= currentDateInMilli && currentDateInMilli <= i.getEnd().getMillis()) {
                currentIndex = index;
              //  Paper.book().write("Current_Index",index);
//                System.out.println("startdate=: "+i.getStart().getMillis() +" current date :"
//                        + new DateTime(currentDateInMilli)  +" end date: "+i.getEnd().getMillis());
            }
            System.out.println("week : " + i.getStart().getWeekOfWeekyear()
                    + " start: " + df.format(i.getStart().toDate())
                    + " ending: " + df.format(i.getEnd().minusMillis(1).toDate()));
            arr.add(df.format(i.getStart().toDate())
                    + " -- " + df.format(i.getEnd().minusMillis(1).toDate()));
            Paper.book(df.format(i.getStart().toDate())).write("startWeekwisedata",i.getStart().getMillis());
            Paper.book(df.format(i.getEnd().toDate())).write("endWeekwisedata",i.getEnd().getMillis());
            startweekmil.add((i.getStart().getMillis()));
            endweekmil.add((i.getEnd().getMillis()));
            Paper.book(String.valueOf(i.getStart().getWeekOfWeekyear())).write("startweekbyyear",i.getStart().getMillis());
            Paper.book(String.valueOf(i.getStart().getWeekOfWeekyear())).write("endweekbyyear",i.getEnd().getMillis());

            i = new Interval(i.getStart().plus(weekPeriod), weekPeriod);

            index++;
        }
      //  Paper.book().write("startweekname",startweekmil);
    //    Paper.book().write("endweekname",endweekmil);
      //  Paper.book().write("Week_array",arr);
      //  System.out.println("Array List==" + arr.get(currentIndex));
        String dim = arr.get(currentIndex);
        try {

            readHistoryData("week", currentIndex);
            readActiveHistoryData("week", currentIndex);
            readDistanceHistoryData("week", currentIndex);
        }catch (Exception e){
           /* textsync.setText("Last synced 3 minute ago");
            progressBar3.setVisibility(View.GONE);
            progressBar3.setEnabled(true);
            sync.setVisibility(View.VISIBLE);*/
        }
      //  System.out.println("Checkng dim Current index"+dim);
       // txtWeek.setText(dim);
    }

    /////Daily Task
    public static class DailyRunnerDaemon1
    {
        private final Runnable dailyTask;
        private final int hour;
        private final int minute;
        private final int second;
        private String runThreadName = "daily step";

        public DailyRunnerDaemon1(Calendar timeOfDay, Runnable dailyTask, String runThreadName)
        {
            this.dailyTask = dailyTask;
            this.hour = timeOfDay.get(Calendar.HOUR_OF_DAY);
            this.minute = timeOfDay.get(Calendar.MINUTE);
            this.second = timeOfDay.get(Calendar.SECOND);
            this.runThreadName = runThreadName;
        }

        public void start()
        {
            startTimer();
        }

        private void startTimer() {

        }

        {
            new Timer(runThreadName, true).schedule(new TimerTask()
            {
                @Override
                public void run()
                {
                    dailyTask.run();
                    startTimer();
                }
            }, getNextRunTime());
        }


        private Date getNextRunTime()
        {
            Calendar startTime = Calendar.getInstance();
            Calendar now = Calendar.getInstance();
            startTime.set(Calendar.HOUR_OF_DAY, hour);
            startTime.set(Calendar.MINUTE, minute);
            startTime.set(Calendar.SECOND, second);
            startTime.set(Calendar.MILLISECOND, 0);

            if(startTime.before(now) || startTime.equals(now))
            {
                startTime.add(Calendar.DATE, 1);
            }

            return startTime.getTime();
        }
    }
    //////////////Active mins code

    @RequiresApi(api = Build.VERSION_CODES.O)
    private Task<DataReadResponse> readActiveHistoryData(String type,int index) throws ParseException {
        // Begin by creating the query.
        DataReadRequest readRequest = ActivequeryFitnessData(type,index);
///
        GoogleSignInOptionsExtension fitnessOptions =
                FitnessOptions.builder()
                        .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                        .build();

        GoogleSignInAccount googleSignInAccount =
                GoogleSignIn.getAccountForExtension(this, fitnessOptions);
        // Invoke the History API to fetch the data with the query
        return Fitness.getHistoryClient(this, googleSignInAccount)
                .readData(readRequest)
                .addOnSuccessListener(
                        new OnSuccessListener<DataReadResponse>() {
                            @Override
                            public void onSuccess(DataReadResponse dataReadResponse) {
                                // For the sake of the sample, we'll print the data so we can see what we just
                                // added. In general, logging fitness information should be avoided for privacy
                                // reasons.
                                ActiveprintData(dataReadResponse,type);
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e(TAG, "There was a problem reading the data.", e);
                            }
                        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public  void ActiveprintData(DataReadResponse dataReadResult, String type) {
        // [START parse_read_data_result]
        // If the DataReadRequest object specified aggregated data, dataReadResult will be returned
        // as buckets containing DataSets, instead of just DataSets.
        mMins.clear();
        int checZero = 0;
        if (dataReadResult.getBuckets().size() > 0) {
            Log.i(TAG, "Number of returned buckets of DataSets is: " + dataReadResult.getBuckets().size());
            for (Bucket bucket : dataReadResult.getBuckets()) {

                List<DataSet> dataSets = bucket.getDataSets();


                for (DataSet dataSet : dataSets) {


                    if (dataSet.getDataPoints().isEmpty()) {


                        long startTime = bucket.getEndTime(TimeUnit.MILLISECONDS);
                        long endTime = bucket.getEndTime(TimeUnit.MILLISECONDS);
                        DataSource dataSource = new DataSource.Builder()
                                .setAppPackageName(BuildConfig.APPLICATION_ID)
                                .setDataType(DataType.TYPE_MOVE_MINUTES)
                                .setStreamName("move mins")
                                .setType(DataSource.TYPE_RAW)
                                .build();
                        dataSet = DataSet.create(dataSource);
                        // For each data point, specify a start time, end time, and the data value -- in this case,
                        // the number of new steps.
                        DataPoint dataPoint =
                                dataSet.createDataPoint()
                                        .setTimeInterval(startTime, endTime, TimeUnit.MILLISECONDS);
                        dataPoint.getValue(Field.FIELD_DURATION).setInt(checZero);
                        dataSet.add(dataPoint);


                    }

                    Log.i(TAG, "Get Data Points==Point::" + dataSet.getDataPoints());

                    ActivedumpDataSet(dataSet,type);

                }
            }






        } else if (dataReadResult.getDataSets().size() > 0) {

            for (DataSet dataSet : dataReadResult.getDataSets()) {
                ActivedumpDataSet(dataSet,type);
            }
        }
        // [END parse_read_data_result]
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public  DataReadRequest ActivequeryFitnessData(String h,int index) throws ParseException {
        // [START build_read_data_request]
        // Setting a start and end date using a range of 1 week before this moment.
        long startTime=0l;
        long endTime=0l;
        Calendar cal = Calendar.getInstance();
        Date now = new Date();
        cal.setTime(now);
        cal.setTimeInMillis(0);
        cal.set(Calendar.getInstance().get(Calendar.YEAR),Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DATE),23,59,59);
        cal.getTime();
        if(index==0) {
            endTime = cal.getTimeInMillis();
        }

        cal.set(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DATE));
//        Log.i(TAG,"EndDate"+cal.getTime());
//        cal.getTime();
        LocalDate s= LocalDate.now().with ( ChronoField.DAY_OF_MONTH , 1 );


        if (h.equalsIgnoreCase("week")) {
            cal.add(Calendar.WEEK_OF_YEAR, -1);
            if(index==0) {
                endTime = cal.getTimeInMillis();
                startTime = cal.getTimeInMillis();
            }
            else{
                startTime=startweekmil.get(index);
                endTime=endweekmil.get(index);
            }

            //  startTime = cal.getTimeInMillis();
        }//1596479400000l



        java.text.DateFormat dateFormat = getDateInstance(DateFormat.FULL);
        Log.i(TAG, "Range Start: " + dateFormat.format(startTime));
        Log.i(TAG, "Range End: " + dateFormat.format(endTime));

        DataReadRequest readRequest =
                new DataReadRequest.Builder()
                        // The data request can specify multiple data types to return, effectively
                        // combining multiple data queries into one call.
                        // In this example, it's very unlikely that the request is for several hundred
                        // datapoints each consisting of a few steps and a timestamp.  The more likely
                        // scenario is wanting to see how many steps were walked per day, for 7 days.
                        .aggregate(DataType.TYPE_MOVE_MINUTES, DataType.TYPE_MOVE_MINUTES)
                        // Analogous to a "Group By" in SQL, defines how data should be aggregated.
                        // bucketByTime allows for a time span, whereas bucketBySession would allow
                        // bucketing by "sessions", which would need to be defined in code.
                        .bucketByTime(1, TimeUnit.DAYS)
                        .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                        .enableServerQueries()
                        .build();
        // [END build_read_data_request]

        return readRequest;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private  void ActivedumpDataSet(DataSet dataSet,String type) {
        Log.i(TAG, "Data returned for Data type: " + dataSet.getDataType().getName());
        DateFormat dateFormat = getTimeInstance();

        java.text.DateFormat dateFormat3 = getDateInstance(DateFormat.FULL);
        for (DataPoint dp : dataSet.getDataPoints()) {

           // Log.i(TAG, "Data point:");
          //  Log.i(TAG, "\tType: " + dp.getDataType().getName());
           // Log.i(TAG, "\tStart: Date: "+"==" +dateFormat3.format(dp.getStartTime(TimeUnit.MILLISECONDS)));
           // Log.i(TAG, "\tEnd: Date: "+"==" + dateFormat3.format(dp.getEndTime(TimeUnit.MILLISECONDS)));
            //   mDate.add(dateFormat3.format(dp.getEndTime(TimeUnit.MILLISECONDS)));
            // mDate.add(dayStringFormat(dp.getEndTime(TimeUnit.MILLISECONDS)));
            for (Field field : dp.getDataType().getFields()) {
                Log.i(TAG, "\tField: " + field.getName() + " Value: " + dp.getValue(field));

                //   System.out.println("Checking size mMins ::"+mMins.size());
                // mMins.add(dp.getValue(field).toString());

                //Paper.book().write("Active_minutes",mMins);

                if(type.equalsIgnoreCase("week")){
                    mMins.add(dp.getValue(field).toString());
                    System.out.println("Daily MINUTE"+dp.getValue(field).toString());
                }

            }
            if(mMins!=null)
            {
                Paper.book().write("Weekly_Mins",mMins);
            }

        }

    }

    ////////////Active mins end

    /// Distance API
    @RequiresApi(api = Build.VERSION_CODES.O)
    private Task<DataReadResponse> readDistanceHistoryData(String type,int index) throws ParseException {
        // Begin by creating the query.
        DataReadRequest readRequest = DistancequeryFitnessData(type,index);

        // Invoke the History API to fetch the data with the query
        return Fitness.getHistoryClient(this, GoogleSignIn.getLastSignedInAccount(this))
                .readData(readRequest)
                .addOnSuccessListener(
                        new OnSuccessListener<DataReadResponse>() {
                            @Override
                            public void onSuccess(DataReadResponse dataReadResponse) {
                                // For the sake of the sample, we'll print the data so we can see what we just
                                // added. In general, logging fitness information should be avoided for privacy
                                // reasons.
                                DistanceprintData(dataReadResponse,type);
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e(TAG, "There was a problem reading the data.", e);
                            }
                        });
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public  DataReadRequest DistancequeryFitnessData(String h,int index) throws ParseException {
        // [START build_read_data_request]
        // Setting a start and end date using a range of 1 week before this moment.
        long startTime=0l;
        long endTime=0l;
        Calendar cal = Calendar.getInstance();
        Date now = new Date();
        cal.setTime(now);
        cal.setTimeInMillis(0);
        cal.set(Calendar.getInstance().get(Calendar.YEAR),Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DATE),23,59,59);
        cal.getTime();
        if(index==0) {
            endTime = cal.getTimeInMillis();
        }



        cal.set(Calendar.getInstance().get(Calendar.YEAR),Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DATE));
//        Log.i(TAG,"EndDate"+cal.getTime());
//        cal.getTime();
        LocalDate s= LocalDate.now().with ( ChronoField.DAY_OF_MONTH , 1 );


        if (h.equalsIgnoreCase("week")) {
            cal.add(Calendar.WEEK_OF_YEAR, -1);
            if(index==0) {
                endTime = cal.getTimeInMillis();
                startTime = cal.getTimeInMillis();
            }
            else{
                startTime=startweekmil.get(index);
                endTime=endweekmil.get(index);
            }
            //startTime = cal.getTimeInMillis();
        }//1596479400000l
        else if (h.equalsIgnoreCase("month")) {
            if(index==0) {
                cal.add(Calendar.MONTH, -1);
                /// endTime = cal.getTimeInMillis();
                startTime= datetomill(s);
            }
            else{
                // cal.add(Calendar.MONTH, -(month-index));
                endTime=endmonthmil.get(index);
                startTime=startmonthmil.get(index);
            }
            // startTime = cal.getTimeInMillis();
        } else if (h.equalsIgnoreCase("year")){
            if(index==0) {
                Date d = new Date();
                DateFormat dateFormat = new SimpleDateFormat("yyyy");
                String format = dateFormat.format(d);
                // System.out.println("Current date and time = " + format);
                //System.out.printf("Four-digit Year = %TY\n",d);
                int year = Integer.parseInt(format);
                Calendar calendarStart = Calendar.getInstance();
                calendarStart.set(Calendar.YEAR, year);
                calendarStart.set(Calendar.MONTH, 0);
                calendarStart.set(Calendar.DAY_OF_MONTH, 1);
                // returning the first date


                Calendar calendarEnd = Calendar.getInstance();
                calendarEnd.set(Calendar.YEAR, year);
                calendarEnd.set(Calendar.MONTH, 11);
                calendarEnd.set(Calendar.DAY_OF_MONTH, 31);
                startTime = calendarStart.getTimeInMillis();
                endTime = cal.getTimeInMillis();


                // returning the last date
                Date endDate = calendarEnd.getTime();

                cal.add(Calendar.YEAR, -1);
            }
            else{
                endTime=endyearmil.get(index);
                startTime=startyearmil.get(index);

            }

          /*  Date d = new Date();
            DateFormat dateFormat = new SimpleDateFormat("yyyy");
            String format = dateFormat.format(d);
            // System.out.println("Current date and time = " + format);
            //System.out.printf("Four-digit Year = %TY\n",d);
            int year=Integer.parseInt(format);
            Calendar calendarStart=Calendar.getInstance();
            calendarStart.set(Calendar.YEAR,year);
            calendarStart.set(Calendar.MONTH,0);
            calendarStart.set(Calendar.DAY_OF_MONTH,1);
            // returning the first date
            startTime=calendarStart.getTimeInMillis();

            Calendar calendarEnd=Calendar.getInstance();
            calendarEnd.set(Calendar.YEAR,year);
            calendarEnd.set(Calendar.MONTH,11);
            calendarEnd.set(Calendar.DAY_OF_MONTH,31);

            // returning the last date
            Date endDate=calendarEnd.getTime();

            cal.add(Calendar.YEAR, -1);*/
            //  startTime = cal.getTimeInMillis();
        }


//        ArrayList<Long> dates = new ArrayList<>();
//        dates.add(startTime);

        java.text.DateFormat dateFormat = getDateInstance(DateFormat.FULL);

//        Log.i(TAG, "Range : " + dateFormat.format(dates));
//        Log.i(TAG, "Size : " + dates.size());
        Log.i(TAG, "Range Start: " + dateFormat.format(startTime));
        Log.i(TAG, "Range End: " + dateFormat.format(endTime));

        DataReadRequest readRequest =
                new DataReadRequest.Builder()
                        // The data request can specify multiple data types to return, effectively
                        // combining multiple data queries into one call.
                        // In this example, it's very unlikely that the request is for several hundred
                        // datapoints each consisting of a few steps and a timestamp.  The more likely
                        // scenario is wanting to see how many steps were walked per day, for 7 days.
                        .aggregate(DataType.TYPE_DISTANCE_DELTA, DataType.AGGREGATE_DISTANCE_DELTA)
                        // Analogous to a "Group By" in SQL, defines how data should be aggregated.
                        // bucketByTime allows for a time span, whereas bucketBySession would allow
                        // bucketing by "sessions", which would need to be defined in code.
                        .bucketByTime(1, TimeUnit.DAYS)
                        .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                        .build();
        // [END build_read_data_request]

        return readRequest;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public  void DistanceprintData(DataReadResponse dataReadResult, String type) {
        // [START parse_read_data_result]
        // If the DataReadRequest object specified aggregated data, dataReadResult will be returned
        // as buckets containing DataSets, instead of just DataSets.
        mDist.clear();
        if (dataReadResult.getBuckets().size() > 0) {
            Log.i(
                    TAG, "Number of returned buckets of DataSets is: " + dataReadResult.getBuckets().size());
            for (Bucket bucket : dataReadResult.getBuckets()) {
                List<DataSet> dataSets = bucket.getDataSets();
                for (DataSet dataSet : dataSets) {
                    if (dataSet.getDataPoints().isEmpty()) {

                        float checZero = 0;
                        long startTime = bucket.getEndTime(TimeUnit.MILLISECONDS);
                        long endTime = bucket.getEndTime(TimeUnit.MILLISECONDS);
                        DataSource dataSource = new DataSource.Builder()
                                .setAppPackageName(BuildConfig.APPLICATION_ID)
                                .setDataType(DataType.TYPE_DISTANCE_DELTA)
                                .setStreamName("move mins")
                                .setType(DataSource.TYPE_RAW)
                                .build();
                        dataSet = DataSet.create(dataSource);
                        // For each data point, specify a start time, end time, and the data value -- in this case,
                        // the number of new steps.
                        DataPoint dataPoint =
                                dataSet.createDataPoint()
                                        .setTimeInterval(startTime, endTime, TimeUnit.MILLISECONDS);
                        dataPoint.getValue(Field.FIELD_DISTANCE).setFloat(checZero);
                        dataSet.add(dataPoint);


                    }
                    DistancedumpDataSet(dataSet,type);
                }
            }

        } else if (dataReadResult.getDataSets().size() > 0) {
            Log.i(TAG, "Number of returned DataSets is: " + dataReadResult.getDataSets().size());
            for (DataSet dataSet : dataReadResult.getDataSets()) {
                DistancedumpDataSet(dataSet,type);
            }
        }
        // [END parse_read_data_result]
    }

    // [START parse_dataset]
    @RequiresApi(api = Build.VERSION_CODES.O)
    private  void DistancedumpDataSet(DataSet dataSet, String type) {
        Log.i(TAG, "Data returned for Data type: " + dataSet.getDataType().getName());
        DateFormat dateFormat = getTimeInstance();

        java.text.DateFormat dateFormat3 = getDateInstance(DateFormat.FULL);

        for (DataPoint dp : dataSet.getDataPoints()) {

            Log.i(TAG, "Data point:");
            Log.i(TAG, "\tType: " + dp.getDataType().getName());
            Log.i(TAG, "\tStart: Date: "+"==" +dateFormat3.format(dp.getStartTime(TimeUnit.MILLISECONDS)));
            Log.i(TAG, "\tEnd: Date: "+"==" + dateFormat3.format(dp.getEndTime(TimeUnit.MILLISECONDS)));
            for (Field field : dp.getDataType().getFields()) {
                double km ;
                km= dp.getValue(field).asFloat();
                double m = km/1000;
                str = String.format("%.4f", m);
                Log.i(TAG, "\tField: " + field.getName() + " Value: " + str);
                System.out.println("Checking  mMiles ::"+str);

            }

            if(type.equalsIgnoreCase("week")){
                mDist.add(str);
            }


        }
        if(mDist!=null)
        {
            Paper.book().write("Weekly_Miles",mDist);
        }

    }

}