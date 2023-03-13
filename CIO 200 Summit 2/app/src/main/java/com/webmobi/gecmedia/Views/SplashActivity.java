package com.webmobi.gecmedia.Views;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
//import com.crashlytics.android.Crashlytics;
//import com.crashlytics.android.core.CrashlyticsCore;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.singleevent.sdk.App;
import com.singleevent.sdk.Custom_View.Error_Dialog;
import com.singleevent.sdk.Custom_View.VolleyErrorLis;
import com.webmobi.gecmedia.Config.ApiList;
import com.webmobi.gecmedia.Models.Event;
import com.webmobi.gecmedia.Models.MultiEvent;
import com.webmobi.gecmedia.R;
import com.webmobi.gecmedia.SingleEventHome;
import com.webmobi.gecmedia.WelcomeActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
//import io.fabric.sdk.android.Fabric;
import io.paperdb.Paper;

public class SplashActivity extends AppCompatActivity {


    File EventsDownloaded, PreviewDownloaded;
    String regId;
    Uri deepLink = null, deepEventID = null;
    String temp_eventid, event_id;
//    boolean apptype=false;
    boolean apptype=true; //changed
    boolean check_auth_for_login;
    ArrayList<MultiEvent> multieventlist;
    ArrayList<Event> catelist=new ArrayList<>();
    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Fabric.with(this, new Crashlytics());
        FirebaseApp.initializeApp(this);
//        CrashlyticsCore crashlyticsCore = new CrashlyticsCore.Builder()
//                .disabled(BuildConfig.DEBUG)
//                .build();
        //Fabric.with(this, new Crashlytics.Builder().core(crashlyticsCore).build());
        Paper.init(this);
        try{
            catelist.add(Paper.book().read("TotalEvent"));}catch (NullPointerException e)
        {
            e.printStackTrace();
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.act_splash);

        //intents for deeplink
        Intent deeplinkIntent = getIntent();
        if (deeplinkIntent != null) {
            //getting deeplink intent data
            deepEventID = deeplinkIntent.getData();
            if (deepEventID != null)
                temp_eventid = deepEventID.toString();
            else
                temp_eventid = "";
        } else {
            temp_eventid = "";
        }
        if(apptype) {
            Paper.book().write("AppType", "Branded");
        }
        loadjson();
        System.out.println("Splash event U RL : " + temp_eventid);
        event_id = temp_eventid.substring(temp_eventid.lastIndexOf("=") + 1);
        System.out.println("Splash event ID : " + event_id);
        //Creating a dir to Save the downloaded Event in EventsDownloaded Folder

        EventsDownloaded = new File(getFilesDir() + "/EventsDownload");
        PreviewDownloaded = new File(getFilesDir() + "/PreviewDownloaded");
        if (!EventsDownloaded.exists()) EventsDownloaded.mkdir();
        if (!PreviewDownloaded.exists()) PreviewDownloaded.mkdir();

