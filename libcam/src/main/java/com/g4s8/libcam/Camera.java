package com.g4s8.libcam;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import com.g4s8.libcam.activity.ActCamera;
import java.io.File;
import java.io.IOException;

/**
 * Android camera app API.
 *
 * @author Kirill g4s8.public@gmail.com
 */
public class Camera {

    private final Context ctx;
    private final String authority;

    /**
     * Ctor.
     *
     * @param ctx       Context
     * @param authority Provider authority
     */
    public Camera(@NonNull final Context ctx, @NonNull final String authority) {
        this.ctx = ctx;
        this.authority = authority;
    }

    /**
     * Take new photo into provided output file, notify via local broadcast.
     *
     * @param callback Local broadcast intent
     * @param output   Photo output
     * @throws IOException if IO problems
     */
    public void takePhoto(
        @NonNull final Intent callback,
        @NonNull final File output
    ) throws IOException {
        ActCamera.start(
            this.ctx,
            output,
            this.authority,
            callback
        );
    }
}
