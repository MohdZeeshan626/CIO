package com.singleevent.sdk.gallery_camera.Camera;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.singleevent.sdk.gallery_camera.Gallery_Model.Config;


/**
 * Created by webMOBI on 9/5/2017.
 */

public interface CameraPresenter {
    void captureImage(Activity activity, Config config, int requestCode);

    void finishCaptureImage(Context context, Intent data, final Config config);
}
