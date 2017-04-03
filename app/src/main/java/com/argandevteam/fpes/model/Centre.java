package com.argandevteam.fpes.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by markc on 24/11/2016.
 */
@IgnoreExtraProperties
public class Centre implements Parcelable{

    public String address;
    public String uid;
    public String province;
    public String city;
    public String nature;
    public String type;
    public String municipality;
    public int center_code;
    public int postal_code;
    public String specific_den;
    public String generic_den;

    public Centre() {
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public Centre(String address, String uid, String province, String city, String nature,
                  String type, String municipality, int center_code,
                  int postal_code, String specific_den, String generic_den) {
        this.uid = uid;
        this.address = address;
        this.province = province;
        this.city = city;
        this.nature = nature;
        this.type = type;
        this.municipality = municipality;
        this.center_code = center_code;
        this.postal_code = postal_code;
        this.specific_den = specific_den;
        this.generic_den = generic_den;
    }

    public Centre(String s, String s1) {
        this.province = s;
        this.specific_den = s1;
    }

    protected Centre(Parcel in) {
        uid = in.readString();
        province = in.readString();
        city = in.readString();
        nature = in.readString();
        type = in.readString();
        municipality = in.readString();
        specific_den = in.readString();
        generic_den = in.readString();
        center_code = in.readInt();
        postal_code = in.readInt();
    }

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

    // [START centre_to_map]
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("province", province);
        result.put("city", city);
        result.put("nature", nature);
        result.put("type", type);
        result.put("municipality", municipality);
        result.put("center_code",center_code);
        result.put("specific_den", specific_den);
        result.put("generic_den", generic_den);

        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uid);
        dest.writeString(city);
        dest.writeString(province);
        dest.writeString(nature);
        dest.writeString(type);
        dest.writeString(municipality);
        dest.writeString(specific_den);
        dest.writeString(generic_den);
        dest.writeInt(center_code);
        dest.writeInt(postal_code);
    }
    // [END centre_to_map]
}
