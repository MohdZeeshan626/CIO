package com.singleevent.sdk.View.LeftActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.singleevent.sdk.R;
import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;

import io.agora.rtc.Constants;
import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.models.UserInfo;

public class AgoraMain extends AppCompatActivity{

    private static final int PERMISSION_REQ_ID = 22;
    private RtcEngine mRtcEngine;

    private final UserInfo mLocal = new UserInfo();
    private UserInfo mRemote;


    // Ask for Android device permissions at runtime.
    private static final String[] REQUESTED_PERMISSIONS = {
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE


    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Context context;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_chat_view);
        mLocal.userAccount = getAlphaNumericString(8);//getIntent().getStringExtra(BuildConfig.KEY_STRINGIFIED_ACCOUNT);

        // If all the permissions are granted, initialize the RtcEngine object and join a channel.
        if (checkSelfPermission(REQUESTED_PERMISSIONS[0], PERMISSION_REQ_ID) &&
                checkSelfPermission(REQUESTED_PERMISSIONS[1], PERMISSION_REQ_ID) &&
                checkSelfPermission(REQUESTED_PERMISSIONS[2], PERMISSION_REQ_ID)) {
            //initEngineAndJoinChannel();
        }
    }
    public String getAlphaNumericString(int n)
    {

        // chose a Character random from this String
        String AlphaNumericString = "0123456789";

        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {

            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index
                    = (int)(AlphaNumericString.length()
                    * Math.random());

            // add Character one by one in end of sb
            sb.append(AlphaNumericString
                    .charAt(index));
        }

        return sb.toString();
    }

    private boolean checkSelfPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(AgoraMain.this, permission) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AgoraMain.this, REQUESTED_PERMISSIONS, requestCode);
            return false;
        }

        return true;
    }

    public void initEngineAndJoinChannel(){

            try {
                mRtcEngine = RtcEngine.create(getBaseContext(), getString(R.string.agora_app_id), mRtcEventHandler);
                mRtcEngine.registerLocalUserAccount(getString(R.string.agora_app_id), mLocal.userAccount);
            } catch (Exception e) {
             //   Log.e(TAG, Log.getStackTraceString(e));
                throw new RuntimeException("NEED TO check rtc sdk init fatal error\n" + Log.getStackTraceString(e));

        }
    }


    private final IRtcEngineEventHandler mRtcEventHandler = new IRtcEngineEventHandler() {
        @Override
        // Listen for the onJoinChannelSuccess callback.
        // This callback occurs when the local user successfully joins the channel.
        public void onJoinChannelSuccess(String channel, final int uid, int elapsed) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.i("agora","Join channel success, uid: " + (uid & 0xFFFFFFFFL));
                }
            });
        }

        @Override
        // Listen for the onFirstRemoteVideoDecoded callback.
        // This callback occurs when the first video frame of the broadcaster is received and decoded after the broadcaster successfully joins the channel.
        // You can call the setupRemoteVideo method in this callback to set up the remote video view.
        public void onFirstRemoteVideoDecoded(final int uid, int width, int height, int elapsed) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.i("agora","First remote video decoded, uid: " + (uid & 0xFFFFFFFFL));
                   /* setupRemoteVideo(uid);
                    rtcEngine().setupRemoteVideo(new VideoCanvas(surfaceV, VideoCanvas.RENDER_MODE_HIDDEN, uid));*/

                }
            });
        }


        @Override
        public void onLocalUserRegistered(int uid, String userAccount) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mLocal.uid = uid;
                    mLocal.userAccount = userAccount;
                    onUpdateUserAccountInfo();
                }
            });
        }

        @Override
        public void onUserInfoUpdated(int uid, UserInfo user) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mRemote == null) {
                        mRemote = user;
                        onUpdateUserAccountInfo();
                    }
                }
            });
        }
        @Override
        // Listen for the onUserOffline callback.
        // This callback occurs when the broadcaster leaves the channel or drops offline.
        public void onUserOffline(final int uid, int reason) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.i("agora","User offline, uid: " + (uid & 0xFFFFFFFFL));
                  //  onRemoteUserLeft();
                }
            });
        }
    };


    // Initialize the RtcEngine object.
    private void initializeEngine() {
        try {
            mRtcEngine = RtcEngine.create(getBaseContext(), getString(R.string.agora_app_id), mRtcEventHandler);
        } catch (Exception e) {
          //  Log.e(TAG, Log.getStackTraceString(e));
            throw new RuntimeException("NEED TO check rtc sdk init fatal error\n" + Log.getStackTraceString(e));
        }
    }
    private void setChannelProfile() {
        mRtcEngine.setChannelProfile(Constants.CHANNEL_PROFILE_LIVE_BROADCASTING);
    }
    private void onUpdateUserAccountInfo() {
        //TextView userInfo = (TextView) findViewById(R.id.user_info);

        String info = "Local: " + (mLocal.uid & 0xFFFFFFFFL) + " " + mLocal.userAccount;

        if (mRemote != null) {
            info += ", Remote: " + (mRemote.uid & 0xFFFFFFFFL) + " " + mRemote.userAccount;
        }

     //   userInfo.setText(info);
    }

}
