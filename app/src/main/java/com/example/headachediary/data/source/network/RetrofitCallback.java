package com.example.headachediary.data.source.network;

import android.content.Context;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RetrofitCallback<T> implements Callback<T> {

    ApiCallback<T> apiCallback;

    public RetrofitCallback(ApiCallback<T> callback) {
        apiCallback = callback;
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (response.isSuccessful() && response.body() != null){
            apiCallback.onSuccess(response.body());
        } else {
            apiCallback.onError(String.valueOf(response.code()));
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        apiCallback.onError(t.getMessage());
    }
}
