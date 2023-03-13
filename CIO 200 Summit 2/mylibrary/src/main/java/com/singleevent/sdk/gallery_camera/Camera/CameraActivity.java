package com.singleevent.sdk.gallery_camera.Camera;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.singleevent.sdk.gallery_camera.Gallery_Model.Config;
import com.singleevent.sdk.gallery_camera.Gallery_Model.Image;
import com.singleevent.sdk.gallery_camera.Helper.CameraHelper;
import com.singleevent.sdk.gallery_camera.Helper.PermissionHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by webMOBI on 9/5/2017.
 */

public class CameraActivity extends AppCompatActivity implements CameraView {


    private Config config;
    private boolean isOpeningCamera = false;
    private CameraPresenterImpl presenter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if (intent == null) {
            finish();
            return;
        }

        config = intent.getParcelableExtra(Config.EXTRA_CONFIG);
        presenter = new CameraPresenterImpl(this, CameraActivity.this);
        requestCameraPermission();

    }

    private void requestCameraPermission() {


        boolean wesGranted = PermissionHelper.hasSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        boolean cameraGranted = PermissionHelper.hasSelfPermission(this, Manifest.permission.CAMERA);


        List<String> permissions = new ArrayList<>();


        if (!wesGranted) {
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (!cameraGranted) {
            permissions.add(Manifest.permission.CAMERA);
        }

        if (!wesGranted || !cameraGranted)
            PermissionHelper.requestAllPermissions(this, permissions.toArray(new String[permissions.size()]), Config.RC_CAMERA_PERMISSION);
        else
            captureImage();

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        switch (requestCode) {
            case Config.RC_CAMERA_PERMISSION: {
                if (PermissionHelper.hasGranted(grantResults)) {
                    captureImage();
                    return;
                }
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Config.RC_CAPTURE_IMAGE && resultCode == RESULT_OK) {
            presenter.finishCaptureImage(this, data, config);
        } else {
            setResult(RESULT_CANCELED, new Intent());
            finish();
        }
    }


    private void captureImage() {
        if (!CameraHelper.checkCameraAvailability(this)) {
            finish();
            return;
        }

        presenter.captureImage(this, config, Config.RC_CAPTURE_IMAGE);
        isOpeningCamera = true;

    }


    @Override
    public void finishPickImages(List<Image> images) {
        Intent data = new Intent();
        data.putParcelableArrayListExtra(Config.EXTRA_IMAGES, (ArrayList<? extends Parcelable>) images);
        setResult(RESULT_OK, data);
        finish();
    }


    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
    }

}
