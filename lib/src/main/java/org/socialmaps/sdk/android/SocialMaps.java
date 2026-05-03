package org.socialmaps.sdk.android;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SocialMaps {
    public static final String DEFAULT_BASE_URL = "https://api.socialmaps.org";
    @NonNull
    private final String baseURL;
    @NonNull
    private String userAgent;

    SocialMaps(@NonNull String userAgent, @NonNull String baseURL) {
        this.userAgent = userAgent;
        this.baseURL = baseURL;
    }

    SocialMaps(@NonNull String userAgent) {
        this(userAgent, DEFAULT_BASE_URL);
    }

    public @NonNull Place lookupPlace(@NonNull String name, @NonNull Double lat, @NonNull Double lon) throws JSONException, IOException {
        Map<String, String> queryParams = Map.of("name", name, "lat", lat.toString(), "lon", lon.toString());
        JSONObject json = Http.get(this.baseURL + "/v1/places/lookup", queryParams);
        return new Place(json);
    }

    public @NonNull List<Place> queryPlaces(@NonNull Double maxLat, @NonNull Double maxLon, @NonNull Double minLat, @NonNull Double minLon, @NonNull String predicate) throws JSONException, IOException {
        Map<String, String> queryParams = Map.of(
                "max_lat", maxLat.toString(),
                "max_lon", maxLon.toString(),
                "min_lat", minLat.toString(),
                "min_lon", minLon.toString(),
                "predicate", predicate
        );
        JSONObject json = Http.get(this.baseURL + "/v1/places", queryParams);
        JSONArray data = json.getJSONArray("data");
        List<Place> places = new ArrayList<>(data.length());
        for (int i = 0; i < data.length(); i++) {
            JSONObject placeJSON = data.getJSONObject(i);
            places.add(new Place(placeJSON));
        }
        return places;
    }

    public @NonNull Place retrievePlace(@NonNull Long placeId) throws JSONException, IOException {
        JSONObject json = Http.get(this.baseURL + "/v1/places/" + placeId);
        return new Place(json);
    }

    public @NonNull List<Review> listReviewsOfPlace(@NonNull Long placeId) throws JSONException, IOException {
        JSONObject json = Http.get(this.baseURL + "/v1/places/" + placeId + "/reviews");
        JSONArray data = json.getJSONArray("data");
        List<Review> reviews = new ArrayList<>(data.length());
        for (int i = 0; i < data.length(); i++) {
            JSONObject placeJSON = data.getJSONObject(i);
            reviews.add(new Review(placeJSON));
        }
        return reviews;
    }

    public @NonNull Review createReview(@NonNull String accessToken, @NonNull Long placeId, @NonNull String comment, @NonNull Boolean liked) throws JSONException, IOException {
        JSONObject json = Http.post(
                this.baseURL + "/v1/places/" + placeId + "/reviews",
                new JSONObject()
                        .put("comment", comment)
                        .put("liked", liked),
                accessToken
        );
        return new Review(json);
    }

    public void deleteReview(@NonNull String accessToken, @NonNull Long reviewId) throws JSONException, IOException {
        Http.delete(
                this.baseURL + "/v1/reviews/" + reviewId,
                accessToken
        );
    }

    public void updateReview(@NonNull String accessToken, @NonNull Long reviewId, @NonNull String comment, @NonNull Boolean liked) throws JSONException, IOException {
        Http.put(
                this.baseURL + "/v1/reviews/" + reviewId,
                new JSONObject()
                        .put("comment", comment)
                        .put("liked", liked),
                accessToken
        );
    }

    public void likeReview(@NonNull String accessToken, @NonNull Long reviewId) throws JSONException, IOException {
        Http.put(
                this.baseURL + "/v1/reviews/" + reviewId + "/like",
                accessToken
        );
    }

    public void unlikeReview(@NonNull String accessToken, @NonNull Long reviewId) throws JSONException, IOException {
        Http.put(
                this.baseURL + "/v1/reviews/" + reviewId + "/unlike",
                accessToken
        );
    }
}
