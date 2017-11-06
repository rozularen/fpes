package com.argandevteam.fpes.model;

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
    public String description;
    public HashMap<String, Boolean> reviews;

    public User() {
    }

    public User(String uid, String email, String name, String user_image, String description, HashMap<String, Boolean> reviews) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.reviews = reviews;
        this.user_image = user_image;
        this.description = description;
    }
}
