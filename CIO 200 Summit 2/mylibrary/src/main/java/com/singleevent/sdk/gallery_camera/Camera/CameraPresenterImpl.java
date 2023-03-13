package com.singleevent.sdk.gallery_camera.Camera;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.singleevent.sdk.gallery_camera.Gallery_Adapter.OnImageReadyListener;
import com.singleevent.sdk.gallery_camera.Gallery_Model.Config;
import com.singleevent.sdk.gallery_camera.Gallery_Model.Image;
import com.singleevent.sdk.R;

import java.util.List;

/**
 * Created by webMOBI on 9/5/2017.
 */

public class CameraPresenterImpl implements CameraPresenter {
    private CameraView galleryview;
    private Context context;
    private CameraInteractorImpl cameraInteractor;


    public CameraPresenterImpl(CameraView galleryview, Context context) {
        this.galleryview = galleryview;
        this.context = context;
        this.cameraInteractor = new CameraInteractorImpl();

    }

    @Override
    public void captureImage(Activity activity, Config config, int requestCode) {
        Context context = activity.getApplicationContext();
        Intent intent = cameraInteractor.getCameraIntent(activity, config);
        if (intent == null) {
            Toast.makeText(context, context.getString(R.string.imagepicker_error_create_image_file), Toast.LENGTH_LONG).show();
            return;
        }
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    public void finishCaptureImage(Context context, Intent data, Config config) {
        cameraInteractor.getImage(context, data, new OnImageReadyListener() {
            @Override
            public void onImageReady(List<Image> images) {
                galleryview.finishPickImages(images);
            }
        });
    }
}
