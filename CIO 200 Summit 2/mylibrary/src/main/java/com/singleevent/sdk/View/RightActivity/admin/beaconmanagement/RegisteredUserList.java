package com.singleevent.sdk.View.RightActivity.admin.beaconmanagement;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;

import androidx.annotation.NonNull;

import androidx.core.app.ActivityCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
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
import android.widget.EditText;
import android.widget.TextView;
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
import com.singleevent.sdk.Custom_View.VolleyErrorLis;
import com.singleevent.sdk.model.AppDetails;
import com.singleevent.sdk.model.User_Details;
import com.singleevent.sdk.R;
import com.singleevent.sdk.View.RightActivity.admin.adminSurvey.UsersModel;
import com.singleevent.sdk.View.RightActivity.admin.beaconmanagement.adapter.AssignBeaconUserAdapter;
import com.singleevent.sdk.View.RightActivity.admin.checkin.SimpleScannerActivity;
import com.singleevent.sdk.utils.DataBaseStorage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import cyd.awesome.material.AwesomeText;
import io.paperdb.Paper;

public class RegisteredUserList extends AppCompatActivity implements View.OnClickListener {
    Toolbar toolbar;
    AppDetails appDetails;
    RecyclerView userlist_rv;
    AssignBeaconUserAdapter assignBeaconUserAdapter;
    List<UsersModel> userview;
    String token, theme_color;
    TextView noitems;
    AwesomeText qr_code_ic;
    EditText search_et;
    public static final int CAMERA_PERMISSION = 120;
    public static final int SCANNER_REQUEST_CODE = 220;
    ProgressDialog searchpDialog;
    SwipeRefreshLayout swipeRefreshLayout;
    List<String> assigned_beacon_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registered_user_list);
        appDetails = Paper.book().read("Appdetails");
        theme_color = appDetails.getTheme_color();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(Color.parseColor(theme_color));

        setSupportActionBar(toolbar);

        //setting the resources
        setResources();
        assigned_beacon_list = new ArrayList<>();
        assignBeaconUserAdapter = new AssignBeaconUserAdapter(RegisteredUserList.this, userview, theme_color);
        userlist_rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,
                false));
        userlist_rv.setAdapter(assignBeaconUserAdapter);

        //edittext for search
        search_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //updating the user lists
                filterUser(editable.toString());
            }
        });
    }

    //search filter
    private void filterUser(String s) {
        //shows the based on string from edit text
        List<UsersModel> temp = new ArrayList();
        for (UsersModel d : userview) {
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
            if (d.getFirst_name().toLowerCase().contains(s.toLowerCase())) {
                temp.add(d);
            }
        }
        //update recyclerview
        assignBeaconUserAdapter.updateList(temp);
    }

    private void setResources() {
        //setting the resources
        userview = new ArrayList<>();
        token = Paper.book().read("token", "");
        userlist_rv = (RecyclerView) findViewById(R.id.mb_ussrlist);
        noitems = (TextView) findViewById(R.id.mb_noitems);
        qr_code_ic = (AwesomeText) findViewById(R.id.mb_qrcode_ic);
        search_et = (EditText) findViewById(R.id.mb_search_et);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_reg_user);

        qr_code_ic.setTextColor(Color.parseColor(theme_color));
        qr_code_ic.setOnClickListener(this);


        //for swipe refresh
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (DataBaseStorage.isInternetConnectivity(RegisteredUserList.this)) {
                    if (token != null)
                        getuser();
                } else
                    swipeRefreshLayout.setRefreshing(false);
            }

        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        //setting the title
        SpannableString s = new SpannableString("Beacon Management");
        setTitle(Util.applyFontToMenuItem(this, s));
        getuser();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }

    private void getuser() {
        //getting user lists
        final ProgressDialog dialog = new ProgressDialog(this, R.style.MyAlertDialogStyle);
        dialog.setMessage("Loading the User");
        dialog.show();
        String tag_string_req = "getuser";
        String url = ApiList.Users + appDetails.getAppId() + "&admin_flag=attendee";
        StringRequest strReq = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                swipeRefreshLayout.setRefreshing(false);

                try {
                    System.out.println(response);
                    JSONObject jObj = new JSONObject(response);
                    System.out.println("Beacon User Lists : " + jObj.toString());

                    if (jObj.getBoolean("response")) {
                        parseuser(jObj.getJSONObject("responseString").getJSONArray("users"));
                    } else {
                        if (jObj.getString("responseString").equalsIgnoreCase("Invalid token")) {
                            Error_Dialog.show("Session is Expired Please Login", RegisteredUserList.this);


                        } else
                            Error_Dialog.show(jObj.getString("responseString"), RegisteredUserList.this);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    System.out.println("Error Msg " + e.getMessage());
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
               /* if (swiperefresh.isRefreshing()) {
                    swiperefresh.setRefreshing(false);
                }*/

                if (error instanceof TimeoutError) {
                    Error_Dialog.show("Timeout", RegisteredUserList.this);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, RegisteredUserList.this), RegisteredUserList.this);
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show("Please Check Your Internet Connection", RegisteredUserList.this);
                   /* if (!DataBaseStorage.isInternetConnectivity(RegisteredUserList.this)) {
//                        tv_checkInternet.setVisibility(View.VISIBLE);
                        userview = Paper.book(appDetails.getAppId()).read("BulkPirntUsers", new ArrayList<UsersModel>());
                        bulkPrintingUserAdapter = new BulkPrintingUserAdapter(RegisteredUserList.this, userview, theme_color, false);
                        userlist_rv.setAdapter(bulkPrintingUserAdapter);
                        setspinner();
                    }*/
                }

            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("token", token);
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


    private void parseuser(JSONArray responseString) {
        //parsing json response
        try {
            userview.clear();
            Gson gson = new Gson();

            Random r = new Random();

            for (int i = 0; i < responseString.length(); i++) {
                String eventString = responseString.getJSONObject(i).toString();
                UsersModel obj = gson.fromJson(eventString, UsersModel.class);
                obj.setColor(Color.argb(255, r.nextInt(256), r.nextInt(256), r.nextInt(256)));

                if (!Paper.book().read("userId", "").equals(obj.getUserid()))
                    userview.add(obj);
            }

            /*Shorting based in alphabet*/
            Collections.sort(userview, new Comparator<UsersModel>() {
                @Override
                public int compare(UsersModel usersModel, UsersModel t1) {
                    return usersModel.getFirst_name().compareToIgnoreCase(t1.getFirst_name());
                }


            });

//            Paper.book(appDetails.getAppId()).write("BulkPirntUsers", userview);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        if (userview.size() > 0) {
            assignBeaconUserAdapter.notifyDataSetChanged();
            assigned_beacon_list.clear();
            //getting assigned beacon list
            for (int i = 0; i < userview.size(); i++) {
                assigned_beacon_list.add(userview.get(i).getBeacon_id());
            }
            Paper.book().write("assigned_beacon", assigned_beacon_list);
        } else
            showview(false);


    }

    private void showview(boolean flag) {

        if (flag) {
            userlist_rv.setVisibility(View.VISIBLE);
            noitems.setVisibility(View.GONE);
        } else {
            userlist_rv.setVisibility(View.GONE);
            noitems.setVisibility(View.VISIBLE);
        }

    }


    //checking camera
    private void checkCameraPermission() {

        if (android.os.Build.VERSION.SDK_INT >= 23) {
            // only for marsemellow and newer versions
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION);
            } else {

                //Calling scanner activity
                Intent intent = new Intent(RegisteredUserList.this, SimpleScannerActivity.class);
                startActivityForResult(intent, SCANNER_REQUEST_CODE);
            }
        } else {

           //Calling scanner activity
            Intent intent = new Intent(RegisteredUserList.this, SimpleScannerActivity.class);
            startActivityForResult(intent, SCANNER_REQUEST_CODE);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SCANNER_REQUEST_CODE) {

            if (resultCode == RESULT_OK) {
                String id = data.getStringExtra("content");
                System.out.println("Content " + id);
                searchpDialog = new ProgressDialog(RegisteredUserList.this, R.style.MyAlertDialogStyle);
                searchpDialog.setMessage("Loading ...");
                searchpDialog.show();
                Getprofile(id);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAMERA_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    //Calling the scanner activity if permission granted
                    Intent intent = new Intent(RegisteredUserList.this, SimpleScannerActivity.class);
                    startActivityForResult(intent, SCANNER_REQUEST_CODE);

                } else {
                    Toast.makeText(this, "Please grant camera permission to use camera", Toast.LENGTH_SHORT).show();

                }
                break;
        }
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.mb_qrcode_ic) {
            checkCameraPermission();
        }
    }

    private void Getprofile(String value) {
        //getting the user profile
        if (searchpDialog.isShowing())
            searchpDialog.dismiss();
        final ProgressDialog pDialog = new ProgressDialog(RegisteredUserList.this, R.style.MyAlertDialogStyle);
        pDialog.setMessage("Loading ...");
        pDialog.show();


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
                        UsersModel usersModel = new UsersModel(user_details.getProfile_pic(), user_details.getFirst_name(), user_details.getLast_name(), user_details.getDesignation(), user_details.getCompany(), user_details.getEvent_user_id(), user_details.getEmail());
                        Bundle args = new Bundle();
                        args.putSerializable(BMUserDetails.USER_DETAILS, usersModel);
                        //Navigating to user details page
                        Intent i = new Intent(RegisteredUserList.this, BMUserDetails.class);
                        i.putExtras(args);
                        startActivity(i);
                    } else {
                        if (jObj.getString("responseString").equalsIgnoreCase("Invalid token")) {
                            Intent i = new Intent();
                            i.putExtra("keyMessage", "Session Expired, Please Login ");
                            i.putExtra("keyAlert", "Session Expired");
                            i.setClassName("com.webmobi.eventsapp", "com.webmobi.eventsapp.Views.TokenExpireAlertReceived");
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                        } else
                            Error_Dialog.show(jObj.getString("responseString"), RegisteredUserList.this);
                    }
                } catch (JSONException e) {


                }


            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                pDialog.dismiss();
                if (error instanceof TimeoutError) {
                    Error_Dialog.show("Timeout", RegisteredUserList.this);
                } else if (com.singleevent.sdk.Custom_View.VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(com.singleevent.sdk.Custom_View.VolleyErrorLis.handleServerError(error, RegisteredUserList.this), RegisteredUserList.this);
                } else if (com.singleevent.sdk.Custom_View.VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show("Please Check Your Internet Connection", RegisteredUserList.this);
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
}
