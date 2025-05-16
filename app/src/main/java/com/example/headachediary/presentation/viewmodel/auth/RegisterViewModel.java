package com.example.headachediary.presentation.viewmodel.auth;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.headachediary.data.repository.Repository;
import com.example.headachediary.data.source.network.ApiCallback;
import com.example.headachediary.domain.auth.UserCreate;
import com.example.headachediary.domain.auth.UserResponse;
import com.example.headachediary.presentation.viewmodel.StateBase;


public class RegisterViewModel extends AndroidViewModel {
    private Repository repository;

    private MutableLiveData<StateBase<UserResponse>> registerState;

    public RegisterViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application.getApplicationContext());
        registerState = new MutableLiveData<>();
    }

    public LiveData<StateBase<UserResponse>> getAuthState() {
        return registerState;
    }

    public void registerUser(UserCreate userCreate) {
        registerState.postValue(StateBase.loading());
        repository.registerUser1(userCreate, (new ApiCallback<UserResponse>() {

            @Override
            public void onSuccess(UserResponse data) {
                registerState.postValue(StateBase.success(data));
            }

            @Override
            public void onError(String error) {
                registerState.postValue(StateBase.error(error));
            }
        }));
    }


}
