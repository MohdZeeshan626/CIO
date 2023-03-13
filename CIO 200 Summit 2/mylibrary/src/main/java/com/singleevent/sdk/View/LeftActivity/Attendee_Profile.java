package com.singleevent.sdk.View.LeftActivity;

import android.app.ProgressDialog;
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
import android.text.Html;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.gson.Gson;
import com.makeramen.roundedimageview.RoundedImageView;
import com.singleevent.sdk.ApiList;
import com.singleevent.sdk.App;
import com.singleevent.sdk.Custom_View.Error_Dialog;
import com.singleevent.sdk.Custom_View.HeaderView;
import com.singleevent.sdk.Custom_View.Letter.LetterTileProvider;
import com.singleevent.sdk.Custom_View.Letter.Roundeddrawable;
import com.singleevent.sdk.Custom_View.Util;
import com.singleevent.sdk.Custom_View.VolleyErrorLis;
import com.singleevent.sdk.model.AppDetails;
import com.singleevent.sdk.model.LocalArraylist.ChatMSG;
import com.singleevent.sdk.model.Schedule;
import com.singleevent.sdk.model.User;
import com.singleevent.sdk.R;
import com.singleevent.sdk.View.Fragment.Left_Fragment.MeetingDialogFragment;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cyd.awesome.material.AwesomeText;
import io.paperdb.Paper;

/**
 * Created by Admin on 6/20/2017.
 */

public class Attendee_Profile extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener, View.OnClickListener {

    private static final String TAG = Attendee_Profile.class.getSimpleName();
    Bitmap letterTile;
    AppBarLayout appBarLayout;
    CollapsingToolbarLayout collapsingToolbarLayout;
    private float dpWidth;
    Toolbar toolbar;
    HeaderView toolbarHeaderView;
    HeaderView floatHeaderView;
    private boolean isHideToolbarView = false;
    Button chatwith;

    User user;
    TextView subtitle;
    LetterTileProvider tileProvider;
    TextView des,ucompany;
    ImageView background;
    RoundedImageView profilepic;
    AwesomeText chaticon, inv;
    AppDetails appDetails;
    RelativeLayout chatview, addinvite;
    TextView chattext, message, blog, txtinvite,city2;

    private ArrayList<Schedule> schlist = new ArrayList<Schedule>();
    private String attendee_option="1";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Paper.init(this);
        setContentView(R.layout.s_attendee_profile);
        appDetails = Paper.book().read("Appdetails");

      //  attendee_option = Paper.book().read("attendee_option","");

        tileProvider = new LetterTileProvider(Attendee_Profile.this);
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        dpWidth = displayMetrics.widthPixels * 0.20F;

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        chattext = (TextView) findViewById(R.id.chattxt);
        message = (TextView) findViewById(R.id.message);
        city2=(TextView)findViewById(R.id.city2);
        ucompany=(TextView)findViewById(R.id.ucompany);
        des = (TextView) findViewById(R.id.des);
        chatview = (RelativeLayout) findViewById(R.id.chatview);
        addinvite = (RelativeLayout) findViewById(R.id.addinvite);
        chatwith=(Button) findViewById(R.id.chatwithu);

        appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        toolbarHeaderView = (HeaderView) findViewById(R.id.toolbar_header_view);
        subtitle = (TextView) toolbarHeaderView.findViewById(R.id.header_view_sub_title);
        floatHeaderView = (HeaderView) findViewById(R.id.float_header_view);
        background = (ImageView) findViewById(R.id.image);
        profilepic = (RoundedImageView) findViewById(R.id.profilepic);
        chaticon = (AwesomeText) findViewById(R.id.chaticon);
        inv = (AwesomeText) findViewById(R.id.inv);
        blog = (TextView) findViewById(R.id.blog);
        txtinvite = (TextView) findViewById(R.id.txtinvite);

        des.setTypeface(Util.regulartypeface(this));
        txtinvite.setTypeface(Util.regulartypeface(this));

