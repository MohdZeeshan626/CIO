package com.singleevent.sdk.gallery_camera.Gallery;


import com.singleevent.sdk.gallery_camera.Gallery_Model.Image;

import java.util.List;

/**
 * Created by webMOBI on 9/5/2017.
 */

public interface GalleryView {

    void showLoading(boolean isLoading);

    void showFetchCompleted(List<Image> images);

    void showError(Throwable throwable);

    void showEmpty();

    void showCapturedImage();

    void finishPickImages(List<Image> images);
}
