package com.singleevent.sdk.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.twitter.sdk.android.tweetcomposer.TweetUploadService;

/**
 * Created by webMOBI on 11/23/2017.
 */

public class MyResultReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (TweetUploadService.UPLOAD_SUCCESS.equals(intent.getAction())) {
            // success
          //  final Long tweetId = intentExtras.getLong(TweetUploadService.EXTRA_TWEET_ID);

            Toast.makeText(context,"Post success",Toast.LENGTH_SHORT).show();
        } else if (TweetUploadService.UPLOAD_FAILURE.equals(intent.getAction())) {
            // failure
            Toast.makeText(context,"Upload Falure",Toast.LENGTH_SHORT).show();
           // final Intent retryIntent = intentExtras.getParcelable(TweetUploadService.EXTRA_RETRY_INTENT);
        } else if (TweetUploadService.TWEET_COMPOSE_CANCEL.equals(intent.getAction())) {
            // cancel
            Toast.makeText(context,"Tweet Compose Cancel",Toast.LENGTH_SHORT).show();
        }
    }
}
