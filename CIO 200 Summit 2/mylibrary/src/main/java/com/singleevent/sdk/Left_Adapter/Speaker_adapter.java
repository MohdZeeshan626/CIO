package com.singleevent.sdk.Left_Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.makeramen.roundedimageview.RoundedImageView;
import com.singleevent.sdk.Custom_View.Letter.LetterTileProvider;
import com.singleevent.sdk.Custom_View.Util;
import com.singleevent.sdk.model.Items;
import com.singleevent.sdk.model.LocalArraylist.agendaspeakerlist;
import com.singleevent.sdk.model.SpeakerFilter;
import com.singleevent.sdk.R;
import com.singleevent.sdk.View.LeftActivity.SpeakerDetails;
import com.singleevent.sdk.utils.DrawableUtil;


import java.util.ArrayList;

/**
 * Created by Admin on 5/29/2017.
 */

public class Speaker_adapter extends BaseAdapter implements Filterable {

    private int lastPosition = -1;
    ArrayList<Items> mStringFilterList;
    Context context;
    ValueFilter valueFilter;
    ArrayList<Integer> idlist = new ArrayList<>();
    private ArrayList<Items> speakerItems = new ArrayList<>();
    private double ImgWidth, Imgheight;
    LetterTileProvider tileProvider;
    private float dpWidth;
    Bitmap letterTile;


    public Speaker_adapter(Context context, ArrayList<Items> speakerItems, double ImgWidth, double Imgheight) {
        // TODO Auto-generated constructor stub
        this.speakerItems = speakerItems;
        this.context = context;
        this.ImgWidth = ImgWidth;
        this.Imgheight = Imgheight;
        mStringFilterList = speakerItems;
    }

    @Override
    public Filter getFilter() {
        if (valueFilter == null) {
            valueFilter = new ValueFilter();
        }
        return valueFilter;
    }


    @Override
    public int getCount() {
        return speakerItems.size();
    }

    @Override
    public Object getItem(int i) {
        return speakerItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return speakerItems.get(i).getSpeakerId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ViewHolder holder;
        tileProvider = new LetterTileProvider(context);
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        dpWidth = displayMetrics.widthPixels * 0.10F;

        if (view == null) {

            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = vi.inflate(R.layout.s_userlist_speaker, null);
            holder = new ViewHolder();
            holder.name = (TextView) view.findViewById(R.id.book_title);
            holder.lastmsg = (TextView) view.findViewById(R.id.author_name);
            holder.badge = (TextView) view.findViewById(R.id.counter);
            holder.time = (TextView) view.findViewById(R.id.time);
            holder.city=(TextView)view.findViewById(R.id.ucity);
            holder.profilepic = (RoundedImageView) view.findViewById(R.id.profilepic);

            // setting image width
         //   RelativeLayout.LayoutParams imgParams = (RelativeLayout.LayoutParams) holder.profilepic.getLayoutParams();
          //  imgParams.width = (int) 80;
          //  imgParams.height = (int) 80;
        //    holder.profilepic.setLayoutParams(imgParams);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        final Items c = speakerItems.get(i);
        holder.time.setVisibility(View.GONE);
        holder.city.setVisibility(View.GONE);
        holder.badge.setVisibility(View.GONE);
        holder.name.setText(Html.fromHtml(c.getName()));
        holder.lastmsg.setText(Html.fromHtml(c.getDescription()));
        holder.name.setTypeface(Util.regulartypeface(context));
        holder.lastmsg.setTypeface(Util.lighttypeface(context));
        if(!c.getImage().equalsIgnoreCase("") && c.getImage()!=null) {

            Glide.with(context.getApplicationContext())

                    .load((c.getImage().equalsIgnoreCase("")) ? R.drawable.s_user : c.getImage())
                    .asBitmap()
                    .placeholder(R.drawable.s_user)
                    .error(R.drawable.s_user)
                    .into(holder.profilepic);
            holder.profilepic.setCornerRadius(8, 8, 8, 8);
        }
        else{



                try {
                    if(c.getName()!=null){
                        int n=(DrawableUtil.getColor(context, c.getName()));
                        letterTile = tileProvider.getLetterTile(c.getName(), "key", (int) dpWidth, (int) dpWidth,n);
                        holder.profilepic.setImageBitmap((letterTile));
                        holder.profilepic.setCornerRadius(8, 8, 8, 8);
                    }
                }catch (Exception e)
                {

                }


        }

                        /*new BitmapImageViewTarget(holder.profilepic) {
                    @Override
                    protected void setResource(Bitmap resource) {

                        RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(context.getResources(),
                                Bitmap.createScaledBitmap(resource, (int) ImgWidth, (int) ImgWidth, false));
                        drawable.setCircular(true);
                        holder.profilepic.setImageDrawable(drawable);
                    }
                });*/

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                agendaspeakerlist speakers = new agendaspeakerlist(c.getSpeakerId(), c.getName(), c.getDescription(), c.getImage(), c.getDetails(), c.getFacebook(), c.getLinkedin(), c.getAgendaId(),c.getSpeaker_document_url()
                 ,c.getSpeaker_document_hide(),c.getSpeaker_document_name()  );
                Bundle args = new Bundle();
                args.putSerializable("SpeakerDetails", speakers);
                Intent i = new Intent(context, SpeakerDetails.class);
                i.setAction("com.speaker");
                i.putExtras(args);
                context.startActivity(i);
            }
        });

        return view;
    }

    private class ViewHolder {
        TextView name, lastmsg, badge, time,city;
        RoundedImageView profilepic;
    }

    private class ValueFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if (constraint != null && constraint.length() > 0) {
                ArrayList<Items> filterList = new ArrayList<>();
                for (int i = 0; i < mStringFilterList.size(); i++) {
                    if ((mStringFilterList.get(i).getName().toUpperCase())
                            .contains(constraint.toString().toUpperCase())) {

                        SpeakerFilter speakerFilter = new SpeakerFilter(mStringFilterList.get(i)
                                .getName(), mStringFilterList.get(i)
                                .getDescription(), mStringFilterList.get(i)
                                .getImage(), mStringFilterList.get(i).getSpeakerId(),
                                mStringFilterList.get(i).getAgendaId(), mStringFilterList.get(i).getDetails(),mStringFilterList.get(i).getSpeaker_document_url(),
                                mStringFilterList.get(i).getSpeaker_document_hide(),mStringFilterList.get(i).getSpeaker_document_name());

                        filterList.add(speakerFilter);
                    }
                }
                results.count = filterList.size();
                results.values = filterList;
            } else {
                results.count = mStringFilterList.size();
                results.values = mStringFilterList;
            }
            return results;

        }

        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            speakerItems = (ArrayList<Items>) results.values;
            notifyDataSetChanged();
        }

    }
}
