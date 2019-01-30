/*
 * Copyright (c) 2017 Castle
 */

package io.castle.android.api;

import io.castle.android.Utils;
import io.castle.android.api.model.Batch;
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

    private static final String API_URL = "https://api.castle.io/v1/";
    private static CastleAPI instance;

    public interface CastleAPI {
        @POST("batch")
        public Call<Void> batch(@Body Batch batch);
    }

    /**
     * Get Instance of CastleAPI
     * @return Instance of CastleAPI
     */
    public static CastleAPI getInstance() {
        if (instance == null) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient okHttpClient =
                new OkHttpClient.Builder()
                                .addInterceptor(new CastleAuthenticationInterceptor())
                                .addInterceptor(logging)
                                .build();

            Retrofit retrofit =
                new Retrofit.Builder()
                            .baseUrl(API_URL)
                            .addConverterFactory(GsonConverterFactory.create(Utils.getGsonInstance()))
                            .client(okHttpClient)
                            .build();

            instance = retrofit.create(CastleAPI.class);
        }

        return instance;
    }
}