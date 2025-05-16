package com.example.headachediary.presentation.view.survey;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;


import com.example.headachediary.R;
import com.example.headachediary.domain.headache.NoteCreate;
import com.example.headachediary.domain.headache.NoteResponse;
import com.example.headachediary.presentation.view.mainmenu.MainActivity;
import com.example.headachediary.presentation.viewmodel.survey.SurveyViewModel;

import java.time.LocalDate;


public class HeadacheQuestionFragment extends Fragment {
    private SurveyViewModel viewModel;
    private NoteCreate note;
    private ProgressDialog progressDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(SurveyViewModel.class);
        if (requireActivity().getIntent().getStringExtra("mode").equals("add")) {
            if (viewModel.getSurvey().getValue() == null) {
                note = new NoteCreate();
            } else {
                note = viewModel.getSurvey().getValue();
            }

            // Получение даты из аргументов
            String dateStr = requireActivity().getIntent().getStringExtra("selected_date");
            note.setDate(dateStr);
        } else {
            String date = requireActivity().getIntent()
                    .getStringExtra("selected_date");
            String[] dateParts = date.split("-");
            viewModel.getNoteFromServer(LocalDate.of(Integer.parseInt(dateParts[0]),
                    Integer.parseInt(dateParts[1]), Integer.parseInt(dateParts[2])));
        }
        viewModel.setDirection(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_headache_question, container, false);

        TextView tvQuestion = view.findViewById(R.id.tvQuestion);
        Button btnYes = view.findViewById(R.id.btnYes);
        Button btnNo = view.findViewById(R.id.btnNo);

        String question = "Болела ли у Вас голова " + requireActivity().getIntent()
                .getStringExtra("selected_date") + "?";
        tvQuestion.setText(question);

        btnYes.setOnClickListener(v -> {
            note.setIsHeadache(true);
            navigateToNextQuestion();
        });

        btnNo.setOnClickListener(v -> {
            note.setIsHeadache(false);
            finishSurvey();
        });
        setupSavingObservers();
        setupLoadingNoteObservers();

        return view;
    }

    private void navigateToNextQuestion() {
        // Переход к следующему фрагменту
        viewModel.updateSurvey(note);
        Log.d("UPDATED SURVEY", viewModel.getSurvey().getValue().toString());
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_survey);
        navController.navigate(R.id.action_to_second_question);
    }

    private void finishSurvey() {
        // Сохранение данных и закрытие Activity
        viewModel.saveNote(note);
        if (requireActivity().getIntent().getStringExtra("mode").equals("edit")) {
            String[] dateParts = note.getDate().split("-");
            viewModel.deleteNote(LocalDate.of(Integer.parseInt(dateParts[0]),
                    Integer.parseInt(dateParts[1]), Integer.parseInt(dateParts[2])));
        }
        Intent intent = new Intent(requireContext(), MainActivity.class);
        startActivity(intent);
        requireActivity().finish();
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


    private void setupSavingObservers() {
        viewModel.getSaveState().observe(getViewLifecycleOwner(), state -> {
            switch(state.getStatus()) {
                case ERROR:
                    hideLoading();
                    showError(state.getError());
                    break;
                case LOADING:
                    showLoading("Сохраняем запись...");
                    break;
                case SUCCESS:
                    hideLoading();
                    Toast.makeText(requireContext(), "Запись успешно сохранена",
                            Toast.LENGTH_SHORT).show();
                    break;
            }
        });
    }

    private void setupLoadingNoteObservers() {
        viewModel.getLoadingNoteState().observe(getViewLifecycleOwner(), state -> {
            switch(state.getStatus()) {
                case ERROR:
                    hideLoading();
                    showError(state.getError());
                    break;
                case LOADING:
                    showLoading("Загружаем запись...");
                    break;
                case SUCCESS:
                    NoteResponse loadedNote = state.getData();
                    Log.d("LOADED DATE", loadedNote.toString());
                    note = new NoteCreate();
                    note.setDate(loadedNote.getDate());
                    note.setHeadacheTime(loadedNote.getHeadacheTime());
                    note.setTriggers(loadedNote.getTriggers());
                    note.setArea(loadedNote.getArea());
                    note.setDuration(loadedNote.getDuration());
                    note.setMedicine(loadedNote.getMedicine());
                    note.setComment(loadedNote.getComment());
                    note.setIntensity(loadedNote.getIntensity());
                    note.setPressureEveningDown(loadedNote.getPressureEveningDown());
                    note.setPressureEveningUp(loadedNote.getPressureEveningUp());
                    note.setPressureMorningDown(loadedNote.getPressureMorningDown());
                    note.setPressureMorningUp(loadedNote.getPressureMorningUp());
                    note.setHeadacheType(loadedNote.getHeadacheType());
                    note.setHeadacheType(loadedNote.getHeadacheType());
                    note.setSymptoms(loadedNote.getSymptoms());
                    hideLoading();
                    break;
            }
        });
    }
}
