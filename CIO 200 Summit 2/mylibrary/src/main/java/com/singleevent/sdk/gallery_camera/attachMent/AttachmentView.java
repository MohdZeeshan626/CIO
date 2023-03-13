package com.singleevent.sdk.gallery_camera.attachMent;


import android.provider.MediaStore;
import android.widget.VideoView;

import com.singleevent.sdk.gallery_camera.Gallery_Model.Image;

import java.util.ArrayList;

/**
 * Created by webMOBI on 9/5/2017.
 */

public interface AttachmentView {

    void Sorted(ArrayList<Image> itemList);

    void IsWidth(boolean iswidth);
   // void IsWidth1(boolean iswidth);


}
