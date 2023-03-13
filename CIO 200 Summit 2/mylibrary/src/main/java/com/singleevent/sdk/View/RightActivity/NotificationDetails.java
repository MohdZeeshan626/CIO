package com.singleevent.sdk.View.RightActivity;

import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.SpannableString;
import android.view.MenuItem;
import android.widget.TextView;


import com.singleevent.sdk.Custom_View.Util;
import com.singleevent.sdk.model.AppDetails;
import com.singleevent.sdk.model.Notifications;
import com.singleevent.sdk.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import io.paperdb.Paper;

/**
 * Created by Admin on 6/28/2017.
 */

public class NotificationDetails extends AppCompatActivity {

    AppDetails appDetails;
    Notifications item;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.s_notification);
        appDetails = Paper.book().read("Appdetails");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));
        setSupportActionBar(toolbar);

        Bundle extras = getIntent().getExtras();
        if (extras == null)
            finish();

        // getting agenda details from fragment
        item = (Notifications) getIntent().getSerializableExtra("NotificationView"); //Obtaining data

        TextView txttitle = (TextView) findViewById(R.id.title);
        TextView txtmsg = (TextView) findViewById(R.id.msg);
        TextView time = (TextView) findViewById(R.id.time);

        txttitle.setText(item.getTitle());
        txtmsg.setText(item.getMessage());

        String zone, timeZone;
        timeZone = appDetails.getTimezone();
        if (timeZone == "") {
            zone = "Asia/Calcutta";
        } else if (timeZone.equalsIgnoreCase("IST")) {
            zone = "Asia/Calcutta";
        } else if (timeZone.equalsIgnoreCase("PST")) {
            zone = "America/Los_Angeles";
        } else if (timeZone.equalsIgnoreCase("EST")) {
            zone = "America/New_York";
        } else if (timeZone.equalsIgnoreCase("CST")) {
            zone = "America/Chicago";
        } else {
            zone = "Asia/Calcutta";
        }
        long timediff = TimeZone.getTimeZone(zone.toString()).getOffset(Calendar.getInstance().getTimeInMillis());
        String time_str = Calculatetime(item.getNotification_time());
        time.setText(CalculateMonth(item.getNotification_time()) + " at " + time_str);


    }

    private String CalculateMonth(long milli) {

        SimpleDateFormat myFormat = new SimpleDateFormat("MMM dd, yyyy");
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(milli);
        return myFormat.format(cal.getTime());
    }

    private String Calculatetime(long milli) {

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(milli);
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        return formatter.format(cal.getTime());
      /*  SimpleDateFormat myFormat = new SimpleDateFormat("hh:mm a");
        String zone, timeZone;
        timeZone = appDetails.getTimezone();
        if (timeZone == "") {
            zone = "Asia/Calcutta";
        } else if (timeZone.equalsIgnoreCase("IST")) {
            zone = "Asia/Calcutta";
        } else if (timeZone.equalsIgnoreCase("PST")) {
            zone = "America/Los_Angeles";
        } else if (timeZone.equalsIgnoreCase("EST")) {
            zone = "America/New_York";
        } else if (timeZone.equalsIgnoreCase("CST")) {
            zone = "America/Chicago";
        } else {
            zone = "Asia/Calcutta";
        }

        *//* date formatter in local timezone *//*
        myFormat.setTimeZone(TimeZone.getTimeZone(zone));
        return myFormat.format(milli);*/
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
}
