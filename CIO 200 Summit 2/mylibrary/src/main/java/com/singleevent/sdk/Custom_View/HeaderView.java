package com.singleevent.sdk.Custom_View;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.singleevent.sdk.R;


/**
 * Created by Admin on 6/20/2017.
 */

public class HeaderView extends LinearLayout {

    private int mIndex;

    public HeaderView(Context context) {
        super(context);
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        layoutInflater.inflate(R.layout.headerview, null);
    }
    public HeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public HeaderView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    public int getIndex() {
        return mIndex;
    }

    public void setTitle(String Name,Context context) {
        TextView title = (TextView)findViewById(R.id.header_view_title);
        title.setTypeface(Util.regulartypeface(context));
        title.setText(Name);
    }

    public void setSubitle(String des,Context context) {
        TextView subtitle = (TextView)findViewById(R.id.header_view_sub_title);
        subtitle.setTypeface(Util.regulartypeface(context));
        subtitle.setText(des);
    }


    public void hideOrSetText(TextView tv, String text) {
        if (text == null || text.equals(""))
            tv.setVisibility(GONE);
        else
            tv.setVisibility(VISIBLE);
    }


}
