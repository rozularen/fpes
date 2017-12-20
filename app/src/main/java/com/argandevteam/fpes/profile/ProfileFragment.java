package com.argandevteam.fpes.profile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.argandevteam.fpes.BaseFragment;
import com.argandevteam.fpes.R;
import com.argandevteam.fpes.ui.CustomLinearLayout;
import com.argandevteam.fpes.utils.CircleTransform;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.picasso.transformations.BlurTransformation;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends BaseFragment implements ProfileContract.View {

    private static final String TAG = "ProfileFragment";

    @BindView(R.id.cll_background_image)
    CustomLinearLayout customLinearLayout;

    @BindView(R.id.image_user_image)
    ImageView profileUserImage;

    @BindView(R.id.text_username)
    TextView profileUserName;

    @BindView(R.id.text_update_profile)
    TextView updateProfile;

    @BindView(R.id.text_user_description)
    TextView profileDescription;

    @BindView(R.id.et_user_description)
    EditText descriptionInput;

    private boolean isSaving;

    private ProfileContract.Presenter presenter;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.setAuthListener();
        presenter.start();
    }

    @Override
    public void onStop() {
        presenter.stop();
        presenter.removeAuthListener();
        super.onStop();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void loadUserInfo(FirebaseUser firebaseUser) {
        profileUserName.setText(firebaseUser.getDisplayName());

        Picasso.with(getContext())
                .load(firebaseUser.getPhotoUrl())
                .fit()
                .transform(new CircleTransform())
                .centerCrop()
                .into(profileUserImage);

        Picasso.with(getContext())
                .load(firebaseUser.getPhotoUrl())
                .transform(new BlurTransformation(getContext()))
                .into(customLinearLayout);
    }

    @OnClick(R.id.cll_background_image)
    public void onUserPhotoClick(View view) {
        Toast.makeText(getContext(), "GALERIA", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.text_update_profile)
    public void onUpdateProfileClick(View view) {
        updateProfileDescription();
        Toast.makeText(getActivity(), "actualizar", Toast.LENGTH_SHORT).show();
    }

    private void updateProfileDescription() {
        if (!isSaving) {
            updateProfile.setText("Guardar Cambios");
            profileDescription.setVisibility(View.GONE);
            descriptionInput.setVisibility(View.VISIBLE);
            isSaving = true;
        } else {
            updateProfile.setText("Actualizar Perfil");
            profileDescription.setVisibility(View.VISIBLE);
            descriptionInput.setVisibility(View.GONE);
            isSaving = false;
        }
    }


    @Override
    public void updateUserPhoto(Uri imageUri) {
        Picasso.with(getContext())
                .load(imageUri.toString())
                .transform(new CircleTransform())
                .into(profileUserImage);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        presenter.onResult(requestCode, resultCode, data);
    }

    @Override
    public void setPresenter(ProfileContract.Presenter presenter) {
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
