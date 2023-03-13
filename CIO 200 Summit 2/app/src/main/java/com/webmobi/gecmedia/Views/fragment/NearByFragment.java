package com.webmobi.gecmedia.Views.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
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
import com.singleevent.sdk.model.AppDetails;
import com.singleevent.sdk.model.Events;
import com.singleevent.sdk.model.LocalArraylist.Notes;
import com.singleevent.sdk.model.LocalArraylist.Rating;
import com.singleevent.sdk.View.RightActivity.admin.checkin.SimpleScannerActivity;
import com.singleevent.sdk.utils.DataBaseStorage;
import com.webmobi.gecmedia.Config.ApiList;
import com.webmobi.gecmedia.CustomViews.ColorFilterTransformation;
import com.webmobi.gecmedia.CustomViews.DilatingDotsProgressBar;
import com.webmobi.gecmedia.CustomViews.VolleyErrorLis;
import com.webmobi.gecmedia.LocalDatabase.DatabaseHandler;
import com.webmobi.gecmedia.Models.Event;
import com.webmobi.gecmedia.Models.Events_Wishlist;
import com.webmobi.gecmedia.Models.NearbyInterface;
import com.webmobi.gecmedia.Models.OnItemClickListener;
import com.webmobi.gecmedia.R;
import com.webmobi.gecmedia.SingleEventHome;
import com.webmobi.gecmedia.Views.Adapter.DisEventAdapter;
import com.webmobi.gecmedia.Views.DiscoveryEventDetails;
import com.webmobi.gecmedia.Views.Enter_DetailActivity;
import com.webmobi.gecmedia.Views.LoginActivity;
import com.webmobi.gecmedia.Views.PrivateSearchActivity;
import com.webmobi.gecmedia.Views.Profile;
import com.webmobi.gecmedia.Views.RegActivity;
import com.webmobi.gecmedia.Views.SearchActivity;
import com.webmobi.gecmedia.Views.nearby_filter;

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
import java.util.Calendar;
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
 * Created by Admin on 4/28/2017.
 */

public class NearByFragment extends Fragment implements View.OnClickListener, OnItemClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ResultCallback<LocationSettingsResult>, LocationListener {

    NearbyInterface listener;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    Context context;
    RecyclerView filmlists;
    TextView noitems, tryagain;
    ImageView filter;
    DisEventAdapter f_adapter;
    ProgressDialog pDialog;
    boolean type = false;
    String dateselected = "All Dates", cateselected = "All Event", fromdate = "", todate = "", jsonArray = "[]";
    double lat, lng;
    LinearLayoutManager mLayoutManager;
    int total, preLast;
    ArrayList<Event> nearbyevents;
    RelativeLayout v1, v2;
    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 5;
    int firstVisibleItem, visibleItemCount, totalItemCount;
    private DilatingDotsProgressBar mDilatingDotsProgressBar;
    float ImgWidth, ImgHeight,dlg_ImgWidth, dlg_ImgHeight;
    String baseUrl = "https://webmobi.s3.amazonaws.com/nativeapps/";
    String filename = "app.json";
    private List<String> savedirs;
    File eventDir, jsonFile, descFile;
    private ArrayList<Events> eventsToDisplay;
    View view;

    TextView toolbar_title, search;
    ConstraintLayout pguide;
    ImageView user_login;

    // adding googleapi client code

    protected GoogleApiClient mGoogleApiClient;
    private final int PERMISSION_REQUEST_CODE = 1;
    protected LocationRequest mLocationRequest;
    private int REQUEST_CHECK_SETTINGS = 100;


    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 1000;

    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    protected LocationSettingsRequest mLocationSettingsRequest;
    public static final int CAMERA_PERMISSION = 120;
    public static final int SCANNER_REQUEST_CODE = 220;
    private final int CALENDAR_PERMISSION_REQUEST_CODE = 2;
    Event listing_save, listing;

