package com.singleevent.sdk.gallery_camera;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import androidx.fragment.app.Fragment;

import com.singleevent.sdk.gallery_camera.Camera.CameraActivity;
import com.singleevent.sdk.gallery_camera.Gallery.GalleryActivity;
import com.singleevent.sdk.gallery_camera.Gallery_Model.Config;
import com.singleevent.sdk.gallery_camera.Gallery_Model.Image;
import com.singleevent.sdk.gallery_camera.Gallery_Model.SavePath;
import com.singleevent.sdk.R;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;


/**
 * Created by webMOBI on 9/4/2017.
 */

public class GalleryPicker {

    public static abstract class BaseBuilder {

        protected Config config;

        public BaseBuilder(Context context) {
            this.config = new Config();

            Resources resources = context.getResources();
            config.setCameraMode(false);
            config.setMultipleMode(true);
            config.setShowCamera(false);
            config.setMaxSize(Config.MAX_SIZE);
            config.setDoneTitle(resources.getString(R.string.imagepicker_action_done));
            config.setImageTitle(resources.getString(R.string.imagepicker_title_image));
            config.setSavePath(SavePath.DEFAULT);
            config.setSelectedImages(new ArrayList<Image>());
        }
    }


    public static abstract class Builder extends BaseBuilder {

        public Builder(AppCompatActivity activity) {
            super(activity);
        }

        public Builder(Fragment fragment) {
            super(fragment.getContext());
        }

        public Builder setToolbarColor(int toolbarColor) {
            config.setToolbarColor(toolbarColor);
            return this;
        }


        public Builder setToolbarTextColor(String toolbarTextColor) {
            config.setToolbarTextColor(toolbarTextColor);
            return this;
        }

        public Builder setToolbarIconColor(String toolbarIconColor) {
            config.setToolbarIconColor(toolbarIconColor);
            return this;
        }

        public Builder setProgressBarColor(String progressBarColor) {
            config.setProgressBarColor(progressBarColor);
            return this;
        }


        public Builder setCameraMode(boolean isCameraOnly) {
            config.setCameraMode(isCameraOnly);
            return this;
        }

        public Builder setMultipleMode(boolean isMultipleMode) {
            config.setMultipleMode(isMultipleMode);
            return this;
        }


        public Builder setShowCamera(boolean isShowCamera) {
            config.setShowCamera(isShowCamera);
            return this;
        }

        public Builder setMaxSize(int maxSize) {
            config.setMaxSize(maxSize);
            return this;
        }

        public Builder setDoneTitle(String doneTitle) {
            config.setDoneTitle(doneTitle);
            return this;
        }


        public Builder setImageTitle(String imageTitle) {
            config.setImageTitle(imageTitle);
            return this;
        }

        public Builder setSavePath(String path) {
            config.setSavePath(new SavePath(path, false));
            return this;
        }

        public Builder setSelectedImages(ArrayList<Image> selectedImages) {
            config.setSelectedImages(selectedImages);
            return this;
        }

        public abstract void start();

    }

    static class ActivityBuilder extends Builder {
        private AppCompatActivity activity;

        public ActivityBuilder(AppCompatActivity activity) {
            super(activity);
            this.activity = activity;
        }

        @Override
        public void start() {
            Intent intent;
            if (!config.isCameraOnly()) {
                intent = new Intent(activity, GalleryActivity.class);
                intent.putExtra(Config.EXTRA_CONFIG, config);
                activity.startActivityForResult(intent, Config.RC_PICK_IMAGES);
            } else {
                intent = new Intent(activity, CameraActivity.class);
                intent.putExtra(Config.EXTRA_CONFIG, config);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                activity.overridePendingTransition(0, 0);
                activity.startActivityForResult(intent, Config.RC_PICK_IMAGES);
            }
        }
    }

    public static Builder with(AppCompatActivity activity) {
        return new ActivityBuilder(activity);
    }
}
