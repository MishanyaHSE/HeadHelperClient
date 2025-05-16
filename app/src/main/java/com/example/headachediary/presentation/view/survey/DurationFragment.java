package com.example.headachediary.presentation.view.survey;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.ToggleButton;

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

public class DurationFragment extends Fragment {
    private SurveyViewModel viewModel;
    private FlexboxLayout flexbox;
    private NoteCreate note;
    private List<ToggleButton> buttons = new ArrayList<>();
    private String[] durations = {
            "Менее часа",
            "1-2 часа",
            "2-4 часа",
            "4-7 часов",
            "7-12 часов",
            "Весь день"
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(SurveyViewModel.class);
        note = viewModel.getSurvey().getValue();
        if (!Objects.requireNonNull(viewModel.getQuestions().getValue()).getDurationQuestion()) {
            if(viewModel.moveForward()) {
                navigateNext();
            } else {
                navigateBack();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_duration, container, false);
        flexbox = view.findViewById(R.id.flexbox);

        setupDurationButtons();
        setupNavigationButtons(view);
        restoreSelection();

        return view;
    }

    private void setupDurationButtons() {
        for (String duration : durations) {
            ToggleButton button = new ToggleButton(requireContext());
            button.setLayoutParams(new FlexboxLayout.LayoutParams(
                    FlexboxLayout.LayoutParams.WRAP_CONTENT,
                    FlexboxLayout.LayoutParams.WRAP_CONTENT
            ));
            button.setText(duration);
            button.setTextOn(duration);
            button.setTextOff(duration);
            button.setBackgroundResource(R.drawable.button_duration_selector);
            button.setTextAppearance(R.style.DurationButton);
            button.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    uncheckOtherButtons((ToggleButton) buttonView);
                    note.setDuration(buttonView.getText().toString());
                }
            });

            flexbox.addView(button);
            buttons.add(button);
        }
    }

    private void uncheckOtherButtons(ToggleButton selectedButton) {
        for (ToggleButton button : buttons) {
            if (button != selectedButton) {
                button.setChecked(false);
            }
        }
    }

    private void restoreSelection() {
        String selected = note.getDuration();
        if (selected != null) {
            selected = selected;
            for (ToggleButton button : buttons) {
                if (button.getText().equals(selected)) {
                    button.setChecked(true);
                    break;
                }
            }
        }
    }

    private void setupNavigationButtons(View view) {
        view.findViewById(R.id.btnBack).setOnClickListener(v -> navigateBack());
        view.findViewById(R.id.btnNext).setOnClickListener(v -> navigateNext());
    }

    private void navigateBack() {
        viewModel.setDirection(false);
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_survey);
        navController.navigate(R.id.action_to_second_question);
    }

    private void navigateNext() {
        viewModel.setDirection(true);
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_survey);
        navController.navigate(R.id.action_duration_ToNextQuestion);
    }
}
