package com.singleevent.sdk.View.RightActivity;

import android.os.Bundle;
import androidx.annotation.Nullable;

import com.singleevent.sdk.model.AppDetails;

import io.paperdb.Paper;

/**
 * Created by webMOBI on 10/12/2017.
 */

public class MyRequest extends MyRequestBase {

    AppDetails appDetails;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Paper.init(this);
        appDetails = Paper.book().read("Appdetails");
        init_myrequest(appDetails.getTheme_color(), appDetails.getAppId(), Paper.book(appDetails.getAppId()).read("userId",""), appDetails.getAppName());
        getslots();
    }


}
