package com.singleevent.sdk.View.LeftActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.text.SpannableString;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.makeramen.roundedimageview.RoundedImageView;
import com.singleevent.sdk.ApiList;
import com.singleevent.sdk.App;
import com.singleevent.sdk.Custom_View.Error_Dialog;
import com.singleevent.sdk.Custom_View.TxtVCustomFonts;
import com.singleevent.sdk.Custom_View.Util;
import com.singleevent.sdk.Custom_View.VolleyErrorLis;
import com.singleevent.sdk.Left_Adapter.AgendaAdapter;
import com.singleevent.sdk.Left_Adapter.floorlistadapter;
import com.singleevent.sdk.View.RightActivity.admin.checkin.VolleyResponseListener;
import com.singleevent.sdk.View.RightActivity.admin.checkin.VolleyUtils;
import com.singleevent.sdk.View.RightActivity.admin.utils.Urls;
import com.singleevent.sdk.agora.openvcall.ui.MainActivity;
import com.singleevent.sdk.model.Agenda.Agendadetails;
import com.singleevent.sdk.model.Agenda.LocationDetails;
import com.singleevent.sdk.model.AppDetails;
import com.singleevent.sdk.model.Events;
import com.singleevent.sdk.model.Items;
import com.singleevent.sdk.model.LocalArraylist.Notes;
import com.singleevent.sdk.model.LocalArraylist.Rating;
import com.singleevent.sdk.model.LocalArraylist.agendaspeakerlist;
import com.singleevent.sdk.model.Map.Mapfloorplan;
import com.singleevent.sdk.R;
import com.singleevent.sdk.View.LeftActivity.askAquestion.AskAQuestionActivity;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import cyd.awesome.material.AwesomeText;
import cyd.awesome.material.FontCharacterMaps;
import io.paperdb.Paper;

/**
 * Created by Admin on 27/06/2019.
 */

public class AgendaDetails extends AppCompatActivity implements View.OnClickListener, VolleyResponseListener {

    private static final int LOGIN_REQ_CODE = 108;
    public static final String CHECK_IN_REQ_TAG = "check_in_users0";
    public static final String CHECK_IN_REQ_TAG1 = "check_in_users1";
    TextView etitle, date, time, category, venue, s1, txtnotes, txtsch, txtrate, p1, txtpdf, filesize;
    RelativeLayout loc, addnotes, addsch, ratingbar;
    ArrayList<Items> speakerslist = new ArrayList<>();
    ArrayList<Items> pollingList = new ArrayList<>();
    LinearLayout speakerdetails, speakers, pdfdetails;
    RelativeLayout live_broad;
    TxtVCustomFonts idownload;
    TextView des, tv_des_head_ing;
    LinearLayout rdes;
    Button joinmeeting;
    private double ImgWidth, Imgheight;
    Agendadetails item;
    int itempos;
    Items items;
    public CounterClass sessionstimer;
    String c_name ;
    String role,group_id,group_name;
    boolean isAdmin;

    long Agendadate;
    private SimpleDateFormat myFormat;
    private ArrayList<Events> events = new ArrayList<Events>();
    private ArrayList<Items> pollingCategories = new ArrayList<>();
    ArrayList<agendaspeakerlist> speakername = new ArrayList<>();
    Events e;
    int pos,typeid,polltypeid;
    int mappos;
    String maptitle;
    WebView fvenue;
    String floordetail;
    int tabpos;
    String title;
    private int mDisplayDays;
    private int mDisplayHours;
    private int mDisplayMinutes;
    private int mDisplaySeconds;

    AppDetails appDetails;
    String appid;
    int agendaid;
    AwesomeText inote, isch, ipdf, dots;
    String action, token;
    List<Integer> list;
    HashMap<Integer, Notes> Noteslist;
    HashMap<Integer, Rating> Ratinglist;
    Context context;
    RatingBar ratinBar;
    Runnable runnable;
    private ListView floorlistview;
    private RelativeLayout ask_question_layout,
            ask_survey_layout, rl_floor_locHead,ask_poll,floor_map,live_stream,recorded_video;
    private String userId;
    private RelativeLayout RL_floorlist,fullagenda;
    ArrayList<Integer> agendaidlist=new ArrayList<Integer>();
    ArrayList<Integer> pollid=new ArrayList<Integer>();
    Button joinsession;
    LinearLayout timertoshow;
    TextView txtdayshow, txthrshow, txtminshow, txtsecshow;
    boolean b;
    LinearLayout limitofseat;
    RelativeLayout limitseat;
    int totalseat,availseat, seatavailable;
    String status;
    TextView enroll,totalusers;
    ImageView plus;
    Button limit;
    public String userid,agendaidd;
    private RequestQueue queue1;
    int apos;
    ArrayList<String> userid1 = new ArrayList<>();
    ArrayList<String> date1 = new ArrayList<>();
    ArrayList<String> email1 = new ArrayList<>();
    ArrayList<String> agendaid1 = new ArrayList<>();
    ArrayList<String> checkdate1 = new ArrayList<>();
    String checkInUrl = Urls.getCheckInUrl();


    HashMap<String, Integer> categorylist = new HashMap<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Paper.init(this);


        setContentView(R.layout.s_agendadetails);


