package com.singleevent.sdk.View.LeftActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.singleevent.sdk.ApiList;
import com.singleevent.sdk.App;
import com.singleevent.sdk.Custom_View.Error_Dialog;
import com.singleevent.sdk.Custom_View.TxtVCustomFonts;
import com.singleevent.sdk.Custom_View.Util;
import com.singleevent.sdk.model.AppDetails;
import com.singleevent.sdk.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import cyd.awesome.material.AwesomeText;
import io.paperdb.Paper;

/**
 * Created by Admin on 6/8/2017.
 */

public class Register extends AppCompatActivity implements View.OnClickListener {

    AppDetails appDetails;
    AwesomeText backwards;
    EditText fname, lname, email, jpos, company, mnumber;
    String sfname, slname, semail, smnumber, sjpos, scompany;
    Context context;
    TxtVCustomFonts Login;
    TimeZone tz;
    String regId;
    Button signup;
    private ProgressDialog dialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Paper.init(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.s_register);
        appDetails = Paper.book().read("Appdetails");
        regId = Paper.book().read("regId");
        context = this;
        backwards = (AwesomeText) findViewById(R.id.backward);
        Login = (TxtVCustomFonts) findViewById(R.id.login);
        fname = (EditText) findViewById(R.id.fname);
        lname = (EditText) findViewById(R.id.lname);
        email = (EditText) findViewById(R.id.temail);
        jpos = (EditText) findViewById(R.id.jobpos);
        mnumber = (EditText) findViewById(R.id.mphone);
        company = (EditText) findViewById(R.id.company);
        signup = (Button) findViewById(R.id.signup);
        backwards.setOnClickListener(this);
        signup.setOnClickListener(this);
        signup.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));
        Login.setOnClickListener(this);

        Calendar cal = Calendar.getInstance();
        tz = cal.getTimeZone();


    }

    @Override
    public void onClick(View view) {


        int i = view.getId();
        if (i == R.id.signup) {
            if (checkallfeilds()) {
                Registeruser();
            }

        } else if (i == R.id.backward) {
            onBackPressed();

        } else if (i == R.id.login) {
            finish();

        }

    }


    private boolean checkallfeilds() {

        boolean flag = true;

        sfname = fname.getText().toString();
        slname = lname.getText().toString();
        semail = email.getText().toString();
        sjpos = jpos.getText().toString();
        scompany = company.getText().toString();
        smnumber = mnumber.getText().toString();

        if (TextUtils.isEmpty(semail) || !Patterns.EMAIL_ADDRESS.matcher(semail).matches()) {
            flag = false;
            Error_Dialog.show("Enter the Valid Email", Register.this);

        }

        if (TextUtils.isEmpty(sfname) || !sfname.matches("[a-zA-Z]*")) {
            flag = false;
            Error_Dialog.show("Please Valid First Name", Register.this);
        }

        if (TextUtils.isEmpty(slname) || !slname.matches("[a-zA-Z]*")) {
            flag = false;
            Error_Dialog.show("Please Valid Last Name", Register.this);
        }


        if (TextUtils.isEmpty(sjpos) || !sjpos.matches("[a-zA-Z0-9_\\s]+")) {
            flag = false;
            Error_Dialog.show("Please Valid Designation", Register.this);
        }

        if (TextUtils.isEmpty(smnumber) || !smnumber.matches("[0-9]*")) {
            flag = false;
            Error_Dialog.show("Please Valid Mobile Number", Register.this);
        }

        if (TextUtils.isEmpty(scompany)) {
            flag = false;
            Error_Dialog.show("Please Valid Comapany Name", Register.this);
        }


        return flag;


    }


    private void Registeruser() {

        dialog = new ProgressDialog(Register.this,R.style.MyAlertDialogStyle);
        dialog.setMessage("Registering User");
        dialog.show();


        String tag_string_req = "Login";
        String url = ApiList.Register_User;
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                try {
                    dialog.dismiss();
                    System.out.println(response);
                    JSONObject jObj = new JSONObject(response);


                    if (jObj.getBoolean("response")) {

                        showDialog(jObj.getJSONObject("responseString").getString("message"), "Registration  Notifications");

                    } else {
                        Error_Dialog.show(jObj.getString("responseString"), Register.this);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("firstname", fname.getText().toString());
                params.put("lastname", lname.getText().toString());
                params.put("email", email.getText().toString());
                params.put("appid", appDetails.getAppId());
                params.put("info_privacy", String.valueOf(appDetails.getInfo_privacy()));
                params.put("timezone", tz.getDisplayName());
                params.put("userType", "verified");
                params.put("company", scompany);
                params.put("designation", sjpos);
                params.put("phone", smnumber);
                params.put("deviceId", regId);
                params.put("deviceType", "android");
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




    public void showDialog(String msg, String title) {

        final Dialog alertdialog = new Dialog(context, android.R.style.Theme_Translucent);
        alertdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertdialog.setCancelable(false);
        alertdialog.setContentView(R.layout.alert_dialog);
        RelativeLayout rl = (RelativeLayout) alertdialog.findViewById(R.id.rl);
        rl.setBackground(Util.setshape(Color.parseColor(appDetails.getTheme_color()), Color.parseColor("#ffffff")));
        final Button btnCancel = (Button) alertdialog.findViewById(R.id.btncancel);
        final Button btnview = (Button) alertdialog.findViewById(R.id.btnview);
        final TextView tvTitle = (TextView) alertdialog.findViewById(R.id.tvTitle);
        final TextView tvMessage = (TextView) alertdialog.findViewById(R.id.tvMessage);
        tvTitle.setTypeface(Util.boldtypeface(context));
        tvMessage.setTypeface(Util.lighttypeface(context));

        tvMessage.setVisibility(View.INVISIBLE);
        tvTitle.setVisibility(View.INVISIBLE);
        btnCancel.setVisibility(View.INVISIBLE);
        btnview.setVisibility(View.INVISIBLE);

        tvMessage.setText(msg);
        tvTitle.setText(title);

        btnCancel.setText("OK");


        Animation scale_up = AnimationUtils.loadAnimation(context, R.anim.layout_zoom_open);

        scale_up.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                tvMessage.setVisibility(View.VISIBLE);
                tvTitle.setVisibility(View.VISIBLE);
                btnview.setVisibility(View.GONE);
                btnCancel.setVisibility(View.VISIBLE);


            }
        });


        rl.setAnimation(scale_up);

        btnCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {

                        finish();

                    }
                }, 100);
                alertdialog.dismiss();


            }
        });

        btnview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertdialog.dismiss();

            }
        });

        alertdialog.show();
    }


}
