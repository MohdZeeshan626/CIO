package com.singleevent.sdk.health.Fragment;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.singleevent.sdk.Custom_View.Util;
import com.singleevent.sdk.health.Activity.CreateChallengeActivity;
import com.singleevent.sdk.health.Activity.JoinChallenge;
import com.singleevent.sdk.health.Adapter.LiveRecyclerView;
import com.singleevent.sdk.health.Adapter.RecyclerViewCompleteChallengeAdapter;
import com.singleevent.sdk.health.Adapter.RecyclerViewUpcomingChallengeAdapter;
import com.singleevent.sdk.R;
import com.singleevent.sdk.health.Model.GetChallengeList;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.paperdb.Paper;

import static android.view.View.GONE;

/**
 * A simple {@link Fragment} subclass.
 */
public class CompleteFragment extends Fragment {


    TextView txtUpcoming;
    LinearLayout linearLayoutcomplete;
    Button btn_create,btn_join;

    private ArrayList<String> mDate = new ArrayList<>();
    private Context mContext;

    RecyclerView completeRecylerView;
    RecyclerViewCompleteChallengeAdapter mAdapter;

    private ArrayList<String> mchallenge_desc = new ArrayList<>();
    private ArrayList<String> mCreatedBy = new ArrayList<>();

    private  ArrayList<String> mStepsweek = new ArrayList<>();
    private  ArrayList<String> mStepsmonth = new ArrayList<>();
    private  ArrayList<String> mStepsyesr = new ArrayList<>();
    private  ArrayList<String> mMiles = new ArrayList<>();
    private  ArrayList<String> mMins = new ArrayList<>();
    LiveRecyclerView adapterjoin;
    RecyclerView recyclerViewjoin;
    ArrayList<GetChallengeList> challengeLists=new ArrayList<>();
    ArrayList<GetChallengeList> completedchallengeLists=new ArrayList<>();
    Context context;
    ArrayList<Drawable> arrayList=new ArrayList<>();
    TextView totalchallenge;
    Button btn_create_challenge;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      //  View view= inflater.inflate(R.layout.empty_fragment, container, false);
       // View view = inflater.inflate(R.layout.fragment_complete, container, false);
       /* txtUpcoming= (TextView)view.findViewById(R.id.txt_complete) ;
        linearLayoutcomplete = (LinearLayout)view.findViewById(R.id.linearComplete_chllenge);
       txtUpcoming.setVisibility(GONE);

       completeRecylerView = (RecyclerView)view.findViewById(R.id.recyclerviewComplere_Challenge);
        btn_create =(Button)view. findViewById(R.id.btncreate_challenge);
        btn_join =(Button)view.findViewById(R.id.btnjoin_challenge);
        btn_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getActivity(), JoinChallenge.class);
                startActivity(in);
            }
        });
        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent (getActivity(), CreateChallengeActivity.class);
                startActivity(in);
            }
        });

        initData();*/

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
        challengeLists= Paper.book().read("challengelist");
        //  btn_create_challenge.setVisibility(View.GONE);
        if(challengeLists.size()>0&& challengeLists!=null){
            completedchallengeLists.clear();
            for(int i=0; i<challengeLists.size(); i++) {
                if (System.currentTimeMillis() >challengeLists.get(i).getEndDate()){
                    completedchallengeLists.add(challengeLists.get(i));
                }
            }
            if(completedchallengeLists.size()>0){
                totalchallenge.setText(completedchallengeLists.size()+" "+"Available Challenges");}
            else{
                totalchallenge.setText("0 "+" "+"Available Challenges");
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

    private void initData() {



            mDate.add("Tuesday,June 9");


            mDate.add("Tuesday,June 30");

          //  initRecyclerView();
        }

        private void initRecyclerView() {

           /* mAdapter = new RecyclerViewCompleteChallengeAdapter(mDate,getContext());
            completeRecylerView.setAdapter(mAdapter);
            completeRecylerView.setLayoutManager(new LinearLayoutManager(getContext()));
            mAdapter.notifyDataSetChanged();*/
            adapterjoin = new LiveRecyclerView(completedchallengeLists,getContext(),2);
            recyclerViewjoin.setAdapter(adapterjoin);
            recyclerViewjoin.setLayoutManager(new LinearLayoutManager(context));
            adapterjoin.notifyDataSetChanged();
        }
}
