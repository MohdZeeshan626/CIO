package com.singleevent.sdk.gallery_camera.Gallery;

import android.content.Context;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

import com.singleevent.sdk.gallery_camera.Gallery_Model.Image;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by webMOBI on 9/5/2017.
 */

public class GalleryInteractorImpl implements GalleryInteractor {


    private final String[] Imageprojection = new String[]{MediaStore.Images.Media._ID, MediaStore.Images.Media.DISPLAY_NAME, MediaStore.Images.Media.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME};
    private final String[] Videoprojection = new String[]{MediaStore.Video.Media._ID, MediaStore.Video.Media.DISPLAY_NAME, MediaStore.Video.Media.DATA, MediaStore.Video.Media.BUCKET_DISPLAY_NAME};
    String[] columns = {MediaStore.Files.FileColumns._ID, MediaStore.Files.FileColumns.DISPLAY_NAME, MediaStore.Files.FileColumns.DATA, MediaStore.Files.FileColumns.MEDIA_TYPE, MediaStore.Files.FileColumns.MIME_TYPE, MediaStore.Files.FileColumns.WIDTH, MediaStore.Files.FileColumns.SIZE};
    //    String selection = MediaStore.Files.FileColumns.MEDIA_TYPE + "=" + MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE + " OR " + MediaStore.Files.FileColumns.MEDIA_TYPE + "=" + MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;
    String selection = MediaStore.Files.FileColumns.MEDIA_TYPE + "=" + MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;

    private Context context;
    private ExecutorService executorService;

    BitmapFactory.Options options;


    private static File makeSafeFile(String path) {
        if (path == null || path.isEmpty()) {
            return null;
        }
        try {
            return new File(path);
        } catch (Exception ignored) {
            return null;
        }
    }


    @Override
    public void loadDeviceImages(Context context, OnLoadFinishedListener listener) {
        this.context = context;
        options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        getExecutorService().execute(new VideLoadRunnable(listener));

    }

    private ExecutorService getExecutorService() {
        if (executorService == null) {
            executorService = Executors.newSingleThreadExecutor();
        }
        return executorService;
    }

    public void abortLoadImages() {
        if (executorService != null) {
            executorService.shutdown();
            executorService = null;
        }
    }

    private class ImageLoadRunnable implements Runnable {

        private OnLoadFinishedListener listener;

        public ImageLoadRunnable(OnLoadFinishedListener listener) {
            this.listener = listener;
        }

        @Override
        public void run() {
            Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, Imageprojection,
                    null, null, MediaStore.Images.Media.DATE_ADDED + " DESC");

            if (cursor == null) {
                listener.onFailed(new NullPointerException());
                return;
            }

            List<Image> images = new ArrayList<>(cursor.getCount());

            if (cursor.moveToLast()) {
                do {
                    long id = cursor.getLong(cursor.getColumnIndex(Imageprojection[0]));
                    String name = cursor.getString(cursor.getColumnIndex(Imageprojection[1]));
                    String path = cursor.getString(cursor.getColumnIndex(Imageprojection[2]));
                    String bucket = cursor.getString(cursor.getColumnIndex(Imageprojection[3]));

                    File file = makeSafeFile(path);
                    if (file != null && file.exists()) {
                        Image image = new Image(id, name, path);
                        images.add(image);

                    }

                } while (cursor.moveToPrevious());
            }
            cursor.close();


            listener.onImageLoaded(images);
        }
    }


    private class VideLoadRunnable implements Runnable {

        private OnLoadFinishedListener listener;

        public VideLoadRunnable(OnLoadFinishedListener listener) {
            this.listener = listener;
        }

        @Override
        public void run() {
            Cursor cursor = context.getContentResolver().query(MediaStore.Files.getContentUri("external"), columns,
                    selection, null, MediaStore.Files.FileColumns.DATE_ADDED);

            if (cursor == null) {
                listener.onFailed(new NullPointerException());
                return;
            }

            List<Image> images = new ArrayList<>(cursor.getCount());

            if (cursor.moveToLast()) {
                do {

                    long id = cursor.getLong(0);
                    String name = cursor.getString(1);
                    String path = cursor.getString(2);
                    String type = cursor.getString(4);
                    Uri uri = Uri.parse(path);
                    try {
                        BitmapFactory.decodeFile(new File(uri.getPath()).getAbsolutePath(), options);
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    int imageHeight = options.outHeight;
                    int imageWidth = options.outWidth;

                    File file = makeSafeFile(path);
                    if (file != null && file.exists()) {
                        Image image = new Image(id, name, path, imageHeight, imageWidth,type);
                        images.add(image);

                    }

                } while (cursor.moveToPrevious());
            }
            cursor.close();


            listener.onImageLoaded(images);
        }
    }


}
