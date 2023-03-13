package com.singleevent.sdk.gallery_camera.Gallery;

import android.content.Context;

import com.singleevent.sdk.gallery_camera.Gallery_Model.Image;

import java.util.List;

/**
 * Created by webMOBI on 9/5/2017.
 */

public interface GalleryInteractor {


    interface OnLoadFinishedListener {
        void onImageLoaded(List<Image> images);

        void onFailed(Throwable throwable);
    }

    void loadDeviceImages(Context context, GalleryInteractor.OnLoadFinishedListener listener);

}
