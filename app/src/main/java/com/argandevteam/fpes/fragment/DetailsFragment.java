package com.argandevteam.fpes.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ScrollView;

import com.argandevteam.fpes.R;
import com.argandevteam.fpes.adapter.ReviewsAdapter;
import com.argandevteam.fpes.model.Centre;
import com.argandevteam.fpes.model.Review;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsFragment extends Fragment implements OnMapReadyCallback{

    Centre centre;
    ReviewsAdapter reviewsAdapter;
    ArrayList<Review> reviewsList;
    @BindView(R.id.reviewList)
    ListView lvReview;
    MapView mapView;


    public DetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        centre = args.getParcelable("centre");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_details, container, false);
        ButterKnife.bind(this, view);


        getActivity().setTitle("Detalles");

        ViewGroup listViewHeader = (ViewGroup) inflater.inflate(R.layout.list_view_header, lvReview, false);
        lvReview.addHeaderView(listViewHeader);

        mapView = (MapView) listViewHeader.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mapView.getMapAsync(this);

        ViewGroup listViewFooter = (ViewGroup) inflater.inflate(R.layout.list_view_footer, lvReview, false);
        lvReview.addFooterView(listViewFooter);


        lvReview.setDivider(null);
        reviewsList = new ArrayList<>();

        reviewsList.add(new Review());
        reviewsList.add(new Review());
        reviewsAdapter = new ReviewsAdapter(getContext(), reviewsList);
        lvReview.setAdapter(reviewsAdapter);
        lvReview.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }
}
