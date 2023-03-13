package com.singleevent.sdk.health.Adapter;

import android.content.Context;

import com.singleevent.sdk.health.Fragment.ActiveFragment;
import com.singleevent.sdk.health.Fragment.CompleteFragment;
import com.singleevent.sdk.health.Fragment.UpcomingFragment;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class MyAdapter extends FragmentPagerAdapter {

    private final List<Fragment> fragmentList = new ArrayList<>();
    private final List<String> FragmentListtitles = new ArrayList<>();



    public MyAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return FragmentListtitles.size();
    }

    public CharSequence getPageTitle(int position)
    {
        return FragmentListtitles.get(position);
    }

    public void AddFragment(Fragment fragment, String Title)
    {
        fragmentList.add(fragment);
        FragmentListtitles.add(Title);
    }

    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}