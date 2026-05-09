package org.socialmaps.sdk.android;

import androidx.annotation.NonNull;

public abstract class SocialMapsObject {
    @NonNull
    public final Long id;

    SocialMapsObject(Long id) {
        this.id = id;
    }
}
