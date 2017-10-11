package io.castle.android.api;

import java.io.IOException;

import io.castle.android.Castle;
import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Copyright (c) 2017 Castle
 */

class CastleAuthenticationInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();

        Request.Builder builder = originalRequest.newBuilder()
                .header("Authorization", Credentials.basic("", Castle.publishableKey()))
                .header("X-Castle-Client-Id", Castle.deviceIdentifier());

        return chain.proceed(builder.build());
    }
}
