package com.g4s8.libcam;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import com.g4s8.libcam.activity.ActCamera;
import java.io.File;
import java.io.IOException;

/**
 * Android camera app API.
 *
 * @author Kirill g4s8.public@gmail.com
 */
public class Camera {

    private static final String FOLDER_NAME = "cam_exchange";

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

    @NonNull
    private File tmpFile() throws IOException {
        final File cameraFolder = new File(ctx.getFilesDir(), FOLDER_NAME);
        if (!cameraFolder.exists() && !cameraFolder.mkdirs()) {
            throw new IOException("Can't create camera folder");
        }
        return File.createTempFile("cam", ".bitmap", cameraFolder);
    }

    @NonNull
    private Uri contentUri(@NonNull final File file) {
        return FileProvider.getUriForFile(this.ctx, this.authority, file);
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
        final File file = tmpFile();
        ActCamera.start(
            this.ctx,
            contentUri(file),
            output,
            file,
            callback
        );
    }
}