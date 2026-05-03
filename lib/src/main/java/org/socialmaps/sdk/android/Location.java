package org.socialmaps.sdk.android;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

public class Location {
    @NonNull
    public final Double lat;
    @NonNull
    public final Double lon;

    public Location(@NonNull Double lat, @NonNull Double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    @NonNull
    static public Location fromJSON(JSONObject json) throws JSONException {
        return new Location(json.getDouble("lat"), json.getDouble("lon"));
    }
}
