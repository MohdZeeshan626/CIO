/*Disclaimer: IMPORTANT: This Colworx software is proprietary confidential property owned exclusively by Colworx Tech Inc. ("CTI") Without prior written consent and compensation, Colworx expressly forbids the use, installation, modification or redistribution of this CTI software.
 CTI forbids all parties, without a non-exclusive license, under CTI's copyrights in this original CTI software (the "CTI Software"), to use, reproduce, modify and redistribute the CTI Software, with or without modifications, in source and/or binary forms. In all cases, you must retain this notice and the following text and  Disclaimers in all such redistributions of the CTI Software.
 The CTI Software is provided by CTI on an "AS IS" basis. CTI MAKES NO WARRANTIES, EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION THE IMPLIED WARRANTIES OF NON-INFRINGEMENT, MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE, REGARDING THE CTI SOFTWARE OR ITS USE AND OPERATION ALONE OR IN COMBINATION WITH YOUR PRODUCTS.
 IN NO EVENT SHALL CTI BE LIABLE FOR ANY SPECIAL, INDIRECT, INCIDENTAL OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION). ARISING IN ANY WAY OUT OF THE USE, REPRODUCTION, MODIFICATION AND/OR DISTRIBUTION OF THE APPLE SOFTWARE, HOWEVER CAUSED AND WHETHER UNDER THEORY OF CONTRACT, TORT (INCLUDING NEGLIGENCE), STRICT LIABILITY OR OTHERWISE, EVEN IF CTI HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 Copyright (c) 2015 Colworx Tech Inc. All rights reserved.*/

package com.webmobi.gecmedia.CustomViews;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.webmobi.gecmedia.R;
import com.webmobi.gecmedia.Views.RegActivity;


public class DialogCustom {

    String stTitle;
    String stMessage;
    Activity act;
    RelativeLayout rl;
    Button btnCancel, btnview;
    TextView tvTitle;
    TextView tvMessage;
    Dialog dialog;

    public DialogCustom(String stTitle, String stMessage, Activity act) {

        this.stMessage = stMessage;
        this.stTitle = stTitle;
        this.act = act;

    }

    public void showDialog() {

        dialog = new Dialog(act,android.R.style.Theme_Translucent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.alert_dialog);
        rl = (RelativeLayout) dialog.findViewById(R.id.rl);
        btnCancel = (Button) dialog.findViewById(R.id.btncancel);
        btnview = (Button) dialog.findViewById(R.id.btnview);

        tvTitle = (TextView) dialog.findViewById(R.id.tvTitle);
        tvMessage = (TextView) dialog.findViewById(R.id.tvMessage);

        tvMessage.setVisibility(View.INVISIBLE);
        tvTitle.setVisibility(View.INVISIBLE);
        btnCancel.setVisibility(View.INVISIBLE);
        btnview.setVisibility(View.INVISIBLE);


        tvMessage.setText(stMessage);
        tvTitle.setText(stTitle);


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
                btnCancel.setVisibility(View.VISIBLE);
                btnview.setVisibility(View.VISIBLE);

            }
        });


        rl.setAnimation(scale_up);

        btnview.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                dialog.dismiss();
                Intent i = new Intent(act, RegActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                act.startActivity(i);
                act.finish();
//				new Timer().schedule(new TimerTask() {
//				    @Override
//				    public void run() {
//				        // run AsyncTask here.
//
//
//				    }
//				}, 100);
//				dialog.dismiss();


            }
        });

        btnCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                act.finish();
            }
        });

        dialog.show();
    }
}
