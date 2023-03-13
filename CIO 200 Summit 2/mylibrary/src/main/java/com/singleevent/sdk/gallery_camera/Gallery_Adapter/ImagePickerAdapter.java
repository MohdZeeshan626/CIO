package com.singleevent.sdk.gallery_camera.Gallery_Adapter;

import android.content.Context;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.singleevent.sdk.gallery_camera.Gallery_Model.Image;
import com.singleevent.sdk.gallery_camera.Helper.ImageHelper;
import com.singleevent.sdk.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by webMOBI on 9/5/2017.
 */

public class ImagePickerAdapter extends RecyclerView.Adapter<ImagePickerAdapter.MyViewHolder> {


    private List<Image> images = new ArrayList<>();
    private List<Image> selectedImages = new ArrayList<>();
    private OnImageClickListener itemClickListener;
    private OnImageSelectionListener imageSelectionListener;

    ImageLoader imageLoader;
    Context context;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        private FrameLayout container;
        private ImageView image, videoplay;
        private View alphaView;
        private View gifIndicator;

        public MyViewHolder(View itemView) {
            super(itemView);
            container = (FrameLayout) itemView;
            image = (ImageView) itemView.findViewById(R.id.image_thumbnail);
            alphaView = itemView.findViewById(R.id.view_alpha);
            gifIndicator = itemView.findViewById(R.id.gif_indicator);
            videoplay = (ImageView) itemView.findViewById(R.id.video);

        }
    }

    public ImagePickerAdapter(Context context, ImageLoader imageLoader, List<Image> selectedImages, OnImageClickListener itemClickListener) {
        this.imageLoader = imageLoader;
        this.context = context;
        this.itemClickListener = itemClickListener;


        if (selectedImages != null && !selectedImages.isEmpty()) {
            this.selectedImages.addAll(selectedImages);
        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.imagepicker_item_image, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder viewHolder, final int position) {
        final Image image = images.get(position);
        final boolean isSelected = isSelected(image);

        imageLoader.loadImage(image.getPath(), viewHolder.image);

        viewHolder.gifIndicator.setVisibility(ImageHelper.isGifFormat(image) ? View.VISIBLE : View.GONE);
        viewHolder.videoplay.setVisibility(ImageHelper.isVideoFormat(image) ? View.VISIBLE : View.GONE);

        viewHolder.alphaView.setAlpha(isSelected ? 0.5f : 0.0f);
        viewHolder.container.setForeground(isSelected ? ContextCompat.getDrawable(context, R.drawable.imagepicker_ic_selected) : null);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean shouldSelect = itemClickListener.onImageClick(view, viewHolder.getAdapterPosition(), !isSelected);
                if (isSelected) {
                    removeSelected(image, position);
                } else if (shouldSelect) {
                    addSelected(image, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return images.size();
    }


    private boolean isSelected(Image image) {
        for (Image selectedImage : selectedImages) {
            if (selectedImage.getPath().equals(image.getPath())) {
                return true;
            }
        }
        return false;
    }


    public void setData(List<Image> images) {
        if (images != null) {
            this.images.clear();
            this.images.addAll(images);
        }
        notifyDataSetChanged();
    }


    public List<Image> getSelectedImages() {
        return selectedImages;
    }

    public List<Image> getTotalImages() {
        return images;
    }


    public void addSelected(Image image, int position) {
        selectedImages.add(image);
        notifyItemChanged(position);
        notifySelectionChanged();
    }

    public void removeSelected(Image image, int position) {
        selectedImages.remove(image);
        notifyItemChanged(position);
        notifySelectionChanged();
    }

    public void removeAllSelected() {
        selectedImages.clear();
        notifyDataSetChanged();
        notifySelectionChanged();
    }

    public void setOnImageSelectionListener(OnImageSelectionListener imageSelectedListener) {
        this.imageSelectionListener = imageSelectedListener;
    }

    private void notifySelectionChanged() {
        if (imageSelectionListener != null) {
            imageSelectionListener.onSelectionUpdate(selectedImages);
        }
    }


}
