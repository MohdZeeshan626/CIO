package com.singleevent.sdk.health.Adapter;

import android.actionsheet.demo.com.khoiron.actionsheetiosforandroid.ActionSheet;
import android.actionsheet.demo.com.khoiron.actionsheetiosforandroid.Interface.ActionSheetCallBack;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.singleevent.sdk.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewUpcomingChallengeAdapter extends RecyclerView.Adapter<RecyclerViewUpcomingChallengeAdapter.ViewHolder>{

    private static final String TAG = "RecyclerViewAdapter";


    private ArrayList<String> mDate = new ArrayList<>();
    private ArrayList<String> mchallenge_desc = new ArrayList<>();
    private ArrayList<String> mCreatedBy = new ArrayList<>();




    private Context mContext;

    public RecyclerViewUpcomingChallengeAdapter(ArrayList<String> mDate, ArrayList<String> mchallenge_desc, ArrayList<String> mCreatedBy, Context mContext) {
        this.mDate = mDate;
        this.mchallenge_desc = mchallenge_desc;
        this.mCreatedBy = mCreatedBy;
        this.mContext = mContext;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }


    @Override
    public RecyclerViewUpcomingChallengeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout_upcomingchallenge, parent, false);
        RecyclerViewUpcomingChallengeAdapter.ViewHolder holder = new RecyclerViewUpcomingChallengeAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.txtStartDate.setText(mDate.get(position));
        holder.txtDesc.setText(mchallenge_desc.get(position));
        holder.txtCreatedBy.setText(mCreatedBy.get(position));


    }

    @Override
    public int getItemCount() {
        return mCreatedBy.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder{


        TextView txtStartDate,txtDesc,txtCreatedBy;
        RelativeLayout parentLayout;
        Button btn;
        private CharSequence[] mAlertItems;
        private boolean[] mSelectedItems;

        public ViewHolder(View itemView) {
            super(itemView);
            txtStartDate = itemView.findViewById(R.id.txt_upcoming_Challenge_startDate);
            txtDesc = itemView.findViewById(R.id.txt_upcoming_challenge_Desc);
            txtCreatedBy = itemView.findViewById(R.id.txt_upcoming_Challenge_createdby);
            parentLayout = itemView.findViewById(R.id.parentRelative_upcoming);
            btn = itemView.findViewById(R.id.btn_material);

            ArrayList<String> data = new ArrayList<>();

            data.add("View Challenge Details");
            data.add("Leave Challenge");
//            data.add("Move");
//            data.add("Duplicate");

            btn.setOnClickListener(new View.OnClickListener() {
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
