package com.singleevent.sdk.View.RightActivity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.SpannableString;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.singleevent.sdk.ApiList;
import com.singleevent.sdk.App;
import com.singleevent.sdk.Custom_View.Error_Dialog;
import com.singleevent.sdk.Custom_View.Letter.LetterTileProvider;
import com.singleevent.sdk.Custom_View.SortingMSG;
import com.singleevent.sdk.Custom_View.Util;
import com.singleevent.sdk.Custom_View.VolleyErrorLis;
import com.singleevent.sdk.Left_Adapter.ChatAdapter;
import com.singleevent.sdk.model.AppDetails;
import com.singleevent.sdk.model.LocalArraylist.ChatMSG;
import com.singleevent.sdk.R;
import com.singleevent.sdk.View.LeftActivity.Attendee;
import com.singleevent.sdk.utils.DataBaseStorage;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import io.paperdb.Paper;

/**
 * Created by Admin on 6/21/2017.
 */

public class MyChat extends AppCompatActivity implements View.OnClickListener {

    HashMap<String, JSONObject> newmessages;
    AppDetails appDetails;
    TextView noitems, mychat;
    ListView userlist;
    Toolbar toolbar;
    LetterTileProvider tileProvider;
    private float dpWidth, badgewidth;
    Bitmap letterTile;
    private Context context;
    TextView error;
    RelativeLayout trygain;
    LinearLayout v1;

    ChatAdapter adapter;
    ArrayList<ChatMSG> messages;
    private String attendee_option="1";
    private String token;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Paper.init(this);
        setContentView(R.layout.s_chatlayout);
        context = this;
        appDetails = Paper.book().read("Appdetails");
      //  attendee_option = Paper.book().read("attendee_option", "");

        noitems = (TextView) findViewById(R.id.noitems);
        userlist = (ListView) findViewById(R.id.userlist);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mychat = (TextView) findViewById(R.id.mychat);

        toolbar.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));
        setSupportActionBar(toolbar);
        newmessages = Paper.book(appDetails.getAppId()).read("Message", new HashMap<String, JSONObject>());
        trygain = (RelativeLayout) findViewById(R.id.vtryagain);
        v1 = (LinearLayout) findViewById(R.id.view1);
        error = (TextView) trygain.findViewById(R.id.tryagain);
        tileProvider = new LetterTileProvider(context);
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        dpWidth = displayMetrics.widthPixels * 0.10F;
        badgewidth = displayMetrics.widthPixels * 0.05F;
        trygain.setOnClickListener(this);
        mychat.setTypeface(Util.regulartypeface(this));
        messages = new ArrayList<>();
        adapter = new ChatAdapter(context, messages, dpWidth, badgewidth, newmessages);
        userlist.setAdapter(adapter);
        adapter.notifyDataSetChanged();
