package com.webmobi.gecmedia.Views;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.CalendarContract;
import androidx.annotation.Nullable;

import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.appcompat.widget.Toolbar;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.singleevent.sdk.model.AppDetails;
import com.singleevent.sdk.model.Events;
import com.singleevent.sdk.model.LocalArraylist.Notes;
import com.singleevent.sdk.model.LocalArraylist.Rating;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.webmobi.gecmedia.Config.ApiList;
import com.webmobi.gecmedia.CustomViews.ColorFilterTransformation;
import com.webmobi.gecmedia.CustomViews.ConnectionDetector;
import com.webmobi.gecmedia.CustomViews.DilatingDotsProgressBar;
import com.singleevent.sdk.Custom_View.Error_Dialog;
import com.webmobi.gecmedia.CustomViews.VolleyErrorLis;
import com.webmobi.gecmedia.LocalDatabase.DatabaseHandler;
import com.webmobi.gecmedia.Models.Event;
import com.webmobi.gecmedia.Models.Events_Wishlist;
import com.webmobi.gecmedia.Models.OnItemClickListener;
import com.webmobi.gecmedia.R;
import com.webmobi.gecmedia.SingleEventHome;
import com.webmobi.gecmedia.Views.Adapter.EventCategoryAdapter;

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
import java.util.List;
import java.util.Map;

import io.paperdb.Paper;

/**
 * Created by Admin on 4/28/2017.
 */

public class EventCategory extends AppCompatActivity implements OnItemClickListener, AbsListView.OnScrollListener{

    Context context;
//        RecyclerView filmlists;
    ListView filmlists;
//    DisEventAdapter f_adapter;
    EventCategoryAdapter eventCategoryAdapter;
    Toolbar toolbar;
    String dir;
    String categoryname;

    TextView noitems, tryagain;

    ArrayList<Event> categoryevents;
    private List<String> savedirs;
    String regId;
    private DilatingDotsProgressBar mDilatingDotsProgressBar;
    Bundle bundle;
    RelativeLayout v1, v2;
    float ImgWidth, ImgHeight;
    String baseUrl = "https://webmobi.s3.amazonaws.com/nativeapps/";
    String filename = "app.json";
    static ProgressDialog pDialog;
    private ArrayList<Events> eventsToDisplay;
    LinearLayoutManager mLayoutManager;

    File eventDir, jsonFile, descFile;

    Dialog event_dialog;
    SupportMapFragment mapFragment;
    private GoogleMap mMap;
    DatabaseHandler db;
    private final int CALENDAR_PERMISSION_REQUEST_CODE = 2;
    String share_event_title, share_event_date, share_location, share_search_key;
    Dialog help_dialog;
    LinearLayout whatsapp_dlg, twitter_dlg, fb_dlg, linkedin_dlg;
    boolean isPreview = false;
    Dialog dialog;
    TextView dlg_title, dlg_msg, dlg_ok;
    TwitterAuthClient twitterAuthClient;
    String appID_str, appTtitle_str, appStartDate_str, appEndDate_str, appDescription_str;
    SharedPreferences spf;


    int currentFirstVisibleItem = 0;
    int currentVisibleItemCount = 0;
    int totalItemCount = 0;
    int currentScrollState = 0;
    boolean loadingMore = false;
    int page_count = 1;
RelativeLayout footer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Paper.init(this);
        setContentView(R.layout.category_events);
        regId = Paper.book().read("regId");
       /* DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        ImgWidth = displayMetrics.widthPixels;
        ImgHeight = displayMetrics.widthPixels * 0.50F;*/

        context = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        v1 = (RelativeLayout) findViewById(R.id.view1);
        v2 = (RelativeLayout) findViewById(R.id.view2);

        spf = getApplicationContext().getSharedPreferences(ApiList.LOCALSTORAGE, MODE_PRIVATE);
        db = new DatabaseHandler(this);

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        ImgWidth = displayMetrics.widthPixels * 0.30F;
        ImgHeight = ImgWidth * 1.7F;


        event_dialog = new Dialog(this);
        event_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        event_dialog.setContentView(R.layout.event_dialog);
        mDilatingDotsProgressBar = (DilatingDotsProgressBar) findViewById(R.id.progress);
        tryagain = (TextView) findViewById(R.id.tryagain);
//        filmlists = (RecyclerView) findViewById(R.id.filmlist);
        filmlists = (ListView) findViewById(R.id.filmlist);
        noitems = (TextView) findViewById(R.id.noitems);
        footer = (RelativeLayout) findViewById(R.id.list_item_footer);
        footer.setVisibility(View.GONE);
        noitems.setText("No Event");

