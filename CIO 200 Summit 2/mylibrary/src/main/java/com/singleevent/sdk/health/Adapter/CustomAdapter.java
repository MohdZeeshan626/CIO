package com.singleevent.sdk.health.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.singleevent.sdk.R;
import com.singleevent.sdk.health.Model.CustomItem;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CustomAdapter extends ArrayAdapter {
    public CustomAdapter(@NonNull Context context, ArrayList<CustomItem> customAdapters) {
        super(context, 0, customAdapters);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView==null){
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.custom_dropdown_layout,parent,false);
        }
        CustomItem item= (CustomItem) getItem(position);
        ImageView imageView=convertView.findViewById(R.id.ivdrpdownimage);
        TextView textView=convertView.findViewById(R.id.tvdropdown);
        if(item!=null) {
            imageView.setImageResource(item.getSpinnneritemimage());
            textView.setText(item.getSpinneritemname());
        }return  convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView==null){
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.custom_dropdown_layout,parent,false);
        }
        CustomItem item= (CustomItem) getItem(position);
        ImageView imageView1=convertView.findViewById(R.id.ivdrpdownimage);
        TextView textView1=convertView.findViewById(R.id.tvdropdown);
        if(item!=null) {
            imageView1.setImageResource(item.getSpinnneritemimage());
            textView1.setText(item.getSpinneritemname());
        }return  convertView;
    }
}
