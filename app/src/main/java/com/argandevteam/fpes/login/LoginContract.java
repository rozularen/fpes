package com.argandevteam.fpes.login;

import android.content.Intent;

import com.argandevteam.fpes.BasePresenter;
import com.argandevteam.fpes.BaseView;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by markc on 13/12/2017.
 */

public class LoginContract {
    interface View extends BaseView<Presenter> {

        void navigateToRegister();

        void navigateToHome();

        void showFirebaseLoginFailed();

        void setPasswordError();

        void setEmailError();

        void loadUserInfo(FirebaseUser firebaseUser);

    }

    interface Presenter extends BasePresenter {

        void onResult(int requestCode, int resultCode, Intent data);

        void setAuthListener();

        void removeAuthListener();

        void doLoginWithEmailAndPassword(String email, String password);

        void doLoginWithGoogle();

        void doLoginWithFacebook();

        void logOff();
    }
}
