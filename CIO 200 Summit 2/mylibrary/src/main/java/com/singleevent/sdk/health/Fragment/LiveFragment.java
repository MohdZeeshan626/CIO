package com.singleevent.sdk.health.Fragment;

import android.actionsheet.demo.com.khoiron.actionsheetiosforandroid.ActionSheet;
import android.actionsheet.demo.com.khoiron.actionsheetiosforandroid.Interface.ActionSheetCallBack;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.singleevent.sdk.Custom_View.Util;
import com.singleevent.sdk.R;
import com.singleevent.sdk.health.Activity.CreateChallengeActivity;
import com.singleevent.sdk.health.Activity.JoinChallenge;
import com.singleevent.sdk.health.Activity.MainChallengeView;
import com.singleevent.sdk.health.Activity.SeperateChallenge;
import com.singleevent.sdk.health.Adapter.RecylerViewActiveChallengeAdapter;
import com.singleevent.sdk.model.AppDetails;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.paperdb.Paper;

import static android.view.View.GONE;

public class LiveFragment  extends Fragment {

    RecyclerView activeRecylerView;
    RecylerViewActiveChallengeAdapter recylerViewActiveChallengeAdapter;


    private ArrayList<String> mPosition = new ArrayList<>();
    private ArrayList<String> mImage = new ArrayList<>();
    private ArrayList<String> mName = new ArrayList<>();
    private ArrayList<String> mTotal = new ArrayList<>();
   Button views;
   LinearLayout m3,m4;
    float ImgWidth, ImgHeight, lwidth, lheight;
    AppDetails appDetails;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.challengeadapter, container, false);
        views=v.findViewById(R.id.views);
        m3=v.findViewById(R.id.m3);
        m4=v.findViewById(R.id.m4);
        appDetails = Paper.book().read("Appdetails");
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        ImgWidth = displayMetrics.widthPixels * 0.30F;
        ImgHeight = ImgWidth * 1.7F;
        lwidth = ImgWidth * 0.70F;
        lheight = lwidth * 0.75F;
        views.setBackground(Util.setrounded(Color.LTGRAY));
       // m3.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));
       // m4.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));

        /*GradientDrawable drawable = (GradientDrawable) m3.getBackground();
        drawable.setStroke (2, Color.GRAY); // Sets the border width and color

        GradientDrawable drawable1 = (GradientDrawable) m4.getBackground();
        drawable1.setStroke (2, Color.RED); // Sets the border width and color*/



        views.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getActivity(), SeperateChallenge.class);
                startActivity(in);
            }
        });


/*
        private void showpreview(){
            int clogo_height = 0, margintop = 0;

            for (int i = 0; i < 3; i++) {



                RelativeLayout v2 = (RelativeLayout) v.findViewById(R.id.m0);
                RelativeLayout.LayoutParams logolayoutParams = (RelativeLayout.LayoutParams) v2.getLayoutParams();
                logolayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;//(int) (ImgWidth * 1.30);
                logolayoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                logolayoutParams.setMargins(10, 0, 0, 0);
                v2.setLayoutParams(logolayoutParams);

                clogo_height = (int) (ImgHeight * 0.9);
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams((int) (ImgWidth * 2), clogo_height);

                //used to set the center logo of the tile
                //com.makeramen.roundedimageview.RoundedImageView clogo_center = (com.makeramen.roundedimageview.RoundedImageView) child.findViewById(R.id.logo2);
                //used to set the tile banner image
                //  com.makeramen.roundedimageview.RoundedImageView clogo = (RoundedImageView) child.findViewById(R.id.re_tile_logo);

                layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

                //setting tile banner
           */
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
*//*
            Button challengecomplete=(Button)v.findViewById(R.id.views);
                challengecomplete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent I=new Intent(getActivity(),SeperateChallenge.class);
                        startActivity(I);
                    }
                });


                //  View split_View = (View) child.findViewById(R.id.split_view);
                ////  RelativeLayout.LayoutParams split_view_params = (RelativeLayout.LayoutParams) split_View.getLayoutParams();
                // split_view_params.height = (int) (ImgWidth * 2);
                // split_View.setLayoutParams(split_view_params);
                //setting event title
           */
/* if(i==0){
                etitle.setText("Health challenge");}
            else if(i==1){
                etitle.setText("An Hour of Walking");}
            else{
                etitle.setText("5 min of Meditations");}*//*


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
                v.setId(i);
                v.setTag(i);

                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int pos = (int) view.getTag();

                        Toast.makeText(getActivity(), "Information not available", Toast.LENGTH_LONG).show();

                    }
                });


              //  myEvents.addView(v);
             //   myEvents.setVisibility(View.VISIBLE);

            }
        }
*/



        return v;

    }


    private void initRecylcer() {
        System.out.println("calling function initRecycler");
        recylerViewActiveChallengeAdapter = new RecylerViewActiveChallengeAdapter(mPosition,mImage,mName,mTotal,getContext());
        activeRecylerView.setAdapter(recylerViewActiveChallengeAdapter);
        activeRecylerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recylerViewActiveChallengeAdapter.notifyDataSetChanged();
    }


}
