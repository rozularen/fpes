package com.argandevteam.fpes.model;

import android.net.Uri;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;

/**
 * Created by markc on 30/03/2017.
 */
@IgnoreExtraProperties
public class User {

    public String user_image;
    public String uid;
    public String name;
    public String email;
    public HashMap<String, Boolean> reviews;

    public User() {
    }

    public User(String uid, String email, String name, String user_image, HashMap<String, Boolean> reviews) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.reviews = reviews;
        this.user_image = user_image;
    }

    public User(String uid, String email, String displayName, String photo_url) {
        this.uid = uid;
        this.email = email;
        this.name = displayName;
        this.user_image = photo_url;
    }
}
