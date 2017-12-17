package com.argandevteam.fpes.mvp.login.facebook;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.argandevteam.fpes.mvp.login.LoginPresenter;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;

import java.util.Arrays;
import java.util.Collection;

/**
 * Created by markc on 17/12/2017.
 */

public class FacebookSignIn implements FacebookSignInPresenter {

    private Context context;
    private AuthCredential facebookCredential;
    private CallbackManager facebookCallbackManager;
    private Collection<String> facebookPermissions = Arrays.asList("email", "public_profile");

    private LoginPresenter loginPresenter;
    private String TAG = "FacebookSignIn";

    public FacebookSignIn(Context context, LoginPresenter loginPresenter) {
        this.context = context;
        this.loginPresenter = loginPresenter;
    }

    @Override
    public void logIn() {
        LoginManager loginManager = LoginManager.getInstance();

        facebookCallbackManager = CallbackManager.Factory.create();

        FacebookCallback<LoginResult> callback = new Callback();

        loginManager.logInWithReadPermissions((Activity) context, facebookPermissions);

        loginManager.registerCallback(facebookCallbackManager, callback);
    }

    @Override
    public void logOff() {
        LoginManager.getInstance().logOut();
    }

    @Override
    public void onResult(int requestCode, int resultCode, Intent data) {
        facebookCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private class Callback implements FacebookCallback<LoginResult> {

        @Override
        public void onSuccess(LoginResult loginResult) {
            Log.d(TAG, "onSuccess: AAAAAAAAAAAAAAAAAAAAAAA");
            facebookCredential = FacebookAuthProvider.getCredential(loginResult.getAccessToken().getToken());
            loginPresenter.signInWithCredentials(facebookCredential);
        }

        @Override
        public void onCancel() {
            //Login Canceled
            Log.d(TAG, "onCancel: aaaaaaaaaaaaaaaaaaaaaaaa");
        }

        @Override
        public void onError(FacebookException error) {
            Log.d(TAG, "onError: !!!!!!!!!!!!!!!!!");
            //Show error facebook
        }
    }
}
