package com.webmobi.gecmedia.Views;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.provider.CalendarContract;

import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.text.SpannableString;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
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
import com.google.gson.Gson;
import com.singleevent.sdk.App;
import com.singleevent.sdk.Custom_View.Error_Dialog;
import com.singleevent.sdk.Custom_View.TxtVCustomFonts;
import com.singleevent.sdk.Custom_View.Util;
import com.webmobi.gecmedia.Config.ApiList;
import com.webmobi.gecmedia.CustomViews.ColorFilterTransformation;
import com.webmobi.gecmedia.LocalDatabase.DatabaseHandler;
import com.webmobi.gecmedia.Models.Event;
import com.webmobi.gecmedia.Models.Events_Wishlist;
import com.webmobi.gecmedia.Models.GetEventsModel;
import com.webmobi.gecmedia.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.paperdb.Paper;

public class DiscoveryEventDetails extends AppCompatActivity implements View.OnClickListener {
    public static final String DISCOVERY_DETAILS_APPID = "appid";
    public static final String DISCOVERY_DETAILS_LIST = "list";
    ArrayList<? extends Event> events_lists;
    String appid;
    ImageView details_banner, details_logo, addwishlist;
    float ImgWidth, ImgHeight;
    TxtVCustomFonts title;
    TextView date_txt, desc, event_location, invite_people, add_calendar, interest_dtl, interest_btn;
    SupportMapFragment mapFragment;
    private GoogleMap mMap;
    int clogo_height;
    GetEventsModel eventsModel;
    String date_start, date_end = null;
    String regId;
    DatabaseHandler db;
    private final int CALENDAR_PERMISSION_REQUEST_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Paper.init(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discovery_event_details);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle extras = getIntent().getExtras();

        events_lists = new ArrayList<Event>();

        if (extras == null)
            return;
        appid = extras.getString(DISCOVERY_DETAILS_APPID);

        System.out.println("GetEvents API AppNew ID : " + appid);
        getEvents(appid);
        db = new DatabaseHandler(this);

        regId = Paper.book().read("regId");

        //resources
        details_banner = (ImageView) findViewById(R.id.dis_details_banner);
        details_logo = (ImageView) findViewById(R.id.dis_details_logo);
        title = (TxtVCustomFonts) findViewById(R.id.dis_details_eventtitle);
        date_txt = (TextView) findViewById(R.id.dis_details_eventdate);
        event_location = (TextView) findViewById(R.id.dis_details_eventlocation);
        invite_people = (TextView) findViewById(R.id.dis_details_invite);
        desc = (TextView) findViewById(R.id.dis_details_desc);
        interest_dtl = (TextView) findViewById(R.id.dis_details_interest_dtl);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.dis_details_map);
        interest_btn = (TextView) findViewById(R.id.dis_details_int_btn);
        addwishlist = (ImageView) findViewById(R.id.dis_details_addwishlist);
        add_calendar = (TextView) findViewById(R.id.dis_details_calendar);

        //Click listeners
        addwishlist.setOnClickListener(this);
        add_calendar.setOnClickListener(this);
        interest_btn.setOnClickListener(this);
        invite_people.setOnClickListener(this);

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        ImgWidth = displayMetrics.widthPixels * 0.30F;
        float temp_imgwidth = ImgWidth;

