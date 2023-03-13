package com.singleevent.sdk.View.RightActivity.admin.checkin;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.singleevent.sdk.ApiList;
import com.singleevent.sdk.Custom_View.Error_Dialog;
import com.singleevent.sdk.Custom_View.Util;
import com.singleevent.sdk.Custom_View.VolleyErrorLis;
import com.singleevent.sdk.model.AppDetails;
import com.singleevent.sdk.R;
import com.singleevent.sdk.View.RightActivity.admin.model.EventUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import io.paperdb.Paper;

/**
 * Created by ashish on 2/19/2018.
 */

public class RegisterEventUserActivity extends AppCompatActivity implements View.OnClickListener{
    private static final int REGISTER_USER_REQUEST = 101;
    private static final String TAG = RegisterEventUserActivity.class.getCanonicalName();
    AppDetails appDetails;
    private Toolbar toolbar;
    private Context context;
    private Button save,cancel;
    EditText fName,lName,email,phone,designation,company;
    private RequestQueue queue;
    private String checkMail="1";
    ArrayList<EventUser> filelist;
    private ImageView iv_sync;

    /*offline storage */
    ArrayList<JSONArray> jsonArrayList = new ArrayList<>();
    ArrayList<String>   list = new ArrayList<String>();
    JSONArray jsonOffArray = new JSONArray();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Paper.init(this);
        context = RegisterEventUserActivity.this;
        setContentView(R.layout.act_regiser_event_user );
        appDetails = Paper.book().read("Appdetails");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        iv_sync = (ImageView) findViewById(R.id.iv_sync);
        setSupportActionBar(toolbar);

        filelist = new ArrayList<>();


        filelist = Paper.book(appDetails.getAppId()).read("AllusersList",new ArrayList<EventUser>());

        save = (Button)findViewById(R.id.save);
        cancel = (Button)findViewById(R.id.cancel);
        fName = (EditText)findViewById(R.id.et_FName);
        lName = (EditText)findViewById(R.id.et_LName);
        email = (EditText)findViewById(R.id.et_Email);
        phone = (EditText)findViewById(R.id.et_Phone);
        designation = (EditText)findViewById(R.id.et_Designation);
        company = (EditText) findViewById(R.id.et_Company);

