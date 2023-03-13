package com.singleevent.sdk.View.LeftActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.singleevent.sdk.ApiList;
import com.singleevent.sdk.App;
import com.singleevent.sdk.Custom_View.Error_Dialog;
import com.singleevent.sdk.Custom_View.VolleyErrorLis;
import com.singleevent.sdk.model.AppDetails;
import com.singleevent.sdk.model.LeaderBoard.LeaderBoard;
import com.singleevent.sdk.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class GlobalFragment extends Fragment implements  View.OnClickListener{

    private ArrayList<String> rankimage = new ArrayList<>();
    private ArrayList<String> rank = new ArrayList<>();
    private ArrayList<String> rankname = new ArrayList<>();
    private ArrayList<String> rankscore = new ArrayList<>();
    private ArrayList<LeaderBoard> totalleader;
    TextView name, name1, name2, score, score1, score2;
    ImageView image_view, image_view1, image_view2;
    AppDetails appDetails;
    ArrayList<LeaderBoard> fl=new ArrayList<>();
    ArrayList<String> uprofile = new ArrayList<>();
    private ArrayList<LeaderBoard> dataleader;
    ArrayList<String> ufname = new ArrayList<>();
    ArrayList<String> ulname = new ArrayList<>();
    ArrayList<String> utotalscore = new ArrayList<>();
    RecyclerView recyclerView;
    String checkValue,usercat;
    String temp;
    /*
        private ArrayList<TimeLineModel> mDataList = new ArrayList<>();
    */
    CircleImageView image, image1, image2;


    RelativeLayout relative_rank1, relative_rank2, relative_rank3;

    public GlobalFragment() {

    }


    public static GlobalFragment newInstance(String param1, String param2) {
        GlobalFragment fragment = new GlobalFragment();
        Bundle args = new Bundle();


        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        appDetails = Paper.book().read("Appdetails");
        checkValue = Paper.book(appDetails.getAppId()).read("Gamification");

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            temp = bundle.getString("ucat","global" );
        }

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_global, container, false);
        relative_rank1 = (RelativeLayout) v.findViewById(R.id.relative_rank1);
        relative_rank2 = (RelativeLayout) v.findViewById(R.id.relative_rank2);
        relative_rank3 = (RelativeLayout) v.findViewById(R.id.relative_rank3);
        image = (CircleImageView) v.findViewById(R.id.image_view);
        image1 = (CircleImageView) v.findViewById(R.id.image_view1);
        image2 = (CircleImageView) v.findViewById(R.id.image_view2);
        name = (TextView) v.findViewById(R.id.name);
        name1 = (TextView) v.findViewById(R.id.name1);
        name2 = (TextView) v.findViewById(R.id.name2);
        score = (TextView) v.findViewById(R.id.score);
        score1 = (TextView) v.findViewById(R.id.score1);
        score2 = (TextView) v.findViewById(R.id.score2);
        totalleader = new ArrayList<>();
        image.setOnClickListener(this);
        image1.setOnClickListener(this);
        image2.setOnClickListener(this);
        usercat=Paper.book().read("Usercat","global");

        getLeaderboardDetails();


        // lastTime();

      /*  image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), Scoredetail.class);
                startActivity(i);


            }
        });*/
        image1 = (CircleImageView) v.findViewById(R.id.image_view1);
       /* image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), Scoredetail.class);
                startActivity(i);
            }
        });*/
        image2 = (CircleImageView) v.findViewById(R.id.image_view2);
       /* image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), Scoredetail.class);
                startActivity(i);
            }
        });*/
        recyclerView = v.findViewById(R.id.ranking);

      /* RecyclerView recyclerView = v.findViewById(R.id.ranking);
        ArrayList<LeaderBoard> al1=new ArrayList<>();
        al1=Paper.book().read("templead");
        RankingViewAdapter cadapter = new RankingViewAdapter( al1,getContext());
       // RankingViewAdapter cadapter = new RankingViewAdapter( rankimage,rank,rankname,rankscore,getContext());
        recyclerView.setAdapter(cadapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
*/

        return v;
    }