        //for preview events to force update
        Paper.book().write("ForceUpdateForPreview", true);

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(SplashActivity.this, new OnSuccessListener<InstanceIdResult>(){
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String deviceToken = instanceIdResult.getToken();
                Paper.book().write("regId",deviceToken);
                Log.e("newToken", deviceToken);
                callingmain();


            }
        });

       if(regId!=null) {
           Paper.book().write("regId", regId);
       }

     //   getKeyHash();
        if (regId != null) {
            callingmain();
        }

        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter(ApiList.REGISTRATION_COMPLETE));


        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        // Get deep link from result (may be null if no link is found)

                        if (pendingDynamicLinkData != null) {
                            deepLink = pendingDynamicLinkData.getLink();
                            System.out.println("");
                        }
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println("Dynamic Link Error : " + e);
                    }
                });



    }
   public void loadjson() {
       String jsonUrl="https://webmobi.s3.amazonaws.com/nativeapps/46699/appData.json";
        String tag_string_req = "Updating";
        System.out.println("Url " + jsonUrl);
        StringRequest jsonRequest = new StringRequest(Request.Method.GET, jsonUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                getDataForPanel(response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError) {
                    Error_Dialog.show("Timeout", SplashActivity.this);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, SplashActivity.this), SplashActivity.this);
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show("Please Check Your Internet Connection", SplashActivity.this);
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

    private void getDataForPanel(String jsonFile) {
        try {
            JSONObject jsonObject = new JSONObject(jsonFile);
//            JSONObject jsonObject = new JSONObject(readJSON());
            JSONArray jsonArray = jsonObject.getJSONArray("disable_items");
            Log.d("jsonarray_check", "getDataForPanel: " + jsonArray.length());
            if (jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    String check_auth = jsonArray.getString(i);
                    if (check_auth.equals("disable_auth")) {
                        check_auth_for_login=false;
                        Paper.book().write("check_auth_for_login", check_auth_for_login);
                        Log.d("logindisable", "getDataForPanel: " + check_auth);
//                        Toast.makeText(SplashActivity.this, "logindisable", Toast.LENGTH_SHORT).show();
                        break;
                    }else{
                        check_auth_for_login=true;
                        Paper.book().write("check_auth_for_login", check_auth_for_login);
                        Log.d("logindisable", "getDataForPanel: " + false);
                    }
                }
            }

        } catch (Exception e) {

        }
    }

    public String readJSON() {
        String json = null;
        try {
            // Opening data.json file
            InputStream inputStream = getAssets().open("tabMenu.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            // read values in the byte array
            inputStream.read(buffer);
            inputStream.close();
            // convert byte to string
            json = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return json;
        }
        return json;
    }
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
           // Paper.book().write("regId",(intent.getStringExtra("token")));
            int appVersion = getAppVersion(context);
            Paper.book().read("appVersion", appVersion);
          //  callingmain();
        }
    };
    private void callingmain() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                regId = getRegistrationId(SplashActivity.this);
                if (!TextUtils.isEmpty(regId)) {
                    finishactivity();

                }
            }
        }, 1 * 2000); // wait for 2 seconds

    }
    private void finishactivity() {

        Intent sending;
        int upd=Paper.book().read("is_from_update",0);
        if (Paper.book().read("Islogin", false)) {
            if(catelist.size()>=1){
            try {

                if (catelist.size() != 1) {
                    sending = new Intent(SplashActivity.this, HomeScreen.class);
                    startActivity(sending);
                    finish();
                } else if (catelist.size() == 1) {
                    sending = new Intent(SplashActivity.this, SingleEventHome.class);
                    sending.putExtra("Engagement","");
                    sending.putExtra("back","APPS");
                    startActivity(sending);
                    finish();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }}
            else {
                if (upd != 1) {

                    if (!apptype) {
                        sending = new Intent(SplashActivity.this, HomeScreen.class);
                        startActivity(sending);
                        finish();
                    } else {
                        try {
                            multieventlist = new ArrayList<>();
                            multieventlist = Paper.book().read("MultiEvents");
                            int pos = 0;
                            if (multieventlist != null && multieventlist.size() > 0) {
                                for (int i = 0; i < multieventlist.size(); i++) {
                                    Log.d("check_event_name", "finishactivity: " + multieventlist.get(i).getEvent_name());
                                    if (multieventlist.get(i).getEvent_id().equalsIgnoreCase("a5d72f633c6f8a0e964525123a500376a26d")) {
                                        pos = i;
                                        Paper.book().write("Multieventpos", pos);
                                        Paper.book().write("MultiEvents", multieventlist);
                                    }
                                }

                            }
                            Intent i1;
                            i1 = new Intent(this, HomeScreenMulti.class);
                            i1.putExtra("Event_ID", "a5d72f633c6f8a0e964525123a500376a26d");
                            i1.putExtra("Event_Name", "GEC Media Group");
                            i1.putExtra("Event_Logo", "https://webmobi.s3.amazonaws.com/nativeapps/devwebmobitechconference2018/download__1_.jhttps://webmobi.s3.amazonaws.com/nativeapps/10105/1667577338751_Event_Logo_The_world_CIO_200_1024x1024px.pngpg");
                            i1.putExtra("Event_Theme", "F814A7");
                            i1.putExtra("Multi_event_logo", "https://webmobi.s3.amazonaws.com/nativeapps/devwebmobitechconference2018/download__1_.jhttps://webmobi.s3.amazonaws.com/nativeapps/10105/1667577338751_Event_Logo_The_world_CIO_200_1024x1024px.pngpg");
                            startActivity(i1);
                            finish();
                        } catch (Exception e) {

                        }
                    }

                } else {

                    logout();
                    Paper.book().write("is_from_update", 0);
                }
            }


        }else {
            sending = new Intent(SplashActivity.this, RegActivity.class);
//            sending.putExtra("enable_login",Paper.book().read("check_auth_for_login", true));
            startActivity(sending);
            finish();
        }
        if(!apptype) {
            Intent intent = new Intent(SplashActivity.this, WelcomeActivity.class);
            intent.putExtra("EVENT_ID", event_id);
            startActivity(intent);
            finish();
            startActivity(new Intent(SplashActivity.this, WelcomeActivity.class));
        }

    }
    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            Log.d("RegisterActivity",
                    "I never expected this! Going down, going down!" + e);
            throw new RuntimeException(e);
        }
    }
    private String getRegistrationId(Context context) {
        String registrationId = Paper.book().read("regId", "");
        if (registrationId.isEmpty()) {
            Log.i("DeviceID", "Registration not found.");
            return "";
        }
        return registrationId;
    }
    private void logout() {

        // final ProgressDialog dialog = new ProgressDialog(activity);
        //  dialog.setMessage("Logout...");
        //   dialog.show();
        String tag_string_req = "Logout";
        String url = com.singleevent.sdk.ApiList.Logout;
        StringRequest strReq = new StringRequest(com.android.volley.Request.Method.POST,
                url, new com.android.volley.Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                //   dialog.dismiss();
                try {
                    System.out.println(response);
                    JSONObject jObj = new JSONObject(response);

                    if (jObj.getBoolean("response")) {
                        Paper.book().write("Islogin",false);

                        // clearalllogin();
                        //clearApplicationData();
                        Intent i=new Intent(SplashActivity.this,RegActivity.class);
                        startActivity(i);


                        //   setResult(RESULT_OK);
                        finish();

                    } else {
                        if (jObj.getString("responseString").equalsIgnoreCase("Invalid token")) {

                            //  Error_Dialog.show("Session Expired, Please Login", SplashActivity.this);
                            Intent i=new Intent(SplashActivity.this,RegActivity.class);
                            startActivity(i);


                        } else
                            Error_Dialog.show(jObj.getString("responseString"), SplashActivity.this);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //  dialog.dismiss();
                if (error instanceof TimeoutError) {
                    Error_Dialog.show("Timeout", SplashActivity.this);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, SplashActivity.this), SplashActivity.this);
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show("Please Check Your Internet Connection", SplashActivity.this);
                }
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("deviceType", "android");
                params.put("deviceId", Paper.book().read("regId", ""));
                System.out.println(params);
                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("token", Paper.book().read("token",""));
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
    private void getKeyHash() {
        try {

            PackageInfo info =
                    getPackageManager().getPackageInfo("com.inq.serviceapp",

                            PackageManager.GET_SIGNATURES);

            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("MY KEY HASH:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
                System.out.println("MY KEY HASH:" + Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

}