//        ImgHeight = ImgWidth * 1.7F;
        ImgHeight = temp_imgwidth * 1.8F;
        clogo_height = (int) (ImgHeight * 1.30);

    }

    @Override
    protected void onResume() {
        super.onResume();
        //setting the title
        SpannableString s = new SpannableString("Events");
        setTitle(Util.applyFontToMenuItem(this, s));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dis_details_invite:
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String content = "Event : " + eventsModel.getEventDetail().get(0).getAppTitle() + "\n" + "Date : " + date_start + "\n" + "Location : " + eventsModel.getEventDetail().get(0).getLocation() + "\n" + "Search Key : " + eventsModel.getEventDetail().get(0).getAppName() + "\n" + "https://webmobi.com/";
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, content);
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, eventsModel.getEventDetail().get(0).getAppName());
                startActivity(Intent.createChooser(sharingIntent, "Share using"));
                break;
            case R.id.dis_details_int_btn:
                //click on interest button
                //check user is loggedin or not
                if (Paper.book().read("Islogin", false)) {
                    registeruser();
                } else
                    Error_Dialog.show("Please Login", DiscoveryEventDetails.this);
                break;
            case R.id.dis_details_addwishlist:
                if (db.getwishlist(appid) > 0) {
                    db.deletewishlist(appid);
                    view.setSelected(false);
                } else {

                    Gson gson = new Gson();
                    String details = gson.toJson(eventsModel.getEventDetail().get(0));
                    System.out.println("Text Details Value : " + details);
                    db.addingwishlist(new Events_Wishlist(appid, details));
                    view.setSelected(true);
                }
                break;
            case R.id.dis_details_calendar:
                calendarpermission(true);
                break;
        }
    }

    private void getEvents(final String appid) {
        final ProgressDialog dialog = new ProgressDialog(this, R.style.MyAlertDialogStyle);
        dialog.setMessage("Loading...");
        dialog.show();


        String tag_string_req = "GetEvents";
        String url = ApiList.GetEvents;
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                try {
                    dialog.dismiss();
                    System.out.println("Response Savedevice AppNew : " + response);
                    JSONObject jObj = new JSONObject(response);


                    if (jObj.getBoolean("response")) {

                        System.out.println("GetEvents API : " + jObj.toString());
                        parseData(response);
//                        downloadjson(i, app_url, appid);

                    } else {
                        Error_Dialog.show(jObj.getString("responseString"), DiscoveryEventDetails.this);
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

    private void parseData(String jObj) throws JSONException {

        Gson gson = new Gson();


        eventsModel = gson.fromJson(jObj, GetEventsModel.class);

        setValues(eventsModel);
        /*System.out.println("GetEvents API :");
        System.out.println("GetEvents API :");*/

    }

    private void setValues(final GetEventsModel eventsModel) {

        setInerestBtn(eventsModel.getUserExist());

        //setting the wish list icon
        if (db.getwishlist(appid) > 0)
            addwishlist.setSelected(true);
        else
            addwishlist.setSelected(false);


        date_start = eventsModel.getEventDetail().get(0).getStartDate().split("T")[0];
        date_end = eventsModel.getEventDetail().get(0).getEndDate().split("T")[0];
        String date_str;

        //setting the date
        if (date_start.equalsIgnoreCase(date_end))
            date_str = date_start;
        else
            date_str = date_start + " to " + date_end;

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, clogo_height);
        details_banner.setLayoutParams(layoutParams);

        //setting the banner
        Glide.with(getApplicationContext()).load((eventsModel.getEventDetail().get(0).getAppImage().equalsIgnoreCase("")) ? R.drawable.medium_no_image : eventsModel.getEventDetail().get(0).getAppImage())
                .fitCenter()
                .dontAnimate()
                .placeholder(R.drawable.medium_no_image)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.medium_no_image)
                .bitmapTransform(new ColorFilterTransformation(this, Color.argb(0, 0, 0, 0)))
                .into(details_banner);

        //setting the logo
        Glide.with(getApplicationContext()).load((eventsModel.getEventDetail().get(0).getAppLogo().equalsIgnoreCase("")) ? R.drawable.logo : eventsModel.getEventDetail().get(0).getAppLogo())
                .fitCenter()
                .dontAnimate()
                .placeholder(R.drawable.logo)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.logo)
                .bitmapTransform(new ColorFilterTransformation(this, Color.argb(0, 0, 0, 0)))
                .into(details_logo);


        //setting data
        title.setText(eventsModel.getEventDetail().get(0).getAppTitle());
        date_txt.setText(date_str);
        event_location.setText(eventsModel.getEventDetail().get(0).getLocation());
        desc.setText(eventsModel.getEventDetail().get(0).getAppDescription());


        //setting map
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;

                MarkerOptions marker = new MarkerOptions().position(new LatLng(eventsModel.getEventDetail().get(0).getLatitude(), eventsModel.getEventDetail().get(0).getLongitude())).title(eventsModel.getEventDetail().get(0).getVenue());
                marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker));
                mMap.addMarker(marker);

                LatLng latlng = new LatLng(eventsModel.getEventDetail().get(0).getLatitude(), eventsModel.getEventDetail().get(0).getLongitude());
                CameraPosition cameraPosition = new CameraPosition.Builder().target(latlng).zoom(8).build();
                mMap.getUiSettings().setZoomControlsEnabled(true);
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            }
        });

        setIntrestedUsers(eventsModel.getUserExist(), eventsModel.getUsers().getUserName(), eventsModel.getUsers().getUserLength());
    }

    private void setInerestBtn(Boolean stat) {
        //disabling the interest button once the user is registered
        if (stat)
            interest_btn.setVisibility(View.GONE);
        else
            interest_btn.setVisibility(View.VISIBLE);
    }

    private void setIntrestedUsers(Boolean userExist, String userName, Integer userLength) {
        //Setting the message for interest
        String final_string;
        //check whether the user is registered with the particular event
        if (userExist) {
            //if the user length is > 0
            if (userLength > 0)
                final_string = "You and " + userLength + " others are interested in this event";
            else
                final_string = "You are interested in this event";
        } else {
            //if the user is not registered
            if (userLength > 0) {
                //if the user is not registered and other users are registered
                final_string = userName + " and " + userLength + " others are interested in this event";
            }
            else if (userName.equalsIgnoreCase("")) {
                //if none of them are registered
                final_string = "Be the first person to show the interest";
            }
            else {
                //if only one user is registered
                final_string = userName + " interested in this event";
            }
        }

        interest_dtl.setText(final_string);
    }

    private void registeruser() {

        //registering the user to the event
        final ProgressDialog dialog = new ProgressDialog(DiscoveryEventDetails.this, R.style.MyAlertDialogStyle);
        dialog.setMessage("Register the User");
        dialog.show();
        String tag_string_req = "Login";
        String url = ApiList.discovereventsregister;
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                try {
                    System.out.println(response);
                    JSONObject jObj = new JSONObject(response);


                    if (jObj.getBoolean("response")) {
                        //changing the interest button visibility
                        setInerestBtn(true);
                        //setting the interested user message
                        setIntrestedUsers(true, eventsModel.getUsers().getUserName(), eventsModel.getUsers().getUserLength());



                    } else {
                        Error_Dialog.show(jObj.getString("responseString"), DiscoveryEventDetails.this);
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
                params.put("appid", appid);
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

    private void AdddingTocalendar(boolean b) {

        //adding to calendar
        HashMap<String, String> getcalendarlist = Paper.book().read("CalendarList", new HashMap<String, String>());

        if (!getcalendarlist.containsKey(appid)) {
            Date Startdate = null, Enddate = null;
            String Startsplits = eventsModel.getEventDetail().get(0).getStartDate().split("T")[0];
            String Endsplits = eventsModel.getEventDetail().get(0).getEndDate().split("T")[0];
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
            values.put(CalendarContract.Events.TITLE, eventsModel.getEventDetail().get(0).getAppTitle());
            values.put("allDay", 0);
            values.put(CalendarContract.Events.DESCRIPTION, eventsModel.getEventDetail().get(0).getAppDescription());
            values.put(CalendarContract.Events.CALENDAR_ID, calID);
            values.put(CalendarContract.Events.EVENT_TIMEZONE, "America/Los_Angeles");
            Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);
            long eventID = Long.parseLong(uri.getLastPathSegment());
            Error_Dialog.show("Successfully added to Calendar", DiscoveryEventDetails.this);
            getcalendarlist.put(appid, appid);
            Paper.book().write("CalendarList", getcalendarlist);

        } else {
            Error_Dialog.show("Already added to Calendar", DiscoveryEventDetails.this);
        }

    }

    public void calendarpermission(boolean b) {
        //checking calendar permission
        if (ActivityCompat.checkSelfPermission(DiscoveryEventDetails.this, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(DiscoveryEventDetails.this, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(DiscoveryEventDetails.this, new String[]{Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR}, 1);
        } else {
            AdddingTocalendar(b);


        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case CALENDAR_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    calendarpermission(true);
                } else {
                    Error_Dialog.show("Without Permission We Couldn't Add to Calendar", DiscoveryEventDetails.this);
                }
                break;
        }
    }
}
