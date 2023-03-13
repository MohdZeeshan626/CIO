package com.singleevent.sdk.View.Fragment.Left_Fragment;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


import com.singleevent.sdk.Left_Adapter.floorlistadapter;
import com.singleevent.sdk.model.Agenda.Agendadetails;
import com.singleevent.sdk.model.Events;
import com.singleevent.sdk.model.Map.Mapfloorplan;
import com.singleevent.sdk.R;

import java.util.ArrayList;

import io.paperdb.Paper;


public class Floorfragment extends Fragment {
    Events e;
    ListView floorlistview;
    ArrayList<Mapfloorplan> floorplans = new ArrayList<>();
    Context context;
    int pos;
    boolean isAgenda;
    private ArrayList<Events> events = new ArrayList<Events>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        Paper.init(getActivity());
        events = Paper.book().read("Appevents");
        e = events.get(0);
        Bundle args = getArguments();
        pos = args.getInt("pos");
        isAgenda = args.getBoolean("isAgenda",false);

        floorplans.clear();

        if (isAgenda){

            Agendadetails items = (Agendadetails) args.getSerializable("AgendaDetails");

            if (items.getLocation_detail().getFloorsSize() > 0){

                for (int j = 0; j < items.getLocation_detail().getFloorsSize(); j++){

                    floorplans.add(items.getLocation_detail().getFloors(j));
                }
            }

        }else {
            if (e.getTabs(pos).getFloorsSize() > 0) {
                for (int j = 0; j < e.getTabs(pos).getFloorsSize(); j++) {
                    floorplans.add(e.getTabs(pos).getFloors(j));
                }
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.s_agendadays, container, false);
        floorlistview = (ListView) view.findViewById(R.id.listView);
        floorlistview.setAdapter(new floorlistadapter(getActivity(), floorplans));
        return view;
    }
}
