package com.singleevent.sdk.View.RightActivity.admin.checkin.beacon;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Handler;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.SpannableString;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.common.primitives.Ints;
import com.minew.beaconplus.sdk.MTCentralManager;
import com.minew.beaconplus.sdk.MTFrameHandler;
import com.minew.beaconplus.sdk.MTPeripheral;
import com.minew.beaconplus.sdk.enums.FrameType;
import com.minew.beaconplus.sdk.frames.IBeaconFrame;
import com.minew.beaconplus.sdk.frames.MinewFrame;
import com.minew.beaconplus.sdk.frames.UidFrame;
import com.minew.beaconplus.sdk.interfaces.MTCentralManagerListener;
import com.singleevent.sdk.App;
import com.singleevent.sdk.Custom_View.Util;
import com.singleevent.sdk.model.AppDetails;
import com.singleevent.sdk.model.Events;
import com.singleevent.sdk.R;
import com.singleevent.sdk.View.RightActivity.admin.beaconmanagement.GifImageView;
import com.singleevent.sdk.View.RightActivity.admin.checkin.VolleyResponseListener;
import com.singleevent.sdk.View.RightActivity.admin.checkin.VolleyUtils;
import com.singleevent.sdk.View.RightActivity.admin.model.EventUser;
import com.singleevent.sdk.View.RightActivity.admin.utils.Urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.paperdb.Paper;

public class BeaconCheckin extends AppCompatActivity implements BeaconCheckinAdapter.OnItemUserClick, VolleyResponseListener {
    MTCentralManager mtCentralManager = null;
    ArrayList<MTPeripheral> scanned_device_list = new ArrayList<>();
    RelativeLayout refresh_layout, refresh_anim_layout, refresh_static_layout;
    GifImageView gifImageView;
    Toolbar toolbar;
    AppDetails appDetails;
    String token, theme_color, appId;
    LinearLayout wait_msg;
    RecyclerView user_list;
    Spinner spinner;
    ArrayList<String> spinner_text;
    int spinnerID, agenda_id;
    private ArrayList<Events> events = new ArrayList<Events>();
    Events e;
    int pos = 0;
    HashMap<Integer, String> agenda_set;
    BeaconCheckinAdapter beaconCheckinAdapter;
    public static String USER_LISTS = "user_lists";
    public static String AGENDA_ID = "agenda_id";
    private ArrayList<EventUser> eventUserList = new ArrayList<>();
    private ArrayList<EventUser> tempeventUserList = new ArrayList<>();
    int spinner_item;
    public static final String CHECK_IN_REQ_TAG = "check_in_users";
    private static final String TAG = "BeaconCheckin";

    ArrayList<String> checkedUsers = new ArrayList<>();
    ArrayList<String> tempcheckedUsers = new ArrayList<>();
    ArrayList<String> checkedEmails = new ArrayList<>();
    ArrayList<String> tempcheckedEmails = new ArrayList<>();
    private ArrayList<String> ckInTimeInMillisList = new ArrayList<>();
    private ArrayList<String> tempckInTimeInMillisList = new ArrayList<>();
    private ArrayList<String> agenda_id_array_list = new ArrayList<>();
    private ArrayList<String> tempagenda_id_array_list = new ArrayList<>();
    String user_name, user_mail, checkin_UserID;
    String checkInUrl = Urls.getCheckInUrl();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Paper.init(this);
        setContentView(R.layout.activity_beacon_checkin);
        appDetails = Paper.book().read("Appdetails");
        theme_color = appDetails.getTheme_color();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(Color.parseColor(theme_color));

        setSupportActionBar(toolbar);

        Bundle extras = getIntent().getExtras();
        if (extras == null)
            finish();

        eventUserList = extras.getParcelableArrayList(USER_LISTS);
        spinner_item = extras.getInt(AGENDA_ID);


