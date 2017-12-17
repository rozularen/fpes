package com.argandevteam.fpes.mvp.login.facebook;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.argandevteam.fpes.mvp.login.LoginPresenter;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
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

    public FacebookSignIn(Context context) {
        this.context = context;
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
        if (FacebookSdk.isFacebookRequestCode(requestCode)) {
            facebookCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private class Callback implements FacebookCallback<LoginResult> {

        @Override
        public void onSuccess(LoginResult loginResult) {
            facebookCredential = FacebookAuthProvider.getCredential(loginResult.getAccessToken().getToken());
            loginPresenter.signInWithCredentials(facebookCredential);
        }

        @Override
        public void onCancel() {
            //Login Canceled
        }

        @Override
        public void onError(FacebookException error) {
            //Show error facebook
        }
    }
}
