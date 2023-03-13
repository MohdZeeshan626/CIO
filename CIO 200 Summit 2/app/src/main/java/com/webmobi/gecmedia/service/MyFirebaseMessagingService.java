package com.webmobi.gecmedia.service;

import android.content.Context;
import android.content.Intent;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;


import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.singleevent.sdk.View.LeftActivity.Feeds;
import com.singleevent.sdk.View.RightActivity.MyChat;
import com.singleevent.sdk.View.RightActivity.Notification;
import com.webmobi.gecmedia.CustomViews.NotificationUtils;
import com.webmobi.gecmedia.MyDiscRequest;
import com.webmobi.gecmedia.Views.HomeScreen;
import com.webmobi.gecmedia.Views.MyDiscMeetings;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Random;

import io.paperdb.Paper;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    private NotificationUtils notificationUtils;

    HashMap<String, JSONObject> unreadmsg;

    static Random rand = new Random();
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Paper.init(this);
//        System.out.println("Msg " + remoteMessage);
        //Log.e(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage == null)
            return;

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {

            String id = remoteMessage.getData().get("id");
            String body = remoteMessage.getData().get("body");
            String type = remoteMessage.getData().get("type");
            String title = remoteMessage.getData().get("title");
            String sender_name = remoteMessage.getData().get("sender_name");
            String datatype = remoteMessage.getData().get("datatype");
            String appid = remoteMessage.getData().get("appid");
            String appname = remoteMessage.getData().get("appname");
            String rich_text = remoteMessage.getData().get("rich_text");

            storeid(appid);


           try {
               if (remoteMessage.getData().get("type").equalsIgnoreCase("normal")) {
                   Intent notify_intent = new Intent(getApplicationContext(), HomeScreen.class);
                   notify_intent.putExtra("EVENT_ID", "");
                   notify_intent.putExtra("appid", appid);
                   showNotificationMessage(getApplicationContext(), title, body, System.currentTimeMillis(), notify_intent, new HashMap<String, JSONObject>(), type, appid);
               } else {
                   if (!remoteMessage.getData().get("type").equalsIgnoreCase("meeting")) {
                       handleDataMessage(id, body, type, title, sender_name, datatype, appid, appname, rich_text);
                   } else {
                       Intent resultIntent = new Intent(getApplicationContext(), datatype.equalsIgnoreCase("request") ? MyDiscRequest.class : (datatype.equalsIgnoreCase("activityfeed")) ? Feeds.class : MyDiscMeetings.class);
                       resultIntent.putExtra("keyid", id);
                       resultIntent.putExtra("keyMessage", body);
                       resultIntent.putExtra("keytype", type);
                       resultIntent.putExtra("keyAlert", title);
                       resultIntent.putExtra("keyname", sender_name);
                       resultIntent.putExtra("keydatatype", datatype);
                       resultIntent.putExtra("Keyappid", appid);
                       resultIntent.putExtra("appname", appname);
                       showNotificationMessage(getApplicationContext(), title, body, System.currentTimeMillis(), resultIntent, new HashMap<String, JSONObject>(), type, appid);
                   }
               }
           }catch (Exception e)
           {

           }

        }
    }

    private void storeid(String appid) {

        HashMap<String, Integer> ids = Paper.book().read("UniqueID", new HashMap<String, Integer>());
        if (!ids.containsKey(appid))
            ids.put(appid, rand.nextInt());


        Paper.book().write("UniqueID", ids);
    }

    private void handleDataMessage(String id, String body, String type, String title, String sender_name, String datatype, String appid, String appname, String rich_text) {
       try {
           if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
               Intent i;
               if (body.contains("@")) {
                   i = new Intent();
                   i.setAction("com.feeds." + appid + "." + id);
               } else {
                   i = new Intent();
                   i.setAction("com.mychats." + appid + "." + id);
               }
               boolean isRegistered = LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(i);
               if (!isRegistered) {

                   int badgecount = storeunreadmsg(id, body, sender_name, appid);


                   i = new Intent();
                   if (body.contains("@")) {
                       i.setAction("com.feeds." + appid);
                   } else {
                       i.setAction("com.mychats." + appid);
                   }
                   i.putExtra("keyid", id);
                   i.putExtra("keyMessage", body);
                   i.putExtra("keytype", type);
                   i.putExtra("keyAlert", title);
                   i.putExtra("keyname", sender_name);
                   i.putExtra("keydatatype", datatype);
                   i.putExtra("badgecount", badgecount);
                   i.putExtra("Keyappid", appid);
                   i.putExtra("appname", appname);
                   i.putExtra("rich_text", rich_text);
                   i.putExtra("time", System.currentTimeMillis());
                   isRegistered = LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(i);
                   if (!isRegistered) {
                       Intent resultIntent;
                       if (body.contains("@")) {
                           resultIntent = new Intent(getApplicationContext(), Feeds.class);

                       } else {
                           resultIntent = new Intent(getApplicationContext(), datatype.equalsIgnoreCase("activityfeed") ? Feeds.class : (type.equalsIgnoreCase("message")) ? MyChat.class : Notification.class);
                       }
                       resultIntent.setAction("com.view");
                       resultIntent.putExtra("keyid", id);
                       resultIntent.putExtra("keyMessage", body);
                       resultIntent.putExtra("keytype", type);
                       resultIntent.putExtra("keyAlert", title);
                       resultIntent.putExtra("keyname", sender_name);
                       resultIntent.putExtra("badgecount", badgecount);
                       resultIntent.putExtra("keydatatype", datatype);
                       resultIntent.putExtra("Keyappid", appid);
                       resultIntent.putExtra("appname", appname);
                       resultIntent.putExtra("rich_text", rich_text);
                       showNotificationMessage(getApplicationContext(), title, body, System.currentTimeMillis(), resultIntent, unreadmsg, type, appid);

                   }
               }

           } else {
               // app is in background, show the notification in notification tray
               Intent resultIntent;


               storeunreadmsg(id, body, sender_name, appid);
               if (body.contains("@")) {
                   resultIntent = new Intent(getApplicationContext(), Feeds.class);
               } else {
                   resultIntent = new Intent(getApplicationContext(), type.equalsIgnoreCase("message") ? Notification.class : Notification.class);
               }
               //   resultIntent = new Intent(getApplicationContext(), type.equalsIgnoreCase("message") ? Notification.class : Notification.class);
               resultIntent.setAction("com.view");
               resultIntent.putExtra("keyid", id);
               resultIntent.putExtra("keyMessage", body);
               resultIntent.putExtra("keytype", type);
               resultIntent.putExtra("keyAlert", title);
               resultIntent.putExtra("keyname", sender_name);
               resultIntent.putExtra("keydatatype", datatype);
               resultIntent.putExtra("Keyappid", appid);
               resultIntent.putExtra("appname", appname);
               showNotificationMessage(getApplicationContext(), title, body, System.currentTimeMillis(), resultIntent, unreadmsg, type, appid);
               // If the app is in background, firebase itself handles the notification
           }
       }catch (Exception e){}
    }

    private int storeunreadmsg(String id, String body, String sender_name, String appid) {

        unreadmsg = Paper.book(appid).read("Message", new HashMap<String, JSONObject>());
        if (unreadmsg.containsKey(id))
            unreadmsg.put(id, buildjson(unreadmsg.get(id), body));
        else
            unreadmsg.put(id, object(body, sender_name));

        Paper.book(appid).write("Message", unreadmsg);

        int badgecount = 0;
        try {
            badgecount = unreadmsg.get(id).getInt("badgecount");
        } catch (JSONException e) {
            e.printStackTrace();
            badgecount = 0;
        }

        return badgecount;

    }


    private void showNotificationMessage(Context context, String title, String message, long timeStamp, Intent intent, HashMap<String, JSONObject> unreadmsg, String type, String appid) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, unreadmsg, type, appid);
    }

    // existing value updated

    private JSONObject buildjson(JSONObject jsonObject, String body) {

        try {
            jsonObject.put("badgecount", jsonObject.getInt("badgecount") + 1);
//            json.put("name",json.getString("name"));
            JSONArray array = jsonObject.getJSONArray("msg");
            array.put(body);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;

    }


    private JSONObject object(String body, String name) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject();
            jsonObject.put("badgecount", 1);
            jsonObject.put("name", name);
            JSONArray array = new JSONArray();
            array.put(body);
            jsonObject.put("msg", array);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;

    }


}
