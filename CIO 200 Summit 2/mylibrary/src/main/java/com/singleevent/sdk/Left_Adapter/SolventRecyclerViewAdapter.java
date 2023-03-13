package com.singleevent.sdk.Left_Adapter;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.singleevent.sdk.Custom_View.Util;
import com.singleevent.sdk.model.AppDetails;
import com.singleevent.sdk.model.LocalArraylist.Notes;
import com.singleevent.sdk.R;
import com.singleevent.sdk.View.RightActivity.NotesDetail;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

import io.paperdb.Paper;

/**
 * Created by Admin on 6/26/2017.
 */

public class SolventRecyclerViewAdapter extends RecyclerView.Adapter<SolventRecyclerViewAdapter.ViewHolder> {

    ArrayList<Notes> n = new ArrayList<>();
    Context context;
    AppDetails appDetails;

    public SolventRecyclerViewAdapter(Context context, ArrayList<Notes> n) {
        this.n = n;
        this.context = context;
        appDetails = Paper.book().read("Appdetails");

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView body, txtLineOne, time;

        public ViewHolder(View view) {
            super(view);
            txtLineOne = (TextView) view.findViewById(R.id.txt_line1);
            body = (TextView) view.findViewById(R.id.text_body);
            time = (TextView) view.findViewById(R.id.time_item);
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_sample, parent, false);

        return new ViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Notes note = n.get(position);
        holder.txtLineOne.setText(note.getName());
        holder.body.setText(note.getNotes());

        holder.txtLineOne.setTypeface(Util.boldtypeface(context));
        holder.body.setTypeface(Util.regulartypeface(context));
        holder.time.setTypeface(Util.lighttypeface(context));
        holder.time.setText(converlontostring(Long.valueOf(note.getLast_updated())));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, NotesDetail.class);
                i.putExtra("pos", note.getId());
                i.putExtra("type", note.getType());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return n.size();
    }

    /* private String converlontostring(Long key) {

         SimpleDateFormat myFormat = new SimpleDateFormat("d MMM h:mm a");
         myFormat.setTimeZone(TimeZone.getTimeZone(appDetails.getTimezone()));

         Calendar cal = Calendar.getInstance();
         cal.setTimeInMillis(key);
         return myFormat.format(cal.getTime());
     }*/
    private String converlontostring(Long key) {

        Calendar cal = Calendar.getInstance();
        TimeZone tz = cal.getTimeZone();
        String zone, timeZone;
        timeZone = appDetails.getTimezone();
        if (timeZone == "") {
            zone = "Asia/Calcutta";
        } else if (timeZone.equalsIgnoreCase("IST")) {
            zone = "Asia/Calcutta";
        } else if (timeZone.equalsIgnoreCase("PST")) {
            zone = "America/Los_Angeles";
        } else if (timeZone.equalsIgnoreCase("EST")) {
            zone = "America/New_York";
        } else if (timeZone.equalsIgnoreCase("CST")) {
            zone = "America/Chicago";
        } else {
            zone = "Asia/Calcutta";
        }

        /* date formatter in local timezone */
        SimpleDateFormat sdf = new SimpleDateFormat("d MMM h:mm a"/*"dd/MM/yyyy HH:mm:ss" */);
        sdf.setTimeZone(TimeZone.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone(zone));

        /* print your timestamp and double check it's the date you expect */
        long timestamp = key;
        String localTime = sdf.format(timestamp);

        return localTime;
    }

}
