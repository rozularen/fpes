package com.argandevteam.fpes.register;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by markc on 14/12/2017.
 */

public class RegisterPresenter implements RegisterContract.Presenter {

    private RegisterContract.View view;

    private String TAG = "RegisterPresenter";

    private FirebaseAuth firebaseAuth;

    private FirebaseAuth.AuthStateListener authListener = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                //User is logged, notify MainActivity to change screen
                view.navigateToHome();
                Log.d(TAG, "onAuthStateChanged: USER LOGGED IN");
            } else {
                // Users is not logged in
                Log.d(TAG, "onAuthStateChanged: USER OFF");
            }
        }
    };


    public RegisterPresenter(RegisterContract.View view) {
        if (view != null) {
            this.view = view;
            view.setPresenter(this);
            firebaseAuth = FirebaseAuth.getInstance();
        }
    }

    @Override
    public void start() {
        //Load something into ui
    }

    @Override
    public void onDestroy() {
        view = null;
    }

    @Override
    public void registerUser(String username, String email, String password) {
        if (validateString(username)) {
            if (validateString(email)) {
                if (validateString(password)) {
                    firebaseAuth
                            .createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (!task.isSuccessful()) {
                                        view.userRegisterFailed();
                                    } else {
                                        view.navigateToHome();
                                    }
                                }
                            });
                } else {
                    view.showPasswordError();
                }
            } else {
                view.showEmailError();
            }
        } else {
            view.showUsernameError();
        }
    }

    private boolean validateString(String text) {
        boolean isValid = true;

        if (text == null) {
            isValid = false;
        }

        if (TextUtils.isEmpty(text)) {
            isValid = false;
        }

        if (text != null) {
            if (text.length() == 0) {
                isValid = false;
            }
        }

        return isValid;
    }
}
