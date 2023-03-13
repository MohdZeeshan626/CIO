package com.singleevent.sdk.health.Adapter;

import android.actionsheet.demo.com.khoiron.actionsheetiosforandroid.ActionSheet;
import android.actionsheet.demo.com.khoiron.actionsheetiosforandroid.Interface.ActionSheetCallBack;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.singleevent.sdk.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewCompleteChallengeAdapter extends RecyclerView.Adapter<RecyclerViewCompleteChallengeAdapter.ViewHolder>{

    private static final String TAG = "RecyclerViewAdapter";


    private ArrayList<String> mDate = new ArrayList<>();





    private Context mContext;

    public RecyclerViewCompleteChallengeAdapter(ArrayList<String> mDate, Context mContext) {
        this.mDate = mDate;
        this.mContext = mContext;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }


    @Override
    public RecyclerViewCompleteChallengeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout_completechallenge, parent, false);
        RecyclerViewCompleteChallengeAdapter.ViewHolder holder = new RecyclerViewCompleteChallengeAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.txtStartDate.setText(mDate.get(position));



    }

    @Override
    public int getItemCount() {
        return mDate.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder{


        TextView txtStartDate;
        RelativeLayout parentLayout;
        Button btn_more_options;

        public ViewHolder(View itemView) {
            super(itemView);
            txtStartDate = itemView.findViewById(R.id.txt_complete_Challenge_endDate);
            parentLayout = itemView.findViewById(R.id.parentRelative_complete);
            btn_more_options = itemView.findViewById(R.id.btn_material_completed);
            ArrayList<String> data = new ArrayList<>();

            data.add("View Challenge Details");
//            data.add("Leave Challenge");
//            data.add("Move");
//            data.add("Duplicate");

            btn_more_options.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    new ActionSheet(mContext,data)
                            .setTitle("Challenge Options")
                            .setCancelTitle("Cancel")
                            .setColorTitle(Color.BLUE)
                            .setColorTitleCancel(Color.RED)
                            .setColorData(Color.BLUE)
                            .create(new ActionSheetCallBack() {
                                @Override
                                public void data(@NotNull String data, int position) {
                                    switch (position){
                                        case 0:

                                            // your action
                                        case 1:
                                            // your action
                                        case 2:
                                            // your action
                                        case 3:
                                            // your action
                                    }
                                }
                            });
                }
            });


        }
    }
}
