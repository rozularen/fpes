package com.argandevteam.fpes.mvp.register;

import com.argandevteam.fpes.mvp.data.source.UsersRepository;

/**
 * Created by markc on 14/12/2017.
 */

public class RegisterPresenter implements RegisterContract.Presenter {

    private RegisterContract.View mView;
    private UsersRepository mUsersRepository;

    public RegisterPresenter(RegisterContract.View view, UsersRepository usersRepository) {
        mView = view;
        mUsersRepository = usersRepository;
    }

    @Override
    public void start() {

    }
}
