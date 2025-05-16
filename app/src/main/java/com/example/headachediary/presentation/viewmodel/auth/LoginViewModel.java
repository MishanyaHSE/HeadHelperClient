package com.example.headachediary.presentation.viewmodel.auth;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.headachediary.data.repository.Repository;
import com.example.headachediary.data.source.network.ApiCallback;
import com.example.headachediary.data.source.network.RetrofitCallback;
import com.example.headachediary.domain.auth.TokenResponse;
import com.example.headachediary.domain.auth.UserAuth;
import com.example.headachediary.presentation.viewmodel.StateBase;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel extends AndroidViewModel {
    private Repository repository;

    private MutableLiveData<StateBase<TokenResponse>> loginState;

    public LoginViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application.getApplicationContext());
        loginState = new MutableLiveData<>();
    }

    public LiveData<StateBase<TokenResponse>> getLoginState() {
        return loginState;
    }

    public void login(UserAuth auth) {
        loginState.postValue(StateBase.loading());
        repository.login(auth, new ApiCallback<TokenResponse>() {
            @Override
            public void onSuccess(TokenResponse data) {
                loginState.postValue(StateBase.success(data));
            }

            @Override
            public void onError(String error) {
                loginState.postValue(StateBase.error(error));
            }

        });
    }
}

