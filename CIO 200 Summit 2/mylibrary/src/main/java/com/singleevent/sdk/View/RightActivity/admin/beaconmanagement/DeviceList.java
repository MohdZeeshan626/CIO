package com.singleevent.sdk.View.RightActivity.admin.beaconmanagement;

import android.graphics.Color;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.minew.beaconplus.sdk.MTCentralManager;
import com.minew.beaconplus.sdk.MTFrameHandler;
import com.minew.beaconplus.sdk.MTPeripheral;
import com.minew.beaconplus.sdk.enums.FrameType;
import com.minew.beaconplus.sdk.frames.IBeaconFrame;
import com.minew.beaconplus.sdk.frames.MinewFrame;
import com.minew.beaconplus.sdk.frames.UidFrame;
import com.minew.beaconplus.sdk.interfaces.MTCentralManagerListener;
import com.singleevent.sdk.Custom_View.Util;
import com.singleevent.sdk.model.AppDetails;
import com.singleevent.sdk.R;
import com.singleevent.sdk.View.RightActivity.admin.beaconmanagement.adapter.DeviceRVAdapter;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class DeviceList extends AppCompatActivity {
    public static final String USER_NAME = "user_name";
    Toolbar toolbar;
    AppDetails appDetails;
    String token, theme_color;
    MTCentralManager mtCentralManager = null;
    ArrayList<MTPeripheral> scanned_device_list = new ArrayList<>();
    ArrayList<MTPeripheral> old_device = new ArrayList<>();
    ArrayList<MTPeripheral> new_device = new ArrayList<>();
    ArrayList<MTPeripheral> device_list = new ArrayList<>();
    DeviceRVAdapter deviceListAdapter;
    RecyclerView lv;
    EditText search;
    GifImageView gifImageView;
    ImageView refresh_img;
    TextView refresh_btn;
    Button btn;
    RelativeLayout refresh_layout, refresh_anim_layout, refresh_static_layout;
    String user_name;
    List<String> assigned_beacon_list;
    LinearLayout wait_msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);

        appDetails = Paper.book().read("Appdetails");
        theme_color = appDetails.getTheme_color();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(Color.parseColor(theme_color));

        setSupportActionBar(toolbar);

        Bundle extras = getIntent().getExtras();
        if (extras == null)
            finish();

        assigned_beacon_list = Paper.book().read("assigned_beacon");

        user_name = extras.getString(USER_NAME);
        mtCentralManager = MTCentralManager.getInstance(this);

        //setting the resources
        setResources();

        deviceListAdapter = new DeviceRVAdapter(this, R.layout.list_device_row, scanned_device_list, user_name, assigned_beacon_list);
        lv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,
                false));

        gifImageView.setGifImageResource(R.drawable.loader_pg);

        //search text
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filterUser(editable.toString());
            }
        });


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanFunc();
            }
        });
        refresh_static_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanFunc();
            }
        });

    }

    private void setResources() {
        lv = (RecyclerView) findViewById(R.id.device_list);
        search = (EditText) findViewById(R.id.search);
        btn = (Button) findViewById(R.id.search_btn);
        refresh_btn = (TextView) findViewById(R.id.refresh_btn);
        refresh_img = (ImageView) findViewById(R.id.refresh_static_img);
        refresh_layout = (RelativeLayout) findViewById(R.id.refresh_layout);
        gifImageView = (GifImageView) findViewById(R.id.refresh_img);
        refresh_anim_layout = (RelativeLayout) findViewById(R.id.refresh_animated_layout);
        refresh_static_layout = (RelativeLayout) findViewById(R.id.refresh_static_layout);
        wait_msg = (LinearLayout) findViewById(R.id.wait_msg);
    }

    //filtering the beacon
    private void filterUser(String s) {
        List<MTPeripheral> temp = new ArrayList();
        for (MTPeripheral d : scanned_device_list) {
            if (d.mMTFrameHandler.getMac().toLowerCase().contains(s.toLowerCase())) {
                temp.add(d);
            }
        }
        deviceListAdapter.updateList(temp);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //starting the scan
        scanFunc();
        SpannableString s = new SpannableString("Beacon Management");
        setTitle(Util.applyFontToMenuItem(this, s));


    }

    private void scanFunc() {
        mtCentralManager = MTCentralManager.getInstance(this);

        mtCentralManager.startScan();
        setRefreshAnimation(true);
        search.setText("");

        scanned_device_list.clear();
        old_device.clear();
        new_device.clear();
        device_list.clear();
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
                    //setting the range to -80
                    if (rssi >= -80)
                        if (!scanned_device_list.contains(mtPeripheral))
                            scanned_device_list.add(mtPeripheral);
                    ArrayList<MinewFrame> advFrames = mtFrameHandler.getAdvFrames();
                    for (MinewFrame minewFrame : advFrames) {
                        FrameType frameType = minewFrame.getFrameType();
                        switch (frameType) {
                            case FrameiBeacon:
                                IBeaconFrame iBeaconFrame = (IBeaconFrame) minewFrame;

                                System.out.println("BLE Bluetooth Device FrameiBeacon : " + iBeaconFrame.getUuid() + " Major : " + iBeaconFrame.getMajor() + " Major : " + iBeaconFrame.getMinor());
                                break;
                            case FrameUID:
                                UidFrame uidFrame = (UidFrame) minewFrame;
                                System.out.println("BLE Bluetooth Device FrameUID : " + uidFrame.getNamespaceId() + uidFrame.getInstanceId());
                                break;

                        }
                    }

                }
                System.out.println("BLE List Size : " + scanned_device_list.size());

            }

        });

        Handler scanHandler = new Handler();
        scanHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (mtCentralManager != null)
                    mtCentralManager.stopScan();
                setRefreshAnimation(false);
                setLists();
            }
        }, 3000);


    }

    private void setLists() {
        //setting the list
        if (assigned_beacon_list.size() > 0 && scanned_device_list.size() > 0) {
            for (int i = 0; i < scanned_device_list.size(); i++) {
                for (int j = 0; j < assigned_beacon_list.size(); j++) {
                    //check whether the scanned device is already assigned to the user
                    //if assigned add it to the old_device list
                    if (scanned_device_list.get(i).mMTFrameHandler.getMac().equalsIgnoreCase(assigned_beacon_list.get(j).toString())) {
                        old_device.add(scanned_device_list.get(i));
                        break;
                    }

                }

            }
            for (int k = 0; k < scanned_device_list.size(); k++) {
                //if old device not contains the assigned device adding to the new_device list
                if (!(old_device.contains(scanned_device_list.get(k)))) {
                    new_device.add(scanned_device_list.get(k));
                }
            }
            //to show the un assigned device in the top of the list
            //first adding the newer device to the list
            if (new_device.size() > 0)
                for (int a = 0; a < new_device.size(); a++) {
                    device_list.add(new_device.get(a));
                }

            //then adding the older device to the list
            if (old_device.size() > 0) {
                for (int b = 0; b < old_device.size(); b++) {
                    device_list.add(old_device.get(b));
                }
            }

            deviceListAdapter = new DeviceRVAdapter(DeviceList.this, R.layout.list_device_row, device_list, user_name, assigned_beacon_list);
            lv.setAdapter(deviceListAdapter);
        } else {
            deviceListAdapter = new DeviceRVAdapter(DeviceList.this, R.layout.list_device_row, scanned_device_list, user_name, assigned_beacon_list);
            lv.setAdapter(deviceListAdapter);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);

    }

    private void setRefreshAnimation(boolean b) {

        if (b) {
            refresh_static_layout.setVisibility(View.GONE);
            refresh_anim_layout.setVisibility(View.VISIBLE);
            wait_msg.setVisibility(View.VISIBLE);
            lv.setVisibility(View.GONE);


        } else {
            refresh_static_layout.setVisibility(View.VISIBLE);
            refresh_anim_layout.setVisibility(View.GONE);
            wait_msg.setVisibility(View.GONE);
            lv.setVisibility(View.VISIBLE);


        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mtCentralManager != null)
            mtCentralManager.stopScan();

    }
}
