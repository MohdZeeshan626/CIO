package com.singleevent.sdk.View.LeftActivity;

import android.animation.Animator;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.ParseException;
import android.os.Bundle;

import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.text.SpannableString;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;


import com.singleevent.sdk.Custom_View.Util;
import com.singleevent.sdk.model.Agenda.Agenda;
import com.singleevent.sdk.model.AppDetails;
import com.singleevent.sdk.model.DataRange;
import com.singleevent.sdk.model.Events;
import com.singleevent.sdk.R;
import com.singleevent.sdk.View.Fragment.Left_Fragment.AgendaFragement;
import com.singleevent.sdk.View.Fragment.Left_Fragment.EmptyFragment;
import com.singleevent.sdk.View.Fragment.Left_Fragment.MyScheduleFragment;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import io.paperdb.Paper;

/**
 * Created by Admin on 5/26/2017.
 */

public class AgendaRoot extends AppCompatActivity implements View.OnClickListener {

    long startdate, enddate;
    AppDetails appDetails;
    FrameLayout frame;
    TextView monthofevent;
    LinearLayout sch;
    int pos;
    private String title;
    private ArrayList<Events> events = new ArrayList<Events>();
    static ArrayList<Agenda> agendalist = new ArrayList<>();
    Events e;
    double CellWidth, ImgWidth, fab1, fab2;
    SimpleDateFormat myFormat;
    long diff = 0;
    ArrayList<DataRange> dataRanges;
    List<View> toggleviews;
    int firstitempostion,todayagendapos=0;
    boolean isFirstget = false;

    TimeZone tz;


    //adding fab layout in activty
    LinearLayout fablayout1, fablayout2, ll_buttons;
    FloatingActionButton fab;
    boolean isFABOpen;
    private Button btn_myschedule, btn_fullagenda;

    String baseUrl = "https://webmobi.s3.amazonaws.com/nativeapps/";
    String filename = "app.json";
    File eventDir, jsonFile;
    String dir,token="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Paper.init(this);
        setContentView(R.layout.s_agendaroot);
        appDetails = Paper.book().read("Appdetails");

