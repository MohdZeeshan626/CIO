package com.singleevent.sdk.View.RightActivity.admin.checkin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.ViewGroup;

import com.singleevent.sdk.R;

import java.util.ArrayList;

import me.dm7.barcodescanner.core.IViewFinder;
import me.dm7.barcodescanner.zbar.BarcodeFormat;
import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

public class SimpleScannerActivity extends AppCompatActivity implements ZBarScannerView.ResultHandler {

    private static final int ZBAR_CAMERA_PERMISSION = 12;
    private ZBarScannerView mScannerView;


    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_simple_scanner);
        ViewGroup contentFrame = (ViewGroup) findViewById(R.id.content_frame);

        ArrayList<BarcodeFormat> list = new ArrayList<>();

        list.add(BarcodeFormat.CODE128);
        list.add(BarcodeFormat.CODE39);
        list.add(BarcodeFormat.CODE93);
        list.add(BarcodeFormat.QRCODE);


        // for full screen scanner ui
        mScannerView = new ZBarScannerView(this) {
            /**
             * this will alter the scanning area
             **/
            @Override
            protected IViewFinder createViewFinderView(Context context) {
                return new CustomViewFinderView(context);
            }
        };



        mScannerView.setAutoFocus(true);
        mScannerView.setFormats(list);
        contentFrame.addView(mScannerView);

    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result rawResult) {

        Intent intent = new Intent();
        intent.putExtra("content", rawResult.getContents());
        setResult(RESULT_OK, intent);
        finish();

    }


}
