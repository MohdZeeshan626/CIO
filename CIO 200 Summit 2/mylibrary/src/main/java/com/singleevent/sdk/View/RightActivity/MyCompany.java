package com.singleevent.sdk.View.RightActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.text.SpannableString;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.makeramen.roundedimageview.RoundedImageView;
import com.singleevent.sdk.Custom_View.Error_Dialog;
import com.singleevent.sdk.Custom_View.Util;
import com.singleevent.sdk.Left_Adapter.SponsorsAdapter;
import com.singleevent.sdk.View.LeftActivity.SponsorDetails;
import com.singleevent.sdk.View.LeftActivity.SponsorRoot;
import com.singleevent.sdk.model.AppDetails;
import com.singleevent.sdk.model.Events;
import com.singleevent.sdk.model.Items;
import com.singleevent.sdk.R;
import com.twitter.sdk.android.core.models.SafeListAdapter;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cyd.awesome.material.AwesomeText;
import io.paperdb.Paper;

/**
 * Created by Admin on 6/12/2017.
 */

public class MyCompany extends AppCompatActivity implements View.OnClickListener {

    AppDetails appDetails;
    int pos;
    private ArrayList<Events> events = new ArrayList<Events>();
    Events e;
    static ArrayList<Items> exhibitorlist = new ArrayList<>();
    static ArrayList<Items> tempexhibitorlist = new ArrayList<>();
    static ArrayList<Items> sponsorlist = new ArrayList<>();
    private double ImgWidth, Imgheight;
    LinearLayout contents;
    String[] ecat, scat;
    List<Integer> Elist, Slist;
    HashMap<String, Integer> categorylist = new HashMap<>();
    HashMap<String, Integer> tempcategorylist = new HashMap<>();
    RelativeLayout nocompany;
    Button mycombut;
    String ctitle;
    LinearLayout search_view;

