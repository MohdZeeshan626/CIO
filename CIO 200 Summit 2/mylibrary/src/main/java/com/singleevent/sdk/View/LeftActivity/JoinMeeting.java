package com.singleevent.sdk.View.LeftActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.singleevent.sdk.ApiList;
import com.singleevent.sdk.App;
import com.singleevent.sdk.Custom_View.Error_Dialog;
import com.singleevent.sdk.Custom_View.Util;
import com.singleevent.sdk.Custom_View.VolleyErrorLis;
import com.singleevent.sdk.Left_Adapter.ChatAdapter;
import com.singleevent.sdk.Left_Adapter.GroupMessageAdapter;
import com.singleevent.sdk.View.LeftActivity.askAquestion.adapter.AskAQueAdapter;
import com.singleevent.sdk.View.LeftActivity.askAquestion.model.EventQuestionModel;
import com.singleevent.sdk.agora.openvcall.ui.CallActivity;
import com.singleevent.sdk.agora.openvcall.ui.layout.GridVideoViewContainer;
import com.singleevent.sdk.model.Agenda.Agendadetails;
import com.singleevent.sdk.model.AppDetails;
import com.singleevent.sdk.model.Events;
import com.singleevent.sdk.model.LocalArraylist.ChatMSG;
import com.singleevent.sdk.model.User;
import com.singleevent.sdk.model.User_Details;
import com.singleevent.sdk.R;
import com.singleevent.sdk.View.RightActivity.admin.model.EventUser;
import com.singleevent.sdk.agora.openvcall.model.GroupChat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.paperdb.Paper;
import io.socket.emitter.Emitter;

public class JoinMeeting  extends AppCompatActivity implements View.OnClickListener {

    AppDetails appDetails;
    String url;
    int agenda_id;
    private String title;
    private ArrayList<Events> events = new ArrayList<Events>();
    // TextView tv_app_version_name;
    Events e;
    WebView aboutus;
    final String googleDocs = "https://docs.google.com/viewer?url=";
    private ListView listView;
    EditText sendchat;
    ImageView sendmsg;
    String group_id;
    User_Details userDetails;
    ChatAdapter adapter;
    private Context context;
    private float dpWidth, badgewidth;
    HashMap<String, JSONObject> newmessages;
    private GroupMessageAdapter chatArrayAdapter;
    String UserID,username,profile_pic;
    private io.socket.client.Socket mSocket;
    GroupChat user;
    String gid[];
    String target;
    ChatMSG user1;
    ArrayList<EventUser> filelist;
    List<com.singleevent.sdk.model.User> userview;
    TextView chatedit,quesedit,chatedit1,quesedit1;

    RelativeLayout dynvideo,controlbutton,askaquestin;
    private ArrayList<EventQuestionModel> questionsList ;
    private Agendadetails item;
    private AskAQueAdapter askAQueAdapter;
    private TextView addYourQues;
    RequestQueue queue;
    private EventQuestionModel eventQuestionModel;
    private static final String TAG = JoinMeeting.class.getSimpleName();





    ArrayList<GroupChat> messages;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Paper.init(this);
        context = this;
        setContentView(R.layout.joinmeeting);

