package com.singleevent.sdk.gallery_camera.Gallery_Adapter;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.singleevent.sdk.gallery_camera.Gallery_Model.Config;
import com.singleevent.sdk.gallery_camera.Gallery_Model.Image;
import com.singleevent.sdk.gallery_camera.ScrollDetection.ListItemData;
import com.singleevent.sdk.gallery_camera.ScrollDetection.ScrollDirectionDetector;
import com.singleevent.sdk.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by webMOBI on 9/5/2017.
 */

public class RecyclerViewManager implements ScrollDirectionDetector.OnDetectScrollListener {

    private Context context;
    private RecyclerView recyclerView;
    private Config config;

    private GridLayoutManager layoutManager;

    private ImagePickerAdapter imageAdapter;

    private int imageColumns;

    private ImageLoader imageLoader;
    private String title;
    private GridSpacingItemDecoration itemOffsetDecoration;

    /**
     * Initial scroll direction should be UP in order to set as active most top item if no active item yet
     */
    private ScrollDirectionDetector.ScrollDirection mScrollDirection = ScrollDirectionDetector.ScrollDirection.UP;
    ScrollDirectionDetector mScrollDirectionDetector;
    private final Rect mCurrentViewRect = new Rect();
    private final ListItemData mCurrentItem = new ListItemData();



    public RecyclerViewManager(RecyclerView recyclerView, Config config, int orientation) {
        this.recyclerView = recyclerView;
        this.config = config;
        context = recyclerView.getContext();
        changeOrientation(orientation);
        imageLoader = new ImageLoader();
        mScrollDirectionDetector = new ScrollDirectionDetector(this);
    }


    /**
     * Set item size, column size base on the screen orientation
     */
    public void changeOrientation(int orientation) {
        imageColumns = orientation == Configuration.ORIENTATION_PORTRAIT ? 3 : 5;

        int columns = imageColumns;
        layoutManager = new GridLayoutManager(context, columns);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        setItemDecoration(columns);

    }

    private void setItemDecoration(int columns) {
        if (itemOffsetDecoration != null) {
            recyclerView.removeItemDecoration(itemOffsetDecoration);
        }
        itemOffsetDecoration = new GridSpacingItemDecoration(columns, context.getResources().getDimensionPixelSize(R.dimen.imagepicker_item_padding), false);
        recyclerView.addItemDecoration(itemOffsetDecoration);
        layoutManager.setSpanCount(columns);
    }


    public void setupAdapters(OnImageClickListener imageClickListener) {
        ArrayList<Image> selectedImages = null;
        if (config.isMultipleMode() && !config.getSelectedImages().isEmpty()) {
            selectedImages = config.getSelectedImages();
        }

        imageAdapter = new ImagePickerAdapter(context, imageLoader, selectedImages, imageClickListener);
    }


    public void setImageAdapter(List<Image> images, String title) {
        imageAdapter.setData(images);
        setItemDecoration(imageColumns);
        recyclerView.setAdapter(imageAdapter);
        this.title = title;
    }


    public boolean selectImage() {
        if (config.isMultipleMode()) {
            if (imageAdapter.getSelectedImages().size() >= config.getMaxSize()) {
                String message = String.format(context.getString(R.string.imagepicker_msg_limit_images), config.getMaxSize());
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {
            if (imageAdapter.getItemCount() > 0) {
                imageAdapter.removeAllSelected();
            }
        }
        return true;
    }

    public List<Image> getSelectedImages() {
        checkAdapterIsInitialized();
        return imageAdapter.getSelectedImages();
    }


    public List<Image> getTotalImages() {
        checkAdapterIsInitialized();
        return imageAdapter.getTotalImages();
    }

    private void checkAdapterIsInitialized() {
        if (imageAdapter == null) {
            throw new IllegalStateException("Must call setupAdapters first!");
        }
    }

    public void setOnImageSelectionListener(OnImageSelectionListener imageSelectionListener) {
        checkAdapterIsInitialized();
        imageAdapter.setOnImageSelectionListener(imageSelectionListener);
    }


    public String getTitle() {

        return config.getImageTitle();

    }

    public boolean isShowDoneButton() {
        return config.isMultipleMode() && imageAdapter.getSelectedImages().size() > 0;
    }


    public void onScrollStateIdle() {

        calculateMostVisibleItem();

    }

    private void calculateMostVisibleItem() {

        ListItemData mockCurrentItemData;
        switch (mScrollDirection) {
            case UP:
                int lastVisibleItemIndex;
                if (layoutManager.findLastVisibleItemPosition() < 0/*-1 may be returned from ListView*/) {
                    lastVisibleItemIndex = layoutManager.findFirstVisibleItemPosition();
                } else {
                    lastVisibleItemIndex = layoutManager.findLastVisibleItemPosition();
                }

                mockCurrentItemData = new ListItemData().fillWithData(lastVisibleItemIndex, layoutManager.getChildAt(layoutManager.getChildCount() - 1));
                break;
            case DOWN:
                mockCurrentItemData = new ListItemData().fillWithData(layoutManager.findFirstVisibleItemPosition(), layoutManager.getChildAt(0/*first visible*/));
                break;
            default:
                throw new RuntimeException("not handled mScrollDirection " + mScrollDirection);
        }

        int maxVisibilityPercents = getVisibilityPercents(mockCurrentItemData.getView());


    }

    public void onScroll(int mScrollState) {
        mScrollDirectionDetector.onDetectedListScroll(layoutManager, layoutManager.findFirstVisibleItemPosition());
    }

    @Override
    public void onScrollDirectionChanged(ScrollDirectionDetector.ScrollDirection scrollDirection) {
        this.mScrollDirection = scrollDirection;
    }


    public int getVisibilityPercents(View currentView) {

        int percents = 100;

        currentView.getLocalVisibleRect(mCurrentViewRect);

        int height = currentView.getHeight();

        if (viewIsPartiallyHiddenTop()) {
            // view is partially hidden behind the top edge
            percents = (height - mCurrentViewRect.top) * 100 / height;
        } else if (viewIsPartiallyHiddenBottom(height)) {
            percents = mCurrentViewRect.bottom * 100 / height;
        }

        return percents;
    }

    private boolean viewIsPartiallyHiddenBottom(int height) {
        return mCurrentViewRect.bottom > 0 && mCurrentViewRect.bottom < height;
    }

    private boolean viewIsPartiallyHiddenTop() {
        return mCurrentViewRect.top > 0;
    }



}

