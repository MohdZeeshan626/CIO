package com.singleevent.sdk.Left_Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.makeramen.roundedimageview.RoundedImageView;
import com.singleevent.sdk.Custom_View.Util;
import com.singleevent.sdk.View.RightActivity.MyCompanyDetails;
import com.singleevent.sdk.model.AppDetails;
import com.singleevent.sdk.model.Items;
import com.singleevent.sdk.R;
import com.singleevent.sdk.View.LeftActivity.SponsorDetails;


import java.util.HashMap;
import java.util.List;

import cyd.awesome.material.AwesomeText;
import io.paperdb.Paper;

public class SponsorsAdapter extends RecyclerView.Adapter<SponsorsAdapter.ViewHolder> {

    private static Context context1;
    List<Items> sponsorsList;
    AppDetails appDetails;
    public String userid;
    public static double ImgWidth, Imgheight;
    public HashMap<String,Integer> categoriyList;
    public static int  form;

    public SponsorsAdapter(Context context, List<Items> myDataset, HashMap<String,Integer> categoryList, double width, double hight,int form) {
        sponsorsList = myDataset;
        this.categoriyList =categoryList;
        SponsorsAdapter.context1 = context;
        ImgWidth = width;
        Imgheight = hight;
        this.form=form;

        Paper.init(context);
        userid = Paper.book().read("userId","");
        appDetails = Paper.book().read("Appdetails",null);

    }

    //after search update list
    public void updateList(List<Items> list){

        sponsorsList = list;
        notifyDataSetChanged();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent,
                                         int viewType) {
        // create a new view
        View v = LayoutInflater.from(context1)
                .inflate(R.layout.s_sponsorview, parent, false);

        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        final Items items = sponsorsList.get(position);


        holder.setItem(items,categoriyList,position,sponsorsList);


    }

    @Override
    public int getItemCount() {
        return sponsorsList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        Context context;
        Items items;
        HashMap<String,Integer> categorylst;
        AppDetails appDetails;
        LinearLayout sponsorimage;
        static OnCardClickListner onCardClickListner;
        TextView sname;
        TextView sdes ;
        TextView  alpha_header;
        TextView scat;
        AwesomeText dots;
        RoundedImageView simage;

        ViewHolder(View v) {
            super(v);
            appDetails = Paper.book().read("Appdetails");

            sponsorimage = (LinearLayout) v.findViewById(R.id.sponsorview);
             simage = (RoundedImageView) v.findViewById(R.id.sponsorimage);
        //    RelativeLayout.LayoutParams sparams = (RelativeLayout.LayoutParams) sponsorimage.getLayoutParams();
         //   sparams.width = (int) ImgWidth;
         //   sparams.height = (int) Imgheight;
          //  sponsorimage.setLayoutParams(sparams);


             sname = (TextView) v.findViewById(R.id.title);
             sdes = (TextView) v.findViewById(R.id.des);
             scat = (TextView) v.findViewById(R.id.cat);
             dots = (AwesomeText) v.findViewById(R.id.dots1);
            alpha_header = (TextView) v.findViewById(R.id.alpha_header);

             v.setOnClickListener(this);
        }

        void setItem(Items itm,HashMap<String,Integer> categorylist,int position,List<Items> itemlist) {
            try {
                this.items = itm;
                this.categorylst = categorylist;

                //new logic
                if (position == 0) {
                    alpha_header.setVisibility(View.GONE);
                    alpha_header.setText(items.getCompany().substring(0, 1));
                } else if (!items.getCompany().substring(0, 1).equalsIgnoreCase(itemlist.get(position - 1).getCompany().substring(0, 1))) {
                    alpha_header.setVisibility(View.GONE);
                    alpha_header.setText(items.getCompany().substring(0, 1));
                } else {
                    alpha_header.setVisibility(View.GONE);

                }


                String Image = items.getImage();
                Glide.with(context1.getApplicationContext())

                        .load((Image.equalsIgnoreCase("")) ? R.drawable.default_partner : Image)
                        .asBitmap()
                        .placeholder(R.drawable.exhibitiorimg)
                        .error(R.drawable.exhibitiorimg)
                        .into(simage);
                simage.setCornerRadius(12,12,12,12);
                /*new BitmapImageViewTarget(simage) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                simage.setImageBitmap(Util.scaleBitmap(resource, (int) ImgWidth, (int) Imgheight));
                            }
                        });*/


                sname.setText(items.getCompany());
                sdes.setText(Html.fromHtml(items.getWebsite()));
                //make first letter capital then settext
                String newstr = items.getCategories().substring(0, 1).toUpperCase() + items.getCategories().substring(1);
                scat.setText(newstr);
                dots.setTextColor(categorylist.get(items.getCategories()));

            }catch (Exception e){

            }
        }

        @Override
        public void onClick(View view) {
            System.out.println("Item clicked pos " + getAdapterPosition());

            int position = getAdapterPosition();

            //onCardClickListner.OnItemClick( view, items , position );
            if(form==0) {
                Bundle args = new Bundle();
                args.putInt("Color", categorylst.get(items.getCategories()));
                args.putSerializable("Items", items);

                Intent i = new Intent(context1, SponsorDetails.class);
                i.putExtras(args);
                context1.startActivity(i);
            }
            else{
                Bundle args = new Bundle();
                args.putInt("Color", categorylst.get(items.getCategories()));
                args.putSerializable("Items", items);

                Intent i = new Intent(context1, MyCompanyDetails.class);
                i.putExtras(args);
                context1.startActivity(i);
            }
        }
    }

    public void setOnCardClickListner(OnCardClickListner onCardClickListner) {
        ViewHolder.onCardClickListner = onCardClickListner;
    }

    public interface OnCardClickListner {
        void OnItemLongClicked(View view, Items item, int position);
        void OnItemClick(View view, Items item, int position);
    }
}

