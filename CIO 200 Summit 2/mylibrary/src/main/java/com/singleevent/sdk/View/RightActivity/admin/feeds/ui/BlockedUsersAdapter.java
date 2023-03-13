package com.singleevent.sdk.View.RightActivity.admin.feeds.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.singleevent.sdk.Custom_View.Letter.LetterTileProvider;
import com.singleevent.sdk.Custom_View.Letter.Roundeddrawable;
import com.singleevent.sdk.R;
import com.singleevent.sdk.View.RightActivity.admin.adminSurvey.UsersModel;


import java.util.List;

public class BlockedUsersAdapter extends RecyclerView.Adapter<BlockedUsersAdapter.ViewHolder> {

    Context context;
    //    ArrayList<Feed> feeds = new ArrayList<>();
    List<UsersModel> blockedusers;
    String theme_color;
    static LetterTileProvider tileProvider;
    UnBlockUser unBlockUserListener;

    public BlockedUsersAdapter(Context context, List<UsersModel> blockedusers, String theme_color, UnBlockUser unBlockUserListener) {
        this.context = context;
        this.blockedusers = blockedusers;
        this.theme_color = theme_color;

        this.unBlockUserListener = unBlockUserListener;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.blocked_user_view, parent,
                false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final UsersModel usersModel = blockedusers.get(position);
        holder.name.setText(usersModel.getFirst_name() + " " + usersModel.getLast_name());
        holder.unblock_btn.setBackgroundColor(Color.parseColor(theme_color));
        tileProvider = new LetterTileProvider(context);

        DisplayMetrics displayMetrics;
        final float dpWidth;
        displayMetrics = context.getResources().getDisplayMetrics();
        dpWidth = displayMetrics.widthPixels * 0.12F;
        RelativeLayout.LayoutParams imgParams = (RelativeLayout.LayoutParams) holder.profilePic.getLayoutParams();
        imgParams.width = (int) dpWidth;
        imgParams.height = (int) dpWidth;

        if (null != usersModel.getProfile_pic() && !usersModel.getProfile_pic().isEmpty()) {

            if (URLUtil.isValidUrl(usersModel.getProfile_pic()))
                Glide.with(context.getApplicationContext())

                        .load(usersModel.getProfile_pic())
                        .asBitmap()
                        .placeholder(R.drawable.round_user)
                        .error(R.drawable.round_user)
                        .into(new BitmapImageViewTarget(holder.profilePic) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(context.getResources(),
                                        Bitmap.createScaledBitmap(resource, (int) dpWidth, (int) dpWidth, false));
                                drawable.setCircular(true);
                                holder.profilePic.setImageDrawable(drawable);
                            }
                        });
        } else {
            Bitmap letterTile = tileProvider.getLetterTile(usersModel.getFirst_name().trim(), "key", (int) dpWidth, (int) dpWidth, usersModel.getColor());
            holder.profilePic.setImageDrawable(new Roundeddrawable(letterTile));
        }

        holder.unblock_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unBlockUserListener.unblockuser(usersModel.getUserid());
            }
        });
    }

    @Override
    public int getItemCount() {
        return blockedusers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView unblock_btn, name;
        ImageView profilePic;

        public ViewHolder(View itemView) {
            super(itemView);

            unblock_btn = (TextView) itemView.findViewById(R.id.unblock_btn);
            name = (TextView) itemView.findViewById(R.id.tv_name);
            profilePic = (ImageView) itemView.findViewById(R.id.profilepic);


        }

    }

    public interface UnBlockUser {
        public void unblockuser(String userid);
    }
}
