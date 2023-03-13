package com.singleevent.sdk.gallery_camera.Gallery;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.singleevent.sdk.gallery_camera.Gallery_Adapter.OnImageClickListener;
import com.singleevent.sdk.gallery_camera.Gallery_Adapter.OnImageSelectionListener;
import com.singleevent.sdk.gallery_camera.Gallery_Adapter.RecyclerViewManager;
import com.singleevent.sdk.gallery_camera.Gallery_Model.Config;
import com.singleevent.sdk.gallery_camera.Gallery_Model.Image;
import com.singleevent.sdk.gallery_camera.Helper.PermissionHelper;
import com.singleevent.sdk.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by webMOBI on 9/5/2017.
 */

public class GalleryActivity extends AppCompatActivity implements GalleryView {

    private Config config;
    private RecyclerView recyclerView;
    private View emptyLayout;
    private ProgressBar progressWheel;
    private GalleryPresenterImpl presenter;
    private RecyclerViewManager recyclerViewManager;
    private Toolbar toolbar;
    private TextView done;
    private int mScrollState = AbsListView.OnScrollListener.SCROLL_STATE_IDLE;



    private OnImageClickListener imageClickListener = new OnImageClickListener() {
        @Override
        public boolean onImageClick(View view, int position, boolean isSelected) {
            return recyclerViewManager.selectImage();
        }
    };

    private View.OnClickListener doneClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            onDone();
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (intent == null) {
            finish();
            return;
        }

        config = intent.getParcelableExtra(Config.EXTRA_CONFIG);

        setContentView(R.layout.imagepicker_activity_picker);
        setupView();
        setupComponents();
        setupToolbar();
    }

    private void setupToolbar() {
        toolbar.setBackgroundColor(config.getToolbarColor());
        setSupportActionBar(toolbar);
        if (!config.isMultipleMode())
            done.setVisibility(View.GONE);
        else
            done.setOnClickListener(doneClickListener);


    }


    private void setupView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        progressWheel = (ProgressBar) findViewById(R.id.progressWheel);
        emptyLayout = findViewById(R.id.layout_empty);
        done = (TextView) findViewById(R.id.filterapply);
    }


    private void setupComponents() {
        recyclerViewManager = new RecyclerViewManager(recyclerView, config, getResources().getConfiguration().orientation);
        recyclerViewManager.setupAdapters(imageClickListener);
        recyclerViewManager.setOnImageSelectionListener(new OnImageSelectionListener() {
            @Override
            public void onSelectionUpdate(List<Image> images) {
                invalidateToolbar();
                if (!config.isMultipleMode() && !images.isEmpty()) {
                    onDone();
                }
            }
        });


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int scrollState) {
                mScrollState = scrollState;
                if (scrollState == RecyclerView.SCROLL_STATE_IDLE && !recyclerViewManager.getTotalImages().isEmpty()) {

                    recyclerViewManager.onScrollStateIdle();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (!recyclerViewManager.getTotalImages().isEmpty()) {
                    recyclerViewManager.onScroll(mScrollState);
                }
            }
        });

        presenter = new GalleryPresenterImpl(GalleryActivity.this, this);
    }


    @Override
    public void showLoading(boolean isLoading) {

        progressWheel.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        recyclerView.setVisibility(isLoading ? View.GONE : View.VISIBLE);
        emptyLayout.setVisibility(View.GONE);
    }

    @Override
    public void showFetchCompleted(List<Image> images) {

        System.out.println("Images size " + images.size());
        recyclerViewManager.setImageAdapter(images, config.getImageTitle());
        invalidateToolbar();

    }

    @Override
    public void showError(Throwable throwable) {
        String message = getString(R.string.imagepicker_error_unknown);
        if (throwable != null && throwable instanceof NullPointerException) {
            message = getString(R.string.imagepicker_error_images_not_exist);
        }
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showEmpty() {
        progressWheel.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        emptyLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void showCapturedImage() {

    }

    @Override
    public void finishPickImages(List<Image> images) {
        Intent data = new Intent();
        data.putParcelableArrayListExtra(Config.EXTRA_IMAGES, (ArrayList<? extends Parcelable>) images);
        setResult(RESULT_OK, data);
        finish();
    }


    @Override
    protected void onResume() {
        super.onResume();
        getDataWithPermission();
    }

    private void getDataWithPermission() {

        final String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        PermissionHelper.checkPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, new PermissionHelper.PermissionAskListener() {
            @Override
            public void onNeedPermission() {
                PermissionHelper.requestAllPermissions(GalleryActivity.this, permissions, Config.RC_WRITE_EXTERNAL_STORAGE_PERMISSION);
            }

            @Override
            public void onPermissionPreviouslyDenied() {
                PermissionHelper.requestAllPermissions(GalleryActivity.this, permissions, Config.RC_WRITE_EXTERNAL_STORAGE_PERMISSION);
            }

            @Override
            public void onPermissionDisabled() {

            }

            @Override
            public void onPermissionGranted() {
                getData();
            }
        });
    }

    private void getData() {
        presenter.abortLoading();
        presenter.loadImages();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case Config.RC_WRITE_EXTERNAL_STORAGE_PERMISSION: {
                if (PermissionHelper.hasGranted(grantResults)) {
                    getData();
                    return;
                }
            }

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (presenter != null) {
            presenter.abortLoading();
        }

    }

    private void onDone() {
        presenter.onDoneSelectImages(recyclerViewManager.getSelectedImages());
    }


    private void invalidateToolbar() {
        toolbar.setTitle(recyclerViewManager.getTitle());
        done.setEnabled(recyclerViewManager.isShowDoneButton());

    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
