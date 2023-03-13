package com.singleevent.sdk.View.RightActivity.admin.bulkprinting.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.core.widget.CompoundButtonCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.makeramen.roundedimageview.RoundedImageView;
import com.singleevent.sdk.Custom_View.Letter.LetterTileProvider;
import com.singleevent.sdk.Custom_View.Letter.Roundeddrawable;
import com.singleevent.sdk.R;
import com.singleevent.sdk.View.RightActivity.admin.adminSurvey.UsersModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lenovo on 23-10-2018.
 */

public class BulkPrintingUserAdapter extends RecyclerView.Adapter<BulkPrintingUserAdapter.ViewHolder> {

    Context context;
    List<UsersModel> userlList;
    List<UsersModel> selectedUserList = new ArrayList<>();
    String theme_color;
    static LetterTileProvider tileProvider;
    boolean isSelectall;

    public BulkPrintingUserAdapter(Context context, List<UsersModel> userlist, String theme_color, boolean isSelectall) {
        //constructor
        //assigning the values
        this.context = context;
        this.userlList = userlist;
        this.theme_color = theme_color;
        this.isSelectall = isSelectall;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bulk_printing_row, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final UsersModel user = userlList.get(position);
        tileProvider = new LetterTileProvider(context);
        //setting the values
        holder.bp_user_category.setText(userlList.get(position).getAdmin_flag());
        holder.user_name_tv.setText(userlList.get(position).getFirst_name() + " " + userlList.get(position).getLast_name());
        if (isSelectall) {
            //if all users are selected
            holder.user_check.setChecked(true);
            if (selectedUserList.contains(userlList.get(position))) {
                selectedUserList.remove(userlList.get(position));
                selectedUserList.add(userlList.get(position));
            } else
                selectedUserList.add(userlList.get(position));

        } else {
            holder.user_check.setChecked(false);
            if (selectedUserList.contains(userlList.get(position)))
                selectedUserList.remove(userlList.get(position));

        }

        holder.user_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    if (selectedUserList.contains(userlList.get(position))) {
                        selectedUserList.remove(userlList.get(position));
                        selectedUserList.add(userlList.get(position));
                    } else
                        selectedUserList.add(userlList.get(position));
                } else {
                    if (selectedUserList.contains(userlList.get(position)))
                        selectedUserList.remove(userlList.get(position));
                }
            }
        });
        DisplayMetrics displayMetrics;
        final float dpWidth;
        displayMetrics = context.getResources().getDisplayMetrics();
        dpWidth = displayMetrics.widthPixels * 0.12F;
    //    RelativeLayout.LayoutParams imgParams = (RelativeLayout.LayoutParams) holder.user_iv.getLayoutParams();
      //  imgParams.width = (int) dpWidth;
      //  imgParams.height = (int) dpWidth;

        //setting the profile pic
        if (null != user.getProfile_pic() && !user.getProfile_pic().isEmpty()) {

            if (URLUtil.isValidUrl(user.getProfile_pic()))
                Glide.with(context.getApplicationContext())

                        .load(user.getProfile_pic())
                        .asBitmap()
                        .placeholder(R.drawable.round_user)
                        .error(R.drawable.round_user)
                        .into(holder.user_iv);
            holder.user_iv.setCornerRadius(12,12,12,12);

                                /*new BitmapImageViewTarget(holder.user_iv) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(context.getResources(),
                                        Bitmap.createScaledBitmap(resource, (int) dpWidth, (int) dpWidth, false));
                                drawable.setCircular(true);
                                holder.user_iv.setImageDrawable(drawable);
                            }
                        });*/
        } else {
            Bitmap letterTile = tileProvider.getLetterTile(user.getFirst_name().trim(), "key", (int) dpWidth, (int) dpWidth, user.getColor());
            holder.user_iv.setImageBitmap((letterTile));
            holder.user_iv.setCornerRadius(8, 8, 8, 8);
        }
    }

    public List<UsersModel> getSelectedUserList() {
        return selectedUserList;
    }

    @Override
    public int getItemCount() {
        return userlList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView user_iv;
        TextView user_name_tv, bp_user_category;
        CheckBox user_check;

        public ViewHolder(View itemView) {
            super(itemView);
            //setting the resources
            user_iv = (RoundedImageView) itemView.findViewById(R.id.bp_user_image);
            user_name_tv = (TextView) itemView.findViewById(R.id.bp_user_name);
            bp_user_category = (TextView) itemView.findViewById(R.id.bp_user_category);
            user_check = (CheckBox) itemView.findViewById(R.id.bp_user_check);

            int states[][] = {{android.R.attr.state_checked}, {}};
            int colors[] = {Color.parseColor(theme_color), Color.GRAY};
            CompoundButtonCompat.setButtonTintList(user_check, new ColorStateList(states, colors));

        }
    }
}
