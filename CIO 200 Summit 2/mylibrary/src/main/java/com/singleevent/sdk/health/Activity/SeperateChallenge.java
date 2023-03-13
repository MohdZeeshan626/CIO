package com.singleevent.sdk.health.Activity;

import android.actionsheet.demo.com.khoiron.actionsheetiosforandroid.ActionSheet;
import android.actionsheet.demo.com.khoiron.actionsheetiosforandroid.Interface.ActionSheetCallBack;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Icon;
import android.media.Image;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.makeramen.roundedimageview.RoundedImageView;
import com.singleevent.sdk.ApiList;
import com.singleevent.sdk.App;
import com.singleevent.sdk.Custom_View.ColorFilterTransformation;
import com.singleevent.sdk.Custom_View.Error_Dialog;
import com.singleevent.sdk.Custom_View.Util;
import com.singleevent.sdk.Custom_View.VolleyErrorLis;
import com.singleevent.sdk.R;

import com.singleevent.sdk.View.RightActivity.group_feed.PrivateListAdapter;
import com.singleevent.sdk.View.RightActivity.group_feed.Privategroup;
import com.singleevent.sdk.health.Adapter.CustomAdapter;
import com.singleevent.sdk.health.Adapter.MyAdapter;
import com.singleevent.sdk.health.Adapter.RecyclerViewAdapter;
import com.singleevent.sdk.health.Adapter.RecylerViewActiveChallengeAdapter;
import com.singleevent.sdk.health.Fragment.CompleteFragment;
import com.singleevent.sdk.health.Fragment.RunningFragment;
import com.singleevent.sdk.health.Fragment.UpcomingFragment;
import com.singleevent.sdk.health.Model.ChallengeList;
import com.singleevent.sdk.health.Model.CustomItem;
import com.singleevent.sdk.health.Model.GetChallengeList;
import com.singleevent.sdk.health.Model.LeaderBoardPoints;
import com.singleevent.sdk.model.AppDetails;
import com.singleevent.sdk.model.User;


import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.paperdb.Paper;

import static android.view.View.GONE;
import static java.security.AccessController.getContext;

public class SeperateChallenge extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private ArrayList<String> mImageNames = new ArrayList<>();
    private ArrayList<String> mImages = new ArrayList<>();
    private ArrayList<String> mTotalkm = new ArrayList<>();
    private ArrayList<Integer> mPosition = new ArrayList<>();
    RecyclerViewAdapter adapter;
    RecyclerView recyclerView;
    Spinner spinner,spinmode;
    Button btn;
    RecyclerView activeRecylerView;
    LinearLayout myEvents;
    float ImgWidth, ImgHeight, lwidth, lheight;
    ArrayList<CustomItem> customlist;
    ArrayList<LeaderBoardPoints> leaderBoardPoints=new ArrayList<>();
    List<com.singleevent.sdk.model.User> userview;
    int totalsteps,totaldistance,totaltimes;
    ImageView totalstep,totaltime,totaldist;
    private ArrayList<String> mImage = new ArrayList<>();
    private ArrayList<String> mName = new ArrayList<>();
    private ArrayList<String> mTotal = new ArrayList<>();
    RecylerViewActiveChallengeAdapter recylerViewActiveChallengeAdapter;
    TextView txtActive;
    LinearLayout linearLayout;
    Button btn_create,btn_join,btn_active_option;
    ImageView backpress;
    TextView txtChallenge_name,txtChallenge_dates;
    int pos;
    int totaluser;
    AppDetails appDetails;
    ArrayList<GetChallengeList> getChallengeLists=new ArrayList<>();
    TextView txtChallenge_participants;
    ArrayList<String> joineduser=new ArrayList<>();
    List<String > list;
    String[] values;
    TextView tsteps,tdist,ttime;

    String[] DayOfWeek = {"Average steps", "Total steps", "Average minute",
            "Total minute", "Average distance", "Total distance"};
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seperatechallenge);
     //   recyclerView = (RecyclerView)findViewById(R.id.recyclerview);
    //    btn =(Button)findViewById(R.id.testBtn);
        activeRecylerView = findViewById(R.id.recyclerviewActiveChallenge);
        txtActive= (TextView)findViewById(R.id.txt_active) ;
        linearLayout = (LinearLayout)findViewById(R.id.linearActiveChallenge);
        txtChallenge_name=(TextView) findViewById(R.id.txtChallenge_name);
        txtChallenge_dates=(TextView) findViewById(R.id.txtChallenge_dates);
