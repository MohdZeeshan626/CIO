package com.singleevent.sdk.View.Fragment.Left_Fragment;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import com.google.android.gms.maps.model.MarkerOptions;
import com.singleevent.sdk.model.AppDetails;
import com.singleevent.sdk.R;


import io.paperdb.Paper;


public class Mapstaticview  extends SupportMapFragment implements OnMapReadyCallback {
    protected GoogleMap map;
    String venue, des;
    double lat, lng;
    TextView txtvenue, txtdes, textgetdirection;
    private RelativeLayout layout;
    ScrollView scroll;
    LinearLayout footerdes;
    AppDetails d;
    private float dpWidth;
    RelativeLayout r1;
    public static final String TAG="ContentValues";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Paper.init(getActivity());
        Bundle b = getArguments();
        d = Paper.book().read("Appdetails");
        DisplayMetrics displayMetrics = getActivity().getResources().getDisplayMetrics();
        dpWidth = displayMetrics.widthPixels * 0.27F;
        if (b != null) {
            venue = b.getString("venue");
           // des = b.getString("des");
            lat = b.getDouble("lat");
            lng = b.getDouble("lng");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        layout = (RelativeLayout) inflater.inflate(R.layout.s_fragment_venue,
                container, false);
       footerdes = (LinearLayout) layout.findViewById(R.id.footerdescription);
        footerdes.setVisibility(View.GONE);
        scroll = (ScrollView) layout.findViewById(R.id.scrool);
        r1=(RelativeLayout)layout.findViewById(R.id.r1);
        r1.setVisibility(View.GONE);
        int height = (int) dpWidth;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height);
//        params.addRule(RelativeLayout.ABOVE,R.id.getdirection);
        scroll.setLayoutParams(params);

       // txtvenue = (TextView) layout.findViewById(R.id.venue);
      //  txtdes = (TextView) layout.findViewById(R.id.description);
       // textgetdirection = (TextView) layout.findViewById(R.id.getdirection);
        //textgetdirection.setBackgroundColor(Color.parseColor(d.getTheme_color()));
        View v = super.onCreateView(inflater, container, savedInstanceState);
/*
        textgetdirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/?q=" + venue));
                startActivity(intent);
            }
        });
*/
        layout.addView(v, 0);
        return layout;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLngBounds ADELAIDE = new LatLngBounds(
                new LatLng(lat, lng), new LatLng(lat, lng));
        googleMap.setLatLngBoundsForCameraTarget(ADELAIDE);

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(lat, lng))
               // .bearing(0)
              //  .tilt(90)
                .zoom(15).build();

        //Zoom in and animate the camera.
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
//
//        CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(lat, lng));
//        CameraUpdate zoom = CameraUpdateFactory.zoomTo(14);
//        googleMap.moveCamera(center);
//        googleMap.animateCamera(zoom);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(new LatLng(lat, lng));
        markerOptions.title(venue);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        googleMap.addMarker(markerOptions);
      /*  txtvenue.setText(venue);
        txtdes.setText(Html.fromHtml(des));*/
    }
}
