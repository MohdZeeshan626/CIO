package com.webmobi.gecmedia.Views.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.webmobi.gecmedia.CustomViews.TxtVCustomFonts;
import com.webmobi.gecmedia.Models.User;
import com.webmobi.gecmedia.R;

import java.util.ArrayList;

/**
 * Created by Admin on 5/11/2017.
 */

public class UserAdapter extends BaseAdapter {

    Context mContext;
    ArrayList<User> users = new ArrayList<>();
    float dpwidth;


    public UserAdapter(Context mContext, ArrayList<User> users, float dpwidth) {
        this.mContext = mContext;
        this.users = users;
        this.dpwidth = dpwidth;
    }


    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        MyViewHolder holder = null;

        if (view == null) {
            LayoutInflater vi = (LayoutInflater) mContext.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            view = vi.inflate(R.layout.act_users, null);
            holder = new MyViewHolder();
            holder.name = (TxtVCustomFonts) view.findViewById(R.id.name);
            holder.designation = (TxtVCustomFonts) view.findViewById(R.id.designation);
            holder.profilepic = (ImageView) view.findViewById(R.id.profilepic);
            view.setTag(holder);
        } else {
            holder = (MyViewHolder) view.getTag();
        }

        User user = users.get(i);
        holder.name.setText(user.getFirst_name() + " " + user.getLast_name());
        holder.designation.setText(user.getDesignation());

        final MyViewHolder finalHolder = holder;
        Glide.with(mContext.getApplicationContext())

                .load((user.getProfile_pic().equalsIgnoreCase("")) ? R.drawable.default_user : user.getProfile_pic())
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.default_user)
                .error(R.drawable.default_user)
                .into(new BitmapImageViewTarget(finalHolder.profilepic) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(mContext.getResources(), Bitmap.createScaledBitmap(resource, (int) dpwidth, (int) dpwidth, false));
                        drawable.setCircular(true);
                        finalHolder.profilepic.setImageDrawable(drawable);
                    }
                });


        return view;
    }

    private class MyViewHolder {
        public TxtVCustomFonts name, designation;
        public ImageView profilepic;
    }
}
