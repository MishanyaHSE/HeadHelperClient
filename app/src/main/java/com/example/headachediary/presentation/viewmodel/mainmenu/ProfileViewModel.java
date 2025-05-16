package com.example.headachediary.presentation.viewmodel.mainmenu;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.headachediary.data.repository.Repository;
import com.example.headachediary.data.source.network.ApiCallback;
import com.example.headachediary.domain.auth.UserResponse;
import com.example.headachediary.presentation.viewmodel.StateBase;

public class ProfileViewModel extends AndroidViewModel {

    private MutableLiveData<StateBase<UserResponse>> profileState;
    private Repository repository;
    public ProfileViewModel(@NonNull Application application) {
        super(application);
        profileState = new MutableLiveData<>();
        repository = new Repository(application.getApplicationContext());
    }

    public LiveData<StateBase<UserResponse>> getProfileState() {
        return profileState;
    }

    public void getProfileData(){
        profileState.postValue(StateBase.loading());
        repository.getUser(new ApiCallback<UserResponse>() {
            @Override
            public void onSuccess(UserResponse data) {
                profileState.postValue(StateBase.success(data));
            }

            @Override
            public void onError(String error) {
                profileState.postValue(StateBase.error(error));
            }
        });
    }
}
