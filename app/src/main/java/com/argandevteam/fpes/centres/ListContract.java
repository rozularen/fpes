package com.argandevteam.fpes.centres;

import com.argandevteam.fpes.BasePresenter;
import com.argandevteam.fpes.BaseView;
import com.argandevteam.fpes.data.Centre;

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
