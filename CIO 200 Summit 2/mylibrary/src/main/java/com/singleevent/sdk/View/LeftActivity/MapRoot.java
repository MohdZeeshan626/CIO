package com.singleevent.sdk.View.LeftActivity;

import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
//import androidx.fragment.app.Fragment;vv
import androidx.fragment.app.FragmentManager;
//import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.SpannableString;
import android.view.MenuItem;


import com.singleevent.sdk.Custom_View.Util;
import com.singleevent.sdk.model.Agenda.Agendadetails;
import com.singleevent.sdk.model.AppDetails;
import com.singleevent.sdk.model.Events;
import com.singleevent.sdk.R;
import com.singleevent.sdk.View.Fragment.Left_Fragment.Floorfragment;
import com.singleevent.sdk.View.Fragment.Left_Fragment.Mapstaticview;
import com.singleevent.sdk.View.Fragment.Left_Fragment.VenueFragment;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

/**
 * Created by Admin on 6/1/2017.
 */

public class MapRoot extends AppCompatActivity {

    AppDetails appDetails;
    int pos;
    private String title, venue, description;
    private ArrayList<Events> events = new ArrayList<Events>();
    Events e;
    Agendadetails items;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private int[] tabIcons = {
            R.drawable.loc,
            R.drawable.sfloor,
            R.drawable.maps
    };

    double endLat, endLong;
    ArrayList<String> tabstoshow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Paper.init(this);
        setContentView(R.layout.s_maproot);
        appDetails = Paper.book().read("Appdetails");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));
        setSupportActionBar(toolbar);


        // getting the data from previous activity

        if (getIntent().getExtras() == null)
            finish();

        viewPager = (ViewPager) findViewById(R.id.viewpager);


        if (getIntent().getAction()!=null && getIntent().getAction().equals("com.AgendaDetails")){
        pos = getIntent().getExtras().getInt("pos");
        title = getIntent().getExtras().getString("title");
            items = (Agendadetails)getIntent().getSerializableExtra("Agendaview");
            tabstoshow = new ArrayList<>();
            venue = items.getLocation_detail().getVenue();
            description = items.getLocation_detail().getDescription();
            try{
                endLat = Double.parseDouble(items.getLocation_detail().getLat());
                endLong = Double.parseDouble(items.getLocation_detail().getLng());
            }catch(Exception e)
            {
                System.out.print("Double Exception");
            }

            tabstoshow.add("0");
            if (items.getLocation_detail().getFloors().length > 0) {
                tabstoshow.add("1");
            }
            if (e.getTabs(pos).getMapsSize() >= 0) {
                tabstoshow.add("2");
            }

            setupViewPagerforAgenda(viewPager, tabstoshow);

        }else{
            pos = getIntent().getExtras().getInt("pos");
            title = getIntent().getExtras().getString("title");
            events = Paper.book().read("Appevents");
            e = events.get(0);
            tabstoshow = new ArrayList<>();
            venue = e.getTabs(pos).getVenue();
            description = e.getTabs(pos).getDescription();
            endLat = e.getTabs(pos).getLat();
            endLong = e.getTabs(pos).getLng();
            tabstoshow.add("0");
            if (e.getTabs(pos).getFloorsSize() > 0) {
                tabstoshow.add("1");
            }
            if (e.getTabs(pos).getMapsSize() >= 0) {
                tabstoshow.add("2");
            }

        setupViewPager(viewPager, tabstoshow);
        }

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
      tabLayout.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));
        setupTabIcons(tabstoshow);

        setupTabIconsforAgenda(tabstoshow);
    }
    private void setupTabIconsforAgenda(ArrayList<String> tabstoshow) {
        for (int i = 0; i < tabstoshow.size(); i++) {
            switch (tabstoshow.get(i)) {
                case "0":
                    tabLayout.getTabAt(i).setIcon(tabIcons[0]);
                    break;
                case "1":
                    tabLayout.getTabAt(i).setIcon(tabIcons[1]);
                    break;
                case "2":
                    tabLayout.getTabAt(i).setIcon(tabIcons[2]);
                    break;
            }
        }
    }

    private void setupTabIcons(ArrayList<String> tabstoshow) {

        for (int i = 0; i < tabstoshow.size(); i++) {
            switch (tabstoshow.get(i)) {
                case "0":
                    tabLayout.getTabAt(i).setIcon(tabIcons[0]);
                    break;
                case "1":
                    tabLayout.getTabAt(i).setIcon(tabIcons[1]);
                    break;
                case "2":
                    tabLayout.getTabAt(i).setIcon(tabIcons[2]);
                    break;
            }
        }


    }


    @Override
    public void onResume() {
        super.onResume();
        SpannableString s = new SpannableString(title);
        setTitle(Util.applyFontToMenuItem(this, s));

    }

    private void setupViewPager(ViewPager viewPager, ArrayList<String> tabstoshow) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        Bundle b;
        for (int i = 0; i < tabstoshow.size(); i++) {
            switch (tabstoshow.get(i)) {
                case "0":
                    VenueFragment one = new VenueFragment();
                    b = new Bundle();
                    b.putBoolean("isAgenda",false);
                    b.putString("venue", venue);
                    b.putString("des", description);
                    b.putDouble("lat", endLat);
                    b.putDouble("lng", endLong);
                    one.setArguments(b);
                    adapter.addFrag(one,"Venue");

                    break;
                case "1":
                    Floorfragment floor = new Floorfragment();
                    b = new Bundle();
                    b.putInt("pos", pos);
                    b.putBoolean("isAgenda",false);
                    floor.setArguments(b);
                    adapter.addFrag(floor, "Floorplans");
                    break;
                case "2":
                    Mapstaticview mapstatic = new Mapstaticview();
                    b = new Bundle();
                    b.putInt("pos", pos);
                    b.putString("venue", venue);
                    b.putDouble("lat", endLat);
                    b.putDouble("lng", endLong);
                    b.putBoolean("isAgenda",false);
                    mapstatic.setArguments(b);
                    adapter.addFrag(mapstatic, "Maps");
                    break;
            }
        }

        viewPager.setAdapter(adapter);
    }
    private void setupViewPagerforAgenda(ViewPager viewPager, ArrayList<String> tabstoshow) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        Bundle b;
        for (int i = 0; i < tabstoshow.size(); i++) {
            switch (tabstoshow.get(i)) {
                case "0":
                    VenueFragment one = new VenueFragment();
                    b = new Bundle();
                    b.putBoolean("isAgenda",true);
                    b.putString("venue", venue);
                    b.putString("des", description);
                    b.putDouble("lat", endLat);
                    b.putDouble("lng", endLong);
                    one.setArguments(b);
                    adapter.addFrag(one, "Venue");
                    break;
                case "1":
                    Floorfragment floor = new Floorfragment();
                    b = new Bundle();
                    b.putInt("pos", pos);
                    b.putSerializable("AgendaDetails",items);
                    b.putBoolean("isAgenda",true);
                    floor.setArguments(b);
                    adapter.addFrag(floor, "Floorplans");
                    break;
                case "2":
                    Mapstaticview mapstatic = new Mapstaticview();
                    b = new Bundle();
                    b.putInt("pos", pos);
                    b.putBoolean("isAgenda",true);
                    mapstatic.setArguments(b);
                    adapter.addFrag(mapstatic, "Maps");
                    break;
            }
        }

        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
