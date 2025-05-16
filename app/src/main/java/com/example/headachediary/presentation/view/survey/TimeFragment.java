package com.example.headachediary.presentation.view.survey;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.headachediary.R;
import com.example.headachediary.domain.headache.NoteCreate;
import com.example.headachediary.domain.headache.QuestionResponse;
import com.example.headachediary.presentation.viewmodel.survey.SurveyViewModel;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class TimeFragment extends Fragment {
    private SurveyViewModel viewModel;
    private TextView tvSelectedTime;
    private NoteCreate note;
    private QuestionResponse questions;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(SurveyViewModel.class);
        note = viewModel.getSurvey().getValue();
        if (!Objects.requireNonNull(viewModel.getQuestions().getValue()).getTimeQuestion()) {
            if(viewModel.moveForward()) {
                navigateNext();
            } else {
                navigateBack();
            }
        }
        Log.d("NOTE IN TIME", note.toString());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_time_picker, container, false);
        tvSelectedTime = view.findViewById(R.id.tvSelectedTime);

        if (note.getHeadacheTime() != null) {
            updateTimeDisplay(LocalTime.parse(note.getHeadacheTime()));
        }

        setupTimePicker(view);
        setupNavigationButtons(view);
        return view;
    }

    private void setupTimePicker(View view) {
        Button btnSelectTime = view.findViewById(R.id.btnSelectTime);
        btnSelectTime.setOnClickListener(v -> showTimePicker());
    }

    private void showTimePicker() {
        TimePickerDialog timePicker = new TimePickerDialog(
                requireContext(),
                (view, hourOfDay, minute) -> {
                    LocalTime selectedTime = LocalTime.of(hourOfDay, minute);
                    note.setHeadacheTime(selectedTime.toString());
                    updateTimeDisplay(selectedTime);
                },
                LocalTime.now().getHour(),
                LocalTime.now().getMinute(),
                true // 24-часовой формат
        );
        timePicker.show();
    }

    private void updateTimeDisplay(LocalTime time) {
        String timeStr = time.format(DateTimeFormatter.ofPattern("HH:mm"));
        tvSelectedTime.setText(getString(R.string.selected_time_format, timeStr));
    }

    private void setupNavigationButtons(View view) {
        view.findViewById(R.id.btnBack).setOnClickListener(v -> navigateBack());
        view.findViewById(R.id.btnNext).setOnClickListener(v -> navigateNext());
    }

    private void navigateBack() {
        viewModel.setDirection(false);
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_survey);
        navController.navigate(R.id.action_timePickerFragment_to_previousQuestionFragment);
    }

    private void navigateNext() {
        viewModel.setDirection(true);
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_survey);
        navController.navigate(R.id.action_timePickerFragment_to_nextQuestionFragment);
    }
}