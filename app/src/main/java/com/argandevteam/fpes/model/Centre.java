package com.argandevteam.fpes.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import com.squareup.picasso.Target;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by markc on 24/11/2016.
 */
@IgnoreExtraProperties
public class Centre implements Parcelable {

    public static final Creator<Centre> CREATOR = new Creator<Centre>() {
        @Override
        public Centre createFromParcel(Parcel in) {
            return new Centre(in);
        }

        @Override
        public Centre[] newArray(int size) {
            return new Centre[size];
        }
    };
    public Target target;
    public String address;
    public int id;
    public double lat;
    public double lon;
    public String province;
    public String city;
    public String nature;
    public String municipality;
    public String specific_den;
    public String generic_den;
    public String thumbnail_url;
    public float sum_ratings;
    public int num_ratings;
    public int postal_code;
    public int num_reviews;
    //    public List<HashMap<String, Boolean>> reviews;
    public HashMap<String, Boolean> reviews;
    public float rating_average;

    public Centre() {
    }

    public Centre(String address, int uid, String province, String city, String nature,
                  String municipality, String thumbnail_url,
                  int postal_code, String specific_den, String generic_den, int num_ratings, int num_reviews, double lat, double lon, HashMap<String, Boolean> reviews) {
        this.id = uid;
        this.address = address;
        this.province = province;
        this.city = city;
        this.nature = nature;
        this.thumbnail_url = thumbnail_url;
        this.municipality = municipality;
        this.postal_code = postal_code;
        this.specific_den = specific_den;
        this.generic_den = generic_den;
        this.num_ratings = num_ratings;
        this.num_reviews = num_reviews;
        this.lat = lat;
        this.lon = lon;
        this.reviews = reviews;
    }

    public Centre(String s, String s1) {
        this.province = s;
        this.specific_den = s1;
    }

    protected Centre(Parcel in) {
        id = in.readInt();
        province = in.readString();
        city = in.readString();
        nature = in.readString();
        municipality = in.readString();
        specific_den = in.readString();
        generic_den = in.readString();
        thumbnail_url = in.readString();
        postal_code = in.readInt();
        num_ratings = in.readInt();
        num_reviews = in.readInt();
        reviews = in.readHashMap(null);
//        in.readList(reviews, null);
    }

    @Override
    public String toString() {
        return super.toString();
    }

    // [START centre_to_map]
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("province", province);
        result.put("city", city);
        result.put("nature", nature);
        result.put("municipality", municipality);
        result.put("specific_den", specific_den);
        result.put("generic_den", generic_den);
        result.put("thumbnail_url", thumbnail_url);
        result.put("num_reviews", num_reviews);
        result.put("num_ratings", num_ratings);
        result.put("lat", lat);
        result.put("lon", lon);
        result.put("reviews", reviews);
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(city);
        dest.writeString(province);
        dest.writeString(nature);
        dest.writeString(municipality);
        dest.writeString(specific_den);
        dest.writeString(generic_den);
        dest.writeInt(num_reviews);
        dest.writeInt(num_ratings);
        dest.writeInt(postal_code);
        dest.writeString(thumbnail_url);
        dest.writeDouble(lat);
        dest.writeDouble(lon);
//        dest.writeList(reviews);
        dest.writeMap(reviews);
    }

    // [END centre_to_map]
}