        appDetails = Paper.book().read("Appdetails");
        context = AgendaDetails.this;
        token = Paper.book(appDetails.getAppId()).read("token", "");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));
        setSupportActionBar(toolbar);
        Toolbar wvenue = (Toolbar) findViewById(R.id.wvenue);


        Bundle extras = getIntent().getExtras();
        if (extras == null)
            finish();

        // getting the action
        action = getIntent().getAction();

        // getting agenda details from fragment
        item = (Agendadetails) getIntent().getSerializableExtra("Agendaview"); //Obtaining data
        categorylist = (HashMap<String, Integer>) getIntent().getSerializableExtra("category_colorList");
        if (categorylist == null || categorylist.size() == 0)
            categorylist = Paper.book(appDetails.getAppId()).read("categoryList", new HashMap<String, Integer>());

        Agendadate = extras.getLong("Date");
        //getting location_details from Aganda details
        final LocationDetails locationDetails = item.getLocation_detail();
        //getting speakers details

        events = Paper.book().read("Appevents");
        e = events.get(0);

        for (int i = 0; i < e.getTabs().length; i++) {
            if (e.getTabs(i).getType().compareTo("speakersData") == 0) {
                pos = i;
                break;
            }

        }
        speakerslist = new ArrayList<>();
        for (int j = 0; j < e.getTabs(pos).getItemsSize(); j++) {
            speakerslist.add(e.getTabs(pos).getItems(j));
        }

        //intializing all view

        speakerdetails = (LinearLayout) findViewById(R.id.speakerdetails);
        pdfdetails = (LinearLayout) findViewById(R.id.pdfdetails);
        speakers = (LinearLayout) findViewById(R.id.speakers);
        etitle = (TextView) findViewById(R.id.etitle);
        date = (TextView) findViewById(R.id.date);
        txtnotes = (TextView) findViewById(R.id.txtnote);
        txtsch = (TextView) findViewById(R.id.txtsch);
        time = (TextView) findViewById(R.id.day);
        s1 = (TextView) findViewById(R.id.s1);
        p1 = (TextView) findViewById(R.id.p1);
        category = (TextView) findViewById(R.id.category);
        dots = (AwesomeText) findViewById(R.id.dots);
        venue = (TextView) findViewById(R.id.venue);
        rl_floor_locHead = (RelativeLayout) findViewById(R.id.rl_floor_locHead);
        txtrate = (TextView) findViewById(R.id.txtrate);
        txtpdf = (TextView) findViewById(R.id.txtpdf);
        filesize = (TextView) findViewById(R.id.filesize);
        loc = (RelativeLayout) findViewById(R.id.loc);
        floor_map = (RelativeLayout) findViewById(R.id.floor_map);
        // RL_floorlist = (RelativeLayout) findViewById(R.id.RL_floorlist);
        floorlistview = (ListView) findViewById(R.id.listView);
        addnotes = (RelativeLayout) findViewById(R.id.addnotes);
        ratingbar = (RelativeLayout) findViewById(R.id.ratingbar);
        ratinBar = (RatingBar) findViewById(R.id.ratingBar);
        addsch = (RelativeLayout) findViewById(R.id.addschedule);
        fullagenda = (RelativeLayout) findViewById(R.id.fullagenda);
        tv_des_head_ing = (TextView) findViewById(R.id.tv_des_head_ing);
        rdes=(LinearLayout)findViewById(R.id.rdes);
        des = (TextView) findViewById(R.id.des);
        inote = (AwesomeText) findViewById(R.id.inote);
        isch = (AwesomeText) findViewById(R.id.isch);
        // idownload = (TxtVCustomFonts) findViewById(R.id.idownload);
        ask_question_layout = (RelativeLayout) findViewById(R.id.ask_question_layout);
        ask_poll = (RelativeLayout) findViewById(R.id.ask_poll);
        ask_survey_layout = (RelativeLayout) findViewById(R.id.ask_survey_layout);
        live_stream = (RelativeLayout) findViewById(R.id.live_stream);
        recorded_video = (RelativeLayout) findViewById(R.id.recorded_video);
        joinsession=(Button) findViewById(R.id.joinsession);
        timertoshow = (LinearLayout) findViewById(R.id.timer1);
        timertoshow.setVisibility(View.GONE);
        txtdayshow = (TextView) findViewById(R.id.dayshow);
        txthrshow = (TextView) findViewById(R.id.hrshow);
        txtminshow = (TextView) findViewById(R.id.minshow);
        txtsecshow = (TextView) findViewById(R.id.secshow);
        live_broad=(RelativeLayout)findViewById(R.id.live_broad);

        limitseat = (RelativeLayout) findViewById(R.id.limitseat);
        limitofseat = (LinearLayout) findViewById(R.id.limitofseat);
        enroll = (TextView) findViewById(R.id.enroll);
        plus=(ImageView)findViewById(R.id.plus);
        limit = (Button) findViewById(R.id.limit);
        totalusers = (TextView) findViewById(R.id.totalusers);

        limit.setBackground(Util.setdrawable(AgendaDetails.this, R.drawable.healthpostbut, Color.parseColor(appDetails.getTheme_color())));
        //  txtdayshow.setBackgroundColor(R.drawable.rounded_bg_home);
        //txthrshow.setBackgroundColor(R.drawable.rounded_bg_home);
       // txtminshow.setBackgroundColor(R.drawable.rounded_bg_home);
       // txtsecshow.setBackgroundColor(R.drawable.rounded_bg_home);
        live_broad.setVisibility(View.GONE);
        // setting the fonts
        etitle.setTypeface(Util.regulartypeface(this));
        date.setTypeface(Util.regulartypeface(this));
        category.setTypeface(Util.regulartypeface(this));
        venue.setTypeface(Util.regulartypeface(this));
        txtnotes.setTypeface(Util.regulartypeface(this));
        txtsch.setTypeface(Util.regulartypeface(this));
        txtrate.setTypeface(Util.regulartypeface(this));
        time.setTypeface(Util.boldtypeface(this));
        s1.setTypeface(Util.boldtypeface(this));
        p1.setTypeface(Util.boldtypeface(this));
        txtpdf.setTypeface(Util.regulartypeface(this));
        filesize.setTypeface(Util.regulartypeface(this));
        //setting the texcolor

        txtnotes.setTextColor(Color.parseColor(appDetails.getTheme_color()));
        txtsch.setTextColor(Color.parseColor(appDetails.getTheme_color()));
        //inote.setTextColor(Color.parseColor(appDetails.getTheme_color()));
        isch.setTextColor(Color.parseColor(appDetails.getTheme_color()));
        // idownload.setTextColor(Color.parseColor(appDetails.getTheme_color()));

        try {
            dots.setTextColor(categorylist.get(item.getCategory()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        //   idownload.setVisibility(View.INVISIBLE);

        addnotes.setOnClickListener(this);
        ask_survey_layout.setOnClickListener(this);
        live_stream.setOnClickListener(this);
        recorded_video.setOnClickListener(this);
        addsch.setOnClickListener(this);
        ratingbar.setOnClickListener(this);
        ask_question_layout.setOnClickListener(this);
        ask_poll.setOnClickListener(this);
        wvenue.setOnClickListener(this);
        joinsession.setOnClickListener(this);
        limitseat.setOnClickListener(this);
        limitofseat.setOnClickListener(this);
        plus.setOnClickListener(this);
        queue1 = Volley.newRequestQueue(this);


        //calculating the speaker image size

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        ImgWidth = (displayMetrics.widthPixels) * 0.20;
        Imgheight = (displayMetrics.widthPixels) * 0.20;

        ratinBar.setVisibility(View.INVISIBLE);
        showtimer(true);
        settingdata();
        startthread();
    }

    private void startthread() {
        runnable = new Runnable() {
            public void run() {

                getrating();


            }
        };
        Thread mythread = new Thread(runnable);
        mythread.start();

        /*getting floor if location_details has floor maps */
        // addFloorInMap();

    }

    /* setting Floor-list using custom view */
    private void addFloorInMap() {
        ArrayList<Mapfloorplan> floorplans = new ArrayList<>();

        if (item.getLocation_detail() != null)
            if (item.getLocation_detail().getFloorsSize() > 0) {
                rl_floor_locHead.setVisibility(View.GONE);
                loc.setVisibility(View.GONE);
                for (int j = 0; j < item.getLocation_detail().getFloorsSize(); j++) {


                    floorplans.add(item.getLocation_detail().getFloors(j));
                }

                floorlistview.setAdapter(new floorlistadapter(this, floorplans));
            } else {
                rl_floor_locHead.setVisibility(View.GONE);
                /* loc.setVisibility(View.VISIBLE);*/
            }
    }


    private Handler messageHandler = new Handler() {
        public void handleMessage(Message msg) {
            if ((int) msg.obj == 1) {
                Bundle bundle = msg.getData();
                String response = bundle.getString("response");

                try {
                    JSONObject jObj = new JSONObject(response);

                    if (jObj.getBoolean("response")) {
                        double ratingvalue = jObj.getDouble("responseString");
                        ratinBar.setRating((float) ratingvalue);
                        ratinBar.setVisibility(View.VISIBLE);

                    } else {
                        Error_Dialog.show(jObj.getString("responseString"), AgendaDetails.this);
                    }
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }


            }
            if ((int) msg.obj == 3) {
                Bundle bundle1 = msg.getData();
                String response = bundle1.getString("response");

                try {
                    JSONObject jObj = new JSONObject(response);

                    /*setting the rating*/
                    if (jObj.getBoolean("response")) {
                        JSONObject nlp = jObj.getJSONObject("data");

                        totalseat  = nlp.getInt("total_seat");
                        availseat  = nlp.getInt("available_seat");
                        status=nlp.get("status").toString();
                        seatavailable=totalseat-availseat;
                        if(availseat==0)
                        {

                            if(status.equalsIgnoreCase("not_registered")){
                                enroll.setText("Enroll to Add WaitList");
                                limit.setText("FULL");
                            }
                            else if(status.equalsIgnoreCase("waiting")){
                                enroll.setText("Withdraw From Waiting List");
                                limit.setText("FULL");
                            }
                            else{
                                enroll.setText("Withdraw My Enrollment");
                                limit.setText("FULL");
                            }

                            totalusers.setText(Html.fromHtml("("+String.valueOf(seatavailable)+"/"+String.valueOf(totalseat)+")"));
                        }
                        else if(status.equalsIgnoreCase("not_registered")){
                            enroll.setText("Enroll & Add to My Agenda");
                            plus.setImageResource(R.drawable.plus);
                            limit.setText("Limited Seating");
                            totalusers.setText(Html.fromHtml("("+String.valueOf(seatavailable)+"/"+String.valueOf(totalseat)+")"));
                        }
                        else if(status.equalsIgnoreCase("registered")){
                            enroll.setText("Withdraw My Enrollment");
                            plus.setImageResource(R.drawable.rightlogo);
                            limit.setText("Limited Seating");
                            totalusers.setText(Html.fromHtml("("+String.valueOf(seatavailable)+"/"+String.valueOf(totalseat)+")"));

                        }

                    } else {
                        Error_Dialog.show(jObj.getString("responseString"), AgendaDetails.this);
                    }
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }


            }
            else if ((int) msg.obj == 2) {
                Bundle bundle = msg.getData();
                String error = bundle.getString("error");
                Error_Dialog.show(error, AgendaDetails.this);
            }

        }
    };



    public void checkSeat(String userId,String appid,String agenda_id){

        String tag_string_req = "get_sessionReg_detail";
        String url = ApiList.Enroll+userId+"&appid="+appid+"&agenda_id="+agenda_id;

        StringRequest strReq = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {

                Message message = messageHandler.obtainMessage();
                message.obj = 3;

                Bundle bundle1 = new Bundle();
                bundle1.putString("response", response);

                message.setData(bundle1);


                messageHandler.sendMessage(message);

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                System.out.print("some issue");


            }
        }) {
            //  @Override
           /* protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userid", userId);
                params.put("appid", appid);
                //   params.put("appid", finalappid);
                params.put("agenda_id", agendaidd);

                System.out.println("Upload Contacts Param : " + params.toString());
                return params;

            }*/
        };

//        AppNew.getInstance().getRequestQueue().getCache().clear();
        App.getInstance().addToRequestQueue(strReq, tag_string_req);
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                100000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));



    }

    private void getrating() {

        String tag_string_req = "Login";
        String url = ApiList.GetRating + appDetails.getAppId() + "&type=agenda" + "&type_id=" + item.getAgendaId();
        StringRequest strReq = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                Message message = messageHandler.obtainMessage();
                message.obj = 1;

                Bundle bundle = new Bundle();
                bundle.putString("response", response);

                message.setData(bundle);


                messageHandler.sendMessage(message);


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Message message = messageHandler.obtainMessage();
                message.obj = 2;
                Bundle bundle = new Bundle();
                if (error instanceof TimeoutError) {
                    bundle.putString("error", "Timeout");
                    message.setData(bundle);
                    messageHandler.sendMessage(message);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    bundle.putString("error", VolleyErrorLis.handleServerError(error, context));
                    message.setData(bundle);
                    messageHandler.sendMessage(message);
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    bundle.putString("error", "Please Check Your Internet Connection");
                    message.setData(bundle);
                    messageHandler.sendMessage(message);
                }

            }
        });


        strReq.setShouldCache(false);
        App.getInstance().addToRequestQueue(strReq, tag_string_req);
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
    }

    private void settingdata() {



        // Dynamic code for Agenda Rating by NP
        String s[]=new String[20];
        s=appDetails.getDisable_items();

        if(s.length>0&& s!=null){
            for(int i=0; i<s.length; i++)
            {
                if(s[i].equalsIgnoreCase("agendaRating")){
                    ratingbar.setVisibility(View.GONE);}
                else if(s[i].equalsIgnoreCase("askaquestion")){
                    ask_question_layout.setVisibility(View.GONE);}
                else if(s[i].equalsIgnoreCase("agendaasksurvey")){
                    ask_survey_layout.setVisibility(View.GONE);
                }
                else if(s[i].equalsIgnoreCase("agendapoll")){
                    ask_poll.setVisibility(View.GONE);
                }
                else if(s[i].equalsIgnoreCase("addagendaschedule")){
                    addsch.setVisibility(View.GONE);
                }
                else if (s[i].equalsIgnoreCase("agora")) {

                    live_stream.setVisibility(View.GONE);
                }
                else if (s[i].equalsIgnoreCase("sessionstream")) {
                    live_stream.setVisibility(View.GONE);
                }else if(s[i].equalsIgnoreCase("sessionsurvey")){
                    ask_survey_layout.setVisibility(View.GONE);
                }
                else if(!item.getStream_link().equalsIgnoreCase(""))
                {
                    String st=item.getStream_link();
                    b=st.contains("zoom");
                    if(b) {
                        recorded_video.setVisibility(View.GONE);
                    }
                    else{
                        recorded_video.setVisibility(View.VISIBLE);
                    }

                }






            }
        }

        // setting for session

        if(item.getAgenda_registration()==0){
            limitseat.setVisibility(View.GONE);
            limitofseat.setVisibility(View.GONE);

        } else
        {
            limitseat.setVisibility(View.VISIBLE);
            limitofseat.setVisibility(View.VISIBLE);
            addsch.setVisibility(View.GONE);
        }
        appid=appDetails.getAppId();
        userid=Paper.book().read("userId", "");
        agendaid=item.getAgendaId();
        agendaidd= Integer.toString(agendaid);
        checkSeat(userid,appid,agendaidd);


        //setting title
        etitle.setText(item.getTopic());

        //setting date
        date.setText(converlontostring(Agendadate, true));

        //setting fromtime and totime

        time.setText(converlontostring(item.getFromtime(), false) + " - " + converlontostring(item.getTotime(), false));

        // setting catgory

        String newCatStr = item.getCategory().substring(0, 1).toUpperCase() + item.getCategory().substring(1);
        category.setText(newCatStr);

        //setting location

        if (!item.getLocation().equalsIgnoreCase("") &&!item.getMap_checkvalue().equalsIgnoreCase("")&& !item.getFloor_id().equalsIgnoreCase("")) {
            venue.setText(item.getLocation());
            loc.setVisibility(View.VISIBLE);
            venue.setOnClickListener(this);
        } else if(!item.getLocation().equalsIgnoreCase("")&&!item.getFloor_id().equalsIgnoreCase("")) {
            venue.setText(item.getLocation());
            loc.setVisibility(View.VISIBLE);
            venue.setOnClickListener(this);
        }
        else if(item.getLocation_detail()!=null && !item.getMap_checkvalue().equalsIgnoreCase(""))
        {
            try {
                venue.setText(item.getLocation());
                loc.setVisibility(View.VISIBLE);
                String ch = item.getMap_checkvalue();


                for (int i = 0; i < e.getTabs().length; i++) {
                    if (e.getTabs(i).getType().contains("map") && e.getTabs(i).getCheckvalue().equalsIgnoreCase(ch)) {
                        mappos = i;
                        maptitle = e.getTabs(i).getTitle();
                        break;

                    }
                }
                venue.setOnClickListener(this);
            }catch (NullPointerException e)
            {

            }



        }
        else
        {
         loc.setVisibility(View.GONE);

        }

        //setting description

        if (!item.getDescription().equalsIgnoreCase("")) {

            des.setText(Html.fromHtml(item.getDescription()));
            tv_des_head_ing.setVisibility(View.VISIBLE);
            des.setVisibility(View.VISIBLE);
            rdes.setVisibility(View.VISIBLE);

        } else {

            tv_des_head_ing.setVisibility(View.GONE);
            des.setVisibility(View.GONE);
            rdes.setVisibility(View.GONE);

        }


        // fetching the speakers attending this session

        int[] speakerid = item.getSpeakerId();

        speakername = new ArrayList<>();
        for (int k = 0; k < speakerid.length; k++) {
            for (int m = 0; m < speakerslist.size(); m++) {
                if (speakerid[k] == speakerslist.get(m).getSpeakerId()) {
                    speakername.add(new agendaspeakerlist(speakerslist.get(m).getSpeakerId(), speakerslist.get(m).getName(), speakerslist.get(m).getDescription(), speakerslist.get(m).getImage(), speakerslist.get(m).getDetails(), speakerslist.get(m).getFacebook(), speakerslist.get(m).getLinkedin(), speakerslist.get(m).getAgendaId(),speakerslist.get(m).getSpeaker_document_url()
                    ,speakerslist.get(m).getSpeaker_document_hide(),speakerslist.get(m).getSpeaker_document_name()));
                    break;
                }
            }

        }

        if (speakername.size() > 0) {
            speakerdetails.setVisibility(View.VISIBLE);
            Addthespeaker();
        } else
            speakerdetails.setVisibility(View.GONE);


        //setting the pdf if its present

        if (item.getAttachment_url().equalsIgnoreCase(""))
            pdfdetails.setVisibility(View.GONE);
        else {
            pdfdetails.setVisibility(View.VISIBLE);
            txtpdf.setText(item.getAttachment_name());
            pdfdetails.setOnClickListener(this);
        }

    }



    private void floorMap(String appid,String type,String type_id){



        // converting arraylist to jsonarray

       final String finaltype=type;
       final String finalappid=appid;
       final String finaltypeid=type_id;
/*
        Gson gson = new GsonBuilder().create();
        final JsonArray myCustomArray = gson.toJsonTree(list).getAsJsonArray();*/


        final ProgressDialog dialog = new ProgressDialog(AgendaDetails.this,R.style.MyAlertDialogStyle);
        //dialog.setMessage(msg);
        dialog.show();
        String tag_string_req = "FloorMap";
        String url = ApiList.Floor_map;
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                try {

                    JSONObject jObj = new JSONObject(response);


                    if (jObj.getBoolean("response")) {
                        Error_Dialog.show(jObj.getString("responseString"), AgendaDetails.this);
                        floordetail=jObj.getString("html_file").toString();
                        fvenue.loadUrl(String.format("data:text/html;charset=utf-8,<html><body style=\"text-align:justify;font-size:12px;\"> %s </body></Html>", Uri.encode(floordetail)));

                    } else {
                        if (jObj.getString("responseString").equalsIgnoreCase("Invalid token")) {
                            Intent i = new Intent();
                            i.putExtra("keyMessage", "Session Expired, Please Login ");
                            i.putExtra("keyAlert", "Session Expired");
                            i.setClassName(getPackageName(), getPackageName()+".SessionExpired");
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);

                        } else
                            Error_Dialog.show(jObj.getString("responseString"), AgendaDetails.this);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Paper.book().write("Sync", true);
                if (error instanceof TimeoutError) {
                    Error_Dialog.show("Timeout", AgendaDetails.this);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, context), AgendaDetails.this);
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show("Please Check Your Internet Connection", AgendaDetails.this);
                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("appid",finalappid);
                params.put("type", finaltype);
                params.put("type_id", finaltypeid);


                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("token", token );
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


    private void Addthespeaker() {
        speakers.removeAllViews();
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for (int i = 0; i < speakername.size(); i++) {
            final agendaspeakerlist item = speakername.get(i);
            View child = inflater.inflate(R.layout.s_agendaspeakerview, null);
            LinearLayout speakerimage = (LinearLayout) child.findViewById(R.id.speakerview);
            final RoundedImageView simage = (RoundedImageView) child.findViewById(R.id.speakerimage);
            RelativeLayout.LayoutParams sparams = (RelativeLayout.LayoutParams) speakerimage.getLayoutParams();
          //  sparams.width = (int) ImgWidth;
          //  sparams.height = (int) Imgheight;
            //speakerimage.setLayoutParams(sparams);

            Glide.with(getApplicationContext())

                    .load((item.getImage().equalsIgnoreCase("")) ? R.drawable.default_user : item.getImage())
                    .asBitmap()
                    .placeholder(R.drawable.default_user)
                    .error(R.drawable.default_user)
                    .into(simage);
            simage.setCornerRadius(12,12,12,12);

                            /*new BitmapImageViewTarget(simage) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            simage.setImageBitmap(scaleBitmap(resource, (int) ImgWidth, (int) Imgheight));
                        }
                    });*/

            TextView sname = (TextView) child.findViewById(R.id.title);
            TextView sdes = (TextView) child.findViewById(R.id.des);
            sname.setTypeface(Util.regulartypeface(this));
            sdes.setTypeface(Util.regulartypeface(this));

            sname.setText(item.getName());
            sdes.setText(Html.fromHtml(item.getProf()));

            if (!action.equalsIgnoreCase("com.speakeragenda"))

                child.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Bundle args = new Bundle();
                        args.putSerializable("SpeakerDetails", item);
                        Intent i = new Intent(AgendaDetails.this, SpeakerDetails.class);
                        i.setAction("com.agendaspeaker");
                        i.putExtras(args);
                        startActivity(i);
                    }
                });

            speakers.addView(child);

        }

    }

    public static Bitmap scaleBitmap(Bitmap bitmap, int newWidth, int newHeight) {
        Bitmap scaledBitmap = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888);

        float scaleX = newWidth / (float) bitmap.getWidth();
        float scaleY = newHeight / (float) bitmap.getHeight();
        float pivotX = 0;
        float pivotY = 0;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(scaleX, scaleY, pivotX, pivotY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bitmap, 0, 0, new Paint(Paint.FILTER_BITMAP_FLAG));

        return scaledBitmap;
    }


    private String converlontostring(Long key, boolean flag) {

        if (flag) {
            myFormat = new SimpleDateFormat("E, MMM dd, yyyy");
            myFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            myFormat.format(key);
            return myFormat.format(key);
        } else {

            myFormat = new SimpleDateFormat("h:mm a");
            myFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            myFormat.format(key);
            return myFormat.format(key);

        }

    }

    @Override
    protected void onResume() {
        super.onResume();


        SpannableString s = new SpannableString("Details");
        setTitle(Util.applyFontToMenuItem(this, s));

        settingsch();


    }

    private void settingsch() {

try {
    list = Paper.book(appDetails.getAppId()).read("SCH", new ArrayList<Integer>() {
    });
    if (list.contains(item.getAgendaId())) {
        txtsch.setText("REMOVE FROM SCHEDULE");
        isch.setPixedenStrokeIcon(FontCharacterMaps.Pixeden.PE_CHECK);
    } else {
        txtsch.setText("ADD TO MY SCHEDULE");
        isch.setPixedenStrokeIcon(FontCharacterMaps.Pixeden.PE_PLUS);
    }
}catch (Exception e){

}
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
    public void onClick(View view) {
        int i1 = view.getId();
        if (i1 == R.id.pdfdetails) {
            Uri path = Uri.parse(item.getAttachment_url());
            Intent pdfIntent = new Intent(Intent.ACTION_VIEW, path);
            pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            try {
                startActivity(pdfIntent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(AgendaDetails.this, "No Application available to view PDF",
                        Toast.LENGTH_SHORT).show();
            }

        }
        else if (i1 == R.id.addnotes) {
            if (Paper.book().read("Islogin", false))
                shownotes();
            else {
                Error_Dialog.show("Please Login", AgendaDetails.this);
                Intent i = new Intent(this, LoginActivity.class);
                startActivityForResult(i, 1);
            }
        }

        else if (i1 == R.id.joinsession) {
          //  callApiForChannel();
            /*Intent intent = null;
            try {
                Bundle args = new Bundle();
                args.putInt("agendaId", item.getAgendaId());
                args.putString("token", token);
                args.putSerializable("AgendaList",item);
                Intent i = new Intent(this, AgoraClass.class);
                i.putExtras(args);
                startActivity(i);
            } catch (Exception e) {
                e.printStackTrace();
            }*/

        } else if (i1 == R.id.addschedule) {
            if (Paper.book().read("Islogin", false)) {
                list = Paper.book(appDetails.getAppId()).read("SCH", new ArrayList<Integer>());
                if (list.contains(item.getAgendaId())) {
                    list.remove(new Integer(item.getAgendaId()));
                    addtosch("Removing from Schedules", "unmark");
                } else {
                    list.add(item.getAgendaId());
                    addtosch("Adding to Schedules", "mark");
                }
                Paper.book(appDetails.getAppId()).write("SCH", list);
                settingsch();

            } else {
                Error_Dialog.show("Please Login", AgendaDetails.this);
                Intent i = new Intent(this, LoginActivity.class);
                startActivityForResult(i, 2);
            }


        } else if (i1 == R.id.ratingbar) {
            if (Paper.book().read("Islogin", false))
                showratingbar();
            else {
                Error_Dialog.show("Please Login", AgendaDetails.this);
                Intent i = new Intent(this, LoginActivity.class);
                startActivityForResult(i, 3);
            }

        } else if (i1 == R.id.ask_question_layout) {
            if (Paper.book().read("Islogin", false)) {
                Bundle args = new Bundle();
                args.putSerializable("Agendaview", item);

                Intent intent = new Intent(this, AskAQuestionActivity.class);
                intent.setAction("com.agendadetails");
                intent.putExtras(args);
                startActivity(intent);
            } else {
                Error_Dialog.show("Please Login", AgendaDetails.this);
            }

        }
        else if (i1 == R.id.live_stream) {
            if (Paper.book().read("Islogin", false)) {
                Intent i = null;
                callApiForChannel(0);
            /*    try {



                    Bundle args = new Bundle();
                    args.putInt("agendaId", item.getAgendaId());
                    args.putString("token", null);
                    args.putSerializable("AgendaList",item);
                     i = new Intent(this, AgoraClass.class);
                    i.putExtras(args);
                    System.out.println("A Details"+args);
                    startActivity(i);

                } catch (Exception e) {
                    e.printStackTrace();
                }*/
            } else {
                Error_Dialog.show("Please Login", AgendaDetails.this);
            }

        }
        else if (i1 == R.id.recorded_video) {
            if (Paper.book().read("Islogin", false)) {
                callApiForChannel(1);
                /*Bundle args = new Bundle();

                args.putInt("agendaId",item.getAgendaId());
                args.putString("token",null);
                args.putString("stream_url",item.getStream_link());
                args.putString("title",item.getTopic());
                args.putSerializable("AgendaList",item);
                Intent intent = new Intent(AgendaDetails.this, Streamlink.class);
                intent.putExtras(args);
                startActivity(intent);*/
            } else {
                Error_Dialog.show("Please Login", AgendaDetails.this);
            }

        }

        else if (i1 == R.id.ask_poll) {
            if (Paper.book().read("Islogin", false)) {
                checkAgenda();
            } else {
                Error_Dialog.show("Please Login", AgendaDetails.this);
            }

        }        else if (i1 == R.id.ask_survey_layout) {
            if (Paper.book().read("Islogin", false)) {


                checkSurveyIsAlreadyDone();
            } else {
                Error_Dialog.show("Please Login", AgendaDetails.this);
            }

        }

        else if(i1==R.id.wvenue){
            onBackPressed();
        }

        else if (i1 == R.id.venue) {
          if(!item.getFloor_id().equalsIgnoreCase("")) {
              try {
                  fvenue = (WebView) findViewById(R.id.s_mapvenue);
                  //  fvenue.setWebChromeClient(new WebChromeClient());
                  fvenue.getSettings().setJavaScriptEnabled(true);
                  fvenue.getSettings().setLoadWithOverviewMode(true);
                  fvenue.getSettings().setUseWideViewPort(true);
                  fvenue.getSettings().setBuiltInZoomControls(true);

                  fvenue.getSettings().setPluginState(WebSettings.PluginState.ON);
                  agendaid = item.getAgendaId();
                  String agendaidd = Integer.toString(agendaid);

                  floorMap(appDetails.getAppId(), "agenda", agendaidd);

                  /*loading html content in webview*/
                  //  fvenue.loadUrl("https://webmobi.s3.amazonaws.com/nativeapps/personaltest/FloorMap0.png");

                  fvenue.setInitialScale(1);
                  fvenue.getSettings().setLoadWithOverviewMode(true);
                  fvenue.getSettings().setUseWideViewPort(true);
                  fvenue.setWebViewClient(new WebViewClient());


                  floor_map.setVisibility(View.VISIBLE);
                  fullagenda.setVisibility(View.INVISIBLE);
              } catch (Exception e) {
                  e.printStackTrace();
              }


          }else{
              Intent intent = new Intent(this, MapRoot.class);
              intent.putExtra("pos",mappos);
              intent.putExtra("title", maptitle);
              startActivity(intent);
          }



           /* int mapPosition = 0;
            for (int i = 0; i < e.getTabs().length; i++) {
                if (e.getTabs(i).getType().compareTo("map") == 0) {
                    mapPosition = i;
                    break;
                }
*/

          /*  if (e.getTabs(mapPosition).getType().compareTo("map") == 0) {
                Intent intent = new Intent(this, MapRoot.class);
                intent.putExtra("pos", mapPosition);
                intent.putExtra("title", e.getTabs(mapPosition).getTitle());
                startActivity(intent);
            }*/
        }

        else if(i1==R.id.limitseat) {


                if (status.equalsIgnoreCase("waiting")) {
                    sendenroll("remove", userid, appid, agendaidd);
                    plus.setImageResource(R.drawable.plus);
                    limit.setText("FULL");
                    enroll.setText("Enroll to Add WaitList");
                    status = "not_registered";

                }
            else if (status.equalsIgnoreCase("not_registered")) {
                    sendenroll("add", userid, appid, agendaidd);
                    plus.setImageResource(R.drawable.rightlogo);
                    if (availseat == 0) {
                        limit.setText("FULL");
                        enroll.setText("Withdraw from Waiting List");
                        status = "waiting";
                    } else {
                        limit.setText("Limited Seating");
                        enroll.setText("Withdraw My Enrollment");
                        status = "registered";
                        totalusers.setText(Html.fromHtml("(" + String.valueOf((totalseat - availseat) + 1)) + "/" + String.valueOf(totalseat) + ")");
                        availseat--;
                    }

                    //  list = Paper.book().read("SCH", new ArrayList<Integer>());


                    //  int  seat = seatavailable +1;


                }
            else {
                    sendenroll("remove", userid, appid, agendaidd);
                    enroll.setText("Enroll and  Add to my Agenda");
                    plus.setImageResource(R.drawable.plus);
                    if (totalseat - availseat == 0) {
                        limit.setText("FULL");
                    } else {
                        limit.setText("Limited Seating");
                    }
                    // list = Paper.book().read("SCH", new ArrayList<Integer>());

                    status = "not_registered";

                    //  int seat = seatavailable -1;
                    totalusers.setText(Html.fromHtml("(" + String.valueOf((totalseat - availseat) - 1) + "/" + String.valueOf(totalseat) + ")"));
                    availseat++;


                }


        }


    }


    private void callApiForChannel(int from) {

        final ProgressDialog dialog = new ProgressDialog(AgendaDetails.this, R.style.MyAlertDialogStyle);
        dialog.setMessage("Loading Details ...");
        dialog.show();


        String tag_string_req = "Login";
        String url = ApiList.ChannelBroadCast;
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {

                try {
                    //   System.out.println(response);
                    JSONObject jObj = new JSONObject(response);
                    System.out.print("This is API response "+response);


                    if (jObj.getBoolean("response")) {
                        dialog.dismiss();
                        c_name = jObj.getString("channelName");
                        role = jObj.getString("session_admin_flag");
                        group_id=jObj.getString("group_id");
                        group_name=jObj.getString("group_name");
                        Paper.book().write("GROUP_ID",group_id);
                        Paper.book().write("Group_Name",group_name);

                        addchatID(from);
                        if(role.equalsIgnoreCase("speaker")){
                            isAdmin=true;}
                        else{
                            isAdmin=false;
                        }

                    } else {
                        dialog.dismiss();
                        Error_Dialog.show(jObj.getString("responseString"), AgendaDetails.this);
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
                    Error_Dialog.show("Timeout", AgendaDetails.this);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, AgendaDetails.this), AgendaDetails.this);
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show("Please Check Your Internet Connection", AgendaDetails.this);
                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("appid", appDetails.getAppId());
                params.put("userid", Paper.book().read("userId",""));
                params.put("agenda_id",String.valueOf(item.getAgendaId()));
                System.out.println(params);
                return params;
            }
        };
        App.getInstance().addToRequestQueue(strReq, tag_string_req);
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
    }
    public void addchatID(int from) {

        // converting arraylist to jsonarray
        final ProgressDialog dialog = new ProgressDialog(AgendaDetails.this,R.style.MyAlertDialogStyle);
        dialog.setMessage("Adding User");
        dialog.show();
        String tag_string_req = "Schdule";
        String url ="https://chat.webmobi.com/add_user";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                System.out.println("Chat Response"+response);
                try {

                    JSONObject jObj = new JSONObject(response);


                    if (jObj.getBoolean("response")) {
                        try {
                            sendCheckinData(from);
                           /* Intent intent = new Intent(AgoraClass.this, MainActivity.class);
                            intent.putExtra("Channel_name",c_name);
                            intent.putExtra("Role",role);
                            intent.putExtra("Group_id",group_id);
                            intent.putExtra("Agenda_id",agendaID);
                            startActivity(intent);
                            finish();*/


                        }catch (Exception e)
                        {}

                    } else {
                        if (jObj.getString("responseString").equalsIgnoreCase("Invalid token")) {
                            Intent i = new Intent();
                            i.putExtra("keyMessage", "Session Expired, Please Login ");
                            i.putExtra("keyAlert", "Session Expired");
                            i.setClassName(AgendaDetails.this.getPackageName(), AgendaDetails.this.getPackageName()+".SessionExpired");
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            AgendaDetails.this.startActivity(i);

                        } else
                            Error_Dialog.show(jObj.getString("responseString"), AgendaDetails.this);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Paper.book().write("Sync", true);
                if (error instanceof TimeoutError) {
                    Error_Dialog.show("Timeout", AgendaDetails.this);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, AgendaDetails.this), AgendaDetails.this);
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show("Please Check Your Internet Connection", AgendaDetails.this);
                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                String uid= Paper.book().read("userId","");
                String [] newuid=uid.split(" ");
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id[0]",Paper.book().read("userId",""));
                params.put("group_id", group_id);
                params.put("joined_date", String.valueOf(System.currentTimeMillis()));

                if(isAdmin)
                {                params.put("is_admin", "true");}
                else{
                    params.put("is_admin","false");
                }


                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
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
    private void sendCheckinData(int from) {
        String reqTag="";
        if(from==0){
         reqTag = CHECK_IN_REQ_TAG;
        }
        else{
             reqTag = CHECK_IN_REQ_TAG1;
        }
        JSONObject jsonObject = new JSONObject();
        try{
            agendaid1.add(String.valueOf(item.getAgendaId()));
            appid=appDetails.getAppId();
            userid1.add((Paper.book().read("userId","")));
            email1.add(Paper.book().read("usermail"));
            // email.add(user_details.getEmail());
            checkdate1.add(String.valueOf(System.currentTimeMillis()));

        }catch (Exception e){

        }
        try {
            jsonObject.put("appId", appDetails.getAppId());
            jsonObject.put("userIds", new JSONArray(Arrays.asList(userid1.toArray())));
            jsonObject.put("checkin_date", new JSONArray(Arrays.asList(checkdate1.toArray())));
            jsonObject.put("email_id", new JSONArray(Arrays.asList(email1.toArray())));
            jsonObject.put("agenda_id", new JSONArray(Arrays.asList(agendaid1.toArray())));



            JsonObjectRequest jsonObjReq = VolleyUtils.createJsonPostReq(reqTag, checkInUrl, jsonObject,
                    AgendaDetails.this);
            // Adding request to request queue
            jsonObjReq.setShouldCache(false);
            queue1.add(jsonObjReq);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void onVolleyResponse(String tag, JSONObject response) throws JSONException {
        if (tag.equals(CHECK_IN_REQ_TAG)) {
            String responseString = response.getString("responseString");
            System.out.println("Checkin response"+responseString);
            if (response.getString("response").equals("success")) {
                Boolean selfsuccess=true;

                Bundle args = new Bundle();
                args.putString("Channel_name",c_name);
                args.putString("Role",role);
                args.putString("Group_id",group_id);
                args.putString("Agenda_id", String.valueOf(item.getAgendaId()));
                args.putSerializable("AgendaList",item);
                Intent i = new Intent(AgendaDetails.this, MainActivity.class);
                i.putExtras(args);
                //i.setAction("com.agenda");
                startActivity(i);
                finish();
                ////////////////////////////////////////////




            } else if (response.getString("response").equals("error")) {
                if (response.getString("responseString").equals("Insufficient data.")) {

                } else {
                    Toast.makeText(this, "Backup was not successful", Toast.LENGTH_SHORT).show();

                }
            }
        }
        if(tag.equals(CHECK_IN_REQ_TAG1)){
            String responseString = response.getString("responseString");
            System.out.println("Checkin response"+responseString);
            if (response.getString("response").equals("success")) {
                Boolean selfsuccess = true;
                Bundle args = new Bundle();
                args.putInt("agendaId", item.getAgendaId());
                args.putString("token", token);
                args.putString("url", item.getStream_link());
                args.putString("title", item.getTopic());
                args.putString("Group_id", group_id);
                args.putSerializable("AgendaList", item);
                Intent intent = new Intent(AgendaDetails.this, JoinMeeting.class);
                intent.putExtras(args);
                startActivity(intent);
                finish();
            } else if (response.getString("response").equals("error")) {
                if (response.getString("responseString").equals("Insufficient data.")) {

                } else {
                    Toast.makeText(this, "Backup was not successful", Toast.LENGTH_SHORT).show();

                }
            }
        }
    }

    @Override
    public void onVolleyError(String tag, VolleyError error) {
        /*if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
        boolean_print_online = false;
        getUserDetails(usersUrl, true);
        if (file_write_permission) {
            if (isPrintEnabled && agenda_id == 0)
                try {
                    generatePDF(boolean_print_online);
                } catch (DocumentException e1) {
                    e1.printStackTrace();
                }
        } else
            fileWritePermission(true);
        VolleyLog.d(TAG, "Error: " + error);*/
        if ("com.android.volley.TimeoutError".equals(error.toString()))
            Toast.makeText(this, "Timeout Error", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, "You are Offline", Toast.LENGTH_SHORT).show();
    }

    private void sendenroll(String flag,String userid,String appid,String agenda_id){


        // converting arraylist to jsonarray
        String finalflag=flag;
        String finaluserid=userid;
        String finalappid=appid;
        String finalagenda_id=agenda_id;

        Gson gson = new GsonBuilder().create();
        final JsonArray myCustomArray = gson.toJsonTree(list).getAsJsonArray();


        final ProgressDialog dialog = new ProgressDialog(AgendaDetails.this,R.style.MyAlertDialogStyle);
        //dialog.setMessage(msg);
        dialog.show();
        String tag_string_req = "takeenroll";
        String url = ApiList.Take_Enroll;
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                try {

                    JSONObject jObj = new JSONObject(response);


                    if (jObj.getBoolean("response")) {
                        Error_Dialog.show(jObj.getString("responseString"), AgendaDetails.this);
                    } else {
                        if (jObj.getString("responseString").equalsIgnoreCase("Invalid token")) {
                            Intent i = new Intent();
                            i.putExtra("keyMessage", "Session Expired, Please Login ");
                            i.putExtra("keyAlert", "Session Expired");
                            i.setClassName(getPackageName(), getPackageName()+".SessionExpired");
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);

                        } else
                            Error_Dialog.show(jObj.getString("responseString"), AgendaDetails.this);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Paper.book().write("Sync", true);
                if (error instanceof TimeoutError) {
                    Error_Dialog.show("Timeout", AgendaDetails.this);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, context), AgendaDetails.this);
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show("Please Check Your Internet Connection", AgendaDetails.this);
                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("flag",flag);
                params.put("userid", finaluserid);
                //params.put("action", mark);
                params.put("appid", finalappid);
                params.put("agenda_id", finalagenda_id);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("token", token );
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



public void joinmeet(View view)
{
    Intent i=new Intent(AgendaDetails.this,JoinMeeting.class);
    startActivity(i);
}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {

            shownotes();
        } else if (requestCode == 2 && resultCode == RESULT_OK) {

            list = Paper.book(appDetails.getAppId()).read("SCH", new ArrayList<Integer>());
            if (list.contains(item.getAgendaId())) {
                list.remove(new Integer(item.getAgendaId()));
                addtosch("Removing from Schedules", "unmark");
            } else {
                list.add(item.getAgendaId());
                addtosch("Adding to Schedules", "mark");
            }
            Paper.book(appDetails.getAppId()).write("SCH", list);
            settingsch();


        } else if (requestCode == 3 && resultCode == RESULT_OK) {

            showratingbar();

        }

    }

    private void showratingbar() {

        Ratinglist = Paper.book(appDetails.getAppId()).read("AgendaRating", new HashMap<Integer, Rating>());

        final Dialog mBottomSheetDialog = new Dialog(AgendaDetails.this, R.style.MaterialDialogSheet);

        mBottomSheetDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mBottomSheetDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
        mBottomSheetDialog.setContentView(R.layout.s_rating_dialog);
        mBottomSheetDialog.setCancelable(true);
        mBottomSheetDialog.show();

        final RatingBar ratingbar = (RatingBar) mBottomSheetDialog.findViewById(R.id.ratingbar);
        TextView heading = (TextView) mBottomSheetDialog.findViewById(R.id.h1);
        final TextView warning = (TextView) mBottomSheetDialog.findViewById(R.id.h2);
        warning.setVisibility(View.GONE);
        TextView rating = (TextView) mBottomSheetDialog.findViewById(R.id.rating);
        TextView cancel = (TextView) mBottomSheetDialog.findViewById(R.id.cancel);

        heading.setTypeface(Util.boldtypeface(context));
        rating.setTypeface(Util.regulartypeface(context));
        warning.setTypeface(Util.lighttypeface(context));
        cancel.setTypeface(Util.regulartypeface(context));
        rating.setTextColor(Color.parseColor(appDetails.getTheme_color()));
        cancel.setTextColor(Color.parseColor(appDetails.getTheme_color()));
        warning.setTextColor(getResources().getColor(android.R.color.holo_red_dark));

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBottomSheetDialog.dismiss();
            }
        });

        // set the previous rating if it present

        if (Ratinglist.containsKey(item.getAgendaId())) {
            Rating r = Ratinglist.get(item.getAgendaId());
            ratingbar.setRating((float) r.getRating());
        }

        ratingbar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                if (b && v > 0) {
                    warning.setVisibility(View.GONE);
                } else if (b && v <= 0)
                    warning.setVisibility(View.VISIBLE);

            }
        });


        rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ratingbar.getRating() > 0) {
                    mBottomSheetDialog.dismiss();

                    sendRating(ratingbar.getRating(), Ratinglist.containsKey(item.getAgendaId()) ? "update" : "create");
                    Ratinglist.put(item.getAgendaId(), new Rating(ratingbar.getRating(), item.getAgendaId()));
                    Paper.book(appDetails.getAppId()).write("AgendaRating", Ratinglist);
                } else
                    warning.setVisibility(View.VISIBLE);

            }
        });


    }


    private void shownotes() {

        Noteslist = Paper.book(appDetails.getAppId()).read("AgendaNote", new HashMap<Integer, Notes>());


        final Dialog mBottomSheetDialog = new Dialog(AgendaDetails.this, R.style.MaterialDialogSheet);

        mBottomSheetDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mBottomSheetDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
        mBottomSheetDialog.setContentView(R.layout.s_note);
        mBottomSheetDialog.setCancelable(true);
        mBottomSheetDialog.show();

        final LinearLayout cancel = (LinearLayout) mBottomSheetDialog.findViewById(R.id.cancel);
        LinearLayout save = (LinearLayout) mBottomSheetDialog.findViewById(R.id.save);
        TextView canceltxt = (TextView) mBottomSheetDialog.findViewById(R.id.canceltxt);
        TextView savetxt = (TextView) mBottomSheetDialog.findViewById(R.id.savetxt);

        canceltxt.setTypeface(Util.regulartypeface(context));
        savetxt.setTypeface(Util.regulartypeface(context));
        // canceltxt.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));
        //savetxt.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));
        canceltxt.setBackground(Util.setdrawable(context, R.drawable.act_button_border, Color.parseColor(appDetails.getTheme_color())));
        savetxt.setBackground(Util.setdrawable(context, R.drawable.act_button_border, Color.parseColor(appDetails.getTheme_color())));

        final EditText notes = (EditText) mBottomSheetDialog.findViewById(R.id.notes);
        notes.setTypeface(Util.regulartypeface(context));

        if (Noteslist.containsKey(item.getAgendaId())) {
            Notes n = Noteslist.get(item.getAgendaId());
            notes.setText(n.getNotes());
        }


        canceltxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBottomSheetDialog.dismiss();
            }
        });

        savetxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!notes.getText().toString().isEmpty()) {
                    mBottomSheetDialog.dismiss();
                    Addnotes(notes.getText().toString(), Noteslist.containsKey(item.getAgendaId()) ? "update" : "create");
                    Noteslist.put(item.getAgendaId(), new Notes(item.getAgendaId(), notes.getText().toString(), "agenda", item.getTopic(), String.valueOf(System.currentTimeMillis())));
                    Paper.book(appDetails.getAppId()).write("AgendaNote", Noteslist);
                }


            }
        });


    }

    private void Addnotes(final String notes, final String action) {

        final ProgressDialog dialog = new ProgressDialog(AgendaDetails.this, R.style.MyAlertDialogStyle);
        dialog.setMessage("Adding Note");
        dialog.show();
        String tag_string_req = "Note";
        String url = ApiList.Notes;
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                try {

                    JSONObject jObj = new JSONObject(response);


                    if (jObj.getBoolean("response")) {
                        Error_Dialog.show("Saved", AgendaDetails.this);
                    } else {
                        Error_Dialog.show(jObj.getString("responseString"), AgendaDetails.this);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Paper.book("Sync").write(appDetails.getAppId(), true);
                if (error instanceof TimeoutError) {
                    Error_Dialog.show("Timeout", AgendaDetails.this);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, context), AgendaDetails.this);
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show("Please Check Your Internet Connection", AgendaDetails.this);
                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userid", Paper.book().read("userId", ""));
                params.put("action", action);
                params.put("type", "agenda");
                params.put("notes", notes);
                params.put("type_id", String.valueOf(item.getAgendaId()));
                params.put("appid", appDetails.getAppId());
                params.put("type_name", item.getTopic());
                params.put("last_updated", String.valueOf(System.currentTimeMillis()));
                System.out.println(params);
                return params;
            }

        };


        App.getInstance().addToRequestQueue(strReq, tag_string_req);
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

    }


    private void addtosch(String msg, final String mark) {

        // converting arraylist to jsonarray

        Gson gson = new GsonBuilder().create();
        final JsonArray myCustomArray = gson.toJsonTree(list).getAsJsonArray();


        final ProgressDialog dialog = new ProgressDialog(AgendaDetails.this, R.style.MyAlertDialogStyle);
        dialog.setMessage(msg);
        dialog.show();
        String tag_string_req = "Schdule";
        String url = ApiList.Schdule;
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                try {

                    JSONObject jObj = new JSONObject(response);


                    if (jObj.getBoolean("response")) {
                        Error_Dialog.show(jObj.getString("responseString"), AgendaDetails.this);
                    } else {
                        if (jObj.getString("responseString").equalsIgnoreCase("Invalid token")) {
                            Intent i = new Intent();
                            i.putExtra("keyMessage", "Session Expired, Please Login ");
                            i.putExtra("keyAlert", "Session Expired");
                            i.setClassName(getPackageName(), getPackageName() + ".SessionExpired");
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);

                        } else
                            Error_Dialog.show(jObj.getString("responseString"), AgendaDetails.this);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Paper.book("Sync").write(appDetails.getAppId(), true);
                if (error instanceof TimeoutError) {
                    Error_Dialog.show("Timeout", AgendaDetails.this);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, context), AgendaDetails.this);
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show("Please Check Your Internet Connection", AgendaDetails.this);
                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userid", Paper.book().read("userId", ""));
                params.put("action", mark);
                params.put("schedules", myCustomArray.toString());
                params.put("appid", appDetails.getAppId());

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
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
    protected void onPause() {
        super.onPause();
        messageHandler.removeCallbacks(runnable);
    }

    @Override
    protected void onStop() {
        App.getInstance().cancelall();
        super.onStop();
    }

    // send the rating to server

    private void sendRating(final float rating, final String s) {

        final ProgressDialog dialog = new ProgressDialog(AgendaDetails.this, R.style.MyAlertDialogStyle);
        dialog.setMessage("Submitting...");
        dialog.show();
        String tag_string_req = "Rating";
        String url = ApiList.SetRating;
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                try {

                    JSONObject jObj = new JSONObject(response);


                    if (jObj.getBoolean("response")) {
                        ratinBar.setRating((float) jObj.getDouble("rating_average"));
                        Error_Dialog.show("Thank you for your feedback", AgendaDetails.this);
                    } else {
                        Error_Dialog.show(jObj.getString("responseString"), AgendaDetails.this);
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
                    Error_Dialog.show("Timeout", AgendaDetails.this);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, context), AgendaDetails.this);
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show("Please Check Your Internet Connection", AgendaDetails.this);
                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userid", Paper.book().read("userId", ""));
                params.put("action", s);
                params.put("type", "agenda");
                params.put("rating", String.valueOf(rating));
                params.put("type_id", String.valueOf(item.getAgendaId()));
                params.put("appid", appDetails.getAppId());

                return params;
            }

        };


        App.getInstance().addToRequestQueue(strReq, tag_string_req);
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

    }

    // checking for agenda Polling

