package com.argandevteam.fpes.centres;

import com.argandevteam.fpes.data.Centre;
import com.argandevteam.fpes.data.source.CentresDataSource;
import com.argandevteam.fpes.data.source.CentresRepository;

import java.util.List;

/**
 * Created by markc on 06/12/2017.
 */

public class CentresPresenter implements CentresContract.Presenter {

    private CentresContract.View mView;
    private CentresRepository mCentresRepository;

    public CentresPresenter(CentresContract.View view) {
        if (view != null) {
            mView = view;
        }
    }

    public CentresPresenter(CentresContract.View view, CentresRepository centresRepository) {
        if (centresRepository != null) {
            mCentresRepository = centresRepository;
            if (view != null) {
                mView = mView;
                mView.setPresenter(this);
            }
        }
    }

    @Override
    public void start() {
        loadCentres();
    }

    @Override
    public void stop() {
        mView = null;
    }

    private void loadCentres() {
        mCentresRepository.getCentres(new CentresDataSource.LoadCentresCallback() {
            @Override
            public void onCentresLoaded(List<Centre> centreList) {
                mView.showCentres(centreList);
            }

            @Override
            public void onError() {
                mView.showLoadingCentresError();
            }
        });
    }
}