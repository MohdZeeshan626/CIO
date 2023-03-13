package com.singleevent.sdk.Left_Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.singleevent.sdk.Custom_View.Error_Dialog;
import com.singleevent.sdk.model.Items;
import com.singleevent.sdk.R;
import com.singleevent.sdk.View.LeftActivity.SocialMediaView;
import com.singleevent.sdk.View.LeftActivity.TwitterHashTag;

import java.util.ArrayList;

/**
 * Created by Admin on 5/31/2017.
 */

public class SocialMediaAdapter extends BaseAdapter {
    private int lastPosition = -1;
    Activity context;
    private ArrayList<Items> eventsToDisplay1 = new ArrayList<>();

    public SocialMediaAdapter(Activity context1, ArrayList<Items> eventsToDisplay1) {
        // TODO Auto-generated constructor stub
        this.eventsToDisplay1 = eventsToDisplay1;
        context = context1;

    }

    @Override
    public int getCount() {
        return eventsToDisplay1.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        Log.v("ConvertView", String.valueOf(position));
        if (convertView == null) {
           try {
               LayoutInflater vi = (LayoutInflater) context.getSystemService(
                       Context.LAYOUT_INFLATER_SERVICE);
               convertView = vi.inflate(R.layout.s_socialmediaview, null);

               holder = new ViewHolder();
               holder.txtmedianame = (TextView) convertView.findViewById(R.id.medianame);
               holder.mediaurl = (TextView) convertView.findViewById(R.id.mediaurl);
               holder.logo = (ImageView) convertView.findViewById(R.id.logo);

               convertView.setTag(holder);
           }catch(Exception e)
           {

           }
        } else {
           try {
               holder = (ViewHolder) convertView.getTag();
           }catch (Exception e)
           {

           }

        }


        final Items c = eventsToDisplay1.get(position);
        holder.txtmedianame.setText(c.getName());
     try {
         if (c.getUrl() != null) {
             holder.mediaurl.setText(c.getUrl());
             holder.mediaurl.setVisibility(View.VISIBLE);
         }
     }catch (Exception e){

     }

        //holder.mediaurl.setVisibility(View.GONE);


        if (c.getType().compareToIgnoreCase("facebook") == 0) {
            holder.logo.setBackground(context.getResources().getDrawable(R.drawable.fb2));

        }

        else if (c.getType().compareToIgnoreCase("linkedin") == 0) {
            holder.logo.setBackground(context.getResources().getDrawable(R.drawable.linkedin2));

        }
        else if (c.getType().compareToIgnoreCase("twitter") == 0) {
            holder.logo.setBackground(context.getResources().getDrawable(R.drawable.twitter2));

        }
        else if (c.getType().compareToIgnoreCase("instagram") == 0) {
            holder.logo.setBackground(context.getResources().getDrawable(R.drawable.insta2));

        }
        else if (c.getType().compareToIgnoreCase("sharepost") == 0)
            holder.logo.setBackground(context.getResources().getDrawable(R.drawable.other2));
        else if (c.getType().compareToIgnoreCase("google+") == 0)
            holder.logo.setBackground(context.getResources().getDrawable(R.drawable.google2));
        else if (c.getType().compareToIgnoreCase("google") == 0)
            holder.logo.setBackground(context.getResources().getDrawable(R.drawable.google2));
        else if (c.getType().compareToIgnoreCase("Twitterhash") == 0)
            holder.logo.setBackground(context.getResources().getDrawable(R.drawable.twitter2));

        else
            holder.logo.setBackground(context.getResources().getDrawable(R.drawable.other2));

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //calling twitter hashtag activity

              try {
                  if (String.valueOf(c.getUrl().charAt(0)).compareTo("#") == 0) {

                      Intent twitterHashTag = new Intent(context, TwitterHashTag.class);
                      if (String.valueOf(c.getUrl().charAt(0)).compareTo("#") == 0)
                          twitterHashTag.putExtra("HashTagStrValue", c.getUrl());
                      else
                          twitterHashTag.putExtra("HashTagStrValue", "#" + c.getUrl());
                      twitterHashTag.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                      context.startActivity(twitterHashTag);

                  } else if (c.getType().compareTo("sharepost") != 0) {

                      if (c.getUrl() != null) {
                          if (c.getUrl().compareTo("") != 0) {
                              try {
                               /* Intent i2 = new Intent(Intent.ACTION_VIEW,
                                        Uri.parse(c.getUrl()));*/

                                  Intent i3 = new Intent(context, SocialMediaView.class);
                                  i3.putExtra("url", c.getUrl());
                                  i3.putExtra("title", c.getType());
                                  context.startActivity(i3);
                              } catch (Exception e) {
                                  System.out.println("URL Exception : " + e.toString());
                                  Error_Dialog.show("Invalid Url", context);
                              }
                          } else
                              Error_Dialog.show("Sorry there is no url", context);
                      } else {
                          Error_Dialog.show("Sorry there is no url", context);
                      }

                  } else {
                      Intent share = new Intent(Intent.ACTION_SEND);
                      share.setType("text/plain");
                      share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                      context.startActivity(Intent.createChooser(share, "Share Via!"));
                  }
              }catch (Exception e)
              {

              }
            }
        });
        return convertView;
    }

    private class ViewHolder {
        TextView txtmedianame,mediaurl;
        ImageView logo;
    }

}