package com.singleevent.sdk.Left_Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.singleevent.sdk.Custom_View.Error_Dialog;
import com.singleevent.sdk.model.AppDetails;
import com.singleevent.sdk.model.Events;
import com.singleevent.sdk.model.Items;
import com.singleevent.sdk.R;
import com.singleevent.sdk.View.LeftActivity.AdLandingWebview;
import com.singleevent.sdk.View.LeftActivity.SponsorDetails;

import java.util.ArrayList;
import java.util.HashMap;

import io.paperdb.Paper;

/**
 * Created by Lenovo on 22-03-2018.
 */

    public class AdsImageAdapter extends PagerAdapter {
    private ArrayList<String> ban_type;
    private ArrayList<String> btn_url;
    private ArrayList<Integer> type_id;
    private ArrayList<String> ban_name;
    private ArrayList<String> images;
    private LayoutInflater inflater;
    private Context context;
    private ArrayList<Events> events = new ArrayList<Events>();
    static ArrayList<Items> exhibitorlist = new ArrayList<>();
    Events e;
    AppDetails appDetails;
    int pos, typeid;
    Items c = null;
    Activity activity;
    HashMap<String, Integer> categorylist = new HashMap<>();

    public AdsImageAdapter(Activity activity, Context context, ArrayList<String> images, ArrayList<String> btn_url, ArrayList<String> ban_type, ArrayList<Integer> type_id, ArrayList<String> ban_name) {
       //constructor
        //assigning the values
        Paper.init(context);
        this.context = context;
        this.images = images;
        this.ban_type = ban_type;
        this.btn_url = btn_url;
        this.type_id = type_id;
        this.ban_name = ban_name;
        this.activity = activity;
        inflater = LayoutInflater.from(context);
        events = Paper.book().read("Appevents");
        e = events.get(0);
        appDetails = Paper.book().read("Appdetails");
    }

    private void setEventsListsItems() {
        //adding the exhibitors to the lists
        for (int j = 0; j < e.getTabs(pos).getItemsSize(); j++) {
            exhibitorlist.add(e.getTabs(pos).getItems(j));
        }


        if (e.getTabs(pos).getcatSize() > 0) {
            for (int j = 0; j < e.getTabs(pos).getcatSize(); j++) {
                try {
                    //adding category and category color to the lists
                    categorylist.put(e.getTabs(pos).getCategories(j).getCategory(),
                            Color.parseColor(e.getTabs(pos).getCategories(j).getColor_code()));
                } catch (NumberFormatException e) {
                    System.out.println(e.toString());
                } catch (Exception e) {
                    System.out.println(e.toString());
                }


            }
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return images.size();
    }


    @Override
    public Object instantiateItem(final ViewGroup view, final int position) {
        View myImageLayout = inflater.inflate(R.layout.ads_image_layout, view, false);
        ImageView myImage = (ImageView) myImageLayout
                .findViewById(R.id.adsImage);

        //setting images to the imageview
        Glide.with(context.getApplicationContext()).load(images.get(position)).into(myImage);
        System.out.println("Banner Type : " + ban_type.get(position) + " Banner URL : " + btn_url.get(position));

        //click event for banner image
        myImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {

                System.out.println("Banner Image Clicked : Button Type = " + ban_type.get(position) + " button url : " + btn_url.get(position));
                //if the banner type is " Website","Image","Custom" it will navigate to webview
                if (ban_type.get(position).equalsIgnoreCase("Website") || ban_type.get(position).equalsIgnoreCase("Image") || ban_type.get(position).equalsIgnoreCase("Custom")) {
                    Intent i = new Intent(context, AdLandingWebview.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.putExtra(AdLandingWebview.BANNER_NAME, ban_name.get(position));
                    i.putExtra(AdLandingWebview.BANNER_URL, btn_url.get(position));
                    context.startActivity(i);
                } else if (ban_type.get(position).equalsIgnoreCase("Exhibitor")) {
                    //banner type = "Exhibitor"
                    //first will check whether the user is logged in or not
                    if (Paper.book().read("Islogin", false)) {
                        //if the user is logged in
                        exhibitorAds(position);
                    } else {
                        //if user is not logged in
                        //if it is public event it will call the method
                        //otherwise it will show the error msg to login
                        if (!appDetails.getInfo_privacy())
                            exhibitorAds(position);
                        else {
                            Error_Dialog.show("Please Login", activity);
                            Toast.makeText(context, "Please Login", Toast.LENGTH_LONG);
                        }
                    }
                } else if (ban_type.get(position).equalsIgnoreCase("Sponsor")) {
                    //banner type = "Exhibitor"
                    //first will check whether the user is logged in or not
                    if (Paper.book().read("Islogin", false)) {
                        sponsorAds(position);
                    } else {
                        if (!appDetails.getInfo_privacy())
                            sponsorAds(position);
                        else {
                            Error_Dialog.show("Please Login", activity);
                            Toast.makeText(context, "Please Login", Toast.LENGTH_LONG);
                        }
                    }
                }
            }
        });
        view.addView(myImageLayout, 0);
        return myImageLayout;
    }

    private void sponsorAds(int position) {
        //register the action as Sponsor
        Paper.book().write("action", "Sponsor");
        for (int i = 0; i < e.getTabs().length; i++)
            if (e.getTabs(i).getType().contains("sponsorsData")) {
                pos = i;
                //getting Sponsor type id
                typeid = type_id.get(position);
                break;
            }
        setEventsListsItems();
        Paper.book(appDetails.getAppId()).write("action", "Sponsor");
        navigateAdExhibitor(typeid);
    }

    private void exhibitorAds(int position) {
        //register the action as Exhibitor
        Paper.book().write("action", "Exhibitor");
        for (int i = 0; i < e.getTabs().length; i++)
            if (e.getTabs(i).getType().contains("exhibitorsData")) {
                pos = i;
                //getting exhibitors type id
                typeid = type_id.get(position);
                break;
            }
        setEventsListsItems();
        Paper.book(appDetails.getAppId()).write("action", "Exhibitor");

        navigateAdExhibitor(typeid);
    }

    private void navigateAdExhibitor(int position) {
        //Common navigation method for both sponsors and exhibitors
        int temp_pos = 1;
        for (int i = 0; i < exhibitorlist.size(); i++) {
            if (exhibitorlist.get(i).getSponsor_id() == position) {
                temp_pos = i;
                break;
            }
        }

        //getting the particular exhibitor/sponsor data
        c = exhibitorlist.get(temp_pos);

        Bundle args = new Bundle();
        args.putInt("Color", categorylist.get(c.getCategories()));
        args.putSerializable("Items", c);
        //navigating to the SponsorsDetails Page
        Intent i = new Intent(context, SponsorDetails.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtras(args);
        context.startActivity(i);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }
}
