package com.singleevent.sdk.gallery_camera.attachMent;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.singleevent.sdk.gallery_camera.Gallery_Model.Image;
import com.singleevent.sdk.R;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by webMOBI on 9/7/2017.
 */

public class AttachmentImpl {

    Context context;
    AttachmentView attachmentview;
    ArrayList<Image> itemList = new ArrayList<Image>();

    int width;
    int height;

    public enum ViewType {
        Isvertical, isHorizontal, isNormal
    }

    public AttachmentImpl(AttachmentView attachmentview, Context context, int width,int height) {
        this.attachmentview = attachmentview;
        this.context = context;
        this.width = width;
        this.height=height;

    }

    public void listofitems(ArrayList<Image> itemList) {
        this.itemList.clear();
        this.itemList.addAll(itemList);
        Collections.sort(this.itemList, new SortingImage());
        attachmentview.Sorted(itemList);
        attachmentview.IsWidth(this.itemList.get(0).getImageWidth() < this.itemList.get(0).getImageHeight() ? false : true);
    }

/*
    public void listofitems1(ArrayList<VideoView> itemList) {
        this.itemList1.clear();
        this.itemList1.addAll(itemList);
       //Collections.sort(this.itemList1, new SortingImage());
        attachmentview.Sorted1(itemList1);
      //  attachmentview.IsWidth(this.itemList.get(0).getImageWidth() < this.itemList.get(0).getImageHeight() ? false : true);
        attachmentview.IsWidth1(true);
    }
*/



    public View getViewone(ViewType viewtype, int startindex, int endindex, boolean isSelected) {

        switch (viewtype) {
            case Isvertical:
                return loadimage(startindex, endindex, LinearLayout.VERTICAL, (int) (width * 0.70), width, isSelected);

            case isHorizontal:
                return loadimage(startindex, endindex, LinearLayout.HORIZONTAL, width, width / 2, isSelected);

            case isNormal:
                return loadimage(startindex, endindex, LinearLayout.VERTICAL, (int)(width*0.90), (int)(width*0.80), isSelected);
            default:
                return null;


        }
    }
/*
    public View getViewone1(ViewType viewtype, int startindex, int endindex, boolean isSelected) {

        switch (viewtype) {
            case Isvertical:
                return loadimage1(startindex, endindex, LinearLayout.VERTICAL, (int) (width * 0.70), width, isSelected);

            case isHorizontal:
                return loadimage1(startindex, endindex, LinearLayout.HORIZONTAL, width, width / 2, isSelected);

            case isNormal:
                return loadimage1(startindex, endindex, LinearLayout.VERTICAL, (int)(width*0.90), (int)(width*1.1), isSelected);
            default:
                return null;


        }
    }
*/


    public View getViewTwo(ViewType viewtype, int startindex, int endindex, boolean isSelected) {


        switch (viewtype) {
            case Isvertical:
                return loadimage(startindex, endindex, LinearLayout.VERTICAL, width / 2, width / 2, isSelected);
            case isHorizontal:
                return loadimage(startindex, endindex, LinearLayout.HORIZONTAL, width / 2, width / 2, isSelected);
            case isNormal:
                return loadimage(startindex, endindex, LinearLayout.HORIZONTAL, width / 2, width, isSelected);
            default:
                return null;
        }

    }


    public View getViewThree(ViewType viewtype, int startindex, int endindex, boolean isSelected) {

        switch (viewtype) {
            case Isvertical:
                return loadimage(startindex, endindex, LinearLayout.VERTICAL, width / 3, width / 3, isSelected);
            case isHorizontal:
                return loadimage(startindex, endindex, LinearLayout.HORIZONTAL, width / 3, width / 3, isSelected);
            default:
                return null;
        }

    }

    private View loadimage(int startindex, int endindex, int orientation, int width, int height, boolean isSelected) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout main = new LinearLayout(context);
        main.setOrientation(orientation);
        for (int i = startindex; i < endindex; i++) {
            View child = inflater.inflate(R.layout.attachment_item_image, null);
            ImageView image = (ImageView) child.findViewById(R.id.image_thumbnail);

            TextView morecount = (TextView) child.findViewById(R.id.morecount);
            morecount.setVisibility(View.GONE);




                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) image.getLayoutParams();
                params.width = width;
                params.height = height;
                image.setLayoutParams(params);
                Glide.with(context.getApplicationContext())
                        .load(itemList.get(i).getPath())
                        .placeholder(R.drawable.imagepicker_image_placeholder)
                        .error(R.drawable.imagepicker_image_placeholder)
                        .into(image);




            if ((isSelected) && (i == endindex - 1)) {
                image.setColorFilter(context.getResources().getColor(R.color.imagepicker_black_alpha_50), android.graphics.PorterDuff.Mode.SRC_ATOP);
                morecount.setVisibility(View.VISIBLE);
                morecount.setText("+ " + (itemList.size() - 5));
            }

            main.addView(child);
        }
        return main;
    }

/*
    private View loadimage1(int startindex, int endindex, int orientation, int width, int height, boolean isSelected) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout main = new LinearLayout(context);
        main.setOrientation(orientation);
        for (int i = startindex; i < endindex; i++) {
            View child = inflater.inflate(R.layout.attachment_item_image, null);
            ImageView image = (ImageView) child.findViewById(R.id.image_thumbnail);

            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) image.getLayoutParams();
            params.width = width;
            params.height = height;
            image.setLayoutParams(params);
            TextView morecount = (TextView) child.findViewById(R.id.morecount);
            morecount.setVisibility(View.GONE);
            if(!itemList.get(i).getType().equalsIgnoreCase("mp4")) {
                Glide.with(context.getApplicationContext())
                        .load(itemList.get(i))
                        .placeholder(R.drawable.imagepicker_image_placeholder)
                        .error(R.drawable.imagepicker_image_placeholder)
                        .into(image);
            }
            else{
                String path="file://"+itemList.get(i).getPath();
                image.setImageBitmap(createVideoThumbNail(path));

            }

            if ((isSelected) && (i == endindex - 1)) {
                image.setColorFilter(context.getResources().getColor(R.color.imagepicker_black_alpha_50), android.graphics.PorterDuff.Mode.SRC_ATOP);
                morecount.setVisibility(View.VISIBLE);
                morecount.setText("+ " + (itemList1.size() - 5));
            }

            main.addView(child);
        }
        return main;
    }
*/
    public Bitmap
    createVideoThumbNail(String path){
        return ThumbnailUtils.createVideoThumbnail(path,MediaStore.Video.Thumbnails.MICRO_KIND);
    }

}
