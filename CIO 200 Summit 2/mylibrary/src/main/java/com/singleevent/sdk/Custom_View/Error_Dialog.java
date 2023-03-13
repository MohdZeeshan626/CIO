package com.singleevent.sdk.Custom_View;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.singleevent.sdk.R;


/**
 * Created by webmodi on 6/2/2016.
 */
public class Error_Dialog {

    View view;

    public static void show(String notifText, Activity context) {

        if (context == null)
            return;
        LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        View view = li.inflate(R.layout.act_errordialog, null);


        final Dialog mBottomSheetDialog = new Dialog(context, R.style.MaterialDialogSheet);
        mBottomSheetDialog.setContentView(view);
        mBottomSheetDialog.setCancelable(true);
        mBottomSheetDialog.setOwnerActivity(context);
        mBottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);
        if (!((Activity) context).isFinishing())
            mBottomSheetDialog.show();

        TextView txttitle = (TextView) view.findViewById(R.id.title);
        txttitle.setTextColor(Color.parseColor("#000000"));
        txttitle.setText(notifText);

        new CountDownTimer(2000, 1000) {

            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                Activity activity = mBottomSheetDialog.getOwnerActivity();
                if (activity != null && !activity.isFinishing()) {
                    mBottomSheetDialog.dismiss();
                }
            }
        }.start();

    }


}
