package com.argandevteam.fpes.data.source;

import com.argandevteam.fpes.data.Centre;

import java.util.List;

/**
 * Created by markc on 06/12/2017.
 */

public class CentresRepository implements CentresDataSource {

    private static final String TAG = "CentresRepository";

    private static CentresRepository INSTANCE = null;

    private CentresDataSource mCentresLocalDataSource = null;
    private CentresDataSource mCentresRemoteDataSource = null;

    public CentresRepository(CentresDataSource mCentresLocalDataSource,
                             CentresDataSource mCentresRemoteDataSource) {
        if (mCentresLocalDataSource != null) {
            this.mCentresLocalDataSource = mCentresLocalDataSource;
        }
        if (mCentresRemoteDataSource != null) {
            this.mCentresRemoteDataSource = mCentresRemoteDataSource;
        }
    }

    @Override
    public void getCentres(LoadCentresCallback callback) {
        if (callback != null) {
            //TODO: first from cache, if not possible, then from local and finally from remote
            getCentresFromRemoteDataSource(callback);
        }
    }

    @Override
    public void getCentre(int tripId, LoadCentreCallback callback) {
        getCentreFromRemoteDataSource(tripId, callback);
    }

    private void getCentreFromRemoteDataSource(int tripId, final LoadCentreCallback callback) {
        mCentresRemoteDataSource.getCentre(tripId, new LoadCentreCallback() {
            @Override
            public void onCentreCallback(Centre centre) {
                callback.onCentreCallback(centre);
            }

            @Override
            public void onError() {
                callback.onError();
            }
        });
    }

    private void getCentresFromRemoteDataSource(final LoadCentresCallback callback) {
        mCentresRemoteDataSource.getCentres(new LoadCentresCallback() {
            @Override
            public void onCentresLoaded(List<Centre> centreList) {
                //TODO: refreshCache, refreshLocalDataSource
                callback.onCentresLoaded(centreList);
            }

            @Override
            public void onError() {
                callback.onError();
            }
        });
    }
}
