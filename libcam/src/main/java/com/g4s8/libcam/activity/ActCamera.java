package com.g4s8.libcam.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * Camera app interaction activity.
 */
public final class ActCamera extends Activity {

    private static final int REQUEST_ID = ActCamera.class.hashCode() & 0x0000FFFF;

    private static final String ARG_CALLBACK = "callback";
    private static final String ARG_EXCHANGE = "exchange";
    private static final String ARG_TMP = "temp";
    private static final String ARG_OUTPUT = "output";

    private Intent callback;
    private File tmp;
    private File output;

    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);
        this.callback = Intent.class.cast(
            getIntent().getParcelableExtra(ARG_CALLBACK)
        );
        final Uri exchange = Uri.class.cast(
            getIntent().getParcelableExtra(ARG_EXCHANGE)
        );
        this.tmp = new File(
            getIntent().getStringExtra(ARG_TMP)
        );
        this.output = new File(
            getIntent().getStringExtra(ARG_OUTPUT)
        );
        final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            .putExtra(MediaStore.EXTRA_OUTPUT, exchange);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            final List<ResolveInfo> infoList = getPackageManager()
                .queryIntentActivities(
                    intent,
                    PackageManager.MATCH_DEFAULT_ONLY
                );
            for (final ResolveInfo info : infoList) {
                grantUriPermission(
                    info.activityInfo.packageName,
                    exchange,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                );
            }
        } else {
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }
        startActivityForResult(
            intent,
            REQUEST_ID
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ID) {
            if (output.exists()) {
                if (!output.delete()) {
                    Log.w("LibCam", "Failed to clean output file");
                    return;
                }
            }
            try {
                copy(tmp, output);
            } catch (IOException e) {
                Log.w("LibCam", "Failed to copy image to output");
                return;
            }
            if (!tmp.delete()) {
                Log.w("LibCam", "Failed to delete temporary file");
            }
            LocalBroadcastManager.getInstance(this).sendBroadcast(callback);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    /**
     * Start activity.
     *
     * @param ctx      context
     * @param exchange exchange uri
     * @param output   output file
     * @param tmp      temporary file
     * @param callback callback
     */
    public static void start(
        @NonNull final Context ctx,
        @NonNull final Uri exchange,
        @NonNull final File output,
        @NonNull final File tmp,
        @NonNull final Intent callback
    ) {
        ctx.startActivity(
            new Intent(ctx, ActCamera.class)
                .putExtra(ARG_EXCHANGE, exchange)
                .putExtra(ARG_OUTPUT, output.getAbsolutePath())
                .putExtra(ARG_TMP, tmp.getAbsolutePath())
                .putExtra(ARG_CALLBACK, callback)
        );
    }

    //CHECKSTYLE: OFF
    // got it here https://stackoverflow.com/questions/9292954/how-to-make-a-copy-of-a-file-in-android
    private static void copy(File src, File dst) throws IOException {

        final InputStream in = new FileInputStream(src);
        final OutputStream out = new FileOutputStream(dst);

        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }
    //CHECKSTYLE: ON
}