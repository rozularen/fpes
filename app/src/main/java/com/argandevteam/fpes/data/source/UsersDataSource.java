package com.argandevteam.fpes.data.source;

import com.argandevteam.fpes.data.User;

import java.util.List;

/**
 * Created by markc on 13/12/2017.
 */

public interface UsersDataSource {

    void getUser(int id, LoadUserCallback callback);

    void getUsers(LoadUsersCallback callback);

    interface LoadUserCallback {
        void onUserLoaded(User user);

        void onError();
    }

    interface LoadUsersCallback {
        void onUsersLoaded(List<User> usersList);

        void onError();
    }
}
