package com.argandevteam.fpes;

import android.app.Activity;
import android.content.Context;

/**
 * Created by markc on 06/12/2017.
 */

public interface BaseView<P> {
    void setPresenter(P presenter);


    Context getViewContext();

    Activity getViewActivity();
}
