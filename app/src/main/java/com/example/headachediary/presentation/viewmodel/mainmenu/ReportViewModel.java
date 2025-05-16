package com.example.headachediary.presentation.viewmodel.mainmenu;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.headachediary.data.repository.NotesRepository;
import com.example.headachediary.data.repository.Repository;
import com.example.headachediary.data.source.network.ApiCallback;
import com.example.headachediary.domain.Message;
import com.example.headachediary.domain.auth.UserResponse;
import com.example.headachediary.domain.headache.ReportCreate;
import com.example.headachediary.presentation.viewmodel.StateBase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.concurrent.Executors;

import okhttp3.ResponseBody;

public class ReportViewModel extends AndroidViewModel {

    private MutableLiveData<StateBase<Message>> reportState;

    private NotesRepository notesRepository;
    public ReportViewModel(@NonNull Application application) {
        super(application);
        reportState = new MutableLiveData<>();
        notesRepository = new NotesRepository(application.getApplicationContext());
    }

    public LiveData<StateBase<Message>> getReportState() {
        return reportState;
    }

    public void getReport(ReportCreate reportCreate, Boolean saveLocal) {
        reportState.postValue(StateBase.loading());
        Executors.newSingleThreadExecutor().execute(() -> {
            notesRepository.getReport(reportCreate, new ApiCallback<ResponseBody>() {

                @Override
                public void onSuccess(ResponseBody data) {
                    if (saveLocal) {
                        String format = ".pdf";
                        Log.d("CONTEXT TYPE", String.valueOf(data.contentType()));
                        if (String.valueOf(data.contentType()).contains("text/csv")){
                            format = ".csv";
                            Log.d("SAVING REPORT", "Changed format");
                        }
                        File file = saveFile(data.byteStream(), format);
                        reportState.postValue(StateBase.success("Сохранено в "+ file.getAbsolutePath()));
                    } else {
                        reportState.postValue(StateBase.success("Файл отправлен на почту, указанную в аккаунте"));
                    }
                }

                @Override
                public void onError(String error) {
                    Log.d("ERROR IN LOADING", error);
                    reportState.postValue(StateBase.error(error));
                }
            });
        });
    }

    private File saveFile(InputStream inputStream, String format) {
        File downloadsDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS);
//        reportSavingState.postValue(StateBase.loading());
        File file = new File(downloadsDir, "HeadacheReport_" + System.currentTimeMillis() + format);
        try {
//            Executors.newSingleThreadExecutor().execute(() -> {
            try (FileOutputStream fos = new FileOutputStream(file)) {
                byte[] buffer = new byte[8192];
                int bytesReader;
                while ((bytesReader = inputStream.read(buffer)) != -1){
                    fos.write(buffer, 0, bytesReader);
                }
            } catch (IOException e) {
//                    reportSavingState.postValue(StateBase.error(e.getMessage()));
            }
//                reportSavingState.postValue(StateBase.success(file.getAbsolutePath()));
//            });
        } catch (Exception e) {
//            reportSavingState.postValue(StateBase.error(e.getMessage()));
        }
        return file;
    }
}
