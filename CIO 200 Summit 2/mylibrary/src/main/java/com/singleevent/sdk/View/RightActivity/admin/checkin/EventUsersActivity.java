package com.singleevent.sdk.View.RightActivity.admin.checkin;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import androidx.annotation.NonNull;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.common.primitives.Ints;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.singleevent.sdk.ApiList;
import com.singleevent.sdk.App;
import com.singleevent.sdk.Custom_View.Error_Dialog;
import com.singleevent.sdk.Custom_View.Util;
import com.singleevent.sdk.Custom_View.VolleyErrorLis;
import com.singleevent.sdk.model.AppDetails;
import com.singleevent.sdk.model.Events;
import com.singleevent.sdk.R;
import com.singleevent.sdk.View.RightActivity.admin.adapter.UserListAdapter;
import com.singleevent.sdk.View.RightActivity.admin.checkin.beacon.BeaconCheckin;
import com.singleevent.sdk.View.RightActivity.admin.model.EventUser;
import com.singleevent.sdk.View.RightActivity.admin.model.EventUsersHashMap;
import com.singleevent.sdk.View.RightActivity.admin.utils.Urls;
import com.singleevent.sdk.utils.DataBaseStorage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.paperdb.Paper;


public class EventUsersActivity extends AppCompatActivity implements VolleyResponseListener, UserListAdapter.OnCardClickListner {

    public static final String USER_LIST_REQ_TAG = "list_users_req";
    public static final String CHECK_IN_REQ_TAG = "check_in_users";
    private static final int SCANNER_REQUEST_CODE = 2;
    private static final String TAG = "ListUserActivity";
    private static final int ZBAR_CAMERA_PERMISSION = 12;
    private static final int REGISTER_USER_REQUEST = 101;
    private static Integer agendaId;
    public static final int FILE_WRITE_PERMISSION = 1;
 /*   Set<String> checkedUsers = new HashSet<>();
    Set<String> checkedEmails = new HashSet<>();*/

    ArrayList<String> checkedUsers = new ArrayList<>();
    ArrayList<String> tempcheckedUsers = new ArrayList<>();
    ArrayList<String> checkedEmails = new ArrayList<>();
    ArrayList<String> tempcheckedEmails = new ArrayList<>();
    private EventUsersHashMap eventUsersHashMap = App.getInstance().getEventUsersHashMap();

    private RecyclerView recyclerView;
    private Button scanBtn, btn_register, btn_beacon;
    private ImageView syncIV;
    private SwipeRefreshLayout swipeRefreshLayout;
    String usersUrl = Urls.getUsersUrl(), userId;
    String checkInUrl = Urls.getCheckInUrl();
    private ArrayList<EventUser> eventUserList = new ArrayList<>();
    private ArrayList<EventUser> tempeventUserList = new ArrayList<>();
    private ArrayList<String> ckInTimeInMillisList = new ArrayList<>();
    private ArrayList<String> tempckInTimeInMillisList = new ArrayList<>();
    private ArrayList<String> agenda_id_array_list = new ArrayList<>();
    private ArrayList<String> tempagenda_id_array_list = new ArrayList<>();
    //private Set<Integer> agenda_id_array_list = new LinkedHashSet<>();
    private AppDetails appDetails;
    private UserListAdapter userListAdapter;
    private Toolbar toolbar;
    Gson gson;
    private BottomSheetBehavior behavior;
    private View bottomSheet;
    EditText search_edittext;
    private TextView tvTotalUser, tvCheckedInUser;
    private SearchView searchView;
    private LinearLayout LayAds, LayOnline;
    ArrayList<EventUser> allEventUser;
    private String token, appId;
    private RequestQueue queue;
    EventUser checkedUser = null;
    String user_name, user_mail, checkin_UserID;
    boolean file_write_permission = false, boolean_print_online = false;

    Spinner spinner;
    Events e;
    private ArrayList<Events> events = new ArrayList<Events>();
    int pos = 0;
    HashMap<Integer, String> agenda_set;
    ArrayList<String> spinner_text;
    int spinnerID, agenda_id;

    boolean isPrintEnabled = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_eventusers);

        gson = new GsonBuilder().create();
        Paper.init(this);

        appDetails = Paper.book().read("Appdetails");
        Paper.book().write("appid", appDetails.getAppId());

        appId = appDetails.getAppId();
        queue = Volley.newRequestQueue(this);
        //testing apId
        // appId = "e4b11d36071d516d84e85c800d05e2ed6438";
        token = Paper.book().read("token", "");
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        userId = Paper.book().read("userId", "");
        isPrintEnabled = Paper.book(appDetails.getAppId()).read("print_status", false);
        syncIV = (ImageView) findViewById(R.id.iv_sync);
        recyclerView = (RecyclerView) findViewById(R.id.rvUserList);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        scanBtn = (Button) findViewById(R.id.btn_scan);
        btn_beacon = (Button) findViewById(R.id.btn_beacon);
        btn_register = (Button) findViewById(R.id.btn_register);
        search_edittext = (EditText) findViewById(R.id.search_edittext);
        bottomSheet = (View) findViewById(R.id.rlPopup);
        LayAds = (LinearLayout) findViewById(R.id.LayAds);
        LayOnline = (LinearLayout) findViewById(R.id.LayOnline);
        tvTotalUser = (TextView) findViewById(R.id.tvTotalUsers);
        tvCheckedInUser = (TextView) findViewById(R.id.tvCheckedInUser);


       // scanBtn.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));
      //  btn_beacon.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));
        toolbar.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));

      //  btn_register.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));
        scanBtn.setBackground(Util.setdrawable(EventUsersActivity.this, R.drawable.healthpostbut, Color.parseColor(appDetails.getTheme_color())));
        btn_beacon.setBackground(Util.setdrawable(EventUsersActivity.this, R.drawable.healthpostbut, Color.parseColor(appDetails.getTheme_color())));
        btn_register.setBackground(Util.setdrawable(EventUsersActivity.this, R.drawable.healthpostbut, Color.parseColor(appDetails.getTheme_color())));

        //  LayAds.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));
        LayOnline.setBackground(Util.setdrawable(EventUsersActivity.this, R.drawable.healthpostbut, Color.parseColor(appDetails.getTheme_color())));
        LayAds.setBackground(Util.setdrawable(EventUsersActivity.this, R.drawable.healthpostbut, Color.parseColor(appDetails.getTheme_color())));


        setSupportActionBar(toolbar);

        fileWritePermission(false);

