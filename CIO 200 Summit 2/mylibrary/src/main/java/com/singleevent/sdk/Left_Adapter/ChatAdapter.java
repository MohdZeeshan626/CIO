package com.singleevent.sdk.Left_Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.makeramen.roundedimageview.RoundedImageView;
import com.singleevent.sdk.Custom_View.Letter.LetterTileProvider;
import com.singleevent.sdk.Custom_View.Letter.Roundeddrawable;
import com.singleevent.sdk.Custom_View.Util;
import com.singleevent.sdk.model.LocalArraylist.ChatMSG;
import com.singleevent.sdk.R;
import com.singleevent.sdk.View.LeftActivity.MessageView;


import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Admin on 6/21/2017.
 */

public class ChatAdapter extends BaseAdapter {

    Context context;
    ArrayList<ChatMSG> messages = new ArrayList<>();
    float dpWidth, badgewidth;
    LetterTileProvider tileProvider;
    Bitmap letterTile;
    HashMap<String, JSONObject> newmessages = new HashMap<>();

    public ChatAdapter(Context context, ArrayList<ChatMSG> messages, float dpWidth, float badgewidth,
                       HashMap<String, JSONObject> newmessages) {
        this.context = context;
        this.messages = messages;
        this.dpWidth = dpWidth;
        this.badgewidth = badgewidth;
        tileProvider = new LetterTileProvider(context);
        this.newmessages = newmessages;
    }

    @Override
    public int getCount() {
        return messages.size();

    }

    @Override
    public Object getItem(int position) {
        return messages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        ViewHolder holder = null;
        if (view == null) {

            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = vi.inflate(R.layout.s_userlist_attendee, null);
            holder = new ViewHolder();
            holder.name = (TextView) view.findViewById(R.id.book_title);
            holder.lastmsg = (TextView) view.findViewById(R.id.author_name);
            holder.badge = (TextView) view.findViewById(R.id.counter);
            holder.time = (TextView) view.findViewById(R.id.time);
            holder.profilepic = (RoundedImageView) view.findViewById(R.id.profilepic);

            // setting badge count
            RelativeLayout.LayoutParams badgeParams = (RelativeLayout.LayoutParams) holder.badge.getLayoutParams();
            badgeParams.width = (int) badgewidth;
            badgeParams.height = (int) badgewidth;
            holder.badge.setLayoutParams(badgeParams);

            // setting image width
            RelativeLayout.LayoutParams imgParams = (RelativeLayout.LayoutParams) holder.profilepic.getLayoutParams();
            imgParams.width = (int) dpWidth;
            imgParams.height = (int) dpWidth;
            holder.profilepic.setLayoutParams(imgParams);


            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        ChatMSG chatusers = messages.get(position);
        holder.time.setTypeface(Util.lighttypeface(context));
        holder.lastmsg.setTypeface(Util.lighttypeface(context));
        holder.name.setTypeface(Util.boldtypeface(context));
        if(chatusers.getSender_id()!=null)
        holder.name.setText(chatusers.getSender_name());
        else{
            holder.name.setText(chatusers.getRecipient_name());
        }
        if(chatusers.getMessage()!=null)
        holder.lastmsg.setText(chatusers.getMessage());
        else{
            holder.lastmsg.setText(chatusers.getMessage_body());
        }
        holder.time.setText(Util.calheader(Long.parseLong(chatusers.getCreate_date())));

        if (chatusers.getProfile_pic().equalsIgnoreCase("")) {

            try {
                if(chatusers.getName()!=null){
                letterTile = tileProvider.getLetterTile(chatusers.getName(), "key", (int) dpWidth, (int) dpWidth, chatusers.getColor());
                holder.profilepic.setImageBitmap((letterTile));
                    holder.profilepic.setCornerRadius(8, 8, 8, 8);
                }

                else{
                    letterTile = tileProvider.getLetterTile(chatusers.getSender_name(), "key", (int) dpWidth, (int) dpWidth, chatusers.getColor());
                    holder.profilepic.setImageBitmap((letterTile));
                    holder.profilepic.setCornerRadius(8, 8, 8, 8);
                }
            }catch (Exception e)
            {

            }

        } else {

            final ViewHolder finalHolder = holder;
            Glide.with(context.getApplicationContext())

                    .load(chatusers.getProfile_pic())
                    .asBitmap()
                    .placeholder(R.drawable.round_user)
                    .error(R.drawable.round_user)
                    .into(holder.profilepic);
            holder.profilepic.setCornerRadius(8, 8, 8, 8);
            /*new BitmapImageViewTarget(finalHolder.profilepic) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(context.getResources(),
                                    Bitmap.createScaledBitmap(resource, (int) dpWidth, (int) dpWidth, false));
                            drawable.setCircular(true);
                            finalHolder.profilepic.setImageDrawable(drawable);
                        }
                    });*/
        }

        if (chatusers.getBadgecount() > 0) {
            holder.badge.setText("" + chatusers.getBadgecount());
            holder.badge.setVisibility(View.VISIBLE);
        } else
            holder.badge.setVisibility(View.GONE);

        holder.badge.setTag(position);

        final ViewHolder finalHolder1 = holder;


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = (int) finalHolder1.badge.getTag();
                messages.get(pos).setBadgecount(0);
                finalHolder1.badge.setVisibility(View.GONE);
                Bundle args = new Bundle();
                args.putSerializable("UserItem", messages.get(pos));
                Intent intent = new Intent(context, MessageView.class);
                intent.putExtras(args);
                context.startActivity(intent);

            }
        });


        return view;
    }

    private class ViewHolder {
        TextView name, lastmsg, badge, time;
        RoundedImageView profilepic;
    }
}