    TextView qrsearch;
    ArrayList<Event> Searchevnets;
    boolean isPreview = false, isPrivateEvent = false;
    String event_id = "", app_id = "";
    Dialog event_dialog;
    DatabaseHandler db;
    private List<String> savePrevdirs;
    String dir, dir_prev, regId;
    String share_event_title, share_event_date, share_location, share_search_key;
    SupportMapFragment mapFragment;
    private GoogleMap mMap;
    private ArrayList<Events> offlineevents;
    SharedPreferences spf;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        //  listener = (NearbyInterface) activity;
        context = activity;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Paper.init(getActivity());
        regId = Paper.book().read("regId");
        buildGoogleApiClient();
        createLocationRequest();
        buildLocationSettingsRequest();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view != null)
            return view;
        view = inflater.inflate(R.layout.fragment_nearby, container, false);
        dir = getActivity().getFilesDir() + File.separator + "EventsDownload" + File.separator;
        dir_prev = getActivity().getFilesDir() + File.separator + "PreviewDownloaded" + File.separator;
        spf = getActivity().getSharedPreferences(ApiList.LOCALSTORAGE, MODE_PRIVATE);

        DisplayMetrics displayMetrics = getActivity().getResources().getDisplayMetrics();
        ImgWidth = displayMetrics.widthPixels;
        ImgHeight = displayMetrics.widthPixels * 0.60F;

        dlg_ImgWidth = displayMetrics.widthPixels * 0.30F;
        dlg_ImgHeight = dlg_ImgWidth * 1.7F;

        filmlists = (RecyclerView) view.findViewById(R.id.filmlist);
        noitems = (TextView) view.findViewById(R.id.noitems);
        tryagain = (TextView) view.findViewById(R.id.tryagain);
        qrsearch = (TextView) view.findViewById(R.id.qrsearch_tab);

        v1 = (RelativeLayout) view.findViewById(R.id.view1);
        v2 = (RelativeLayout) view.findViewById(R.id.view2);
        filter = (ImageView) view.findViewById(R.id.filter_button);

        pguide = (ConstraintLayout) view.findViewById(R.id.pguide);
        search = (TextView) view.findViewById(R.id.search);
        user_login = (ImageView) view.findViewById(R.id.title_right_ic);
        //filter.setTypeface(Util.regulartypeface(getActivity()));
        mDilatingDotsProgressBar = (DilatingDotsProgressBar) view.findViewById(R.id.progress);
        mLayoutManager = new LinearLayoutManager(context);
        filter.setOnClickListener(this);
        pguide.setOnClickListener(this);
        search.setOnClickListener(this);
        qrsearch.setOnClickListener(this);


        Searchevnets = new ArrayList<>();
        event_dialog = new Dialog(getActivity());
        event_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        event_dialog.setContentView(R.layout.event_dialog_nearby);
        db = new DatabaseHandler(getActivity());
        savePrevdirs = new ArrayList<>();
        savePrevdirs = getSaveDirs(dir_prev);
        savedirs = new ArrayList<>();
        savedirs = getSaveDirs(dir);
        // setting swipe refresh layout

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.container);
        mSwipeRefreshLayout.setColorSchemeColors(Color.parseColor("#f0712b"), Color.GREEN, Color.BLUE, Color.YELLOW);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(false);
                checklocationservice();
            }
        });


        // setting the vents to adapter

        nearbyevents = new ArrayList<>();


        filmlists.setLayoutManager(mLayoutManager);
        f_adapter = new DisEventAdapter(getActivity(), nearbyevents, ImgWidth, ImgHeight, NearByFragment.this);
        filmlists.setAdapter(f_adapter);

        addscrolllistner();


        tryagain.setOnClickListener(this);

        user_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userLogin();
            }
        });
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


    private void openevent(String appid) throws IOException {
        f_adapter.notifyDataSetChanged();

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
            Intent sending = new Intent(getActivity(), SingleEventHome.class);sending.putExtra("Engagement","");sending.putExtra("back","APPS");
            startActivity(sending);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    private void addscrolllistner() {


        filmlists.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {


                if (dy > 0) {
                    visibleItemCount = recyclerView.getChildCount();
                    totalItemCount = mLayoutManager.getItemCount();
                    firstVisibleItem = mLayoutManager.findFirstVisibleItemPosition();

                    if (loading) {
                        if (totalItemCount > previousTotal) {
                            loading = false;
                            previousTotal = totalItemCount;
                        }
                    }
                    if (!loading && (totalItemCount - visibleItemCount)
                            <= (firstVisibleItem + visibleThreshold)) {
                        loading = true;
                    }
                }


            }
        });

    }

    @Override
    public void onClick(View view) {
        Intent cat;
        switch (view.getId()) {
            case R.id.filter_button:
                Intent i = new Intent(getActivity(), nearby_filter.class);
                i.putExtra("type", type);
                i.putExtra("dateselected", dateselected);
                i.putExtra("fromdate", fromdate);
                i.putExtra("todate", todate);
                i.putExtra("categoryselected", cateselected);
                i.putExtra("jsonarray", jsonArray);
                startActivityForResult(i, 30);
                break;
            case R.id.tryagain:
                showview(true);
                checklocationservice();
                break;
            case R.id.pguide:
                if (Paper.book().read("Islogin", false)) {
//                    cat = new Intent(context, PrivateSearch.class);
                    cat = new Intent(context, PrivateSearchActivity.class);
                    startActivity(cat);
                } else {
                    cat = new Intent();
                    cat.putExtra("keyMessage", "Please Login to Search or Download the Private Events");
                    cat.putExtra("keyAlert", "Login/Register");
                    cat.setClassName("com.webmobi.eventsapp", "com.webmobi.eventsapp.Views.TokenExpireAlertReceived");
                    cat.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(cat);
                }
                break;
            case R.id.search:
//                cat = new Intent(context, DisCover_Search.class);
                cat = new Intent(context, SearchActivity.class);
                startActivity(cat);
                break;
            case R.id.qrsearch_tab:
                checkCameraPermission();
                break;

        }
    }

    private void checkCameraPermission() {
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            // only for marsemellow and newer versions
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION);
            } else {

                //Do something
                Intent intent = new Intent(getActivity(), SimpleScannerActivity.class);
                startActivityForResult(intent, SCANNER_REQUEST_CODE);
            }
        } else {

            //Do something
            Intent intent = new Intent(getActivity(), SimpleScannerActivity.class);
            startActivityForResult(intent, SCANNER_REQUEST_CODE);
        }
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
            startLocationUpdates();
        } else {
            pDialog.dismiss();
            checklocationenabled();
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

        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_CODE);
        } else {
            gettinglocation();
        }
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

    public void deny() {
        if (pDialog.isShowing() && pDialog != null) {
            pDialog.dismiss();
            filmlists.setAdapter(null);
            filmlists.setVisibility(View.GONE);
            noitems.setVisibility(View.VISIBLE);
            noitems.setText("Enable your Location to find events around you");
        }
    }

    public void getLocation(double lat, double lng) {

        if (pDialog.isShowing() && pDialog != null)
            pDialog.dismiss();
        this.lat = lat;
        this.lng = lng;
        System.out.println("latitutude" + lat + "longitude" + lng);
        buildingurl(lat, lng, 0, false);

    }

    public void nearbyparams(String dateselected, boolean type, String fromdate, String todate, String cateselected, String array) {
        this.dateselected = dateselected;
        this.type = type;
        this.fromdate = fromdate;
        this.todate = todate;
        this.cateselected = cateselected;
        jsonArray = array;
        checklocationservice();
    }


    private void buildingurl(double lat, double lon, int offset, boolean flag) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateString, presentdate, city, category;
        String date, startdate = "", enddate = "";
        Calendar c = Calendar.getInstance();
        c.setTime(new Date()); // Now use today date.
        switch (dateselected) {
            case "All Dates":
                date = "anydate";
                break;
            case "Today":
                dateString = sdf.format(c.getTime());
                date = dateString;
                startdate = dateString;
                enddate = dateString;
                break;
            case "Tomorrow":
                c.add(Calendar.DATE, 1); // Adding 1 days
                dateString = sdf.format(c.getTime());
                date = dateString;
                startdate = dateString;
                enddate = dateString;
                break;
            case "Next Week":
                presentdate = sdf.format(c.getTime());
                c.add(Calendar.DATE, 7); // Adding 1 days
                dateString = sdf.format(c.getTime());
                date = presentdate;
                startdate = presentdate;
                enddate = dateString;
                break;
            case "Next Month":
                presentdate = sdf.format(c.getTime());
                c.add(Calendar.MONTH, 1); // Adding 1 month
                dateString = sdf.format(c.getTime());
                date = presentdate;
                startdate = presentdate;
                enddate = dateString;
                break;
            default:
                presentdate = sdf.format(new Date(fromdate));
                dateString = sdf.format(new Date(todate));
                date = presentdate;
                startdate = presentdate;
                enddate = dateString;
                break;
        }

        switch (cateselected) {
            case "All Event":
                category = "anycategory";
                break;
            default:
                category = jsonArray;
        }
        if (!flag)
            fetchingsearched(lat, lon, offset, date, startdate, enddate, category);
        else
            fetchingscollevnts(lat, lon, offset, date, startdate, enddate, category);
    }

    private void fetchingsearched(final double lat, final double lon, final int offset, final String date, final String startdate, final String enddate, final String category) {
        // listener.disabletab(1);
        mDilatingDotsProgressBar.showNow();
        final int distance = (int) Math.round(Paper.book().read("SettingProgress", 25) * 0.621371);
        String url = ApiList.Nearby;
        System.out.println("Nearby url" + url);
        final String tag_string_req = "Discovery Event";
        final StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                Log.d("Near by", "Response: " + response.toString());
                mDilatingDotsProgressBar.hideNow();
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
                // listener.enableall();
                mDilatingDotsProgressBar.hideNow();

                if (error instanceof TimeoutError) {
                    Error_Dialog.show("Timeout", getActivity());
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, context), getActivity());
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    showview(false);
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("offset", String.valueOf(offset));
                params.put("distance", String.valueOf(distance));
                params.put("latitude", String.valueOf(lat));
                params.put("longitude", String.valueOf(lon));
                params.put("category", category);
                params.put("date", date);
                params.put("startdate", startdate);
                params.put("enddate", enddate);
                System.out.println(params);
                return params;
            }

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

        App.getInstance().addToRequestQueue(strReq, tag_string_req);

    }


    private void fetchingscollevnts(final double lat, final double lon, final int offset, final String date, final String startdate, final String enddate, final String category) {

        // listener.disabletab(1);
        final int distance = (int) Math.round(Paper.book().read("SettingProgress", 25) * 0.621371);
        String url = ApiList.Nearby;
        System.out.println("Nearby url" + url);
        String tag_string_req = "Discovery Event";
        StringRequest strReq = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                Log.d("Near by", "Response: " + response.toString());
                try {
                    JSONObject json = new JSONObject(response);
                    if (json.getBoolean("response")) {
                        parseResult(json.getJSONArray("events"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // listener.enableall();
                if (error instanceof TimeoutError) {
                    Error_Dialog.show("Timeout", getActivity());
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, context), getActivity());
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show("Please Check Your Internet Connection", getActivity());
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("offset", String.valueOf(offset));
                params.put("distance", String.valueOf(distance));
                params.put("latitude", String.valueOf(lat));
                params.put("longitude", String.valueOf(lon));
                params.put("category", category);
                params.put("date", date);
                params.put("startdate", startdate);
                params.put("enddate", enddate);
                System.out.println(params);
                return params;
            }

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


        // Adding request to request queue
        App.getInstance().addToRequestQueue(strReq, tag_string_req);
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));


    }

    private void showitems(boolean flag) {
        // listener.enableall();
        if (flag) {
            filmlists.setVisibility(View.VISIBLE);
            noitems.setVisibility(View.GONE);
        } else {
            filmlists.setVisibility(View.GONE);
            noitems.setVisibility(View.VISIBLE);
            noitems.setText("No Event");
        }


    }

    private void parseResult(JSONArray jobj) {
        try {

            nearbyevents.clear();
            Gson gson = new Gson();

            for (int i = 0; i < jobj.length(); i++) {
                String eventString = jobj.getJSONObject(i).toString();
                Event obj = gson.fromJson(eventString, Event.class);

                String appurl = jobj.getJSONObject(i).getString("appid");

                if (savedirs.contains(appurl))
                    obj.setDownloaded(true);
                else
                    obj.setDownloaded(false);


                nearbyevents.add(obj);
            }


            if (nearbyevents.size() > 0) {
                f_adapter.notifyDataSetChanged();
                showitems(true);
            } else
                showitems(false);


            addscrolllistner();


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private void showview(boolean flag) {
        if (flag) {
            v1.setVisibility(View.VISIBLE);
            v2.setVisibility(View.GONE);
        } else {
            v1.setVisibility(View.GONE);
            v2.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View view, int position, View Imageview) {

        long viewId = view.getId();

        if (viewId == R.id.download) {
            Event events = (Event) view.getTag();
            savedeviceapp(events, position);
        } else if (viewId == R.id.moredetails) {
            Event events = (Event) view.getTag();
            if (events.getAccesstype().equalsIgnoreCase("dashboard")) {
                if (!events.isDownloaded()) {

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("events", events);
                    Intent intent = new Intent(getActivity(), Enter_DetailActivity.class);
                    intent.setAction(ApiList.EventCategory);
                    intent.putExtras(bundle);
                    intent.putExtra("pos", position);
                    intent.putExtra("Disc_DetailActivity:image", events.getApp_image());
                    startActivityForResult(intent, 2);

                } else {
                    try {
                        openevent(events.getAppid());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            } else {
               /* Bundle bundle = new Bundle();
                bundle.putSerializable("events", events);
                Intent intent = new Intent(getActivity(), Disc_DetailActivity.class);
                intent.putExtras(bundle);
                intent.putExtra("Disc_DetailActivity:image", events.getApp_image());
                getActivity().startActivity(intent);*/
                Intent intent = new Intent(getActivity(), DiscoveryEventDetails.class);
                intent.putExtra(DiscoveryEventDetails.DISCOVERY_DETAILS_APPID, events.getAppid());
                getActivity().startActivity(intent);
            }

        }


    }

    private void savedeviceapp(final Event events, final int position) {


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
                    System.out.println(response);
                    JSONObject jObj = new JSONObject(response);


                    if (jObj.getBoolean("response")) {


                        downloadjson(events, position);

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
                params.put("appid", events.getAppid());
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


    private void downloadjson(final Event events, final int downpos) {

        String jsonUrl = baseUrl + events.getApp_url() + "/appData.json";
        pDialog = new ProgressDialog(context, R.style.MyAlertDialogStyle);
        pDialog.setMessage("Downloading ... ...");
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.show();


        String tag_string_req = "Downloading";
        System.out.println("Url " + jsonUrl);
        StringRequest jsonRequest = new StringRequest(Request.Method.GET, jsonUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialog.dismiss();
                System.out.println("JSON Response " + response);

                eventDir = new File(dir + events.getAppid());
                if (!eventDir.exists())
                    eventDir.mkdir();
                jsonFile = new File(eventDir, filename);
                descFile = new File(eventDir, "description.txt");
                try {
                    Files.write(response, jsonFile, Charset.defaultCharset());
                    Files.append(events.getApp_name() + "\n", descFile, Charset.defaultCharset());
                    Files.append(events.getApp_category() + "\n", descFile, Charset.defaultCharset());
                    Files.append(events.getStart_date() + "\n", descFile, Charset.defaultCharset());
                    Files.append(events.getAppid() + "\n", descFile, Charset.defaultCharset());
                    Files.append(events.getLocation() + "\n", descFile, Charset.defaultCharset());
                    Files.append(events.getApp_title() + "\n", descFile, Charset.defaultCharset());
                    Files.append(events.getVenue() + "\n", descFile, Charset.defaultCharset());
                    Files.append(events.getApp_logo() + "\n", descFile, Charset.defaultCharset());
                    Files.append(events.getApp_url() + "\n", descFile, Charset.defaultCharset());
                    Files.append(events.getApp_image() + "\n", descFile, Charset.defaultCharset());

                    Event update = nearbyevents.get(downpos);
                    update.setDownloaded(true);
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
                    Error_Dialog.show("Timeout", getActivity());
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, context), getActivity());
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
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


    @Override
    public void onResume() {
        super.onResume();
        f_adapter.notifyDataSetChanged();

    }


    protected synchronized void buildGoogleApiClient() {

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();

        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    protected void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        builder.setAlwaysShow(true);
        mLocationSettingsRequest = builder.build();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (DataBaseStorage.isInternetConnectivity(getActivity().getApplicationContext()))
            checklocationservice();
        else
            showview(false);
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startLocationUpdates();
                } else {
                    // listener.enableall();
                    deny();
                }
            }
            break;
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case 100:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        startLocationUpdates();
                        Log.i("", "User agreed to make required location settings changes.");
                        break;
                    case Activity.RESULT_CANCELED:
                        Log.i("", "User chose not to make required location settings changes.");
                        break;

                }
                break;

            case 30:
                if (resultCode == Activity.RESULT_OK) {
                    String fromdate, todate;
                    boolean type = data.getExtras().getBoolean("type");
                    if (type) {
                        fromdate = data.getExtras().getString("fromdate");
                        todate = data.getExtras().getString("todate");
                    } else {
                        fromdate = "";
                        todate = "";
                    }
                    nearbyparams(data.getExtras().getString("selected"), data.getExtras().getBoolean("type"), fromdate, todate, data.getExtras().getString("selcategory"), data.getExtras().getString("array"));
                }
                break;

            case 2:
                if (resultCode == Activity.RESULT_OK) {
                    if (data.getExtras() != null) {
                        int pos = data.getIntExtra("pos", 0);
                        //  listener.enableall();
                        Event update = nearbyevents.get(pos);
                        update.setDownloaded(true);

                        try {
                            openevent(update.getAppid());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;
            case SCANNER_REQUEST_CODE: {

                if (resultCode == RESULT_OK) {
                    String id = data.getStringExtra("content");
                    System.out.println("Event Fragment Content " + id);
                    searchFilter(id);

                }
            }


        }
    }

    private void searchFilter(String id) {
        String privacy, name;
        if (id.contains("=")) {
            privacy = id.substring(0, id.lastIndexOf("="));
            name = id.substring(id.lastIndexOf("=") + 1);
            System.out.println("Event Fragment Content Privacy : " + privacy + " Name : " + name);
            if (!privacy.equalsIgnoreCase("private")) {
                doSmth(name, false);
            } else
                doSmth(name, true);
        }else{
            Error_Dialog.show("Not a valid QR Code",getActivity());
        }
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
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
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
        if (isPrivate) {
            if (!Paper.book().read("Islogin", false)) {
                dialog.dismiss();
                Error_Dialog.show("Please Login to view private event", getActivity());
                return;
            } else {
                isPrivateEvent = true;
                url = ApiList.PSearchEvents1 + searchstring + "&userid=" + Paper.book().read("userId");
            }
        } else {
            if (searchstring.endsWith("_preview")) {
                if (!Paper.book().read("Islogin", false)) {
                    dialog.dismiss();
                    Error_Dialog.show("Please Login to view preview of event", getActivity());
                    return;
                } else {

                }
                isPreview = true;
                prevSearch = searchstring.substring(0, searchstring.lastIndexOf("_"));
//            searchstring = prevSearch;
                url = ApiList.SearchEvents + prevSearch + "&preview_flag=1" + "&userid=" + Paper.book().read("userId", "");
            } else
                url = ApiList.SearchEvents + searchstring + "&preview_flag=0";
        }
        // url = "https://api.webmobi.com/api/event/event_search?q=demopreview&preview_flag=1";
        System.out.println("Search URL : " + url);
//        final String finalSearchstring = searchstring;
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

        ImageView close_dlg = (ImageView) event_dialog.findViewById(R.id.dlg_close_nearby);
        RoundedImageView clogo_center = (RoundedImageView) event_dialog.findViewById(R.id.dlg_logo2_nearby);
        ImageView clogo = (ImageView) event_dialog.findViewById(R.id.dlg_banner_nearby);
        final TextView app_name = (TextView) event_dialog.findViewById(R.id.dlg_app_name_nearby);
        final TextView event_date = (TextView) event_dialog.findViewById(R.id.dlg_date_nearby);
        TextView download = (TextView) event_dialog.findViewById(R.id.dlg_download_event_nearby);
        TextView description = (TextView) event_dialog.findViewById(R.id.dlg_description_nearby);
        ImageView addwishlist = (ImageView) event_dialog.findViewById(R.id.dlg_addwishlist_nearby);
        LinearLayout add2calendar = (LinearLayout) event_dialog.findViewById(R.id.dlg_add2calendar_nearby);
        LinearLayout invite = (LinearLayout) event_dialog.findViewById(R.id.dlg_invite_nearby);

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
                            Intent intent = new Intent(context, SingleEventHome.class);intent.putExtra("Engagement","");intent.putExtra("back","APPS");
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
        RelativeLayout v2 = (RelativeLayout) event_dialog.findViewById(R.id.v2_nearby);
        // CardView v2 = (CardView) child.findViewById(R.id.main_content);
        // FrameLayout.LayoutParams logolayoutParams = (FrameLayout.LayoutParams)v2.getLayoutParams();
        FrameLayout.LayoutParams logolayoutParams = (FrameLayout.LayoutParams) v2.getLayoutParams();
        logolayoutParams.width = (int) (dlg_ImgWidth * 2.30);
        logolayoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        v2.setLayoutParams(logolayoutParams);
//            clogo_height = (int) (ImgHeight * 0.40);
        clogo_height = (int) (dlg_ImgHeight * 0.57);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams((int) (dlg_ImgWidth * 2.30), clogo_height);
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


        RelativeLayout logo2_layout = (RelativeLayout) event_dialog.findViewById(R.id.logo2_layout_nearby);
        margintop = (int) (clogo_height - (getResources().getDimensionPixelSize(R.dimen.abc_action_bar_default_height_material) / 1.5));
        RelativeLayout.LayoutParams logo2_layout_params = new RelativeLayout.LayoutParams((int) (dlg_ImgWidth * 0.65), (int) (dlg_ImgWidth * 0.65));
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

        mapFragment = (SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.dlg_map_nearby);
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

    private void showjs(File jsonFile) throws IOException {
        ProgressDialog dialog = new ProgressDialog(context, R.style.MyAlertDialogStyle);
        dialog.setMessage("Loading Event..");
        dialog.show();

        String contents = Files.toString(jsonFile, Charset.defaultCharset());

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
                    Intent intent = new Intent(context, SingleEventHome.class);intent.putExtra("Engagement","");intent.putExtra("back","APPS");
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

    private void openOffline(File jsonFile, boolean isPreview) throws IOException {
        ProgressDialog dialog = new ProgressDialog(context, R.style.MyAlertDialogStyle);
        dialog.setMessage("Loading Event..");
        dialog.show();

        String contents = Files.toString(jsonFile, Charset.defaultCharset());
        System.out.println("File Contents : " + contents);

        offlineevents = new ArrayList<>();
        Gson gson = new Gson();


        JSONObject args = null;
        try {
            args = getAppDetailsFromJSON(contents);
            AppDetails obj = gson.fromJson(args.toString(), AppDetails.class);
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
            parseresult(args, obj.getAppId());


        } catch (JSONException e) {
            e.printStackTrace();
        }
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
        Intent sending = new Intent(getActivity(), SingleEventHome.class);sending.putExtra("Engagement","");sending.putExtra("back","APPS");
        startActivity(sending);
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

    private void deleteEventFromCalendar(long appid, ContentResolver cr) {

        Uri eventUri = Uri.parse("content://com.android.calendar/events");
        Uri deleteUri = null;
        deleteUri = ContentUris.withAppendedId(eventUri, appid);
        int rows = cr.delete(deleteUri, null, null);

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
            if (DataBaseStorage.isInternetConnectivity(getApplicationContext())) {
                if (appDetails.getForceUpdate())
                    forceUpdate(appDetails, tag);
                else
                    showjs(jsonFile);
            } else {
                openOffline(jsonFile, false);
                Intent sending = new Intent(getActivity(), SingleEventHome.class);sending.putExtra("Engagement","");sending.putExtra("back","APPS");
                startActivity(sending);
            }

        } catch (Exception e) {

        }

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
                    eventDir = new File(dir + appDetails.getAppId());
                    jsonFile = new File(eventDir, filename);
                    jsonFile.delete();
                    descFile = new File(eventDir, "description.txt");

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

}
