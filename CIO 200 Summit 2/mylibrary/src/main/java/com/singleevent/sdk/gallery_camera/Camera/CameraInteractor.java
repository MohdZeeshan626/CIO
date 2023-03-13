package com.singleevent.sdk.gallery_camera.Camera;

import android.content.Context;
import android.content.Intent;

import com.singleevent.sdk.gallery_camera.Gallery_Adapter.OnImageReadyListener;
import com.singleevent.sdk.gallery_camera.Gallery_Model.Config;


/**
 * Created by webMOBI on 9/5/2017.
 */

public interface CameraInteractor {
    Intent getCameraIntent(Context context, Config config);

    void getImage(Context context, Intent intent, OnImageReadyListener imageReadyListener);
}
