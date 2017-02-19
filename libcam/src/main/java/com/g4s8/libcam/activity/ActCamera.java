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
import android.support.v4.content.FileProvider;
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

    private static final String FOLDER_NAME = "cam_exchange";

    private static final int REQUEST_ID = ActCamera.class.hashCode() & 0x0000FFFF;

    private static final String ARG_CALLBACK = "callback";
    private static final String ARG_OUTPUT = "output";
    private static final String ARG_AUTHORITY = "authority";

    private static final String SS_TMP = "tmp";

    private Intent callback;
    private File tmp;
    private File output;

    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);
        this.callback = Intent.class.cast(getIntent().getParcelableExtra(ARG_CALLBACK));
        if (state == null) {
            tmp = tmpFile();
        } else {
            final String tmpPath = state.getString(SS_TMP);
            if (tmpPath == null) {
                throw new RuntimeException("tmpPath must not be null");
            }
            tmp = new File(tmpPath);
        }
        this.output = new File(getIntent().getStringExtra(ARG_OUTPUT));
        startActivityForResult(
            intent(
                FileProvider.getUriForFile(
                    this,
                    getIntent().getStringExtra(ARG_AUTHORITY),
                    this.tmp
                )
            ),
            REQUEST_ID
        );
    }

    private Intent intent(@NonNull final Uri exchange) {
        final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            .putExtra(MediaStore.EXTRA_OUTPUT, exchange);

        //see https://stackoverflow.com/questions/32789027
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            final List<ResolveInfo> infos = getPackageManager()
                .queryIntentActivities(
                    intent,
                    PackageManager.MATCH_DEFAULT_ONLY
                );
            for (final ResolveInfo info : infos) {
                grantUriPermission(
                    info.activityInfo.packageName,
                    exchange,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                );
            }
        } else {
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }
        return intent;
    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        outState.putString(SS_TMP, this.tmp.getAbsolutePath());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ID) {
            if (output.exists() && !output.delete()) {
                Log.w("LibCam", "Failed to clean output file");
                return;
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

    @NonNull
    private File tmpFile() {
        final File cameraFolder = new File(getFilesDir(), FOLDER_NAME);
        if (!cameraFolder.exists() && !cameraFolder.mkdirs()) {
            throw new RuntimeException("Can't create camera folder");
        }
        try {
            return File.createTempFile("cam", ".bitmap", cameraFolder);
        } catch (IOException e) {
            throw new RuntimeException("Failed to create new temp file", e);
        }
    }

    /**
     * Start activity.
     *
     * @param ctx       context
     * @param output    output file
     * @param authority authority
     * @param callback  callback
     */
    public static void start(
        @NonNull final Context ctx,
        @NonNull final File output,
        @NonNull final String authority,
        @NonNull final Intent callback
    ) {
        ctx.startActivity(
            new Intent(ctx, ActCamera.class)
                .putExtra(ARG_OUTPUT, output.getAbsolutePath())
                .putExtra(ARG_AUTHORITY, authority)
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
