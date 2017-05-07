package com.argandevteam.fpes.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.argandevteam.fpes.R;
import com.argandevteam.fpes.utils.CircleTransform;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {


    @BindView(R.id.profile_user_image)
    ImageView profileUserImage;
    @BindView(R.id.profile_user_name)
    TextView profileUserName;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        profileUserName.setText(currentUser.getDisplayName());
        Picasso.with(getContext())
                .load(currentUser.getPhotoUrl())
                .fit()
                .transform(new CircleTransform())
                .centerCrop().into(profileUserImage);

        return view;
    }

}
