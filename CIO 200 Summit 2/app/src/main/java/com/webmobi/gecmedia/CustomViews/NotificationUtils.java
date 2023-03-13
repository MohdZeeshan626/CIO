package com.webmobi.gecmedia.CustomViews;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import android.text.Html;
import android.text.TextUtils;


import com.webmobi.gecmedia.Config.ApiList;
import com.webmobi.gecmedia.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import io.paperdb.Paper;


/**
 * Created by Ravi on 31/03/15.
 */
public class NotificationUtils {

    private static String TAG = NotificationUtils.class.getSimpleName();

    private Context mContext;

    NotificationCompat.InboxStyle inboxStyle = null;

    NotificationCompat.Builder mBuilder = null;
    String chanel="admin";

    public NotificationUtils(Context mContext) {
        this.mContext = mContext;
    }

    public void showNotificationMessage(String title, String message, long timeStamp, Intent intent, HashMap<String, JSONObject> unreadmsg, String type, String appid) {
        showNotificationMessage(title, message, timeStamp, intent, type, unreadmsg, appid);
    }

    public void showNotificationMessage(final String title, final String message, final long timeStamp, Intent intent, String type, HashMap<String, JSONObject> unreadmsg, String appid) {
        // Check for empty push message
        if (TextUtils.isEmpty(message))
            return;



        // notification icon
        final int icon = R.mipmap.ic_launcher;

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        final PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        mContext,
                        0,
                        intent,
                        PendingIntent.FLAG_CANCEL_CURRENT
                );
        if (mBuilder == null)
            mBuilder = new NotificationCompat.Builder(mContext);

        final Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                + "://" + mContext.getPackageName() + "/raw/notification");


        if (type.equalsIgnoreCase("normal") || type.equalsIgnoreCase("meeting")) {
            shownormalNotification(mBuilder, icon, title, message, timeStamp, resultPendingIntent, alarmSound, appid);
        } else
            showSmallNotification(mBuilder, icon, title, message, timeStamp, resultPendingIntent, alarmSound, unreadmsg, appid);
    }

    private void shownormalNotification(NotificationCompat.Builder mBuilder, int icon, String title, String message, long timeStamp, PendingIntent resultPendingIntent, Uri alarmSound, String appid) {

        HashMap<String, Integer> ids = Paper.book().read("UniqueID", new HashMap<String, Integer>());


        if (inboxStyle == null)
            inboxStyle = new NotificationCompat.InboxStyle();


        NotificationManager mNotifyManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "some_channel_id";
            CharSequence channelName = "Some Channel";
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mNotifyManager.createNotificationChannel(notificationChannel);
            mBuilder = new NotificationCompat.Builder(mContext)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), icon))
                    .setColor(ContextCompat.getColor(mContext, R.color.m_blue))
                    .setAutoCancel(true)
                    .setContentTitle(title)
                    .setChannelId(channelId)
                    .setContentIntent(resultPendingIntent)
                    .setSound(alarmSound)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setWhen(timeStamp)
                    .setContentText(message);
            mNotifyManager.notify(ApiList.NOTIFICATION_ID, mBuilder.build());
        }
        else {
            Notification notification;
            notification = mBuilder.setSmallIcon(icon).setTicker(title).setWhen(0)
                    .setAutoCancel(true)
                    .setContentTitle(title)
                    .setContentIntent(resultPendingIntent)
                    .setSound(alarmSound)
                    .setOngoing(false)
                    .setWhen(timeStamp)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), icon))
                    .setContentText(message)
                    .build();

            NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(ids.get(appid), notification);
        }

    }


    private void showSmallNotification(NotificationCompat.Builder mBuilder, int icon, String title, String message, long timeStamp, PendingIntent resultPendingIntent, Uri alarmSound, HashMap<String, JSONObject> unreadmsg, String appid) {

        if (inboxStyle == null)
            inboxStyle = new NotificationCompat.InboxStyle();

//        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        int badge = 0;


        for (String key : unreadmsg.keySet()) {
            JSONObject json = unreadmsg.get(key);
            try {
                badge = badge + json.getInt("badgecount");
                JSONArray msgs = json.getJSONArray("msg");
                for (int i = 0; i < msgs.length(); i++) {
                    inboxStyle.addLine(json.getString("name") + " : " + msgs.getString(i));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        String msg = badge + " messages from " + unreadmsg.size() + " chats";
        inboxStyle.setBigContentTitle(msg);
        if (badge > 8)
            inboxStyle.setSummaryText("+ " + (badge - 8) + " more");

        NotificationManager mNotifyManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //createChannel(mNotifyManager);
            String channelId = "some_channel_id";
            CharSequence channelName = "Some Channel";
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mNotifyManager.createNotificationChannel(notificationChannel);
            mBuilder = new NotificationCompat.Builder(mContext)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), icon))
                    .setColor(ContextCompat.getColor(mContext, R.color.m_blue))
                    .setAutoCancel(true)
                    .setContentTitle(title)
                    .setChannelId(channelId)
                    .setContentIntent(resultPendingIntent)
                    .setSound(alarmSound)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setWhen(timeStamp)
                    .setContentText(message);
            mNotifyManager.notify(ApiList.NOTIFICATION_ID, mBuilder.build());
        }
        else {
            Notification notification;
            notification = mBuilder.setSmallIcon(icon).setTicker(title).setWhen(0)
                    .setAutoCancel(true)
                    .setContentTitle(title)
                    .setContentIntent(resultPendingIntent)
                    .setSound(alarmSound)
                    .setStyle(inboxStyle)
                    .setOngoing(true)
                    .setWhen(timeStamp)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), icon))
                    .setContentText(badge == 1 ? message : msg)
                    .build();
            NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(ApiList.NOTIFICATION_ID, notification);
        }
    }
    @TargetApi(26)
    private void createChannel(NotificationManager notificationManager) {
        String channelId = "some_channel_id";
        CharSequence channelName = "Some Channel";
        int importance = NotificationManager.IMPORTANCE_LOW;
        NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, importance);
        notificationChannel.enableLights(true);
        notificationChannel.setLightColor(Color.RED);
        notificationChannel.enableVibration(true);
        notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
        notificationManager.createNotificationChannel(notificationChannel);
    }
    private void showBigNotification(Bitmap bitmap, NotificationCompat.Builder mBuilder, int icon, String title, String message, long timeStamp, PendingIntent resultPendingIntent, Uri alarmSound) {
        NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
        bigPictureStyle.setBigContentTitle(title);
        bigPictureStyle.setSummaryText(Html.fromHtml(message).toString());
        bigPictureStyle.bigPicture(bitmap);
        Notification notification;
        notification = mBuilder.setSmallIcon(icon).setTicker(title).setWhen(0)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentIntent(resultPendingIntent)
                .setSound(alarmSound)
                .setStyle(bigPictureStyle)
                .setWhen(timeStamp)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), icon))
                .setContentText(message)
                .build();

        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(ApiList.NOTIFICATION_ID_BIG_IMAGE, notification);
    }

    /**
     * Downloading push notification image before displaying it in
     * the notification tray
     */
    public Bitmap getBitmapFromURL(String strURL) {
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Playing notification sound
    public void playNotificationSound() {
        try {
            Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                    + "://" + mContext.getPackageName() + "/raw/notification");
            Ringtone r = RingtoneManager.getRingtone(mContext, alarmSound);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method checks if the app is in background or not
     */
    public static boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }

    // Clears notification tray messages
    public static void clearNotifications(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }


}
