package com.singleevent.sdk.View.RightActivity.admin.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.makeramen.roundedimageview.RoundedImageView;
import com.singleevent.sdk.Custom_View.Letter.LetterTileProvider;
import com.singleevent.sdk.Custom_View.Util;
import com.singleevent.sdk.View.LeftActivity.Attendee_Profile;
import com.singleevent.sdk.model.AppDetails;
import com.singleevent.sdk.R;
import com.singleevent.sdk.View.RightActivity.admin.model.EventUser;
import com.singleevent.sdk.utils.DrawableUtil;

import java.util.List;

import io.paperdb.Paper;

/**
 * Created by User on 9/28/2016.
 */

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder> {
    private static Context context1;
    private List<EventUser> eventUserList;
    AppDetails appDetails;
    static int agenda_id;
    static Bitmap letterTile;



    // Provide a suitable constructor (depends on the kind of dataset)
    public UserListAdapter(Context context, List<EventUser> myDataset) {
        eventUserList = myDataset;
        UserListAdapter.context1 = context;



    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public UserListAdapter(Context context, List<EventUser> myDataset, int agenda_id) {
        eventUserList = myDataset;
        UserListAdapter.context1 = context;
        UserListAdapter.agenda_id = agenda_id;


    }

    //after search update list
    public void updateList(List<EventUser> list) {

        eventUserList = list;
        notifyDataSetChanged();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent,
                                         int viewType) {
        // create a new view
        View v = LayoutInflater.from(context1)
                .inflate(R.layout.user_adapter, parent, false);

        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        final EventUser user = eventUserList.get(position);

        // - replace the contents of the view with that element
        holder.setItem(user);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return eventUserList.size();
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        Context context;
        EventUser user;
        TextView tvName, tvCheckin, tvCheckedIn;
        TextView tvDesignation;
        RoundedImageView profileIV;
        ImageView checkedImg;
        AppDetails appDetails;
        LetterTileProvider tileProvider = new LetterTileProvider(context1);
        static OnCardClickListner onCardClickListner;


        ViewHolder(View v) {
            super(v);
            appDetails = Paper.book().read("Appdetails");
            tvName = (TextView) v.findViewById(R.id.tv_name);
            tvCheckin = (TextView) v.findViewById(R.id.tvCheckin);
            tvCheckedIn = (TextView) v.findViewById(R.id.tvCheckedIn);
            tvDesignation = (TextView) v.findViewById(R.id.tv_designation);
            profileIV = (RoundedImageView) v.findViewById(R.id.iv_profile);
            checkedImg = (ImageView) v.findViewById(R.id.iv_checked);


            v.setOnClickListener(this);
            tvCheckin.setOnClickListener(this);
//            profileIV.setOnLongClickListener(this);
        }

        void setItem(EventUser user) {
            this.user = user;
            tvName.setText(user.getFirst_name());
            if (user.getDesignation() != null) {
                tvDesignation.setText(user.getDesignation());
                tvDesignation.setVisibility(user.getDesignation().trim().isEmpty() ? View.GONE : View.VISIBLE);
            }

            tvCheckin.setBackground(Util.setdrawable(context1, R.drawable.act_button_border, Color.parseColor(appDetails.getTheme_color())));


            //login to check event checkin status and agenda checkin status
            if (agenda_id == 0) {
                tvCheckedIn.setVisibility(user.getCheckin_status().equalsIgnoreCase("reached") ? View.VISIBLE : View.GONE);
                tvCheckin.setVisibility(user.getCheckin_status().equalsIgnoreCase("reached") ? View.GONE : View.VISIBLE);

            } else {
                int flag = 0;
                int[] agendas = user.getAgenda_id();
                for (int agendaNum = 0; agendaNum < user.getAgenda_id().length; agendaNum++) {
                    if (agendas[agendaNum] == agenda_id) {
                        tvCheckedIn.setVisibility(View.VISIBLE);
                        tvCheckin.setVisibility(View.GONE);
                        flag = 1;
                        break;
                    }
                }
                if (flag == 0) {
                    tvCheckedIn.setVisibility(View.GONE);
                    tvCheckin.setVisibility(View.VISIBLE);

                }

            }


            DisplayMetrics displayMetrics;
            final float dpWidth;
            displayMetrics = context1.getResources().getDisplayMetrics();
            dpWidth = displayMetrics.widthPixels * 0.15F;
            RelativeLayout.LayoutParams imgParams = (RelativeLayout.LayoutParams) profileIV.getLayoutParams();
            imgParams.width = (int) dpWidth;
            imgParams.height = (int) dpWidth;

            if (!user.getProfile_pic().equalsIgnoreCase("") && user.getProfile_pic()!=null) {

                    Glide.with(context1.getApplicationContext())

                            .load(user.getProfile_pic())
                            .asBitmap()
                            .placeholder(R.drawable.round_user)
                            .error(R.drawable.round_user)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(profileIV);
                profileIV.setCornerRadius(12,12,12,12);


                    /*new BitmapImageViewTarget(profileIV) {
                                @Override
                                protected void setResource(Bitmap resource) {
                                    RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(context1.getResources(),
                                            Bitmap.createScaledBitmap(resource, (int) dpWidth, (int) dpWidth, false));
                                    drawable.setCircular(true);
                                    profileIV.setImageDrawable(drawable);
                                }
                            });*/
            }
            else{
                int n=(DrawableUtil.getColor(context1, user.getFirst_name()));
                    letterTile = tileProvider.getLetterTile(user.getFirst_name(), "key", (int) dpWidth,
                            (int) dpWidth, n);
                profileIV.setImageBitmap((letterTile));
                profileIV.setCornerRadius(12,12,12,12);


            }
        }

        @Override
        public void onClick(View v) {
            System.out.println("Item clicked pos " + getAdapterPosition());

            int position = getAdapterPosition();
            if (v.getId() == tvCheckin.getId()) {
                onCardClickListner.OnItemClick(v, user, position);
            }


        }

        @Override
        public boolean onLongClick(View v) {
            int position = getAdapterPosition();
            onCardClickListner.OnItemLongClicked(v, user, position);
            return true;
        }
    }

    public void setOnCardClickListner(OnCardClickListner onCardClickListner) {
        ViewHolder.onCardClickListner = onCardClickListner;
    }

    public interface OnCardClickListner {
        void OnItemLongClicked(View view, EventUser user, int position);

        void OnItemClick(View view, EventUser user, int position);
    }
}
