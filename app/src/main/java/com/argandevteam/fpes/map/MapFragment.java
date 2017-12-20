package com.argandevteam.fpes.map;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.argandevteam.fpes.BaseFragment;
import com.argandevteam.fpes.R;
import com.argandevteam.fpes.centredetails.CentreDetailsFragment;
import com.argandevteam.fpes.centres.ListFragment;
import com.argandevteam.fpes.data.Centre;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends BaseFragment
        implements MapContract.View, OnMapReadyCallback, LocationListener, GoogleMap.OnInfoWindowClickListener {

    private static final String TAG = "MapFragment";
    private static final int MY_PERMISSIONS_LOCATION = 1;
    GoogleMap map;
    @BindView(R.id.mapView)
    MapView mapView;
    LocationManager locationManager;
    //    @BindView(R.id.btn_try)
//    Button btnTry;
    private DatabaseReference mDatabase;
    private ArrayList<Centre> myList;
    private MapContract.Presenter presenter;

    public MapFragment() {
        // Required empty public constructor
    }

    public static MapFragment newInstance() {
        return new MapFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreateView(inflater, container, savedInstanceState);

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
        mDatabase = FirebaseDatabase.getInstance().getReference();


        return view;
    }


    @Override
    public void onMapReady(final GoogleMap googleMap) {
        map = googleMap;
        map.clear();
        myList = new ArrayList<Centre>();
        final ArrayList<Double[]> coordArr = new ArrayList<>();
        map.setOnInfoWindowClickListener(this);

        int fineLocationPermission = ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION);
        if (fineLocationPermission != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        try {
            map.setMyLocationEnabled(true);
            int isLocationEnabled =
                    Settings.Secure.getInt(getContext().getContentResolver(), Settings.Secure.LOCATION_MODE);
            if (isLocationEnabled == 0) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
                dialogBuilder.setTitle("Activar Localizacion GPS")
                        .setMessage(
                                "Debe activar la localización GPS para que la aplicación funcione correctamente")
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FragmentManager supportFragmentManager = getActivity().getSupportFragmentManager();
                                ListFragment fragment = new ListFragment();
                                supportFragmentManager.beginTransaction()
                                        .replace(R.id.container, fragment, fragment.getTag())
                                        .commit();
                            }
                        })
                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent onGPS = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(onGPS);
                            }
                        })
                        .create()
                        .show();
            }
        } catch (SecurityException | Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        mDatabase.child("centres").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot centreSnapshot : dataSnapshot.getChildren()) {
                    Centre centre = centreSnapshot.getValue(Centre.class);
                    myList.add(centre);
                    Log.d(TAG, "onDataChange: " + centreSnapshot.getValue());
                    Marker marker = map.addMarker(
                            new MarkerOptions().position(new LatLng(centre.lat, centre.lon))
                                    .snippet("Dirección: " + centre.address)
                                    .title(centre.specific_den));
                    marker.setTag(centre);

                }
                LatLng coordinate = new LatLng(40.4212748, -3.7523294);
                CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(coordinate, 8.5f);
                map.animateCamera(yourLocation);
                Log.d(TAG, "onDataChange: HEEEEE");

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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

        CentreDetailsFragment centreDetailsFragment = new CentreDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("centre", centreMarker);

        centreDetailsFragment.setArguments(bundle);

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, centreDetailsFragment, centreDetailsFragment.getTag());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void setPresenter(MapContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public Context getViewContext() {
        return getContext();
    }

    @Override
    public Activity getViewActivity() {
        return getActivity();
    }
}
