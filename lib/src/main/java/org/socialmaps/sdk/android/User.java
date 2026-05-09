package org.socialmaps.sdk.android;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

public class User extends SocialMapsObject {
    public final String displayName;

    User(@NonNull JSONObject json) throws JSONException {
        super(json.getLong("id"));
        this.displayName = json.getString("display_name");
    }
}
