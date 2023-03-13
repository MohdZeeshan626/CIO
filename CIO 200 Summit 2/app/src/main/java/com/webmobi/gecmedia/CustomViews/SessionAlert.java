package com.webmobi.gecmedia.CustomViews;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.singleevent.sdk.Custom_View.Util;
import com.singleevent.sdk.R;
import com.singleevent.sdk.View.LeftActivity.Attendee;
import com.webmobi.gecmedia.MainActivity;


public class SessionAlert {

    Activity act;
    RelativeLayout rl;
    Button btnCancel, btnview;
    TextView tvTitle;
    TextView tvMessage;
    Dialog dialog;
    int newcolor, bordercolor;
    Bundle extras;

    public SessionAlert(int newcolor, int bordercolor, Bundle extras, Activity act) {

        this.extras = extras;
        this.act = act;
        this.newcolor = newcolor;
        this.bordercolor = bordercolor;

    }

    public void showDialog() {

        dialog = new Dialog(act, android.R.style.Theme_Translucent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.alert_dialog);
        rl = (RelativeLayout) dialog.findViewById(R.id.rl);
        rl.setBackground(Util.setshape(newcolor, bordercolor));
        btnCancel = (Button) dialog.findViewById(R.id.btncancel);
        btnview = (Button) dialog.findViewById(R.id.btnview);
        tvTitle = (TextView) dialog.findViewById(R.id.tvTitle);
        tvMessage = (TextView) dialog.findViewById(R.id.tvMessage);

        tvMessage.setVisibility(View.INVISIBLE);
        tvTitle.setVisibility(View.INVISIBLE);
        btnCancel.setVisibility(View.INVISIBLE);
        btnview.setVisibility(View.INVISIBLE);

        tvMessage.setText(extras.getString("keyMessage"));
        tvTitle.setText(extras.getString("keyAlert"));


        Animation scale_up = AnimationUtils.loadAnimation(act, R.anim.layout_zoom_open);

        scale_up.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                tvMessage.setVisibility(View.VISIBLE);
                tvTitle.setVisibility(View.VISIBLE);
                btnview.setVisibility(View.GONE);
                btnCancel.setVisibility(View.VISIBLE);


            }
        });


        rl.setAnimation(scale_up);

        btnCancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent i = new Intent(act, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.putExtras(extras);
                act.startActivity(i);


            }
        });

        btnview.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Intent i = new Intent(act, Attendee.class);
                i.setAction("com.view");
                i.putExtras(extras);
                act.startActivity(i);
            }
        });

        dialog.show();
    }
}