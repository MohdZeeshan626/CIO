package com.singleevent.sdk.gallery_camera.ScrollDetection;

import android.app.LauncherActivity;
import android.view.View;

/**
 * Created by webMOBI on 9/6/2017.
 */

public class ListItemData {
    private static final String TAG = LauncherActivity.ListItem.class.getSimpleName();

    private Integer mIndexInAdapter;
    private View mView;

    private boolean mIsMostVisibleItemChanged;

    public int getIndex() {
        return mIndexInAdapter;
    }

    public View getView() {
        return mView;
    }

    public ListItemData fillWithData(int indexInAdapter, View view) {
        mIndexInAdapter = indexInAdapter;
        mView = view;
        return this;
    }

    public boolean isAvailable() {
        boolean isAvailable = mIndexInAdapter != null && mView != null;
        return isAvailable;
    }



    public void setMostVisibleItemChanged(boolean isDataChanged) {
        mIsMostVisibleItemChanged = isDataChanged;
    }

    public boolean isMostVisibleItemChanged() {
        return mIsMostVisibleItemChanged;
    }

    @Override
    public String toString() {
        return "ListItemData{" +
                "mIndexInAdapter=" + mIndexInAdapter +
                ", mView=" + mView +
                ", mIsMostVisibleItemChanged=" + mIsMostVisibleItemChanged +
                '}';
    }
}

