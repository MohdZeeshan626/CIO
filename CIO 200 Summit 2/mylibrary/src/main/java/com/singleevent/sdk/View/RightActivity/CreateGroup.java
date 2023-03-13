package com.singleevent.sdk.View.RightActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.singleevent.sdk.ApiList;
import com.singleevent.sdk.App;
import com.singleevent.sdk.Custom_View.Error_Dialog;
import com.singleevent.sdk.Custom_View.VolleyErrorLis;
import com.singleevent.sdk.model.AppDetails;
import com.singleevent.sdk.R;
import com.singleevent.sdk.View.RightActivity.group_feed.Privategroup;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cyd.awesome.material.AwesomeText;
import io.paperdb.Paper;
public class CreateGroup  extends AppCompatActivity implements View.OnClickListener{

    int mode;
    EditText ed_group_name;
    Button savetxt;
    String eid="84bd3ad16b94dab13393e61e261abdea8bc7";
    String regId;
    RadioButton user_check,user_check1;
    String groupname,grouptype,groupprivacy;
    AppDetails appDetails;
    AwesomeText close;
    RadioGroup radio1,radio2;
    ProgressDialog dialog;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Paper.init(this);
        appDetails = Paper.book().read("Appdetails");
        setContentView(R.layout.create_group);

        ed_group_name=(EditText)findViewById(R.id.ed_group_name);
        savetxt=(Button)findViewById(R.id.savetxt);
        user_check=(RadioButton) findViewById(R.id.user_check);
        user_check1=(RadioButton) findViewById(R.id.user_check1);
        close=(AwesomeText)findViewById(R.id.gclose);
        radio1=(RadioGroup)findViewById(R.id.radio1);
        radio2=(RadioGroup)findViewById(R.id.radio2 );
        close.setOnClickListener(this);
        regId = Paper.book().read("regId");

        if (Build.VERSION.SDK_INT >= 21) {

            ColorStateList colorStateList = new ColorStateList(
                    new int[][]{

                            new int[]{-android.R.attr.state_enabled}, //disabled
                            new int[]{android.R.attr.state_enabled} //enabled
                    },
                    new int[]{

                            Color.parseColor(appDetails.getTheme_color()) //disabled
                            , Color.parseColor(appDetails.getTheme_selected()) //enabled

                    }
            );


            user_check.setButtonTintList(colorStateList);//set the color tint list
            user_check.invalidate(); //could not be necessary
            user_check1.setButtonTintList(colorStateList);
            user_check1.invalidate();

        }

