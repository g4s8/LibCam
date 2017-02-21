package com.g4s8.libcam;

import android.support.annotation.NonNull;

/**
 * Local file provider authority.
 *
 * @author g4s8
 */
public final class Authority {

    private final String string;

    /**
     * Ctor.
     *
     * @param string file provider authority string
     */
    public Authority(@NonNull final String string) {
        this.string = string;
    }

    @Override
    public String toString() {
        return this.string;
    }
}
