package com.g4s8.libcam;

import android.content.Intent;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;

/**
 * Exchange uri in intent.
 *
 * @author g4s8
 */
public final class IntentUri implements Parcelable {

    /**
     * Parcelable creator.
     */
    public static final Creator<IntentUri> CREATOR = new ParcelIntentUri();

    private final Intent intent;

    /**
     * Ctor.
     *
     * @param intent intent with uri.
     */
    public IntentUri(@NonNull final Intent intent) {
        this.intent = intent;
    }

    /**
     * Exchange uri.
     *
     * @return uri in intent
     */
    public Uri uri() {
        return Uri.class.cast(
            this.intent.getParcelableExtra(MediaStore.EXTRA_OUTPUT)
        );
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeParcelable(this.intent, flags);
    }
}
