package com.argandevteam.fpes.model;

import android.net.Uri;

/**
 * Created by markc on 30/03/2017.
 */

public class User {

    public Uri profilePhoto;
    public String name;
    public String email;

    public User() {
    }

    public User(String email) {
        this.email = email;
    }
}
