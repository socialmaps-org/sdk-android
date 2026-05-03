package org.socialmaps.sdk.android;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class Review extends SocialMapsObject {
    @NonNull
    public final String comment;
    @NonNull
    public final Date created;
    @NonNull
    public final Boolean liked;
    @NonNull
    public final Long nLikes;
    @NonNull
    public final PlaceRef placeRef;
    @NonNull
    public final UserRef userRef;


    Review(JSONObject json) throws JSONException {
        super(ObjectType.REVIEW, json.getLong("id"));
        this.comment = json.getString("comment");
        this.created = new Date(json.getLong("created") * 1_000L);
        this.liked = json.getBoolean("liked");
        this.nLikes = json.getLong("n_likes");
        this.placeRef = new PlaceRef(json.getJSONObject("place"));
        this.userRef = new UserRef(json.getJSONObject("user"));
    }
}
