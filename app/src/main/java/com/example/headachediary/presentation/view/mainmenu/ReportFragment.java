package com.example.headachediary.presentation.view.mainmenu;

import static com.example.headachediary.presentation.viewmodel.StateBase.Status.LOADING;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.headachediary.R;
import com.example.headachediary.domain.headache.ReportCreate;
import com.example.headachediary.presentation.viewmodel.mainmenu.ProfileViewModel;
import com.example.headachediary.presentation.viewmodel.mainmenu.ReportViewModel;
import com.google.android.material.textfield.TextInputEditText;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class ReportFragment extends Fragment {
    private TextInputEditText etStartDate, etEndDate;
    private RadioGroup rgFormat, rgSaveOption;
    private LocalDate startDate, endDate;

    private ReportViewModel reportViewModel;
    private ProgressDialog progressDialog;
    private ReportCreate reportCreate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_report, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Инициализация элементов
        etStartDate = view.findViewById(R.id.etStartDate);
        etEndDate = view.findViewById(R.id.etEndDate);
        rgFormat = view.findViewById(R.id.rgFormat);
        rgSaveOption = view.findViewById(R.id.rgSaveOption);
        Button btnGenerate = view.findViewById(R.id.btnGenerate);
        reportViewModel = new ViewModelProvider(this).get(ReportViewModel.class);
        // Настройка DatePicker
        setupDatePickers();
        setupLoadingReportObserver();

//        reportViewModel = new ViewModelProvider(this).get(ReportViewModel.class);
//        setupObservers();

        btnGenerate.setOnClickListener(v -> generateReport());
    }

    private void setupDatePickers() {
        DatePickerDialog.OnDateSetListener startDateListener = (view, year, month, day) -> {
            startDate = LocalDate.of(year, month + 1, day);
            etStartDate.setText(startDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        };

        DatePickerDialog.OnDateSetListener endDateListener = (view, year, month, day) -> {
            endDate = LocalDate.of(year, month + 1, day);
            etEndDate.setText(endDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        };

        etStartDate.setOnClickListener(v -> showDatePicker(startDateListener));
        etEndDate.setOnClickListener(v -> showDatePicker(endDateListener));
    }

    private void showDatePicker(DatePickerDialog.OnDateSetListener listener) {
        Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(requireContext(), listener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void generateReport() {
        // Валидация дат
        if (startDate == null || endDate == null) {
            Toast.makeText(requireContext(), "Выберите даты", Toast.LENGTH_SHORT).show();
            return;
        }

        // Получение параметров
        String format = rgFormat.getCheckedRadioButtonId() == R.id.rbPdf ? "pdf" : "csv";
        boolean saveLocal = rgSaveOption.getCheckedRadioButtonId() == R.id.rbSaveLocal;
        reportCreate = new ReportCreate();
        reportCreate.setDateStart(startDate.toString());
        reportCreate.setDateEnd(endDate.toString());
        reportCreate.setFormat(format == "pdf" ? 0 : 1);
        reportCreate.setSendToMail(saveLocal);
        Log.d("REPORT CREATE", reportCreate.getDateStart().toString());
        Log.d("REPORT CREATE", reportCreate.getDateEnd().toString());
        Log.d("REPORT CREATE", reportCreate.toString());

        if (saveLocal) {
            checkStoragePermission();
        } else {
            reportViewModel.getReport(reportCreate, false);
        }

    }



    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                Log.d("PermissionResult", "Permission granted: " + isGranted);
                if (isGranted) {
                    Toast.makeText(requireContext(), "Разрешение получено",
                            Toast.LENGTH_SHORT).show();
                    reportViewModel.getReport(reportCreate, true);
                } else {
                    Toast.makeText(requireContext(), "Разрешение отклонено",
                            Toast.LENGTH_SHORT).show();
                }
            });

    private void checkStoragePermission() {
        // Для API >= 33 (Android 13+) используем отдельные разрешения
        Log.d("PermissionCheck", "SDK_INT: " + Build.VERSION.SDK_INT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Log.d("PermissionCheck", "Checking READ_MEDIA_IMAGES");
            if (ContextCompat.checkSelfPermission(requireContext(),
                    Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED) {
                reportViewModel.getReport(reportCreate, true);
            } else {
                requestPermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES);
            }
        }
        // Для API 29-32 (Android 10-12L)
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Log.d("PermissionCheck", "API 29-32: No permission needed");
            // Разрешение не требуется для записи в папку Downloads
            reportViewModel.getReport(reportCreate, true);
        }
        // Для API < 29
        else {
            Log.d("PermissionCheck", "Checking WRITE_EXTERNAL_STORAGE");
            if (ContextCompat.checkSelfPermission(requireContext(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                reportViewModel.getReport(reportCreate, true);
            } else {
                requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);

            }
        }
    }


    private void setupLoadingReportObserver() {
        reportViewModel.getReportState().observe(getViewLifecycleOwner(), state -> {
            switch(state.getStatus()) {
                case LOADING:
                    showLoading("Загружаем файл...");
                    break;
                case SUCCESS:
                    showSuccess("" + state.getData());
                    hideLoading();
                    break;
                case ERROR:
                    hideLoading();
                    showError(state.getError());
                    Log.d("ERROR WHILE LOADING", "error");
                    break;
            }
        });
    }

    private void showLoading(String message) {
        progressDialog = new ProgressDialog(requireContext());
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void hideLoading() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    private void showError(String message) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Ошибка")
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }

    private void showSuccess(String message) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Отчет получен")
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }
}
