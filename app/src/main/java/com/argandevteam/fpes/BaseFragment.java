package com.argandevteam.fpes;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by markc on 16/12/2017.
 */

public abstract class BaseFragment extends Fragment {

    protected MainActivity mainActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable
                                     ViewGroup container,
                             Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        mainActivity = (MainActivity) getActivity();

        return view;
    }
}
