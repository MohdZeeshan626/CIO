package com.singleevent.sdk.View.RightActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.SpannableString;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.singleevent.sdk.App;
import com.singleevent.sdk.Custom_View.Error_Dialog;
import com.singleevent.sdk.Custom_View.Util;
import com.singleevent.sdk.Custom_View.VolleyErrorLis;
import com.singleevent.sdk.Left_Adapter.Group_List_Adapter;
import com.singleevent.sdk.Left_Adapter.Invite_List_Adapter;
import com.singleevent.sdk.model.AppDetails;
import com.singleevent.sdk.R;
import com.singleevent.sdk.View.RightActivity.admin.checkin.VolleyResponseListener;
import com.singleevent.sdk.View.RightActivity.admin.checkin.VolleyUtils;
import com.singleevent.sdk.View.RightActivity.group_feed.GroupFeedView;
import com.singleevent.sdk.agora.openvcall.model.GroupInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import io.paperdb.Paper;

public class Group_feed extends AppCompatActivity implements View.OnClickListener,Group_List_Adapter.OnCardClickListner,Invite_List_Adapter.OnCardClickListner ,VolleyResponseListener {

    RelativeLayout rightgroup, leftgroup,groupheader;
    RecyclerView groupList;
    Group_List_Adapter group_list_adapter;
    Invite_List_Adapter invite_list_adapter;
    private SwipeRefreshLayout swiperefresh;
    public static final String CHECK_IN_REQ_TAG = "add_user";
    private RequestQueue queue1;
    Toolbar toolbar;
    AppDetails appDetails;
    TextView noitems,grouplisttext;
    static ArrayList<GroupInfo> groupinfo;
    static ArrayList<GroupInfo> privateinfo;
    String u_status;
    String u_group_id;
    TextView gmygroups,ginvite;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Paper.init(this);
        appDetails = Paper.book().read("Appdetails");
        setContentView(R.layout.main_group_feed);
        rightgroup = (RelativeLayout) findViewById(R.id.rightgroup);
        leftgroup = (RelativeLayout) findViewById(R.id.leftgroup);
        groupheader=(RelativeLayout)findViewById(R.id.groupheader);
        swiperefresh = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        noitems = (TextView) findViewById(R.id.noitems);
        groupList=(RecyclerView)findViewById(R.id.grouplist);
        grouplisttext=(TextView)findViewById(R.id.grouplisttext);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        gmygroups=(TextView)findViewById(R.id.gmygroups);
        ginvite=(TextView)findViewById(R.id.ginvite) ;
        toolbar.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));
        toolbar.setOnClickListener(this);
        if (Paper.book(appDetails.getAppId()).read("admin_flag", "").equals("admin")) {
            groupheader.setVisibility(View.VISIBLE
            );

        }else{
            groupheader.setVisibility(View.GONE);
        }
        gmygroups.setTextColor(Color.parseColor("#000000"));
        ginvite.setTextColor(Color.parseColor("#ff8b8b8b"));

        leftgroup.setOnClickListener(this);
        rightgroup.setOnClickListener(this);
        groupinfo = new ArrayList<>();
        privateinfo = new ArrayList<>();
        queue1 = Volley.newRequestQueue(this);

        if (getIntent().getExtras() == null)
        {}
       else{
       try{ u_status=getIntent().getExtras().getString("status");
        u_group_id=getIntent().getExtras().getString("group_id");
        sendAccept(u_group_id,u_status);}catch (Exception e)
       {}}
       try {
           group_list_adapter = new Group_List_Adapter(this, groupinfo);
           groupList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,
                   false));

           groupList.setAdapter(group_list_adapter);
           group_list_adapter.setOnCardClickListner(this);
       }catch(Exception e){}


        getGroupList();



        swiperefresh.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {

                        swiperefresh.setRefreshing(true);
                        getGroupList();
                    }
                }
        );


    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.rightgroup)
        {
            Intent i = new Intent(Group_feed.this, CreateGroup.class);
            startActivity(i);
        }
        else if (view.getId() == R.id.leftgroup)
        {
            getGroupList();
        }
        else if(view.getId()==R.id.toolbar)
            onBackPressed();
    }


    public void mygrouplist(View view) {
        getGroupList();
        ginvite.setTextColor(Color.parseColor("#ff8b8b8b"));
        gmygroups.setTextColor(Color.parseColor("#000000"));



    }

    public void ginvite(View view) {
       // getGroupList();
        gmygroups.setTextColor(Color.parseColor("#ff8b8b8b"));
        ginvite.setTextColor(Color.parseColor("#000000"));
      try {
          parseuser(Paper.book().read("prigroup"), "private");
          invite_list_adapter = new Invite_List_Adapter(this, privateinfo);
          groupList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,
                  false));

          groupList.setAdapter(invite_list_adapter);
          invite_list_adapter.setOnCardClickListner(this);
      }catch (Exception e)
      {

      }

        /*Intent i = new Intent(Group_feed.this, GroupFeedView.class);
        startActivity(i);
*/

    }

    private void getGroupList() {


        // converting arraylist to jsonarray


/*
        Gson gson = new GsonBuilder().create();
        final JsonArray myCustomArray = gson.toJsonTree(list).getAsJsonArray();*/


        final ProgressDialog dialog = new ProgressDialog(Group_feed.this, R.style.MyAlertDialogStyle);
       // dialog.setMessage(msg);
        dialog.show();
        String tag_string_req = "Creat Group";
        String url = "https://api.webmobi.com/api/event/feedgroup_info";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                if (swiperefresh.isRefreshing()) {
                    swiperefresh.setRefreshing(false);
                }
                try {

                    JSONObject jObj = new JSONObject(response);
                    if (jObj.getBoolean("response")) {
                        Error_Dialog.show(jObj.getString("responseString"), Group_feed.this);
                        JSONArray mygroup = jObj.getJSONArray("group_info");
                        GroupInfo gp;
                        parseuser(jObj.getJSONArray("group_info"),"mygroup");
                        JSONArray pgr=jObj.getJSONArray("invited_group_info");
                        Paper.book().write("prigroup",pgr);


                       /* for (int i = 0; i < mygroup.length(); i++) {
                            gp=new GroupInfo();
                            JSONObject jsonObject = (JSONObject) mygroup.get(i);
                           // groupinfo.add(jsonObject);

                            gp.setGroup_id(jsonObject.isNull("group_name") ? null : jsonObject.getString("group_name"));
                            gp.setGroup_id(jsonObject.isNull("group_id") ? null : jsonObject.getString("group_id"));
                            gp.setGroup_id(jsonObject.isNull("status") ? null : jsonObject.getString("status"));
                        }*/


                    } else {
                        if (jObj.getString("responseString").equalsIgnoreCase("Invalid token")) {
                            Intent i = new Intent();
                            i.putExtra("keyMessage", "Session Expired, Please Login ");
                            i.putExtra("keyAlert", "Session Expired");
                            i.setClassName(getPackageName(), getPackageName() + ".SessionExpired");
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);

                        } else
                            Error_Dialog.show(jObj.getString("responseString"), Group_feed.this);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    if (swiperefresh.isRefreshing()) {
                        swiperefresh.setRefreshing(false);
                    }
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                if (swiperefresh.isRefreshing()) {
                    swiperefresh.setRefreshing(false);
                }
                Paper.book().write("Sync", true);
                if (error instanceof TimeoutError) {
                    Error_Dialog.show("Timeout", Group_feed.this);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, Group_feed.this), Group_feed.this);
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show("Please Check Your Internet Connection", Group_feed.this);
                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("appid", appDetails.getAppId());
                params.put("userid", Paper.book().read("userId"));

                return params;
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


    private void parseuser(JSONArray responseString,String from) {


        String temp=from;
        if(from.equalsIgnoreCase("mygroup")){
        try {
            groupinfo.clear();
            Gson gson = new Gson();

            Random r = new Random();

            for (int i = 0; i < responseString.length(); i++) {
                String eventString = responseString.getJSONObject(i).toString();
                GroupInfo obj = gson.fromJson(eventString, GroupInfo.class);
                groupinfo.add(obj);

            }


            Paper.book(appDetails.getAppId()).write("AllGroups", groupinfo);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        if (groupinfo.size() > 0) {
            group_list_adapter = new Group_List_Adapter(this, groupinfo);
            groupList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,
                    false));

            groupList.setAdapter(group_list_adapter);
            group_list_adapter.notifyDataSetChanged();

            showview(true);
        } else
            showview(false);}
            else {
            try {
                privateinfo.clear();
                Gson gson = new Gson();

                Random r = new Random();

            for (int i = 0; i < responseString.length(); i++) {
                String eventString = responseString.getJSONObject(i).toString();
                GroupInfo obj = gson.fromJson(eventString, GroupInfo.class);
                privateinfo.add(obj);

            }


            Paper.book(appDetails.getAppId()).write("AllGroups1", privateinfo);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        if (privateinfo.size() > 0) {
         //   invite_list_adapter.notifyDataSetChanged();

            showview(true);
        } else
            showview(false);
        }




    }

    private void showview(boolean flag) {

        if (flag) {
            swiperefresh.setVisibility(View.VISIBLE);
            noitems.setVisibility(View.GONE);
          //  grouplisttext.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));
            grouplisttext.setVisibility(View.VISIBLE);
        } else {
            swiperefresh.setVisibility(View.GONE);
            noitems.setVisibility(View.VISIBLE);
            grouplisttext.setVisibility(View.GONE);
        }

    }

    @Override
    public void OnItemLongClicked(View view, GroupInfo user, int position) {

    }

    @Override
    public void OnItemClick(View view, GroupInfo user, int position) {
        Intent i;
        i = new Intent(Group_feed.this, GroupFeedView.class);
        i.putExtra("group_id", user.getGroup_id());
        i.putExtra("group_name", user.getGroup_name());
        startActivity(i);

    }
    public void sendAccept(String group_id,String status) {
        String st=status;

        String g_id=group_id;
        String reqTag = CHECK_IN_REQ_TAG;
        String checkInUrl ="https://chat.webmobi.com/add_user";
        ArrayList<String> s=new ArrayList<>();
        s.add( Paper.book().read("userId"));
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("user_id", new JSONArray(Arrays.asList(s.toArray())));
            jsonObject.put("group_id", g_id);
            jsonObject.put("joined_date",String.valueOf(System.currentTimeMillis()));
            jsonObject.put("is_admin","false");
            jsonObject.put("status",st);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjReq = VolleyUtils.createJsonPostReq(reqTag, checkInUrl, jsonObject,
                Group_feed.this);
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
                        new SpannableString("Accepted Successfully")), Toast.LENGTH_SHORT).show();
               /* new CountDownTimer(2000, 1000) {

                    public void onTick(long millisUntilFinished) {
                    }

                    public void onFinish() {
                        Intent i=new Intent(Group_feed.this,Group_feed.class);
                        startActivity(i);
                        finish();
                    }
                }.start();*/


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





}
