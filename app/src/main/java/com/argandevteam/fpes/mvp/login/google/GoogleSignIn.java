package com.argandevteam.fpes.mvp.login.google;

import android.content.Intent;
import android.support.annotation.NonNull;

import com.argandevteam.fpes.activity.MainActivity;
import com.argandevteam.fpes.mvp.login.LoginPresenter;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;

/**
 * Created by markc on 17/12/2017.
 */

public class GoogleSignIn
        implements GoogleSignInPresenter, GoogleApiClient.OnConnectionFailedListener {

    private static final int RC_SIGN_IN_G = 100;

    private static final String TAG = "GSIPresenter";
    private final String googleIdToken =
            "64500376949-1erlve8i66osmdfq6l6kc3p4e7igivhg.apps.googleusercontent.com";
    private MainActivity activity;
    private LoginPresenter loginPresenter;
    private GoogleApiClient googleApiClient;
    private AuthCredential googleCredential;

    public GoogleSignIn(MainActivity activity, LoginPresenter loginPresenter) {
        this.activity = activity;
        this.loginPresenter = loginPresenter;
    }

    @Override
    public void createGoogleClient() {
        GoogleSignInOptions gso =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail()
                        .requestIdToken(googleIdToken)
                        .build();

        googleApiClient =
                new GoogleApiClient.Builder(activity).enableAutoManage(activity /* FragmentActivity */, this /* OnConnectionFailedListener */)
                        .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                        .build();
    }

    @Override
    public void onStart() {
        googleApiClient.connect();
    }

    @Override
    public void logIn() {
        Intent googleSignInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        activity.startActivityForResult(googleSignInIntent, RC_SIGN_IN_G);
    }

    @Override
    public void logOff() {
        Auth.GoogleSignInApi.signOut(googleApiClient);
    }

    @Override
    public void onResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_SIGN_IN_G) {
            GoogleSignInResult googleSignInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(googleSignInResult);
        }
    }

    private void handleSignInResult(GoogleSignInResult googleSignInResult) {
        if (googleSignInResult.isSuccess()) {
            GoogleSignInAccount googleSignInAccount = googleSignInResult.getSignInAccount();

            String personName = googleSignInAccount.getDisplayName();
            String personEmail = googleSignInAccount.getEmail();
            String personId = googleSignInAccount.getId();

            googleCredential = GoogleAuthProvider.getCredential(googleSignInAccount.getIdToken(), null);

            loginPresenter.signInWithCredentials(googleCredential);
        } else {
            //Google sign in failed
        }
    }

    @Override
    public void onDestroy() {

    }

    //GoogleApiClient.OnConnectionFailedListener
    @Override
    public void onConnectionFailed(
            @NonNull
                    ConnectionResult connectionResult) {

    }
}
