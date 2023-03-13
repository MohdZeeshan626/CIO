package com.singleevent.sdk.health.Activity;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.singleevent.sdk.ApiList;
import com.singleevent.sdk.App;
import com.singleevent.sdk.Custom_View.Error_Dialog;
import com.singleevent.sdk.Custom_View.Util;
import com.singleevent.sdk.Custom_View.VolleyErrorLis;
import com.singleevent.sdk.View.LeftActivity.AgendaDetails;
import com.singleevent.sdk.health.Adapter.JoinCreateRecyclerViewAdapter;

import com.singleevent.sdk.R;
import com.singleevent.sdk.health.Model.GetChallengeList;
import com.singleevent.sdk.model.AppDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.paperdb.Paper;

public class JoinChallenge extends AppCompatActivity {

    RecyclerView recyclerViewjoin;

    private ArrayList<String>  mDate = new ArrayList<>();
    private ArrayList<Integer>  join = new ArrayList<>();
    private ArrayList<String> mChallengename = new ArrayList<>();
    private ArrayList<String> mChallengenameId = new ArrayList<>();
    private ArrayList<String> mParticipants = new ArrayList<>();
    JoinCreateRecyclerViewAdapter adapterjoin;
    Button btn_create_challenge;
    ArrayList<GetChallengeList> challengeLists =new ArrayList<>();
    AppDetails appDetails;
    List<String > list;
    String[] values;
    int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_challenge);
        appDetails = Paper.book().read("Appdetails");
        recyclerViewjoin = findViewById(R.id.recyclerviewcreate_joinChallenge);
        btn_create_challenge=(Button)findViewById(R.id.btn_create_challenge);
        btn_create_challenge.setBackground(Util.setrounded(Color.parseColor("#ff8c80f8")));
        getChallenge();


        btn_create_challenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getApplicationContext(),CreateChallengeActivity.class);
                startActivity(in);
                finish();
            }
        });

    }

    private void initImages()
    {
         for(int i=0; i<challengeLists.size(); i++){
             if(challengeLists.get(i).getJoinedChallenge()!=null) {
                 values = challengeLists.get(i).getJoinedChallenge().split(",");
                 list = new ArrayList<String>(Arrays.asList(values));


                 if (challengeLists.get(i).getJoinedChallenge() != null && !list.contains(Paper.book().read("userId", ""))) {
                     try {
                         mDate.add(gettime(challengeLists.get(i).getStartDate()) + " - " + gettime(challengeLists.get(i).getEndDate()));
                     } catch (Exception e) {

                     }


                     mChallengename.add(challengeLists.get(i).getTitle());
                     mChallengenameId.add(challengeLists.get(i).getChallenge_id());
                     if (challengeLists.get(i).getJoinedChallenge() != null) {
                         int n = challengeLists.get(i).getJoinedChallenge().split(",").length;
                         mParticipants.add(String.valueOf(n) + " " + "Participants");
                         if (list.contains(Paper.book().read("userId", ""))) {
                             join.add(1);
                         } else {
                             join.add(0);
                         }
                     } else {
                         join.add(0);
                     }
                 }
             }

    else{
                 try {
                     mDate.add(gettime(challengeLists.get(i).getStartDate()) + " - " + gettime(challengeLists.get(i).getEndDate()));
                 } catch (Exception e) {

                 }
                 mChallengename.add(challengeLists.get(i).getTitle());
                 mChallengenameId.add(challengeLists.get(i).getChallenge_id());
                 mParticipants.add("0 Participants");
                 join.add(0);
             }
         }


         if(join.size()>0) {

             for (int i = 0; i < join.size(); i++) {
                 if(join.get(i)==1){
                     count++;
                 }
             }
             Paper.book().write("JoinedChallenge", count);
         }

       /* mDate.add("Jun 15- Jun 30");S
        mChallengename.add("1k steps everyday");
        mParticipants.add("434");

        mDate.add("Jun 15- Jun 30");
        mChallengename.add("10k steps everyday");
        mParticipants.add("534");

        mDate.add("Jun 1- Sept 30");
        mChallengename.add("Choose your option");
        mParticipants.add("150");

        mDate.add("Jan 15- Jun 30");
        mChallengename.add("Healthy eat");
        mParticipants.add("324");

        mDate.add("Feb 5- Jul 30");
        mChallengename.add("Jogging through June - 15+ steps/day");
        mParticipants.add("654");

        mDate.add("Mar 1 - Jun 30");
        mChallengename.add("1k steps everyday + eat healthy");
        mParticipants.add("834");
*/

                initRecyclerView();
    }
    public String gettime(long mill){
        String dateString;

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(mill);
        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
        String days = myFormat.format(cal.getTime());
       int  year = cal.get(Calendar.YEAR);
      int   month = cal.get(Calendar.MONTH);
      int  day = cal.get(Calendar.DAY_OF_MONTH);
      String mo=getmonth(month);
      String temp=mo+" "+day;
        return temp;
    }
public String getmonth(int n){
        ArrayList<String> monthArray=new ArrayList<>();
    monthArray.add("Jan");
    monthArray.add("Feb");
    monthArray.add("Mar");
    monthArray.add("Apr");
    monthArray.add("May");
    monthArray.add("Jun");
    monthArray.add("Jul");
    monthArray.add("Aug");
    monthArray.add("Sep");
    monthArray.add("Oct");
    monthArray.add("Nov");
    monthArray.add("Dec");
    return monthArray.get(n);
}
    public void getChallenge() {

        String tag_string_req = "Checking Update";
        String url ="https://api2.webmobi.com/health/getallchallenges?app_id="+appDetails.getAppId()+"&user_id="+Paper.book().read("userId","");
        System.out.println(url);
        final ProgressDialog pDialog = new ProgressDialog(JoinChallenge.this, R.style.MyAlertDialogStyle);
        pDialog.setMessage("Checking ...");
        pDialog.show();

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, new JSONObject(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Gson gson = new Gson();
                    pDialog.dismiss();
                    System.out.println("JSON Response " + response);
                    if (response.getBoolean("response")) {
                        JSONArray jsonArray = response.getJSONArray("challenges");
                        challengeLists.clear();
                        for(int i=0; i<jsonArray.length(); i++)
                        {
                            String temp = jsonArray.getJSONObject(i).toString();
                            GetChallengeList obj = gson.fromJson(temp, GetChallengeList.class);
                            challengeLists.add(obj);
                        }

                        Paper.book().write("challengelist",challengeLists);
                        initImages();

                    }
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                if (error instanceof TimeoutError) {
                    Error_Dialog.show("Timeout", JoinChallenge.this);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, JoinChallenge.this), JoinChallenge.this);
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show("Please Check Your Internet Connection", JoinChallenge.this);
                }

            }

        }){

        };


        App.getInstance().addToRequestQueue(jsonRequest, tag_string_req);
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
    }
    private void initRecyclerView()
    {
        adapterjoin = new JoinCreateRecyclerViewAdapter(mDate,mChallengename,mParticipants,join,mChallengenameId,this);
        recyclerViewjoin.setAdapter(adapterjoin);
        recyclerViewjoin.setLayoutManager(new LinearLayoutManager(this));
        adapterjoin.notifyDataSetChanged();
    }



}
