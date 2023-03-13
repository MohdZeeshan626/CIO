package com.singleevent.sdk.gallery_camera.Gallery_Adapter;



import com.singleevent.sdk.gallery_camera.Gallery_Model.Image;

import java.util.List;

/**
 * Created by hoanglam on 8/18/17.
 */

public interface OnImageSelectionListener {
    void onSelectionUpdate(List<Image> images);
}
