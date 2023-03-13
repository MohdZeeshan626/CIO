package com.webmobi.gecmedia.Views.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
//import android.app.Notification;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.facebook.share.widget.ShareDialog;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
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
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.share.model.ShareLinkContent;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.makeramen.roundedimageview.RoundedImageView;
import com.singleevent.sdk.App;
import com.singleevent.sdk.Custom_View.Error_Dialog;
import com.singleevent.sdk.Custom_View.Util;
import com.singleevent.sdk.model.AppDetails;
import com.singleevent.sdk.model.Events;
import com.singleevent.sdk.model.LocalArraylist.Notes;
import com.singleevent.sdk.model.LocalArraylist.Rating;
import com.singleevent.sdk.View.RightActivity.Notification;
import com.singleevent.sdk.View.RightActivity.admin.checkin.SimpleScannerActivity;
import com.singleevent.sdk.utils.DataBaseStorage;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.tweetcomposer.ComposerActivity;
import com.wang.avi.AVLoadingIndicatorView;
import com.webmobi.gecmedia.Config.ApiList;
import com.webmobi.gecmedia.CustomViews.ColorFilterTransformation;
import com.webmobi.gecmedia.CustomViews.DilatingDotsProgressBar;
import com.webmobi.gecmedia.CustomViews.VolleyErrorLis;
import com.webmobi.gecmedia.LocalDatabase.DatabaseHandler;
import com.webmobi.gecmedia.Models.Event;
import com.webmobi.gecmedia.Models.Events_Wishlist;
import com.webmobi.gecmedia.Models.MultiEvent;
import com.webmobi.gecmedia.Models.NearbyInterface;
import com.webmobi.gecmedia.R;
import com.webmobi.gecmedia.SingleEventHome;
import com.webmobi.gecmedia.Views.DiscoveryEventDetails;
import com.webmobi.gecmedia.Views.EventCategory;
import com.webmobi.gecmedia.Views.HomeScreenMulti;
import com.webmobi.gecmedia.Views.LoginActivity;
import com.webmobi.gecmedia.Views.Profile;
import com.webmobi.gecmedia.Views.RegActivity;
import com.webmobi.gecmedia.Views.SearchActivity;

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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import io.paperdb.Paper;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static com.facebook.FacebookSdk.getApplicationContext;


/**
 * Created by Admin on 4/24/2017.
 */

public class EventFragment extends Fragment implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ResultCallback<LocationSettingsResult>, LocationListener {
    boolean isPreview = false, isPrivateEvent = false;LinearLayout mainview, myEvents, reEvents,myspace, myPreview, mypreviewView, museum, airports, park, personal, association, myeventsview,myspaceview, remview;
    Context context;
    TextView cat_viewall, more_txt;
    ImageView more_icon;
    ConstraintLayout tradeshow, school, community;
    float ImgWidth, ImgHeight, lwidth, lheight;
    private float navdpWidth;
    double navheight, lwidth1, lheight1;
    int i;
    int temppos,temppos1;

    private DilatingDotsProgressBar mDilatingDotsProgressBar;
    List<String> savedirs;
    private List<String> savePrevdirs;
    File eventDir, jsonFile, descFile;
    String baseUrl = "https://webmobi.s3.amazonaws.com/nativeapps/";
    String filename = "app.json";
    AppDetails appDetails;
    String dir, dir_prev, regId;
    ArrayList<Event> recommendevents;

    ArrayList<MultiEvent> multieventlist;
    ArrayList<MultiEvent> multieventid;
    ArrayList<MultiEvent> finalmultieventid;

    ArrayList<String> multieventlist2;
    ArrayList<String> multieventlist3;
    ArrayList<String> multieventlist4;

    ArrayList<Event> recommdEventsAfterSorting;
    List<String> appurls;

    ArrayList<Event> myPreviewEventsArrayList;
    Dialog event_dialog;
    SupportMapFragment mapFragment;
    private GoogleMap mMap;
    DatabaseHandler db;
    NearbyInterface listener;
    private ArrayList<Events> eventsToDisplay;
    private ArrayList<Events> previewEventsToDisplay;
    private ArrayList<Events> offlineevents;
    View view;
    SharedPreferences spf;
    protected GoogleApiClient mGoogleApiClient;
    protected LocationRequest mLocationRequest;
    double lat, lng;
   // RelativeLayout mainrrr;
    ImageView title_left_ic;
    private DrawerLayout drawerLayout;
   // NavigationView navigationview;
    RelativeLayout v3;
    ArrayList<Event> categoryevents;
    ArrayList<String> meid;
    ArrayList<String> mename;
    ArrayList<String> melogo;
    ArrayList<String> metheme;
    Intent i1;
    Button passphrase;



    AppBarLayout appBarLayout;
    CollapsingToolbarLayout collapsingToolbarLayout;
    TextView toolbar_title, search,text_search, qrsearch;
    ConstraintLayout pguide;
    ImageView user_login;
    View act_bar_view;
    ProgressDialog pDialog, leventDialog;
    private final int PERMISSION_REQUEST_CODE = 1;
    private final int CALENDAR_PERMISSION_REQUEST_CODE = 2;
    protected LocationSettingsRequest mLocationSettingsRequest;

    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 1000;
    private int REQUEST_CHECK_SETTINGS = 100;

    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;
    Event listing_save, listing;

    Dialog help_dialog;
    LinearLayout whatsapp_dlg, twitter_dlg, fb_dlg, linkedin_dlg;
    boolean isWhatsApp = false, isTwitter = false, isLinkedin = false;
    Dialog dialog;
    TextView dlg_title, dlg_msg, dlg_ok;
    String share_event_title, share_event_date, share_location, share_search_key;
    TwitterAuthClient twitterAuthClient;
    final static int TWITTER_REQ_CODE = 140;

    public int pgn_count = 1;
    boolean hasNextPage = false;

    AVLoadingIndicatorView avl;

    String event_id = "", app_id = "";
    ArrayList<Event> Searchevnets;
    ArrayList<Event> Searchevnetstemp;
    public static final int CAMERA_PERMISSION = 120;
    public static final int SCANNER_REQUEST_CODE = 220;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        //listener = (NearbyInterface) activity;
        context = activity;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Twitter.initialize(getActivity());
        twitterAuthClient = new TwitterAuthClient();
       // appDetails = Paper.book().read("Appdetails");
//       AppUpdateChecker appUpdateChecker=new AppUpdateChecker(getActivity());  //pass the activity in constructure
//        appUpdateChecker.checkForUpdate(false);

        finalmultieventid =new ArrayList<>();




       /* Bundle extras = getActivity().getIntent().getExtras();
        if (extras == null)
            getActivity().finish();

        String eid=getActivity().getIntent().getStringExtra("multie_id");
        String ename=getActivity().getIntent().getStringExtra("multie_id");*/


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        buildGoogleApiClient();
      //  createLocationRequest();
        buildLocationSettingsRequest();

      //  mainrrr=(RelativeLayout)view.findViewById(R.id.mainrrr);
        mainview = (LinearLayout) view.findViewById(R.id.l1);
        myEvents = (LinearLayout) view.findViewById(R.id.llayout1);
        reEvents = (LinearLayout) view.findViewById(R.id.llayout2);
        myspace = (LinearLayout) view.findViewById(R.id.msllayout1);
        myPreview = (LinearLayout) view.findViewById(R.id.llayout3);
        myeventsview = (LinearLayout) view.findViewById(R.id.myevent);
        myspaceview = (LinearLayout) view.findViewById(R.id.myspace);
      //  remview = (LinearLayout) view.findViewById(R.id.recommevent);
        mypreviewView = (LinearLayout) view.findViewById(R.id.mypreview);
      //  cat_viewall = (TextView) view.findViewById(R.id.category_vwall);
        mDilatingDotsProgressBar = (DilatingDotsProgressBar) view.findViewById(R.id.progress);

        appBarLayout = (AppBarLayout) view.findViewById(R.id.MyAppbar);
        collapsingToolbarLayout = (CollapsingToolbarLayout) view.findViewById(R.id.toolbar_layout);
        toolbar_title = (TextView) view.findViewById(R.id.title_text);
        user_login = (ImageView) view.findViewById(R.id.title_right_ic);
        act_bar_view = (View) view.findViewById(R.id.sec_action_bar);
       // pguide = (ConstraintLayout) view.findViewById(R.id.pguide);
      //  search = (TextView) view.findViewById(R.id.search);
       // text_search = (TextView) view.findViewById(R.id.text_search);
      //  qrsearch = (TextView) view.findViewById(R.id.qrsearch_tab);
        title_left_ic=(ImageView)view.findViewById(R.id.title_left_ic);
        drawerLayout = (DrawerLayout)view.findViewById(R.id.drawer_layout);
        passphrase=(Button)view.findViewById(R.id.passphrase);
     //   navigationview=(NavigationView)view.findViewById(R.id.navigation_view);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);

        //category

        recommdEventsAfterSorting = new ArrayList<>();

        tradeshow = (ConstraintLayout) view.findViewById(R.id.tradeshow);
        school = (ConstraintLayout) view.findViewById(R.id.school);
        community = (ConstraintLayout) view.findViewById(R.id.community);
       /* museum = (LinearLayout) view.findViewById(R.id.museum);
        airports = (LinearLayout) view.findViewById(R.id.airports);
        park = (LinearLayout) view.findViewById(R.id.park);
        personal = (LinearLayout) view.findViewById(R.id.personal);
        association = (LinearLayout) view.findViewById(R.id.association);*/

        tradeshow.setOnClickListener(this);
        school.setOnClickListener(this);
        community.setOnClickListener(this);
     //   cat_viewall.setOnClickListener(this);
     //   pguide.setOnClickListener(this);
     //   search.setOnClickListener(this);
      //  text_search.setOnClickListener(this);
     //   qrsearch.setOnClickListener(this);
        passphrase.setOnClickListener(this);

        /*museum.setOnClickListener(this);
        airports.setOnClickListener(this);
        park.setOnClickListener(this);
        association.setOnClickListener(this);
        personal.setOnClickListener(this);*/
        mainview.setVisibility(View.GONE);
        multieventid=Paper.book().read("MYAPPS",null);
       try{
        if(multieventid!=null) {
            savemyspace();
        }
       else{
            showspace(false);
        }
       }catch (Exception e)
       {

       }

        getEventInfo();

//        mDilatingDotsProgressBar.showNow();


        // listener.disabletab(0);

      /*  title_left_ic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    //drawer is open
                    drawerLayout.closeDrawer(Gravity.LEFT);
                    setleftnavigationitems();
                    //CLOSE Nav Drawer!

                }
                else
                    setleftnavigationitems();
                    drawerLayout.openDrawer(GravityCompat.START);

            }
        });*/
        navdpWidth = getResources().getDimensionPixelSize(R.dimen.abc_action_bar_default_height_material) * 5;
//        margintop = getResources().getDimensionPixelSize(R.dimen.abc_action_bar_default_height_material) + getResources().getDimensionPixelSize(R.dimen.abc_action_bar_default_height_material) * 0.50;
        navheight = navdpWidth * (0.375);
        lwidth1 = navheight * 0.60;
        lheight1 = lwidth * 0.83;

       /* DrawerLayout.LayoutParams params;
        //left nav
        params = (DrawerLayout.LayoutParams) navigationview.getLayoutParams();
        params.width = (int) navdpWidth;
        navigationview.setLayoutParams(params);



    //    navigationview.setItemIconTintList(null);
        View header = navigationview.getHeaderView(0);*/
       // v3 = (RelativeLayout) header.findViewById(R.id.v3);
       // final ImageView coverp = (ImageView) header.findViewById(R.id.coverimage);
        // final ImageView logo = (ImageView) header.findViewById(R.id.logo);

        //RelativeLayout.LayoutParams rparams = (RelativeLayout.LayoutParams) v3.getLayoutParams();
        RelativeLayout.LayoutParams lparams = new RelativeLayout.LayoutParams((int) lwidth, (int) lheight);
       // rparams.width = (int) navdpWidth;
      //  rparams.height = (int) navheight;
      //  v3.setLayoutParams(rparams);
        //logo.setLayoutParams(lparams);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer) {

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


        //setleftnavigationitems();
/*
        navigationview.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                String itemname;
                Menu menu = navigationview.getMenu();
                int n=item.getItemId();
                int gid=item.getGroupId();
            //    menu.getItem(n).setActionView(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
                BottomNavigationView navBar = getActivity().findViewById(R.id.navigation);
                menu.getItem(n).setActionView(null);

                //  int secondLastItem = menu.size()-2;

               */
/* int lastitem = menu.size() - 1;
                if (lastitem == item.getItemId())
                    itemname = "RefreshApp";
*//*

                switch (item.getItemId()) {


                    case 1:
                        // Error_Dialog.show("No Events Found",getActivity());
                        drawerLayout.closeDrawers();
                        navBar.setVisibility(View.GONE);


                        break;
                    case 2:
                        Intent cat;
                        if (Paper.book().read("Islogin", false)) {

                            cat = new Intent(context, MultiEventSearch.class);
//                    cat = new Intent(context, PrivateSearch.class);
                            startActivity(cat);
                            drawerLayout.closeDrawers();




                        } else {
                            cat = new Intent();
                            cat.putExtra("keyMessage", "Please Login to Search or Download the Private Events");
                            cat.putExtra("keyAlert", "Login/Register");
                            cat.setClassName("com.webmobi.eventsapp", "com.webmobi.eventsapp.Views.TokenExpireAlertReceived");
                            cat.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(cat);
                            drawerLayout.closeDrawers();
                        }
                        break;
                    case 3:
                        // Error_Dialog.show("No Events Found",getActivity());
                        drawerLayout.closeDrawers();
                        menu.getItem(n).setActionView(R.layout.nav_right);
                         i1=new Intent(context,HomeScreenMulti.class);
                        i1.putExtra("Event_ID",(multieventlist.get(0).getEvent_id()));
                        i1.putExtra("Event_Name",(multieventlist.get(0).getEvent_name()));
                        i1.putExtra("Event_Logo",(multieventlist.get(0).getMulti_event_logo()));
                        i1.putExtra("Event_Theme",(multieventlist.get(0).getColor_code()));
                        i1.putExtra("Evnet_logo",multieventlist.get(0).getApp_image());
                        startActivity(i1);
                        break;
                    default:
                        if(item.getGroupId()>3)
                        {
                            drawerLayout.closeDrawers();
                            menu.getItem(n).setActionView(R.layout.nav_right);
                            i1 =new Intent(context,HomeScreenMulti.class);
                            String s=multieventlist.get(n-3).getEvent_id();
                            i1.putExtra("Event_ID",(multieventlist.get(n-3).getEvent_id()));
                            i1.putExtra("Event_Name",(multieventlist.get(n-3).getEvent_name()));
                            i1.putExtra("Event_Logo",(multieventlist.get(n-3).getMulti_event_logo()));
                            i1.putExtra("Event_Theme",(multieventlist.get(n-3).getColor_code()));

                            startActivity(i1);

                        }
                }
                return true;
            }
        });
*/


