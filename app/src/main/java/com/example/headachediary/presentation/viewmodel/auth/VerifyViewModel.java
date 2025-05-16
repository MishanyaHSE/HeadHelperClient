package com.example.headachediary.presentation.viewmodel.auth;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.headachediary.data.repository.Repository;
import com.example.headachediary.data.source.network.ApiCallback;
import com.example.headachediary.domain.Message;
import com.example.headachediary.domain.auth.UserResponse;
import com.example.headachediary.presentation.viewmodel.StateBase;

public class VerifyViewModel extends AndroidViewModel {

    private MutableLiveData<StateBase<Message>> verificationState = new MutableLiveData<>();

    private Repository repository;

    public VerifyViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application.getApplicationContext());
        verificationState = new MutableLiveData<>();
    }

    public LiveData<StateBase<Message>> getVerificationState() {
        return verificationState;
    }


    public void verifyCode(String email, String code) {
        verificationState.postValue(StateBase.loading());
        repository.verifyEmail(email, code, (new ApiCallback<Message>() {

            @Override
            public void onSuccess(Message data) {
                verificationState.postValue(StateBase.success(data));
            }

            @Override
            public void onError(String error) {
                verificationState.postValue(StateBase.error(error));
            }
        }));
    }


}
