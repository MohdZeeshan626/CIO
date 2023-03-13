package com.singleevent.sdk.View.LeftActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.singleevent.sdk.Custom_View.Util;
import com.singleevent.sdk.model.AppDetails;
import com.singleevent.sdk.model.Events;
import com.singleevent.sdk.model.Items;
import com.singleevent.sdk.R;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cyd.awesome.material.AwesomeText;
import io.paperdb.Paper;

/**
 * Created by Admin on 7/6/2017.
 */

public class VideoActivity extends AppCompatActivity {

    AppDetails appDetails;
    int pos;
    private String title;
    private ArrayList<Events> events = new ArrayList<Events>();
    Events e;
    ArrayList<Items> videolists = new ArrayList<>();
    LinearLayout listview;
    TextView novideos;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Paper.init(this);
        setContentView(R.layout.s_attachmentroot);
        novideos = (TextView) findViewById(R.id.novideos);
        appDetails = Paper.book().read("Appdetails");
      
        listview = (LinearLayout) findViewById(R.id.contains);

        listview.removeAllViews();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));
        setSupportActionBar(toolbar);

        // getting the data from previous activity

        if (getIntent().getExtras() == null)
            finish();

        pos = getIntent().getExtras().getInt("pos");
        title = getIntent().getExtras().getString("title");
        events = Paper.book().read("Appevents");
        e = events.get(0);

        videolists.clear();
        for (int j = 0; j < e.getTabs(pos).getItemsSize(); j++) {
            videolists.add(e.getTabs(pos).getItems(j));
        }

        listview = (LinearLayout) findViewById(R.id.contains);

        showvideolist();
    }
    Bitmap image;
    private void getBItmapFromUrl(final String imgUrl ){




    }

    private void showvideolist() {

        if (videolists.size() > 0) {

            listview.removeAllViews();

            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            for (int i = 0; i < videolists.size(); i++) {

                final Items item = videolists.get(i);
                final View child1 = inflater.inflate(R.layout.s_attachement_video_view, null);
                TextView txtmedianame = (TextView) child1.findViewById(R.id.medianame);
                TextView txtmediaurl = (TextView) child1.findViewById(R.id.mediaurl);
                AwesomeText logo = (AwesomeText) child1.findViewById(R.id.logo);

                int id = 1;
                ImageView iv = (ImageView ) child1.findViewById(R.id.iv_thmb);
                String pattern = "(?<=watch\\?v=|/videos/|embed\\/)[^#\\&\\?]*";

                Pattern compiledPattern = Pattern.compile(pattern);
                Matcher matcher = compiledPattern.matcher(item.getAttachment_url());

                /*http://img.youtube.com/vi/GDFUdMvacI0/3.jpg*/
                if(matcher.find()){
                    Glide.with(getApplicationContext()).
                            load("http://img.youtube.com/vi/"+matcher.group()+"/0.jpg")
                            .into(iv);
                }else {
                    iv.setBackground(getResources().getDrawable(R.drawable.thumbnail));
                }



               /* final AwesomeText download = (AwesomeText) child1.findViewById(R.id.idownload);*/
                txtmedianame.setText(item.getAttachment_name());
                txtmedianame.setTypeface(Util.regulartypeface(VideoActivity.this));
                txtmediaurl.setTypeface(Util.regulartypeface(VideoActivity.this));
                txtmediaurl.setText(item.getAttachment_url());

          /*  logo.setTextColor(Color.parseColor(appDetails.getTheme_color()));
            logo.setFontAwesomeIcon(FontCharacterMaps.FontAwesome.FA_FILE_VIDEO_O);
            download.setTextColor(Color.parseColor(appDetails.getTheme_color()));
            download.setPixedenStrokeIcon(FontCharacterMaps.Pixeden.PE_ANGLE_RIGHT);*/

            child1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(VideoActivity.this, VideoView.class);
                    i.putExtra("url", item.getAttachment_url());
                    i.putExtra("title", item.getAttachment_name());
                    startActivity(i);
                }
            });

                listview.addView(child1);

            }
            novideos.setVisibility(View.GONE);
            listview.setVisibility(View.VISIBLE);
        } else {

            listview.setVisibility(View.GONE);
            novideos.setVisibility(View.VISIBLE);
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

    @Override
    protected void onResume() {
        super.onResume();

        SpannableString s = new SpannableString(title);
        setTitle(Util.applyFontToMenuItem(this, s));

    }
}
