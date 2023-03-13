package com.webmobi.gecmedia.Views.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.webmobi.gecmedia.R;

/**
 * Created by Lenovo on 04-05-2018.
 */

public class CategoryListAdapter extends ArrayAdapter<String> {
    private final Activity context;
    private final String[] cat_name;
    private final Integer[] imgid;

    public CategoryListAdapter(int resource, String[] cat_name, Integer[] imgid, Activity context) {
        super(context, resource, cat_name);
        this.context = context;
        this.cat_name = cat_name;
        this.imgid = imgid;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.category_list_row, null, true);

        TextView titleText = (TextView) rowView.findViewById(R.id.cat_list_name);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.cat_list_ic);

        titleText.setText(cat_name[position]);
        imageView.setImageResource(imgid[position]);

        return rowView;

    }

    ;
}
