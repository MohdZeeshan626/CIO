package com.webmobi.gecmedia.Views;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.makeramen.roundedimageview.RoundedImageView;
import com.singleevent.sdk.App;
import com.singleevent.sdk.Custom_View.Util;
import com.singleevent.sdk.model.AppDetails;
import com.singleevent.sdk.model.Events;
import com.singleevent.sdk.model.LocalArraylist.Notes;
import com.singleevent.sdk.model.LocalArraylist.Rating;
import com.singleevent.sdk.utils.DataBaseStorage;
import com.webmobi.gecmedia.Config.ApiList;
import com.webmobi.gecmedia.CustomViews.ColorFilterTransformation;
import com.webmobi.gecmedia.CustomViews.DilatingDotsProgressBar;
import com.singleevent.sdk.Custom_View.Error_Dialog;
import com.webmobi.gecmedia.CustomViews.VolleyErrorLis;
import com.webmobi.gecmedia.Models.Event;
import com.webmobi.gecmedia.R;
import com.webmobi.gecmedia.SingleEventHome;
import com.webmobi.gecmedia.Views.fragment.ContactUsFragment;
import com.webmobi.gecmedia.Views.fragment.EventFragment;
import com.webmobi.gecmedia.Views.fragment.MoreFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import io.paperdb.Paper;

/**
 * Created by Admin on 4/28/2017.
 */

public class HomeScreenMulti extends AppCompatActivity implements View.OnClickListener,  BottomNavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = HomeScreenMulti.class.getSimpleName();
    Context context;
    LinearLayout filmlists, myEvents, reEvents, myeventsview, remview;
    ConstraintLayout view5;

    Toolbar toolbar;
    ImageView key_img;
    String dir;

    ArrayList<Event> categoryevents;
    private List<String> savedirs;
    private List<String> savedirs1;
    List<String> appurls;
    LinearLayout test;
    String regId;
    private DilatingDotsProgressBar mDilatingDotsProgressBar;
    RelativeLayout v1, v2;
    float ImgWidth, ImgHeight, lwidth, lheight;
    String baseUrl = "https://webmobi.s3.amazonaws.com/nativeapps/";
    String filename = "app.json";
    TextView privateguide;
    ImageView login,login1,logo;
    static ProgressDialog pDialog;
    private ArrayList<Events> eventsToDisplay;
    private LinearLayout view_no_Internet;
    File eventDir, jsonFile, descFile;
    String event_id,event_name,event_logo,event_theme,bannerimage,mtheme;
    RelativeLayout bannerview;
    TextView mevent_name;
    Bundle bundle;
    Intent i;
    int temp,temp1;
    String apptype;
    private BottomNavigationView bottomNavigationView;
    EventFragment hfragment;
    MoreFragment moreFragment;
    ContactUsFragment contactUsFragment;
    FrameLayout frame_containers;
    EventFragment homefragment;

    TextView textViewToolBar;

    SharedPreferences spf;
    String isfrom="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Paper.init(this);
        setContentView(R.layout.act_eventlist_new);
        regId = Paper.book().read("regId");
        spf = getSharedPreferences(ApiList.LOCALSTORAGE,MODE_PRIVATE);

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        ImgWidth = displayMetrics.widthPixels * 0.30F;
        ImgHeight = ImgWidth * 1.7F;
        lwidth = ImgWidth * 0.70F;
        lheight = lwidth * 0.75F;
        apptype=Paper.book().read("AppType","");


        context = this;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //   view_no_Internet = (LinearLayout) findViewById(R.id.view_no_Internet);



        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            event_id = extras.getString("Event_ID");
            event_name=extras.getString("Event_Name");
            event_logo = extras.getString("Multi_event_logo");
            event_theme = extras.getString("Event_Theme");
            bannerimage=extras.getString("Evnet_logo");
            isfrom=extras.getString("isfrom","");
        }
        if(event_id!=null)
        {
            Paper.book().write("Current_Event_Id",event_id);
        }
        myEvents = (LinearLayout) findViewById(R.id.llayout1);
        reEvents = (LinearLayout) findViewById(R.id.llayout2);
        privateguide = (TextView) findViewById(R.id.privateguide);
        myeventsview = (LinearLayout) findViewById(R.id.myevent);
        remview = (LinearLayout) findViewById(R.id.recommevent);
        mDilatingDotsProgressBar = (DilatingDotsProgressBar) findViewById(R.id.progress);
        filmlists = (LinearLayout) findViewById(R.id.filmlist);
        login=(ImageView)findViewById(R.id.login);
        login1=(ImageView)findViewById(R.id.login1);
        logo=(ImageView)findViewById(R.id.multi_logo);
        bannerview=(RelativeLayout)findViewById(R.id.bannerview);
        mevent_name=(TextView)findViewById(R.id.mevent_name);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        test=(LinearLayout)findViewById(R.id.test);
        frame_containers=(FrameLayout)findViewById(R.id.frame_containers);
        view5=(ConstraintLayout) findViewById(R.id.pguide);
        key_img=(ImageView)findViewById(R.id.key_img);

