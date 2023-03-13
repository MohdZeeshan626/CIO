package com.singleevent.sdk.health.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.singleevent.sdk.R;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ActiveChallengesFragment  extends Fragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.activity_challenge, container, false);

        return v;

    }
}