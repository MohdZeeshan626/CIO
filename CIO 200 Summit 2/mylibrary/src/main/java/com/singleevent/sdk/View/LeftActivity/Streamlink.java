package com.singleevent.sdk.View.LeftActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.text.SpannableString;
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
import com.singleevent.sdk.ApiList;
import com.singleevent.sdk.App;
import com.singleevent.sdk.Custom_View.Error_Dialog;
import com.singleevent.sdk.Custom_View.Util;
import com.singleevent.sdk.Custom_View.VolleyErrorLis;
import com.singleevent.sdk.Left_Adapter.AgendaAdapter;
import com.singleevent.sdk.model.Agenda.Agendadetails;
import com.singleevent.sdk.model.AppDetails;
import com.singleevent.sdk.R;
import com.singleevent.sdk.View.RightActivity.admin.checkin.VolleyResponseListener;
import com.singleevent.sdk.View.RightActivity.admin.checkin.VolleyUtils;
import com.singleevent.sdk.View.RightActivity.admin.utils.Urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import io.paperdb.Paper;

public class Streamlink extends AppCompatActivity implements VolleyResponseListener
{
    AppDetails appDetails;
    int temp;
    String agendaID;
    String c_name ,title;
    private String appid,username;

    String role,group_id,token,group_name,surl;
    boolean isAdmin;
    ArrayList<String> userid = new ArrayList<>();
    ArrayList<String> date = new ArrayList<>();
    ArrayList<String> email = new ArrayList<>();
    ArrayList<String> agendaid = new ArrayList<>();
    ArrayList<String> checkdate = new ArrayList<>();
    String checkInUrl = Urls.getCheckInUrl();
    public static final String CHECK_IN_REQ_TAG = "check_in_users";
    private RequestQueue queue1;
    private Agendadetails item;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Paper.init(this);
        appDetails = Paper.book().read("Appdetails", null);


/*
        Intent intent = null;
        try {
            intent = new Intent(this,
                    Class.forName("io.agora.openvcall.ui.MainActivity"));
            startActivity(intent);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }*/

        Bundle extras = getIntent().getExtras();
        if (extras == null)
            finish();


        surl = extras.getString("stream_url");
        title = extras.getString("title");
        group_id=getIntent().getExtras().getString("Group_id");
        temp=extras.getInt("agendaId");
        item = (Agendadetails) getIntent().getSerializableExtra("AgendaList");

        agendaID=String.valueOf(temp);
        token=extras.getString("token");
        agendaid.add(agendaID);
        appid=appDetails.getAppId();
        userid.add((Paper.book().read("userId","")));
        email.add(Paper.book().read("usermail"));

        // email.add(user_details.getEmail());
        checkdate.add(String.valueOf(System.currentTimeMillis()));
        queue1 = Volley.newRequestQueue(this);

        callApiForChannel();


        //  Addnotes();
    }
    private void sendCheckinData() {
        String reqTag = CHECK_IN_REQ_TAG;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("appId", appDetails.getAppId());
            jsonObject.put("userIds", new JSONArray(Arrays.asList(userid.toArray())));
            jsonObject.put("checkin_date", new JSONArray(Arrays.asList(checkdate.toArray())));
            jsonObject.put("email_id", new JSONArray(Arrays.asList(email.toArray())));
            jsonObject.put("agenda_id", new JSONArray(Arrays.asList(agendaid.toArray())));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjReq = VolleyUtils.createJsonPostReq(reqTag, checkInUrl, jsonObject,
                Streamlink.this);
        // Adding request to request queue
        jsonObjReq.setShouldCache(false);
        queue1.add(jsonObjReq);
    }
    public void onVolleyResponse(String tag, JSONObject response) throws JSONException {
        if (tag.equals(CHECK_IN_REQ_TAG)) {
            String responseString = response.getString("responseString");
            if (response.getString("response").equals("success")) {
                Boolean selfsuccess=true;
                //handleUser(selfsuccess);
                //notifyTone(selfsuccess);

                Toast.makeText(this, Util.applyFontToMenuItem(this,
                        new SpannableString("Checkin Successfully")), Toast.LENGTH_SHORT).show();
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

    public void addchatID() {

        // converting arraylist to jsonarray
        final ProgressDialog dialog = new ProgressDialog(Streamlink.this, R.style.MyAlertDialogStyle);
        dialog.setMessage("Adding User");
        dialog.show();
        String tag_string_req = "Schdule";
        String url ="https://chat.webmobi.com/add_user";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                try {

                    JSONObject jObj = new JSONObject(response);


                    if (jObj.getBoolean("response")) {
                        try {
                            sendCheckinData();
                            /*Intent intent = new Intent(Streamlink.this, JoinMeeting.class);
                            intent.putExtra("url",surl);
                            intent.putExtra("title",title);

                            intent.putExtra("Group_id",group_id);
                            startActivity(intent);
                            finish();*/

                            Bundle args = new Bundle();
                            args.putInt("agendaId",temp);
                            args.putString("token",token);
                            args.putString("url",surl);
                            args.putString("title",title);
                            args.putString("Group_id",group_id);
                            args.putSerializable("AgendaList",item);
                            Intent intent = new Intent(Streamlink.this, JoinMeeting.class);
                            intent.putExtras(args);
                            startActivity(intent);
                            finish();

                        }catch (Exception e)
                        {}

                    } else {
                        if (jObj.getString("responseString").equalsIgnoreCase("Invalid token")) {
                            Intent i = new Intent();
                            i.putExtra("keyMessage", "Session Expired, Please Login ");
                            i.putExtra("keyAlert", "Session Expired");
                            i.setClassName(Streamlink.this.getPackageName(), Streamlink.this.getPackageName()+".SessionExpired");
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            Streamlink.this.startActivity(i);

                        } else
                            Error_Dialog.show(jObj.getString("responseString"), Streamlink.this);
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
                    Error_Dialog.show("Timeout", Streamlink.this);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, Streamlink.this), Streamlink.this);
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show("Please Check Your Internet Connection", Streamlink.this);
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
                    params.put("is_admin","false");}

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

    private void callApiForChannel() {

        final ProgressDialog dialog = new ProgressDialog(Streamlink.this, R.style.MyAlertDialogStyle);
        dialog.setMessage("Loading Details ...");
        dialog.show();


        String tag_string_req = "Login";
        String url = ApiList.ChannelBroadCast;
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {

                try {
                    System.out.println(response);
                    JSONObject jObj = new JSONObject(response);
                    System.out.print("This is API response "+response);

                    if (jObj.getBoolean("response")) {
                        c_name = jObj.getString("channelName");
                        role = jObj.getString("session_admin_flag");
                        group_id=jObj.getString("group_id");
                        group_name=jObj.getString("group_name");
                        Paper.book().write("GROUP_ID",group_id);
                        Paper.book().write("Group_Name",group_name);

                        addchatID();
                        if(role.equalsIgnoreCase("speaker")){
                            isAdmin=true;
                        }
                        else{
                            isAdmin=false;
                        }

                        dialog.dismiss();

                    } else {
                        dialog.dismiss();
                        Error_Dialog.show(jObj.getString("responseString"), Streamlink.this);
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
                    Error_Dialog.show("Timeout", Streamlink.this);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, Streamlink.this), Streamlink.this);
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show("Please Check Your Internet Connection", Streamlink.this);
                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("appid", appDetails.getAppId());
                params.put("userid", Paper.book().read("userId",""));
                params.put("agenda_id",agendaID);
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


}
