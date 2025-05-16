package com.example.headachediary.presentation.viewmodel.auth;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.headachediary.data.repository.Repository;
import com.example.headachediary.data.source.network.ApiCallback;
import com.example.headachediary.domain.auth.PasswordReset;
import com.example.headachediary.presentation.viewmodel.StateBase;

import okhttp3.ResponseBody;

public class ResetViewModel extends AndroidViewModel {

    private MutableLiveData<String> email;
    private Repository repository;
    private MutableLiveData<StateBase<ResponseBody>> forgotPasswordState;
    private MutableLiveData<StateBase<ResponseBody>> resetPasswordState;
    public ResetViewModel(@NonNull Application application) {
        super(application);
        email = new MutableLiveData<>();
        repository = new Repository(application.getApplicationContext());
        forgotPasswordState = new MutableLiveData<>();
        resetPasswordState = new MutableLiveData<>();
    }

    public LiveData<String> getResetEmail() {
        return email;
    }

    public LiveData<StateBase<ResponseBody>> getForgotState() {
        return forgotPasswordState;
    }

    public LiveData<StateBase<ResponseBody>> getResetState() {
        return resetPasswordState;
    }

    public void setEmail(String email) {
        this.email.postValue(email);
    }

    public void forgotPassword(String email) {
        forgotPasswordState.postValue(StateBase.loading());
        repository.forgotPassword(email, new ApiCallback<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody data) {
                forgotPasswordState.postValue(StateBase.success(data));
            }

            @Override
            public void onError(String error) {
                forgotPasswordState.postValue(StateBase.error(error));
            }
        });
    }

    public void resetPassword(PasswordReset passwordReset) {
        resetPasswordState.postValue(StateBase.loading());
        repository.resetPassword(passwordReset, new ApiCallback<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody data) {
                resetPasswordState.postValue(StateBase.success(data));
            }

            @Override
            public void onError(String error) {
                resetPasswordState.postValue(StateBase.error(error));
            }
        });
    }
}