//        linearLayout.setVisibility(GONE);
        txtActive.setVisibility(GONE);
        btn_active_option = (Button)findViewById(R.id.btn_material_active);
        btn_create =(Button)findViewById(R.id.btncreate_challenge);
        btn_join =(Button)findViewById(R.id.btnjoin_challenge);
        spinner = findViewById(R.id.spinnuser);
        spinmode = findViewById(R.id.spinmode);
        txtChallenge_participants=findViewById(R.id.txtChallenge_participants);
        tsteps=(TextView)findViewById(R.id.ttoallsteps);
        tdist=(TextView)findViewById(R.id.tmiles);
        ttime=(TextView)findViewById(R.id.tmins);

        totalstep=(ImageView)findViewById(R.id.totalstep);
        totaldist=(ImageView)findViewById(R.id.totaldist);
        totaltime=(ImageView)findViewById(R.id.totaltime);
        appDetails = Paper.book().read("Appdetails");
        userview =new ArrayList<>();
        getuser();
      //  userview = Paper.book(appDetails.getAppId()).read("OfflineAttendeeList",new ArrayList<com.singleevent.sdk.model.User>());
       /* if(userview.size()==0){
            getuser();
        }*/


        try {
          //  getChallengeLists= Paper.book().read("challengelist");
           if (getIntent().getExtras() == null)
               finish();
            Bundle bundle = getIntent().getExtras();

            getChallengeLists = (ArrayList<GetChallengeList>) bundle.getSerializable("ChallengeList");
           pos = getIntent().getExtras().getInt("challenge_id_pos");
         //  totaluser=getIntent().getExtras().getInt("Totaluser");
       }catch (Exception e){

       }
       try {
           txtChallenge_name.setText(getChallengeLists.get(pos).getTitle());
           txtChallenge_dates.setText(gettime(getChallengeLists.get(pos).getStartDate()) + " - " + gettime(getChallengeLists.get(pos).getEndDate()));
           if(getChallengeLists.get(pos).getJoinedChallenge()!=null&& !getChallengeLists.get(pos).getJoinedChallenge().equalsIgnoreCase("")){
               joineduser.clear();
              values = getChallengeLists.get(pos).getJoinedChallenge().split(",");
               list = new ArrayList<String>(Arrays.asList(values));
          //     list= (ArrayList<String>) Arrays.asList(getChallengeLists.get(pos).getJoinedChallenge().split(","));

               //txtChallenge_participants.setText(getChallengeLists.get(pos).getJoinedChallenge().split(",").length+" "+"Participants");
           }
       }catch (Exception e){

       }
        txtChallenge_participants.setText(getChallengeLists.get(pos).getTotaljoineduser() +" Participants");
        myEvents = (LinearLayout) findViewById(R.id.llayout1);
        backpress=(ImageView)findViewById(R.id.backpress) ;
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        ImgWidth = displayMetrics.widthPixels * 0.30F;
        ImgHeight = ImgWidth * 1.7F;
        lwidth = ImgWidth * 0.70F;
        lheight = lwidth * 0.75F;

        btn_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getApplicationContext(), JoinChallenge.class);
                startActivity(in);
                finish();
            }
        });

        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent (getApplicationContext(), CreateChallengeActivity.class);
                startActivity(in);
                finish();
            }
        });
        backpress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent i=new Intent(SeperateChallenge.this,ChallengeActivity.class);
               startActivity(i);
               finish();
            }
        });
        ArrayList<String> data = new ArrayList<>();
        ArrayList<Integer>data1=new ArrayList<>();

        data.add("View Challenge Details");
        data.add("Leave Challenge");
        btn_active_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ActionSheet(SeperateChallenge.this,data)
                        .setTitle("Challenge Options")
                        .setCancelTitle("Cancel")
                        .setColorTitle(Color.BLUE)
                        .setColorTitleCancel(Color.RED)
                        .setColorData(Color.BLUE)
                        .create(new ActionSheetCallBack() {
                            @Override
                            public void data(@NotNull String data, int position) {
                                switch (position){
                                    case 0:
                                     Toast.makeText(SeperateChallenge.this,data+position,Toast.LENGTH_SHORT).show();
                                     break;
                                        // your action
                                    case 1:
                                        //Toast.makeText(SeperateChallenge.this,data+position,Toast.LENGTH_SHORT).show();
                                        Intent i=new Intent(SeperateChallenge.this,ChallengeActivity.class);
                                        startActivity(i);
                                        finish();
                                        break;
                                        // your action
                                    case 2:
                                        Toast.makeText(SeperateChallenge.this,data+position,Toast.LENGTH_SHORT).show();
                                        break;
                                        // your action
                                    case 3:
                                        Toast.makeText(SeperateChallenge.this,data+position,Toast.LENGTH_SHORT).show();
                                        break;
                                        // your action
                                }
                            }
                        });
            }
        });








          showpreview();
          getLeaderBoardPoints();
          customlist=getCustomList();


        CustomAdapter customAdapter=new CustomAdapter(this,customlist);
        if(spinmode!=null) {
            spinmode.setAdapter(customAdapter);
            spinmode.setOnItemSelectedListener(this);
        }
    }
