package com.singleevent.sdk.Left_Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.RecyclerView;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.makeramen.roundedimageview.RoundedImageView;
import com.singleevent.sdk.Custom_View.Letter.LetterTileProvider;
import com.singleevent.sdk.Custom_View.Util;
import com.singleevent.sdk.model.AppDetails;
import com.singleevent.sdk.model.LeaderBoard.LeaderBoard;
import com.singleevent.sdk.R;
import com.singleevent.sdk.utils.DrawableUtil;

import java.util.ArrayList;
import java.util.Random;

import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

/**
 * Created by webMOBI on 1/5/2018.
 */

public class LeaderBoardAdapter extends RecyclerView.Adapter<LeaderBoardAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<LeaderBoard> values;
    private Unbinder unbinder;
    private AppDetails appDetails;
    private double CellWidth;
    static int  dataPos;
    LetterTileProvider tileProvider;
    Bitmap letterTile;
    double dpWidth;
    Random r;


    //constructor
    public LeaderBoardAdapter(Context context, ArrayList<LeaderBoard> values,double CellWidth){
        appDetails = Paper.book().read("Appdetails");
        this.context = context;
        this.values = values;
        this.CellWidth = CellWidth;
        tileProvider = new LetterTileProvider(context);
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.leaderboard_adapter,parent,
                false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.setIsRecyclable(false);
        holder.tv_name.setText(values.get(position).getFirst_name()+" "+values.get(position).getLast_name() );
        holder.tv_email.setText(values.get(position).getEmail());
        holder.tv_leadPoints.setText(String.valueOf(values.get(position).getTotal_points()));
        holder.tv_leadPoints.setBackground(Util.setdrawable(context, R.drawable.rectanglebackground,
                Color.parseColor(appDetails.getTheme_color())));


        if (!values.get(position).getProfile_pic().equalsIgnoreCase("")){
            Glide.with(context.getApplicationContext())

                    .load(values.get(position).getProfile_pic().equalsIgnoreCase("") ? R.drawable.round_user : values.get(position).getProfile_pic())
                    .asBitmap()
                    .placeholder(R.drawable.round_user)
                    .error(R.drawable.round_user)
                    .into(holder.iv_profile_pic);
            holder.iv_profile_pic.setCornerRadius(12,12,12,12);
            /*new BitmapImageViewTarget(holder.iv_profile_pic) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(context.getResources(),
                                    Bitmap.createScaledBitmap(resource, (int) CellWidth, (int) CellWidth, false));
                            drawable.setCircular(true);
                            holder.iv_profile_pic.setImageDrawable(drawable);
                        }
                    });*/
        }else if (!values.get(position).getFirst_name().equals("")){
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            dpWidth = displayMetrics.widthPixels * 0.10F;
           /* holder.iv_profile_pic.setBackground(DrawableUtil.genBackgroundDrawable(context,values.get(position).getFirst_name()));
            holder.iv_profile_pic.setImageResource(DrawableUtil.getDrawableForName(values.get(position).getFirst_name() ));
            holder.iv_profile_pic.setCornerRadius(12,12,12,12);*/
            r=new Random();
            int color = Color.argb(255, r.nextInt(256), r.nextInt(256), r.nextInt(256));
            letterTile = tileProvider.getLetterTile(values.get(position).getFirst_name(), "key", (int) dpWidth, (int) dpWidth, color);
            holder.iv_profile_pic.setImageBitmap((letterTile));
            holder.iv_profile_pic.setCornerRadius(12, 12, 12, 12);
        }


        dataPos = position;
        if(dataPos==0){
            holder.tv_count.setBackgroundColor(context.getResources().getColor(R.color.m_yellow));
            holder.scoreimg.setVisibility(View.VISIBLE);
            holder.scoreimg.setImageDrawable(context.getResources().getDrawable(R.drawable.goldscore));
        }

        if (dataPos == 1){
            holder.tv_count.setBackgroundColor(context.getResources().getColor(R.color.m_yellow));
            holder.scoreimg.setVisibility(View.VISIBLE);
            holder.scoreimg.setImageDrawable(context.getResources().getDrawable(R.drawable.medsecond));

        }
        if ( dataPos == values.size()-1)
            holder.tv_count.setBackground(context.getResources().getDrawable(R.drawable.rectanglebackground));
        if(dataPos==2){
            holder.scoreimg.setVisibility(View.VISIBLE);
            holder.scoreimg.setImageDrawable(context.getResources().getDrawable(R.drawable.medthird));
        }
       /* if(dataPos==3)
        {
            holder.scoreimg.setVisibility(View.VISIBLE);
            holder.scoreimg.setImageDrawable(context.getResources().getDrawable(R.drawable.medthird));
        }*/

        holder.tv_count.setText(String.valueOf( 1 + position));

       /* holder.progressBar.setMax(78);
        holder.progressBar.setProgress(values.get(position).getLeads());*/


    }

    @Override
    public int getItemCount() {
        return values.size();
    }

    //viewholder for adapter
    static class MyViewHolder extends RecyclerView.ViewHolder{
        RoundedImageView iv_profile_pic;
        TextView tv_name, tv_email;
        ImageView scoreimg;
        Button tv_leadPoints,tv_count;
       /* ProgressBar progressBar;*/

        public MyViewHolder(View itemView) {
            super(itemView);

            iv_profile_pic = (RoundedImageView) itemView.findViewById(R.id.iv_profile_pic);
            tv_name = (TextView)itemView.findViewById(R.id.tv_name);
            scoreimg=(ImageView)itemView.findViewById(R.id.scoreimg);
            tv_email = (TextView)itemView.findViewById(R.id.tv_email);
            tv_leadPoints = (Button)itemView.findViewById(R.id.tv_leads);
            tv_count = (Button)itemView.findViewById(R.id.tv_count);
            //progressBar = (ProgressBar)itemView.findViewById(R.id.progressBar);


        }
    }
	
    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
