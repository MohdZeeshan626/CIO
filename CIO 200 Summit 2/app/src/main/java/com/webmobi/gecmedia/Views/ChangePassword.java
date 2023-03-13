package com.webmobi.gecmedia.Views;

import android.app.ProgressDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
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
import com.singleevent.sdk.Custom_View.VolleyErrorLis;
import com.webmobi.gecmedia.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.paperdb.Paper;

public class ChangePassword extends AppCompatActivity {
    Button done;
    EditText opwd, npwd, cpwd;
    String sopwd, snpwd, scpwd, UserID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        UserID = Paper.book().read("userId");

        //settingview

        done = (Button) findViewById(R.id.btn_profile_done);
        opwd = (EditText) findViewById(R.id.profile_et_old_pwd);
        npwd = (EditText) findViewById(R.id.profile_et_new_pwd);
        cpwd = (EditText) findViewById(R.id.profile_et_con_pwd);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkallfeilds())
                    changepwd();
            }
        });
    }
    private boolean checkallfeilds() {
        boolean flag = true;

        sopwd = opwd.getText().toString().trim();
        snpwd = npwd.getText().toString().trim();
        scpwd = cpwd.getText().toString().trim();


        if (sopwd.equals("")){
            flag = false;
            Error_Dialog.show("Please fill password ", ChangePassword.this);
        }else if (sopwd.length() < 4 ){
            flag = false;
            Error_Dialog.show("Password length should be minimum 4", ChangePassword.this);

        }

        if (snpwd.equals("")){
            flag = false;
            Error_Dialog.show("Please fill password ", ChangePassword.this);
        }else if (snpwd.length() < 4 ){
            flag = false;
            Error_Dialog.show("Password length should be minimum 4", ChangePassword.this);

        }

        if (scpwd.equals("")){
            flag = false;
            Error_Dialog.show("Please fill password ", ChangePassword.this);
        }else if (scpwd.length() < 4 ){
            flag = false;
            Error_Dialog.show("Password length should be minimum 4", ChangePassword.this);

        }
        if (!snpwd.equals(scpwd)) {
            flag = false;
            Error_Dialog.show("password and confirm password do not match", ChangePassword.this);
        }


        return flag;
    }


    private void changepwd() {

        final ProgressDialog dialog = new ProgressDialog(ChangePassword.this, com.singleevent.sdk.R.style.MyAlertDialogStyle);
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
                            Error_Dialog.show("Session is Expired Please Login", ChangePassword.this);


                        } else
                            Error_Dialog.show(jObj.getString("responseString"), ChangePassword.this);
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
                    Error_Dialog.show("Timeout", ChangePassword.this);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, ChangePassword.this), ChangePassword.this);
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show("Please Check Your Internet Connection", ChangePassword.this);
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
    @Override
    protected void onResume() {
        setTitle("CHANGE PASSWORD");
        super.onResume();
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
}
