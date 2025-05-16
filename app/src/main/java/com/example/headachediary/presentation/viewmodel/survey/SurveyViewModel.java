package com.example.headachediary.presentation.viewmodel.survey;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.headachediary.data.repository.NotesRepository;
import com.example.headachediary.data.source.network.ApiCallback;
import com.example.headachediary.domain.Message;
import com.example.headachediary.domain.headache.Medicine;
import com.example.headachediary.domain.headache.NoteCreate;
import com.example.headachediary.domain.headache.NoteResponse;
import com.example.headachediary.domain.headache.QuestionResponse;
import com.example.headachediary.presentation.viewmodel.StateBase;

import java.time.LocalDate;

public class SurveyViewModel extends AndroidViewModel {
    private final MutableLiveData<NoteCreate> surveyLiveData;
    private final MutableLiveData<QuestionResponse> questionsLiveData;

    private final MutableLiveData<Boolean> directionLiveData;

    private final MutableLiveData<StateBase<NoteResponse>> saveNoteState;

    private final MutableLiveData<StateBase<NoteResponse>> getNoteForDateState;

    private NotesRepository notesRepository;

    public SurveyViewModel(@NonNull Application application) {
        super(application);
        surveyLiveData = new MutableLiveData<>();
        questionsLiveData = new MutableLiveData<>();
        notesRepository = new NotesRepository(application.getApplicationContext());
        directionLiveData = new MutableLiveData<>();
        saveNoteState = new MutableLiveData<>();
        getNoteForDateState = new MutableLiveData<>();
    }


    public LiveData<NoteCreate> getSurvey() {
        return surveyLiveData;
    }

    public Boolean moveForward() {
        return directionLiveData.getValue();
    }

    public void updateSurvey(NoteCreate updatedSurvey) {
        surveyLiveData.setValue(updatedSurvey);
    }

    public LiveData<QuestionResponse> getQuestions() {
        return questionsLiveData;
    }


    public void getQuestionsFromServer() {
        notesRepository.getQuestions(new ApiCallback<QuestionResponse>() {
            @Override
            public void onSuccess(QuestionResponse data) {
                questionsLiveData.postValue(data);
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    public void getNoteFromServer(LocalDate date) {
        getNoteForDateState.postValue(StateBase.loading());
        notesRepository.getOneNote(date, new ApiCallback<>() {
            @Override
            public void onSuccess(NoteResponse data) {
                getNoteForDateState.postValue(StateBase.success(data));
            }

            @Override
            public void onError(String error) {
                getNoteForDateState.postValue(StateBase.error(error));
            }
        });
    }

    public LiveData<StateBase<NoteResponse>> getSaveState() {
        return saveNoteState;
    }

    public LiveData<StateBase<NoteResponse>> getLoadingNoteState() {
        return getNoteForDateState;
    }

    public void saveNote(NoteCreate note) {
        saveNoteState.postValue(StateBase.loading());
        notesRepository.saveNote(note, new ApiCallback<>() {
            @Override
            public void onSuccess(NoteResponse data) {
                saveNoteState.postValue(StateBase.success(data));
            }

            @Override
            public void onError(String error) {
                saveNoteState.postValue(StateBase.error(error));
            }
        });
    }

    public void deleteNote(LocalDate date) {
        notesRepository.deleteNoteForDate(date, new ApiCallback<Message>() {
            @Override
            public void onSuccess(Message data) {

            }

            @Override
            public void onError(String error) {

            }
        });
    }

    public void addMedicine(Medicine medicine) {
        NoteCreate current = surveyLiveData.getValue();
        if (current != null) {
            current.getMedicine().add(medicine);
            surveyLiveData.setValue(current);
        }
    }

    public void removeMedicine(int index) {
        NoteCreate current = surveyLiveData.getValue();
        if (current != null && index < current.getMedicine().size()) {
            current.getMedicine().remove(index);
            surveyLiveData.setValue(current);
        }
    }

    public void setDirection(Boolean direction) {
        directionLiveData.postValue(direction);
    }

    @Override
    protected void onCleared() {
        // Очистка ресурсов при уничтожении
        super.onCleared();
    }
}
