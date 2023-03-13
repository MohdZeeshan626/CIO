package com.singleevent.sdk.agora.openvcall.ui;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Rect;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;

import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.telecom.Call;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewStub;
import android.view.Window;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;

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
import com.singleevent.sdk.View.LeftActivity.askAquestion.AskAQuestionActivity;
import com.singleevent.sdk.View.LeftActivity.askAquestion.adapter.AskAQueAdapter;
import com.singleevent.sdk.View.LeftActivity.askAquestion.model.EventQuestionModel;
import com.singleevent.sdk.model.Agenda.Agendadetails;
import com.singleevent.sdk.model.AppDetails;
import com.singleevent.sdk.model.JoinUser;
import com.singleevent.sdk.model.LocalArraylist.ChatMSG;
import com.singleevent.sdk.model.User_Details;
import com.singleevent.sdk.R;
import com.singleevent.sdk.View.RightActivity.admin.model.EventUser;
import com.singleevent.sdk.agora.openvcall.model.AGEventHandler;
import com.singleevent.sdk.agora.openvcall.model.ConstantApp;
import com.singleevent.sdk.agora.openvcall.model.DuringCallEventHandler;
import com.singleevent.sdk.agora.openvcall.model.GroupChat;
import com.singleevent.sdk.agora.openvcall.model.Message;
import com.singleevent.sdk.agora.openvcall.model.User;
import com.singleevent.sdk.agora.openvcall.ui.layout.GridVideoViewContainer;
import com.singleevent.sdk.agora.openvcall.ui.layout.InChannelMessageListAdapter;
import com.singleevent.sdk.agora.openvcall.ui.layout.MessageListDecoration;
import com.singleevent.sdk.agora.openvcall.ui.layout.SmallVideoViewAdapter;
import com.singleevent.sdk.agora.openvcall.ui.layout.SmallVideoViewDecoration;
import com.singleevent.sdk.agora.propeller.Constant;
import com.singleevent.sdk.agora.propeller.UserStatusData;
import com.singleevent.sdk.agora.propeller.VideoInfoData;
import com.singleevent.sdk.agora.propeller.ui.RecyclerItemClickListener;
import com.singleevent.sdk.agora.propeller.ui.RtlLinearLayoutManager;
import io.agora.rtc.Constants;
import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.models.UserInfo;
import io.agora.rtc.video.VideoCanvas;
import io.agora.rtc.video.VideoEncoderConfiguration;
import io.paperdb.Paper;
import io.socket.client.IO;
import io.socket.emitter.Emitter;


public class CallActivity extends BaseActivity implements DuringCallEventHandler,View.OnClickListener{

    public static final int LAYOUT_TYPE_DEFAULT = 0;
    public static final int LAYOUT_TYPE_SMALL = 1;
    private static final String TAG = CallActivity.class.getSimpleName();
    private final static Logger log = LoggerFactory.getLogger(CallActivity.class);

    // should only be modified under UI thread
    private final HashMap<Integer, SurfaceView> mUidsList = new HashMap<>(); // uid = 0 || uid == EngineConfig.mUid
    public int mLayoutType = LAYOUT_TYPE_DEFAULT;
    private GridVideoViewContainer mGridVideoViewContainer;
    private RelativeLayout mSmallVideoViewDock;
    ArrayList<String> headergroup = new ArrayList<>();
    private volatile boolean mVideoMuted = false;
    private volatile boolean mAudioMuted = false;
    private volatile boolean mMixingAudio = false;
    RelativeLayout dynvideo,controlbutton,askaquestin;
    boolean ischatopen=true;
    boolean isqueopen;
    private com.singleevent.sdk.View.RecyclerItemClickListener.OnItemClickListener mListener;

    private volatile int mAudioRouting = Constants.AUDIO_ROUTE_DEFAULT;

    private volatile boolean mFullScreen = false;

    private boolean mIsLandscape = false;
    private final UserInfo mLocal = new UserInfo();
    private UserInfo mRemote;

    private InChannelMessageListAdapter mMsgAdapter;
    private ArrayList<Message> mMsgList;

    private SmallVideoViewAdapter mSmallVideoViewAdapter;

    private final Handler mUIHandler = new Handler();
    private ListView listView;
    EditText sendchat;
    ImageView sendmsg;
    AppDetails appDetails;
    String group_id,agenda_id;
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
    String uid;
    ArrayList<JoinUser> joinUsers;
    ArrayList<String> joinfrommobile;
    Agendadetails agendadetails;
    private AskAQueAdapter askAQueAdapter;
    private EventQuestionModel eventQuestionModel;
    private ArrayList<EventQuestionModel> questionsList ;
    private TextView addYourQues;
    RequestQueue queue;
    private Agendadetails item;
    TextView chatedit,quesedit,chatedit1,quesedit1;
    RelativeLayout bottom_cont;


    ArrayList<GroupChat> messages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        makeActivityContentShownUnderStatusBar();
        setContentView(R.layout.activity_call);
        user1= (ChatMSG) getIntent().getSerializableExtra("UserItem"); //Obtaining data


        userDetails = Paper.book().read("UserDetails");
        appDetails = Paper.book().read("Appdetails");
        userview = new ArrayList<>();
        UserID = Paper.book().read("userId");
        username=Paper.book().read("username");
        profile_pic=Paper.book().read("profile_pic");
        context = this;
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        newmessages = Paper.book(appDetails.getAppId()).read("Message", new HashMap<String, JSONObject>());
        dpWidth = displayMetrics.widthPixels * 0.10F;
        badgewidth = displayMetrics.widthPixels * 0.05F;
        messages = new ArrayList<>();
        joinfrommobile=new ArrayList<>();


        target=convertStrigyfy(UserID,Paper.book().read("GROUP_ID"));
        Bundle extras = getIntent().getExtras();
        if (extras == null)
            finish();
        agenda_id=extras.getString("Agenda_ID");
        agendadetails=(Agendadetails)extras.getSerializable("AgendaDetails");

        try {
           // mSocket = IO.socket(ApiList.CHAT_SERVER_U);
           mSocket = App.getInstance().getSocket();//have to use this
        }catch (Exception e)
        {

        }

