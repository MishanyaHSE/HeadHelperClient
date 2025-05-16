package com.example.headachediary.data.source.network;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.headachediary.data.source.util.HeadacheApplication;
import com.example.headachediary.data.source.util.managers.CredentialManager;
import com.example.headachediary.data.source.util.managers.TokenManager;
import com.example.headachediary.domain.auth.TokenResponse;
import com.example.headachediary.presentation.view.auth.AuthActivity;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class RefreshInterceptor implements Interceptor {

    private Context context;
    private TokenManager tokenManager;
    private CredentialManager credentialManager;


    public RefreshInterceptor(Context context, TokenManager tokenManager, CredentialManager credentialManager) {
        this.context = context;
        this.tokenManager = tokenManager;
        this.credentialManager = credentialManager;
    }


    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request originalRequest = chain.request();
        Response response = chain.proceed(originalRequest);
        Request newRequest;
        Log.d("REFRESING", "Start refresh");
        Log.d("ERROR CODE", String.valueOf(response.message()));
        if (response.code() == 401) {
            Log.d("REFRESING", "Response executed");
            String auth = "Bearer " + tokenManager.getRefreshToken();
            if (tokenManager.getRefreshToken() == null) {
                refreshFailed();
                return response;
            }
            response.close();

            HeadacheAPI headacheAPI = RetrofitClient.getApiServiceWithoutRefreshInterceptor(context);
            // Синхронный запрос на обновление токена
            retrofit2.Response<TokenResponse> refreshResponse = headacheAPI.refreshToken(auth).execute();
            if (refreshResponse.isSuccessful()){
                Log.d("REFRESING", "Success");
                tokenManager.saveToken(refreshResponse.body().getAccessToken(),
                        refreshResponse.body().getRefreshToken());

                newRequest = originalRequest.newBuilder()
                        .header("Authorization", "Bearer " + tokenManager.getToken())
                        .build();
                return chain.proceed(newRequest);
            } else {
                Log.d("REFRESING", "Error");
                refreshFailed();
            }
        }
        return response;
    }

    private void refreshFailed() {
        Log.d("ERROR IN REFRESH", "FOUND");
        tokenManager.deleteTokens();
        Log.d("ERROR IN REFRESH", HeadacheApplication.getCurrentActivity().getClass().getSimpleName());
        if (!HeadacheApplication.getCurrentActivity().getClass().getSimpleName().equals("AuthActivity")) {
            Log.d("ERROR IN REFRESH", "HERE");
            Intent intent = new Intent(context, AuthActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);
        }

    }

}