//        scanFunc();
        Handler scanHandler = new Handler();
        scanHandler.postDelayed(new Runnable() {
            @Override
            public void run() {


            }
        }, 1000);

        syncIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //sending offline data when internet is available
                if (!Paper.book(appDetails.getAppId()).read("offlineReg", "").equals("") &&
                        DataBaseStorage.isInternetConnectivity(EventUsersActivity.this)) {

                    regesterAllOffline();

                } else if (Paper.book(appDetails.getAppId()).read("CheckedUsers", new ArrayList<String>()).size() > 0
                        && DataBaseStorage.isInternetConnectivity(EventUsersActivity.this)) {
                    sendOfflineCheckinData();
                }
            }
        });

        //setting adapter
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        userListAdapter = new UserListAdapter(this, eventUserList, 0 /*zero for event checkin */);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,
                false));
        recyclerView.setAdapter(userListAdapter);

        //Spinner Items

        spinner = (Spinner) findViewById(R.id.section_list_spinner);
        agenda_set = new HashMap<Integer, String>();
        spinner_text = new ArrayList<>();
        setSpinnerValues();
        spinnerID = spinner.getSelectedItemPosition();
        getAgendatID(agenda_set.get(spinnerID));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spinnerID = i;
                //getting agenda id

                agenda_id = getAgendatID(agenda_set.get(spinnerID));
                EventUsersActivity.agendaId = agenda_id;

                userListAdapter = new UserListAdapter(EventUsersActivity.this, eventUserList, agenda_id);
                recyclerView.setAdapter(userListAdapter);

                updateView();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        eventUserList = new ArrayList<>();
        ckInTimeInMillisList = new ArrayList<>();
        userListAdapter = new UserListAdapter(this, eventUserList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,
                false));
        recyclerView.setAdapter(userListAdapter);

        usersUrl = usersUrl + "appid=" + appDetails.getAppId() + "&userid=" + userId;


        // calling api to get all users to checkin
