package com.example.headachediary.presentation.view.survey;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.headachediary.R;
import com.example.headachediary.domain.headache.NoteCreate;
import com.example.headachediary.presentation.viewmodel.survey.SurveyViewModel;
import com.google.android.flexbox.FlexboxLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SymptomsFragment extends Fragment {

    private SurveyViewModel viewModel;
    private NoteCreate note;
    private FlexboxLayout flexbox;
    private List<ToggleButton> buttons = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(SurveyViewModel.class);
        note = viewModel.getSurvey().getValue();

        if (note.getSymptoms() == null) {
            note.setSymptoms(new ArrayList<>());
        }

        if (!Objects.requireNonNull(viewModel.getQuestions().getValue()).getSymptomsQuestion()) {
            if(viewModel.moveForward()) {
                navigateNext();
            } else {
                navigateBack();
            }
        }
        Log.d("NOTE IN SYMP", note.toString());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_symptoms, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        flexbox = view.findViewById(R.id.flexbox);
        setupSymptomsButtons();
        setupNavigationButtons();
        restoreSelection();
    }

    private void setupSymptomsButtons() {
        String[] symptoms = getResources().getStringArray(R.array.symptoms_list);
        int minWidth = getResources().getDimensionPixelSize(R.dimen.pain_type_button_width);

        for (String symptom : symptoms) {
            ToggleButton button = new ToggleButton(requireContext());
            FlexboxLayout.LayoutParams params = new FlexboxLayout.LayoutParams(
                    minWidth,
                    getResources().getDimensionPixelSize(R.dimen.button_height)
            );

            params.setMargins(
                    getResources().getDimensionPixelSize(R.dimen.small_margin),
                    getResources().getDimensionPixelSize(R.dimen.small_margin),
                    getResources().getDimensionPixelSize(R.dimen.small_margin),
                    getResources().getDimensionPixelSize(R.dimen.small_margin)
            );
            button.setLayoutParams(params);
            button.setText(symptom);
            button.setTextOn(symptom);
            button.setTextOff(symptom);
            button.setBackgroundResource(R.drawable.button_duration_selector);
            button.setTextAppearance(R.style.PainTypeButton);
            button.setSingleLine(true);
            button.setEllipsize(TextUtils.TruncateAt.END);

            button.setOnCheckedChangeListener((buttonView, isChecked) ->
                    handleSymptomSelection(buttonView.getText().toString(), isChecked)
            );

            flexbox.addView(button);
            buttons.add(button);
        }
    }

    private void handleSymptomSelection(String symptom, boolean isChecked) {
        if (isChecked) {
            if (!note.getSymptoms().contains(symptom)) {
                note.getSymptoms().add(symptom);
            }
        } else {
            note.getSymptoms().remove(symptom);
        }
    }

    private void restoreSelection() {
        for (ToggleButton button : buttons) {
            button.setChecked(note.getSymptoms().contains(button.getText().toString()));
        }
    }

    private void setupNavigationButtons() {
        requireView().findViewById(R.id.btnBack).setOnClickListener(v -> navigateBack());
        requireView().findViewById(R.id.btnNext).setOnClickListener(v -> navigateNext());
    }

    private void navigateBack() {
        viewModel.setDirection(false);
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_survey);
        navController.navigate(R.id.action_symptomsFragment_to_previousFragment);
    }

    private void navigateNext() {
        viewModel.setDirection(true);
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_survey);
        navController.navigate(R.id.action_symptomsFragment_to_nextFragment);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        viewModel.updateSurvey(note);
    }
}
