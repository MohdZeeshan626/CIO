package com.singleevent.sdk.View.RightActivity.group_feed;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.google.gson.Gson;
import com.singleevent.sdk.ApiList;
import com.singleevent.sdk.App;
import com.singleevent.sdk.Custom_View.Error_Dialog;
import com.singleevent.sdk.Custom_View.Letter.LetterTileProvider;
import com.singleevent.sdk.Custom_View.Util;
import com.singleevent.sdk.Custom_View.VolleyErrorLis;
import com.singleevent.sdk.Left_Adapter.AdsImageAdapter;
import com.singleevent.sdk.model.AppDetails;
import com.singleevent.sdk.model.Events;
import com.singleevent.sdk.model.User;
import com.singleevent.sdk.R;
import com.singleevent.sdk.View.LeftActivity.Attendee;
import com.singleevent.sdk.View.LeftActivity.Attendee_Profile;
import com.singleevent.sdk.View.RightActivity.Group_feed;
import com.singleevent.sdk.View.RightActivity.Profile;
import com.singleevent.sdk.View.RightActivity.admin.checkin.VolleyResponseListener;
import com.singleevent.sdk.View.RightActivity.admin.checkin.VolleyUtils;
import com.singleevent.sdk.utils.DataBaseStorage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import io.paperdb.Paper;

public class Privategroup extends AppCompatActivity implements View.OnClickListener,PrivateListAdapter.OnCardClickListner,VolleyResponseListener {

    private static final String TAG = Attendee.class.getSimpleName();
    AppDetails appDetails;
    private Context context;

    int tileSize;
    Resources res;
    LetterTileProvider tileProvider;
    TextView noitems,tv_checkInternet;
    RecyclerView userlist;
    Toolbar toolbar;
    int pos, ban_pos;
    List<User> userview;
    PrivateListAdapter bookListAdapter;
    EditText attendee_search;
    Events e;
    ArrayList<String> suid=new ArrayList();
    public static final String CHECK_IN_REQ_TAG = "add_user";
    private RequestQueue queue1;

    ArrayList<Events> events;
    DisplayMetrics displayMetrics;
    int adHeight, adWidth;
    CardView cardView;
    private static int currentPage = 0;
    private SwipeRefreshLayout swiperefresh;
    ImageView adsImage;
    String userid,token,group_id;
    private static ViewPager mPager;
    private ArrayList<String> img_arr = new ArrayList<String>();
    private ArrayList<String> butn_url_arr = new ArrayList<String>();
    private ArrayList<String> ban_type_arr = new ArrayList<String>();
    private ArrayList<Integer> type_id_arr = new ArrayList<Integer>();
    private ArrayList<String> ban_name_arr = new ArrayList<String>();
    Button selectdone;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Paper.init(this);
        setContentView(R.layout.private_group);
        res = getResources();


        token = Paper.book().read("token", "");
        userid = Paper.book().read("userId","");

        context = this;

