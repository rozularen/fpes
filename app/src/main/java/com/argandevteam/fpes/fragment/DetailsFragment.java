package com.argandevteam.fpes.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.argandevteam.fpes.R;
import com.argandevteam.fpes.adapter.ReviewsAdapter;
import com.argandevteam.fpes.model.Centre;
import com.argandevteam.fpes.model.Review;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsFragment extends Fragment {

    Centre centre;
    ReviewsAdapter reviewsAdapter;
    ArrayList<Review> reviewsList;

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
        reviewsAdapter = new ReviewsAdapter(getContext(), reviewsList);


        return view;
    }

}
