package com.argandevteam.fpes.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by markc on 24/11/2016.
 */
@IgnoreExtraProperties
public class Centre {

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

    public Centre(String uid, String province, String city, String nature,
                  String type, String municipality, int center_code,
                  int postal_code, String specific_den, String generic_den) {
        this.uid = uid;
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
    // [END centre_to_map]
}
