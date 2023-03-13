package com.webmobi.gecmedia.Views;

import android.os.Bundle;
import androidx.annotation.Nullable;

import com.singleevent.sdk.View.RightActivity.MyMeetingBase;

import io.paperdb.Paper;

/**
 * Created by webMOBI on 10/12/2017.
 */

public class MyDiscMeetings extends MyMeetingBase {

    String appid, appname;
    Bundle extras;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Paper.init(this);
        if (getIntent().getExtras() == null)
            finish();

        extras = getIntent().getExtras();
        appid = extras.getString("Keyappid");
        appname = extras.getString("appname");

        init_myrequest("#0a6a99", appid, Paper.book(appid).read("userId", ""), appname);
        getslots();
    }
}
