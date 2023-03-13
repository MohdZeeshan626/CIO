package com.webmobi.gecmedia;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.navigation.NavigationView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.MyApplication;
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
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.singleevent.sdk.ApiList;
import com.singleevent.sdk.App;
import com.singleevent.sdk.Custom_View.Error_Dialog;
import com.singleevent.sdk.Custom_View.Util;
import com.singleevent.sdk.Custom_View.VolleyErrorLis;
import com.singleevent.sdk.model.AppDetails;
import com.singleevent.sdk.model.Events;
import com.singleevent.sdk.View.Fragment.Left_Fragment.HomeFragment;
import com.singleevent.sdk.View.LeftActivity.AboutUs;
import com.singleevent.sdk.View.LeftActivity.AgendaRoot;
import com.singleevent.sdk.View.LeftActivity.Attachment;
import com.singleevent.sdk.View.LeftActivity.Attendee;
import com.singleevent.sdk.View.LeftActivity.Contact_Us;
import com.singleevent.sdk.View.LeftActivity.Feeds;
import com.singleevent.sdk.View.LeftActivity.LeaderBoardActivity;
import com.singleevent.sdk.View.LeftActivity.MapRoot;
import com.singleevent.sdk.View.LeftActivity.PollingRoot;
import com.singleevent.sdk.View.LeftActivity.SocialMedaiRoot;
import com.singleevent.sdk.View.LeftActivity.SpeakerRoot;
import com.singleevent.sdk.View.LeftActivity.SponsorRoot;
import com.singleevent.sdk.View.LeftActivity.SurveyRoot;
import com.singleevent.sdk.View.LeftActivity.VideoActivity;
import com.singleevent.sdk.View.RightActivity.AdminPanelActivity;
import com.singleevent.sdk.View.RightActivity.Group_feed;
import com.singleevent.sdk.View.RightActivity.MyChat;
import com.singleevent.sdk.View.RightActivity.MyCompany;
import com.singleevent.sdk.View.RightActivity.MyMeeting;
import com.singleevent.sdk.View.RightActivity.Profile;
import com.singleevent.sdk.View.RightActivity.MyQrCodeGeneratorActivity;
import com.singleevent.sdk.View.RightActivity.MyRequest;
import com.singleevent.sdk.View.RightActivity.MySchedule;
import com.singleevent.sdk.View.RightActivity.Note;
import com.singleevent.sdk.View.RightActivity.Notification;
import com.singleevent.sdk.utils.DataBaseStorage;
import com.webmobi.gecmedia.Views.LoginActivity;
import com.webmobi.gecmedia.Views.fragment.ContactUsFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        HomeFragment.OnTabSelectedListener, View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    AppDetails appDetails;
    private NavigationView navigationView, rightnaviagtion;
    private DrawerLayout drawerLayout;
    private float navdpWidth;
    private double margintop;
    double navheight, lwidth, lheight;
    RelativeLayout v1, v2, v3;
    private FragmentManager fragmentManager;
    private ArrayList<Events> events = new ArrayList<>();
    Events e;
    int request;
    ImageView rightview;
    TextView login;
    HomeFragment home;
    String baseUrl = "https://webmobi.s3.amazonaws.com/nativeapps/";
    String filename = "app.json";
    File eventDir, jsonFile;
    String dir;
    TextView navHeader;
    boolean hasToCheck;
    ContactUsFragment contactUsFragment;

    boolean isRefreshApp = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Paper.init(this);
        setContentView(R.layout.activity_preview_main);
        appDetails = Paper.book().read("Appdetails");


        dir = getFilesDir() + File.separator + "Demo" + File.separator;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));

        rightview = (ImageView) toolbar.findViewById(R.id.showevents);
        login = (TextView) toolbar.findViewById(R.id.login);
        login.setTypeface(Util.regulartypeface(MainActivity.this));
        login.setOnClickListener(this);
        rightview.setOnClickListener(this);
        setSupportActionBar(toolbar);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        rightnaviagtion = (NavigationView) findViewById(R.id.right_navigation_view);
        navigationView.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));
        rightnaviagtion.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));
        navigationView.setItemBackground(makeSelector(Color.parseColor(appDetails.getTheme_selected())));
        rightnaviagtion.setItemBackground(makeSelector(Color.parseColor(appDetails.getTheme_selected())));
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, findViewById(R.id.right_navigation_view));


        navdpWidth = getResources().getDimensionPixelSize(R.dimen.abc_action_bar_default_height_material) * 5;
