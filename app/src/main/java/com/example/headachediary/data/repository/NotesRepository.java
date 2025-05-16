package com.example.headachediary.data.repository;

import android.content.Context;

import com.example.headachediary.data.source.network.ApiCallback;
import com.example.headachediary.data.source.network.HeadacheAPI;
import com.example.headachediary.data.source.network.RetrofitCallback;
import com.example.headachediary.data.source.network.RetrofitClient;
import com.example.headachediary.domain.Message;
import com.example.headachediary.domain.headache.NoteCreate;
import com.example.headachediary.domain.statistics.StatisticsCreate;
import com.example.headachediary.domain.headache.NoteResponse;
import com.example.headachediary.domain.headache.QuestionData;
import com.example.headachediary.domain.headache.QuestionResponse;
import com.example.headachediary.domain.headache.ReportCreate;
import com.example.headachediary.domain.statistics.StatisticsResponse;

import java.time.LocalDate;
import java.util.ArrayList;

import okhttp3.ResponseBody;


public class NotesRepository {

    private Context context;
    private final HeadacheAPI apiService;



    public NotesRepository(Context context) {
        this.apiService = RetrofitClient.getApiService(context);
        this.context = context;
    }


    public void getNotesForMonth(LocalDate date, ApiCallback<ArrayList<NoteResponse>> call) {
        apiService.getNotesForMonth(date).enqueue(new RetrofitCallback<>(call));
    }

    public void deleteNoteForDate(LocalDate date, ApiCallback<Message> call) {
        apiService.deleteNoteForDate(date).enqueue(new RetrofitCallback<>(call));
    }

    public void getQuestions(ApiCallback<QuestionResponse> call) {
        apiService.getQuestionsForUser().enqueue(new RetrofitCallback<>(call));
    }

    public void saveQuestions(QuestionData questions, ApiCallback<QuestionResponse> call) {
        apiService.saveQuestionsForUser(questions).enqueue(new RetrofitCallback<>(call));
    }

    public void getReport(ReportCreate reportCreate, ApiCallback<ResponseBody> call) {
        apiService.getReport(reportCreate).enqueue(new RetrofitCallback<>(call));
    }

    public void getStatistics(StatisticsCreate statisticsCreate, ApiCallback<StatisticsResponse> call) {
        apiService.getStatistics(statisticsCreate).enqueue(new RetrofitCallback<>(call));
    }

    public void saveNote(NoteCreate noteCreate, ApiCallback<NoteResponse> call) {
        apiService.saveNote(noteCreate).enqueue(new RetrofitCallback<>(call));
    }

    public void getOneNote(LocalDate date, ApiCallback<NoteResponse> call) {
        apiService.getOneNoteFromServer(date).enqueue(new RetrofitCallback<>(call));
    }

}
