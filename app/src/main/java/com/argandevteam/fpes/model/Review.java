package com.argandevteam.fpes.model;

import android.widget.ImageView;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Date;

/**
 * Created by markc on 30/03/2017.
 */
@IgnoreExtraProperties
public class Review {
    public User user;
    public float rating;
    public String text;
    public String date;
    public Centre centre;

    public Review() {
    }

    public Review(User user, float rating, String text) {
        this.user = user;
        this.rating = rating;
        this.text = text;
    }

    public Review(float rating, String text, String date) {
        this.rating = rating;
        this.text = text;
        this.date = date;
    }

    public Review(User user, float rating, String reviewText, String date) {
        this.user = user;
        this.rating = rating;
        this.text = reviewText;
        this.date = date;
    }

    public Review(User user, Centre centre, float rating, String reviewText, String date) {
        this.user = user;
        this.centre = centre;
        this.rating = rating;
        this.text = reviewText;
        this.date = date;
    }
}

