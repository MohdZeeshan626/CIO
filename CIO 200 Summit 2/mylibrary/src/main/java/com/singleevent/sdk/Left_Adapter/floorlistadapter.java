package com.singleevent.sdk.Left_Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.singleevent.sdk.Custom_View.Error_Dialog;
import com.singleevent.sdk.model.Map.Mapfloorplan;
import com.singleevent.sdk.R;
import com.singleevent.sdk.View.Fragment.Left_Fragment.floorimageview;

import java.util.ArrayList;

/**
 * Created by webmodi on 4/15/2016.
 */
public class floorlistadapter extends BaseAdapter {

    Activity context;
    private ArrayList<Mapfloorplan> eventsToDisplay1 = new ArrayList<>();
    private int lastPosition = -1;

    public floorlistadapter(Activity context1, ArrayList<Mapfloorplan> eventsToDisplay1) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        Log.v("ConvertView", String.valueOf(position));

        if (convertView == null) {

            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.s_agendalist, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.txt);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        lastPosition = position;
        final Mapfloorplan c = eventsToDisplay1.get(position);
        holder.name.setText(c.getName());

        convertView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (!c.getImageUrl().equalsIgnoreCase("")) {
                    Intent i = new Intent(context, floorimageview.class);
                    i.putExtra("imageurl", c.getImageUrl());
                    context.startActivity(i);
                } else
                    Error_Dialog.show("Invalid Details", context);

            }
        });
        return convertView;
    }

    private class ViewHolder {
        TextView name;
    }
}
