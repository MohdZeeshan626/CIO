package com.singleevent.sdk.View.RightActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.SpannableString;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.singleevent.sdk.ApiList;
import com.singleevent.sdk.App;
import com.singleevent.sdk.Custom_View.Error_Dialog;
import com.singleevent.sdk.Custom_View.Util;
import com.singleevent.sdk.View.RightActivity.admin.leadGeneration.AdminLeadGenerationActivity;
import com.singleevent.sdk.model.AppDetails;
import com.singleevent.sdk.model.User_Details;
import com.singleevent.sdk.R;
import com.singleevent.sdk.View.RightActivity.admin.checkin.SimpleScannerActivity;
import com.singleevent.sdk.View.RightActivity.admin.checkin.VolleyResponseListener;
import com.singleevent.sdk.View.RightActivity.admin.checkin.VolleyUtils;
import com.singleevent.sdk.View.RightActivity.admin.model.EventUser;
import com.singleevent.sdk.View.RightActivity.admin.utils.Urls;
import com.singleevent.sdk.utils.DataBaseStorage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import io.paperdb.Paper;


public class MyQrCodeGeneratorActivity extends AppCompatActivity implements VolleyResponseListener {

    private ImageView imageView;
    private Toolbar toolbar;
    private Button btn_scan_qr;
    private static final int SCAN_QR_REQUEST_CODE = 5000;
    public static final int SCANNER_REQUEST_CODE = 220;
    public static final String CHECK_IN_REQ_TAG = "check_in_users";
    private static final int ZBAR_CAMERA_PERMISSION = 444;
    public static final String USER_LIST_REQ_TAG = "list_users_req";
    private String appid, username;
    private RequestQueue queue1;
    Gson gson = new Gson();
    User_Details user_details;

    ArrayList<String> userid = new ArrayList<>();
    ArrayList<String> date = new ArrayList<>();
    ArrayList<String> email = new ArrayList<>();
    ArrayList<String> agendaid = new ArrayList<>();
    ArrayList<String> checkdate = new ArrayList<>();
    AppDetails appDetails;
    String checkInUrl = Urls.getCheckInUrl();
    EventUser eventuser;
    String admin_type;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Paper.init(this);
        appDetails = Paper.book().read("Appdetails");
        setContentView(R.layout.activity_qr_code_generate);
        admin_type = Paper.book(appDetails.getAppId()).read("admin_flag", "");
        TextView tv_userName, tv_eventName;
        imageView = (ImageView) findViewById(R.id.imageView);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tv_eventName = (TextView) findViewById(R.id.tv_eventName);
        tv_userName = (TextView) findViewById(R.id.tv_userName);
        btn_scan_qr = (Button) findViewById(R.id.btn_selfcheckin);
        btn_scan_qr.setBackground(Util.setdrawable(MyQrCodeGeneratorActivity.this, com.singleevent.sdk.R.drawable.healthpostbut,
                Color.parseColor(appDetails.getTheme_color())));
        queue1 = Volley.newRequestQueue(this);

        btn_scan_qr.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                checkCameraPermission();

            }
        });
        username = Paper.book().read("username", "");


        setSupportActionBar(toolbar);
        tv_eventName.setText(appDetails.getAppName());
        tv_userName.setText(DataBaseStorage.decrypt(Paper.book().read("user", "")));

        getSupportActionBar().setTitle("My Badge");
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));
        String userId = Paper.book().read("Islogin", false) ?
                DataBaseStorage.encrypt(Paper.book().read("userId", "")) : "";

        /*Generating qr code into bitmap format and setting it to imageView*/
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(userId, BarcodeFormat.QR_CODE, 250, 250);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            imageView.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }

    }

    public void checkCameraPermission() {

        if (android.os.Build.VERSION.SDK_INT >= 23) {
            // only for marsemellow and newer versions
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA}, ZBAR_CAMERA_PERMISSION);
            } else {

                //Do something
                Intent intent = new Intent(MyQrCodeGeneratorActivity.this, SimpleScannerActivity.class);
                startActivityForResult(intent, SCANNER_REQUEST_CODE);
            }
        } else {

            //Do something
            Intent intent = new Intent(MyQrCodeGeneratorActivity.this, SimpleScannerActivity.class);
            startActivityForResult(intent, SCANNER_REQUEST_CODE);
        }
    }