public ArrayList<CustomItem> getCustomList(){
        customlist=new ArrayList<>();

    customlist.add(new CustomItem("Total steps",R.drawable.steps));
    customlist.add(new CustomItem("Average steps",R.drawable.steps));
    customlist.add(new CustomItem("Total minutes",R.drawable.steps));
    customlist.add(new CustomItem("Average minutes",R.drawable.steps));
    customlist.add(new CustomItem("Total distance",R.drawable.steps));
    customlist.add(new CustomItem("Average steps",R.drawable.steps));
return customlist;

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

    public void shoepopup(View v){
        PopupMenu menu = new PopupMenu(getApplicationContext(), v);
        menu.inflate(R.menu.challenge);
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {


                public boolean onMenuItemClick(MenuItem item){
                    int i = item.getItemId();
                    if (i == R.id.menu_leave) {
                    Toast.makeText(getApplicationContext(),"Ok",Toast.LENGTH_SHORT).show();
                }
                    return false;
                }});




    }
    private void showpreview(){
        int clogo_height = 0, margintop = 0;
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for (int i = 0; i < 3; i++) {
            final View child = inflater.inflate(R.layout.events_view_my, null);


            RelativeLayout v2 = (RelativeLayout) child.findViewById(R.id.v2);
            RelativeLayout.LayoutParams logolayoutParams = (RelativeLayout.LayoutParams) v2.getLayoutParams();
            logolayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;//(int) (ImgWidth * 1.30);
            logolayoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            logolayoutParams.setMargins(10, 0, 0, 0);
            v2.setLayoutParams(logolayoutParams);

            clogo_height = (int) (ImgHeight * 0.9);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams((int) (ImgWidth * 2), clogo_height);

            //used to set the center logo of the tile
            com.makeramen.roundedimageview.RoundedImageView clogo_center = (com.makeramen.roundedimageview.RoundedImageView) child.findViewById(R.id.logo2);
            //used to set the tile banner image
            com.makeramen.roundedimageview.RoundedImageView clogo = (RoundedImageView) child.findViewById(R.id.re_tile_logo);

            layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

            //setting tile banner
            if(i==0 ||i==2){
            Glide.with(getApplicationContext()).load(R.drawable.fitnesschallenge)
                    .fitCenter()
                    .placeholder(R.drawable.stepcount)
                    .dontAnimate()
                    .error(R.drawable.stepcount)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .bitmapTransform(new ColorFilterTransformation(getApplicationContext(), Color.argb(0, 0, 0, 0)))
                    .into(clogo);}
            if(i==1){
                Glide.with(getApplicationContext()).load(R.drawable.fitc)
                        .fitCenter()
                        .placeholder(R.drawable.stepcount)
                        .dontAnimate()
                        .error(R.drawable.stepcount)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .bitmapTransform(new ColorFilterTransformation(getApplicationContext(), Color.argb(0, 0, 0, 0)))
                        .into(clogo);
            }

            clogo.setCornerRadius(7, 7, 0, 0);
            clogo.setLayoutParams(layoutParams);
            ImageView logo2 = (ImageView) child.findViewById(R.id.logo2);
            RelativeLayout logo2_layout = (RelativeLayout) child.findViewById(R.id.logo2_layout);
            if (this != null) {
                margintop = (int) (clogo_height - (getResources().getDimensionPixelSize(R.dimen.abc_action_bar_default_height_material) / 2));
            }
            RelativeLayout.LayoutParams logo2_layout_params = new RelativeLayout.LayoutParams((int) (ImgWidth * 0.45), (int) (ImgWidth * 0.45));
            logo2_layout_params.setMargins(0, margintop, 0, 0);
            logo2_layout_params.addRule(RelativeLayout.CENTER_HORIZONTAL);
            logo2_layout.setLayoutParams(logo2_layout_params);

            clogo_center.setLayoutParams(new RelativeLayout.LayoutParams((int) (ImgWidth * 0.45), (int) (ImgWidth * 0.45)));

            //setting tile center logo
            Glide.with(getApplicationContext()).load((R.drawable.stepcount))
                    .fitCenter()
                    .placeholder(R.drawable.stepcount)
                    .dontAnimate()
                    .error(R.drawable.stepcount)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .bitmapTransform(new ColorFilterTransformation(getApplicationContext(), Color.argb(0, 0, 0, 0)))
                    .into(clogo_center);
            TextView etitle = (TextView) child.findViewById(R.id.eventname);
            // TextView ecat = (TextView) child.findViewById(R.id.eventcat);
            // TextView edate = (TextView) child.findViewById(R.id.eventdate);
            Button challengecomplete=(Button)child.findViewById(R.id.challengecomplete);
            challengecomplete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent I=new Intent(SeperateChallenge.this,ChallengeActivity.class);
                    startActivity(I);
                    finish();
                }
            });


            View split_View = (View) child.findViewById(R.id.split_view);
            RelativeLayout.LayoutParams split_view_params = (RelativeLayout.LayoutParams) split_View.getLayoutParams();
            split_view_params.height = (int) (ImgWidth * 2);
            split_View.setLayoutParams(split_view_params);

            //setting event title
            if(i==0){
            etitle.setText("Health challenge");}
            else if(i==1){
            etitle.setText("An Hour of Walking");}
            else{
                etitle.setText("5 min of Meditations");}

            //listing.getApp_title());
            //setting event category
            //    ecat.setText(listing.getApp_category());

            etitle.setTypeface(Util.boldtypeface(getApplicationContext()));
            //  ecat.setTypeface(Util.lighttypeface(context));

            // String Startsplits = listing.getStart_date().split("T")[0];

            /// SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            // SimpleDateFormat formatter1 = new SimpleDateFormat("EEE MMM dd/yyyy");
            //   try {
            //   Date date = formatter.parse(Startsplits);
            //setting event date
            //    edate.setText(formatter1.format(date));
            //  } catch (ParseException e) {
            //      e.printStackTrace();
            // }
              child.setId(i);
             child.setTag(i);

            child.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = (int) view.getTag();

                    Toast.makeText(getApplicationContext(), "Information not available", Toast.LENGTH_LONG).show();

                }
            });


            myEvents.addView(child);
            myEvents.setVisibility(View.VISIBLE);

        }
    }

    private void initImages()
    {
        List<String> listing = new ArrayList<String>();


        if(getChallengeLists.get(pos).getJoinedChallenge()!=null && list!=null){
            for(int i=0; i<userview.size(); i++){
                for(int j=0; j<list.size(); j++) {
                   try {
                       if (userview.get(i).getUserid().toString().equalsIgnoreCase(list.get(j).toString())) {
                           listing.add(userview.get(i).getFirst_name()+" "+userview.get(i).getLast_name());
                           mImages.add(userview.get(i).getProfile_pic());
                           mImageNames.add(userview.get(i).getFirst_name()+" "+userview.get(i).getLast_name());

                        //   mTotalkm.add(String.valueOf(leaderBoardPoints.get(i).getSteps()));
                           for(int k=0; k<leaderBoardPoints.size(); k++){

                               if(leaderBoardPoints.get(k).getUser_id().equalsIgnoreCase(list.get(j)))
                               {
                                   mTotalkm.add(String.valueOf(leaderBoardPoints.get(k).getSteps()));
                                   totalsteps=leaderBoardPoints.get(k).getSteps();
                                   totaldistance+=(int)Math.round(leaderBoardPoints.get(k).getDistance());
                                   totaltimes+=leaderBoardPoints.get(k).getTime();
                               }

                           }
                        //  mTotalkm.add("22 KM");
                           mPosition.add((R.drawable.medfirst));
                       }
                   }catch (Exception e){}
                }
                if(totalsteps>0){
                    totalstep.setVisibility(View.VISIBLE);
                    tsteps.setVisibility(View.VISIBLE);
                    tsteps.setText(String.valueOf(totalsteps));
                }else{
                    tsteps.setVisibility(GONE);
                    totalstep.setVisibility(GONE);
                }
                if(totaldistance>0){
                    totaldist.setVisibility(View.VISIBLE);
                    tdist.setVisibility(View.VISIBLE);
                    tdist.setText(String.valueOf(totaldistance));
                }else{
                    tdist.setVisibility(GONE);
                    totaldist.setVisibility(GONE);
                }
                if(totaltimes>0){
                    totaltime.setVisibility(View.VISIBLE);
                    ttime.setVisibility(View.VISIBLE);
                    ttime.setText(String.valueOf(totaltimes));
                }else{
                    ttime.setVisibility(GONE);
                    totaltime.setVisibility(GONE);
                }
            }
        }
       if(list==null){
           listing.add("Not available");
       }
       else {
           ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                   android.R.layout.simple_spinner_item, listing);
           dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
           spinner.setAdapter(dataAdapter);
           spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
               @Override
               public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                   ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                   spinner.setSelection(position);
               }
               @Override
               public void onNothingSelected(AdapterView<?> parent) {

               }
           });
       }
      /*  mImages.add("https://pixinvent.com/materialize-material-design-admin-template/app-assets/images/user/12.jpg");
        mImageNames.add("Amanda");
        mTotalkm.add("75 KM");
        mPosition.add((R.drawable.medfirst));

        mImages.add("https://pixinvent.com/materialize-material-design-admin-template/app-assets/images/user/12.jpg");
        mImageNames.add("Amanda");
        mTotalkm.add("75 KM");
        mPosition.add((R.drawable.medsecond));

        mImages.add("https://pixinvent.com/materialize-material-design-admin-template/app-assets/images/user/12.jpg");
        mImageNames.add("Amanda");
        mTotalkm.add("75 KM");
        mPosition.add((R.drawable.medthird));

        mImages.add("https://pixinvent.com/materialize-material-design-admin-template/app-assets/images/user/12.jpg");
        mImageNames.add("Amanda");`
        mTotalkm.add("75 KM");
        mPosition.add((R.drawable.medfirst));

        mImages.add("https://pixinvent.com/materialize-material-design-admin-template/app-assets/images/user/12.jpg");
        mImageNames.add("Amanda");
        mTotalkm.add("75 KM");
        mPosition.add((R.drawable.medsecond));

        mImages.add("https://pixinvent.com/materialize-material-design-admin-template/app-assets/images/user/12.jpg");
        mImageNames.add("Amanda");
        mTotalkm.add("75 KM");
        mPosition.add((R.drawable.medthird));

        mImages.add("https://pixinvent.com/materialize-material-design-admin-template/app-assets/images/user/12.jpg");
        mImageNames.add("Amanda");
        mTotalkm.add("75 KM");
        mPosition.add((R.drawable.medsecond));

        mImages.add("https://pixinvent.com/materialize-material-design-admin-template/app-assets/images/user/12.jpg");
        mImageNames.add("Amanda");
        mTotalkm.add("75 KM");
        mPosition.add(R.drawable.medthird);

        mImages.add("https://pixinvent.com/materialize-material-design-admin-template/app-assets/images/user/12.jpg");
        mImageNames.add("Amanda");
        mTotalkm.add("75 KM");
        mPosition.add(R.drawable.medfirst);

        mImages.add("https://pixinvent.com/materialize-material-design-admin-template/app-assets/images/user/12.jpg");
        mImageNames.add("Amanda");
        mTotalkm.add("75 KM");
        mPosition.add(R.drawable.medsecond);*/
        initRecyclerView();
    }
    private void initRecyclerView()
    {
       /* adapter = new RecyclerViewAdapter(mImageNames,mImages,mTotalkm,mPosition,this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.notifyDataSetChanged();*/
        System.out.println("calling function initRecycler");
        adapter = new RecyclerViewAdapter(mImageNames,mImages,mTotalkm,mPosition,getApplicationContext());
        activeRecylerView.setAdapter(adapter);
        activeRecylerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void getLeaderBoardPoints() {

        String tag_string_req = "Checking Update";
        String url ="https://api2.webmobi.com/health/getleaderboard?app_id="+appDetails.getAppId()+"&challenge_id="+getChallengeLists.get(pos).getChallenge_id();
        System.out.println(url);
        final ProgressDialog pDialog = new ProgressDialog(SeperateChallenge.this, R.style.MyAlertDialogStyle);
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
                        JSONArray jsonArray = response.getJSONArray("data");
                        leaderBoardPoints.clear();
                        for(int i=0; i<jsonArray.length(); i++)
                        {
                            String temp = jsonArray.getJSONObject(i).toString();
                            LeaderBoardPoints obj = gson.fromJson(temp, LeaderBoardPoints.class);
                            leaderBoardPoints.add(obj);
                           // totalsteps+=obj.getSteps()
                        }
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
                    Error_Dialog.show("Timeout", SeperateChallenge.this);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, SeperateChallenge.this), SeperateChallenge.this);
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show("Please Check Your Internet Connection", SeperateChallenge.this);
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

    private void getuser() {

        final ProgressDialog dialog = new ProgressDialog(SeperateChallenge.this, R.style.MyAlertDialogStyle);
        dialog.setMessage("Loading the User");
        dialog.show();
        String tag_string_req = "Login";
        String url = ApiList.Users + appDetails.getAppId()+"&userid="+Paper.book().read("userId", "")+"&admin_flag=attendee_option";
        StringRequest strReq = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                dialog.dismiss();
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
                            Error_Dialog.show(jObj.getString("responseString"), SeperateChallenge.this);
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
                    Error_Dialog.show("Timeout", SeperateChallenge.this);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, SeperateChallenge.this), SeperateChallenge.this);
                } else if (VolleyErrorLis.isNetworkProblem(error)) {


                    userview.clear();
                    userview = Paper.book(appDetails.getAppId()).read("OfflineAttendeeList",new ArrayList<User>());

                    if (userview.size() > 0) {

                    } else{}
                       // showview(false);

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

        } else{}
           // showview(false);


    }
}
