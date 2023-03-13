package com.webmobi.gecmedia.Views.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.webmobi.gecmedia.R;
import com.webmobi.gecmedia.Views.HelpActivity;
import com.webmobi.gecmedia.Views.PopularActivity;
import com.webmobi.gecmedia.Views.Profile;
import com.webmobi.gecmedia.Views.RegActivity;
import com.webmobi.gecmedia.Views.WishList;

import io.paperdb.Paper;

import static android.app.Activity.RESULT_OK;

/**
 * Created by webMOBI on 12/6/2017.
 */

public class MoreFragment extends Fragment implements View.OnClickListener {

    private RelativeLayout relativelayout_myprofile, relativelayout_popular,
            relativelayout_favorite_events, relativelayout_help;
    ImageView user_login;
    Context context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.more_fragment, container, false);
        context = getActivity();
        relativelayout_myprofile = (RelativeLayout) view.findViewById(R.id.relativelayout_myprofile);
        relativelayout_popular = (RelativeLayout) view.findViewById(R.id.relativelayout_popular);
        relativelayout_help = (RelativeLayout) view.findViewById(R.id.relativelayout_help);
        relativelayout_favorite_events = (RelativeLayout) view.findViewById(R.id.relativelayout_favorite_events);
        user_login = (ImageView) view.findViewById(R.id.title_right_ic);
        relativelayout_myprofile.setOnClickListener(this);
        relativelayout_popular.setOnClickListener(this);
        relativelayout_favorite_events.setOnClickListener(this);
        relativelayout_help.setOnClickListener(this);
        user_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userLogin();
            }
        });
        return view;
    }

    private void userLogin() {
        Intent i;

        if (Paper.book().read("Islogin", false)) {

//            i = new Intent(context, NewProfile.class);
            i = new Intent(context, Profile.class);
            i.setAction(com.webmobi.gecmedia.Config.ApiList.loginaction);
            startActivity(i);
        } else {
            i = new Intent(context, RegActivity.class);
            i.setAction(com.webmobi.gecmedia.Config.ApiList.loginaction);
            startActivityForResult(i, 40);
        }
    }

    @Override
    public void onClick(View v) {
        Intent i;
        switch (v.getId()) {

            case R.id.relativelayout_myprofile:
                i = new Intent(getActivity(), Profile.class);
//                i = new Intent(getActivity(), NewProfile.class);
                startActivityForResult(i, 41);
                break;
            case R.id.relativelayout_popular:
                i = new Intent(getActivity(), PopularActivity.class);
                startActivity(i);
                break;
            case R.id.relativelayout_favorite_events:

                i = new Intent(getActivity(), WishList.class);
                startActivity(i);

                break;
            case R.id.relativelayout_help:
                i = new Intent(getActivity(), HelpActivity.class);
                startActivity(i);
                break;

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        switch (requestCode) {

            case 41:
                if (resultCode == RESULT_OK) {
                    Intent intent = new Intent(getActivity(), RegActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    getActivity().finish();
                }
                break;

            default:
                super.onActivityResult(requestCode, resultCode, data);


        }
    }
}