/*
public void lastTime(){
     rankimage.add("https://webmobi.s3.amazonaws.com/nativeapps/user/profile_pic_eventuserd16377f0673deb9db693a990a928b85d.png?dt=1569419796615");
     rankname.add("JOHN");
     rankscore.add("100");
     rank.add("#4");
    rankimage.add("https://webmobi.s3.amazonaws.com/nativeapps/user/profile_pic_eventuserd16377f0673deb9db693a990a928b85d.png?dt=1569419796615");
    rankname.add("moHN");
    rankscore.add("100");
    rank.add("#5");
    rankimage.add("https://webmobi.s3.amazonaws.com/nativeapps/user/profile_pic_eventuserd16377f0673deb9db693a990a928b85d.png?dt=1569419796615");
    rankname.add("JggN");
    rankscore.add("100");
    rank.add("#6");
}
*/

    public void getLeaderboardDetails() {
        final ProgressDialog pDialog = new ProgressDialog(getContext(), R.style.MyAlertDialogStyle);
        pDialog.setMessage("Loading  ...");
        pDialog.show();
        String all="All";
        String url="";
        if(temp.equals(all)){
            url = ApiList.GetLeaderDetails + appDetails.getAppId() + "&userid="+Paper.book().read("userId",
                    "") + "&checkvalue="+checkValue ;
        }else {
            url = ApiList.GetLeaderDetails + appDetails.getAppId() + "&userid="+Paper.book().read("userId",
                    "") + "&checkvalue="+checkValue+"&user_category="+temp ;
        }

        String tag_string_req = "Lead";
//        url = ApiList.GetLeaderDetails + appDetails.getAppId() + "&userid="+Paper.book().read("userId",
//                "") + "&checkvalue="+checkValue ;
//        +"&user_category="+temp
        // String url="http://api.webmobi.com/api/event/leaderboard?appid=5b8e42513f8b9d02c750ff469c916d0f14b8&userid=1324565432&checkvalue=gamification1";

        StringRequest strReq = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                pDialog.dismiss();
                System.out.println(response);
                Gson gson = new Gson();
                JSONArray udet;
                ArrayList<LeaderBoard> al = new ArrayList<>();


                JSONObject jObj = null;
                try {
                    jObj = new JSONObject(response);
                    if (jObj.getBoolean("response")) {
                        JSONArray userList = jObj.getJSONArray("users");
                        al.clear();
                        for (int i = 0; i < userList.length(); i++) {
                            String userdetails = userList.getJSONObject(i).toString();
                            LeaderBoard user_details = gson.fromJson(userdetails, LeaderBoard.class);
                            al.add(user_details);
                        }
                        String s=temp+"templead";
                        Paper.book().write(s, al);
                        ArrayList<LeaderBoard> al1 = new ArrayList<>();
                        al1 = Paper.book().read(s);
                        FirstThree();

                        RankingViewAdapter cadapter = new RankingViewAdapter(al1, getActivity().getApplicationContext(),temp);

                        // RankingViewAdapter cadapter = new RankingViewAdapter( rankimage,rank,rankname,rankscore,getContext());
                        recyclerView.setAdapter(cadapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


                    } else {
                        if (jObj.getString("responseString").equalsIgnoreCase("Invalid token")) {
                            Intent i = new Intent();
                            i.putExtra("keyMessage", "Session Expired, Please Login ");
                            i.putExtra("keyAlert", "Session Expired");
                            i.setClassName(getContext(), "com.singleevent.sdk.View.TokenExpireAlertReceived");
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                        } else
                            Error_Dialog.show(jObj.getString("responseString"), getActivity());
                    }
                } catch (JSONException e) {


                }


            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                pDialog.dismiss();
                if (error instanceof TimeoutError) {
                    Error_Dialog.show("Timeout", getActivity());
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, getContext()), getActivity());
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show("Please Check Your Internet Connection", getActivity());
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

    public void FirstThree()

    {
        try {
            String s = temp + "templead";
            fl = Paper.book().read(s);
            int n = fl.size();
            if (n >= 3) {
                for (int i = 0; i < 3; i++) {
                    if (i == 0) {
                        name.setText(fl.get(i).getFirst_name() + " " + fl.get(i).getLast_name());
                        score.setText(String.valueOf(fl.get(i).getTotal_points()));
                        if (!fl.get(i).getProfile_pic().equalsIgnoreCase("")) {
                            Glide.with(getActivity().getApplicationContext())

                                    .load(fl.get(i).getProfile_pic().equalsIgnoreCase("") ? R.drawable.round_user : fl.get(i).getProfile_pic())
                                    .asBitmap()
                                    .placeholder(R.drawable.round_user)
                                    .error(R.drawable.round_user)
                                    .into(image);
                        }
                    }
                    if (i == 1) {
                        name1.setText(fl.get(i).getFirst_name() + " " + fl.get(i).getLast_name());
                        score1.setText(String.valueOf(fl.get(i).getTotal_points()));
                        if (!fl.get(i).getProfile_pic().equalsIgnoreCase("")) {
                            Glide.with(getContext())

                                    .load(fl.get(i).getProfile_pic().equalsIgnoreCase("") ? R.drawable.round_user : fl.get(i).getProfile_pic())
                                    .asBitmap()
                                    .placeholder(R.drawable.round_user)
                                    .error(R.drawable.round_user)
                                    .into(image1);
                        }
                    }
                    if (i == 2) {
                        name2.setText(fl.get(i).getFirst_name() + " " + fl.get(i).getLast_name());
                        score2.setText(String.valueOf(fl.get(i).getTotal_points()));
                        if (!fl.get(i).getProfile_pic().equalsIgnoreCase("")) {
                            Glide.with(getContext())

                                    .load(fl.get(i).getProfile_pic().equalsIgnoreCase("") ? R.drawable.round_user : fl.get(i).getProfile_pic())

                                    .asBitmap()
                                    .placeholder(R.drawable.round_user)
                                    .error(R.drawable.round_user)
                                    .into(image2);
                        }
                    }

                }

            } else if (n == 2) {

                for (int i = 0; i < 2; i++) {
                    if (i == 0) {
                        name.setText(fl.get(i).getFirst_name() + " " + fl.get(i).getLast_name());
                        score.setText(String.valueOf(fl.get(i).getTotal_points()));
                        if (!fl.get(i).getProfile_pic().equalsIgnoreCase("")) {
                            Glide.with(getContext())

                                    .load(fl.get(i).getProfile_pic().equalsIgnoreCase("") ? R.drawable.round_user : fl.get(i).getProfile_pic())
                                    .asBitmap()
                                    .placeholder(R.drawable.round_user)
                                    .error(R.drawable.round_user)
                                    .into(image);
                        }
                    }
                    if (i == 1) {
                        name1.setText(fl.get(i).getFirst_name() + " " + fl.get(i).getLast_name());
                        score1.setText(String.valueOf(fl.get(i).getTotal_points()));
                        if (!fl.get(i).getProfile_pic().equalsIgnoreCase("")) {
                            Glide.with(getContext())

                                    .load(fl.get(i).getProfile_pic().equalsIgnoreCase("") ? R.drawable.round_user : fl.get(i).getProfile_pic())
                                    .asBitmap()
                                    .placeholder(R.drawable.round_user)
                                    .error(R.drawable.round_user)
                                    .into(image1);
                        }
                    }

                }
                relative_rank3.setVisibility(View.GONE);
            } else if (n == 1) {

                for (int i = 0; i < 2; i++) {
                    if (i == 0) {
                        name.setText(fl.get(i).getFirst_name() + " " + fl.get(i).getLast_name());
                        score.setText(String.valueOf(fl.get(i).getTotal_points()));
                        if (!fl.get(i).getProfile_pic().equalsIgnoreCase("")) {
                            Glide.with(getContext())

                                    .load(fl.get(i).getProfile_pic().equalsIgnoreCase("") ? R.drawable.round_user : fl.get(i).getProfile_pic())
                                    .asBitmap()
                                    .placeholder(R.drawable.round_user)
                                    .error(R.drawable.round_user)
                                    .into(image);
                        }
                    }
                }
                relative_rank2.setVisibility(View.GONE);
                relative_rank3.setVisibility(View.GONE);
            } else {
                relative_rank1.setVisibility(View.GONE);
                relative_rank2.setVisibility(View.GONE);
                relative_rank3.setVisibility(View.GONE);
            }
        }catch (Exception E){

        }

    }

    @Override
    public void onClick(View v) {
        try {
            if (fl.get(0).getUserid().equalsIgnoreCase(Paper.book().read("userId", ""))) {
                if (v.getId() == image.getId()) {
                    Intent i = new Intent(getActivity(), LeadScore.class);
                    i.putExtra("position", 0);
                    i.putExtra("cat", temp);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                }
            }
            if (fl.get(1).getUserid().equalsIgnoreCase(Paper.book().read("userId", ""))) {
                if (v.getId() == image1.getId()) {
                    Intent i = new Intent(getActivity(), LeadScore.class);
                    i.putExtra("position", 1);
                    i.putExtra("cat", temp);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                }
            }
            if (fl.get(2).getUserid().equalsIgnoreCase(Paper.book().read("userId", ""))) {
                if (v.getId() == image2.getId()) {
                    Intent i = new Intent(getActivity(), LeadScore.class);
                    i.putExtra("position", 2);
                    i.putExtra("cat", temp);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                }
            }
        }catch (Exception e){

        }
    }
    }