        savetxt.setOnClickListener(this);

    }
    @Override
    public void onClick(View view) {


        if(view.getId()==R.id.gclose)
        {
          onBackPressed();
        }

       /* if (view.getId()==R.id.user_check) {
            if (checked) {
                int selectedId = radio1.getCheckedRadioButtonId();
                user_check1.setEnabled(false);


                groupprivacy = "public";
                mode = 0;
            }
        }
        if(view.getId()==R.id.user_check1)
        {
            if(checked) {
                radio1.setClickable(false);
                groupprivacy = "private";
                user_check.setEnabled(false);
                mode = 1;
            }
        }*/
        if(view.getId()==R.id.savetxt);
        {
            if(ed_group_name!=null){
                groupname=ed_group_name.getText().toString();
            }
            if(mode==1){
                createGroup(1);

            }
            else {

                createGroup(0);
            }
            // Toast.makeText(CreateGroup.this,"Group Created",Toast.LENGTH_LONG).show();
        }
    }


    public void onDeliveryScheduleRadioButton(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        int id = view.getId();
        if (id == R.id.user_check) {
            if (checked) {
                groupprivacy = "public";
                mode = 0;
               // user_check.setEnabled(false);
              //  user_check1.setEnabled(true);
                radio2.clearCheck();
                user_check.setChecked(true);
                user_check1.setChecked(false);

            }
        } else if (id == R.id.user_check1) {
            if (checked) {
                groupprivacy = "private";
                mode = 1;
               // user_check1.setEnabled(false);
               // user_check.setEnabled(true);
                radio1.clearCheck();
                user_check.setChecked(false);
                user_check1.setChecked(true);

            }
        }
    }


    private void createGroup(int mode)
    {
try {

    int privacy = mode;
    // converting arraylist to jsonarray


/*
        Gson gson = new GsonBuilder().create();
        final JsonArray myCustomArray = gson.toJsonTree(list).getAsJsonArray();*/

     dialog = new ProgressDialog(CreateGroup.this, R.style.MyAlertDialogStyle);

    //dialog.setMessage(msg);
    try {
        dialog.dismiss();
    }catch (Exception e){}
    String tag_string_req = "Creat Group";
    String url = "https://chat.webmobi.com/create_group";
    StringRequest strReq = new StringRequest(Request.Method.POST,
            url, new Response.Listener<String>() {


        @Override
        public void onResponse(String response) {

            try {

                DeviceLog("Group feed", "grou feed create api calling");
                JSONObject jObj = new JSONObject(response);


                if (jObj.getBoolean("response")) {
                   try {
                       dialog.dismiss();
                   }catch (Exception e){}
                    //   Error_Dialog.show(jObj.getString("responseString"), CreateGroup.this);
                    String groupid = jObj.getJSONObject("responseString").getString("group_id");
                    if (privacy == 1) {
                        Intent i = new Intent(CreateGroup.this, Privategroup.class);
                        i.putExtra("group_id", groupid);
                        startActivity(i);
                    } else {
                        Intent i = new Intent(CreateGroup.this, Group_feed.class);
                        startActivity(i);
                        finish();

                    }

                } else {
                    if (jObj.getString("responseString").equalsIgnoreCase("Invalid token")) {
                        Intent i = new Intent();
                        i.putExtra("keyMessage", "Session Expired, Please Login ");
                        i.putExtra("keyAlert", "Session Expired");
                        i.setClassName(getPackageName(), getPackageName() + ".SessionExpired");
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);

                    } else
                        Error_Dialog.show(jObj.getString("responseString"), CreateGroup.this);
                }

            } catch (JSONException e) {
                dialog.dismiss();
                e.printStackTrace();
            }

        }
    }, new Response.ErrorListener() {

        @Override
        public void onErrorResponse(VolleyError error) {
            try {
                dialog.dismiss();
            }catch (Exception e){}
            Paper.book().write("Sync", true);
            if (error instanceof TimeoutError) {
                Error_Dialog.show("Timeout", CreateGroup.this);
            } else if (VolleyErrorLis.isServerProblem(error)) {
                Error_Dialog.show(VolleyErrorLis.handleServerError(error, CreateGroup.this), CreateGroup.this);
            } else if (VolleyErrorLis.isNetworkProblem(error)) {
                Error_Dialog.show("Please Check Your Internet Connection", CreateGroup.this);
            }

        }
    }) {
        @Override
        protected Map<String, String> getParams() {
            Map<String, String> params = new HashMap<String, String>();
            params.put("user_id", Paper.book().read("userId"));
            params.put("group_name", groupname);
            params.put("group_type", "feed");
            params.put("group_privacy", groupprivacy);
            params.put("appid", appDetails.getAppId());
            return params;
        }

        @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            Map<String, String> headers = new HashMap<String, String>();
            headers.put("token", Paper.book().read("token", ""));
            return headers;
        }
    };


    strReq.setShouldCache(false);
    App.getInstance().addToRequestQueue(strReq, tag_string_req);
    strReq.setRetryPolicy(new DefaultRetryPolicy(
            500000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
    ));
}catch (Exception e)
{

}

    }
    private void DeviceLog(String info,String error) {
        try {
            String info1, error1;
            info1 = info;
            error1 = error;




            String tag_string_req = "Login";
            String url = ApiList.DeviceLog;
            StringRequest strReq = new StringRequest(Request.Method.POST,
                    url, new Response.Listener<String>() {


                @Override
                public void onResponse(String response) {

                    try {
                        System.out.println(response);
                        JSONObject jObj = new JSONObject(response);


                        if (jObj.getBoolean("response")) {


                        } else {
                            Error_Dialog.show(jObj.getString("responseString"), CreateGroup.this);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {

                    if (error instanceof TimeoutError) {
                        Error_Dialog.show("Timeout", CreateGroup.this);
                    } else if (VolleyErrorLis.isServerProblem(error)) {
                        Error_Dialog.show(VolleyErrorLis.handleServerError(error, CreateGroup.this), CreateGroup.this);
                    } else if (VolleyErrorLis.isNetworkProblem(error)) {
                        Error_Dialog.show("Please Check Your Internet Connection", CreateGroup.this);
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("appid", Paper.book().read("Current_Event_Id", ""));
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


        }catch (Exception e){

        }
    }
    @Override
    public void onPause() {
        super.onPause();

        if ((dialog != null) && dialog.isShowing())
            dialog.dismiss();
        dialog = null;
    }

}
