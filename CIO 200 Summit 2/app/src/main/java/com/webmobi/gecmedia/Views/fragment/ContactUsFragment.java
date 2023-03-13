package com.webmobi.gecmedia.Views.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanFilter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.ParcelUuid;
import android.provider.MediaStore;

import androidx.annotation.Nullable;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.singleevent.sdk.ApiList;
import com.singleevent.sdk.App;
import com.singleevent.sdk.Custom_View.Error_Dialog;
import com.singleevent.sdk.Custom_View.Util;
import com.singleevent.sdk.model.AppDetails;
import com.singleevent.sdk.model.User_Details;
import com.singleevent.sdk.View.RightActivity.admin.checkin.SimpleScannerActivity;
import com.singleevent.sdk.service.UploadResultReceiver;
import com.singleevent.sdk.utils.DataBaseStorage;
import com.webmobi.gecmedia.CustomViews.VolleyErrorLis;
import com.webmobi.gecmedia.R;
import com.webmobi.gecmedia.SingleEventHome;
import com.webmobi.gecmedia.Views.ContactsProfileActivity;
import com.webmobi.gecmedia.Views.Profile;
import com.webmobi.gecmedia.Views.RegActivity;
import com.webmobi.gecmedia.Views.fragment.adapter.ContactsAdapter;
import com.webmobi.gecmedia.Views.fragment.helper.ClickListener;
import com.webmobi.gecmedia.Views.fragment.helper.RecyclerTouchListener;
import com.webmobi.gecmedia.Views.fragment.model.ContactsModel;

import androidx.appcompat.widget.Toolbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import io.paperdb.Paper;

import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getApplicationContext;

public class ContactUsFragment extends Fragment implements View.OnClickListener, UploadResultReceiver.Receiver {

    AppCompatActivity ac = new AppCompatActivity();
    private static final String TAG = ContactUsFragment.class.getSimpleName();
    Context context;
    private AppDetails appDetails;
    User_Details user_details;
    View view;
    private static final int ZBAR_CAMERA_PERMISSION = 1;
    private static final int ZBAR_CAMERA_PERMISSION1 = 444;
    public static final int SCANNER_REQUEST_CODE = 220;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    String regId, userid;
    float imgWidth, imgHight;
    private RelativeLayout view1, view2, view3;
    private Button camera_button, button_retake, button_send, camera_button2, ble_scan_btn, ble_scan_btn_1;
    private ImageView imageview;
    private UploadResultReceiver mReceiver;
    private EditText et_desc;
    private Toolbar toolbar11;
    String mCurrentPhotoPath, someFilepath, extension, fileExtensionName;
    Uri photoURI;
    JSONObject header;
    String requestdata;
    private EditText et_fname, et_lname;
    private RecyclerView recyclerView;
    private ContactsAdapter contactsAdapter;
    private ContactsModel contactsModel;
    boolean ff;
    String qrcode_userid, loggedin_userid;
    private ArrayList<ContactsModel> contactsArrayList;
    ImageView user_login;

    Map<String, String> ble_user_id = new HashMap<>();

    private BluetoothLeScanner mBluetoothLeScanner;
    private Handler mHandler = new Handler();
    private BluetoothAdapter myBluetoothAdapter;
    private static final int REQUEST_ENABLE_BT = 1;
    ProgressDialog searchpDialog;
    String exchange_id, other_user_id;
    String f_fname, appid, f_lname, f_desc, f_addrs, f_company, f_desig, f_website, f_email, f_email1, f_altphone, f_altweb, f_dtext, f_uemail, f_flag, f_itemflag, f_uid, f_phone, f_imageUrl;
    AppCompatActivity a;
    ConstraintLayout tool2;
    ProgressDialog progressDialog;


