/*
 * Copyright (c) 2017 Castle
 */

package io.castle.android;

import java.io.IOException;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

class CastleInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();

        Request.Builder builder = originalRequest.newBuilder();

        Map<String,String> headers = Castle.headers(chain.request().url().toString());

        for (Map.Entry<String, String> entry : headers.entrySet()) {
            builder.addHeader(entry.getKey(), entry.getValue());
        }

        Request newRequest = builder.build();

        // Force a flush if request to whitelisted url
        if (Castle.isUrlWhiteListed(originalRequest.url().toString())) {
            Castle.flush();
        }

        return chain.proceed(newRequest);
    }
}
