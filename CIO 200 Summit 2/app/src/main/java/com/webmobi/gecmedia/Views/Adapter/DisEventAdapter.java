package com.webmobi.gecmedia.Views.Adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.gson.Gson;
import com.webmobi.gecmedia.CustomViews.TxtVCustomFonts;
import com.webmobi.gecmedia.LocalDatabase.DatabaseHandler;
import com.webmobi.gecmedia.Models.Event;
import com.webmobi.gecmedia.Models.Events_Wishlist;
import com.webmobi.gecmedia.Models.OnItemClickListener;
import com.webmobi.gecmedia.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DisEventAdapter extends RecyclerView.Adapter<DisEventAdapter.MyViewHolder> {


    private Activity mContext;

    ArrayList<Event> nearbyevents = new ArrayList<>();
    float ImgWidth, ImgHeight;
    private OnItemClickListener clickListener;

    DatabaseHandler db;

    public DisEventAdapter(Activity mContext, ArrayList<Event> nearbyevents, float ImgWidth, float ImgHeight, OnItemClickListener clickListener) {
        this.mContext = mContext;
        this.nearbyevents = nearbyevents;
        this.ImgWidth = ImgWidth;
        this.ImgHeight = ImgHeight;
        this.clickListener = clickListener;
        db = new DatabaseHandler(this.mContext);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.act_eventsview, parent, false);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        final Event events = nearbyevents.get(listPosition);

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

        holder.locationname.setTypeface(com.singleevent.sdk.Custom_View.Util.regulartypeface(mContext));
        holder.moredetails.setTypeface(com.singleevent.sdk.Custom_View.Util.regulartypeface(mContext));



//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Disc_DetailActivity.launch(mContext, view.findViewById(R.id.logo), events.getApp_image(), events);
//
//            }
//        });


        holder.download.setVisibility(events.getAccesstype().equalsIgnoreCase("dashboard") ? events.isDownloaded() ? View.GONE : View.VISIBLE : View.GONE);

        holder.footer.setWeightSum(events.getAccesstype().equalsIgnoreCase("dashboard") ? events.isDownloaded() ? 1 : 2 : 1);

        holder.moredetails.setText(events.getAccesstype().equalsIgnoreCase("dashboard") ? events.isDownloaded() ? "OPEN APP" : "More Details" : "More Details");

        holder.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setTag(events);
                if (clickListener != null) clickListener.onClick(v, listPosition, holder.itemView);

            }
        });

        holder.moredetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setTag(events);
                if (clickListener != null) clickListener.onClick(v, listPosition, holder.f_image);

            }
        });

      /*  holder.preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setTag(events);
                if (clickListener != null) clickListener.onClick(view, listPosition, holder.f_image);
            }
        });*/


        Glide.with(mContext.getApplicationContext())
                .load((events.getApp_image().equalsIgnoreCase("")) ? R.drawable.medium_no_image : events.getApp_image())
                .asBitmap()
                .placeholder(R.drawable.medium_no_image)
                .error(R.drawable.medium_no_image)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(new BitmapImageViewTarget(holder.f_image) {
                    @Override
                    protected void setResource(Bitmap resource) {

                        holder.f_image.setImageBitmap(scaleBitmap(resource, (int) ImgWidth, (int) ImgHeight));
                    }
                });

        if (db.getwishlist(events.getAppid()) > 0)
            holder.addwishlist.setSelected(true);
        else
            holder.addwishlist.setSelected(false);

        holder.addwishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (db.getwishlist(events.getAppid()) > 0) {
                    db.deletewishlist(events.getAppid());
                    v.setSelected(false);
                } else {

                    Gson gson = new Gson();
                    String details = gson.toJson(events);
                    System.out.println("Nearby Favourite details : "+details);
                    db.addingwishlist(new Events_Wishlist(events.getAppid(), details));
                    v.setSelected(true);
                }

                if (clickListener != null)
                    clickListener.onClick(v, listPosition, holder.addwishlist);

            }
        });


    }

    @Override
    public int getItemCount() {
        return nearbyevents.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public TxtVCustomFonts eventtitle, date;
        TextView moredetails, locationname;
        LinearLayout footer;
        public ImageView f_image, addwishlist;
        RelativeLayout download,preview;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.eventtitle = (TxtVCustomFonts) itemView.findViewById(R.id.event_title);
            this.locationname = (TextView) itemView.findViewById(R.id.locationname);
            this.date = (TxtVCustomFonts) itemView.findViewById(R.id.date);
            this.moredetails = (TextView) itemView.findViewById(R.id.moredetails);
            this.f_image = (ImageView) itemView.findViewById(R.id.logo);
            this.addwishlist = (ImageView) itemView.findViewById(R.id.addwishlist);
            this.footer = (LinearLayout) itemView.findViewById(R.id.footer);
            this.download = (RelativeLayout) itemView.findViewById(R.id.download);
            this.preview = (RelativeLayout) itemView.findViewById(R.id.preview);
        }
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
