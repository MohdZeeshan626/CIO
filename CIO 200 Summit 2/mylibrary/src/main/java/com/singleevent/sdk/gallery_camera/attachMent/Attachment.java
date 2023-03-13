package com.singleevent.sdk.gallery_camera.attachMent;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Parcelable;
import androidx.annotation.Nullable;

import android.provider.MediaStore;
import android.util.AttributeSet;


import com.singleevent.sdk.gallery_camera.Gallery_Model.Image;
import com.singleevent.sdk.R;

import java.util.ArrayList;import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.VideoView;


/**
 * Created by webMOBI on 9/7/2017.
 */

public class Attachment extends LinearLayout implements AttachmentView, View.OnClickListener {


    ArrayList<Image> itemList = new ArrayList<Image>();

    Context context;
    int width;
    int height;
    boolean iswidth = false;
    private LinearLayout MainView;

    private OnClickListener mOnClickListener;
    private OnTouchListener mOnTouchListener;
    private AttachmentImpl attachmentInteractor;


    public Attachment(Context context) {
        super(context);
        this.context = context;
        init(context);
    }

    public Attachment(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(context);
    }

    public Attachment(Context context, AttributeSet attrs,
                      int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        init(context);
    }

    public ArrayList<Image> getItemList() {
        return itemList;
    }

    public void setItemList(ArrayList<Image> itemList) {
        this.itemList.clear();
        this.itemList.addAll(itemList);
        if (itemList.size() > 0)
            attachmentInteractor.listofitems(this.itemList);
    }


    protected void onRestoreInstanceState(Parcelable state) {

       try {
           String id = this.getId() + " ";
           if (state instanceof Bundle) // implicit null check
           {
               Bundle bundle = (Bundle) state;
               this.itemList = bundle.getParcelableArrayList(id + "itemList");

               super.onRestoreInstanceState(state);
           }
       }catch (Exception e){}
    }

    protected Parcelable onSaveInstanceState() {
        super.onSaveInstanceState();
        String id = this.getId() + " ";
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(id + "itemList", this.itemList);
        return bundle;
    }

    private void init(Context context) {
        LayoutInflater.from(getContext()).inflate(R.layout.attachment_parent, this, true);
        MainView = (LinearLayout) findViewById(R.id.mainview11);
        final Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Point deviceDisplay = new Point();
        display.getSize(deviceDisplay);
        width = deviceDisplay.x;
        height=deviceDisplay.y;
        attachmentInteractor = new AttachmentImpl(this, context, width,height);
        setCustomTouchListener();
    }


    protected void setCustomTouchListener() {
        super.setOnTouchListener(new TouchListener());
    }


    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        mOnClickListener = l;
    }

    @Override
    public void Sorted(ArrayList<Image> itemList) {
        this.itemList = itemList;
    }



    @Override
    public void IsWidth(boolean iswidth) {
        this.iswidth = iswidth;
        loadimages();
    }


    @Override
    public void onClick(View view) {
        if (mOnClickListener != null) {
            mOnClickListener.onClick(view);
        }
    }


    //================================================================================
    // Inner classes
    //================================================================================
    private final class TouchListener implements OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    onTouchDown(event);
                    break;
                case MotionEvent.ACTION_UP:
                    onTouchUp(event);
                    break;
            }
            if (mOnTouchListener != null) {
                mOnTouchListener.onTouch(v, event);
            }
            return true;
        }
    }

    @Override
    public void setOnTouchListener(OnTouchListener onTouchListener) {
        mOnTouchListener = onTouchListener;
    }

    public OnTouchListener getOnTouchListener() {
        return mOnTouchListener;
    }

    private void onTouchDown(MotionEvent motionEvent) {

    }

    private void onTouchUp(MotionEvent motionEvent) {
        // Handle user defined click listeners
        if (mOnClickListener != null) {
            mOnClickListener.onClick(this);
        }
    }


    private void loadimages() {
        MainView.removeAllViews();
        MainView.setOrientation(VERTICAL);
        int size = itemList.size();


        if (iswidth) {
            setOrientation(VERTICAL);
            if (size == 1) {
                MainView.addView(attachmentInteractor.getViewone(AttachmentImpl.ViewType.isNormal, 0, itemList.size(), false));
            } else if (size == 2)
                MainView.addView(attachmentInteractor.getViewTwo(AttachmentImpl.ViewType.isHorizontal, 0, itemList.size(), false));
            else if (size == 3) {
                MainView.addView(attachmentInteractor.getViewone(AttachmentImpl.ViewType.isHorizontal, 0, 1, false));
                MainView.addView(attachmentInteractor.getViewTwo(AttachmentImpl.ViewType.isHorizontal, 1, itemList.size(), false));
            } else if (size == 4) {
                MainView.addView(attachmentInteractor.getViewone(AttachmentImpl.ViewType.isHorizontal, 0, 1, false));
                MainView.addView(attachmentInteractor.getViewThree(AttachmentImpl.ViewType.isHorizontal, 1, itemList.size(), false));
            } else if (size == 5) {
                MainView.addView(attachmentInteractor.getViewTwo(AttachmentImpl.ViewType.isHorizontal, 0, 2, false));
                MainView.addView(attachmentInteractor.getViewThree(AttachmentImpl.ViewType.isHorizontal, 2, itemList.size(), false));
            } else {
                MainView.addView(attachmentInteractor.getViewTwo(AttachmentImpl.ViewType.isHorizontal, 0, 2, false));
                MainView.addView(attachmentInteractor.getViewThree(AttachmentImpl.ViewType.isHorizontal, 2, 5, true));
            }

        } else {
            MainView.setOrientation(HORIZONTAL);
            if (size == 1)
                MainView.addView(attachmentInteractor.getViewone(AttachmentImpl.ViewType.isNormal, 0, itemList.size(), false));
            else if (size == 2)
                MainView.addView(attachmentInteractor.getViewTwo(AttachmentImpl.ViewType.isNormal, 0, itemList.size(), false));
            else if (size == 3) {
                MainView.addView(attachmentInteractor.getViewone(AttachmentImpl.ViewType.Isvertical, 0, 1, false));
                MainView.addView(attachmentInteractor.getViewTwo(AttachmentImpl.ViewType.Isvertical, 1, itemList.size(), false));
            } else if (size == 4) {
                MainView.addView(attachmentInteractor.getViewone(AttachmentImpl.ViewType.Isvertical, 0, 1, false));
                MainView.addView(attachmentInteractor.getViewThree(AttachmentImpl.ViewType.Isvertical, 1, itemList.size(), false));
            } else if (size == 5) {
                MainView.setOrientation(VERTICAL);
                MainView.addView(attachmentInteractor.getViewTwo(AttachmentImpl.ViewType.isHorizontal, 0, 2, false));
                MainView.addView(attachmentInteractor.getViewThree(AttachmentImpl.ViewType.isHorizontal, 2, itemList.size(), false));
            } else {
                MainView.setOrientation(VERTICAL);
                MainView.addView(attachmentInteractor.getViewTwo(AttachmentImpl.ViewType.isHorizontal, 0, 2, false));
                MainView.addView(attachmentInteractor.getViewThree(AttachmentImpl.ViewType.isHorizontal, 2, 5, true));
            }

        }

    }



}
