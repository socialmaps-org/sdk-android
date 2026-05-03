package org.socialmaps.sdk.android;

import androidx.annotation.NonNull;

public enum OsmType {
    NODE,
    RELATION,
    WAY;

    @NonNull
    public static OsmType fromString(@NonNull String value) {
        return switch (value) {
            case "N" -> NODE;
            case "R" -> RELATION;
            case "W" -> WAY;
            default -> throw new Error("unexpected value");
        };
    }
}
