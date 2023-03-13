package com.singleevent.sdk.gallery_camera.Gallery;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.singleevent.sdk.gallery_camera.Gallery_Model.Image;

import java.io.File;
import java.util.List;

/**
 * Created by webMOBI on 9/5/2017.
 */

public class GalleryPresenterImpl implements GalleryPresenter, GalleryInteractor.OnLoadFinishedListener {


    private GalleryView galleryview;
    private GalleryInteractorImpl galleryInteractor;
    Context context;
    private Handler handler = new Handler(Looper.getMainLooper());


    public GalleryPresenterImpl(GalleryView galleryview, Context context) {
        this.galleryview = galleryview;
        this.context = context;
        this.galleryInteractor = new GalleryInteractorImpl();
    }


    public void abortLoading() {
        galleryInteractor.abortLoadImages();
    }


    @Override
    public void loadImages() {

        if (galleryview != null) {
            galleryview.showLoading(true);
        }

        galleryInteractor.loadDeviceImages(context, this);


    }


    @Override
    public void onImageLoaded(final List<Image> images) {

        handler.post(new Runnable() {
            @Override
            public void run() {

                if (galleryview != null) {
                    galleryview.showFetchCompleted(images);
                    final boolean isEmpty = images.isEmpty();
                    if (isEmpty) {
                        galleryview.showEmpty();
                    } else {
                        galleryview.showLoading(false);
                    }
                }
            }
        });


    }

    @Override
    public void onFailed(final Throwable throwable) {

        handler.post(new Runnable() {
            @Override
            public void run() {
                if (galleryview != null) {
                    galleryview.showError(throwable);
                }
            }
        });

    }

    @Override
    public void onDoneSelectImages(List<Image> selectedImages) {
        if (selectedImages != null && !selectedImages.isEmpty()) {
            for (int i = 0; i < selectedImages.size(); i++) {
                Image image = selectedImages.get(i);
                File file = new File(image.getPath());
                if (!file.exists()) {
                    selectedImages.remove(i);
                    i--;
                }
            }
        }
        galleryview.finishPickImages(selectedImages);
    }
}
