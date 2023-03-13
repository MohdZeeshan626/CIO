package com.singleevent.sdk.View.RightActivity.admin.checkin.beacon;

import android.content.Context;
import android.graphics.Bitmap;

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
import com.singleevent.sdk.Custom_View.Letter.LetterTileProvider;
import com.singleevent.sdk.model.AppDetails;
import com.singleevent.sdk.R;
import com.singleevent.sdk.View.RightActivity.admin.model.EventUser;

import java.util.List;

/**
 * Created by Lenovo on 06-12-2018.
 */

public class BeaconCheckinAdapter extends RecyclerView.Adapter<BeaconCheckinAdapter.ViewHolder> {

    private static Context context;
    private List<EventUser> eventUserList;
    AppDetails appDetails;
    static int agenda_id;
    static LetterTileProvider tileProvider;
    EventUser user;
    OnItemUserClick onItemUserClick;

    public BeaconCheckinAdapter(Context context, List<EventUser> userlist, int agenda_id, OnItemUserClick onItemUserClick) {
        //constructor
        this.context = context;
        this.eventUserList = userlist;
        this.agenda_id = agenda_id;
        this.onItemUserClick = onItemUserClick;
    }

    @NonNull
    @Override
    public BeaconCheckinAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.beaconcheckin_user_row, parent, false);

        BeaconCheckinAdapter.ViewHolder viewHolder = new BeaconCheckinAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final BeaconCheckinAdapter.ViewHolder holder, int position) {

        tileProvider = new LetterTileProvider(context);
        user = eventUserList.get(position);


        holder.bp_user_category.setText(user.getCompany());
        holder.user_name_tv.setText(user.getFirst_name() + " " + user.getLast_name());


        DisplayMetrics displayMetrics;
        final float dpWidth;
        displayMetrics = context.getResources().getDisplayMetrics();
        dpWidth = displayMetrics.widthPixels * 0.12F;
        ConstraintLayout.LayoutParams imgParams = (ConstraintLayout.LayoutParams) holder.user_iv.getLayoutParams();
        imgParams.width = (int) dpWidth;
        imgParams.height = (int) dpWidth;
        //setting the profile picture
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
        }/* else {
            Bitmap letterTile = tileProvider.getLetterTile(user.getFirst_name().trim(), "key", (int) dpWidth, (int) dpWidth, user.getColor());
            holder.user_iv.setImageDrawable(new Roundeddrawable(letterTile));
        }*/

        //setting the checkin status
        if (agenda_id == 0) {
            if (user.getCheckin_status().equalsIgnoreCase("reached"))
                holder.checkin_status.setVisibility(View.VISIBLE);
            else
                holder.checkin_status.setVisibility(View.GONE);
        } else {
            int flag = 0;
            int[] agendas = user.getAgenda_id();
            for (int agendaNum = 0; agendaNum < user.getAgenda_id().length; agendaNum++) {
                if (agendas[agendaNum] == agenda_id) {
                    holder.checkin_status.setVisibility(View.VISIBLE);
                    flag = 1;
                    break;
                }
            }
            if (flag == 0) {
                holder.checkin_status.setVisibility(View.GONE);
            }
        }

        //selecting the user
        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onItemUserClick.onUserItemClick(user);
            }
        });
    }

    @Override
    public int getItemCount() {
        return eventUserList.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView user_iv;
        TextView user_name_tv, bp_user_category;
        ConstraintLayout constraintLayout;
        LinearLayout checkin_status;

        public ViewHolder(View itemView) {
            super(itemView);
            //setting the resources
            user_iv = (ImageView) itemView.findViewById(R.id.bc_ul_user_image);
            user_name_tv = (TextView) itemView.findViewById(R.id.bc_ul_user_name);
            bp_user_category = (TextView) itemView.findViewById(R.id.bc_ul_user_category);
            constraintLayout = (ConstraintLayout) itemView.findViewById(R.id.bc_constraint_layout);
            checkin_status = (LinearLayout) itemView.findViewById(R.id.bc_checkedin_status);
        }


    }

    public static interface OnItemUserClick {
        //interface for selected users
        void onUserItemClick(EventUser eventUser);
    }

}
