package com.argandevteam.fpes.profile;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

/**
 * Created by markc on 18/12/2017.
 */

public class ProfilePresenter implements ProfileContract.Presenter {

    private static final int GET_FROM_GALLERY = 1;
    private String TAG = "ProfilePresenter";
    private ProfileContract.View view;

    private FirebaseAuth firebaseAuth;

    private FirebaseUser firebaseUser;

    private FirebaseAuth.AuthStateListener authListener = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(
                @NonNull
                        FirebaseAuth firebaseAuth) {
            firebaseUser = firebaseAuth.getCurrentUser();
            if (firebaseUser != null) {
                view.loadUserInfo(firebaseUser);

                //User is logged, notify MainActivity to change screen
                Log.d(TAG, "onAuthStateChanged: USER LOGGED IN");
            } else {
                // Users is not logged in
                Log.d(TAG, "onAuthStateChanged: USER OFF");
            }
        }
    };

    public ProfilePresenter(ProfileContract.View view) {
        if (view != null) {
            this.view = view;
            this.view.setPresenter(this);
            firebaseAuth = FirebaseAuth.getInstance();
        }
    }

    @Override
    public void setAuthListener() {
        firebaseAuth.addAuthStateListener(authListener);
    }

    @Override
    public void removeAuthListener() {
        firebaseAuth.removeAuthStateListener(authListener);
    }

    @Override
    public void start() {
    }

    @Override
    public void stop() {
        view = null;
    }

    @Override
    public void onResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            setNewUserImage(selectedImage);
        }
    }

    private void setNewUserImage(final Uri imageUri) {
        UserProfileChangeRequest profileUpdates =
                new UserProfileChangeRequest.Builder().setPhotoUri(imageUri).build();

        firebaseUser.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            view.updateUserPhoto(imageUri);
                        }
                    }
                });

    }

}
