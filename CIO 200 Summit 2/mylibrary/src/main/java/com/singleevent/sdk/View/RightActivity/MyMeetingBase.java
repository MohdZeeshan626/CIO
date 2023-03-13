package com.singleevent.sdk.View.RightActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;

import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.SpannableString;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.singleevent.sdk.ApiList;
import com.singleevent.sdk.App;
import com.singleevent.sdk.BuildConfig;
import com.singleevent.sdk.Custom_View.Error_Dialog;
import com.singleevent.sdk.Custom_View.Util;
import com.singleevent.sdk.Custom_View.VolleyErrorLis;
import com.singleevent.sdk.feeds_class.Scheduler;
import com.singleevent.sdk.model.EventIDObj;
import com.singleevent.sdk.model.My_Request;
import com.singleevent.sdk.R;
import com.singleevent.sdk.View.Fragment.Left_Fragment.MyAdapter;
import com.singleevent.sdk.databinding.ActivitySchedulerBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by webMOBI on 10/11/2017.
 */

public class MyMeetingBase extends AppCompatActivity implements Scheduler {

    float dpWidth;
    ArrayList<My_Request> requestlist;
    private MyAdapter mAdapter;
    LinearLayoutManager mLayoutmanger;

    ActivitySchedulerBinding binding;
    String appid, userid, appname;
    ArrayList<Long> evenIdList = new ArrayList<>();
    EventIDObj obj;
    private ProgressDialog dialog1;
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 108;
    private static final String EVENT_DATA_LIST_NAME = "EVENTLIST";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_scheduler);


        binding.nofav.setText("No Meetings");
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        dpWidth = displayMetrics.widthPixels * 0.18F;
        requestlist = new ArrayList<>();

