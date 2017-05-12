package com.argandevteam.fpes.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.argandevteam.fpes.R;
import com.argandevteam.fpes.adapter.ReviewsAdapter;
import com.argandevteam.fpes.model.Centre;
import com.argandevteam.fpes.model.Review;
import com.argandevteam.fpes.model.User;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsFragment extends Fragment implements OnMapReadyCallback, View.OnClickListener {

    private static final String TAG = "DetailsFragment";
    Centre centre;
    ReviewsAdapter reviewsAdapter;
    ArrayList<Review> reviewsList;
    @BindView(R.id.reviewList)
    ListView lvReview;
    MapView mapView;
    TextView numReviews;
    RatingBar ratingBar;
    EditText reviewEditText;
    TextView btnSend;
    FirebaseUser currentUser;
    ViewGroup listViewHeader;
    ImageView centreImage;
    TextView centreSpecificDen;

    RatingBar centreRating;

    private DatabaseReference mDatabase;
    private DatabaseReference centreRef;
    private DatabaseReference centresReviews;
    private DatabaseReference userReviewsRef;
    private DatabaseReference userRef;
    private DatabaseReference reviewsRef;

    public DetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        centre = (Centre) args.getParcelable("centre");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_details, container, false);
        lvReview = (ListView) view.findViewById(R.id.reviewList);
        ButterKnife.bind(this, view);
        listViewHeader = (ViewGroup) inflater.inflate(R.layout.list_view_header, lvReview, false);
        getActivity().setTitle("Detalles");

        currentUser = FirebaseAuth.getInstance().getCurrentUser();


        FirebaseDatabase instance = FirebaseDatabase.getInstance();
        mDatabase = instance.getReference();

        centreRef = mDatabase.child("centres").child(String.valueOf(centre.id - 1));
        centresReviews = mDatabase.child("centres-reviews").child(String.valueOf(centre.id - 1));
        userRef = mDatabase.child("users");
        userReviewsRef = mDatabase.child("users-reviews");
        reviewsRef = mDatabase.child("reviews");

        setUpListView(inflater, savedInstanceState);
        return view;
    }

    private void setUpAdapter() {
        reviewsList = new ArrayList<>();
        centresReviews.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                reviewsList.clear();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot centreReviewSnapshot : dataSnapshot.getChildren()) {
                        final Review review = centreReviewSnapshot.getValue(Review.class);
                        userRef.child(review.user_uid).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                review.user = dataSnapshot.getValue(User.class);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        reviewsList.add(review);
                    }
                }
                reviewsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        reviewsAdapter = new ReviewsAdapter(getContext(), reviewsList);
        lvReview.setAdapter(reviewsAdapter);
        lvReview.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
    }


    private void setUpListView(LayoutInflater inflater, Bundle savedInstanceState) {
        ratingBar = (RatingBar) listViewHeader.findViewById(R.id.user_rating);
        reviewEditText = (EditText) listViewHeader.findViewById(R.id.user_review);
        btnSend = (TextView) listViewHeader.findViewById(R.id.btnSend);
        numReviews = (TextView) listViewHeader.findViewById(R.id.centre_num_reviews);
        centreImage = (ImageView) listViewHeader.findViewById(R.id.centre_image);
        centreSpecificDen = (TextView) listViewHeader.findViewById(R.id.centre_specific_den);
        Picasso.with(getContext()).load(centre.thumbnail_url)
                .fit()
                .placeholder(R.drawable.com_facebook_button_background)
                .centerCrop()
                .into(centreImage);

        btnSend.setOnClickListener(this);
        ratingBar.setRating(centre.sum_ratings / centre.num_ratings);
        numReviews.setText(String.valueOf(centre.num_reviews));
        centreSpecificDen.setText(centre.specific_den);
        lvReview.addHeaderView(listViewHeader);

        initializeMaps(savedInstanceState, listViewHeader);

        lvReview.setDivider(null);
        setUpAdapter();
    }

    private void initializeMaps(Bundle savedInstanceState, ViewGroup listViewHeader) {
        mapView = (MapView) listViewHeader.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSend:
                sendReview();
        }
    }

    private void sendReview() {

        String reviewText = reviewEditText.getText().toString();
        final float rating = ratingBar.getRating();
        String date = DateFormat.getInstance().format(new Date()).split(" ")[0];

        final Review review = new Review(currentUser.getUid(), rating, reviewText, date);

        String reviewPushKey = centreRef.child("reviews").push().getKey();
        userReviewsRef.child(currentUser.getUid()).child(reviewPushKey).setValue(review);
        centresReviews.child(reviewPushKey).setValue(review);
        reviewsRef.child(reviewPushKey).setValue(review);
        centreRef.child("reviews").child(reviewPushKey).setValue(true);
        userRef.child(currentUser.getUid()).child("reviews").child(reviewPushKey).setValue(true);
    }
}
