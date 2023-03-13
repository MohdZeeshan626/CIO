package com.webmobi.gecmedia;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.makeramen.roundedimageview.RoundedImageView;
import com.singleevent.sdk.ApiList;
import com.singleevent.sdk.App;
import com.singleevent.sdk.Custom_View.Error_Dialog;
import com.singleevent.sdk.Custom_View.Util;
import com.singleevent.sdk.Custom_View.VolleyErrorLis;
import com.singleevent.sdk.View.LeftActivity.Moodometer;
import com.singleevent.sdk.View.LeftActivity.PollingRoot;
import com.singleevent.sdk.View.LeftActivity.QuizModule.QuizActivity;
import com.singleevent.sdk.View.LeftActivity.facebookModule.FaceBookActivity;
import com.singleevent.sdk.View.LeftActivity.linkedInModule.LinkedInMainActivity;
import com.singleevent.sdk.View.LeftActivity.pollingActivities.PollingActivity;
import com.singleevent.sdk.View.LeftActivity.twitterModule.TwitterSearch;
import com.singleevent.sdk.model.AppDetails;
import com.singleevent.sdk.model.Event;
import com.singleevent.sdk.model.Events;
import com.singleevent.sdk.model.LocalArraylist.Notes;
import com.singleevent.sdk.model.LocalArraylist.Rating;
import com.singleevent.sdk.View.Fragment.Left_Fragment.HomeFragment;
import com.singleevent.sdk.View.LeftActivity.AboutUs;
import com.singleevent.sdk.View.LeftActivity.AgendaRoot;
import com.singleevent.sdk.View.LeftActivity.Attachment;
import com.singleevent.sdk.View.LeftActivity.Attendee;
import com.singleevent.sdk.View.LeftActivity.Contact_Us;
import com.singleevent.sdk.View.LeftActivity.LMainActivity;
import com.singleevent.sdk.View.LeftActivity.LeaderBoardActivity;
import com.singleevent.sdk.View.LeftActivity.LoginActivity;
import com.singleevent.sdk.View.LeftActivity.MapRoot;
import com.singleevent.sdk.View.LeftActivity.MyGallery;
import com.singleevent.sdk.View.LeftActivity.SocialMedaiRoot;
import com.singleevent.sdk.View.LeftActivity.SpeakerRoot;
import com.singleevent.sdk.View.LeftActivity.SponsorRoot;
import com.singleevent.sdk.View.LeftActivity.SurveyRoot;
import com.singleevent.sdk.View.LeftActivity.VideoActivity;
import com.singleevent.sdk.View.LeftActivity.Feeds;
import com.singleevent.sdk.View.RightActivity.AdminPanelActivity;
import com.singleevent.sdk.View.RightActivity.Group_feed;
import com.singleevent.sdk.View.RightActivity.MyProfile;
import com.singleevent.sdk.View.RightActivity.MyChat;
import com.singleevent.sdk.View.RightActivity.MyCompany;
import com.singleevent.sdk.View.RightActivity.MyMeeting;
import com.singleevent.sdk.View.RightActivity.MyQrCodeGeneratorActivity;
import com.singleevent.sdk.View.RightActivity.MyRequest;
import com.singleevent.sdk.View.RightActivity.MySchedule;
import com.singleevent.sdk.View.RightActivity.Note;
import com.singleevent.sdk.View.RightActivity.Notification;
import com.webmobi.gecmedia.Views.RegActivity;
import com.webmobi.gecmedia.Views.fragment.ContactUsFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class SingleEventHome extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, HomeFragment.OnTabSelectedListener, View.OnClickListener {

    private static final String TAG = SingleEventHome.class.getSimpleName();
    private static final int REQUEST_FOR_FEED = 11;
    private static final int REQUEST_FOR_ATTENDEE = 12;
    AppDetails appDetails;
    private NavigationView navigationView, rightnaviagtion, navigationView1;
    private DrawerLayout drawerLayout;
    private float navdpWidth;
    CircleImageView image_view1, image_view2, image_view3, image_view4;
    LinearLayout circlelogos;
    private double margintop;
    double navheight, lwidth, lheight;
    RelativeLayout v1, v2, v3;
    String q_id;
    private FragmentManager fragmentManager;

    private ArrayList<Events> events = new ArrayList<>();
    Events e, e2;
    int recheck;
    int request;
    ImageView rightview;
    TextView login;
    HomeFragment home;
    String baseUrl = "https://webmobi.s3.amazonaws.com/nativeapps/";
    String filename = "app.json";
    File eventDir, jsonFile;
    String dir, dir_prev;
    String regId;
    TextView navHeader;
    RoundedImageView uprofile;
    LinearLayout llpreview;
    ContactUsFragment contactUsFragment;
    String backtitle;
    SharedPreferences spf;
    ArrayList<Event> catelist = new ArrayList<>();
    boolean isRefreshApp = false;
    DisplayMetrics displayMetrics;
    private static final int REQUEST_OAUTH_REQUEST_CODE = 0x1001;
    FitnessOptions fitnessOptions;
    ImageView rightback;
    List<NavigationPojo> navigationPojoList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Paper.init(this);
        setContentView(R.layout.s_main);
        ///Test Crash report


        appDetails = Paper.book().read("Appdetails");
        regId = Paper.book().read("regId");
        dir = getFilesDir() + File.separator + "EventsDownload" + File.separator;
        dir_prev = getFilesDir() + File.separator + "PreviewDownloaded" + File.separator;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));
        spf = getSharedPreferences(com.webmobi.gecmedia.Config.ApiList.LOCALSTORAGE, MODE_PRIVATE);
        rightview = (ImageView) toolbar.findViewById(R.id.showevents);
        login = (TextView) toolbar.findViewById(R.id.login);
        login.setTypeface(Util.regulartypeface(SingleEventHome.this));
        login.setOnClickListener(this);
        rightview.setOnClickListener(this);
        setSupportActionBar(toolbar);
        spf = getSharedPreferences(ApiList.LOCALSTORAGE, MODE_PRIVATE);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        circlelogos = (LinearLayout) findViewById(R.id.circlelogos);
        // navigationView = (NavigationView) findViewById(R.id.navigation);
        rightnaviagtion = (NavigationView) findViewById(R.id.right_navigation_view);
        navigationView.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));
        rightnaviagtion.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));
        navigationView.setItemBackground(makeSelector(Color.parseColor(appDetails.getTheme_selected())));
        rightnaviagtion.setItemBackground(makeSelector(Color.parseColor(appDetails.getTheme_selected())));
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, findViewById(R.id.right_navigation_view));


        displayMetrics = getResources().getDisplayMetrics();

        navdpWidth = getResources().getDimensionPixelSize(R.dimen.abc_action_bar_default_height_material) * 5;
