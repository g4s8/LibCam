package com.g4s8.libcam;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;

/**
 * Camera intent factory for current sdk version.
 *
 * @author g4s8
 */
final class IntentFactoryForSdk extends IntentFactory.Wrap {

    /**
     * Ctor.
     *
     * @param ctx android context
     */
    IntentFactoryForSdk(@NonNull final Context ctx) {
        super(origin(Build.VERSION.SDK_INT, ctx));
    }

    private static IntentFactory origin(final int sdk, @NonNull final Context ctx) {
        if (sdk >= Build.VERSION_CODES.LOLLIPOP) {
            return new IntentFactoryLollipop();
        } else {
            return new IntentFactoryOld(ctx);
        }
    }
}
