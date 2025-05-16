package com.example.headachediary.presentation.viewmodel.mainmenu;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.headachediary.data.repository.NotesRepository;
import com.example.headachediary.data.source.network.ApiCallback;
import com.example.headachediary.domain.statistics.StatisticsCreate;
import com.example.headachediary.domain.statistics.StatisticsResponse;
import com.example.headachediary.presentation.viewmodel.StateBase;

public class StatisticsViewModel extends AndroidViewModel {

    private NotesRepository notesRepository;
    private MutableLiveData<StateBase<StatisticsResponse>> statisticsState;

    public StatisticsViewModel(@NonNull Application application) {
        super(application);
        notesRepository = new NotesRepository(application.getApplicationContext());
        statisticsState = new MutableLiveData<>();
    }

    public LiveData<StateBase<StatisticsResponse>> getStatisticsState() {
        return statisticsState;
    }

    public void getStatistics(StatisticsCreate statisticsCreate) {
        statisticsState.postValue(StateBase.loading());
        notesRepository.getStatistics(statisticsCreate, new ApiCallback<StatisticsResponse>() {
            @Override
            public void onSuccess(StatisticsResponse data) {
                statisticsState.postValue(StateBase.success(data));
            }

            @Override
            public void onError(String error) {
                statisticsState.postValue(StateBase.error(error));
            }
        });
    }


}