//        margintop = getResources().getDimensionPixelSize(R.dimen.abc_action_bar_default_height_material) + getResources().getDimensionPixelSize(R.dimen.abc_action_bar_default_height_material) * 0.50;
        navheight = navdpWidth * (0.475);
        lwidth = navheight * 0.60;
        // lheight = lwidth * 0.83;
        lheight = lwidth;


        Bundle extras = getIntent().getExtras();
        if (extras == null)
            finish();

        // getting agenda details from fragment
        String check = getIntent().getExtras().getString("Engagement");
        if (!check.equalsIgnoreCase("engagement")) {
            circlelogos.setVisibility(View.GONE);
        }
        try {
            backtitle = getIntent().getExtras().getString("back");
        } catch (Exception e) {
        }

        DrawerLayout.LayoutParams params;
        //left nav
        params = (DrawerLayout.LayoutParams) navigationView.getLayoutParams();
        params.width = (int) navdpWidth;
        navigationView.setLayoutParams(params);
        //right nav

        params = (DrawerLayout.LayoutParams) rightnaviagtion.getLayoutParams();
        params.width = (int) navdpWidth;
        params.setMargins(0, (int) margintop, 0, 0);
        rightnaviagtion.setLayoutParams(params);
        navigationView.setItemIconTintList(null);
        View header = navigationView.getHeaderView(0);
        v1 = (RelativeLayout) header.findViewById(R.id.v1);
        v2 = (RelativeLayout) header.findViewById(R.id.v2);
        v3 = (RelativeLayout) header.findViewById(R.id.v3);
        image_view1 = (CircleImageView) findViewById(R.id.meimage);
        image_view2 = (CircleImageView) findViewById(R.id.weimage);
        image_view3 = (CircleImageView) findViewById(R.id.photoimage);
        image_view4 = (CircleImageView) findViewById(R.id.notifyimage);
        // llpreview = (LinearLayout) header.findViewById(R.id.llpreview);

        // final ImageView coverp = (ImageView) header.findViewById(R.id.coverimage);
        //   final ImageView logo = (ImageView) header.findViewById(R.id.logo);
        ImageView back_img = (ImageView) header.findViewById(R.id.back_img);
        TextView txt_home = (TextView) header.findViewById(R.id.txt_home);
        v1.setVisibility(View.VISIBLE);
        //  v1.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));
        v1.setOnClickListener(this);
        image_view1.setOnClickListener(this);
        image_view2.setOnClickListener(this);
        image_view3.setOnClickListener(this);
        image_view4.setOnClickListener(this);
        ///////Comment for left new UI below code

        ////For left view
        ImageView llogo = (ImageView) header.findViewById(R.id.llogo);
        final ImageView lbannerimage = (ImageView) header.findViewById(R.id.lbannerimage);
        TextView ltitle = (TextView) header.findViewById(R.id.eventtitle);

       /* RelativeLayout.LayoutParams rparams = (RelativeLayout.LayoutParams) v2.getLayoutParams();
        RelativeLayout.LayoutParams lparams = new RelativeLayout.LayoutParams((int) lwidth, (int) lheight);
        rparams.width = (int) navdpWidth;
        rparams.height = (int) navheight;
        v2.setLayoutParams(rparams);
        llogo.setLayoutParams(lparams);*/

        //showing or hiding preview test based on selection of an event
        //  if (Paper.book(appDetails.getAppId()).read("isPreview", false))
        //      llpreview.setVisibility(View.VISIBLE);
        //   else
        //     llpreview.setVisibility(View.GONE);

        //setting cover image
        catelist.add(Paper.book().read("TotalEvent"));

        //  txt_home.setTextColor(Color.parseColor(appDetails.getTheme_color()));
        txt_home.setTypeface(Util.boldtypeface(SingleEventHome.this));
        //back_img.setImageDrawable(Util.setdrawable(SingleEventHome.this, R.drawable.ic_back_arrow, Color.parseColor(appDetails.getTheme_color())));
        txt_home.setText(backtitle);
        txt_home.setOnClickListener(this);

        Glide.with(getApplicationContext())
                .load(appDetails.getAppLoadingImage())
                .asBitmap()
                .placeholder(R.drawable.dback)
                .error(R.drawable.dback)
                .into(lbannerimage);/*new BitmapImageViewTarget(lbannerimage) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        lbannerimage.setImageBitmap(scaleBitmap(resource, (int) 230, (int) lwidth));
                    }
                });*/
        //  coverp.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));


        //setting login

        Glide.with(getApplicationContext())
                .load(appDetails.getAppLogo())
                .asBitmap()
                .placeholder(R.drawable.default_logo)
                .error(R.drawable.default_logo)
                .into(new BitmapImageViewTarget(llogo) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        llogo.setImageBitmap(scaleBitmap(resource, (int) lwidth, (int) lheight));
                    }
                });


        View rightNavHeader = rightnaviagtion.getHeaderView(0);
        navHeader = (TextView) rightNavHeader.findViewById(R.id.tvRHeader);
        rightback = (ImageView) rightNavHeader.findViewById(R.id.rightbck);
        uprofile = (RoundedImageView) rightNavHeader.findViewById(R.id.uprofile);
        rightback.setOnClickListener(this);
        //   GradientDrawable bgShape = (GradientDrawable) navHeader.getBackground();
        // bgShape.setColor(Color.parseColor(appDetails.getTheme_selected()));

        //   TextView title = (TextView) header.findViewById(R.id.eventtitle);
        // TextView loc = (TextView) header.findViewById(R.id.eventlocation);
        // AwesomeText marker=(AwesomeText)header.findViewById(R.id.marker);


        ltitle.setText(appDetails.getAppName());
        // loc.setText(appDetails.getLocation());
        //loc.setSelected(true);
        String s[] = new String[20];
        s = appDetails.getDisable_items();
        if (s.length > 0 && s != null) {
            for (int i = 0; i < s.length; i++) {
                if (s[i].equalsIgnoreCase("leftpanelappname")) {
                    ltitle.setVisibility(View.GONE);
                } else if (s[i].equalsIgnoreCase("leftpanelapplocation")) {
                    //loc.setVisibility(View.GONE);
                    // marker.setVisibility(View.GONE);

                } else if (s[i].equalsIgnoreCase("leftpanelappicon")) {
                    llogo.setVisibility(View.GONE);

                } else {

                }
            }
        }

        getDataForPanel();
        fragmentManager = getSupportFragmentManager();
        events = Paper.book().read("Appevents");
//        events = Paper.book().read("Appevents2"); //static for testing
        e2 = events.get(0);

        request = 30;


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank

                super.onDrawerOpened(drawerView);
            }
        };

        //setting the actionbarToggle to drawer layout
        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessay or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();

        Log.d("check_events", "onCreate: " + e2.getTabsSize());
//        getDataForPanel();
        setleftnavigationitems();
        setrightnavigationitems();

        navigationView.setNavigationItemSelectedListener(this);
        rightnaviagtion.setNavigationItemSelectedListener(rightclicklistener);

        // home view is by default

        home = new HomeFragment();
        FragmentTransaction ft;
        ft = fragmentManager.beginTransaction();
        ft.replace(R.id.frame, home, "home");
        ft.commit();

