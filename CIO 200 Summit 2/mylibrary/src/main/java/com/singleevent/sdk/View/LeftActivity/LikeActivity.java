package com.singleevent.sdk.View.LeftActivity;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

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
import com.singleevent.sdk.Custom_View.VolleyErrorLis;
import com.singleevent.sdk.Left_Adapter.Like_User_Adapter;
import com.singleevent.sdk.model.AppDetails;
import com.singleevent.sdk.model.Likeinfo;
import com.singleevent.sdk.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cyd.awesome.material.AwesomeText;
import io.paperdb.Paper;

public class LikeActivity extends AppCompatActivity implements View.OnClickListener,Like_User_Adapter.OnCardClickListner {
    AppDetails appDetails;
    ArrayList<Likeinfo> feeds=new ArrayList<>();
    ArrayList<Likeinfo> dummy=new ArrayList<>();
    Like_User_Adapter like_user_adapter;
    RecyclerView groupList;
    AwesomeText backward;
    TextView totallikes;
    ProgressBar loading;
    int total;
    int id;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Paper.init(this);
        appDetails = Paper.book().read("Appdetails");
        setContentView(R.layout.likeactivity);
        groupList=(RecyclerView)findViewById(R.id.grouplist);
        backward=(AwesomeText)findViewById(R.id.backward);
        totallikes=(TextView)findViewById(R.id.alll);

        backward.setOnClickListener(this);
        dummy.clear();





        if (getIntent().getExtras() == null)
        {
        }
        else{
            try{
                //  fe=Paper.book().read("likeuser");
                total=getIntent().getExtras().getInt("totallikes");
                id=getIntent().getExtras().getInt("IDs");
                dummy=Paper.book().read("LikeUser"+id);
                // LoadComment(id);
                if(dummy.size()>0){
                    try {

                        totallikes.setText("  "+total);
                        like_user_adapter = new Like_User_Adapter(this, dummy);
                        groupList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,
                                false));

                        groupList.setAdapter(like_user_adapter);
                        like_user_adapter.setOnCardClickListner(this);
                    }

                    catch(Exception e){}
                }
                else{
                    onBackPressed();
                }

            }catch (Exception e)
            {}}



    }
    private void LoadComment(int postid) {

        String tag_string_req = "Get_Feed";
        String url = ApiList.Get_Comments + appDetails.getAppId() + "&post_id=" + postid + "&action=like";

        loading.setVisibility(View.VISIBLE);
        StringRequest strReq = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                loading.setVisibility(View.GONE);
                JSONObject jObj = null;
                try {
                    jObj = new JSONObject(response);
                    if (jObj.getBoolean("response")) {
                        parseresult(jObj,postid);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                loading.setVisibility(View.GONE);
                if (error instanceof TimeoutError) {
                    Error_Dialog.show("Timeout", LikeActivity.this);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, LikeActivity.this), LikeActivity.this);
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show("Please Check Your Internet Connection", LikeActivity.this);
                }

            }
        });


        App.getInstance().addToRequestQueue(strReq, tag_string_req);
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

    }

    private void parseresult(JSONObject args,int pos) {
        int id=pos;
        feeds.clear();
        Gson gson = new Gson();

        try {
            JSONArray jaarray = args.getJSONArray("likes");
            for (int i = 0; i < jaarray.length(); i++) {
                String eventString = jaarray.getJSONObject(i).toString();
                Likeinfo eobj = gson.fromJson(eventString, Likeinfo.class);
                feeds.add(eobj);
            }
            Paper.book().write("LikeUser"+id,feeds);
          /*  if(feeds.size()>0)
            {

                try {

                    totallikes.setText("  "+total);
                    like_user_adapter = new Like_User_Adapter(this, feeds);
                    groupList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,
                            false));

                    groupList.setAdapter(like_user_adapter);
                    like_user_adapter.setOnCardClickListner(this);
                }*/

            // catch(Exception e){}

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (feeds.size() > 0) {
            //adapter.notifyDataSetChanged();
            // addcommentlist(feeds, context, dpWidth);
            //   tcomment.setText("Total Comments - " + feeds.size());
        } //else
        //   tcomment.setText("Comment");


    }

    @Override
    public void OnItemLongClicked(View view, Likeinfo user, int position) {

    }

    @Override
    public void OnItemClick(View view, Likeinfo user, int position) {
        Intent i;
        //i = new Intent(LikeActivity.this, GroupFeedView.class);
        //   i.putExtra("group_id", user.getGroup_id());
        //  i.putExtra("group_name", user.getGroup_name());
        //   startActivity(i);

    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.backward) {
            onBackPressed();
        }

    }

}
