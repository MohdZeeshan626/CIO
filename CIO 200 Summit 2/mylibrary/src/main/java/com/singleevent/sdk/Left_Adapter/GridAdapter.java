package com.singleevent.sdk.Left_Adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.singleevent.sdk.Custom_View.Util;
import com.singleevent.sdk.model.LocalArraylist.Notes;
import com.singleevent.sdk.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

/**
 * Created by Fujitsu on 14-06-2017.
 */

public class GridAdapter extends ArrayAdapter<Notes> {

    ArrayList<Notes> n = new ArrayList<>();
    Context context;
    private static final SparseArray<Double> sPositionHeightRatios = new SparseArray<Double>();
    private final Random mRandom;
    private final ArrayList<Integer> mBackgroundColors;

    public GridAdapter(Context context, int textViewResourceId, ArrayList<Notes> n) {
        super(context, textViewResourceId, n);
        this.n = n;
        this.context = context;
        mRandom = new Random();
        mBackgroundColors = new ArrayList<Integer>();
        mBackgroundColors.add(R.color.white);

    }

    @Override
    public int getCount() {
        return super.getCount();
    }


    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder vh;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_sample, parent, false);
            vh = new ViewHolder();
            vh.txtLineOne = (TextView) convertView.findViewById(R.id.txt_line1);
            vh.body = (TextView) convertView.findViewById(R.id.text_body);
            vh.time = (TextView) convertView.findViewById(R.id.time_item);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        Notes note = n.get(position);

        int backgroundIndex = position >= mBackgroundColors.size() ?
                position % mBackgroundColors.size() : position;

        convertView.setBackgroundResource(mBackgroundColors.get(backgroundIndex));
        double positionHeight = getPositionRatio(position);

//        vh.body.setHeightRatio(positionHeight);

        vh.txtLineOne.setText(note.getName());
        vh.body.setText(note.getNotes());

        vh.txtLineOne.setTypeface(Util.boldtypeface(context));
        vh.body.setTypeface(Util.regulartypeface(context));
        vh.time.setTypeface(Util.lighttypeface(context));
        vh.time.setText(converlontostring(Long.valueOf(note.getLast_updated())));


        return convertView;
    }

    static class ViewHolder {
        TextView body;
        //        Button btnGo;
        TextView txtLineOne, time;
    }

    private String converlontostring(Long key) {

        SimpleDateFormat myFormat = new SimpleDateFormat("d MMM h:mm a");
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(key);
        return myFormat.format(cal.getTime());
    }


    private double getPositionRatio(final int position) {
        double ratio = sPositionHeightRatios.get(position, 0.0);
        // if not yet done generate and stash the columns height
        // in our real world scenario this will be determined by
        // some match based on the known height and width of the image
        // and maybe a helpful way to get the column height!
        if (ratio == 0) {
            ratio = getRandomHeightRatio();
            sPositionHeightRatios.append(position, ratio);
        }
        return ratio;
    }

    private double getRandomHeightRatio() {
        return (mRandom.nextDouble() / 2.0) + .5; // height will be 1.0 - 1.5 the width
    }



}
