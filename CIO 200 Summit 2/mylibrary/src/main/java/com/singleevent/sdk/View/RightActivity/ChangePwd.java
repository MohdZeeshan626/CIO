package com.singleevent.sdk.View.RightActivity;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
import com.singleevent.sdk.Custom_View.Util;
import com.singleevent.sdk.Custom_View.VolleyErrorLis;
import com.singleevent.sdk.R;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cyd.awesome.material.AwesomeText;
import io.paperdb.Paper;

/**
 * Created by Admin on 6/19/2017.
 */

public class ChangePwd extends AppCompatActivity implements View.OnClickListener {


    AwesomeText  backwards;
    Button done;
    EditText opwd, npwd, cpwd;
    String sopwd, snpwd, scpwd, UserID;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Paper.init(this);
        setContentView(R.layout.s_changepwd);
        UserID = Paper.book().read("userId");

        //settingview

        done = (Button) findViewById(R.id.done);
        backwards = (AwesomeText) findViewById(R.id.backward);
        opwd = (EditText) findViewById(R.id.old_pwd);
        npwd = (EditText) findViewById(R.id.new_pwd);
        cpwd = (EditText) findViewById(R.id.con_pwd);
        backwards.setOnClickListener(this);
        done.setOnClickListener(this);
        done.setBackground(Util.setdrawable(ChangePwd.this, R.drawable.rectanglebackground, Color.parseColor("#0a6a99")));

        done.setEnabled(true);
    }


    private boolean checkallfeilds() {
        boolean flag = true;

        sopwd = opwd.getText().toString().trim();
        snpwd = npwd.getText().toString().trim();
        scpwd = cpwd.getText().toString().trim();


        if (sopwd.equals("")){
            flag = false;
            Error_Dialog.show("Please fill password ", ChangePwd.this);
        }else if (sopwd.length() < 4 ){
            flag = false;
            Error_Dialog.show("Password length should be minimum 4", ChangePwd.this);

        }

        if (snpwd.equals("")){
            flag = false;
            Error_Dialog.show("Please fill password ", ChangePwd.this);
        }else if (snpwd.length() < 4 ){
            flag = false;
            Error_Dialog.show("Password length should be minimum 4", ChangePwd.this);

        }

        if (scpwd.equals("")){
            flag = false;
            Error_Dialog.show("Please fill password ", ChangePwd.this);
        }else if (scpwd.length() < 4 ){
            flag = false;
            Error_Dialog.show("Password length should be minimum 4", ChangePwd.this);

        }
        if (!snpwd.equals(scpwd)) {
            flag = false;
            Error_Dialog.show("password and confirm password do not match", ChangePwd.this);
        }


        return flag;
    }


    @Override
    public void onClick(View view) {

        int i = view.getId();
        if (i == R.id.done) {
            if (checkallfeilds())
                changepwd();


        } else if (i == R.id.backward) {
            onBackPressed();

        }
    }

    private void changepwd() {

        final ProgressDialog dialog = new ProgressDialog(ChangePwd.this,R.style.MyAlertDialogStyle);
        dialog.setMessage("Updating Password...");
        dialog.show();
        String tag_string_req = "Password`";
        String url = ApiList.Chanepwd;
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
                        if (jObj.getString("responseString").equalsIgnoreCase("Invalid token")) {
                            Error_Dialog.show("Session is Expired Please Login", ChangePwd.this);


                        } else
                            Error_Dialog.show(jObj.getString("responseString"), ChangePwd.this);
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
                    Error_Dialog.show("Timeout", ChangePwd.this);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, ChangePwd.this), ChangePwd.this);
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show("Please Check Your Internet Connection", ChangePwd.this);
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userid", Paper.book().read("userId", ""));
                params.put("old_pass", sopwd);
                params.put("new_pass", snpwd);
                params.put("deviceId", Paper.book().read("regId", ""));
                System.out.println(params);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
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
