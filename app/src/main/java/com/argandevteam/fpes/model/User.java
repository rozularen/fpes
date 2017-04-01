package com.argandevteam.fpes.model;

import android.net.Uri;

/**
 * Created by markc on 30/03/2017.
 */

public class User {

    public Uri photoUrl;
    public String uid;
    public String name;
    public String email;

    public User() {
    }

    public User(String uid, String email, String name, Uri photoUrl) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.photoUrl = photoUrl;
    }
}
