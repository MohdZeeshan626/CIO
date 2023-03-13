package com.webmobi.gecmedia.Views;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.singleevent.sdk.App;
import com.webmobi.gecmedia.Config.ApiList;
import com.singleevent.sdk.Custom_View.Error_Dialog;
import com.webmobi.gecmedia.CustomViews.TxtVCustomFonts;
import com.webmobi.gecmedia.CustomViews.VolleyErrorLis;
import com.webmobi.gecmedia.Models.MultiEvent;
import com.webmobi.gecmedia.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cyd.awesome.material.AwesomeText;
import cyd.awesome.material.FontCharacterMaps;
import io.paperdb.Paper;

/**
 * Created by Admin on 3/24/2017.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    AwesomeText backwards, passwordshow;
    Context context;
    EditText email, password;
    Button sigin;
    String semail, spassword;
    String regId;
    boolean showflag = false;
    TxtVCustomFonts forgotpassword;
    private ProgressDialog dialog;
    private List<String> savedirs;
    ProgressDialog pdialog;
    ArrayList<MultiEvent> multieventlist;
    private String eid="a5d72f633c6f8a0e964525123a500376a26d";
    JSONArray myevents;
    int pos = 0;
    SharedPreferences spf;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Paper.init(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.act_login);
        spf = getSharedPreferences(ApiList.LOCALSTORAGE,MODE_PRIVATE);

        context = this;
        regId = Paper.book().read("regId");
        String gm=Paper.book().read("usermail");
        backwards = (AwesomeText) findViewById(R.id.backward);
        passwordshow = (AwesomeText) findViewById(R.id.show);
        //   email = (EditText) findViewById(R.id.email);
        //   email.setText(gm);
        password = (EditText) findViewById(R.id.password);
        sigin = (Button) findViewById(R.id.sigin_in);
        //  forgotpassword = (TxtVCustomFonts) findViewById(R.id.forgotpassword);
        //     email.addTextChangedListener(watch);
        password.addTextChangedListener(watch);
        backwards.setOnClickListener(this);
        sigin.setOnClickListener(this);
        // forgotpassword.setOnClickListener(this);
        passwordshow.setOnClickListener(this);

    }



    TextWatcher watch = new TextWatcher() {

        @Override
        public void afterTextChanged(Editable arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onTextChanged(CharSequence s, int a, int b, int c) {


            if (checkallfeilds()) {
                sigin.setSelected(true);
                sigin.setEnabled(true);
            } else {
                sigin.setSelected(false);
                sigin.setEnabled(false);
            }

        }
    };


    private boolean checkallfeilds() {

        boolean flag = true;

        /*semail = email.getText().toString();*/
        semail=Paper.book().read("usermail");

        if (TextUtils.isEmpty(semail) || !Patterns.EMAIL_ADDRESS.matcher(semail).matches())
            flag = false;

        spassword = password.getText().toString().trim();
        try {
            if (spassword.length() == 6){
                Loginuser();
            }
            else{

            }
        }
        catch (Exception e)
        {}

/*
        if (spassword.length() < 4 ){
            flag = false;
         //   Error_Dialog.show("Password length should be minimum 4", LoginActivity.this);

        }*/


        return flag;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.sigin_in:
                Loginuser();
                break;

            case R.id.backward:
                onBackPressed();
                break;
            case R.id.show:
                if (showflag) {
                    password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    showflag = false;
                    passwordshow.setMaterialDesignIcon(FontCharacterMaps.MaterialDesign.MD_VISIBILITY);

                } else {
                    password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    showflag = true;
                    passwordshow.setMaterialDesignIcon(FontCharacterMaps.MaterialDesign.MD_VISIBILITY_OFF);
                }
                break;
            case R.id.forgotpassword:
                final Dialog dialog = new Dialog(LoginActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                View forgotview = inflater.inflate(R.layout.act_login_forgot, null, false);
                dialog.setCanceledOnTouchOutside(false);
                dialog.setContentView(forgotview);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                final EditText txtMessage = (EditText) dialog.findViewById(R.id.txt_dialog_message);
                Button submit = (Button) dialog.findViewById(R.id.btn_open_browser);
                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (txtMessage.getText().toString().length() > 0) {
                            if (validateemail(txtMessage.getText().toString())) {
                                recoverynewpassword(txtMessage.getText().toString());
                                dialog.dismiss();
                            }
                        } else {
                            email.setError("enter a valid email address");
                        }
                    }
                });


                // Display the dialog
                dialog.show();

                break;
        }

    }


    private void Loginuser() {

        dialog = new ProgressDialog(LoginActivity.this, R.style.MyAlertDialogStyle);
        dialog.setMessage("Login...");
        dialog.show();

        String tag_string_req = "Login";
//        String url = ApiList.Container_login;
        String url = ApiList.multievent_Email_login;
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                try {
                    System.out.println(response);
                    JSONObject jObj = new JSONObject(response);
                    if (jObj.getBoolean("response")) {

                        Paper.book().write("Email", semail);
                        Paper.book().write("email",semail);
                        Paper.book().write("Password", spassword);
                        Paper.book().write("Islogin", true);
                        Paper.book().write("Container_info",jObj.getJSONArray("event_app"));
                        Paper.book().write("Multi_info",jObj.getJSONArray("multi_event_app"));
                        Paper.book().write("token", jObj.getJSONObject("token_obj").getString("token"));
                        Paper.book().write("userId", jObj.getString("userid"));
                        Paper.book().write("username", jObj.getString("username"));

                        parsemulti(jObj.getJSONArray("multi_event_app"));

                        Log.d("check_event", "onResponse: "+jObj.getJSONArray("multi_event_app").get(0).toString());

                    } else {
                        Error_Dialog.show(jObj.getString("responseString"), LoginActivity.this);
                    }

                } catch (JSONException e) {
                    Error_Dialog.show(e.toString() ,LoginActivity.this);
                }

            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                DeviceLog("LOGINUSER",error.toString());
                if (error instanceof TimeoutError) {
                    Error_Dialog.show("Timeout", LoginActivity.this);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, context), LoginActivity.this);
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show("Please Check Your Internet Connection", LoginActivity.this);
                }
            }
        })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("event_id", eid);//changed
                params.put("email", semail);
                params.put("action", "verify");
                params.put("passkey", spassword);
                params.put("deviceId", regId);
                params.put("deviceType", "android");
                System.out.println(params);
                return params;
            }

           /* @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                // add headers <key,value>
                String credentials = Paper.book().read("usermail") + ":" + password.getText().toString();
                String auth = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                headers.put("Authorization", auth);
                return headers;
            }*/
        };


        App.getInstance().addToRequestQueue(strReq, tag_string_req);
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));


    }