    RecyclerView recycler_view;
    private SponsorsAdapter sponsorsAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Paper.init(this);
        setContentView(R.layout.s_sponsorroot);
        appDetails = Paper.book().read("Appdetails");
        search_view=(LinearLayout)findViewById(R.id.search_view);
        recycler_view = (RecyclerView) findViewById(R.id.recycler_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));
        setSupportActionBar(toolbar);
        search_view.setVisibility(View.GONE);

        events = Paper.book().read("Appevents");
        e = events.get(0);

        // finding the exhibitor

        for (int i = 0; i < e.getTabs().length; i++) {
            if (e.getTabs(i).getType().compareTo("exhibitorsData") == 0) {
                pos = i;
                ctitle=e.getTabs(i).getTitle();
                break;
            }

        }

        // adding exhibitor categories

        if (e.getTabs(pos).getcatSize() > 0) {
            for (int j = 0; j < e.getTabs(pos).getcatSize(); j++) {
                try {
                    System.out.println(e.getTabs(pos).getCategories(j).getCategory()+ Color.parseColor(e.getTabs(pos).getCategories(j).getColor_code()));

                    categorylist.put(e.getTabs(pos).getCategories(j).getCategory(), Color.parseColor(e.getTabs(pos).getCategories(j).getColor_code()));
                }catch (Exception e){

                }
            }
        }


        exhibitorlist.clear();

        for (int j = 0; j < e.getTabs(pos).getItemsSize(); j++) {
            exhibitorlist.add(e.getTabs(pos).getItems(j));
        }

        // finding the sponsor

        for (int i = 0; i < e.getTabs().length; i++) {
            if (e.getTabs(i).getType().compareTo("sponsorsData") == 0) {
                pos = i;
                ctitle=e.getTabs(i).getTitle();
                break;
            }

        }

        // adding sponsor categories

        if (e.getTabs(pos).getcatSize() > 0) {
            for (int j = 0; j < e.getTabs(pos).getcatSize(); j++) {
                categorylist.put(e.getTabs(pos).getCategories(j).getCategory(), Color.parseColor(e.getTabs(pos).getCategories(j).getColor_code()));

            }
        }


        sponsorlist.clear();

        for (int j = 0; j < e.getTabs(pos).getItemsSize(); j++) {
            sponsorlist.add(e.getTabs(pos).getItems(j));
        }


        //calculating the speaker image size

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        ImgWidth = (displayMetrics.widthPixels) * 0.20;
        Imgheight = (displayMetrics.widthPixels) * 0.15;

        System.out.println("Categories size" + categorylist.size());

        contents = (LinearLayout) findViewById(R.id.contains);
        nocompany = (RelativeLayout) findViewById(R.id.nocompany);
        mycombut=(Button) findViewById(R.id.mycombut);
        mycombut.setBackground(Util.setdrawable(MyCompany.this, com.singleevent.sdk.R.drawable.healthpostbut,
                Color.parseColor(appDetails.getTheme_color())));
        mycombut.setOnClickListener(this);



        //recycleradapter added for new ui with searchbox



         setdata();

    }

    private void setdata() {
        contents.removeAllViews();

        // adding favorite exhibitor
       // Elist.clear();
      //  Slist.clear();
        Elist = Paper.book(appDetails.getAppId()).read("Exhibitor", new ArrayList<Integer>() {
        });
        Slist = Paper.book(appDetails.getAppId()).read("Sponsor", new ArrayList<Integer>() {
        });

        if (Elist.size() > 0 || Slist.size() > 0) {
            tempcategorylist.clear();
            tempexhibitorlist.clear();
            addcontent(exhibitorlist, Elist, true, "Exhibitor");
            addcontent(sponsorlist, Slist, false, "Sponsor");
            nocompany.setVisibility(View.GONE);
            contents.setVisibility(View.VISIBLE);

        } else {
            search_view.setVisibility(View.GONE);
            nocompany.setVisibility(View.VISIBLE);
            contents.setVisibility(View.GONE);
        }


    }

    private void addcontent(ArrayList<Items> exhibitorlist, List<Integer> elist, boolean flag, String action) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View child = inflater.inflate(R.layout.s_sponsorview, null);

        for (int i = 0; i < exhibitorlist.size(); i++) {
            final Items c = exhibitorlist.get(i);
            if (elist.contains(flag == true ? c.getExhibitor_id() : c.getSponsor_id())) {
                tempexhibitorlist.add(c);
                tempcategorylist.put(c.getCategories(), categorylist.get(c.getCategories()));
            }else{

            }


              /*   View child = inflater.inflate(R.layout.s_sponsorview, null);
                LinearLayout sponsorimage = (LinearLayout) child.findViewById(R.id.sponsorview);
                RelativeLayout relativeLayout1st=(RelativeLayout)child.findViewById(R.id.relativeLayout1st);
                final RoundedImageView simage = (RoundedImageView) child.findViewById(R.id.sponsorimage);
               *//* RelativeLayout.LayoutParams sparams = (RelativeLayout.LayoutParams) sponsorimage.getLayoutParams();
                sparams.width = (int) ImgWidth;
               sparams.height = (int) Imgheight;
                sponsorimage.setLayoutParams(sparams);*//*
                String Image = c.getImage();
                Glide.with(getApplicationContext())

                        .load((Image.equalsIgnoreCase("")) ? R.drawable.default_partner : Image)
                        .asBitmap()
                        .placeholder(R.drawable.default_partner)
                        .error(R.drawable.default_partner)
                        .into(simage);
                simage.setCornerRadius(12,12,12,12);
                ;*//*new BitmapImageViewTarget(simage) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                simage.setImageBitmap(Util.scaleBitmap(resource, (int) ImgWidth, (int) Imgheight));
                            }
                        });*//*


                TextView sname = (TextView) child.findViewById(R.id.title);
                TextView sdes = (TextView) child.findViewById(R.id.des);
                TextView scat = (TextView) child.findViewById(R.id.category);
                AwesomeText dots = (AwesomeText) child.findViewById(R.id.dotes);
                sname.setTypeface(Util.regulartypeface(this));
                sdes.setTypeface(Util.regulartypeface(this));
                scat.setTypeface(Util.regulartypeface(this));

                sname.setText(c.getCompany());
                sdes.setText(Html.fromHtml(c.getWebsite()));
                //make first letter capital then settext
                String newstr = c.getCategories().substring(0,1).toUpperCase()+c.getCategories().substring(1);
                scat.setText(newstr);
                dots.setTextColor(categorylist.get(c.getCategories()));
                child.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Bundle args = new Bundle();
                        args.putInt("Color", categorylist.get(c.getCategories()));
                        args.putSerializable("Items", c);
                        Intent i = new Intent(MyCompany.this, MyCompanyDetails.class);
                        i.putExtras(args);
                        startActivityForResult(i, 1);
                    }
                });
                relativeLayout1st.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Bundle args = new Bundle();
                        args.putInt("Color", categorylist.get(c.getCategories()));
                        args.putSerializable("Items", c);
                        Intent i = new Intent(MyCompany.this, MyCompanyDetails.class);
                        i.putExtras(args);
                        startActivityForResult(i, 1);
                    }
                });
                simage.setOnClickListener(this);


                contents.addView(child);*/
               // callmethod(categorylist.get(c.getCategories()),exhibitorlist.get(i));
          //  }
            try {
                sponsorsAdapter = new SponsorsAdapter(this, tempexhibitorlist, tempcategorylist, ImgWidth, Imgheight,1);
                recycler_view.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,
                        false));
                recycler_view.setAdapter(sponsorsAdapter);
            }catch (Exception e){

            }

        }
    }




/*
    public void callmethod(int color,Items c){
      //  Paper.book().write("action", action);
       */
/* Bundle args = new Bundle();
        args.putInt("Color", color);
        args.putSerializable("Items", c);
        Intent i = new Intent(MyCompany.this, MyCompanyDetails.class);
        i.putExtras(args);
        startActivityForResult(i, 1);*//*

        Bundle args = new Bundle();
        args.putInt("Color", color);
        args.putSerializable("Items", c);

        Intent i = new Intent(MyCompany.this, SponsorDetails.class);
        i.putExtras(args);
        startActivity(i);
    }
*/

    @Override
    protected void onResume() {
        super.onResume();

        SpannableString s = new SpannableString("My Companies");
        setTitle(Util.applyFontToMenuItem(this, s));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            setdata();
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
        if(view.getId()==R.id.mycombut){
            if(e.getTabs(pos).getItemsSize()>0) {
                Paper.book(appDetails.getAppId()).write("action", "Sponsor");
                Intent i = new Intent(MyCompany.this, SponsorRoot.class);
                i.putExtra("pos", pos);
                i.putExtra("title", ctitle);

                startActivity(i);

            }

            else{
                Error_Dialog.show("No details found",MyCompany.this);
            }
        }
    }
}