        dir = getFilesDir().toString();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));
        setSupportActionBar(toolbar);
        btn_myschedule = (Button) findViewById(R.id.btn_myschedule);
        btn_fullagenda = (Button) findViewById(R.id.btn_fullagenda);
        ll_buttons = (LinearLayout) findViewById(R.id.ll_buttons);

        // ll_buttons.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));

        //setting background color for buttons
        //change the text and background color of button
        btn_fullagenda.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));
        btn_fullagenda.setTextColor(getResources().getColor(R.color.white));
        btn_myschedule.setBackgroundColor(getResources().getColor(R.color.white));
        btn_myschedule.setTextColor(Color.parseColor(appDetails.getTheme_color()));


        // getting the data from previous activity

        if (getIntent().getExtras() == null)
            finish();

        pos = getIntent().getExtras().getInt("pos");
        title = getIntent().getExtras().getString("title");


        //initializing the view
        frame = (FrameLayout) findViewById(R.id.realTabContent);
        monthofevent = (TextView) findViewById(R.id.monthofevent);
        monthofevent.setTypeface(Util.regulartypeface(this));
        sch = (LinearLayout) findViewById(R.id.sch);

        // getting the data range
        startdate = appDetails.getStartdate();
        enddate = appDetails.getEnddate();

        // getting the agenda data

        tz = TimeZone.getDefault();
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));

        // getting the agenda data

        agendalist.clear();
        events = Paper.book().read("Appevents");
        e = events.get(0);
        for (int j = 0; j < e.getTabs(this.pos).getAgendaSize(); j++) {
            agendalist.add(e.getTabs(this.pos).getAgenda(j));
        }


        // dynamically setting cell size based on screen width
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        CellWidth = (displayMetrics.widthPixels) / 7;

        //adding fab button

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(appDetails.getTheme_color())));
        fablayout1 = (LinearLayout) findViewById(R.id.fabLayout1);
        fablayout2 = (LinearLayout) findViewById(R.id.fabLayout2);
        ImgWidth = (displayMetrics.widthPixels) * 0.20;
        fab1 = (displayMetrics.widthPixels) * 0.15;
        fab2 = fab1 * 2;

        fab.setOnClickListener(this);
        fablayout1.setOnClickListener(this);
        fablayout2.setOnClickListener(this);
        btn_fullagenda.setOnClickListener(this);
        btn_myschedule.setOnClickListener(this);

        //fablayout1.performClick();


        initializeTabs();
        setupTabHost();
        if(todayagendapos>0)
        sch.getChildAt(todayagendapos).performClick();
        else
            sch.getChildAt(firstitempostion).performClick();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);

        // Associate searchable configuration with the SearchView

        MenuItem ourSearchItem = menu.findItem(R.id.search);
        SearchView sv = (SearchView) ourSearchItem.getActionView();
        SpannableString s = new SpannableString(Html.fromHtml("<font color = #ffffff>Search here...</font>"));

        sv.setQueryHint(Util.applyFontToMenuItem(AgendaRoot.this,s));



        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                Intent intent = new Intent("SEARCH_KEY");
                intent.putExtra("searched_text",s);
                sendBroadcast(intent);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                Intent intent = new Intent("SEARCH_KEY");
                intent.putExtra("searched_text",s);
                sendBroadcast(intent);
                return true;
            }
        });

        return true;
    }

    // calculating the difference how many days agenda goes and setting view

    private void initializeTabs() {
        myFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            diff = enddate - startdate;
            diff = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
            diff = diff + 1;

            if (diff < 7) {
                switch ((int) diff) {
                    case 6:
                        dataRanges = new ArrayList<>();
                        Adddaterange();
                        Addfutredate(6);

                        break;
                    case 5:
                        dataRanges = new ArrayList<>();
                        Addpasdate(-1);
                        Adddaterange();
                        Addfutredate(5);

                        break;
                    case 4:
                        dataRanges = new ArrayList<>();
                        Addpasdate(-1);
                        Adddaterange();
                        Addfutredate(4);
                        Addfutredate(5);

                        break;

                    case 3:
                        dataRanges = new ArrayList<>();
                        Addpasdate(-2);
                        Addpasdate(-1);
                        Adddaterange();
                        Addfutredate(3);
                        Addfutredate(4);

                        break;
                    case 2:
                        dataRanges = new ArrayList<>();
                        Addpasdate(-3);
                        Addpasdate(-2);
                        Addpasdate(-1);
                        Adddaterange();
                        Addfutredate(2);
                        Addfutredate(3);

                        break;
                    case 1:
                        dataRanges = new ArrayList<>();
                        Addpasdate(-3);
                        Addpasdate(-2);
                        Addpasdate(-1);
                        Adddaterange();
                        Addfutredate(1);
                        Addfutredate(2);
                        Addfutredate(3);
                        break;
                }
            } else {
                dataRanges = new ArrayList<>();
                Adddaterange();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }


    private void Adddaterange() {


        for (int i = 0; i < diff; i++) {
            myFormat = new SimpleDateFormat("yyyy-MM-dd");
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(startdate);
            cal.add(Calendar.DATE, i);
            myFormat = new SimpleDateFormat("yyyy-MM-dd");
            String day = myFormat.format(cal.getTime());
            dataRanges.add(new DataRange(day, true, cal.getTimeInMillis()));
        }

    }

    private void Addfutredate(int pos) {


        myFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(startdate);
        cal.add(Calendar.DATE, pos);
        myFormat = new SimpleDateFormat("yyyy-MM-dd");
        String day = myFormat.format(cal.getTime());
        dataRanges.add(new DataRange(day, false, cal.getTimeInMillis()));
    }

    private void Addpasdate(int pos) {

        myFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(startdate);
        cal.add(Calendar.DATE, pos);
        myFormat = new SimpleDateFormat("yyyy-MM-dd");
        String day = myFormat.format(cal.getTime());
        dataRanges.add(new DataRange(day, false, cal.getTimeInMillis()));
    }


    private String CalculateMonth(long milli) {

        myFormat = new SimpleDateFormat("MMM yyyy");
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(milli);
        return myFormat.format(cal.getTime());
    }

    private String CalculateDay(long milli) {


        myFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(milli);
        myFormat = new SimpleDateFormat("yyyy-MM-dd");
       return  myFormat.format(cal.getTime());

    }


    private void setupTabHost() {

        toggleviews = new ArrayList<>();
        for (int i = 0; i < dataRanges.size(); i++) {
            DataRange range = dataRanges.get(i);
            myFormat = new SimpleDateFormat("yyyy-MM-dd");
            Calendar cal = Calendar.getInstance();
            try {
                cal.setTime(myFormat.parse(range.getDate()));

            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }

            Date dates = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            String strDate = formatter.format(dates);
            if(dataRanges.get(i).getDate().equalsIgnoreCase(strDate))
            {
                for(int k=0; k<agendalist.size(); k++)
                {
                    for(int l=0; l<agendalist.get(k).getDetailSize(); l++)
                    {
                        long miliSec = agendalist.get(k).getDetail(l).getFromtime();

                        // Creating date format
                        DateFormat simple = new SimpleDateFormat("yyyy-MM-dd");

                        // Creating date from milliseconds
                        // using Date() constructor
                        Date result = new Date(miliSec);
                        String tempdate=(simple.format(result));
                        if(tempdate.equalsIgnoreCase(strDate))
                        {
                                todayagendapos = k;
                                break;
                        }

                    }



                }
               // todayagendapos=i;
            }
//            cal.add(Calendar.DATE, i);
            myFormat = new SimpleDateFormat("EEE");
            String day = myFormat.format(cal.getTime());
            myFormat = new SimpleDateFormat("dd");
            String date = myFormat.format(cal.getTime());
            myFormat = new SimpleDateFormat("yyyy-MM-dd");
            // adding the tabs dynamically
            String tag = myFormat.format(cal.getTime());

            final View tabIndicator = LayoutInflater.from(this).inflate(R.layout.s_day_tab, null, false);
            TextView txtday = (TextView) tabIndicator.findViewById(R.id.day);
            TextView txtdate = (TextView) tabIndicator.findViewById(R.id.date);
            txtdate.setText(date);
            txtday.setText(day);
            tabIndicator.setLayoutParams(new LinearLayout.LayoutParams((int) CellWidth, ViewGroup.LayoutParams.WRAP_CONTENT));
            tabIndicator.setTag(range.getDateinmillis());
            txtdate.setBackground(Util.setImageButtonState(AgendaRoot.this, R.drawable.round_selected, Color.parseColor(appDetails.getTheme_color())));


            if (!range.isviewclickable()) {
                tabIndicator.setEnabled(false);
                txtdate.setTextColor(Color.parseColor("#32000000"));
                txtday.setTextColor(Color.parseColor("#32000000"));
            } else {
                txtdate.setTextColor(Util.textcolordate(AgendaRoot.this));
                txtday.setTextColor(Util.textcolorday(AgendaRoot.this, Color.parseColor(appDetails.getTheme_color())));

                if (!isFirstget) {
                    firstitempostion = i;
                    isFirstget = true;
                }
                toggleviews.add(tabIndicator);
                tabIndicator.setEnabled(true);
                tabIndicator.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        long tag = (long) view.getTag();

                        //storing last clicked date to get in myschedule fragment
                        Paper.book().write("Current_date",tag);

                        monthofevent.setText(CalculateMonth(tag));

                        Agenda agendaview = new Agenda();
                         for (int j = 0; j < agendalist.size(); j++) {


                            if (CalculateDay(agendalist.get(j).getName()).equalsIgnoreCase(CalculateDay(tag)) && agendalist.get(j).getDetailSize() > 0) {
                                agendaview = agendalist.get(j);
                                break;
                            }
                        }

                        String activefrag = Paper.book().read("activefragment", "");
                        if (agendaview != null && activefrag != null && activefrag.equals("myschedule")) {
                            // add your code here

                            tabIndicator.setSelected(true);
                            tabIndicator.setClickable(false);
                            disableother(view);


                            //change the text and backgound color of button
                            btn_fullagenda.setBackgroundColor(getResources().getColor(R.color.white));
                            btn_fullagenda.setTextColor(Color.parseColor(appDetails.getTheme_color()));
                            btn_myschedule.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));
                            btn_myschedule.setTextColor(getResources().getColor(R.color.white));

                            //closing fragment

                            AgendaFragement closingagendaFragement = new AgendaFragement();
                            FragmentTransaction tra = getSupportFragmentManager().beginTransaction();
                            tra.remove(closingagendaFragement);
                            tra.commit();

                            //open myschedule fragment
                            MyScheduleFragment newFragment = new MyScheduleFragment();
                            Bundle args = new Bundle();
                            args.putInt("pos",pos);
                            args.putLong("Date", tag);
                            args.putString("token",token);
                            newFragment.setArguments(args);
                            args.putSerializable("Agendaview", agendaview);
                            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.realTabContent, newFragment, "MyScheduleFragment");
                            // Commit the transaction
                            transaction.commit();
                            fab.setVisibility(View.INVISIBLE);


                        } else if (agendaview.getDetail() != null) {
                            tabIndicator.setSelected(true);
                            tabIndicator.setClickable(false);
                            disableother(view);
                            //change the text and backgound color of button
                            btn_fullagenda.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));
                            btn_fullagenda.setTextColor(getResources().getColor(R.color.white));
                            btn_myschedule.setBackgroundColor(getResources().getColor(R.color.white));
                            btn_myschedule.setTextColor(Color.parseColor(appDetails.getTheme_color()));

                            fab.setVisibility(View.VISIBLE);

                            AgendaFragement newFragment = new AgendaFragement();
                            Bundle args = new Bundle();
                            args.putLong("Date", tag);
                            args.putSerializable("Agendaview", agendaview);
                            args.putInt("pos",pos);
                            args.putString("token",token);
                            newFragment.setArguments(args);
                            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.realTabContent, newFragment, "AgendaFragment");
                            // Commit the transaction
                            transaction.commit();
                        } else {
                            tabIndicator.setSelected(true);
                            tabIndicator.setClickable(false);
                            disableother(view);

                            EmptyFragment emptyFragment = new EmptyFragment();
                            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.realTabContent, emptyFragment, "EmptyFragment");
                            transaction.commit();

                        }

                    }
                });


            }

            sch.addView(tabIndicator);


        }


    }

    private void disableother(View view) {
        long viewtag = (long) view.getTag();
        for (int i = 0; i < toggleviews.size(); i++) {
            if (viewtag != (long) toggleviews.get(i).getTag()) {
                toggleviews.get(i).setSelected(false);
                toggleviews.get(i).setClickable(true);
            }
        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int i = item.getItemId();
        if (i == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (i == R.id.search) {
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setTitle(Util.applyFontToMenuItem(this, new SpannableString(title)));
        //getting token if user id logged-in
        if (Paper.book().read("Islogin",false)) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (token == null || token.equals("")) {
                        token = Paper.book(appDetails.getAppId()).read("token", "");;
                    }
                }
            });

        }

        if (Paper.book().read("activefragment", "").equals("myschedule")) {
            performMySchedule();
        } else {
            performFullAgenda();
        }

    }

    @Override
    public void onClick(View v) {

        Intent updatesorting = new Intent("AgendaFragment");
        int i = v.getId();
        if (i == R.id.fab) {
            if (!isFABOpen)
                showFABMenu();
            else
                closeFABMenu();

        } else if (i == R.id.fabLayout1) {
            updatesorting.putExtra("Is_Sortbytime", true);
            LocalBroadcastManager.getInstance(this).sendBroadcast(updatesorting);
            performFullAgenda();
            closeFABMenu();
            Paper.book().write("SessionSort","Yes");
        }
        else if(i==R.id.fabLayout2){
            updatesorting.putExtra("Is_Sortbytime", false);
            LocalBroadcastManager.getInstance(this).sendBroadcast(updatesorting);

            closeFABMenu();
            Paper.book().write("SessionSort","No");



        } else if (i == R.id.btn_myschedule) {
            performMySchedule();
        } else if (i == R.id.btn_fullagenda) {
            performFullAgenda();

        }
    }

    private void performMySchedule() {
        Paper.book().write("activefragment", "myschedule");
        //change the text and backgound color of button
        btn_fullagenda.setBackgroundColor(getResources().getColor(R.color.white));
        btn_fullagenda.setTextColor(Color.parseColor(appDetails.getTheme_color()));
        btn_myschedule.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));
        btn_myschedule.setTextColor(getResources().getColor(R.color.white));

        //closing fragment

        AgendaFragement closingagendaFragement = new AgendaFragement();
        FragmentTransaction tra = getSupportFragmentManager().beginTransaction();
        tra.remove(closingagendaFragement);
        tra.commit();

        long tag = Paper.book().read("Current_date",0L);
        if (tag!=0L)
            monthofevent.setText(CalculateMonth(tag));
        else
            sch.getChildAt(firstitempostion).performClick();
        sch.getChildAt(firstitempostion).performClick();

        fab.setVisibility(View.INVISIBLE);
    }

    private void performFullAgenda() {
        //closing fragment

        Paper.book().write("activefragment", "fullagenda");
        MyScheduleFragment closingscheduleFragement = new MyScheduleFragment();
        FragmentTransaction traschedule = getSupportFragmentManager().beginTransaction();
        traschedule.remove(closingscheduleFragement);
        traschedule.commit();

        //change the text and backgound color of button
        btn_fullagenda.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));
        btn_fullagenda.setTextColor(getResources().getColor(R.color.white));
        btn_myschedule.setBackgroundColor(getResources().getColor(R.color.white));
        btn_myschedule.setTextColor(Color.parseColor(appDetails.getTheme_color()));

        fab.setVisibility(View.VISIBLE);
        long tag = Paper.book().read("Current_date",0L);
        if (tag!=0L)
            monthofevent.setText(CalculateMonth(tag));
        else
            sch.getChildAt(firstitempostion).performClick();

        Agenda agendaview = new Agenda();
        for (int j = 0; j < agendalist.size(); j++) {

            if (CalculateDay(agendalist.get(j).getName()).equalsIgnoreCase(CalculateDay(tag)) && agendalist.get(j).getDetailSize() > 0) {
                agendaview = agendalist.get(j);
                break;
            }
        }
        if (agendaview.getDetail() != null) {


            AgendaFragement agendaFragement = new AgendaFragement();
            Bundle agendaArgs = new Bundle();
            agendaArgs.putInt("pos", pos);
            agendaArgs.putLong("Date", tag);
            agendaArgs.putString("token",token);
            agendaArgs.putSerializable("Agendaview", agendaview);
            agendaFragement.setArguments(agendaArgs);
            FragmentTransaction agendaTrans = getSupportFragmentManager().beginTransaction();
            agendaTrans.replace(R.id.realTabContent, agendaFragement, "AgendaFragment");
            agendaTrans.commit();
        }


    }


    private void showFABMenu() {
        isFABOpen = true;
        fablayout1.setVisibility(View.VISIBLE);
        fablayout2.setVisibility(View.VISIBLE);
        frame.setAlpha(.35F);
        frame.setAlpha(.35F);

//        fab.animate().rotationBy(135);
        fablayout1.animate().translationY(-(int) fab1);
        fablayout2.animate().translationY(-(int) fab2);
    }

    private void closeFABMenu() {
        isFABOpen = false;

//        fab.animate().rotationBy(-135);
        fablayout1.animate().translationY(0);
        fablayout2.animate().translationY(0).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if (!isFABOpen) {
                    fablayout1.setVisibility(View.GONE);
                    fablayout2.setVisibility(View.GONE);
                    frame.setAlpha(1F);
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }
}

