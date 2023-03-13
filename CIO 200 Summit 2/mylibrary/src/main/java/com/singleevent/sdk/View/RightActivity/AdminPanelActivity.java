package com.singleevent.sdk.View.RightActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.appcompat.widget.Toolbar;
import android.text.SpannableString;
import android.util.DisplayMetrics;
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
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.makeramen.roundedimageview.RoundedImageView;
import com.singleevent.sdk.ApiList;
import com.singleevent.sdk.App;
import com.singleevent.sdk.Custom_View.Error_Dialog;
import com.singleevent.sdk.Custom_View.Util;
import com.singleevent.sdk.Custom_View.VolleyErrorLis;
import com.singleevent.sdk.model.AppDetails;
import com.singleevent.sdk.model.Items;
import com.singleevent.sdk.R;
import com.singleevent.sdk.View.RightActivity.admin.SendNotification;
import com.singleevent.sdk.View.RightActivity.admin.adminSurvey.AdminSurveyActivity;
import com.singleevent.sdk.View.RightActivity.admin.adminSurvey.AdminSurveyRoot;
import com.singleevent.sdk.View.RightActivity.admin.beaconmanagement.RegisteredUserList;
import com.singleevent.sdk.View.RightActivity.admin.bulkprinting.BulkPrintingActivity;
import com.singleevent.sdk.View.RightActivity.admin.checkin.EventUsersActivity;
import com.singleevent.sdk.View.RightActivity.admin.feedback.FeedBackActivity;
import com.singleevent.sdk.View.RightActivity.admin.feeds.ui.FeedActivity;
import com.singleevent.sdk.View.RightActivity.admin.leadGeneration.ExhibitorLeadGenerationActivity;
import com.singleevent.sdk.View.RightActivity.admin.printsettings.PrintSettingsActivity;
import com.singleevent.sdk.View.RightActivity.admin.report.ExhibitorReportActivity;
import com.singleevent.sdk.View.RightActivity.admin.report.ReportActivity;
import com.singleevent.sdk.utils.DataBaseStorage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import io.paperdb.Paper;

/**
 * Created by webMOBI on 11/27/2017.
 */