//        getUserDetails(usersUrl);


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (DataBaseStorage.isInternetConnectivity(EventUsersActivity.this)) {
                    if (token != null)
                        getUserDetails(usersUrl, true);
                } else
                    swipeRefreshLayout.setRefreshing(false);
            }

        });

        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        swipeRefreshLayout.setDistanceToTriggerSync(420);

        userListAdapter.setOnCardClickListner(this);

        behavior = BottomSheetBehavior.from(bottomSheet);
        behavior.setHideable(true);
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_DRAGGING:
                        Log.i("BottomSheetCallback", "BottomSheetBehavior.STATE_DRAGGING");
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        Log.i("BottomSheetCallback", "BottomSheetBehavior.STATE_SETTLING");
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        Log.i("BottomSheetCallback", "BottomSheetBehavior.STATE_EXPANDED");
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        Log.i("BottomSheetCallback", "BottomSheetBehavior.STATE_COLLAPSED");

                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:

                        Log.i("BottomSheetCallback", "BottomSheetBehavior.STATE_HIDDEN");
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
                    // Do something
                } else if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    // Do something
                } else {
                    // Do something
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0) {
                    // Scrolling up
                    if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                        behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                    } else {
                        behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                    }
                } else {
                    // Scrolling down
                    if (behavior.getState() == BottomSheetBehavior.STATE_HIDDEN) {
                        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    } else {
                        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    }
                }
            }
        });

        //filter search
        search_edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

                // filter your list from your input
                filter(s.toString());
                //you can use runnable postDelayed like 500 ms to delay search text
            }
        });

        btn_beacon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EventUsersActivity.this, BeaconCheckin.class);
                intent.putExtra(BeaconCheckin.USER_LISTS,eventUserList);
                intent.putExtra(BeaconCheckin.AGENDA_ID,spinner.getSelectedItemPosition());
                startActivity(intent);
            }
        });
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(EventUsersActivity.this, RegisterEventUserActivity.class);
                startActivityForResult(intent, REGISTER_USER_REQUEST);
            }
        });


    }//end oncreate method



    @Override
    protected void onDestroy() {
        super.onDestroy();
//        mtCentralManager.stopScan();
    }

    private int getAgendatID(String s) {
        int agenda_id = 0;
        for (Map.Entry<Integer, String> entry : agenda_set.entrySet()) {
            String value = entry.getValue();
            Integer key = entry.getKey();
            // do something with key and/or tab
            if (value.equalsIgnoreCase(s)) {
                System.out.println("Agenda Selected Position : " + key + " " + value);
                agenda_id = key;
                break;
            }

        }
        return agenda_id;
    }


    private void setSpinnerValues() {
        events = Paper.book().read("Appevents");
        e = events.get(0);
        for (int i = 0; i < e.getTabsSize(); i++) {
            if (e.getTabs(i).getCheckvalue().equalsIgnoreCase("agenda0")) {
                pos = i;
                break;
            }
        }

        agenda_set.put(0, "Event Checkin");
        spinner_text.add("Event Checkin");
        for (int i = 0; i < e.getTabs(pos).getAgendaSize(); i++) {
            String day_str = "Day " + (i + 1) + " ";
            for (int j = 0; j < e.getTabs(pos).getAgenda(i).getDetailSize(); j++) {
                String temp_str = day_str + e.getTabs(pos).getAgenda(i).getDetail(j).getTopic();
                int agenda_id = e.getTabs(pos).getAgenda(i).getDetail(j).getAgendaId();
                agenda_set.put(agenda_id, temp_str);
                spinner_text.add(temp_str);
                System.out.println("Agenda Details Agenda  :  " + agenda_id + " " + temp_str);
            }
        }

        // Initializing an ArrayAdapter
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this, R.layout.spinner_item, spinner_text
        );
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(spinnerArrayAdapter);
    }

    public void filter(String text) {
        List<EventUser> temp = new ArrayList();
        for (EventUser d : eventUserList) {
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
            if (d.getFirst_name().toLowerCase().contains(text.toLowerCase())) {
                temp.add(d);
            }
        }
        //update recyclerview
        userListAdapter.updateList(temp);
    }

    private void getUserDetails(String url, final boolean isFromSwipe) {
        swipeRefreshLayout.setRefreshing(true);
        final ProgressDialog pDialog = new ProgressDialog(EventUsersActivity.this,
                R.style.MyAlertDialogStyle);
        pDialog.setMessage("Please Wait ...");
        pDialog.show();
        String tag_string_req = "Updating";
        System.out.println("User Url " + url);

        String reqTag = USER_LIST_REQ_TAG;

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        swipeRefreshLayout.setRefreshing(false);
                        pDialog.dismiss();
                        try {
                            if (response.getString("response").equals("true")) {
                                JSONObject jsonObject = response.getJSONObject("responseString");
                                JSONArray array = jsonObject.getJSONArray("users");


                                eventUserList.clear();
                                MAINlOOP:
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject object = array.getJSONObject(i);
                                    JSONArray agenda_id_array=object.getJSONArray("agenda_id");
                                    for(int k=0;k<agenda_id_array.length();k++){
                                        String obj_agenda=agenda_id_array.getString(k);
                                        if(obj_agenda==null || obj_agenda.equals("null")){
                                            Log.e(TAG, "onResponse: agendaloop :"+obj_agenda.toString() );
                                            continue MAINlOOP;
                                        }
                                    }
                                    EventUser user = gson.fromJson(object.toString(), EventUser.class);
                                    eventUserList.add(user);
                                   /* for (EventUser eventUser:eventUserListBackup)
                                        if (eventUser==user)
                                            user.setCheckin_status(eventUser.getCheckin_status());

                                    // keep backup of server checkedin status
                                    if (user.getCheckin_status().equalsIgnoreCase("reached"))
                                        checkedUsersServer.add(user.getUserid());*/
                                }

                                allEventUser = new ArrayList<>();
                                // allEventUser.addAll(eventUserList);
                                Paper.book(appDetails.getAppId()).write("AllusersList", eventUserList);
                                updateEventUsers();
                                userListAdapter = new UserListAdapter(EventUsersActivity.this, eventUserList, 0/*zero for event checkin*/);
                                recyclerView.setAdapter(userListAdapter);
                                updateView();
                                // spinner.setSelection(0);
                                if (isFromSwipe)
                                    refreshView();



                               /* userListAdapter.notifyDataSetChanged();*/
                            } else {
                                // unsuccessful request
                                System.out.println("Unsuccessful response");
                            }
                        } catch (Exception e) {

                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                swipeRefreshLayout.setRefreshing(false);

                // hide the progress dialog
                pDialog.dismiss();
                if (error instanceof TimeoutError) {
                    Error_Dialog.show("Timeout", EventUsersActivity.this);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, EventUsersActivity.this),
                            EventUsersActivity.this);
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show("Please Check Your Internet Connection",
                            EventUsersActivity.this);


                    eventUserList = Paper.book(appDetails.getAppId()).read("AllusersList", new ArrayList<EventUser>());
                    updateEventUsers();
                    userListAdapter = new UserListAdapter(EventUsersActivity.this, eventUserList, 0/*zero for event checkin*/);
                    recyclerView.setAdapter(userListAdapter);
                    updateView();
                    if (isFromSwipe)
                        refreshView();


                }

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("token", Paper.book().read("token", ""));
                return headers;
            }
        };

