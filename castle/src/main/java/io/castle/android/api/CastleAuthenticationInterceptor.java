/*
 * Copyright (c) 2020 Castle
 */

package io.castle.android.api;

import java.io.IOException;

import io.castle.android.Castle;
import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Interceptor used for auth to Castle APIs
 */
class CastleAuthenticationInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();

        Request.Builder builder = originalRequest.newBuilder()
                .header("X-Castle-Publishable-Api-Key", Castle.publishableKey())
                .header(Castle.requestTokenHeaderName, Castle.createRequestToken())
                .header("User-Agent", Castle.userAgent());

        return chain.proceed(builder.build());
    }
}
