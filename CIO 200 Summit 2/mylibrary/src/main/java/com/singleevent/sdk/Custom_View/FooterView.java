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

public class FooterView extends LinearLayout {

    private int mIndex;

    public FooterView(Context context) {
        super(context);
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        layoutInflater.inflate(R.layout.footerheaderview, null);
    }
    public FooterView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FooterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public FooterView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    public int getIndex() {
        return mIndex;
    }

    public void setTitle() {
        TextView title = (TextView)findViewById(R.id.header_view_title);
        title.setText("Followers");
    }

    public void setSubitle() {
        TextView subtitle = (TextView)findViewById(R.id.header_view_sub_title);
        subtitle.setText("435");
    }


}
