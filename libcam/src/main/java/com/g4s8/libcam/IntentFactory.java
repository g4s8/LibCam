package com.g4s8.libcam;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;

/**
 * Camera intent factory.
 *
 * @author g4s8
 */
interface IntentFactory {

    /**
     * New camera intent with specified exchange dir uri.
     *
     * @param exchange Uri to exchange dir
     * @return Camera intent
     */
    @NonNull
    Intent intent(@NonNull final Uri exchange);

    /**
     * Decorator.
     */
    @SuppressWarnings({"PMD.AbstractNaming", "PMD.ShortClassName"})
    abstract class Wrap implements IntentFactory {

        private final IntentFactory origin;

        /**
         * Ctor.
         *
         * @param origin decorating object.
         */
        public Wrap(@NonNull final IntentFactory origin) {
            this.origin = origin;
        }

        @NonNull
        @Override
        public Intent intent(@NonNull final Uri exchange) {
            return this.origin.intent(exchange);
        }
    }
}
