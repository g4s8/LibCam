package com.g4s8.libcam;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import java.io.IOException;

/**
 * Android camera app API.
 *
 * @author Kirill g4s8.public@gmail.com
 */
public class Camera {

    private final Context context;
    private final IntentFactory intentFactory;
    private final Exchange exchange;

    /**
     * Ctor.
     *
     * @param context android context
     */
    public Camera(@NonNull final Context context) {
        this(
            context,
            new IntentFactoryForSdk(context),
            new Exchange(context)
        );
    }

    /**
     * Ctor.
     *
     * @param context       launcher context
     * @param exchange      exchange dir
     * @param intentFactory camera intent factory
     */
    private Camera(
        @NonNull final Context context,
        @NonNull final IntentFactory intentFactory,
        @NonNull final Exchange exchange
    ) {
        this.context = context;
        this.intentFactory = intentFactory;
        this.exchange = exchange;
    }

    /**
     * Create camera app intent with provided authority.
     *
     * @param authority local file provider authority
     * @return intent to take a photo from camera
     * @throws IOException if failed to create exchange file with provided authority
     */
    @NonNull
    public final Intent intent(
        @NonNull final Authority authority
    ) throws IOException {
        return intentFactory.intent(
            exchange.uri(authority)
        );
    }
}
