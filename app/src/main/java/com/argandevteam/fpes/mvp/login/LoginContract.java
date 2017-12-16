package com.argandevteam.fpes.mvp.login;

import android.content.Intent;

import com.argandevteam.fpes.activity.MainActivity;
import com.argandevteam.fpes.mvp.BasePresenter;
import com.argandevteam.fpes.mvp.BaseView;

/**
 * Created by markc on 13/12/2017.
 */

public class LoginContract {
    interface View extends BaseView<Presenter> {

        void navigateToHome();

        void showFirebaseLoginFailed();
    }

    interface Presenter extends BasePresenter {

        void onStop();

        void onActivityResult(int requestCode, int resultCode, Intent data);

        void setAuthListener();

        void removeAuthListener();

        void doLoginWithEmailAndPassword(String email, String password);

        void doLoginWithGoogle();

        void doLoginWithFacebook();

        void signOff();
    }
}
