package com.g4s8.libcam;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;

/**
 * Intent factory for >= 'Lollipop' version devices
 *
 * @author g4s8
 */
final class IntentFactoryLollipop implements IntentFactory {

    @NonNull
    @Override
    public Intent intent(@NonNull final Uri exchange) {
        return new Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            .putExtra(MediaStore.EXTRA_OUTPUT, exchange)
            .addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
    }
}
