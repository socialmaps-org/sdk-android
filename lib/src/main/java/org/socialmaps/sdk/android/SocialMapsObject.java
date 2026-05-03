package org.socialmaps.sdk.android;

import androidx.annotation.NonNull;

public abstract class SocialMapsObject {
    @NonNull
    public final Long id;
    @NonNull
    public final ObjectType object;

    SocialMapsObject(ObjectType object, Long id) {
        this.object = object;
        this.id = id;
    }
}
