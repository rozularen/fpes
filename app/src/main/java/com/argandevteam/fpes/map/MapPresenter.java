package com.argandevteam.fpes.map;


/**
 * Created by markc on 18/12/2017.
 */

public class MapPresenter implements MapContract.Presenter {

    private MapContract.View view;

    public MapPresenter(MapContract.View view) {
        if (view != null) {
            this.view = view;
            this.view.setPresenter(this);
        }
    }

    @Override
    public void start() {
        //load Map maybe
    }

    @Override
    public void stop() {
        view = null;
    }
}
