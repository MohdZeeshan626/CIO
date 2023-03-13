package com.singleevent.sdk.health.Fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.singleevent.sdk.health.Adapter.JoinCreateRecyclerViewAdapter;
import com.singleevent.sdk.health.Adapter.RecyclerViewParticipantsAdapter;
import com.singleevent.sdk.R;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * A simple {@link Fragment} subclass.
 */
public class JoinParticipantsFragment extends Fragment {


    private ArrayList<String> mPartciImage = new ArrayList<>();
    private ArrayList<String> mPartciName = new ArrayList<>();
    RecyclerView recyclerViewParticipants;
    RecyclerViewParticipantsAdapter recyclerViewParticipantsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_joinparticipants, container, false);
        recyclerViewParticipants = (RecyclerView)v.findViewById(R.id.recyclerview_Participants);

        AddData();
        return v;

    }

    private void AddData(){

        mPartciImage.add("https://image.shutterstock.com/image-photo/headshot-cute-asian-woman-professional-600w-518624602.jpg");
        mPartciName.add("Mich");

        mPartciImage.add("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcSdDgPwZ3krgLxmINFKjpfKUuifilrMy7uGQAVh78KuSyeWtrsd&usqp=CAU");
        mPartciName.add("Joe");

        mPartciImage.add("https://images.pexels.com/photos/937481/pexels-photo-937481.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500");
        mPartciName.add("Mitch");

        mPartciImage.add("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcRYZw18uFgK7AItbKqm3jsCV4FZa8hlZzk4B3wS4d3Ha9yglr9R&usqp=CAU");
        mPartciName.add("Ken");

        mPartciImage.add("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcSoQn-bbaB0km4fA77fECxNQ7ybULs05FG9JNvP__-rfYhAvcvp&usqp=CAU");
        mPartciName.add("Selena");


        initRecyclerViewP();
    }

    private void initRecyclerViewP()
    {
        recyclerViewParticipantsAdapter = new RecyclerViewParticipantsAdapter(mPartciImage,mPartciName,getContext());
        recyclerViewParticipants.setAdapter(recyclerViewParticipantsAdapter);
        recyclerViewParticipants.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewParticipantsAdapter.notifyDataSetChanged();
    }

}
