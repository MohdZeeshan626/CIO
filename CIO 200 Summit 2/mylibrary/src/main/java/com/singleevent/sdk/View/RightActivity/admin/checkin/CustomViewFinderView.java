package com.singleevent.sdk.View.RightActivity.admin.checkin;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import androidx.core.content.ContextCompat;

import com.singleevent.sdk.R;

import me.dm7.barcodescanner.core.ViewFinderView;

/**
 * Created by webMOBI on 4/30/2018.
 */

public class CustomViewFinderView extends ViewFinderView {
    Context context;

    public CustomViewFinderView(Context context) {
        super(context);
        this.context = context;
        setLaserEnabled(true);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (getFramingRect() != null) {
            updateFrameRect();
            setBorderColor(ContextCompat.getColor(context, R.color.sColorPrimary));
        }
    }

    private void updateFrameRect() {
        Point viewResolution = new Point(this.getWidth(), this.getHeight());
        int height = (int) ((float) this.getHeight());
        int width = (int) ((float) this.getWidth());

        int leftOffset = (viewResolution.x - width) / 2;
        int topOffset = (viewResolution.y - height) / 2;

        getFramingRect().left = leftOffset;
        getFramingRect().top = topOffset;
        getFramingRect().right = leftOffset + width;
        getFramingRect().bottom = topOffset + height;
    }
}
