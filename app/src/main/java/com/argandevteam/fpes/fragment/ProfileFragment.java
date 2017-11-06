package com.argandevteam.fpes.fragment;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.argandevteam.fpes.R;
import com.argandevteam.fpes.ui.CustomLinearLayout;
import com.argandevteam.fpes.utils.CircleTransform;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.picasso.transformations.BlurTransformation;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener {


    private static final int GET_FROM_GALLERY = 1;
    private static final String TAG = "ProfileFragment";

    @BindView(R.id.user_photo_container)
    CustomLinearLayout customLinearLayout;
    @BindView(R.id.user_photo_view)
    ImageView profileUserImage;
    @BindView(R.id.profile_user_name)
    TextView profileUserName;
    @BindView(R.id.update_profile)
    TextView updateProfile;
    @BindView(R.id.profile_description)
    TextView profileDescription;
    @BindView(R.id.description_input)
    EditText descriptionInput;

    private FirebaseUser firebaseUser;
    private Uri photoUri;
    private boolean isSaving;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);
        updateProfile.setOnClickListener(this);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        customLinearLayout.setOnClickListener(this);
        Log.d(TAG, "onCreateView: setting user image");
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


        profileUserName.setText(firebaseUser.getDisplayName());

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_photo_container:
                //   Intent selectImageIntent = new Intent(
//                        Intent.ACTION_PICK,
//                        MediaStore.Images.Media.INTERNAL_CONTENT_URI);

                // startActivityForResult(selectImageIntent, GET_FROM_GALLERY);

                Toast.makeText(getContext(), "GALERIA", Toast.LENGTH_SHORT).show();
                break;
            case R.id.update_profile:
                updateProfileDescription();
                Toast.makeText(getActivity(), "actualizar", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Detects request codes
        Log.d(TAG, "onActivityResult: ");
        if (requestCode == GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            setNewUserImage(selectedImage);
        }
    }

    private void setNewUserImage(final Uri imageUri) {
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setPhotoUri(imageUri)
                .build();
        Log.d(TAG, "setNewUserImage: " + imageUri.toString());

        firebaseUser.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "onComplete: User Image updated succesfully");
                    Picasso.with(getContext())
                            .load(imageUri.toString())
                            .transform(new CircleTransform())
                            .into(profileUserImage);
                }
            }
        });
    }
}
