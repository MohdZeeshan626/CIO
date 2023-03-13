package com.singleevent.sdk.View.RightActivity.admin.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.singleevent.sdk.Custom_View.TxtVCustomFonts;
import com.singleevent.sdk.R;
import com.singleevent.sdk.View.RightActivity.admin.model.ReportModel;

import java.util.List;

/**
 * Created by webMOBI on 12/18/2017.
 */

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ViewHolder> {
    private Context context;
    private List<ReportModel> mDataSet;


    public ReportAdapter(Context context, List<ReportModel> myDataset) {
        this.mDataSet = myDataset;
        this.context = context;


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(context)
                .inflate(R.layout.report_adapter_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {


        ReportModel model = mDataSet.get(position);

        holder.tv_exhibitor_name.setText((++position) +". " +model.getExhibitorName()+" - "+ model.getLeadCount());

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TxtVCustomFonts tv_exhibitor_name;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_exhibitor_name = (TxtVCustomFonts) itemView.findViewById(R.id.tv_exhibitor_name);
        }

    }
}
