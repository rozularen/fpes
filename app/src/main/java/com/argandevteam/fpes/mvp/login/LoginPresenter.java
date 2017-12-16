package com.argandevteam.fpes.mvp.login;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.argandevteam.fpes.activity.MainActivity;
import com.argandevteam.fpes.mvp.data.source.UsersRepository;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Arrays;
import java.util.Collection;

/**
 * Created by markc on 13/12/2017.
 */

public class LoginPresenter implements LoginContract.Presenter, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "LoginPresenter";
    private static final int RC_SIGN_IN_G = 100;

    private LoginContract.View mView;

    private MainActivity mMainActivity;

    private UsersRepository mUsersRepository;

    private FirebaseAuth mFirebaseAuth;

    private Context mContext;

    private CallbackManager mFacebookCallbackManager;

    AuthCredential facebookCredential;

    private FirebaseAuth.AuthStateListener mAuthListener = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                //User is logged, notify MainActivity to change screen
                mView.navigateToHome();
                Log.d(TAG, "onAuthStateChanged: USER LOGGED IN");
            } else {
                // Users is not logged in
                Log.d(TAG, "onAuthStateChanged: USER OFF");
            }
        }
    };
    private Collection<String> facebookPermissions = Arrays.asList("email", "public_profile");
    private GoogleApiClient mGoogleApiClient;
    private AuthCredential googleCredential;


    public LoginPresenter(LoginContract.View view) {
        if (view != null) {
            mView = view;
            mView.setPresenter(this);
            mFirebaseAuth = FirebaseAuth.getInstance();
        }
    }


    public LoginPresenter(LoginContract.View view, UsersRepository usersRepository) {
        mView = mView;
        mUsersRepository = usersRepository;
    }


    @Override
    public void setAuthListener() {
        mFirebaseAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void removeAuthListener() {
        mFirebaseAuth.removeAuthStateListener(mAuthListener);
    }

    @Override
    public void start() {
        mContext = mView.getViewContext();
        mMainActivity = (MainActivity) mContext;
        if (mGoogleApiClient == null) {
            setUpGoogleSignIn();
        }
    }

    private void setUpGoogleSignIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken("64500376949-1erlve8i66osmdfq6l6kc3p4e7igivhg.apps.googleusercontent.com")
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                .enableAutoManage(mMainActivity /* FragmentActivity */,
                        this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }


    @Override
    public void doLoginWithGoogle() {
        Intent googleSignInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        mMainActivity.startActivityForResult(googleSignInIntent, RC_SIGN_IN_G);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
            signInWithCredentials(googleCredential);

        } else {
            //Google sign in failed
        }
    }

    @Override
    public void onStop() {
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }


    @Override
    public void doLoginWithFacebook() {
        LoginManager loginManager = LoginManager.getInstance();
        mFacebookCallbackManager = CallbackManager.Factory.create();

        FacebookCallback<LoginResult> callback = new Callback();

        loginManager.logInWithReadPermissions(mMainActivity, facebookPermissions);

        loginManager.registerCallback(mFacebookCallbackManager, callback);

    }

    @Override
    public void signOff() {
        FirebaseAuth.getInstance().signOut();
        Auth.GoogleSignInApi.signOut(mGoogleApiClient);
        LoginManager.getInstance().logOut();
    }

    @Override
    public void doLoginWithEmailAndPassword(String email, String password) {
        mFirebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(mMainActivity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            mView.showFirebaseLoginFailed();
                        } else {
//                            if (!sharedPreferences.contains(Constants.FIRST_LAUNCH)) {
//                                addUser(task.getResult().getUser());
//                                sharedPreferences.edit().putBoolean(Constants.FIRST_LAUNCH, true).apply();
//                            }
                        }
                    }
                });
    }

    protected void signInWithCredentials(AuthCredential credential) {
        mFirebaseAuth.signInWithCredential(credential).addOnCompleteListener(mMainActivity, new OnCompleteListener<AuthResult>() {
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

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private class Callback implements FacebookCallback<LoginResult> {

        @Override
        public void onSuccess(LoginResult loginResult) {
            facebookCredential = FacebookAuthProvider.getCredential(loginResult.getAccessToken().getToken());
            signInWithCredentials(facebookCredential);
        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException error) {
            //Show error facebook
        }
    }
}
