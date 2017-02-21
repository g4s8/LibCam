package com.g4s8.libcam;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import java.util.List;

/**
 * Intent factory for old devices.
 *
 * @author g4s8
 */
final class IntentFactoryOld implements IntentFactory {

    private final Context context;

    /**
     * Ctor.
     *
     * @param context android context
     */
    IntentFactoryOld(@NonNull final Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public Intent intent(@NonNull final Uri exchange) {
        final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            .putExtra(MediaStore.EXTRA_OUTPUT, exchange);
        //see https://stackoverflow.com/questions/32789027
        for (final ResolveInfo info : infos(intent)) {
            context.grantUriPermission(
                info.activityInfo.packageName,
                exchange,
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION
            );
        }
        return intent;
    }

    private List<ResolveInfo> infos(@NonNull final Intent intent) {
        return this.context.getPackageManager()
            .queryIntentActivities(
                intent,
                PackageManager.MATCH_DEFAULT_ONLY
            );
    }
}