//        margintop = getResources().getDimensionPixelSize(R.dimen.abc_action_bar_default_height_material) + getResources().getDimensionPixelSize(R.dimen.abc_action_bar_default_height_material) * 0.50;
        navheight = navdpWidth * (0.375);
        lwidth = navheight * 0.60;
        lheight = lwidth * 0.83;

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
        final ImageView coverp = (ImageView) header.findViewById(R.id.coverimage);
        final ImageView logo = (ImageView) header.findViewById(R.id.logo);
        v1.setVisibility(View.GONE);
        RelativeLayout.LayoutParams rparams = (RelativeLayout.LayoutParams) v2.getLayoutParams();
        RelativeLayout.LayoutParams lparams = new RelativeLayout.LayoutParams((int) lwidth, (int) lheight);
        rparams.width = (int) navdpWidth;
        rparams.height = (int) navheight;
        v2.setLayoutParams(rparams);
        logo.setLayoutParams(lparams);

        //setting cover image


        Glide.with(getApplicationContext())
                .load(appDetails.getAppLoadingImage())
                .asBitmap()
                .placeholder(R.drawable.dback)
                .error(R.drawable.dback)
                .into(new BitmapImageViewTarget(coverp) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        coverp.setImageBitmap(scaleBitmap(resource, (int) navdpWidth, (int) navheight));
                    }
                });


        //setting login

        Glide.with(this)
                .load(appDetails.getAppLogo())
                .asBitmap()
                .placeholder(R.drawable.default_logo)
                .error(R.drawable.default_logo)
                .into(new BitmapImageViewTarget(logo) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        logo.setImageBitmap(scaleBitmap(resource, (int) lwidth, (int) lheight));
                    }
                });

        View rightNavHeader = rightnaviagtion.getHeaderView(0);
        navHeader = (TextView) rightNavHeader.findViewById(R.id.tvRHeader);
        GradientDrawable bgShape = (GradientDrawable) navHeader.getBackground();
        bgShape.setColor(Color.parseColor(appDetails.getTheme_selected()));


        // setting app name

       /* TxtVCustomFonts title = (TxtVCustomFonts) header.findViewById(R.id.eventtitle);
        TxtVCustomFonts loc = (TxtVCustomFonts) header.findViewById(R.id.eventlocation);*/
        TextView title = (TextView) header.findViewById(R.id.eventtitle);
        TextView loc = (TextView) header.findViewById(R.id.eventlocation);

        title.setText(appDetails.getAppName());
        loc.setText(appDetails.getLocation());

        fragmentManager = getSupportFragmentManager();
        events = Paper.book().read("Appevents");
        e = events.get(0);
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


        if (getIntent().getExtras() != null) {
            hasToCheck = getIntent().getExtras().getBoolean("hasToCheck");
            if (appDetails.getForceUpdate() && hasToCheck) {
                getIntent().removeExtra("hasToCheck");
                //
                //  checkupdate(appDetails.getAppId());

            }
        }

    }

    private void setrightnavigationitems() {
        final Menu menu = rightnaviagtion.getMenu();


        //adding admin panel
        menu.add(0, 0, 0, "Admin Panel");
        menu.getItem(0).setIcon(new IconDrawable(this,
                FontAwesomeIcons.fa_desktop).colorRes(R.color.white));


        // adding the Schedule menu
        menu.add(1, 1, 0, "My Schedules");
        menu.getItem(1).setIcon(new IconDrawable(this, FontAwesomeIcons.fa_calendar_check_o).colorRes(R.color.white));


        // adding the Companies

        menu.add(2, 2, 0, "My Companies");
        menu.getItem(2).setIcon(new IconDrawable(this, FontAwesomeIcons.fa_calendar_check_o).colorRes(R.color.white));

        // adding the notes


        menu.add(3, 3, 0, "My Notes");
        menu.getItem(3).setIcon(new IconDrawable(this, FontAwesomeIcons.fa_sticky_note_o).colorRes(R.color.white));

        menu.add(4, 4, 0, "My Chats");
        menu.getItem(4).setIcon(new IconDrawable(this, FontAwesomeIcons.fa_commenting_o).colorRes(R.color.white));

        // adding push notification

        menu.add(5, 5, 0, "Notifications");
        menu.getItem(5).setIcon(new IconDrawable(this, FontAwesomeIcons.fa_bell_o).colorRes(R.color.white));


        //adding inbox
        menu.add(6, 6, 0, "Inbox");
        menu.getItem(6).setIcon(new IconDrawable(this, FontAwesomeIcons.fa_inbox).colorRes(R.color.white));

        //adding my meetings
        menu.add(7, 7, 0, "My Meetings");
        menu.getItem(7).setIcon(new IconDrawable(this, FontAwesomeIcons.fa_calendar_check_o).colorRes(R.color.white));

        String[] schedule = appDetails.getAddon_modules();
        if (schedule != null && schedule.length > 0)
            if ("schedule".equals(schedule[0])) {
                Log.v(TAG, schedule[0]);
                menu.getItem(7).setVisible(true);
            } else {
                menu.getItem(7).setVisible(false);
            }
        else {
            menu.getItem(7).setVisible(false);
        }
        menu.add(8, 8, 0, "My Badge");
        menu.getItem(8).setIcon(new IconDrawable(this, FontAwesomeIcons.fa_qrcode).colorRes(R.color.white));


        menu.add(9, 9, 0, "My Contacts");
        menu.getItem(9).setIcon(new IconDrawable(this, FontAwesomeIcons.fa_users).colorRes(R.color.white));
        // adding the My profile

        menu.add(10, 10, 0, "My Profile");
        menu.getItem(10).setIcon(new IconDrawable(this, FontAwesomeIcons.fa_gear).colorRes(R.color.white));


        //applying font style roboto
        for (int i = 0; i < menu.size(); i++) {
            MenuItem mi = menu.getItem(i);
            Util.applyFontToMenuItem(mi, this);
        }
    }


    // setting the left menu programmatically

    private void setleftnavigationitems() {
        final Menu menu = navigationView.getMenu();
        for (int j = 0; j < e.getTabs().length; j++) {
            if (e.getTabs(j).getType().toLowerCase().equals("gamification")) {
                Paper.book(appDetails.getAppId()).write("isGamification", true);
                Paper.book(appDetails.getAppId()).write("Gamification", e.getTabs(j).getCheckvalue());

            }
            menu.add(j, j, 0, e.getTabs(j).getTitle());
            String iconname = "fa_" + e.getTabs(j).getIconCls().replace("-", "_");
            menu.getItem(j).setIcon(new IconDrawable(this, FontAwesomeIcons.valueOf(iconname)).colorRes(R.color.white));
            menu.getItem(j).setCheckable(true);

        }
        hideleftnaviagtionitem();
    }


    //hiding the left items based on itemsize and type value is present


    private void hideleftnaviagtionitem() {
        final Menu menu = navigationView.getMenu();

        int size = menu.size();
        menu.add(size, size, 0, "Refresh AppNew");
        menu.getItem(size).setIcon(new IconDrawable(this, FontAwesomeIcons.fa_refresh).colorRes(R.color.white));
        menu.getItem(size).setCheckable(true);


        for (int i = 0; i < menu.size() - 1; i++) {
            MenuItem subMenuItem = menu.getItem(i);
            // setting font roboto
            Util.applyFontToMenuItem(subMenuItem, MainActivity.this);
            if (getMenuId(e.getTabs(i).getType()) == 0) {
                menu.getItem(i).setVisible(false);
            }
        }
        MenuItem subMenuItem = menu.getItem(menu.size() - 1);
        Util.applyFontToMenuItem(subMenuItem, MainActivity.this);
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
            case "polling":
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
            case "contactUs":
                return 1;
            case "rssfeeds":
                return 1;
            case "groupfeed":
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
            case "video":
                return 1;
            case "attendee":
                return 1;
            case "feeds":
                return 1;
            case "gamification":
                return 1;
            default:
                return 0;

        }
    }


    //checking the items size of each module if its less then or equal to zero hiding the items

    private boolean checkitemsize(String type, int pos) {
        switch (type) {
            case "survey":
                return e.getTabs(pos).getItemsSize() > 0;

            case "polling":
                return e.getTabs(pos).getItemsSize() > 0;

            case "speakersData":
                return e.getTabs(pos).getItemsSize() > 0;

            case "agenda":
                return e.getTabs(pos).getAgendaSize() > 0;

            case "exhibitorsData":
                return e.getTabs(pos).getItemsSize() > 0;

            case "socialmedia":
                return e.getTabs(pos).getItemsSize() > 0;

            case "map":
                return true;

            case "aboutus":
                return true;

            case "groupfeed":
                return true;

            case "contactUs":
                return true;

            case "rssfeeds":
                return true;

            case "pdf":
                return e.getTabs(pos).getpdfSize() > 0;


            case "home":
                return true;

            case "eventslist":
                return e.getTabs(pos).getItemsSize() > 0;

            case "weather":
                return true;

            case "attendee":
                return true;


            case "currency":
                return true;


            case "sponsorsData":
                return e.getTabs(pos).getItemsSize() > 0;

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

            Intent i;
            item.setChecked(true);

            Bundle args = new Bundle();
            drawerLayout.closeDrawers();
            FragmentTransaction fc;

            Menu menu = navigationView.getMenu();
            //  int secondLastItem = menu.size()-2;

            int lastitem = menu.size() - 1;

            String itemname;
            if (lastitem == item.getItemId())
                itemname = "RefreshApp";
            else
                itemname = e.getTabs(item.getItemId()).getType();

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

                    intent = new Intent(MainActivity.this, AboutUs.class);
                    intent.putExtra("pos", item.getItemId());
                    intent.putExtra("title", item.getTitle().toString());
                    startActivity(intent);

                    break;
                case "groupfeed":


                    intent = new Intent(MainActivity.this, Group_feed.class);
                    startActivity(intent);

                    break;

                case "agenda":

                    if (checkitemsize(e.getTabs(item.getItemId()).getType(), item.getItemId())) {
                        intent = new Intent(MainActivity.this, AgendaRoot.class);
                        intent.putExtra("pos", item.getItemId());
                        intent.putExtra("title", item.getTitle().toString());
                        startActivity(intent);
                    } else
                        Error_Dialog.show("No details", MainActivity.this);
                    break;

                case "speakersData":
                    if (checkitemsize(e.getTabs(item.getItemId()).getType(), item.getItemId())) {
                        intent = new Intent(MainActivity.this, SpeakerRoot.class);
                        intent.putExtra("pos", item.getItemId());
                        intent.putExtra("title", item.getTitle().toString());
                        startActivity(intent);
                    } else
                        Error_Dialog.show("No details", MainActivity.this);

                    break;

                case "sponsorsData":
                    if (checkitemsize(e.getTabs(item.getItemId()).getType(), item.getItemId())) {
                        Paper.book().write("action", "Sponsor");
                        intent = new Intent(MainActivity.this, SponsorRoot.class);
                        intent.putExtra("pos", item.getItemId());
                        intent.putExtra("title", item.getTitle().toString());
                        startActivity(intent);
                    } else
                        Error_Dialog.show("No details", MainActivity.this);

                    break;

                case "exhibitorsData":
                    if (checkitemsize(e.getTabs(item.getItemId()).getType(), item.getItemId())) {
                        Paper.book().write("action", "Exhibitor");
                        intent = new Intent(MainActivity.this, SponsorRoot.class);
                        intent.putExtra("pos", item.getItemId());
                        intent.putExtra("title", item.getTitle().toString());
                        startActivity(intent);
                    } else
                        Error_Dialog.show("No details", MainActivity.this);
                    break;
                case "survey":
                    if (Paper.book().read("Islogin", false)) {
                        if (checkitemsize(e.getTabs(item.getItemId()).getType(), item.getItemId())) {
                            int checkSurvey = Paper.book().read("survey_flag", 2);
                            if (checkSurvey == 0) {
                                intent = new Intent(MainActivity.this, SurveyRoot.class);
                                intent.putExtra("pos", item.getItemId());
                                intent.putExtra("title", item.getTitle().toString());
                                startActivity(intent);
                            } else if (checkSurvey == 1 && Paper.book(appDetails.getAppId()).read("offlineSurveyAns").equals("") ) {
                                Error_Dialog.show("You have already submitted the Survey", MainActivity.this);
                            }else if (!DataBaseStorage.isInternetConnectivity(this) && checkSurvey == 1 && !Paper.book(appDetails.getAppId()).read("offlineSurveyAns").equals("")){
                                Error_Dialog.show("You have already submitted the Survey", MainActivity.this);
                            }else if (DataBaseStorage.isInternetConnectivity(this) && checkSurvey == 1 && !Paper.book(appDetails.getAppId()).read("offlineSurveyAns").equals("")){
                                intent = new Intent(MainActivity.this, SurveyRoot.class);
                                intent.putExtra("pos", item.getItemId());
                                intent.putExtra("title", item.getTitle().toString());
                                startActivity(intent);
                            }

                        }else if(!Paper.book().read("Islogin",false)){
                            Error_Dialog.show("Please login and try again", MainActivity.this);
                        } else
                            Error_Dialog.show("Information not available.", MainActivity.this);

                    }
                    break;

                case "polling":
                    if (Paper.book().read("Islogin", false)) {
                        if (checkitemsize(e.getTabs(item.getItemId()).getType(), item.getItemId())) {
                            intent = new Intent(MainActivity.this, PollingRoot.class);
                            intent.putExtra("pos", item.getItemId());
                            intent.putExtra("title", item.getTitle().toString());
                            intent.putExtra("polltype","global");
                            startActivity(intent);
                        } else
                            Error_Dialog.show("No details", MainActivity.this);
                    } else {
                        Error_Dialog.show("Please Login", MainActivity.this);
                        intent = new Intent(this, LoginActivity.class);
                        startActivityForResult(intent, 1);

                    }
                    break;

                case "socialmedia":
                    if (checkitemsize(e.getTabs(item.getItemId()).getType(), item.getItemId())) {
                        intent = new Intent(MainActivity.this, SocialMedaiRoot.class);
                        intent.putExtra("pos", item.getItemId());
                        intent.putExtra("title", item.getTitle().toString());
                        startActivity(intent);
                    } else
                        Error_Dialog.show("No details", MainActivity.this);

                    break;

                case "pdf":
                    if (checkitemsize(e.getTabs(item.getItemId()).getType(), item.getItemId())) {
                        intent = new Intent(MainActivity.this, Attachment.class);
                        intent.putExtra("pos", item.getItemId());
                        intent.putExtra("title", item.getTitle().toString());
                        startActivity(intent);
                    } else
                        Error_Dialog.show("No details", MainActivity.this);

                    break;

                case "video":

                    intent = new Intent(MainActivity.this, VideoActivity.class);
                    intent.putExtra("pos", item.getItemId());
                    intent.putExtra("title", item.getTitle().toString());
                    startActivity(intent);
                    break;


                case "map":
                    intent = new Intent(MainActivity.this, MapRoot.class);
                    intent.putExtra("pos", item.getItemId());
                    intent.putExtra("title", item.getTitle().toString());
                    startActivity(intent);
                    break;

                case "contactUs":
                    intent = new Intent(MainActivity.this, Contact_Us.class);
                    intent.putExtra("pos", item.getItemId());
                    intent.putExtra("title", item.getTitle().toString());
                    startActivity(intent);
                    break;

                case "attendee":
                    if (Paper.book().read("Islogin", false)) {
                        intent = new Intent(MainActivity.this, Attendee.class);
                        intent.putExtra("pos", item.getItemId());
                        intent.putExtra("title", item.getTitle().toString());
                        startActivity(intent);
                    } else {
                        Error_Dialog.show("Please Login", MainActivity.this);
                        intent = new Intent(this, LoginActivity.class);
                        startActivityForResult(intent, 1);

                    }
                    break;

                case "feeds":

                    if (Paper.book().read("Islogin", false)) {
//                        intent = new Intent(MainActivity.this, FeedActivity.class);
                        intent = new Intent(MainActivity.this, Feeds.class);
                        startActivity(intent);
                    } else {
                        Error_Dialog.show("Please Login", MainActivity.this);
                        intent = new Intent(this, LoginActivity.class);
                        startActivityForResult(intent, 1);
                    }


                    break;

                case "gamification":

                    if (Paper.book().read("Islogin", false)) {
                        intent = new Intent(MainActivity.this, LeaderBoardActivity.class);
                        startActivity(intent);
                    } else {
                        Error_Dialog.show("Please Login", MainActivity.this);
                        intent = new Intent(this, LoginActivity.class);
                        startActivityForResult(intent, 1);
                    }


                    break;

                case "RefreshApp":
//                    checkupdate(appDetails.getAppId());
                    String query = "";
                    try {
                        query = URLEncoder.encode(appDetails.getAppUrl(), "utf-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    String jsonUrl = baseUrl + query + "/temp/appData.json";

                    // String jsonUrl = baseUrl + appDetails.getAppUrl() + "/temp/appData.json";
                    loadjson(jsonUrl);
                    break;


            }
        } else
            Error_Dialog.show("Please Login or Contact Event Administrator", MainActivity.this);

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
                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                startActivityForResult(i, 1);
                break;
            case R.id.showevents:
                if (drawerLayout.isDrawerOpen(navigationView)) {
                    drawerLayout.closeDrawer(navigationView);
                }
                if (drawerLayout.isDrawerOpen(rightnaviagtion)) {
                    drawerLayout.closeDrawer(rightnaviagtion);
                } else
                    drawerLayout.openDrawer(rightnaviagtion);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        settingheader();
    }

    private void settingheader() {

        Menu menu = rightnaviagtion.getMenu();
        if ((Paper.book(appDetails.getAppId()).read("admin_flag", "").equals("admin") ||
                Paper.book(appDetails.getAppId()).read("admin_flag", "").equals("exhibitor"))) {

            menu.getItem(0).setVisible(true);
        } else {
            menu.getItem(0).setVisible(false);
        }

        boolean islogin = Paper.book().read("Islogin", false);


        if (islogin) {
            rightview.setVisibility(View.VISIBLE);
            login.setVisibility(View.GONE);
            String firstWord = Paper.book().read("username").toString();
            if (firstWord.contains(" ")) {
                firstWord = firstWord.substring(0, firstWord.indexOf(" "));
                System.out.println(firstWord);
            }
            navHeader.setText("Hi " + firstWord);


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
            Menu menu = rightnaviagtion.getMenu();
            if ((Paper.book(appDetails.getAppId()).read("admin_flag", "").equals("admin") ||
                    Paper.book(appDetails.getAppId()).read("admin_flag", "").equals("exhibitor"))) {

                menu.getItem(0).setVisible(true);
            } else {
                menu.getItem(0).setVisible(false);
            }
        }
        if(requestCode==50)
        {
            ContactUsFragment contactUsFragment = (ContactUsFragment) getSupportFragmentManager().findFragmentByTag("contactusfragment");
            if (contactUsFragment != null) {
                contactUsFragment.onActivityResult(requestCode, resultCode, data);
            }
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
                    i = new Intent(MainActivity.this, MySchedule.class);
                    startActivity(i);
                    break;

                case "My Companies":
                    i = new Intent(MainActivity.this, MyCompany.class);
                    startActivity(i);
                    break;

                case "My Profile":
                    i = new Intent(MainActivity.this, Profile.class);
                    startActivity(i);
                    break;

                case "My Notes":
                    i = new Intent(MainActivity.this, Note.class);
                    startActivity(i);
                    break;
                case "My Chats":
                    i = new Intent(MainActivity.this, MyChat.class);
                    startActivity(i);
                    break;

                case "Notifications":
                    i = new Intent(MainActivity.this, Notification.class);
                    startActivity(i);

                    break;

                case "Inbox":
                    i = new Intent(MainActivity.this, MyRequest.class);
                    startActivity(i);
                    break;


                case "My Meetings":
                    i = new Intent(MainActivity.this, MyMeeting.class);
                    startActivity(i);
                    break;

                case "My Badge":
                    i = new Intent(MainActivity.this, MyQrCodeGeneratorActivity.class);
                    startActivity(i);
                    break;
                //for admin
                case "Admin Panel":

                    i = new Intent(MainActivity.this, AdminPanelActivity.class);
                    i.putExtra("USER_IS", "Admin Panel");
                    startActivity(i);
                    break;
                case "My Contacts":
                   /* i = new Intent(MainActivity.this, ContactUsFragment.class);
                    startActivity(i);
*/
                    if (Paper.book().read("Islogin", false)) {
                        setContentView(R.layout.activity_main2);
                        if (contactUsFragment == null)

                            contactUsFragment = new ContactUsFragment();
                        replaceFragment(contactUsFragment, "contactusfragment");
                        navigationView.setVisibility(View.GONE);

                        //main_linear.setVisibility(View.GONE);

                        //ll_toolbar_content.setVisibility(View.GONE);
                        // textViewToolBar.setVisibility(View.VISIBLE);
                        //textViewToolBar.setText((this, new SpannableString("Contacts")));
                        return true;
                    }
                    break;
                //for admin

                case "My Leader Board":

                    i = new Intent(MainActivity.this, LeaderBoardActivity.class);
                    startActivity(i);

            }
            return true;
        }
    };

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(navigationView))
            drawerLayout.closeDrawers();
        else if (drawerLayout.isDrawerOpen(rightnaviagtion))
            drawerLayout.closeDrawers();
        else
            super.onBackPressed();
    }
    /// Add Force Update
    @Override
    protected void onStart() {
        super.onStart();
        MyApplication myApp=new MyApplication();
        Log.d(TAG, TAG + " onStart");

        if (myApp.wasInBackground) {
            Toast.makeText(getApplicationContext(),
                    "Application came to foreground", Toast.LENGTH_SHORT)
                    .show();
               checkupdate(appDetails.getAppId());
            myApp.wasInBackground = false;
        }checkupdate(appDetails.getAppId());
    }

    // checking update


    public void checkupdate(String appId) {

        String tag_string_req = "Checking Update";
        String url = ApiList.CheckUpdate + "appId=" + appId;
        System.out.println(url);
        final ProgressDialog pDialog = new ProgressDialog(MainActivity.this, R.style.MyAlertDialogStyle);
        pDialog.setMessage("Checking ...");
        pDialog.show();

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    pDialog.dismiss();
                    System.out.println("JSON Response " + response);
                    if (response.getBoolean("response")) {
                        if (response.getInt("responseString") > appDetails.getVersion()) {

                            String query = "";
                            try {
                                query = URLEncoder.encode(appDetails.getAppUrl(), "utf-8");
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                            String jsonUrl = baseUrl + query + "/temp/appData.json";
                            // String jsonUrl = baseUrl + appDetails.getAppUrl() + "/temp/appData.json";
                            loadjson(jsonUrl);

                        } else
                            Error_Dialog.show("No Update Available", MainActivity.this);

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
                    Error_Dialog.show("Timeout", MainActivity.this);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, MainActivity.this), MainActivity.this);
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show("Please Check Your Internet Connection", MainActivity.this);
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


    // Alert Dialog asking for the user to whether to update or not


    private void updateavailable() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                MainActivity.this);

        // set title
        alertDialogBuilder.setTitle("Update Available");

        // set dialog message
        alertDialogBuilder
                .setMessage("Do you want update event")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String query = "";
                        try {
                            query = URLEncoder.encode(appDetails.getAppUrl(), "utf-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        String jsonUrl = baseUrl + query + "/temp/appData.json";
                        //String jsonUrl = baseUrl + appDetails.getAppUrl() + "/temp/appData.json";
                        loadjson(jsonUrl);
                        dialog.cancel();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
        int textViewId = alertDialog.getContext().getResources().getIdentifier("android:id/alertTitle", null, null);
        TextView tv = (TextView) alertDialog.findViewById(textViewId);
        tv.setTextColor(getResources().getColor(R.color.black));
        TextView tv1 = (TextView) alertDialog.findViewById(android.R.id.message);
        tv1.setTextColor(getResources().getColor(R.color.black));
    }


    // loading updated json

    void loadjson(String jsonUrl) {
        final ProgressDialog pDialog = new ProgressDialog(MainActivity.this, R.style.MyAlertDialogStyle);
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
                    eventDir = new File(dir + appDetails.getAppUrl());
                    jsonFile = new File(eventDir, filename);
                    jsonFile.delete();
                    Files.write(jsonResponse, jsonFile, Charset.defaultCharset());

                    showjs(jsonFile);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                if (error instanceof TimeoutError) {
                    Error_Dialog.show("Timeout", MainActivity.this);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, MainActivity.this), MainActivity.this);
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show("Please Check Your Internet Connection", MainActivity.this);
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


    private void showjs(File jsonFile) throws IOException {
        String contents = Files.toString(jsonFile, Charset.defaultCharset());
        System.out.println("File Contents : " + contents);
        ArrayList<Events> eventsToDisplay = new ArrayList<>();
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
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


}
