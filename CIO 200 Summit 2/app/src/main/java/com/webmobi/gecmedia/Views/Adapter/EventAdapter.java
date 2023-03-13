package com.webmobi.gecmedia.Views.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.webmobi.gecmedia.CustomViews.ScrollDetectableListView;
import com.webmobi.gecmedia.CustomViews.TxtVCustomFonts;
import com.webmobi.gecmedia.Models.Event;
import com.webmobi.gecmedia.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Admin on 4/28/2017.
 */

public class EventAdapter extends BaseAdapter {


    private Activity mContext;
    ArrayList<Event> CategoryEvents = new ArrayList<>();
    float ImgWidth, ImgHeight;
    ScrollDetectableListView filmlists;

    public EventAdapter(Activity mContext, ScrollDetectableListView filmlists, ArrayList<Event> CategoryEvents, float ImgWidth, float ImgHeight) {
        this.CategoryEvents = CategoryEvents;
        this.mContext = mContext;
        this.ImgWidth = ImgWidth;
        this.ImgHeight = ImgHeight;
        this.filmlists = filmlists;
    }

    @Override
    public int getCount() {
        return CategoryEvents.size();
    }

    @Override
    public Object getItem(int i) {
        return CategoryEvents.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        MyViewHolder holder = null;

        if (view == null) {
            LayoutInflater vi = (LayoutInflater) mContext.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            view = vi.inflate(R.layout.act_eventsview, null);
            holder = new MyViewHolder();
            holder.eventtitle = (TxtVCustomFonts) view.findViewById(R.id.event_title);
            holder.locationname = (TextView) view.findViewById(R.id.locationname);
            holder.date = (TxtVCustomFonts) view.findViewById(R.id.date);
            holder.moredetails = (TextView) view.findViewById(R.id.moredetails);
            holder.f_image = (ImageView) view.findViewById(R.id.logo);
            holder.addwishlist = (ImageView) view.findViewById(R.id.addwishlist);
            holder.download = (RelativeLayout) view.findViewById(R.id.download);
            view.setTag(holder);
        } else {
            holder = (MyViewHolder) view.getTag();
        }

        final Event events = CategoryEvents.get(i);
        holder.eventtitle.setText(events.getApp_name());

        holder.locationname.setSelected(true);
        holder.locationname.setText(events.getLocation());
        String Startsplits = events.getStart_date().split("T")[0];
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MMM-yyyy");
        try {
            Date date = formatter.parse(Startsplits);
            holder.date.setText(formatter1.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Typeface type = Typeface.createFromAsset(mContext.getAssets(), "fonts/Mark Simonson - Proxima Nova Alt Regular.otf");
        holder.locationname.setTypeface(type);


        final MyViewHolder finalHolder = holder;

        Glide.with(mContext.getApplicationContext())
                .load((events.getApp_image().equalsIgnoreCase("")) ? R.drawable.medium_no_image : events.getApp_image())
                .asBitmap()
                .placeholder(R.drawable.medium_no_image)
                .error(R.drawable.medium_no_image)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(new BitmapImageViewTarget(finalHolder.f_image) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        finalHolder.f_image.setImageBitmap(scaleBitmap(resource,(int)ImgWidth,(int)ImgHeight));


                    }
                });


        holder.download.setVisibility(events.isDownloaded() ? View.GONE : View.VISIBLE);

        holder.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setTag(events);
                filmlists.performItemClick(v, i, 0); // Let the event be handled in onItemClick()

            }
        });

        holder.moredetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setTag(events);
                filmlists.performItemClick(v, i, 0); // Let the event be handled in onItemClick()

            }
        });


        return view;
    }


    private class MyViewHolder {
        public TxtVCustomFonts eventtitle, date;
        TextView moredetails, locationname;
        public ImageView f_image, addwishlist;
        RelativeLayout download;
    }

    public static Bitmap scaleBitmap(Bitmap bitmap, int newWidth, int newHeight) {
        Bitmap scaledBitmap = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888);

        float scaleX = newWidth / (float) bitmap.getWidth();
        float scaleY = newHeight / (float) bitmap.getHeight();
        float pivotX = 0;
        float pivotY = 0;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(scaleX, scaleY, pivotX, pivotY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bitmap, 0, 0, new Paint(Paint.FILTER_BITMAP_FLAG));

        return scaledBitmap;
    }
}
