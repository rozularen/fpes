package com.argandevteam.fpes.model;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by markc on 30/03/2017.
 */
@IgnoreExtraProperties
public class Review {
    public User user;
    public String user_uid;
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

    public Review(String user_uid, float rating, String text, String date) {
        this.user_uid = user_uid;
        this.rating = rating;
        this.text = text;
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

