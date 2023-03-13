package com.singleevent.sdk.View.LeftActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
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

import com.android.volley.AuthFailureError;
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
import com.singleevent.sdk.model.Polling.Report;
import com.singleevent.sdk.model.Polling.polling;
import com.singleevent.sdk.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import io.paperdb.Paper;

/**
 * Created by Admin on 5/31/2017.
 */

public class PollingItems extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    AppDetails appDetails;
    int pos, questionsize, itempos,polltypeid;
    private String title, toJson, answer, pollName, dateTime, Question,polltype;
    private String sq;
    private ArrayList<Events> events = new ArrayList<Events>();
    Events e;
    ArrayList<Report> surveylist = new ArrayList<>();
    private ViewFlipper mViewFlipper;
    TextView submitanswer;
    String[][] finalanswer;
    ArrayList<String> group[] = new ArrayList[115]; //Put the length of the array you need
    Gson converter = new Gson();
    int myProgress = 0, QuestionPosition;
    ProgressBar pb;
    TextView up, back;
    private SimpleDateFormat myFormat;
    ArrayList<Report> reportList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Paper.init(this);
        // setContentView(R.layout.s_surveyroot);
        setContentView(R.layout.act_pollingitems);
        appDetails = Paper.book().read("Appdetails");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));
        setSupportActionBar(toolbar);


        // getting the data from previous activity

        if (getIntent().getExtras() == null)
            finish();

        pos = getIntent().getExtras().getInt("Tabpos");
        itempos = getIntent().getExtras().getInt("Itempos");
        pollName = getIntent().getExtras().getString("pollName");
        title = getIntent().getExtras().getString("title");
        QuestionPosition = getIntent().getExtras().getInt("QuestionPosition");
        Question = getIntent().getExtras().getString("Question");
        reportList = (ArrayList<Report>) getIntent().getExtras().getSerializable("ReportList");
        events = Paper.book().read("Appevents");
        polltype=getIntent().getExtras().getString("polltype");
        polltypeid=getIntent().getExtras().getInt("polltypeid");
        e = events.get(0);

        surveylist.clear();
       /* for (int j = 0; j < e.getTabs(pos).getItems(itempos).getpollingSize(); j++) {
            surveylist.add(e.getTabs(pos).getItems(itempos).getpoll(j));
        }*/

        surveylist.add(reportList.get(QuestionPosition));

        mViewFlipper = (ViewFlipper) findViewById(R.id.viewflipper);
        submitanswer = (TextView) findViewById(R.id.txtcon);
        submitanswer.setTypeface(Util.regulartypeface(this));

        pb = (ProgressBar) findViewById(R.id.progressBar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            pb.setProgressTintList(ColorStateList.valueOf(Color.parseColor(appDetails.getTheme_color())));
        }

        submitanswer.setTextColor(Color.parseColor(appDetails.getTheme_color()));

        /*setting background of button*/

        ShapeDrawable shapedrawable = new ShapeDrawable();
        shapedrawable.setShape(new RectShape());
        shapedrawable.getPaint().setColor(Color.parseColor(appDetails.getTheme_color()));
        shapedrawable.getPaint().setStrokeWidth(5f);
        shapedrawable.getPaint().setStyle(Paint.Style.STROKE);
        submitanswer.setBackground(shapedrawable);


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
        back.setVisibility(View.INVISIBLE);
        myProgress++;
        pb.setProgress(myProgress);

        if (questionsize <= 1) {
            up.setVisibility(View.GONE);
            submitanswer.setText("Submit");
            submitanswer.setVisibility(View.VISIBLE);
        }else {
            submitanswer.setVisibility(View.GONE);
        }


        // setting the viewflipper

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        for (int i = 0; i < questionsize; i++) {

            LinearLayout l1 = new LinearLayout(this);
            l1.setOrientation(LinearLayout.VERTICAL);
            l1.setGravity(Gravity.TOP);

            TextView heading = new TextView(this);
            heading.setText("Question " + (i + 1));
            heading.setTextColor(Color.parseColor("#000000"));
            heading.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
            heading.setTypeface(Util.regulartypeface(this));
            params.setMargins(0, 0, 0, 20);
            heading.setLayoutParams(params);
            heading.setVisibility(View.GONE);
            l1.addView(heading);

            TextView title = new TextView(this);
            title.setText(surveylist.get(i).getQuestion());
            title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            title.setTextColor(Color.parseColor("#414548"));
            title.setTypeface(Util.boldtypeface(this));
            params.setMargins(0, 0, 0, 40);
            title.setLayoutParams(params);
            l1.addView(title);

            switch (surveylist.get(i).getType().toLowerCase()) {

                case "messagebox":
                    EditText feedback = new EditText(this);
                    feedback.setTextColor(getResources().getColor(R.color.black));
                    feedback.setGravity(Gravity.CENTER_HORIZONTAL);
                    feedback.addTextChangedListener(q);
                    int minHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
                    feedback.setBackground(getResources().getDrawable(R.drawable.s_aboutborder));

                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, minHeight);
                    lp.setMargins(2, 2, 2, 2);
                    l1.addView(feedback, lp);


                    break;

                case "multiple":
                    for (int j = 0; j < surveylist.get(i).getAnswer().length; j++) {
                        CheckBox checkedTextView = new CheckBox(this);
                        checkedTextView.setText(Util.applyFontToMenuItem(this, new SpannableString(surveylist.get(i).getAnswer()[j])));
                        checkedTextView.setButtonDrawable(Util.setsurveycheckbox(PollingItems.this, R.drawable.n2_ic_checkbox_checked, Color.parseColor(appDetails.getTheme_color())));
                        checkedTextView.setId(j);
                        checkedTextView.setPadding(10, 10, 10, 15);
                        LinearLayout.LayoutParams lc = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        lc.setMargins(2, 2, 2, 2);
                        checkedTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);

//                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                                    checkedTextView.setButtonTintList(getResources().getColorStateList(R.color.gray));
//                                }
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

                            radioButtonView.setButtonTintList(ColorStateList.valueOf(
                                    Color.parseColor( appDetails.getTheme_color() ) ) );
                        }
                        radioGroup1.addView(radioButtonView);

                    }
                    l1.addView(radioGroup1);
                    break;
            }


            mViewFlipper.addView(l1);
        }

        up.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));
        back.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));

        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (questionsize > 1)
                    shownextquestion();
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
                    submitanswer.setVisibility(View.GONE);
                    //submitanswer.setText("Continue");
                    mViewFlipper.showPrevious();
                }
            }
        });

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
        sq=toJson.replace(",",", ");
        if (mViewFlipper.getDisplayedChild() == surveylist.size() - 1) {

            if (Paper.book().read("Islogin", false)) {

                sendingfeddback(sq);

            }
            else {
                Intent i = new Intent(PollingItems.this, LoginActivity.class);
                startActivityForResult(i, 1);
            }
        } else
            shownextquestion();

    }

    private View.OnClickListener mThisButtonListener = new View.OnClickListener() {
        public void onClick(View v) {
            answer = ((RadioButton) v).getText().toString();
            group[mViewFlipper.getDisplayedChild()].clear();
            group[mViewFlipper.getDisplayedChild()].add(answer);

        }
    };

    private void shownextquestion() {
        if (myProgress == questionsize - 1) {
            up.setVisibility(View.INVISIBLE);
            submitanswer.setText("Submit");
            submitanswer.setVisibility(View.VISIBLE);
        } else {
            submitanswer.setText("");
            submitanswer.setVisibility(View.GONE);
        }//submitanswer.setText("Continue");
        back.setVisibility(View.VISIBLE);
        myProgress++;
        pb.setProgress(myProgress);
        mViewFlipper.showNext();
    }

    @Override
    public void onClick(View view) {
        String question = surveylist.get(mViewFlipper.getDisplayedChild()).getQuestion();

        switch (surveylist.get(mViewFlipper.getDisplayedChild()).getType().toLowerCase()) {

            case "messagebox":
                try {
                    if (group[mViewFlipper.getDisplayedChild()].size() > 0) {
                        int size = group[mViewFlipper.getDisplayedChild()].size();
                        String[] ans = new String[size + 1];
                        ans[0] = question;
                        ans[1] = group[mViewFlipper.getDisplayedChild()].get(0);
                        addingtojson(ans, mViewFlipper.getDisplayedChild());

                    } else
                        Error_Dialog.show("Please Give the FeedBack", PollingItems.this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case "multiple":

                if (group[mViewFlipper.getDisplayedChild()].size() > 0) {
                    int size = group[mViewFlipper.getDisplayedChild()].size();
                    String[] ans1 = new String[size + 1];
                    for (int i = 0; i < group[mViewFlipper.getDisplayedChild()].size(); i++) {
                        ans1[0] = question;
                        ans1[i + 1] = group[mViewFlipper.getDisplayedChild()].get(i);
                    }
                    addingtojson(ans1, mViewFlipper.getDisplayedChild());
                } else
                    Error_Dialog.show("Please Select the Answer", PollingItems.this);

                break;

            case "single":
                try {
                    if (group[mViewFlipper.getDisplayedChild()].size() > 0) {
                        int size = group[mViewFlipper.getDisplayedChild()].size();
                        String[] ans2 = new String[size + 1];
                        ans2[0] = question;
                        ans2[1] = group[mViewFlipper.getDisplayedChild()].get(0);
                        addingtojson(ans2, mViewFlipper.getDisplayedChild());
                    } else
                        Error_Dialog.show("Please Select the Answer", PollingItems.this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        SpannableString s = new SpannableString(title);
        setTitle(Util.applyFontToMenuItem(this, s));

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

    private void sendingfeddback(final String toJson) {
        dateTime = String.valueOf(System.currentTimeMillis());
        final ProgressDialog dialog = new ProgressDialog(PollingItems.this, R.style.MyAlertDialogStyle);
        dialog.setMessage("Sending Feedback...");
        dialog.show();
        String tag_string_req = "Schdule";
        String url = ApiList.PollingFeedback;
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                try {

                    JSONObject jObj = new JSONObject(response);


                    if (jObj.getBoolean("response")) {
                        Error_Dialog.show(jObj.getString("responseString"), PollingItems.this);
                        final Intent i = new Intent(getBaseContext(), LivePollingDetail.class);
                        Gson gson = new Gson();
                        ArrayList<Report> reportArrayList = new ArrayList<>();
                        if (jObj.getBoolean("submitted_answer")) {
                            JSONArray reportList = jObj.getJSONArray("report");

                            for (int count = 0; count < reportList.length(); count++) {
                                String reoortString = reportList.getJSONObject(count).toString();
                                Report report = gson.fromJson(reoortString, Report.class);
                                Log.d("TAG", report.toString());
                                reportArrayList.add(report);
                            }

                        }
                        //  ArrayList<Report> finalList = getfinalReportArrayListAfterSubmitTheAns(reportArrayList);
                        i.putExtra("ReportList", reportArrayList);
                        i.putExtra("submitted_answer", jObj.getBoolean("submitted_answer"));
                        i.setAction("PollingItem");
                        new CountDownTimer(2000, 1000) {

                        public void onTick(long millisUntilFinished) {
                        }

                            public void onFinish() {
                                /*After submitting ans polling detail activity will open*/


                                Bundle arg = new Bundle();
                                arg.putInt("Tabpos", pos);
                                arg.putInt("Itempos", itempos);
                                arg.putString("pollName", pollName);
                                arg.putInt("QuestionPosition", QuestionPosition);
                                arg.putString("Question", Question);
                                arg.putString("title", title);
                                i.putExtras(arg);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(i);
                                finish();
                            }
                        }.start();

                    } else {
                        if (jObj.getString("responseString").equalsIgnoreCase("Invalid token")) {
                            Intent i = new Intent();
                            i.putExtra("keyMessage", "Session Expired, Please Login ");
                            i.putExtra("keyAlert", "Session Expired");
                            i.setClassName(PollingItems.this.getPackageName(), PollingItems.this.getPackageName() + ".SessionExpired");
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);

                        } else
                            Error_Dialog.show(jObj.getString("responseString"), PollingItems.this);
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
                    Error_Dialog.show("Timeout", PollingItems.this);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, PollingItems.this), PollingItems.this);
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show("Please Check Your Internet Connection", PollingItems.this);
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
                params.put("pollName", pollName);
                params.put("submissiondate", dateTime);
                params.put("flag","update");
                params.put("poll_id",String.valueOf(surveylist.get(0).getId()));
                params.put("poll_type",polltype);
                params.put("poll_type_id",String.valueOf(polltypeid));
                params.put("eventdate", converlontostring(appDetails.getStartdate()));

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

    private String converlontostring(Long key) {

        myFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(key);
        return myFormat.format(cal.getTime());
    }

    private String decodeStringValue(String decodestr) {
        String result = null;

        try {
            result = new String(Base64.decode(decodestr, Base64.NO_WRAP), "UTF-16");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        return result;
    }

    private ArrayList<Report> getfinalReportArrayListAfterSubmitTheAns(ArrayList<Report> reportList){

        ArrayList<polling> pollingList = new ArrayList<>();
        ArrayList<Report> finalPollingList = new ArrayList<>();


        for (int j = 0; j < e.getTabs(pos).getItems(itempos).getpollingSize(); j++) {

            pollingList.add(e.getTabs(pos).getItems(itempos).getpoll(j));
        }

        /*Changes in Adapter list  */
        for (int position = 0; position < pollingList.size(); position++) {

            if (checkPollingId(pollingList.get(position).getId())) {
                int pollresPos;
                pollresPos = getReportListPosition(pollingList.get(position).getId());

                /*(true or false) ? (execute this if true) : (execute this if false)*/
                Report report = new Report(pollresPos != -1 ? reportList.get(pollresPos).getPollresult() : null,
                        pollingList.get(position).getId(), pollingList.get(position).getQuestion(),
                        pollingList.get(position).getAnswer(), pollingList.get(position).getType(),
                        pollingList.get(position).getDetail(), true);

                finalPollingList.add(report);
            } else {
                Report report = new Report(null, pollingList.get(position).getId(),
                        pollingList.get(position).getQuestion(),
                        pollingList.get(position).getAnswer(), pollingList.get(position).getType(),
                        pollingList.get(position).getDetail(), false);

                finalPollingList.add(report);
            }

        }//close outer for loop


        return finalPollingList;
    }


    private int getReportListPosition(int id) {

        for (int count = 0; count < reportList.size(); count++) {
            if (reportList.get(count).getPolling_id() != null &&
                    !reportList.get(count).getPolling_id().equals("") &&
                    id == Integer.valueOf(reportList.get(count).getPolling_id())) {

                return count;

            }

        }

        return -1;
    }

    private Boolean checkPollingId(int id) {

        for (int count = 0; count < reportList.size(); count++) {
            if (reportList.get(count).getPolling_id() != null &&
                    !reportList.get(count).getPolling_id().equals("") &&
                    id == Integer.valueOf(reportList.get(count).getPolling_id())) {

                return true;

            }

        }

        return false;
    }


}
