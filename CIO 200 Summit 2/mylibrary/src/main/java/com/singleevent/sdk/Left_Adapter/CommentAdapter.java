package com.singleevent.sdk.Left_Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.singleevent.sdk.Custom_View.Letter.LetterTileProvider;
import com.singleevent.sdk.Custom_View.Letter.Roundeddrawable;
import com.singleevent.sdk.Custom_View.Util;
import com.singleevent.sdk.model.Comments;
import com.singleevent.sdk.R;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Fujitsu on 30-08-2017.
 */

public class CommentAdapter extends BaseAdapter {

    ArrayList<Comments> com_list = new ArrayList<>();
    Context context;
    LetterTileProvider tileProvider;
    float dpWidth, pwidth;
    Bitmap letterTile;
    Random r;

    public CommentAdapter(ArrayList<Comments> com_list, Context context, float dpWidth) {
        this.com_list = com_list;
        this.context = context;
        this.dpWidth = dpWidth;
        tileProvider = new LetterTileProvider(context);
        this.dpWidth = dpWidth;
        r = new Random();
    }

    @Override
    public int getCount() {
        return com_list.size();
    }

    @Override
    public Object getItem(int position) {
        return com_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        if (convertView == null) {

            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.comments_view, null);
            holder = new ViewHolder();
            holder.username = (TextView) convertView.findViewById(R.id.username);
            holder.datetime = (TextView) convertView.findViewById(R.id.datetime);
            holder.postmsg = (TextView) convertView.findViewById(R.id.postmsg);
            holder.profilepic = (ImageView) convertView.findViewById(R.id.profilepic);


            // setting image width
            pwidth = dpWidth * 0.12F;
            ConstraintLayout.LayoutParams imgParams = (ConstraintLayout.LayoutParams) holder.profilepic.getLayoutParams();
            imgParams.width = (int) pwidth;
            imgParams.height = (int) pwidth;
            holder.profilepic.setLayoutParams(imgParams);


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Comments comment = com_list.get(position);

        holder.username.setTypeface(Util.boldtypeface(context));
        holder.datetime.setTypeface(Util.lighttypeface(context));
        holder.postmsg.setTypeface(Util.regulartypeface(context));

        holder.username.setText(comment.getName());
        holder.datetime.setText(Util.calheader(comment.getCommentedon()));
        holder.postmsg.setText(comment.getCommenttxt());


        // Loading the Profile_Pic

        if (comment.getProfilepic().equalsIgnoreCase("")) {
            int color = Color.argb(255, r.nextInt(256), r.nextInt(256), r.nextInt(256));
            letterTile = tileProvider.getLetterTile(comment.getName(), "key", (int) pwidth, (int) pwidth, color);
            holder.profilepic.setImageDrawable(new Roundeddrawable(letterTile));

        } else {

            final ViewHolder finalHolder = holder;
            Glide.with(context.getApplicationContext())

                    .load(comment.getProfilepic())
                    .asBitmap()
                    .placeholder(R.drawable.default_user)
                    .error(R.drawable.default_user)
                    .into(new BitmapImageViewTarget(finalHolder.profilepic) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(context.getResources(),
                                    Bitmap.createScaledBitmap(resource, (int) pwidth, (int) pwidth, false));
                            drawable.setCircular(true);
                            finalHolder.profilepic.setImageDrawable(drawable);
                        }
                    });
        }

        return convertView;
    }

    private class ViewHolder {
        TextView username, datetime, postmsg;
        ImageView profilepic;
    }

}
