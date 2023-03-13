package com.singleevent.sdk.gallery_camera.Gallery_Adapter;



import com.singleevent.sdk.gallery_camera.Gallery_Model.Image;

import java.util.List;

public interface OnImageReadyListener {
    void onImageReady(List<Image> images);
}