//        userlist.setOnItemClickListener(this);

    }

    private void startThread() {

        final Handler handler = new Handler();

        Runnable r = new Runnable() {
            @Override
            public void run() {
                // your code here

                try {
                    FileInputStream fis = openFileInput(DataBaseStorage.F_I_L_ENCP2);
                    ObjectInputStream ois = new ObjectInputStream(fis);

                    byte[] decrypted = DataBaseStorage.decryptData((HashMap<String, byte[]>) ois.readObject(),
                            DataBaseStorage.token_pass);
                    if (decrypted != null) {
                        //decryptedString[0] = new String(decrypted);
                        token = new String(decrypted);
                    }
                    ois.close();

                } catch (Exception e) {

                    e.printStackTrace();
                }


                handler.post(new Runnable()  //If you want to update the UI, queue the code on the UI thread
                {
                    public void run() {
                        //Code to update the UI
                        try {
                            // Loading chat user history
                            getchatuser();
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    }
                });
            }
        };

        Thread t = new Thread(r);
        t.start();
    }

    private void getchatuser() {

        final ProgressDialog dialog = new ProgressDialog(MyChat.this, R.style.MyAlertDialogStyle);
        dialog.setMessage("Loading the User");
        dialog.show();
        String tag_string_req = "Chat_User";
        String url = ApiList.ChatUsers + appDetails.getAppId() + "&user_id=" + Paper.book().read("userId");
        StringRequest strReq = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                try {
                    System.out.println(response);
                    JSONObject jObj = new JSONObject(response);


                    if (jObj.getBoolean("response")) {
                        showconnectionerror(true);
                        parseuser(jObj.getJSONObject("responseString"));


                    } else {
                        if (jObj.getString("responseString").equalsIgnoreCase("Invalid token")) {
                            Intent i = new Intent();
                            i.putExtra("keyMessage", "Session Expired, Please Login ");
                            i.putExtra("keyAlert", "Session Expired");
                            i.setClassName(getPackageName(), getPackageName() + ".SessionExpired");
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);

                        } else
                            Error_Dialog.show(jObj.getString("responseString"), MyChat.this);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                if (error instanceof TimeoutError) {
                    Error_Dialog.show("Timeout", MyChat.this);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, context), MyChat.this);
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    showconnectionerror(false);
                }

            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("token", Paper.book().read("token", ""));
                return headers;
            }
        };

        strReq.setShouldCache(false);
        App.getInstance().addToRequestQueue(strReq, tag_string_req);
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));


    }

    @Override
    protected void onResume() {
        super.onResume();

        startThread();

       // attendee_option = Paper.book().read("attendee_option", "1");
        SpannableString s = new SpannableString("My Chat");
        setTitle(Util.applyFontToMenuItem(this, s));
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter("com.mychats." + appDetails.getAppId()));
        LocalBroadcastManager.getInstance(context).registerReceiver(lastmsg, new IntentFilter("com.local.msg." + appDetails.getAppId()));


    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            Bundle extras = intent.getExtras();

            String id = extras.getString("keyid");
            String lastmsg = extras.getString("keyMessage");
            long time = extras.getLong("time", 0);
            int badegcount = extras.getInt("badgecount");

            int i;
            for (i = 0; i < messages.size(); i++) {
                ChatMSG c = messages.get(i);
                if (c.getSender_id().equalsIgnoreCase(id)) {
                    c.setCreate_date(String.valueOf(time));
                    c.setMessage(lastmsg);
                    c.setBadgecount(badegcount);
                    break;
                }
            }

            Collections.sort(messages, new SortingMSG());
            adapter.notifyDataSetChanged();

            ChatMSG msg = new ChatMSG("", extras.getString("keyname"), extras.getString("keyid"), "", "", "", 0, extras.getInt("badgecount"));


        }
    };

    private BroadcastReceiver lastmsg = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String id = intent.getStringExtra("id");
            String lastmsg = intent.getStringExtra("lastmsg");
            long time = intent.getLongExtra("time", 0);

            int i;
            for (i = 0; i < messages.size(); i++) {
                ChatMSG c = messages.get(i);
             try {
                 if (c.getSender_id().equalsIgnoreCase(id)) {
                     c.setCreate_date(String.valueOf(time));
                     c.setMessage(lastmsg);
                     break;
                 }
             }catch (Exception e){}
            }

            Collections.sort(messages, new SortingMSG());
            adapter.notifyDataSetChanged();


            System.out.println("new msg");


        }
    };


    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(lastmsg);
        super.onDestroy();
    }


    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onClick(View view) {

        int i = view.getId();
        if (i == R.id.tryagain) {
            getchatuser();

        }

    }

    private void parseuser(JSONObject responseString) {

        try {
            messages.clear();
            Gson gson = new Gson();

            Random r = new Random();

            JSONArray sendmsg = responseString.getJSONArray("sendmsg");
            JSONArray recmsg = responseString.getJSONArray("receivemsg");

            for (int i = 0; i < sendmsg.length(); i++) {
                String eventString = sendmsg.getJSONObject(i).toString();
                ChatMSG obj = gson.fromJson(eventString, ChatMSG.class);
                obj.setBadgecount(newmessages.containsKey(obj.getSender_id()) ? newmessages.get(obj.getSender_id()).getInt("badgecount") : 0);
                obj.setColor(Color.argb(255, r.nextInt(256), r.nextInt(256), r.nextInt(256)));
                messages.add(obj);
            }

            for (int i = 0; i < recmsg.length(); i++) {
                String eventString = recmsg.getJSONObject(i).toString();
                ChatMSG obj = gson.fromJson(eventString, ChatMSG.class);
                if(obj.getSender_id()!=null)
                     obj.setBadgecount(newmessages.containsKey(obj.getSender_id()) ? newmessages.get(obj.getSender_id()).getInt("badgecount") : 0);
                else
                    obj.setBadgecount(newmessages.containsKey(obj.getRecipient_id()) ? newmessages.get(obj.getRecipient_id()).getInt("badgecount") : 0);
                obj.setColor(Color.argb(255, r.nextInt(256), r.nextInt(256), r.nextInt(256)));
                messages.add(obj);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


        if (messages.size() > 0) {

            Collections.sort(messages, new SortingMSG());
            adapter.notifyDataSetChanged();

        } else
            showview(false);


    }

    private void showview(boolean flag) {

        if (flag) {
            userlist.setVisibility(View.VISIBLE);
            noitems.setVisibility(View.GONE);
        } else {
            userlist.setVisibility(View.GONE);
            noitems.setVisibility(View.VISIBLE);
        }

    }


    private void showconnectionerror(boolean flag) {
        if (flag) {
            v1.setVisibility(View.VISIBLE);
            trygain.setVisibility(View.GONE);
        } else {
            v1.setVisibility(View.GONE);
            trygain.setVisibility(View.VISIBLE);
        }
    }

//    @Override
//    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//
//        TextView badge = (TextView) view.findViewById(R.id.counter);
//        ChatMSG chatid = (ChatMSG) badge.getTag();
//
//        chatid.getSender_id();
//        badge.setVisibility(View.GONE);
//        Bundle args = new Bundle();
//        args.putSerializable("UserItem", messages.get(i));
//        Intent intent = new Intent(MyChat.this, MessageView.class);
//        intent.putExtras(args);
//        startActivity(intent);
//
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void showAttendeesList(View view) {


        if (attendee_option.equals("1")) {
            startActivity(new Intent(this, Attendee.class));
        } else {
            Error_Dialog.show("Please Opt-In first to Chat.", MyChat.this);
        }

    }
}