//        if (Build.VERSION.SDK_INT >= 23) {
//            checkPermission();
//        }
        // Initialize recycler view
        mLayoutmanger = new LinearLayoutManager(this);
        binding.recyclerview.setLayoutManager(mLayoutmanger);

        mAdapter = new MyAdapter(requestlist, MyMeetingBase.this, false);
        binding.recyclerview.setAdapter(mAdapter);

        binding.recyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int firstVisibleItem = mLayoutmanger.findFirstVisibleItemPosition();
                if (firstVisibleItem == 0)
                    binding.container.setEnabled(true);
                else
                    binding.container.setEnabled(false);

            }
        });


        binding.container.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()

        {
            @Override
            public void onRefresh() {
                binding.container.setRefreshing(false);
                getslots();
            }
        });


        binding.btnsyns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dexter.withActivity(MyMeetingBase.this).withPermissions(Manifest.permission.WRITE_CALENDAR,Manifest.permission.READ_CALENDAR).withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        dialog1 = new ProgressDialog(MyMeetingBase.this, R.style.MyAlertDialogStyle);
                        dialog1.setMessage("Syncing....");
                        dialog1.show();
                        obj = new EventIDObj(new ArrayList<Long>());

                        SharedPreferences mPrefs = getSharedPreferences(EVENT_DATA_LIST_NAME, MODE_PRIVATE);
                        evenIdList.clear();
                        Gson gson = new Gson();
                        String json = mPrefs.getString("MyObject", "");
                        EventIDObj obj = gson.fromJson(json, EventIDObj.class);
                        if (obj != null)
                            if (obj.getUriList().size() > 0)
                                for (int count = 0; count < obj.getUriList().size(); count++) {
                                    ContentResolver resolver = getContentResolver();
                                    getContentResolver().delete(Uri.parse("content://com.android.calendar/events"),
                                            String.valueOf(obj.getUriList().get(count)), null);
                  /* new QueryHandler(resolver).deleteEvent(1,null,obj.getUriList().get(count),
                           null,null);*/
                                }

                        if (requestlist.size() > 0) {
                            for (int index = 0; index < requestlist.size(); index++) {
                                if (requestlist.get(index).getBooking_status().equalsIgnoreCase("accepted")) {

                                    ContentResolver resolver = getContentResolver();
                                    new QueryHandler(resolver).insertEvent(MyMeetingBase.this,
                                            Util.getTime_for_meeting(requestlist.get(index).getFrom_time()),
                                            Util.getTime_for_meeting(requestlist.get(index).getTo_time()), requestlist.get(index).getTitle());
                                }
                            }

                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {

                    }
                }).onSameThread().check();

            }
        });


    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CALENDAR)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_CALENDAR, Manifest.permission.READ_CALENDAR},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted

        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        if (requestCode == MY_PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // permission was granted, yay! Do the
                // contacts-related task you need to do.

            } else {

//                finish();
                // permission denied, boo! Disable the
                // functionality that depends on this permission.
            }

        }
    }


    public void init_myrequest(String theme_color, String appid, String userid, String appname) {
        this.appid = appid;
        this.userid = userid;
        this.appname = appname;
        binding.toolbar.setBackgroundColor(Color.parseColor(theme_color));
        binding.btnsyns.setBackgroundColor(Color.parseColor(theme_color));
        setSupportActionBar(binding.toolbar);

        binding.container.setColorSchemeColors(Color.parseColor(theme_color), Color.GREEN, Color.BLUE, Color.YELLOW);


    }

    public void getslots() {

        final ProgressDialog dialog = new ProgressDialog(MyMeetingBase.this, R.style.MyAlertDialogStyle);
        dialog.setMessage("Fetching the booked timeslot....");
        dialog.show();
        String tag_string_req = "Getting_Slot";
        String url = ApiList.Get_Mymeetings + appid + "&userid=" + userid;
        StringRequest strReq = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                try {
                    System.out.println(response);
                    JSONObject jObj = new JSONObject(response);

                    if (jObj.getBoolean("response")) {
                        parsetimeslot(jObj.getJSONArray("meetings"));
                    } else {
                        if (jObj.getString("responseString").equalsIgnoreCase("Invalid token")) {
                            Error_Dialog.show("Session is Expired Please Login", MyMeetingBase.this);
                        } else {
                            Error_Dialog.show(jObj.getString("responseString"), MyMeetingBase.this);
                            binding.recyclerview.setVisibility(View.GONE);
                            binding.nofav.setVisibility(View.VISIBLE);
                            binding.btnsyns.setVisibility(View.GONE);
                        }
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
                    Error_Dialog.show("Timeout", MyMeetingBase.this);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, MyMeetingBase.this), MyMeetingBase.this);
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show("Please Check Your Internet Connection", MyMeetingBase.this);
                }

            }
        });


        App.getInstance().addToRequestQueue(strReq, tag_string_req);
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));


    }


    private void parsetimeslot(JSONArray pending_requests) {
        try {
            requestlist.clear();
            Gson gson = new Gson();

            for (int i = 0; i < pending_requests.length(); i++) {
                String eventString = null;

                eventString = pending_requests.getJSONObject(i).toString();
                My_Request data = gson.fromJson(eventString, My_Request.class);
                requestlist.add(data);

                if (requestlist.size() > 0) {
                    mAdapter.notifyDataSetChanged();
                    binding.recyclerview.setVisibility(View.VISIBLE);
                    binding.nofav.setVisibility(View.GONE);
                    binding.btnsyns.setVisibility(View.VISIBLE);
                } else {
                    binding.recyclerview.setVisibility(View.GONE);
                    binding.nofav.setVisibility(View.VISIBLE);
                    binding.btnsyns.setVisibility(View.GONE);
                }


            }
        } catch (JSONException e) {

        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        SpannableString s = new SpannableString("My Meetings");
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

    @Override
    public void accept(My_Request mtrequest) {

    }

    @Override
    public void decline(My_Request mtrequest) {

    }


    //inner class for storing events

    /**
     * Created by webMOBI on 4/11/2018.
     */

// QueryHandler
    public class QueryHandler extends AsyncQueryHandler {
        private static final String TAG = "QueryHandler";

        // Projection arrays
        private final String[] CALENDAR_PROJECTION = new String[]
                {
                        CalendarContract.Calendars._ID
                };

        // The indices for the projection array above.
        private static final int CALENDAR_ID_INDEX = 0;

        private static final int CALENDAR = 0;
        private static final int EVENT = 1;
        private static final int REMINDER = 2;

        private QueryHandler queryHandler;

        // QueryHandler
        public QueryHandler(ContentResolver resolver) {
            super(resolver);

        }

        public void deleteEvent(int token,
                                Object cookie,
                                Long uri,
                                String selection,
                                String[] selectionArgs) {

            /*startDelete(EVENT, null, Uri.parse(uri), null,null);*/
        }

        // insertEvent
        public void insertEvent(Context context, long startTime,
                                long endTime, String title) {
            ContentResolver resolver = context.getContentResolver();

            if (queryHandler == null)
                queryHandler = new QueryHandler(resolver);

            ContentValues values = new ContentValues();
            values.put(CalendarContract.Events.DTSTART, startTime);
            values.put(CalendarContract.Events.DTEND, endTime);
            values.put(CalendarContract.Events.TITLE, title);

            if (BuildConfig.DEBUG)
                Log.d(TAG, "Calendar query start");

            queryHandler.startQuery(CALENDAR, values, CalendarContract.Calendars.CONTENT_URI,
                    CALENDAR_PROJECTION, null, null, null);
        }

        // onQueryComplete
        @Override
        public void onQueryComplete(int token, Object object, Cursor cursor) {
            // Use the cursor to move through the returned records
            cursor.moveToFirst();

            // Get the field values
            long calendarID = cursor.getLong(CALENDAR_ID_INDEX);

            if (BuildConfig.DEBUG)
                Log.d(TAG, "Calendar query complete " + calendarID);

            ContentValues values = (ContentValues) object;
            values.put(CalendarContract.Events.CALENDAR_ID, calendarID);
            values.put(CalendarContract.Events.EVENT_TIMEZONE,
                    TimeZone.getDefault().getDisplayName());

            startInsert(EVENT, null, CalendarContract.Events.CONTENT_URI, values);

        }

        // onInsertComplete
        @Override
        public void onInsertComplete(int token, Object object, Uri uri) {
            if (uri != null) {
                if (BuildConfig.DEBUG)
                    Log.d(TAG, "Insert complete " + uri.getLastPathSegment());

                switch (token) {
                    case EVENT:
                        long eventID = Long.parseLong(uri.getLastPathSegment());
                        ContentValues values = new ContentValues();
                        values.put(CalendarContract.Reminders.MINUTES, 10);
                        values.put(CalendarContract.Reminders.EVENT_ID, eventID);
                        values.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
                        startInsert(REMINDER, null, CalendarContract.Reminders.CONTENT_URI, values);
                        evenIdList.add(eventID);
                        System.out.println("List Items : " + evenIdList.toString());
                        // Paper.book().write(EVENT_DATA_LIST_NAME,evenIdList);
                        EventIDObj eventIDObj = new EventIDObj(evenIdList);
                        SharedPreferences mPrefs = getSharedPreferences(EVENT_DATA_LIST_NAME, MODE_PRIVATE);
                        SharedPreferences.Editor prefsEditor = mPrefs.edit();
                        Gson gson = new Gson();
                        String json = gson.toJson(eventIDObj);
                        prefsEditor.putString("MyObject", json);
                        prefsEditor.commit();
                        dialog1.hide();
                        Error_Dialog.show("Meeting date synced with calendar", MyMeetingBase.this);
                        break;

                }
            }
        }

        @Override
        protected void onUpdateComplete(int token, Object cookie, int result) {
            super.onUpdateComplete(token, cookie, result);

            if (BuildConfig.DEBUG)
                Log.d(TAG, "Update complete " + result + "");

        }


    }
}
