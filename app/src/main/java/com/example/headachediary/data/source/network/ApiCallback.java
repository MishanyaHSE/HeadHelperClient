package com.example.headachediary.data.source.network;

import java.io.IOException;

public interface ApiCallback<T> {
    void onSuccess(T data);
    void onError(String error);
}
