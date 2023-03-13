package com.webmobi.gecmedia.Views.Adapter;

import android.app.Activity;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.webmobi.gecmedia.CustomViews.ColorFilterTransformation;
import com.webmobi.gecmedia.Models.Event;
import com.webmobi.gecmedia.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by Lenovo on 18-05-2018.
 */

public class SearchAdapter extends ArrayAdapter<Event> {

    Activity context;
    ArrayList<Event> nearbyevents = new ArrayList<>();

    public SearchAdapter(@NonNull Activity context, int resource, @NonNull ArrayList<Event> objects) {
        super(context, resource, objects);
        this.context = context;
        this.nearbyevents = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.search_row, null, true);

        ImageView event_ic = (ImageView) rowView.findViewById(R.id.search_event_ic);
        TextView event_title = (TextView) rowView.findViewById(R.id.search_event_name);
        TextView even_date = (TextView) rowView.findViewById(R.id.search_date);
        TextView event_location = (TextView) rowView.findViewById(R.id.search_location);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MMM-yyyy");
        String date_str = nearbyevents.get(position).getStart_date().split("T")[0];
        /*Date date = formatter.parse();
        even_date.setText(formatter1.format(date));*/
        event_title.setText(nearbyevents.get(position).getApp_name());

        even_date.setText(date_str);
        event_location.setText(nearbyevents.get(position).getLocation());
        System.out.println("Adapter JSON Value : "+nearbyevents.get(position).getAppid());
        Glide.with(context.getApplicationContext()).load((nearbyevents.get(position).getApp_logo().equalsIgnoreCase("")) ? R.drawable.medium_no_image : nearbyevents.get(position).getApp_logo())
                .fitCenter()
                .placeholder(R.drawable.medium_no_image)
                .dontAnimate()
                .error(R.drawable.medium_no_image)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .bitmapTransform(new ColorFilterTransformation(context, Color.argb(0, 0, 0, 0)))
                .into(event_ic);
        return rowView;
    }
}