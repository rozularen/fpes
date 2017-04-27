package com.argandevteam.fpes.model;

import android.net.Uri;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by markc on 30/03/2017.
 */
@IgnoreExtraProperties
public class User {

    public String user_image;
    public String uid;
    public String name;
    public String email;

    public User() {
    }



    public User(String uid, String email, String name, String user_image) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.user_image = user_image;
    }

    public User(String email) {
        this.email = email;
    }
}
