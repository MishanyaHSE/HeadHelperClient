package com.example.headachediary.data.source.network;


import com.example.headachediary.domain.Message;
import com.example.headachediary.domain.auth.PasswordReset;
import com.example.headachediary.domain.headache.NoteCreate;
import com.example.headachediary.domain.statistics.StatisticsCreate;
import com.example.headachediary.domain.headache.NoteResponse;
import com.example.headachediary.domain.auth.TokenResponse;
import com.example.headachediary.domain.auth.UserAuth;
import com.example.headachediary.domain.auth.UserCreate;
import com.example.headachediary.domain.auth.UserResponse;
import com.example.headachediary.domain.headache.QuestionData;
import com.example.headachediary.domain.headache.QuestionResponse;
import com.example.headachediary.domain.headache.ReportCreate;
import com.example.headachediary.domain.statistics.StatisticsResponse;

import java.time.LocalDate;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;


public interface HeadacheAPI {
    @POST("register")
    Call<UserResponse> registerUser(@Body UserCreate user);

    @POST("register/{email}/{code}")
    Call<Message> verifyEmail(@Path("email") String email, @Path("code") String code);


    @POST("login")
    Call<TokenResponse> login(@Body UserAuth user);

//    @POST("login/1")
//    MutableLiveData<TokenResponse> login1(@Body UserAuth user);

    @GET("users/me")
    Call<UserResponse> getCurrentUser();

    @POST("/login/refresh")
    Call<TokenResponse> refreshToken(@Header("Authorization") String token);

    @GET("users/notes/{date}")
    Call<ArrayList<NoteResponse>> getNotesForMonth(@Path("date") LocalDate date);

    @DELETE("/users/notes/one/{date}/")
    Call<Message> deleteNoteForDate(@Path("date") LocalDate date);

    @GET("/users/notes/one/{date}/")
    Call<NoteResponse> getOneNoteFromServer(@Path("date") LocalDate date);

    @GET("users/questions")
    Call<QuestionResponse> getQuestionsForUser();

    @PUT("users/questions")
    Call<QuestionResponse> saveQuestionsForUser(@Body QuestionData questions);

    @POST("users/report")
    Call<ResponseBody> getReport(@Body ReportCreate reportCreate);

    @POST("users/statistics")
    Call<StatisticsResponse> getStatistics(@Body StatisticsCreate reportCreate);

    @POST("users/notes")
    Call<NoteResponse> saveNote(@Body NoteCreate noteCreate);

    @POST("/users/forgot-password/{email}")
    Call<ResponseBody> forgotPassword(@Path("email") String email);

    @PATCH("/users/reset-password")
    Call<ResponseBody> resetPassword(@Body PasswordReset passwordReset);


}
