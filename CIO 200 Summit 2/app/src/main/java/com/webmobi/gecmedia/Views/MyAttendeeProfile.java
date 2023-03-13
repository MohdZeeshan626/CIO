package com.webmobi.gecmedia.Views;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.singleevent.sdk.Custom_View.HeaderView;
import com.singleevent.sdk.Custom_View.Letter.LetterTileProvider;
import com.singleevent.sdk.Custom_View.Letter.Roundeddrawable;
import com.singleevent.sdk.Custom_View.Util;
import com.singleevent.sdk.model.LocalArraylist.ChatMSG;
import com.singleevent.sdk.model.User;
import com.webmobi.gecmedia.R;

import cyd.awesome.material.AwesomeText;
import io.paperdb.Paper;


public class MyAttendeeProfile extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener, View.OnClickListener {

    Bitmap letterTile;

    AppBarLayout appBarLayout;

    CollapsingToolbarLayout collapsingToolbarLayout;

    private float dpWidth;

    Toolbar toolbar;

    HeaderView toolbarHeaderView;

    HeaderView floatHeaderView;

    private boolean isHideToolbarView = false;

    User user;
    TextView subtitle;
    LetterTileProvider tileProvider;

    ImageView background, profilepic;

    AwesomeText chaticon;
    RelativeLayout chatview;
    TextView chattext, message;
    Button chatwith;

    String appid, appname;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Paper.init(this);
        setContentView(R.layout.s_attendee_profile);
        tileProvider = new LetterTileProvider(MyAttendeeProfile.this);
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        dpWidth = displayMetrics.widthPixels * 0.20F;

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        chattext = (TextView) findViewById(R.id.chattxt);
        message = (TextView) findViewById(R.id.message);
        chatview = (RelativeLayout) findViewById(R.id.chatview);
        appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        toolbarHeaderView = (HeaderView) findViewById(R.id.toolbar_header_view);
        subtitle = (TextView) toolbarHeaderView.findViewById(R.id.header_view_sub_title);
        floatHeaderView = (HeaderView) findViewById(R.id.float_header_view);
        background = (ImageView) findViewById(R.id.image);
        profilepic = (ImageView) findViewById(R.id.profilepic);
        chaticon = (AwesomeText) findViewById(R.id.chaticon);
        chatwith=(Button) findViewById(R.id.chatwithu);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        collapsingToolbarLayout.setTitle(" ");


        appBarLayout.addOnOffsetChangedListener(this);


        Bundle extras = getIntent().getExtras();
        if (extras == null)
            finish();

        // getting agenda details from fragment
        user = (User) getIntent().getSerializableExtra("UserItem"); //Obtaining data
        appid = getIntent().getStringExtra("appid");
        appname = getIntent().getStringExtra("appname");

        if (user.getProfile_pic().equalsIgnoreCase("")) {
            // setting background
            int newColor = (user.getColor() & 0x00FFFFFF) | (0x40 << 24);
//        int backgroundOpacity = 1 * 0x01000000;
            background.setBackgroundColor(newColor);
            background.setImageAlpha(50);
        } else {
            Glide.with(getApplicationContext())
                    .load(user.getProfile_pic())
                    .into(background);
            background.setImageAlpha(50);
        }

        // setting profilepic

        // setting profilepic its there

        if (user.getProfile_pic().equalsIgnoreCase("")) {
            letterTile = tileProvider.getLetterTile(user.getFirst_name().trim(), "key", (int) dpWidth, (int) dpWidth, user.getColor());
            profilepic.setImageDrawable(new Roundeddrawable(letterTile));

        } else {

            Glide.with(getApplicationContext())
                    .load(user.getProfile_pic())
                    .asBitmap()
                    .placeholder(com.singleevent.sdk.R.drawable.round_user)
                    .error(com.singleevent.sdk.R.drawable.round_user)
                    .into(new BitmapImageViewTarget(profilepic) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(getResources(),
                                    Bitmap.createScaledBitmap(resource, (int) dpWidth, (int) dpWidth, false));
                            drawable.setCircular(true);
                            profilepic.setImageDrawable(drawable);
                        }
                    });
        }


        toolbarHeaderView.setTitle(user.getFirst_name() + " " + user.getLast_name(), MyAttendeeProfile.this);
        toolbarHeaderView.setSubitle(user.getDesignation() + " " + user.getCompany(), MyAttendeeProfile.this);
        floatHeaderView.setTitle(user.getFirst_name() + " " + user.getLast_name(), MyAttendeeProfile.this);
        floatHeaderView.setSubitle(user.getDesignation() + " " + user.getCompany(), MyAttendeeProfile.this);

        chaticon.setBackground(Util.setdrawable(MyAttendeeProfile.this, R.drawable.round_selected, Color.parseColor("#0a6a99")));
      //  chattext.setText("Chat with " + user.getFirst_name());
        chatwith.setText("Chat with " + user.getFirst_name());
        message.setText("You're both attending " + appname);
        chattext.setTypeface(Util.regulartypeface(MyAttendeeProfile.this));
        message.setTypeface(Util.boldtypeface(MyAttendeeProfile.this));

        chatview.setOnClickListener(this);
        chatwith.setOnClickListener(this);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(verticalOffset) / (float) maxScroll;

        if (percentage == 1f && isHideToolbarView) {
            toolbarHeaderView.hideOrSetText(subtitle, "");
            toolbarHeaderView.setVisibility(View.VISIBLE);
            isHideToolbarView = !isHideToolbarView;

        } else if (percentage < 1f && !isHideToolbarView) {
            toolbarHeaderView.hideOrSetText(subtitle, "subtitle");
            toolbarHeaderView.setVisibility(View.GONE);
            isHideToolbarView = !isHideToolbarView;
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
    public void onClick(View view) {
        int i1 = view.getId();
        if (i1 == R.id.chatwithu) {
            Paper.book(appid).write("token", Paper.book().read("token", ""));
            ChatMSG msg = new ChatMSG("", user.getFirst_name() + " " + user.getLast_name(), user.getUserid(), "", "", "", user.getColor(), 0);
            Bundle args = new Bundle();
            args.putSerializable("UserItem", msg);
            args.putString("appid", appid);
            args.putString("appname", appname);
            Intent i = new Intent(MyAttendeeProfile.this, LocalMessageView.class);
            i.putExtras(args);
            startActivity(i);

        }

    }
}

