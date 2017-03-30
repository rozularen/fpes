package com.argandevteam.fpes.model;

import android.widget.ImageView;

/**
 * Created by markc on 30/03/2017.
 */

public class Review {
    public User user;
    public float rating;
    public String text;

    public Review() {
    }

    public Review(User user, float rating, String text) {
        this.user = user;
        this.rating = rating;
        this.text = text;
    }
}

