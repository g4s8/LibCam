package com.g4s8.libcam;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Intent uri parcelable creator.
 *
 * @author g4s8
 */
final class ParcelIntentUri implements Parcelable.Creator<IntentUri> {

    @Override
    public IntentUri createFromParcel(Parcel in) {
        return new IntentUri(
            in.<Intent>readParcelable(
                Intent.class.getClassLoader()
            )
        );
    }

    @Override
    public IntentUri[] newArray(int size) {
        return new IntentUri[size];
    }
}
