package com.argandevteam.fpes.mvp.login.facebook;

import android.content.Intent;

/**
 * Created by markc on 17/12/2017.
 */

public interface FacebookSignInPresenter {
    void logIn();

    void logOff();

    void onResult(int requestCode, int resultCode, Intent data);
}
