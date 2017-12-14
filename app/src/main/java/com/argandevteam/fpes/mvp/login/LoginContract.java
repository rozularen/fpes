package com.argandevteam.fpes.mvp.login;

import com.argandevteam.fpes.mvp.BasePresenter;
import com.argandevteam.fpes.mvp.BaseView;
import com.argandevteam.fpes.mvp.data.User;

/**
 * Created by markc on 13/12/2017.
 */

public class LoginContract {
    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter {
        User doLoginWithEmailAndPassword(String email, String password);
    }
}