        appId = appDetails.getAppId();
        setResources();
//        scanFunc();


    }

    private void setResources() {
        //setting resources
        refresh_layout = (RelativeLayout) findViewById(R.id.bc_refresh_layout);
        gifImageView = (GifImageView) findViewById(R.id.bc_refresh_img);
        refresh_anim_layout = (RelativeLayout) findViewById(R.id.bc_refresh_animated_layout);
        refresh_static_layout = (RelativeLayout) findViewById(R.id.bc_refresh_static_layout);
        wait_msg = (LinearLayout) findViewById(R.id.bc_wait_msg);
        user_list = (RecyclerView) findViewById(R.id.bc_user_list);
        spinner = (Spinner) findViewById(R.id.bc_section_list_spinner);
        gifImageView.setGifImageResource(R.drawable.loader_pg);

        spinner_text = new ArrayList<>();
        agenda_set = new HashMap<Integer, String>();
        user_list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,
                false));

        //setting the spinner values
        setSpinnerValues();


        spinner.setSelection(spinner_item);
        refresh_static_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanFunc();
            }
        });


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spinnerID = i;
                //getting agenda id

                agenda_id = getAgendatID(agenda_set.get(spinnerID));
//                this.agendaId = agenda_id;

                setListItems();

//                updateView();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void setListItems() {
        //setting list items
        spinnerID = spinner.getSelectedItemPosition();
        agenda_id = getAgendatID(agenda_set.get(spinnerID));
        //setting the list adapter
        beaconCheckinAdapter = new BeaconCheckinAdapter(BeaconCheckin.this, tempeventUserList, agenda_id, this);
        user_list.setAdapter(beaconCheckinAdapter);
        beaconCheckinAdapter.notifyDataSetChanged();
    }

    private int getAgendatID(String s) {
        //getting agenda id
        int agenda_id = 0;
        for (Map.Entry<Integer, String> entry : agenda_set.entrySet()) {
            String value = entry.getValue();
            Integer key = entry.getKey();
            // do something with key and/or tab
            if (value.equalsIgnoreCase(s)) {
                agenda_id = key;
                break;
            }

        }
        return agenda_id;
    }

    private void setSpinnerValues() {
        //setting the spinner values
        events = Paper.book().read("Appevents");
        e = events.get(0);
        for (int i = 0; i < e.getTabsSize(); i++) {
            if (e.getTabs(i).getCheckvalue().equalsIgnoreCase("agenda0")) {
                pos = i;
                break;
            }
        }

        //default value for event checkin
        agenda_set.put(0, "Event Checkin");
        spinner_text.add("Event Checkin");
        for (int i = 0; i < e.getTabs(pos).getAgendaSize(); i++) {
            String day_str = "Day " + (i + 1) + " ";
            for (int j = 0; j < e.getTabs(pos).getAgenda(i).getDetailSize(); j++) {
                String temp_str = day_str + e.getTabs(pos).getAgenda(i).getDetail(j).getTopic();
                int agenda_id = e.getTabs(pos).getAgenda(i).getDetail(j).getAgendaId();
                agenda_set.put(agenda_id, temp_str);
                spinner_text.add(temp_str);
            }
        }

        // Initializing an ArrayAdapter
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this, R.layout.spinner_item, spinner_text
        );
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        //setting spinner adapter
        spinner.setAdapter(spinnerArrayAdapter);
    }

    private void scanFunc() {
        //scan beacon
        mtCentralManager = MTCentralManager.getInstance(this);
        setRefreshAnimation(true);
        //starting the scan method
        mtCentralManager.startScan();


        mtCentralManager.setMTCentralManagerListener(new MTCentralManagerListener() {
            @Override
            public void onScanedPeripheral(final List<MTPeripheral> peripherals) {
                for (MTPeripheral mtPeripheral : peripherals) {

                    // get FrameHandler of a device.
                    MTFrameHandler mtFrameHandler = mtPeripheral.mMTFrameHandler;
                    String mac = mtFrameHandler.getMac();        //mac address of device
                    String name = mtFrameHandler.getName();        // name of device
                    int battery = mtFrameHandler.getBattery();    //battery
                    int rssi = mtFrameHandler.getRssi();//rssi
                    int last_update = mtFrameHandler.getScanInterval();
                    //checking the range
                    if (rssi >= -70)
                        if (!scanned_device_list.contains(mtPeripheral)) {
                            //adding the scanned devices
                            scanned_device_list.add(mtPeripheral);
                            for (int i = 0; i < eventUserList.size(); i++) {
                                //getting the mac from the device
                                String user_mac = eventUserList.get(i).getBeacon_id();
                                //comparing the mac with user mac
                                //if matching adding the user
                                if (user_mac.equalsIgnoreCase(mac))
                                    tempeventUserList.add(eventUserList.get(i));
                            }
                        }


                    ArrayList<MinewFrame> advFrames = mtFrameHandler.getAdvFrames();
                    for (MinewFrame minewFrame : advFrames) {
                        FrameType frameType = minewFrame.getFrameType();
                        switch (frameType) {
                            case FrameiBeacon:
                                IBeaconFrame iBeaconFrame = (IBeaconFrame) minewFrame;

//                                System.out.println("Checkin BLE Bluetooth Device FrameiBeacon : " + iBeaconFrame.getUuid() + " Major : " + iBeaconFrame.getMajor() + " Major : " + iBeaconFrame.getMinor());
                                break;
                            case FrameUID:
                                UidFrame uidFrame = (UidFrame) minewFrame;
//                                System.out.println("Checkin BLE Bluetooth Device FrameUID : " + uidFrame.getNamespaceId() + uidFrame.getInstanceId());
                                break;

                        }
                    }

                }
               /* deviceListAdapter = new DeviceRVAdapter(DeviceList.this, R.layout.list_device_row, scanned_device_list, user_name);
                lv.setAdapter(deviceListAdapter);*/
            }

        });

        Handler scanHandler = new Handler();
        scanHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
//                scanFunc();
                //stopping the scan after 5 sec
                if (mtCentralManager != null)
                    mtCentralManager.stopScan();
                //setting the scan animation
                setRefreshAnimation(false);

                if (tempeventUserList.size() > 0)
                    setListItems();
                else
                    alertDialog();
            }
        }, 5000);


    }

    private void alertDialog() {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(this, R.style.MyAlertDialogStyle);
        //if no device is found alert dialog will popup
        builder.setMessage("No devices found. Do you want to retry?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //if press ok scan will call again
                        scanFunc();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                       //if press no/cancel actviity will close
                        finish();
                    }
                })

                .show();
    }

    private void setRefreshAnimation(boolean b) {
        //setting the animation for scanning
        if (b) {
            refresh_static_layout.setVisibility(View.GONE);
//            gifImageView.setGifImageResource(R.drawable.loader);
            refresh_anim_layout.setVisibility(View.VISIBLE);
            wait_msg.setVisibility(View.VISIBLE);
            user_list.setVisibility(View.GONE);

        } else {
            refresh_static_layout.setVisibility(View.VISIBLE);
            refresh_anim_layout.setVisibility(View.GONE);
            wait_msg.setVisibility(View.GONE);
            user_list.setVisibility(View.VISIBLE);
//            setListItems();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //starting the scan
        scanFunc();
        //setting title
        SpannableString s = new SpannableString("Beacon Management");
        setTitle(Util.applyFontToMenuItem(this, s));
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mtCentralManager != null)
            mtCentralManager.stopScan();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onUserItemClick(EventUser eventUser) {
        confirmDialog(eventUser);
    }

    private void confirmDialog(final EventUser eventUser) {
        //show popup alert with user name
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(this, R.style.MyAlertDialogStyle);

        builder.setMessage("Are you sure you want to Check-in " + eventUser.getFirst_name() + " " + eventUser.getLast_name() + " ?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        addCheckedUser(eventUser);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })

                .show();
    }

    private void sendCheckinData() {
        //adding user details params for checkin
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
                BeaconCheckin.this);
        // Adding request to request queue
        jsonObjReq.setShouldCache(false);
        App.getInstance().addToRequestQueue(jsonObjReq, reqTag);
    }


    private void addCheckedUser(EventUser checkedUser) {
        user_name = checkedUser.getFirst_name() + " " + checkedUser.getLast_name();
        user_mail = checkedUser.getEmail();
        checkin_UserID = checkedUser.getUserid();
        if (agenda_id == 0) {
            //for event check-in
            checkedUser.setCheckin_status("reached");

        } else {
            //for session check-in
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

//        updateView();
    }

    @Override
    public void onVolleyResponse(String tag, JSONObject response) throws JSONException {
        if (tag.equals(CHECK_IN_REQ_TAG)) {
            String responseString = response.getString("responseString");
            if (response.getString("response").equals("success")) {
                Toast.makeText(this, Util.applyFontToMenuItem(this,
                        new SpannableString("Checkin data backup successful")), Toast.LENGTH_SHORT).show();
                scanFunc();

            } else if (response.getString("response").equals("error")) {
                scanFunc();
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

    }
}
