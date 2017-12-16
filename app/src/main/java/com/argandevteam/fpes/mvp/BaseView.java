package com.argandevteam.fpes.mvp;

import android.content.Context;

/**
 * Created by markc on 06/12/2017.
 */

public interface BaseView<P> {
    void setPresenter(P presenter);

    Context getViewContext();
}
