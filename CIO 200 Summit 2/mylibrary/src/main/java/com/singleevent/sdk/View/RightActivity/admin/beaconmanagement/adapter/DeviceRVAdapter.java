package com.singleevent.sdk.View.RightActivity.admin.beaconmanagement.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.minew.beaconplus.sdk.MTFrameHandler;
import com.minew.beaconplus.sdk.MTPeripheral;
import com.singleevent.sdk.R;
import com.singleevent.sdk.View.RightActivity.admin.beaconmanagement.BMUserDetails;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lenovo on 22-11-2018.
 */

public class DeviceRVAdapter extends RecyclerView.Adapter<DeviceRVAdapter.ViewHolder> {
    Context context;
    List<MTPeripheral> device_list;
    List<String> assigned_beacon_list;
    String user_name;

    public DeviceRVAdapter(@NonNull Context context, int resource, ArrayList<MTPeripheral> device_list, String user_name, List<String> assigned_beacon_list) {
        this.context = context;
        this.device_list = device_list;
        this.user_name = user_name;
        this.assigned_beacon_list = assigned_beacon_list;
    }

    public void updateList(List<MTPeripheral> list) {

        device_list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DeviceRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_device_row, parent, false);

        DeviceRVAdapter.ViewHolder viewHolder = new DeviceRVAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceRVAdapter.ViewHolder holder, int position) {
        MTFrameHandler mtFrameHandler = device_list.get(position).mMTFrameHandler;
        final String mac = mtFrameHandler.getMac();
        int rssi = mtFrameHandler.getRssi();
        holder.user_name_tv.setText(mac);
        if (assigned_beacon_list.contains(mac)) {
            holder.assigned_beacon.setImageResource(R.drawable.assigned_beacon);
        } else
            holder.assigned_beacon.setImageResource(R.drawable.assigned_beacon_white);

        //setting the signal strength according to the rssi value
        if (rssi >= -60)
            holder.signal_ic.setImageResource(R.drawable.good_signal);
        else if (rssi < -60 && rssi >= -75)
            holder.signal_ic.setImageResource(R.drawable.average_signal);
        else
            holder.signal_ic.setImageResource(R.drawable.low_signal);

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmDialog(mac, user_name);
            }
        });
    }

    private void confirmDialog(final String mac, String user_name) {
        //alert dialog for assigning device to the particular user
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder((Activity) context, R.style.MyAlertDialogStyle);
        builder.setMessage("Are you sure you want to assign " + mac + " to " + user_name + " ?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra(BMUserDetails.RESULT_DETAILS, mac);
                        if (context instanceof Activity) {
                            ((Activity) context).setResult(Activity.RESULT_OK, resultIntent);
                            ((Activity) context).finish();
                        }
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })

                .show();
    }

    @Override
    public int getItemCount() {
        return device_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView signal_ic, assigned_beacon;
        TextView user_name_tv, device_rssi;
        LinearLayout linearLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            user_name_tv = (TextView) itemView.findViewById(R.id.list_row_devicename);
            signal_ic = (ImageView) itemView.findViewById(R.id.list_row_devicerssi);
            assigned_beacon = (ImageView) itemView.findViewById(R.id.list_row_assignedbeacon);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.device_list_ll);
        }
    }
}
