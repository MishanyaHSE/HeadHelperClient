package com.example.headachediary.presentation.viewmodel.mainmenu;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.headachediary.data.repository.NotesRepository;
import com.example.headachediary.data.source.network.ApiCallback;
import com.example.headachediary.domain.headache.QuestionData;
import com.example.headachediary.domain.headache.QuestionResponse;
import com.example.headachediary.presentation.viewmodel.StateBase;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuestionsViewModel extends AndroidViewModel {

    private MutableLiveData<StateBase<QuestionResponse>> questionsLoadState;

    private MutableLiveData<StateBase<QuestionData>> questionSaveState;
    private NotesRepository notesRepository;


    public QuestionsViewModel(@NonNull Application application) {
        super(application);
        notesRepository = new NotesRepository(application.getApplicationContext());
        questionsLoadState = new MutableLiveData<>();
        questionSaveState = new MutableLiveData<>();
    }

    public LiveData<StateBase<QuestionResponse>> getLoadQuestionsState() {
        return questionsLoadState;
    }

    public LiveData<StateBase<QuestionData>> getSaveQuestionsState() {
        return questionSaveState;
    }

    public void getQuestions() {
        questionsLoadState.postValue(StateBase.loading());
        notesRepository.getQuestions(new ApiCallback<QuestionResponse>() {
            @Override
            public void onSuccess(QuestionResponse data) {
                questionsLoadState.postValue(StateBase.success(data));

            }

            @Override
            public void onError(String error) {
                questionSaveState.postValue(StateBase.error(error));
            }
        });
    }

    public void updateQuestions(QuestionData questions) {
        questionSaveState.postValue(StateBase.loading());
        notesRepository.saveQuestions(questions, new ApiCallback<QuestionResponse>() {
            @Override
            public void onSuccess(QuestionResponse data) {
                questionSaveState.postValue(StateBase.success(data));
            }

            @Override
            public void onError(String error) {
                questionSaveState.postValue(StateBase.error(error));
            }

        });
    }
}