//        getDataForPanel();


        checkupdate(appDetails.getAppId(), 1);

        //for preview events to force update
       /* if (Paper.book(appDetails.getAppId()).read("isPreview", false)) {

            if (Paper.book().read("ForceUpdateForPreview", false)) {
                forceUpdate(appDetails.getAppId());
            }

        }*/


    }


    @Override
    protected void onStart() {
        super.onStart();
        // checkin();
          /* if (Paper.book(appDetails.getAppId()).read("isPreview",false))
        forceUpdate(appDetails.getAppUrl());*/
        checkupdate(appDetails.getAppId(), 1);
    }

    private void getDataForPanel() {
        try {
            navigationPojoList.clear();
            ArrayList<Events> eventsToDisplay = new ArrayList<>();
            Gson gson = new Gson();
            JSONObject jsonObject = new JSONObject(readJSON());
            JSONArray jsonArray = jsonObject.getJSONArray("events");
            Log.d("jsonarray_check", "getDataForPanel: " + jsonArray.length());
            JSONArray eventslist = jsonObject.getJSONArray("events");
            String eventString = eventslist.getJSONObject(0).toString();
            Events eobj = gson.fromJson(eventString, Events.class);
            eventsToDisplay.add(eobj);
            Paper.book().write("Appevents2", eventsToDisplay);
        } catch (Exception e) {

        }
    }

    public String readJSON() {
        String json = null;
        try {
            // Opening data.json file
            InputStream inputStream = getAssets().open("tabMenu.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            // read values in the byte array
            inputStream.read(buffer);
            inputStream.close();
            // convert byte to string
            json = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return json;
        }
        return json;
    }

    private void checkin() {


        if (Paper.book().read("Islogin", false)) {

            if (appDetails.getInfo_privacy()) {
                FetchPrivateUserData();

            } else
                FetchPublicUSerdata();

        }
    }

    private void setrightnavigationitems() {
        final Menu menu = rightnaviagtion.getMenu();
        rightnaviagtion.setPadding(15, 10, 5, 0);
        // admin panel
        menu.add(0, 0, 0, "Admin Panel");
        menu.getItem(0).setIcon(new IconDrawable(this,
                FontAwesomeIcons.fa_desktop).colorRes(R.color.white));
        if ((Paper.book(appDetails.getAppId()).read("admin_flag", "").equals("admin") ||
                Paper.book(appDetails.getAppId()).read("admin_flag", "").equals("exhibitor"))) {

            menu.getItem(0).setVisible(true);
        } else {
            menu.getItem(0).setVisible(false);
        }
        // adding the Schedule menu
        menu.add(1, 1, 0, "My Schedules");
        menu.getItem(1).setIcon(R.drawable.rschedule);

        // adding the Companies

        menu.add(2, 2, 0, "My Companies");
        menu.getItem(2).setIcon(R.drawable.date);

        // adding the notes


        menu.add(3, 3, 0, "My Notes");
        menu.getItem(3).setIcon(R.drawable.rnote);

        menu.add(4, 4, 0, "My Chats");
        menu.getItem(4).setIcon(R.drawable.rchat);

        // adding push notification

        menu.add(5, 5, 0, "Notifications");
        menu.getItem(5).setIcon(R.drawable.rnotification);

        //adding inbox
        menu.add(6, 6, 0, "Inbox");
        menu.getItem(6).setIcon(new IconDrawable(this, FontAwesomeIcons.fa_inbox).colorRes(R.color.white));

        //adding my meetings
        menu.add(7, 7, 0, "My Meetings");
        menu.getItem(7).setIcon(new IconDrawable(this, FontAwesomeIcons.fa_calendar_check_o).colorRes(R.color.white));

        String[] schedule = appDetails.getAddon_modules();
        if (schedule != null && schedule.length > 0) {
            for (int index = 0; index < schedule.length; index++) {
                if ("schedule".equals(schedule[index])) {

                    menu.getItem(7).setVisible(true);
                    menu.getItem(6).setVisible(true);
                    Paper.book(appDetails.getAppId()).write("isMeetingEnabled", true);
                    break;
                } else {
                    menu.getItem(7).setVisible(false);
                    menu.getItem(6).setVisible(false);
                    Paper.book(appDetails.getAppId()).write("isMeetingEnabled", false);
                }
            }

        } else {
            menu.getItem(7).setVisible(false);
        }
        menu.add(8, 8, 0, "My Badge");
        menu.getItem(8).setIcon(R.drawable.rqrcode);
        menu.add(9, 9, 0, "My Contacts");
        menu.getItem(9).setIcon(R.drawable.rcontacts);

      /*  menu.add(10, 10, 0, "Feed group");
        menu.getItem(10).setIcon(new IconDrawable(this, FontAwesomeIcons.fa_users).colorRes(R.color.white));*/
        menu.add(10, 10, 0, "My Settings");
        menu.getItem(10).setIcon(R.drawable.rsetting);
//        menu.add(11, 11, 0, "Health App");
//        menu.getItem(11).setIcon(new IconDrawable(this, FontAwesomeIcons.fa_heartbeat).colorRes(R.color.white));
        //set font for all rightnavigation menu items
        //applying font style roboto
        for (int i = 0; i < menu.size(); i++) {
            MenuItem mi = menu.getItem(i);
            Util.applyFontToMenuItem(mi, this);
        }

    }
    /// Get Profile api


    // setting the left menu programmatically

    private void setleftnavigationitems() {
        final Menu menu = navigationView.getMenu();
        for (int j = 0; j < e2.getTabs().length; j++) {
            Log.d("check_item_here", "setleftnavigationitems: " + e2.getTabs(j).getTitle());
            if (e2.getTabs(j).getType().toLowerCase().equals("gamification")) {
                Paper.book(appDetails.getAppId()).write("isGamification", true);
                Paper.book(appDetails.getAppId()).write("Gamification", e2.getTabs(j).getCheckvalue());

            }

            menu.add(j, j, 0, e2.getTabs(j).getTitle());
            String iconname = "fa_" + e2.getTabs(j).getIconCls().replace("-", "_");
            menu.getItem(j).setIcon(new IconDrawable(this, FontAwesomeIcons.valueOf(iconname)).colorRes(R.color.white));
            menu.getItem(j).setCheckable(true);
        }
//        for (int j = 0; j < navigationPojoList.size(); j++) {
//            Log.d("check_item_here", "setleftnavigationitems: " + navigationPojoList.get(j).getTitle());
//            if (navigationPojoList.get(j).getType().toLowerCase().equals("gamification")) {
//
//            }
//            menu.add(j, j, 0, navigationPojoList.get(j).getTitle());
//            menu.getItem(j).setCheckable(true);
//        }

        hideleftnaviagtionitem();
    }


    //hiding the left items based on itemsize and type value is present


    private void hideleftnaviagtionitem() {

        final Menu menu = navigationView.getMenu();

        int size = menu.size();
        menu.add(size, size, 0, "Refresh App");
        menu.getItem(size).setIcon(new IconDrawable(this, FontAwesomeIcons.fa_refresh).colorRes(R.color.white));
        menu.getItem(size).setCheckable(true);


        for (int i = 0; i < menu.size() - 1; i++) {
            Log.d("check_menu", "hideleftnaviagtionitem: " + menu.size());

            MenuItem subMenuItem = menu.getItem(i);
            Log.d("submenu", "hideleftnaviagtionitem: " + subMenuItem.toString());
            Util.applyFontToMenuItem(subMenuItem, SingleEventHome.this);

            if (getMenuId(e2.getTabs(i).getType()) == 0) {
                menu.getItem(i).setVisible(false);
            } else {
                if (e2.getTabs(i).getMod_display() != null) {
                    if (e2.getTabs(i).getMod_display().equalsIgnoreCase("webmobi") || e2.getTabs(i).getMod_display().equalsIgnoreCase("mobile")) {
                        if (!e2.getTabs(i).getSub_type().equalsIgnoreCase("agenda")) {
                            menu.getItem(i).setVisible(true);
                        } else {
                            menu.getItem(i).setVisible(false);
                        }
                    } else
                        menu.getItem(i).setVisible(false);

                }
            }


        }

        MenuItem subMenuItem = menu.getItem(menu.size() - 1);
        Util.applyFontToMenuItem(subMenuItem, SingleEventHome.this);
    }


    //modules supported

    private int getMenuId(String type) {
        switch (type) {
            case "Mybadge":
                return 1;
            case "webmobieventapp":
                return 1;
            case "home":
                return 1;
            case "survey":
                return 1;
            case "moodometer":
                return 1;
            case "polling":
                return 1;
            case "Polling2":  //changed
                return 1;
            case "speakersData":
                return 1;
            case "agenda":
                return 1;
            case "exhibitorsData":
                return 1;
            case "socialmedia":
                return 1;
            case "map":
                return 1;
            case "aboutus":
                return 1;
            case "gallery":
                return 1;
            case "contactUs":
                return 1;
            case "rssfeeds":
                return 1;
            case "pdf":
                return 1;
            case "eventslist":
                return 1;
            case "weather":
                return 1;
            case "currency":
                return 1;
            case "sponsorsData":
                return 1;
            case "myInfo":
                return 1;
            case "video":
                return 1;
            case "attendee":
                return 1;
            case "feeds":
                return 1;
            case "gamification":
                return 1;
            case "groupfeed":
                return 1;
            case "Quiz":   //changed
                return 1;
            case "FaceBook":   //changed
                return 1;
            case "Twitter":  //changed
                return 1;
            case "LinkedIn":  //changed
                return 1;
            default:
                return 0;

        }
    }


    //checking the items size of each module if its less then or equal to zero hiding the items

    private boolean checkitemsize(String type, int pos) {
        switch (type) {
            case "Quiz":   //changed
                return e2.getTabs(pos).getItemsSize() > 0;
            case "FaceBook":   //changed
                return e2.getTabs(pos).getItemsSize() > 0;
            case "Twitter":   //changed
                return e2.getTabs(pos).getItemsSize() > 0;
            case "LinkedIn":  //changed
                return e2.getTabs(pos).getItemsSize() > 0;
            case "survey":
                return e2.getTabs(pos).getItemsSize() > 0;

            case "moodometer":
                return e2.getTabs(pos).getItemsSize() > 0;

            case "polling":
                return e2.getTabs(pos).getItemsSize() > 0;

            case "Polling2":  //changed
                return e2.getTabs(pos).getItemsSize() > 0;

            case "speakersData":
                return e2.getTabs(pos).getItemsSize() > 0;

            case "agenda":
                return e2.getTabs(pos).getAgendaSize() > 0;

            case "exhibitorsData":
                return e2.getTabs(pos).getItemsSize() > 0;

            case "socialmedia":
                return e2.getTabs(pos).getItemsSize() > 0;

            case "map":
                return true;

            case "aboutus":
                return true;
            case "groupfeed":
                return true;
            case "gallery":
                return true;

            case "contactUs":
                return true;

            case "rssfeeds":
                return true;

            case "pdf":
                return e2.getTabs(pos).getpdfSize() > 0;


            case "home":
                return true;

            case "eventslist":
                return e2.getTabs(pos).getItemsSize() > 0;

            case "weather":
                return true;

            case "attendee":
                return true;


            case "currency":
                return true;


            case "sponsorsData":
                return e2.getTabs(pos).getItemsSize() > 0;

            case "myInfo":
                return e2.getTabs(pos).getItemsSize() > 0;

            case "video":
                return true;

            case "Mybadge":
                return true;

            case "webmobieventapp":
                return true;
            case "feeds":
                return true;

            case "gamification":
                return true;
            default:
                return false;

        }

    }


    public static StateListDrawable makeSelector(int color) {
        StateListDrawable res = new StateListDrawable();
        res.setExitFadeDuration(400);
        res.setAlpha(15);
        res.addState(new int[]{android.R.attr.state_checked}, new ColorDrawable(color));
        res.addState(new int[]{-android.R.attr.state_checked}, new ColorDrawable(Color.TRANSPARENT));
        return res;
    }

    public static Bitmap scaleBitmap(Bitmap bitmap, int newWidth, int newHeight) {
        Bitmap scaledBitmap = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888);

        float scaleX = newWidth / (float) bitmap.getWidth();
        float scaleY = newHeight / (float) bitmap.getHeight();
        float pivotX = 0;
        float pivotY = 0;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(scaleX, scaleY, pivotX, pivotY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bitmap, 0, 0, new Paint(Paint.FILTER_BITMAP_FLAG));

        return scaledBitmap;
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        if (appDetails.getInfo_privacy() ? Paper.book().read("Islogin", false) : true) {
            Boolean b = Paper.book().read("Islogin", false);
            if (b) {

                Intent i;
                item.setChecked(true);

                Bundle args = new Bundle();
                drawerLayout.closeDrawers();
                FragmentTransaction fc;

                Menu menu = navigationView.getMenu();

                int lastitem = menu.size() - 1;

                String itemname;
                if (lastitem == item.getItemId()) {
                    itemname = "RefreshApp";
                } else {
                    itemname = e2.getTabs(item.getItemId()).getType();
                }
                Intent intent;

                switch (itemname) {

                    case "home":

                        if (home == null)
                            home = new HomeFragment();
                        fc = fragmentManager.beginTransaction();
                        fc.replace(R.id.frame, home, "home");
                        fc.addToBackStack(null);
                        fc.commit();

                        break;

                    case "aboutus":

                        intent = new Intent(SingleEventHome.this, AboutUs.class);
                        intent.putExtra("pos", item.getItemId());
                        intent.putExtra("title", item.getTitle().toString());
                        startActivity(intent);

                        break;
                    case "groupfeed":

                        intent = new Intent(SingleEventHome.this, Group_feed.class);
                        startActivity(intent);

                        break;
                    case "gallery":

                        intent = new Intent(SingleEventHome.this, MyGallery.class);
                        intent.putExtra("pos", item.getItemId());
                        intent.putExtra("title", item.getTitle().toString());
                        startActivity(intent);
                        break;


                    case "agenda":

                        if (checkitemsize(e2.getTabs(item.getItemId()).getType(), item.getItemId())) {
                            intent = new Intent(SingleEventHome.this, AgendaRoot.class);
                            intent.putExtra("pos", item.getItemId());
                            intent.putExtra("title", item.getTitle().toString());
                            startActivity(intent);
                        } else
                            Error_Dialog.show("No details", SingleEventHome.this);
                        break;

                    case "speakersData":
                        if (checkitemsize(e2.getTabs(item.getItemId()).getType(), item.getItemId())) {
                            intent = new Intent(SingleEventHome.this, SpeakerRoot.class);
                            intent.putExtra("pos", item.getItemId());
                            intent.putExtra("title", item.getTitle().toString());
                            startActivity(intent);
                        } else
                            Error_Dialog.show("No details", SingleEventHome.this);

                        break;

                    case "sponsorsData":
                        if (checkitemsize(e2.getTabs(item.getItemId()).getType(), item.getItemId())) {
                            Paper.book(appDetails.getAppId()).write("action", "Sponsor");
                            intent = new Intent(SingleEventHome.this, SponsorRoot.class);
                            intent.putExtra("pos", item.getItemId());
                            intent.putExtra("title", item.getTitle().toString());
                            startActivity(intent);
                        } else
                            Error_Dialog.show("No details", SingleEventHome.this);

                        break;

                    case "myInfo":
                        intent = new Intent(SingleEventHome.this, MyQrCodeGeneratorActivity.class);
                        startActivity(intent);
                        break;

                    case "exhibitorsData":
                        if (checkitemsize(e2.getTabs(item.getItemId()).getType(), item.getItemId())) {
                            Paper.book(appDetails.getAppId()).write("action", "Exhibitor");
                            intent = new Intent(SingleEventHome.this, SponsorRoot.class);
                            intent.putExtra("pos", item.getItemId());
                            intent.putExtra("title", item.getTitle().toString());
                            startActivity(intent);
                        } else
                            Error_Dialog.show("No details", SingleEventHome.this);
                        break;
                    case "survey":
                        if (Paper.book().read("Islogin", false)) {
                            int p = item.getItemId();
                            String title = item.getTitle().toString();
                            String check = checkSurvey(p, e2.getTabs(item.getItemId()).getType(), title);

                        } else {
                            Error_Dialog.show("Please Login", SingleEventHome.this);
                        }


                        break;
                    case "moodometer":
                        if (Paper.book().read("Islogin", false)) {
                            int p = item.getItemId();
                            String title = item.getTitle().toString();
                            System.out.print("p & type" + p + " " + e2.getTabs(item.getItemId()).getType());
                            String check = checkMood(p, e2.getTabs(item.getItemId()).getType());
                        } else {
                            Error_Dialog.show("Please Login", SingleEventHome.this);
                        }


                        break;

                    case "polling":
                        if (Paper.book().read("Islogin", false)) {
                            if (checkitemsize(e2.getTabs(item.getItemId()).getType(), item.getItemId())) {
                                intent = new Intent(SingleEventHome.this, PollingRoot.class);
                                intent.putExtra("pos", item.getItemId());
                                intent.putExtra("title", item.getTitle().toString());
                                intent.putExtra("polltype", "global");
                                startActivity(intent);
                            } else
                                Error_Dialog.show("No details", SingleEventHome.this);
                        } else {
                            Error_Dialog.show("Please Login", SingleEventHome.this);
                            intent = new Intent(this, LoginActivity.class);
                            startActivityForResult(intent, 1);

                        }
                        break;
                    case "Polling2":  //changed
//                        if (Paper.book().read("Islogin", false)) {
                        intent = new Intent(SingleEventHome.this, PollingActivity.class);
                        startActivity(intent);

//                        } else {
//                            Error_Dialog.show("Please Login", SingleEventHome.this);
//                            intent = new Intent(this, LoginActivityDemo.class);
//                            startActivityForResult(intent, 1);
//
//                        }
                        break;
                    case "socialmedia":
                        if (checkitemsize(e2.getTabs(item.getItemId()).getType(), item.getItemId())) {
                            intent = new Intent(SingleEventHome.this, SocialMedaiRoot.class);
                            intent.putExtra("pos", item.getItemId());
                            intent.putExtra("title", item.getTitle().toString());
                            startActivity(intent);
                        } else
                            Error_Dialog.show("No details", SingleEventHome.this);

                        break;

                    case "pdf":
                        if (checkitemsize(e2.getTabs(item.getItemId()).getType(), item.getItemId())) {
                            intent = new Intent(SingleEventHome.this, Attachment.class);
                            intent.putExtra("pos", item.getItemId());
                            intent.putExtra("title", item.getTitle().toString());
                            startActivity(intent);
                        } else
                            Error_Dialog.show("No details", SingleEventHome.this);

                        break;

                    case "video":

                        intent = new Intent(SingleEventHome.this, VideoActivity.class);
                        intent.putExtra("pos", item.getItemId());
                        intent.putExtra("title", item.getTitle().toString());
                        startActivity(intent);
                        break;


                    case "map":
                        intent = new Intent(SingleEventHome.this, MapRoot.class);
                        intent.putExtra("pos", item.getItemId());
                        intent.putExtra("title", item.getTitle().toString());
                        startActivity(intent);
                        break;

                    case "contactUs":
                        intent = new Intent(SingleEventHome.this, Contact_Us.class);
                        intent.putExtra("pos", item.getItemId());
                        intent.putExtra("title", item.getTitle().toString());
                        startActivity(intent);
                        break;

                    case "attendee":
                        if (Paper.book().read("Islogin", false)) {
                            intent = new Intent(SingleEventHome.this, Attendee.class);
                            intent.putExtra("pos", item.getItemId());
                            intent.putExtra("title", item.getTitle().toString());
                            startActivity(intent);
                        } else {
                            i = new Intent(SingleEventHome.this, LoginActivity.class);
                            i.setAction(com.webmobi.gecmedia.Config.ApiList.loginaction);
                            startActivityForResult(i, 1);
                        }

                        break;

                    case "feeds":
                        if (Paper.book().read("Islogin", false)) {
//                            intent = new Intent(SingleEventHome.this, FeedActivity.class);
                            intent = new Intent(SingleEventHome.this, Feeds.class);
                            startActivity(intent);

                        } else {
                            i = new Intent(SingleEventHome.this, LoginActivity.class);
                            i.setAction(com.webmobi.gecmedia.Config.ApiList.loginaction);
                            startActivityForResult(i, 1);
                        }

                        break;
                    case "gamification":

                        if (Paper.book().read("Islogin", false)) {
                            intent = new Intent(SingleEventHome.this, LeaderBoardActivity.class);
                            startActivity(intent);
                        } else {
                            i = new Intent(SingleEventHome.this, LoginActivity.class);
                            i.setAction(com.webmobi.gecmedia.Config.ApiList.loginaction);
                            startActivityForResult(i, 1);
                        }

                        break;
                    case "Quiz":  //changed
                        intent = new Intent(SingleEventHome.this, QuizActivity.class);
                        intent.putExtra("pos", item.getItemId());
                        intent.putExtra("title", item.getTitle().toString());
                        intent.putExtra("polltype", "global");
                        startActivity(intent);
                        break;
                    case "Twitter":  //changed
                        intent = new Intent(SingleEventHome.this, TwitterSearch.class);
                        intent.putExtra("pos", item.getItemId());
                        startActivity(intent);
                        break;
                    case "FaceBook":  //changed
                        startActivity(new Intent(SingleEventHome.this, FaceBookActivity.class));
                        break;
                    case "LinkedIn":  //changed
                        intent = new Intent(SingleEventHome.this, LinkedInMainActivity.class);
                        intent.putExtra("pos", item.getItemId());
                        startActivity(intent);
                        break;
                    case "RefreshApp":
                        checkupdate(appDetails.getAppId(), 0);
                        break;


                }
            } else {
                Intent intent = new Intent(SingleEventHome.this, RegActivity.class);
                startActivity(intent);
                finishAffinity();
            }
        } else
            Error_Dialog.show("Please Login or Contact Administrator", SingleEventHome.this);

        return true;
    }


    //interface method to know the tab item clicked

    @Override
    public void onTabSelected(int position) {
        navigationView.getMenu().performIdentifierAction(position, 0);

    }

    @Override
    public MenuItem getMenuItem(int menuId) {
        return navigationView.getMenu().findItem(menuId);
    }

    @Override
    public void setActiveMenu(int menuId) {


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login:
                Intent i = new Intent(SingleEventHome.this, RegActivity.class);
                i.setAction(com.webmobi.gecmedia.Config.ApiList.loginaction);
                startActivityForResult(i, 1);
                break;
            case R.id.meimage:
                if (Paper.book().read("Islogin", false)) {
                    Intent me = new Intent(SingleEventHome.this, MyProfile.class);
                    startActivity(me);
                } else {
                    Error_Dialog.show("please Login", SingleEventHome.this);
                }
                break;
            case R.id.photoimage:
                if (Paper.book().read("Islogin", false)) {
                    Intent p = new Intent(SingleEventHome.this, MyGallery.class);
                    p.putExtra("title", "My Gallery");
                    startActivity(p);
                } else {
                    Error_Dialog.show("please Login", SingleEventHome.this);
                }
                break;
            case R.id.notifyimage:
                if (Paper.book().read("Islogin", false)) {
                    Intent no = new Intent(SingleEventHome.this, Notification.class);
                    startActivity(no);
                } else {
                    Error_Dialog.show("please Login", SingleEventHome.this);
                }
                break;
            case R.id.weimage:
                if (Paper.book().read("Islogin", false)) {
                    Intent we = new Intent(SingleEventHome.this, LMainActivity.class);
                    startActivity(we);
                } else {
                    Error_Dialog.show("please Login", SingleEventHome.this);
                }
                break;
            case R.id.showevents:
                if (drawerLayout.isDrawerOpen(navigationView)) {
                    drawerLayout.closeDrawer(navigationView);
                }
                if (drawerLayout.isDrawerOpen(rightnaviagtion)) {
                    drawerLayout.closeDrawer(rightnaviagtion);
                } else
                    //    Getprofile();
                    drawerLayout.openDrawer(rightnaviagtion);
                break;
            case R.id.rightbck:

                if (drawerLayout.isDrawerOpen(rightnaviagtion)) {
                    drawerLayout.closeDrawer(rightnaviagtion);
                }
                break;
            case R.id.v1:
                Paper.book().delete("admin_flag");
                finish();
                break;
        }
    }


    public String checkSurvey(int p, String type, String title) {
        if (e2.getTabs(p).getItemsSize() > 0) {
            q_id = "ch";
            checkSurveyfeedback(p, type, title);
        } else {
            Error_Dialog.show("Information not available", SingleEventHome.this);
        }
        return q_id;
    }

    private void checkSurveyfeedback(final int p, final String type, final String title) {


        final ProgressDialog dialog = new ProgressDialog(SingleEventHome.this, com.singleevent.sdk.R.style.MyAlertDialogStyle);
        dialog.setMessage("Checking Survey...");
        dialog.show();
        String tag_string_req = "Schdule";
        String url = ApiList.SurveyFeedback;
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                try {
                    System.out.println(response);
                    JSONObject jObj = new JSONObject(response);


                    if (jObj.getBoolean("response")) {
                        //storing all answers offline to send when internet is available
                        int survey_flag = jObj.getInt("survey_flag");
                        if (checkitemsize(type, p)) {
                            int checkSurvey = Paper.book().read("survey_flag", 2);
                            if (survey_flag == 0) {
                                Intent intent = new Intent(SingleEventHome.this, SurveyRoot.class);
                                intent.putExtra("pos", p);
                                intent.putExtra("title", title);
                                startActivity(intent);
                            } else if (survey_flag == 1) {
                                Error_Dialog.show("You have already submitted the Survey.", SingleEventHome.this);
                            } else {
                                // Error_Dialog.show("Please Login", MainActivity.this);
                                Error_Dialog.show("No details.", SingleEventHome.this);
                            }

                        } else
                            Error_Dialog.show("No details.", SingleEventHome.this);

                    } else {
                        if (jObj.getString("responseString").equalsIgnoreCase("Invalid token")) {
                            Error_Dialog.show("Session is Expired Please Login", SingleEventHome.this);
                        } else
                            Error_Dialog.show(jObj.getString("responseString"), SingleEventHome.this);
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
                    Error_Dialog.show("Timeout", SingleEventHome.this);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, SingleEventHome.this), SingleEventHome.this);
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show(" Thank you. Survey has been saved successfully. ", SingleEventHome.this);


                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userid", Paper.book().read("userId", ""));
                params.put("username", Paper.book().read("username", ""));
                params.put("email", Paper.book().read("Email", ""));
                params.put("appId", appDetails.getAppId());
                params.put("flag", "check");
                params.put("default_id", e2.getTabs(p).getCheckvalue());
                params.put("feedback", "");
                params.put("submissiondate", "");
                params.put("eventdate", "");
                params.put("agenda_id", "");
                params.put("survey_type", e2.getTabs(p).getSub_type());
                System.out.println(params);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("token", Paper.book().read("token", ""));
                return headers;
            }
        };


        strReq.setShouldCache(false);
        App.getInstance().addToRequestQueue(strReq, tag_string_req);
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

    }


    @Override
    protected void onResume() {
        super.onResume();
        settingheader();
       /* if (Paper.book().read("Islogin", false))
            if (!Paper.book(appDetails.getAppId()).read("registered", false))
                registeruser();*/
        //    checkupdate(appDetails.getAppId(),1);
    }

    private void settingheader() {

        boolean islogin = Paper.book().read("Islogin", false);
        String firstWord = Paper.book().read("username");
        if (islogin) {
            rightview.setVisibility(View.VISIBLE);
            login.setVisibility(View.GONE);
            if (firstWord.contains(" ")) {
                firstWord = firstWord.substring(0, firstWord.indexOf(" "));
                System.out.println(firstWord);

            }
            navHeader.setText("Hi " + firstWord);


            String profile = Paper.book().read("ProfilePIC", "");
            if (profile != null && !profile.equalsIgnoreCase("")) {
                Glide.with(getApplicationContext())
                        .load(profile)
                        .asBitmap()
                        .placeholder(R.drawable.default_user)
                        .error(R.drawable.default_user)
                        .into(uprofile);
                uprofile.setCornerRadius(12, 12, 12, 12);
            }

        } else {
            rightview.setVisibility(View.GONE);
            login.setVisibility(View.VISIBLE);

        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            settingheader();
            checkin();
            /*setleftnavigationitems();
            setrightnavigationitems();*/

        } else if (requestCode == REQUEST_FOR_FEED && resultCode == RESULT_OK) {
            Intent intent = new Intent(this, Feeds.class);
            startActivity(intent);
        }
    }

    private void replaceFragment(Fragment fragment, String popularfragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        androidx.fragment.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_container, fragment, popularfragment);
        transaction.commit();
    }

    // right click listnere


    NavigationView.OnNavigationItemSelectedListener rightclicklistener = new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            drawerLayout.closeDrawers();
            String title = (String) item.getTitle().toString();
            Intent i;

            switch (title) {
                case "My Schedules":
                    i = new Intent(SingleEventHome.this, MySchedule.class);
                    startActivity(i);
                    break;

                case "My Companies":
                    i = new Intent(SingleEventHome.this, MyCompany.class);
                    startActivity(i);
                    break;

                case "My Products":
                    i = new Intent(SingleEventHome.this, MyCompany.class);
                    startActivity(i);
                    break;

                case "My Notes":
                    i = new Intent(SingleEventHome.this, Note.class);
                    startActivity(i);
                    break;
                case "My Chats":
                    i = new Intent(SingleEventHome.this, MyChat.class);
                    startActivity(i);
                    break;

                case "Notifications":
                    i = new Intent(SingleEventHome.this, Notification.class);
                    startActivity(i);
                    break;

                case "Inbox":
                    i = new Intent(SingleEventHome.this, MyRequest.class);
                    startActivity(i);
                    break;

                case "My Meetings":
                    i = new Intent(SingleEventHome.this, MyMeeting.class);
                    startActivity(i);
                    break;

                case "Admin Panel":

                    i = new Intent(SingleEventHome.this, AdminPanelActivity.class);
                    i.putExtra("USER_IS", "Admin Panel");
                    startActivity(i);
                    break;
                case "My Contacts":
                   /* i = new Intent(MainActivity.this, ContactUsFragment.class);
                    startActivity(i);
*/
                    if (Paper.book().read("Islogin", false)) {
                        setContentView(R.layout.lactivity_main3);
                        if (contactUsFragment == null)

                            contactUsFragment = new ContactUsFragment();
                        replaceFragment(contactUsFragment, "contactusfragment");
                        //  navigationView.setVisibility(View.GONE);


                        //main_linear.setVisibility(View.GONE);

                        //ll_toolbar_content.setVisibility(View.GONE);
                        // textViewToolBar.setVisibility(View.VISIBLE);
         /*case "Feed group":
                    i = new Intent(SingleEventHome.this, Group_feed.class);
                    startActivity(i);
                    break;*/               //textViewToolBar.setText((this, new SpannableString("Contacts")));
                        return true;
                    }
                    break;
                case "My Badge":
                    i = new Intent(SingleEventHome.this, MyQrCodeGeneratorActivity.class);
                    startActivity(i);
                    break;

                case "My Settings":
                    if (Paper.book().read("Islogin", false)) {
                        i = new Intent(SingleEventHome.this, MyProfile.class);
                        startActivity(i);
                    } else {
                        Error_Dialog.show("Please Login", SingleEventHome.this);
                    }
                    break;
//                case "Health App":
//                    if (Paper.book().read("Islogin", false)) {
//                        i = new Intent(SingleEventHome.this, ChallengeActivity.class);
//                        startActivity(i);
//                    } else {
//                        Error_Dialog.show("Please Login", SingleEventHome.this);
//                    }
//                    break;

            }
            return true;
        }
    };


    //Adding code for Health app

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(navigationView))
            drawerLayout.closeDrawers();
        else if (drawerLayout.isDrawerOpen(rightnaviagtion))
            drawerLayout.closeDrawers();
        else
            Paper.book().delete("admin_flag");
        super.onBackPressed();
    }


    // checking update

    private void forceUpdate(String appurl) {
        String query = "";
        try {
            query = URLEncoder.encode(appDetails.getAppUrl(), "utf-8");
            String jsonUrl = baseUrl + query + "/appData.json";  //temp/appData.json
            loadPrevjson(jsonUrl);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String jsonUrl = "";
    }

    private void checkupdate(String appId, int n) {

        String tag_string_req = "Checking Update";
        String url = ApiList.CheckUpdate + "appId=" + appId;
        System.out.println(url);
        final ProgressDialog pDialog = new ProgressDialog(SingleEventHome.this, R.style.MyAlertDialogStyle);
        pDialog.setMessage("Loading...");
        pDialog.show();

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    pDialog.dismiss();
                    System.out.println("JSON Response " + response);

                    if (response.getInt("responseString") > appDetails.getVersion()) {
                        String query = "";
                        try {
                            query = URLEncoder.encode(appDetails.getAppUrl(), "utf-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        String jsonUrl = baseUrl + query + "/appData.json";
                        // String jsonUrl = baseUrl + appDetails.getAppUrl() + "/temp/appData.json";
                        appDetails.setVersion(response.getInt("responseString"));
                        loadjson(jsonUrl, n);

                    } else {
                        if (n == 0)
                            Error_Dialog.show("No Update Available", SingleEventHome.this);
                    }
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }

                   /* if (response.getBoolean("response")) {
                        String query = "";
                        String jsonUrl = "";
                        try {
                            query = URLEncoder.encode(appDetails.getAppUrl(), "utf-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }

                        if (Paper.book(appDetails.getAppId()).read("isPreview", false)) {
                            if (Paper.book(appDetails.getAppId()).read("isPreview", false)) {
                                jsonUrl = baseUrl + query + "/temp/appData.json";
                                loadPrevjson(jsonUrl);
                            }
                        } else*//* if (response.getInt("responseString") > appDetails.getVersion())*//* {

                            jsonUrl = baseUrl + query + "/appData.json";
                            loadjson(jsonUrl);

                        }
                    }*/
            }/* catch (JSONException e1) {
                    e1.printStackTrace();
                }
*/

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                if (error instanceof TimeoutError) {
                    Error_Dialog.show("Timeout", SingleEventHome.this);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, SingleEventHome.this), SingleEventHome.this);
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show("Please Check Your Internet Connection", SingleEventHome.this);
                }

            }
        });

        App.getInstance().addToRequestQueue(jsonRequest, tag_string_req);
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
    }


    // loading updated json

    void loadjson(String jsonUrl, int n) {
        final ProgressDialog pDialog = new ProgressDialog(SingleEventHome.this, R.style.MyAlertDialogStyle);
        pDialog.setMessage("Updating ...");
        pDialog.show();
        String tag_string_req = "Updating";
        System.out.println("Url " + jsonUrl);
        StringRequest jsonRequest = new StringRequest(Request.Method.GET, jsonUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialog.dismiss();
                JSONObject res = null;
                try {
                    res = new JSONObject(response);
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                String jsonResponse = response;
                System.out.println("JSON Response " + jsonResponse);
                try {
                    eventDir = new File(dir + appDetails.getAppId());
                    jsonFile = new File(eventDir, filename);
                    jsonFile.delete();
                    Files.write(jsonResponse, jsonFile, Charset.defaultCharset());

                    showjs(jsonFile, n);

                   /* if (res.getInt("version") != appDetails.getVersion()) {

                    } else {
                        Error_Dialog.show("No Updates Available", SingleEventHome.this);
                    }*/
                } catch (IOException e1) {

                    e1.printStackTrace();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                if (error instanceof TimeoutError) {
                    Error_Dialog.show("Timeout", SingleEventHome.this);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, SingleEventHome.this), SingleEventHome.this);
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show("Please Check Your Internet Connection", SingleEventHome.this);
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
        ;

        App.getInstance().addToRequestQueue(jsonRequest, tag_string_req);
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

    }

    void loadPrevjson(String jsonUrl) {
        final ProgressDialog pDialog = new ProgressDialog(SingleEventHome.this, R.style.MyAlertDialogStyle);
        pDialog.setMessage("Updating ...");
        pDialog.show();
        String tag_string_req = "Updating";
        StringRequest jsonRequest = new StringRequest(Request.Method.GET, jsonUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialog.dismiss();
                JSONObject res = null;
                try {
                    res = new JSONObject(response);
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                String jsonResponse = response;
                Log.d("jsonresponsetabs", "onResponse: " + jsonResponse);
                System.out.println("JSON Response " + jsonResponse);
                try {
                  /*  if (res.getInt("version") != appDetails.getVersion()) {

                    } else {
                        Error_Dialog.show("No Updates Available", SingleEventHome.this);
                    }*/
                    eventDir = new File(dir_prev + appDetails.getAppId());
                    jsonFile = new File(eventDir, filename);
                    jsonFile.delete();
                    Files.write(jsonResponse, jsonFile, Charset.defaultCharset());

                    Paper.book().write("ForceUpdateForPreview", false);
                    showjs(jsonFile, 0);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                if (error instanceof TimeoutError) {
                    Error_Dialog.show("Timeout", SingleEventHome.this);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, SingleEventHome.this), SingleEventHome.this);
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show("Please Check Your Internet Connection", SingleEventHome.this);
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
        ;

        App.getInstance().addToRequestQueue(jsonRequest, tag_string_req);
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

    }

    // updating the existing value


    private void showjs(File jsonFile, int n) throws IOException {
        String contents = Files.toString(jsonFile, Charset.defaultCharset());
        System.out.println("File Contents : " + contents);
        ArrayList<Events> eventsToDisplay = new ArrayList<>();
        Gson gson = new Gson();


        try {
            JSONObject args;
            args = new JSONObject(contents);
            AppDetails obj = gson.fromJson(args.toString(), AppDetails.class);
            if (n == 0) {

                Paper.book().write("Appdetails", obj);
                JSONArray eventslist = args.getJSONArray("events");
                String eventString = eventslist.getJSONObject(0).toString();
                Events eobj = gson.fromJson(eventString, Events.class);
                eventsToDisplay.add(eobj);
                Paper.book().write("Appevents", eventsToDisplay);
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            } else {
                if (obj.getForceUpdate()) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(SingleEventHome.this, R.style.MyAlertDialogStyle);
                    builder.setTitle("Update is Available");
                    //  builder.setMessage("Its better to update now");
                    builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Click button action
                            //  SingleEventHome.this.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+SingleEventHome.this.getPackageName())));
                            try {
                                Paper.book().write("Appdetails", obj);
                                JSONArray eventslist = args.getJSONArray("events");
                                String eventString = eventslist.getJSONObject(0).toString();
                                Events eobj = gson.fromJson(eventString, Events.class);
                                eventsToDisplay.add(eobj);
                                Paper.book().write("Appevents", eventsToDisplay);
                                Error_Dialog.show("Updated Successfully", SingleEventHome.this);
                                /*Intent intent = getIntent();
                               // finish();
                                startActivity(intent);*/
                            } catch (JSONException ex) {
                                ex.printStackTrace();
                            }
                            dialog.dismiss();

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
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    private void FetchPrivateUserData() {

        final ProgressDialog dialog = new ProgressDialog(SingleEventHome.this, R.style.MyAlertDialogStyle);
        dialog.setMessage("Loading Details ...");
        dialog.show();


        String tag_string_req = "Login";
        String url = ApiList.Login_User;
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {

                try {
                    System.out.println(response);
                    JSONObject jObj = new JSONObject(response);


                    if (jObj.getBoolean("response")) {
                        parseresult(jObj.getJSONObject("responseString"));
                        dialog.dismiss();
                    } else {
                        dialog.dismiss();
                        Error_Dialog.show(jObj.getString("responseString"), SingleEventHome.this);
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
                    Error_Dialog.show("Timeout", SingleEventHome.this);
                } else if (com.webmobi.gecmedia.CustomViews.VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(com.webmobi.gecmedia.CustomViews.VolleyErrorLis.handleServerError(error, SingleEventHome.this), SingleEventHome.this);
                } else if (com.webmobi.gecmedia.CustomViews.VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show("Please Check Your Internet Connection", SingleEventHome.this);
                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("deviceType", "android");
                params.put("deviceId", regId);
                params.put("appid", appDetails.getAppId());
                params.put("userid_flag", String.valueOf(!Patterns.EMAIL_ADDRESS.matcher(Paper.book().read("Email", "")).matches()));
                params.put("info_privacy", String.valueOf(appDetails.getInfo_privacy()));
                System.out.println(params);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                // add headers <key,value>
                String credentials = Paper.book().read("Email", "") + ":" + Paper.book(appDetails.getAppId()).read("PrivateKey", "");
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

    private void parseresult(JSONObject details) throws JSONException {
        System.out.println(details);
        Paper.book(appDetails.getAppId()).write("IsFirstTime", false);
        if (!details.isNull("profile_pic")) {
            Paper.book().write("profile_pic", details.getString("profile_pic"));
        }

        Paper.book(appDetails.getAppId()).write("token", details.getJSONObject("token").getString("token"));
        Paper.book().write("userId", details.getJSONObject("token").getString("userId"));
        Paper.book(appDetails.getAppId()).write("userId", details.getJSONObject("token").getString("userId"));
        Paper.book().write("username", details.getJSONObject("token").getString("username"));
        Paper.book().write("email", Paper.book().read("Email"));

        Paper.book(appDetails.getAppId()).write("admin_flag", details.getString("admin_flag"));


        int checkSurvey = Paper.book(appDetails.getAppId()).read("survey_flag", 0);
        if (checkSurvey != 1)
            Paper.book(appDetails.getAppId()).write("survey_flag", Integer.parseInt(details.getString("survey_flag")));

        SharedPreferences.Editor editor = spf.edit();
        editor.putString("username", Paper.book().read("username").toString());
        editor.putString("user_id", Paper.book().read("userId").toString());
        editor.apply();
        // adding the sch list
        List<Integer> list = new ArrayList<>();


        JSONArray jsonArr = new JSONArray(details.getString("schedules"));

        for (int i = 0; i < jsonArr.length(); i++)
            list.add(jsonArr.getInt(i));

        Paper.book(appDetails.getAppId()).write("SCH", list);

        // adding exhibitor list

        list = new ArrayList<>();
        if (!details.getString("exhibitor_favorites").equalsIgnoreCase("")) {
            jsonArr = new JSONArray(details.getString("exhibitor_favorites"));
            for (int i = 0; i < jsonArr.length(); i++)
                list.add(jsonArr.getInt(i));
        }

        Paper.book().write("Exhibitor", list);

        //adding schedule list

        list = new ArrayList<>();

        if (!details.getString("sponsor_favorites").equalsIgnoreCase("")) {
            jsonArr = new JSONArray(details.getString("sponsor_favorites"));
            for (int i = 0; i < jsonArr.length(); i++)
                list.add(jsonArr.getInt(i));
        }


        Paper.book().write("Sponsor", list);

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

        Paper.book().write("AgendaNote", noteslist);


        // Adding Note of Exhibitor
        JSONArray exh = notesdetails.getJSONArray("exhibitors");

        noteslist = new HashMap<>();
        for (int i = 0; i < exh.length(); i++) {
            String eventString = exh.getJSONObject(i).toString();
            Notes eobj = gson.fromJson(eventString, Notes.class);
            noteslist.put(eobj.getId(), eobj);
        }

        Paper.book().write("ExhibitorNote", noteslist);

        // Adding Note of Sponsor

        JSONArray sponsor = notesdetails.getJSONArray("sponsors");

        noteslist = new HashMap<>();
        for (int i = 0; i < sponsor.length(); i++) {
            String eventString = sponsor.getJSONObject(i).toString();
            Notes eobj = gson.fromJson(eventString, Notes.class);
            noteslist.put(eobj.getId(), eobj);
        }

        Paper.book().write("SponsorNote", noteslist);


        // Adding Rating of agenda
        JSONObject ratingdetails = details.getJSONObject("ratings");

        JSONArray AgendaRating = ratingdetails.getJSONArray("agenda");

        HashMap<Integer, Rating> ratinglist = new HashMap<>();

        for (int i = 0; i < AgendaRating.length(); i++) {
            String eventString = AgendaRating.getJSONObject(i).toString();
            Rating eobj = gson.fromJson(eventString, Rating.class);
            ratinglist.put(eobj.getType_id(), eobj);
        }

        Paper.book().write("AgendaRating", ratinglist);


        //Adding Rating of Speaker

        JSONArray SpeakerRating = ratingdetails.getJSONArray("speakers");

        ratinglist = new HashMap<>();
        for (int i = 0; i < SpeakerRating.length(); i++) {
            String eventString = SpeakerRating.getJSONObject(i).toString();
            Rating eobj = gson.fromJson(eventString, Rating.class);
            ratinglist.put(eobj.getType_id(), eobj);
        }

        Paper.book().write("SpeakerRating", ratinglist);
        setMenuRefresh();

    }

    private void setMenuRefresh() {
        Menu menu = rightnaviagtion.getMenu();
        if ((Paper.book(appDetails.getAppId()).read("admin_flag", "").equals("admin") ||
                Paper.book(appDetails.getAppId()).read("admin_flag", "").equals("exhibitor"))) {

            menu.getItem(0).setVisible(true);
        } else {
            menu.getItem(0).setVisible(false);
        }
    }


    private void FetchPublicUSerdata() {

        final ProgressDialog dialog = new ProgressDialog(SingleEventHome.this, R.style.MyAlertDialogStyle);
        dialog.setMessage("Loading Details...");
        dialog.show();


        String tag_string_req = "Login";
        String url = ApiList.CheckIN;
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                try {
                    System.out.println(response);
                    JSONObject jObj = new JSONObject(response);


                    if (jObj.getBoolean("response")) {
                        parseresult(jObj.getJSONObject("responseString"));
                        dialog.dismiss();
                    } else {
                        dialog.dismiss();
                        Error_Dialog.show(jObj.getString("responseString"), SingleEventHome.this);
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
                    Error_Dialog.show("Timeout", SingleEventHome.this);
                } else if (com.webmobi.gecmedia.CustomViews.VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(com.webmobi.gecmedia.CustomViews.VolleyErrorLis.handleServerError(error, SingleEventHome.this), SingleEventHome.this);
                } else if (com.webmobi.gecmedia.CustomViews.VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show("Please Check Your Internet Connection", SingleEventHome.this);
                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("deviceType", "android");
                params.put("deviceId", regId);
                params.put("appid", appDetails.getAppId());
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


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        App.getInstance().cancelall();
    }

    private void registeruser() {

        final ProgressDialog dialog = new ProgressDialog(SingleEventHome.this, R.style.MyAlertDialogStyle);
        dialog.setMessage("");
        dialog.show();
        String tag_string_req = "Login";
        String url = com.webmobi.gecmedia.Config.ApiList.discovereventsregister;
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                try {
                    System.out.println(response);
                    JSONObject jObj = new JSONObject(response);


                    if (jObj.getBoolean("response")) {
                        checkin();
                        Toast.makeText(SingleEventHome.this, "User Registered Successfully", Toast.LENGTH_SHORT).show();
                        Paper.book(appDetails.getAppId()).write("registered", true);
                        /*isregistered = true;
                        register.setAnimation(animFadeOut);
                        register.setVisibility(View.GONE);*/

                    } else {
                        checkin();
                        if (jObj.getString("responseString").equalsIgnoreCase("This user is already registered."))
                            Paper.book(appDetails.getAppId()).write("registered", true);
//                        Error_Dialog.show(jObj.getString("responseString"), SingleEventHome.this);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userid", Paper.book().read("userId", ""));
                params.put("appid", appDetails.getAppId());
                params.put("deviceType", "android");
                params.put("deviceId", regId);
                params.put("email", Paper.book().read("Email", ""));
                params.put("username", Paper.book().read("username", ""));
                System.out.println(params);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
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

    ////Mood-o-meter code
    private void sendMoodFeedback(final String qid, int p, String type) {

        final ProgressDialog dialog = new ProgressDialog(SingleEventHome.this, com.singleevent.sdk.R.style.MyAlertDialogStyle);
        dialog.setMessage("Checking....");
        dialog.show();
        String tag_string_req = "Schdule";
        String url = ApiList.MoodFeedback;
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                try {
                    System.out.println(response);
                    JSONObject jObj = new JSONObject(response);


                    if (jObj.getBoolean("response")) {
                        //  Error_Dialog.show(jObj.getString("responseString"), SingleEventHome.this);
                        recheck = (jObj.getInt("moodometer_feedback"));
                        if (checkitemsize(type, p)) {
                            int checkmood = recheck;
                            if (checkmood == 0) {
                                Intent intent = new Intent(SingleEventHome.this, Moodometer.class);
                                intent.putExtra("pos", p);
                                intent.putExtra("title", "Mood-O-Meeter");
                                startActivity(intent);
                            } else if (checkmood == 1) {
                                Error_Dialog.show("You have already submitted the Survey.", SingleEventHome.this);
                            } else {
                                // Error_Dialog.show("Please Login", MainActivity.this);
                                Error_Dialog.show("No details.", SingleEventHome.this);
                            }

                        } else {
                            Error_Dialog.show("No details.", SingleEventHome.this);
                        }


                    } else {
                        if (jObj.getString("responseString").equalsIgnoreCase("Invalid token")) {
                            Error_Dialog.show("Session is Expired Please Login", SingleEventHome.this);
                        } else
                            Error_Dialog.show(jObj.getString("responseString"), SingleEventHome.this);

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
                    Error_Dialog.show("Timeout", SingleEventHome.this);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, SingleEventHome.this), SingleEventHome.this);
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show("Survey Couldn't submit.", SingleEventHome.this);


                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userid", Paper.book().read("userId", ""));
                params.put("flag", "check");
                params.put("default_id", "moodometer0");
                params.put("appid", appDetails.getAppId());
                params.put("question_id", qid);
                params.put("question", "");
                params.put("emoji_name", "");
                params.put("tags", "[]");
                params.put("details", "");
                System.out.println(params);
                return params;
            }
        };


        strReq.setShouldCache(false);
        App.getInstance().addToRequestQueue(strReq, tag_string_req);
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

    }

    public String checkMood(int p, String type) {

        for (int i = 0; i < e2.getTabs(p).getItemsSize(); i++) {
            for (int j = 0; j < e2.getTabs(p).getItems(i).getEmoji_info(); j++) {
                if (e2.getTabs(p).getItems(i).getActive() != 0) {
                    q_id = String.valueOf(e2.getTabs(p).getItems(i).getQuestion_id());

                }
            }
            sendMoodFeedback(q_id, p, type);
        }
        return q_id;
    }
}
