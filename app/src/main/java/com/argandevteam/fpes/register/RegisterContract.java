package com.argandevteam.fpes.register;

import com.argandevteam.fpes.BasePresenter;
import com.argandevteam.fpes.BaseView;

/**
 * Created by markc on 14/12/2017.
 */

public interface RegisterContract {
    interface View extends BaseView<RegisterContract.Presenter> {

        void showPasswordError();

        void showEmailError();

        void showUsernameError();

        void userRegisterFailed();

        void navigateToHome();
    }

    interface Presenter extends BasePresenter {

        void registerUser(String username, String email, String password);
    }
}
