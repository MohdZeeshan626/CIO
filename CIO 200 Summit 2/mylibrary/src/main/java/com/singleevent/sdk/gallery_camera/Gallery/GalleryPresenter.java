package com.singleevent.sdk.gallery_camera.Gallery;


import com.singleevent.sdk.gallery_camera.Gallery_Model.Image;

import java.util.List;

/**
 * Created by webMOBI on 9/5/2017.
 */

public interface GalleryPresenter {

    void loadImages();

    void onDoneSelectImages(List<Image> selectedImages);

}
