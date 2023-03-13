package com.singleevent.sdk.View.RightActivity;

import android.content.Context;
import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.singleevent.sdk.R;


public class ExportBottomSheetFragment extends BottomSheetDialogFragment {
    ClickAction clickAction;
    ConstraintLayout download_layout, share_layout, cancel_layout;
    Context context;

    public ExportBottomSheetFragment() {

    }
   /* public ExportBottomSheetFragment(Context context) {
        // Required empty public constructor
        this.context=context;
    }*/


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_export_bottom_sheet, container, false);
        download_layout = (ConstraintLayout) view.findViewById(R.id.btmsheet_download_layout);
        share_layout = (ConstraintLayout) view.findViewById(R.id.btmsheet_share_layout);
        cancel_layout = (ConstraintLayout) view.findViewById(R.id.btmsheet_cancel_layout);

        //listeners
        download_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickAction.onDownloadClick();
            }
        });

        cancel_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickAction.onCancel();
            }
        });

        share_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickAction.onShareClick();
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        clickAction = (ClickAction) context;

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public interface ClickAction {
        //to download
        public void onDownloadClick();
        //to share
        public void onShareClick();
        //to cancel
        public void onCancel();
    }

}
