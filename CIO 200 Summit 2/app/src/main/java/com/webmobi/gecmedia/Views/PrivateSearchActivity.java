package com.webmobi.gecmedia.Views;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.provider.CalendarContract;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.facebook.share.widget.ShareDialog;
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
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.tweetcomposer.ComposerActivity;
import com.webmobi.gecmedia.Config.ApiList;
import com.webmobi.gecmedia.CustomViews.ColorFilterTransformation;
import com.webmobi.gecmedia.CustomViews.VolleyErrorLis;
import com.webmobi.gecmedia.LocalDatabase.DatabaseHandler;
import com.webmobi.gecmedia.Models.Event;
import com.webmobi.gecmedia.Models.Events_Wishlist;
import com.webmobi.gecmedia.R;
import com.webmobi.gecmedia.SingleEventHome;
import com.webmobi.gecmedia.Views.Adapter.DisEventAdapter;
import com.webmobi.gecmedia.Views.Adapter.SearchAdapter;

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

public class PrivateSearchActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView close, dlg_banner, dlg_logo;
    EditText search_et;
    Handler handler = new Handler(Looper.getMainLooper() /*UI thread*/);
    Runnable workRunnable;
    Context context;
    String searchstr;
    TextView no_msg;
    ListView listView;
    ArrayList<Event> Searchevnets;
    private List<String> savedirs;
    private List<String> savePrevdirs;
    DisEventAdapter f_adapter;
    String dir, dir_prev;
    SearchAdapter searchAdapter;
    Dialog event_dialog;
    float ImgWidth, ImgHeight, lwidth, lheight;
    SupportMapFragment mapFragment;
    private GoogleMap mMap;
    DatabaseHandler db;
    String regId;
    String baseUrl = "https://webmobi.s3.amazonaws.com/nativeapps/";
    String filename = "app.json";
    static ProgressDialog pDialog;
    File eventDir, jsonFile, descFile;
    private ArrayList<Events> eventsToDisplay;
    SharedPreferences spf;
    private String passPhrase = "";

    private final int CALENDAR_PERMISSION_REQUEST_CODE = 2;
    String share_event_title, share_event_date, share_location, share_search_key;
    Dialog help_dialog;
    LinearLayout whatsapp_dlg, twitter_dlg, fb_dlg, linkedin_dlg;
    boolean isWhatsApp = false, isTwitter = false, isLinkedin = false;
    Dialog dialog;
    TextView dlg_title, dlg_msg, dlg_ok;
    TwitterAuthClient twitterAuthClient;

    String appID_str, appTtitle_str, appStartDate_str, appEndDate_str, appDescription_str;
    RelativeLayout v2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private_search);
        context = this;

        spf = getApplicationContext().getSharedPreferences(ApiList.LOCALSTORAGE, MODE_PRIVATE);

        Twitter.initialize(this);
        twitterAuthClient = new TwitterAuthClient();


        close = (ImageView) findViewById(R.id.pvt_search_close);
        search_et = (EditText) findViewById(R.id.pvt_search_editText);
        no_msg = (TextView) findViewById(R.id.pvt_no_events_msg);
        listView = (ListView) findViewById(R.id.pvt_search_list);
        v2 = (RelativeLayout) findViewById(R.id.view2);

        event_dialog = new Dialog(this);
        event_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        event_dialog.setContentView(R.layout.event_dialog);

        db = new DatabaseHandler(this);
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        ImgWidth = displayMetrics.widthPixels * 0.30F;
        ImgHeight = ImgWidth * 1.7F;
        lwidth = ImgWidth * 0.70F;
        lheight = lwidth * 0.75F;
        Searchevnets = new ArrayList<>();
        dir = getFilesDir() + File.separator + "EventsDownload" + File.separator;
        dir_prev = getFilesDir() + File.separator + "PreviewDownloaded" + File.separator;
        savedirs = new ArrayList<>();
        savePrevdirs = new ArrayList<>();
        savedirs = getSaveDirs(dir);
        for (int i = 0; i < savedirs.size(); i++)
            System.out.println("Savedir :" + savedirs.get(i));
        savePrevdirs = getSaveDirs(dir_prev);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        regId = Paper.book().read("regId");
        searchAdapter = new SearchAdapter(this, R.layout.search_row, Searchevnets);
        listView.setAdapter(searchAdapter);


        showitems(false, "To access private events please enter the code provided by your event's organizer");
