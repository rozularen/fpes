package com.argandevteam.fpes.fragment;


import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.argandevteam.fpes.R;
import com.argandevteam.fpes.model.Centre;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class CentreMapFragment extends Fragment implements OnMapReadyCallback {

    private static final String TAG = "CentreMapFragment";
    GoogleMap map;
    @BindView(R.id.mapView)
    MapView mapView;
    private DatabaseReference mDatabase;
    private ArrayList<Centre> myList;

    public CentreMapFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        ButterKnife.bind(this, view);

        mapView.onCreate(savedInstanceState);

        mapView.onResume();

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mapView.getMapAsync(this);
        final ArrayList<Double[]> coordArr = new ArrayList<>();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("centres").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot centreSnapshot : dataSnapshot.getChildren()) {
                    Centre centre = centreSnapshot.getValue(Centre.class);
                    coordArr.add(stringToCoord(centre.address));

                    Log.d(TAG, "onDataChange: " + centreSnapshot.getValue());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        return view;
    }


    public Double[] stringToCoord(String address){
        Geocoder geoCoder = new Geocoder(getContext(), Locale.getDefault());
        Double lat = null, lon = null;
        try
        {
            List<Address> addresses = geoCoder.getFromLocationName(address, 5);
            if (addresses.size() > 0)
            {
                lat = (double) (addresses.get(0).getLatitude());
                lon = (double) (addresses.get(0).getLongitude());

                Log.d("lat-long", "" + lat + "......." + lon);
                final LatLng user = new LatLng(lat, lon);
        /*used marker for show the location */

            }
        }
        catch (IOException e)
        {
            Log.d(TAG, "stringToCoord: "+e.getMessage());
        }
        return new Double[]{lat, lon};
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;


    }
}
