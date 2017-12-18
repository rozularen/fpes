package com.argandevteam.fpes.data.source.remote;

import com.argandevteam.fpes.data.source.UsersDataSource;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by markc on 14/12/2017.
 */

public class UsersRemoteDataSource implements UsersDataSource {

    private FirebaseDatabase instance;
    private DatabaseReference mDatabase;
    private DatabaseReference mUsersReference;

    public UsersRemoteDataSource() {
        instance = FirebaseDatabase.getInstance();
        mDatabase = instance.getReference();
        mUsersReference = mDatabase.child("users");
    }

    @Override
    public void getUser(int id, LoadUserCallback callback) {
        //        mUsersReference = mDatabase.child();
    }

    @Override
    public void getUsers(LoadUsersCallback callback) {

    }
}
