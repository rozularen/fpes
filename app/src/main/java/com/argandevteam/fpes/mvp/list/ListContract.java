package com.argandevteam.fpes.mvp.list;

import com.argandevteam.fpes.mvp.BasePresenter;
import com.argandevteam.fpes.mvp.BaseView;
import com.argandevteam.fpes.mvp.data.Centre;

import java.util.List;

/**
 * Created by markc on 06/12/2017.
 */

public interface ListContract {

    interface View extends BaseView<Presenter> {

        void showCentres(List<Centre> centreList);

        void showLoadingCentresError();
    }

    interface Presenter extends BasePresenter {

    }
}