//listView.setVisibility(View.VISIBLE);
        listView.setClickable(true);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (Searchevnets.get(i).getAccesstype().toString().equalsIgnoreCase("discovery")) {
                    Intent intent = new Intent(context, DiscoveryEventDetails.class);
                    intent.putExtra(DiscoveryEventDetails.DISCOVERY_DETAILS_APPID, Searchevnets.get(i).getAppid());
                    startActivity(intent);
                } else
                    showEventDialog(i, Searchevnets);
            }

        });

        /*search_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(final Editable editable) {
                if (editable.length() >= 3) {
                    handler.removeCallbacks(workRunnable);
                    workRunnable = new Runnable() {
                        @Override
                        public void run() {
                            doSmth(editable.toString());
                        }
                    };
                    handler.postDelayed(workRunnable, 500 *//*delay*//*);
                }

            }
        });*/

        search_et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    if (textView.length() >= 3)
                        doSmth(textView.getText().toString());
                }
                return false;
            }
        });

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
        if (Searchevnets.get(i).getAccesstype().equalsIgnoreCase("discovery"))
            download.setVisibility(View.GONE);
        else
            download.setVisibility(View.VISIBLE);
        if (savedirs.contains(Searchevnets.get(i).getAppid())) {
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
                    savedeviceapp(Searchevnets.get(i).getAppid(), i);
                else {
                    event_dialog.dismiss();
//                    finish();
                    String appURL = Searchevnets.get(i).getApp_url();
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
        final String date_str = Searchevnets.get(i).getStart_date().split("T")[0];

        app_name.setText(Searchevnets.get(i).getApp_title());
        event_date.setText(date_str);
        description.setText(Searchevnets.get(i).getApp_description());

        add2calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                appTtitle_str = listing.get(i).getApp_title();
                appStartDate_str = listing.get(i).getStart_date().split("T")[0];
                appEndDate_str = listing.get(i).getEnd_date().split("T")[0];
                appDescription_str = listing.get(i).getApp_description();
                appID_str = listing.get(i).getAppid();
                calendarpermission(true, appID_str, appTtitle_str, appStartDate_str, appEndDate_str, appDescription_str);
                /*if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(new String[]{Manifest.permission.WRITE_CALENDAR, Manifest.permission.READ_CALENDAR}, CALENDAR_PERMISSION_REQUEST_CODE);
                    }
                } else {
                    try {

                        syncCalendar(context, date_str, listing.get(i).getApp_title(), listing.get(i).getEnd_date().split("T")[0], listing.get(i).getAppid());
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

        Glide.with(getApplicationContext()).load((Searchevnets.get(i).getApp_image().equalsIgnoreCase("")) ? R.drawable.medium_no_image : Searchevnets.get(i).getApp_image())
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
        Glide.with(getApplicationContext()).load((Searchevnets.get(i).getApp_logo().equalsIgnoreCase("")) ? R.drawable.logo : Searchevnets.get(i).getApp_logo())
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

                MarkerOptions marker = new MarkerOptions().position(new LatLng(Searchevnets.get(i).getLatitude(), Searchevnets.get(i).getLongitude())).title(Searchevnets.get(i).getVenue());
                marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker));
                mMap.addMarker(marker);

                LatLng latlng = new LatLng(Searchevnets.get(i).getLatitude(), Searchevnets.get(i).getLongitude());
                CameraPosition cameraPosition = new CameraPosition.Builder().target(latlng).zoom(8).build();
                mMap.getUiSettings().setZoomControlsEnabled(true);
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            }
        });


        if (db.getwishlist(Searchevnets.get(i).getAppid()) > 0)
            addwishlist.setSelected(true);
        else
            addwishlist.setSelected(false);

        addwishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (db.getwishlist(Searchevnets.get(i).getAppid()) > 0) {
                    db.deletewishlist(Searchevnets.get(i).getAppid());
                    v.setSelected(false);
                } else {

                    Gson gson = new Gson();
                    String details = gson.toJson(Searchevnets.get(i));
                    System.out.println("Text Details Value : " + details);
                    db.addingwishlist(new Events_Wishlist(Searchevnets.get(i).getAppid(), details));
                    v.setSelected(true);
                }

                /*if (clickListener != null)
                    clickListener.onClick(v, listPosition, holder.addwishlist);*/

            }
        });
    }

    public void calendarpermission(boolean b, String appID_str, String appTtitle_str, String appStartDate_str, String appEndDate_str, String appDescription_str) {
        if (ActivityCompat.checkSelfPermission(PrivateSearchActivity.this, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(PrivateSearchActivity.this, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(PrivateSearchActivity.this, new String[]{Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR}, 1);
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
            Error_Dialog.show("Successfully added to Calendar", PrivateSearchActivity.this);
            getcalendarlist.put(appID_str, appID_str);
            Paper.book().write("CalendarList", getcalendarlist);

        } else {
            Error_Dialog.show("Already added to Calendar", PrivateSearchActivity.this);
        }

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

    private void displayDialog(String dlg_title_str, String dlg_msg_str) {
        callDialog();
        dlg_title.setText(dlg_title_str);
        dlg_msg.setText(dlg_msg_str);
        dialog.show();
    }

    public void callDialog() {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog);
        dlg_title = (TextView) dialog.findViewById(R.id.custom_dlg_title);
        dlg_msg = (TextView) dialog.findViewById(R.id.custom_dlg_msg);
        dlg_ok = (TextView) dialog.findViewById(R.id.custom_dlg_ok);
        dialog.setCancelable(false);
        dlg_ok.setOnClickListener(this);
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

    private void shareonFacebook() {
        ShareDialog shareDialog = new ShareDialog(this);
        if (shareDialog.canShow(ShareLinkContent.class)) {

            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setContentUrl(Uri.parse("https://www.webmobi.com/"))
                    .setQuote(getShareTextContent())
                    .build();
            shareDialog.show(linkContent);  // Show facebook ShareDialog
        }

    }

    private void shareOnTwitter() {
        boolean isTwitterAvailable = appInstalledOrNot("com.twitter.android");
        if (isTwitterAvailable) {
            twitterAuthClient.authorize(this, new com.twitter.sdk.android.core.Callback<TwitterSession>() {
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


    private void doSmth(final String searchstring) {

        final ProgressDialog dialog = new ProgressDialog(PrivateSearchActivity.this, ProgressDialog.THEME_HOLO_LIGHT);
        dialog.setMessage("Loading..");
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();

        String tag_string_req = "events";
        passPhrase = searchstring;
        String url = ApiList.PSearchEvents1 + searchstring + "&userid=" + Paper.book().read("userId");
        StringRequest strReq = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {

                System.out.println("Private Search Response : " + response);
                dialog.dismiss();
                try {
                    JSONObject json = new JSONObject(response);
                    if (json.getBoolean("response")) {
                        v2.setVisibility(View.GONE);
                        parseResult1(json.getJSONArray("events"), searchstring);
                    } else {
                        no_msg.setText(json.getString("responseString"));
                        System.out.println("Event Response : " + json.getString("responseString"));
                        showitems(false, "You are not registered to this event, Please contact the event organizer for registration");
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
                    Error_Dialog.show("Timeout", PrivateSearchActivity.this);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, context), PrivateSearchActivity.this);
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show("Please Check Your Internet Connection", PrivateSearchActivity.this);
                    v2.setVisibility(View.VISIBLE);
                    no_msg.setVisibility(View.GONE);
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

    private void parseResult1(JSONArray events, String searchString) {


        try {

            Searchevnets.clear();
            Gson gson = new Gson();

            for (int i = 0; i < events.length(); i++) {
                String eventString = events.getJSONObject(i).toString();
                Event obj = gson.fromJson(eventString, Event.class);

                String app_id = events.getJSONObject(i).getString("appid");

                Paper.book(app_id).write("PrivateKey", searchString);
                Paper.book(app_id).write("InfoPrivacy", obj.getInfo_privacy());

                if (savedirs.contains(app_id))
                    obj.setDownloaded(true);
                else
                    obj.setDownloaded(false);


                Searchevnets.add(obj);
            }


            if (Searchevnets.size() > 0) {
                Paper.book().write("PrivateEventInfo",Searchevnets);
                searchAdapter.notifyDataSetChanged();
                showitems(true, "");
            } else
                showitems(false, "No Events");


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private void showitems(boolean flag, String s) {
        if (flag) {
            listView.setVisibility(View.VISIBLE);
            no_msg.setVisibility(View.GONE);
        } else {
            listView.setVisibility(View.GONE);
            no_msg.setVisibility(View.VISIBLE);
            no_msg.setText(s);
            v2.setVisibility(View.GONE);
        }
    }


    private void savedeviceapp(final String appid, final int i) {

        final ProgressDialog dialog = new ProgressDialog(PrivateSearchActivity.this, R.style.MyAlertDialogStyle);
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

                        downloadjson(i);

                    } else {
                        Error_Dialog.show(jObj.getString("responseString"), PrivateSearchActivity.this);
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
                    Error_Dialog.show("Timeout", PrivateSearchActivity.this);
                } else if (com.webmobi.gecmedia.CustomViews.VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(com.webmobi.gecmedia.CustomViews.VolleyErrorLis.handleServerError(error, PrivateSearchActivity.this), PrivateSearchActivity.this);
                } else if (com.webmobi.gecmedia.CustomViews.VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show("Please Check Your Internet Connection", PrivateSearchActivity.this);
                }

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
                    eventDir = new File(dir + Searchevnets.get(i).getAppid());
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
                    Error_Dialog.show("Timeout", PrivateSearchActivity.this);
                } else if (com.singleevent.sdk.Custom_View.VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(com.singleevent.sdk.Custom_View.VolleyErrorLis.handleServerError(error, PrivateSearchActivity.this), PrivateSearchActivity.this);
                } else if (com.singleevent.sdk.Custom_View.VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show("Please Check Your Internet Connection", PrivateSearchActivity.this);
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
                        Error_Dialog.show(jObj.getString("responseString"), PrivateSearchActivity.this);
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
                    Error_Dialog.show("Timeout", PrivateSearchActivity.this);
                } else if (com.webmobi.gecmedia.CustomViews.VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(com.webmobi.gecmedia.CustomViews.VolleyErrorLis.handleServerError(error, PrivateSearchActivity.this), PrivateSearchActivity.this);
                } else if (com.webmobi.gecmedia.CustomViews.VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show("Please Check Your Internet Connection", PrivateSearchActivity.this);
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
                        Error_Dialog.show(jObj.getString("responseString"), PrivateSearchActivity.this);
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
                    Error_Dialog.show("Timeout", PrivateSearchActivity.this);
                } else if (com.webmobi.gecmedia.CustomViews.VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(com.webmobi.gecmedia.CustomViews.VolleyErrorLis.handleServerError(error, PrivateSearchActivity.this), PrivateSearchActivity.this);
                } else if (com.webmobi.gecmedia.CustomViews.VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show("Please Check Your Internet Connection", PrivateSearchActivity.this);
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

    private void downloadjson(final int downpos) {

        String jsonUrl = baseUrl + Searchevnets.get(downpos).getApp_url() + "/appData.json";
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
                JSONObject jObj = null;

                eventDir = new File(dir + Searchevnets.get(downpos).getAppid());
                if (!eventDir.exists())
                    eventDir.mkdir();
                jsonFile = new File(eventDir, filename);
                descFile = new File(eventDir, "description.txt");
                try {
                    jObj = new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String private_key_str = null, appid_str = null;

                try {
                    private_key_str = jObj.getString("private_key");
                    appid_str = jObj.getString("appId");
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                if (!(private_key_str.isEmpty() || private_key_str.equalsIgnoreCase("")))
                    Paper.book(appid_str).write("PrivateKey", private_key_str);

//                Paper.book(Searchevnets.get(downpos).getAppid()).write("PrivateKey", Searchevnets.get(downpos).getPrivate_key());

                try {

                    Files.write(response, jsonFile, Charset.defaultCharset());
                    Files.append(Searchevnets.get(downpos).getApp_name() + "\n", descFile, Charset.defaultCharset());
                    Files.append(Searchevnets.get(downpos).getApp_category() + "\n", descFile, Charset.defaultCharset());
                    Files.append(Searchevnets.get(downpos).getStart_date() + "\n", descFile, Charset.defaultCharset());
                    Files.append(Searchevnets.get(downpos).getAppid() + "\n", descFile, Charset.defaultCharset());
                    Files.append(Searchevnets.get(downpos).getLocation() + "\n", descFile, Charset.defaultCharset());
                    Files.append(Searchevnets.get(downpos).getApp_title() + "\n", descFile, Charset.defaultCharset());
                    Files.append(Searchevnets.get(downpos).getVenue() + "\n", descFile, Charset.defaultCharset());
                    Files.append(Searchevnets.get(downpos).getApp_logo() + "\n", descFile, Charset.defaultCharset());
                    Files.append(Searchevnets.get(downpos).getApp_url() + "\n", descFile, Charset.defaultCharset());
                    Files.append(Searchevnets.get(downpos).getApp_image() + "\n", descFile, Charset.defaultCharset());

                    /*if (action.equalsIgnoreCase(ApiList.EventCategory))
                        update(Searchevnets.get(downpos).getAppid(), downpos);
                    else if (action.equalsIgnoreCase(ApiList.Eventrecom))*/
                    update(Searchevnets.get(downpos).getAppid(), downpos);

                    if (event_dialog.isShowing())
                        event_dialog.dismiss();
                    openevent(Searchevnets.get(downpos).getAppid());

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
                    Error_Dialog.show("Timeout", PrivateSearchActivity.this);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    System.out.println("Search Error Server " + VolleyErrorLis.handleServerError(error, context));
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, context), PrivateSearchActivity.this);
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    System.out.println("Search Error No Internet Connection  ");
                    Error_Dialog.show("No Internet Connection", PrivateSearchActivity.this);
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

    private void update(String appid, int pos) {

        Intent i = new Intent();
        i.putExtra("pos", pos);
        i.putExtra("appid", appid);
        setResult(RESULT_OK, i);
        finish();

    }

    private void parseResult(JSONArray events, final String searchstring) {

        try {

            if (searchstring.endsWith("_preview")) {
                String admin_flag = events.getJSONObject(0).getString("admin_flag");
                if (!admin_flag.equals("admin")) {
                    Error_Dialog.show("Only Admin can Preview the AppNew", PrivateSearchActivity.this);

                    showitems(false, "");
                    return;
                }
            }
            Searchevnets.clear();
            Gson gson = new Gson();

            for (int i = 0; i < events.length(); i++) {
                String eventString = events.getJSONObject(i).toString();
                Event obj = gson.fromJson(eventString, Event.class);

                String appurl = events.getJSONObject(i).getString("appid");

                if (!searchstring.endsWith("_preview")) {
                    if (savedirs.contains(appurl)) {
                        obj.setDownloaded(true);
                    } else
                        obj.setDownloaded(false);
                    Searchevnets.add(obj);


                } else {
                    if (savePrevdirs.contains(appurl)) {
                        obj.setDownloaded(true);
                    } else
                        obj.setDownloaded(false);
                    Searchevnets.add(obj);

                }

            }

            if (Searchevnets.size() > 0) {
                searchAdapter.notifyDataSetChanged();
                showitems(true, "");
            } else
                showitems(false, "No events found");


        } catch (JSONException e) {
            e.printStackTrace();
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {

            case CALENDAR_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    startLocationUpdates();
                    AdddingTocalendar(true, appID_str, appTtitle_str, appStartDate_str, appEndDate_str, appDescription_str);
                } else {
                    Toast.makeText(context, "Enable calendar permission to add event to calendar", Toast.LENGTH_LONG).show();
                }
            }
            return;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
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
        }
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
            Intent sending = new Intent(PrivateSearchActivity.this, SingleEventHome.class);sending.putExtra("Engagement","");sending.putExtra("back","APPS");
            startActivity(sending);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
