package com.singleevent.sdk.View.LeftActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.makeramen.roundedimageview.RoundedImageView;
import com.singleevent.sdk.ApiList;
import com.singleevent.sdk.App;
import com.singleevent.sdk.Custom_View.Error_Dialog;
import com.singleevent.sdk.Custom_View.Util;
import com.singleevent.sdk.Custom_View.VolleyErrorLis;
import com.singleevent.sdk.model.AppDetails;
import com.singleevent.sdk.model.Events;
import com.singleevent.sdk.model.Items;
import com.singleevent.sdk.model.LocalArraylist.Notes;
import com.singleevent.sdk.model.Schedule;
import com.singleevent.sdk.model.User;
import com.singleevent.sdk.R;
import com.singleevent.sdk.View.Fragment.Left_Fragment.MeetingDialogFragment;
import com.singleevent.sdk.utils.DataBaseStorage;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cyd.awesome.material.AwesomeText;
import cyd.awesome.material.FontCharacterMaps;
import io.paperdb.Paper;

/**
 * Created by Admin on 5/30/2017.
 */

public class SponsorDetails extends AppCompatActivity implements View.OnClickListener {

    private static final int LOGIN_REQ_CODE = 108;
    AppDetails appDetails;
    RelativeLayout addnotes, addsch, rl_floor_locHead, loc,floor_map,r_category;
    RoundedImageView sponsorimage;
    ImageView iv1, iv2, iv3, iv4,iv5;
    private double ImgWidth, Imgheight;
    TextView Companyname, website, category, txtnotes, venue, txtsch,txtrate, des, tv_email, tv_fblink,
            tv_linkInLink, tv_twittrLnk, tv_head_cont_us, txtinvite,tv_instalnk,txtpdf, filesize,txtpdf1, filesize1,txtpdf2, filesize2,txtpdf3, filesize3,txtpdf4, filesize4;
    int color;
    private ArrayList<Events> events = new ArrayList<Events>();
    AwesomeText inote, isch, idownload, idots, inv,f_location;
    Items item;
    List<Integer> list;
    HashMap<Integer, Notes> Noteslist;
    String action;
    Context context;
    private String token;
    String floordetail;
    WebView fvenue;
    int mappos;
    String maptitle;
    Events e;
    int sponsorid;