//        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        // Showmore.setOnClickListener(this);

        //   pguide.setOnClickListener(this);



        // bottomNavigationView.setSelectedItemId(R.id.navigation_events);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        //    bottomNavigationView.performClick();



        if(apptype.equalsIgnoreCase("Branded")){
            login1.setVisibility(View.INVISIBLE);
        }

        privateguide.setTypeface(Util.lighttypeface(HomeScreenMulti.this));

        ///// Set for multi event

        setEventTheme();

        dir = getFilesDir() + File.separator + "EventsDownload" + File.separator;
        savedirs = new ArrayList<>();
        savedirs = getSaveDirs(dir);
        login.setOnClickListener(this);
        login1.setOnClickListener(this);



        categoryevents = new ArrayList<>();
        getCategoryEvents(ApiList.MultiEvents+event_id);



        privateguide.setOnClickListener(this);
        if (!DataBaseStorage.isInternetConnectivity(this)){
            //  view_no_Internet.setVisibility(View.VISIBLE);
        }else {
            // view_no_Internet.setVisibility(View.GONE);
        }

        showrecomevents();

    }

    private List<String> getSaveDirs(String dir) {
        List<String> paths = new ArrayList<>();
        File directory = new File(dir);

        File[] files = directory.listFiles();

        for (File file : files) {
            if (file.isDirectory())
                paths.add(file.getName());
        }
        return paths;
    }


    private void showitems(boolean flag) {
        if (flag) {
            remview.setVisibility(View.VISIBLE);
        } else {
            remview.setVisibility(View.GONE);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);//Menu Resource, Menu
        if (Paper.book().read("Islogin", false)) {
            menu.findItem(R.id.login).setVisible(false);
            menu.findItem(R.id.profile).setVisible(true);
        } else {
            menu.findItem(R.id.login).setVisible(true);
            menu.findItem(R.id.profile).setVisible(false);
        }
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i;
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                finish();
                return true;

            case R.id.login:
                i = new Intent(HomeScreenMulti.this, RegActivity.class);
                startActivity(i);
                return true;
            case R.id.profile:
                i = new Intent(HomeScreenMulti.this, Profile.class);
                startActivityForResult(i, 41);
                return true;
          /*  case R.id.help:
                getCategoryEvents(ApiList.MultiEvents);
                return true;*/
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        if (Paper.book().read("Islogin", false)) {
            menu.findItem(R.id.login).setVisible(false);
            menu.findItem(R.id.profile).setVisible(true);
        } else {
            menu.findItem(R.id.login).setVisible(true);
            menu.findItem(R.id.profile).setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onResume() {
        super.onResume();

        setTitle("");
        showrecomevents();
        showsavedevents();
        invalidateOptionsMenu();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.navigation_events:
                /*Log.v(TAG, "navigation_home");
                if (homefragment == null)
                    homefragment = new EventFragment();

                Bundle bundle = new Bundle();
                bundle.putString("EVENT_ID", event_id);
                bundle.putString("APP_ID", appDetails.getAppId());
                homefragment.setArguments(bundle);

                replaceFragment(homefragment, "homefragment");*/

                //   event_text.setVisibility(View.VISIBLE);
                //   textViewToolBar.setVisibility(View.GONE);

                if (Paper.book().read("Islogin", false)) {
                    i = new Intent(this, HomeScreenMulti.class);
                    i = getIntent();
                    startActivity(i);
                    finish();
                    return true;
                }
                else{
                    i = new Intent(HomeScreenMulti.this, RegActivity.class);
                    i.setAction(ApiList.loginaction);
                    startActivityForResult(i, 40);


                    break;
                }
            case R.id.navigation_nearby:

                if (Paper.book().read("Islogin", false)) {

                       /* if (nearByFragment == null)
                            nearByFragment = new NearByFragment();
                        replaceFragment(nearByFragment, "nearbyfragment");
                        ll_toolbar_content.setVisibility(View.VISIBLE);
                        textViewToolBar.setVisibility(View.GONE);*/
                    i = new Intent(getApplicationContext(), Profile.class);
                    //                i = new Intent(getActivity(), NewProfile.class);
                    startActivityForResult(i, 41);
                    return true;
                } else {
                    i = new Intent(HomeScreenMulti.this, RegActivity.class);
                    i.setAction(ApiList.loginaction);
                    startActivityForResult(i, 40);

                    break;
                }

            case R.id.navigation_contactus:

                Log.v(TAG, "more");

                if (Paper.book().read("Islogin", false)) {
                    if (contactUsFragment == null)
                        contactUsFragment = new ContactUsFragment();
                    replaceFragment(contactUsFragment, "contactusfragment");
                    test.setVisibility(View.GONE);
                    frame_containers.setVisibility(View.VISIBLE);

                    return  true;
                        //  textViewToolBar.setVisibility(View.VISIBLE);
                        // textViewToolBar.setText(Util.applyFontToMenuItem(this, new SpannableString("Contacts")));
                      /*  contactUsFragment = new ContactUsFragment();
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.frame_containers, contactUsFragment).commit();
*/
                  /*  i = new Intent(HomeScreenMulti.this, ContactUsFragment.class);
                    startActivity(i);*/
                   // return true;
                } else {
                    i = new Intent(HomeScreenMulti.this, RegActivity.class);
                    i.setAction(ApiList.loginaction);
                    startActivityForResult(i, 40);

                    break;
                }

            case R.id.navigation_more:
                Log.v(TAG, "more");
                if (Paper.book().read("Islogin", false)) {

                  /*  if (moreFragment == null)
                        moreFragment = new MoreFragment();
                    replaceFragment(moreFragment, "morefragment");*/
                    i = new Intent(HomeScreenMulti.this, HelpActivity.class);
                    startActivity(i);

                    //  textViewToolBar.setVisibility(View.VISIBLE);
                    // textViewToolBar.setText(Util.applyFontToMenuItem(this, new SpannableString("More ")));
                    return true;
                } else {
                    i = new Intent(HomeScreenMulti.this, RegActivity.class);
                    i.setAction(ApiList.loginaction);
                    startActivityForResult(i, 40);


                    break;
                }

        }


        return false;
    }


    private void replaceFragment(Fragment fragment, String popularfragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        androidx.fragment.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_containers, fragment, popularfragment);
        transaction.commit();
    }

    ///
    public  void setEventTheme(){

        try {
            if (event_logo != null && !event_logo.equals("")) {

                Glide.with(getApplicationContext()).load((event_logo))
                        .placeholder(R.drawable.default_logo)
                        .dontAnimate()
                        .error(R.drawable.medium_no_image)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .bitmapTransform(new ColorFilterTransformation(context, Color.argb(0, 0, 0, 0)))
                        .into(logo);

            }

            if (event_name != null && !event_name.equals("")) {
                mevent_name.setText(event_name);
            }
            if (event_theme != null && !event_theme.equals("")) {
                mtheme="#"+event_theme;
                bannerview.setBackgroundColor(Color.parseColor(mtheme));
                login1.setBackgroundColor(Color.parseColor(mtheme));
                login.setBackgroundColor(Color.parseColor(mtheme));
                view5.setBackgroundColor(Color.parseColor(mtheme));
                key_img.setBackgroundColor(Color.parseColor(mtheme));
            }
        }catch (Exception e)
        {}
    }


    private void getCategoryEvents(String url) {
        String tag_string_req = "category events";
        url = url.replaceAll(" ", "%20");

        StringRequest strReq = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {

                mDilatingDotsProgressBar.hideNow();
                //   view_no_Internet.setVisibility(View.GONE);

                try {
                    JSONObject json = new JSONObject(response);
                    if (json.getBoolean("response")) {
                        parseResult(json.getJSONArray("events"));
                    } else
                        showitems(false);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                mDilatingDotsProgressBar.hideNow();
                if (error instanceof TimeoutError) {
                    Error_Dialog.show("Timeout", HomeScreenMulti.this);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, context), HomeScreenMulti.this);
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    //     view_no_Internet.setVisibility(View.VISIBLE);
                    final Dialog derror = Util.showinternt(HomeScreenMulti.this);
                    TextView txttitle = (TextView) derror.findViewById(com.singleevent.sdk.R.id.retry);
                    txttitle.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            derror.dismiss();
                            getCategoryEvents(ApiList.MultiEvents+event_id);
                        }
                    });

                    derror.show();

                }

            }
        });


        // Adding request to request queue
        App.getInstance().addToRequestQueue(strReq, tag_string_req);
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

    }

    private void parseResult(JSONArray events) {

        try {

            /*categoryevents.clear();*/
            Gson gson = new Gson();
            appurls=new ArrayList<>();
            for (int i = 0; i < events.length(); i++) {
                String eventString = events.getJSONObject(i).toString();
                Event obj = gson.fromJson(eventString, Event.class);

                String appurl = events.getJSONObject(i).getString("appid");
                if(savedirs.contains(appurl))
                {
                    appurls.add(appurl);
                }
                Paper.book(appurl).write("InfoPrivacy",obj.getInfo_privacy());
                Paper.book(appurl).write("Engagement",obj.getEvent_category());
                if (savedirs.contains(appurl))
                    obj.setDownloaded(true);
                else
                    obj.setDownloaded(false);

                categoryevents.add(obj);
            }
            showsavedevents();
            savedirs.clear();
            // savedirs=appurls;


            if (categoryevents.size() > 0) {
                if(categoryevents.size()==1){
                    downloadjson(categoryevents.get(0),0);

                }else {
                    showitems(true);
                    showrecomevents();
                }
            } else
                showitems(false);


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private void downloadjson(@NonNull final Event events, final int pos) {

        String query="";
        try {
            query = URLEncoder.encode( events.getApp_url(), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String jsonUrl = baseUrl +query + "/appData.json";


        // String jsonUrl = baseUrl + events.getApp_url() + "/appData.json";
        pDialog = new ProgressDialog(context, R.style.MyAlertDialogStyle);
        pDialog.setMessage("Downloading...");
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.show();


        String tag_string_req = "Downloading";
        System.out.println("Url " + jsonUrl);

        StringRequest jsonRequest = new StringRequest(Request.Method.GET, jsonUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialog.dismiss();
                categoryevents.get(pos).setDownloaded(true);
                System.out.println("JSON Response " + response);

                eventDir = new File(dir + events.getAppid());
                if (!eventDir.exists())
                    eventDir.mkdir();
                jsonFile = new File(eventDir, filename);
                descFile = new File(eventDir, "description.txt");
                try {
                    Files.write(response, jsonFile, Charset.defaultCharset());
                    Files.append(events.getApp_name() + "\n", descFile, Charset.defaultCharset());
                    Files.append(events.getApp_title() + "\n", descFile, Charset.defaultCharset());
                    Files.append(events.getApp_logo()+"\n",descFile,Charset.defaultCharset());
                    Files.append(events.getApp_category() + "\n", descFile, Charset.defaultCharset());
                    Files.append(events.getStart_date() + "\n", descFile, Charset.defaultCharset());
                    Files.append(events.getAppid() + "\n", descFile, Charset.defaultCharset());
                    Files.append(events.getLocation() + "\n", descFile, Charset.defaultCharset());
                    Files.append(events.getVenue() + "\n", descFile, Charset.defaultCharset());
                    Files.append(events.getApp_image() + "\n", descFile, Charset.defaultCharset());
                    Files.append(events.getApp_url() + "\n", descFile, Charset.defaultCharset());


                    openevent(events.getAppid());
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                if (error instanceof TimeoutError) {
                    Error_Dialog.show("Timeout", HomeScreenMulti.this);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, context), HomeScreenMulti.this);
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show("No Internet Connection", HomeScreenMulti.this);
                }

            }
        }) {
            @Override
            protected Response<String> parseNetworkResponse(
                    NetworkResponse response) {

                String strUTF8 = null;
                try {
                    strUTF8 = new String(response.data, "UTF-8");

                } catch (UnsupportedEncodingException e) {

                    e.printStackTrace();
                }
                return Response.success(strUTF8,
                        HttpHeaderParser.parseCacheHeaders(response));
            }
        };

        App.getInstance().addToRequestQueue(jsonRequest, tag_string_req);
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

    }

    private void openevent(String appid) throws IOException {

        eventDir = new File(dir + appid);
        jsonFile = new File(eventDir, filename);

        String contents = Files.toString(jsonFile, Charset.defaultCharset());
        System.out.println("File Contents : " + contents);


        eventsToDisplay = new ArrayList<>();
        Gson gson = new Gson();


        JSONObject args = null;
        try {
            args = new JSONObject(contents);
            AppDetails obj = gson.fromJson(args.toString(), AppDetails.class);
            Paper.book().write("Appdetails", obj);
            JSONArray eventslist = args.getJSONArray("events");
            String eventString = eventslist.getJSONObject(0).toString();
            Events eobj = gson.fromJson(eventString, Events.class);
            eventsToDisplay.add(eobj);
            Paper.book().write("Appevents", eventsToDisplay);
            int info_privacy = Paper.book(obj.getAppId()).read("InfoPrivacy",0);
            String engage=Paper.book(obj.getAppId()).read("Engagement","");



            //if user is logged in
            if (Paper.book().read("Islogin",false)){
              try {
                  if (info_privacy == 0) {
                      //call checkin to check whether user is registered or not
                      FetchPublicUSerdata(obj.getAppId());
                  } else {
                      //info_privacy is 1 call single event login
                      FetchPrivateUserData(obj.getAppId(), info_privacy);
                  }
              }catch (Exception e){}

            } else{
                //if user is not logged in
                if (info_privacy==0){
                    Intent intent = new Intent( context, SingleEventHome.class );
                    intent.putExtra("Engagement",engage);
                    intent.putExtra("back","APPS");


                    startActivity(intent);
                }else {
                    //info_privacy is 1
                    Intent intent = new Intent( context, LoginActivity.class );
                    intent.putExtra("Engagement",engage);
                    startActivity(intent);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case 40:
                if (resultCode == RESULT_OK)
                    invalidateOptionsMenu();

                break;

            case 41:

                if (resultCode == RESULT_OK)
                    if (Build.VERSION.SDK_INT >= 11) {
                        recreate();
                    } else {
                        Intent intent = getIntent();
                        startActivity(intent);
                    }
                break;
            case 1:
                if (resultCode==RESULT_OK){

                    Log.d(TAG,"Login Successfull ");
                }



        }

    }


    // geting the downloaded events


    private void showsavedevents() {

        myEvents.removeAllViews();
        savedirs = new ArrayList<>();
        savedirs = getSaveDirs(dir);
        try {
            String s = appurls.get(0);
        }catch (Exception e)
        {

        }
        if (savedirs.size() > 0) {
            try {
                if (appurls.size() > 0) {
                    loadSavedEvents(appurls);
                    myeventsview.setVisibility(View.VISIBLE);
                }
                else{
                    myeventsview.setVisibility(View.GONE);
                }
            }catch (Exception e)
            {

            }

        } else {
            myeventsview.setVisibility(View.GONE);
        }
    }


    private void loadSavedEvents(List<String> savedDirs) {
        for (String dir : savedDirs)
            addToSaved(dir, false);
    }


    public void addToSaved(String tag, boolean b) {

        List<String> text = new ArrayList<>();
        eventDir = new File(dir + tag);
        descFile = new File(eventDir, "description.txt");

        try {
            text = Files.readLines(descFile, Charset.defaultCharset());
            System.out.println("Values " + text);
        } catch (IOException e) {
            e.printStackTrace();
        }


        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View child = inflater.inflate(R.layout.events_view, null);


        RelativeLayout v2 = (RelativeLayout) child.findViewById(R.id.v2);
        RelativeLayout.LayoutParams logolayoutParams = (RelativeLayout.LayoutParams) v2.getLayoutParams();
        logolayoutParams.width = (int) (ImgWidth * 1.30);
        logolayoutParams.height = (int)(ImgWidth *1.50);
        logolayoutParams.setMargins(10, 0, 0, 0);
        v2.setLayoutParams(logolayoutParams);


        int clogo_height = (int) (ImgHeight * 0.30);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams((int) (ImgWidth * 1.30), clogo_height);

        RoundedImageView clogo_center = (RoundedImageView) child.findViewById(R.id.logo2);
        RoundedImageView clogo = (RoundedImageView) child.findViewById(R.id.re_tile_logo);

        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        clogo.setLayoutParams(layoutParams);
        clogo.setCornerRadius(7, 7, 0, 0);
        TextView etitle = (TextView) child.findViewById(R.id.eventname);
        // TextView ecat = (TextView) child.findViewById(R.id.eventcat);
        TextView edate = (TextView) child.findViewById(R.id.eventdate);


        for(int k=0; k<categoryevents.size(); k++)
        {
            if(text.get(5).equalsIgnoreCase(categoryevents.get(k).getAppid()))
            {
                temp=k;
                break;
            }
        }
        etitle.setText(categoryevents.get(temp).getApp_title());
        //  ecat.setText(text.get(0));

        etitle.setTypeface(Util.boldtypeface(context));
        //ecat.setTypeface(Util.lighttypeface(context));
        ImageView logo2 = (ImageView) child.findViewById(R.id.logo2);
        RelativeLayout logo2_layout = (RelativeLayout) child.findViewById(R.id.logo2_layout);
        int margintop = (int) (clogo_height - (getResources().getDimensionPixelSize(R.dimen.abc_action_bar_default_height_material) / 2));
        RelativeLayout.LayoutParams logo2_layout_params = new RelativeLayout.LayoutParams((int) (ImgWidth * 0.45), (int) (ImgWidth * 0.45));
        logo2_layout_params.setMargins(0, margintop, 0, 0);
        logo2_layout_params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        logo2_layout.setLayoutParams(logo2_layout_params);

        View split_View = (View) child.findViewById(R.id.split_view);
        RelativeLayout.LayoutParams split_view_params = (RelativeLayout.LayoutParams) split_View.getLayoutParams();
        split_view_params.height = (int) (ImgWidth * 1.30);
        split_View.setLayoutParams(split_view_params);

        String Startsplits = categoryevents.get(temp).getStart_date().split("T")[0];

        clogo_center.setLayoutParams(new RelativeLayout.LayoutParams((int) (ImgWidth * 0.45), (int) (ImgWidth * 0.45)));
        Glide.with(getApplicationContext()).load((categoryevents.get(temp).getApp_logo().equalsIgnoreCase("")) ? R.drawable.medium_no_image : categoryevents.get(temp).getApp_logo())
                .fitCenter()
                .placeholder(R.drawable.medium_no_image)
                .dontAnimate()
                .error(R.drawable.medium_no_image)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .bitmapTransform(new ColorFilterTransformation(context, Color.argb(0, 0, 0, 0)))
                .into(clogo_center);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatter1 = new SimpleDateFormat("EEE MMM dd/yyyy");
        try {
            Date date = formatter.parse(Startsplits);
            edate.setText(formatter1.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }


        Glide.with(getApplicationContext()).load((categoryevents.get(temp).getApp_image().equalsIgnoreCase("")) ? R.drawable.medium_no_image : categoryevents.get(temp).getApp_image())
                .fitCenter()
                .dontAnimate()
                .placeholder(R.drawable.medium_no_image)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.medium_no_image)
                .bitmapTransform(new ColorFilterTransformation(context, Color.argb(0, 0, 0, 0)))
                .into(clogo);

        child.setTag(tag);
        child.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String tag = String.valueOf(view.getTag());

                eventDir = new File(dir + tag);
                jsonFile = new File(eventDir, filename);
                System.out.println("Loaded event");
                try {
                    child.setEnabled(false);
                    showjs(jsonFile);
                    enablingview(child);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


        child.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                String tag = String.valueOf(v.getTag());
                eventDir = new File(dir + tag);
                jsonFile = new File(eventDir, filename);

                //opens dialog to delete an event
                dialogToDeleteEvent(eventDir, child,myEvents);
                // myEvents.removeView(child);

                if (myEvents.getChildCount() == 0)
                    myeventsview.setVisibility(View.GONE);


              /*  eventDir = new File(dir + tag);
                jsonFile = new File(eventDir, filename);
                try {
                    deleteFile(eventDir);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                int ImagePosition = myEvents.indexOfChild(child);
                myEvents.removeView(child);
                if (myEvents.getChildCount() == 0)
                    myeventsview.setVisibility(View.GONE);
                Toast.makeText(context, "Event Removed", Toast.LENGTH_LONG).show();*/
                //Event events = categoryevents.get(position);
                //events.setDownloaded(false);
                return true;
            }
        });

        myEvents.addView(child);


        if (b) {
            eventDir = new File(dir + tag);
            jsonFile = new File(eventDir, filename);

            try {
                showjs(jsonFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void dialogToDeleteEvent(final File file,final View child,final LinearLayout llParentView ){


        AlertDialog.Builder builder = new AlertDialog.Builder(HomeScreenMulti.this,R.style.MyAlertDialogStyle);
        builder.setMessage(R.string.dialog_fire_missiles)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!
                        try {
                            deleteFile(file);

                            if (llParentView.getId()==myEvents.getId()){


                                myEvents.removeView(child);
                                if (myEvents.getChildCount() == 0)
                                    myeventsview.setVisibility(View.GONE);
                            }


                        } catch (IOException e) {

                            e.printStackTrace();

                        }

                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog

                    }
                });
        // Create the AlertDialog object and return it
        builder.create();
        builder.show();
    }


    private void deleteFile(File dir) throws IOException {

        String[] entries = dir.list();
        for (String s : entries) {
            File currentFile = new File(dir.getPath(), s);
            currentFile.delete();
        }
        boolean dirdel = dir.delete();

    }

    AppDetails appDetails = Paper.book().read("Appdetails");

    private void FetchPrivateUserData(final String appId, final int privacy) {

        final ProgressDialog dialog = new ProgressDialog(context, R.style.MyAlertDialogStyle);
        dialog.setMessage("Loading Details ...");
        dialog.show();


        String tag_string_req = "Login";
        String url = com.singleevent.sdk.ApiList.Login_User;
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {

                try {
                    System.out.println(response);
                    JSONObject jObj = new JSONObject(response);


                    if (jObj.getBoolean("response")) {
                        parseresult(jObj.getJSONObject("responseString"),appId);
                        dialog.dismiss();
                    } else {
                        dialog.dismiss();
                        Error_Dialog.show(jObj.getString("responseString"), HomeScreenMulti.this);
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
                    Error_Dialog.show("Timeout", HomeScreenMulti.this);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, HomeScreenMulti.this), HomeScreenMulti.this);
                } else if (VolleyErrorLis.isNetworkProblem(error)) {

                    Intent sending = new Intent(HomeScreenMulti.this, SingleEventHome.class);
                    sending.putExtra("Engagement",Paper.book(appId).read("Engagement",""));
                    sending.putExtra("back","APPS");
                    startActivity(sending);
                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("deviceType", "android");
                params.put("deviceId", regId);
                params.put("appid", appId);
                params.put("userid_flag", "0");
                params.put("info_privacy", "true");

                //params.put("info_privacy", String.valueOf(privacy));
                System.out.println(params);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                // add headers <key,value>
                String credentials = Paper.book().read("Email", "") + ":" + Paper.book(appId).read("PrivateKey", "");
                String auth = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                headers.put("Authorization", auth);
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

    private void parseresult(JSONObject details,String appId) throws JSONException {

        if (!details.isNull("profile_pic")){
            Paper.book().write("profile_pic",details.getString("profile_pic"));
        }

        Paper.book(appId).write("token", details.getJSONObject("token").getString("token"));
        Paper.book().write("userId", details.getJSONObject("token").getString("userId"));
        Paper.book(appId).write("userId", details.getJSONObject("token").getString("userId"));
        Paper.book().write("username", details.getJSONObject("token").getString("username"));
        Paper.book().write("email", Paper.book().read("Email"));
        Paper.book().write("survey_flag",details.getInt("survey_flag"));
        Paper.book(appId).write("survey_flag",details.getInt("survey_flag"));

        Paper.book().write("admin_flag",details.getString("admin_flag"));
        Paper.book(appId).write("admin_flag",details.getString("admin_flag"));
        try{
            Paper.book(appId).write("attendee_option",details.getString("attendee_option"));
        }catch (Exception e){

        }

        SharedPreferences.Editor editor = spf.edit();
        editor.putString("username", Paper.book().read("username").toString());
        editor.putString("user_id",Paper.book().read("userId").toString());
        editor.apply();
        // adding the sch list
        List<Integer> list = new ArrayList<>();


        JSONArray jsonArr = new JSONArray(details.getString("schedules"));

        for (int i = 0; i < jsonArr.length(); i++)

           try {
               list.add(jsonArr.getInt(i));
           }catch (Exception e){}

        Paper.book(appId).write("SCH", list);

        // adding exhibitor list

        list = new ArrayList<>();
        if (!details.getString("exhibitor_favorites").equalsIgnoreCase("")) {
            jsonArr = new JSONArray(details.getString("exhibitor_favorites"));
            for (int i = 0; i < jsonArr.length(); i++)
              try {
                  list.add(jsonArr.getInt(i));
              }catch (Exception e){}
        }

        Paper.book(appId).write("Exhibitor", list);

        //adding schedule list

        list = new ArrayList<>();

        if (!details.getString("sponsor_favorites").equalsIgnoreCase("")) {
            jsonArr = new JSONArray(details.getString("sponsor_favorites"));
            for (int i = 0; i < jsonArr.length(); i++)
                list.add(jsonArr.getInt(i));
        }


        Paper.book(appId).write("Sponsor", list);

        JSONObject notesdetails = details.getJSONObject("notes");

        // Adding Note of Agenda
        JSONArray agenda = notesdetails.getJSONArray("agenda");
        Gson gson = new Gson();
        HashMap<Integer, Notes> noteslist = new HashMap<>();
        for (int i = 0; i < agenda.length(); i++) {
            String eventString = agenda.getJSONObject(i).toString();
            Notes eobj = gson.fromJson(eventString, Notes.class);
            noteslist.put(eobj.getId(), eobj);
        }

        Paper.book(appId).write("AgendaNote", noteslist);


        // Adding Note of Exhibitor
        JSONArray exh = notesdetails.getJSONArray("exhibitors");

        noteslist = new HashMap<>();
        for (int i = 0; i < exh.length(); i++) {
            String eventString = exh.getJSONObject(i).toString();
            Notes eobj = gson.fromJson(eventString, Notes.class);
            noteslist.put(eobj.getId(), eobj);
        }

        Paper.book(appId).write("ExhibitorNote", noteslist);

        // Adding Note of Sponsor

        JSONArray sponsor = notesdetails.getJSONArray("sponsors");

        noteslist = new HashMap<>();
        for (int i = 0; i < sponsor.length(); i++) {
            String eventString = sponsor.getJSONObject(i).toString();
            Notes eobj = gson.fromJson(eventString, Notes.class);
            noteslist.put(eobj.getId(), eobj);
        }

        Paper.book(appId).write("SponsorNote", noteslist);


        // Adding Rating of agenda
        JSONObject ratingdetails = details.getJSONObject("ratings");

        JSONArray AgendaRating = ratingdetails.getJSONArray("agenda");

        HashMap<Integer, Rating> ratinglist = new HashMap<>();

        for (int i = 0; i < AgendaRating.length(); i++) {
            String eventString = AgendaRating.getJSONObject(i).toString();
            Rating eobj = gson.fromJson(eventString, Rating.class);
            ratinglist.put(eobj.getType_id(), eobj);
        }

        Paper.book(appId).write("AgendaRating", ratinglist);


        //Adding Rating of Speaker

        JSONArray SpeakerRating = ratingdetails.getJSONArray("speakers");

        ratinglist = new HashMap<>();
        for (int i = 0; i < SpeakerRating.length(); i++) {
            String eventString = SpeakerRating.getJSONObject(i).toString();
            Rating eobj = gson.fromJson(eventString, Rating.class);
            ratinglist.put(eobj.getType_id(), eobj);
        }

        Paper.book(appId).write("SpeakerRating", ratinglist);


        Intent sending = new Intent(HomeScreenMulti.this, SingleEventHome.class);
        sending.putExtra("Engagement",Paper.book(appId).read("Engagement",""));
        sending.putExtra("back","APPS");
        startActivity(sending);
    }


    private void FetchPublicUSerdata(final String appId ) {
try {
    final ProgressDialog dialog = new ProgressDialog(HomeScreenMulti.this, R.style.MyAlertDialogStyle);
    dialog.setMessage("Loading Details...");
    dialog.show();


    String tag_string_req = "Login";
    String url = com.singleevent.sdk.ApiList.CheckIN;
    StringRequest strReq = new StringRequest(Request.Method.POST,
            url, new Response.Listener<String>() {


        @Override
        public void onResponse(String response) {
            dialog.dismiss();
            try {
                System.out.println(response);
                JSONObject jObj = new JSONObject(response);


                if (jObj.getBoolean("response")) {
                    parseresult(jObj.getJSONObject("responseString"), appId);
                    dialog.dismiss();
                } else {
                    dialog.dismiss();
                    Error_Dialog.show(jObj.getString("responseString"), HomeScreenMulti.this);
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
                Error_Dialog.show("Timeout", HomeScreenMulti.this);
            } else if (VolleyErrorLis.isServerProblem(error)) {
                Error_Dialog.show(VolleyErrorLis.handleServerError(error, HomeScreenMulti.this), HomeScreenMulti.this);
            } else if (VolleyErrorLis.isNetworkProblem(error)) {

                Intent sending = new Intent(HomeScreenMulti.this, SingleEventHome.class);
                sending.putExtra("Engagement", Paper.book(appId).read("Engagement", ""));
                sending.putExtra("back", "APPS");
                startActivity(sending);

            }

        }
    }) {
        @Override
        protected Map<String, String> getParams() {
            Map<String, String> params = new HashMap<String, String>();
            params.put("deviceType", "android");
            params.put("deviceId", regId);
            params.put("appid", appId);
            params.put("email", Paper.book().read("Email", ""));
            params.put("username", Paper.book().read("username", ""));
            params.put("userid", Paper.book().read("userId", ""));
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
}catch (Exception e){

}


    }

    private void showjs(File jsonFile) throws IOException {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage("Loading Event..");
        dialog.show();

        String contents = Files.toString(jsonFile, Charset.defaultCharset());
        System.out.println("File Contents : " + contents);

        eventsToDisplay = new ArrayList<>();
        Gson gson = new Gson();


        JSONObject args = null;
        try {
            args = new JSONObject(contents);
            AppDetails obj = gson.fromJson(args.toString(), AppDetails.class);
            Paper.book().write("Appdetails", obj);
            JSONArray eventslist = args.getJSONArray("events");
            JSONArray items=eventslist.getJSONObject(0).getJSONArray("tabs").getJSONObject(0).getJSONArray("items");
            Log.d("items_array", "showjs: "+items.length());
            String eventString = eventslist.getJSONObject(0).toString();
            Events eobj = gson.fromJson(eventString, Events.class);
            eventsToDisplay.add(eobj);
            Paper.book().write("Appevents", eventsToDisplay);
            dialog.dismiss();


            int info_privacy = Paper.book(obj.getAppId()).read("InfoPrivacy",0);
            String engage=Paper.book(obj.getAppId()).read("Engagement","");

            //if user is logged in
            if (Paper.book().read("Islogin",false)){

                if (info_privacy==0){
                    //call checkin to check whether user is registered or not
                    FetchPublicUSerdata( obj.getAppId() );
                }else {
                    //info_privacy is 1 call single event login
                    FetchPrivateUserData(obj.getAppId(),info_privacy );
                }

            } else{
                //if user is not logged in
                if (info_privacy==0){
                    Intent intent = new Intent( context, SingleEventHome.class );
                    intent.putExtra("Engagement",engage);
                    intent.putExtra("back","APPS");
                    startActivity(intent);
                }else {
                    //info_privacy is 1
                    Intent intent = new Intent( context, LoginActivity.class );
                    startActivity(intent);
                }
            }



        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void enablingview(final View child) {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                child.setEnabled(true);
            }
        }, 1 * 1000); // wait for 1 seconds
    }


    private void showrecomevents() {
        int clogo_height = 0, margintop = 0;
        reEvents.removeAllViews();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for (int i = 0; i < categoryevents.size(); i++) {
            final Event listing = categoryevents.get(i);
            final View child = inflater.inflate(R.layout.events_view, null);

            RelativeLayout v2 = (RelativeLayout) child.findViewById(R.id.v2);
            RelativeLayout.LayoutParams logolayoutParams = (RelativeLayout.LayoutParams) v2.getLayoutParams();
            logolayoutParams.width = (int) (ImgWidth * 1.30);
            logolayoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            logolayoutParams.setMargins(10, 0, 0, 0);
            v2.setLayoutParams(logolayoutParams);

            clogo_height = (int) (ImgHeight * 0.32);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams((int) (ImgWidth * 1.30), clogo_height);


            //used to set the center logo of the tile
            RoundedImageView clogo_center = (RoundedImageView) child.findViewById(R.id.logo2);
            //used to set the tile banner image
            RoundedImageView clogo = (RoundedImageView) child.findViewById(R.id.re_tile_logo);

            layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

            //setting tile banner
            Glide.with(getApplicationContext()).load((listing.getApp_image().equalsIgnoreCase("")) ? R.drawable.medium_no_image : listing.getApp_image())
                    .fitCenter()
                    .placeholder(R.drawable.medium_no_image)
                    .dontAnimate()
                    .error(R.drawable.medium_no_image)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .bitmapTransform(new ColorFilterTransformation(context, Color.argb(0, 0, 0, 0)))
                    .into(clogo);

            clogo.setCornerRadius(7, 7, 0, 0);
            clogo.setLayoutParams(layoutParams);
            ImageView logo2 = (ImageView) child.findViewById(R.id.logo2);
            RelativeLayout logo2_layout = (RelativeLayout) child.findViewById(R.id.logo2_layout);
            if (this != null ) {
                margintop = (int) (clogo_height - (getResources().getDimensionPixelSize(R.dimen.abc_action_bar_default_height_material) / 2));
            }
            RelativeLayout.LayoutParams logo2_layout_params = new RelativeLayout.LayoutParams((int) (ImgWidth * 0.45), (int) (ImgWidth * 0.45));
            logo2_layout_params.setMargins(0, margintop, 0, 0);
            logo2_layout_params.addRule(RelativeLayout.CENTER_HORIZONTAL);
            logo2_layout.setLayoutParams(logo2_layout_params);

            clogo_center.setLayoutParams(new RelativeLayout.LayoutParams((int) (ImgWidth * 0.45), (int) (ImgWidth * 0.45)));

            //setting tile center logo
            Glide.with(getApplicationContext()).load((listing.getApp_logo().equalsIgnoreCase("")) ? R.drawable.logo : listing.getApp_logo())
                    .fitCenter()
                    .placeholder(R.drawable.medium_no_image)
                    .dontAnimate()
                    .error(R.drawable.medium_no_image)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .bitmapTransform(new ColorFilterTransformation(context, Color.argb(0, 0, 0, 0)))
                    .into(clogo_center);
            TextView etitle = (TextView) child.findViewById(R.id.eventname);
            // TextView ecat = (TextView) child.findViewById(R.id.eventcat);
            TextView edate = (TextView) child.findViewById(R.id.eventdate);

            View split_View = (View) child.findViewById(R.id.split_view);
            RelativeLayout.LayoutParams split_view_params = (RelativeLayout.LayoutParams) split_View.getLayoutParams();
            split_view_params.height = (int) (ImgWidth * 1.30);
            split_View.setLayoutParams(split_view_params);

            //setting event title
            etitle.setText(listing.getApp_title());
            //setting event category
            //    ecat.setText(listing.getApp_category());

            etitle.setTypeface(Util.boldtypeface(context));
            //  ecat.setTypeface(Util.lighttypeface(context));

            String Startsplits = listing.getStart_date().split("T")[0];

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat formatter1 = new SimpleDateFormat("EEE MMM dd/yyyy");
            try {
                Date date = formatter.parse(Startsplits);
                //setting event date
                edate.setText(formatter1.format(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            child.setId(i);
            child.setTag(i);

            child.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = (int) view.getTag();

                    if (Paper.book().read("Islogin", false)){
                        Event events = categoryevents.get(pos);
                        savedirs = getSaveDirs(dir);
                        if (savedirs.contains(events.getAppid()))
                            events.setDownloaded(true);
                        else
                            events.setDownloaded(false);

                        if ( !events.isDownloaded() )
                            downloadjson(events, pos);

                        else {
                            eventDir = new File(dir + events.getAppid());
                            jsonFile = new File(eventDir, filename);
                            System.out.println("Loaded event");
                            try {
                                child.setEnabled(false);
                                showjs(jsonFile);

                            } catch (IOException e) {
                                e.printStackTrace();
                            }finally {
                                enablingview(child);
                            }
                        }}
                    else{
                        Intent i;
                        i = new Intent(HomeScreenMulti.this, RegActivity.class);
                        startActivity(i);

                    }


                }
            });


            reEvents.addView(child);
        }

    }


    @Override
    public void onClick(View view) {

        Intent i;
        switch (view.getId()) {
            case R.id.privateguide:
                if (Paper.book().read("Islogin", false)) {
                    i = new Intent(HomeScreenMulti.this, PrivateSearch.class);
                    i.putExtra("Event_id",event_id);
                    startActivity(i);
                } else {
                   /* i = new Intent();
                    i.putExtra("keyMessage", "Please Login to Search or Download the Private Events");
                    i.putExtra("keyAlert", "Login/Register");
                    i.setClassName(HomeScreenMulti.this.getPackageName(), HomeScreenMulti.this.getPackageName()+".Views.TokenExpireAlertReceived");
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);*/
                    i = new Intent(HomeScreenMulti.this, RegActivity.class);
                    startActivity(i);
                }
                break;
            case R.id.login:
                if (Paper.book().read("Islogin", false)) {
                    i = new Intent(HomeScreenMulti.this, Profile.class);
                    startActivityForResult(i, 41);
                } else {
                    i = new Intent(HomeScreenMulti.this, RegActivity.class);
                    startActivity(i);
                }
                break;
            case R.id.login1:
                   
                    if(isfrom.equalsIgnoreCase("demo")){
                        i=new Intent(HomeScreenMulti.this,RegActivity.class);
                        startActivity(i);
                        finish();
                }else {

                        if (!apptype.equalsIgnoreCase("Branded")) {
                            i = new Intent(HomeScreenMulti.this, HomeScreen.class);
                            startActivity(i);
                        }
                    }
                break;
        }
    }
}
