package com.singleevent.sdk.View.RightActivity.admin.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.singleevent.sdk.Custom_View.Letter.LetterTileProvider;
import com.singleevent.sdk.R;
import com.singleevent.sdk.View.RightActivity.admin.model.ExhibitorReportModel;
import com.singleevent.sdk.utils.DrawableUtil;

import java.util.List;

/**
 * Created by webMOBI on 12/18/2017.
 */

public class ExhibitorReportAdapter extends RecyclerView.Adapter<ExhibitorReportAdapter.ViewHolder> {
    private Context context;
    private List<ExhibitorReportModel> values;

    float dpWidth;
    LetterTileProvider tileProvider;
    Bitmap letterTile;


    public ExhibitorReportAdapter(Context context,float dpWidth, List<ExhibitorReportModel> myDataset) {
        this.values = myDataset;
        this.context = context;
        this.dpWidth = dpWidth;
        tileProvider = new LetterTileProvider(context);


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(context)
                .inflate(R.layout.exhibitor_report_adapter_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {


        ExhibitorReportModel model = values.get(position);

       holder.lead_name.setText(model.getLead_name());
      // holder.name.setText(model.getUsername());
        // setting image width
        RelativeLayout.LayoutParams imgParams = (RelativeLayout.LayoutParams) holder.profilePic.getLayoutParams();
        imgParams.width = (int) dpWidth;
        imgParams.height = (int) dpWidth;
        holder.profilePic.setLayoutParams(imgParams);

        holder.profilePic.setBackground(DrawableUtil.genBackgroundDrawable(context, model.getLead_name()));
        holder.profilePic.setImageResource(DrawableUtil.getDrawableForName(model.getLead_name()));

    }

    @Override
    public int getItemCount() {
        return values.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView lead_name,name;
        ImageView profilePic;

        public ViewHolder(View itemView) {
            super(itemView);

           lead_name = (TextView)itemView.findViewById(R.id.lead_name);
           name = (TextView)itemView.findViewById(R.id.name);
           profilePic = (ImageView)itemView.findViewById(R.id.profilepic);



        }

    }
}
