package com.singleevent.sdk.View.LeftActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.SpannableString;
import android.view.View;
import android.widget.FrameLayout;

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
import com.singleevent.sdk.Custom_View.Util;
import com.singleevent.sdk.Custom_View.VolleyErrorLis;
import com.singleevent.sdk.model.AppDetails;
import com.singleevent.sdk.model.LeaderBoard.LeaderBoard;
import com.singleevent.sdk.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.paperdb.Paper;

public class LMainActivity  extends AppCompatActivity implements View.OnClickListener {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    FrameLayout container;
    AppDetails appDetails;
    ArrayList<String> catlist=new ArrayList<String>();
    String appid,userid;
    Toolbar toolbarlead;
    String checkValue;
    private ArrayList<LeaderBoard> totalleader,loginuser;
    ViewPagerAdapter adapter;
    private ViewPagerAdapter mSectionsPageAdapter;
    private ArrayList<LeaderBoard> leaderBoardArrayList1,listexpectLoginUser1;
    private static final String TAG = LMainActivity.class.getSimpleName();





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.l_main_activity);
        mSectionsPageAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        appDetails = Paper.book().read("Appdetails");
        checkValue = Paper.book(appDetails.getAppId()).read("Gamification");
        leaderBoardArrayList1=new ArrayList<LeaderBoard>();
        listexpectLoginUser1=new ArrayList<LeaderBoard>();

