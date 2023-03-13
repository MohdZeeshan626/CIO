package com.singleevent.sdk.View.Fragment.Left_Fragment;

/**
 * Created by webMOBI on 10/11/2017.
 */

import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import android.graphics.Color;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.singleevent.sdk.Custom_View.Util;


import com.singleevent.sdk.feeds_class.Scheduler;
import com.singleevent.sdk.model.AppDetails;

import com.singleevent.sdk.model.My_Request;
import com.singleevent.sdk.R;


import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

import io.paperdb.Paper;

/**
 * Created by carl on 12/1/15.
 */

public class MyAdapter extends RecyclerView.Adapter<CustomViewHolder> {
    public List<My_Request> mMyModels;
    AppDetails appDetails;
    int background;

    public Scheduler sch;
    boolean isRequest;

    public MyAdapter(List<My_Request> myModels, Scheduler sch, boolean isRequest) {



        mMyModels = myModels;
        this.sch = sch;
        this.isRequest = isRequest;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        Paper.init(viewGroup.getContext());
        appDetails = Paper.book().read("Appdetails");
        background =  Color.parseColor(appDetails.getTheme_color());


        ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()),
                R.layout.s_requestinvitation, viewGroup, false);
        return new CustomViewHolder(binding, sch,isRequest,background);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, int i) {
        ViewDataBinding viewDataBinding = customViewHolder.getViewDataBinding();
        viewDataBinding.setVariable(com.singleevent.sdk.BR.myrequest, mMyModels.get(i));
    }

    @Override
    public int getItemCount() {
        return (null != mMyModels ? mMyModels.size() : 0);
    }

    @BindingAdapter({"fromtime", "totime"})
    public static void loadtime(TextView ttimeiew, long fromtime, long tottime) {
        ttimeiew.setText(Util.TimeToTime(fromtime) + " - " + Util.TimeToTime(tottime));
    }

    @BindingAdapter({"month"})
    public static void loadmonth(TextView monthview, long monthtime) {

        SimpleDateFormat myFormat = new SimpleDateFormat("MMM");
        myFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        monthview.setText(myFormat.format(monthtime));
    }


    @BindingAdapter({"date"})
    public static void loaddate(TextView monthview, long datetime) {
        SimpleDateFormat myFormat = new SimpleDateFormat("dd");
        myFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        monthview.setText(myFormat.format(datetime));
    }

    @BindingAdapter({"accept"})
    public static void setBackground(Button button, int background){
        button.setBackgroundColor(background);
    }

    @BindingAdapter({"relativeLayout2"})
    public static void setBackground(RelativeLayout relativeLayout,int background ){
        relativeLayout.setBackgroundColor(background);
    }


}
