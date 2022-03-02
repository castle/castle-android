/*
 * Copyright (c) 2020 Castle
 */

package io.castle.android.api;

import java.util.concurrent.TimeUnit;

import io.castle.android.Castle;
import io.castle.android.Utils;
import io.castle.android.api.model.Monitor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * API Service for Castle APIs, do not use directly, use suitable method in {@link io.castle.android.Castle}
 */
public class CastleAPIService {

    private static final int CONNTECTION_TIMEOUT = 10;
    private static CastleAPI instance;

    public interface CastleAPI {
        @POST("monitor")
        public Call<Void> monitor(@Body Monitor monitor);
    }

    /**
     * Get Instance of CastleAPI
     * @return Instance of CastleAPI
     */
    public static CastleAPI getInstance() {
        if (instance == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder()
                                .addInterceptor(new CastleAuthenticationInterceptor())
                    .connectTimeout(CONNTECTION_TIMEOUT, TimeUnit.SECONDS);

            if (Castle.debugLoggingEnabled()) {
                HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
                loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                builder.addInterceptor(loggingInterceptor);
            }

            OkHttpClient okHttpClient = builder.build();

            Retrofit retrofit =
                new Retrofit.Builder()
                            .baseUrl(Castle.baseUrl())
                            .addConverterFactory(GsonConverterFactory.create(Utils.getGsonInstance()))
                            .client(okHttpClient)
                            .build();

            instance = retrofit.create(CastleAPI.class);
        }

        return instance;
    }
}