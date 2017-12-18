package com.argandevteam.fpes.centredetails;

/**
 * Created by markc on 18/12/2017.
 */

public class CentreDetailsPresenter implements CentreDetailsContract.Presenter {

    private CentreDetailsContract.View view;

    public CentreDetailsPresenter(CentreDetailsContract.View view) {
        if (view != null) {
            this.view = view;
            view.setPresenter(this);
        }
    }

    @Override
    public void start() {
        //load centre details UI
    }

    @Override
    public void onDestroy() {
        view = null;
    }
}
