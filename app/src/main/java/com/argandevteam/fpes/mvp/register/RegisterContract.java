package com.argandevteam.fpes.mvp.register;

import com.argandevteam.fpes.mvp.BasePresenter;
import com.argandevteam.fpes.mvp.BaseView;
import com.argandevteam.fpes.mvp.list.ListContract;

/**
 * Created by markc on 14/12/2017.
 */

public interface RegisterContract {
    interface View extends BaseView<ListContract.Presenter> {

    }

    interface Presenter extends BasePresenter {

    }
}
