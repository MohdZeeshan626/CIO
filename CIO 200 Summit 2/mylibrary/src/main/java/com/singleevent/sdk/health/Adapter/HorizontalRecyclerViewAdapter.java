package com.singleevent.sdk.health.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.singleevent.sdk.R;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;
import io.paperdb.Paper;

public class HorizontalRecyclerViewAdapter extends RecyclerView.Adapter<HorizontalRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";

    //vars
    private ArrayList<String> mSteps = new ArrayList<>();
    private ArrayList<String> mMiles = new ArrayList<>();
    private ArrayList<String> mMins = new ArrayList<>();
    private ArrayList<String> mTimeStamp = new ArrayList<>();
    private Context mContext;

    public HorizontalRecyclerViewAdapter(ArrayList<String> mTimeStamp,ArrayList<String> mSteps,
                                         ArrayList<String> mMiles,
                                         ArrayList<String> mMins, Context mContext) {
        this.mTimeStamp= mTimeStamp;
        this.mSteps = mSteps;
        this.mMiles = mMiles;
        this.mMins = mMins;
        this.mContext = mContext;


        if(mTimeStamp.size()>0){
            Paper.book().write("TimeStamp",mTimeStamp);}
        if(mSteps.size()>0){
        Paper.book().write("Weekly_Steps",mSteps);}
        if(mMiles.size()>0)
        {
            Paper.book().write("Weekly_Miles",mMiles);
        }
        if(mMins.size()>0){
            Paper.book().write("Weekly_Mins",mMins);}
        }



    @Override
    public HorizontalRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_horizontalitems, parent, false);
        HorizontalRecyclerViewAdapter.ViewHolder holder = new HorizontalRecyclerViewAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");

       try {
           holder.date.setText(mTimeStamp.get(position));
           holder.steps.setText(mSteps.get(position));
           holder.miles.setText(mMiles.get(position));
           holder.time.setText(mMins.get(position));
       }catch (Exception e)
       {

       }

    }

    @Override
    public int getItemCount() {
        return mSteps.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{


        TextView steps,miles,time,date;

        public ViewHolder(View itemView) {
            super(itemView);
            steps = itemView.findViewById(R.id.txt_Steps);
            miles = itemView.findViewById(R.id.txt_Miles);
            time = itemView.findViewById(R.id.txt_Mins);
            date= itemView.findViewById(R.id.txtTodaysActivity);

        }
    }
}