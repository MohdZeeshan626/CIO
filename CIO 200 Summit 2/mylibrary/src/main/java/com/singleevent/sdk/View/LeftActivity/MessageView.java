package com.singleevent.sdk.View.LeftActivity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.singleevent.sdk.ApiList;
import com.singleevent.sdk.App;
import com.singleevent.sdk.Custom_View.Error_Dialog;
import com.singleevent.sdk.Custom_View.Util;
import com.singleevent.sdk.Custom_View.VolleyErrorLis;
import com.singleevent.sdk.Left_Adapter.MessagesAdapter;
import com.singleevent.sdk.model.AppDetails;
import com.singleevent.sdk.model.LocalArraylist.ChatMSG;
import com.singleevent.sdk.model.LocalArraylist.ChatMessage;
import com.singleevent.sdk.R;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import io.paperdb.Paper;
import io.socket.emitter.Emitter;

/**
 * Created by Admin on 6/12/2017.
 */

public class MessageView extends AppCompatActivity {
    Context context;
    AppDetails appDetails;
    ChatMSG user;
    String UserID, Username, appid;
    private EditText mInputMessageView;
    AppDetails d;
    ImageView sendButton;
    private ListView listView;
    private io.socket.client.Socket mSocket;
    private MessagesAdapter chatArrayAdapter;
    ArrayList<String> headergroup = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Paper.init(this);
        setContentView(R.layout.s_sendmsg);
        appDetails = Paper.book().read("Appdetails");
        UserID = Paper.book(appDetails.getAppId()).read("userId");
        context = MessageView.this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));
        setSupportActionBar(toolbar);


        Bundle extras = getIntent().getExtras();
        if (extras == null)
            finish();

        // getting agenda details from fragment
        user = (ChatMSG) getIntent().getSerializableExtra("UserItem"); //Obtaining data
        Username = user.getSender_name();
        listView = (ListView) findViewById(R.id.chat_list_view);
        mInputMessageView = (EditText) findViewById(R.id.chat_edit_text1);
        sendButton = (ImageView) findViewById(R.id.enter_chat1);

        //initializing adapter

        chatArrayAdapter = new MessagesAdapter(context, Color.parseColor(appDetails.getTheme_color()));
        listView.setAdapter(chatArrayAdapter);


        //clciklistener for send button
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptSend();
                InputMethodManager imm = (InputMethodManager) getSystemService(context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);//to hide the input keyboard
            }
        });


        mInputMessageView.setOnKeyListener(keyListener);
        mInputMessageView.addTextChangedListener(watcher);


        // getting socket objects from application class
        mSocket = App.getInstance().getSocket();
        mSocket.on("confirm_connection", onConnect);
        mSocket.on("confirm_checkin", onConfirm);
        mSocket.on("message", onNewMessage);
        mSocket.on("msg_ack", onConfirmMessage);
        mSocket.connect();

        // Loading chat history
        Loadingdata();

        // clearing previous unread msg

        HashMap<String, JSONObject> newmessages = Paper.book(appDetails.getAppId()).read("Message", new HashMap<String, JSONObject>());
        if (newmessages.containsKey(user.getSender_id())) {
            newmessages.remove(user.getSender_id());
            Paper.book(appDetails.getAppId()).write("Message", newmessages);
        }
    }

    private void Loadingdata() {

        String tag_string_req = "Attendee";
        String mid;
        if(user.getSender_id()!=null)
            mid=user.getSender_id();
        else
            mid=user.getRecipient_id();
        String url = ApiList.ChatHistory + appDetails.getAppId() + "&user_id=" + UserID + "&recipient_id=" + mid + "&chat_type=single";
        Log.d("url is", url);
        final ProgressDialog pDialog = new ProgressDialog(context,R.style.MyAlertDialogStyle);
        pDialog.setMessage("Loading history ...");
        pDialog.show();
        StringRequest strReq = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                pDialog.dismiss();
                try {
                    JSONObject jObj = new JSONObject(response);
                    if (jObj.getBoolean("response")) {
                        JSONArray chatlist = jObj.getJSONObject("responseString").getJSONArray("messages");
                        int length = chatlist.length() - 1;
                        for (int i = length; i >= 0; i--) {
                            JSONObject chatdata = chatlist.getJSONObject(i);
                            ChatMessage message = new ChatMessage();
                            message.setId(chatdata.getString("id"));
                            message.setUserID(chatdata.getString("sender_id"));
                            message.setUsername(chatdata.getString("sender_name"));
                            message.setDate(calheader(Long.parseLong(chatdata.getString("create_date"))));
                            message.setMessage(chatdata.getString("message_body"));
                            message.setMsgtype(chatdata.getString("msg_datatype"));
                            if (UserID.equalsIgnoreCase(chatdata.getString("sender_id")))
                                message.setIncomingMessage(false);
                            else
                                message.setIncomingMessage(true);

                            message.setSection(false);

                            chatArrayAdapter.addMessage(message);

                        }

                    } else {
                        if (jObj.getString("responseString").equalsIgnoreCase("Invalid token")) {
                            Error_Dialog.show("Session is Expired Please Login", MessageView.this);


                        } else
                            Error_Dialog.show(jObj.getString("responseString"), MessageView.this);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                if (error instanceof TimeoutError) {
                    Error_Dialog.show("Timeout", MessageView.this);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, context), MessageView.this);
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show("Please Check Your Internet Connection", MessageView.this);
                }
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("token", Paper.book(appDetails.getAppId()).read("token", ""));
                return headers;
            }
        };


        // Adding request to request queue
        App.getInstance().addToRequestQueue(strReq, tag_string_req);
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));


    }


    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mSocket.emit("checkin", UserID);
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
                    // Error_Dialog.show("Connected", MessageView.this);
                }
            });
        }
    };

    private Emitter.Listener onConfirmMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String id = (String) args[0];
                    String msg = (String) args[1];
                    String fromuserid = (String) args[2];
                    String fromname = (String) args[3];
                    String appid = (String) args[4];
                    String appname = (String) args[5];
                    String time = (String) args[6];
                    String msgtype = (String) args[7];

                    ChatMessage message = new ChatMessage();
                    message.setId(id);
                    message.setUserID(fromuserid);
                    message.setAppid(appid);
                    message.setAppname(appname);
                    message.setMsgtype(msgtype);
                    message.setMessage(msg);
                    message.setUsername(fromname);
                    message.setIncomingMessage(false);
                    message.setDate(calheader(Long.parseLong(time)));
                    message.setSection(false);
                    chatArrayAdapter.addMessage(message);


                }
            });
        }
    };


    private void attemptSend() {
        String msg = mInputMessageView.getText().toString().trim();
        mInputMessageView.setText("");
        mSocket.emit("message", user.getSender_id(), user.getSender_name(), UserID, Paper.book().read("username"), appDetails.getAppId(), appDetails.getAppName(), "single", System.currentTimeMillis(), msg, "message");

        Intent chatIntent = new Intent("com.local.msg." + appDetails.getAppId());
        chatIntent.putExtra("id", user.getSender_id());
        chatIntent.putExtra("lastmsg", msg);
        chatIntent.putExtra("time", System.currentTimeMillis());
        boolean isRegistered = LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(chatIntent);


    }

    private EditText.OnKeyListener keyListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {

            // If the event is a key-down event on the "enter" button
            if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                    (keyCode == KeyEvent.KEYCODE_ENTER)) {
                // Perform action on key press

                EditText editText = (EditText) v;

                if (v == mInputMessageView) {
                    attemptSend();
                }

                mInputMessageView.setText("");

                return true;
            }
            return false;

        }
    };


    private final TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            if (mInputMessageView.getText().toString().equals("")) {

            } else {
                sendButton.setImageResource(R.drawable.ic_chat_send);

            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (editable.length() == 0) {
                sendButton.setImageResource(R.drawable.ic_chat_send);
            } else {
                sendButton.setImageResource(R.drawable.ic_chat_send_active);
            }
        }
    };


    private String calheader(long senttime) {

        ChatMessage message = new ChatMessage();

        //calc time and adding header
        Calendar today = Calendar.getInstance();
        Calendar gettingtime = Calendar.getInstance();
        gettingtime.setTimeInMillis(senttime);
        SimpleDateFormat dateformat1 = new SimpleDateFormat(" EEE, MMM d,yyyy");
        SimpleDateFormat dateformat2 = new SimpleDateFormat(" h:mm a");
        Calendar Yesterday = Calendar.getInstance();
        Yesterday.add(Calendar.DATE, -1);

        String header;

        if ((dateformat1.format(today.getTime()).equals(dateformat1.format(gettingtime.getTime())))) {
            header = "Today";
        } else if ((dateformat1.format(Yesterday.getTime()).equals(dateformat1.format(gettingtime.getTime())))) {
            header = "Yesterday";
        } else {
            header = dateformat1.format(gettingtime.getTime());
        }
        if (!headergroup.contains(header)) {
            headergroup.add(header);
            message.setMessage(header);
            message.setSection(true);
            chatArrayAdapter.addMessage(message);
        }
        return dateformat2.format(gettingtime.getTime());
    }

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String id = (String) args[0];
                    String msg = (String) args[1];
                    String fromuserid = (String) args[2];
                    String fromname = (String) args[3];
                    String appid = (String) args[4];
                    String appname = (String) args[5];
                    String time = (String) args[6];
                    String msgtype = (String) args[7];

                    if (user.getSender_id().equalsIgnoreCase(fromuserid)) {
                        ChatMessage message = new ChatMessage();
                        message.setId(id);
                        message.setUserID(fromuserid);
                        message.setAppid(appid);
                        message.setAppname(appname);
                        message.setMsgtype(msgtype);
                        message.setMessage(msg);
                        message.setUsername(fromname);
                        message.setIncomingMessage(true);
                        message.setDate(calheader(Long.parseLong(time)));
                        message.setSection(false);
                        chatArrayAdapter.addMessage(message);
                    }

                    Intent chatIntent = new Intent("com.local.msg." + appDetails.getAppId());
                    chatIntent.putExtra("id", fromuserid);
                    chatIntent.putExtra("lastmsg", msg);
                    chatIntent.putExtra("time", Long.parseLong(time));
                    boolean isRegistered = LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(chatIntent);


                }
            });
        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(broadcastReceiver);
        mSocket.off("confirm_connection", onConnect);
        mSocket.off("confirm_checkin", onConfirm);
        mSocket.off("message", onNewMessage);
        mSocket.disconnect();
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

    @Override
    protected void onResume() {
        super.onResume();

        try {
            SpannableString s = new SpannableString(Username);
            setTitle(Util.applyFontToMenuItem(this, s));
            LocalBroadcastManager.getInstance(context).registerReceiver(broadcastReceiver, new IntentFilter("com.mychats." + appDetails.getAppId() + "." + user.getSender_id()));
        }catch (Exception e)
        {

        }

    }


    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

        }
    };


}
