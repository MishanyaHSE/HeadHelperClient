package com.example.headachediary.presentation.view.mainmenu;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.headachediary.R;
import com.example.headachediary.domain.headache.QuestionData;
import com.example.headachediary.domain.headache.QuestionResponse;
import com.example.headachediary.presentation.viewmodel.mainmenu.QuestionsViewModel;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class SurveyFragment extends Fragment {

    private SwitchMaterial switchPainTime, switchIntensity, switchMedication, switchDuration,
    switchComment, switchPressure, switchSymptoms, switchArea, switchTriggers, switchType;
    private Button btnSave;

    private ProgressDialog progressDialog;

    private QuestionsViewModel questionsViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_survey, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Инициализация элементов
        switchPainTime = view.findViewById(R.id.switchPainTime);
        switchIntensity = view.findViewById(R.id.switchIntensity);
        switchMedication = view.findViewById(R.id.switchMedication);
        switchDuration = view.findViewById(R.id.switchDuration);
        switchArea = view.findViewById(R.id.switchArea);
        switchComment = view.findViewById(R.id.switchComment);
        switchPressure = view.findViewById(R.id.switchPressure);
        switchTriggers = view.findViewById(R.id.switchTriggers);
        switchType = view.findViewById(R.id.switchType);
        switchSymptoms = view.findViewById(R.id.switchSymptoms);
        btnSave = view.findViewById(R.id.btnSave);


        questionsViewModel =  new ViewModelProvider(this).get(QuestionsViewModel.class);

        questionsViewModel.getQuestions();
        setupLoadQuestionObservers();
        setupSavingObservers();

        btnSave.setOnClickListener(v -> savePreferences());
    }

    private void savePreferences() {

        QuestionData questions = new QuestionData();
        questions.setTimeQuestion(switchPainTime.isChecked());
        questions.setIntensityQuestion(switchIntensity.isChecked());
        questions.setMedicineQuestion(switchMedication.isChecked());
        questions.setAreaQuestion(switchArea.isChecked());
        questions.setSymptomsQuestion(switchSymptoms.isChecked());
        questions.setCommentQuestion(switchComment.isChecked());
        questions.setPressureQuestion(switchPressure.isChecked());
        questions.setDurationQuestion(switchDuration.isChecked());
        questions.setTriggersQuestion(switchTriggers.isChecked());
        questions.setPainTypeQuestion(switchType.isChecked());
        // Возврат назад
        questionsViewModel.updateQuestions(questions);
    }



    private void setupSavingObservers() {
        questionsViewModel.getSaveQuestionsState().observe(getViewLifecycleOwner(), state -> {
            switch(state.getStatus()) {
                case LOADING:
                    showLoading("Сохраняем опрос...");
                    break;
                case SUCCESS:
                    Log.d("SURVEY QUESTIONS", "SUCCSESSFULLY SAVED");
                    Toast.makeText(requireContext(), "Данные сохранены", Toast.LENGTH_LONG).show();
                    if (getActivity() != null) {
                        getActivity().onBackPressed();
                    }
                    hideLoading();
                    break;
                case ERROR:
                    hideLoading();
                    showError(state.getError());
                    break;
            }
        });
    }

    private void setupLoadQuestionObservers() {
        questionsViewModel.getLoadQuestionsState().observe(getViewLifecycleOwner(), state -> {
            switch(state.getStatus()) {
                case LOADING:
                    showLoading("Загружаем опрос...");
                    break;
                case SUCCESS:
                    QuestionResponse data = state.getData();
                    switchPainTime.setChecked(data.getTimeQuestion());
                    switchIntensity.setChecked(data.getIntensityQuestion());
                    switchMedication.setChecked(data.getMedicineQuestion());
                    switchDuration.setChecked(data.getDurationQuestion());
                    switchComment.setChecked(data.getCommentQuestion());
                    switchPressure.setChecked(data.getPressureQuestion());
                    switchArea.setChecked(data.getAreaQuestion());
                    switchSymptoms.setChecked(data.getSymptomsQuestion());
                    switchTriggers.setChecked(data.getTriggersQuestion());
                    switchType.setChecked(data.getPainTypeQuestion());
                    hideLoading();
                    break;
                case ERROR:
                    hideLoading();
                    showError(state.getError());
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
}