/*
    int p;
    boolean flag=false;
            private void checkAgenda() {
                String s[]=items.getPoll_type();
                for(int j=0; j<2; j++){
                    if(s[j].equals("agenda"))
                    {
                        flag=true;
                        p=j;
                    }
                }
                if(flag){*/

    public boolean ispollAvailable(int pid) {
        for (int i = 0; i < e.getTabs().length; i++){
            if (e.getTabs(i).getType().contains("agenda")) {
                typeid = i;
            }}
  /*  for (int i = 0; i < e.getTabs(typeid).getAgendaSize(); i++) {
        for (int j = 0; j < e.getTabs(typeid).getAgenda(i).getDetailSize(); j++) {
            agendaidlist.add(e.getTabs(typeid).getAgenda(i).getDetail(j).getAgendaId());
        }}*/
        /*   *//* for(int j=0; j<e.getTabs(i).getItems().length; j++) {
        if(e.getTabs(i).getItems(j).getPoll_type().contains("agenda"))}*//*
    for (int j = 0; j < e.getTabs(typeid).getAgendaSize(); j++) {
        agendaidlist.add(e.getTabs(typeid).getAgenda(j).);
    }*/

        for (int i = 0; i < e.getTabs().length; i++){
            if (e.getTabs(i).getType().contains("polling")) {
                pos = i;
                for(int j=0; j<e.getTabs(i).getItems().length; j++) {
                    if(e.getTabs(i).getItems(j).getPoll_type().contains("agenda"))
                        pollid.add(e.getTabs(i).getItems(j).getPoll_type_id());
                }
            }}
        int s=  item.getAgendaId();
        if(pollid.contains(s))
            return true;

        return false;
    }


    private void checkAgenda() {
        boolean flag = false;
        for (int i = 0; i < e.getTabs().length; i++)
            if (e.getTabs(i).getType().contains("polling")) {
                pos = i;
                flag=true;
                for(int j=0; j<e.getTabs(i).getItems().length; j++) {
                    if(e.getTabs(i).getItems(j).getPoll_type().contains("agenda"))
                        flag = true;
                    polltypeid= e.getTabs(i).getItems(j).getPoll_type_id();
                }
                break;
            }
        if(flag){
            Intent j = new Intent(AgendaDetails.this, PollingRoot.class);

            j.putExtra("pos", pos);
            j.putExtra("title", e.getTabs(pos).getTitle());
            j.putExtra("polltype","agenda");
            j.putExtra("polltypeid",polltypeid);
            if(ispollAvailable(polltypeid)){
                startActivity(j);}
            else{
                Error_Dialog.show("This Agenda has no Polling",AgendaDetails.this);
            }


        } else {
            Error_Dialog.show("Polling is not available for this agenda", AgendaDetails.this);
        }

    }



    // check survey feedback is done for that item or not
    private void checkSurveyIsAlreadyDone() {
        String dateTime = String.valueOf(System.currentTimeMillis());
        final ProgressDialog dialog = new ProgressDialog(AgendaDetails.this, R.style.MyAlertDialogStyle);
        dialog.setMessage("Checking Survey Feedback...");
        dialog.show();
        String tag_string_req = "SurveyCheck";
        String url = ApiList.SurveyFeedback;
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                try {
                    System.out.println(response);
                    JSONObject jObj = new JSONObject(response);


                    if (jObj.getBoolean("response") && jObj.getInt("survey_flag") == 0) {


                        int position = -1;
                        for (int count = 0; count < e.getTabs().length; count++) {
                            if (e.getTabs(count).getType().compareTo("survey") == 0
                                    && e.getTabs(count).getSub_type().equalsIgnoreCase("agenda") &&
                                    e.getTabs(count).getCheckvalue().equalsIgnoreCase(item.getSurvey_checkvalue())) {
                                if (e.getTabs(count).getItemsSize() > 0) {
                                    position = count;
                                    break;
                                }


                            }

                        }

                        if (position != -1 &&!item.getSurvey_checkvalue().equalsIgnoreCase("") &&e.getTabs(position).getSub_type().equalsIgnoreCase("agenda")
                                && e.getTabs(position).getItemsSize() > 0) {
                            Bundle args = new Bundle();
                            args.putSerializable("Agendaview", item);

                            Intent intent = new Intent(AgendaDetails.this, SurveyRoot.class);
                            intent.setAction("com.agendadetails");
                            intent.putExtra("pos", position);
                            intent.putExtra("title", "Survey");
                            intent.putExtras(args);
                            startActivity(intent);
                        } else {
                            Error_Dialog.show("Information not available.", AgendaDetails.this);
                        }

                    } else {

                        if (jObj.getBoolean("response") && jObj.getInt("survey_flag") == 1) {

                            Error_Dialog.show(jObj.getString("responseString"), AgendaDetails.this);
                        } else if (jObj.getString("responseString").equalsIgnoreCase("Invalid token")) {
                            Error_Dialog.show("Session is Expired Please Login", AgendaDetails.this);
                        } else
                            Error_Dialog.show(jObj.getString("responseString"), AgendaDetails.this);
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
                    Error_Dialog.show("Timeout", AgendaDetails.this);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, AgendaDetails.this),
                            AgendaDetails.this);
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show("Please Check Your Internet Connection", AgendaDetails.this);
                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userid", Paper.book().read("userId", ""));
                params.put("appId", appDetails.getAppId());
                params.put("agenda_id", String.valueOf(item.getAgendaId()));
                params.put("survey_type", "agenda");
                params.put("flag", "check");

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("token", token);
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





    private void showtimer(boolean timerflag) {
        try {
            if (timerflag) {
//                long oldMillis = appDetails.getStartdate();

//                System.out.println("Timer Agenda Time : " + e.getTabs(1).getAgenda(0).getDetail(0).getFromtime());
                long oldMillis = item.getFromtime();
                //ong oldMillis = e.getTabs(agenda_pos).getAgenda(0).getDetail(0).getFromtime();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/M/yyyy hh:mm:ss");
                SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("dd/M/yyyy hh:mm:ss");

                SimpleDateFormat format = new SimpleDateFormat("yy/MM/dd HH:mm:ss ");
              /*imeZone tz = TimeZone.getTimeZone(appDetails.getTimezone());
               System.out.print(tz);
                DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");*/

                Calendar cal1 = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                long utcTime = cal1.getTimeInMillis();


                System.out.print(utcTime);


                // String currentdate =utcTime.toString();
                //    System.out.print(tz);
                SimpleDateFormat f = new SimpleDateFormat("dd-MMM-yyyy");

                long temp=0l;
                long endtime=0l;
                temp=item.getFromtime();
                endtime=item.getTotime();





                Date d1 = simpleDateFormat.parse("14/04/2019 00:00:00");
                d1 = simpleDateFormat.parse(simpleDateFormat.format(item.getFromtime()));
                //  Date d2 = simpleDateFormat.parse("10/04/2019 00:00:00");
                // d2 = simpleDateFormat.parse(simpleDateFormat2.format(System.currentTimeMillis()));



              /*  Date d1 = null;
                Date d2 = null;*/

                /*try {
                    d1 =new Date(temp);
                    d2 = currentdate;

                } catch (Exception e) {
                    e.printStackTrace();
                }*/
                String zone, timeZone;
                timeZone = appDetails.getTimezone();

                String s1=checkTimeZone(timeZone);

                if (s1 == "") {
                    zone = "Asia/Calcutta";
                } else if (timeZone.equalsIgnoreCase("IST") ) {
                    zone = "Asia/Calcutta";
                } else if (timeZone.equalsIgnoreCase("PST") ) {
                    zone = "America/Los_Angeles";
                } else if (timeZone.equalsIgnoreCase("EST")) {
                    zone = "America/New_York";
                } else if (timeZone.equalsIgnoreCase("CST") ) {
                    zone = "America/Chicago";
                } else {
                    zone = s1;
                }
                Calendar cal12 = Calendar.getInstance(TimeZone.getTimeZone(zone.toString()));
                long utcTime2 = cal12.getTimeInMillis();
              //  getUTCDateFromLocal(utcTime2);
                System.out.println(timeZone);
                long differ = TimeZone.getTimeZone(zone.toString()).getOffset(Calendar.getInstance().getTimeInMillis());
//                long different = (d1.getTime() - timediff) - d2.getTime();
                long different = ((temp)-differ) - utcTime;
                long diff1=((endtime)-differ) - utcTime;

                getUTCDateFromLocal(System.currentTimeMillis());




                //System.out.println("Date Problem Diff : " + different);

//                long milliDiff = oldMillis - start;
                // long start = System.currentTimeMillis();
                //long milliDiff = oldMillis - start;
                if (different > 0) {
                    sessionstimer = new CounterClass(different, 1000);
                    sessionstimer.start();
                    timertoshow.setVisibility(View.VISIBLE);
                    live_broad.setVisibility(View.GONE);

                }


                else if(diff1>0){
                    live_broad.setVisibility(View.VISIBLE);
                    timertoshow.setVisibility(View.GONE);
                }
                else  {
                    live_broad.setVisibility(View.GONE);
                    timertoshow.setVisibility(View.GONE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public static long getUTCDateFromLocal(long localDate) {
        TimeZone tz = TimeZone.getDefault();
        long gmtOffset = tz.getOffset(localDate);
        return localDate + gmtOffset;
    }
    public String checkTimeZone(String s)
    {String val="";

        Map<String ,String> hm=new HashMap<String,String>();

        hm.put("ADT" ,"America/Halifax");
        hm.put("AKDT", "America/Juneau");
        hm.put("AKST", "America/Juneau");
        hm.put("ART", "America/Argentina/Buenos_Aires");
        hm.put("AST", "America/Halifax");
        hm.put("BDT", "Asia/Dhaka");
        hm.put("BRST", "America/Sao_Paulo");
        hm.put("BRT", "America/Sao_Paulo");
        hm.put("BST", "Europe/London");
        hm.put("CAT", "Africa/Harare");
        hm.put("CDT", "America/Chicago");
        hm.put("CEST", "Europe/Paris");
        hm.put("CET", "Europe/Paris");
        hm.put("CLST", "America/Santiago");
        hm.put("CLT", "America/Santiago");
        hm.put("COT", "America/Bogota");
        hm.put("CST", "America/Chicago");
        hm.put("EAT", "Africa/Addis_Ababa");
        hm.put("EDT", "America/New_York");
        hm.put("EEST", "Europe/Istanbul");
        hm.put("EET", "Europe/Istanbul");
        hm.put("EST", "America/New_York");
        hm.put("GMT", "GMT");
        hm.put("GST", "Asia/Dubai");
        hm.put("HKT", "Asia/Hong_Kong");
        hm.put("HST", "Pacific/Honolulu");
        hm.put("ICT", "Asia/Bangkok");
        hm.put("IRST", "Asia/Tehran");
        hm.put("IST", "Asia/Calcutta");
        hm.put("JST", "Asia/Tokyo");
        hm.put("KST", "Asia/Seoul");
        hm.put("MDT", "America/Denver");
        hm.put("MSD", "Europe/Moscow");
        hm.put("MSK", "Europe/Moscow");
        hm.put("MST", "America/Denver");
        hm.put("NZDT", "Pacific/Auckland");
        hm.put("NZST", "Pacific/Auckland");
        hm.put("PDT", "America/Los_Angeles");
        hm.put("PET", "America/Lima");
        hm.put("PHT", "Asia/Manila");
        hm.put("PKT", "Asia/Karachi");
        hm.put("PST", "America/Los_Angeles");
        hm.put("SGT", "Asia/Singapore");
        hm.put("UTC", "UTC");
        hm.put("WAT", "Africa/Lagos");
        hm.put("WEST", "Europe/Lisbon");
        hm.put("WET", "Europe/Lisbon");
        hm.put("WIT", "Asia/Jakarta");

        if (hm.containsKey(s)) {
            val = (String)hm.get(s);


        }
        return val;
    }

    public class CounterClass extends CountDownTimer {
        public CounterClass(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
//            timertoshow.setVisibility(View.GONE);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            mDisplayDays = (int) ((millisUntilFinished / 1000) / 86400);
            mDisplayHours = (int) (((millisUntilFinished / 1000) - (mDisplayDays * 86400)) / 3600);
            mDisplayMinutes = (int) (((millisUntilFinished / 1000) - ((mDisplayDays * 86400) + (mDisplayHours * 3600))) / 60);
            mDisplaySeconds = (int) ((millisUntilFinished / 1000) % 60);
           /* this.mDisplayDays = (int) (millisUntilFinished / (1000*60*60*24));
            this.mDisplayHours = (int) ((millisUntilFinished / (1000*60*60)) % 24);
            this.mDisplayMinutes = (int) ((millisUntilFinished / (1000*60)) % 60);
            this.mDisplaySeconds = (int) ((millisUntilFinished / 1000) % 60);*/
            txtdayshow.setText("" + mDisplayDays);
            txthrshow.setText("" + mDisplayHours);
            txtminshow.setText("" + mDisplayMinutes);
            txtsecshow.setText("" + mDisplaySeconds);
        }
    }


}
