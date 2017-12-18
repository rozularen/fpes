package com.argandevteam.fpes.profile;

import android.content.Intent;
import android.net.Uri;

import com.argandevteam.fpes.BasePresenter;
import com.argandevteam.fpes.BaseView;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by markc on 18/12/2017.
 */

public interface ProfileContract {
    interface View extends BaseView<Presenter> {

        void loadUserInfo(FirebaseUser user);

        void updateUserPhoto(Uri imageUri);

    }

    interface Presenter extends BasePresenter {

        void onResult(int requestCode, int resultCode, Intent data);

        void setAuthListener();

        void removeAuthListener();

    }
}
