package com.singleevent.sdk.View.LeftActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;

import com.singleevent.sdk.R;
import com.singleevent.sdk.agora.openvcall.ui.MainActivity;

import io.agora.rtc.Constants;

public class AgoraRole {

}//
//
//    public void onClickJoin(View view) {
//        // Show a dialog box to choose a user role.
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setMessage(R.string.msg_choose_role);
//        builder.setNegativeButton(R.string.label_audience, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                AgoraRole.this.forwardToLiveRoom(Constants.CLIENT_ROLE_AUDIENCE);
//            }
//        });
//        builder.setPositiveButton(R.string.label_broadcaster, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                AgoraRole.this.forwardToLiveRoom(Constants.CLIENT_ROLE_BROADCASTER);
//            }
//        });
//        AlertDialog dialog = builder.create();
//
//        dialog.show();
//    }
//
//    // Get the user role and channel name specified by the user.
//// The channel name is used when joining the channel.
//    public void forwardToLiveRoom(int cRole) {
//        final EditText v_room = (EditText) findViewById(R.id.room_name);
//        String room = v_room.getText().toString();
//
//        Intent i = new Intent(AgoraRole.this, LiveRoomActivity.class);
//        i.putExtra("CRole", cRole);
//        i.putExtra("CName", room);
//
//        startActivity(i);
//    }
//
//    // Pass in the user role set by the user.
//    private int mRole;
//    mRole = getIntent().getIntExtra("CRole", 0);
//
//    private void setClientRole() {
//        mRtcEngine.setClientRole(mRole);
//    }
//
// }