        txtinvite.setTextColor(Color.parseColor(appDetails.getTheme_color()));
        inv.setTextColor(Color.parseColor(appDetails.getTheme_color()));

        collapsingToolbarLayout.setTitleEnabled(false);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));
        collapsingToolbarLayout.setTitle(" ");
        appBarLayout.addOnOffsetChangedListener(this);

        boolean isMeetingEnabled=Paper.book(appDetails.getAppId()).read("isMeetingEnabled", false);

        if(isMeetingEnabled)
            addinvite.setVisibility(View.VISIBLE);
        else
            addinvite.setVisibility(View.GONE);
        Bundle extras = getIntent().getExtras();
        if (extras == null)
            finish();

        // getting agenda details from fragment
        user = (User) getIntent().getSerializableExtra("UserItem"); //Obtaining data


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

        if (user.getProfile_pic().equalsIgnoreCase("") && !user.getFirst_name().equals("")) {
            letterTile = tileProvider.getLetterTile(user.getFirst_name(), "key", (int) dpWidth,
                    (int) dpWidth, user.getColor());
            profilepic.setImageBitmap((letterTile));
            profilepic.setCornerRadius(8, 8, 8, 8);

        } else {

            Glide.with(getApplicationContext())

                    .load(user.getProfile_pic())
                    .asBitmap()
                    .placeholder(R.drawable.round_user)
                    .error(R.drawable.round_user)
                    .into(profilepic);
                       /* @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(getResources(),
                                    Bitmap.createScaledBitmap(resource, (int) dpWidth, (int) dpWidth, false));
                            drawable.setCircular(true);
                            profilepic.setImageDrawable(drawable);
                        }
                    });*/
            profilepic.setCornerRadius(8, 8, 8, 8);
        }


        toolbarHeaderView.setTitle(user.getFirst_name() + " " + user.getLast_name(), Attendee_Profile.this);
        toolbarHeaderView.setSubitle(user.getDesignation() + " " + user.getCompany(), Attendee_Profile.this);
        floatHeaderView.setTitle(user.getFirst_name() + " " + user.getLast_name(), Attendee_Profile.this);
        floatHeaderView.setSubitle(user.getDesignation() + " " + user.getCompany(), Attendee_Profile.this);

        chaticon.setBackground(Util.setdrawable(Attendee_Profile.this, R.drawable.round_selected,
                Color.parseColor(appDetails.getTheme_color())));
       // chattext.setText("Chat with " + user.getFirst_name() + " " + user.getLast_name());
        chatwith.setText("Chat with " + user.getFirst_name());
        message.setText("You're both attending " + appDetails.getAppName());
        chattext.setTypeface(Util.regulartypeface(Attendee_Profile.this));
        message.setTypeface(Util.boldtypeface(Attendee_Profile.this));
        chatwith.setBackground(Util.setdrawable(Attendee_Profile.this, R.drawable.healthpostbut, Color.parseColor(appDetails.getTheme_color())));

        chatview.setOnClickListener(this);
        chatwith.setOnClickListener(this);

        if (appDetails.getDisable_items()!=null) {
            String[] ischat = appDetails.getDisable_items();
            for (int i = 0; i < ischat.length; i++) {
                if (ischat[i].equalsIgnoreCase("chat")) {
                    chatview.setVisibility(View.GONE);
                    break;
                }
            }
        }

        // setting description and v blog

        if (!user.getDescription().equalsIgnoreCase("")) {
            des.setText(Html.fromHtml(user.getDescription()));
            des.setVisibility(View.VISIBLE);
            des.setMovementMethod(LinkMovementMethod.getInstance());
        } else {
            des.setText("No Description Found");
        }

        if (user.getUser_blog()!= null && !user.getUser_blog().equalsIgnoreCase("")) {
            blog.setText(Html.fromHtml(user.getUser_blog()));
        } else {
            blog.setText("No Url Found");
        }
        if(user.getCity()!=null && !user.getCity().equalsIgnoreCase("")){
            city2.setText(Html.fromHtml(user.getCity()));
        }
        else{
            city2.setText("No City Found");
        }

        if(user.getCompany()!=null && !user.getCompany().equalsIgnoreCase(""))
        {
            ucompany.setText((user.getCompany()));
        }

        addinvite.setOnClickListener(this);
        //hide unhide meeting schedule functionality based on addon modules
        String [] schedule = appDetails.getAddon_modules();
        if (schedule!=null && schedule.length > 0){
            for (int index=0;index<schedule.length;index++){
                if("schedule".equals(schedule[index])){
                    // Log.v(TAG,schedule[index]);
                    addinvite.setVisibility(View.VISIBLE);
                    break;
                }else {
                    addinvite.setVisibility(View.GONE);
                }
            }

        }
        else {
            addinvite.setVisibility(View.GONE);
        }
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

        if (Math.abs(verticalOffset) > 200) {
            // appBarExpanded = false;
            toolbarHeaderView.setVisibility(View.VISIBLE);
            invalidateOptionsMenu();
        } else {
            //appBarExpanded = true;
            toolbarHeaderView.setVisibility(View.GONE);
            invalidateOptionsMenu();
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {


        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setTitle(Util.applyFontToMenuItem(this,new SpannableString("Profile")));
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

            if (attendee_option.equals("1")){
                ChatMSG msg = new ChatMSG("", user.getFirst_name() + " " + user.getLast_name(), user.getUserid(), "", "", "", user.getColor(), 0);
                Bundle args = new Bundle();
                args.putSerializable("UserItem", msg);
                Intent i = new Intent(Attendee_Profile.this, MessageView.class);
                i.putExtras(args);
                startActivity(i);
            }else {
                Error_Dialog.show("Please, Opt-in first to start chat.", this);
            }


        } else if (i1 == R.id.addinvite){
            if (attendee_option.equals("1")){
                getTime_Slot();
            }else {
                Error_Dialog.show("Please, Opt-in first to invite meeting.", this);
            }
        }



    }

    private void getTime_Slot() {

        final ProgressDialog dialog = new ProgressDialog(Attendee_Profile.this, R.style.MyAlertDialogStyle);
        dialog.setMessage("Fetching the TimeSlot....");
        dialog.show();
        String tag_string_req = "Getting_Slot";
        String url = ApiList.Get_TimeSlot + appDetails.getAppId() + "&userid=" + user.getUserid();
        StringRequest strReq = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                try {
                    System.out.println(response);
                    JSONObject jObj = new JSONObject(response);

                    if (jObj.getBoolean("response")) {
                        parsetimeslot(jObj.getJSONArray("timeslots"));
                    } else {
                        if (jObj.getString("responseString").equalsIgnoreCase("Invalid token")) {
                            Error_Dialog.show("Session is Expired Please Login", Attendee_Profile.this);


                        } else
                            Error_Dialog.show(jObj.getString("responseString"), Attendee_Profile.this);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                if (error instanceof TimeoutError) {
                    Error_Dialog.show("Timeout", Attendee_Profile.this);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, Attendee_Profile.this), Attendee_Profile.this);
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show("Please Check Your Internet Connection", Attendee_Profile.this);
                }

            }
        });


        App.getInstance().addToRequestQueue(strReq, tag_string_req);
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));


    }

    private void parsetimeslot(JSONArray args) {
        schlist = new ArrayList<>();
        Gson gson = new Gson();

        for (int i = 0; i < args.length(); i++) {

            try {
                String eventString = args.getJSONObject(i).toString();
                Schedule eobj = gson.fromJson(eventString, Schedule.class);
                schlist.add(eobj);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (schlist.size() > 0) {
            Paper.book().write("schlist", schlist);
            MeetingDialogFragment.newInstance(user).show(getSupportFragmentManager(), null);


        }
    }
}
