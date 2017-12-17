package com.argandevteam.fpes.mvp.login;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.argandevteam.fpes.mvp.data.source.UsersRepository;
import com.argandevteam.fpes.mvp.login.facebook.FacebookSignIn;
import com.argandevteam.fpes.mvp.login.google.GoogleSignIn;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by markc on 13/12/2017.
 */

public class LoginPresenter implements LoginContract.Presenter {

    private static final String TAG = "LoginPresenter";


    private LoginContract.View view;

    private UsersRepository usersRepository;

    private FirebaseAuth firebaseAuth;

    private GoogleSignIn googleSignInPresenter;

    private FacebookSignIn facebookSignInPresenter;

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

    public LoginPresenter(LoginContract.View view) {
        if (view != null) {
            this.view = view;
            this.view.setPresenter(this);
            firebaseAuth = FirebaseAuth.getInstance();
        }
        Log.d(TAG, "LoginPresenter: HE CREADO UN PRESENTER");
    }

    @Override
    public void start() {
        //Load whatever config or something
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
    public void onDestroy() {
        view = null;
    }

    @Override
    public void doLoginWithGoogle() {
        if ((Activity) view.getViewActivity() != null) {
            Log.d(TAG, "doLoginWithGoogle: HAY ACTIVITY");
        } else {
            Log.d(TAG, "doLoginWithGoogle: NADA");
        }
        googleSignInPresenter.logIn();
    }

    @Override
    public void onResult(int requestCode, int resultCode, Intent data) {
        googleSignInPresenter.onResult(requestCode, resultCode, data);
    }

    @Override
    public void doLoginWithFacebook() {
        facebookSignInPresenter.logIn();
    }

    @Override
    public void logOff() {
        FirebaseAuth.getInstance().signOut();
    }

    @Override
    public void doLoginWithEmailAndPassword(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            view.showFirebaseLoginFailed();
                        } else {
//                            if (!sharedPreferences.contains(Constants.FIRST_LAUNCH)) {
//                                addUser(task.getResult().getUser());
//                                sharedPreferences.edit().putBoolean(Constants.FIRST_LAUNCH, true).apply();
//                            }
                        }
                    }
                });
    }

    public void signInWithCredentials(AuthCredential credential) {

        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Log.d(TAG, "onComplete: AUTH FAILED");
                        } else {
                            Log.d(TAG, "onComplete: GO AHEAD");
                        }
                    }
                });
    }


    public void setGoogleSignInPresenter(GoogleSignIn googleSignInPresenter) {
        this.googleSignInPresenter = googleSignInPresenter;
    }


}
