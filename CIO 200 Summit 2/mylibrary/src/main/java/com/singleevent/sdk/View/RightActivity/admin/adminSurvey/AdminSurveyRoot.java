package com.singleevent.sdk.View.RightActivity.admin.adminSurvey;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.ViewFlipper;

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
import com.singleevent.sdk.model.Items;
import com.singleevent.sdk.model.User;
import com.singleevent.sdk.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import io.paperdb.Paper;
import okhttp3.MediaType;

public class AdminSurveyRoot extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener{

    private static final String TAG = AdminSurveyRoot.class.getSimpleName();
    AppDetails appDetails;
    int pos, questionsize;
    EditText feedback,feedback1;
    private String title, toJson, answer, dateTime;
   // ArrayList<AdminSurveyModel> surveylist = new ArrayList<>();
   ArrayList<Items> surveylist = new ArrayList<>();
    private ArrayList<Events> events = new ArrayList<Events>();
    Events e;
    private ViewFlipper mViewFlipper;
    TextView submitanswer;
    String[][] finalanswer;
    ArrayList<String> group[] ; //Put the length of the array you need
    Gson converter = new Gson();
    int myProgress = 0;
    ProgressBar pb;
    TextView up, back;
    private SimpleDateFormat myFormat;
    User user;
    String attendeeUserId;
    String feed_back1="" ,admin_type="";
    Toolbar toolbar;
    ProgressDialog dialog;
    Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Paper.init(this);
        setContentView(R.layout.s_surveyroot);

