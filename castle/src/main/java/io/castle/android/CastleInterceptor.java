/*
 * Copyright (c) 2020 Castle
 */

package io.castle.android;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

class CastleInterceptor implements Interceptor {
    @Override
    public @NotNull Response intercept(@NotNull Chain chain) throws IOException {
        Request request = chain.request();

        // Only add headers is url is allowlisted
        if (Castle.isUrlAllowlisted(request.url().toString())) {

            Request.Builder builder = request.newBuilder();

            Map<String,String> headers = Castle.headers(chain.request().url().toString());

            for (Map.Entry<String, String> entry : headers.entrySet()) {
                builder.addHeader(entry.getKey(), entry.getValue());
            }

            request = builder.build();

            Castle.flush();
        }

        return chain.proceed(request);
    }
}
