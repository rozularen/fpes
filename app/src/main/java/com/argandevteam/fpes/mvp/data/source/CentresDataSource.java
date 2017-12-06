package com.argandevteam.fpes.mvp.data.source;

import com.argandevteam.fpes.mvp.data.Centre;

import java.util.List;

/**
 * Created by markc on 06/12/2017.
 */

public interface CentresDataSource {
    void getCentres(LoadCentresCallback callback);

    void getCentre(int tripId, LoadCentreCallback callback);

    interface LoadCentresCallback {
        void onCentresLoaded(List<Centre> centreList);

        void onError();
    }

    interface LoadCentreCallback {
        void onCentreCallback(Centre centre);

        void onError();
    }
}
