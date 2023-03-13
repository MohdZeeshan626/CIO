package com.singleevent.sdk.health.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;


import com.singleevent.sdk.R;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewParticipantsAdapter extends RecyclerView.Adapter<RecyclerViewParticipantsAdapter.ViewHolder>{

    private static final String TAG = "RecyclerViewAdapter";


    private ArrayList<String> mPartciImage = new ArrayList<>();
    private ArrayList<String> mPartciName = new ArrayList<>();
    private Context mContext;

    public RecyclerViewParticipantsAdapter(ArrayList<String> mPartciImage, ArrayList<String> mPartciName, Context mContext) {
        this.mPartciImage = mPartciImage;
        this.mPartciName = mPartciName;
        this.mContext = mContext;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }


    @Override
    public RecyclerViewParticipantsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout_join_participants, parent, false);
        RecyclerViewParticipantsAdapter.ViewHolder holder = new RecyclerViewParticipantsAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        Glide.with(mContext)
                .load(mPartciImage.get(position))
              //  .circleCrop()
                .into(holder.participantImage);
        holder.txtparticipantname.setText(mPartciName.get(position));



    }

    @Override
    public int getItemCount() {
        return mPartciName.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder{


        ImageView participantImage;
        TextView txtparticipantname;
        RelativeLayout parentLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            participantImage = itemView.findViewById(R.id.participant_image);
            txtparticipantname = itemView.findViewById(R.id.txt_participant_name);
            parentLayout = itemView.findViewById(R.id.parent_layout_joinPartcipants);


        }
    }
}
