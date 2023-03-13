package com.webmobi.gecmedia;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;

//import com.crashlytics.android.Crashlytics;
//import com.crashlytics.android.core.CrashlyticsCore;
import com.singleevent.sdk.model.AppDetails;
import com.webmobi.gecmedia.CustomViews.SessionAlert;

//import io.fabric.sdk.android.Fabric;
import io.paperdb.Paper;

/**
 * Created by Admin on 6/19/2017.
 */

public class SessionExpired extends Activity {

    AppDetails appDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Paper.init(this);
        appDetails = Paper.book().read("Appdetails");
        Paper.book().delete("Islogin");

        displayAlert(appDetails.getTheme_color(), getIntent().getExtras());

        configureCrashReports();
    }

    private void configureCrashReports() {
//        CrashlyticsCore core = new CrashlyticsCore.Builder().disabled(com.singleevent.sdk.BuildConfig.DEBUG).build();
//        Fabric.with(this, new Crashlytics.Builder().core(core).build());
    }

    private void displayAlert(String theme_color, Bundle extras) {
        SessionAlert dc = new SessionAlert(Color.parseColor(theme_color),
                Color.parseColor("#ffffff"), extras, SessionExpired.this);


        dc.showDialog();
    }


}