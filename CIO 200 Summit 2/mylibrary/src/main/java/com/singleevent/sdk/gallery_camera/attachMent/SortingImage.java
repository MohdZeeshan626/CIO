package com.singleevent.sdk.gallery_camera.attachMent;


import com.singleevent.sdk.gallery_camera.Gallery_Model.Image;

import java.util.Comparator;

/**
 * Created by webMOBI on 9/6/2017.
 */

public class SortingImage implements Comparator<Image> {

    public Integer getArea(int width, int height) {
        return width * height;
    }


    @Override
    public int compare(Image image1, Image image2) {
        if (image1.getArea() == image2.getArea())
            return 0;
        else if (image1.getArea() < image2.getArea())
            return 1;
        else
            return -1;
    }
}
