package com.singleevent.sdk.View.LeftActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
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
import com.singleevent.sdk.model.Events;
import com.singleevent.sdk.model.User_Details;
import com.singleevent.sdk.R;
import com.singleevent.sdk.utils.DataBaseStorage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cyd.awesome.material.AwesomeText;
import io.paperdb.Paper;

/**
 * Created by Admin on 6/7/2017.
 */

public class Contact_Us extends AppCompatActivity implements View.OnClickListener {

    AppDetails appDetails;
    int pos;
    private String title;
    private ArrayList<Events> events = new ArrayList<Events>();
    Events e;
    RelativeLayout h1;
    private float navdpWidth;
    TextView emailaddress, phonenumber;
    String toemail, phonemuber, UserID;
    AwesomeText close/*, send*/;
    Button send;
    EditText name, email, msg, pnumber;
    double width, height;
    Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Paper.init(this);
        setContentView(R.layout.s_contactus);
        context = this;
        appDetails = Paper.book().read("Appdetails");
        UserID = Paper.book().read("userId", "");

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        width = metrics.widthPixels * 0.50;
        height = (width) * (3 / 5);
        // getting the data from previous activity

        if (getIntent().getExtras() == null)
            finish();

        pos = getIntent().getExtras().getInt("pos");
        title = getIntent().getExtras().getString("title");
        events = Paper.book().read("Appevents");
        e = events.get(0);
        toemail = e.getTabs(pos).getToEmail();

        //toemail="ashish@mendios.com";
        phonemuber = e.getTabs(pos).getPhone();
        // setting view

        h1 = (RelativeLayout) findViewById(R.id.h1);
        navdpWidth = getResources().getDimensionPixelSize(R.dimen.abc_action_bar_default_height_material) * 5;
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) navdpWidth);
        h1.setLayoutParams(params);

        emailaddress = (TextView) findViewById(R.id.emailaddress);
        phonenumber = (TextView) findViewById(R.id.phonenumber);
        close = (AwesomeText) findViewById(R.id.backward);
        name = (EditText) findViewById(R.id.name);
        email = (EditText) findViewById(R.id.email);
        msg = (EditText) findViewById(R.id.msg);
        pnumber = (EditText) findViewById(R.id.pnumber);
        send = (Button) findViewById(R.id.send);
        emailaddress.setText(toemail);
        emailaddress.setTypeface(Util.regulartypeface(this));
        phonenumber.setTypeface(Util.regulartypeface(this));
        phonenumber.setText(phonemuber);
        //phonenumber.setText("9696576868");
        h1.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));
        send.setBackground(Util.setdrawable(Contact_Us.this, R.drawable.healthpostbut, Color.parseColor(appDetails.getTheme_color())));
        //send.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));
        close.setOnClickListener(this);
        send.setOnClickListener(this);
        phonenumber.setOnClickListener(this);
        emailaddress.setOnClickListener(this);

        getProfile();
        if (!DataBaseStorage.isInternetConnectivity(this)){

            User_Details u_detils = Paper.book().read("UserDetails");
            if (u_detils!=null)
                setdata(u_detils);
        }

    }

    @Override
    public void onClick(View view) {


        int i = view.getId();
        if (i == R.id.backward) {
            finish();

        } else if (i == R.id.send) {
            if (checkallfeilds()) {
                Feebback();
            } else
                Error_Dialog.show("Please fill the details", Contact_Us.this);

        }else if (i==R.id.phonenumber){
            callUs(phonenumber.getText().toString());
        }else if (i==R.id.emailaddress){
            openGmail(emailaddress.getText().toString());
        }
    }

    private void openGmail(String email) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto",email, null));
       /* emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Body");*/
        startActivity(Intent.createChooser(emailIntent, "Send email..."));

    }


    private boolean checkallfeilds() {

        boolean flag = true;

        String sfname = name.getText().toString();
        String semail = email.getText().toString();
        String smsg = msg.getText().toString();
        String sphone = pnumber.getText().toString();

        if (TextUtils.isEmpty(sfname) || !sfname.matches("[a-zA-Z]*")) {
            flag = false;
            Error_Dialog.show("Please Valid First Name", Contact_Us.this);
        } else if (TextUtils.isEmpty(semail) || !Patterns.EMAIL_ADDRESS.matcher(semail).matches()) {
            flag = false;
            Error_Dialog.show("Enter the Valid Email", Contact_Us.this);
        } else if (TextUtils.isEmpty(smsg)) {
            flag = false;
            Error_Dialog.show("Enter the Message", Contact_Us.this);
        } else if (TextUtils.isEmpty(sphone)) {
            flag = false;
            Error_Dialog.show("Enter the Valid Phone", Contact_Us.this);
        }

        return flag;


    }

    @Override
    protected void onResume() {
        super.onResume();

    }


    private void Feebback() {


        final ProgressDialog dialog = new ProgressDialog(Contact_Us.this, R.style.MyAlertDialogStyle);
        dialog.setMessage("Sending Feedback..");
        dialog.show();
        String tag_string_req = "Contact_Us";
        String url = ApiList.Contact_Us;
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                try {
                    System.out.println(response);
                    JSONObject jObj = new JSONObject(response);


                    if (jObj.getBoolean("response")) {
                        Error_Dialog.show(jObj.getString("responseString"), Contact_Us.this);
                        new CountDownTimer(2000, 1000) {

                            public void onTick(long millisUntilFinished) {
                            }

                            public void onFinish() {
                                finish();
                            }
                        }.start();


                    } else {
                        Error_Dialog.show(jObj.getString("responseString"), Contact_Us.this);
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
                    Error_Dialog.show("Timeout", Contact_Us.this);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, context), Contact_Us.this);
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show("Please Check Your Internet Connection", Contact_Us.this);
                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", name.getText().toString());
                params.put("email", email.getText().toString());
                params.put("message", msg.getText().toString());
                params.put("to_email", toemail);
                params.put("phone", pnumber.getText().toString());
                params.put("appname", appDetails.getAppName());
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

    //get userdetails
    private void getProfile() {


        final ProgressDialog pDialog = new ProgressDialog(Contact_Us.this);
        pDialog.setMessage("Loading ...");
        pDialog.show();


        String tag_string_req = "Login";
        String url = ApiList.GetProfile + UserID;
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
                        Paper.book().write("UserDetails",user_details);
                        setdata(user_details);
                    } else {
                        if (jObj.getString("responseString").equalsIgnoreCase("Invalid token")) {
                            Intent i = new Intent();
                            i.putExtra("keyMessage", "Session Expired, Please Login ");
                            i.putExtra("keyAlert", "Session Expired");
                            i.setClassName(getPackageName(), getPackageName() + ".Views.TokenExpireAlertReceived");
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);

                        } /*else
                            Error_Dialog.show(jObj.getString("responseString"), Contact_Us.this);
                    */
                    }
                } catch (JSONException e) {


                }


            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                pDialog.dismiss();
                if (error instanceof TimeoutError) {
                    Error_Dialog.show("Timeout", Contact_Us.this);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    // Error_Dialog.show(VolleyErrorLis.handleServerError(error, Contact_Us.this), Contact_Us.this);
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    Toast.makeText(context,new SpannableString("Please Check Your Internet Connection"),
                            Toast.LENGTH_SHORT).show();
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

    private void setdata(User_Details user_details) {

        name.setText(user_details.getFirst_name());
        email.setText(user_details.getEmail());
        pnumber.setText(user_details.getMobile());
        msg.setText(user_details.getMessege());

    }

    //call to particular number
    private void callUs(String MOB ) {
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:"+MOB ));
        startActivity(callIntent);
    }

}
