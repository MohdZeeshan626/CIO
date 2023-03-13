package com.singleevent.sdk.View.RightActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
//import com.google.android.gms.vision.text.Line;
import com.google.gson.Gson;
import com.singleevent.sdk.ApiList;
import com.singleevent.sdk.App;
import com.singleevent.sdk.Custom_View.Error_Dialog;
import com.singleevent.sdk.Custom_View.Util;
import com.singleevent.sdk.Custom_View.VolleyErrorLis;
import com.singleevent.sdk.model.AppDetails;
import com.singleevent.sdk.model.Notifications;
import com.singleevent.sdk.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

import cyd.awesome.material.AwesomeText;
import io.paperdb.Paper;

/**
 * Created by Admin on 6/27/2017.
 */

public class Notification extends AppCompatActivity {

    AppDetails appDetails;
    LinearLayout contents;
    TextView nosch;
    ArrayList<Notifications> notifications;
    String appid;
    String isFromNotifi = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Paper.init(this);
        setContentView(R.layout.s_mysch);
        appDetails = Paper.book().read("Appdetails");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));
        setSupportActionBar(toolbar);
        notifications = new ArrayList<>();

        Bundle extras = getIntent().getExtras();

        if (extras == null) {
            appid = appDetails.getAppId();
            isFromNotifi = "";
        } else {
            appid = extras.getString("appid");
            isFromNotifi = extras.getString("isFromNotifi");
        }
        if (appid == null || appid.equalsIgnoreCase(""))
            appid = appDetails.getAppId();

        contents = (LinearLayout) findViewById(R.id.contains);
        nosch = (TextView) findViewById(R.id.noitems);
        nosch.setText("No Notifications");

        getnotification();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        /*if (!(isFromNotifi==null||isFromNotifi.equalsIgnoreCase(""))) {
            finish();
        } else
           finish();*/
        finish();
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
    protected void onResume() {
        super.onResume();

        SpannableString s = new SpannableString("Notifications");
        setTitle(Util.applyFontToMenuItem(this, s));

    }


    private void getnotification() {

        final ProgressDialog pDialog = new ProgressDialog(Notification.this, R.style.MyAlertDialogStyle);
        pDialog.setMessage("Loading ...");
        pDialog.show();


        String tag_string_req = "Notifications";
        String url = ApiList.Notification + appid + "&userid=" + Paper.book(appDetails.getAppId()).read("userId") + "&device_type=android";
        StringRequest strReq = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                pDialog.dismiss();
                JSONObject jObj = null;
                try {
                    jObj = new JSONObject(response);
                    if (jObj.getBoolean("response")) {
                        parseuser(jObj.getJSONArray("responseString"));
                    } else {
                        nosch.setVisibility(View.VISIBLE);
                        contents.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                if (error instanceof TimeoutError) {
                    Error_Dialog.show("Timeout", Notification.this);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, Notification.this), Notification.this);
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show("Please Check Your Internet Connection", Notification.this);
                }
            }
        });


        App.getInstance().addToRequestQueue(strReq, tag_string_req);
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
    }

    private void parseuser(JSONArray responseString) throws JSONException {
        notifications.clear();
        Gson gson = new Gson();

        JSONArray sendmsg = responseString;
        for (int i = 0; i < sendmsg.length(); i++) {
            String eventString = sendmsg.getJSONObject(i).toString();
            Notifications obj = gson.fromJson(eventString, Notifications.class);
            notifications.add(obj);
        }


        Collections.sort(notifications, new Comparator<Notifications>() {
            @Override
            public int compare(Notifications o1, Notifications o2) {
                // ## Ascending order
                // return Integer.valueOf(obj1.getLeads()).compareTo(obj2.getLeads()); // To compare integer values
                // ## Descending order
                // return obj2.firstName.compareToIgnoreCase(obj1.firstName); // To compare string values

                return Long.valueOf(o2.getNotification_time()).compareTo(o1.getNotification_time());
            }
        });


        if (notifications.size() > 0)
            shownotification();
        else {
            nosch.setVisibility(View.VISIBLE);
            contents.setVisibility(View.GONE);
        }

    }

    private static String tempDate = "";

    private void shownotification() {

        contents.removeAllViews();
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for (int i = 0; i < notifications.size(); i++) {
            final Notifications not = notifications.get(i);
            View child = inflater.inflate(R.layout.s_notificationview, null);
            LinearLayout linear_layout_date = (LinearLayout) child.findViewById(R.id.linear_layout_date);
            TextView txtMonth = (TextView) child.findViewById(R.id.month);
            TextView date = (TextView) child.findViewById(R.id.date);
            TextView txttitle = (TextView) child.findViewById(R.id.title);
            TextView txtmsg = (TextView) child.findViewById(R.id.msg);
            TextView time = (TextView) child.findViewById(R.id.time);
            AwesomeText rightarrow = (AwesomeText) child.findViewById(R.id.rightarrow);
            txttitle.setTypeface(Util.boldtypeface(Notification.this));
            txtmsg.setTypeface(Util.regulartypeface(Notification.this));
            time.setTypeface(Util.lighttypeface(Notification.this));

            linear_layout_date.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));
          //  rightarrow.setTextColor(Color.parseColor(appDetails.getTheme_color()));
            txttitle.setText(not.getTitle());
            txtmsg.setText(not.getMessage());
            time.setText(calculateTime(not.getNotification_time()));
            String getMonth = CalculateMonth(not.getNotification_time());
            txtMonth.setText(getMonth);
            date.setText(getMonth+" "+calculateDate(not.getNotification_time()));


           /* //logic for showing content based on date
            if (i==0){
                tempDate = CalculateMonth((not.getNotification_time()));
                time.setVisibility(View.VISIBLE);
            }else if ( i!=0 && !tempDate.equalsIgnoreCase(CalculateMonth(not.getNotification_time()))){
                tempDate =CalculateMonth(not.getNotification_time());
                time.setVisibility(View.VISIBLE);
            }else {
                time.setVisibility(View.GONE);
            }*/


            child.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Bundle args = new Bundle();
                    args.putSerializable("NotificationView", not);

                    Intent i = new Intent(Notification.this, NotificationDetails.class);
                    i.putExtras(args);
                    startActivity(i);
                }
            });

            contents.addView(child);
        }

    }

    private String CalculateMonth(long milli) {

        SimpleDateFormat myFormat = new SimpleDateFormat("MMM");
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(milli);
        return myFormat.format(cal.getTime());
    }

    private String calculateDate(long milli) {

        SimpleDateFormat myFormat = new SimpleDateFormat("dd");
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(milli);
        return myFormat.format(cal.getTime());
    }

    private String calculateTime(long milli) {

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(milli);
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");

        return formatter.format(cal.getTime());
    }
}
