package com.singleevent.sdk.View.LeftActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

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
import com.singleevent.sdk.model.Polling.Report;
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

public class LivePollingDetail extends AppCompatActivity {

    private Toolbar toolbar;
    private AppDetails appDetails;
    private TextView tv_anspoll, tv_totalNoOfAns, tv_poll_head;
    private String ans_no, pollName, title;
    private ArrayList<Report> reportArrayList = new ArrayList<>();
    private int position, pos, itempos, QuestionPosition;
    private LinearLayout listview;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Paper.init(this);
        setContentView(R.layout.act_poll_details);

        appDetails = Paper.book().read("Appdetails");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));


        tv_anspoll = (TextView) findViewById(R.id.tv_anspoll);

        tv_poll_head = (TextView) findViewById(R.id.tv_poll_head);
        tv_totalNoOfAns = (TextView) findViewById(R.id.tv_totalNoOfAns);
        listview = (LinearLayout) findViewById(R.id.container);

        if (getIntent().getExtras() == null) {
            finish();
        }

        pos = getIntent().getExtras().getInt("Tabpos");
        itempos = getIntent().getExtras().getInt("Itempos");
        pollName = getIntent().getExtras().getString("pollName");
        title = getIntent().getExtras().getString("title");
        reportArrayList = (ArrayList<Report>) getIntent().getExtras().getSerializable("ReportList");

        if (getIntent().getAction() != null && getIntent().getAction().equals("PollingItem")) {
            QuestionPosition = 0;
            position = 0;
            tv_poll_head.setText(getIntent().getExtras().getString("Question"));
        } else {
            QuestionPosition = getIntent().getExtras().getInt("QuestionPosition");
            position = getIntent().getExtras().getInt("QuestionPosition");
            tv_poll_head.setText(reportArrayList.get(position).getQuestion());
        }


        settingAnslist();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getUserFeedbackDetails();

        }

        return true;

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        getUserFeedbackDetails();

    }

    @Override
    protected void onResume() {
        super.onResume();
        SpannableString s = new SpannableString("Poll Details");
        setTitle(Util.applyFontToMenuItem(this, s));
    }


    private void settingAnslist() {

        listview.removeAllViews();
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for (int count = 0; count < reportArrayList.get(QuestionPosition).getPollresult().length; count++) {


            View child = inflater.inflate(R.layout.s_polling_view, null);
            final TextView option1 = (TextView) child.findViewById(R.id.option1);
            final TextView tvTotal_1st = (TextView) child.findViewById(R.id.tvTotal_1st);
            final TextView tvPercent_1st = (TextView) child.findViewById(R.id.tvPercent_1st);
            ProgressBar progressbar = (ProgressBar) child.findViewById(R.id.progressbar);
            option1.setText(reportArrayList.get(position).getPollresult()[count].getFeedback());

            int total = 0;
            if (reportArrayList.get(position).getTotal_count() != null && !reportArrayList.get(position).getTotal_count().equals("")) {
                total = Integer.valueOf(reportArrayList.get(position).getTotal_count());
            }
            int subtotal = 0;
            if (reportArrayList.get(position).getPollresult()[count].getCount() != null &&
                    !reportArrayList.get(position).getPollresult()[count].getCount().equals("")) {
                subtotal = Integer.valueOf(reportArrayList.get(position).getPollresult()[count].getCount());
            }

            tvTotal_1st.setText(subtotal + "/" + total);

            String ans = "";
            tv_totalNoOfAns.setText("Total numbers of answers:" + total);
            tvTotal_1st.setText(reportArrayList.get(position).getPollresult()[count].getCount() + "/" + total);

           /* ans += reportArrayList.get(position).getPollresult()[count].getFeedback() ;
            tv_anspoll.setText("Your answer: "+ans);*/
            progressbar.setMax(total);
            progressbar.setProgress(Integer.valueOf(reportArrayList.get(position).getPollresult()[count].getCount()));

            /*calculate percentage*/

            float perc = (float) (subtotal * 100) / total;

            tvPercent_1st.setText(String.format("%.2f", perc) + "%");

            listview.addView(child);
        }


    }


    private void getUserFeedbackDetails() {

        final ProgressDialog dialog = new ProgressDialog(this, R.style.MyAlertDialogStyle);
        dialog.setMessage(" Updating...");
        dialog.show();
        String tag_string_req = "check / update";
        String url = ApiList.PollingFeedback;
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                if (!LivePollingDetail.this.isFinishing() && dialog.isShowing())
                    dialog.dismiss();
                try {

                    JSONObject jObj = new JSONObject(response);


                    if (jObj.getBoolean("response")) {

                        Gson gson = new Gson();
                        ArrayList<Report> reportArrayList = new ArrayList<>();
                        if (jObj.getBoolean("submitted_answer")) {
                            JSONArray reportList = jObj.getJSONArray("report");

                            for (int i = 0; i < reportList.length(); i++) {
                                String reoortString = reportList.getJSONObject(i).toString();
                                Report report = gson.fromJson(reoortString, Report.class);
                                Log.d("TAG", report.toString());
                                reportArrayList.add(report);
                            }

                        }
                        Intent i = new Intent(getApplicationContext(), PollingQuestions.class);
                        Bundle arg = new Bundle();
                        arg.putInt("Tabpos", pos);
                        arg.putInt("Itempos", itempos);
                        arg.putString("pollName", pollName);
                        arg.putString("title", title);
                        i.putExtra("ReportList", reportArrayList);
                        i.putExtra("submitted_answer", jObj.getBoolean("submitted_answer"));
                        i.putExtras(arg);
                        setIntent(i);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                        finish();
                       /* Error_Dialog.show(jObj.getString("responseString"), LivePollingDetail.this);*/

                    } else {
                        if (jObj.getString("responseString").equalsIgnoreCase("Invalid token")) {
                            Intent i = new Intent();
                            i.putExtra("keyMessage", "Session Expired, Please Login ");
                            i.putExtra("keyAlert", "Session Expired");
                            i.setClassName(getPackageName(), getPackageName() + ".SessionExpired");
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);

                        } else
                            Error_Dialog.show(jObj.getString("responseString"), LivePollingDetail.this);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if (!LivePollingDetail.this.isFinishing() && dialog.isShowing())
                    dialog.dismiss();
                if (error instanceof TimeoutError) {
                    Error_Dialog.show("Timeout", LivePollingDetail.this);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, LivePollingDetail.this), LivePollingDetail.this);
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show("Please Check Your Internet Connection", LivePollingDetail.this);
                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userid", Paper.book().read("userId", ""));
                params.put("username", Paper.book().read("username", ""));
                params.put("email", Paper.book().read("email", ""));
                params.put("appId", appDetails.getAppId());
                params.put("flag", "check");
                params.put("pollName", pollName);
                params.put("submissiondate", String.valueOf(System.currentTimeMillis()));
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

        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(key);
        return myFormat.format(cal.getTime());
    }
}
