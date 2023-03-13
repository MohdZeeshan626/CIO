package com.singleevent.sdk.View.LeftActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.widget.CompoundButtonCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.makeramen.roundedimageview.RoundedImageView;
import com.singleevent.sdk.Custom_View.Util;
import com.singleevent.sdk.Left_Adapter.SponsorsAdapter;
import com.singleevent.sdk.model.AppDetails;
import com.singleevent.sdk.model.Events;
import com.singleevent.sdk.model.Items;
import com.singleevent.sdk.R;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import cyd.awesome.material.AwesomeText;
import io.paperdb.Paper;

/**
 * Created by Admin on 5/30/2017.
 */

public class SponsorRoot extends AppCompatActivity {

    private static final String TAG = SponsorRoot.class.getSimpleName();
    AppDetails appDetails;
    int pos;
    private String title;
    private ArrayList<Events> events = new ArrayList<Events>();
    Events e;
    static ArrayList<Items> exhibitorlist = new ArrayList<>();
    private double ImgWidth, Imgheight;
    LinearLayout contents;
    HashMap<String, Integer> categorylist = new HashMap<>();
    HashMap<String, View> Categoryselected;
    RecyclerView recycler_view;
    private SponsorsAdapter sponsorsAdapter;
    EditText attendee_search;
    private SwipeRefreshLayout refrash;
    ArrayList<Items> exhibitorCategoryFilteredlist = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Paper.init(this);
        setContentView(R.layout.s_sponsorroot);
        appDetails = Paper.book().read("Appdetails");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        recycler_view = (RecyclerView) findViewById(R.id.recycler_view);
        attendee_search = (EditText) findViewById(R.id.attendee_search_edittext);
        refrash = (SwipeRefreshLayout) findViewById(R.id.refrash);
        toolbar.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));
        setSupportActionBar(toolbar);


        // getting the data from previous activity

        if (getIntent().getExtras() == null)
            finish();


        pos = getIntent().getExtras().getInt("pos");
        title = getIntent().getExtras().getString("title");
        events = Paper.book().read("Appevents");
        e = events.get(0);

        if (e.getTabs(pos).getcatSize() > 0) {
            for (int j = 0; j < e.getTabs(pos).getcatSize(); j++) {
                try {
                    String color=e.getTabs(pos).getCategories(j).getColor_code().equals("#")?appDetails.getTheme_color():e.getTabs(pos).getCategories(j).getColor_code();
                    categorylist.put(e.getTabs(pos).getCategories(j).getCategory(),
                            Color.parseColor(color));
                }catch (NumberFormatException e){
                    Log.v(TAG,e.toString());
                }catch (Exception e){
                    Log.v(TAG,e.toString());
                }
            }
        }else{
            Log.d(TAG, "onCreate: ");
        }


        exhibitorlist.clear();

        for (int j = 0; j < e.getTabs(pos).getItemsSize(); j++) {
            exhibitorlist.add(e.getTabs(pos).getItems(j));
        }

        //shorting list based on company name
      /*  Collections.sort(exhibitorlist, new Comparator<Items>() {
            @Override
            public int compare(Items o1, Items o2) {
                return o1.getCompany().compareToIgnoreCase(o2.getCompany());
            }
        });*/


        contents = (LinearLayout) findViewById(R.id.contains);


        //calculating the speaker image size

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        ImgWidth = (displayMetrics.widthPixels) * 0.20;
        Imgheight = (displayMetrics.widthPixels) * 0.15;

        //recycleradapter added for new ui with searchbox
        sponsorsAdapter = new SponsorsAdapter(this,exhibitorlist,categorylist,ImgWidth,Imgheight,0);
        recycler_view.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,
                false));
        recycler_view.setAdapter(sponsorsAdapter);


        Categoryselected = new HashMap<>();
        //setdata();

        attendee_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });

        refrash.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refrash.setRefreshing(true);
                for (String viewkeys : Categoryselected.keySet()) {
                    View v = Categoryselected.get(viewkeys);
                    CheckBox c = (CheckBox) v.findViewById(R.id.checkBox);
                    c.setChecked(false);
                }
                Categoryselected = new HashMap<String, View>();
                exhibitorCategoryFilteredlist.clear();

                //recycleradapter added for new ui with searchbox
                sponsorsAdapter = new SponsorsAdapter(SponsorRoot.this,exhibitorlist,categorylist,ImgWidth,Imgheight,0);
                recycler_view.setLayoutManager(new LinearLayoutManager(SponsorRoot.this, LinearLayoutManager.VERTICAL,
                        false));
                recycler_view.setAdapter(sponsorsAdapter);
                refrash.setRefreshing(false);
            }
        });
    }

    public void filter(String text){
        List<Items> temp = new ArrayList();
        if (Categoryselected.size()>0){
            for(Items d: exhibitorCategoryFilteredlist){

                if(d.getCompany().toLowerCase().contains( text.toLowerCase() )){
                    temp.add(d);
                }
            }
        }
       else{
            for(Items d: exhibitorlist){

                if(d.getCompany().toLowerCase().contains( text.toLowerCase() )){
                    temp.add(d);
                }
            }
        }
        //update recyclerview
        sponsorsAdapter.updateList(temp);
    }

    private void setdata() {
        contents.removeAllViews();

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for (int i = 0; i < exhibitorlist.size(); i++) {
            final Items c = exhibitorlist.get(i);
            View child = inflater.inflate(R.layout.s_sponsorview, null);
            LinearLayout sponsorimage = (LinearLayout) child.findViewById(R.id.sponsorview);
            final RoundedImageView simage = (RoundedImageView) child.findViewById(R.id.sponsorimage);
            RelativeLayout.LayoutParams sparams = (RelativeLayout.LayoutParams) sponsorimage.getLayoutParams();
            sparams.width = (int) ImgWidth;
            sparams.height = (int) Imgheight;
            sponsorimage.setLayoutParams(sparams);
            String Image = c.getImage();
            Glide.with(getApplicationContext())
                    .load((Image.equalsIgnoreCase("")) ? R.drawable.default_partner : Image)
                    .asBitmap()
                    .placeholder(R.drawable.exhibitiorimg)
                    .error(R.drawable.exhibitiorimg)
                    .into(simage);
            simage.setCornerRadius(12,12,12,12);
            /*new BitmapImageViewTarget(simage) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            simage.setImageBitmap(Util.scaleBitmap(resource, (int) ImgWidth, (int) Imgheight));
                        }
                    });*/


            TextView sname = (TextView) child.findViewById(R.id.title);
            TextView sdes = (TextView) child.findViewById(R.id.des);
            TextView scat = (TextView) child.findViewById(R.id.category);
            AwesomeText dots = (AwesomeText) child.findViewById(R.id.dots);
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

                    Intent i = new Intent(SponsorRoot.this, SponsorDetails.class);
                    i.putExtras(args);
                    startActivity(i);
                }
            });


            contents.addView(child);

        }


    }


    private void setfilterdata() {
        exhibitorCategoryFilteredlist = new ArrayList<>();

        for (int i = 0; i < exhibitorlist.size(); i++) {
            final Items c = exhibitorlist.get(i);
            if (Categoryselected.containsKey(c.getCategories())) {

                exhibitorCategoryFilteredlist.add(c);

            }



        }
        //recycleradapter added for new ui with searchbox
        sponsorsAdapter = new SponsorsAdapter(this,exhibitorCategoryFilteredlist,categorylist,ImgWidth,Imgheight,0);
        recycler_view.setAdapter(sponsorsAdapter);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.filtermenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int i = item.getItemId();
        if (i == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (i == R.id.action_filter) {
            show(SponsorRoot.this);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    public void show(Context context) {


        // Create custom dialog object
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
        // Include dialog.xml file
        dialog.setContentView(R.layout.s_filterdialog);

        TextView done = (TextView) dialog.findViewById(R.id.done);
        TextView clearall = (TextView) dialog.findViewById(R.id.clearall);
        AwesomeText backward = (AwesomeText) dialog.findViewById(R.id.backward);
        // setting the fonts
        done.setTypeface(Util.regulartypeface(this));
        clearall.setTypeface(Util.regulartypeface(this));
        done.setTextColor(Color.parseColor(appDetails.getTheme_color()));
        clearall.setTextColor(Color.parseColor(appDetails.getTheme_color()));

        LinearLayout content = (LinearLayout) dialog.findViewById(R.id.content);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        content.removeAllViews();

        for (final String key : categorylist.keySet()) {
            final View child = inflater.inflate(R.layout.s_categorylist, null);
            TextView categoryname = (TextView) child.findViewById(R.id.category);
            categoryname.setTypeface(Util.regulartypeface(this));

            //making first letter letter
            String newCategoryName = key.substring(0, 1).toUpperCase() + key.substring(1);
            categoryname.setText(newCategoryName.toUpperCase());
            AwesomeText dots = (AwesomeText) child.findViewById(R.id.dots);
            dots.setTextColor(categorylist.get(key));
            final CheckBox checked = (CheckBox) child.findViewById(R.id.checkBox);
            int states[][] = {{android.R.attr.state_checked}, {}};
            int colors[] = {Color.parseColor(appDetails.getTheme_color()), Color.GRAY};
            CompoundButtonCompat.setButtonTintList(checked, new ColorStateList(states, colors));
            checked.setChecked(Categoryselected.containsKey(key));
           /* checked.setButtonDrawable(Util.setcheckbox(SponsorRoot.this,
                    R.drawable.n2_ic_checkbox_checked, Color.parseColor(appDetails.getTheme_color())));*/
            checked.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checked.isChecked())
                        Categoryselected.put(key, child);
                    else
                        Categoryselected.remove(key);

                }
            });
            content.addView(child);

        }

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                if (Categoryselected.size() > 0)
                    setfilterdata();
                else{
                    sponsorsAdapter.notifyDataSetChanged();

                }

            }
        });

        clearall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                for (String viewkeys : Categoryselected.keySet()) {
                    View v = Categoryselected.get(viewkeys);
                    CheckBox c = (CheckBox) v.findViewById(R.id.checkBox);
                    c.setChecked(false);
                }
                Categoryselected = new HashMap<String, View>();
                exhibitorCategoryFilteredlist.clear();
                sponsorsAdapter = new SponsorsAdapter(SponsorRoot.this,exhibitorlist,
                        categorylist,ImgWidth,Imgheight,0);

                recycler_view.setAdapter(sponsorsAdapter);

//                dialog.dismiss();
                //setdata();

            }
        });

        backward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();


    }

    @Override
    protected void onResume() {
        super.onResume();

      try {
          SpannableString s = new SpannableString(title);
          setTitle(Util.applyFontToMenuItem(this, s));
      }catch (Exception e){

      }

    }
}