    EditText tv_company, tv_designation, tv_address, tv_phone, tv_email, tv_email1, tv_website, tv_fax, tv_alt_phone, tv_altEmail, tv_altWebsite;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        Paper.init(getActivity());
        regId = Paper.book().read("regId");
        try {
            appDetails = Paper.book().read("Appdetails");

            if (appDetails == null) {
                appid = "";
            } else
                appid = appDetails.getAppId();
        } catch (Exception e) {


        }
        userid = Paper.book().read("userId", "");
        contactsArrayList = new ArrayList<>();
        contactsAdapter = new ContactsAdapter(context, contactsArrayList);
        try {
            getContactsLists();
        } catch (Exception e) {

        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        if (view != null)
            return view;


        view = inflater.inflate(R.layout.fragment_contact, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        view1 = (RelativeLayout) view.findViewById(R.id.view1);
        view2 = (RelativeLayout) view.findViewById(R.id.view2);
        view3 = (RelativeLayout) view.findViewById(R.id.view3);

        ble_scan_btn = (Button) view.findViewById(R.id.ble_scan_button);
        ble_scan_btn_1 = (Button) view.findViewById(R.id.ble_scan_button_1);
        camera_button = (Button) view.findViewById(R.id.camera_button);
        camera_button2 = (Button) view.findViewById(R.id.camera_button2);
        button_retake = (Button) view.findViewById(R.id.button_retake);
        button_send = (Button) view.findViewById(R.id.button_send);
        toolbar11 = (Toolbar) view.findViewById(R.id.toolbar11);
        tool2 = (ConstraintLayout) view.findViewById(R.id.tool2);
        try {
            System.out.println("activit name is" + getActivity().getClass().getSimpleName());
            String aname = getActivity().getClass().getSimpleName();
            if (aname != null) {
                if (aname.equalsIgnoreCase("SingleEventHome")) {

                } else if (aname.equalsIgnoreCase("HomeScreenMulti")) {
                    toolbar11.setNavigationIcon(null);

                } else if (aname.equalsIgnoreCase("HomeScreen")) {
                    toolbar11.setNavigationIcon(null);
                }
            } else {
                toolbar11.setNavigationIcon(null);
            }

        } catch (Exception e) {

        }

        try {
            /*if(appDetails!=null) {
                ble_scan_btn.setBackground(Util.setdrawable(context, com.singleevent.sdk.R.drawable.healthpostbut,
                        Color.parseColor(appDetails.getTheme_color())));
                ble_scan_btn_1.setBackground(Util.setdrawable(context, com.singleevent.sdk.R.drawable.healthpostbut,
                        Color.parseColor(appDetails.getTheme_color())));
                camera_button.setBackground(Util.setdrawable(context, com.singleevent.sdk.R.drawable.healthpostbut,
                        Color.parseColor(appDetails.getTheme_color())));
                camera_button2.setBackground(Util.setdrawable(context, com.singleevent.sdk.R.drawable.healthpostbut,
                        Color.parseColor(appDetails.getTheme_color())));
                toolbar11.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));
                tool2.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));
            }else{*/
            ble_scan_btn.setBackground(Util.setdrawable(context, com.singleevent.sdk.R.drawable.healthpostbut,
                    getResources().getColor(R.color.licspa_theme)));
            ble_scan_btn_1.setBackground(Util.setdrawable(context, com.singleevent.sdk.R.drawable.healthpostbut,
                    getResources().getColor(R.color.licspa_theme)));
            camera_button.setBackground(Util.setdrawable(context, com.singleevent.sdk.R.drawable.healthpostbut,
                    getResources().getColor(R.color.licspa_theme)));
            camera_button2.setBackground(Util.setdrawable(context, com.singleevent.sdk.R.drawable.healthpostbut,
                    getResources().getColor(R.color.licspa_theme)));
            toolbar11.setBackgroundColor(getResources().getColor(R.color.licspa_theme));
            tool2.setBackgroundColor(getResources().getColor(R.color.licspa_theme));
            //}
        } catch (Exception e) {
        }

        et_fname = (EditText) view.findViewById(R.id.et_fname);
        et_lname = (EditText) view.findViewById(R.id.et_lname);
        et_desc = (EditText) view.findViewById(R.id.et_desc);
        //     user_login = (ImageView) view.findViewById(R.id.title_right_ic);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);


        toolbar11.setOnClickListener(this);
        // other
        tv_company = (EditText) view.findViewById(R.id.tv_company);
        tv_designation = (EditText) view.findViewById(R.id.tv_designation);
        tv_address = (EditText) view.findViewById(R.id.tv_address);

        tv_phone = (EditText) view.findViewById(R.id.tv_phone);
        tv_email = (EditText) view.findViewById(R.id.tv_email);
        tv_email1 = (EditText) view.findViewById(R.id.tv_altEmail);

        tv_website = (EditText) view.findViewById(R.id.tv_website);

        tv_fax = (EditText) view.findViewById(R.id.tv_fax);
        tv_alt_phone = (EditText) view.findViewById(R.id.tv_alt_phone);

        tv_altEmail = (EditText) view.findViewById(R.id.tv_altEmail);
        tv_altWebsite = (EditText) view.findViewById(R.id.tv_altWebsite);


        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(contactsAdapter);
        imageview = (ImageView) view.findViewById(R.id.attachment1);
        //For upper Layout


        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView,
                new ClickListener() {
                    @Override
                    public void onClick(View view, int position) {

                        contactsModel = contactsArrayList.get(position);

                        Log.v(TAG, "contactlist Position: " + position);
                        Intent intent = new Intent(getActivity(), ContactsProfileActivity.class);
                        Gson gS = new Gson();
                        String target = gS.toJson(contactsModel);
                        intent.putExtra("ContactsModel", target);
                        startActivity(intent);

                    }

                    @Override
                    public void onLongClick(View view, int position) {
                        Log.v(TAG, "Onlong click Position: " + position);
                    }
                }));


        //adding listener
        camera_button.setOnClickListener(this);
        button_send.setOnClickListener(this);
        button_retake.setOnClickListener(this);
        camera_button2.setOnClickListener(this);
        ble_scan_btn.setOnClickListener(this);
        ble_scan_btn_1.setOnClickListener(this);
        if (contactsArrayList.size() > 0) {

            view1.setVisibility(View.GONE);
            view2.setVisibility(View.GONE);
            view3.setVisibility(View.VISIBLE);
           /* if (checkAvertisement())
                ble_scan_btn.setVisibility(View.VISIBLE);
            else
                ble_scan_btn.setVisibility(View.GONE);*/
        } else {
            view1.setVisibility(View.VISIBLE);
            view2.setVisibility(View.GONE);
            view3.setVisibility(View.GONE);
           /* if (checkAvertisement())
                ble_scan_btn_1.setVisibility(View.VISIBLE);
            else
                ble_scan_btn_1.setVisibility(View.GONE);*/
        }

       /* user_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userLogin();
            }
        });*/