        context =this;
        appDetails = Paper.book().read("Appdetails");
        events = Paper.book().read("Appevents");
        e = events.get(0);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));
        setSupportActionBar(toolbar);

        surveylist.clear();

        // getting the data from previous activity

        try{
            surveylist= (ArrayList<Items>)getIntent().getExtras().getSerializable("surveyqueslist");
            user = (User) getIntent().getExtras().getSerializable("AttendeeUser");


            if (user!=null)attendeeUserId = user.getUserid();

        }catch (NullPointerException exception){
            attendeeUserId="";
            System.out.print("Null Pointer while getting AttendeeUser");
        }
        //Based on User we are adding admin_type and attendeeUserId type.
        String flag = Paper.book(appDetails.getAppId()).read("admin_flag","");
        if (flag.equalsIgnoreCase("admin")){
            admin_type = "attendee";
        }else if (flag.equalsIgnoreCase("exhibitor")){
            admin_type ="exhibitor";
            attendeeUserId = Paper.book().read("userId","");
        }




        mViewFlipper = (ViewFlipper) findViewById(R.id.viewflipper);
        submitanswer = (TextView) findViewById(R.id.txtcon);
        submitanswer.setTypeface(Util.regulartypeface(this));

        pb = (ProgressBar) findViewById(R.id.progressBar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            pb.setProgressTintList(ColorStateList.valueOf(Color.parseColor(appDetails.getTheme_color())));
        }

        submitanswer.setTextColor(Color.parseColor(appDetails.getTheme_color()));


        submitanswer.setOnClickListener(this);

       initView();
    }


    @Override
    protected void onResume() {
        super.onResume();


        SpannableString s = new SpannableString("Admin Survey");
        setTitle(Util.applyFontToMenuItem(this, s));

    }

    @Override
    public void onClick(View view) {

        String question = surveylist.get(mViewFlipper.getDisplayedChild()).getQuestion();

        switch (surveylist.get(mViewFlipper.getDisplayedChild()).getType()) {

            case "messagebox":
                try {
                    if (group[mViewFlipper.getDisplayedChild()].size() > 0) {
                        int size = group[mViewFlipper.getDisplayedChild()].size();
                        String[] ans = new String[4];
                        ans[0] = question;
                        ans[1] = group[mViewFlipper.getDisplayedChild()].get(0);
                        ans[2]="details";
                        ans[3]="";
                        addingtojson(ans, mViewFlipper.getDisplayedChild());

                    } else if (surveylist.get(mViewFlipper.getDisplayedChild()).getRequired() == 0){

                         //Error_Dialog.show("Please Select the Answer", AdminSurveyRoot.this);
                            //shownextquestion();

                            String[] emptyAns = new String[4];
                            emptyAns[0] = question;
                        emptyAns[1]="";
                        emptyAns[2]="details";
                        emptyAns[3]="";
                            addingtojson(emptyAns, mViewFlipper.getDisplayedChild());
                    }else
                        Error_Dialog.show("Please Give the FeedBack", AdminSurveyRoot.this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case "multiple":

                if (group[mViewFlipper.getDisplayedChild()].size() > 0) {
                    int size = group[mViewFlipper.getDisplayedChild()].size();
                    String[] ans1 = new String[4];

                    String[] temp = new String[size];
                    for (int i = 0; i < group[mViewFlipper.getDisplayedChild()].size(); i++) {
                        ans1[0] = question;

                        temp[i] = group[mViewFlipper.getDisplayedChild()].get(i);

                    }
                    Gson gson = new Gson();
                    String tojson = gson.toJson(temp);

                    ans1[1]= tojson;
                    ans1[2]="details";
                    if (feedback1!=null){
                        ans1[3] = feedback1.getText().toString();
                        feedback1.setText("");
                    }


                    addingtojson(ans1, mViewFlipper.getDisplayedChild());

                } else if (surveylist.get(mViewFlipper.getDisplayedChild()).getRequired() == 0) {
                    //Error_Dialog.show("Please Select the Answer", AdminSurveyRoot.this);
                    //shownextquestion();

                    String[] emptyAns = new String[4];
                    emptyAns[0] = question;
                    emptyAns[1]="";
                    emptyAns[2]="details";
                    emptyAns[3]="";

                    addingtojson(emptyAns, mViewFlipper.getDisplayedChild());
                }else
                    Error_Dialog.show("Please Select the Answer", AdminSurveyRoot.this);

                break;

            case "single":
                try {
                    if (group[mViewFlipper.getDisplayedChild()].size() > 0) {

                        int size = group[mViewFlipper.getDisplayedChild()].size();
                        String[] ans2 = new String[size + 3];
                        ans2[0] = question;
                        ans2[1] = group[mViewFlipper.getDisplayedChild()].get(0);
                        ans2[2] = "details";
                        if (feedback1!=null){
                            ans2[3] = feedback1.getText().toString();
                            feedback1.setText("");
                        }

                        addingtojson(ans2, mViewFlipper.getDisplayedChild());
                    } else if (surveylist.get(mViewFlipper.getDisplayedChild()).getRequired() == 0){
                        //Error_Dialog.show("Please Select the Answer", AdminSurveyRoot.this);
                        //shownextquestion();

                        String[] emptyAns = new String[4];
                        emptyAns[0] = question;
                        emptyAns[1]="";
                        emptyAns[2]="details";
                        emptyAns[3]="";
                        addingtojson(emptyAns, mViewFlipper.getDisplayedChild());
                    }else
                        Error_Dialog.show("Please Select the Answer", AdminSurveyRoot.this);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }

    }

    private TextWatcher q = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            group[mViewFlipper.getDisplayedChild()].clear();
            group[mViewFlipper.getDisplayedChild()].add(s.toString());
        }
    };

    private TextWatcher q1 = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

            feed_back1 = s.toString();
        }
    };

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            group[mViewFlipper.getDisplayedChild()].add(String.valueOf(buttonView.getText()));
        } else {
            group[mViewFlipper.getDisplayedChild()].remove(String.valueOf(buttonView.getText()));
        }
    }

    private void addingtojson(String[] ans1, int displayedChild) {
        finalanswer[displayedChild] = ans1;
        toJson = converter.toJson(finalanswer);
        if (mViewFlipper.getDisplayedChild() == surveylist.size() - 1) {
            if (Paper.book().read("Islogin", false))
                sendingfeddback(toJson);
            else {
                Error_Dialog.show("Please Login to submit survey",AdminSurveyRoot.this );
                /*Intent i = new Intent(SurveyRoot.this, LoginActivityDemo.class);
                startActivityForResult(i, 1);*/
            }
        } else
            shownextquestion();

    }
    JSONObject finalanslist;
    private void sendingfeddback(final String toJson) {

        /*Storing offline */

        HashMap<String, JSONObject> singleUserRecords = new HashMap<>();

        JSONObject object = new JSONObject();
        JSONArray jsonArray1 = new JSONArray();
        try {
            object.put("userid", attendeeUserId);
            object.put("user_type", admin_type);
            JSONArray jsonArray = new JSONArray(toJson);
            JSONArray ansArr = new JSONArray();
            ansArr.put(toJson);
            object.put("ans_data", jsonArray);
            System.out.println("JSON Object : " + object.toString());

            jsonArray1 = new JSONArray();
            jsonArray1.put(object);
            singleUserRecords.put(attendeeUserId, object);

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
            ofLinUsrAnsList.put(attendeeUserId, object);

            Paper.book(appDetails.getAppId()).write("offAdminSurveyAns", ofLinUsrAnsList);
        }

        //getting offline survey answers with user details
        final JSONArray retriveJSONArray;
        retriveJSONArray = new JSONArray();
        finalanslist = new JSONObject();

        HashMap<String, JSONObject> retriveofLinUsrAnsList = new HashMap<>();
        retriveofLinUsrAnsList = Paper.book(appDetails.getAppId()).read("offAdminSurveyAns", new HashMap<String, JSONObject>());
        for (Map.Entry<String, JSONObject> entry : retriveofLinUsrAnsList.entrySet()) {
          //  String value = String.valueOf(entry.getValue());
            String key = entry.getKey();
            JSONObject value = entry.getValue();


            retriveJSONArray.put(value);

            System.out.println("JSON Retrived : " + retriveJSONArray);

        }
        try {
            finalanslist.put("responseString",retriveJSONArray);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }

        dateTime = String.valueOf(System.currentTimeMillis());
        final ProgressDialog dialog = new ProgressDialog(context, R.style.MyAlertDialogStyle);
        dialog.setMessage("Sending Feedback...");
        dialog.show();
        String tag_string_req = "Schdule";
        String url = ApiList.PostAdminSurveyFeedBack;
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
                        Error_Dialog.show(jObj.getString("responseString"), AdminSurveyRoot.this);
                        /*After submitting all the answers , i am deleting from local storage*/
                        Paper.book(appDetails.getAppId()).write("offAdminSurveyAns",new HashMap<String, JSONObject>());
                        /*here store all submitted survey's attendee list*/
                        JSONArray jsonArray = jObj.getJSONArray("submitted_users");
                        ArrayList<String> userIds = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject object = jsonArray.getJSONObject(i);
                            userIds.add(object.getString("userid"));

                        }

                        //storing all user ids in paper db
                        if (userIds.size()>0)
                            Paper.book(appDetails.getAppId()).write("AdminAttendeesUsers",userIds);


                        new CountDownTimer(2000, 1000) {

                            public void onTick(long millisUntilFinished) {
                            }

                            public void onFinish() {
                                finish();
                            }
                        }.start();


                    } else {
                        if (jObj.getString("responseString").equalsIgnoreCase("Invalid token")) {
                            Error_Dialog.show("Session is Expired Please Login", AdminSurveyRoot.this);
                        } else
                            Error_Dialog.show(jObj.getString("responseString"), AdminSurveyRoot.this);
                    }

                    new CountDownTimer(2000, 1000) {

                        public void onTick(long millisUntilFinished) {
                        }

                        public void onFinish() {
                            finish();
                        }
                    }.start();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                if (error instanceof TimeoutError) {
                    Error_Dialog.show("Timeout", AdminSurveyRoot.this);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error,
                            AdminSurveyRoot.this), AdminSurveyRoot.this);
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show("Survey Submitted Offline.", AdminSurveyRoot.this);

                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
              /*  params.put("userid", attendeeUserId);
                params.put("appid",  appDetails.getAppId());
                params.put("ans_data", toJson);
                params.put("user_type",admin_type);
                params.put("adminsurvey_flag","1");*/


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
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
/*
    private void submitsurveyans(){
        OkHttpClient client;
        client = new OkHttpClient();
        String url=ApiList.PostAdminSurveyFeedBack;

            RequestBody body = RequestBody.create(JSON, finalanslist.toString());
            okhttp3.Request request = new okhttp3.Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
        okhttp3.Response response = null;
        try {
            response = client.newCall(request).execute();
            String resStr = response.body().string();
            Log.d(TAG,resStr);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
*/

    private View.OnClickListener mThisButtonListener = new View.OnClickListener() {
        public void onClick(View v) {
            answer = ((RadioButton) v).getText().toString();
            if (feedback1!=null)
            feed_back1 = feedback1.getText().toString();

            group[mViewFlipper.getDisplayedChild()].clear();
            group[mViewFlipper.getDisplayedChild()].add(answer);
            if (feedback1!=null)
            feedback1.setText(feed_back1);


        }
    };

    private void shownextquestion() {
        if (myProgress == questionsize - 1) {
            up.setVisibility(View.INVISIBLE);
            submitanswer.setText("Finish");
        } else
        back.setVisibility(View.VISIBLE);
        myProgress++;
        pb.setProgress(myProgress);
        mViewFlipper.showNext();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            sendingfeddback(toJson);
        }
    }

    private String converlontostring(Long key) {

        myFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(key);
        return myFormat.format(cal.getTime());
    }

    private void initView(){

        group = new  ArrayList[surveylist.size()];
        questionsize = surveylist.size();
        for (int x = 0; x < group.length; x++) {
            group[x] = new ArrayList<>();
        }
        finalanswer = new String[questionsize][questionsize];
        for (int i = 0; i < finalanswer.length; i++) {
            for (int j = 0; j < finalanswer[i].length; j++) {
                finalanswer[i][0] = surveylist.get(i).getQuestion();
            }
        }

        //setting progress
        pb.setMax(questionsize);
        up = (TextView) findViewById(R.id.up);
        back = (TextView) findViewById(R.id.backward);

        up.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));
        back.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));

        back.setVisibility(View.INVISIBLE);
        myProgress++;
        pb.setProgress(myProgress);

        if (questionsize <= 1) {
            up.setVisibility(View.GONE);
            submitanswer.setText("Finish");
        }


        // setting the viewflipper

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        for (int i = 0; i < questionsize; i++) {

            LinearLayout l1 = new LinearLayout(this);
            l1.setOrientation(LinearLayout.VERTICAL);

            TextView heading = new TextView(this);
            heading.setText("Question " + (i + 1));
            heading.setTextColor(Color.parseColor("#000000"));
            heading.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
            heading.setTypeface(Util.regulartypeface(this));
            params.setMargins(0, 0, 0, 20);
            heading.setLayoutParams(params);

            l1.addView(heading);

            TextView title = new TextView(this);
            title.setText(surveylist.get(i).getQuestion());
            title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            title.setTextColor(Color.parseColor("#414548"));
            title.setTypeface(Util.regulartypeface(this));
            params.setMargins(0, 0, 0, 40);
            title.setLayoutParams(params);
            l1.addView(title);

            switch (surveylist.get(i).getType()) {

                case "messagebox":
                    feedback = new EditText(this);
                    feedback.setTextColor(getResources().getColor(R.color.black));
                    feedback.setGravity(Gravity.START);
                    feedback.addTextChangedListener(q);
                    feedback.setMovementMethod(new ScrollingMovementMethod());
                    int minHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
                    feedback.setBackground(getResources().getDrawable(R.drawable.s_aboutborder));
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, minHeight);
                    lp.setMargins(5, 5, 5, 5);
                    l1.addView(feedback, lp);

                    break;

                case "multiple":

                    for (int j = 0; j < surveylist.get(i).getAnswer().length; j++) {
                        CheckBox checkedTextView = new CheckBox(this);
                        checkedTextView.setText(Util.applyFontToMenuItem(this, new SpannableString(surveylist.get(i).getAnswer()[j])));
                        checkedTextView.setButtonDrawable(Util.setsurveycheckbox(AdminSurveyRoot.this, R.drawable.n2_ic_checkbox_checked, Color.parseColor(appDetails.getTheme_color())));
                        checkedTextView.setId(j);
                        checkedTextView.setPadding(25, 25, 100, 25);
                        LinearLayout.LayoutParams lc = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        lc.setMargins(5, 5, 5, 5);
                        checkedTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);

                        checkedTextView.setOnCheckedChangeListener(this);
                        l1.addView(checkedTextView, lc);
                    }

                    break;

                case "single":
                    RadioGroup radioGroup1 = new RadioGroup(this);
                    for (int j = 0; j < surveylist.get(i).getAnswer().length; j++) {

                        RadioButton radioButtonView = new RadioButton(this);
                        radioButtonView.setText(Util.applyFontToMenuItem(this,
                                new SpannableString(surveylist.get(i).getAnswer()[j])));
                        radioButtonView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                        radioButtonView.setOnClickListener(mThisButtonListener);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            radioButtonView.setButtonTintList(ColorStateList.valueOf(Color.parseColor(appDetails.getTheme_color())));
                        }
                        radioGroup1.addView(radioButtonView);


                    }
                    l1.addView(radioGroup1);
                    break;
            }

            if (!surveylist.get(i).getType().equalsIgnoreCase("messagebox") && surveylist.get(i).getDetail().equalsIgnoreCase("Yes")){
                feedback1 = new EditText(this);
                feedback1.setTextColor(getResources().getColor(R.color.black));
                feedback1.setGravity(Gravity.START);
                feedback1.addTextChangedListener(q1);
                feedback1.setMovementMethod(new ScrollingMovementMethod());
                int minHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
                feedback1.setBackground(getResources().getDrawable(R.drawable.s_aboutborder));
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, minHeight);
                lp.setMargins(5, 5, 5, 5);
                l1.addView(feedback1, lp);
            }


            mViewFlipper.addView(l1);
        }



        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String question = surveylist.get(mViewFlipper.getDisplayedChild()).getQuestion();


                switch (surveylist.get(mViewFlipper.getDisplayedChild()).getType()) {

                    case "messagebox":
                        try {
                            if (group[mViewFlipper.getDisplayedChild()].size() > 0) {
                                int size = group[mViewFlipper.getDisplayedChild()].size();
                                String[] ans = new String[4];
                                ans[0] = question;
                                ans[1] = group[mViewFlipper.getDisplayedChild()].get(0);
                                ans[2]="details";
                                ans[3]="";
                                addingtojson(ans, mViewFlipper.getDisplayedChild());

                            }else if (surveylist.get(mViewFlipper.getDisplayedChild()).getRequired() == 0){
                                //Error_Dialog.show("Please Select the Answer", AdminSurveyRoot.this);
                                //shownextquestion();

                                String[] emptyAns = new String[4];
                                emptyAns[0] = question;
                                emptyAns[1]="";
                                emptyAns[2]="details";
                                emptyAns[3]="";
                                addingtojson(emptyAns, mViewFlipper.getDisplayedChild());
                            }else {
                                Error_Dialog.show("Please Submit feedback", AdminSurveyRoot.this);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;

                    case "multiple":

                        if (group[mViewFlipper.getDisplayedChild()].size() > 0) {
                            int size = group[mViewFlipper.getDisplayedChild()].size();
                            String[] ans1 = new String[4];

                            String[] temp = new String[size];
                            for (int i = 0; i < group[mViewFlipper.getDisplayedChild()].size(); i++) {
                                ans1[0] = question;

                                temp[i] = group[mViewFlipper.getDisplayedChild()].get(i);

                            }
                            Gson gson = new Gson();
                            String tojson = gson.toJson(temp);
                            ans1[1]= tojson;
                            ans1[2]="details";
                            if (feedback1!=null)
                            ans1[3] = feedback1.getText().toString();

                            addingtojson(ans1, mViewFlipper.getDisplayedChild());
                        } else if (surveylist.get(mViewFlipper.getDisplayedChild()).getRequired() == 0){

                            String[] emptyAns = new String[4];
                            emptyAns[0] = question;
                            emptyAns[1]="";
                            emptyAns[2]="details";
                            emptyAns[3]="";
                            addingtojson(emptyAns, mViewFlipper.getDisplayedChild());
                        }else{
                            Error_Dialog.show("Please Select the Answer", AdminSurveyRoot.this);
                        }


                        break;

                    case "single":
                        try {
                            if (group[mViewFlipper.getDisplayedChild()].size() > 0) {

                                int size = group[mViewFlipper.getDisplayedChild()].size();
                                String[] ans2 = new String[size + 3];
                                ans2[0] = question;
                                ans2[1] = group[mViewFlipper.getDisplayedChild()].get(0);
                                ans2[2] ="details";
                                ans2[3] = feed_back1;

                                addingtojson(ans2, mViewFlipper.getDisplayedChild());
                            } else if (surveylist.get(mViewFlipper.getDisplayedChild()).getRequired() == 0){

                                String[] emptyAns = new String[4];
                                emptyAns[0] = question;
                                emptyAns[1]="";
                                emptyAns[2]="details";
                                emptyAns[3]="";
                                addingtojson(emptyAns, mViewFlipper.getDisplayedChild());
                            }else{
                                Error_Dialog.show("Please Select the Answer", AdminSurveyRoot.this);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                }

              /*  if (questionsize > 1)
                    shownextquestion();*/
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (myProgress > 1) {
                    up.setVisibility(View.VISIBLE);
                    if (myProgress == 2)
                        back.setVisibility(View.INVISIBLE);
                    myProgress--;
                    pb.setProgress(myProgress);
                    submitanswer.setText("");
                    if (feedback1!=null)
                    feedback1.setText("");
                    mViewFlipper.showPrevious();
                }
            }
        });

    }
}
