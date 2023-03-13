package com.singleevent.sdk.View.RightActivity.admin;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

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
import com.singleevent.sdk.View.RightActivity.admin.model.UserListModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.paperdb.Paper;

/**
 * Created by webMOBI on 11/30/2017.
 */

public class ChooseDeviceIdActivity extends AppCompatActivity implements View.OnClickListener{

    private Button  btnGo, btnChoose;
    private cyd.awesome.material.AwesomeText backward;
    private EditText etSelectForUser, etChooseForUser;
    private RelativeLayout h3;
    private AppDetails appDetails;
    private String userName="";
    private ArrayList<UserListModel> notifUserArrayList;
    private String event_user_id ="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Paper.init(this);

        appDetails = Paper.book().read("Appdetails");
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.choose_device_id_layout);

        backward = (cyd.awesome.material.AwesomeText) findViewById(R.id.backward);
        etSelectForUser = (EditText) findViewById(R.id.etSelectForUser);
        h3 = (RelativeLayout) findViewById(R.id.h3);
        etChooseForUser = (EditText) findViewById(R.id.etChooseForUser);
        btnGo = (Button) findViewById(R.id.btnGo);
        btnChoose = (Button) findViewById(R.id.btnChoose);
        btnChoose.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));
        btnGo.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));
        //setting view listeners
        backward.setOnClickListener(this);
        btnChoose.setOnClickListener(this);
        btnGo.setOnClickListener(this);

        //visibility gone for showing emailid and choose button
        h3.setVisibility(View.GONE);
        etChooseForUser.setOnClickListener(this);
        btnChoose.setVisibility(View.GONE);

    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
            if(id== R.id.backward) {
                //call finish() method to close activity
                finish();
            }
            if (id== R.id.btnChoose){
                    //call api to send notification to particular user
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("user_details", notifUserArrayList);
                    intent.putExtra("event_user_id",event_user_id);
                    intent.putExtras(bundle);
                    setResult(11, intent);
                    finish();
            }
            if (id== R.id.btnGo){
                    //call api to get email of user
                    h3.setVisibility(View.GONE);
                    btnChoose.setVisibility(View.GONE);

                    if(etSelectForUser.getText().toString().equals("")){
                        //invalid input
                        Toast.makeText(this,"Please enter valid name. ",Toast.LENGTH_SHORT).show();
                    }else if(etSelectForUser.getText().toString().length()<=2 ){

                        //invalid input
                        Toast.makeText(this,"Title should be minimum 2 characters ",Toast.LENGTH_SHORT).show();
                    }else {
                        //valid input
                        userName = etSelectForUser.getText().toString();
                        getUserByCallingApi();
                    }
            }
            else if (id== R.id.etChooseForUser){
                dialogToSelectEmail();
            }
    }
    //string array  to store emails of all users
    String[] arrEmail;
    private void dialogToSelectEmail() {

        arrEmail = new String[notifUserArrayList.size()];
        for (int i=0;i<notifUserArrayList.size();i++){
            arrEmail[i] = notifUserArrayList.get(i).getEmail();
        }


        AlertDialog.Builder builder = new AlertDialog.Builder(ChooseDeviceIdActivity.this, R.style.CalendarDialog);
        builder.setTitle("Select Email-Id")
                .setItems(arrEmail, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                        etChooseForUser.setText(arrEmail[which]);
                        event_user_id = notifUserArrayList.get(which).getEvent_user_id();
                    }
                });
        builder.create();
        builder.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
     ProgressDialog dialog;
    private void getUserByCallingApi(){
         dialog = new ProgressDialog(ChooseDeviceIdActivity.this,R.style.MyAlertDialogStyle);
        dialog.setMessage("Please wait...");
        dialog.show();

        String tag = "Get_User_details";

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                ApiList.GetUser+userName+"&appid="+appDetails.getAppId(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog.hide();
                        try {
                            //Do it with this it will work
                            JSONObject person = new JSONObject(response);
                            notifUserArrayList = new ArrayList<>();
                          UserListModel userListModel ;
                            boolean isSuccess = person.getBoolean("response");
                            if(isSuccess){
                                JSONArray responseString = person.getJSONArray("responseString");
                                for (int i=0;i<responseString.length();i++){
                                    JSONObject jsonObject = responseString.getJSONObject(i);
                                    String fname = jsonObject.getString("first_name");
                                    String lname = jsonObject.getString("last_name");
                                    String event_user_id = jsonObject.getString("event_user_id");
                                    String email = jsonObject.getString("email");
                                    userListModel = new UserListModel(fname,lname,event_user_id,email);
                                    notifUserArrayList.add(userListModel);
                                }





                                if(notifUserArrayList.size() > 0 ){
                                  //  etChooseForUser.setText(email);

                                    h3.setVisibility(View.VISIBLE);
                                    btnChoose.setVisibility(View.VISIBLE);
                                }


                            }else {
                                Error_Dialog.show("No User with this name.", ChooseDeviceIdActivity.this);
                            }

                        } catch (JSONException e) {
                            dialog.hide();
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Sorry Try Again ", Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                dialog.hide();
                if (error instanceof TimeoutError) {
                    Error_Dialog.show("Timeout", ChooseDeviceIdActivity.this);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, ChooseDeviceIdActivity.this),
                            ChooseDeviceIdActivity.this);
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show("Please Check Your Internet Connection", ChooseDeviceIdActivity.this);
                }
            }
        });

        App.getInstance().addToRequestQueue(stringRequest,tag);
    }

}