        appDetails = Paper.book().read("Appdetails");
        username=Paper.book().read("username");
        userview = new ArrayList<>();
        profile_pic=Paper.book().read("profile_pic");
        UserID = Paper.book().read("userId");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //    tv_app_version_name = (TextView) findViewById(R.id.tv_app_version_name);
        toolbar.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));
        target=convertStrigyfy(UserID,Paper.book().read("GROUP_ID"));
        setSupportActionBar(toolbar);
        mSocket = App.getInstance().getSocket();
        mSocket.on("confirm_connection", onConnect);
        //  mSocket.on("confirm_connection",onConfirm);
        mSocket.on("group_message", onConfirmMessage);
        // mSocket.on("msg_ack",onConfirmMessage);
        mSocket.connect();



        // getting the data from previous activity
        Bundle extras = getIntent().getExtras();
        if (extras == null)
            finish();


        url = extras.getString("url");
        title = extras.getString("title");
        group_id=getIntent().getExtras().getString("Group_id");
        agenda_id=extras.getInt("agendaId");
        events = Paper.book().read("Appevents");
        e = events.get(0);
        getuser();
        discussChat(getCurrentFocus());
        aboutus = (WebView)findViewById(R.id.aboutus);

        aboutus.setWebChromeClient(new WebChromeClient());
        aboutus.getSettings().setJavaScriptEnabled(true);
        aboutus.getSettings().setLoadWithOverviewMode(true);
        aboutus.getSettings().setUseWideViewPort(true);
        aboutus.getSettings().setBuiltInZoomControls(true);
        aboutus.getSettings().setDisplayZoomControls(false);
        aboutus.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
        aboutus.getSettings().setPluginState(WebSettings.PluginState.ON);
        aboutus.getSettings().setAllowContentAccess(true);
        aboutus.getSettings().setDomStorageEnabled(true);
        aboutus.loadUrl(url);
        //https://global.gotomeeting.com/join/468866973


        /*loading html content in webview*/
        aboutus.setWebViewClient(new JoinMeeting.myWebClient(){

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //some code


                if (url != null) {
                    if (url != null) {
                        //  aboutus.loadUrl(String.format("data:text/html;charset=utf-8,<html><body style=\"text-align:left\"> %s </body></Html>", Uri.encode(e.getTabs(pos).getContent())));
                        view.getContext().startActivity(
                                new Intent(Intent.ACTION_VIEW, Uri.parse(url)));

                        //  aboutus.loadUrl(String.format("data:text/html;charset=utf-8,<html><body style=\"text-align:left\"> %s </body></Html>", Uri.encode(e.getTabs(pos).getContent())));

                    }

                }



                else{
                    view.getContext().startActivity(
                            new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                }
                return true;
            }
        });

    }

    public  class myWebClient extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            //some code
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//            if (url != null && (url.startsWith("http://") || url.startsWith("https://"))) {
            if (url != null) {
                if (validateEmail(url))
                    view.getContext().startActivity(
                            new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                else
                    view.loadUrl(url);
                return true;

            } else {
                return false;
            }
        }

        @Override
        public void onReceivedSslError(WebView view, final SslErrorHandler handler,
                                       SslError error) {


            final AlertDialog.Builder builder = new AlertDialog.Builder(JoinMeeting.this);
            builder.setMessage("SSL Certificate Error");
            builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    handler.proceed();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    handler.cancel();
                }
            });
            final AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mSocket.emit("group_checkin", target);
                }
            });
        }
    };


    private Emitter.Listener onConfirm = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // Error_Dialog.show("Connected", CallActivity.this);
                }
            });
        }
    };


    private Emitter.Listener onConfirmMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            System.out.print(args);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    String msg = (String) args[1];
                    String To_UserID = (String) args[2];
                    String To_UserName = (String) args[3];

                    String From_UserID = (String) args[4];
                    String From_UserName = (String) args[5];
                    String appid = (String) args[6];
                    String appname = (String) args[7];

                    String TimeinMilli = (String) args[8];
                    int multievent_flag = (int) args[10];
                    //  String eventid = (String) args[11];
                    GroupChat message = new GroupChat();

                    if(!From_UserID.equals(UserID)) {

                        for (int i = 0; i > userview.size(); i++) {
                            if (userview.get(i).getUserid().equals(From_UserID)) {

                                message.setProfile_pic(userview.get(i).getProfile_pic());

                            }
                        }

                        message.setTo_UserID(To_UserID);
                        message.setTo_UserName(To_UserName);

                        message.setFrom_UserID(From_UserID);
                        message.setFrom_UserName(From_UserName);
                        message.setAppid(appid);
                        message.setAppname(appname);
                        message.setChat_type("group");
                        message.setMessage_body(msg);
                        message.setMsg_datatype("normal");
                        //    message.setMultievent_flag(multievent_flag);
                        //  message.setEventid(eventid);
                        message.setIncomingMessage(false);
                        message.setCreate_date(calheader(Long.parseLong(TimeinMilli)));
                        message.setSection(false);
                        chatArrayAdapter.addMessage(message);

                    }


                }
            });
        }
    };
    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String msg = (String) args[1];
                    String To_UserID = (String) args[2];
                    String To_UserName = (String) args[3];

                    String From_UserID = (String) args[4];
                    String From_UserName = (String) args[5];
                    String appid = (String) args[6];
                    String appname = (String) args[7];

                    String TimeinMilli = (String) args[8];
                    int multievent_flag = (int) args[10];
                    //  String eventid = (String) args[11];
                    GroupChat message = new GroupChat();

                    int n= userview.size();

                    for(int i=0; i>userview.size(); i++)
                    {
                        if(userview.get(i).getUserid().equals(From_UserID)){
                            message.setTo_UserName(userview.get(i).getFirst_name()+userview.get(i).getLast_name());
                            message.setProfile_pic(userview.get(i).getProfile_pic());

                        }
                    }

                    message.setTo_UserID(To_UserID);
                    message.setTo_UserName(To_UserName);

                    message.setFrom_UserID(From_UserID);
                    message.setFrom_UserName(From_UserName);
                    message.setAppid(appid);
                    message.setAppname(appname);
                    message.setChat_type("group");
                    message.setMessage_body(msg);
                    message.setMsg_datatype("normal");
                    //    message.setMultievent_flag(multievent_flag);
                    //  message.setEventid(eventid);
                    message.setIncomingMessage(true);
                    message.setCreate_date(calheader(Long.parseLong(TimeinMilli)));
                    message.setSection(false);
                    chatArrayAdapter.addMessage(message);



                }
            });
        }
    };

    public String convertStrigyfy(String userid,String groupid)
    {
        ArrayList<String> s=new ArrayList<>();
        s.add(groupid);


        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId", userid);
            jsonObject.put("groupIds",new JSONArray(Arrays.asList(s.toArray())));


        }catch (Exception e)
        {

        }
        String d=jsonObject.toString();

        return d;
    }

    private boolean validateEmail(String url) {
        boolean isValidEmail = false;
        String regex = "^(.+)@(.+)$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(url);
        isValidEmail = matcher.matches();

        return isValidEmail;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getuser() {

        final ProgressDialog dialog = new ProgressDialog(JoinMeeting.this, R.style.MyAlertDialogStyle);
        String tag_string_req = "Login";
        String url = ApiList.Users + appDetails.getAppId()+"&userid="+UserID;
        StringRequest strReq = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                dialog.dismiss();

                try {

                    JSONObject jObj = new JSONObject(response);


                    if (jObj.getBoolean("response")) {
                        userview.clear();

                        parseuserget(jObj.getJSONObject("responseString").getJSONArray("users"));
                    } else {
                        if (jObj.getString("responseString").equalsIgnoreCase("Invalid token")) {
                            Intent i = new Intent();
                            i.putExtra("keyMessage", "Session Expired, Please Login ");
                            i.putExtra("keyAlert", "Session Expired");
                            i.setClassName(getPackageName(), "com.singleevent.sdk.View.TokenExpireAlertReceived");
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);

                        } else
                            Error_Dialog.show(jObj.getString("responseString"), JoinMeeting.this);
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
                    Error_Dialog.show("Timeout", JoinMeeting.this);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, context), JoinMeeting.this);
                } else if (VolleyErrorLis.isNetworkProblem(error)) {


                    userview.clear();
                    userview = Paper.book(appDetails.getAppId()).read("OfflineAttendeeList",new ArrayList<com.singleevent.sdk.model.User>());

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


        App.getInstance().addToRequestQueue(strReq, tag_string_req);
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));


    }


    private void parseuserget(JSONArray responseString) {


        try {
            userview.clear();
            Gson gson = new Gson();

            Random r = new Random();

            for (int i = 0; i < responseString.length(); i++) {
                String eventString = responseString.getJSONObject(i).toString();
                com.singleevent.sdk.model.User obj = gson.fromJson(eventString, com.singleevent.sdk.model.User.class);
                obj.setColor(Color.argb(255, r.nextInt(256), r.nextInt(256), r.nextInt(256)));

                userview.add(obj);


            }

            Collections.sort(userview, new Comparator<User>() {
                @Override
                public int compare(com.singleevent.sdk.model.User o1, com.singleevent.sdk.model.User o2) {
                    return o1.getFirst_name().compareToIgnoreCase(o2.getFirst_name());
                }
            });

            Paper.book(appDetails.getAppId()).write("ALLATEENDEE",userview);

        } catch (JSONException e) {
            e.printStackTrace();
        }




    }
    // Adding Chat
    public void discussChat(View view)
    {


        try {
            listView = (ListView) findViewById(R.id.chat_list_view);
            sendchat = (EditText) findViewById(R.id.sendchat);
            sendmsg = (ImageView) findViewById(R.id.sendmsg);
            chatedit=(TextView) findViewById(R.id.chatedit);
            chatedit1=(TextView) findViewById(R.id.chatedit1);
            askaquestin=(RelativeLayout)findViewById(R.id.mainview1) ;
            askaquestin.setVisibility(View.GONE);
            dynvideo = (RelativeLayout) findViewById(R.id.mainview);
            dynvideo.setVisibility(View.VISIBLE);
            chatedit.setBackground(Util.setdrawable(this,R.drawable.rectanglebackground,
                    Color.parseColor(appDetails.getTheme_color())));
            chatedit1.setBackground(Util.setdrawable(this,R.drawable.rectanglebackground,
                    Color.parseColor(appDetails.getTheme_color())));

            chatedit.setTextColor(getResources().getColor(R.color.white));
            chatedit1.setTextColor(getResources().getColor(R.color.white));

            quesedit=(TextView)findViewById(R.id.quesedit);
            quesedit1=(TextView)findViewById(R.id.quesedit1);
            quesedit.setBackground(Util.setdrawable(this,R.drawable.rectanglebackground,
                    Color.parseColor("#f5f6fa")));
            quesedit1.setBackground(Util.setdrawable(this,R.drawable.rectanglebackground,
                    Color.parseColor("#f5f6fa")));
            quesedit.setTextColor(Color.parseColor("#727272"));
            quesedit1.setTextColor(Color.parseColor("#727272"));
            chatArrayAdapter = new GroupMessageAdapter(context, Color.parseColor("#0a6a99"));
            listView.setAdapter(chatArrayAdapter);
            sendmsg.setOnClickListener(this::sendMessage);







        }catch (Exception e)
        {

        }
        getchat();
    }
    private void getchat() {

        final ProgressDialog dialog = new ProgressDialog(JoinMeeting.this, R.style.MyAlertDialogStyle);
        dialog.setMessage("Loading the User");
        dialog.show();
        String tag_string_req = "Chat_User";
        String url = "https://chat.webmobi.com/get_group_chats?" +"sender_id=" + Paper.book().read("userId")+"&recipient_id="+group_id;
        StringRequest strReq = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                try {
                    System.out.println(response);
                    JSONObject jObj = new JSONObject(response);


                    if (jObj.getBoolean("response")) {

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
                            Error_Dialog.show(jObj.getString("responseString"), JoinMeeting.this);
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
                    Error_Dialog.show("Timeout", JoinMeeting.this);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, JoinMeeting.this), JoinMeeting.this);
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    //   showconnectionerror(false);
                    Error_Dialog.show("Check your Internet Connection ", JoinMeeting.this);

                }

            }
        }) {
/*
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("token", Paper.book().read("token", ""));
                return headers;
            }*/
        };

        strReq.setShouldCache(false);
        App.getInstance().addToRequestQueue(strReq, tag_string_req);
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));


    }
    private void parseuser(JSONObject responseString) {

        try {

            Gson gson = new Gson();

            Random r = new Random();

            //  JSONArray sendmsg = responseString.getJSONArray("sendmsg");
            JSONArray recmsg = responseString.getJSONArray("messages");

//            for (int i = 0; i < sendmsg.length(); i++) {
//                String eventString = sendmsg.getJSONObject(i).toString();
//                ChatMSG obj = gson.fromJson(eventString, ChatMSG.class);
//                obj.setBadgecount(newmessages.containsKey(obj.getSender_id()) ? newmessages.get(obj.getSender_id()).getInt("badgecount") : 0);
//                obj.setColor(Color.argb(255, r.nextInt(256), r.nextInt(256), r.nextInt(256)));
//                messages.add(obj);
//            }

            for (int i = 0; i < recmsg.length(); i++) {
                String eventString = recmsg.getJSONObject(i).toString();
                JSONObject msg = recmsg.getJSONObject(i);
                //GroupChat obj = gson.fromJson(eventString, GroupChat.class);
                Paper.book(appDetails.getAppId()).read("A",userview);


                GroupChat gp=new GroupChat();
                if(userview!=null){
                    for(int j=0; j<userview.size(); j++)
                    {
                        if(userview.get(j).getUserid().equals(msg.getString("sender_id"))){
                            gp.setFrom_UserName(userview.get(j).getFirst_name()+ " " +userview.get(j).getLast_name());
                            gp.setProfile_pic(userview.get(j).getProfile_pic());

                        }
                    }

                }
                gp.setCreate_date(calheader(Long.parseLong(msg.getString("create_date"))));
                gp.setMessage_body(msg.getString("message_body"));
                gp.setRecipient_group_id(msg.getString("recipient_group_id"));
                gp.setSender_id(msg.getString("sender_id"));
                gp.setColor(Color.argb(255, r.nextInt(256), r.nextInt(256), r.nextInt(256)));
                if (UserID.equalsIgnoreCase(msg.getString("sender_id")))
                {   gp.setIncomingMessage(false);
                    gp.setProfile_pic(profile_pic);
                    gp.setFrom_UserName(username);
                }
                else
                    gp.setIncomingMessage(true);

                chatArrayAdapter.addMessage(gp);


            }



        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private String calheader(long senttime) {
        GroupChat message = new GroupChat();

        //calc time and adding header
        Calendar today = Calendar.getInstance();
        Calendar gettingtime = Calendar.getInstance();
        gettingtime.setTimeInMillis(senttime);
        SimpleDateFormat dateformat1 = new SimpleDateFormat(" EEE, MMM d,yyyy");
        SimpleDateFormat dateformat2 = new SimpleDateFormat(" h:mm a");
        Calendar Yesterday = Calendar.getInstance();
        Yesterday.add(Calendar.DATE, -1);

        Calendar cal = Calendar.getInstance();
        TimeZone tz = cal.getTimeZone();
        String zone, timeZone;
        timeZone = appDetails.getTimezone();
        if (timeZone == "") {
            zone = "Asia/Calcutta";
        } else if (timeZone.equalsIgnoreCase("IST")) {
            zone = "Asia/Calcutta";
        } else if (timeZone.equalsIgnoreCase("PST")) {
            zone = "America/Los_Angeles";
        } else if (timeZone.equalsIgnoreCase("EST")) {
            zone = "America/New_York";
        } else if (timeZone.equalsIgnoreCase("CST")) {
            zone = "America/Chicago";
        } else {
            zone = "Asia/Calcutta";
        }

        /* date formatter in local timezone */
        SimpleDateFormat sdf = new SimpleDateFormat("h:mm a"/*"dd/MM/yyyy HH:mm:ss" */);
        sdf.setTimeZone(TimeZone.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone(zone));

        /* print your timestamp and double check it's the date you expect */
        long timestamp = senttime;
        String localTime = sdf.format(timestamp);

        return localTime;

       /* String header;

        if ((dateformat1.format(today.getTime()).equals(dateformat1.format(gettingtime.getTime())))) {
            header = "Today";
        } else if ((dateformat1.format(Yesterday.getTime()).equals(dateformat1.format(gettingtime.getTime())))) {
            header = "Yesterday";
        } else {
            header = dateformat1.format(gettingtime.getTime());
        }
        if (!headergroup.contains(header)) {
            headergroup.add(header);
            message.setMessage_body(header);
            message.setSection(true);
            chatArrayAdapter.addMessage(message);
        }*/
        // return dateformat2.format(gettingtime.getTime());
    }


    public void  sendMessage(View view) {
        String msg = sendchat.getText().toString().trim();
        sendchat.setText("");
        String groupid=Paper.book().read("GROUP_ID");
        String groupname=Paper.book().read("Group_Name");
        String appid=appDetails.getAppId();
        String appname=appDetails.getAppName();

        mSocket.emit("message",groupid,groupname,UserID,username,appid,appname,"group",System.currentTimeMillis(),msg,"normal",1,Paper.book().read("Current_Event_Id",""));
        GroupChat message = new GroupChat();
        message.setTo_UserID(group_id);
        message.setTo_UserName(groupname);

        message.setFrom_UserID(UserID);
        message.setFrom_UserName(username);
        message.setAppid(appid);
        message.setAppname(appname);
        message.setChat_type("group");
        message.setMessage_body(msg);
        message.setMsg_datatype("normal");
        //    message.setMultievent_flag(multievent_flag);
        //  message.setEventid(eventid);
        message.setIncomingMessage(false);
        message.setCreate_date(calheader((System.currentTimeMillis())));
        message.setSection(false);
        chatArrayAdapter.addMessage(message);
    }
    public void questionAsk(View view)
    {
        quesedit=(TextView)findViewById(R.id.quesedit);
        quesedit1=(TextView)findViewById(R.id.quesedit1);
        quesedit.setBackground(Util.setdrawable(this,R.drawable.rectanglebackground,
                Color.parseColor(appDetails.getTheme_color())));
        quesedit1.setBackground(Util.setdrawable(this,R.drawable.rectanglebackground,
                Color.parseColor(appDetails.getTheme_color())));
        quesedit.setTextColor(getResources().getColor(R.color.white));
        quesedit1.setTextColor(getResources().getColor(R.color.white));

        chatedit=(TextView) findViewById(R.id.chatedit);
        chatedit1=(TextView) findViewById(R.id.chatedit1);
        chatedit.setBackground(Util.setdrawable(this,R.drawable.rectanglebackground,
                Color.parseColor("#f5f6fa")));
        chatedit1.setBackground(Util.setdrawable(this,R.drawable.rectanglebackground,
                Color.parseColor("#f5f6fa")));
        chatedit1.setTextColor(Color.parseColor("#727272"));

        chatedit.setTextColor(Color.parseColor("#727272"));
        dynvideo = (RelativeLayout) findViewById(R.id.mainview);
        askaquestin=(RelativeLayout)findViewById(R.id.mainview1) ;
        dynvideo.setVisibility(View.GONE);
        askaquestin.setVisibility(View.VISIBLE);



        questionsList = new ArrayList<>();
        item = (Agendadetails) getIntent().getSerializableExtra("AgendaList");
        askAQueAdapter = new AskAQueAdapter( this, questionsList );
        queue = Volley.newRequestQueue(this);
        RecyclerView recycler_view =(RecyclerView)findViewById( R.id.recyclerView);
        addYourQues =(TextView)findViewById(R.id.addYourQues);
        addYourQues.setBackground(Util.setdrawable(this,R.drawable.rectanglebackground,
                Color.parseColor(appDetails.getTheme_color())));


        recycler_view.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,
                false));
        //recycler_view.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recycler_view.setAdapter(askAQueAdapter);
        recycler_view.addOnItemTouchListener(new com.singleevent.sdk.View.RecyclerItemClickListener(this, recycler_view,
                new com.singleevent.sdk.View.RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        if (!questionsList.get(position).isUser_vote()){
                            questionsList.get(position).setUser_vote(false);
                            getAgandaVote( questionsList.get(position).getAgenda_id(),
                                    questionsList.get(position).getQuestion_id());

                        }
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                }));

        addYourQues.setOnClickListener(this);
        /*askAQueAdapter.setOnCardClickListner(this);*/
        getAgendaQuestions();
    }


    @Override
    protected void onResume() {
        super.onResume();

        SpannableString s = new SpannableString(title);
        setTitle(Util.applyFontToMenuItem(this, s));
        //tv_app_version_name.setText("Version : " + DataBaseStorage.getAppVersionName(this));

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.addYourQues){

            openDialogToAddQuestion();
        }
    }


    private void getAgandaVote(final int aganda_id,final int question_id) {

        final ProgressDialog dialog = new ProgressDialog(this,R.style.MyAlertDialogStyle);
        dialog.setMessage("Loading...");
        dialog.show();

        String tag_string_req = "add_question";
        String url = ApiList.Vote_Agenda_Question;

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                dialog.hide();
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getBoolean("response")){
                        Error_Dialog.show(object.getString("responseString"),
                                JoinMeeting.this );

                        getAgendaQuestions();

                    }else {
                        dialog.hide();
                        Error_Dialog.show(object.getString("responseString"),
                                JoinMeeting.this );
                    }

                } catch (JSONException e) {
                    dialog.hide();
                    e.printStackTrace();

                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.hide();
                Log.d(TAG,error.toString() );

            }
        }){

            @Override
            public Map<String, String> getParams() {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("userid", Paper.book().read("userId", ""));
                params.put("appid",appDetails.getAppId());
                params.put("agenda_id", String.valueOf(item.getAgendaId()));
                params.put("question_id", String.valueOf(question_id));
                return params;
            }
        };


        queue.add(strReq);
    }
    private void getAgendaQuestions() {

        String url = ApiList.Agenda_Questions+"appid="+appDetails.getAppId()+"&agenda_id="+agenda_id
                +"&userid="+ Paper.book().read("userId");
        String tag_string_req = "Agenda_question";

        final ProgressDialog dialog = new ProgressDialog(context,R.style.MyAlertDialogStyle);
        dialog.setMessage("Loading...");
        dialog.show();

        StringRequest strReq = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                dialog.hide();
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getBoolean("response")){
                        JSONArray jsonArray = object.getJSONArray("questions");
                        questionsList.clear();
                        for (int i = 0; i < jsonArray.length();i++ ){
                            Log.d(TAG,jsonArray.length()+"" );
                            JSONObject obje = jsonArray.getJSONObject(i);
                            eventQuestionModel = new EventQuestionModel();
                            eventQuestionModel.setQuestion_id(obje.getInt("question_id"));
                            eventQuestionModel.setQuestion(obje.getString("question").trim());
                            eventQuestionModel.setAgenda_id(obje.getInt("agenda_id"));
                            eventQuestionModel.setAppid(obje.getString("appid"));
                            eventQuestionModel.setUserid(obje.getString("userid"));
                            //  eventQuestionModel.setVote();
                            eventQuestionModel.setUp_votes(obje.getInt("up_votes"));
                            eventQuestionModel.setUser_vote(obje.getBoolean("user_vote"));
                            questionsList.add(eventQuestionModel);


                        }
                        askAQueAdapter.notifyDataSetChanged();


                    }else {
                        dialog.hide();
                        Error_Dialog.show(object.getString("responseString"),
                                JoinMeeting.this );
                    }

                } catch (JSONException e) {
                    dialog.hide();
                    e.printStackTrace();

                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.hide();
                if (error instanceof TimeoutError) {
                    Error_Dialog.show("Timeout", JoinMeeting.this);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, context), JoinMeeting.this);
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show("Please Check Your Internet Connection", JoinMeeting.this);
                }

            }
        });
        queue.add(strReq);

    }

    private void openDialogToAddQuestion() {
        final Dialog dialog = new Dialog(JoinMeeting.this,
                R.style.MaterialDialogSheet);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
        dialog.setContentView(R.layout.dialog_add_questions);
        dialog.setCancelable(true);
        dialog.show();

        TextView canceltxt = (TextView) dialog.findViewById(R.id.canceltxt);
        TextView savetxt = (TextView) dialog.findViewById(R.id.savetxt);
        savetxt.setBackground(Util.setdrawable(this, R.drawable.act_button_border,
                Color.parseColor(appDetails.getTheme_color())));
        canceltxt.setBackground(Util.setdrawable(this, R.drawable.act_button_border,
                Color.parseColor(appDetails.getTheme_color())));

        final TextView textLimit =(TextView) dialog.findViewById(R.id.textLimit);
        canceltxt.setTypeface(Util.regulartypeface(context));
        savetxt.setTypeface(Util.regulartypeface(context));

        final EditText questions = (EditText) dialog.findViewById(R.id.questions);
        questions.setTypeface(Util.regulartypeface(context));

        canceltxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        savetxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!questions.getText().toString().isEmpty()) {


                    if (questions.getText().toString().trim().length() < 8 ){

                        Toast.makeText(context, Util.applyFontToMenuItem(context,
                                new SpannableString("Question should be more than 8 characters.")),
                                Toast.LENGTH_SHORT).show();
                    }else {
                        getNewQuestion( questions.getText().toString() );
                        dialog.dismiss();
                    }

                }


            }
        });
        TextWatcher mTextEditorWatcher = new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //This sets a textview to the current length


                textLimit.setText(String.valueOf(1000 - s.length()));
            }

            public void afterTextChanged(Editable s) {
            }
        };
        questions.addTextChangedListener(mTextEditorWatcher);


    }

    private void getNewQuestion(final String question ) {

        final ProgressDialog dialog = new ProgressDialog(this,R.style.MyAlertDialogStyle);
        dialog.setMessage("Loading...");
        dialog.show();

        String tag_string_req = "add_question";
        String url = ApiList.Add_Agenda_Question;

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                dialog.hide();
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getBoolean("response")){
                        Error_Dialog.show(object.getString("responseString"),
                                JoinMeeting.this );
                        getAgendaQuestions();

                    }else {
                        dialog.hide();
                        Error_Dialog.show(object.getString("responseString"),
                                JoinMeeting.this );
                    }

                } catch (JSONException e) {
                    dialog.hide();
                    e.printStackTrace();

                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.hide();
                if (error instanceof TimeoutError) {
                    Error_Dialog.show("Timeout", JoinMeeting.this);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, context), JoinMeeting.this);
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show("Please Check Your Internet Connection", JoinMeeting.this);
                }


            }
        }){

            @Override
            public Map<String, String> getParams() {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("userid", Paper.book().read("userId", ""));
                params.put("appid", appDetails.getAppId());
                params.put("agenda_id", String.valueOf(item.getAgendaId()));
                params.put("question",question);
                return params;
            }
        };


        queue.add(strReq);
    }

}
