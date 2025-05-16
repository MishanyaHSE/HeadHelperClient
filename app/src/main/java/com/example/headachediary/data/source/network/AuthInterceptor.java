package com.example.headachediary.data.source.network;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.example.headachediary.data.source.util.managers.TokenManager;
import com.example.headachediary.presentation.view.auth.AuthActivity;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {

    TokenManager tokenManager;
    Context context;

    public AuthInterceptor(TokenManager tokenManager, Context context) {
        this.tokenManager = tokenManager;
        this.context = context;
    }

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request originalRequest = chain.request();
        HttpUrl httpUrl = originalRequest.url();
        if (httpUrl.toString().contains("login") || httpUrl.toString().contains("register")) {
            return chain.proceed(originalRequest);
        } else {
            String token = tokenManager.getToken();

            if (token != null) {
                Request newRequest = originalRequest.newBuilder()
                        .header("Authorization", "Bearer " + token)
                        .build();
                return chain.proceed(newRequest);
            } else {
                Intent intent = new Intent(context, AuthActivity.class);
                context.startActivity(intent);
                return chain.proceed(originalRequest);
            }
        }
    }
}