       /* if (DataBaseStorage.isInternetConnectivity(getApplicationContext())) {
            leventDialog = new ProgressDialog(getActivity(), R.style.MyAlertDialogStyle);
            leventDialog.setMessage("Loading...");
            leventDialog.setCanceledOnTouchOutside(false);
            leventDialog.show();
            loadnormalrect(1, false);

        } else {
            showitems(false);
        }*/
        mainview.setVisibility(View.VISIBLE);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    isShow = true;
                    toolbar_title.setVisibility(View.VISIBLE);
                    collapsingToolbarLayout.setTitle("webMOBI");

                } else if (isShow) {
                    isShow = false;
                    toolbar_title.setVisibility(View.GONE);
                  /*  if(savedirs.size()>0) {
                        mainrrr.setVisibility(View.GONE);}
                        else{

                        mainrrr.setVisibility(View.VISIBLE);
                        *//*RelativeLayout.LayoutParams relativeParams = (RelativeLayout.LayoutParams)mainrrr.getLayoutParams();
                        relativeParams.setMargins(0, 80, 0, 0);  // left, top, right, bottom
                        mainrrr.setLayoutParams(relativeParams);
                        mainrrr.setVisibility(View.VISIBLE);
/*

                        RelativeLayout.LayoutParams logo2_layout_params = new RelativeLayout.LayoutParams((int) (ImgWidth * 0.45), (int) (ImgWidth * 0.45));
                        logo2_layout_params.setMargins(0, margintop, 0, 0);
                        logo2_layout_params.addRule(RelativeLayout.CENTER_HORIZONTAL);
                        logo2_layout.setLayoutParams(logo2_layout_params);
*//*



                        *//*float heightDp = getResources().getDisplayMetrics().heightPixels / 3;
                        int lp=(int)heightDp;
                        collapsingToolbarLayout.setMinimumHeight(lp);*//*
                       *//* float heightDp = getResources().getDisplayMetrics().heightPixels / 3;
                        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams)collapsingToolbarLayout.getLayoutParams();
                        lp.height = (int)heightDp;*//*

                    *//*    CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) collapsingToolbarLayout.getLayoutParams();
                        params.height = 3*200; // HEIGHT
                        c

                        collapsingToolbarLayout.setLayoutParams(params);*//*
                        //collapsingToolbarLayout.setExpanded(true);
                      //  CollapsingToolbarLayout.LayoutParams(int width, int height)
                    }*/

                }
            }
        });

        //appBarLayout.setMinimumHeight(400);

        user_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userLogin();
            }
        });
    }

    protected synchronized void buildGoogleApiClient() {

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }


    public void getEventInfo(){

        ProgressDialog  dialog = new ProgressDialog(context, R.style.MyAlertDialogStyle);
        dialog.setMessage("Loading...");
        dialog.show();

        String tag_string_req = "Login";
        String url = ApiList.GetContainerInfo;
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                try {
                    System.out.println(response);
                    JSONObject jObj = new JSONObject(response);
                    if (jObj.getBoolean("response")) {

                   //     Paper.book().write("Container_info1",jObj.getJSONArray("event_app"));
                        Paper.book().write("Multi_info1",jObj.getJSONArray("multi_event_app"));
                        //   Paper.book().write("token", jObj.getJSONObject("responseString").getString("token"));
                         try{
                             parsemulti(jObj.getJSONArray("multi_event_app"));
                           //  parseResult(jObj.getJSONArray("event_app"));
                         }catch (Exception e){}

                    } else {
                        Error_Dialog.show(jObj.getString("responseString"),getActivity());
                    }

                } catch (JSONException e) {
                    Error_Dialog.show(e.toString() ,getActivity());
                }

            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
//                DeviceLog("LOGINUSER",error.toString());
                if (error instanceof TimeoutError) {
                    Error_Dialog.show("Timeout", getActivity());
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, context), getActivity());
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show("Please Check Your Internet Connection",getActivity());
                }
            }
        })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                //params.put("event_id", eid);

                params.put("userid", Paper.book().read("userId"));
                System.out.println(params);
                return params;
            }

           /* @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                // add headers <key,value>
                String credentials = Paper.book().read("usermail") + ":" + password.getText().toString();
                String auth = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                headers.put("Authorization", auth);
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

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();

        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }



   /* private void setleftnavigationitems() {
       try {
           final Menu menu = navigationview.getMenu();
           menu.clear();
           menu.add("Spaces");
           menu.getItem(0).setActionView(null);
           hideleftnaviagtionitem();
       }catch (Exception e)
       {

       }
    }
    private void hideleftnaviagtionitem() {
        final Menu menu = navigationview.getMenu();
        int m=0;

        int size = menu.size();
        menu.add(1,1,0,"webMOBI");
        menu.getItem(1).setIcon(R.drawable.logo);
        menu.getItem(1).setCheckable(true);
        menu.getItem(1).setActionView(null);



        menu.add(2, 2, 0, "Access another space.");
        menu.getItem(2).setIcon(new IconDrawable(getActivity(), FontAwesomeIcons.fa_plus).colorRes(R.color.white));
        menu.getItem(2).setCheckable(true);
        menu.getItem(2).setActionView(null);

        ////// Adding for left multi event
    try {



        loadnormalrect(1,false);
      //  showsavedevents();



     // parsemulti(Paper.book().read("Multi_info"));

       try {
            m= multieventlist.size();
       }catch (Exception e)
       {

       }

        if(m>0){
*//*        for ( i = 0; i < m; i++) {


          if(!multieventlist.get(i).getEvent_name().equalsIgnoreCase(""))
          {
            menu.add(i+3, i+3, 0, multieventlist.get(i).getEvent_name());}
            if(!multieventlist.get(i).getMulti_event_logo().equalsIgnoreCase("")){

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            URL url = new URL(multieventlist.get(i).getMulti_event_logo());
                            Bitmap myBitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());

                            Log.e("keshav", "Bitmap " + myBitmap);
                            menu.getItem(i+3).setIcon(new BitmapDrawable(getResources(), myBitmap));

                        } catch (IOException e) {
                            Log.e("keshav", "Exception " + e.getMessage());
                        }
                    }
                });

          }




           // menu.getItem(i+3).setIcon(new IconDrawable(getActivity(), FontAwesomeIcons.fa_home).colorRes(R.color.white));
            menu.getItem(i+3).setCheckable(true);
            menu.getItem(i+3).setActionView(null);

        }*//*
        }


    }catch (Exception e)
    {
        e.printStackTrace();
    }


        for (int i = 0; i < menu.size() - 1; i++) {
            MenuItem subMenuItem = menu.getItem(i);
            // setting font roboto
            Util.applyFontToMenuItem(subMenuItem, getApplicationContext());
           *//* if (getMenuId(e.getTabs(i).getType()) == 0) {
                menu.getItem(i).setVisible(false);
            }*//*
        }
        MenuItem subMenuItem = menu.getItem(menu.size() - 1);
        Util.applyFontToMenuItem(subMenuItem, getApplicationContext());
    }*/



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (view != null)
            return view;
        //dir
        view = inflater.inflate(R.layout.act_home, container, false);
        dir = getActivity().getFilesDir() + File.separator + "EventsDownload" + File.separator;
        dir_prev = getActivity().getFilesDir() + File.separator + "PreviewDownloaded" + File.separator;

        event_id = getArguments().getString("EVENT_ID");
        app_id = getArguments().getString("APP_ID");

        spf = getActivity().getSharedPreferences(ApiList.LOCALSTORAGE, MODE_PRIVATE);
        regId = Paper.book().read("regId");
        DisplayMetrics displayMetrics = getActivity().getResources().getDisplayMetrics();
        ImgWidth = displayMetrics.widthPixels * 0.30F;
        ImgHeight = ImgWidth * 1.7F;
        lwidth = ImgWidth * 0.70F;
        lheight = lwidth * 0.75F;

        Searchevnets = new ArrayList<>();
        event_dialog = new Dialog(getActivity());

        try{event_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        event_dialog.setContentView(R.layout.event_dialog);
        db = new DatabaseHandler(getActivity());}catch (Exception e){}
        return view;
    }

    private void userLogin() {
        Intent i;


        if (Paper.book().read("Islogin", false)) {

            i = new Intent(context, Profile.class);
            i.setAction(ApiList.loginaction);
            startActivity(i);
        } else {
            i = new Intent(context, RegActivity.class);
            i.setAction(ApiList.loginaction);
            startActivityForResult(i, 40);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    private void parsemulti(JSONArray events) {
        int n=events.length();
        multieventlist =new ArrayList<>();
        multieventid=new ArrayList<>();

       try{
        Gson gson=new Gson();
            for (int i = 0; i < events.length(); i++) {
                String eventString = events.getJSONObject(i).toString();

                MultiEvent obj = gson.fromJson(eventString, MultiEvent.class);
                multieventlist.add(obj);

                //   MultiEvent obj = gson.fromJson(eventString, Event.class);
            }
            if(multieventlist.size()>0)
            {
                showmyspace();
                showspace(true);
            }

            else{
                showspace(false);
            }
           boolean mflag=false;
           try {
               multieventid = Paper.book().read("MYAPPS",null);
               mflag = (multieventid != null && multieventid.size()>0);
           }catch (Exception e)
           {

           }
            if(mflag)
            {
               try {
                   savemyspace();
                   showsavespace(true);
               }catch(Exception e)
               {

               }
            }
            else
            {
                showsavespace(false);
            }

        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    //method to get recommended events lists
    private void loadnormalrect(int pgn, boolean stat) {

        //if stat is true will show the loader in the more tile
        //and hide the more text and icon in the more tile
        if (stat) {
            more_txt.setVisibility(View.GONE);
            more_icon.setVisibility(View.GONE);
            avl.setVisibility(View.VISIBLE);
        }
        String url;

        if (lat == 0.0) {
            //if user denied the location permission this url will be called
            url = ApiList.RecommendEvents + "&page_number=" + pgn;
        } else {
            //if user allows the location permission first it will get the lat & long and this url will be called
            url = ApiList.RecommendEvents + "latitude=" + lat + "&longitude=" + lng + "&distance=10" + "&page_number=" + pgn;
        }

        /*String tag_string_req = "recommend events";
        System.out.println("Home Lat = " + lat + " long = " + lng + " URL : " + url);
        StringRequest strReq = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject json = new JSONObject(response);
                    if (json.getBoolean("response")) {
                        //if the events lists have more than one page this value is set to true
                        //then the list will be appended with load more tile in the end
                        hasNextPage = json.getBoolean("next_page");

                        parseResult(json.getJSONArray("events"));
                    } else {
//                        mDilatingDotsProgressBar.hide();
                        leventDialog.dismiss();
                        showitems(false);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // listener.enableall();
                if (getActivity() != null)
                    mDilatingDotsProgressBar.hideNow();
                if (error instanceof TimeoutError) {
                    Error_Dialog.show("Timeout", getActivity());
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, context), getActivity());
                } else if (VolleyErrorLis.isNetworkProblem(error)) {

                    Error_Dialog.show("Please Check Your Internet Connection", getActivity());
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

*/

       try{
           //parseResult(Paper.book().read("Container_info"));
           getEventInfo();
       }catch (Exception e)
       {

       }

    }



    private void showitems(boolean flag) {
        // listener.enableall();
        if (flag) {
          //  remview.setVisibility(View.GONE);
        } else {
            //remview.setVisibility(View.GONE);
        }
    }

    private void showspace(boolean flag) {
        // listener.enableall();
        if (flag) {
            myspaceview.setVisibility(View.VISIBLE);
        } else {
            myspaceview.setVisibility(View.GONE);
        }
    }
    private void showsavespace(boolean flag) {
        // listener.enableall();
        if (flag) {
            myeventsview.setVisibility(View.VISIBLE);
        } else {
            myeventsview.setVisibility(View.GONE);
        }
    }


    private void parseResult(JSONArray events) {

        try {
            if (events.length() > 0)
//                mDilatingDotsProgressBar.hide(events.length() * 89);
                if (leventDialog != null && leventDialog.isShowing())
                    leventDialog.dismiss();
                else
                    mDilatingDotsProgressBar.hide();


            recommendevents = new ArrayList<>();
            Gson gson = new Gson();
            appurls=new ArrayList<>();

            for (int i = 0; i < events.length(); i++) {
                String eventString = events.getJSONObject(i).toString();
                Event obj = gson.fromJson(eventString, Event.class);

                String appurl = events.getJSONObject(i).getString("appid");
               //Paper.book(obj.getAppid()).write("PrivateKey", obj.getPrivate_key());

              try {
                  if (savedirs.contains(appurl)) {
                      appurls.add(appurl);
                  }
              }catch (Exception E)
              {

              }


                if (savedirs != null) {
                    if (savedirs.contains(appurl))
                        obj.setDownloaded(true);
                    else
                        obj.setDownloaded(false);

                }

                recommendevents.add(obj);
            }
          //  showsavedevents();


            if (recommendevents.size() > 0) {

                /*for (int i = 0; i < recommendevents.size(); i++) {
                    if (recommendevents.get(i).getAppid().equals("a5620fea35a9ef1a6accdd2f6697107ab6df")) {
                        recommdEventsAfterSorting.add(recommendevents.get(i));
                        break;
                    }
                }*/
                recommdEventsAfterSorting.clear();
                for (int i = 0; i < recommendevents.size(); i++) {
                    if (!recommendevents.get(i).getAppid().equals("a5620fea35a9ef1a6accdd2f6697107ab6df")) {
                        recommdEventsAfterSorting.add(recommendevents.get(i));
                    }
                }

                System.out.println("Events List Size  : " + recommendevents.size() + " " + recommdEventsAfterSorting.size());
                showitems(true);
                showrecomevents();
            } else
                showitems(false);


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    //this will show the list of recommended events
    private void showrecomevents() {

        reEvents.removeAllViews();
        int clogo_height = 0, margintop = 0;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for (int i = 0; i < recommdEventsAfterSorting.size(); i++) {
            listing = recommdEventsAfterSorting.get(i);
            final View child = inflater.inflate(R.layout.testing, null);


            RelativeLayout v2 = (RelativeLayout) child.findViewById(R.id.v2);
            FrameLayout.LayoutParams logolayoutParams = (FrameLayout.LayoutParams) v2.getLayoutParams();
            logolayoutParams.width = (int) (ImgWidth * 1.30);
            logolayoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
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
            if (getActivity() != null && isAdded()) {
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
            TextView ecat = (TextView) child.findViewById(R.id.eventcat);
            TextView edate = (TextView) child.findViewById(R.id.eventdate);

            View split_View = (View) child.findViewById(R.id.split_view);
            RelativeLayout.LayoutParams split_view_params = (RelativeLayout.LayoutParams) split_View.getLayoutParams();
            split_view_params.height = (int) (ImgWidth * 1.30);
            split_View.setLayoutParams(split_view_params);

            //setting event title
            etitle.setText(listing.getApp_title());
            //setting event category
            ecat.setText(listing.getApp_category());

            etitle.setTypeface(Util.boldtypeface(context));
            ecat.setTypeface(Util.lighttypeface(context));

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

            //listener for click event
            child.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = (int) view.getTag();
                    if (recommdEventsAfterSorting.size() > 0) {
                        if (recommdEventsAfterSorting.get(pos).getAccesstype().toString().equalsIgnoreCase("discovery")) {
                            //if accesstype is discovery it will navigate to DiscoveryEventDetails.class else it will show the event dialog
                            if (DataBaseStorage.isInternetConnectivity(getApplicationContext())) {
                                Intent i = new Intent(context, DiscoveryEventDetails.class);
                                i.putExtra(DiscoveryEventDetails.DISCOVERY_DETAILS_APPID, recommdEventsAfterSorting.get(pos).getAppid());
                                startActivity(i);
                            } else
                                Error_Dialog.show("Please Check Your Internet Connection", getActivity());
                        } else
                            showEventDialog(pos, recommdEventsAfterSorting);
                    }


                }
            });


            reEvents.addView(child);

        }
        //if the recommended events have next page this block will execute
        //it will add one empty tile with more message
        //if user want to load more events this tile should be clicked
        //if there is no next page it will not appear
        if (hasNextPage) {
            final View empty_child = inflater.inflate(R.layout.empty_tile, null);
            RelativeLayout.LayoutParams layoutParams_txt = new RelativeLayout.LayoutParams((int) (ImgWidth * 1.30), ViewGroup.LayoutParams.MATCH_PARENT);
            layoutParams_txt.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
            empty_child.setLayoutParams(layoutParams_txt);
            avl = (AVLoadingIndicatorView) empty_child.findViewById(R.id.avl);
            more_txt = (TextView) empty_child.findViewById(R.id.more_txt);
            more_icon = (ImageView) empty_child.findViewById(R.id.more_icon);
            more_txt.setVisibility(View.VISIBLE);
            more_icon.setVisibility(View.VISIBLE);
            avl.setVisibility(View.GONE);
            //click listener for more tile
            empty_child.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //increase the page count and call the api again
                    pgn_count++;
                    loadnormalrect(pgn_count, true);
                }
            });
            reEvents.addView(empty_child, recommdEventsAfterSorting.size());
        }
    }
    private void showmyspace() {

        myspace.removeAllViews();
        int clogo_height = 0, margintop = 0;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for (int i = 0; i < multieventlist.size(); i++) {
          //  listing = recommdEventsAfterSorting.get(i);
            final View child = inflater.inflate(R.layout.testing, null);


            RelativeLayout v2 = (RelativeLayout) child.findViewById(R.id.v2);
            ConstraintLayout edateq=(ConstraintLayout)child.findViewById(R.id.edate);
            FrameLayout.LayoutParams logolayoutParams = (FrameLayout.LayoutParams) v2.getLayoutParams();
            logolayoutParams.width = (int) (ImgWidth * 1.30);
            logolayoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            edateq.setVisibility(View.INVISIBLE);
            v2.setLayoutParams(logolayoutParams);
            clogo_height = (int) (ImgHeight * 0.32);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams((int) (ImgWidth * 1.30), clogo_height);


            //used to set the center logo of the tile
            RoundedImageView clogo_center = (RoundedImageView) child.findViewById(R.id.logo2);
            //used to set the tile banner image
            RoundedImageView clogo = (RoundedImageView) child.findViewById(R.id.re_tile_logo);

            layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);


            //setting tile banner

            Glide.with(getApplicationContext()).load((multieventlist.get(i).getApp_image().equalsIgnoreCase("")) ? R.drawable.medium_no_image : multieventlist.get(i).getApp_image())
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
            if (getActivity() != null && isAdded()) {
                margintop = (int) (clogo_height - (getResources().getDimensionPixelSize(R.dimen.abc_action_bar_default_height_material) / 2));
            }
            RelativeLayout.LayoutParams logo2_layout_params = new RelativeLayout.LayoutParams((int) (ImgWidth * 0.45), (int) (ImgWidth * 0.45));
            logo2_layout_params.setMargins(0, margintop, 0, 0);
            logo2_layout_params.addRule(RelativeLayout.CENTER_HORIZONTAL);
            logo2_layout.setLayoutParams(logo2_layout_params);

            clogo_center.setLayoutParams(new RelativeLayout.LayoutParams((int) (ImgWidth * 0.45), (int) (ImgWidth * 0.45)));

            //setting tile center logo
            Glide.with(getApplicationContext()).load((multieventlist.get(i).getMulti_event_logo().equalsIgnoreCase("")) ? R.drawable.default_logo : multieventlist.get(i).getMulti_event_logo())
                    .fitCenter()
                    .placeholder(R.drawable.medium_no_image)
                    .dontAnimate()
                    .error(R.drawable.medium_no_image)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .bitmapTransform(new ColorFilterTransformation(context, Color.argb(0, 0, 0, 0)))
                    .into(clogo_center);
            TextView etitle = (TextView) child.findViewById(R.id.eventname);
            //TextView ecat = (TextView) child.findViewById(R.id.eventcat);
            TextView edate = (TextView) child.findViewById(R.id.eventdate);


            View split_View = (View) child.findViewById(R.id.split_view);
            RelativeLayout.LayoutParams split_view_params = (RelativeLayout.LayoutParams) split_View.getLayoutParams();
            split_view_params.height = (int) (ImgWidth * 1.30);
            split_View.setLayoutParams(split_view_params);

            //setting event title
            etitle.setText(multieventlist.get(i).getEvent_name());
            //setting event category
          //  ecat.setText(listing.getApp_category());

            etitle.setTypeface(Util.boldtypeface(context));
         //   ecat.setTypeface(Util.lighttypeface(context));

         //   String Startsplits = listing.getStart_date().split("T")[0];

        //    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        //    SimpleDateFormat formatter1 = new SimpleDateFormat("EEE MMM dd/yyyy");
           /* try {
                Date date = formatter.parse(Startsplits);
                //setting event date
                edate.setText(formatter1.format(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }*/
            child.setId(i);
            child.setTag(i);

            //listener for click event
            child.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    multieventid=new ArrayList<>();
                    int pos = (int) view.getTag();
                    boolean isavail=false;
                  try {
                      if (multieventlist.size() > 0) {

                          showMultiEventDialog(pos, multieventlist);
                          boolean mflag = false;
                          try {
                              multieventid = Paper.book().read("MYAPPS", null);
                              mflag = (multieventid != null && multieventid.size()>0);
                          } catch (Exception e) {

                          }


                          if (mflag) {
                              //  if (!multieventid.contains(multieventlist.get(pos))) {
                              for (int i = 0; i < multieventid.size(); i++) {
                                  if (multieventid.get(i).getEvent_id().equalsIgnoreCase(multieventlist.get(pos).getEvent_id())) {
                                      isavail = true;
                                      break;

                                  }
                              }
                              if (!isavail) {
                                  multieventid.add(multieventlist.get(pos));
                                  Paper.book().write("MYAPPS", multieventid);
                              }


                              //}
                          } else {
                              multieventid = new ArrayList<>();
                              multieventid.add(multieventlist.get(pos));
                              Paper.book().write("MYAPPS", multieventid);
                          }


                      }
                  }catch (Exception e)
                  {
                      e.printStackTrace();
                  }

                      try {
                          if (multieventid != null) {
                             // updatmyspace();
                              savemyspace();
                          }
                      }catch (Exception e)
                      {

                      }

                   /* Intent i1 =new Intent(context,HomeScreenMulti.class);
                        i1.putExtra("Event_ID",(multieventlist.get(pos).getEvent_id()));
                        i1.putExtra("Event_Name",(multieventlist.get(pos).getEvent_name()));
                        i1.putExtra("Event_Logo",(multieventlist.get(pos).getMulti_event_logo()));
                        i1.putExtra("Event_Theme",(multieventlist.get(pos).getColor_code()));
                        i1.putExtra("Evnet_logo",multieventlist.get(pos).getApp_image());
                        startActivity(i1);*/

                    }




            });


            myspace.addView(child);

        }
        //if the recommended events have next page this block will execute
        //it will add one empty tile with more message
        //if user want to load more events this tile should be clicked
        //if there is no next page it will not appear

    }

    private void savemyspace() {

        boolean mflag=false;
        try {
            multieventid = Paper.book().read("MYAPPS",null);
            mflag = (multieventid != null && multieventid.size()>0);
        }catch (Exception e)
        {

        }

        if(mflag) {
            myEvents.removeAllViews();

            int clogo_height = 0, margintop = 0;

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            for (int i = 0; i < multieventid.size(); i++) {
                //  listing = recommdEventsAfterSorting.get(i);
                final View child = inflater.inflate(R.layout.testing, null);

                RelativeLayout v2 = (RelativeLayout) child.findViewById(R.id.v2);
                ConstraintLayout edateq = (ConstraintLayout) child.findViewById(R.id.edate);
                FrameLayout.LayoutParams logolayoutParams = (FrameLayout.LayoutParams) v2.getLayoutParams();
                logolayoutParams.width = (int) (ImgWidth * 1.30);
                logolayoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                edateq.setVisibility(View.INVISIBLE);
                v2.setLayoutParams(logolayoutParams);
                clogo_height = (int) (ImgHeight * 0.32);
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams((int) (ImgWidth * 1.30), clogo_height);


                //used to set the center logo of the tile
                RoundedImageView clogo_center = (RoundedImageView) child.findViewById(R.id.logo2);
                //used to set the tile banner image
                RoundedImageView clogo = (RoundedImageView) child.findViewById(R.id.re_tile_logo);

                layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

                 for(int p=0; p<multieventlist.size(); p++)
                 {
                     if(multieventid.get(i).getEvent_id().equalsIgnoreCase(multieventlist.get(p).getEvent_id())){
                         temppos=p;
                         break;
                     }

                 }
                //setting tile banner
                Glide.with(getApplicationContext()).load((multieventlist.get(temppos).getApp_image().equalsIgnoreCase("")) ? R.drawable.medium_no_image : multieventlist.get(temppos).getApp_image())
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
                if (getActivity() != null && isAdded()) {
                    margintop = (int) (clogo_height - (getResources().getDimensionPixelSize(R.dimen.abc_action_bar_default_height_material) / 2));
                }
                RelativeLayout.LayoutParams logo2_layout_params = new RelativeLayout.LayoutParams((int) (ImgWidth * 0.45), (int) (ImgWidth * 0.45));
                logo2_layout_params.setMargins(0, margintop, 0, 0);
                logo2_layout_params.addRule(RelativeLayout.CENTER_HORIZONTAL);
                logo2_layout.setLayoutParams(logo2_layout_params);

                clogo_center.setLayoutParams(new RelativeLayout.LayoutParams((int) (ImgWidth * 0.45), (int) (ImgWidth * 0.45)));

                //setting tile center logo
                Glide.with(getApplicationContext()).load((multieventlist.get(temppos).getMulti_event_logo().equalsIgnoreCase("")) ? R.drawable.default_logo : multieventlist.get(temppos).getMulti_event_logo())
                        .fitCenter()
                        .placeholder(R.drawable.medium_no_image)
                        .dontAnimate()
                        .error(R.drawable.medium_no_image)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .bitmapTransform(new ColorFilterTransformation(context, Color.argb(0, 0, 0, 0)))
                        .into(clogo_center);
                TextView etitle = (TextView) child.findViewById(R.id.eventname);
                //TextView ecat = (TextView) child.findViewById(R.id.eventcat);
                TextView edate = (TextView) child.findViewById(R.id.eventdate);


                View split_View = (View) child.findViewById(R.id.split_view);
                RelativeLayout.LayoutParams split_view_params = (RelativeLayout.LayoutParams) split_View.getLayoutParams();
                split_view_params.height = (int) (ImgWidth * 1.30);
                split_View.setLayoutParams(split_view_params);

                //setting event title
                etitle.setText(multieventlist.get(temppos).getEvent_name());
                //setting event categoryecat.setText(listing.getApp_category());
                //

                etitle.setTypeface(Util.boldtypeface(context));
                //   ecat.setTypeface(Util.lighttypeface(context));

                //   String Startsplits = listing.getStart_date().split("T")[0];

                //    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                //    SimpleDateFormat formatter1 = new SimpleDateFormat("EEE MMM dd/yyyy");
           /* try {
                Date date = formatter.parse(Startsplits);
                //setting event date
                edate.setText(formatter1.format(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }*/
                child.setId(i);
                child.setTag(i);

                //listener for click event
                child.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int pos = (int) view.getTag();
                        if (multieventid.size() > 0) {
                            Intent i1;
                            for(int k=0; k<multieventlist.size(); k++)
                            {
                                if(multieventid.get(pos).getEvent_id().equalsIgnoreCase(multieventlist.get(k).getEvent_id())){
                                    temppos1=k;
                                    break;
                                }

                            }
                          try  {showMultiEventDialog(pos,multieventid);}catch (Exception e){

                          }
                           /* i1 = new Intent(context, HomeScreenMulti.class);

                            i1.putExtra("Event_ID", (multieventlist.get(temppos1).getEvent_id()));
                            i1.putExtra("Event_Name", (multieventlist.get(temppos1).getEvent_name()));
                            i1.putExtra("Event_Logo", (multieventlist.get(temppos1).getMulti_event_logo()));
                            i1.putExtra("Event_Theme", (multieventlist.get(temppos1).getColor_code()));
                            i1.putExtra("Evnet_logo", multieventlist.get(temppos1).getApp_image());
                            startActivity(i1);*/
                        }


                    }
                });

                child.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        String tag = String.valueOf(v.getTag());
                      int n=Integer.parseInt(tag);
                       /* eventDir = new File(dir + tag);
                        jsonFile = new File(eventDir, filename);*/

                        //opens dialog to delete an event
                          dialogToDeleteEvent(n,child);
                        // myEvents.removeView(child);

                        if (myEvents.getChildCount() == 0)
                            myeventsview.setVisibility(View.GONE);
                        // Toast.makeText(context, "Event Removed", Toast.LENGTH_LONG).show();


             /*   try {


                    deleteFile(eventDir);
                } catch (IOException e) {
                    e.printStackTrace();
                }


                myEvents.removeView(child);
                if (myEvents.getChildCount() == 0)
                    myeventsview.setVisibility(View.GONE);
                Toast.makeText(context, "Event Removed", Toast.LENGTH_LONG).show();*/
                        return true;
                    }
                });



                myEvents.addView(child);

            }
            //if the recommended events have next page this block will execute
            //it will add one empty tile with more message
            //if user want to load more events this tile should be clicked
            //if there is no next page it will not appear

        }
    }



    @Override
    public void onClick(View view) {

        Intent cat;
        switch (view.getId()) {
            case R.id.tradeshow:
                cat = new Intent(getActivity(), EventCategory.class);
                cat.putExtra("categoryname", "Trade Show and Events");
                startActivity(cat);
                break;
            case R.id.school:
                cat = new Intent(getActivity(), EventCategory.class);
                cat.putExtra("categoryname", "Schools");
                startActivity(cat);
                break;
            case R.id.community:
                cat = new Intent(getActivity(), EventCategory.class);
                cat.putExtra("categoryname", "Community Centers");
                startActivity(cat);
                break;
          /*  case R.id.category_vwall:
                cat = new Intent(getActivity(), AllCategory.class);
                startActivity(cat);
                break;*/
           /* case R.id.pguide:
                if (Paper.book().read("Islogin", false)) {
                    cat = new Intent(context, PrivateSearchActivity.class);
//                    cat = new Intent(context, PrivateSearch.class);
                    startActivity(cat);
                } else {
                    cat = new Intent();
                    cat.putExtra("keyMessage", "Please Login to Search or Download the Private Events");
                    cat.putExtra("keyAlert", "Login/Register");
                    cat.setClassName("com.webmobi.eventsapp", "com.webmobi.eventsapp.Views.TokenExpireAlertReceived");
                    cat.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(cat);
                }
                break;*/
           /* case R.id.search:
//                cat = new Intent(context, DisCover_Search.class);
                cat = new Intent(context, SearchActivity.class);
                startActivity(cat);
                break;*/
            /*case R.id.text_search:
//                cat = new Intent(context, DisCover_Search.class);
                cat = new Intent(context, SearchActivity.class);
                startActivity(cat);
                break;*/
            case R.id.passphrase:
//                cat = new Intent(context, DisCover_Search.class);
                cat = new Intent(context, SearchActivity.class);
                startActivity(cat);
                break;

            case R.id.whatsapp_dlg:
                shareOnWhatsapp();
                break;
            case R.id.custom_dlg_ok:
                if (isWhatsApp) {
                    openPlayStore("com.whatsapp");
                }
                break;
            case R.id.fb_dlg:
                shareonFacebook();
                break;
            case R.id.twitter_dlg:
                shareOnTwitter();
                break;
            /*case R.id.qrsearch_tab:
                //checking the camera permission
                checkCameraPermission();
                break;*/
            /*case R.id.personal:
                cat = new Intent(getActivity(), EventCategory.class);
                cat.putExtra("categoryname", "Personal Events");
                startActivity(cat);

                break;
            case R.id.association:
                cat = new Intent(getActivity(), EventCategory.class);
                cat.putExtra("categoryname", "Associations");
                startActivity(cat);
                break;
            case R.id.airports:
                cat = new Intent(getActivity(), EventCategory.class);
                cat.putExtra("categoryname", "Airports");
                startActivity(cat);
                break;
            case R.id.park:
                cat = new Intent(getActivity(), EventCategory.class);
                cat.putExtra("categoryname", "National Park");
                startActivity(cat);
                break;*/
        }
    }

    private void checkCameraPermission() {
        //check the version whether Marshmallow or higher
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            // only for marsemellow and newer versions
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION);
            } else {

                //Calls the SimpleScannerActivity
                Intent intent = new Intent(getActivity(), SimpleScannerActivity.class);
                startActivityForResult(intent, SCANNER_REQUEST_CODE);
            }
        } else {

            //Lower Versions dont need run time permission
            Intent intent = new Intent(getActivity(), SimpleScannerActivity.class);
            startActivityForResult(intent, SCANNER_REQUEST_CODE);
        }
    }


    private void shareOnTwitter() {
        boolean isTwitterAvailable = appInstalledOrNot("com.twitter.android");
        if (isTwitterAvailable) {
            twitterAuthClient.authorize(getActivity(), new com.twitter.sdk.android.core.Callback<TwitterSession>() {
                @Override
                public void success(Result<TwitterSession> result) {
                    TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();
                    Intent twitterIntent = new ComposerActivity.Builder(getApplicationContext())
                            .session(session)
                            .text(getShareTextContent())
                            .hashtags("webMOBI")
                            .createIntent();
                    startActivity(twitterIntent);
                }

                @Override
                public void failure(TwitterException exception) {
                    System.out.println("Twitter error: " + exception);

                }
            });
        } else {
            isTwitter = true;
            displayDialog("Alert", "Twitter have not been installed");

        }
    }

    private boolean appInstalledOrNot(String uri) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
        }

        return false;
    }

    private void shareonFacebook() {
        ShareDialog shareDialog = new ShareDialog(getActivity());
        if (shareDialog.canShow(ShareLinkContent.class)) {

            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setContentUrl(Uri.parse("https://www.webmobi.com/"))
                    .setQuote(getShareTextContent())
                    .build();
            shareDialog.show(linkContent);  // Show facebook ShareDialog
        }

    }


    private void shareOnWhatsapp() {
        String shareText = getShareTextContent();
        System.out.println("Shared Text : " + shareText);
        Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
        whatsappIntent.setType("text/plain");
        whatsappIntent.setPackage("com.whatsapp");
        whatsappIntent.putExtra(Intent.EXTRA_TEXT, shareText);
        try {
            startActivity(whatsappIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            isWhatsApp = true;
            displayDialog("Alert", "Whatsapp has not been installed");
        }

    }

    private String getShareTextContent() {
        StringBuilder shareText = new StringBuilder();

        shareText.append("Name :");
        shareText.append(share_event_title);
        shareText.append("\nDate :");
        shareText.append(share_event_date);
        shareText.append("\nLocation :");
        shareText.append(share_location);
        shareText.append("\nSearch Key :");
        shareText.append(share_search_key);
        shareText.append("\nhttps://webmobi.com");

        return shareText.toString();
    }

    private void openPlayStore(String s) {
        dialog.dismiss();
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + s)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + s)));
        }
    }

    private void displayDialog(String dlg_title_str, String dlg_msg_str) {
        callDialog();
        dlg_title.setText(dlg_title_str);
        dlg_msg.setText(dlg_msg_str);
        dialog.show();
    }

    public void callDialog() {
        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog);
        dlg_title = (TextView) dialog.findViewById(R.id.custom_dlg_title);
        dlg_msg = (TextView) dialog.findViewById(R.id.custom_dlg_msg);
        dlg_ok = (TextView) dialog.findViewById(R.id.custom_dlg_ok);
        dialog.setCancelable(false);
        dlg_ok.setOnClickListener(this);
    }
    // geting the downloaded events


    private void showsavedevents() {

        myEvents.removeAllViews();
        savedirs = new ArrayList<>();
        savedirs = getSaveDirs(dir);
        Searchevnetstemp = new ArrayList<>();


        try{
           Searchevnetstemp= Paper.book().read("PrivateEventInfo");

            if(Searchevnetstemp.size()>0){
                for(int i=0; i<Searchevnetstemp.size(); i++)
                {
                    if(!appurls.contains(Searchevnetstemp.get(i).getAppid())){
                    appurls.add(Searchevnetstemp.get(i).getAppid());}
                }
            }

        }catch (Exception e)
        {

        }

        if (savedirs.size() > 0) {
            try {

                if (appurls.size() > 0) {
                    loadSavedEvents(appurls);
                    myeventsview.setVisibility(View.VISIBLE);
                }
            }catch (Exception e)
            {

            }
           // loadSavedEvents(savedirs);
          //  myeventsview.setVisibility(View.VISIBLE);
        } else {
            myeventsview.setVisibility(View.GONE);
        }
    }

    //getting saved preview events //method for preview
    private void showPreviewSavedEvents() {

        myPreview.removeAllViews();
        savePrevdirs = new ArrayList<>();
        savePrevdirs = getSaveDirs(dir_prev);

        if (savePrevdirs.size() > 0) {
            loadPreviewSavedEvents(savePrevdirs);
            mypreviewView.setVisibility(View.GONE);
        } else {
            mypreviewView.setVisibility(View.GONE);
        }
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


    private void loadSavedEvents(List<String> savedDirs) {
        for (String dir : savedDirs)
            addToSaved(dir, false);
    }

    //method for preview
    private void loadPreviewSavedEvents(List<String> savedDirs) {
        for (String dir : savedDirs)
            addToPreviewSaved(dir, false);
    }

    //method for preview
    public void addToPreviewSaved(String tag, boolean b) {

        List<String> text = new ArrayList<>();
        eventDir = new File(dir_prev + tag);
        descFile = new File(eventDir, "description.txt");

        try {
            text = Files.readLines(descFile, Charset.defaultCharset());
            System.out.println("File Values preview " + text);
        } catch (IOException e) {
            e.printStackTrace();
        }


        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View child = inflater.inflate(R.layout.testing, null);

        RelativeLayout mainContent = (RelativeLayout) child.findViewById(R.id.v2);
        FrameLayout.LayoutParams mainContentLayoutParams = (FrameLayout.LayoutParams) mainContent.getLayoutParams();
        /*mainContentLayoutParams.width = (int) (ImgWidth * 1.30);
        mainContentLayoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;*/
//        mainContentLayoutParams.setMargins(20, 20, 20, 20);
        mainContent.setLayoutParams(mainContentLayoutParams);
        RelativeLayout v2 = (RelativeLayout) child.findViewById(R.id.v2);
//        CardView v2 = (CardView) child.findViewById(R.id.main_content);
        //  FrameLayout.LayoutParams logolayoutParams = (FrameLayout.LayoutParams) v2.getLayoutParams();
        FrameLayout.LayoutParams logolayoutParams = (FrameLayout.LayoutParams) v2.getLayoutParams();
        logolayoutParams.width = (int) (ImgWidth * 1.30);
        logolayoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        v2.setLayoutParams(logolayoutParams);
        int clogo_height = (int) (ImgHeight * 0.30);

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams((int) (ImgWidth * 1.30), clogo_height);
//        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams((int) (ImgWidth * 1.10), (int) (ImgHeight * 0.65));

        RoundedImageView clogo_center = (RoundedImageView) child.findViewById(R.id.logo2);
        RoundedImageView clogo = (RoundedImageView) child.findViewById(R.id.re_tile_logo);

        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        clogo.setLayoutParams(layoutParams);
        clogo.setCornerRadius(7, 7, 0, 0);
        TextView etitle = (TextView) child.findViewById(R.id.eventname);
        TextView ecat = (TextView) child.findViewById(R.id.eventcat);
        TextView edate = (TextView) child.findViewById(R.id.eventdate);
        etitle.setText(text.get(5));
        ecat.setText(text.get(1));

        etitle.setTypeface(Util.boldtypeface(context));
        ecat.setTypeface(Util.lighttypeface(context));

        RelativeLayout logo2_layout = (RelativeLayout) child.findViewById(R.id.logo2_layout);
        int margintop = (int) (clogo_height - (getResources().getDimensionPixelSize(R.dimen.abc_action_bar_default_height_material) / 2));
        RelativeLayout.LayoutParams logo2_layout_params = new RelativeLayout.LayoutParams((int) (ImgWidth * 0.45), (int) (ImgWidth * 0.45));
        logo2_layout_params.setMargins(0, margintop, 0, 0);
        logo2_layout_params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        logo2_layout.setLayoutParams(logo2_layout_params);

        clogo_center.setLayoutParams(new RelativeLayout.LayoutParams((int) (ImgWidth * 0.45), (int) (ImgWidth * 0.45)));
        Glide.with(getApplicationContext()).load((text.get(7).equalsIgnoreCase("")) ? R.drawable.logo : text.get(7))
                .fitCenter()
                .placeholder(R.drawable.medium_no_image)
                .dontAnimate()
                .error(R.drawable.medium_no_image)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .bitmapTransform(new ColorFilterTransformation(context, Color.argb(0, 0, 0, 0)))
                .into(clogo_center);

        View split_View = (View) child.findViewById(R.id.split_view);
        RelativeLayout.LayoutParams split_view_params = (RelativeLayout.LayoutParams) split_View.getLayoutParams();
        split_view_params.height = (int) (ImgWidth * 1.30);
        split_View.setLayoutParams(split_view_params);

        String Startsplits = text.get(2).split("T")[0];

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatter1 = new SimpleDateFormat("EEE MMM dd/yyyy");
        try {
            Date date = formatter.parse(Startsplits);
            edate.setText(formatter1.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Glide.with(getApplicationContext()).load((text.get(9).equalsIgnoreCase("")) ? R.drawable.medium_no_image : text.get(9))
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

                eventDir = new File(dir_prev + tag);
                jsonFile = new File(eventDir, filename);
                System.out.println("Loaded event");
                try {
                    child.setEnabled(false);
                    if (DataBaseStorage.isInternetConnectivity(getApplicationContext()))
                        showjsPrev(jsonFile);
                    else {
                        openOffline(jsonFile, true);
                        Intent intent = new Intent(context, SingleEventHome.class);
                        intent.putExtra("Engagement","");
                        intent.putExtra("back","APPS");
                        startActivity(intent);
                    }
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
                eventDir = new File(dir_prev + tag);
                jsonFile = new File(eventDir, filename);

                //opens dialog to delete an event
               // dialogToDeleteEvent(eventDir, child, myPreview);

                //myPreview.removeView(child);
                if (myPreview.getChildCount() == 0)
                    mypreviewView.setVisibility(View.GONE);


              /*  try {
                    deleteFile(eventDir);
                } catch (IOException e) {
                    e.printStackTrace();
                }*/


                return true;
            }
        });

        myPreview.addView(child);


        if (b) {
            eventDir = new File(dir_prev + tag);
            jsonFile = new File(eventDir, filename);

            try {
                showjs(jsonFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void dialogToDeleteEvent(int n, final View child) {


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.MyAlertDialogStyle);
        builder.setMessage(R.string.dialog_fire_missiles)
                .setPositiveButton(R.string.fire, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!
                        try {
                          //  deleteFile(file);
                            multieventid.remove(n);
                            Paper.book().write("MYAPPS",multieventid);
                            myEvents.removeView(child);
                            if (myEvents.getChildCount() == 0)
                                myspaceview.setVisibility(View.GONE);
                            refreshFragment();

                           /* if (llParentView.getId() == myEvents.getId()) {

                                myEvents.removeView(child);
                                if (myEvents.getChildCount() == 0)
                                    myeventsview.setVisibility(View.GONE);
                                refreshFragment();
                            } else if (llParentView.getId() == myPreview.getId()) {

                                myPreview.removeView(child);
                                Paper.book(child.getTag().toString()).delete("isPreview");
                                if (myPreview.getChildCount() == 0)
                                    mypreviewView.setVisibility(View.GONE);
                            }*/


                        } catch (Exception e) {

                            e.printStackTrace();

                        }

                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog

                    }
                });
        // Create the AlertDialog object and return it
        builder.create();
        builder.show();
    }
    public void getCategoryEvents(String url,String event_id) {
        String tag_string_req = "category events";
        final String eid=event_id;
        final ProgressDialog dialog = new ProgressDialog(getApplicationContext(), R.style.MyAlertDialogStyle);
        dialog.setMessage("Loading Details...");
        dialog.show();
        url = url.replaceAll(" ", "%20");

        StringRequest strReq = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                //mDilatingDotsProgressBar.hideNow();
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
                dialog.dismiss();
                if (error instanceof TimeoutError) {
                    Error_Dialog.show("Timeout", getActivity());
                } else if (com.singleevent.sdk.Custom_View.VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(com.singleevent.sdk.Custom_View.VolleyErrorLis.handleServerError(error, getActivity()), getActivity());
                } else if (com.singleevent.sdk.Custom_View.VolleyErrorLis.isNetworkProblem(error)) {
                    //     view_no_Internet.setVisibility(View.VISIBLE);
                    final Dialog derror = Util.showinternt(getActivity());
                    TextView txttitle = (TextView) derror.findViewById(com.singleevent.sdk.R.id.retry);
                    txttitle.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            derror.dismiss();
                            getCategoryEvents(ApiList.MultiEvents,eid);
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
    private void parseMultiResult(JSONArray events) {

        try {

            /*categoryevents.clear();*/
            Gson gson = new Gson();

            for (int i = 0; i < events.length(); i++) {
                String eventString = events.getJSONObject(i).toString();
                Event obj = gson.fromJson(eventString, Event.class);

                String appurl = events.getJSONObject(i).getString("appid");
                Paper.book(appurl).write("InfoPrivacy",obj.getInfo_privacy());
                if (savedirs.contains(appurl))
                    obj.setDownloaded(true);
                else
                    obj.setDownloaded(false);

                categoryevents.add(obj);
            }


            if (categoryevents.size() > 0) {
                showitems(true);
                showrecomevents();
            } else
                showitems(false);


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }




    public void addToSaved(String tag, boolean b) {

        List<String> text = new ArrayList<>();
        eventDir = new File(dir + tag);
        descFile = new File(eventDir, "description.txt");

        try {
            text = Files.readLines(descFile, Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < text.size(); i++)
            System.out.println("Text Value [ " + i + "] : " + text.get(i));
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View child = inflater.inflate(R.layout.testing, null);


        RelativeLayout v2 = (RelativeLayout) child.findViewById(R.id.v2);
//        CardView v2 = (CardView) child.findViewById(R.id.main_content);
        //  FrameLayout.LayoutParams logolayoutParams = (FrameLayout.LayoutParams) v2.getLayoutParams();
        FrameLayout.LayoutParams logolayoutParams = (FrameLayout.LayoutParams) v2.getLayoutParams();
        logolayoutParams.width = (int) (ImgWidth * 1.30);
        logolayoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        v2.setLayoutParams(logolayoutParams);

        int clogo_height = (int) (ImgHeight * 0.30);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams((int) (ImgWidth * 1.30), clogo_height);

        RoundedImageView clogo_center = (RoundedImageView) child.findViewById(R.id.logo2);
        RoundedImageView clogo = (RoundedImageView) child.findViewById(R.id.re_tile_logo);

        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        clogo.setLayoutParams(layoutParams);
        clogo.setCornerRadius(7, 7, 0, 0);
        TextView etitle = (TextView) child.findViewById(R.id.eventname);
        TextView ecat = (TextView) child.findViewById(R.id.eventcat);
        TextView edate = (TextView) child.findViewById(R.id.eventdate);
        etitle.setText(text.get(5));
        ecat.setText(text.get(1));

        etitle.setTypeface(Util.boldtypeface(context));
        ecat.setTypeface(Util.lighttypeface(context));
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

        String Startsplits = text.get(2).split("T")[0];

        clogo_center.setLayoutParams(new RelativeLayout.LayoutParams((int) (ImgWidth * 0.45), (int) (ImgWidth * 0.45)));
        Glide.with(getApplicationContext()).load((text.get(7).equalsIgnoreCase("")) ? R.drawable.medium_no_image : text.get(7))
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


        Glide.with(getApplicationContext()).load((text.get(9).equalsIgnoreCase("")) ? R.drawable.medium_no_image : text.get(9))
                .fitCenter()
                .dontAnimate()
                .placeholder(R.drawable.medium_no_image)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.medium_no_image)
                .bitmapTransform(new ColorFilterTransformation(context, Color.argb(0, 0, 0, 0)))
                .into(clogo);


        child.setTag(tag);
        final List<String> finalText = text;
        child.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String tag = String.valueOf(view.getTag());
                openEvent(tag);
            }
        });


        child.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                String tag = String.valueOf(v.getTag());
                eventDir = new File(dir + tag);
                jsonFile = new File(eventDir, filename);

                //opens dialog to delete an event
             //   dialogToDeleteEvent(eventDir, child, myEvents);
                // myEvents.removeView(child);

                if (myEvents.getChildCount() == 0)
                    myeventsview.setVisibility(View.GONE);
                // Toast.makeText(context, "Event Removed", Toast.LENGTH_LONG).show();


             /*   try {


                    deleteFile(eventDir);
                } catch (IOException e) {
                    e.printStackTrace();
                }


                myEvents.removeView(child);
                if (myEvents.getChildCount() == 0)
                    myeventsview.setVisibility(View.GONE);
                Toast.makeText(context, "Event Removed", Toast.LENGTH_LONG).show();*/
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

    private void openEvent(String tag) {
        eventDir = new File(dir + tag);
        jsonFile = new File(eventDir, filename);
        System.out.println("Event Fragment File Name : " + jsonFile);
        String contents = null;

        try {
            contents = Files.toString(jsonFile, Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace();
        }


            Gson gson = new Gson();


            JSONObject args = null;
            try {
                args = new JSONObject(contents);
                AppDetails appDetails = gson.fromJson(args.toString(), AppDetails.class);
            //check whether the app is online or offline
            if (DataBaseStorage.isInternetConnectivity(getApplicationContext())) {
                //check whether the forceupdate is on/off
                if (appDetails.getForceUpdate())
                    forceUpdate(appDetails, tag);
                else
                    showjs(jsonFile);
            } else {
                //once app is offline this method will open the event in offline
                openOffline(jsonFile, false);
                Intent sending = new Intent(getActivity(), SingleEventHome.class);
                sending.putExtra("Engagement","");
                sending.putExtra("back","APPS");
                startActivity(sending);
            }

        } catch (Exception e) {

        }

    }

    private JSONObject getAppDetailsFromJSON(String contents) {

        JSONObject args = null;
        Gson gson = new Gson();
        try {
            args = new JSONObject(contents);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return args;

    }

    private void openOffline(File jsonFile, boolean isPreview) throws IOException {
        //Loading the event in offline mode

        ProgressDialog dialog = new ProgressDialog(context, R.style.MyAlertDialogStyle);
        dialog.setMessage("Loading Event..");
        dialog.show();

        //getting json from file
        String contents = Files.toString(jsonFile, Charset.defaultCharset());
        System.out.println("File Contents : " + contents);

        offlineevents = new ArrayList<>();
        Gson gson = new Gson();


        JSONObject args = null;
        try {
            args = getAppDetailsFromJSON(contents);
            AppDetails obj = gson.fromJson(args.toString(), AppDetails.class);

            //storing the appdetails locally
            Paper.book().write("Appdetails", obj);

            JSONArray eventslist = args.getJSONArray("events");
            String eventString = eventslist.getJSONObject(0).toString();
            Events eobj = gson.fromJson(eventString, Events.class);
            offlineevents.add(eobj);
            Paper.book().write("Appevents", offlineevents);

            //for showing preview in home screen of event
            if (isPreview)
                Paper.book(obj.getAppId()).write("isPreview", true);
            else
                Paper.book(obj.getAppId()).write("isPreview", false);
            dialog.dismiss();
            //parsing the result
            parseresult(args, obj.getAppId());


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void forceUpdate(AppDetails appDetails, String appid) {
        String query = "";
        try {
            query = URLEncoder.encode(appDetails.getAppUrl(), "utf-8");
            String jsonUrl = baseUrl + query + "/appData.json";

            updateMyEventJsonBeforeOpening(appDetails, jsonUrl);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String jsonUrl = "";
    }

    private void updateMyEventJsonBeforeOpening(final AppDetails appDetails, String jsonUrl) {

        final ProgressDialog pDialog = new ProgressDialog(getActivity(), R.style.MyAlertDialogStyle);
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
                  /*  if (res.getInt("version") != appDetails.getVersion()) {

                    } else {
                        Error_Dialog.show("No Updates Available", SingleEventHome.this);
                    }*/
                    eventDir = new File(dir + appDetails.getAppId());
                    jsonFile = new File(eventDir, filename);
                    jsonFile.delete();
                    descFile = new File(eventDir, "description.txt");

                    Files.write(jsonResponse, jsonFile, Charset.defaultCharset());

                    /*byte[] bytes = new byte[0];
                    try {
                        bytes = response.toString().getBytes("UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    HashMap<String, byte[]> map = DataEncryption.encryptBytes(bytes, DataEncryption.events_logo_pass_enc);

                    FileOutputStream fis = null;
                    try {
                        fis = new FileOutputStream(new File(String.valueOf(jsonFile)));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    ObjectOutputStream oos = null;
                    try {
                        oos = new ObjectOutputStream(fis);
                        oos.writeObject(map);
                        oos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
*/
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
                    Error_Dialog.show("Timeout", getActivity());
                } else if (com.singleevent.sdk.Custom_View.VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(com.singleevent.sdk.Custom_View.VolleyErrorLis.handleServerError(error, getActivity()), getActivity());
                } else if (com.singleevent.sdk.Custom_View.VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show("Please Check Your Internet Connection", getActivity());
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


    private void deleteFile(File dir) throws IOException {

        String[] entries = dir.list();
        for (String s : entries) {
            File currentFile = new File(dir.getPath(), s);
            currentFile.delete();
        }
        boolean dirdel = dir.delete();

    }

    // Method for preview
    private void showjsPrev(File jsonFile) throws IOException {
        ProgressDialog dialog = new ProgressDialog(context, R.style.MyAlertDialogStyle);
        dialog.setMessage("Loading Event..");
        dialog.show();

        String contents = Files.toString(jsonFile, Charset.defaultCharset());
        System.out.println("File Contents : " + contents);

        previewEventsToDisplay = new ArrayList<>();
        Gson gson = new Gson();


        JSONObject args = null;
        try {
            args = new JSONObject(contents);
            AppDetails obj = gson.fromJson(args.toString(), AppDetails.class);
            Paper.book().write("Appdetails", obj);
            JSONArray eventslist = args.getJSONArray("events");
            String eventString = eventslist.getJSONObject(0).toString();
            Events eobj = gson.fromJson(eventString, Events.class);
            previewEventsToDisplay.add(eobj);
            Paper.book().write("Appevents", previewEventsToDisplay);
            dialog.dismiss();

            //for showing preview in home screen of event
            Paper.book(obj.getAppId()).write("isPreview", true);

            int info_privacy = Paper.book(obj.getAppId()).read("InfoPrivacy", 0);
            //if user is logged in
            if (Paper.book().read("Islogin", false)) {

                if (info_privacy == 0) {
                    //call checkin to check whether user is registered or not
                    FetchPublicUSerdata(obj.getAppId());
                } else {
                    //info_privacy is 1 call single event login
                    FetchPrivateUserData(obj.getAppId(), info_privacy);
                }

            } else {
                //if user is not logged in
                if (info_privacy == 0) {
                    Intent intent = new Intent(context, SingleEventHome.class);
                    intent.putExtra("Engagement","");
                    intent.putExtra("back","APPS");
                    startActivity(intent);
                } else {
                    //info_privacy is 1
                    Intent intent = new Intent(context, LoginActivity.class);
                    startActivity(intent);
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private void showjs(File jsonFile) throws IOException {
        ProgressDialog dialog = new ProgressDialog(context, R.style.MyAlertDialogStyle);
        dialog.setMessage("Loading Event..");
        dialog.show();

        String contents = Files.toString(jsonFile, Charset.defaultCharset());
        /*String contents = null;
        byte[] decrypted = new byte[0];

        try {
//                    contents = Files.toString(jsonFile, Charset.defaultCharset());
            FileInputStream fis = new FileInputStream(jsonFile);
            ObjectInputStream ois = new ObjectInputStream(fis);

            try {
                decrypted = DataEncryption.decryptData((HashMap<String, byte[]>) ois.readObject(),
                        DataEncryption.events_logo_pass_enc);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            contents=new String(decrypted);
            System.out.println("JSON File Value : " + contents);
            System.out.println("JSON File Value : " + Files.toString(jsonFile, Charset.defaultCharset()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("File Contents : " + contents);*/

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
            dialog.dismiss();

            //for hiding preview in main screen of event
//            Paper.book(obj.getAppId()).write("isPreview", false);
            if (isPreview)
                Paper.book(obj.getAppId()).write("isPreview", true);
            else
                Paper.book(obj.getAppId()).write("isPreview", false);
            isPreview = false;
            int info_privacy = Paper.book(obj.getAppId()).read("InfoPrivacy", 0);

            //if user is logged in
            if (Paper.book().read("Islogin", false)) {

                if (info_privacy == 0) {
                    //call checkin to check whether user is registered or not
                    FetchPublicUSerdata(obj.getAppId());
                } else {

                    //info_privacy is 1 call single event login
                    FetchPrivateUserData(obj.getAppId(), info_privacy);
                }


            } else {
                //if user is not logged in
                if (info_privacy == 0) {
                    Intent intent = new Intent(context, SingleEventHome.class);
                    intent.putExtra("Engagement","");
                    intent.putExtra("back","APPS");
                    startActivity(intent);
                } else {
                    //info_privacy is 1
                    Intent intent = new Intent(context, LoginActivity.class);
                    startActivity(intent);
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
            dialog.dismiss();
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

    public void updaterecdownloadevents(int id, String appid) {

       try {
           Event update = recommdEventsAfterSorting.get(id);
           update.setDownloaded(true);

           myeventsview.setVisibility(View.VISIBLE);
           addToSaved(appid, true);
       }catch (Exception e)
       {

       }


    }

    @Override
    public void onResume() {
        super.onResume();
        boolean mflag=false;
        try {
            multieventid = Paper.book().read("MYAPPS",null);
            mflag = (multieventid != null && multieventid.size()>0);
        }catch (Exception e)
        {

        }


           if (mflag) {
               try {
                   savemyspace();
                   showsavespace(true);

               } catch (Exception e) {

               }
           } else {
               showsavespace(false);
           }



        // showsavedevents();
       // showPreviewSavedEvents();

       try {
           if (DataBaseStorage.isInternetConnectivity(context))
               if (event_id == null || event_id.equalsIgnoreCase("")) {
//            doSmth(event_id);
                   if (!(app_id == null || app_id.equalsIgnoreCase("")))
                       openNotificationPage();

               } else {
                   doSmth(event_id, false);
               }
       }catch (Exception e)
       {

       }
    }

    private void openNotificationPage() {
        Intent notifyIntent = new Intent(context, Notification.class);
        notifyIntent.putExtra("appid", app_id);
        notifyIntent.putExtra("isFromNotifi", "yes");
        this.app_id = "";
        startActivity(notifyIntent);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    if (data.getExtras() != null) {
                        int pos = data.getIntExtra("pos", 0);
                        String appid = data.getStringExtra("appid");
                       // updaterecdownloadevents(pos, appid);
                    }
                }
                break;
            case 100:
                switch (resultCode) {
                    case RESULT_OK:
                        startLocationUpdates();
                        Log.i("", "User agreed to make required location settings changes.");
                        break;
                    case Activity.RESULT_CANCELED:
                        Log.i("", "User chose not to make required location settings changes.");
                        break;

                }
                break;
            case TWITTER_REQ_CODE: {
                twitterAuthClient.onActivityResult(requestCode, resultCode, data);

                System.out.println("Request code : " + requestCode + " Result code : " + resultCode);
            }
            break;
            case SCANNER_REQUEST_CODE: {

                //once the request code is SCANNER_REQUEST_CODE

                if (resultCode == RESULT_OK) {
                    //storing the result in the String id
                    String id = data.getStringExtra("content");
                    searchFilter(id);

                }
            }

        }
    }

    private void searchFilter(String id) {
        String privacy, name;
        //checking the string contains "=" or not
        //3 possible data are
        //private=eventname
        //public=eventname
        //preview=eventname_preview
        if (id.contains("=")) {
            //getting the privacy key (public/private/preview)
            privacy = id.substring(0, id.lastIndexOf("="));
            //getting the eventname
            name = id.substring(id.lastIndexOf("=") + 1);
            //check whether the event is private or public/preview
            if (!privacy.equalsIgnoreCase("private")) {
                doSmth(name, false);
            } else
                doSmth(name, true);
        } else {
            Error_Dialog.show("Not a valid QR Code", getActivity());
        }
    }

    private void FetchPrivateUserData(final String appID, final int privacy) {

        final ProgressDialog dialog = new ProgressDialog(getActivity(), R.style.MyAlertDialogStyle);
        dialog.setMessage("Loading Details ...");
        dialog.show();


        String tag_string_req = "Login";
        String url = ApiList.Single_event_login;
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {

                try {
                    System.out.println(response);
                    JSONObject jObj = new JSONObject(response);


                    if (jObj.getBoolean("response")) {
                        parseresult(jObj.getJSONObject("responseString"), appID);
                        dialog.dismiss();
                    } else {
                        dialog.dismiss();
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
                } else if (com.webmobi.gecmedia.CustomViews.VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(com.webmobi.gecmedia.CustomViews.VolleyErrorLis.handleServerError(error, getContext()), getActivity());
                } else if (com.webmobi.gecmedia.CustomViews.VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show("Please Check Your Internet Connection", getActivity());
                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("deviceType", "android");
                params.put("deviceId", regId);
                params.put("appid", appID);
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
                String credentials = Paper.book().read("Email", "") + ":" + Paper.book(appID).read("PrivateKey", "");
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

    private void parseresult(JSONObject details, String appId) throws JSONException {

        if (!details.isNull("profile_pic")) {
            Paper.book().write("profile_pic", details.getString("profile_pic"));
        }

        Paper.book(appId).write("token", details.getJSONObject("token").getString("token"));
        Paper.book().write("userId", details.getJSONObject("token").getString("userId"));
        Paper.book(appId).write("userId", details.getJSONObject("token").getString("userId"));
        Paper.book().write("username", details.getJSONObject("token").getString("username"));
        Paper.book().write("email", Paper.book().read("Email"));
        Paper.book().write("attendee_option", details.getString("attendee_option"));
        System.out.println("Attendee Option : " + details.getString("attendee_option"));
        Paper.book(appId).write("admin_flag", details.getString("admin_flag"));
        Paper.book(appId).write("survey_flag", Integer.parseInt(details.getString("survey_flag")));
        SharedPreferences.Editor editor = spf.edit();
        editor.putString("username", Paper.book().read("username").toString());
        editor.putString("user_id", Paper.book().read("userId").toString());
        editor.apply();
        // adding the sch list
        List<Integer> list = new ArrayList<>();


        JSONArray jsonArr = new JSONArray(details.getString("schedules"));

        for (int i = 0; i < jsonArr.length(); i++)
            list.add(jsonArr.getInt(i));

        Paper.book().write("SCH", list);

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

                       /* setResult(RESULT_OK);
                        finish();*/
        Intent sending = new Intent(getActivity(), SingleEventHome.class);
        sending.putExtra("Engagement","");
        sending.putExtra("back","APPS");
        startActivity(sending);
    }


    private void FetchPublicUSerdata(final String appId) {

        final ProgressDialog dialog = new ProgressDialog(getActivity(), R.style.MyAlertDialogStyle);
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
                } else if (com.webmobi.gecmedia.CustomViews.VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(com.webmobi.gecmedia.CustomViews.VolleyErrorLis.handleServerError(error, getActivity()), getActivity());
                } else if (com.webmobi.gecmedia.CustomViews.VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show("Please Check Your Internet Connection", getActivity());
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


    }

    public void checklocationservice() {
        //  listener.disabletab(1);
        if (getActivity() == null)
            return;
        pDialog = new ProgressDialog(getActivity(), R.style.MyAlertDialogStyle);
        pDialog.setMessage("Fetching Location  ...");
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.show();
        if (isLocationServiceEnabled()) {
            pDialog.dismiss();
            startLocationUpdates();
        } else {
            pDialog.dismiss();
            checklocationenabled();
        }
    }

    public void checklocationenabled() {


        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(
                        mGoogleApiClient,
                        mLocationSettingsRequest
                );
        result.setResultCallback(this);

    }

    @Override
    public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
        final Status status = locationSettingsResult.getStatus();
        Intent in;
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:
                Log.i("", "All location settings are satisfied.");
                break;
            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                Log.i("", "Location settings are not satisfied. Show the user a sdialog to" +
                        "upgrade location settings ");

                try {
                    status.startResolutionForResult(getActivity(), REQUEST_CHECK_SETTINGS);
                } catch (IntentSender.SendIntentException e) {
                    Log.i("", "PendingIntent unable to execute request.");
                }
                break;
            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                Log.i("", "Location settings are inadequate, and cannot be fixed here. Dialog " + "not created.");

                break;
        }
    }


    public boolean isLocationServiceEnabled() {
        LocationManager locationManager = null;
        boolean gps_enabled = false, network_enabled = false;

        if (locationManager == null)
            locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        try {
            gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
            //do nothing...
        }


        return gps_enabled;

    }

    public void startLocationUpdates() {

        /*if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_CODE);
        } else {
            gettinglocation();
        }*/
    }

    private void gettinglocation() {
//        Location l = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
//        if (l != null) {
//            System.out.println(l.getLatitude());
//            getLocation(l.getLatitude(), l.getLongitude());
//        } else
//            gettinglocation();

        try {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
          /*  case PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startLocationUpdates();
                } else {
                    // listener.enableall();
                    deny();
                }
            }
            break;*/

            case CALENDAR_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    startLocationUpdates();
                } else {
                    Toast.makeText(context, "Enable calendar permission to add event to calendar", Toast.LENGTH_LONG).show();
                }
            }
            break;
            case CAMERA_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    //Do something
                    Intent intent = new Intent(getActivity(), SimpleScannerActivity.class);
                    startActivityForResult(intent, SCANNER_REQUEST_CODE);

                } else {
                    Toast.makeText(getActivity(), "Please grant camera permission to use camera", Toast.LENGTH_SHORT).show();

                }
                break;
        }
    }

    public void deny() {
        if (pDialog.isShowing() && pDialog != null) {
            pDialog.dismiss();
            /*filmlists.setAdapter(null);
            filmlists.setVisibility(View.GONE);
            noitems.setVisibility(View.VISIBLE);*/
            Toast.makeText(getContext(), "Enable your Location to find events around you", Toast.LENGTH_LONG).show();
        }
    }

    public void getLocation(double lat, double lng) {

        if (pDialog.isShowing() && pDialog != null)
            pDialog.dismiss();
        this.lat = lat;
        this.lng = lng;
        System.out.println("Home latitutude" + lat + "longitude" + lng);
        recommdEventsAfterSorting.clear();
        if (DataBaseStorage.isInternetConnectivity(getApplicationContext()))
            loadnormalrect(1, false);

    }

    @Override
    public void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    protected void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        builder.setAlwaysShow(true);
        mLocationSettingsRequest = builder.build();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        checklocationservice();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

        if (location != null) {
            stopLocationUpdates();
            getLocation(location.getLatitude(), location.getLongitude());
        }

    }

    /**
     * Stopping location updates
     */
    protected void stopLocationUpdates() {
        if (mGoogleApiClient.isConnected())
            LocationServices.FusedLocationApi.removeLocationUpdates(
                    mGoogleApiClient, (LocationListener) this);
    }

    private void showEventDialog(final int i, final ArrayList<Event> listing) {
        int clogo_height, margintop;
        event_dialog.setCancelable(false);
        event_dialog.show();

        ImageView close_dlg = (ImageView) event_dialog.findViewById(R.id.dlg_close);
        RoundedImageView clogo_center = (RoundedImageView) event_dialog.findViewById(R.id.dlg_logo2);
        ImageView clogo = (ImageView) event_dialog.findViewById(R.id.dlg_banner);
        final TextView app_name = (TextView) event_dialog.findViewById(R.id.dlg_app_name);
        final TextView event_date = (TextView) event_dialog.findViewById(R.id.dlg_date);
        TextView download = (TextView) event_dialog.findViewById(R.id.dlg_download_event);
        TextView description = (TextView) event_dialog.findViewById(R.id.dlg_description);
        ImageView addwishlist = (ImageView) event_dialog.findViewById(R.id.dlg_addwishlist);
        LinearLayout add2calendar = (LinearLayout) event_dialog.findViewById(R.id.dlg_add2calendar);
        LinearLayout invite = (LinearLayout) event_dialog.findViewById(R.id.dlg_invite);

        System.out.println("Event Category : " + listing.get(i).getAccesstype());
        if (listing.get(i).getAccesstype().equalsIgnoreCase("discovery"))
            download.setVisibility(View.GONE);
        else
            download.setVisibility(View.VISIBLE);
        if (savedirs.contains(listing.get(i).getAppid())) {
            download.setText("OPEN APP");
        } else {
            download.setText("DOWNLOAD");
        }

        listing_save = listing.get(i);

        final String action_name = download.getText().toString();
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listing.size() > 0)
                    if (action_name.equalsIgnoreCase("DOWNLOAD")) {
                        savedeviceapp(listing.get(i).getAppid(), listing.get(i).getApp_url(), i);
                    } else {
                        event_dialog.dismiss();
                        eventDir = new File(dir + listing.get(i).getAppid());
                        jsonFile = new File(eventDir, filename);
                        System.out.println("Loaded event");
                        try {
//                        child.setEnabled(false);
                            if (DataBaseStorage.isInternetConnectivity(getApplicationContext()))
                                showjs(jsonFile);
                            else {
                                openOffline(jsonFile, false);
                                Intent intent = new Intent(context, SingleEventHome.class);
                                intent.putExtra("Engagement","");
                                intent.putExtra("back","APPS");
                                startActivity(intent);
                            }
//                        enablingview(child);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
            }
        });
        close_dlg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                event_dialog.dismiss();
            }
        });
        final String date_str = listing.get(i).getStart_date().split("T")[0];

        app_name.setText(listing.get(i).getApp_title());
        event_date.setText(date_str);
        description.setText(listing.get(i).getApp_description());

        add2calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.WRITE_CALENDAR, Manifest.permission.READ_CALENDAR}, CALENDAR_PERMISSION_REQUEST_CODE);
                } else {
                    try {
                        syncCalendar(context, date_str, listing.get(i).getApp_title(), listing.get(i).getEnd_date().split("T")[0], listing.get(i).getAppid());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                share_event_title = app_name.getText().toString();
                share_event_date = event_date.getText().toString();
                share_location = listing.get(i).getLocation();
                share_search_key = listing.get(i).getApp_name();
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String content = "Event : " + share_event_title + "\n" + "Date : " + share_event_date + "\n" + "Location : " + share_location + "\n" + "Search Key : " + share_search_key + "\n" + "https://play.google.com/store/apps/details?id=com.webmobi.eventsapp";
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, content);
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, share_search_key);
                startActivity(Intent.createChooser(sharingIntent, "Share using"));
                /*sharedialog();
                help_dialog.show();*/
            }
        });
        RelativeLayout v2 = (RelativeLayout) event_dialog.findViewById(R.id.v2);
        // CardView v2 = (CardView) child.findViewById(R.id.main_content);
        // FrameLayout.LayoutParams logolayoutParams = (FrameLayout.LayoutParams)v2.getLayoutParams();
        FrameLayout.LayoutParams logolayoutParams = (FrameLayout.LayoutParams) v2.getLayoutParams();
        logolayoutParams.width = (int) (ImgWidth * 2.30);
        logolayoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        v2.setLayoutParams(logolayoutParams);
//            clogo_height = (int) (ImgHeight * 0.40);
        clogo_height = (int) (ImgHeight * 0.57);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams((int) (ImgWidth * 2.30), clogo_height);
//            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams((int) (ImgWidth * 1.10), (int) (ImgHeight * 0.65));


//        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

        clogo.setLayoutParams(layoutParams);

        Glide.with(getApplicationContext()).load((listing.get(i).getApp_image().equalsIgnoreCase("")) ? R.drawable.medium_no_image : listing.get(i).getApp_image())
                .fitCenter()
                .placeholder(R.drawable.medium_no_image)
                .dontAnimate()
                .error(R.drawable.medium_no_image)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .bitmapTransform(new ColorFilterTransformation(context, Color.argb(0, 0, 0, 0)))
                .into(clogo);


        RelativeLayout logo2_layout = (RelativeLayout) event_dialog.findViewById(R.id.logo2_layout);
        margintop = (int) (clogo_height - (getResources().getDimensionPixelSize(R.dimen.abc_action_bar_default_height_material) / 1.5));
        RelativeLayout.LayoutParams logo2_layout_params = new RelativeLayout.LayoutParams((int) (ImgWidth * 0.65), (int) (ImgWidth * 0.65));
        logo2_layout_params.setMargins(0, margintop, 0, 0);
        logo2_layout_params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        logo2_layout.setLayoutParams(logo2_layout_params);
        clogo_center.setCornerRadius(8, 8, 8, 8);
        Glide.with(getApplicationContext()).load((listing.get(i).getApp_logo().equalsIgnoreCase("")) ? R.drawable.logo : listing.get(i).getApp_logo())
                .fitCenter()
                .placeholder(R.drawable.medium_no_image)
                .dontAnimate()
                .error(R.drawable.medium_no_image)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .bitmapTransform(new ColorFilterTransformation(context, Color.argb(0, 0, 0, 0)))
                .into(clogo_center);

        mapFragment = (SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.dlg_map);
//        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;

                MarkerOptions marker = new MarkerOptions().position(new LatLng(listing.get(i).getLatitude(), listing.get(i).getLongitude())).title(listing.get(i).getVenue());
                marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker));
                mMap.addMarker(marker);

                LatLng latlng = new LatLng(listing.get(i).getLatitude(), listing.get(i).getLongitude());
                CameraPosition cameraPosition = new CameraPosition.Builder().target(latlng).zoom(8).build();
                mMap.getUiSettings().setZoomControlsEnabled(true);
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            }
        });


        if (db.getwishlist(listing.get(i).getAppid()) > 0)
            addwishlist.setSelected(true);
        else
            addwishlist.setSelected(false);

        addwishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (db.getwishlist(listing.get(i).getAppid()) > 0) {
                    db.deletewishlist(listing.get(i).getAppid());
                    v.setSelected(false);
                } else {

                    Gson gson = new Gson();
                    String details = gson.toJson(listing.get(i));
                    System.out.println("Text Details Value : " + details);
                    db.addingwishlist(new Events_Wishlist(listing.get(i).getAppid(), details));
                    v.setSelected(true);
                }

                /*if (clickListener != null)
                    clickListener.onClick(v, listPosition, holder.addwishlist);*/

            }
        });
    }
    private void showMultiEventDialog(final int i, final ArrayList<MultiEvent> multilisting) {
        int clogo_height, margintop;

        boolean eavail=false;
        event_dialog.setCancelable(false);
        event_dialog.show();

        ImageView close_dlg = (ImageView) event_dialog.findViewById(R.id.dlg_close);
        RoundedImageView clogo_center = (RoundedImageView) event_dialog.findViewById(R.id.dlg_logo2);
        ImageView clogo = (ImageView) event_dialog.findViewById(R.id.dlg_banner);
        final TextView app_name = (TextView) event_dialog.findViewById(R.id.dlg_app_name);
        final TextView event_date = (TextView) event_dialog.findViewById(R.id.dlg_date);
        TextView download = (TextView) event_dialog.findViewById(R.id.dlg_download_event);
        TextView description = (TextView) event_dialog.findViewById(R.id.dlg_description);
        ImageView addwishlist = (ImageView) event_dialog.findViewById(R.id.dlg_addwishlist);
        LinearLayout add2calendar = (LinearLayout) event_dialog.findViewById(R.id.dlg_add2calendar);
        LinearLayout invite = (LinearLayout) event_dialog.findViewById(R.id.dlg_invite);
        try {
            addwishlist.setVisibility(View.GONE);
        }catch (Exception e){

        }



        //  System.out.println("Event Category : " + listing.get(i).getAccesstype());

        boolean mflag=false;
        try {
            multieventid = Paper.book().read("MYAPPS",null);
            mflag = (multieventid != null && multieventid.size()>0);
        }catch (Exception e)
        {

        }


           if (mflag) {
               for (int j = 0; j < multieventid.size(); j++) {
                   if (multieventid.get(j).getEvent_id().equalsIgnoreCase(multilisting.get(i).getEvent_id())) {
                       eavail = true;

                       break;

                   }
               }
           }
        if (eavail) {

          try {
              download.setText("OPEN APP");
              event_date.setVisibility(View.GONE);
              add2calendar.setVisibility(View.GONE);
              invite.setVisibility(View.GONE);
              app_name.setText(multilisting.get(i).getEvent_name());
              clogo_center.setCornerRadius(8, 8, 8, 8);
              RelativeLayout v2 = (RelativeLayout) event_dialog.findViewById(R.id.v2);
              // CardView v2 = (CardView) child.findViewById(R.id.main_content);
              // FrameLayout.LayoutParams logolayoutParams = (FrameLayout.LayoutParams)v2.getLayoutParams();
              FrameLayout.LayoutParams logolayoutParams = (FrameLayout.LayoutParams) v2.getLayoutParams();
              logolayoutParams.width = (int) (ImgWidth * 2.30);
              logolayoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
              v2.setLayoutParams(logolayoutParams);
              Glide.with(getApplicationContext()).load((multilisting.get(i).getMulti_event_logo().equalsIgnoreCase("")) ? R.drawable.default_logo : multilisting.get(i).getMulti_event_logo())
                      .fitCenter()
                      .placeholder(R.drawable.medium_no_image)
                      .dontAnimate()
                      .error(R.drawable.medium_no_image)
                      .diskCacheStrategy(DiskCacheStrategy.ALL)
                      .bitmapTransform(new ColorFilterTransformation(context, Color.argb(0, 0, 0, 0)))
                      .into(clogo_center);

              //  clogo_height = (int) (ImgHeight * 0.40);
              clogo_height = (int) (ImgHeight * 0.57);
              RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams((int) (ImgWidth * 2.30), clogo_height);
//            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams((int) (ImgWidth * 1.10), (int) (ImgHeight * 0.65));


//        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

              clogo.setLayoutParams(layoutParams);

              Glide.with(getApplicationContext()).load((multilisting.get(i).getApp_image().equalsIgnoreCase("")) ? R.drawable.medium_no_image : multilisting.get(i).getApp_image())
                      .fitCenter()
                      .placeholder(R.drawable.medium_no_image)
                      .dontAnimate()
                      .error(R.drawable.medium_no_image)
                      .diskCacheStrategy(DiskCacheStrategy.ALL)
                      .bitmapTransform(new ColorFilterTransformation(context, Color.argb(0, 0, 0, 0)))
                      .into(clogo);
          }catch (Exception e){}

        } else {
          try {
              download.setText("DOWNLOAD");
              event_date.setVisibility(View.GONE);
              add2calendar.setVisibility(View.GONE);
              invite.setVisibility(View.GONE);
              app_name.setText(multilisting.get(i).getEvent_name());
              RelativeLayout v2 = (RelativeLayout) event_dialog.findViewById(R.id.v2);
              // CardView v2 = (CardView) child.findViewById(R.id.main_content);
              // FrameLayout.LayoutParams logolayoutParams = (FrameLayout.LayoutParams)v2.getLayoutParams();
              FrameLayout.LayoutParams logolayoutParams = (FrameLayout.LayoutParams) v2.getLayoutParams();
              logolayoutParams.width = (int) (ImgWidth * 2.30);
              logolayoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
              v2.setLayoutParams(logolayoutParams);
              clogo_center.setCornerRadius(6, 6, 6, 6);
              Glide.with(getApplicationContext()).load((multilisting.get(i).getMulti_event_logo().equalsIgnoreCase("")) ? R.drawable.default_logo : multilisting.get(i).getMulti_event_logo())
                      .fitCenter()
                      .placeholder(R.drawable.medium_no_image)
                      .dontAnimate()
                      .error(R.drawable.medium_no_image)
                      .diskCacheStrategy(DiskCacheStrategy.ALL)
                      .bitmapTransform(new ColorFilterTransformation(context, Color.argb(0, 0, 0, 0)))
                      .into(clogo_center);

              clogo_height = (int) (ImgHeight * 0.57);
              RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams((int) (ImgWidth * 2.30), clogo_height);
//            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams((int) (ImgWidth * 1.10), (int) (ImgHeight * 0.65));


//        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

              clogo.setLayoutParams(layoutParams);

              Glide.with(getApplicationContext()).load((multilisting.get(i).getApp_image().equalsIgnoreCase("")) ? R.drawable.medium_no_image : multilisting.get(i).getApp_image())
                      .fitCenter()
                      .placeholder(R.drawable.medium_no_image)
                      .dontAnimate()
                      .error(R.drawable.medium_no_image)
                      .diskCacheStrategy(DiskCacheStrategy.ALL)
                      .bitmapTransform(new ColorFilterTransformation(context, Color.argb(0, 0, 0, 0)))
                      .into(clogo);
          }catch (Exception e){

          }

        }

        //listing_save = listing.get(i);

       // final String action_name = download.getText().toString();
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (multilisting.size() > 0)

                    event_dialog.dismiss();
                // eventDir = new File(dir + listing.get(i).getAppid());
                // jsonFile = new File(eventDir, filename);
                //  System.out.println("Loaded event");
                try {
//                        child.setEnabled(false);
                    System.out.print("point");
                            Intent i1 =new Intent(context,HomeScreenMulti.class);
                            i1.putExtra("Event_ID",(multilisting.get(i).getEvent_id()));
                            i1.putExtra("Event_Name",(multilisting.get(i).getEvent_name()));
                            i1.putExtra("Event_Logo",(multilisting.get(i).getApp_image()));
                            i1.putExtra("Event_Theme",(multilisting.get(i).getColor_code()));
                            i1.putExtra("Multi_event_logo",multilisting.get(i).getMulti_event_logo());
                 ;
                            startActivity(i1);
//                        enablingview(child);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        close_dlg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                event_dialog.dismiss();
            }
        });
       /* final String date_str = listing.get(i).getStart_date().split("T")[0];

        app_name.setText(listing.get(i).getApp_title());
        event_date.setText(date_str);
        description.setText(listing.get(i).getApp_description());

        add2calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.WRITE_CALENDAR, Manifest.permission.READ_CALENDAR}, CALENDAR_PERMISSION_REQUEST_CODE);
                } else {
                    try {
                        syncCalendar(context, date_str, listing.get(i).getApp_title(), listing.get(i).getEnd_date().split("T")[0], listing.get(i).getAppid());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        });*/

        /*invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                share_event_title = app_name.getText().toString();
                share_event_date = event_date.getText().toString();
                share_location = listing.get(i).getLocation();
                share_search_key = listing.get(i).getApp_name();
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String content = "Event : " + share_event_title + "\n" + "Date : " + share_event_date + "\n" + "Location : " + share_location + "\n" + "Search Key : " + share_search_key + "\n" + "https://play.google.com/store/apps/details?id=com.webmobi.eventsapp";
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, content);
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, share_search_key);
                startActivity(Intent.createChooser(sharingIntent, "Share using"));
                *//*sharedialog();
                help_dialog.show();*//*
            }
        });*/
       /* RelativeLayout v2 = (RelativeLayout) event_dialog.findViewById(R.id.v2);
        // CardView v2 = (CardView) child.findViewById(R.id.main_content);
        // FrameLayout.LayoutParams logolayoutParams = (FrameLayout.LayoutParams)v2.getLayoutParams();
        FrameLayout.LayoutParams logolayoutParams = (FrameLayout.LayoutParams) v2.getLayoutParams();
        logolayoutParams.width = (int) (ImgWidth * 2.30);
        logolayoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        v2.setLayoutParams(logolayoutParams);
//            clogo_height = (int) (ImgHeight * 0.40);
        clogo_height = (int) (ImgHeight * 0.57);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams((int) (ImgWidth * 2.30), clogo_height);
//            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams((int) (ImgWidth * 1.10), (int) (ImgHeight * 0.65));


//        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

        clogo.setLayoutParams(layoutParams);

        Glide.with(getApplicationContext()).load((listing.get(i).getApp_image().equalsIgnoreCase("")) ? R.drawable.medium_no_image : listing.get(i).getApp_image())
                .fitCenter()
                .placeholder(R.drawable.medium_no_image)
                .dontAnimate()
                .error(R.drawable.medium_no_image)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .bitmapTransform(new ColorFilterTransformation(context, Color.argb(0, 0, 0, 0)))
                .into(clogo);


        RelativeLayout logo2_layout = (RelativeLayout) event_dialog.findViewById(R.id.logo2_layout);
        margintop = (int) (clogo_height - (getResources().getDimensionPixelSize(R.dimen.abc_action_bar_default_height_material) / 1.5));
        RelativeLayout.LayoutParams logo2_layout_params = new RelativeLayout.LayoutParams((int) (ImgWidth * 0.65), (int) (ImgWidth * 0.65));
        logo2_layout_params.setMargins(0, margintop, 0, 0);
        logo2_layout_params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        logo2_layout.setLayoutParams(logo2_layout_params);
        clogo_center.setCornerRadius(8, 8, 8, 8);
        Glide.with(getApplicationContext()).load((listing.get(i).getApp_logo().equalsIgnoreCase("")) ? R.drawable.logo : listing.get(i).getApp_logo())
                .fitCenter()
                .placeholder(R.drawable.medium_no_image)
                .dontAnimate()
                .error(R.drawable.medium_no_image)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .bitmapTransform(new ColorFilterTransformation(context, Color.argb(0, 0, 0, 0)))
                .into(clogo_center);*/

        //  mapFragment = (SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.dlg_map);
