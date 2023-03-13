package com.singleevent.sdk.health.Fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.Bucket;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataSource;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.result.DataReadResponse;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.singleevent.sdk.Custom_View.Util;
import com.singleevent.sdk.R;
import com.singleevent.sdk.health.Activity.CreateChallengeActivity;
import com.singleevent.sdk.health.Adapter.HorizontalRecyclerViewAdapter;
import com.singleevent.sdk.health.Adapter.JoinCreateRecyclerViewAdapter;
import com.singleevent.sdk.health.Adapter.LiveRecyclerView;
import com.singleevent.sdk.health.Model.GetChallengeList;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.multidex.BuildConfig;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.paperdb.Paper;
import ru.tinkoff.scrollingpagerindicator.ScrollingPagerIndicator;

import static java.text.DateFormat.getDateInstance;
import static java.text.DateFormat.getTimeInstance;

public class RunningFragment  extends Fragment {

    private static String TAG = "Steps History";
    private static String TAG1 = "Active History";

    ScrollingPagerIndicator recyclerIndicator;
    RecyclerView recyclerView;

    //vars
    private ArrayList<String> mDate = new ArrayList<>();
    private  ArrayList<String> mStepsweek = new ArrayList<>();
    private  ArrayList<String> mStepsmonth = new ArrayList<>();
    private  ArrayList<String> mStepsyesr = new ArrayList<>();
    private  ArrayList<String> mMiles = new ArrayList<>();
    private  ArrayList<String> mMins = new ArrayList<>();
    LiveRecyclerView adapterjoin;
    RecyclerView recyclerViewjoin;
    ArrayList<GetChallengeList> challengeLists=new ArrayList<>();
    ArrayList<GetChallengeList> livechallengeLists=new ArrayList<>();

    Context context;
    ArrayList<Drawable> arrayList=new ArrayList<>();
    TextView totalchallenge;
    Button btn_create_challenge;
    /*public RunningFragment(ArrayList<GetChallengeList> challengeLists, Context context) {
        this.challengeLists=challengeLists;
        this.context=context;
    }*/

    public RunningFragment() {

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.activity_join_challenge, container, false);
        totalchallenge=v.findViewById(R.id.totalchallenge);
        btn_create_challenge=v.findViewById(R.id.btn_create_challenge);
        btn_create_challenge.setBackground(Util.setrounded(Color.parseColor("#ff8c80f8")));

        btn_create_challenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getContext(), CreateChallengeActivity.class);
                startActivity(in);

            }
        });
         challengeLists.clear();
        challengeLists=Paper.book().read("challengelist");
      //  btn_create_challenge.setVisibility(View.GONE);
        if(challengeLists.size()>0 && challengeLists!=null){
            livechallengeLists.clear();
            for(int i=0; i<challengeLists.size(); i++) {
                if (System.currentTimeMillis()<=challengeLists.get(i).getEndDate()&& System.currentTimeMillis()>=challengeLists.get(i).getStartDate()){
                    livechallengeLists.add(challengeLists.get(i));
                }
            }
            if(livechallengeLists.size()>0 && livechallengeLists!=null) {
                totalchallenge.setText(livechallengeLists.size() + " " + "Available Challenges");
            }
        }
        recyclerViewjoin = v.findViewById(R.id.recyclerviewcreate_joinChallenge);
        //   recyclerView =v. findViewById(R.id.recyclerview_activity);
        //   recyclerIndicator = v.findViewById(R.id.indicator);

    initRecyclerView();
        try {
            //    initData();
        }catch (Exception e)
        {

        }
        return v;
    }

    private void initRecyclerView()
    {
        adapterjoin = new LiveRecyclerView(livechallengeLists,getContext(),0);
        recyclerViewjoin.setAdapter(adapterjoin);
        recyclerViewjoin.setLayoutManager(new LinearLayoutManager(context));
        adapterjoin.notifyDataSetChanged();
    }
}