        return view;
    }

    private boolean checkAvertisement() {
        boolean result = false;
        //checking whether the multipleadvertisement is supported or not
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (!BluetoothAdapter.getDefaultAdapter().isMultipleAdvertisementSupported())
                result = false;
            else
                result = true;
        } else
            result = false;
        return result;

    }

    private void userLogin() {
        Intent i;

        if (Paper.book().read("Islogin", false)) {

            i = new Intent(context, Profile.class);
            i.setAction(com.webmobi.gecmedia.Config.ApiList.loginaction);
            startActivity(i);
        } else {
            i = new Intent(context, RegActivity.class);
            i.setAction(com.webmobi.gecmedia.Config.ApiList.loginaction);
            startActivityForResult(i, 40);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.camera_button:
                if (DataBaseStorage.isInternetConnectivity(getApplicationContext()))
                    checkCameraPermission();
                else
                    Error_Dialog.show("Please Check Your Internet Connection", getActivity());

                break;

            case R.id.camera_button2:
                checkCameraPermission();
                break;

            case R.id.button_send:
                sendImageToServer();
                getContactsLists();
                if (contactsArrayList.size() > 0) {

                    view1.setVisibility(View.GONE);
                    view2.setVisibility(View.GONE);
                    view3.setVisibility(View.VISIBLE);
                  /*  if (checkAvertisement())
                        ble_scan_btn.setVisibility(View.VISIBLE);
                    else
                        ble_scan_btn.setVisibility(View.GONE);*/
                    //after successfully uploaded scan business card again call for lists
                    contactsArrayList.clear();

                } else {
                    view1.setVisibility(View.VISIBLE);
                    view2.setVisibility(View.GONE);
                    view3.setVisibility(View.GONE);
                    if (checkAvertisement())
                        ble_scan_btn_1.setVisibility(View.VISIBLE);
                    else
                        ble_scan_btn_1.setVisibility(View.GONE);
                }
                break;
            case R.id.toolbar11:
                try {
                    System.out.println("activit name is" + getActivity().getClass().getSimpleName());
                    String aname = getActivity().getClass().getSimpleName();
                    if (aname != null) {
                        if (aname.equalsIgnoreCase("SingleEventHome")) {
                            Intent intent = new Intent(context, SingleEventHome.class);
                            intent.putExtra("Engagement", "");
                            intent.putExtra("back", "APPS");
                            startActivity(intent);
                            getActivity().finish();
                        }
                    } else {

                    }

                } catch (Exception e) {

                }


                break;

            case R.id.button_retake:
                if (contactsArrayList.size() > 0) {
                    view1.setVisibility(View.VISIBLE);
                    view2.setVisibility(View.GONE);
                    view3.setVisibility(View.GONE);
                    if (checkAvertisement())
                        ble_scan_btn_1.setVisibility(View.VISIBLE);
                    else
                        ble_scan_btn_1.setVisibility(View.GONE);
                } else {
                    view1.setVisibility(View.GONE);
                    view2.setVisibility(View.GONE);
                    view3.setVisibility(View.VISIBLE);
                  /*  if (checkAvertisement())
                        ble_scan_btn.setVisibility(View.VISIBLE);
                    else
                        ble_scan_btn.setVisibility(View.GONE);*/
                }

                break;

            case R.id.ble_scan_button:
            case R.id.ble_scan_button_1:
               /* initializeBLE();
                register(true, "");*/
                checkCameraPermission1();

                break;
        }
    }


    private void register(final boolean b, final String value) {
        /*if (searchpDialog.isShowing())
            searchpDialog.dismiss();*/
        //register and verify the bluetooth user id
        final ProgressDialog pDialog = new ProgressDialog(context, R.style.MyAlertDialogStyle);
        pDialog.setMessage("Loading ...");
        pDialog.show();
        String tmp_userid = Paper.book().read("userId", "");


        final String tag_string_req = "Login";
        String url = ApiList.REGISTER_BLE_ID;
        final StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                pDialog.dismiss();
                System.out.println(response);
                Gson gson = new Gson();

                JSONObject jObj = null;
                try {
                    jObj = new JSONObject(response);
                    if (jObj.getBoolean("response")) {

                        if (b) {
                            //getting the exchange id if it is register
                            String exchange_id1 = jObj.getString("bluetooth_exchange_id");
                            //calling advertise method
                            advertise(exchange_id1);
                            //calling the discover method
                            discover();
                        } else {
                            other_user_id = jObj.getString("userid");
                            //if it is verification then call the profile api
                            Getprofile(other_user_id);
                        }

                    } else {
                        if (jObj.getString("responseString").equalsIgnoreCase("Invalid token")) {
                            Intent i = new Intent();
                            i.putExtra("keyMessage", "Session Expired, Please Login ");
                            i.putExtra("keyAlert", "Session Expired");
                            i.setClassName("com.webmobi.gecmedia", "com.webmobi.gecmedia.Views.TokenExpireAlertReceived");
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                        } else
                            Error_Dialog.show(jObj.getString("responseString"), getActivity());
                    }
                } catch (JSONException e) {


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
                    /*User_Details user_details = Paper.book().read("UserDetailsOffline");
                    if (user_details != null)
                        setdataOffline(user_details);*/
                }
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("token", Paper.book().read("token", ""));
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                if (b) {
                    //if it is register
                    params.put("flag", "generate");
                    params.put("userid", Paper.book().read("userId", ""));
                } else {
                    //if it is verification
                    params.put("flag", "verify");
                    params.put("userid", Paper.book().read("userId", ""));
                    params.put("exchange_id", value);
                }
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

    private void initializeBLE() {
        //initializing the ble
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBluetoothLeScanner = BluetoothAdapter.getDefaultAdapter().getBluetoothLeScanner();

            // on_bluetooth();
        }
    }

    /*public void on_bluetooth() {
        //checking the bluetooth is turned on or not
        if (!myBluetoothAdapter.isEnabled()) {

            Intent turnOnIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);

            startActivityForResult(turnOnIntent, REQUEST_ENABLE_BT);


            Toast.makeText(getApplicationContext(), "Bluetooth turned on",

                    Toast.LENGTH_LONG).show();

        } else {
           *//* advertise();
            discover();*//*
     *//* Toast.makeText(getApplicationContext(), "Bluetooth is already on",

                    Toast.LENGTH_LONG).show();
*//*
        }

    }

    private ScanCallback mScanCallback = new ScanCallback() {
        //scanning the ble device
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
           *//* if (result == null
                    || result.getDevice() == null
                    || TextUtils.isEmpty(result.getDevice().getName()))
                return;*//*

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (result == null || result.getDevice() == null)
                    return;
            }


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                String result_txt = new String(result.getScanRecord().getManufacturerSpecificData(100), Charset.forName("UTF-8"));
                if (result_txt != null || !result_txt.equalsIgnoreCase("")) {
                    System.out.println("BLE Scan Result : " + result_txt);
                    //adding the devices to the list
                    ble_user_id.put(result_txt, result_txt);

                }
            }



        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            super.onBatchScanResults(results);
        }

        @Override
        public void onScanFailed(int errorCode) {
            Log.e("BLE", "Discovery onScanFailed: " + errorCode);
            super.onScanFailed(errorCode);
        }
    };*/

    private void discover() {
        //discover other ble devices
        searchpDialog = new ProgressDialog(context, R.style.MyAlertDialogStyle);
        searchpDialog.setMessage("Searching ...");
        searchpDialog.show();
        List<ScanFilter> filters = new ArrayList<ScanFilter>();
      /*  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //uuid filter
            ScanFilter filter = new ScanFilter.Builder()
                    .setServiceUuid(new ParcelUuid(UUID.fromString("00000000-e19a-4fb1-9706-de71d7e28e9b")))
                    .build();
            filters.add(filter);

            ScanSettings settings = new ScanSettings.Builder()
                    .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                    .build();

            mBluetoothLeScanner.startScan(filters, settings, mScanCallback);
//        mBluetoothLeScanner.startScan(mScanCallback);
        }*/
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    // mBluetoothLeScanner.stopScan(mScanCallback);
                    //getting the exchange id from other device
                    for (Map.Entry<String, String> en : ble_user_id.entrySet()) {
                        System.out.println("BLE Scan MAP Result : " + en.getKey() + " = " + en.getValue());
                        //verifying the exchange id
                        register(false, en.getValue());
                        break;
                    }
                }
            }
        }, 5000);

    }

    private void advertise(String exchange_id) {
        //advertise as ble device
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            BluetoothLeAdvertiser advertiser = BluetoothAdapter.getDefaultAdapter().getBluetoothLeAdvertiser();

            AdvertiseSettings settings = new AdvertiseSettings.Builder()
                    .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY)
                    .setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_HIGH)
                    .setConnectable(false)
                    .build();
            //uuid
            ParcelUuid pUuid = new ParcelUuid(UUID.fromString("00000000-e19a-4fb1-9706-de71d7e28e9b"));
