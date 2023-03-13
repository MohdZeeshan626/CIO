package com.singleevent.sdk.View.Fragment.Left_Fragment;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.cardview.widget.CardView;

import android.text.SpannableString;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.singleevent.sdk.Custom_View.Error_Dialog;
import com.singleevent.sdk.Custom_View.Util;
import com.singleevent.sdk.Left_Adapter.AdsImageAdapter;
import com.singleevent.sdk.model.Agenda.Agenda;
import com.singleevent.sdk.model.AppDetails;
import com.singleevent.sdk.model.Events;
import com.singleevent.sdk.model.Items;
import com.singleevent.sdk.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import io.paperdb.Paper;


public class
HomeFragment extends Fragment implements View.OnClickListener {

    public CounterClass eventstimer;
    int n;
    int numOfCol, numOfRow;
    LinearLayout timertoshow;
    GridLayout myGridLayout;
    Context context;
    View view;
    AppDetails d;
    boolean timerflag = false;
    ArrayList<Events> events;
    Events e;
    ArrayList<Items> homelist;
    ArrayList<String> url;
    int pos, ban_pos, agenda_pos;
    TextView txtdayshow, txthrshow, txtminshow, txtsecshow;
    OnTabSelectedListener listener;
    private int mDisplayDays;
    private int mDisplayHours;
    private int mDisplayMinutes;
    private int mDisplaySeconds;
    private AppDetails appDetails;
    double Stripheight;
    Agenda agendaDetails;

    DisplayMetrics displayMetrics;
    int adHeight, adWidth;
    CardView cardView;
    private static int currentPage = 0;
    ImageView adsImage;
    private static ViewPager mPager;
    private ArrayList<String> img_arr = new ArrayList<String>();
    private ArrayList<String> butn_url_arr = new ArrayList<String>();
    private ArrayList<String> ban_type_arr = new ArrayList<String>();
    private ArrayList<Integer> type_id_arr = new ArrayList<Integer>();
    private ArrayList<String> ban_name_arr = new ArrayList<String>();

    long elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        Paper.init(context);
        events = Paper.book().read("Appevents");
        appDetails = Paper.book().read("Appdetails");
        e = events.get(0);
        d = Paper.book().read("Appdetails");
        for (int i = 0; i < e.getTabs().length; i++) {
            if (e.getTabs(i).getType().compareTo("home") == 0) {
                pos = i;
                break;
            }

        }
        url = new ArrayList<>();
        homelist = new ArrayList<>();
        for (int j = 0; j < e.getTabs(pos).getItemsSize(); j++) {
            Items hitem = e.getTabs(pos).getItems(j);
            Log.d("check_value_for_items", "onCreate: " + hitem.getValue() + " \n" + hitem.getImageUrl());
            if (getMenuId(hitem.getValue()) == 0) continue;

//            if (checkitemsize(hitem.getValue())) {
            Log.d("check_value_for_items", "onCreate: " + getMenuId(hitem.getValue()));

            homelist.add(e.getTabs(pos).getItems(j));
            url.add(hitem.getImageUrl());
//            }

        }
        n = homelist.size();
        timerflag = d.getTimer();
        System.out.println("Timer Flag : " + timerflag);


        // dynamically setting cell size based on screen width
        displayMetrics = getResources().getDisplayMetrics();
        Stripheight = (displayMetrics.widthPixels) * 0.08;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (OnTabSelectedListener) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        listener.setActiveMenu(R.id.home);
        if (view != null)
            return view;

        view = inflater.inflate(R.layout.s_fragment_home, container, false);
        timertoshow = (LinearLayout) view.findViewById(R.id.timer);
        txtdayshow = (TextView) view.findViewById(R.id.dayshow);
        txthrshow = (TextView) view.findViewById(R.id.hrshow);
        txtminshow = (TextView) view.findViewById(R.id.minshow);
        txtsecshow = (TextView) view.findViewById(R.id.secshow);
        txtdayshow.setBackgroundColor(Color.parseColor(d.getTheme_strips()));
        txthrshow.setBackgroundColor(Color.parseColor(d.getTheme_strips()));
        txtminshow.setBackgroundColor(Color.parseColor(d.getTheme_strips()));
        txtsecshow.setBackgroundColor(Color.parseColor(d.getTheme_strips()));
        timertoshow.setVisibility(View.GONE);
        myGridLayout = (GridLayout) view.findViewById(R.id.mygrid);

        cardView = (CardView) view.findViewById(R.id.ad_card_view);
        mPager = (ViewPager) view.findViewById(R.id.adsImage_pager);
        adsImage = (ImageView) view.findViewById(R.id.adsImage);

        initializeAd();
        return view;
    }

    private void initializeAd() {
        //setting height and width for banner ads
        adHeight = (int) ((displayMetrics.heightPixels) * 0.08);
        adWidth = (int) ((displayMetrics.widthPixels) * 0.08);

        Log.d("dimentions_for_image", "initializeAd: +"+adHeight+"\n+width"+adWidth);
        LinearLayout.LayoutParams layoutParams;
        layoutParams = new LinearLayout.LayoutParams(adWidth, adHeight);

        for (int i = 0; i < e.getTabs().length; i++)
            //checking banner object from json
            if (e.getTabs(i).getType().contains("banner")) {
                System.out.println("Banner Ad Available");
                ban_pos = i;

                if (e.getTabs(i).getItems().length > 0) {
                    //setting banner ads
                    setAds();
                    break;
                }
            } else
                cardView.setVisibility(View.GONE);
    }

    private void setAds() {
        //enabling cardview for showing ads
        cardView.setVisibility(View.VISIBLE);

        final int img_size = e.getTabs(ban_pos).getItems().length;
        //copying values to array
        for (int i = 0; i < img_size; i++) {
            img_arr.add(e.getTabs(ban_pos).getItems(i).getBanner_url());
            butn_url_arr.add(e.getTabs(ban_pos).getItems(i).getButton_link());
            ban_type_arr.add(e.getTabs(ban_pos).getItems(i).getBanner_type());
            ban_name_arr.add(e.getTabs(ban_pos).getItems(i).getBanner_name());
            type_id_arr.add(e.getTabs(ban_pos).getItems(i).getType_id());
        }
        System.out.println("Banner Image Size : " + img_arr.size());
        //setting values to the adapter
        mPager.setAdapter(new AdsImageAdapter(getActivity(), getActivity(), img_arr, butn_url_arr, ban_type_arr, type_id_arr, ban_name_arr));
        // Auto start of viewpager
        final Handler handler = new Handler();
        //incrementing page for ads auto scroll
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == img_size) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, false);
            }
        };
        Timer swipeTimer = new Timer();
        //3 sec timer for autoscroll
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3000, 3000);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("numbers_of_cards", "onActivityCreated: " + n);
        numOfCol = 2;
        numOfRow = n / 2 + 1;
        myGridLayout.setRowCount(numOfRow);
        myGridLayout.setColumnCount(numOfCol);
        myGridLayout.post(new Runnable() {
            @Override
            public void run() {
                setGrids();
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    private void showtimer(boolean timerflag) {
        try {
            if (timerflag) {
//                long oldMillis = appDetails.getStartdate();

//                System.out.println("Timer Agenda Time : " + e.getTabs(1).getAgenda(0).getDetail(0).getFromtime());

                for (int i = 0; i < e.getTabs().length; i++)
                    if (e.getTabs(i).getType().contains("agenda")) {
                        agenda_pos = i;
                    }
                long oldMillis = d.getStartdate();
                //ong oldMillis = e.getTabs(agenda_pos).getAgenda(0).getDetail(0).getFromtime();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/M/yyyy hh:mm:ss");
                SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("dd/M/yyyy hh:mm:ss");

                SimpleDateFormat format = new SimpleDateFormat("yy/MM/dd HH:mm:ss ");
              /*imeZone tz = TimeZone.getTimeZone(appDetails.getTimezone());
               System.out.print(tz);
                DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");*/

                Calendar cal1 = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                long utcTime = cal1.getTimeInMillis();

                System.out.print(utcTime);


                // String currentdate =utcTime.toString();
                //    System.out.print(tz);
                SimpleDateFormat f = new SimpleDateFormat("dd-MMM-yyyy");

                long temp = 0l;
                // temp=d.getStartdate();
                if (e.getTabs(agenda_pos).getAgenda(0).getDetail(0).getFromtime() != 0) {
                    temp = e.getTabs(agenda_pos).getAgenda(0).getDetail(0).getFromtime();
                } else {
                    temp = d.getStartdate();
                }


                Date d1 = simpleDateFormat.parse("14/04/2019 00:00:00");
                d1 = simpleDateFormat.parse(simpleDateFormat.format(d.getStartdate()));
                //  Date d2 = simpleDateFormat.parse("10/04/2019 00:00:00");
                // d2 = simpleDateFormat.parse(simpleDateFormat2.format(System.currentTimeMillis()));



              /*  Date d1 = null;
                Date d2 = null;*/

                /*try {
                    d1 =new Date(temp);
                    d2 = currentdate;

                } catch (Exception e) {
                    e.printStackTrace();
                }*/
                String zone, timeZone;
                timeZone = appDetails.getTimezone();

                String s1 = checkTimeZone(timeZone);

                if (s1 == "") {
                    zone = "Asia/Calcutta";
                } else if (timeZone.equalsIgnoreCase("IST")) {
                    zone = "Asia/Calcutta";
                } else if (timeZone.equalsIgnoreCase("PST")) {
                    zone = "America/Los_Angeles";
                } else if (timeZone.equalsIgnoreCase("EST")) {
                    zone = "America/New_York";
                } else if (timeZone.equalsIgnoreCase("CST")) {
                    zone = "America/Chicago";
                } else {
                    zone = s1;
                }
                System.out.println(timeZone);
                long differ = TimeZone.getTimeZone(zone.toString()).getOffset(Calendar.getInstance().getTimeInMillis());
//                long different = (d1.getTime() - timediff) - d2.getTime();
                long different = ((temp) - differ) - utcTime;

                //System.out.println("Date Problem Diff : " + different);

//                long milliDiff = oldMillis - start;
                // long start = System.currentTimeMillis();
                //long milliDiff = oldMillis - start;
                if (different > 0) {
                    eventstimer = new CounterClass(different, 1000);
                    eventstimer.start();
                    timertoshow.setVisibility(View.VISIBLE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /*setting all home menu with images*/
    private void setGrids() {
        int pWidth = myGridLayout.getWidth();
        int pHeight = pWidth / 2 * numOfRow;
        int w = pWidth / numOfCol;
        int h = pHeight / numOfRow;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for (int i = 0; i < n; i++) {
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            if (i == 0 || (i == n - 1 && (n % 2 == 0))) {
                params.width = 2 * w;
                params.height = pWidth * 2 / 4;
//                params.height = pWidth * 1/3;
//                params.width = 1440;
//                params.height = 480;
                Log.d("dimentions_for_Top", "setGrids: width = "+params.width+"  height = "+params.height);
                params.columnSpec = GridLayout.spec(0, 2);
            } else {
                params.width = w;
                params.height = pWidth * 1 / 3;
//                params.height = 480;
                Log.d("dimentions_for_", "setGrids_bottom: width = "+params.width+"  height = "+params.height);

            }


            View child = inflater.inflate(R.layout.s_home_items, null);
            ImageView speakerimage = (ImageView) child.findViewById(R.id.images);
            settingimage(homelist.get(i).getValue(), speakerimage, url.get(i));
            int menuId = getpos(homelist.get(i).getValue());
            String title = listener.getMenuItem(menuId).getTitle().toString();
            TextView tvTitle = (TextView) child.findViewById(R.id.text);
            tvTitle.setText(Util.applyFontToMenuItem(context, new SpannableString(title)));
            tvTitle.setBackgroundColor(Color.parseColor(d.getTheme_strips()));
            String s[] = new String[20];
            s = appDetails.getDisable_items();
            if (s.length > 0 && s != null) {
                for (int k = 0; k < s.length; k++) {
                    if (s[k].equalsIgnoreCase("homepanelstriptheme")) {
                        tvTitle.setVisibility(View.GONE);
                    }
                }
            }

            RelativeLayout.LayoutParams rparams = (RelativeLayout.LayoutParams) tvTitle.getLayoutParams();

            rparams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            rparams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            rparams.height = (int) Stripheight;
            tvTitle.setLayoutParams(rparams);
            tvTitle.setGravity(Gravity.CENTER);
            tvTitle.setMaxLines(1);
            tvTitle.setEllipsize(TextUtils.TruncateAt.END);
            tvTitle.setBackgroundColor(Color.parseColor(d.getTheme_strips()));


            child.setTag(homelist.get(i).getValue());
            child.setOnClickListener(this);
            myGridLayout.addView(child, params);
        }

        showtimer(timerflag);
    }

    @Override
    public void onResume() {
        super.onResume();

        SpannableString s = new SpannableString("Home");
        getActivity().setTitle(Util.applyFontToMenuItem(context, s));

    }

    private int getpos(String value) {

        // String type = value.substring(0, value.length() - 1);
        String type = value.replaceAll("^\\d+|\\d+$", "");
        switch (type) {
            case "map":
                return gettingpos("map", value);
            case "agenda":
                return gettingpos("agenda", value);
            case "currency":
                return gettingpos("currency", value);
            case "aboutus":
                return gettingpos("aboutus", value);
            case "gallery":
                return gettingpos("gallery", value);
            case "speakersData":
                return gettingpos("speakersData", value);
            case "sponsorsData":
                return gettingpos("sponsorsData", value);
            case "myInfo":
                return gettingpos("myInfo", value);
            case "contactUs":
                return gettingpos("contactUs", value);
            case "video":
                return gettingpos("video", value);
            case "survey":
                return gettingpos("survey", value);
            case "moodometer":
                return gettingpos("moodometer", value);

            case "polling":
                return gettingpos("polling", value);
            case "pdf":
                return gettingpos("pdf", value);
            case "socialmedia":
                return gettingpos("socialmedia", value);
            case "exhibitorsData":
                return gettingpos("exhibitorsData", value);
            case "eventslist":
                return gettingpos("eventslist", value);
            case "rssfeeds":
                return gettingpos("rssfeeds", value);
            case "weather":
                return gettingpos("weather", value);
            case "attendee":
                return gettingpos("attendee", value);
            case "feeds":
                return gettingpos("feeds", value);
            case "groupfeed":
                return gettingpos("groupfeed", value);
            case "mySchedules":
                return gettingpos("mySchedules", value);
            case "myCompanies":
                return gettingpos("myCompanies", value);
            case "myNotes":
                return gettingpos("myNotes", value);
            case "myChats":
                return gettingpos("myChats", value);
            case "myNotifications":
                return gettingpos("myNotifications", value);
            case "gamification":
                return gettingpos("gamification", value);
            default:
                return 0;
        }

    }

    private int gettingpos(String value, String type) {
        int pos = 0;
        for (int i = 0; i < e.getTabs().length; i++) {
            if (e.getTabs(i).getType().compareTo(value) == 0) {
                if (e.getTabs(i).getCheckvalue() == null) {
                    pos = i;
                    break;
                } else if (e.getTabs(i).getCheckvalue().equalsIgnoreCase(type)) {
                    pos = i;
                    break;
                }
            }
        }
        return pos;
    }

    private int getMenuId(String value) {
        // value = value.substring(0, value.length() - 1);
        value = value.replaceAll("^\\d+|\\d+$", "");
        switch (value) {
            case "map":
                return 1;
            case "agenda":
                return 1;
            case "currency":
                return 1;
            case "aboutus":
                return 1;
            case "gallery":
                return 1;
            case "speakersData":
                return 1;
            case "sponsorsData":
                return 1;
            case "myInfo":
                return 1;
            case "contactUs":
                return 1;
            case "video":
                return 1;
            case "survey":
                return 1;

            case "moodometer":
                return 1;
            case "polling":
                return 1;
            case "pdf":
                return 1;
            case "socialmedia":
                return 1;
            case "exhibitorsData":
                return 1;
            case "eventslist":
                return 1;
            case "rssfeeds":
                return 1;
            case "weather":
                return 1;
            case "attendee":
                return 1;
            case "mySchedules":
                return 1;
            case "myCompanies":
                return 1;
            case "myNotes":
                return 1;
            case "myChats":
                return 1;
            case "myNotifications":
                return 1;
            case "feeds":
                return 1;
            case "groupfeed":
                return 1;
            case "gamification":
                return 1;

            default:
                return 0;
        }
    }

    private void settingimage(String value, ImageView view, String url) {
        // value = value.substring(0, value.length() - 1);
        value = value.replaceAll("^\\d+|\\d+$", "");
        switch (value) {
            case "map":
                Glide.with(context.getApplicationContext()).load(url).placeholder(R.drawable.s_map).error(R.drawable.s_map).into(view);
                break;
            case "agenda":
                Glide.with(context.getApplicationContext()).load(url).placeholder(R.drawable.s_agenda).error(R.drawable.s_agenda).into(view);
                break;
            case "currency":
                Glide.with(context.getApplicationContext()).load(url).placeholder(R.drawable.s_currencys).error(R.drawable.s_currencys).into(view);
                break;
            case "aboutus":
                Glide.with(context.getApplicationContext()).load(url).placeholder(R.drawable.s_aboutus).error(R.drawable.s_aboutus).into(view);
                break;
            case "gallery":
                Glide.with(context.getApplicationContext()).load(url).placeholder(R.drawable.s_videos).error(R.drawable.s_videos).into(view);
                break;
            case "speakersData":
                Glide.with(context.getApplicationContext()).load(url).placeholder(R.drawable.s_speaker).error(R.drawable.s_speaker).into(view);
                break;
            case "sponsorsData":
                Glide.with(context.getApplicationContext()).load(url).placeholder(R.drawable.s_sponsors).error(R.drawable.s_sponsors).into(view);
                break;
            case "myInfo":
                Glide.with(context.getApplicationContext()).load(url).placeholder(R.drawable.s_sponsors).error(R.drawable.s_sponsors).into(view);
                break;
            case "contactUs":
                Glide.with(context.getApplicationContext()).load(url).placeholder(R.drawable.s_contactus).error(R.drawable.s_contactus).into(view);
                break;
            case "video":

                Glide.with(context.getApplicationContext()).load(url).placeholder(context.getResources().getDrawable(R.drawable.s_videos)).error(context.getResources().getDrawable(R.drawable.s_videos)).into(view);

                break;
            case "survey":
                Glide.with(context.getApplicationContext()).load(url).placeholder(context.getResources().getDrawable(R.drawable.s_surveys)).error(context.getResources().getDrawable(R.drawable.s_surveys)).into(view);

                break;
            case "moodometer":
                Glide.with(context.getApplicationContext()).load(url).placeholder(context.getResources().getDrawable(R.drawable.s_surveys)).error(context.getResources().getDrawable(R.drawable.s_surveys)).into(view);
                break;
            case "pdf":
                Glide.with(context.getApplicationContext()).load(url).placeholder(context.getResources().getDrawable(R.drawable.s_awards)).error(context.getResources().getDrawable(R.drawable.s_awards)).into(view);

                break;
            case "socialmedia":
                Glide.with(context.getApplicationContext()).load(url).placeholder(context.getResources().getDrawable(R.drawable.s_socials)).error(context.getResources().getDrawable(R.drawable.s_socials)).into(view);

                break;
            case "feeds":
                Glide.with(context.getApplicationContext()).load(url).placeholder(context.getResources().getDrawable(R.drawable.s_socials)).error(context.getResources().getDrawable(R.drawable.s_socials)).into(view);

                break;
            case "groupfeed":
                Glide.with(context.getApplicationContext()).load(url).placeholder(context.getResources().getDrawable(R.drawable.s_socials)).error(context.getResources().getDrawable(R.drawable.s_socials)).into(view);

                break;
            case "exhibitorsData":
                Glide.with(context.getApplicationContext()).load(url).placeholder(context.getResources().getDrawable(R.drawable.s_exhibitors)).error(context.getResources().getDrawable(R.drawable.s_exhibitors)).into(view);

                break;
            case "eventslist":
                Glide.with(context.getApplicationContext()).load(url).placeholder(context.getResources().getDrawable(R.drawable.s_upcoming)).error(context.getResources().getDrawable(R.drawable.s_upcoming)).into(view);

                break;
            case "rssfeeds":
                Glide.with(context.getApplicationContext()).load(url).placeholder(context.getResources().getDrawable(R.drawable.s_new)).error(context.getResources().getDrawable(R.drawable.s_new)).into(view);

                break;
            case "weather":
                Glide.with(context.getApplicationContext()).load(url).placeholder(context.getResources().getDrawable(R.drawable.s_weathers)).error(context.getResources().getDrawable(R.drawable.s_weathers)).into(view);
                break;
            case "attendee":
                Glide.with(context.getApplicationContext()).load(url).placeholder(context.getResources().getDrawable(R.drawable.s_attendee)).error(context.getResources().getDrawable(R.drawable.s_attendee)).into(view);
                break;
            case "polling":
                Glide.with(context.getApplicationContext()).load(url).placeholder(context.getResources().getDrawable(R.drawable.s_surveys)).error(context.getResources().getDrawable(R.drawable.s_surveys)).into(view);
                break;
            case "mySchedules":
                Glide.with(context.getApplicationContext()).load(url).placeholder(context.getResources().getDrawable(R.drawable.s_surveys)).error(context.getResources().getDrawable(R.drawable.s_surveys)).into(view);
                break;
            case "myCompanies":
                Glide.with(context.getApplicationContext()).load(url).placeholder(context.getResources().getDrawable(R.drawable.s_surveys)).error(context.getResources().getDrawable(R.drawable.s_surveys)).into(view);
                break;
            case "myNotes":
                Glide.with(context.getApplicationContext()).load(url).placeholder(context.getResources().getDrawable(R.drawable.s_surveys)).error(context.getResources().getDrawable(R.drawable.s_surveys)).into(view);
                break;
            case "myChats":
                Glide.with(context.getApplicationContext()).load(url).placeholder(context.getResources().getDrawable(R.drawable.s_surveys)).error(context.getResources().getDrawable(R.drawable.s_surveys)).into(view);
                break;
            case "myNotification":
                Glide.with(context.getApplicationContext()).load(url).placeholder(context.getResources().getDrawable(R.drawable.s_surveys)).error(context.getResources().getDrawable(R.drawable.s_surveys)).into(view);
                break;
            case "gamification":
                Glide.with(context.getApplicationContext()).load(url).placeholder(context.getResources().getDrawable(R.drawable.s_surveys)).error(context.getResources().getDrawable(R.drawable.s_surveys)).into(view);
                break;

            default:
                break;
        }
    }


    private boolean checkitemsize(String type) {
        int pos = 0;
        boolean valid = false;
        //  String value = type.substring(0, type.length() - 1);
        String value = type.replaceAll("^\\d+|\\d+$", "");
        switch (value) {
            case "survey":
                for (int i = 0; i < e.getTabs().length; i++) {
                    if (e.getTabs(i).getType().compareTo("survey") == 0) {
                        if (e.getTabs(i).getCheckvalue() == null) {
                            pos = i;
                            break;
                        } else if (e.getTabs(i).getCheckvalue().equalsIgnoreCase(type)) {
                            pos = i;
                            break;
                        }
                    }

                }
                if (e.getTabs(pos).getItemsSize() > 0)
                    valid = true;
                else
                    valid = false;
                break;
            case "moodometer":
                for (int i = 0; i < e.getTabs().length; i++) {
                    if (e.getTabs(i).getType().compareTo("moodometer") == 0) {
                        if (e.getTabs(i).getCheckvalue() == null) {
                            pos = i;
                            break;
                        } else if (e.getTabs(i).getCheckvalue().equalsIgnoreCase(type)) {
                            pos = i;
                            break;
                        }
                    }

                }
                if (e.getTabs(pos).getItemsSize() > 0)
                    valid = true;
                else
                    valid = false;
                break;

            case "polling":
                for (int i = 0; i < e.getTabs().length; i++) {
                    if (e.getTabs(i).getType().compareTo("polling") == 0) {
                        if (e.getTabs(i).getCheckvalue() == null) {
                            pos = i;
                            break;
                        } else if (e.getTabs(i).getCheckvalue().equalsIgnoreCase(type)) {
                            pos = i;
                            break;
                        }
                    }

                }
                if (e.getTabs(pos).getItemsSize() > 0)
                    valid = true;
                else
                    valid = false;
                break;

            case "speakersData":
                for (int i = 0; i < e.getTabs().length; i++) {
                    if (e.getTabs(i).getType().compareTo("speakersData") == 0) {
                        if (e.getTabs(i).getCheckvalue() == null) {
                            pos = i;
                            break;
                        } else if (e.getTabs(i).getCheckvalue().equalsIgnoreCase(type)) {
                            pos = i;
                            break;
                        }
                    }

                }

                if (e.getTabs(pos).getItemsSize() > 0)
                    valid = true;
                else
                    valid = false;

                break;
            case "agenda":

                for (int i = 0; i < e.getTabs().length; i++) {
                    if (e.getTabs(i).getType().compareTo("agenda") == 0) {
                        if (e.getTabs(i).getCheckvalue() == null) {
                            pos = i;
                            break;
                        } else if (e.getTabs(i).getCheckvalue().equalsIgnoreCase(type)) {
                            pos = i;
                            break;
                        }
                    }

                }

                if (e.getTabs(pos).getAgendaSize() > 0)
                    valid = true;
                else
                    valid = false;

                break;
            case "exhibitorsData":

                for (int i = 0; i < e.getTabs().length; i++) {
                    if (e.getTabs(i).getType().compareTo("exhibitorsData") == 0) {
                        if (e.getTabs(i).getCheckvalue() == null) {
                            pos = i;
                            break;
                        } else if (e.getTabs(i).getCheckvalue().equalsIgnoreCase(type)) {
                            pos = i;
                            break;
                        }
                    }

                }

                if (e.getTabs(pos).getItemsSize() > 0)
                    valid = true;
                else
                    valid = false;

                break;
            case "socialmedia":

                for (int i = 0; i < e.getTabs().length; i++) {
                    if (e.getTabs(i).getType().compareTo("socialmedia") == 0) {
                        if (e.getTabs(i).getCheckvalue() == null) {
                            pos = i;
                            break;
                        } else if (e.getTabs(i).getCheckvalue().equalsIgnoreCase(type)) {
                            pos = i;
                            break;
                        }
                    }
                }

                if (e.getTabs(pos).getItemsSize() > 0)
                    valid = true;
                else
                    valid = false;

                break;
            case "map":
                valid = true;
                break;
            case "aboutus":
                valid = true;
                break;
            case "gallery":
                valid = true;
                break;
            case "groupfeed":
                valid = true;
                break;
            case "contactUs":
                valid = true;
                break;
            case "rssfeeds":
                valid = true;
                break;
            case "pdf":

                for (int i = 0; i < e.getTabs().length; i++) {
                    if (e.getTabs(i).getType().compareTo("pdf") == 0) {
                        if (e.getTabs(i).getCheckvalue() == null) {
                            pos = i;
                            break;
                        } else if (e.getTabs(i).getCheckvalue().equalsIgnoreCase(type)) {
                            pos = i;
                            break;
                        }
                    }
                }
                if (e.getTabs(pos).getpdfSize() > 0)
                    valid = true;
                else
                    valid = false;
                break;
            case "eventslist":
                for (int i = 0; i < e.getTabs().length; i++) {
                    if (e.getTabs(i).getType().compareTo("eventslist") == 0) {
                        if (e.getTabs(i).getCheckvalue() == null) {
                            pos = i;
                            break;
                        } else if (e.getTabs(i).getCheckvalue().equalsIgnoreCase(type)) {
                            pos = i;
                            break;
                        }
                    }
                }

                if (e.getTabs(pos).getItemsSize() > 0)
                    valid = true;
                else
                    valid = false;

                break;
            case "weather":
                valid = true;
                break;
            case "currency":
                valid = true;
                break;

            case "sponsorsData":
                for (int i = 0; i < e.getTabs().length; i++) {
                    if (e.getTabs(i).getType().compareTo("sponsorsData") == 0) {
                        if (e.getTabs(i).getCheckvalue() == null) {
                            pos = i;
                            break;
                        } else if (e.getTabs(i).getCheckvalue().equalsIgnoreCase(type)) {
                            pos = i;
                            break;
                        }
                    }
                }
                if (e.getTabs(pos).getItemsSize() > 0)
                    valid = true;
                else
                    valid = false;

                break;
            case "video":
                valid = true;
                break;

            case "attendee":
                valid = true;
                break;

            default:
                valid = false;

        }

        return valid;
    }

    public String checkTimeZone(String s) {
        String val = "";

        Map<String, String> hm = new HashMap<String, String>();

        hm.put("ADT", "America/Halifax");
        hm.put("AKDT", "America/Juneau");
        hm.put("AKST", "America/Juneau");
        hm.put("ART", "America/Argentina/Buenos_Aires");
        hm.put("AST", "America/Halifax");
        hm.put("BDT", "Asia/Dhaka");
        hm.put("BRST", "America/Sao_Paulo");
        hm.put("BRT", "America/Sao_Paulo");
        hm.put("BST", "Europe/London");
        hm.put("CAT", "Africa/Harare");
        hm.put("CDT", "America/Chicago");
        hm.put("CEST", "Europe/Paris");
        hm.put("CET", "Europe/Paris");
        hm.put("CLST", "America/Santiago");
        hm.put("CLT", "America/Santiago");
        hm.put("COT", "America/Bogota");
        hm.put("CST", "America/Chicago");
        hm.put("EAT", "Africa/Addis_Ababa");
        hm.put("EDT", "America/New_York");
        hm.put("EEST", "Europe/Istanbul");
        hm.put("EET", "Europe/Istanbul");
        hm.put("EST", "America/New_York");
        hm.put("GMT", "GMT");
        hm.put("GST", "Asia/Dubai");
        hm.put("HKT", "Asia/Hong_Kong");
        hm.put("HST", "Pacific/Honolulu");
        hm.put("ICT", "Asia/Bangkok");
        hm.put("IRST", "Asia/Tehran");
        hm.put("IST", "Asia/Calcutta");
        hm.put("JST", "Asia/Tokyo");
        hm.put("KST", "Asia/Seoul");
        hm.put("MDT", "America/Denver");
        hm.put("MSD", "Europe/Moscow");
        hm.put("MSK", "Europe/Moscow");
        hm.put("MST", "America/Denver");
        hm.put("NZDT", "Pacific/Auckland");
        hm.put("NZST", "Pacific/Auckland");
        hm.put("PDT", "America/Los_Angeles");
        hm.put("PET", "America/Lima");
        hm.put("PHT", "Asia/Manila");
        hm.put("PKT", "Asia/Karachi");
        hm.put("PST", "America/Los_Angeles");
        hm.put("SGT", "Asia/Singapore");
        hm.put("UTC", "UTC");
        hm.put("WAT", "Africa/Lagos");
        hm.put("WEST", "Europe/Lisbon");
        hm.put("WET", "Europe/Lisbon");
        hm.put("WIT", "Asia/Jakarta");

        if (hm.containsKey(s)) {
            val = (String) hm.get(s);


        }
        return val;
    }


    @Override
    public void onClick(View v) {

        String tagId = v.getTag().toString();
        int getMenuIndex = getpos(tagId);
        String temp_status = e.getTabs(getMenuIndex).getMod_display();
        if (temp_status.equalsIgnoreCase("web") || temp_status.equalsIgnoreCase("none")) {

            Error_Dialog.show("Information not available.", getActivity());

        } else if (e.getTabs(getMenuIndex).getType().toLowerCase().equals("survey")) {

            if (e.getTabs(getMenuIndex).getSub_type().toLowerCase().equals("global") || e.getTabs(getMenuIndex).getSub_type().toLowerCase().equals("")) {

                listener.onTabSelected(getMenuIndex);
            } else {
                Error_Dialog.show("Information not available.", getActivity());
            }


        } else if (e.getTabs(getMenuIndex).getSub_type().toLowerCase().equals("agenda")) {
            listener.onTabSelected(getMenuIndex);

        } else {
            listener.onTabSelected(getMenuIndex);
        }

    }

    public interface OnTabSelectedListener {
        void onTabSelected(int position);

        MenuItem getMenuItem(int menuId);

        void setActiveMenu(int menuId);
    }


    public class CounterClass extends CountDownTimer {
        public CounterClass(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
//            timertoshow.setVisibility(View.GONE);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            HomeFragment.this.mDisplayDays = (int) ((millisUntilFinished / 1000) / 86400);
            HomeFragment.this.mDisplayHours = (int) (((millisUntilFinished / 1000) - (HomeFragment.this.mDisplayDays * 86400)) / 3600);
            HomeFragment.this.mDisplayMinutes = (int) (((millisUntilFinished / 1000) - ((HomeFragment.this.mDisplayDays * 86400) + (HomeFragment.this.mDisplayHours * 3600))) / 60);
            HomeFragment.this.mDisplaySeconds = (int) ((millisUntilFinished / 1000) % 60);
           /* HomeFragment.this.mDisplayDays = (int) (millisUntilFinished / (1000*60*60*24));
            HomeFragment.this.mDisplayHours = (int) ((millisUntilFinished / (1000*60*60)) % 24);
            HomeFragment.this.mDisplayMinutes = (int) ((millisUntilFinished / (1000*60)) % 60);
            HomeFragment.this.mDisplaySeconds = (int) ((millisUntilFinished / 1000) % 60);*/
            HomeFragment.this.txtdayshow.setText("" + HomeFragment.this.mDisplayDays);
            HomeFragment.this.txthrshow.setText("" + HomeFragment.this.mDisplayHours);
            HomeFragment.this.txtminshow.setText("" + HomeFragment.this.mDisplayMinutes);
            HomeFragment.this.txtsecshow.setText("" + HomeFragment.this.mDisplaySeconds);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Glide.get(getActivity()).clearMemory();
    }

    /*print difference between start date and end date*/
    public void printDifference(Date startDate, Date endDate) {
        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        System.out.println("startDate : " + startDate);
        System.out.println("endDate : " + endDate);
        System.out.println("different : " + different);

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        System.out.printf(
                "%d days, %d hours, %d minutes, %d seconds%n",
                elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds);

        this.elapsedDays = elapsedDays;
        this.elapsedSeconds = elapsedSeconds;
        this.elapsedHours = elapsedHours;
        this.elapsedMinutes = elapsedMinutes;


    }
}
