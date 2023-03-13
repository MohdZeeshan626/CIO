package com.singleevent.sdk.View.RightActivity.admin.feedback;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.SpannableString;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.singleevent.sdk.model.AppDetails;
import com.singleevent.sdk.model.Items;
import com.singleevent.sdk.model.User;
import com.singleevent.sdk.R;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.paperdb.Paper;

public class FeedbackDetails extends AppCompatActivity {
    private static final String TAG = FeedbackDetails.class.getCanonicalName();
    private RelativeLayout feedbackView;
    private Toolbar toolbar;
    private AppDetails appDetails;
    private static int agenda_id;
    User user=null;
    ArrayList<Items> surveylist = new ArrayList<>();
    private String dateTime,question;
    ImageView ivsmiling,ivnetural ,iv_sad;
    TextView tv_ques;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_admin_feedback_detail);
        Paper.init(this);
        appDetails = Paper.book().read("Appdetails");


        feedbackView =  (RelativeLayout) findViewById(R.id.feedbackView);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));
        setSupportActionBar(toolbar);
        ivsmiling = (ImageView) findViewById(R.id.ivsmiling);
        ivnetural = (ImageView) findViewById(R.id.ivnetural);
        iv_sad = (ImageView) findViewById(R.id.iv_sad);
        tv_ques = (TextView) findViewById(R.id.tv_ques);

        Bundle bundle = getIntent().getExtras();

        try {



            surveylist = (ArrayList<Items>) bundle.getSerializable("surveyqueslist");
            if (surveylist==null){
                finish();
            }
            agenda_id = bundle.getInt("agenda_id");

            tv_ques.setText("Ques:- "+surveylist.get(0).getQuestion());
            user = (User) bundle.getSerializable("AttendeeUser");
        }catch (Exception e){
            e.printStackTrace();
        }


        iv_sad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (surveylist.size()>0)
                sendingfeddback("Not Satisfied");
            }
        });

        ivnetural.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (surveylist.size()>0)
                sendingfeddback("Neutral");
            }
        });

        ivsmiling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (surveylist.size()>0)
                sendingfeddback("Satisfied");
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();

        setTitle(Util.applyFontToMenuItem(this, new SpannableString("Feedback")));
        Log.d(TAG,"Onresume");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"OnDestroy");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG,"OnPause");

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG,"OnStart");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG,"OnRestart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG,"OnStop");
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        Log.d(TAG,"OnsaveInstanceState");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d(TAG,"OnRestoreInstanceInstaceState");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                startActivity(new Intent(this,FeedBackActivity.class));
                finish();
        }
        return true;



    }

    private void sendingfeddback(final String toJson) {

        /*Storing offline */

        HashMap<String, JSONObject> singleUserRecords = new HashMap<>();

        JSONObject object = new JSONObject();
        JSONArray jsonArray1 = new JSONArray();
        try {

            if(user==null)
            object.put("userid", "anonymous");
            else{
                object.put("userid", user.getUserid());
            }
            object.put("user_type", "default");
            object.put("agenda_id",agenda_id+"");
            JSONObject ansObj = new JSONObject();

            ansObj.put("question",surveylist.get(0).getQuestion());
            ansObj.put("answer",toJson);

            object.put("ans_data", ansObj);
            System.out.println("JSON Object : " + object.toString());

            jsonArray1 = new JSONArray();
            jsonArray1.put(object);
            singleUserRecords.put(Paper.book().read("userId",""), object);

        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        /*
         * */


        if (Paper.book(appDetails.getAppId()).read("offAdminSurveyAns", new HashMap<String, JSONObject>()).size() == 0) {
            Paper.book(appDetails.getAppId()).write("offAdminSurveyAns", singleUserRecords);

        } else {
            HashMap<String, JSONObject> offvalue =
                    Paper.book(appDetails.getAppId()).read("offAdminSurveyAns", new HashMap<String, JSONObject>());
            HashMap<String, JSONObject> ofLinUsrAnsList = new HashMap<>();
            ofLinUsrAnsList.putAll(offvalue);
            ofLinUsrAnsList.put(Paper.book().read("userId",""), object);

            Paper.book(appDetails.getAppId()).write("offAdminSurveyAns", ofLinUsrAnsList);
        }

        //getting offline survey answers with user details
        final JSONArray retriveJSONArray;
        retriveJSONArray = new JSONArray();


        HashMap<String, JSONObject> retriveofLinUsrAnsList = new HashMap<>();
        retriveofLinUsrAnsList = Paper.book(appDetails.getAppId()).read("offAdminSurveyAns", new HashMap<String, JSONObject>());
        for (Map.Entry<String, JSONObject> entry : retriveofLinUsrAnsList.entrySet()) {
            //  String value = String.valueOf(entry.getValue());
            String key = entry.getKey();
            JSONObject value = entry.getValue();


            retriveJSONArray.put(value);

            System.out.println("JSON Retrived : " + retriveJSONArray);

        }


        dateTime = String.valueOf(System.currentTimeMillis());
        final ProgressDialog dialog = new ProgressDialog(this, R.style.MyAlertDialogStyle);
        dialog.setMessage("Sending Feedback...");
        dialog.show();
        String tag_string_req = "Schdule";
        String url = ApiList.PostAdminSurveyFeedBack ;
        //  url="http://104.131.76.15:3000/api/event/registration_answers";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                try {
                    System.out.println(response);
                    JSONObject jObj = new JSONObject(response);


                    if (jObj.getBoolean("response")) {
                        Error_Dialog.show(jObj.getString("responseString"), FeedbackDetails.this);
                        /*After submitting all the answers , i am deleting from local storage*/
                        Paper.book(appDetails.getAppId()).write("offAdminSurveyAns",new HashMap<String, JSONObject>());


                        /*   *//*here store all submitted survey's attendee list*//*
                        JSONArray jsonArray = jObj.getJSONArray("submitted_users");
                        ArrayList<String> userIds = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject object = jsonArray.getJSONObject(i);
                            userIds.add(object.getString("userid"));

                        }

                        //storing all user ids in paper db
                        if (userIds.size()>0)
                            Paper.book(appDetails.getAppId()).write("AdminAttendeesUsers",userIds);
*/




                    } else {
                        if (jObj.getString("responseString").equalsIgnoreCase("Invalid token")) {
                            Error_Dialog.show("Session is Expired Please Login", FeedbackDetails.this);
                        } else
                            Error_Dialog.show(jObj.getString("responseString"), FeedbackDetails.this);

                       /* new CountDownTimer(2000, 1000) {

                            public void onTick(long millisUntilFinished) {
                            }

                            public void onFinish() {
                                finish();
                            }
                        }.start();
*/
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
                    Error_Dialog.show("Timeout", FeedbackDetails.this);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error,
                            FeedbackDetails.this), FeedbackDetails.this);
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show("Survey Submitted Offline.", FeedbackDetails.this);

                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("appid", appDetails.getAppId());
                params.put("userResponse", retriveJSONArray.toString());
                params.put("adminsurvey_flag", "1");

                System.out.println(params);
                return params;
            }
        };


        App.getInstance().addToRequestQueue(strReq, tag_string_req);
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                100000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        //submitsurveyans();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this,FeedBackActivity.class));
        finish();
    }
}