//        ParcelUuid pUuid = new ParcelUuid(UUID.fromString("webmobi"));
            //advertise data with exchange id
            AdvertiseData data = new AdvertiseData.Builder()
                    .setIncludeDeviceName(false)
                    .addServiceUuid(pUuid)
                    .addManufacturerData(100, exchange_id.getBytes(Charset.forName("UTF-8")))
                    .build();
            AdvertiseCallback advertisingCallback = new AdvertiseCallback() {
                @Override
                public void onStartSuccess(AdvertiseSettings settingsInEffect) {
                    super.onStartSuccess(settingsInEffect);
                }

                @Override
                public void onStartFailure(int errorCode) {
                    Log.e("BLE", "Advertising onStartFailure: " + errorCode);
                    super.onStartFailure(errorCode);
                }
            };

            advertiser.startAdvertising(settings, data, advertisingCallback);
        }
    }

    //Request to send image to server
    private void sendImageToServer() {
        // test for Api call
        //if (isUserDataIsValid())
        uploadData(true, "", "", "", "", "", "", "", "", "", "", "", "");

       /* Intent intent = new Intent(Intent.ACTION_SYNC, null, getActivity(), UploadContactsFragmentDetails.class);
        intent.putExtra("receiver", mReceiver);
        intent.putExtra("header", requestdata);
        intent.putExtra("image", "attachment");
        getActivity().startService(intent);*/
    }

    private boolean isUserDataIsValid() {
        if (et_fname.getText().toString().trim().equals("")) {
            Toast.makeText(getActivity(), "Enter valid First Name.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (et_lname.getText().toString().trim().equals("")) {
            Toast.makeText(getActivity(), "Enter valid Last Name.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    //checking camera
    private void checkCameraPermission() {

        if (android.os.Build.VERSION.SDK_INT >= 23) {
            // only for marshmallow and newer versions
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{
                                Manifest.permission.CAMERA}, ZBAR_CAMERA_PERMISSION);
            } else {
                captureImage(true);
            }
        } else {
            captureImage(true);
        }

    }

    //QR scan calling
    public void checkCameraPermission1() {

        if (android.os.Build.VERSION.SDK_INT >= 23) {
            // only for marsemellow and newer versions
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.CAMERA}, ZBAR_CAMERA_PERMISSION1);
            } else {

                //Do something
                Intent intent = new Intent(getActivity().getApplicationContext(), SimpleScannerActivity.class);
                startActivityForResult(intent, SCANNER_REQUEST_CODE);
                // startActivityForResult(intent, SCANNER_REQUEST_CODE);
            }
        } else {

            //Do something
            Intent intent = new Intent(getActivity().getApplicationContext(), SimpleScannerActivity.class);
            startActivityForResult(intent, SCANNER_REQUEST_CODE);
        }
    }

    private void captureImage(boolean iscamera) {
        dispatchTakePictureIntent();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String imagePath = mCurrentPhotoPath;
        switch (requestCode) {

            case 50:

                if (resultCode == Activity.RESULT_OK) {
                    setPic(imagePath);
                }

                break;

            case REQUEST_ENABLE_BT:
                if (resultCode == Activity.RESULT_OK) {
                    if (checkAvertisement())
                        ble_scan_btn.setVisibility(View.VISIBLE);
                    else
                        ble_scan_btn.setVisibility(View.GONE);
                }
                break;
            case SCANNER_REQUEST_CODE:
                Intent d=data;
                try {

                    if (resultCode == RESULT_OK) {
                        boolean duplicate = false;
                        qrcode_userid = data.getStringExtra("content");
                        String decoded_qrcode_userid=DataBaseStorage.decrypt(qrcode_userid);
                        String arr =(qrcode_userid.substring(qrcode_userid.lastIndexOf("/") + 1));
                        try {
                            appid = appDetails.getAppId();
                        }catch (Exception e){

                        }
                        loggedin_userid=Paper.book().read("userId", "");
                        shareQrCodeAPI(appid,decoded_qrcode_userid,loggedin_userid);
                    /*contactsArrayList.clear();
                    getContactsLists();*/
                    } else {
                        Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){

                }
        }


    }

    private void shareQrCodeAPI(final String appid, final String qrcode_userid, final String loggedin_userid) {

        //api call to upload the contact
        final ProgressDialog progressDialog = new ProgressDialog(getActivity(), R.style.MyAlertDialogStyle);
        progressDialog.setMessage("Please wait....");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();


        // Adding APi to send scan data

        String tag_string_req = "Share_qr";
        String url = ApiList.updateQRContact;


        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                try {
                    progressDialog.hide();

                    System.out.println(response);


                    JSONObject jObj = new JSONObject(response);


                    if (jObj.getBoolean("response")) {
                        contactsArrayList.clear();
                        getContactsLists();


                    }
                } catch (JSONException e) {
                    if (progressDialog.isShowing())
                        progressDialog.hide();

                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if (progressDialog.isShowing()) {
                    progressDialog.hide();
                }
                Log.v(TAG, error.toString());
                if (getActivity() != null)
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

                params.put("appid", appid);
                params.put("qrcode_userid", qrcode_userid);
                params.put("loggedin_userid", loggedin_userid);

                System.out.println("Upload Contacts Param : " + params.toString());
                return params;

            }
        };

//        AppNew.getInstance().getRequestQueue().getCache().clear();
        App.getInstance().addToRequestQueue(strReq, tag_string_req);
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                100000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));


    }


    public void myqrRefresh(Boolean b) {
        if (b) {
            contactsArrayList.clear();
            getContactsLists();
        }
    }


    private void setPic(String imagePath) {
        // Get the dimensions of the View

        int targetW = imageview.getWidth();
        int targetH = imageview.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;


        try {
            // Determine how much to scale down the image
            //  int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

            // Decode the image file into a Bitmap sized to fill the View
            bmOptions.inJustDecodeBounds = false;
            // bmOptions.inSampleSize = scaleFactor;
            bmOptions.inPurgeable = true;
            Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
            imageview.setImageBitmap(bitmap);
            uploadData(false, "", "", "", "", "", "", "", "", "", "", "", "");

            if (contactsArrayList.size() > 0) {
                view1.setVisibility(View.GONE);
                view2.setVisibility(View.GONE);
                view3.setVisibility(View.VISIBLE);
                /*if (checkAvertisement())
                    ble_scan_btn.setVisibility(View.VISIBLE);
                else
                    ble_scan_btn.setVisibility(View.GONE);*/
                //after successfully uploaded scan business card again call for lists
                contactsArrayList.clear();
                getContactsLists();
            } else {
                view1.setVisibility(View.VISIBLE);
                view2.setVisibility(View.GONE);
                view3.setVisibility(View.GONE);
                if (checkAvertisement())
                    ble_scan_btn_1.setVisibility(View.VISIBLE);
                else
                    ble_scan_btn_1.setVisibility(View.GONE);
            }

        } catch (Exception e) {
            e.printStackTrace();
            view1.setVisibility(View.VISIBLE);
            view2.setVisibility(View.GONE);
            view3.setVisibility(View.GONE);
            if (checkAvertisement())
                ble_scan_btn_1.setVisibility(View.VISIBLE);
            else
                ble_scan_btn_1.setVisibility(View.GONE);
        }


    }


    private void uploadData(boolean isFromScan, String fname, String lname, String company, String conphone, String website, String descrip, String e_mail, String u_id, String dtext, String uemail, String flag, String item_flag) {
        //api call to upload the contact
        final ProgressDialog progressDialog = new ProgressDialog(getActivity(), R.style.MyAlertDialogStyle);
        progressDialog.setMessage("Please wait....");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        // progressDialog.dismiss();
        ff = isFromScan;
        String f_name = null;
        String l_name = null;
        String desc = null;
        String photo_data = null;
        String email = null;
        String email1 = tv_altEmail.getText().toString();
        String alt_cont = tv_alt_phone.getText().toString();
        String alt_web = tv_altWebsite.getText().toString();
        String uid = Paper.book().read("userId", "");
        String itemflag = null;
        String detect_text = null;
        if (isFromScan) {
          /*  f_name = fname;
            l_name = lname;
            desc = descrip;
            extension = "";
            fileExtensionName = "";
            photo_data = "";
            email = Paper.book().read("Email_ID", "");
            uid = Paper.book().read("userId", "");
            detect_text = "1";
            item_flag = "contact";*/

            f_fname = et_fname.getText().toString();
            f_lname = et_lname.getText().toString();
            f_desc = et_desc.getText().toString();
            f_company = tv_company.getText().toString();
            f_website = tv_website.getText().toString();
            f_addrs = tv_address.getText().toString();
            f_email = tv_email.getText().toString();
            f_altphone = tv_alt_phone.getText().toString();
            f_altweb = tv_altWebsite.getText().toString();
            f_email1 = tv_email1.getText().toString();
            f_desig = tv_designation.getText().toString();
            f_uemail = Paper.book().read("Email", "");
            f_uid = Paper.book().read("userId", "");
            f_flag = "create";
            f_itemflag = "contact";
            f_phone = tv_phone.getText().toString();
            // Adding APi to send scan data

            String tag_string_req = "upload_contact";
            String url = ApiList.Post_Contacts;

            final String finalF_name = f_fname;
            final String finalL_name = f_lname;
            final String finalDesc = f_desc;
            // final String finalPhoto_data = photo_data;
            final String finalItem_flag = f_itemflag;
            final String finalemail = f_email;
            final String finalemail1 = f_email1;
            final String finalaltphone = f_altphone;
            final String finalaltweb = f_altweb;
            final String finaluemail = f_uemail;
            final String finaluid = f_uid;
            final String finaltextdetect_text = "false";
            final String finalPhone = f_phone;
            final String finalcompany = f_company;
            final String finalwebsite = f_website;
            final String finaldesignation = f_desig;
            final String finaladdrs = f_addrs;
            final String finalappid = appid;


            StringRequest strReq = new StringRequest(Request.Method.POST,
                    url, new Response.Listener<String>() {


                @Override
                public void onResponse(String response) {
                    try {
                        progressDialog.dismiss();

                        System.out.println(response);


                        JSONObject jObj = new JSONObject(response);


                        if (jObj.getBoolean("response")) {


                            JSONObject nlp = jObj.getJSONObject("nlp_detect");


                           /* if(ff) {
                                view1.setVisibility(View.GONE);
                                view2.setVisibility(View.VISIBLE);
                                view3.setVisibility(View.GONE);
                                if (checkAvertisement())
                                    ble_scan_btn_1.setVisibility(View.VISIBLE);
                                else
                                    ble_scan_btn_1.setVisibility(View.GONE);
                            }*/

                            if (contactsArrayList.size() > 0) {
                                view1.setVisibility(View.GONE);
                                view2.setVisibility(View.GONE);
                                view3.setVisibility(View.VISIBLE);
                               /* if (checkAvertisement())
                                ble_scan_btn.setVisibility(View.VISIBLE);
                            else
                                    ble_scan_btn.setVisibility(View.GONE);*/
                                //after successfully uploaded scan business card again call for lists
                                contactsArrayList.clear();
                                getContactsLists();
                            } else {
                                view1.setVisibility(View.VISIBLE);
                                view2.setVisibility(View.GONE);
                                view3.setVisibility(View.GONE);
                                if (checkAvertisement())
                                    ble_scan_btn_1.setVisibility(View.VISIBLE);
                                else
                                    ble_scan_btn_1.setVisibility(View.GONE);
                            }
                        } else if (!jObj.getBoolean("response")) {
                            Error_Dialog.show(jObj.getString("responseString"), getActivity());
                        }

                    } catch (JSONException e) {
                        if (progressDialog.isShowing())
                            //progressDialog.hide();
                            progressDialog.dismiss();

                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    if (progressDialog.isShowing()) {
                        // progressDialog.hide();
                        progressDialog.dismiss();
                    }
                    Log.v(TAG, error.toString());
                    if (getActivity() != null)
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

                    params.put("first_name", finalF_name);
                    params.put("last_name", finalL_name);
                    params.put("email", finaluemail);
                    params.put("contact_email", finalemail);
                    if (finalemail1 != null) {
                        params.put("contact_email_1", finalemail1);
                    } else {
                        params.put("contact_email_1", "");
                    }
                    if (finalaltphone != null) {
                        params.put("contact_phone_1", finalaltphone);
                    } else {
                        params.put("contact_phone_1", "");
                    }
                    if (finalaltweb != null) {
                        params.put("website_1", finalaltweb);
                    } else {
                        params.put("website_1", "");
                    }
                    params.put("userid", finaluid);
                    params.put("appid", finalappid);
                    params.put("contact_info", finalDesc);
                    params.put("contact_phone", finalPhone);
                    params.put("company", finalcompany);
                    params.put("website", finalwebsite);
                    params.put("designation", finaldesignation);
                    params.put("address", finaladdrs);
                    params.put("flag", "create");
                    params.put("item_flag", finalItem_flag);
                    params.put("image_url", f_imageUrl);
                    params.put("detect_text", finaltextdetect_text);
                    System.out.println("Upload Contacts Param : " + params.toString());
                    return params;

                }
            };

//        AppNew.getInstance().getRequestQueue().getCache().clear();
            App.getInstance().addToRequestQueue(strReq, tag_string_req);
            strReq.setRetryPolicy(new DefaultRetryPolicy(
                    100000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            ));


        } else {
            f_name = et_fname.getText().toString();
            l_name = et_lname.getText().toString();
            desc = et_desc.getText().toString();
            email = Paper.book().read("Email", "");
            uid = Paper.book().read("userId", "");
            alt_web = tv_altWebsite.getText().toString();
            alt_cont = tv_alt_phone.getText().toString();
            detect_text = "1";
            item_flag = "photo";
            try {
                someFilepath = mCurrentPhotoPath;
                extension = someFilepath.substring(someFilepath.lastIndexOf("/") + 1);
                fileExtensionName = someFilepath.substring(someFilepath.lastIndexOf(".") + 1);
                photo_data = convert_uri_to_base64(mCurrentPhotoPath);

            } catch (NullPointerException e) {

                e.printStackTrace();

            } catch (Exception e) {
                e.printStackTrace();
            }


            String tag_string_req = "upload_contact";
            String url = ApiList.Post_Contacts;

            final String finalF_name = f_name;
            final String finalL_name = l_name;
            final String finalDesc = desc;
            final String finalPhoto_data = photo_data;
            final String finalItem_flag = item_flag;
            final String finalemail = email;
            final String finalemail1 = email1;
            final String finaluid = uid;
            final String finalalt_cont = alt_cont;
            final String finalalt_web = alt_web;
            final String finaltextdetect_text = detect_text;
            // final String finalappid=appid;
            StringRequest strReq = new StringRequest(Request.Method.POST,
                    url, new Response.Listener<String>() {


                @Override
                public void onResponse(String response) {
                    try {
                        //  progressDialog.hide();
                        progressDialog.dismiss();

                        System.out.println(response);


                        JSONObject jObj = new JSONObject(response);


                        if (jObj.getBoolean("response")) {


                            JSONObject nlp = jObj.getJSONObject("nlp_detect");

                      /*  JSONArray chatlist = nlp.getJSONArray("nlp_detect");
                        String nm=chatlist.get(0).toString();*/
                            String fname = nlp.get("PERSON_NAME").toString();
                            String email = nlp.get("Email").toString();
                            f_imageUrl = jObj.getString("imageUrl");
                            String addrs = nlp.get("LOCATION").toString();
                            String desc = jObj.getString("vision_text");
                            //   int Phoneno=jObj.getInt("Phone No");


                            et_desc.setText(desc);
                            et_fname.setText(fname);
                            tv_email.setText(email);
                            tv_address.setText(addrs);

                            et_lname.setText("");

                            //  uploadData(false, fname,"","",0,"","",email,uid,"",uemail,"update","photo");

                            if (!ff) {
                                view1.setVisibility(View.GONE);
                                view2.setVisibility(View.VISIBLE);
                                view3.setVisibility(View.GONE);
                                if (checkAvertisement())
                                    ble_scan_btn_1.setVisibility(View.VISIBLE);
                                else
                                    ble_scan_btn_1.setVisibility(View.GONE);
                            } else if (contactsArrayList.size() > 0) {
                                view1.setVisibility(View.GONE);
                                view2.setVisibility(View.GONE);
                                view3.setVisibility(View.VISIBLE);
                               /* if (checkAvertisement())
                                    ble_scan_btn.setVisibility(View.VISIBLE);
                                else
                                    ble_scan_btn.setVisibility(View.GONE);*/
                                //after successfully uploaded scan business card again call for lists
                                contactsArrayList.clear();
                                getContactsLists();
                            } else {
                                view1.setVisibility(View.VISIBLE);
                                view2.setVisibility(View.GONE);
                                view3.setVisibility(View.GONE);
                                if (checkAvertisement())
                                    ble_scan_btn_1.setVisibility(View.VISIBLE);
                                else
                                    ble_scan_btn_1.setVisibility(View.GONE);
                            }
                        } else if (!jObj.getBoolean("response")) {
                            Error_Dialog.show(jObj.getString("responseString"), getActivity());
                        }

                    } catch (JSONException e) {
                        if (progressDialog.isShowing())
                            //   progressDialog.hide();
                            progressDialog.dismiss();

                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    if (progressDialog.isShowing()) {
                        // progressDialog.hide();
                        progressDialog.dismiss();
                        System.out.print("some issue");
                    }
                    Log.v(TAG, error.toString());
                    if (getActivity() != null)
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
                    params.put("email", finalemail);
                    params.put("userid", finaluid);
                    //   params.put("appid", finalappid);
                    params.put("filedata", finalPhoto_data);

                    if (finalemail1 != null) {
                        params.put("contact_email_1", finalemail1);
                    } else {
                        params.put("contact_email_1", "");
                    }
                    if (finalalt_cont != null) {
                        params.put("contact_phone_1", finalalt_cont);
                    } else {
                        params.put("contact_phone_1", "");
                    }
                    if (finalalt_web != null) {
                        params.put("website_1", finalalt_web);
                    } else {
                        params.put("website_1", "");
                    }

                    params.put("file_name", System.currentTimeMillis() + extension);
                    params.put("contenttype", "image/" + fileExtensionName);
                    params.put("first_name", finalF_name);
                    params.put("last_name", finalL_name);
                    params.put("contact_info", finalDesc);
                    params.put("flag", "create");
                    params.put("item_flag", finalItem_flag);
                    params.put("image_url", "");
                    params.put("detect_text", finaltextdetect_text);
                    // System.out.println("Upload Contacts Param : " + params.toString());
                    return params;

                }
            };

//        AppNew.getInstance().getRequestQueue().getCache().clear();
            App.getInstance().addToRequestQueue(strReq, tag_string_req);
            strReq.setRetryPolicy(new DefaultRetryPolicy(
                    100000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            ));


        }
    }

    public void getContactsLists() {
        try {
            progressDialog = new ProgressDialog(getActivity(), R.style.MyAlertDialogStyle);

            progressDialog.setMessage("Please wait....");
            progressDialog.show();
            // progressDialog.dismiss();


            String tag_string_req = "get contact list";
            String url = ApiList.CONTACT_LIST + userid + "&appid=" + appid;
            StringRequest strReq = new StringRequest(Request.Method.GET,
                    url, new Response.Listener<String>() {


                @Override
                public void onResponse(String response) {
                    try {
                        // progressDialog.hide();
                        progressDialog.dismiss();

                        System.out.println(response);
                        JSONObject jObj = new JSONObject(response);


                        if (jObj.getBoolean("response")) {


                            JSONArray jsonArray = jObj.getJSONArray("contacts");

                            ContactsModel contactsModel;
                            for (int i = 0; i < jsonArray.length(); i++) {
                                contactsModel = new ContactsModel();
                                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                                contactsModel.setContact_info(jsonObject.isNull("contact_info") ? null : jsonObject.getString("contact_info"));
                                contactsModel.setEmail(jsonObject.isNull("contact_email") ? null : jsonObject.getString("contact_email"));
                                contactsModel.setContact_info(jsonObject.isNull("contact_info") ? null : jsonObject.getString("contact_info"));
                                contactsModel.setAddress(jsonObject.isNull("address") ? null : jsonObject.getString("address"));
                                contactsModel.setContactId(jsonObject.isNull("contact_id") ? null : jsonObject.getString("contact_id"));
                                contactsModel.setName(jsonObject.isNull("contact_first_name") ? null : jsonObject.getString("contact_first_name"));
                                contactsModel.setContact_first_name(jsonObject.isNull("contact_first_name") ? null : jsonObject.getString("contact_first_name"));
                                contactsModel.setImageUrl(jsonObject.isNull("card_image_url") ? null : jsonObject.getString("card_image_url"));

                                contactsModel.setUserId(jsonObject.isNull("userid") ? null : jsonObject.getString("userid"));
                                contactsModel.setContact_last_name(jsonObject.isNull("contact_last_name") ? null : jsonObject.getString("contact_last_name"));
                                contactsModel.setContact_phone(jsonObject.isNull("contact_phone") ? null : jsonObject.getString("contact_phone"));
                                contactsModel.setCompany(jsonObject.isNull("company") ? null : jsonObject.getString("company"));
                                contactsModel.setWebsite(jsonObject.isNull("website") ? null : jsonObject.getString("website"));
                                contactsModel.setDesignation(jsonObject.isNull("designation") ? null : jsonObject.getString("designation"));

                                contactsModel.setContact_email_1(jsonObject.isNull("contact_email_1") ? null : jsonObject.getString("contact_email_1"));
                                contactsModel.setContact_phone_1(jsonObject.isNull("contact_phone_1") ? null : jsonObject.getString("contact_phone_1"));
                                contactsModel.setWebsite_1(jsonObject.isNull("website_1") ? null : jsonObject.getString("website_1"));
                                contactsModel.setCard_type(jsonObject.isNull("card_type") ? null : jsonObject.getString("card_type"));
                                contactsModel.setFax(jsonObject.isNull("fax") ? null : jsonObject.getString("fax"));
                                contactsModel.setCard_image_url(jsonObject.isNull("card_image_url") ? null : jsonObject.getString("card_image_url"));


                                contactsArrayList.add(contactsModel);


                            }
                            if (contactsArrayList.size() > 0) {

                                view1.setVisibility(View.GONE);
                                view2.setVisibility(View.GONE);
                                view3.setVisibility(View.VISIBLE);
                            /*if (checkAvertisement())
                                ble_scan_btn.setVisibility(View.VISIBLE);
                            else
                                ble_scan_btn.setVisibility(View.GONE);*/
                                contactsAdapter = new ContactsAdapter(context, contactsArrayList);
                                recyclerView.setAdapter(contactsAdapter);
                                contactsAdapter.notifyDataSetChanged();
                            }

                        } else {
                            Error_Dialog.show(jObj.getString("responseString"), getActivity());
                        }

                    } catch (JSONException e) {
                        // progressDialog.hide();
                        progressDialog.dismiss();
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    if (progressDialog.isShowing()) {
                        //progressDialog.hide();
                        progressDialog.dismiss();
                    }
                    Log.v(TAG, error.toString());
                    if (getActivity() != null)
                        if (error instanceof TimeoutError) {
                            Error_Dialog.show("Timeout", getActivity());
                        } else if (VolleyErrorLis.isServerProblem(error)) {
                            Error_Dialog.show(VolleyErrorLis.handleServerError(error, context), getActivity());
                        } else if (VolleyErrorLis.isNetworkProblem(error)) {
                            Error_Dialog.show("Please Check Your Internet Connection", getActivity());
                        }
                }
            });


            App.getInstance().addToRequestQueue(strReq, tag_string_req);
            strReq.setRetryPolicy(new DefaultRetryPolicy(
                    500000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            ));
        } catch (Exception e) {

        }

    }

    private String convert_uri_to_base64(String path) {

        Bitmap bm = BitmapFactory.decodeFile(path);
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        bm.compress(Bitmap.CompressFormat.JPEG, 80, baos);
//        return baos.toByteArray();


        if (bm != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 40, baos);
            bm.recycle();
            bm = null;
            byte[] b = baos.toByteArray();
            return Base64.encodeToString(b, Base64.DEFAULT);
        } else {
            Uri imageUri = Uri.parse(path);
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), imageUri);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 40, baos);
                bitmap.recycle();
                bitmap = null;
                byte[] b = baos.toByteArray();
                return Base64.encodeToString(b, Base64.DEFAULT);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onresume is called");

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        switch (requestCode) {
            case ZBAR_CAMERA_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    //Do something
                    captureImage(true);

                } else {
                    Toast.makeText(getActivity(), "Please grant camera permission to use camera", Toast.LENGTH_SHORT).show();

                }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
//        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(getActivity(),
                        getApplicationContext().getPackageName()+".provider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

                startActivityForResult(takePictureIntent, 50);
            }
//        }
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {

        if (resultCode == 0) {

            Toast.makeText(getApplicationContext(), "Posted Successfully", Toast.LENGTH_LONG).show();

        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mReceiver = new UploadResultReceiver(new Handler());
        mReceiver.setReceiver(this);
        myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        // on_bluetooth();
    }

    @Override
    public void onStop() {

        try {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        } catch (Exception e) {

        }
        super.onStop();
    }

    @Override
    public void onPause() {
        try {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        } catch (Exception e) {

        }
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void Getprofile(String value) {
        //getting the profile info of particular user
        if (searchpDialog.isShowing())
            searchpDialog.dismiss();
        final ProgressDialog pDialog = new ProgressDialog(context, R.style.MyAlertDialogStyle);
        pDialog.setMessage("Loading ...");
        pDialog.show();
        String tmp_userid = Paper.book().read("userId", "");

        String tag_string_req = "Login";
        String url = ApiList.GetProfile + value;
        StringRequest strReq = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                pDialog.dismiss();
                System.out.println(response);
                Gson gson = new Gson();

                JSONObject jObj = null;
                try {
                    jObj = new JSONObject(response);
                    if (jObj.getBoolean("response")) {
                        JSONArray chatlist = jObj.getJSONArray("Profile");
                        String userdetails = chatlist.getJSONObject(0).toString();
                        User_Details user_details = gson.fromJson(userdetails, User_Details.class);

                        saveContacts(user_details.getFirst_name(), user_details.getLast_name());
//                        setdata(user_details);
                    } else {
                        if (jObj.getString("responseString").equalsIgnoreCase("Invalid token")) {
                            Intent i = new Intent();
                            i.putExtra("keyMessage", "Session Expired, Please Login ");
                            i.putExtra("keyAlert", "Session Expired");
                            i.setClassName("com.webmobi.gecmedia", "com.webmobi.gecmedia.Views.TokenExpireAlertReceived");
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                        } else
                            Error_Dialog.show(jObj.getString("responseString"), getActivity());
                    }
                } catch (JSONException e) {


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
                    /*User_Details user_details = Paper.book().read("UserDetailsOffline");
                    if (user_details != null)
                        setdataOffline(user_details);*/
                }
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
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

    private void saveContacts(final String first_name, final String last_name) {
       /* first_name = "Dave";
        last_name = "D";*/
        //confirmation dialog
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(context);

        builder.setTitle("Add Contact")
                .setMessage("Are you sure you want to add " + first_name + " " + last_name + " ?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // once confirmed upload the data
                        // uploadData(false, fname,"","",Phoneno,"","",email,uid,"",uemail,"update","photo");
                        uploadData(true, f_fname, f_lname, "", "", "", "", "", "", "", "", "", "");

                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })

                .show();

    }


}
