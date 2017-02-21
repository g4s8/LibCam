package com.g4s8.libcam;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import java.io.File;
import java.io.IOException;

/**
 * Camera exchange.
 *
 * @author g4s8
 */
final class Exchange {

    /**
     * Same value as in local file provider paths xml.
     * See manifest.
     */
    private static final String DIR = "cam_exchange";

    private static final String PREFIX = "cam";
    private static final String SUFFIX = ".tmp";

    private final Context context;

    /**
     * Ctor.
     *
     * @param context android context
     */
    Exchange(@NonNull final Context context) {
        this.context = context;
    }

    @NonNull
    final Uri uri(@NonNull final Authority authority) throws IOException {
        return FileProvider.getUriForFile(
            this.context,
            authority.toString(),
            File.createTempFile(
                Exchange.PREFIX,
                Exchange.SUFFIX,
                exchangeDir()
            )
        );
    }

    @NonNull
    private File exchangeDir() throws IOException {
        final File dir = new File(this.context.getFilesDir(), DIR);
        if (!dir.exists() && !dir.mkdirs()) {
            throw new IOException("Failed to create exchange dir");
        }
        return dir;
    }
}
