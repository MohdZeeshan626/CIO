package com.singleevent.sdk.health.Fragment;


import android.actionsheet.demo.com.khoiron.actionsheetiosforandroid.ActionSheet;
import android.actionsheet.demo.com.khoiron.actionsheetiosforandroid.Interface.ActionSheetCallBack;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.singleevent.sdk.health.Activity.CreateChallengeActivity;
import com.singleevent.sdk.health.Activity.JoinChallenge;
import com.singleevent.sdk.health.Adapter.RecylerViewActiveChallengeAdapter;
import com.singleevent.sdk.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static android.view.View.GONE;

/**
 * A simple {@link Fragment} subclass.
 */
public class ActiveFragment extends Fragment {

    RecyclerView activeRecylerView;


    private ArrayList<String> mPosition = new ArrayList<>();
    private ArrayList<String> mImage = new ArrayList<>();
    private ArrayList<String> mName = new ArrayList<>();
    private ArrayList<String> mTotal = new ArrayList<>();
    RecylerViewActiveChallengeAdapter recylerViewActiveChallengeAdapter;
    TextView txtActive;
    LinearLayout linearLayout;
    Button btn_create,btn_join,btn_active_option;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_active, container, false);
        activeRecylerView = v.findViewById(R.id.recyclerviewActiveChallenge);
        txtActive= (TextView)v.findViewById(R.id.txt_active) ;
        linearLayout = (LinearLayout)v.findViewById(R.id.linearActiveChallenge);
//        linearLayout.setVisibility(GONE);
        txtActive.setVisibility(GONE);
        btn_active_option = (Button)v.findViewById(R.id.btn_material_active);
        btn_create =(Button)v. findViewById(R.id.btncreate_challenge);
        btn_join =(Button)v.findViewById(R.id.btnjoin_challenge);
        btn_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getActivity(), JoinChallenge.class);
                startActivity(in);
            }
        });
        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent (getActivity(), CreateChallengeActivity.class);
                startActivity(in);
            }
        });
        ArrayList<String> data = new ArrayList<>();

        data.add("View Challenge Details");
        data.add("Leave Challenge");
        btn_active_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ActionSheet(getContext(),data)
                        .setTitle("Challenge Options")
                        .setCancelTitle("Cancel")
                        .setColorTitle(Color.BLUE)
                        .setColorTitleCancel(Color.RED)
                        .setColorData(Color.BLUE)
                        .create(new ActionSheetCallBack() {
                            @Override
                            public void data(@NotNull String data, int position) {
                                switch (position){
                                    case 0:

                                        // your action
                                    case 1:
                                        // your action
                                    case 2:
                                        // your action
                                    case 3:
                                        // your action
                                }
                            }
                        });
            }
        });
        initAddData();



        return v;

    }

    private void initAddData() {
        System.out.println("calling function initAddData");
        mPosition.add("1 ");
        mImage.add("https://pixinvent.com/materialize-material-design-admin-template/app-assets/images/user/12.jpg");
        mName.add("Amanda");
        mTotal.add("175");

         mPosition.add("2");
        mImage.add("https://pixinvent.com/materialize-material-design-admin-template/app-assets/images/user/12.jpg");
        mName.add("Amanda");
        mTotal.add("785");

        mPosition.add("3 ");
        mImage.add("https://pixinvent.com/materialize-material-design-admin-template/app-assets/images/user/12.jpg");
        mName.add("Amanda");
        mTotal.add("795");


        mPosition.add("4 ");

        mImage.add("https://pixinvent.com/materialize-material-design-admin-template/app-assets/images/user/12.jpg");
        mName.add("Amanda");
        mTotal.add("795");

        initRecylcer();
    }

    private void initRecylcer() {
        System.out.println("calling function initRecycler");
        recylerViewActiveChallengeAdapter = new RecylerViewActiveChallengeAdapter(mPosition,mImage,mName,mTotal,getContext());
        activeRecylerView.setAdapter(recylerViewActiveChallengeAdapter);
        activeRecylerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recylerViewActiveChallengeAdapter.notifyDataSetChanged();
    }

}