        toolbar.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));
        save.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));
        cancel.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));

        queue = Volley.newRequestQueue(context);
        save.setOnClickListener(this);
        cancel.setOnClickListener(this);
        iv_sync.setOnClickListener(this);


        if (!Paper.book(appDetails.getAppId()).read("offlineReg","").equals("")){
            if (isInternetConnectivity()){
                registerOfflineUsers();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        toolbar.setTitle(Util.applyFontToMenuItem(context,new SpannableString("Register")));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemID = item.getItemId();
        if (itemID ==android.R.id.home){
            finish();
            return true;
        }
        return true;
    }

    private boolean checkFields(){

        String fName="",lName="",email="",phone="";
        fName = this.fName.getText().toString().trim();
        lName = this.lName.getText().toString().trim();
        email = this.email.getText().toString().trim();

        if (TextUtils.isEmpty(fName) || fName.equals("") || fName.contains(" ")){

            Error_Dialog.show("Please Enter valid first name",
                    RegisterEventUserActivity.this);
            return false;
        }
        else if (TextUtils.isEmpty(lName) || lName.equals("") || lName.contains(" ") ){

            Error_Dialog.show("Please Enter valid last name",
                    RegisterEventUserActivity.this);
            return false;
        }
        else if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){

            Error_Dialog.show("Please Enter valid Email",
                    RegisterEventUserActivity.this);
            return  false;
        }

        else{
            return true;
        }

    }



    private void registerUser(final String checkmail){

        //Generate random UUID
        UUID uuid1 = UUID.randomUUID();
        String uuid = uuid1.toString().replaceAll("-","");
        final JSONObject json1 = new JSONObject();
        final  JSONObject json1EventUser = new JSONObject();
        final JSONArray jsonArray = new JSONArray();
        try {
            json1.put("FirstName",fName.getText().toString().trim());
            json1.put("LastName",lName.getText().toString().trim());
            json1.put("Userid",uuid);
            json1.put("Email",email.getText().toString().trim());
            json1.put("Phone",phone.getText().toString().trim());
            json1.put("Company",company.getText().toString());
            json1.put("Designation",designation.getText().toString().trim());
            json1.put("Description","");
            json1.put("BlogUrl","");
            json1.put("City","");
            json1.put("Country","");



            jsonArray.put(json1);

            //storing offline users for CheckinUsers in EventUser model
            json1EventUser.put("first_name",fName.getText().toString().trim());
            json1EventUser.put("last_name",lName.getText().toString().trim());
            json1EventUser.put("userid",uuid);
            json1EventUser.put("email",email.getText().toString().trim());
            json1EventUser.put("mobile",phone.getText().toString().trim());
            json1EventUser.put("company",company.getText().toString().trim());
            json1EventUser.put("designation",designation.getText().toString().trim());
            json1EventUser.put("description","");
            json1EventUser.put("user_blog","");
            json1EventUser.put("website","");
            json1EventUser.put("checkin_status","checkedin");
            json1EventUser.put("profile_pic","");

            JSONObject object = json1EventUser;
            Gson gson = new Gson();

            EventUser user = gson.fromJson(object.toString(), EventUser.class);
            ArrayList<EventUser> eventUsers = new ArrayList<>();
            eventUsers = Paper.book(appDetails.getAppId()).read("AllusersList",new ArrayList<EventUser>());
            eventUsers.add(user);
            Paper.book(appDetails.getAppId()).write("AllusersList",eventUsers);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        final ProgressDialog dialog = new ProgressDialog(RegisterEventUserActivity.this,
                R.style.MyAlertDialogStyle);
        dialog.setMessage("Registering...");
        dialog.show();
        String tag_string_req = "register_user_event";
        String url = ApiList.Register_user_event;
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                try {
                    System.out.println(response);
                    JSONObject jObj = new JSONObject(response);


                    if (jObj.getBoolean("response")) {
                        Error_Dialog.show(jObj.getString("responseString"),
                                RegisterEventUserActivity.this);


                        /*setResult(RESULT_OK);
                        finish();*/
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                               fName.setText("");
                                lName.setText("");
                               email.setText("");
                               phone.setText("");
                                company.setText("");
                              designation.setText("");
                            }
                        });



                    } else {
                        Error_Dialog.show(jObj.getString("responseString"),
                                RegisterEventUserActivity.this);
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
                    Error_Dialog.show("Timeout", RegisterEventUserActivity.this);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, context),
                            RegisterEventUserActivity.this);
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show(" Internet Connection Problem, You have registered offline. ",
                            RegisterEventUserActivity.this);

                    /*Storing offline*/
                    try {

                        jsonOffArray.put(json1);

                        Paper.book(appDetails.getAppId()).write("offlineReg",jsonOffArray.toString());
                        //making empty all fields
                        fName.setText("");
                        lName.setText("");
                        email.setText("");
                        phone.setText("");
                        company.setText("");
                        designation.setText("");

                    }catch (Exception e){
                        e.printStackTrace();

                    }

                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("createdBy",/*"46002"*/ Paper.book().read("userId",""));
                params.put("checkMail",checkmail);
                params.put("regUsers",jsonArray.toString());
                params.put("appId", /*"e4b11d36071d516d84e85c800d05e2ed6438"*/ appDetails.getAppId() );

                return params;
            }

        };


        strReq.setShouldCache(false);
        queue.add(strReq);
    }

    private boolean isInternetConnectivity(){

        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        return isConnected;

    }

    private void registerOfflineUsers(){



        final ProgressDialog dialog = new ProgressDialog(RegisterEventUserActivity.this,
                R.style.MyAlertDialogStyle);
        dialog.setMessage("Registering...");
        dialog.show();
        String tag_string_req = "register_user_event";
        String url = ApiList.Register_user_event;
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                try {
                    System.out.println(response);
                    JSONObject jObj = new JSONObject(response);


                    if (jObj.getBoolean("response")) {
                        Error_Dialog.show(jObj.getString("responseString"),
                                RegisterEventUserActivity.this);

                        //making empty after successfully send offline stored data
                        Paper.book(appDetails.getAppId()).write("offlineReg","");
                        jsonOffArray = new JSONArray();

                    } else {
                        Error_Dialog.show(jObj.getString("responseString"),
                                RegisterEventUserActivity.this);

                        Paper.book(appDetails.getAppId()).delete("offlineReg");
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
                    Error_Dialog.show("Timeout", RegisterEventUserActivity.this);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, context),
                            RegisterEventUserActivity.this);
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show("Data successfully saved.",
                            RegisterEventUserActivity.this);


                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("createdBy", Paper.book().read("userId",""));
                params.put("checkMail","1");
                params.put("regUsers",Paper.book(appDetails.getAppId()).read("offlineReg",""));
                params.put("appId", /*"e4b11d36071d516d84e85c800d05e2ed6438"*/ appDetails.getAppId() );
                System.out.println(params);
                return params;
            }

        };


        strReq.setShouldCache(false);
        queue.add(strReq);
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.save ){

            if (checkFields()){

                if (isInternetConnectivity()){
                    showDialog();
                }else{

                    withoutDialog();

                }
            }

            // showDialog();

        }else if (v.getId()==R.id.cancel){

            finish();
        }else if (v.getId()== R.id.iv_sync){

            if (!Paper.book(appDetails.getAppId()).read("offlineReg","").equals("")){
                if (isInternetConnectivity()){
                    registerOfflineUsers();
                }
            }
        }
    }

    private void withoutDialog() {

        for (int i=0;i<filelist.size();i++){
            if (email.getText().toString().toLowerCase().equals(filelist.get(i).getEmail().toLowerCase())){
                Error_Dialog.show("This Email-Id is already exist",
                        RegisterEventUserActivity.this);
                return;
            }

        }

        try {
            for(int i=0;i<jsonOffArray.length();i++) {

                if (jsonOffArray.getJSONObject(i).getString("Email").equalsIgnoreCase(email.getText().toString())) {
                    Error_Dialog.show("This Email-Id is already exist",
                            RegisterEventUserActivity.this);
                    return;
                }
            }
        }catch (JSONException e){
            e.printStackTrace();

        }catch (Exception e){

            e.printStackTrace();
        }

        registerUser("1");
    }

    private void showDialog(){
        // Use the Builder class for convenient dialog construction
        /*Theme_AppCompat_Light_Dialog_Alert*/

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyAlertDialogStyle);
        builder.setMessage("Would you like to send email to users?")
                .setPositiveButton( "Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        checkMail ="1";
                        for (int i=0;i<filelist.size();i++){
                            if (email.getText().toString().toLowerCase().equals(filelist.get(i).getEmail().toLowerCase())){
                                Error_Dialog.show("This Email-Id is already exist",
                                        RegisterEventUserActivity.this);
                                return;
                            }
                        }

                        try {
                            for(int i=0;i<jsonOffArray.length();i++) {

                                if (jsonOffArray.getJSONObject(i).getString("Email").equalsIgnoreCase(email.getText().toString())) {
                                    Error_Dialog.show("This Email-Id is already exist",
                                            RegisterEventUserActivity.this);
                                    return;
                                }
                            }
                        }catch (JSONException e){
                           e.printStackTrace();
                        }catch (Exception e){

                           e.printStackTrace();
                        }


                        registerUser(checkMail);
                    }
                })
                .setNegativeButton( "No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        checkMail = "0";
                        for (int i=0;i<filelist.size();i++){
                            if (email.getText().toString().toLowerCase().equals(filelist.get(i).getEmail().toLowerCase())){
                                Error_Dialog.show("This Email-Id is already exist",
                                        RegisterEventUserActivity.this);
                                return;
                            }
                        }
                        // duplicate offline stored users
                        try {
                            for(int i=0;i<jsonOffArray.length();i++) {

                                if (jsonOffArray.getJSONObject(i).getString("Email").equalsIgnoreCase(email.getText().toString())) {
                                    Error_Dialog.show("This Email-Id is already exist",
                                            RegisterEventUserActivity.this);
                                    return;
                                }
                            }
                        }catch (JSONException e){
                           e.printStackTrace();
                        }catch (Exception e){

                          e.printStackTrace();
                        }
                        registerUser(checkMail);

                    }
                });
        // Create the AlertDialog object and return it
        builder.create();
        builder.show();
    }
}