//        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

       /* mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;

                MarkerOptions marker = new MarkerOptions().position(new LatLng(listing.get(i).getLatitude(), listing.get(i).getLongitude())).title(listing.get(i).getVenue());
                marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker));
                mMap.addMarker(marker);

                LatLng latlng = new LatLng(listing.get(i).getLatitude(), listing.get(i).getLongitude());
                CameraPosition cameraPosition = new CameraPosition.Builder().target(latlng).zoom(8).build();
                mMap.getUiSettings().setZoomControlsEnabled(true);
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            }
        });*/



/* if (db.getwishlist(listing.get(i).getAppid()) > 0)
            addwishlist.setSelected(true);
        else
            addwishlist.setSelected(false);*/
        /*addwishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (db.getwishlist(listing.get(i).getAppid()) > 0) {
                    db.deletewishlist(listing.get(i).getAppid());
                    v.setSelected(false);
                } else {

                    Gson gson = new Gson();
                    String details = gson.toJson(listing.get(i));
                    System.out.println("Text Details Value : " + details);
                    db.addingwishlist(new Events_Wishlist(listing.get(i).getAppid(), details));
                    v.setSelected(true);
                }

                *//*if (clickListener != null)
                    clickListener.onClick(v, listPosition, holder.addwishlist);*//*

            }
        });*/
    }

    private void sharedialog() {
        help_dialog = new Dialog(context);
        help_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        help_dialog.setContentView(R.layout.help_dialog);

        whatsapp_dlg = (LinearLayout) help_dialog.findViewById(R.id.whatsapp_dlg);
        twitter_dlg = (LinearLayout) help_dialog.findViewById(R.id.twitter_dlg);
        fb_dlg = (LinearLayout) help_dialog.findViewById(R.id.fb_dlg);
        linkedin_dlg = (LinearLayout) help_dialog.findViewById(R.id.linkedin_dlg);

        linkedin_dlg.setOnClickListener(this);
        fb_dlg.setOnClickListener(this);
        whatsapp_dlg.setOnClickListener(this);
        twitter_dlg.setOnClickListener(this);
        help_dialog.setCancelable(true);
    }

    private void syncCalendar(Context context, String date_str, String app_title, String end_Date, String appid) throws ParseException {

        long startdateMillis = 0, enddateMillis = 0;


        startdateMillis = getTimeInMillis(date_str);
        enddateMillis = getTimeInMillis(end_Date);

        TimeZone tz = TimeZone.getDefault();
        ContentResolver cr = context.getContentResolver();
        HashMap<Long, String> savedEvent = new HashMap<>();
        savedEvent = Paper.book("calendar_event").read("calendar_event", new HashMap<Long, String>());

        Iterator iterator = savedEvent.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry mentry2 = (Map.Entry) iterator.next();
            String key = mentry2.getKey().toString();
            String value = mentry2.getValue().toString();
            if (value.equalsIgnoreCase(appid)) {
                deleteEventFromCalendar(Long.parseLong(key), cr);
                savedEvent.remove(Long.parseLong(key));
                break;
            }
        }
        addEventToCalendar(app_title, startdateMillis, enddateMillis, appid, tz, cr, savedEvent, true);
    }

    private long getTimeInMillis(String date_str) {
        Date date = null;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date = sdf.parse(date_str);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date.getTime();
    }

    private void addEventToCalendar(String app_title, long startdateMillis, long enddateMillis, String appid, TimeZone tz, ContentResolver cr, HashMap<Long, String> savedEvent, boolean b) {


        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling

            return;
        }

        Uri eventUriString = Uri.parse("content://com.android.calendar/events");

        ContentValues eventValues = new ContentValues();

        eventValues.put("calendar_id", 1);
        eventValues.put("title", app_title);
        eventValues.put("dtstart", startdateMillis);
        eventValues.put("dtend", enddateMillis);
        eventValues.put("eventTimezone", tz.toString());
        eventValues.put("eventStatus", 1); // This information is sufficient for most entries tentative (0), confirmed (1) or canceled (2):
        eventValues.put("hasAlarm", 1); // 0 for false, 1 for true

        Uri eventUri = cr.insert(eventUriString, eventValues);
        long eventID = Long.parseLong(eventUri.getLastPathSegment());
        savedEvent.put(eventID, appid);
        Toast.makeText(context, "Event Added to Calendar", Toast.LENGTH_LONG).show();
        if (b)
            Paper.book("calendar_event").write("calendar_event", savedEvent);


    }

    private void deleteEventFromCalendar(long appid, ContentResolver cr) {

        Uri eventUri = Uri.parse("content://com.android.calendar/events");
        Uri deleteUri = null;
        deleteUri = ContentUris.withAppendedId(eventUri, appid);
        int rows = cr.delete(deleteUri, null, null);

    }

    private void savedeviceapp(final String appid, final String app_url, final int i) {
        event_dialog.dismiss();
        final ProgressDialog dialog = new ProgressDialog(getActivity(), R.style.MyAlertDialogStyle);
        dialog.setMessage("DownLoading...");
        dialog.show();


        String tag_string_req = "DeviceApp";
        String url = ApiList.DeviceApp;
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                try {
                    dialog.dismiss();
                    System.out.println("Response Savedevice AppNew : " + response);
                    JSONObject jObj = new JSONObject(response);


                    if (jObj.getBoolean("response")) {

                        downloadjson(i, app_url, appid);

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
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("appid", appid);
                params.put("device_id", regId);
                params.put("device_type", "android");
                params.put("userid", Paper.book().read("Islogin", false) ? Paper.book().read("userId", "") : "");
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

    private void downloadjson(final int downpos, String app_url, final String appid) {

        String jsonUrl;
        if (isPreview)
            jsonUrl = baseUrl + app_url + "/temp/appData.json";
        else
            jsonUrl = baseUrl + app_url + "/appData.json";
//        jsonUrl = baseUrl + app_url + "/appData.json";
        System.out.println("Download event : " + jsonUrl);
        pDialog = new ProgressDialog(context, R.style.MyAlertDialogStyle);
        pDialog.setMessage("Downloading ... ...");
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.show();


        String tag_string_req = "Downloading";
        System.out.println("Response APP Url " + jsonUrl);
        StringRequest jsonRequest = new StringRequest(Request.Method.GET, jsonUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialog.dismiss();
                JSONObject jsonObject = null;

                System.out.println("Response JSON " + response);
                try {
                    jsonObject = new JSONObject(response);
                    if (isPrivateEvent) {
                        isPrivateEvent = false;
                        String appID = listing_save.getAppid();
                        boolean isPrivate = Boolean.parseBoolean(jsonObject.getString("info_privacy"));
                        if (isPrivate)
                            Paper.book(appID).write("InfoPrivacy", 1);
                        else
                            Paper.book(appID).write("InfoPrivacy", 0);

                        Paper.book(appID).write("PrivateKey", jsonObject.getString("private_key"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (isPreview)
                    eventDir = new File(dir_prev + appid);
                else
                    eventDir = new File(dir + appid);
                if (!eventDir.exists())
                    eventDir.mkdir();


                jsonFile = new File(eventDir, filename);
                descFile = new File(eventDir, "description.txt");
                try {
                    Files.write(response, jsonFile, Charset.defaultCharset());
                    Files.append(listing_save.getApp_name() + "\n", descFile, Charset.defaultCharset());
                    Files.append(listing_save.getApp_category() + "\n", descFile, Charset.defaultCharset());
                    Files.append(listing_save.getStart_date() + "\n", descFile, Charset.defaultCharset());
                    Files.append(listing_save.getAppid() + "\n", descFile, Charset.defaultCharset());
                    Files.append(listing_save.getLocation() + "\n", descFile, Charset.defaultCharset());
                    Files.append(listing_save.getApp_title() + "\n", descFile, Charset.defaultCharset());
                    Files.append(listing_save.getVenue() + "\n", descFile, Charset.defaultCharset());
                    Files.append(listing_save.getApp_logo() + "\n", descFile, Charset.defaultCharset());
                    Files.append(listing_save.getApp_url() + "\n", descFile, Charset.defaultCharset());
                    Files.append(listing_save.getApp_image() + "\n", descFile, Charset.defaultCharset());

                    /*if (action.equalsIgnoreCase(ApiList.EventCategory))
                        update(Searchevnets.get(downpos).getAppid(), downpos);
                    else if (action.equalsIgnoreCase(ApiList.Eventrecom))*/
//                    update(Searchevnets.get(downpos).getAppid(), downpos);

                    //automatically open the event once download is completed
                    openEvent(appid);
//                    refreshFragment();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                if (error instanceof TimeoutError) {
                    System.out.println("Search Error Timeout");
                    Error_Dialog.show("Timeout", getActivity());
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    System.out.println("Search Error Server " + VolleyErrorLis.handleServerError(error, context));
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, context), getActivity());
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    System.out.println("Search Error No Internet Connection  ");
                    Error_Dialog.show("No Internet Connection", getActivity());
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


    public void refreshFragment() {
//        getFragmentManager().beginTransaction().detach(this).attach(this).commit();
        getFragmentManager().beginTransaction().detach(this).attach(this).commitAllowingStateLoss();

    }

    private void doSmth(final String searchstring, boolean isPrivate) {

        final ProgressDialog dialog = new ProgressDialog(getActivity(), R.style.MyAlertDialogStyle);
        dialog.setMessage("Loading..");
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();

        String tag_string_req = "events";
        String url = null;
        Searchevnets.clear();
        String prevSearch = null;

        //check private or not
        if (isPrivate) {
            //check user is logged in or not
            if (!Paper.book().read("Islogin", false)) {
                dialog.dismiss();
                Error_Dialog.show("Please Login to view private event", getActivity());
                return;
            } else {
                //setting private url
                isPrivateEvent = true;
                url = ApiList.PSearchEvents1 + searchstring + "&userid=" + Paper.book().read("userId");
            }
        } else {
            //checking event ends with _preview
            if (searchstring.endsWith("_preview")) {
                if (!Paper.book().read("Islogin", false)) {
                    dialog.dismiss();
                    Error_Dialog.show("Please Login to view preview of event", getActivity());
                    return;
                } else {

                }
                isPreview = true;
                //eliminating the preview key from event name
                prevSearch = searchstring.substring(0, searchstring.lastIndexOf("_"));
//            searchstring = prevSearch;
                //setting the preview url
                url = ApiList.SearchEvents + prevSearch + "&preview_flag=1" + "&userid=" + Paper.book().read("userId", "");
            } else
                url = ApiList.SearchEvents + searchstring + "&preview_flag=0";
        }

        final String finalPrevSearch;
        if (prevSearch == null)
            finalPrevSearch = searchstring;
        else
            finalPrevSearch = prevSearch;
        StringRequest strReq = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {

                dialog.dismiss();
                try {
                    JSONObject json = new JSONObject(response);
                    if (json.getBoolean("response")) {

                        searchParseResult(json.getJSONArray("events"), finalPrevSearch);

                        System.out.println("JSON Search Result : " + json.getJSONArray("events"));
                    } else {
//                        showitems(false);
                        System.out.println("JSON Search Result : " + response.toString());
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
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, context), getActivity());
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show("Please Check Your Internet Connection", getActivity());

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

    private void searchParseResult(JSONArray events, String event_id) {
        try {


            Searchevnets.clear();
            Gson gson = new Gson();

            for (int i = 0; i < events.length(); i++) {
                String eventString = events.getJSONObject(i).toString();
                Event obj = gson.fromJson(eventString, Event.class);

                Searchevnets.add(obj);
            }
            if (Searchevnets.size() > 0)
                checkEventURL(Searchevnets, event_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void checkEventURL(ArrayList<Event> searchevnets, String event_id) {
        for (int i = 0; i < searchevnets.size(); i++) {
            if (searchevnets.get(i).getApp_url().equalsIgnoreCase(event_id)) {
                System.out.println("Show Dialog Event Name : " + searchevnets.get(i).getApp_url());


                showEventDialogDeepLink(searchevnets.get(i));
                this.event_id = "";
                break;
            }
        }
    }

    private void showEventDialogDeepLink(final Event event) {
        int clogo_height, margintop;
        event_dialog.setCancelable(false);
        event_dialog.show();

        ImageView close_dlg = (ImageView) event_dialog.findViewById(R.id.dlg_close);
        RoundedImageView clogo_center = (RoundedImageView) event_dialog.findViewById(R.id.dlg_logo2);
        ImageView clogo = (ImageView) event_dialog.findViewById(R.id.dlg_banner);
        final TextView app_name = (TextView) event_dialog.findViewById(R.id.dlg_app_name);
        final TextView event_date = (TextView) event_dialog.findViewById(R.id.dlg_date);
        TextView download = (TextView) event_dialog.findViewById(R.id.dlg_download_event);
        TextView description = (TextView) event_dialog.findViewById(R.id.dlg_description);
        ImageView addwishlist = (ImageView) event_dialog.findViewById(R.id.dlg_addwishlist);
        LinearLayout add2calendar = (LinearLayout) event_dialog.findViewById(R.id.dlg_add2calendar);
        LinearLayout invite = (LinearLayout) event_dialog.findViewById(R.id.dlg_invite);

        System.out.println("Event Category : " + event.getAccesstype());
        if (event.getAccesstype().equalsIgnoreCase("discovery"))
            download.setVisibility(View.GONE);
        else
            download.setVisibility(View.VISIBLE);
        if (isPreview) {
            if (savePrevdirs.contains(event.getAppid())) {
                download.setText("OPEN APP");
            } else {
                download.setText("DOWNLOAD");
            }
        } else {
            if (savedirs.contains(event.getAppid())) {
                download.setText("OPEN APP");
            } else {
                download.setText("DOWNLOAD");
            }
        }
       /* if (savedirs.contains(event.getAppid())) {
            download.setText("OPEN APP");
        } else {
            download.setText("DOWNLOAD");
        }*/

        listing_save = event;

        final String action_name = download.getText().toString();
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (action_name.equalsIgnoreCase("DOWNLOAD")) {
                    savedeviceapp(event.getAppid(), event.getApp_url(), 0);
                } else {

                    event_dialog.dismiss();
                    eventDir = new File(dir + event.getAppid());
                    jsonFile = new File(eventDir, filename);
                    System.out.println("Loaded event");
                    try {
//                        child.setEnabled(false);
                        if (DataBaseStorage.isInternetConnectivity(getApplicationContext()))
                            showjs(jsonFile);
                        else {
                            openOffline(jsonFile, false);
                            Intent intent = new Intent(context, SingleEventHome.class);
                            intent.putExtra("Engagement","");
                            intent.putExtra("back","APPS");
                            startActivity(intent);
                        }
//                        enablingview(child);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        close_dlg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                event_dialog.dismiss();
            }
        });
        final String date_str = event.getStart_date().split("T")[0];
//        app_id = event.getAppid();
        app_name.setText(event.getApp_title());
        event_date.setText(date_str);
        description.setText(event.getApp_description());

        add2calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.WRITE_CALENDAR, Manifest.permission.READ_CALENDAR}, CALENDAR_PERMISSION_REQUEST_CODE);
                } else {
                    try {
                        syncCalendar(context, date_str, event.getApp_title(), event.getEnd_date().split("T")[0], event.getAppid());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                share_event_title = app_name.getText().toString();
                share_event_date = event_date.getText().toString();
                share_location = event.getLocation();
                share_search_key = event.getApp_name();
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String content = "Event : " + share_event_title + "\n" + "Date : " + share_event_date + "\n" + "Location : " + share_location + "\n" + "Search Key : " + share_search_key + "\n" + "https://play.google.com/store/apps/details?id=com.webmobi.eventsapp";
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, content);
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, share_search_key);
                startActivity(Intent.createChooser(sharingIntent, "Share using"));
                /*sharedialog();
                help_dialog.show();*/
            }
        });
        RelativeLayout v2 = (RelativeLayout) event_dialog.findViewById(R.id.v2);
        // CardView v2 = (CardView) child.findViewById(R.id.main_content);
        // FrameLayout.LayoutParams logolayoutParams = (FrameLayout.LayoutParams)v2.getLayoutParams();
        FrameLayout.LayoutParams logolayoutParams = (FrameLayout.LayoutParams) v2.getLayoutParams();
        logolayoutParams.width = (int) (ImgWidth * 2.30);
        logolayoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        v2.setLayoutParams(logolayoutParams);
//            clogo_height = (int) (ImgHeight * 0.40);
        clogo_height = (int) (ImgHeight * 0.57);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams((int) (ImgWidth * 2.30), clogo_height);
//            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams((int) (ImgWidth * 1.10), (int) (ImgHeight * 0.65));


//        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

        clogo.setLayoutParams(layoutParams);

        Glide.with(getApplicationContext()).load((event.getApp_image().equalsIgnoreCase("")) ? R.drawable.medium_no_image : event.getApp_image())
                .fitCenter()
                .placeholder(R.drawable.medium_no_image)
                .dontAnimate()
                .error(R.drawable.medium_no_image)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .bitmapTransform(new ColorFilterTransformation(context, Color.argb(0, 0, 0, 0)))
                .into(clogo);


        RelativeLayout logo2_layout = (RelativeLayout) event_dialog.findViewById(R.id.logo2_layout);
        margintop = (int) (clogo_height - (getResources().getDimensionPixelSize(R.dimen.abc_action_bar_default_height_material) / 1.5));
        RelativeLayout.LayoutParams logo2_layout_params = new RelativeLayout.LayoutParams((int) (ImgWidth * 0.65), (int) (ImgWidth * 0.65));
        logo2_layout_params.setMargins(0, margintop, 0, 0);
        logo2_layout_params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        logo2_layout.setLayoutParams(logo2_layout_params);
        clogo_center.setCornerRadius(8, 8, 8, 8);
        Glide.with(getApplicationContext()).load((event.getApp_logo().equalsIgnoreCase("")) ? R.drawable.logo : event.getApp_logo())
                .fitCenter()
                .placeholder(R.drawable.medium_no_image)
                .dontAnimate()
                .error(R.drawable.medium_no_image)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .bitmapTransform(new ColorFilterTransformation(context, Color.argb(0, 0, 0, 0)))
                .into(clogo_center);

        mapFragment = (SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.dlg_map);
//        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;

                MarkerOptions marker = new MarkerOptions().position(new LatLng(event.getLatitude(), event.getLongitude())).title(event.getVenue());
                marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker));
                mMap.addMarker(marker);

                LatLng latlng = new LatLng(event.getLatitude(), event.getLongitude());
                CameraPosition cameraPosition = new CameraPosition.Builder().target(latlng).zoom(8).build();
                mMap.getUiSettings().setZoomControlsEnabled(true);
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            }
        });


        if (db.getwishlist(event.getAppid()) > 0)
            addwishlist.setSelected(true);
        else
            addwishlist.setSelected(false);

        addwishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (db.getwishlist(event.getAppid()) > 0) {
                    db.deletewishlist(event.getAppid());
                    v.setSelected(false);
                } else {

                    Gson gson = new Gson();
                    String details = gson.toJson(event);
                    System.out.println("Text Details Value : " + details);
                    db.addingwishlist(new Events_Wishlist(event.getAppid(), details));
                    v.setSelected(true);
                }

                /*if (clickListener != null)
                    clickListener.onClick(v, listPosition, holder.addwishlist);*/

            }
        });


    }
}