        mSocket.on("confirm_connection", onConnect);
        //  mSocket.on("confirm_connection",onConfirm);
        mSocket.on("group_message", onConfirmMessage);
        mSocket.on("userCount_"+appDetails.getAppId()+"_"+agenda_id,onUserName);

        // mSocket.on("msg_ack",onConfirmMessage);
        mSocket.connect();

        // getting socket objects from application class

        // adapter = new ChatAdapter(context, messages, dpWidth, badgewidth, newmessages);

        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            ab.setCustomView(R.layout.ard_agora_actionbar_with_title);
        }
    }




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
    private Emitter.Listener onUserName = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    try {
                        JSONObject response = (JSONObject) args[0];
                        JSONArray jp= response.getJSONArray("totalusers");
                        joinUsers=new ArrayList<>();
                        Gson gson = new Gson();

                        for(int i=0; i<jp.length(); i++)
                        {
                            String temp = jp.getJSONObject(i).toString();
                            JoinUser obj = gson.fromJson(temp, JoinUser.class);
                            joinUsers.add(obj);
                        }
                        Paper.book().write("ActiveUserlist",joinUsers);
                    }catch (Exception e)
                    {

                    }
                }
            });
        }
    };

    private void getuser() {

        final ProgressDialog dialog = new ProgressDialog(CallActivity.this, R.style.MyAlertDialogStyle);
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
                            Error_Dialog.show(jObj.getString("responseString"), CallActivity.this);
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
                    Error_Dialog.show("Timeout", CallActivity.this);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, context), CallActivity.this);
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

            Collections.sort(userview, new Comparator<com.singleevent.sdk.model.User>() {
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

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mSocket.off("confirm_connection", onConnect);
        //  mSocket.on("confirm_connection",onConfirm);
        mSocket.off("group_message", onConfirmMessage);
        mSocket.disconnect();
    }


    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_call, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        int i=item.getItemId();

        if(i==R.id.action_options) {
            showCallOptions();
            return true;
        }
        else
            return super.onOptionsItemSelected(item);

    }
    @Override
    protected void initUIandEvent() {
        getuser();
        addEventHandler(this);
        String channelName = getIntent().getStringExtra(ConstantApp.ACTION_KEY_CHANNEL_NAME);
        String Role = getIntent().getStringExtra(ConstantApp.ACTION_KEY_USER_ROLE);

        group_id=getIntent().getStringExtra("group_id");


        gid=group_id.split(" ");
        discussChat(getCurrentFocus());



        if((Role.equalsIgnoreCase("attendee")))//|(Role.equalsIgnoreCase("speaker"))||(Role.equalsIgnoreCase("cohost")))
        {
/*
             rtcEngine().muteAllRemoteAudioStreams(true)
*/

          /*   rtcEngine().setChannelProfile(Constants.CLIENT_ROLE_AUDIENCE);
             rtcEngine().setClientRole(0);
             rtcEngine().disableAudio();
             SurfaceView surfaceV = RtcEngine.CreateRendererView(getApplicationContext());
             preview(true, surfaceV, 0);
*/
/*
 SurfaceView surfaceV = RtcEngine.CreateRendererView(getApplicationContext());
             onClickHideIME(surfaceV);

          rtcEngine().disableVideo();
          rtcEngine().enableLocalVideo(true);*/
            rtcEngine().setClientRole(Constants.CLIENT_ROLE_AUDIENCE);





        }
        else if(Role.equalsIgnoreCase("speaker")){
         //   rtcEngine().enableWebSdkInteroperability(true);
            rtcEngine().setChannelProfile(Constants.CHANNEL_PROFILE_COMMUNICATION);
            //rtcEngine().setClientRole(Constants.CLIENT_ROLE_BROADCASTER);
            rtcEngine().setAudioProfile(Constants.AUDIO_PROFILE_DEFAULT,Constants.AUDIO_PROFILE_SPEECH_STANDARD);
        }
        else if(Role.equalsIgnoreCase("cohost"))

        {
            rtcEngine().setChannelProfile(Constants.CHANNEL_PROFILE_COMMUNICATION);
            //rtcEngine().setClientRole(Constants.CLIENT_ROLE_BROADCASTER);
            rtcEngine().setAudioProfile(Constants.AUDIO_PROFILE_DEFAULT,Constants.AUDIO_PROFILE_SPEECH_STANDARD);

            // rtcEngine().setChannelProfile(Constants.CHANNEL_PROFILE_LIVE_BROADCASTING);

        }
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            TextView channelNameView = ((TextView) findViewById(R.id.ovc_page_title));
            channelNameView.setText(channelName);
        }

        // programmatically layout ui below of status bar/action bar
        LinearLayout eopsContainer = findViewById(R.id.extra_ops_container);
        RelativeLayout.MarginLayoutParams eofmp = (RelativeLayout.MarginLayoutParams) eopsContainer.getLayoutParams();
        eofmp.topMargin = getStatusBarHeight()/2 + getActionBarHeight() + getResources().getDimensionPixelOffset(R.dimen.activity_vertical_margin) / 2; // status bar + action bar + divider

        final String encryptionKey = getIntent().getStringExtra(ConstantApp.ACTION_KEY_ENCRYPTION_KEY);
        final String encryptionMode = getIntent().getStringExtra(ConstantApp.ACTION_KEY_ENCRYPTION_MODE);

        doConfigEngine(encryptionKey, encryptionMode);


        mGridVideoViewContainer = (GridVideoViewContainer) findViewById(R.id.grid_video_view_container);
        mGridVideoViewContainer.setItemEventHandler(new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                onBigVideoViewClicked(view, position);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }

            @Override
            public void onItemDoubleClick(View view, int position) {
                onBigVideoViewDoubleClicked(view, position);
            }
        });

        SurfaceView surfaceV = RtcEngine.CreateRendererView(getApplicationContext());
        /*preview(false,surfaceV,0);*/
        if((Role.equalsIgnoreCase("speaker"))||(Role.equalsIgnoreCase("cohost"))) {

            preview(true, surfaceV, 0);
            surfaceV.setZOrderOnTop(false);
            surfaceV.setZOrderMediaOverlay(false);

            mUidsList.put(0, surfaceV); // get first surface view
            mGridVideoViewContainer.initViewContainer(this, 0, mUidsList, mIsLandscape); // first is now full view
        }
        initMessageList();
        notifyMessageChanged(new Message(new User(0, null), "start join " + channelName + " as " + (config().mUid & 0xFFFFFFFFL)));
        //  String newuid=config().mUid+"_"+Role+"_"+"video";
        String agoraappId = context.getString(R.string.agora_app_id);

        uid=getAlphaNumericString(7);

        if(Role.equalsIgnoreCase("speaker"))
        {
            uid="4"+uid;
        }
        else if(Role.equalsIgnoreCase("cohost"))
        {
            uid="5"+uid;
        }
        else
        {
            uid="1"+uid;
        }

       // rtcEngine().registerLocalUserAccount(agoraappId,uid);
        int n=Integer.parseInt(uid);

        joinChannel(channelName, n);
        //joinChannel(channelName, uid);
        config().mUids=uid;
        joinfrommobile.add(uid);
        Paper.book().write("MobileActiveuser",joinfrommobile);
        mSocket.emit("agoraJoin", Paper.book().read("userId").toString(), config().mUids, appDetails.getAppId(), agenda_id);
        optional();
        showOrHideCtrlViews(false);
    }




    // function to generate a random string of length n
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

    private void onBigVideoViewClicked(View view, int position) {
        log.debug("onItemClick " + view + " " + position + " " + mLayoutType);
        toggleFullscreen();
    }

    private void onBigVideoViewDoubleClicked(View view, int position) {
        try {
            log.debug("onItemDoubleClick " + view + " " + position + " " + mLayoutType);

            if (mUidsList.size() < 2) {
                return;
            }

            UserStatusData user = mGridVideoViewContainer.getItem(position);
            int uid = (user.mUid == 0) ? config().mUid : user.mUid;

            if (mLayoutType == LAYOUT_TYPE_DEFAULT && mUidsList.size() != 1) {
                switchToSmallVideoView(uid);
            } else {
                switchToDefaultVideoView();
            }
        }catch (Exception e)
        {

        }
    }

    private void onSmallVideoViewDoubleClicked(View view, int position) {
        log.debug("onItemDoubleClick small " + view + " " + position + " " + mLayoutType);

        switchToDefaultVideoView();
    }

    private void makeActivityContentShownUnderStatusBar() {
        // https://developer.android.com/training/system-ui/status
        // May fail on some kinds of devices
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;

            decorView.setSystemUiVisibility(uiOptions);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setStatusBarColor(getResources().getColor(R.color.agora_blue));
            }
        }
    }

    private void showOrHideStatusBar(boolean hide) {
        // May fail on some kinds of devices
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            View decorView = getWindow().getDecorView();
            int uiOptions = decorView.getSystemUiVisibility();

            if (hide) {
                uiOptions |= View.SYSTEM_UI_FLAG_FULLSCREEN;
            } else {
                uiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
            }

            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    public void toggleFullscreen() {
        mFullScreen = !mFullScreen;

        showOrHideCtrlViews(mFullScreen);

        mUIHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                showOrHideStatusBar(mFullScreen);
            }
        }, 200); // action bar fade duration
    }

    public void  showOrHideCtrlViews(boolean hide) {
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            if (hide) {
                ab.hide();
            } else {
                ab.show();
            }
        }

        findViewById(R.id.extra_ops_container).setVisibility(hide ? View.INVISIBLE : View.VISIBLE);
        findViewById(R.id.bottom_action_container).setVisibility(hide ? View.INVISIBLE : View.VISIBLE);
           findViewById(R.id.bottom_cont).setVisibility(hide ? View.INVISIBLE : View.VISIBLE);

    }

    private void relayoutForVirtualKeyPad(int orientation) {
        int virtualKeyHeight = virtualKeyHeight();

        LinearLayout eopsContainer = findViewById(R.id.extra_ops_container);
        FrameLayout.MarginLayoutParams eofmp = (FrameLayout.MarginLayoutParams) eopsContainer.getLayoutParams();

        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            eofmp.rightMargin = virtualKeyHeight;
            eofmp.leftMargin = 0;
        } else {
            eofmp.leftMargin = 0;
            eofmp.rightMargin = 0;
        }

        LinearLayout bottomContainer = findViewById(R.id.bottom_container);
        FrameLayout.MarginLayoutParams fmp = (FrameLayout.MarginLayoutParams) bottomContainer.getLayoutParams();

        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            fmp.bottomMargin = 0;
            fmp.rightMargin = virtualKeyHeight;
            fmp.leftMargin = 0;
        } else {
            fmp.bottomMargin = virtualKeyHeight;
            fmp.leftMargin = 0;
            fmp.rightMargin = 0;
        }
    }

    private static final int CALL_OPTIONS_REQUEST = 3222;

    public synchronized void showCallOptions() {
        Intent i = new Intent(this, CallOptionsActivity.class);
        startActivityForResult(i, CALL_OPTIONS_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CALL_OPTIONS_REQUEST) {
            RecyclerView msgListView = (RecyclerView) findViewById(R.id.msg_list);
            // msgListView.setVisibility(Constant.DEBUG_INFO_ENABLED ? View.VISIBLE : View.INVISIBLE);
            msgListView.setVisibility(View.GONE);
        }
    }

    public void onClickHideIME(View view) {
        log.debug("onClickHideIME " + view);

        closeIME(findViewById(R.id.msg_content));
        findViewById(R.id.msg_input_container).setVisibility(View.GONE);
        findViewById(R.id.bottom_action_container).setVisibility(View.VISIBLE);
    }

    private void initMessageList() {
        mMsgList = new ArrayList<>();
        RecyclerView msgListView = (RecyclerView) findViewById(R.id.msg_list);

        mMsgAdapter = new InChannelMessageListAdapter(this, mMsgList);
        mMsgAdapter.setHasStableIds(true);
        msgListView.setAdapter(mMsgAdapter);
        msgListView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
        msgListView.addItemDecoration(new MessageListDecoration());
        msgListView.setVisibility(View.GONE);
    }

    private void notifyMessageChanged(Message msg) {
        mMsgList.add(msg);
        int MAX_MESSAGE_COUNT = 16;
        if (mMsgList.size() > MAX_MESSAGE_COUNT) {
            int toRemove = mMsgList.size() - MAX_MESSAGE_COUNT;
            for (int i = 0; i < toRemove; i++) {
                mMsgList.remove(i);
            }
        }

        mMsgAdapter.notifyDataSetChanged();
    }

    private void optional() {
        setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
    }

    private void optionalDestroy() {
    }

    private int getVideoEncResolutionIndex() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        int videoEncResolutionIndex = pref.getInt(ConstantApp.PrefManager.PREF_PROPERTY_VIDEO_ENC_RESOLUTION, ConstantApp.DEFAULT_VIDEO_ENC_RESOLUTION_IDX);
        if (videoEncResolutionIndex > ConstantApp.VIDEO_DIMENSIONS.length - 1) {
            videoEncResolutionIndex = ConstantApp.DEFAULT_VIDEO_ENC_RESOLUTION_IDX;

            // save the new value
            SharedPreferences.Editor editor = pref.edit();
            editor.putInt(ConstantApp.PrefManager.PREF_PROPERTY_VIDEO_ENC_RESOLUTION, videoEncResolutionIndex);
            editor.apply();
        }
        return videoEncResolutionIndex;
    }

    private int getVideoEncFpsIndex() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        int videoEncFpsIndex = pref.getInt(ConstantApp.PrefManager.PREF_PROPERTY_VIDEO_ENC_FPS, ConstantApp.DEFAULT_VIDEO_ENC_FPS_IDX);
        if (videoEncFpsIndex > ConstantApp.VIDEO_FPS.length - 1) {
            videoEncFpsIndex = ConstantApp.DEFAULT_VIDEO_ENC_FPS_IDX;

            // save the new value
            SharedPreferences.Editor editor = pref.edit();
            editor.putInt(ConstantApp.PrefManager.PREF_PROPERTY_VIDEO_ENC_FPS, videoEncFpsIndex);
            editor.apply();
        }
        return videoEncFpsIndex;
    }

    private void doConfigEngine(String encryptionKey, String encryptionMode) {
        VideoEncoderConfiguration.VideoDimensions videoDimension = ConstantApp.VIDEO_DIMENSIONS[0];//getVideoEncResolutionIndex()];
        VideoEncoderConfiguration.FRAME_RATE videoFps = ConstantApp.VIDEO_FPS[getVideoEncFpsIndex()];
        configEngine(videoDimension, videoFps, encryptionKey, encryptionMode);
    }

    public void onSwitchCameraClicked(View view) {
        RtcEngine rtcEngine = rtcEngine();
        rtcEngine.switchCamera();
    }

    public void onSwitchSpeakerClicked(View view) {
        RtcEngine rtcEngine = rtcEngine();
        rtcEngine.setEnableSpeakerphone(mAudioRouting != Constants.AUDIO_ROUTE_SPEAKERPHONE);
    }

    public void onFilterClicked(View view) {
        Constant.BEAUTY_EFFECT_ENABLED = !Constant.BEAUTY_EFFECT_ENABLED;

        if (Constant.BEAUTY_EFFECT_ENABLED) {
            setBeautyEffectParameters(Constant.BEAUTY_EFFECT_DEFAULT_LIGHTNESS, Constant.BEAUTY_EFFECT_DEFAULT_SMOOTHNESS, Constant.BEAUTY_EFFECT_DEFAULT_REDNESS);
            enablePreProcessor();
        } else {
            disablePreProcessor();
        }

        ImageView iv = (ImageView) view;

        iv.setImageResource(Constant.BEAUTY_EFFECT_ENABLED ? R.drawable.btn_filter : R.drawable.btn_filter_off);
    }

    @Override
    protected void deInitUIandEvent() {
        //  discussChat(getCurrentFocus());
        optionalDestroy();
        doLeaveChannel();
        removeEventHandler(this);
        mUidsList.clear();
    }

    private void doLeaveChannel() {
        leaveChannel(config().mChannel);
        preview(false, null, 0);
        mSocket.emit("agoraPeerleave",config().mUids,appDetails.getAppId(),agenda_id);

    }

    public void onHangupClicked(View view) {
        log.info("onHangupClicked " + view);


        onBackPressed();
        try {
            onBackPressed();
        }catch (Exception e){

        }
    }

    public void onVideoMuteClicked(View view) {
        log.info("onVoiceChatClicked " + view + " " + mUidsList.size() + " video_status: " + mVideoMuted + " audio_status: " + mAudioMuted);
        if (mUidsList.size() == 0) {
            return;
        }

        SurfaceView surfaceV = getLocalView();
        ViewParent parent;
        if (surfaceV == null || (parent = surfaceV.getParent()) == null) {
            log.warn("onVoiceChatClicked " + view + " " + surfaceV);
            return;
        }

        RtcEngine rtcEngine = rtcEngine();
        mVideoMuted = !mVideoMuted;

        if (mVideoMuted) {
            rtcEngine.disableVideo();
        } else {
            rtcEngine.enableVideo();
        }

        ImageView iv = (ImageView) view;

        iv.setImageResource(mVideoMuted ? R.drawable.btn_camera_off : R.drawable.mutevideo);

        hideLocalView(mVideoMuted);
    }

    private SurfaceView getLocalView() {
        for (HashMap.Entry<Integer, SurfaceView> entry : mUidsList.entrySet()) {
            if (entry.getKey() == 0 || entry.getKey() == config().mUid) {
                return entry.getValue();
            }
        }

        return null;
    }

    private void hideLocalView(boolean hide) {
        int uid = config().mUid;
        doHideTargetView(uid, hide);
    }

    private void doHideTargetView(int targetUid, boolean hide) {
        HashMap<Integer, Integer> status = new HashMap<>();
        status.put(targetUid, hide ? UserStatusData.VIDEO_MUTED : UserStatusData.DEFAULT_STATUS);
        if (mLayoutType == LAYOUT_TYPE_DEFAULT) {
            mGridVideoViewContainer.notifyUiChanged(mUidsList, targetUid, status, null);
        } else if (mLayoutType == LAYOUT_TYPE_SMALL) {
            UserStatusData bigBgUser = mGridVideoViewContainer.getItem(0);
            if (bigBgUser.mUid == targetUid) { // big background is target view
                mGridVideoViewContainer.notifyUiChanged(mUidsList, targetUid, status, null);
            } else { // find target view in small video view list
                log.warn("SmallVideoViewAdapter call notifyUiChanged " + mUidsList + " " + (bigBgUser.mUid & 0xFFFFFFFFL) + " target: " + (targetUid & 0xFFFFFFFFL) + "==" + targetUid + " " + status);
                mSmallVideoViewAdapter.notifyUiChanged(mUidsList, bigBgUser.mUid, status, null);
            }
        }
    }

    public void onVoiceMuteClicked(View view) {
        log.info("onVoiceMuteClicked " + view + " " + mUidsList.size() + " video_status: " + mVideoMuted + " audio_status: " + mAudioMuted);
        if (mUidsList.size() == 0) {
//            return;
        }

        RtcEngine rtcEngine = rtcEngine();
        rtcEngine.muteLocalAudioStream(mAudioMuted = !mAudioMuted);

        ImageView iv = (ImageView) view;

        iv.setImageResource(mAudioMuted ? R.drawable.btn_microphone_off : R.drawable.microphone);
    }

    public void onMixingAudioClicked(View view) {
        log.info("onMixingAudioClicked " + view + " " + mUidsList.size() + " video_status: " + mVideoMuted + " audio_status: " + mAudioMuted + " mixing_audio: " + mMixingAudio);

        if (mUidsList.size() == 0) {
            return;
        }

        mMixingAudio = !mMixingAudio;

        RtcEngine rtcEngine = rtcEngine();
        if (mMixingAudio) {
            rtcEngine.startAudioMixing(Constant.MIX_FILE_PATH, false, false, -1);
        } else {
            rtcEngine.stopAudioMixing();
        }

        ImageView iv = (ImageView) view;
        iv.setImageResource(mMixingAudio ? R.drawable.btn_audio_mixing : R.drawable.btn_audio_mixing_off);
    }

    public void discussChat(View view)
    {
        chatedit=(TextView) findViewById(R.id.chatedit);
        chatedit1=(TextView) findViewById(R.id.chatedit1);
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

        dynvideo = (RelativeLayout) findViewById(R.id.mainview);
        controlbutton= (RelativeLayout) findViewById(R.id.controlbutton);
        mGridVideoViewContainer= (GridVideoViewContainer) findViewById(R.id.grid_video_view_container);

        dynvideo.setVisibility(View.VISIBLE);
        ViewGroup.LayoutParams params = mGridVideoViewContainer.getLayoutParams();
        ViewGroup.LayoutParams params1 = controlbutton.getLayoutParams();
        params.height =1000;
        params1.height=980;
        try {
            askaquestin=(RelativeLayout)findViewById(R.id.mainview1) ;
            askaquestin.setVisibility(View.GONE);
            listView = (ListView) findViewById(R.id.chat_list_view);
            sendchat = (EditText) findViewById(R.id.sendchat);
            sendmsg = (ImageView) findViewById(R.id.sendmsg);
            chatArrayAdapter = new GroupMessageAdapter(context, Color.parseColor("#0a6a99"));
            listView.setAdapter(chatArrayAdapter);
            sendmsg.setOnClickListener(this::sendMessage);
        }catch (Exception e)
        {

        }
        getchat();



    }
    public void minimiseChat(View view)
    {
        if(!ischatopen) {
            ischatopen=true;
            bottom_cont=(RelativeLayout)findViewById(R.id.bottom_cont);
            bottom_cont.setBackground(Util.setdrawable(this,R.drawable.topcorner,
                    Color.parseColor("#ffffff")));
            LinearLayout.LayoutParams logolayoutParams = (LinearLayout.LayoutParams) bottom_cont.getLayoutParams();
            logolayoutParams.setMargins(0,0,0,0);
            dynvideo = (RelativeLayout) findViewById(R.id.mainview);
            controlbutton= (RelativeLayout) findViewById(R.id.controlbutton);
            mGridVideoViewContainer= (GridVideoViewContainer) findViewById(R.id.grid_video_view_container);

            dynvideo.setVisibility(View.VISIBLE);
            ViewGroup.LayoutParams params = mGridVideoViewContainer.getLayoutParams();
            ViewGroup.LayoutParams params1 = controlbutton.getLayoutParams();
            params.height =1000;
            params1.height=980;


            listView = (ListView) findViewById(R.id.chat_list_view);
            sendchat = (EditText) findViewById(R.id.sendchat);
            sendmsg = (ImageView) findViewById(R.id.sendmsg);
            chatArrayAdapter = new GroupMessageAdapter(context, Color.parseColor("#0a6a99"));
            listView.setAdapter(chatArrayAdapter);
            sendmsg.setOnClickListener(this::sendMessage);
            getchat();
        }
        else{
            ischatopen=false;
            bottom_cont=(RelativeLayout)findViewById(R.id.bottom_cont);
            bottom_cont.setBackground(Util.setdrawable(this,R.drawable.speakerbackground,
                    Color.parseColor("#ffffff")));
            LinearLayout.LayoutParams logolayoutParams = (LinearLayout.LayoutParams) bottom_cont.getLayoutParams();
            logolayoutParams.setMargins(6,0,6,10);
            bottom_cont.setLayoutParams(logolayoutParams);
            dynvideo = (RelativeLayout) findViewById(R.id.mainview);
            askaquestin=(RelativeLayout)findViewById(R.id.mainview1);
            controlbutton= (RelativeLayout) findViewById(R.id.controlbutton);
            mGridVideoViewContainer= (GridVideoViewContainer) findViewById(R.id.grid_video_view_container);
            dynvideo.setVisibility(View.GONE);
            askaquestin.setVisibility(View.GONE);
            ViewGroup.LayoutParams params = mGridVideoViewContainer.getLayoutParams();
            ViewGroup.LayoutParams params1 = controlbutton.getLayoutParams();
            params.height = ViewGroup.LayoutParams.MATCH_PARENT;
            params1.height = ViewGroup.LayoutParams.MATCH_PARENT;

        }
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
    public  void questionAsk(View view)
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
        askaquestin.setVisibility(View.VISIBLE);
        mGridVideoViewContainer= (GridVideoViewContainer) findViewById(R.id.grid_video_view_container);

        ViewGroup.LayoutParams params = mGridVideoViewContainer.getLayoutParams();
        ViewGroup.LayoutParams params1 = controlbutton.getLayoutParams();
        params.height =1000;
        params1.height=980;
        dynvideo.setVisibility(View.GONE);
        questionsList = new ArrayList<>();
        item = (Agendadetails) getIntent().getSerializableExtra("AgendaDetails");
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
                                CallActivity.this );

                        getAgendaQuestions();

                    }else {
                        dialog.hide();
                        Error_Dialog.show(object.getString("responseString"),
                                CallActivity.this );
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
                params.put("agenda_id", String.valueOf(aganda_id));
                params.put("question_id", String.valueOf(question_id));
                return params;
            }
        };


        queue.add(strReq);
    }
    private void getAgendaQuestions() {

        String url = ApiList.Agenda_Questions+"appid="+appDetails.getAppId()+"&agenda_id="+item.getAgendaId()
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
                                CallActivity.this );
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
                    Error_Dialog.show("Timeout", CallActivity.this);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, context), CallActivity.this);
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show("Please Check Your Internet Connection", CallActivity.this);
                }

            }
        });
        queue.add(strReq);

    }

    @Override
    public void onUserJoined(int uid) {
        log.debug("onUserJoined " + (uid & 0xFFFFFFFFL));

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                notifyMessageChanged(new Message(new User(0, null), "user " + (uid & 0xFFFFFFFFL) + " joined"));
            }
        });
    }


  /*  private void onUpdateUserAccountInfo() {
        //TextView userInfo = (TextView) findViewById(R.id.user_info);

        String info = "Local: " + (mLocal.uid & 0xFFFFFFFFL) + " " + mLocal.userAccount;

        if (mRemote != null) {
            info += ", Remote: " + (mRemote.uid & 0xFFFFFFFFL) + " " + mRemote.userAccount;
        }

        //   userInfo.setText(info);
    }*/



    @Override
    public void onFirstRemoteVideoDecoded(int uid, int width, int height, int elapsed) {
        log.debug("onFirstRemoteVideoDecoded " + (uid & 0xFFFFFFFFL) + " " + width + " " + height + " " + elapsed);
        int s=uid;
        doRenderRemoteUi(uid);
    }

    private void doRenderRemoteUi(final int uid) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isFinishing()) {
                    return;
                }

                if (mUidsList.containsKey(uid)) {
                    return;
                }

                SurfaceView surfaceV = RtcEngine.CreateRendererView(getApplicationContext());
                mUidsList.put(uid, surfaceV);



                boolean useDefaultLayout = mLayoutType == LAYOUT_TYPE_DEFAULT;

                surfaceV.setZOrderOnTop(true);
                surfaceV.setZOrderMediaOverlay(true);
              /*  Rect src = new Rect(s, mScrollY, mScrollX+mSurfaceWidth, mScrollY+mSurfaceHeight);
                Rect dst = new Rect(0, 0, mSurfaceWidth, mSurfaceHeight);
                surfaceV.drawBitmap (mLevelBitmap, src, dst, null);*/


                rtcEngine().setupRemoteVideo(new VideoCanvas(surfaceV, VideoCanvas.RENDER_MODE_FIT, uid));

                if (useDefaultLayout) {
                    log.debug("doRenderRemoteUi LAYOUT_TYPE_DEFAULT " + (uid & 0xFFFFFFFFL));
                    int length = String.valueOf(uid).length();
                    if(length==8){
                        switchToDefaultVideoView();

                    }
                    else{

                        int bigBgUid = mSmallVideoViewAdapter == null ? uid : mSmallVideoViewAdapter.getExceptedUid();
                        log.debug("doRenderRemoteUi LAYOUT_TYPE_SMALL " + (uid & 0xFFFFFFFFL) + " " + (bigBgUid & 0xFFFFFFFFL));
                        switchToDefaultVideoView();
                        onBigVideoViewDoubleClicked(surfaceV.getRootView(),mUidsList.size()-1);
                        toggleFullscreen();


                    }
                } else {
                    int bigBgUid = mSmallVideoViewAdapter == null ? uid : mSmallVideoViewAdapter.getExceptedUid();
                    log.debug("doRenderRemoteUi LAYOUT_TYPE_SMALL " + (uid & 0xFFFFFFFFL) + " " + (bigBgUid & 0xFFFFFFFFL));
                    switchToSmallVideoView(bigBgUid);
                }

                notifyMessageChanged(new Message(new User(0, null), "video from user " + (uid & 0xFFFFFFFFL) + " decoded"));

            }
        });
    }




    private void getchat() {

        final ProgressDialog dialog = new ProgressDialog(CallActivity.this, R.style.MyAlertDialogStyle);
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
                            Error_Dialog.show(jObj.getString("responseString"), CallActivity.this);
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
                    Error_Dialog.show("Timeout", CallActivity.this);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, CallActivity.this), CallActivity.this);
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    //   showconnectionerror(false);
                    Error_Dialog.show("Check your Internet Connection ", CallActivity.this);

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

    @Override
    public void onJoinChannelSuccess(String channel, final int uid, int elapsed) {
        log.debug("onJoinChannelSuccess " + channel + " " + (uid & 0xFFFFFFFFL) + " " + elapsed);
    }

    @Override
    public void onUserOffline(int uid, int reason) {
        log.debug("onUserOffline " + (uid & 0xFFFFFFFFL) + " " + reason);
        doRemoveRemoteUi(uid);
    }

    @Override
    public void onExtraCallback(final int type, final Object... data) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isFinishing()) {
                    return;
                }

                doHandleExtraCallback(type, data);
            }
        });
    }
    private void doHandleExtraCallback(int type, Object... data) {
        int peerUid;
        boolean muted;

        switch (type) {
            case AGEventHandler.EVENT_TYPE_ON_USER_AUDIO_MUTED:
                peerUid = (Integer) data[0];
                muted = (boolean) data[1];

                if (mLayoutType == LAYOUT_TYPE_DEFAULT) {
                    HashMap<Integer, Integer> status = new HashMap<>();
                    status.put(peerUid, muted ? UserStatusData.AUDIO_MUTED : UserStatusData.DEFAULT_STATUS);
                    mGridVideoViewContainer.notifyUiChanged(mUidsList, config().mUid, status, null);
                }

                break;

            case AGEventHandler.EVENT_TYPE_ON_USER_VIDEO_MUTED:
                peerUid = (Integer) data[0];
                muted = (boolean) data[1];

                doHideTargetView(peerUid, muted);

                break;

            case AGEventHandler.EVENT_TYPE_ON_USER_VIDEO_STATS:
                IRtcEngineEventHandler.RemoteVideoStats stats = (IRtcEngineEventHandler.RemoteVideoStats) data[0];

                if (Constant.SHOW_VIDEO_INFO) {
                    if (mLayoutType == LAYOUT_TYPE_DEFAULT) {
                        mGridVideoViewContainer.addVideoInfo(stats.uid, new VideoInfoData(stats.width, stats.height, stats.delay, stats.rendererOutputFrameRate, stats.receivedBitrate));
                        int uid = config().mUid;
                        int profileIndex = getVideoEncResolutionIndex();

                        String resolution = getResources().getStringArray(R.array.string_array_resolutions)[profileIndex];
                        String fps = getResources().getStringArray(R.array.string_array_frame_rate)[profileIndex];

                        String[] rwh = resolution.split("x");
                        int width = Integer.valueOf(rwh[0]);
                        int height = Integer.valueOf(rwh[1]);

                        mGridVideoViewContainer.addVideoInfo(uid, new VideoInfoData(width > height ? width : height,
                                width > height ? height : width,
                                0, Integer.valueOf(fps), Integer.valueOf(0)));
                    }
                } else {
                    mGridVideoViewContainer.cleanVideoInfo();
                }

                break;

            case AGEventHandler.EVENT_TYPE_ON_SPEAKER_STATS:
                IRtcEngineEventHandler.AudioVolumeInfo[] infos = (IRtcEngineEventHandler.AudioVolumeInfo[]) data[0];

                if (infos.length == 1 && infos[0].uid == 0) { // local guy, ignore it
                    break;
                }

                if (mLayoutType == LAYOUT_TYPE_DEFAULT) {
                    HashMap<Integer, Integer> volume = new HashMap<>();

                    for (IRtcEngineEventHandler.AudioVolumeInfo each : infos) {
                        peerUid = each.uid;
                        int peerVolume = each.volume;

                        if (peerUid == 0) {
                            continue;
                        }
                        volume.put(peerUid, peerVolume);
                    }
                    mGridVideoViewContainer.notifyUiChanged(mUidsList, config().mUid, null, volume);
                }

                break;

            case AGEventHandler.EVENT_TYPE_ON_APP_ERROR:
                int subType = (int) data[0];

                if (subType == ConstantApp.AppError.NO_CONNECTION_ERROR) {
                    String msg = getString(R.string.msg_connection_error);
                    notifyMessageChanged(new Message(new User(0, null), msg));
                    showLongToast(msg);
                }

                break;

            case AGEventHandler.EVENT_TYPE_ON_DATA_CHANNEL_MSG:

                peerUid = (Integer) data[0];
                final byte[] content = (byte[]) data[1];
                notifyMessageChanged(new Message(new User(peerUid, String.valueOf(peerUid)), new String(content)));

                break;

            case AGEventHandler.EVENT_TYPE_ON_AGORA_MEDIA_ERROR: {
                int error = (int) data[0];
                String description = (String) data[1];

                notifyMessageChanged(new Message(new User(0, null), error + " " + description));

                break;
            }

            case AGEventHandler.EVENT_TYPE_ON_AUDIO_ROUTE_CHANGED:
                notifyHeadsetPlugged((int) data[0]);

                break;

        }
    }

    private void requestRemoteStreamType(final int currentHostCount) {
        log.debug("requestRemoteStreamType " + currentHostCount);
    }

    private void doRemoveRemoteUi(final int uid) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isFinishing()) {
                    return;
                }

                Object target = mUidsList.remove(uid);
                if (target == null) {
                    return;
                }

                int bigBgUid = -1;
                if (mSmallVideoViewAdapter != null) {
                    bigBgUid = mSmallVideoViewAdapter.getExceptedUid();
                }

                log.debug("doRemoveRemoteUi " + (uid & 0xFFFFFFFFL) + " " + (bigBgUid & 0xFFFFFFFFL) + " " + mLayoutType);

                if (mLayoutType == LAYOUT_TYPE_DEFAULT || uid == bigBgUid) {
                    switchToDefaultVideoView();
                } else {
                    switchToSmallVideoView(bigBgUid);
                }

                notifyMessageChanged(new Message(new User(0, null), "user " + (uid & 0xFFFFFFFFL) + " left"));
            }
        });
    }

    private void switchToDefaultVideoView() {
        try {
            if (mSmallVideoViewDock != null) {
                mSmallVideoViewDock.setVisibility(View.GONE);
            }
            mGridVideoViewContainer.initViewContainer(this, config().mUid, mUidsList, mIsLandscape);

            mLayoutType = LAYOUT_TYPE_DEFAULT;
            boolean setRemoteUserPriorityFlag = false;
            int sizeLimit = mUidsList.size();
            if (sizeLimit > ConstantApp.MAX_PEER_COUNT + 1) {
                sizeLimit = ConstantApp.MAX_PEER_COUNT + 1;
            }
            for (int i = 0; i < sizeLimit; i++) {
                int uid = mGridVideoViewContainer.getItem(i).mUid;
                if (config().mUid != uid) {
                    if (!setRemoteUserPriorityFlag) {
                        setRemoteUserPriorityFlag = true;
                        rtcEngine().setRemoteUserPriority(uid, Constants.USER_PRIORITY_HIGH);
                        log.debug("setRemoteUserPriority USER_PRIORITY_HIGH " + mUidsList.size() + " " + (uid & 0xFFFFFFFFL));
                    }
                    else {

                        //rtcEngine().setRemoteUserPriority(uid, Constants.USER_PRIORITY_NORMAL);
                        log.debug("setRemoteUserPriority USER_PRIORITY_NORANL " + mUidsList.size() + " " + (uid & 0xFFFFFFFFL));

                    }
                }
            }
        }catch (Exception e)
        {

        }
        showOrHideCtrlViews(false);

    }

    private void switchToSmallVideoView(int bigBgUid) {

        try {
            HashMap<Integer, SurfaceView> slice = new HashMap<>(1);
            slice.put(bigBgUid, mUidsList.get(bigBgUid));
            Iterator<SurfaceView> iterator = mUidsList.values().iterator();
            while (iterator.hasNext()) {
                SurfaceView s = iterator.next();
                s.setZOrderOnTop(true);
                s.setZOrderMediaOverlay(true);
            }

            mUidsList.get(bigBgUid).setZOrderOnTop(false);
            mUidsList.get(bigBgUid).setZOrderMediaOverlay(false);

            mGridVideoViewContainer.initViewContainer(this, bigBgUid, slice, mIsLandscape);

            bindToSmallVideoView(bigBgUid);

            mLayoutType = LAYOUT_TYPE_SMALL;

            requestRemoteStreamType(mUidsList.size());
        }catch (Exception e)
        {

        }
    }

    private void bindToSmallVideoView(int exceptUid) {
        if (mSmallVideoViewDock == null) {
            ViewStub stub = (ViewStub) findViewById(R.id.small_video_view_dock);
            mSmallVideoViewDock = (RelativeLayout) stub.inflate();
        }

        boolean twoWayVideoCall = mUidsList.size() == 2;

        RecyclerView recycler = (RecyclerView) findViewById(R.id.small_video_view_container);

        boolean create = false;

        if (mSmallVideoViewAdapter == null) {
            create = true;
            mSmallVideoViewAdapter = new SmallVideoViewAdapter(this, config().mUid, exceptUid, mUidsList);
            mSmallVideoViewAdapter.setHasStableIds(true);
        }
        recycler.setHasFixedSize(true);

        log.debug("bindToSmallVideoView " + twoWayVideoCall + " " + (exceptUid & 0xFFFFFFFFL));

        if (twoWayVideoCall) {
            recycler.setLayoutManager(new RtlLinearLayoutManager(getApplicationContext(), RtlLinearLayoutManager.HORIZONTAL, false));
        } else {
            recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        }
        recycler.addItemDecoration(new SmallVideoViewDecoration());
        recycler.setAdapter(mSmallVideoViewAdapter);
        recycler.addOnItemTouchListener(new RecyclerItemClickListener(getBaseContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }

            @Override
            public void onItemLongClick(View view, int position) {

            }

            @Override
            public void onItemDoubleClick(View view, int position) {
                onSmallVideoViewDoubleClicked(view, position);
            }
        }));

        recycler.setDrawingCacheEnabled(true);
        recycler.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_AUTO);

        if (!create) {
            mSmallVideoViewAdapter.setLocalUid(config().mUid);
            mSmallVideoViewAdapter.notifyUiChanged(mUidsList, exceptUid, null, null);
        }
        for (Integer tempUid : mUidsList.keySet()) {
            if (config().mUid != tempUid) {
                if (tempUid == exceptUid) {
                    rtcEngine().setRemoteUserPriority(tempUid, Constants.USER_PRIORITY_HIGH);
                    log.debug("setRemoteUserPriority USER_PRIORITY_HIGH " + mUidsList.size() + " " + (tempUid & 0xFFFFFFFFL));
                } else {
                   // rtcEngine().setRemoteUserPriority(tempUid, Constants.USER_PRIORITY_NORMAL);
                    log.debug("setRemoteUserPriority USER_PRIORITY_NORANL " + mUidsList.size() + " " + (tempUid & 0xFFFFFFFFL));
                }
            }
        }
        recycler.setVisibility(View.VISIBLE);
        mSmallVideoViewDock.setVisibility(View.VISIBLE);
    }

    public void notifyHeadsetPlugged(final int routing) {
        log.info("notifyHeadsetPlugged " + routing + " " + mVideoMuted);

        mAudioRouting = routing;

        ImageView iv = (ImageView) findViewById(R.id.switch_speaker_id);
        if (mAudioRouting == Constants.AUDIO_ROUTE_SPEAKERPHONE) {
            iv.setImageResource(R.drawable.audio);
        } else {
            iv.setImageResource(R.drawable.btn_speaker_off);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mIsLandscape = newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE;

        if (mLayoutType == LAYOUT_TYPE_DEFAULT) {
            switchToDefaultVideoView();
        } else if (mSmallVideoViewAdapter != null) {
            switchToSmallVideoView(mSmallVideoViewAdapter.getExceptedUid());
        }
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.addYourQues){

            openDialogToAddQuestion();
        }
    }
    private void openDialogToAddQuestion() {
        final Dialog dialog = new Dialog(CallActivity.this,
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
                                CallActivity.this );
                        getAgendaQuestions();

                    }else {
                        dialog.hide();
                        Error_Dialog.show(object.getString("responseString"),
                                CallActivity.this );
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
                    Error_Dialog.show("Timeout", CallActivity.this);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, context), CallActivity.this);
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show("Please Check Your Internet Connection", CallActivity.this);
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
