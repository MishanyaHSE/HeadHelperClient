package com.example.headachediary.data.source.network;

import android.content.Context;

import com.example.headachediary.data.source.util.managers.CredentialManager;
import com.example.headachediary.data.source.util.managers.TokenManager;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String BASE_URL = "http://89.111.171.221:8000/";
    private static Retrofit retrofit = null;

    public static HeadacheAPI getApiService(Context context) {
        TokenManager tokenManager = new TokenManager(context);
        CredentialManager credentialManager = new CredentialManager(context);
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        AuthInterceptor authInterceptor = new AuthInterceptor(tokenManager, context);
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        RefreshInterceptor refreshInterceptor = new RefreshInterceptor(context,
                tokenManager, credentialManager);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .addInterceptor(authInterceptor)
                .addInterceptor(refreshInterceptor)
                .build();
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(HeadacheAPI.class);
    }

    public static HeadacheAPI getApiServiceWithoutRefreshInterceptor(Context context) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);


        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(HeadacheAPI.class);
    }
}