        events = Paper.book().read("Appevents");
        e = events.get(0);
        tileSize = res.getDimensionPixelSize(R.dimen.letter_tile_size);
        appDetails = Paper.book().read("Appdetails");
        noitems = (TextView) findViewById(R.id.noitems);
        tv_checkInternet =(TextView) findViewById(R.id.tv_checkInternet);
        swiperefresh = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        userlist = (RecyclerView) findViewById(R.id.userlist);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        cardView = (CardView) findViewById(R.id.atendee_ad_card_view);
        //mPager = (ViewPager) findViewById(R.id.attendee_adsImage_pager);
        adsImage = (ImageView) findViewById(R.id.adsImage);
        attendee_search = (EditText) findViewById(R.id.attendee_search_edittext);
        toolbar.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));
        selectdone=(Button)findViewById(R.id.selectdone);
      //  selectdone.setBackgroundColor(Color.parseColor("#ffffff"));
        selectdone.setOnClickListener(this);
        queue1 = Volley.newRequestQueue(this);
        setSupportActionBar(toolbar);
        if (getIntent().getExtras() == null)
            finish();


        group_id = getIntent().getExtras().getString("group_id");



        tileProvider = new LetterTileProvider(context);

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        userview = new ArrayList<>();
        bookListAdapter = new PrivateListAdapter(this,userview);
        userlist.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,
                false));
        userlist.setAdapter(bookListAdapter);
        bookListAdapter.setOnCardClickListner(this);

        getuser();

        // registering receiver

        attendee_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });

       // initializeAd();

        swiperefresh.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {

                        swiperefresh.setRefreshing(true);
                        getuser();
                    }
                }
        );

    }
    private void startThread() {

        final String[] decryptedString = new String[1];
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

            }
        };

        Thread t = new Thread(r);
        t.start();
    }

    private void initializeAd() {

        LinearLayout.LayoutParams layoutParams;
        layoutParams = new LinearLayout.LayoutParams(adWidth, adHeight);

        //adsImage.setLayoutParams(layoutParams);
        for (int i = 0; i < e.getTabs().length; i++)
            if (e.getTabs(i).getType().contains("banner")) {

                ban_pos = i;
                if (e.getTabs(i).getItems().length > 0) {
                    setAds();
                    break;
                }
            } else
                cardView.setVisibility(View.GONE);
    }

    private void setAds() {
       // cardView.setVisibility(View.VISIBLE);
        final int img_size = e.getTabs(ban_pos).getItems().length;
        for (int i = 0; i < img_size; i++) {
            img_arr.add(e.getTabs(ban_pos).getItems(i).getBanner_url());
            butn_url_arr.add(e.getTabs(ban_pos).getItems(i).getButton_link());
            ban_type_arr.add(e.getTabs(ban_pos).getItems(i).getBanner_type());
            ban_name_arr.add(e.getTabs(ban_pos).getItems(i).getBanner_name());
            type_id_arr.add(e.getTabs(ban_pos).getItems(i).getType_id());
        }

       /* mPager.setAdapter(new AdsImageAdapter(this,getApplicationContext(),
                img_arr, butn_url_arr, ban_type_arr, type_id_arr, ban_name_arr));*/
        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == img_size) {
                    currentPage = 0;
                }
            //    mPager.setCurrentItem(currentPage++, false);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3000, 3000);
    }

    public void filter(String text){
        List<User> temp = new ArrayList();
        for(User d: userview){

            if(d.getFirst_name().toLowerCase().contains( text.toLowerCase() )){
                temp.add(d);
            }
        }
        //update recyclerview
        bookListAdapter.updateList(temp);
    }

    public void getId(String id){
        if(!id.equalsIgnoreCase("")) {
            suid.add(id);
        }
    }

    private void getuser() {

        final ProgressDialog dialog = new ProgressDialog(Privategroup.this, R.style.MyAlertDialogStyle);
        dialog.setMessage("Loading the User");
        dialog.show();
        String tag_string_req = "Login";
        String url = ApiList.Users + appDetails.getAppId()+"&userid="+userid+"&admin_flag=attendee_option";
        StringRequest strReq = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                if (swiperefresh.isRefreshing()){
                    swiperefresh.setRefreshing(false);
                }
                tv_checkInternet.setVisibility(View.GONE);
                try {

                    JSONObject jObj = new JSONObject(response);


                    if (jObj.getBoolean("response")) {
                        userview.clear();

                        parseuser(jObj.getJSONObject("responseString").getJSONArray("users"));
                    } else {
                        if (jObj.getString("responseString").equalsIgnoreCase("Invalid token")) {
                            Intent i = new Intent();
                            i.putExtra("keyMessage", "Session Expired, Please Login ");
                            i.putExtra("keyAlert", "Session Expired");
                            i.setClassName(getPackageName(), "com.singleevent.sdk.View.TokenExpireAlertReceived");
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);

                        } else
                            Error_Dialog.show(jObj.getString("responseString"), Privategroup.this);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    if (swiperefresh.isRefreshing()){
                        swiperefresh.setRefreshing(false);
                    }
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                if (swiperefresh.isRefreshing()){
                    swiperefresh.setRefreshing(false);
                }
                if (error instanceof TimeoutError) {
                    Error_Dialog.show("Timeout", Privategroup.this);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, context), Privategroup.this);
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    tv_checkInternet.setVisibility(View.VISIBLE);

                    userview.clear();
                    userview = Paper.book(appDetails.getAppId()).read("OfflineAttendeeList",new ArrayList<User>());

                    if (userview.size() > 0) {
                        bookListAdapter = new PrivateListAdapter(Privategroup.this,userview);
                        userlist.setAdapter(bookListAdapter);
                        bookListAdapter.notifyDataSetChanged();
                        showview(true);
                    } else
                        showview(false);

                }

            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("token", token);
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

    private void parseuser(JSONArray responseString) {


        try {
            userview.clear();
            Gson gson = new Gson();

            Random r = new Random();

            for (int i = 0; i < responseString.length(); i++) {
                String eventString = responseString.getJSONObject(i).toString();
                User obj = gson.fromJson(eventString, User.class);
                obj.setColor(Color.argb(255, r.nextInt(256), r.nextInt(256), r.nextInt(256)));

                userview.add(obj);

            }

            Collections.sort(userview, new Comparator<User>() {
                @Override
                public int compare(User o1, User o2) {
                    return o1.getFirst_name().compareToIgnoreCase(o2.getFirst_name());
                }
            });

            Paper.book(appDetails.getAppId()).write("OfflineAttendeeList",userview);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        if (userview.size() > 0) {
            bookListAdapter.notifyDataSetChanged();
            showview(true);
        } else
            showview(false);


    }

 /*   private void sendInvite()
    {


        // converting arraylist to jsonarray


*//*
        Gson gson = new GsonBuilder().create();
        final JsonArray myCustomArray = gson.toJsonTree(list).getAsJsonArray();*//*


        final ProgressDialog dialog = new ProgressDialog(Privategroup.this,R.style.MyAlertDialogStyle);
        //dialog.setMessage(msg);
        dialog.show();
        String tag_string_req = "Creat Group";
        String url ="http://check.webmobi.in:3030/add_user";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                try {

                    JSONObject jObj = new JSONObject(response);


                    if (jObj.getBoolean("response")) {
                        Error_Dialog.show(jObj.getString("responseString"), Privategroup.this);



                    } else {
                        if (jObj.getString("responseString").equalsIgnoreCase("Invalid token")) {
                            Intent i = new Intent();
                            i.putExtra("keyMessage", "Session Expired, Please Login ");
                            i.putExtra("keyAlert", "Session Expired");
                            i.setClassName(getPackageName(), getPackageName()+".SessionExpired");
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);

                        } else
                            Error_Dialog.show(jObj.getString("responseString"), Privategroup.this);
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
                    Error_Dialog.show("Timeout", Privategroup.this);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, Privategroup.this), Privategroup.this);
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show("Please Check Your Internet Connection", Privategroup.this);
                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                ArrayList<String> s=new ArrayList<>();
                s.add( Paper.book().read("tempuserid"));
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id",new JSONArray(Arrays.asList(s.toArray())));
                params.put("group_id", group_id);
                params.put("joined_date",String.valueOf(System.currentTimeMillis()));
                params.put("is_admin","true");
                params.put("status","invited");

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("token",  Paper.book().read("token", ""));
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


    }*/
    private void sendInviteReq() {
        String reqTag = CHECK_IN_REQ_TAG;
        String checkInUrl ="https://chat.webmobi.com/add_user";
        ArrayList<String> s=new ArrayList<>();
        s.add( Paper.book().read("tempuserid"));
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", new JSONArray(Arrays.asList(s.toArray())));
            jsonObject.put("group_id", group_id);
            jsonObject.put("joined_date",String.valueOf(System.currentTimeMillis()));
            jsonObject.put("is_admin","false");
            jsonObject.put("status","invited");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjReq = VolleyUtils.createJsonPostReq(reqTag, checkInUrl, jsonObject,
                Privategroup.this);
        // Adding request to request queue
        jsonObjReq.setShouldCache(false);
        queue1.add(jsonObjReq);
    }

    public void onVolleyResponse(String tag, JSONObject response) throws JSONException {
        if (tag.equals(CHECK_IN_REQ_TAG)) {
            String responseString = response.getString("responseString");
            if (response.getBoolean("response")) {


                //handleUser(selfsuccess);
                //notifyTone(selfsuccess);

                Toast.makeText(this, Util.applyFontToMenuItem(this,
                        new SpannableString("Invited Successfully")), Toast.LENGTH_SHORT).show();
                new CountDownTimer(2000, 1000) {

                    public void onTick(long millisUntilFinished) {
                    }

                    public void onFinish() {
                        Intent i=new Intent(Privategroup.this,Group_feed.class);
                        startActivity(i);
                        finish();
                    }
                }.start();


               /* getUserDetails(usersUrl, true);
                if (file_write_permission) {
                    if (isPrintEnabled && agenda_id == 0)
                        try {
                            generatePDF(boolean_print_online);
                        } catch (DocumentException e1) {
                            e1.printStackTrace();
                        }
                } else
                    fileWritePermission(true);
*/
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

    public String convertStrigyfy(ArrayList<String> s1)
    {

        String d=s1.toString();

        return d;
    }
    private void showview(boolean flag) {

        if (flag) {
            swiperefresh.setVisibility(View.VISIBLE);
            noitems.setVisibility(View.GONE);
        } else {
            swiperefresh.setVisibility(View.GONE);
            noitems.setVisibility(View.VISIBLE);
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
    protected void onResume() {
        super.onResume();

        // if ( token == null )startThread();

        SpannableString s = new SpannableString("Attendees List");
        setTitle(Util.applyFontToMenuItem(this, s));
        userid = Paper.book().read("userId","");
        if (userview.size()>0){
            showview(true);
        }else
        {
            showview(false);
        }

    }


    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.selectdone){

            sendInviteReq();
        }

    }
    @Override
    public void OnItemLongClicked(View view, User user, int position) {

    }

    @Override
    public void OnItemClick(View view, User user, int position) {
        Intent i;
        String userid =  user.getUserid();
        if (!Paper.book().read("userId","").equals(userid)){
            Bundle args = new Bundle();
            args.putSerializable("UserItem", user);

            i = new Intent(Privategroup.this, Attendee_Profile.class);
            i.putExtras(args);

            startActivity(i);

        }else {
            i = new Intent(Privategroup.this, Profile.class);
            startActivity(i);

        }
    }
}
