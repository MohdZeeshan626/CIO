package com.singleevent.sdk.View.Fragment.Left_Fragment;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by webMOBI on 9/26/2017.
 */

public class EmptyFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        TextView txt = new TextView(getActivity());
        txt.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        txt.setText("No Session");
        Typeface face = Typeface.createFromAsset(getActivity().getAssets(),
                "fonts/Roboto-Regular.otf");
        txt.setTypeface(face);
        txt.setGravity(Gravity.CENTER);
        txt.setTextColor(Color.parseColor("#000000"));
        return txt;
    }
}