// Adding request to request queue
        jsonObjReq.setShouldCache(false);
        App.getInstance().addToRequestQueue(jsonObjReq, reqTag);
    }

    private void refreshView() {
        spinnerID = spinner.getSelectedItemPosition();
        int agenda_id = getAgendatID(agenda_set.get(spinnerID));
        userListAdapter = new UserListAdapter(EventUsersActivity.this, eventUserList, agenda_id);
        recyclerView.setAdapter(userListAdapter);
        spinner.setSelection(agenda_id);
        updateView();
    }

    private void sendCheckinData() {
        String reqTag = CHECK_IN_REQ_TAG;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("appId", appId);
            jsonObject.put("userIds", new JSONArray(Arrays.asList(checkedUsers.toArray())));
            jsonObject.put("checkin_date", new JSONArray(Arrays.asList(ckInTimeInMillisList.toArray())));
            jsonObject.put("email_id", new JSONArray(Arrays.asList(checkedEmails.toArray())));
            jsonObject.put("agenda_id", new JSONArray(Arrays.asList(agenda_id_array_list.toArray())));


        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjReq = VolleyUtils.createJsonPostReq(reqTag, checkInUrl, jsonObject,
                EventUsersActivity.this);
        // Adding request to request queue
        jsonObjReq.setShouldCache(false);
        App.getInstance().addToRequestQueue(jsonObjReq, reqTag);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (token == null) {
            startThread();
        } else {
            //sending offline data when internet is available
            if (!Paper.book(appDetails.getAppId()).read("offlineReg", "").equals("") &&
                    DataBaseStorage.isInternetConnectivity(EventUsersActivity.this)) {

                regesterAllOffline();

            } else if (Paper.book(appDetails.getAppId()).read("CheckedUsers", new ArrayList<String>()).size() > 0 &&
                    DataBaseStorage.isInternetConnectivity(EventUsersActivity.this)) {

                sendOfflineCheckinData();
            } else {
                //if offline checkin data is empty
                getUserDetails(usersUrl, true);
            }
        }
        setTitle(Util.applyFontToMenuItem(this, new SpannableString("Admin Panel")));


    }

    private void fileWritePermission(boolean isFromPrint) {
        //checking the file read/write permission is enabled or not
        if ((ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) ||
                (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {

            if ((ActivityCompat.shouldShowRequestPermissionRationale(EventUsersActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE))) {

            } else {
                ActivityCompat.requestPermissions(EventUsersActivity.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                        FILE_WRITE_PERMISSION);

            }

            //showing error message if not enabled
            if ((ActivityCompat.shouldShowRequestPermissionRationale(EventUsersActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE))) {
                String title = "Unable to Print";
                String msg = "Currently your storage permission is not enabled so unable to take the print. Checkin is Successfull.";
                if (isFromPrint)
                    createalertdialog(title, msg);
                else
                    createalertdialog(title, msg);

            } else {
                ActivityCompat.requestPermissions(EventUsersActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        FILE_WRITE_PERMISSION);

            }
        } else {
            file_write_permission = true;


        }
    }

    private void createalertdialog(String title, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyAlertDialogStyle);

        builder.setTitle(title);
        builder.setMessage(msg).setPositiveButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // Do nothing but close the dialog
                dialog.dismiss();
               /* Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivityForResult(intent, FILE_WRITE_PERMISSION);*/

            }
        });

       /* builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Do nothing
                dialog.dismiss();
            }
        });*/

        AlertDialog alert = builder.create();
        alert.show();

        TextView messageView = (TextView) alert.findViewById(android.R.id.message);
        messageView.setGravity(Gravity.CENTER_HORIZONTAL);
    }

    private void startThread() {

        // final String[] decryptedString = new String[1];
        Runnable r = new Runnable() {
            @Override
            public void run() {
                // your code here

                try {
                    FileInputStream fis = openFileInput(DataBaseStorage.F_I_L_ENCP2);
                    ObjectInputStream ois = new ObjectInputStream(fis);

                    byte[] decrypted = DataBaseStorage.decryptData((HashMap<String, byte[]>) ois.readObject(),
                            DataBaseStorage.token_pass);
                    if (decrypted != null) {
                        //decryptedString[0] = new String(decrypted);
                        token = new String(decrypted);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                //sending offline data when internet is available
                                if (!Paper.book(appDetails.getAppId()).read("offlineReg", "").equals("") &&
                                        DataBaseStorage.isInternetConnectivity(EventUsersActivity.this)) {

                                    regesterAllOffline();

                                } else if (Paper.book(appDetails.getAppId()).read("CheckedUsers", new ArrayList<String>()).size() > 0 && DataBaseStorage.isInternetConnectivity(EventUsersActivity.this)) {

                                    sendOfflineCheckinData();
                                } else {
                                    //if not offline data is present
                                    getUserDetails(usersUrl, false);
                                }

                            }
                        });

                    }
                    ois.close();

                } catch (Exception e) {

                    e.printStackTrace();
                }


              /*  handler.post(new Runnable()  //If you want to update the UI, queue the code on the UI thread
                {
                    public void run() {
                        //Code to update the UI
                        try {

                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    }
                });*/
            }
        };

        Thread t = new Thread(r);
        t.start();
    }

    private void sendOfflineCheckinData() {
        ArrayList<String> offlineCheckedUsersList = new ArrayList<>();
        Log.d(TAG, Paper.book(appId).read("CheckedUsers", new ArrayList<>()).toString());
        String reqTag = CHECK_IN_REQ_TAG;
        JSONObject jsonObject = new JSONObject();
        try {

            ArrayList<String> offlineCheckedEmails = new ArrayList<>();
            ArrayList<String> offlineTimeInMillsList = new ArrayList<>();
            ArrayList<Integer> offlineAgendIdList = new ArrayList<>();
            offlineTimeInMillsList.addAll(Paper.book(appDetails.getAppId()).read("ckInTimeInMillsList", new ArrayList<String>()));
            offlineCheckedUsersList = Paper.book(appDetails.getAppId()).read("CheckedUsers", new ArrayList<String>());
            offlineCheckedEmails = Paper.book(appDetails.getAppId()).read("CheckedEmails", new ArrayList<String>());
            offlineAgendIdList = Paper.book(appDetails.getAppId()).read("agenda_id_list", new ArrayList<Integer>());

            jsonObject.put("appId", appDetails.getAppId());
            jsonObject.put("userIds", new JSONArray(Arrays.asList(offlineCheckedUsersList.toArray())));
            jsonObject.put("email_id", new JSONArray(Arrays.asList(offlineCheckedEmails.toArray())));

            jsonObject.put("checkin_date", new JSONArray(Arrays.asList(offlineTimeInMillsList.toArray())));
            jsonObject.put("agenda_id", new JSONArray(Arrays.asList(offlineAgendIdList.toArray())));


        } catch (Exception e) {
            e.printStackTrace();
        }


        //new Json post request
        if (offlineCheckedUsersList.size() > 0) {
            final ProgressDialog pDialog = new ProgressDialog(this, R.style.MyAlertDialogStyle);
            pDialog.setMessage("Checking User...");
            pDialog.show();

            JsonObjectRequest jsonObjReq;

            jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    checkInUrl, jsonObject,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            // hide the progress dialog
                            pDialog.dismiss();
                            try {
                                System.out.println("Sending Offline Response : " + response.toString());
                                if (response.getString("response").equals("success")) {

                                    Toast.makeText(EventUsersActivity.this, Util.applyFontToMenuItem(EventUsersActivity.this,
                                            new SpannableString("Checkin data successful")), Toast.LENGTH_SHORT).show();
                                    Paper.book(appDetails.getAppId()).write("TempCheckedUsers", new ArrayList<String>());
                                    Paper.book(appDetails.getAppId()).write("TempCheckedEmails", new ArrayList<String>());
                                    Paper.book(appDetails.getAppId()).write("TempckInTimeInMillsList", new ArrayList<String>());
                                    Paper.book(appDetails.getAppId()).write("Tempagenda_id_list", new ArrayList<String>());

                                    Paper.book(appDetails.getAppId()).write("ckInTimeInMillsList", new ArrayList<String>());
                                    Paper.book(appDetails.getAppId()).write("CheckedUsers", new ArrayList<String>());
                                    Paper.book(appDetails.getAppId()).write("CheckedEmails", new ArrayList<String>());
                                    Paper.book(appDetails.getAppId()).write("agenda_id_list", new ArrayList<Integer>());
                                    getUserDetails(usersUrl, true);

                                } else if (response.getString("response").equals("error")) {
                                    if (response.getString("responseString").equals("Insufficient data.")) {

                                    } else {
                                        Toast.makeText(EventUsersActivity.this, "Backup was not successful", Toast.LENGTH_SHORT).show();

                                    }
                                }
                            } catch (Exception e) {

                            }
                        }

                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {

                    if (swipeRefreshLayout.isRefreshing()) {
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    // hide the progress dialog
                    pDialog.dismiss();
                    if ("com.android.volley.TimeoutError".equals(error.toString()))
                        Toast.makeText(EventUsersActivity.this, "Timeout Error", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(EventUsersActivity.this, "You are Offline", Toast.LENGTH_SHORT).show();

                }
            });
            jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                    DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            jsonObjReq.setShouldCache(false);
            App.getInstance().addToRequestQueue(jsonObjReq, reqTag);

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Paper.book(appDetails.getAppId()).write("CheckedUsers", checkedUsers);
        Paper.book(appDetails.getAppId()).write("ckInTimeInMillsList", ckInTimeInMillisList);
        Paper.book(appDetails.getAppId()).write("CheckedEmails", checkedEmails);
        Paper.book(appDetails.getAppId()).write("agenda_id_list", agenda_id_array_list);

        if (!DataBaseStorage.isInternetConnectivity(this)) {

            if (Paper.book(appDetails.getAppId()).read("TempCheckedUsers", new ArrayList<String>()).size() > 0) {
                tempcheckedUsers = Paper.book(appDetails.getAppId()).read("TempCheckedUsers", new ArrayList<String>());
                tempckInTimeInMillisList = Paper.book(appDetails.getAppId()).read("TempckInTimeInMillsList", new ArrayList<String>());
                tempcheckedEmails = Paper.book(appDetails.getAppId()).read("TempCheckedEmails", new ArrayList<String>());
                tempagenda_id_array_list = Paper.book(appDetails.getAppId()).read("Tempagenda_id_list", new ArrayList<String>());
                checkedUsers = new ArrayList<String>(tempcheckedUsers);
                ckInTimeInMillisList = new ArrayList<String>(tempckInTimeInMillisList);
                checkedEmails = new ArrayList<String>(tempcheckedEmails);
                agenda_id_array_list = new ArrayList<String>(tempagenda_id_array_list);
                Paper.book(appDetails.getAppId()).write("CheckedUsers", checkedUsers);
                Paper.book(appDetails.getAppId()).write("ckInTimeInMillsList", ckInTimeInMillisList);
                Paper.book(appDetails.getAppId()).write("CheckedEmails", checkedEmails);
                Paper.book(appDetails.getAppId()).write("agenda_id_list", agenda_id_array_list);
            }
            for (EventUser user : eventUserList) {
                if (tempcheckedUsers.contains(user.getUserid())) {
                    user.setCheckin_status("checkedin");
                    System.out.println(tempcheckedUsers.toString());
                }

            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:

                finish();
        }
        return true;

    }

    // update checkin status of users for event check-in
    private void updateEventUsers() {

        for (EventUser user : eventUserList) {

            if (checkedUsers.contains(user.getUserid())) {
                user.setCheckin_status("reached");
                tempcheckedUsers = new ArrayList<String>(checkedUsers);
                tempckInTimeInMillisList = new ArrayList<String>(ckInTimeInMillisList);
                tempcheckedEmails = new ArrayList<String>(checkedEmails);
                tempagenda_id_array_list = new ArrayList<String>(agenda_id_array_list);
                if (!DataBaseStorage.isInternetConnectivity(this))
                    tempeventUserList.add(user);
            }

        }
    }

    private void updateView() {
        tvCheckedInUser.setText(String.valueOf(getCheckedCount(eventUserList)));
        tvTotalUser.setText(String.valueOf(eventUserList.size()));
        userListAdapter.notifyDataSetChanged();
        if (eventUserList.size() > 0)
            Paper.book(appDetails.getAppId()).write("AllusersList", eventUserList);
          //  tv_no_attendee.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }/*else {
            tv_no_attendee.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }

        userListAdapter.notifyDataSetChanged();*/



    private int getCheckedCount(List<EventUser> userList) {
        int i = 0;
        for (EventUser user : userList) {
            if (agenda_id == 0) {
                if (user.getCheckin_status().equalsIgnoreCase("reached")) i++;

            } else {
                int[] agendas = user.getAgenda_id();
                for (int agenda_num = 0; agenda_num < user.getAgenda_id().length; agenda_num++) {


                    if (agendas[agenda_num] == agenda_id) {
                        i++;
                    }
                }
            }
        }

        return i;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SCANNER_REQUEST_CODE) {

            if (resultCode == RESULT_OK) {
                boolean duplicate = false;
                String id = data.getStringExtra("content");
                System.out.println("Content " + id);
                // Do something with the contact here (bigger example below)

                EventUser checkedUser = null;
                for (EventUser user : eventUserList) {

                    if (agenda_id == 0) {
                        if (user.getUserid().equals(id)) {
                            if (user.getCheckin_status().equalsIgnoreCase("reached"))
                                duplicate = true;
                            checkedUser = user;
                            break;
                        }
                    } else {
                        int[] agendas = user.getAgenda_id();
                        int flag = 0;
                        if (user.getUserid().equals(id)) {
                            for (int agenda_num = 0; agenda_num < user.getAgenda_id().length; agenda_num++) {

                                if (agendas[agenda_num] == agenda_id) {
                                    duplicate = true;
                                    flag = 1;
                                    break;
                                }
                            }

                            checkedUser = user;
                            if (flag == 1) {
                                break;
                            }
                        }
                    }
                }
                handleUser(checkedUser, duplicate);

            } else {
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == REGISTER_USER_REQUEST) {
            if (resultCode == RESULT_OK) {
                //wait i am thinking for it......
                Log.d(TAG, "HEHEHE");
            }
        } else if (requestCode == FILE_WRITE_PERMISSION) {
            if (resultCode == RESULT_OK)
                if (isPrintEnabled && agenda_id == 0)
                    try {
                        generatePDF(boolean_print_online);
                    } catch (DocumentException e1) {
                        e1.printStackTrace();
                    }
        }
    }

    private void dialogForHandleUserOnClick(final EventUser checkedUser, boolean duplicate) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyAlertDialogStyle);
        builder.setMessage("Do you want to Check-in the user ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        new CustomDialog(EventUsersActivity.this, checkedUser.getFirst_name(),
                                "User checked-in successful", true).show();
                        /*user_name = checkedUser.getFirst_name() + " " + checkedUser.getLast_name();
                        user_mail = checkedUser.getEmail();
                        checkin_UserID = checkedUser.getUserid();*/
                        addCheckedUser(checkedUser);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        builder.create();
        builder.show();
    }

    private void handleUserOnClickItem(EventUser checkedUser, boolean duplicate) {
        if (duplicate) {

            // new CustomDialog(this, checkedUser.getFirst_name(), "Duplicate user", false).show();
            // sendCheckinData();
            new CustomDialog(this, checkedUser.getFirst_name(), "User Checked-in successful", true).show();

            addCheckedUser(checkedUser);
        } else if (checkedUser != null) {
//            Toast.makeText(this, "Found User = " + checkedUser.getName() + ", with Id = " + checkedUser.getUserId(), Toast.LENGTH_LONG).show();
            new CustomDialog(this, checkedUser.getFirst_name(), "User Checked-in successful", true).show();
            addCheckedUser(checkedUser);
//            sendCheckinData();
        } else {
//            Toast.makeText(this, "User not found ", Toast.LENGTH_LONG).show();
            new CustomDialog(this, "Unknown User", "User not found", false).show();
        }
    }

    private void handleUser(EventUser checkedUser, boolean duplicate) {
        if (duplicate) {

            // new CustomDialog(this, checkedUser.getFirst_name(), "Duplicate user", false).show();
            // sendCheckinData();
            new CustomDialog(this, checkedUser.getFirst_name(), "User Checked-in successful", true).show();

            addCheckedUser(checkedUser);
        } else if (checkedUser != null) {
//            Toast.makeText(this, "Found User = " + checkedUser.getName() + ", with Id = " + checkedUser.getUserId(), Toast.LENGTH_LONG).show();
            new CustomDialog(this, checkedUser.getFirst_name(), "User Checked-in successful", true).show();
            addCheckedUser(checkedUser);
//            sendCheckinData();
        } else {
//            Toast.makeText(this, "User not found ", Toast.LENGTH_LONG).show();
            new CustomDialog(this, "Unknown User", "User not found", false).show();
        }
    }

    private void addCheckedUser(EventUser checkedUser) {
        user_name = checkedUser.getFirst_name() + " " + checkedUser.getLast_name();
        user_mail = checkedUser.getEmail();
        checkin_UserID = checkedUser.getUserid();
        if (agenda_id == 0) {
            //for event check-in
            checkedUser.setCheckin_status("reached");

        } else {
            //for agenda check-in


            Set<Integer> addcheckedagenda = new LinkedHashSet<>();

            for (int agendanum = 0; agendanum < checkedUser.getAgenda_id().length; agendanum++) {

                addcheckedagenda.add(agendanum);
            }
            addcheckedagenda.add(agenda_id);
            checkedUser.setAgenda_id(Ints.toArray(addcheckedagenda));
        }
        checkedUsers.add(checkedUser.getUserid());
        if (checkedUsers.size() > 0) {
            tempcheckedUsers.add(checkedUser.getUserid());
            Paper.book(appDetails.getAppId()).write("TempCheckedUsers", tempcheckedUsers);

        }
        checkedEmails.add(checkedUser.getEmail());
        if (checkedEmails.size() > 0) {
            tempcheckedEmails.add(checkedUser.getEmail());
            Paper.book(appDetails.getAppId()).write("TempCheckedEmails", checkedEmails);
        }
        ckInTimeInMillisList.add(String.valueOf(System.currentTimeMillis()));
        if (ckInTimeInMillisList.size() > 0) {
            tempckInTimeInMillisList.add(String.valueOf(System.currentTimeMillis()));
            Paper.book(appDetails.getAppId()).write("TempckInTimeInMillsList", ckInTimeInMillisList);
        }
        agenda_id_array_list.add(String.valueOf(agenda_id));
        if (agenda_id_array_list.size() > 0) {
            tempagenda_id_array_list.add(String.valueOf(agenda_id));
            Paper.book(appDetails.getAppId()).write("Tempagenda_id_list", agenda_id_array_list);
        }
        Paper.book(appDetails.getAppId()).write("CheckedUsers", checkedUsers);
        Paper.book(appDetails.getAppId()).write("CheckedEmails", checkedEmails);
        Paper.book(appDetails.getAppId()).write("ckInTimeInMillsList", ckInTimeInMillisList);
        Paper.book(appDetails.getAppId()).write("agenda_id_list", agenda_id_array_list);

        //callling api to send checking report
        sendCheckinData();

        updateView();
    }


    private void removeCheckedUser(EventUser checkedUser) {
        checkedUser.setCheckin_status("checkedin");
        checkedUsers.remove(checkedUser.getUserid());
        checkedEmails.remove(checkedUser.getEmail());
        Toast.makeText(this, "User removed = " + checkedUser.getFirst_name() +
                ", with Id = " + checkedUser.getUserid(), Toast.LENGTH_LONG).show();

        updateView();
    }

    @Override
    public void OnItemClick(View view, EventUser user, int position) {


        boolean duplicate = false;
        String postId = user.getUserid();
        //String id = data.getStringExtra("content");
        System.out.println("Content " + postId);
        // Do something with the contact here (bigger example below)

        EventUser checkedUser = null;
        for (EventUser users : eventUserList) {
            if (agenda_id == 0) {
                if (users.getUserid().equals(postId)) {
                    if (users.getCheckin_status().equalsIgnoreCase("reached"))
                        duplicate = true;
                    checkedUser = user;
                    break;
                }
            } else {
                int[] agendas = user.getAgenda_id();
                int flag = 0;
                if (users.getUserid().equals(postId))
                    for (int agenda_num = 0; agenda_num < users.getAgenda_id().length; agenda_num++) {
                        if (agendas[agenda_num] == agenda_id) {
                            duplicate = true;

                            flag = 1;
                            break;
                        }
                    }
                checkedUser = user;
                break;
            }
        }
        handleUserOnClickItem(checkedUser, duplicate);

    }

    @Override
    public void OnItemLongClicked(View view, EventUser user, int position) {
        Log.d("OnClick", "Card Position" + position);
        if (user.getCheckin_status().equalsIgnoreCase("reached")) {
            new CustomDialog(this, user.getFirst_name(), "User Successfully Checked Out",
                    true).show();
            removeCheckedUser(user);
        } else {
            Toast.makeText(this, "Found User = " + user.getFirst_name() + ", with Id = " + user.getUserid(), Toast.LENGTH_LONG).show();
            new CustomDialog(this, user.getFirst_name(), "User Checked-in successful", true).show();
            addCheckedUser(user);
        }
    }

    public void notifyTone(boolean succ) {
        ToneGenerator toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 1500);
        if (succ)
            toneGen1.startTone(ToneGenerator.TONE_PROP_BEEP2, 270);
        else
            toneGen1.startTone(ToneGenerator.TONE_CDMA_ABBR_ALERT, 400);
    }

    @Override
    public void onVolleyResponse(String tag, JSONObject response) throws JSONException {
        if (tag.equals(CHECK_IN_REQ_TAG)) {
            String responseString = response.getString("responseString");
            if (response.getString("response").equals("success")) {
                boolean_print_online = true;
                Toast.makeText(this, Util.applyFontToMenuItem(this,
                        new SpannableString("Checkin data backup successful")), Toast.LENGTH_SHORT).show();
                getUserDetails(usersUrl, true);
                //checking file write permission is enabled
                if (file_write_permission) {
                    //checking print is enabled from print settings and checking whether it is a event checkin not a session checkin
                    if (isPrintEnabled && agenda_id == 0)
                        try {
                            //generating pdf file
                            generatePDF(boolean_print_online);
                        } catch (DocumentException e1) {
                            e1.printStackTrace();
                        }
                } else
                    fileWritePermission(true);

            } else if (response.getString("response").equals("error")) {
                if (response.getString("responseString").equals("Insufficient data.")) {
                    Log.i(TAG, "onVolleyResponse: Insufficient data.");
                } else {
                    Toast.makeText(this, "Backup was not successful", Toast.LENGTH_SHORT).show();
                    System.out.println(responseString);
                }
            }
        }
    }



    @Override
    public void onVolleyError(String tag, VolleyError error) {
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
        boolean_print_online = false;
        getUserDetails(usersUrl, true);
        if (file_write_permission) {
            if (isPrintEnabled && agenda_id == 0)
                try {
                    generatePDF(boolean_print_online);
                } catch (DocumentException e1) {
                    e1.printStackTrace();
                }
        } else
            fileWritePermission(true);
        VolleyLog.d(TAG, "Error: " + error);
        if ("com.android.volley.TimeoutError".equals(error.toString()))
            Toast.makeText(this, "Timeout Error", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, "You are Offline", Toast.LENGTH_SHORT).show();
    }

    private void generatePDF(boolean isOnline) throws DocumentException {
        //path to store pdf file
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/PDF";

        File dir = new File(path);
        if (!dir.exists())
            dir.mkdirs();

        Log.d("PDFCreator", "PDF Path: " + path);

        //creating pdf file
        File file = new File(dir, "demo.pdf");
        String file_path = file.getPath();
        FileOutputStream fOut = null;
        try {
            //creating file
            fOut = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error Creating PDF", Toast.LENGTH_SHORT).show();

        }
        //creating the document
        Document document = new Document();
        // Location to save
        try {
            PdfWriter.getInstance(document, fOut);
        } catch (DocumentException e1) {
            e1.printStackTrace();
        }

        // Open to write
        document.open();

        // Document Settings
        document.setPageSize(PageSize.A4);
        document.addCreationDate();
        document.addAuthor("Webmobi");
        document.addCreator("Webmobi");


        BaseColor mColorAccent = new BaseColor(0, 153, 204, 255);
        float mHeadingFontSize = 18.0f;
        float mValueFontSize = 12.0f;

        //setting the font
        BaseFont urName = null;

        try {
            urName = BaseFont.createFont("assets/fonts/Roboto-Regular.otf", "UTF-8", BaseFont.EMBEDDED);
        } catch (DocumentException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        Font nameFont = new Font(urName, mHeadingFontSize, Font.NORMAL, BaseColor.BLACK);
        //adding the user name
        Chunk nameChunk = new Chunk(user_name, nameFont);
        Paragraph nameParagraph = new Paragraph(nameChunk);
        // Setting Alignment for Heading
        nameParagraph.setAlignment(Element.ALIGN_CENTER);
        // Finally Adding that Chunk
        try {
            document.add(new Paragraph("\n\n\n\n"));

            document.add(nameParagraph);
        } catch (DocumentException e1) {
            e1.printStackTrace();
        }



        Font mailFont = new Font(urName, mValueFontSize, Font.NORMAL, BaseColor.BLACK);
        //adding mail
        Chunk mailChunk = new Chunk(user_mail, mailFont);
        Paragraph mailParagraph = new Paragraph(mailChunk);
        mailParagraph.setAlignment(Element.ALIGN_CENTER);
        try {
            document.add(mailParagraph);
        } catch (DocumentException e1) {
            e1.printStackTrace();
        }
        //checking online/offline checkin
        if (isOnline) {
            //creating qr code bitmap
            Bitmap bmp = generateQRCode();
            Image image = null;

            try {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                image = Image.getInstance(stream.toByteArray());
                image.setAlignment(Image.MIDDLE);
                image.scaleAbsolute(80, 80);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            //adding qrcode image to document
            document.add(image);
        }

        //closing the document
        document.close();
        //calling the print method
        printPDF(file_path);

    }

    private Bitmap generateQRCode() {
        //generating qr code
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        BitMatrix bitMatrix = null;
        try {
            //setting qr code image height and width
            bitMatrix = multiFormatWriter.encode(checkin_UserID, BarcodeFormat.QR_CODE, 150, 150);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
        //convertin to bitmap
        Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);

        return bitmap;
    }

    private void printPDF(String path) {
        //sending intent to the printshare app
        String uri_path = "file://" + path;
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setPackage("com.dynamixsoftware.printershare");
        //sending the pdf path to print
        i.setDataAndType(Uri.parse(uri_path), "application/pdf");
        try {
            startActivity(i);
        } catch (ActivityNotFoundException e) {
            //if printshare app is not available error dialog will show
            createalertdialog("Print Module Not Available", "No print module found to complete the print job. Contact support@webmobi.com for more information.");

        }
    }

    public void scanUser(View view) {

        checkCameraPermission();

    }

    //checking camera
    private void checkCameraPermission() {

        if (android.os.Build.VERSION.SDK_INT >= 23) {
            // only for marsemellow and newer versions
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA}, ZBAR_CAMERA_PERMISSION);
            } else {

                //Do something
                Intent intent = new Intent(EventUsersActivity.this, SimpleScannerActivity.class);
                startActivityForResult(intent, SCANNER_REQUEST_CODE);
            }
        } else {

            //Do something
            Intent intent = new Intent(EventUsersActivity.this, SimpleScannerActivity.class);
            startActivityForResult(intent, SCANNER_REQUEST_CODE);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case ZBAR_CAMERA_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    //Do something
                    Intent intent = new Intent(EventUsersActivity.this, SimpleScannerActivity.class);
                    startActivityForResult(intent, SCANNER_REQUEST_CODE);

                } else {
                    Toast.makeText(this, "Please grant camera permission to use camera", Toast.LENGTH_SHORT).show();

                }
                break;
            case FILE_WRITE_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    file_write_permission = true;


                } else {
                    Toast.makeText(getApplicationContext(), "Please allow the permission", Toast.LENGTH_LONG).show();

                }
                break;

        }
    }

    public class CustomDialog extends Dialog {
        public CustomDialog(Context context, String name, String status, boolean success) {
            super(context);
            notifyTone(success);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.
                            LAYOUT_INFLATER_SERVICE);

            View layout = inflater.inflate(R.layout.dialog_checkin_success, null);
            TextView name_tv = (TextView) layout.findViewById(R.id.tv_username);
            TextView status_tv = (TextView) layout.findViewById(R.id.tv_status);
            ImageView status_img = (ImageView) layout.findViewById(R.id.iv_status_icon);
            name_tv.setText(name);
            setContentView(layout);

            if (success) {
                status_tv.setBackgroundColor(Color.parseColor("#70BE00"));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    status_img.setImageDrawable(getResources().getDrawable(R.drawable.checkright, null));
                else
                    status_img.setImageDrawable(getResources().getDrawable(R.drawable.checkright));
            } else {
                status_tv.setBackgroundColor(Color.RED);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    status_img.setImageDrawable(getResources().getDrawable(R.drawable.checkerror, null));
                else
                    status_img.setImageDrawable(getResources().getDrawable(R.drawable.checkerror));
            }
            status_tv.setText(status);

            setCanceledOnTouchOutside(true);
            setCancelable(true);
            Window window = getWindow();
            window.setGravity(Gravity.CENTER);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            new Handler().postDelayed(new Runnable() {

                public void run() {
                    dismiss();
                }
            }, 2500);
        }
    }


    private void regesterAllOffline() {

        final ProgressDialog dialog = new ProgressDialog(EventUsersActivity.this,
                R.style.MyAlertDialogStyle);
        dialog.setMessage("Registering...");
        dialog.show();
        String tag_string_req = "register_user_event";
        String url = ApiList.Register_user_event;
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                try {

                    JSONObject jObj = new JSONObject(response);


                    if (jObj.getBoolean("response")) {
                        Error_Dialog.show(jObj.getString("responseString"),
                                EventUsersActivity.this);
                        Paper.book(appDetails.getAppId()).write("offlineReg", "");
                        if (DataBaseStorage.isInternetConnectivity(EventUsersActivity.this)) {
                            int arrSize = Paper.book(appDetails.getAppId()).read("CheckedUsers", new ArrayList<String>()).size();
                            if (arrSize > 0)
                                sendOfflineCheckinData();
                            else
                                getUserDetails(usersUrl, false);
                        }

                    } else {
                        Error_Dialog.show(jObj.getString("responseString"),
                                EventUsersActivity.this);
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
                    Error_Dialog.show("Timeout", EventUsersActivity.this);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, EventUsersActivity.this),
                            EventUsersActivity.this);
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show("Please Check Internet Connection, You have registered offline.",
                            EventUsersActivity.this);

                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("createdBy", Paper.book().read("userId", ""));
                params.put("checkMail", "1");
                params.put("regUsers", Paper.book(appDetails.getAppId()).read("offlineReg", ""));
                params.put("appId", appDetails.getAppId());

                return params;
            }

        };


        strReq.setShouldCache(false);
        queue.add(strReq);

    }
}
