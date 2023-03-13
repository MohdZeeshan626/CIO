package com.singleevent.sdk.Left_Adapter;


import android.content.Context;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import com.github.chrisbanes.photoview.PhotoView;
import com.singleevent.sdk.R;


import java.util.ArrayList;

public class SwipeAdapters extends PagerAdapter {
    Context context;
    public ArrayList<String> imgurl = new ArrayList<>();
    LayoutInflater layoutInflater;
    int pos;




    public SwipeAdapters(Context context,ArrayList<String> imgurl,int position)
    {
        this. context = context;
        this.imgurl=imgurl;
        this.pos=position;

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return imgurl.size();
    }


    @Override
    public int getItemPosition(@NonNull Object object)
    {
        return POSITION_NONE;
    }




    @Override
    public boolean isViewFromObject(View view, Object object)
    {
        return object == view;
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {



        System.out.println("SWIPE "+position);
        View itemView = layoutInflater.inflate(R.layout.swipetest, container, false);

        PhotoView imageView = (PhotoView) itemView.findViewById(R.id.imageview);
        try {
            Glide.with(context)
                    .load(imgurl.get(position)).diskCacheStrategy(DiskCacheStrategy.ALL)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView);

            container.addView(itemView);
        }catch (Exception e){}
        return itemView;




    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        try{
            (container).removeView((View) object);}
        catch (Exception e){}
        System.out.println("delete position"+position);
    }


}