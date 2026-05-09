package org.socialmaps.sdk.android;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

public class PlaceRef extends SocialMapsObject {
    private final Place place;

    PlaceRef(@NonNull Place place) {
        super(place.id);
        this.place = place;
    }

    PlaceRef(@NonNull JSONObject json) throws JSONException {
        super(json.getLong("id"));
        this.place = null;
    }

    @Nullable
    public Place get() {
        return this.place;
    }
}
