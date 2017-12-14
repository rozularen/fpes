package com.argandevteam.fpes.mvp.login;

import com.argandevteam.fpes.mvp.BasePresenter;
import com.argandevteam.fpes.mvp.data.User;
import com.argandevteam.fpes.mvp.data.source.UsersRepository;

/**
 * Created by markc on 13/12/2017.
 */

public class LoginPresenter implements LoginContract.Presenter {

    private LoginContract.View mView;
    private UsersRepository mUsersRepository;

    public LoginPresenter(LoginContract.View view, UsersRepository usersRepository) {
        mView = mView;
        mUsersRepository = usersRepository;
    }

    @Override
    public void start() {
        //Clear login edit texts
    }


    @Override
    public User doLoginWithEmailAndPassword(String email, String password) {
        return null;
    }
}
