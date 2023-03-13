package com.singleevent.sdk.health.Activity;

import android.actionsheet.demo.com.khoiron.actionsheetiosforandroid.ActionSheet;
import android.actionsheet.demo.com.khoiron.actionsheetiosforandroid.Interface.ActionSheetCallBack;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Bundle;
import android.text.SpannableString;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.tabs.TabLayout;
import com.google.common.io.Files;
import com.makeramen.roundedimageview.RoundedImageView;
import com.singleevent.sdk.Custom_View.ColorFilterTransformation;
import com.singleevent.sdk.Custom_View.Util;
import com.singleevent.sdk.R;
import com.singleevent.sdk.View.Fragment.Left_Fragment.CommentFragment;
import com.singleevent.sdk.View.RightActivity.admin.feeds.ui.BlockedUsersFragment;
import com.singleevent.sdk.View.RightActivity.admin.feeds.ui.FeedFragment;
import com.singleevent.sdk.View.RightActivity.admin.feeds.ui.ReportedFeedsFragment;
import com.singleevent.sdk.health.Adapter.CustomAdapter;
import com.singleevent.sdk.health.Adapter.MyAdapter;
import com.singleevent.sdk.health.Adapter.RecyclerViewAdapter;
import com.singleevent.sdk.health.Adapter.RecylerViewActiveChallengeAdapter;
import com.singleevent.sdk.health.Fragment.ActiveFragment;
import com.singleevent.sdk.health.Fragment.CompleteFragment;
import com.singleevent.sdk.health.Fragment.LiveFragment;
import com.singleevent.sdk.health.Fragment.UpcomingFragment;
import com.singleevent.sdk.health.Model.CustomItem;
import com.singleevent.sdk.model.AppDetails;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import io.paperdb.Paper;

import static android.view.View.GONE;

public class MainChallengeView extends AppCompatActivity implements AdapterView.OnItemSelectedListener , View.OnClickListener {

    private ArrayList<String> mImageNames = new ArrayList<>();
    private ArrayList<String> mImages = new ArrayList<>();
    private ArrayList<String> mTotalkm = new ArrayList<>();
    private ArrayList<Integer> mPosition = new ArrayList<>();
    RecyclerViewAdapter adapter;
    RecyclerView recyclerView;
    //Spinner spinner, spinmode;
    Button btn;
    RecyclerView activeRecylerView;
    LinearLayout myEvents;
    float ImgWidth, ImgHeight, lwidth, lheight;
    TabLayout tabLayout;
    ViewPager viewPager;
    ArrayList<CustomItem> customlist;