// api for device log

    private void DeviceLog(String info,String error) {

        final String info1,error1;
        info1=info;
        error1=error;


        dialog = new ProgressDialog(LoginActivity.this, R.style.MyAlertDialogStyle);
        dialog.setMessage("checking...");
        dialog.show();


        String tag_string_req = "Login";
        String url = ApiList.DeviceLog;
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                try {
                    System.out.println(response);
                    JSONObject jObj = new JSONObject(response);


                    if (jObj.getBoolean("response")) {

                        finish();


                    } else {
                        Error_Dialog.show(jObj.getString("responseString"), LoginActivity.this);
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
                    Error_Dialog.show("Timeout", LoginActivity.this);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, context), LoginActivity.this);
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show("Please Check Your Internet Connection", LoginActivity.this);
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("appid", eid);
                params.put("userid", "");
                params.put("deviceType", "android");
                params.put("deviceId", regId);
                params.put("info", info1);
                params.put("log", error1);
                params.put("device_info", "");
                params.put("device_version", "");
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



    private boolean validateemail(String s) {
        boolean valid = true;
        if (s.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(s).matches()) {
            email.setError("enter a valid email address");
            valid = false;
        } else {
            email.setError(null);
        }
        return valid;
    }


    private void recoverynewpassword(final String s) {

        dialog = new ProgressDialog(LoginActivity.this);
        dialog.setMessage("Resetting your password");
        dialog.show();

        String tag_string_req = "Forgot";
        String url = ApiList.Forgot_Password;
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                Log.d("Login", "Response: " + response.toString());
                try {
                    JSONObject jObj = new JSONObject(response);
                    if (jObj.getBoolean("success")) {
                        Error_Dialog.show("Password Sent to Email!!!", LoginActivity.this);
                    } else {
                        Error_Dialog.show(jObj.getString("responseString"), LoginActivity.this);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                if (error instanceof NoConnectionError) {
                    Error_Dialog.show("Seems like you are offline at the moment. Please try again once you are connected to the internet", LoginActivity.this);
                } else {
                    Error_Dialog.show(error.getMessage(), LoginActivity.this);
                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", s);
                return params;
            }
        };
        App.getInstance().addToRequestQueue(strReq);
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
    }
    private void parsemulti(JSONArray events) {
        int n=events.length();
        multieventlist =new ArrayList<>();
        int pos=0;
        //multieventid=new ArrayList<>();

        try{
            Gson gson=new Gson();
            for (int i = 0; i < events.length(); i++) {
                String eventString = events.getJSONObject(i).toString();

                MultiEvent obj = gson.fromJson(eventString, MultiEvent.class);
                multieventlist.add(obj);

                //   MultiEvent obj = gson.fromJson(eventString, Event.class);
            }

            String s=Paper.book().read("AppType","");
            if(s.equalsIgnoreCase("Branded")){

                try {
                    if (multieventlist.size() > 0 && multieventlist != null) {
                        for (int i = 0; i < multieventlist.size(); i++) {
                            if (multieventlist.get(i).getEvent_id().equalsIgnoreCase("a5d72f633c6f8a0e964525123a500376a26d")) {
                                pos = i;
                                Paper.book().write("Multieventpos", pos);
                                Paper.book().write("MultiEvents", multieventlist);
                            }
                        }
                        Intent i1;
                        i1 = new Intent(context, HomeScreenMulti.class);
                        i1.putExtra("Event_ID", multieventlist.get(pos).getEvent_id());
                        i1.putExtra("Event_Name", multieventlist.get(pos).getEvent_name());
                        i1.putExtra("Event_Logo", multieventlist.get(pos).getApp_image());
                        i1.putExtra("Event_Theme", multieventlist.get(pos).getColor_code());
                        i1.putExtra("Multi_event_logo", multieventlist.get(pos).getMulti_event_logo());
                        startActivity(i1);
                        finish();
                    } else {
                        Intent i1;
                        i1 = new Intent(context, HomeScreenMulti.class);
                        i1.putExtra("Event_ID", "3eb6fbad52a7217a3a310a2f1b98aa1c1ede");
                        i1.putExtra("Event_Name", "WebMOBI CMO 2020");
                        i1.putExtra("Event_Logo", "");
                        i1.putExtra("Event_Theme", "");
                        i1.putExtra("Multi_event_logo", "");
                        startActivity(i1);
                        finish();
                    }
                }catch (Exception e){

                }
            }
            else{
                setResult(RESULT_OK);
                finish();
            }


        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
