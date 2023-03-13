package com.webmobi.gecmedia.Views;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.singleevent.sdk.App;
import com.singleevent.sdk.Custom_View.Error_Dialog;
import com.webmobi.gecmedia.Config.ApiList;
import com.webmobi.gecmedia.CustomViews.TxtVCustomFonts;
import com.webmobi.gecmedia.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Admin on 3/27/2017.
 */

public class Signup extends AppCompatActivity implements View.OnClickListener {

    ImageView backwards;
    EditText fname, lname, mnumber, email, s_password, f_password;
    String sfname, slname, smnumber, semail, spassword, fpassword;
    int loginresult = 9003;
    String showmsg;
    Context context;
    TxtVCustomFonts Login;
    TimeZone tz;
    TextView signup_btn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.act_reg);
        context = this;
        backwards = (ImageView) findViewById(R.id.backward);
        fname = (EditText) findViewById(R.id.fname);
        lname = (EditText) findViewById(R.id.lname);
        mnumber = (EditText) findViewById(R.id.mphone);
        email = (EditText) findViewById(R.id.temail);
        s_password = (EditText) findViewById(R.id.password2);
        f_password = (EditText) findViewById(R.id.password_one);
        signup_btn = (TextView) findViewById(R.id.signup_btn);

        backwards.setOnClickListener(this);
        signup_btn.setOnClickListener(this);
       /* fname.addTextChangedListener(watch);
        lname.addTextChangedListener(watch);
        mnumber.addTextChangedListener(watch);
        email.addTextChangedListener(watch);*/

//        checkallfeilds();
/*
        mViewFlipper.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
                if (mViewFlipper.getDisplayedChild() == 0)
                    checkallfeilds();
                else
                    checkpassword();

            }
        });*/

//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        Calendar cal = Calendar.getInstance();
        tz = cal.getTimeZone();


    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.backward:
                onBackPressed();
                break;
            case R.id.signup_btn:
                boolean isFieldChecked = checkallfeilds();
                boolean isPwdChecked = checkpassword();
                if (isFieldChecked && isPwdChecked)
                    registeruser();
                break;
            case R.id.login:
                dologin();
                break;
        }
    }

    private void dologin() {

        Intent i = new Intent(this, LoginActivity.class);
        startActivityForResult(i, loginresult);

    }

    public void dialogTOLogin() {


        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyAlertDialogStyle);
        builder.setMessage(R.string.dialog_ask_to_login)
                .setPositiveButton(R.string.fire, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!

                        dologin();


                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog

                    }
                });
        // Create the AlertDialog object and return it
        builder.create();
        builder.show();
    }


    /* TextWatcher watch = new TextWatcher() {

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


            *//* if (checkallfeilds()) {
                signup_btn.setEnabled(true);
            } else {
                signup_btn.setEnabled(false);
            }*//*

        }
    };
*/
    private boolean checkallfeilds() {

        boolean flag = true;

        sfname = fname.getText().toString();
        slname = lname.getText().toString();
        smnumber = mnumber.getText().toString();
        semail = email.getText().toString();
        fpassword = f_password.getText().toString();
        spassword = s_password.getText().toString();

        if (TextUtils.isEmpty(fpassword)) {
            f_password.setError("Please enter valid password");
            flag = false;
        }

        if (TextUtils.isEmpty(spassword)) {
            flag = false;
            s_password.setError("Please enter valid password");
        }
        if (TextUtils.isEmpty(semail) || !Patterns.EMAIL_ADDRESS.matcher(semail).matches()) {
            email.setError("Please enter valid MailID");
            flag = false;
        }


        if (TextUtils.isEmpty(sfname) || !sfname.matches("[a-zA-Z]*")) {
            fname.setError("Please enter valid name");
            flag = false;
        }

        if (TextUtils.isEmpty(slname) || !slname.matches("[a-zA-Z]*")) {
            lname.setError("Please enter valid name");
            flag = false;
        }

        if (TextUtils.isEmpty(smnumber) || !smnumber.matches("[0-9]{8,16}")) {
            mnumber.setError("Please enter valid number");
            flag = false;
        }


        return flag;


    }


    private boolean checkpassword() {

        boolean flag = true;
        fpassword = f_password.getText().toString();
        spassword = s_password.getText().toString();

        Pattern p = Pattern.compile("^[A-Za-z0-9\\\\\\\\!\\\"#$%&()*+,./:;<=>?@\\\\[\\\\]^_{|}~]{4,20}$");
        Matcher m1 = p.matcher(fpassword);
        Matcher m2 = p.matcher(spassword);

        if (!(fpassword.length() >= 4)) {
            flag = false;
            f_password.setError("Minimum 4 character required");
        }
        if (!(spassword.length() >= 4)) {
            flag = false;
            s_password.setError("Minimum 4 character required");
        }

        if (!m1.matches()) {
            flag = false;
            f_password.setError("Enter valid password");
        }
        if (!m2.matches()) {
            flag = false;
            s_password.setError("Enter valid password");
        }
        if (!spassword.equalsIgnoreCase(fpassword)) {
            flag = false;
            s_password.setError("Password mismatch");

        }


        return flag;
    }


    private void registeruser() {

        final ProgressDialog dialog = new ProgressDialog(Signup.this, R.style.MyAlertDialogStyle);
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

                        Error_Dialog.show("Registered Successfully,Please Verify Email", Signup.this);
                        dialogTOLogin();


                    } else {
                        Error_Dialog.show(jObj.getString("responseString"), Signup.this);
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
                params.put("password", s_password.getText().toString());
                params.put("mobile", mnumber.getText().toString());
                params.put("loginType", "general");
                params.put("timezone", tz.getDisplayName());
                params.put("userType", "registered");
                params.put("appType", "discovery");
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == loginresult) {
            setResult(RESULT_OK);
            finish();
        }
    }
}
