package org.socialmaps.sdk.android;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

public class PlaceRatingStats {
    @NonNull
    public final Long count;
    @Nullable
    public final Double likeRatio;
    @NonNull
    public final Double score;

    private PlaceRatingStats(@NonNull Long count, @Nullable Double likeRatio, @NonNull Double score) {
        this.count = count;
        this.likeRatio = likeRatio;
        this.score = score;
    }

    static PlaceRatingStats fromJSON(JSONObject json) throws JSONException {
        double likeRatio = json.optDouble("like_ratio");

        return new PlaceRatingStats(
                json.getLong("count"),
                Double.isNaN(likeRatio) ? null : likeRatio,
                json.getDouble("score")
        );
    }
}
