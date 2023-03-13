package com.singleevent.sdk.View.RightActivity.admin.feeds.ui;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.singleevent.sdk.model.AppDetails;
import com.singleevent.sdk.R;

import io.paperdb.Paper;

public class FeedActivity extends AppCompatActivity implements View.OnClickListener{
    AppDetails appDetails;
    Toolbar toolbar;
    FrameLayout container2nd;
    protected TextView feed,blockedUsers,reportedFeeds;
    protected LinearLayout admin_panel;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Paper.init(this);
        setContentView(R.layout.act_feeds);

        appDetails = Paper.book().read("Appdetails");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        reportedFeeds = (TextView) findViewById(R.id.reportedFeeds);
        feed = (TextView) findViewById(R.id.feed);
        blockedUsers = (TextView) findViewById(R.id.blockedUsers);
        admin_panel = (LinearLayout) findViewById(R.id.admin_panel);
        toolbar.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));
        setSupportActionBar(toolbar);

        /* rectangle background for fragment buttons */
        ShapeDrawable shapedrawable1 = new ShapeDrawable();
        shapedrawable1.setShape(new RectShape());
        shapedrawable1.getPaint().setColor(Color.parseColor(appDetails.getTheme_color()));
        shapedrawable1.getPaint().setStrokeWidth(5f);
        shapedrawable1.getPaint().setStyle(Paint.Style.STROKE);
        admin_panel.setBackground(shapedrawable1);

        /*setting background of button*/
        setFeedBackgroundColor();

        container2nd = (FrameLayout) findViewById(R.id.container2nd);

        /* Calling feed for the first time */
        FeedFragment fragment = new FeedFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container2nd, fragment,"Feeds");
        // Commit the transaction
        transaction.commit();


        feed.setOnClickListener(this);
        reportedFeeds.setOnClickListener(this);
        blockedUsers.setOnClickListener(this);

    }



    @Override
    protected void onResume() {
        super.onResume();
        setTitle("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int i1 = item.getItemId();
        if (i1 == android.R.id.home) {
            onBackPressed();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id==R.id.feed){
            FeedFragment fragment = new FeedFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container2nd, fragment,"Feeds");
            // Commit the transaction
            transaction.commit();

            /*setting background of button*/
            setFeedBackgroundColor();

        }else if (id==R.id.reportedFeeds){
            ReportedFeedsFragment fragment = new ReportedFeedsFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container2nd, fragment,"ReportedFeed");
            // Commit the transaction
            transaction.commit();

            /*setting background of button*/
            setReportedFeedsBackgroundColor();
        }else if (id==R.id.blockedUsers){
            BlockedUsersFragment fragment = new BlockedUsersFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container2nd, fragment,"BlockedUsersFeed");
            // Commit the transaction
            transaction.commit();
            /*setting background of button*/
            setBlockedUserBackgroundColor();
        }
    }
    private void setFeedBackgroundColor( ){

        /*setting background of button*/

        feed.setTextColor(Color.parseColor("#FFFFFF"));
        feed.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));

        reportedFeeds.setTextColor(Color.parseColor(appDetails.getTheme_color()));
        blockedUsers.setTextColor(Color.parseColor(appDetails.getTheme_color()));

        ShapeDrawable shapedrawable1 = new ShapeDrawable();
        shapedrawable1.setShape(new RectShape());
        shapedrawable1.getPaint().setColor(Color.parseColor(appDetails.getTheme_color()));
        shapedrawable1.getPaint().setStrokeWidth(5f);
        shapedrawable1.getPaint().setStyle(Paint.Style.STROKE);
        reportedFeeds.setBackground(shapedrawable1);


        ShapeDrawable shapedrawable2 = new ShapeDrawable();
        shapedrawable2.setShape(new RectShape());
        shapedrawable2.getPaint().setColor(Color.parseColor(appDetails.getTheme_color()));
        shapedrawable2.getPaint().setStrokeWidth(5f);
        shapedrawable2.getPaint().setStyle(Paint.Style.STROKE);
        blockedUsers.setBackground(shapedrawable2);

    }
    private void setReportedFeedsBackgroundColor() {

        /*setting background of button*/

        reportedFeeds.setTextColor(Color.parseColor("#FFFFFF"));
        reportedFeeds.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));

        feed.setTextColor(Color.parseColor(appDetails.getTheme_color()));
        blockedUsers.setTextColor(Color.parseColor(appDetails.getTheme_color()));




        ShapeDrawable shapedrawable1 = new ShapeDrawable();
        shapedrawable1.setShape(new RectShape());
        shapedrawable1.getPaint().setColor(Color.parseColor(appDetails.getTheme_color()));
        shapedrawable1.getPaint().setStrokeWidth(5f);
        shapedrawable1.getPaint().setStyle(Paint.Style.STROKE);
        feed.setBackground(shapedrawable1);


        ShapeDrawable shapedrawable2 = new ShapeDrawable();
        shapedrawable2.setShape(new RectShape());
        shapedrawable2.getPaint().setColor(Color.parseColor(appDetails.getTheme_color()));
        shapedrawable2.getPaint().setStrokeWidth(5f);
        shapedrawable2.getPaint().setStyle(Paint.Style.STROKE);
        blockedUsers.setBackground(shapedrawable2);
    }

    private void setBlockedUserBackgroundColor() {
        /*setting background of button*/

        blockedUsers.setTextColor(Color.parseColor("#FFFFFF"));
        blockedUsers.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));

        feed.setTextColor(Color.parseColor(appDetails.getTheme_color()));
        reportedFeeds.setTextColor(Color.parseColor(appDetails.getTheme_color()));



        ShapeDrawable shapedrawable = new ShapeDrawable();
        shapedrawable.setShape(new RectShape());
        shapedrawable.getPaint().setColor(Color.parseColor(appDetails.getTheme_color()));
        shapedrawable.getPaint().setStrokeWidth(5f);
        shapedrawable.getPaint().setStyle(Paint.Style.STROKE);
        feed.setBackground(shapedrawable);



        ShapeDrawable shapedrawable1 = new ShapeDrawable();
        shapedrawable1.setShape(new RectShape());
        shapedrawable1.getPaint().setColor(Color.parseColor(appDetails.getTheme_color()));
        shapedrawable1.getPaint().setStrokeWidth(5f);
        shapedrawable1.getPaint().setStyle(Paint.Style.STROKE);
        reportedFeeds.setBackground(shapedrawable1);

    }
}
