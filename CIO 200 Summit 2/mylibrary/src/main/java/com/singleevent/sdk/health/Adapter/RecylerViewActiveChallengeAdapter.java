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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecylerViewActiveChallengeAdapter extends RecyclerView.Adapter<RecylerViewActiveChallengeAdapter.ViewHolder> {

    private ArrayList<String> mPosition = new ArrayList<>();
    private ArrayList<String> mImage = new ArrayList<>();
    private ArrayList<String> mName = new ArrayList<>();
    private ArrayList<String> mTotal = new ArrayList<>();
    private Context mContext;

    public RecylerViewActiveChallengeAdapter(ArrayList<String> mPosition, ArrayList<String> mImage, ArrayList<String> mName, ArrayList<String> mTotal, Context mContext) {
        this.mPosition = mPosition;
        this.mImage = mImage;
        this.mName = mName;
        this.mTotal = mTotal;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout_activechallenge, parent, false);
        RecylerViewActiveChallengeAdapter.ViewHolder holder = new RecylerViewActiveChallengeAdapter.ViewHolder(view);
        return holder;


    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.txtPos.setText(mPosition.get(position));
        Glide.with(mContext)
                .load(mImage.get(position))
               // .apply(RequestOptions.bitmapTransform(new RoundedCorners(14)))
                .into(holder.imageViewPic);
        holder.txtName.setText(mName.get(position));
        holder.txtTotalSteps.setText(mTotal.get(position));

    }

    @Override
    public int getItemCount() {
        return mPosition.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtPos,txtName,txtTotalSteps;
        ImageView imageViewPic;
        RelativeLayout parentRelativeLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtPos =itemView.findViewById(R.id.txt_pos_active);
            imageViewPic =itemView.findViewById(R.id.active_image);
            txtName =itemView.findViewById(R.id.active_txtname);
            txtTotalSteps =itemView.findViewById(R.id.active_totalSteps_txt);
            parentRelativeLayout = itemView.findViewById(R.id.parentRelative_ActiveChallenge);

        }
    }
}
