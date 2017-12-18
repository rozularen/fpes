package com.argandevteam.fpes.mvp.details;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.argandevteam.fpes.R;
import com.argandevteam.fpes.adapter.ReviewsAdapter;
import com.argandevteam.fpes.mvp.data.Centre;
import com.argandevteam.fpes.mvp.data.Review;
import com.argandevteam.fpes.mvp.data.User;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.DecimalFormat;
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
    RatingBar centreRating;
    RatingBar userRating;
    TextInputLayout reviewInputLayout;
    TextInputEditText reviewEditText;
    Button btnSend;
    FirebaseUser currentUser;
    ViewGroup listViewHeader;
    ImageView centreImage;
    TextView numReviews;
    TextView centreSpecificDen;
    TextView centreAddress;
    TextView centreNature;
    TextView centreRatingValue;
    @BindView(R.id.adView)
    AdView mAdView;
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
        View view = inflater.inflate(R.layout.centre_list_reviews, container, false);
        lvReview = (ListView) view.findViewById(R.id.reviewList);
        ButterKnife.bind(this, view);

        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        listViewHeader = (ViewGroup) inflater.inflate(R.layout.centre_details, lvReview, false);
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
        btnSend = (Button) listViewHeader.findViewById(R.id.btnSend);
        userRating = (RatingBar) listViewHeader.findViewById(R.id.user_rating);
        centreRating = (RatingBar) listViewHeader.findViewById(R.id.centre_rating);
        centreImage = (ImageView) listViewHeader.findViewById(R.id.centre_image);
        reviewInputLayout =
                (TextInputLayout) listViewHeader.findViewById(R.id.user_review_input_layout);
        reviewEditText = (TextInputEditText) listViewHeader.findViewById(R.id.user_review);
        numReviews = (TextView) listViewHeader.findViewById(R.id.centre_num_reviews);
        centreSpecificDen = (TextView) listViewHeader.findViewById(R.id.centre_specific_den);
        centreRatingValue = (TextView) listViewHeader.findViewById(R.id.centre_rating_value);
        centreAddress = (TextView) listViewHeader.findViewById(R.id.centre_address);
        centreNature = (TextView) listViewHeader.findViewById(R.id.centre_nature);

        Picasso.with(getContext())
                .load(centre.thumbnail_url)
                .fit()
                .placeholder(R.drawable.com_facebook_button_background)
                .centerCrop()
                .into(centreImage);

        btnSend.setOnClickListener(this);
        numReviews.setText(String.valueOf(centre.num_reviews));
        centreSpecificDen.setText(centre.specific_den);
        lvReview.addHeaderView(listViewHeader);
        centreRating.setRating(centre.rating_average);
        DecimalFormat df = new DecimalFormat("#.#");

        centreRatingValue.setText(String.valueOf(df.format(centre.rating_average)) + "/5");
        centreNature.setText(centre.nature);
        centreAddress.setText(centre.address);
        if (centre.reviews != null) {
            numReviews.setText(centre.reviews.size() + " opiniones de usuarios");
        }
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
        LatLng centreLatLng = new LatLng(centre.lat, centre.lon);
        CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(centreLatLng, 8.5f);
        googleMap.animateCamera(yourLocation);
        Marker marker = googleMap.addMarker(new MarkerOptions().position(centreLatLng)
                .snippet("Dirección: " + centre.address)
                .title(centre.specific_den));
        marker.setTag(centre);
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
        String errorMessage = null;
        if (TextUtils.isEmpty(reviewText)) {
            errorMessage = "Introduzca una breve opinión";
        }
        if (reviewText.length() < 6) {
            errorMessage = "Opinion demasiado corta";
        }

        toggleTextInputLayoutError(reviewInputLayout, errorMessage);
        final float rating = userRating.getRating();
        String date = DateFormat.getInstance().format(new Date()).split(" ")[0];

        final Review review = new Review(currentUser.getUid(), rating, reviewText, date);

        String reviewPushKey = centreRef.child("reviews").push().getKey();
        userReviewsRef.child(currentUser.getUid()).child(reviewPushKey).setValue(review);
        centresReviews.child(reviewPushKey).setValue(review);
        reviewsRef.child(reviewPushKey).setValue(review);
        centreRef.child("reviews").child(reviewPushKey).setValue(true);
        userRef.child(currentUser.getUid()).child("reviews").child(reviewPushKey).setValue(true);
        reviewEditText.setText("");
        userRating.setRating(0);
        Toast.makeText(getActivity(), "Tu opinión ha sido registrada.", Toast.LENGTH_SHORT).show();
    }

    private void toggleTextInputLayoutError(
            @NonNull
                    TextInputLayout textInputLayout, String msg) {
        textInputLayout.setError(msg);
        if (msg == null) {
            textInputLayout.setErrorEnabled(false);
        } else {
            textInputLayout.setErrorEnabled(true);
            reviewEditText.setBackgroundColor(Color.WHITE);
        }
    }

    private void clearFocus() {
        View view = getActivity().getCurrentFocus();
        if (view != null && view instanceof EditText) {
            InputMethodManager inputMethodManager =
                    (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            view.clearFocus();
        }
    }
}
