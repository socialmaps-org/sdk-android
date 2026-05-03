package org.socialmaps.sdk.android;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

public class UserRef extends SocialMapsObject {
    @Nullable
    private final User user;

    UserRef(@NonNull JSONObject json) throws JSONException {
        super(ObjectType.USER, json.getLong("id"));
        if (json.optString("object").equals("user")) {
            this.user = new User(json);
        } else {
            this.user = null;
        }
    }

    @Nullable
    public User get() {
        return this.user;
    }
}
