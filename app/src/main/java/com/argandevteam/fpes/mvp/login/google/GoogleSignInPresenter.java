package com.argandevteam.fpes.mvp.login.google;

import android.content.Intent;

/**
 * Created by markc on 17/12/2017.
 */

public interface GoogleSignInPresenter {
    void createGoogleClient();

    void onStart();

    void logIn();

    void logOff();

    void onResult(int requestCode, int resultCode, Intent data);

    void onDestroy();
}