    private ArrayList<String> mImage = new ArrayList<>();
    private ArrayList<String> mName = new ArrayList<>();
    private ArrayList<String> mTotal = new ArrayList<>();
    RecylerViewActiveChallengeAdapter recylerViewActiveChallengeAdapter;
    Toolbar toolbar;
    protected TextView live,upcoming,completed;
    LinearLayout admin_panel;
    AppDetails appDetails;
    FrameLayout container2nd;
   // TextView txtActive;
    LinearLayout live1,upcoming1,completed1,llayout1;
    RelativeLayout r1,r2;
   // Button btn_create, btn_join, btn_active_option;
  //  ImageView backpress;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainchallengeview);
        appDetails = Paper.book().read("Appdetails");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        upcoming = (TextView) findViewById(R.id.upcoming);
        live = (TextView) findViewById(R.id.live);
        completed = (TextView) findViewById(R.id.completed);
        admin_panel = (LinearLayout) findViewById(R.id.admin_panel);
        live1=(LinearLayout)findViewById(R.id.live1);
        upcoming1=(LinearLayout)findViewById(R.id.upcoming1);
        completed1=(LinearLayout)findViewById(R.id.completed1);
        llayout1=(LinearLayout)findViewById(R.id.llayout1);
        toolbar.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));
        myEvents = (LinearLayout) findViewById(R.id.llayout1);
       // r1=(RelativeLayout)findViewById(R.id.r1);
       // r2=(RelativeLayout)findViewById(R.id.r2);
       // r1.setBackground(Util.setrounded(Color.parseColor("#14a8b0")));
       // r2.setBackground(Util.setrounded(Color.parseColor("#14a8b0")));

        setSupportActionBar(toolbar);
        showpreview();


        GradientDrawable drawable = (GradientDrawable) live1.getBackground();
        drawable.setStroke (4, Color.parseColor ("#000000")); // Sets the border width and color
        live.setTextColor(Color.parseColor("#000000"));


      //  imgpublic.setColorFilter(Color.parseColor(appDetails.getTheme_color()));
      //  gp_text1.setTextColor(Color.parseColor("#203142"));
      //  imgpublic1.setColorFilter(Color.parseColor("#203142"));
       // private_privacy.setBackground(Util.setrounded(Color.WHITE));
      ///  GradientDrawable drawable1 = (GradientDrawable) private_privacy.getBackground();
      //  drawable1.setStroke (1, Color.GRAY);

        /*setting background of button*/
      //  setFeedBackgroundColor();

        container2nd = (FrameLayout) findViewById(R.id.container2nd);

        /* Calling feed for the first time */
        LiveFragment fragment = new LiveFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container2nd, fragment,"Feeds");
        // Commit the transaction
        transaction.commit();


        live1.setOnClickListener(this);
        upcoming1.setOnClickListener(this);
        completed1.setOnClickListener(this);


        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        ImgWidth = displayMetrics.widthPixels * 0.30F;
        ImgHeight = ImgWidth * 1.7F;
        lwidth = ImgWidth * 0.70F;
        lheight = lwidth * 0.75F;


      //  showpreview();
        //initImages();
       /* customlist = getCustomList();


        CustomAdapter customAdapter = new CustomAdapter(this, customlist);
        if (spinmode != null) {
            spinmode.setAdapter(customAdapter);
            spinmode.setOnItemSelectedListener(this);
        }*/
    }
    private void showpreview(){
        int clogo_height = 0, margintop = 0;
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for (int i = 0; i < 3; i++) {
            final View child = inflater.inflate(R.layout.challengeadapter, null);


            RelativeLayout v2 = (RelativeLayout) child.findViewById(R.id.m0);
            TextView cname=(TextView)child.findViewById(R.id.cname);
            Button views=(Button)child.findViewById(R.id.views);
            views.setBackground(Util.setrounded(Color.DKGRAY));
//             GradientDrawable drawable = (GradientDrawable) v2.getBackground();
  //            drawable.setStroke (2, Color.GRAY); // Sets the border width and color

            //v2.setBackground(Util.setrounded(Color.WHITE));
            RelativeLayout.LayoutParams logolayoutParams = (RelativeLayout.LayoutParams) v2.getLayoutParams();
            logolayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;//(int) (ImgWidth * 1.30);
            logolayoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            logolayoutParams.setMargins(5, 0, 0, 0);
            v2.setLayoutParams(logolayoutParams);
            LinearLayout m3=child.findViewById(R.id.m3);
            LinearLayout m4=child.findViewById(R.id.m4);
            m3.setBackground(Util.setrounded(Color.parseColor("#ff8c80f8")));
            m4.setBackground(Util.setrounded(Color.parseColor("#ff8c80f8")));

            clogo_height = (int) (ImgHeight * 0.9);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams((int) (ImgWidth * 2), clogo_height);

            //used to set the center logo of the tile
            //com.makeramen.roundedimageview.RoundedImageView clogo_center = (com.makeramen.roundedimageview.RoundedImageView) child.findViewById(R.id.logo2);
            //used to set the tile banner image
          //  com.makeramen.roundedimageview.RoundedImageView clogo = (RoundedImageView) child.findViewById(R.id.re_tile_logo);

            layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

            if(i==1){
                cname.setText("Team Workout");
            }
            //setting tile banner
           /* if(i==0 ||i==2){
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
*/            Button challengecomplete=(Button)child.findViewById(R.id.views);
            challengecomplete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent I=new Intent(MainChallengeView.this,SeperateChallenge.class);
                    startActivity(I);
                }
            });


          //  View split_View = (View) child.findViewById(R.id.split_view);
          ////  RelativeLayout.LayoutParams split_view_params = (RelativeLayout.LayoutParams) split_View.getLayoutParams();
           // split_view_params.height = (int) (ImgWidth * 2);
           // split_View.setLayoutParams(split_view_params);
            //setting event title
           /* if(i==0){
                etitle.setText("Health challenge");}
            else if(i==1){
                etitle.setText("An Hour of Walking");}
            else{
                etitle.setText("5 min of Meditations");}*/

            //listing.getApp_title());
            //setting event category
            //    ecat.setText(listing.getApp_category());

           // etitle.setTypeface(Util.boldtypeface(getApplicationContext()));
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

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id==R.id.live1){

            GradientDrawable drawable = (GradientDrawable) live1.getBackground();
            drawable.setStroke (4, Color.parseColor ("#000000")); // Sets the border width and color
            live.setTextColor(Color.parseColor("#000000"));


            GradientDrawable drawable1 = (GradientDrawable) upcoming1.getBackground();
            drawable1.setStroke (2, Color.LTGRAY); // Sets the border width and color
            upcoming.setTextColor(Color.LTGRAY);

            GradientDrawable drawable2 = (GradientDrawable) completed1.getBackground();
            drawable2.setStroke (2, Color.LTGRAY); // Sets the border width and color
            completed.setTextColor(Color.LTGRAY);

            LiveFragment fragment = new LiveFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container2nd, fragment,"Feeds");
            // Commit the transaction
            transaction.commit();

            /*setting background of button*/
         //   setFeedBackgroundColor();

        }else if (id==R.id.upcoming1){

            GradientDrawable drawable = (GradientDrawable) upcoming1.getBackground();
            drawable.setStroke (4, Color.parseColor ("#000000")); // Sets the border width and color
            upcoming.setTextColor(Color.parseColor ("#000000"));


            GradientDrawable drawable1 = (GradientDrawable) live1.getBackground();
            drawable1.setStroke (2, Color.LTGRAY); // Sets the border width and color
            live.setTextColor(Color.LTGRAY);

            GradientDrawable drawable2 = (GradientDrawable) completed1.getBackground();
            drawable2.setStroke (2, Color.LTGRAY); // Sets the border width and color
            completed.setTextColor(Color.LTGRAY);

            UpcomingFragment fragment = new UpcomingFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container2nd, fragment,"ReportedFeed");
            // Commit the transaction
            transaction.commit();

            /*setting background of button*/
            //setReportedFeedsBackgroundColor();
        }else if (id==R.id.completed1){

            GradientDrawable drawable = (GradientDrawable) completed1.getBackground();
            drawable.setStroke (4, Color.parseColor ("#000000")); // Sets the border width and color
            completed.setTextColor(Color.parseColor ("#000000"));


            GradientDrawable drawable1 = (GradientDrawable) live1.getBackground();
            drawable1.setStroke (2, Color.LTGRAY); // Sets the border width and color
            live.setTextColor(Color.LTGRAY);

            GradientDrawable drawable2 = (GradientDrawable) upcoming1.getBackground();
            drawable2.setStroke (2, Color.LTGRAY); // Sets the border width and color
            upcoming.setTextColor(Color.LTGRAY);


            CompleteFragment fragment = new CompleteFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container2nd, fragment,"BlockedUsersFeed");
            // Commit the transaction
            transaction.commit();
            /*setting background of button*/
           // setBlockedUserBackgroundColor();
        }


    }
    @Override
    protected void onResume() {
        super.onResume();
        SpannableString s = new SpannableString("Leaderboard");
        setTitle(Util.applyFontToMenuItem(this, s));
    }
}

        //    getSupportActionBar().hide();

