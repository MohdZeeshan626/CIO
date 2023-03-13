package com.singleevent.sdk.gallery_camera.Gallery_Adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.singleevent.sdk.R;

/**
 * Created by hoanglam on 8/17/17.
 */

public class ImageLoader {


    public ImageLoader() {
    }

    public void loadImage(String path, ImageView imageView) {
        Glide.with(imageView.getContext().getApplicationContext())
                .load(path)
                .placeholder(R.drawable.imagepicker_image_placeholder)
                .error(R.drawable.imagepicker_image_placeholder)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }


    public void loadImage(String path, ImageView imageView,int width,int height) {
        Glide.with(imageView.getContext().getApplicationContext())
                .load(path)
                .placeholder(R.drawable.imagepicker_image_placeholder)
                .error(R.drawable.imagepicker_image_placeholder)
                .override(width, height)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }
}
