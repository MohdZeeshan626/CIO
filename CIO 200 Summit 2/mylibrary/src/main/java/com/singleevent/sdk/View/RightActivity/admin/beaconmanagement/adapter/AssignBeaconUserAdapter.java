package com.singleevent.sdk.View.RightActivity.admin.beaconmanagement.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
//import com.google.android.gms.vision.text.Line;
import com.singleevent.sdk.Custom_View.Letter.LetterTileProvider;
import com.singleevent.sdk.Custom_View.Letter.Roundeddrawable;
import com.singleevent.sdk.R;
import com.singleevent.sdk.View.RightActivity.admin.adminSurvey.UsersModel;
import com.singleevent.sdk.View.RightActivity.admin.beaconmanagement.BMUserDetails;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lenovo on 21-11-2018.
 */

public class AssignBeaconUserAdapter extends RecyclerView.Adapter<AssignBeaconUserAdapter.ViewHolder> {

    Context context;
    List<UsersModel> userlList = new ArrayList<>();
    String theme_color;
    static LetterTileProvider tileProvider;

    public AssignBeaconUserAdapter(Context context, List<UsersModel> userlist, String theme_color) {
        //constructor
        //assigning the values
        this.context = context;
        this.userlList = userlist;
        this.theme_color = theme_color;

    }

    public void updateList(List<UsersModel> list) {

        userlList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.assignbeacon_userlist_row, parent, false);

        AssignBeaconUserAdapter.ViewHolder viewHolder = new AssignBeaconUserAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        //setting the vaalues
        final UsersModel user = userlList.get(position);
        tileProvider = new LetterTileProvider(context);
        holder.bp_user_category.setText(userlList.get(position).getAdmin_flag());
        holder.user_name_tv.setText(userlList.get(position).getFirst_name() + " " + userlList.get(position).getLast_name());
        String beacon_id = userlList.get(position).getBeacon_id();
        //if beacon is assigned to the user
        //it will show the Beacon Activated Message otherwise it will hide
        if (!(beacon_id.equalsIgnoreCase(""))) {
            holder.beacon_status.setVisibility(View.VISIBLE);
        } else
            holder.beacon_status.setVisibility(View.GONE);
        DisplayMetrics displayMetrics;
        final float dpWidth;
        displayMetrics = context.getResources().getDisplayMetrics();
        dpWidth = displayMetrics.widthPixels * 0.12F;
        ConstraintLayout.LayoutParams imgParams = (ConstraintLayout.LayoutParams) holder.user_iv.getLayoutParams();
        imgParams.width = (int) dpWidth;
        imgParams.height = (int) dpWidth;

        //setting the profile pic
        if (null != user.getProfile_pic() && !user.getProfile_pic().isEmpty()) {

            if (URLUtil.isValidUrl(user.getProfile_pic()))
                Glide.with(context.getApplicationContext())


                        .load(user.getProfile_pic())
                        .asBitmap()
                        .placeholder(R.drawable.round_user)
                        .error(R.drawable.round_user)
                        .into(new BitmapImageViewTarget(holder.user_iv) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(context.getResources(),
                                        Bitmap.createScaledBitmap(resource, (int) dpWidth, (int) dpWidth, false));
                                drawable.setCircular(true);
                                holder.user_iv.setImageDrawable(drawable);
                            }
                        });
        } else {
            Bitmap letterTile = tileProvider.getLetterTile(user.getFirst_name().trim(), "key", (int) dpWidth, (int) dpWidth, user.getColor());
            holder.user_iv.setImageDrawable(new Roundeddrawable(letterTile));
        }
        //click listener for list items
        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle args = new Bundle();
                args.putSerializable(BMUserDetails.USER_DETAILS, user);
                Intent i = new Intent(context, BMUserDetails.class);
                i.putExtras(args);
                context.startActivity(i);
            }
        });
    }


    @Override
    public int getItemCount() {
        return userlList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView user_iv;
        TextView user_name_tv, bp_user_category;
        ConstraintLayout constraintLayout;
        LinearLayout beacon_status;

        public ViewHolder(View itemView) {
            super(itemView);
            //setting the resources
            user_iv = (ImageView) itemView.findViewById(R.id.mb_ul_user_image);
            user_name_tv = (TextView) itemView.findViewById(R.id.mb_ul_user_name);
            bp_user_category = (TextView) itemView.findViewById(R.id.mb_ul_user_category);
            constraintLayout = (ConstraintLayout) itemView.findViewById(R.id.mb_constraint_layout);
            beacon_status = (LinearLayout) itemView.findViewById(R.id.beacon_status);
        }
    }
}
