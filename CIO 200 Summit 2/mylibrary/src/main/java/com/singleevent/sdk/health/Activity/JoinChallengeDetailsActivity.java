package com.singleevent.sdk.health.Activity;


import android.graphics.Color;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.singleevent.sdk.health.Adapter.MyAdapter;
import com.singleevent.sdk.health.Fragment.ActiveminsFragment;
import com.singleevent.sdk.health.Fragment.DistanceFragment;
import com.singleevent.sdk.health.Fragment.JoinDetailsFragment;
import com.singleevent.sdk.health.Fragment.JoinParticipantsFragment;
import com.singleevent.sdk.health.Fragment.StepsFragment;
import com.singleevent.sdk.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

public class JoinChallengeDetailsActivity extends AppCompatActivity {


    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_challenge_details);

        tabLayout = findViewById(R.id.tabLayout_Join);
        viewPager = findViewById(R.id.viewPager_Join);




        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#000000"));
        MyAdapter tabadapter = new MyAdapter(getSupportFragmentManager());
        tabadapter.AddFragment(new JoinDetailsFragment(),"Details");
        tabadapter.AddFragment(new JoinParticipantsFragment(),"Participants");

        viewPager.setAdapter(tabadapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}
