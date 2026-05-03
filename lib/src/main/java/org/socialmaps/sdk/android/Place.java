package org.socialmaps.sdk.android;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Place extends SocialMapsObject {
    @NonNull
    public final String name;
    @NonNull
    public final Location location;
    @NonNull
    public final PlaceRatingStats ratingStats;
    @NonNull
    public final OsmType osmType;
    @NonNull
    public final Long osmId;
    @NonNull
    public final Map<String, String> osmTags;

    Place(JSONObject json) throws JSONException {
        super(ObjectType.PLACE, json.getLong("id"));
        this.name = json.getString("name");
        this.location = Location.fromJSON(json.getJSONObject("location"));
        this.ratingStats = PlaceRatingStats.fromJSON(json.getJSONObject("rating_stats"));
        this.osmType = OsmType.fromString(json.getString("osm_type"));
        this.osmId = json.getLong("osm_id");
        this.osmTags = parseOsmTags(json.getJSONObject("osm_tags"));
    }

    private Map<String, String> parseOsmTags(JSONObject json) throws JSONException {
        Map<String, String> map = new HashMap<>(json.length());
        for (Iterator<String> it = json.keys(); it.hasNext(); ) {
            String key = it.next();
            map.put(key, json.getString(key));
        }
        return map;
    }
}
