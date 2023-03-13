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
 * Created by Lenovo on 30-08-2018.
 */

public class EventCategoryAdapter extends ArrayAdapter {
    Activity context;
    ArrayList<Event> nearbyevents = new ArrayList<>();

    public EventCategoryAdapter(@NonNull Activity context, int resource, @NonNull ArrayList<Event> objects) {
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
        event_title.setText(nearbyevents.get(position).getApp_title());

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

/*
public class EventCategoryAdapter extends RecyclerView.Adapter<EventCategoryAdapter.ViewHolder> {
    private static Context context1;
    private List<Event> eventUserList;
    static OnCardClickListner onCardClickListner;

    public EventCategoryAdapter(Context context, List<Event> myDataset) {
        this.context1 = context;
        this.eventUserList = myDataset;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context1)
                .inflate(R.layout.search_row, parent, false);
        return new EventCategoryAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // - get element from your dataset at this position
        final Event event = eventUserList.get(position);

        holder.setItem(event, position, eventUserList);
    }

    public void updateList(List<Event> list) {

        eventUserList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return eventUserList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        ImageView event_ic;
        TextView event_title;
        TextView even_date;
        TextView event_location;
        Event event;
        static OnCardClickListner onCardClickListner;

        public ViewHolder(View itemView) {
            super(itemView);
            event_ic = (ImageView) itemView.findViewById(R.id.search_event_ic);
            event_title = (TextView) itemView.findViewById(R.id.search_event_name);
            even_date = (TextView) itemView.findViewById(R.id.search_date);
            event_location = (TextView) itemView.findViewById(R.id.search_location);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            System.out.println("Item clicked pos " + getAdapterPosition());

            int position = getAdapterPosition();
           */
/* if (v.getId()==authorName.getId()){

            }*//*

            onCardClickListner.OnItemClick(view, event, position);
        }

        @Override
        public boolean onLongClick(View view) {
            return false;
        }

        void setItem(Event event, int position, List<Event> eventList) {
            this.event = event;
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MMM-yyyy");
            String date_str = eventList.get(position).getStart_date().split("T")[0];
      */
/* Date date = formatter.parse();
            even_date.setText(formatter1.format(date));*//*

            event_title.setText(eventList.get(position).getApp_name());

            even_date.setText(date_str);
            event_location.setText(eventList.get(position).getLocation());
            System.out.println("Adapter JSON Value : " + eventList.get(position).getAppid());
            Glide.with(context1).load((eventList.get(position).getApp_logo().equalsIgnoreCase("")) ? R.drawable.medium_no_image : eventList.get(position).getApp_logo())
                    .fitCenter()
                    .placeholder(R.drawable.medium_no_image)
                    .dontAnimate()
                    .error(R.drawable.medium_no_image)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .bitmapTransform(new ColorFilterTransformation(context1, Color.argb(0, 0, 0, 0)))
                    .into(event_ic);
        }

    }

    public void setOnCardClickListner(OnCardClickListner onCardClickListner) {
        ViewHolder.onCardClickListner = onCardClickListner;
    }

    public interface OnCardClickListner {
        void OnItemLongClicked(View view, Event user, int position);

        void OnItemClick(View view, Event user, int position);
    }
}*/