    /* variables for invite meeting for seller and buyer
    *
    * */
    private RelativeLayout addinvite;
    private ArrayList<Schedule> schlist = new ArrayList<Schedule>();
    private String attendee_option="1";
    LinearLayout pdfdetails,pdfdetails1,pdfdetails2,pdfdetails3,pdfdetails4;





    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Paper.init(this);
        setContentView(R.layout.s_sponsordetails);
        appDetails = Paper.book().read("Appdetails");
        context = SponsorDetails.this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));
        setSupportActionBar(toolbar);
        Toolbar wvenue = (Toolbar) findViewById(R.id.wvenue);

        events = Paper.book().read("Appevents");
        e = events.get(0);


        sponsorimage = (RoundedImageView) findViewById(R.id.sponsorimage);
        Companyname = (TextView) findViewById(R.id.sponsorname);
        website = (TextView) findViewById(R.id.website);
        txtnotes = (TextView) findViewById(R.id.txtnote);
        txtsch = (TextView) findViewById(R.id.txtsch);
        txtinvite = (TextView) findViewById(R.id.txtinvite);
        /*Contact info */
        tv_head_cont_us = (TextView) findViewById(R.id.tv_head_cont_us);
        tv_email = (TextView) findViewById(R.id.tv_email);
        tv_fblink = (TextView) findViewById(R.id.tv_fblink);
        tv_linkInLink = (TextView) findViewById(R.id.tv_linkInLink);
        tv_twittrLnk = (TextView) findViewById(R.id.tv_twittrLnk);
        tv_instalnk = (TextView) findViewById(R.id.tv_instalnk);
        iv1 = (ImageView) findViewById(R.id.iv1);
        iv2 = (ImageView) findViewById(R.id.iv2);
        iv3 = (ImageView) findViewById(R.id.iv3);
        iv4 = (ImageView) findViewById(R.id.iv4);
        iv5= (ImageView) findViewById(R.id.iv5);

        /*Description*/
        des = (TextView) findViewById(R.id.des);
        inote = (AwesomeText) findViewById(R.id.inote);
        isch = (AwesomeText) findViewById(R.id.isch);
        inv = (AwesomeText) findViewById(R.id.inv);
        f_location = (AwesomeText) findViewById(R.id.f_location);

        idots = (AwesomeText) findViewById(R.id.doting);
        category = (TextView) findViewById(R.id.category);
        addnotes = (RelativeLayout) findViewById(R.id.addnotes);
        addsch = (RelativeLayout) findViewById(R.id.addschedule);
        addinvite = (RelativeLayout) findViewById(R.id.addinvite);

        pdfdetails = (LinearLayout) findViewById(R.id.pdfdetails);
        txtpdf = (TextView) findViewById(R.id.txtpdf);
        filesize = (TextView) findViewById(R.id.filesize);

        pdfdetails1 = (LinearLayout) findViewById(R.id.pdfdetails1);
        txtpdf1 = (TextView) findViewById(R.id.txtpdf1);
        filesize1 = (TextView) findViewById(R.id.filesize1);

        pdfdetails2 = (LinearLayout) findViewById(R.id.pdfdetails2);
        txtpdf2 = (TextView) findViewById(R.id.txtpdf2);
        filesize2 = (TextView) findViewById(R.id.filesize2);

        pdfdetails3 = (LinearLayout) findViewById(R.id.pdfdetails3);
        txtpdf3 = (TextView) findViewById(R.id.txtpdf3);
        filesize3 = (TextView) findViewById(R.id.filesize3);

        pdfdetails4 = (LinearLayout) findViewById(R.id.pdfdetails4);
        txtpdf4 = (TextView) findViewById(R.id.txtpdf4);
        filesize4 = (TextView) findViewById(R.id.filesize4);

        venue = (TextView) findViewById(R.id.venue);
        rl_floor_locHead = (RelativeLayout) findViewById(R.id.rl_floor_locHead);
        txtrate = (TextView) findViewById(R.id.txtrate);
        loc = (RelativeLayout) findViewById(R.id.loc);
        floor_map = (RelativeLayout) findViewById(R.id.floor_map);
        r_category = (RelativeLayout) findViewById(R.id.r_category);



        // setting the fonts
        Companyname.setTypeface(Util.regulartypeface(this));
        website.setTypeface(Util.boldtypeface(this));
        category.setTypeface(Util.regulartypeface(this));
        txtnotes.setTypeface(Util.regulartypeface(this));
        txtsch.setTypeface(Util.regulartypeface(this));
        des.setTypeface(Util.regulartypeface(this));
        txtpdf.setTypeface(Util.regulartypeface(this));
        filesize.setTypeface(Util.regulartypeface(this));
        txtpdf1.setTypeface(Util.regulartypeface(this));
        filesize1.setTypeface(Util.regulartypeface(this));
        txtpdf2.setTypeface(Util.regulartypeface(this));
        filesize2.setTypeface(Util.regulartypeface(this));
        txtpdf3.setTypeface(Util.regulartypeface(this));
        filesize3.setTypeface(Util.regulartypeface(this));
        txtpdf4.setTypeface(Util.regulartypeface(this));
        filesize4.setTypeface(Util.regulartypeface(this));






        //setting the texcolor

        //txtnotes.setTextColor(Color.parseColor(appDetails.getTheme_color()));
        txtsch.setTextColor(Color.parseColor(appDetails.getTheme_color()));
        txtinvite.setTextColor(Color.parseColor(appDetails.getTheme_color()));
        //inote.setTextColor(Color.parseColor(appDetails.getTheme_color()));
        isch.setTextColor(Color.parseColor(appDetails.getTheme_color()));
        inv.setTextColor(Color.parseColor(appDetails.getTheme_color()));
        f_location.setTextColor(Color.parseColor(appDetails.getTheme_color()));


        addnotes.setOnClickListener(this);
        addsch.setOnClickListener(this);
        wvenue.setOnClickListener(this);

        //calculating the speaker image size

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        ImgWidth = (displayMetrics.widthPixels) * 0.30;
        Imgheight = (displayMetrics.widthPixels) * 0.25;


        item = (Items) getIntent().getSerializableExtra("Items"); //Obtaining data
        color = getIntent().getIntExtra("Color", 0);
        action = Paper.book(appDetails.getAppId()).read("action"," ");

      //  attendee_option = Paper.book().read("attendee_option", "");

        token = Paper.book().read("token", "");

        //meeting added for seller and buyer
        boolean isMeetingEnabled = Paper.book(appDetails.getAppId()).read("isMeetingEnabled", false);
        addinvite.setOnClickListener(this);
        try {
            if (isMeetingEnabled && action.equals("Exhibitor"))
                addinvite.setVisibility(View.VISIBLE);
            else
                addinvite.setVisibility(View.GONE);
        }catch (Exception e){

        }

        idots.setTextColor(color);

        // setting data

        settingdata();


    }

    private void settingdata() {

        try {
            if (item.getExhibitor_document_url().equalsIgnoreCase(""))
                pdfdetails.setVisibility(View.GONE);
            else {

                    pdfdetails.setVisibility(View.VISIBLE);
                    txtpdf.setText(item.getExhibitor_document_name());
                    pdfdetails.setOnClickListener(this);}

        }catch (Exception e){}

        try {
            if (item.getExhibitor_document_url2().equalsIgnoreCase(""))
                pdfdetails1.setVisibility(View.GONE);
            else {

                pdfdetails1.setVisibility(View.VISIBLE);
                txtpdf1.setText(item.getExhibitor_document_name2());
                pdfdetails1.setOnClickListener(this);}

        }catch (Exception e)
        {

        }
        try {
            if (item.getExhibitor_document_name3().equalsIgnoreCase(""))
                pdfdetails2.setVisibility(View.GONE);
            else {

                pdfdetails2.setVisibility(View.VISIBLE);
                txtpdf2.setText(item.getExhibitor_document_name3());
                pdfdetails2.setOnClickListener(this);}

        }catch (Exception e)
        {

        }
        try {
            if (item.getExhibitor_document_url4().equalsIgnoreCase(""))
                pdfdetails3.setVisibility(View.GONE);
            else {

                pdfdetails3.setVisibility(View.VISIBLE);
                txtpdf3.setText(item.getExhibitor_document_name4());
                pdfdetails3.setOnClickListener(this);}

        }catch (Exception e)
        {

        }
        try {
            if (item.getExhibitor_document_url5().equalsIgnoreCase(""))
                pdfdetails4.setVisibility(View.GONE);
            else {

                pdfdetails4.setVisibility(View.VISIBLE);
                txtpdf4.setText(item.getExhibitor_document_name5());
                pdfdetails4.setOnClickListener(this);}

        }catch (Exception e)
        {

        }


        //setting companyname
        Companyname.setText(item.getCompany());

        //setting website
        String linkText = "<a href=" + item.getWebsite() + ">" + item.getWebsite() + "</a > ";


        website.setText(Html.fromHtml(linkText));
        website.setMovementMethod(LinkMovementMethod.getInstance());


        //setting location

        if (item.getLocation()!=null &&item.getMap_checkvalue()!=null&& item.getFloor_id()!=null) {
          //  venue.setText(item.getLocation());
            loc.setVisibility(View.GONE);
            //venue.setOnClickListener(this);
            r_category.setOnClickListener(this);
        } else if(item.getLocation()!=null&&item.getFloor_id()!=null && item.getFloor_id().equalsIgnoreCase("")) {
           // venue.setText(item.getLocation());
            loc.setVisibility(View.GONE);
           // venue.setOnClickListener(this);
            r_category.setOnClickListener(this);
        }
        else if(item.getLocation_detail()!=null && !item.getMap_checkvalue().equalsIgnoreCase(""))
        {
            try {
               // venue.setText(item.getLocation());
                loc.setVisibility(View.GONE);
                String ch = item.getMap_checkvalue();


                for (int i = 0; i < e.getTabs().length; i++) {
                    if (e.getTabs(i).getType().contains("map") && e.getTabs(i).getCheckvalue().equalsIgnoreCase(ch)) {
                        mappos = i;
                        maptitle = e.getTabs(i).getTitle();
                        break;

                    }
                }
               // venue.setOnClickListener(this);
                r_category.setOnClickListener(this);
            }catch (NullPointerException e)
            {

            }



        }
        else
        {
            loc.setVisibility(View.GONE);

        }
        //setting description

        if (!item.getDetail().equalsIgnoreCase("")) {
            des.setText(Html.fromHtml(item.getDetail()));
            des.setMovementMethod(LinkMovementMethod.getInstance());
            des.setMovementMethod(new ScrollingMovementMethod());


        } else {
            des.setText("No Description Found");
        }
        if (!item.getEmail().equalsIgnoreCase("")) {
            tv_email.setText(item.getEmail().trim());
            tv_email.setSelected(true);

            tv_email.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    try {
                        //pass the url to intent data

                      /*  Intent intent = new Intent(Intent.ACTION_SENDTO);
                        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
                        intent.putExtra(Intent.EXTRA_EMAIL, item.getEmail().trim());
                        intent.putExtra(Intent.EXTRA_SUBJECT, " ");
                        if (intent.resolveActivity(getPackageManager()) != null) {
                            startActivity(intent);*/
                        Intent email = new Intent(Intent.ACTION_SEND);
                        email.putExtra(Intent.EXTRA_EMAIL, new String[]{item.getEmail().trim()});
                        email.putExtra(Intent.EXTRA_SUBJECT, "");
                        email.putExtra(Intent.EXTRA_TEXT, "");

                        email.setType("message/rfc822");

                        startActivityForResult(Intent.createChooser(email, "Choose an Email client :"), 12);
                    } catch (Exception e) {
                        System.out.print("Exception handled ");
                    }

                }
            });



        } else {
            tv_email.setText("No Email Found");
            tv_email.setSelected(false);
            tv_email.setVisibility(View.GONE);
            iv1.setVisibility(View.GONE);
        }
        if (!item.getFblink().equalsIgnoreCase("")) {
            tv_fblink.setText(item.getFblink().trim());
            tv_fblink.setSelected(true);
            tv_fblink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent();
                    try {
                        //pass the url to intent data

                        intent = new Intent(getBaseContext(), SocialMediaView.class);
                        intent.putExtra("url", item.getFblink());
                        intent.putExtra("title", "Facebook");
                        startActivity(intent);

                    } catch (Exception e) {
                        System.out.print("Exception handled ");
                    }

                }
            });

        } else {
            tv_fblink.setText("No Facebook Link Found");
            tv_fblink.setSelected(false);
            tv_fblink.setVisibility(View.GONE);
            iv2.setVisibility(View.GONE);
        }
        if (!item.getLinkedinlink().equalsIgnoreCase("")) {
            tv_linkInLink.setText(item.getLinkedinlink().trim());
            tv_linkInLink.setSelected(true);
            tv_linkInLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    try {
                        //pass the url to intent data

                        intent = new Intent(getBaseContext(), SocialMediaView.class);
                        intent.putExtra("url", item.getLinkedinlink());
                        intent.putExtra("title", "LinkedIn");
                        startActivity(intent);

                    } catch (Exception e) {
                        System.out.print("Exception handled ");
                    }
                }
            });
        } else {
            tv_linkInLink.setText("No LinkedIn Link Found");
            tv_linkInLink.setSelected(false);
            tv_linkInLink.setVisibility(View.GONE);
            iv3.setVisibility(View.GONE);
        }
        if (!item.getTwittername().equalsIgnoreCase("")) {
            tv_twittrLnk.setText(item.getTwittername().trim());
            tv_twittrLnk.setSelected(true);
            tv_twittrLnk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    try {
                        //pass the url to intent data

                        intent = new Intent(getBaseContext(), SocialMediaView.class);
                        intent.putExtra("url", item.getTwittername());
                        intent.putExtra("title", "Twitter");
                        startActivity(intent);

                    } catch (Exception e) {
                        System.out.print("Exception handled ");
                    }
                }
            });


        } else {
            tv_twittrLnk.setText("No Twitter Name Found");
            tv_twittrLnk.setSelected(false);
            tv_twittrLnk.setVisibility(View.GONE);
            iv4.setVisibility(View.GONE);
        }

        if (item.getLinkedinlink().equals("") && item.getFblink().equals("") && item.getTwittername().equals("")
                && item.getEmail().equals("")) {

            tv_head_cont_us.setVisibility(View.GONE);

        }


        String newString = item.getCategories().substring(0, 1).toUpperCase() + item.getCategories().substring(1);
        category.setText(newString);

        // setting speaker image

        Glide.with(getApplicationContext())

                .load((item.getImage().equalsIgnoreCase("")) ? R.drawable.default_partner : item.getImage())
                .asBitmap()
                .placeholder(R.drawable.default_partner)
                .error(R.drawable.default_partner)
                .into(sponsorimage);
        sponsorimage.setCornerRadius(12,12,12,12);
        /*new BitmapImageViewTarget(sponsorimage) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        sponsorimage.setImageBitmap(Util.scaleBitmap(resource, (int) ImgWidth, (int) Imgheight));
                    }
                });*/


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Paper.book().read("Islogin", false))
            getProfile();


        SpannableString s = new SpannableString("Details");
        setTitle(Util.applyFontToMenuItem(this, s));
        settingsch();

    }

    private void floorMap(String appid,String type,String type_id){



        // converting arraylist to jsonarray

        final String finaltype=type;
        final String finalappid=appid;
        final String finaltypeid=type_id;
/*
        Gson gson = new GsonBuilder().create();
        final JsonArray myCustomArray = gson.toJsonTree(list).getAsJsonArray();*/


        final ProgressDialog dialog = new ProgressDialog(SponsorDetails.this,R.style.MyAlertDialogStyle);
        //dialog.setMessage(msg);
        dialog.show();
        String tag_string_req = "FloorMap";
        String url = ApiList.Floor_map;
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                try {

                    JSONObject jObj = new JSONObject(response);


                    if (jObj.getBoolean("response")) {
                        Error_Dialog.show(jObj.getString("responseString"), SponsorDetails.this);
                        floordetail=jObj.getString("html_file").toString();
                        fvenue.loadUrl(String.format("data:text/html;charset=utf-8,<html><body style=\"text-align:justify;font-size:12px;\"> %s </body></Html>", Uri.encode(floordetail)));

                    } else {
                        if (jObj.getString("responseString").equalsIgnoreCase("Invalid token")) {
                            Intent i = new Intent();
                            i.putExtra("keyMessage", "Session Expired, Please Login ");
                            i.putExtra("keyAlert", "Session Expired");
                            i.setClassName(getPackageName(), getPackageName()+".SessionExpired");
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);

                        } else
                            Error_Dialog.show(jObj.getString("responseString"), SponsorDetails.this);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Paper.book().write("Sync", true);
                if (error instanceof TimeoutError) {
                    Error_Dialog.show("Timeout", SponsorDetails.this);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, context), SponsorDetails.this);
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show("Please Check Your Internet Connection", SponsorDetails.this);
                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("appid",finalappid);
                params.put("type", finaltype);
                params.put("type_id", finaltypeid);


                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("token", token );
                return headers;
            }
        };


        strReq.setShouldCache(false);
        App.getInstance().addToRequestQueue(strReq, tag_string_req);
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));


    }

    private void settingsch() {
try {

//        list = Paper.book().read(action, new ArrayList<Integer>() {});
    list = Paper.book(appDetails.getAppId()).read(action, new ArrayList<Integer>() {
    });
    if (list.contains(action.equalsIgnoreCase("Exhibitor") ? item.getExhibitor_id() : item.getSponsor_id())) {
        txtsch.setText("REMOVE FROM FAVOURITE");
        isch.setPixedenStrokeIcon(FontCharacterMaps.Pixeden.PE_CHECK);
    } else {
        txtsch.setText("ADD TO FAVOURITE");
        isch.setPixedenStrokeIcon(FontCharacterMaps.Pixeden.PE_PLUS);
    }
}catch (Exception e){

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


        int i = view.getId();

        if (i == R.id.pdfdetails) {
            Uri path = Uri.parse(item.getExhibitor_document_url());
            Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
            pdfIntent.setDataAndType(path, "application/pdf");
            pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            try {
                startActivity(pdfIntent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(SponsorDetails.this, "No Application available to view PDF", Toast.LENGTH_SHORT).show();
            }

        }
        if (i == R.id.pdfdetails1) {
            Uri path = Uri.parse(item.getExhibitor_document_url2());
            Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
            pdfIntent.setDataAndType(path, "application/pdf");
            pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            try {
                startActivity(pdfIntent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(SponsorDetails.this, "No Application available to view PDF", Toast.LENGTH_SHORT).show();
            }

        }
        if (i == R.id.pdfdetails2) {
            Uri path = Uri.parse(item.getExhibitor_document_name3());
            Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
            pdfIntent.setDataAndType(path, "application/pdf");
            pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            try {
                startActivity(pdfIntent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(SponsorDetails.this, "No Application available to view PDF", Toast.LENGTH_SHORT).show();
            }

        }
        if (i == R.id.pdfdetails3) {
            Uri path = Uri.parse(item.getExhibitor_document_url4());
            Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
            pdfIntent.setDataAndType(path, "application/pdf");
            pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            try {
                startActivity(pdfIntent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(SponsorDetails.this, "No Application available to view PDF", Toast.LENGTH_SHORT).show();
            }

        }
        if (i == R.id.pdfdetails4) {
            Uri path = Uri.parse(item.getExhibitor_document_name5());
            Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
            pdfIntent.setDataAndType(path, "application/pdf");
            pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            try {
                startActivity(pdfIntent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(SponsorDetails.this, "No Application available to view PDF", Toast.LENGTH_SHORT).show();
            }

        }

        if (i == R.id.addnotes) {
            if (Paper.book().read("Islogin", false))
                shownotes();
            else {
                Error_Dialog.show("Please Login", this);
                Intent intent = new Intent(this, LoginActivity.class);
                startActivityForResult(intent, 2);

            }

        } else if (i == R.id.addschedule) {
            if (Paper.book().read("Islogin", false)) {
                try {
                    list  = Paper.book(appDetails.getAppId()).read(action, new ArrayList<Integer>());
                    if (list.contains(action.equalsIgnoreCase("Exhibitor") ? item.getExhibitor_id() : item.getSponsor_id())) {
                        list.remove(new Integer(action.equalsIgnoreCase("Exhibitor") ? item.getExhibitor_id() : item.getSponsor_id()));
                        addtosch("Removing from FAVORITE", "unmark");
                    } else {
                        list.add(action.equalsIgnoreCase("Exhibitor") ? item.getExhibitor_id() : item.getSponsor_id());
                        addtosch("Adding to FAVORITE", "mark");
                    }
                    Paper.book(appDetails.getAppId()).write(action, list);
                    settingsch();
                }catch (Exception e){

                }

            } else {
                Error_Dialog.show("Please Login", this);
                Intent intent = new Intent(SponsorDetails.this, LoginActivity.class);
                startActivityForResult(intent, LOGIN_REQ_CODE);

            }


        } else if (i == R.id.addinvite) {
            if (attendee_option.equals("1")) {
                getTime_Slot();
            } else {
                Error_Dialog.show("Please, Opt-in first to invite meeting.", this);
            }
        }
        else if(i==R.id.wvenue){
            onBackPressed();
        }
        else if (i == R.id.r_category) {
            if(!item.getFloor_id().equalsIgnoreCase("")) {
                try {
                    fvenue = (WebView) findViewById(R.id.s_mapvenue);
                    //  fvenue.setWebChromeClient(new WebChromeClient());
                    fvenue.getSettings().setJavaScriptEnabled(true);
                    fvenue.getSettings().setLoadWithOverviewMode(true);
                    fvenue.getSettings().setUseWideViewPort(true);
                    fvenue.getSettings().setBuiltInZoomControls(true);

                    fvenue.getSettings().setPluginState(WebSettings.PluginState.ON);
                    if(action.equalsIgnoreCase("Exhibitor")){
                        sponsorid = item.getExhibitor_id();
                        floorMap(appDetails.getAppId(), "exhibitor", Integer.toString(sponsorid));
                    }
                    else{
                        sponsorid = item.getSponsor_id();
                        floorMap(appDetails.getAppId(), "sponsor", Integer.toString(sponsorid));
                    }







                    /*loading html content in webview*/
                    //  fvenue.loadUrl("https://webmobi.s3.amazonaws.com/nativeapps/personaltest/FloorMap0.png");

                    fvenue.setInitialScale(1);
                    fvenue.getSettings().setLoadWithOverviewMode(true);
                    fvenue.getSettings().setUseWideViewPort(true);
                    fvenue.setWebViewClient(new WebViewClient());


                    floor_map.setVisibility(View.VISIBLE);
                    //fullagenda.setVisibility(View.INVISIBLE);
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }else{
                Intent intent = new Intent(this, MapRoot.class);
                intent.putExtra("pos",mappos);
                intent.putExtra("title", maptitle);
                startActivity(intent);
            }



           /* int mapPosition = 0;
            for (int i = 0; i < e.getTabs().length; i++) {
                if (e.getTabs(i).getType().compareTo("map") == 0) {
                    mapPosition = i;
                    break;
                }
*/

          /*  if (e.getTabs(mapPosition).getType().compareTo("map") == 0) {
                Intent intent = new Intent(this, MapRoot.class);
                intent.putExtra("pos", mapPosition);
                intent.putExtra("title", e.getTabs(mapPosition).getTitle());
                startActivity(intent);
            }*/
        }

    }


    private void getTime_Slot() {

        final ProgressDialog dialog = new ProgressDialog(SponsorDetails.this, R.style.MyAlertDialogStyle);
        dialog.setMessage("Fetching the Time Slot....");
        dialog.show();
        String tag_string_req = "Getting_Slot";
        String url = ApiList.Get_TimeSlot + appDetails.getAppId() + "&userid=" + Paper.book().read("userId", "");
        Log.d("check_time_slots", "getTime_Slot: "+url);
        StringRequest strReq = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                try {

                    JSONObject jObj = new JSONObject(response);

                    if (jObj.getBoolean("response")) {
                        if (jObj.getJSONArray("timeslots").length() > 0)
                            parsetimeslot(jObj.getJSONArray("timeslots"));
                        else
                            Error_Dialog.show("Sorry, All meeting slots are booked.", SponsorDetails.this);
                    } else {
                        if (jObj.getString("responseString").equalsIgnoreCase("Invalid token")) {
                            Error_Dialog.show("Session is Expired Please Login.", SponsorDetails.this);


                        } else
                            Error_Dialog.show(jObj.getString("responseString"), SponsorDetails.this);
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
                    Error_Dialog.show("Timeout", SponsorDetails.this);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, SponsorDetails.this),
                            SponsorDetails.this);
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show("Please Check Your Internet Connection.", SponsorDetails.this);
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
            User user = Paper.book().read("LoggedInUser", null);
            if (user != null)
                MeetingDialogFragment.newInstance(user, item.getEmail()).show(getSupportFragmentManager(), null);


        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            list = Paper.book().read(action, new ArrayList<Integer>());
            if (list.contains(action.equalsIgnoreCase("Exhibitor") ? item.getExhibitor_id() : item.getSponsor_id())) {
                list.remove(new Integer(action.equalsIgnoreCase("Exhibitor") ? item.getExhibitor_id() : item.getSponsor_id()));
                addtosch("Removing from FAVORITE", "unmark");
            } else {
                list.add(action.equalsIgnoreCase("Exhibitor") ? item.getExhibitor_id() : item.getSponsor_id());
                addtosch("Adding to FAVORITE", "mark");
            }
            Paper.book().write(action, list);
            settingsch();
        } else if (requestCode == 2 && resultCode == RESULT_OK) {
            shownotes();
        }
    }

    private void shownotes() {


        Noteslist = Paper.book(appDetails.getAppId()).read(action.equalsIgnoreCase("Exhibitor") ? "ExhibitorNote" : "SponsorNote", new HashMap<Integer, Notes>());

        final Dialog mBottomSheetDialog = new Dialog(SponsorDetails.this, R.style.MaterialDialogSheet);

        mBottomSheetDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mBottomSheetDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
        mBottomSheetDialog.setContentView(R.layout.s_note);
        mBottomSheetDialog.setCancelable(true);
        mBottomSheetDialog.show();

        final LinearLayout cancel = (LinearLayout) mBottomSheetDialog.findViewById(R.id.cancel);
        LinearLayout save = (LinearLayout) mBottomSheetDialog.findViewById(R.id.save);
        TextView canceltxt = (TextView) mBottomSheetDialog.findViewById(R.id.canceltxt);
        TextView savetxt = (TextView) mBottomSheetDialog.findViewById(R.id.savetxt);

        canceltxt.setTypeface(Util.regulartypeface(context));
        savetxt.setTypeface(Util.regulartypeface(context));

        //canceltxt.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));
        //savetxt.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));
        canceltxt.setBackground(Util.setdrawable(context, R.drawable.act_button_border, Color.parseColor(appDetails.getTheme_color())));
        savetxt.setBackground(Util.setdrawable(context, R.drawable.act_button_border, Color.parseColor(appDetails.getTheme_color())));

        final EditText notes = (EditText) mBottomSheetDialog.findViewById(R.id.notes);
        notes.setTypeface(Util.regulartypeface(context));

        if (Noteslist.containsKey(action.equalsIgnoreCase("Exhibitor") ? item.getExhibitor_id() : item.getSponsor_id())) {
            Notes n = Noteslist.get(action.equalsIgnoreCase("Exhibitor") ? item.getExhibitor_id() : item.getSponsor_id());
            notes.setText(n.getNotes());
        }


        canceltxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBottomSheetDialog.dismiss();
            }
        });

        savetxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!notes.getText().toString().isEmpty()) {
                    mBottomSheetDialog.dismiss();
                    Addnotes(notes.getText().toString(), Noteslist.containsKey(action.equalsIgnoreCase("Exhibitor") ? item.getExhibitor_id() : item.getSponsor_id()) ? "update" : "create", action.equalsIgnoreCase("Exhibitor") ? "exhibitor" : "sponsor");
                    Noteslist.put(action.equalsIgnoreCase("Exhibitor") ? item.getExhibitor_id() : item.getSponsor_id(), new Notes(action.equalsIgnoreCase("Exhibitor") ? item.getExhibitor_id() : item.getSponsor_id(), notes.getText().toString(), action.equalsIgnoreCase("Exhibitor") ? "exhibitor" : "sponsor", item.getCompany(), String.valueOf(System.currentTimeMillis())));
                    Paper.book(appDetails.getAppId()).write(action.equalsIgnoreCase("Exhibitor") ? "ExhibitorNote" : "SponsorNote", Noteslist);
                }
            }
        });


    }

    private void addtosch(String msg, final String mark) {

        // converting arraylist to jsonarray

        Gson gson = new GsonBuilder().create();
        final JsonArray myCustomArray = gson.toJsonTree(list).getAsJsonArray();


        final ProgressDialog dialog = new ProgressDialog(SponsorDetails.this, R.style.MyAlertDialogStyle);
        dialog.setMessage(msg);
        dialog.show();
        String tag_string_req = "Schdule";
        String url = ApiList.Favorites;
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                try {
                    System.out.println(response);
                    JSONObject jObj = new JSONObject(response);


                    if (jObj.getBoolean("response")) {
                        Error_Dialog.show(jObj.getString("responseString"), SponsorDetails.this);
                    } else {
                        if (jObj.getString("responseString").equalsIgnoreCase("Invalid token")) {
                            Intent i = new Intent();
                            i.putExtra("keyMessage", "Session Expired, Please Login ");
                            i.putExtra("keyAlert", "Session Expired");
                            i.setClassName(SponsorDetails.this.getPackageName(), SponsorDetails.this.getPackageName() + ".SessionExpired");
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);

                        } else
                            Error_Dialog.show(jObj.getString("responseString"), SponsorDetails.this);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Paper.book("Sync").write(appDetails.getAppId(), true);
                if (error instanceof TimeoutError) {
                    Error_Dialog.show("Timeout", SponsorDetails.this);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, context), SponsorDetails.this);
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show("Please Check Your Internet Connection", SponsorDetails.this);
                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userid", Paper.book().read("userId", ""));
                params.put("action", mark);
                params.put("type", action);
                params.put("favorites", myCustomArray.toString());
                params.put("appid", appDetails.getAppId());
                System.out.println(params);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("token", Paper.book().read("token", ""));
                return headers;
            }
        };


        strReq.setShouldCache(false);
        App.getInstance().addToRequestQueue(strReq, tag_string_req);
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));


    }

    private void startThread() {

        final Handler handler = new Handler();

        Runnable r = new Runnable() {
            @Override
            public void run() {
                // your code here

                try {
                    FileInputStream fis = openFileInput(DataBaseStorage.F_I_L_ENCP2);
                    ObjectInputStream ois = new ObjectInputStream(fis);

                    byte[] decrypted = DataBaseStorage.decryptData((HashMap<String, byte[]>) ois.readObject(),
                            DataBaseStorage.token_pass);
                    if (decrypted != null) {
                        //decryptedString[0] = new String(decrypted);
                        token = new String(decrypted);
                    }
                    ois.close();

                } catch (Exception e) {

                    e.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getProfile();
                    }
                });
            }
        };

        Thread t = new Thread(r);
        t.start();
    }


    private void Addnotes(final String notes, final String actiontype, final String type) {

        final ProgressDialog dialog = new ProgressDialog(SponsorDetails.this, R.style.MyAlertDialogStyle);
        dialog.setMessage("Adding Note");
        dialog.show();
        String tag_string_req = "Note";
        String url = ApiList.Notes;
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                try {
                    System.out.println(response);
                    JSONObject jObj = new JSONObject(response);


                    if (jObj.getBoolean("response")) {
                        Error_Dialog.show("Saved", SponsorDetails.this);
                    } else {
                        Error_Dialog.show(jObj.getString("responseString"), SponsorDetails.this);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Paper.book("Sync").write(appDetails.getAppId(), true);
                if (error instanceof TimeoutError) {
                    Error_Dialog.show("Timeout", SponsorDetails.this);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, context), SponsorDetails.this);
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show("Please Check Your Internet Connection", SponsorDetails.this);
                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userid", Paper.book().read("userId", ""));
                params.put("action", actiontype);
                params.put("type", type);
                params.put("notes", notes);
                params.put("last_updated", String.valueOf(System.currentTimeMillis()));
                params.put("type_id", String.valueOf(action.equalsIgnoreCase("Exhibitor") ? item.getExhibitor_id() : item.getSponsor_id()));
                params.put("appid", appDetails.getAppId());
                params.put("type_name", item.getCompany());
                System.out.println(params);
                return params;
            }

        };


        strReq.setShouldCache(false);
        App.getInstance().addToRequestQueue(strReq, tag_string_req);
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

    }

    private void getProfile() {


        final ProgressDialog pDialog = new ProgressDialog(SponsorDetails.this, R.style.MyAlertDialogStyle);
        pDialog.setMessage("Loading ...");
        pDialog.show();


        String tag_string_req = "Login";
        String url = ApiList.GetProfile + Paper.book().read("userId", "");
        StringRequest strReq = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                pDialog.dismiss();

                Gson gson = new Gson();

                JSONObject jObj = null;
                try {
                    jObj = new JSONObject(response);
                    if (jObj.getBoolean("response")) {
                        JSONArray chatlist = jObj.getJSONArray("Profile");
                        String userdetails = chatlist.getJSONObject(0).toString();
                        User user_details = gson.fromJson(userdetails, User.class);
                        Paper.book().write("LoggedInUser", user_details);


                    } else {
                        if (jObj.getString("responseString").equalsIgnoreCase("Invalid token")) {
                            Intent i = new Intent();
                            i.putExtra("keyMessage", "Session Expired, Please Login ");
                            i.putExtra("keyAlert", "Session Expired");
                            i.setClassName(getPackageName(), getPackageName() + ".SessionExpired");
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);

                        } else
                            Error_Dialog.show(jObj.getString("responseString"), SponsorDetails.this);
                    }
                } catch (JSONException e) {


                }


            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                pDialog.dismiss();
                if (error instanceof TimeoutError) {
                    Error_Dialog.show("Timeout", SponsorDetails.this);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, SponsorDetails.this),
                            SponsorDetails.this);
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    /*view1.setVisibility(View.VISIBLE);*/
                }
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("token", token);
                return headers;
            }
        };


        strReq.setShouldCache(false);
        App.getInstance().addToRequestQueue(strReq, tag_string_req);
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
    }


}
