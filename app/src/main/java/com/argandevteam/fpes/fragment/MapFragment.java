package com.argandevteam.fpes.fragment;


import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.argandevteam.fpes.R;
import com.argandevteam.fpes.activity.MainActivity;
import com.argandevteam.fpes.model.Centre;
import com.google.android.gms.maps.CameraUpdate;
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
public class MapFragment extends Fragment implements OnMapReadyCallback, LocationListener, GoogleMap.OnInfoWindowClickListener {

    private static final String TAG = "MapFragment";
    private static final int MY_PERMISSIONS_LOCATION = 1;
    GoogleMap map;
    @BindView(R.id.mapView)
    MapView mapView;
    LocationManager locationManager;
    private DatabaseReference mDatabase;
    private ArrayList<Centre> myList;

    public MapFragment() {
        // Required empty public constructor
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public void onResume() {
        super.onResume();

//        if (checkPermissionsLocation()) {
//            if (ContextCompat.checkSelfPermission(getContext(),
//                    Manifest.permission.ACCESS_FINE_LOCATION)
//                    == PackageManager.PERMISSION_GRANTED) {
//
//                //Request location updates:
//                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 400, 1, this);
//            }
//        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_map, container, false);

        ButterKnife.bind(this, view);

        checkPermissionsLocation();

        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mapView.getMapAsync(this);
        mDatabase = FirebaseDatabase.getInstance().getReference();


        return view;
    }

    private boolean checkPermissionsLocation() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.READ_CONTACTS)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(getContext()).setTitle("T").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{Manifest.permission.READ_CONTACTS},
                                MY_PERMISSIONS_LOCATION);
                    }
                }).create().show();

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.READ_CONTACTS},
                        MY_PERMISSIONS_LOCATION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
            return false;
        } else {
            return true;
        }
    }


    public Double[] stringToCoord(List<Centre> centres) {
        Geocoder geoCoder = new Geocoder(getContext(), Locale.getDefault());
        Double lat = null, lon = null;
        try {
            for (Centre centre : centres) {
                String address = centre.address + " " + centre.municipality + " " + centre.province;
                List<Address> addresses = geoCoder.getFromLocationName(address, 5);
                if (addresses.size() > 0) {
                    lat = (double) (addresses.get(0).getLatitude());
                    lon = (double) (addresses.get(0).getLongitude());

                    Log.d("lat-long", "" + lat + "......." + lon);
                    final LatLng user = new LatLng(lat, lon);
                    /*used marker for show the location */
                    mDatabase.child("centres").child(String.valueOf(centre.id - 1)).child("lat").setValue(lat);
                    mDatabase.child("centres").child(String.valueOf(centre.id - 1)).child("lon").setValue(lon);
                }
            }
        } catch (IOException e) {
            Log.d(TAG, "stringToCoord: " + e.getMessage());
        }
        return new Double[]{lat, lon};
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        map = googleMap;


        myList = new ArrayList<Centre>();
        final ArrayList<Double[]> coordArr = new ArrayList<>();
        map.setOnInfoWindowClickListener(this);
        mDatabase.child("centres").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot centreSnapshot : dataSnapshot.getChildren()) {
                    Centre centre = centreSnapshot.getValue(Centre.class);
                    myList.add(centre);
                    Log.d(TAG, "onDataChange: " + centreSnapshot.getValue());
                    Marker marker = map.addMarker(new MarkerOptions()
                            .position(new LatLng(centre.lat, centre.lon))
                            .snippet("Dirección: " + centre.address)
                            .title(centre.specific_den));
                    marker.setTag(centre);

                }
                LatLng coordinate = new LatLng(40.4212748, -3.7523294);
                CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(coordinate, 8.5f);
                map.animateCamera(yourLocation);
                //    coordArr.add(stringToCoord(myList));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        googleMap.setMyLocationEnabled(true);
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Centre centreMarker = (Centre) marker.getTag();

        DetailsFragment detailsFragment = new DetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("centre", centreMarker);

        detailsFragment.setArguments(bundle);

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, detailsFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
