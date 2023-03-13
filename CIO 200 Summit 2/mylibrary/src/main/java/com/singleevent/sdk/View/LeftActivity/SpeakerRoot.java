package com.singleevent.sdk.View.LeftActivity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;


import com.singleevent.sdk.Custom_View.Letter.LetterTileProvider;
import com.singleevent.sdk.Custom_View.Util;
import com.singleevent.sdk.Left_Adapter.AdsImageAdapter;
import com.singleevent.sdk.Left_Adapter.Speaker_adapter;
import com.singleevent.sdk.model.AppDetails;
import com.singleevent.sdk.model.Events;
import com.singleevent.sdk.model.Items;
import com.singleevent.sdk.R;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import io.paperdb.Paper;

/**
 * Created by Admin on 5/29/2017.
 */

public class SpeakerRoot extends AppCompatActivity {

    AppDetails appDetails;
    private MenuItem mSearchAction;
    private boolean isSearchOpened = false;
    private EditText edtSeach;
    int pos;
    private Context context;
    private String title;
    LetterTileProvider tileProvider;
    private ArrayList<Events> events = new ArrayList<Events>();
    Events e;
    static ArrayList<Items> speakerlists = new ArrayList<>();
    ListView speakerlistview;
    private double ImgWidth, Imgheight;
    private Speaker_adapter adapter;


    int ban_pos;
    DisplayMetrics displayMetrics;
    int adHeight, adWidth;
    CardView cardView;
    private static int currentPage = 0;
    ImageView adsImage;
    EditText speaker_search;
    private static ViewPager mPager;
    private ArrayList<String> img_arr = new ArrayList<String>();
    private ArrayList<String> butn_url_arr = new ArrayList<String>();
    private ArrayList<String> ban_type_arr = new ArrayList<String>();
    private ArrayList<Integer> type_id_arr = new ArrayList<Integer>();
    private ArrayList<String> ban_name_arr = new ArrayList<String>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Paper.init(this);
        setContentView(R.layout.s_speakerroot);
        appDetails = Paper.book().read("Appdetails");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));
        setSupportActionBar(toolbar);
        context = this;


        cardView = (CardView) findViewById(R.id.speaker_ad_card_view);
        mPager = (ViewPager) findViewById(R.id.speaker_adsImage_pager);
        adsImage = (ImageView) findViewById(R.id.adsImage);
        speaker_search=(EditText)findViewById(R.id.speaker_search);
        speaker_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                adapter.getFilter().filter(editable.toString());
            }
        });
        // getting the data from previous activity

        if (getIntent().getExtras() == null)
            finish();

        pos = getIntent().getExtras().getInt("pos");
        title = getIntent().getExtras().getString("title");
        events = Paper.book().read("Appevents");
        e = events.get(0);

        speakerlists.clear();
        for (int j = 0; j < e.getTabs(pos).getItemsSize(); j++) {
            if(e.getTabs(pos).getItems(j).getHide_speaker()!=1) {
                speakerlists.add(e.getTabs(pos).getItems(j));
            }
        }



        speakerlistview = (ListView) findViewById(R.id.speakerlist);

        //calculating the speaker image size

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        ImgWidth = (displayMetrics.widthPixels) * 0.10F;
        Imgheight = (displayMetrics.widthPixels) * 0.15;

        adapter = new Speaker_adapter(SpeakerRoot.this, speakerlists, ImgWidth, Imgheight);

        speakerlistview.setAdapter(adapter);
        tileProvider = new LetterTileProvider(context);

//        setAds();

        initializeAd();
    }

    private void initializeAd() {
       /* adHeight = (int) ((displayMetrics.heightPixels) * 0.08);
        adWidth = (int) ((displayMetrics.widthPixels) * 0.08);*/
        System.out.println("Width and Height : " + adWidth + " " + adHeight);
        LinearLayout.LayoutParams layoutParams;
        layoutParams = new LinearLayout.LayoutParams(adWidth, adHeight);

        //adsImage.setLayoutParams(layoutParams);
        for (int i = 0; i < e.getTabs().length; i++)
            if (e.getTabs(i).getType().contains("banner")) {
                System.out.println("Banner Ad Available");
                ban_pos = i;
                if (e.getTabs(i).getItems().length > 0) {
                    setAds();
                    break;
                }
            } else
                cardView.setVisibility(View.GONE);
    }

    private void setAds() {
        cardView.setVisibility(View.VISIBLE);
       /* for (int i = 0; i < img_res.length; i++)
            img_arr.add(img_res[i]);*/
        final int img_size = e.getTabs(ban_pos).getItems().length;
        for (int i = 0; i < img_size; i++) {
            img_arr.add(e.getTabs(ban_pos).getItems(i).getBanner_url());
            butn_url_arr.add(e.getTabs(ban_pos).getItems(i).getButton_link());
            ban_type_arr.add(e.getTabs(ban_pos).getItems(i).getBanner_type());
            ban_name_arr.add(e.getTabs(ban_pos).getItems(i).getBanner_name());
            type_id_arr.add(e.getTabs(ban_pos).getItems(i).getType_id());
        }
        System.out.println("Banner Image Size : " + img_arr.size());
        mPager.setAdapter(new AdsImageAdapter(this,getApplicationContext(), img_arr, butn_url_arr, ban_type_arr, type_id_arr, ban_name_arr));
        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == img_size) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, false);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3000, 3000);
    }


   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        mSearchAction = menu.findItem(R.id.action_search);
        return super.onPrepareOptionsMenu(menu);
    }*/


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int i = item.getItemId();
        if (i == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (i == R.id.action_search) {
           // handleMenuSearch();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    protected void handleMenuSearch() {
        ActionBar action = getSupportActionBar(); //get the actionbar

        if (isSearchOpened) { //test if the search is open

            action.setDisplayShowCustomEnabled(false); //disable a custom view inside the actionbar
            action.setDisplayShowTitleEnabled(true); //show the title in the action bar

            //hides the keyboard
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(edtSeach.getWindowToken(), 0);

            //add the search icon in the action bar
            mSearchAction.setIcon(getResources().getDrawable(R.drawable.ic_action_action_search));

            isSearchOpened = false;
            adapter.getFilter().filter("");
        } else { //open the search entry

            action.setDisplayShowCustomEnabled(true); //enable it to display a
            // custom view in the action bar.
            action.setCustomView(R.layout.search_bar);//add the custom view
            action.setDisplayShowTitleEnabled(false); //hide the title

            edtSeach = (EditText) action.getCustomView().findViewById(R.id.edtSearch); //the text editor


            edtSeach.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    adapter.getFilter().filter(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });


            edtSeach.requestFocus();

            //open the keyboard focused in the edtSearch
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(edtSeach, InputMethodManager.SHOW_IMPLICIT);


            //add the close icon
            mSearchAction.setIcon(getResources().getDrawable(R.drawable.ic_action_navigation_close_inverted));

            isSearchOpened = true;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        SpannableString s = new SpannableString(title);
        setTitle(Util.applyFontToMenuItem(this, s));

    }

}