// For self checkin new code 18/07/19

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SCANNER_REQUEST_CODE) {
            Intent d = data;

            if (resultCode == RESULT_OK) {
                boolean duplicate = false;
                String id = data.getStringExtra("content");
                Log.d("null_or_not", "onActivityResult: " + id);

                if (id.startsWith("ex")) {
                    if (id.contains(appDetails.getAppId())) {
                        String arr = "";
                        arr = (id.substring(appDetails.getAppId().length() + 2));
                        if (!arr.isEmpty() && arr.length() > 0) {
                            try {
                                Log.d("exhibitors_id", "onActivityResult: " + arr);
                                int exhibitor_id = Integer.parseInt(arr); //to check if id is getting converted in to Integer
                                checkUserIdToCheckin(arr);

                            } catch (Exception e) {
                                Toast.makeText(MyQrCodeGeneratorActivity.this, "Invalid Exhibitor Id", Toast.LENGTH_SHORT).show();

                            }

                        } else {
                            Toast.makeText(MyQrCodeGeneratorActivity.this, "QR code Don't have Exhibitor id", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(MyQrCodeGeneratorActivity.this, "QR code don't belong to this app", Toast.LENGTH_SHORT).show();

                    }

                } else {
                    if (id.contains(appDetails.getAppId())) {
                        String arr = "";
                        arr = (id.substring(appDetails.getAppId().length()));
                        if (!arr.isEmpty() && arr.length() > 0) {
                            try {
                                int agenda_id = Integer.parseInt(arr); //to check if id is getting converted in to Integer
                                agendaid.add(arr);
                                appid = appDetails.getAppId();
                                // userid.add(DataBaseStorage.decrypt(Paper.book().read("userId", "")));
                                userid.add((Paper.book().read("userId", "")));
                                // Paper.book().read("userId", "")
                                email.add(Paper.book().read("Email", ""));
                                // email.add(user_details.getEmail());
                                checkdate.add(String.valueOf(System.currentTimeMillis()));
                                // API call to send the data for self checkin 19/07/2019
                                sendCheckinData();
                            } catch (Exception ex) {
                                Toast.makeText(MyQrCodeGeneratorActivity.this, "Session is Not Valid", Toast.LENGTH_SHORT).show();

                            }
                        } else {
                            Toast.makeText(MyQrCodeGeneratorActivity.this, "No Session Id Available", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(MyQrCodeGeneratorActivity.this, "Please Scan the Correct QR Code", Toast.LENGTH_SHORT).show();
                    }
                }

            } else {
                Toast.makeText(MyQrCodeGeneratorActivity.this, "Please Scan the Correct QR Code", Toast.LENGTH_SHORT).show();
            }


            // String date[]= JSONArray(Arrays.asList(offlineTimeInMillsList.toArray())));

            // Do something with the contact here (bigger example below)

            //   EventUser checkedUser = null;

            //  handleUser(checkedUser, duplicate);

        } else {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkUserIdToCheckin(String exhibitor_id) {

        final String url = ApiList.ADD_Leads;
        String str_rq = "check_User_Id_To_Checkin";

        // reportModelArrayList = new ArrayList<>();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.v("exhibitors_id", response);
                    if (jsonObject.getBoolean("response")) {

                        Error_Dialog.show(jsonObject.getString("responseString"), MyQrCodeGeneratorActivity.this);

                    } else {
                        Error_Dialog.show(jsonObject.getString("responseString"), MyQrCodeGeneratorActivity.this);
                    }

                } catch (JSONException e) {
                    Log.v("exhibitors_id", e.toString());
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.v("exhibitors_id", error.toString());

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> param = new HashMap<>();
                param.put("appid", appDetails.getAppId());
                param.put("userid", Paper.book().read("userId", ""));
                param.put("username", Paper.book().read("username", ""));
                param.put("leadid", exhibitor_id);
                param.put("action", "checkin");
                Log.v("exhibitors_id", param.toString());

                return param;
            }
        };

        App.getInstance().addToRequestQueue(stringRequest, str_rq);
        
    }

    private void handleUser(boolean duplicate) {
        if (duplicate) {

            // new CustomDialog(this, checkedUser.getFirst_name(), "Duplicate user", false).show();
            // sendCheckinData();
            new CustomDialog(this, username, "User Checked-in successful", true).show();

            //  addCheckedUser(checkedUser);
        }/* else if (u!= null) {
//            Toast.makeText(this, "Found User = " + checkedUser.getName() + ", with Id = " + checkedUser.getUserId(), Toast.LENGTH_LONG).show();
            new MyQrCodeGeneratorActivity.CustomDialog(this, user_details.getFirst_name(), "User Checked-in successful", true).show();
            //addCheckedUser(checkedUser);
//            sendCheckinData();
        }*/ else {
//            Toast.makeText(this, "User not found ", Toast.LENGTH_LONG).show();
            new CustomDialog(this, "Unknown User", "User not found", false).show();
        }
    }

    // alert sound when self checkin is successful
    private void dialogForHandleUserOnClick(boolean duplicate) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyAlertDialogStyle);
        builder.setMessage("Do you want to Check-in the user ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        new MyQrCodeGeneratorActivity.CustomDialog(MyQrCodeGeneratorActivity.this, username,
                                "User checked-in successful", true).show();

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


    // call API for self checkin 18/07/2019
    boolean is_agenda_null;
    boolean isNumber;

    private void sendCheckinData() {
        String reqTag = CHECK_IN_REQ_TAG;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("appId", appDetails.getAppId());
            jsonObject.put("userIds", new JSONArray(Arrays.asList(userid.toArray())));
            jsonObject.put("checkin_date", new JSONArray(Arrays.asList(checkdate.toArray())));
            jsonObject.put("email_id", new JSONArray(Arrays.asList(email.toArray())));
            jsonObject.put("agenda_id", new JSONArray(Arrays.asList(agendaid.toArray())));
            System.out.println("Json Object" + jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjReq = VolleyUtils.createJsonPostReq(reqTag, checkInUrl, jsonObject,
                MyQrCodeGeneratorActivity.this);
        // Adding request to request queue
        jsonObjReq.setShouldCache(false);
        queue1.add(jsonObjReq);
    }


    @Override
    public void onOptionsMenuClosed(Menu menu) {
        super.onOptionsMenuClosed(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return true;
    }

    private String decodeStringValue(String decodestr) {
        String result = null;

        try {
            result = new String(Base64.decode(decodestr, Base64.NO_WRAP), "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        return result;
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
                Boolean selfsuccess = true;
                handleUser(selfsuccess);
                notifyTone(selfsuccess);

                Toast.makeText(this, Util.applyFontToMenuItem(this,
                        new SpannableString("Checkin Successfully")), Toast.LENGTH_SHORT).show();
               /* getUserDetails(usersUrl, true);
                if (file_write_permission) {
                    if (isPrintEnabled && agenda_id == 0)
                        try {
                            generatePDF(boolean_print_online);
                        } catch (DocumentException e1) {
                            e1.printStackTrace();
                        }
                } else
                    fileWritePermission(true);
*/
            } else if (response.getString("response").equals("error")) {
                if (response.getString("responseString").equals("Insufficient data.")) {

                } else {
                    Toast.makeText(this, "Backup was not successful", Toast.LENGTH_SHORT).show();

                }
            }
        }
    }

    @Override
    public void onVolleyError(String tag, VolleyError error) {
        /*if (swipeRefreshLayout.isRefreshing()) {
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
        VolleyLog.d(TAG, "Error: " + error);*/
        if ("com.android.volley.TimeoutError".equals(error.toString()))
            Toast.makeText(this, "Timeout Error", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, "You are Offline", Toast.LENGTH_SHORT).show();
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
            }, 2000);
        }
    }
}

