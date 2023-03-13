package com.singleevent.sdk.Left_Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.singleevent.sdk.Custom_View.Letter.LetterTileProvider;
import com.singleevent.sdk.model.User;
import com.singleevent.sdk.R;
import com.singleevent.sdk.agora.openvcall.model.GroupChat;
import com.singleevent.sdk.utils.DrawableUtil;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class GroupMessageAdapter extends BaseAdapter {

    Context context;
    ArrayList<GroupChat> chatmessage = new ArrayList<>();
    public static final int DIRECTION_INCOMING = 0;
    public static final int DIRECTION_OUTGOING = 1;
    public static final int Separator = 2;
    int color;
    List<User> userview;
    Bitmap letterTile;
    LetterTileProvider tileProvider;
    private float dpWidth;

    public GroupMessageAdapter(Context context, int color) {
        this.context = context;
        this.color = color;
    }

    public void addMessage(GroupChat msg) {
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
        GroupChat msg = chatmessage.get(position);
        LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            int res = 0;
            if (direction == DIRECTION_INCOMING) {
                res = R.layout.group_message_left;
            } else if (direction == DIRECTION_OUTGOING) {
                res = R.layout.group_message_left;
            } else if (direction == Separator)
                res = R.layout.group_message_left;
            convertView = vi.inflate(res, parent, false);
        }

        TextView txtMessage = (TextView) convertView.findViewById(R.id.txtMessage);
        TextView txtdate = (TextView) convertView.findViewById(R.id.txtDate);
        RoundedImageView logoIV = (RoundedImageView) convertView.findViewById(R.id.prof_iv);
        TextView uNmae=(TextView)convertView.findViewById(R.id.uNmae);

        if (direction != Separator) {
            RelativeLayout bodycolor = (RelativeLayout) convertView.findViewById(R.id.txtbody);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                // bodycolor.setBackgroundTintList(direction == DIRECTION_INCOMING ? ColorStateList.valueOf(color) : ColorStateList.valueOf(Color.parseColor("#969697")));
            }

        }

        txtdate.setText(msg.getCreate_date());
        txtMessage.setText(msg.getMessage_body());
        userview = new ArrayList<>();

        userview = Paper.book().read("ALLATEENDEE", new ArrayList<User>());

        if(msg.getFrom_UserName()!=null)
        {
            uNmae.setText(msg.getFrom_UserName());
        }

            if(msg.getProfile_pic()!=null&& !msg.getProfile_pic().equalsIgnoreCase(""))
            {
                Glide.with(context.getApplicationContext())
                        .load(msg.getProfile_pic())
                        .placeholder(R.drawable.round_user)
                        .error(R.drawable.round_user)
                        .into((logoIV) );
                logoIV.setCornerRadius(8, 8, 8, 8);

        }else {
            if (msg.getFrom_UserName() != null) {
               // logoIV.setBackground(DrawableUtil.genBackgroundDrawable(context, msg.getFrom_UserName()));
               // logoIV.setImageResource(DrawableUtil.getDrawableForName(msg.getFrom_UserName()));
                int n=(DrawableUtil.getColor(context, msg.getFrom_UserName()));
                //logoIV.setImageResource(DrawableUtil.getDrawableForName(msg.getUsername()));
                // logoIV.setCornerRadius(8, 8, 8, 8);
                letterTile = tileProvider.getLetterTile(msg.getFrom_UserName(), "key", (int) dpWidth,
                        (int) dpWidth,n);
                logoIV.setImageBitmap((letterTile));
                logoIV.setCornerRadius(8, 8, 8, 8);
            }
        }
        return convertView;
    }
}