public class AdminPanelActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private RoundedImageView ivEvent;
    private TextView tvEventName, tvLocation;
    private float dpWidth;
    AppDetails appDetails;
    ImageView ivCheckIn, ivSendNotification, ivLeadGeneration, iv_admin_survey, ivBulkPrinting, ivActivityFeed,
            ivLeadGeneration2, ivReports, ivReports2, iv_exhi_admin_survey, iv_print_settings, ivBeaconManagement;
    private CardView view_admin_panel, view_lead_generation;
    private CardView view_speakers_question;
    TextView tv_app_version_name;
    private ArrayList<Items> surveylist = new ArrayList<>();


    ImageView iv_feed;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Paper.init(this);

        ButterKnife.bind(this);
        setContentView(R.layout.activity_admin_panel);

        appDetails = Paper.book().read("Appdetails");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        ivEvent = (RoundedImageView) findViewById(R.id.ivEvent);
        tvEventName = (TextView) findViewById(R.id.tvEventName);
        tvLocation = (TextView) findViewById(R.id.tvLocation);
        tv_app_version_name = (TextView) findViewById(R.id.tv_app_version_name);
        view_admin_panel = (CardView) findViewById(R.id.view_admin_panel);
        view_lead_generation = (CardView) findViewById(R.id.view_lead_generation);
        view_speakers_question = (CardView) findViewById(R.id.view_speakers_question);
        ivLeadGeneration2 = (ImageView) findViewById(R.id.ivLeadGeneration2);
        //ivLeadGeneration = (ImageView) findViewById(R.id.ivLeadGeneration);
        ivSendNotification = (ImageView) findViewById(R.id.ivSendNotification);
        ivReports = (ImageView) findViewById(R.id.ivReports);
        ivReports2 = (ImageView) findViewById(R.id.ivReports2);
        ivCheckIn = (ImageView) findViewById(R.id.ivCheckIn);
        ivActivityFeed = (ImageView) findViewById(R.id.iv_activity_feed);
        iv_admin_survey = (ImageView) findViewById(R.id.iv_admin_survey);
        iv_exhi_admin_survey = (ImageView) findViewById(R.id.iv_exhi_admin_survey);
        iv_print_settings = (ImageView) findViewById(R.id.ivPrintSettings);
        ivBulkPrinting = (ImageView) findViewById(R.id.ivBulkPrinting);
        ivBeaconManagement = (ImageView) findViewById(R.id.ivBeaconManagement);
        iv_feed = (ImageView) findViewById(R.id.iv_feed);
        tvEventName.setText(appDetails.getAppName());
        tvLocation.setText(appDetails.getLocation());
        toolbar.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));
        setSupportActionBar(toolbar);
        //listener for admin panel
        ivCheckIn.setOnClickListener(this);
        ivSendNotification.setOnClickListener(this);
        iv_admin_survey.setOnClickListener(this);
        iv_exhi_admin_survey.setOnClickListener(this);
        iv_print_settings.setOnClickListener(this);
        ivBulkPrinting.setOnClickListener(this);
        iv_feed.setOnClickListener(this);
        ivBeaconManagement.setOnClickListener(this);
        //ivLeadGeneration.setOnClickListener(this);
        ivReports.setOnClickListener(this);
        ivActivityFeed.setOnClickListener(this);

        // listener for exhibitor
        ivReports2.setOnClickListener(this);
        ivLeadGeneration2.setOnClickListener(this);
        view_speakers_question.setOnClickListener(this);


        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            finish();
        }

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        dpWidth = displayMetrics.widthPixels * 0.25F;
        RelativeLayout.LayoutParams imgParams = (RelativeLayout.LayoutParams) ivEvent.getLayoutParams();
        imgParams.width = (int) dpWidth;
        imgParams.height = (int) dpWidth;
      //Rou  ivEvent.setLayoutParams(imgParams);
        Glide.with(getApplicationContext())

                .load(appDetails.getAppLogo())
                .asBitmap()
                .placeholder(R.drawable.ic_wallpaper_black_24dp)
                .error(R.drawable.ic_wallpaper_black_24dp)
                .into(ivEvent);
        ivEvent.setCornerRadius(12,12,12,12);

        String admin_flag = Paper.book(appDetails.getAppId()).read("admin_flag", "");
        if (admin_flag.equals("exhibitor")) {

            if (Paper.book(appDetails.getAppId()).read("offAdminSurveyAns", new HashMap<String, JSONObject>()).size() > 0) {

                sendingfeddback("not needed");
            }


            view_admin_panel.setVisibility(View.GONE);
            view_lead_generation.setVisibility(View.VISIBLE);
        } else if (admin_flag.equals("admin")) {

            if (Paper.book(appDetails.getAppId()).read("offAdminSurveyAns", new HashMap<String, JSONObject>()).size() > 0) {

                sendingfeddback("not needed");
            }

            view_admin_panel.setVisibility(View.VISIBLE);
            view_lead_generation.setVisibility(View.GONE);
        } else {
            view_admin_panel.setVisibility(View.GONE);
            view_lead_generation.setVisibility(View.GONE);
            finish();
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        SpannableString s = new SpannableString("Admin Panel");
        setTitle(Util.applyFontToMenuItem(this, s));
        tv_app_version_name.setText("Version: " + DataBaseStorage.getAppVersionName(this));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return true;


    }

    @Override
    public void onClick(View v) {
        Intent intent;
        int i = v.getId();
        if (i == R.id.ivCheckIn) {
            intent = new Intent(this, EventUsersActivity.class);
            startActivity(intent);


        } else if (i == R.id.ivSendNotification) {
            intent = new Intent(this, SendNotification.class);
            startActivity(intent);


        } else if (i == R.id.ivReports) {
            intent = new Intent(this, ReportActivity.class);
            startActivity(intent);


        } else if (i == R.id.ivReports2) {
            intent = new Intent(this, ExhibitorReportActivity.class);
            startActivity(intent);


            /*case R.id.ivLeadGeneration:

                //calling lead generation for admin
                intent = new Intent(this, AdminLeadGenerationActivity.class);
                startActivity(intent);

                break;*/
        } else if (i == R.id.iv_admin_survey) {
            intent = new Intent(this, AdminSurveyActivity.class);
            startActivity(intent);

        } else if (i == R.id.ivLeadGeneration2) {//calling lead generation activiy for exhibitor
            intent = new Intent(this, ExhibitorLeadGenerationActivity.class);
            startActivity(intent);


        } else if (i == R.id.iv_exhi_admin_survey) {

            getAdminSurveyQuestions();

        } else if (i == R.id.ivPrintSettings) {

            intent = new Intent(this, PrintSettingsActivity.class);
            startActivity(intent);

        } else if (i == R.id.iv_feed) {
            intent = new Intent(this, FeedBackActivity.class);
            intent.setAction("admin_feedback_action");
            startActivity(intent);
        } else if (i == R.id.ivBulkPrinting) {
            intent = new Intent(this, BulkPrintingActivity.class);
            startActivity(intent);
        } else if (i == R.id.ivBeaconManagement) {
            intent = new Intent(this, RegisteredUserList.class);
            startActivity(intent);
        }else if(i==R.id.iv_activity_feed){
            intent = new Intent( this, FeedActivity.class);
            intent.setAction("admin_panel");
            startActivity(intent);
        }
    }

    private void startSurvey() {
        Bundle bundle = new Bundle();
        bundle.putSerializable("AttendeeUser", null);
        bundle.putSerializable("surveyqueslist", surveylist);
        startActivity(new Intent(AdminPanelActivity.this, AdminSurveyRoot.class).putExtras(bundle));
    }


    private void getAdminSurveyQuestions() {
        surveylist.clear();
        final ProgressDialog dialog = new ProgressDialog(AdminPanelActivity.this,
                R.style.MyAlertDialogStyle);
        dialog.setMessage("Getting Survey Questions...");
        dialog.show();

        String url = ApiList.GetAdminSurvey;

        String tag_string_req = "Survey";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                if (dialog.isShowing())
                    dialog.dismiss();
                try {
                    System.out.println(response);
                    JSONObject jObj = new JSONObject(response);


                    if (jObj.getBoolean("response")) {
                        //  Error_Dialog.show(jObj.getString("responseString"), AdminSurveyRoot.this);
                        Gson gson = new Gson();
                        JSONArray jsonArray = jObj.getJSONArray("responseString");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            /* AdminSurveyModel model = gson.fromJson(obj.toString(),AdminSurveyModel.class);*/
                            Items model = gson.fromJson(obj.toString(), Items.class);
                            Log.d("HII", model.toString());
                            surveylist.add(model);
                        }

                        //start survey activity
                        startSurvey();
                        // initView();

                    } else {
                        if (jObj.getString("responseString").equalsIgnoreCase("Invalid token")) {
                            Error_Dialog.show("Session is Expired Please Login", AdminPanelActivity.this);
                        } else
                            Error_Dialog.show(jObj.getString("responseString"), AdminPanelActivity.this);
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
                    Error_Dialog.show("Timeout", AdminPanelActivity.this);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, AdminPanelActivity.this),
                            AdminPanelActivity.this);
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show("Please Check Your Internet Connection", AdminPanelActivity.this);
                }

            }


        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userid", Paper.book().read("userId", ""));
                params.put("appid", appDetails.getAppId());
                params.put("adminsurvey_flag", "1");
                params.put("adminsurvey_type", "exhibitor");

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

    private void sendingfeddback(final String toJson) {

        //getting offline survey answers with user details
        final JSONArray retriveJSONArray;
        retriveJSONArray = new JSONArray();

        HashMap<String, JSONObject> retriveofLinUsrAnsList = new HashMap<>();
        retriveofLinUsrAnsList = Paper.book(appDetails.getAppId()).read("offAdminSurveyAns", new HashMap<String, JSONObject>());
        for (Map.Entry<String, JSONObject> entry : retriveofLinUsrAnsList.entrySet()) {

            String key = entry.getKey();
            JSONObject value = entry.getValue();
            retriveJSONArray.put(value);

        }

        final ProgressDialog dialog = new ProgressDialog(AdminPanelActivity.this, R.style.MyAlertDialogStyle);
        dialog.setMessage("Please Wait...");
        dialog.show();
        String tag_string_req = "Post_Survey";
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
                        Error_Dialog.show(jObj.getString("responseString"), AdminPanelActivity.this);
                        /*After submitting all the answers , i am deleting from local storage*/
                        Paper.book(appDetails.getAppId()).write("offAdminSurveyAns", new HashMap<String, JSONObject>());
                        /*here store all submitted survey's attendee list*/
                        JSONArray jsonArray = jObj.getJSONArray("submitted_users");
                        ArrayList<String> userIds = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject object = jsonArray.getJSONObject(i);
                            userIds.add(object.getString("userid"));

                        }

                        //storing all user ids in paper db
                        if (userIds.size() > 0)
                            Paper.book(appDetails.getAppId()).write("AdminAttendeesUsers", userIds);


                    } else {
                        if (jObj.getString("responseString").equalsIgnoreCase("Invalid token")) {
                            Error_Dialog.show("Session is Expired Please Login", AdminPanelActivity.this);
                        } else
                            Error_Dialog.show(jObj.getString("responseString"), AdminPanelActivity.this);


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
                    Error_Dialog.show("Timeout", AdminPanelActivity.this);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error,
                            AdminPanelActivity.this), AdminPanelActivity.this);
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                   /* Error_Dialog.show("Survey Submitted Offline.", AdminPanelActivity.this);*/

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
}
