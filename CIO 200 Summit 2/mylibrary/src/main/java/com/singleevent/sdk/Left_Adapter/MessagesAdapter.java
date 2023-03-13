package com.singleevent.sdk.Left_Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.singleevent.sdk.Custom_View.Letter.LetterTileProvider;
import com.singleevent.sdk.View.LeftActivity.Attendee_Profile;
import com.singleevent.sdk.model.LocalArraylist.ChatMessage;
import com.singleevent.sdk.R;
import com.singleevent.sdk.utils.DrawableUtil;

import java.util.ArrayList;

/**
 * Created by Admin on 9/23/2016.
 */
public class MessagesAdapter extends BaseAdapter {

    Context context;
    ArrayList<ChatMessage> chatmessage = new ArrayList<>();
    public static final int DIRECTION_INCOMING = 0;
    public static final int DIRECTION_OUTGOING = 1;
    public static final int Separator = 2;
    int color;
    Bitmap letterTile;
    LetterTileProvider tileProvider;
    private float dpWidth;

    public MessagesAdapter(Context context, int color) {
        this.context = context;
        this.color = color;
    }

    public void addMessage(ChatMessage msg) {
        chatmessage.add(msg);
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return chatmessage.size();
    }

    @Override
    public Object getItem(int position) {
        return chatmessage.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public int getItemViewType(int i) {
        if (chatmessage.get(i).isSection())
            return 2;
        else if (chatmessage.get(i).isIncomingMessage())
            return 0;
        else
            return 1;

//        return chatmessage.get(i).isIncomingMessage() ? 0 : 1;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int direction = getItemViewType(position);
        tileProvider = new LetterTileProvider(context);
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        dpWidth = displayMetrics.widthPixels * 0.20F;
        ChatMessage msg = chatmessage.get(position);
        LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            int res = 0;
            if (direction == DIRECTION_INCOMING) {
                res = R.layout.message_left_;
            } else if (direction == DIRECTION_OUTGOING) {
                res = R.layout.message_right;
            } else if (direction == Separator)
                res = R.layout.separator;
            convertView = vi.inflate(res, parent, false);
        }
        TextView txtMessage = (TextView) convertView.findViewById(R.id.txtMessage);
        TextView txtdate = (TextView) convertView.findViewById(R.id.txtDate);
        RoundedImageView logoIV = (RoundedImageView) convertView.findViewById(R.id.prof_iv);

        if (direction != Separator) {
            RelativeLayout bodycolor = (RelativeLayout) convertView.findViewById(R.id.txtbody);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
               // bodycolor.setBackgroundTintList(direction == DIRECTION_INCOMING ? ColorStateList.valueOf(color) : ColorStateList.valueOf(Color.parseColor("#969697")));
            }

        }

        txtdate.setText(msg.getDate());
        txtMessage.setText(msg.getMessage());
        if(msg.getUsername()!=null){
            //logoIV.setBackground(DrawableUtil.genBackgroundDrawable(context, msg.getUsername()));
            int n=(DrawableUtil.getColor(context, msg.getUsername()));
            //logoIV.setImageResource(DrawableUtil.getDrawableForName(msg.getUsername()));
           // logoIV.setCornerRadius(8, 8, 8, 8);
            letterTile = tileProvider.getLetterTile(msg.getUsername(), "key", (int) dpWidth,
                    (int) dpWidth,n);
            logoIV.setImageBitmap((letterTile));
            logoIV.setCornerRadius(8, 8, 8, 8);
        }

        return convertView;
    }
}
