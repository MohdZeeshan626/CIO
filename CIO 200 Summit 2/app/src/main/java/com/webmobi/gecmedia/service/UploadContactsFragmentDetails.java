package com.webmobi.gecmedia.service;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import android.support.v4.os.ResultReceiver;
import android.util.Base64;
import android.util.Log;
import android.widget.RemoteViews;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.singleevent.sdk.ApiList;
import com.singleevent.sdk.App;
import com.singleevent.sdk.gallery_camera.Gallery_Model.Image;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by webMOBI on 12/8/2017.
 */

public class UploadContactsFragmentDetails extends IntentService {
    JSONObject header = null;
    ResultReceiver receiver;
    private static final String TAG = UploadContactsFragmentDetails.class.getSimpleName();
    private ArrayList<Image> imageselected = new ArrayList<>();
    public static NotificationManager mNotificationManager;
    static NotificationCompat.Builder builder;
     Context context;
    static int NOTIFICATION_ID = 111;
    Intent intent;

    public UploadContactsFragmentDetails() {
        super(UploadContactsFragmentDetails.class.getName());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        this.intent = intent;
        receiver = intent.getParcelableExtra("receiver");
        try {
            header = new JSONObject(intent.getStringExtra("header"));

            uploaddata();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "Service Stopping!");
        this.stopSelf();
    }

    private void uploaddata() {


        FileUploadNotification(getApplicationContext());

        JSONObject main = new JSONObject();

        // Initialize a new JsonObjectRequest instance
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                ApiList.Post_Contacts,
                main,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        success_notification();

                        try {
                            if (response.getBoolean("response")) {
                                Bundle b = new Bundle();
                                b.putString("data", response.toString());
                                receiver.send(0, b);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        failUploadNotification();

                    }
                }
        );

        App.getInstance().addToRequestQueue(jsonObjectRequest, "Feeds");
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


    }


    public void FileUploadNotification(Context context) {

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


        mNotificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        builder = new NotificationCompat.Builder(context,"Sending_ch_ID");
        builder.setContentTitle("Uploading Post")
                .setContentText("Please wait...")
                .setSmallIcon(android.R.drawable.stat_sys_upload)
                .setProgress(100, 0, true)
                .setSound(soundUri)
                .setAutoCancel(false);

        mNotificationManager.notify(NOTIFICATION_ID, builder.build());


    }


    private void success_notification() {

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        if (builder != null) {
            builder.setContentTitle("Successfully Posted")
                    .setContentText("")
                    .setProgress(0, 0, false)
                    .setSmallIcon(android.R.drawable.stat_sys_upload_done)
                    .setSound(soundUri)
                    .setOngoing(false);

            mNotificationManager.notify(NOTIFICATION_ID, builder.build());

            new Handler().postDelayed(new Runnable() {
                public void run() {
                    deleteNotification();
                }
            }, 1000);
        } else {
            mNotificationManager.cancel(NOTIFICATION_ID);
        }

    }

    public void failUploadNotification() {
        Log.e("downloadsize", "failed notification...");

        PendingIntent pendingIntent = PendingIntent.getService(UploadContactsFragmentDetails.this, 0, intent, 0);

        RemoteViews contentView = new RemoteViews(getPackageName(), com.singleevent.sdk.R.layout.act_custom_notification);
        contentView.setImageViewResource(com.singleevent.sdk.R.id.small_icon, com.singleevent.sdk.R.drawable.ic_uploadfailed);

        contentView.setOnClickPendingIntent(com.singleevent.sdk.R.id.retry, pendingIntent);

        if (builder != null) {
            builder.setContentText("")
                    .setProgress(0, 0, false)
                    .setSmallIcon(com.singleevent.sdk.R.drawable.ic_uploadfailed)
                    .setOngoing(false)
                    .setCustomContentView(contentView);

            mNotificationManager.notify(NOTIFICATION_ID, builder.build());

        } else {
            mNotificationManager.cancel(NOTIFICATION_ID);
        }
    }


    public static void deleteNotification() {
        mNotificationManager.cancel(NOTIFICATION_ID);
        builder = null;
    }

    private String convert_uri_to_base64(String path) {

        Bitmap bm = BitmapFactory.decodeFile(path);
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        bm.compress(Bitmap.CompressFormat.JPEG, 80, baos);
//        return baos.toByteArray();


        if(bm!=null){
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            bm.recycle();
            bm = null;
            byte[] b = baos.toByteArray();
            return Base64.encodeToString(b, Base64.DEFAULT);
        }else{
            Uri imageUri = Uri.parse(path);
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                bitmap.recycle();
                bitmap = null;
                byte[] b = baos.toByteArray();
                return Base64.encodeToString(b, Base64.DEFAULT);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;

    }
}
