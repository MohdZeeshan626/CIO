package com;

import android.app.Application;

import com.singleevent.sdk.model.AppDetails;
import com.webmobi.gecmedia.MainActivity;

import java.util.Timer;
import java.util.TimerTask;

public class MyApplication extends Application {
    AppDetails appDetails;
    private Timer mActivityTransitionTimer;
    private TimerTask mActivityTransitionTimerTask;
    public boolean wasInBackground;
    private final long MAX_ACTIVITY_TRANSITION_TIME_MS = 2000;

    public void startActivityTransitionTimer() {
        this.mActivityTransitionTimer = new Timer();
        this.mActivityTransitionTimerTask = new TimerTask() {
            public void run() {
                MyApplication.this.wasInBackground = true;
                MainActivity m=new MainActivity();
                boolean hasToCheck=true;
                //checkupdate(appDetails.getAppId());
                if (m.getIntent().getExtras() != null) {
                    hasToCheck = m.getIntent().getExtras().getBoolean("hasToCheck");
                    if (appDetails.getForceUpdate() && hasToCheck) {
                        m.getIntent().removeExtra("hasToCheck");
                        m.checkupdate(appDetails.getAppId());

                    }
                }
            }
        };

        this.mActivityTransitionTimer.schedule(mActivityTransitionTimerTask,
                MAX_ACTIVITY_TRANSITION_TIME_MS);
    }

    public void stopActivityTransitionTimer() {
        if (this.mActivityTransitionTimerTask != null) {
            this.mActivityTransitionTimerTask.cancel();
        }

        if (this.mActivityTransitionTimer != null) {
            this.mActivityTransitionTimer.cancel();
        }

        this.wasInBackground = false;
    }


}