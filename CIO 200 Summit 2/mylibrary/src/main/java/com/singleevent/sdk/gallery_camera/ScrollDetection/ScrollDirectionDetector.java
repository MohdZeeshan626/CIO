package com.singleevent.sdk.gallery_camera.ScrollDetection;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.View;

/**
 * Created by webMOBI on 9/6/2017.
 */

public class ScrollDirectionDetector {

    private static final String TAG = ScrollDirectionDetector.class.getSimpleName();

    private final OnDetectScrollListener mOnDetectScrollListener;

    private int mOldTop;
    private int mOldFirstVisibleItem;

    private ScrollDirection mOldScrollDirection = null;

    public ScrollDirectionDetector(OnDetectScrollListener onDetectScrollListener) {
        mOnDetectScrollListener = onDetectScrollListener;
    }

    public interface OnDetectScrollListener {
        void onScrollDirectionChanged(ScrollDirection scrollDirection);
    }

    public enum ScrollDirection {
        UP, DOWN
    }

    public void onDetectedListScroll(GridLayoutManager layoutManager, int firstVisibleItem) {

        View view = layoutManager.getChildAt(0);
        int top = (view == null) ? 0 : view.getTop();

        if (firstVisibleItem == mOldFirstVisibleItem) {
            if (top > mOldTop) {
                onScrollUp();
            } else if (top < mOldTop) {
                onScrollDown();
            }
        } else {
            if (firstVisibleItem < mOldFirstVisibleItem) {
                onScrollUp();
            } else {
                onScrollDown();
            }
        }

        mOldTop = top;
        mOldFirstVisibleItem = firstVisibleItem;
    }

    public void onDetectedListScroll(LinearLayoutManager layoutManager, int firstVisibleItem) {

        View view = layoutManager.getChildAt(0);
        int top = (view == null) ? 0 : view.getTop();

        if (firstVisibleItem == mOldFirstVisibleItem) {
            if (top > mOldTop) {
                onScrollUp();
            } else if (top < mOldTop) {
                onScrollDown();
            }
        } else {
            if (firstVisibleItem < mOldFirstVisibleItem) {
                onScrollUp();
            } else {
                onScrollDown();
            }
        }

        mOldTop = top;
        mOldFirstVisibleItem = firstVisibleItem;
    }

    private void onScrollDown() {

        if (mOldScrollDirection != ScrollDirection.DOWN) {
            mOldScrollDirection = ScrollDirection.DOWN;
            mOnDetectScrollListener.onScrollDirectionChanged(ScrollDirection.DOWN);
        }
    }

    private void onScrollUp() {

        if (mOldScrollDirection != ScrollDirection.UP) {
            mOldScrollDirection = ScrollDirection.UP;
            mOnDetectScrollListener.onScrollDirectionChanged(ScrollDirection.UP);
        }
    }
}

