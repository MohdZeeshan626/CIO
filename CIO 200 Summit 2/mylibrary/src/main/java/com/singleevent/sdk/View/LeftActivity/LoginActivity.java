package com.singleevent.sdk.View.LeftActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.ImageView;
import android.widget.TextView;

/*import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;*/
//import com.android.volley.Request;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.singleevent.sdk.ApiList;
import com.singleevent.sdk.App;
import com.singleevent.sdk.Custom_View.Error_Dialog;
import com.singleevent.sdk.Custom_View.TxtVCustomFonts;
import com.singleevent.sdk.Custom_View.VolleyErrorLis;
;
import com.singleevent.sdk.model.Event;
import com.singleevent.sdk.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cyd.awesome.material.AwesomeText;
import cyd.awesome.material.FontCharacterMaps;
import io.paperdb.Paper;
/*import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;*/

/**
 * Created by Admin on 3/24/2017.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    AwesomeText passwordshow;
    ImageView backwards;
    Context context;
    EditText email, password;
    TextView sigin;
    String semail, spassword;
    String regId;
    boolean showflag = true;
    private TxtVCustomFonts forgotpassword;
    private ProgressDialog dialog;
    String baseUrl = "https://webmobi.s3.amazonaws.com/nativeapps/";
    String filename = "app.json";
    String dir;
    File eventDir, jsonFile, descFile;
    private List<String> savedirs;
    ProgressDialog pdialog;
    JSONArray myevents;
    int pos = 0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Paper.init(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        context = this;
        regId = Paper.book().read("regId");
        backwards = (ImageView) findViewById(R.id.backward);
        passwordshow = (AwesomeText) findViewById(R.id.show);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        sigin = (TextView) findViewById(R.id.sigin_in);
        forgotpassword = (TxtVCustomFonts) findViewById(R.id.forgotpassword);
        email.addTextChangedListener(watch);
        password.addTextChangedListener(watch);
        backwards.setOnClickListener(this);
        sigin.setOnClickListener(this);
        forgotpassword.setOnClickListener(this);
        passwordshow.setOnClickListener(this);
        dir = getFilesDir() + File.separator + "EventsDownload" + File.separator;
        savedirs = new ArrayList<>();
        savedirs = getSaveDirs(dir);

    }

    private List<String> getSaveDirs(String dir) {
        List<String> paths = new ArrayList<>();
        File directory = new File(dir);

        File[] files = directory.listFiles();

        for (File file : files) {
            if (file.isDirectory())
                paths.add(file.getName());
        }
        return paths;
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
//                sigin.setEnabled(true);
            } else {
                sigin.setSelected(false);
//                sigin.setEnabled(false);
            }

        }
    };


    private boolean checkallfeilds() {

        boolean flag = true;

        semail = email.getText().toString();

        if (TextUtils.isEmpty(semail) || !Patterns.EMAIL_ADDRESS.matcher(semail).matches())
            flag = false;

        spassword = password.getText().toString();
        if (spassword.length() < 4)
            flag = false;

        return flag;


    }


    private boolean checkbeforelogin() {

        boolean flag = true;

        semail = email.getText().toString();

        if (TextUtils.isEmpty(semail) || !Patterns.EMAIL_ADDRESS.matcher(semail).matches()) {
            flag = false;
            Error_Dialog.show("Enter the Valid Email", LoginActivity.this);

        } else {
            spassword = password.getText().toString().trim();
            if (spassword.equals("")) {
                flag = false;
                Error_Dialog.show("Please fill password ", LoginActivity.this);
            }
            if (spassword.length() < 4) {
                flag = false;
                Error_Dialog.show("Password length should be minimum 4", LoginActivity.this);

            }
        }

        return flag;


    }


    @Override
    public void onClick(View view) {

        int i = view.getId();
        if (i == R.id.sigin_in) {
            if (checkbeforelogin())
                Loginuser();


        } else if (i == R.id.backward) {
            onBackPressed();

        } else if (i == R.id.show) {
            if (showflag) {
                password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                showflag = false;
                passwordshow.setMaterialDesignIcon(FontCharacterMaps.MaterialDesign.MD_VISIBILITY);

            } else {
                password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                showflag = true;
                passwordshow.setMaterialDesignIcon(FontCharacterMaps.MaterialDesign.MD_VISIBILITY_OFF);
            }

        } else if (i == R.id.forgotpassword) {
            final Dialog dialog = new Dialog(LoginActivity.this);
            LayoutInflater inflater = getLayoutInflater();
            View forgotview = inflater.inflate(R.layout.activity_login_forget, null, false);
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


        }

    }

    /*  OkHttp Request API call for login
        After successfull login downloads and stores the user detail
     */

    private void Loginuser() {

        dialog = new ProgressDialog(LoginActivity.this, R.style.MyAlertDialogStyle);
        dialog.setMessage("Login...");
        dialog.show();

        String tag_string_req = "Login";
        String url = ApiList.Multi_Event_Login_User;
        StringRequest strReq = new StringRequest(com.android.volley.Request.Method.POST,
                url, new com.android.volley.Response.Listener<String>() {


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
                        //   Paper.book().write("token", jObj.getJSONObject("responseString").getString("token"));
                        Paper.book().write("userId", jObj.getString("userid"));
                        Paper.book().write("username", jObj.getString("username"));
                        setResult(RESULT_OK);
                        finish();



                    } else {
                        Error_Dialog.show(jObj.getString("responseString"), LoginActivity.this);
                    }

                } catch (JSONException e) {
                    Error_Dialog.show(e.toString() ,LoginActivity.this);
                }

            }

        }, new com.android.volley.Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
               // DeviceLog("LOGINUSER",error.toString());
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
                //params.put("event_id", eid);

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

    /*  Recover Password API Call
        send recovery mail to the entered mail id
     */
    private void recoverynewpassword(final String s) {

        dialog = new ProgressDialog(LoginActivity.this);
        dialog.setMessage("Resetting your password");
        dialog.show();

        String tag_string_req = "Forgot";
        String url = ApiList.Forgot_Password;
        StringRequest strReq = new StringRequest(com.android.volley.Request.Method.POST,
                url, new com.android.volley.Response.Listener<String>() {


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
        }, new com.android.volley.Response.ErrorListener() {

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
   /* private void recoverynewpassword(final String s) {

        dialog = new ProgressDialog(LoginActivity.this, R.style.MyAlertDialogStyle);
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
                    if (jObj.getBoolean("response")) {
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


    }*/


    private void parseResult() {


        Gson gson = new Gson();

        if (pos < myevents.length()) {


            String eventString = null;
            try {
                eventString = myevents.getJSONObject(pos).toString();
                Event obj = gson.fromJson(eventString, Event.class);
               /* if (obj.getInfo_privacy() == 1)*/
                Paper.book(obj.getAppid()).write("PrivateKey", obj.getPrivate_key());
                Paper.book(obj.getAppid()).write("InfoPrivacy", obj.getInfo_privacy());

                if (!savedirs.contains(obj.getAppid())) {
                    downloadjson(obj);
                } else {
                    pos++;
                    parseResult();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            setResult(RESULT_OK);
            finish();
        }


    }


    private void downloadjson(final Event events) {
        if (pdialog == null) {
            pdialog = new ProgressDialog(LoginActivity.this, R.style.MyAlertDialogStyle);
            pdialog.setMessage("Loading the User details...");
            pdialog.show();
        }
        String query = "";
        try {
            query = URLEncoder.encode(events.getApp_url(), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String jsonUrl = baseUrl + query + "/appData.json";


        String tag_string_req = "Downloading";
        System.out.println("Url " + jsonUrl);
        StringRequest jsonRequest = new StringRequest(com.android.volley.Request.Method.GET, jsonUrl, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (pos == myevents.length() - 1) {
                    pdialog.dismiss();
                }
                System.out.println("JSON Response " + response);

                eventDir = new File(dir + events.getAppid());
                if (!eventDir.exists())
                    eventDir.mkdir();
                jsonFile = new File(eventDir, filename);
                descFile = new File(eventDir, "description.txt");
                try {
                    Files.write(response, jsonFile, Charset.defaultCharset());
                    Files.append(events.getApp_name() + "\n", descFile, Charset.defaultCharset());
                    Files.append(events.getApp_category() + "\n", descFile, Charset.defaultCharset());
                    Files.append(events.getStart_date() + "\n", descFile, Charset.defaultCharset());
                    Files.append(events.getAppid() + "\n", descFile, Charset.defaultCharset());
                    Files.append(events.getLocation() + "\n", descFile, Charset.defaultCharset());
                    Files.append(events.getApp_title() + "\n", descFile, Charset.defaultCharset());
                    Files.append(events.getVenue() + "\n", descFile, Charset.defaultCharset());
                    Files.append(events.getApp_logo() + "\n", descFile, Charset.defaultCharset());
                    Files.append(events.getApp_url() + "\n", descFile, Charset.defaultCharset());
                    Files.append(events.getApp_image() + "\n", descFile, Charset.defaultCharset());
                    pos++;
                    parseResult();


                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (pdialog != null)
                    pdialog.dismiss();
                if (error instanceof TimeoutError) {
                    Error_Dialog.show("Timeout", LoginActivity.this);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    pos++;
                    parseResult();
                    // Error_Dialog.show(VolleyErrorLis.handleServerError(error, context), LoginActivity.this);
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show("No Internet Connection", LoginActivity.this);
                }

            }
        }) {
            @Override
            protected com.android.volley.Response<String> parseNetworkResponse(
                    NetworkResponse response) {

                String strUTF8 = null;
                try {
                    strUTF8 = new String(response.data, "UTF-8");

                } catch (UnsupportedEncodingException e) {

                    e.printStackTrace();
                }
                return com.android.volley.Response.success(strUTF8,
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


}