        if (savedInstanceState == null) {
            bundle = getIntent().getExtras();
            categoryname = bundle.getString("categoryname");
        } else {
            categoryname = savedInstanceState.getString("categoryname");
        }

        mLayoutManager = new LinearLayoutManager(context);
        categoryevents = new ArrayList<>();
        categoryevents.clear();
//        filmlists.setLayoutManager(mLayoutManager);

        /*f_adapter = new DisEventAdapter(EventCategory.this, categoryevents, ImgWidth, ImgHeight, EventCategory.this);
        filmlists.setAdapter(f_adapter);*/

        eventCategoryAdapter = new EventCategoryAdapter(this, R.layout.search_row, categoryevents);
        filmlists.setAdapter(eventCategoryAdapter);
        /*eventCategoryAdapter = new EventCategoryAdapter(this, categoryevents);
        filmlists.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,
                false));
        filmlists.setAdapter(eventCategoryAdapter);
        eventCategoryAdapter.setOnCardClickListner(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            filmlists.setOnScrollChangeListener(this);

        }*/
        filmlists.setOnScrollListener(this);
        dir = getFilesDir() + File.separator + "EventsDownload" + File.separator;
        savedirs = new ArrayList<>();
        savedirs = getSaveDirs(dir);

        if (new ConnectionDetector(this).isConnectingToInternet()) {
            showview(true);
            mDilatingDotsProgressBar.showNow();
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    getCategoryEvents(ApiList.Category + categoryname + "&page_number=" + page_count);
                }
            }, 2000);


        } else {
            showview(false);
        }


        filmlists.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (categoryevents.get(i).getAccesstype().toString().equalsIgnoreCase("discovery")) {
                    Intent intent = new Intent(context, DiscoveryEventDetails.class);
                    intent.putExtra(DiscoveryEventDetails.DISCOVERY_DETAILS_APPID, categoryevents.get(i).getAppid());
                    startActivity(intent);
                } else
                    showEventDialog(i, categoryevents);
            }
        });
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
            filmlists.setVisibility(View.VISIBLE);
            noitems.setVisibility(View.GONE);
        } else {
            filmlists.setVisibility(View.GONE);
            noitems.setVisibility(View.VISIBLE);
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
    protected void onPostResume() {
        super.onPostResume();
        setTitle(categoryname);
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


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("categoryname", categoryname);

    }


    private void getCategoryEvents(String url) {
        String tag_string_req = "category events";
        url = url.replaceAll(" ", "%20");
        footer.setVisibility(View.VISIBLE);
        StringRequest strReq = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {

                mDilatingDotsProgressBar.hideNow();
                footer.setVisibility(View.GONE);
                try {
                    JSONObject json = new JSONObject(response);
                    if (json.getBoolean("response")) {
                        loadingMore = json.getBoolean("next_page");
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
                    Error_Dialog.show("Timeout", EventCategory.this);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, context), EventCategory.this);
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    showview(false);
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

            //categoryevents.clear();
            Gson gson = new Gson();

            for (int i = 0; i < events.length(); i++) {
                String eventString = events.getJSONObject(i).toString();
                Event obj = gson.fromJson(eventString, Event.class);

                String appurl = events.getJSONObject(i).getString("appid");

                if (savedirs.contains(appurl))
                    obj.setDownloaded(true);
                else
                    obj.setDownloaded(false);

                categoryevents.add(obj);
            }


            if (categoryevents.size() > 0) {
//                f_adapter.notifyDataSetChanged();
                eventCategoryAdapter.notifyDataSetChanged();
                showitems(true);
            } else
                showitems(false);


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private void downloadjson(final Event events, final int downpos) {

        String jsonUrl = baseUrl + events.getApp_url() + "/appData.json";
        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Downloading...");
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

                    Event update = categoryevents.get(downpos);
                    update.setDownloaded(true);
                    boolean sended = LocalBroadcastManager.getInstance(EventCategory.this).sendBroadcast(new Intent("com.reloadsavedevents"));

                    //openevent(events.getAppid());
//                    f_adapter.notifyDataSetChanged();
                    eventCategoryAdapter.notifyDataSetChanged();

                    finish();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                if (error instanceof TimeoutError) {
                    Error_Dialog.show("Timeout", EventCategory.this);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, context), EventCategory.this);
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show("No Internet Connection", EventCategory.this);
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
            Intent sending = new Intent(EventCategory.this, SingleEventHome.class);sending.putExtra("Engagement","");sending.putExtra("back","APPS");
            startActivity(sending);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                if (data.getExtras() != null) {


                    Event update = categoryevents.get(data.getIntExtra("pos", 0));
                    update.setDownloaded(true);

                    try {
                        openevent(update.getAppid());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
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
                    Intent intent = new Intent(EventCategory.this, Enter_DetailActivity.class);
                    intent.setAction(ApiList.EventCategory);
                    intent.putExtras(bundle);
                    intent.putExtra("pos", position);
                    intent.putExtra("Disc_DetailActivity:image", events.getApp_logo());
                    startActivityForResult(intent, 2);
                } else {
                    try {
                        openevent(events.getAppid());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }


        }


    }


    private void savedeviceapp(final Event events, final int downpos) {

        final ProgressDialog dialog = new ProgressDialog(EventCategory.this, R.style.MyAlertDialogStyle);
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


                        downloadjson(events, downpos);

                    } else {
                        Error_Dialog.show(jObj.getString("responseString"), EventCategory.this);
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

    //Event Dilaogbox

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
        if (categoryevents.get(i).getAccesstype().equalsIgnoreCase("discovery"))
            download.setVisibility(View.GONE);
        else
            download.setVisibility(View.VISIBLE);
        if (savedirs.contains(categoryevents.get(i).getAppid())) {
            download.setText("OPEN APP");
        } else {
            download.setText("DOWNLOAD");
        }

//        final Event listing = Searchevnets.get(i);

        final String action_name = download.getText().toString();
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (action_name.equalsIgnoreCase("DOWNLOAD"))
                    savedeviceapp(categoryevents.get(i), i);
                else {
                    event_dialog.dismiss();
//                    finish();
                    String appURL = categoryevents.get(i).getApp_url();
                    forceUpdate(appURL, i);
                }
            }
        });
        close_dlg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                event_dialog.dismiss();
            }
        });
        final String date_str = categoryevents.get(i).getStart_date().split("T")[0];

        app_name.setText(categoryevents.get(i).getApp_title());
        event_date.setText(date_str);
        description.setText(categoryevents.get(i).getApp_description());

        add2calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                appTtitle_str = listing.get(i).getApp_title();
                appStartDate_str = listing.get(i).getStart_date().split("T")[0];
                appEndDate_str = listing.get(i).getEnd_date().split("T")[0];
                appDescription_str = listing.get(i).getApp_description();
                appID_str = listing.get(i).getAppid();
                calendarpermission(true, appID_str, appTtitle_str, appStartDate_str, appEndDate_str, appDescription_str);
               /* if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(new String[]{Manifest.permission.WRITE_CALENDAR, Manifest.permission.READ_CALENDAR}, CALENDAR_PERMISSION_REQUEST_CODE);
                    }
                } else {
                    try {
                        syncCalendar(context, date_str, Searchevnets.get(i).getApp_title(), Searchevnets.get(i).getEnd_date().split("T")[0], Searchevnets.get(i).getAppid());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }*/
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
               /* sharedialog();
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

        Glide.with(getApplicationContext()).load((categoryevents.get(i).getApp_image().equalsIgnoreCase("")) ? R.drawable.medium_no_image : categoryevents.get(i).getApp_image())
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
        Glide.with(getApplicationContext()).load((categoryevents.get(i).getApp_logo().equalsIgnoreCase("")) ? R.drawable.logo : categoryevents.get(i).getApp_logo())
                .fitCenter()
                .placeholder(R.drawable.medium_no_image)
                .dontAnimate()
                .error(R.drawable.medium_no_image)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .bitmapTransform(new ColorFilterTransformation(context, Color.argb(0, 0, 0, 0)))
                .into(clogo_center);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.dlg_map);
//        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;

                MarkerOptions marker = new MarkerOptions().position(new LatLng(categoryevents.get(i).getLatitude(), categoryevents.get(i).getLongitude())).title(categoryevents.get(i).getVenue());
                marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker));
                mMap.addMarker(marker);

                LatLng latlng = new LatLng(categoryevents.get(i).getLatitude(), categoryevents.get(i).getLongitude());
                CameraPosition cameraPosition = new CameraPosition.Builder().target(latlng).zoom(8).build();
                mMap.getUiSettings().setZoomControlsEnabled(true);
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            }
        });


        if (db.getwishlist(categoryevents.get(i).getAppid()) > 0)
            addwishlist.setSelected(true);
        else
            addwishlist.setSelected(false);

        addwishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (db.getwishlist(categoryevents.get(i).getAppid()) > 0) {
                    db.deletewishlist(categoryevents.get(i).getAppid());
                    v.setSelected(false);
                } else {

                    Gson gson = new Gson();
                    String details = gson.toJson(categoryevents.get(i));
                    System.out.println("Text Details Value : " + details);
                    db.addingwishlist(new Events_Wishlist(categoryevents.get(i).getAppid(), details));
                    v.setSelected(true);
                }

                /*if (clickListener != null)
                    clickListener.onClick(v, listPosition, holder.addwishlist);*/

            }
        });
    }

    private void forceUpdate(String appURL, int i) {
        String query = "";
        try {
            query = URLEncoder.encode(appURL, "utf-8");
            String jsonUrl = baseUrl + query + "/appData.json";

            updateMyEventJsonBeforeOpening(jsonUrl, i);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void calendarpermission(boolean b, String appID_str, String appTtitle_str, String appStartDate_str, String appEndDate_str, String appDescription_str) {
        if (ActivityCompat.checkSelfPermission(EventCategory.this, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(EventCategory.this, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(EventCategory.this, new String[]{Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR}, 1);
        } else {
            AdddingTocalendar(b, appID_str, appTtitle_str, appStartDate_str, appEndDate_str, appDescription_str);


        }
    }

    private void AdddingTocalendar(boolean b, String appID_str, String appTtitle_str, String appStartDate_str, String appEndDate_str, String appDescription_str) {

        HashMap<String, String> getcalendarlist = Paper.book().read("CalendarList", new HashMap<String, String>());

        if (!getcalendarlist.containsKey(appID_str)) {
            Date Startdate = null, Enddate = null;
            String Startsplits = appStartDate_str;
            String Endsplits = appEndDate_str;
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

            try {
                Startdate = formatter.parse(Startsplits);
                Enddate = formatter.parse(Endsplits);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            long calID = 1;
            long startMillis = 0;
            long endMillis = 0;
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(Startdate);
            startMillis = calendar.getTimeInMillis();
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(Enddate);
            endMillis = calendar1.getTimeInMillis();
            ContentResolver cr = getContentResolver();
            ContentValues values = new ContentValues();
            values.put(CalendarContract.Events.DTSTART, startMillis);
            values.put(CalendarContract.Events.DTEND, endMillis);
            values.put(CalendarContract.Events.TITLE, appTtitle_str);
            values.put("allDay", 0);
            values.put(CalendarContract.Events.DESCRIPTION, appDescription_str);
            values.put(CalendarContract.Events.CALENDAR_ID, calID);
            values.put(CalendarContract.Events.EVENT_TIMEZONE, "America/Los_Angeles");
            Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);
            long eventID = Long.parseLong(uri.getLastPathSegment());
            Error_Dialog.show("Successfully added to Calendar", EventCategory.this);
            getcalendarlist.put(appID_str, appID_str);
            Paper.book().write("CalendarList", getcalendarlist);

        } else {
            Error_Dialog.show("Already added to Calendar", EventCategory.this);
        }

    }

    private void updateMyEventJsonBeforeOpening(String jsonUrl, final int i) {
        final ProgressDialog pDialog = new ProgressDialog(getApplicationContext(), R.style.MyAlertDialogStyle);
        /*pDialog.setMessage("Updating ...");
        pDialog.show();*/
        String tag_string_req = "Updating";
        System.out.println("Url " + jsonUrl);
        StringRequest jsonRequest = new StringRequest(Request.Method.GET, jsonUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                pDialog.dismiss();
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
                    eventDir = new File(dir + categoryevents.get(i).getAppid());
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
                    Error_Dialog.show("Timeout", EventCategory.this);
                } else if (com.singleevent.sdk.Custom_View.VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(com.singleevent.sdk.Custom_View.VolleyErrorLis.handleServerError(error, EventCategory.this), EventCategory.this);
                } else if (com.singleevent.sdk.Custom_View.VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show("Please Check Your Internet Connection", EventCategory.this);
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


    private void showjs(File jsonFile) throws IOException {
        ProgressDialog dialog = new ProgressDialog(context, R.style.MyAlertDialogStyle);
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
            String eventString = eventslist.getJSONObject(0).toString();
            Events eobj = gson.fromJson(eventString, Events.class);
            eventsToDisplay.add(eobj);
            Paper.book().write("Appevents", eventsToDisplay);
            dialog.dismiss();

            //for hiding preview in main screen of event
            Paper.book(obj.getAppId()).write("isPreview", false);

            int info_privacy = Paper.book(obj.getAppId()).read("InfoPrivacy", 0);
            //if user is logged in
            if (Paper.book().read("Islogin", false)) {

                if (info_privacy==0) {
                    //call checkin to check whether user is registered or not
                    FetchPublicUSerdata(obj.getAppId());
                } else {
                    //info_privacy is 1 call single event login
                    FetchPrivateUserData(obj.getAppId(), info_privacy);
                }


            } else {
                //if user is not logged in
                if (info_privacy==0) {
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


    private void FetchPublicUSerdata(final String appId) {

        final ProgressDialog dialog = new ProgressDialog(this, R.style.MyAlertDialogStyle);
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
                        Error_Dialog.show(jObj.getString("responseString"), EventCategory.this);
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
                    Error_Dialog.show("Timeout", EventCategory.this);
                } else if (com.webmobi.gecmedia.CustomViews.VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(com.webmobi.gecmedia.CustomViews.VolleyErrorLis.handleServerError(error, EventCategory.this), EventCategory.this);
                } else if (com.webmobi.gecmedia.CustomViews.VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show("Please Check Your Internet Connection", EventCategory.this);
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

    private void FetchPrivateUserData(final String appId, final int privacy) {

        final ProgressDialog dialog = new ProgressDialog(this, R.style.MyAlertDialogStyle);
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
                        parseresult(jObj.getJSONObject("responseString"), appId);
                        dialog.dismiss();
                    } else {
                        dialog.dismiss();
                        Error_Dialog.show(jObj.getString("responseString"), EventCategory.this);
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
                    Error_Dialog.show("Timeout", EventCategory.this);
                } else if (com.webmobi.gecmedia.CustomViews.VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(com.webmobi.gecmedia.CustomViews.VolleyErrorLis.handleServerError(error, EventCategory.this), EventCategory.this);
                } else if (com.webmobi.gecmedia.CustomViews.VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show("Please Check Your Internet Connection", EventCategory.this);
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

    private void parseresult(JSONObject details, String appId) throws JSONException {

        if (!details.isNull("profile_pic")) {
            Paper.book().write("profile_pic", details.getString("profile_pic"));
        }

        Paper.book(appId).write("token", details.getJSONObject("token").getString("token"));
        Paper.book().write("userId", details.getJSONObject("token").getString("userId"));
        Paper.book(appId).write("userId", details.getJSONObject("token").getString("userId"));
        Paper.book().write("username", details.getJSONObject("token").getString("username"));
        Paper.book().write("email", Paper.book().read("Email"));

        Paper.book(appId).write("admin_flag", details.getString("admin_flag"));

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
        Intent sending = new Intent(this, SingleEventHome.class);sending.putExtra("Engagement","");sending.putExtra("back","APPS");
        startActivity(sending);
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int scrollstate) {
        //assigning the scroll state while scrolling
        currentScrollState = scrollstate;
        isScrollCompleted();
    }

    private void isScrollCompleted() {
        //check whether the scroll is completed
        if (this.currentVisibleItemCount > 0 && this.currentScrollState == SCROLL_STATE_IDLE && this.totalItemCount == (currentFirstVisibleItem + currentVisibleItemCount)) {
            // In this way I detect if there's been a scroll which has completed
            if (loadingMore) {
                //increment the page_count and call the api again
                page_count++;
                getCategoryEvents(ApiList.Category + categoryname + "&page_number=" + page_count);
            }


        }
    }

    @Override
    public void onScroll(AbsListView absListView, int firstvisibleitem, int visibleitemcount, int totalitemcount) {
        //assigning the current values based on the scroll
        currentFirstVisibleItem = firstvisibleitem;
        currentVisibleItemCount = visibleitemcount;
        totalItemCount = totalitemcount;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        categoryevents.clear();
    }




}
