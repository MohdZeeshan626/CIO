package com.singleevent.sdk.View.LeftActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.gson.Gson;
import com.singleevent.sdk.ApiList;
import com.singleevent.sdk.App;
import com.singleevent.sdk.Custom_View.Error_Dialog;
import com.singleevent.sdk.Custom_View.Util;
import com.singleevent.sdk.Custom_View.VolleyErrorLis;
import com.singleevent.sdk.model.Agenda.Agendadetails;
import com.singleevent.sdk.model.AppDetails;
import com.singleevent.sdk.model.Events;
import com.singleevent.sdk.model.Items;
import com.singleevent.sdk.R;
import com.singleevent.sdk.model.LocalArraylist.agendaspeakerlist;
import com.singleevent.sdk.utils.DataBaseStorage;


import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import io.paperdb.Paper;

/**
 * Created by Admin on 5/31/2017.
 */

public class SurveyRoot extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    AppDetails appDetails;
    int pos, questionsize;
    TextView b1,b2,b3,b4,b5,b6,d1,d2,d3,d4,d5,d6;
    LinearLayout sch;
    private String title,checkvalue, toJson, answer, dateTime;
    ArrayList<Items> surveylist = new ArrayList<>();
    private ArrayList<Events> events = new ArrayList<Events>();
    Events e;
    private ViewFlipper mViewFlipper;
    TextView submitanswer;
    String[][] finalanswer;
    ArrayList<String> group[]; //Put the length of the array you need
    Gson converter = new Gson();
    int myProgress = 0;
    ProgressBar pb;
    TextView up, back;
    private SimpleDateFormat myFormat;
    String feed_back1 = "";
    EditText feedback1;
    Agendadetails item;
    private String survey_type = "";
    private String agendaId = "";
    private String userId;
    private String token;
    private TextView tv_survey_heading;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Paper.init(this);
        setContentView(R.layout.s_surveyroot);


        appDetails = Paper.book().read("Appdetails");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));
        tv_survey_heading = (TextView) findViewById(R.id.tv_survey_heading);
        sch=(LinearLayout)findViewById(R.id.sch);
        /*b1=(TextView)findViewById(R.id.b1);
        b2=(TextView)findViewById(R.id.b2);
        b3=(TextView)findViewById(R.id.b3);
        b4=(TextView)findViewById(R.id.b4);
        b5=(TextView)findViewById(R.id.b5);
        b6=(TextView)findViewById(R.id.b6);
        d1=(TextView)findViewById(R.id.d1);
        d2=(TextView)findViewById(R.id.d2);
        d3=(TextView)findViewById(R.id.d3);
        d4=(TextView)findViewById(R.id.d4);
        d5=(TextView)findViewById(R.id.d5);
        d6=(TextView)findViewById(R.id.d6);
*/

        setSupportActionBar(toolbar);


        // getting the data from previous activity

        if (getIntent().getExtras() == null)
            finish();

        pos = getIntent().getExtras().getInt("pos");
        title = getIntent().getExtras().getString("title");
        // getting agenda details from AgendaDetails.class activity
        item = (Agendadetails) getIntent().getSerializableExtra("Agendaview"); //Obtaining data
        // Agendadate = extras.getLong("Date");
        events = Paper.book().read("Appevents");
        e = events.get(0);

        surveylist.clear();

        //if item is not null, i am storing only those items that is for agenda survey only
        if (item != null) {//means this is agenda survey ,we need to add agendaId in submitting ans
            agendaId = String.valueOf(item.getAgendaId());
            checkvalue=item.getSurvey_checkvalue();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                tv_survey_heading.setText(Html.fromHtml("<b>Submit survey for </b>" + item.getTopic(), Html.FROM_HTML_MODE_COMPACT));
            } else {
                tv_survey_heading.setText(Html.fromHtml("<b>Submit survey for </b>" + item.getTopic()));
            }
            tv_survey_heading.setTypeface(Util.regulartypeface(this));
            survey_type = "agenda";
            for (int j = 0; j < e.getTabs(pos).getItemsSize(); j++) {
                if ((!e.getTabs(pos).getItems(j).getSurvey_type().equals("") && e.getTabs(pos).getItems(j).getSurvey_type().equalsIgnoreCase("agenda")))
                {
                    surveylist.add(e.getTabs(pos).getItems(j));
                    System.out.println("Survey Items : " + e.getTabs(pos).getItems(j).getSurvey_type());
                }
            }
        } else {
            //this is main survey so we need to add only global survey questions and ans
            survey_type = "global";
            for (int j = 0; j < e.getTabs(pos).getItemsSize(); j++) {
                if(e.getTabs(pos).getSub_type().equalsIgnoreCase("global")){
                    if ((!e.getTabs(pos).getItems(j).getSurvey_type().equals("") && e.getTabs(pos).getItems(j).getSurvey_type().equalsIgnoreCase("global"))&&
                            (e.getTabs(pos).getMod_display().equalsIgnoreCase("webmobi") || e.getTabs(pos).getMod_display().equalsIgnoreCase("mobile"))) {
                        surveylist.add(e.getTabs(pos).getItems(j));
                    }
                }

            }
        }
        group = new ArrayList[surveylist.size()];

        mViewFlipper = (ViewFlipper) findViewById(R.id.viewflipper);
        submitanswer = (TextView) findViewById(R.id.txtcon);
        submitanswer.setTypeface(Util.regulartypeface(this));

        pb = (ProgressBar) findViewById(R.id.progressBar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            pb.setProgressTintList(ColorStateList.valueOf(Color.parseColor(appDetails.getTheme_color())));
        }

        submitanswer.setTextColor(Color.parseColor(appDetails.getTheme_color()));


        submitanswer.setOnClickListener(this);
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

        // up.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));
        // back.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));

        back.setVisibility(View.INVISIBLE);
        myProgress++;
        pb.setProgress(myProgress);
        //   b1.setText(String.valueOf(myProgress));


        if (questionsize <= 1) {
            up.setVisibility(View.GONE);
            submitanswer.setText("Finish");
        }


        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for (int i = 0; i < questionsize; i++) {
            //   final agendaspeakerlist item = speakername.get(i);
            View child = inflater.inflate(R.layout.surveycircleui, null);
            TextView b1 = (TextView) child.findViewById(R.id.b1);
            TextView d1 = (TextView) child.findViewById(R.id.d1);
            if(i==0)
            {
                TextView t1=child.findViewById(R.id.b1);


                t1.setBackground(getResources().getDrawable(R.drawable.surveyround));
                t1.setText("1");
                t1.setTextColor(getResources().getColor(R.color.white));

            }
            if(i==questionsize-1){
                TextView t11 = (TextView) child.findViewById(R.id.d1);
                TextView d11 = (TextView) child.findViewById(R.id.b1);
                d11.setText(String.valueOf(questionsize));
                t11.setVisibility(View.GONE);
            }
            else{
                TextView d22 = (TextView) child.findViewById(R.id.b1);
                d22.setText(String.valueOf(i+1));

            }

            sch.addView(child);

        }




        // setting the viewflipper

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        for (int i = 0; i < questionsize; i++) {

            LinearLayout l1 = new LinearLayout(this);
            l1.setBackground(this.getResources().getDrawable(R.drawable.speakerbackground));
            l1.setPadding(10,10,10,10);
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
                    EditText feedback = new EditText(this);
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
                        checkedTextView.setButtonDrawable(Util.setsurveycheckbox(SurveyRoot.this, R.drawable.n2_ic_checkbox_checked, Color.parseColor(appDetails.getTheme_color())));
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
                        radioButtonView.setText(Util.applyFontToMenuItem(this, new SpannableString(surveylist.get(i).getAnswer()[j])));
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

            if (!surveylist.get(i).getType().equalsIgnoreCase("messagebox") && surveylist.get(i).getDetail().equalsIgnoreCase("Yes")) {
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
                /* -----------*/

                String question = surveylist.get(mViewFlipper.getDisplayedChild()).getQuestion();

                switch (surveylist.get(mViewFlipper.getDisplayedChild()).getType()) {

                    case "messagebox":
                        try {
                            if (group[mViewFlipper.getDisplayedChild()].size() > 0) {
                                int size = group[mViewFlipper.getDisplayedChild()].size();
                                String[] ans = new String[size + 1];
                                ans[0] = question;
                                ans[1] = group[mViewFlipper.getDisplayedChild()].get(0);
                                addingtojson(ans, mViewFlipper.getDisplayedChild());

                            } else
                                Error_Dialog.show("Please Give the FeedBack", SurveyRoot.this);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;

                    case "multiple":

                        if (group[mViewFlipper.getDisplayedChild()].size() > 0) {
                            int size = group[mViewFlipper.getDisplayedChild()].size();
                            String[] ans1 = new String[size + 3];
                            for (int i = 0; i < group[mViewFlipper.getDisplayedChild()].size(); i++) {
                                ans1[0] = question;
                                ans1[i + 1] = group[mViewFlipper.getDisplayedChild()].get(i);
                            }
                            ans1[size + 1] = "details";
                            ans1[size + 2] = feed_back1;
                            addingtojson(ans1, mViewFlipper.getDisplayedChild());
                        } else
                            Error_Dialog.show("Please Select the Answer", SurveyRoot.this);

                        break;

                    case "single":
                        try {
                            if (group[mViewFlipper.getDisplayedChild()].size() > 0) {
                                int size = group[mViewFlipper.getDisplayedChild()].size();
                                String[] ans2 = new String[size + 3];
                                ans2[0] = question;
                                ans2[1] = group[mViewFlipper.getDisplayedChild()].get(0);
                                ans2[2] = "details";
                                ans2[3] = feed_back1;

                                addingtojson(ans2, mViewFlipper.getDisplayedChild());
                            } else
                                Error_Dialog.show("Please Select the Answer", SurveyRoot.this);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                }

                /*------------*/


                /*if (questionsize > 1)
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
                    View view1 = sch.getChildAt(myProgress);

                    pb.setProgress(myProgress);

                    TextView t=view1.findViewById(R.id.b1);


                    //t.setText(String.valueOf(myProgress));
                    t.setBackground(getResources().getDrawable(R.drawable.surveyroundwhite));
                    t.setTextColor(getResources().getColor(R.color.black));

                    pb.setProgress(myProgress);

                    submitanswer.setText("");
                    mViewFlipper.showPrevious();

                }
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        token = Paper.book().read("token", "");
        if (token != null) {
            startThread();
        }
        SpannableString s = new SpannableString(title);
        setTitle(Util.applyFontToMenuItem(this, s));

    }

    private void sendOfflineSurveyAns() {

        String strJson = Paper.book(appDetails.getAppId()).read("offlineSurveyAns", "");
        //checking network connectivity & strJson is empty or not and survey_flag should be 1 to send
        if (DataBaseStorage.isInternetConnectivity(SurveyRoot.this) && !strJson.equals("")
        ) {
            String temp_email = Paper.book().read("Email");
            if (appDetails.getAppId().equalsIgnoreCase("e51674d0e7f74adc0936a69a834be9912ed4") && temp_email.equalsIgnoreCase("frank12@webmobi.in")) {
                Toast.makeText(SurveyRoot.this, "Survey Submited Successfully", Toast.LENGTH_SHORT).show();
            } else {
                sendingfeddback(strJson);
            }
        }
    }

    @Override
    public void onClick(View view) {

        try {
            String question = surveylist.get(mViewFlipper.getDisplayedChild()).getQuestion();

            switch (surveylist.get(mViewFlipper.getDisplayedChild()).getType()) {

                case "messagebox":
                    try {
                        if (group[mViewFlipper.getDisplayedChild()].size() > 0) {
                            int size = group[mViewFlipper.getDisplayedChild()].size();
                            String[] ans = new String[size + 1];
                            ans[0] = question;
                            ans[1] = group[mViewFlipper.getDisplayedChild()].get(0);
                            addingtojson(ans, mViewFlipper.getDisplayedChild());

                        } else
                            Error_Dialog.show("Please Give the FeedBack", SurveyRoot.this);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

                case "multiple":

                    if (group[mViewFlipper.getDisplayedChild()].size() > 0) {
                        int size = group[mViewFlipper.getDisplayedChild()].size();
                        String[] ans1 = new String[size + 3];
                        for (int i = 0; i < group[mViewFlipper.getDisplayedChild()].size(); i++) {
                            ans1[0] = question;
                            ans1[i + 1] = group[mViewFlipper.getDisplayedChild()].get(i);
                        }

                        ans1[size + 1] = "details";
                        if (feedback1 != null) {
                            ans1[size + 2] = feedback1.getText().toString();
                            feedback1.setText("");
                        } else {
                            ans1[size + 2] = "";
                        }


                        addingtojson(ans1, mViewFlipper.getDisplayedChild());
                    } else
                        Error_Dialog.show("Please Select the Answer", SurveyRoot.this);

                    break;

                case "single":
                    try {
                        if (group[mViewFlipper.getDisplayedChild()].size() > 0) {
                            int size = group[mViewFlipper.getDisplayedChild()].size();
                            String[] ans2 = new String[size + 3];
                            ans2[0] = question;
                            ans2[1] = group[mViewFlipper.getDisplayedChild()].get(0);
                            ans2[2] = "details";
                            if (feedback1 != null) {
                                ans2[3] = feedback1.getText().toString();
                                feedback1.setText("");
                            } else {
                                ans2[3] = "";
                            }

                       /* ans2[3] = feedback1.getText().toString();
                        feedback1.setText("");*/

                            addingtojson(ans2, mViewFlipper.getDisplayedChild());
                        } else
                            Error_Dialog.show("Please Select the Answer", SurveyRoot.this);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Sorry,can't submit", Toast.LENGTH_SHORT).show();
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
            System.out.println("Answer Submit : " + toJson);
            if (Paper.book().read("Islogin", false)) {
                String temp_email = Paper.book().read("Email");
                if (appDetails.getAppId().equalsIgnoreCase("e51674d0e7f74adc0936a69a834be9912ed4") && temp_email.equalsIgnoreCase("frank12@webmobi.in")) {
                    Toast.makeText(SurveyRoot.this, "Survey Submited Successfully", Toast.LENGTH_SHORT).show();
                } else {
                    sendingfeddback(toJson);
                }
            } else {
                Error_Dialog.show("Please Login to submit survey", SurveyRoot.this);
                /*Intent i = new Intent(SurveyRoot.this, LoginActivityDemo.class);
                startActivityForResult(i, 1);*/
            }
        } else
            shownextquestion();

    }

    private void sendingfeddback(final String toJson) {
        System.out.println("Answer Submit : " + toJson);
        dateTime = String.valueOf(System.currentTimeMillis());
        final ProgressDialog dialog = new ProgressDialog(SurveyRoot.this, R.style.MyAlertDialogStyle);
        dialog.setMessage("Sending Feedback...");
        dialog.show();
        String tag_string_req = "Schdule";
        String url = ApiList.SurveyFeedback;
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                try {
                    System.out.println(response);
                    JSONObject jObj = new JSONObject(response);


                    if (jObj.getBoolean("response")) {
                        Error_Dialog.show(jObj.getString("responseString"), SurveyRoot.this);
                        //storing all answers offline to send when internet is available
                        Paper.book(appDetails.getAppId()).write("offlineSurveyAns", "");
                        //after submitting survey flag has to be change so that next time user will not be able to got to survey agian.
                        Paper.book(appDetails.getAppId()).write("survey_flag", 1);

                    } else {
                        if (jObj.getString("responseString").equalsIgnoreCase("Invalid token")) {
                            Error_Dialog.show("Session is Expired Please Login", SurveyRoot.this);
                        } else
                            Error_Dialog.show(jObj.getString("responseString"), SurveyRoot.this);
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
                    Error_Dialog.show("Timeout", SurveyRoot.this);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, SurveyRoot.this), SurveyRoot.this);
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show(" Thank you. Survey has been saved successfully. ", SurveyRoot.this);

                    if (toJson.length() > 0) {
                        //storing all answers offline to send when internet is available
                        Paper.book(appDetails.getAppId()).write("offlineSurveyAns", toJson);
                        //after submitting survey flag has to be change so that next time user will not be able to got to survey agian.
                        Paper.book(appDetails.getAppId()).write("survey_flag", 1);
                    }
                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userid", Paper.book().read("userId", ""));
                params.put("username", Paper.book().read("username", ""));
                params.put("email", Paper.book().read("Email", ""));
                params.put("appId", appDetails.getAppId());
                params.put("feedback", toJson);
                params.put("flag","update");
                params.put("default_id",e.getTabs(pos).getCheckvalue());
                params.put("submissiondate", dateTime);
                params.put("eventdate", converlontostring(appDetails.getStartdate()));
                params.put("agenda_id", String.valueOf(agendaId));
                params.put("survey_type", survey_type);
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


        strReq.setShouldCache(false);
        App.getInstance().addToRequestQueue(strReq, tag_string_req);
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

    }

    private View.OnClickListener mThisButtonListener = new View.OnClickListener() {
        public void onClick(View v) {
            answer = ((RadioButton) v).getText().toString();
            if (feedback1 != null)
                feed_back1 = feedback1.getText().toString();

            group[mViewFlipper.getDisplayedChild()].clear();
            group[mViewFlipper.getDisplayedChild()].add(answer);

            if (feedback1 != null)
                feedback1.setText(feed_back1);

        }
    };

    private void shownextquestion() {

        if (myProgress == questionsize - 1) {
            up.setVisibility(View.INVISIBLE);
            submitanswer.setText("Finish");
        } else
            submitanswer.setText("");
        back.setVisibility(View.VISIBLE);
        View view = sch.getChildAt(myProgress);
        myProgress++;
        pb.setProgress(myProgress);

        TextView t=view.findViewById(R.id.b1);


        t.setText(String.valueOf(myProgress));
        t.setBackground(getResources().getDrawable(R.drawable.surveyround));
        t.setTextColor(getResources().getColor(R.color.white));

        /*if(myProgress==2) {
            b2.setText(String.valueOf(myProgress));
            b2.setBackground(getResources().getDrawable(R.drawable.surveyround));
        }
        if(myProgress==3) {
            b3.setText(String.valueOf(myProgress));
            b3.setBackground(getResources().getDrawable(R.drawable.surveyround));
        }
        if(myProgress==4) {
            b4.setText(String.valueOf(myProgress));
            b4.setBackground(getResources().getDrawable(R.drawable.surveyround));
        }
        if(myProgress==5) {
            b5.setText(String.valueOf(myProgress));
            b5.setBackground(getResources().getDrawable(R.drawable.surveyround));
        }
        if(myProgress==6) {
            b6.setText(String.valueOf(myProgress));
            b6.setBackground(getResources().getDrawable(R.drawable.surveyround));
        }*/
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
            String temp_email = Paper.book().read("Email");
            if (appDetails.getAppId().equalsIgnoreCase("e51674d0e7f74adc0936a69a834be9912ed4") && temp_email.equalsIgnoreCase("frank12@webmobi.in")) {
                Toast.makeText(SurveyRoot.this, "Survey Submited Successfully", Toast.LENGTH_SHORT).show();
            } else {
                sendingfeddback(toJson);
            }
        }
    }

    private String converlontostring(Long key) {

        myFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(key);
        return myFormat.format(cal.getTime());
    }

    private void startThread() {

        final String[] decryptedString = new String[1];
        Runnable r = new Runnable() {
            @Override
            public void run() {
                // your code here

                /*try {
                    FileInputStream fis = openFileInput(DataBaseStorage.F_I_L_ENCP2);
                    ObjectInputStream ois = new ObjectInputStream(fis);

                    byte[] decrypted = DataBaseStorage.decryptData((HashMap<String, byte[]>) ois.readObject(),
                            DataBaseStorage.token_pass);
                    if (decrypted != null) {
                        //decryptedString[0] = new String(decrypted);
                        token = new String(decrypted);
                    }
                    ois.close();

                } catch (Exception e) {

                    e.printStackTrace();
                }
*/
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        //sending offline data if it is not empty from Paper.b
                        sendOfflineSurveyAns();
                    }
                });
            }
        };

        Thread t = new Thread(r);
        t.start();
    }

}
