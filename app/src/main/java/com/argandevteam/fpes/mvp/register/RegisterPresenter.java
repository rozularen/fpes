package com.argandevteam.fpes.mvp.register;

/**
 * Created by markc on 14/12/2017.
 */

public class RegisterPresenter implements RegisterContract.Presenter {

    private RegisterContract.View view;

    public RegisterPresenter(RegisterContract.View view) {
        if (view != null) {
            this.view = view;
            view.setPresenter(this);
        }
    }

    @Override
    public void start() {
        //Load something into ui
    }

    @Override
    public void onDestroy() {
        view = null;
    }
}