//         getLeaders();
//         getLeaderboardDetails();
        GetCategory();

        appid=appDetails.getAppId();
        userid=Paper.book().read("userId", "");
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager =(ViewPager)findViewById(R.id.viewPager);
        toolbarlead=(Toolbar)findViewById(R.id.toolbarlead);
        toolbarlead.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));
        toolbarlead.setTitle(Util.applyFontToMenuItem(LMainActivity.this,
                new SpannableString("WE")));
        toolbarlead.setOnClickListener(this);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        GlobalFragment fragment=new GlobalFragment();
       /* Bundle gf = new Bundle();
        gf.putString("name", "Danny");
        gf.putInt("score", 200);
        gf.putString("image","https://www.facebook.com");
     //   gf.putSerializable("Agendaview", agendaview);
        fragment.setArguments(gf);
        FragmentTransaction agendaTrans = getSupportFragmentManager().beginTransaction();
        agendaTrans.replace(R.id.realTabContent, fragment, "GlobalFragment");
        agendaTrans.commit();*/


      /*  adapter.AddFragment(new GlobalFragment(),"Global");
        adapter.AddFragment(new MarketFragment(),"Marketing");
        adapter.AddFragment(new DesignFragment(),"Designing");
        adapter.AddFragment(new DevelopFragment(),"Development");*/




        // Create an initial view to display; must be a subclass of FrameLayout.



    }

    private void GetCategory() {


        final ProgressDialog pDialog = new ProgressDialog(LMainActivity.this, R.style.MyAlertDialogStyle);
        pDialog.setMessage("Loading ...");
        pDialog.show();


        String tag_string_req = "Login";
        String url = ApiList.GetCategory + appDetails.getAppId()+ "&userid=" +Paper.book().read("userId", "");
        StringRequest strReq = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                pDialog.dismiss();
                System.out.println(response);
                Gson gson = new Gson();

                JSONObject jObj = null;
                try {
                    jObj = new JSONObject(response);
                    if (jObj.getBoolean("response")) {
                        JSONArray cat = jObj.getJSONArray("data");
                        for(int i=0; i<cat.length(); i++){
                            if(i==0){
                                catlist.add("All");
                            }
                            catlist.add(cat.getJSONObject(i).getString("user_category"));
                        }
                        Paper.book().write("USERCATEGORY",catlist);
                        Bundle b;
                        if(catlist.size()>0)
                        {   for(int j=0; j<catlist.size(); j++){
                            GlobalFragment gb=new GlobalFragment();
                            b=new Bundle();
                            b.putString("ucat",catlist.get(j));
                            gb.setArguments(b);
                            adapter.addFragment(gb,catlist.get(j));
                            viewPager.setAdapter(adapter);
                            tabLayout.setupWithViewPager(viewPager);
                        }
                        }
                        // viewPager.setAdapter(adapter);
                        //   tabLayout.setupWithViewPager(viewPager);

                    } else {
                        if (jObj.getString("responseString").equalsIgnoreCase("Invalid token")) {
                            Intent i = new Intent();
                            i.putExtra("keyMessage", "Session Expired, Please Login ");
                            i.putExtra("keyAlert", "Session Expired");
                            i.setClassName(LMainActivity.this.getPackageName(), "com.singleevent.sdk.View.TokenExpireAlertReceived");
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                        } else
                            Error_Dialog.show(jObj.getString("responseString"), LMainActivity.this);
                    }
                } catch (JSONException e) {


                }


            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                pDialog.dismiss();
                if (error instanceof TimeoutError) {
                    Error_Dialog.show("Timeout", LMainActivity.this);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, LMainActivity.this), LMainActivity.this);
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show("Please Check Your Internet Connection", LMainActivity.this);
                }
            }
        }) {


        };


        App.getInstance().addToRequestQueue(strReq, tag_string_req);
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
    }


    /* private void getLeaders() {

         final ProgressDialog pDialog = new ProgressDialog(LMainActivity.this,
                 R.style.MyAlertDialogStyle );
         pDialog.setMessage("Please wait...");
         pDialog.show();


         String tag_string_req = "getLeaders";
         String url = ApiList.Get_Leader_board + "userid=1324565432"+"&checkvalue=gamification1"+"&appid=5b8e42513f8b9d02c750ff469c916d0f14b8";
         StringRequest strReq = new StringRequest(Request.Method.GET,
                 url, new Response.Listener<String>() {


             @Override
             public void onResponse(String response) {
                 pDialog.dismiss();


                 Gson gson = new Gson();

                 JSONObject jObj = null;
                 try {
                     jObj = new JSONObject(response);
                     if (jObj.getBoolean("response")) {
                         JSONArray userList = jObj.getJSONArray("users");
                         leaderBoardArrayList1.clear();
                         listexpectLoginUser1.clear();
                         for (int count=0;count< userList.length();count++ ){

                             String userdetails = userList.getJSONObject(count).toString();
                             LeaderBoard user_details = gson.fromJson(userdetails, LeaderBoard.class);
                             leaderBoardArrayList1.add(user_details);


                         }

                         Paper.book().write("totalleaderdetail",leaderBoardArrayList1);

                        *//* for (int count =0;count<leaderBoardArrayList.size();count++){
                            if (!leaderBoardArrayList.get(count).getEmail().toLowerCase().equals(Email.toLowerCase())){
                                listexpectLoginUser.add(leaderBoardArrayList.get(count));

                            }else {
                                //Setting lead points for logged-in user
                                user_leadcount.setText( String.valueOf(leaderBoardArrayList.get(count).getTotal_points()) );
                                Paper.book(appDetails.getAppId()).write("AuthUserTotalPoints",String.valueOf(leaderBoardArrayList.get(count).getTotal_points()));
                                if (Paper.book(appDetails.getAppId()).read("admin_flag","none").equals("admin"))
                                    user_leadcount.setVisibility(View.INVISIBLE);
                                else {
                                    user_leadcount.setVisibility(View.VISIBLE);
                                }

                                if (leaderBoardArrayList.get(count).getTotal_points()>0 ){

                                }
                            }
                        }*//*
     *//* adapter = new LeaderBoardAdapter(context,listexpectLoginUser,CellWidth);
                        recyclerView.setAdapter(adapter);

                        sortByLeads();
                        Paper.book(appDetails.getAppId()).write("OfflineLeaderBoardUserList",listexpectLoginUser);
                        if (listexpectLoginUser.size() > 0){
                            tv_no_items.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                        }else{
                            tv_no_items.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        }*//*


                    } else {
                        Error_Dialog.show(jObj.getString("responseString"),
                                LMainActivity.this);
                    }
                } catch (JSONException e) {
                    Log.e(TAG,e.toString());

                }


            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                pDialog.dismiss();

                if (error instanceof TimeoutError) {
                    Error_Dialog.show("Timeout", LMainActivity.this);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error,
                            LMainActivity.this), LMainActivity.this);
                } else if (VolleyErrorLis.isNetworkProblem(error)) {

                    Error_Dialog.show("Please Check Your Internet Connection", LMainActivity.this);
                  *//* // view1.setVisibility(View.VISIBLE);
                    listexpectLoginUser.clear();
                    user_leadcount.setText(Paper.book(appDetails.getAppId()).read("AuthUserTotalPoints",""));

                    listexpectLoginUser = Paper.book(appDetails.getAppId()).read("OfflineLeaderBoardUserList",
                            new ArrayList<LeaderBoard>());
                    if (listexpectLoginUser.size() > 0){
                        tv_no_items.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }else{
                        tv_no_items.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    }
                    adapter = new LeaderBoardAdapter(context,listexpectLoginUser,CellWidth);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();*//*
                }
            }
        });


        App.getInstance().addToRequestQueue(strReq, tag_string_req);
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
    }*/
    @Override
    public void onClick(View v){
        if(v.getId()==R.id.toolbarlead) {
            onBackPressed();
        }

    }


}




