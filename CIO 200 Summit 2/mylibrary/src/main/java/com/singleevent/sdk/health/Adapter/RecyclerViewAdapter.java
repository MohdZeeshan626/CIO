package com.singleevent.sdk.health.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;


import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.singleevent.sdk.R;

import java.util.ArrayList;

import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewAdapter  extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    private static final String TAG = "RecyclerViewAdapter";


    private ArrayList<String> mImageNames = new ArrayList<>();
    private ArrayList<String> mImages = new ArrayList<>();
    private ArrayList<String> mTotalkm = new ArrayList<>();
    private ArrayList<Integer> mPosition = new ArrayList<>();
    private Context mContext;

    public RecyclerViewAdapter(ArrayList<String> mImageNames, ArrayList<String> mImages, ArrayList<String> mTotalkm, ArrayList<Integer> mPosition, Context mContext) {
        this.mImageNames = mImageNames;
        this.mImages = mImages;
        this.mTotalkm = mTotalkm;
        this.mPosition = mPosition;
        this.mContext = mContext;
    }
    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_layout, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        Glide.with(mContext)
                .load(mImages.get(position))
               // .apply(RequestOptions.bitmapTransform(new RoundedCorners(14)))
                .into(holder.image);
         /*.into(new BitmapImageViewTarget(profile_image) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(getResources(), Bitmap.createScaledBitmap(resource, (int) cellWidth, (int) cellWidth, false));
                drawable.setCircular(true);
                profile_image.setImageDrawable(drawable);
            }
        });*/

        holder.imageName.setText(mImageNames.get(position));
        holder.totalkm.setText(mTotalkm.get(position));
        Glide.with(mContext).load(mPosition.get(position)).into(holder.imageView);
            }

    @Override
    public int getItemCount() {
        return mImageNames.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{

        CircleImageView image;
        ImageView imageView;
        TextView imageName,totalkm;
        RelativeLayout parentLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.leaderboard_image);
            imageName = itemView.findViewById(R.id.leaderboard_txtname);
            imageView = itemView.findViewById(R.id.leaderboard_pos);
            totalkm = itemView.findViewById(R.id.leaderboard_txtkm);
            parentLayout = itemView.findViewById(R.id.parent_layout);



        }
    }
}
