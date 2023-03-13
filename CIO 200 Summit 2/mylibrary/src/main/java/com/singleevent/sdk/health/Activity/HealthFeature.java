package com.singleevent.sdk.health.Activity;

import android.actionsheet.demo.com.khoiron.actionsheetiosforandroid.ActionSheet;
import android.actionsheet.demo.com.khoiron.actionsheetiosforandroid.Interface.ActionSheetCallBack;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.makeramen.roundedimageview.RoundedImageView;
import com.singleevent.sdk.Custom_View.ColorFilterTransformation;
import com.singleevent.sdk.Custom_View.Util;
import com.singleevent.sdk.R;
import com.singleevent.sdk.health.Adapter.CustomAdapter;
import com.singleevent.sdk.health.Adapter.RecyclerViewAdapter;
import com.singleevent.sdk.health.Adapter.RecylerViewActiveChallengeAdapter;
import com.singleevent.sdk.health.Model.CustomItem;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static android.view.View.GONE;

public class HealthFeature extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private ArrayList<String> mImageNames = new ArrayList<>();
    private ArrayList<String> mImages = new ArrayList<>();
    private ArrayList<String> mTotalkm = new ArrayList<>();
    private ArrayList<Integer> mPosition = new ArrayList<>();
    RecyclerViewAdapter adapter;
    RecyclerView recyclerView;
  //  Spinner spinner,spinmode;
    Button btn,button6;
    //RecyclerView activeRecylerView;
    LinearLayout myEvents,myEvents1;
    float ImgWidth, ImgHeight, lwidth, lheight;
    ArrayList<CustomItem> customlist;





    private ArrayList<String> mImage = new ArrayList<>();
    private ArrayList<String> mName = new ArrayList<>();
    private ArrayList<String> mTotal = new ArrayList<>();
    RecylerViewActiveChallengeAdapter recylerViewActiveChallengeAdapter;
    //TextView txtActive;
 //   LinearLayout linearLayout;
   // Button btn_create,btn_join,btn_active_option;
   // ImageView backpress;

    String[] DayOfWeek = {"Average steps", "Total steps", "Average minute",
            "Total minute", "Average distance", "Total distance"};
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.healthfeature);
        myEvents = (LinearLayout) findViewById(R.id.llayout1);
        myEvents1 = (LinearLayout) findViewById(R.id.llayout11);
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        ImgWidth = displayMetrics.widthPixels * 0.30F;
        ImgHeight = ImgWidth * 1.7F;
        lwidth = ImgWidth * 0.70F;
        lheight = lwidth * 0.75F;
        ArrayList<String> data = new ArrayList<>();
        ArrayList<Integer>data1=new ArrayList<>();
        button6=(Button)findViewById(R.id.button6);
        showpreview();
        showpreview1();
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getApplicationContext(),ChallengeGoal.class);
                startActivity(in);
            }
        });
       // customlist=getCustomList();


       // CustomAdapter customAdapter=new CustomAdapter(this,customlist);

    }
    public ArrayList<CustomItem> getCustomList(){
        customlist=new ArrayList<>();

        customlist.add(new CustomItem("Total steps",R.drawable.steps));
        customlist.add(new CustomItem("Average steps",R.drawable.steps));
        customlist.add(new CustomItem("Total minutes",R.drawable.steps));
        customlist.add(new CustomItem("Average minutes",R.drawable.steps));
        customlist.add(new CustomItem("Total distance",R.drawable.steps));
        customlist.add(new CustomItem("Average steps",R.drawable.steps));
        return customlist;

    }



    private void showpreview(){
        int clogo_height = 0, margintop = 0;
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for (int i = 0; i < 3; i++) {
            final View child = inflater.inflate(R.layout.events_view_feature, null);
            RelativeLayout v2 = (RelativeLayout) child.findViewById(R.id.v2);

            RelativeLayout.LayoutParams logolayoutParams = (RelativeLayout.LayoutParams) v2.getLayoutParams();
            logolayoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;//(int) (ImgWidth * 1.30);
            logolayoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            logolayoutParams.setMargins(10, 0, 0, 0);
           // v2.setLayoutParams(logolayoutParams);
           // v2.setBackground(Util.setrounded(Color.WHITE));
           // GradientDrawable drawable1 = (GradientDrawable) v2.getBackground();
           // drawable1.setStroke (1, Color.GRAY);

            clogo_height = (int) (ImgHeight * 1.5);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams((int) (ImgWidth * 3), clogo_height);

            //used to set the center logo of the tile
            com.makeramen.roundedimageview.RoundedImageView clogo_center = (com.makeramen.roundedimageview.RoundedImageView) child.findViewById(R.id.logo2);
            //used to set the tile banner image
            com.makeramen.roundedimageview.RoundedImageView clogo = (RoundedImageView) child.findViewById(R.id.re_tile_logo);

            layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

            //setting tile banner
            if(i==0){
                Glide.with(getApplicationContext()).load(R.drawable.rect1)
                        .fitCenter()
                        .placeholder(R.drawable.stepcount)
                        .dontAnimate()
                        .error(R.drawable.stepcount)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .bitmapTransform(new ColorFilterTransformation(getApplicationContext(), Color.argb(0, 0, 0, 0)))
                        .into(clogo);}
            if(i==1){
                Glide.with(getApplicationContext()).load(R.drawable.rect2)
                        .fitCenter()
                        .placeholder(R.drawable.stepcount)
                        .dontAnimate()
                        .error(R.drawable.stepcount)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .bitmapTransform(new ColorFilterTransformation(getApplicationContext(), Color.argb(0, 0, 0, 0)))
                        .into(clogo);
            }
            if(i==2){
                Glide.with(getApplicationContext()).load(R.drawable.rect3)
                        .fitCenter()
                        .placeholder(R.drawable.stepcount)
                        .dontAnimate()
                        .error(R.drawable.stepcount)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .bitmapTransform(new ColorFilterTransformation(getApplicationContext(), Color.argb(0, 0, 0, 0)))
                        .into(clogo);
            }

            //clogo.setCornerRadius(7, 7, 0, 0);
         //   clogo.setLayoutParams(layoutParams);
            ImageView logo2 = (ImageView) child.findViewById(R.id.logo2);
            RelativeLayout logo2_layout = (RelativeLayout) child.findViewById(R.id.logo2_layout);
            if (this != null) {
                margintop = (int) (clogo_height - (getResources().getDimensionPixelSize(R.dimen.abc_action_bar_default_height_material) / 2));
            }
            RelativeLayout.LayoutParams logo2_layout_params = new RelativeLayout.LayoutParams((int) (ImgWidth * 0.45), (int) (ImgWidth * 0.45));
            logo2_layout_params.setMargins(0, margintop, 0, 0);
            logo2_layout_params.addRule(RelativeLayout.CENTER_HORIZONTAL);
          //  logo2_layout.setLayoutParams(logo2_layout_params);

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


            View split_View = (View) child.findViewById(R.id.split_view);
            RelativeLayout.LayoutParams split_view_params = (RelativeLayout.LayoutParams) split_View.getLayoutParams();
            split_view_params.height = (int) (ImgWidth * 3);
            split_View.setLayoutParams(split_view_params);

            //setting event title
            if(i==0){
                etitle.setText("Health challenge");}
            else if(i==1){
                etitle.setText("An Hour of Walking");}
            else{
                etitle.setText("5 min of Meditations");}

            //listing.getApp_title());
            //setting event category
            //    ecat.setText(listing.getApp_category());

            etitle.setTypeface(Util.boldtypeface(getApplicationContext()));
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
    private void showpreview1(){
        int clogo_height = 0, margintop = 0;
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for (int i = 0; i < 3; i++) {
            final View child = inflater.inflate(R.layout.events_view_feature, null);
            RelativeLayout v2 = (RelativeLayout) child.findViewById(R.id.v2);

            RelativeLayout.LayoutParams logolayoutParams = (RelativeLayout.LayoutParams) v2.getLayoutParams();
            logolayoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;//(int) (ImgWidth * 1.30);
            logolayoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            logolayoutParams.setMargins(10, 0, 0, 0);
            v2.setLayoutParams(logolayoutParams);
           /* GradientDrawable drawable = (GradientDrawable) v2.getBackground();
            drawable.setStroke (4, Color.LTGRAY); // Sets the border width and color
*/


            clogo_height = (int) (ImgHeight * 1.5);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams((int) (ImgWidth * 3), clogo_height);

            //used to set the center logo of the tile
            com.makeramen.roundedimageview.RoundedImageView clogo_center = (com.makeramen.roundedimageview.RoundedImageView) child.findViewById(R.id.logo2);
            //used to set the tile banner image
            com.makeramen.roundedimageview.RoundedImageView clogo = (RoundedImageView) child.findViewById(R.id.re_tile_logo);

            layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

            //setting tile banner
            if(i==0){
                Glide.with(getApplicationContext()).load(R.drawable.rect1)
                        .fitCenter()
                        .placeholder(R.drawable.stepcount)
                        .dontAnimate()
                        .error(R.drawable.stepcount)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .bitmapTransform(new ColorFilterTransformation(getApplicationContext(), Color.argb(0, 0, 0, 0)))
                        .into(clogo);}
            if(i==1){
                Glide.with(getApplicationContext()).load(R.drawable.rect2)
                        .fitCenter()
                        .placeholder(R.drawable.stepcount)
                        .dontAnimate()
                        .error(R.drawable.stepcount)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .bitmapTransform(new ColorFilterTransformation(getApplicationContext(), Color.argb(0, 0, 0, 0)))
                        .into(clogo);
            }
            if(i==2){
                Glide.with(getApplicationContext()).load(R.drawable.rect3)
                        .fitCenter()
                        .placeholder(R.drawable.stepcount)
                        .dontAnimate()
                        .error(R.drawable.stepcount)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .bitmapTransform(new ColorFilterTransformation(getApplicationContext(), Color.argb(0, 0, 0, 0)))
                        .into(clogo);
            }

            //clogo.setCornerRadius(7, 7, 0, 0);
            //   clogo.setLayoutParams(layoutParams);
            ImageView logo2 = (ImageView) child.findViewById(R.id.logo2);
            RelativeLayout logo2_layout = (RelativeLayout) child.findViewById(R.id.logo2_layout);
            if (this != null) {
                margintop = (int) (clogo_height - (getResources().getDimensionPixelSize(R.dimen.abc_action_bar_default_height_material) / 2));
            }
            RelativeLayout.LayoutParams logo2_layout_params = new RelativeLayout.LayoutParams((int) (ImgWidth * 0.45), (int) (ImgWidth * 0.45));
            logo2_layout_params.setMargins(0, margintop, 0, 0);
            logo2_layout_params.addRule(RelativeLayout.CENTER_HORIZONTAL);
            //  logo2_layout.setLayoutParams(logo2_layout_params);

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


            View split_View = (View) child.findViewById(R.id.split_view);
            RelativeLayout.LayoutParams split_view_params = (RelativeLayout.LayoutParams) split_View.getLayoutParams();
            split_view_params.height = (int) (ImgWidth * 2);
            split_View.setLayoutParams(split_view_params);

            //setting event title
            if(i==0){
                etitle.setText("Health challenge");}
            else if(i==1){
                etitle.setText("An Hour of Walking");}
            else{
                etitle.setText("5 min of Meditations");}

            //listing.getApp_title());
            //setting event category
            //    ecat.setText(listing.getApp_category());

            etitle.setTypeface(Util.boldtypeface(getApplicationContext()));
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
            myEvents1.addView(child);
            myEvents1.setVisibility(View.VISIBLE);

        }
    }
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
