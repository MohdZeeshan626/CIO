package com.singleevent.sdk.health.Fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.singleevent.sdk.R;

import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class JoinDetailsFragment extends Fragment {


   TextView c_name,c_desc,c_date;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_joindetails, container, false);
        c_name=v.findViewById(R.id.c_name);
        c_desc=v.findViewById(R.id.c_desc);
        c_date=v.findViewById(R.id.c_date);



        return v;

    }



}
