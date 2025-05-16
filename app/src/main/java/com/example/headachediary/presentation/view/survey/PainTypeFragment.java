package com.example.headachediary.presentation.view.survey;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class PainTypeFragment extends Fragment {
    private SurveyViewModel viewModel;
    private NoteCreate note;
    private FlexboxLayout flexbox;
    private List<ToggleButton> buttons = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(SurveyViewModel.class);
        note = viewModel.getSurvey().getValue();


        if (note.getHeadacheType() == null) {
            note.setHeadacheType(new ArrayList<>());
        }

        if (!Objects.requireNonNull(viewModel.getQuestions().getValue()).getPainTypeQuestion()) {
            if(viewModel.moveForward()) {
                navigateNext();
            } else {
                navigateBack();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pain_type, container, false);
        flexbox = view.findViewById(R.id.flexbox);
        setupPainTypeButtons();
        setupNavigationButtons(view);
        restoreSelection();
        return view;
    }

    private void setupPainTypeButtons() {
        String[] painTypes = getResources().getStringArray(R.array.pain_types);

        for (String type : painTypes) {
            ToggleButton button = new ToggleButton(requireContext());
            int width = getResources().getDimensionPixelSize(R.dimen.pain_type_button_width);
            FlexboxLayout.LayoutParams params = new FlexboxLayout.LayoutParams(
                    width, FlexboxLayout.LayoutParams.WRAP_CONTENT
            );

            // Добавляем отступы вокруг кнопок
            params.setMargins(
                    getResources().getDimensionPixelSize(R.dimen.small_margin),
                    getResources().getDimensionPixelSize(R.dimen.small_margin),
                    getResources().getDimensionPixelSize(R.dimen.small_margin),
                    getResources().getDimensionPixelSize(R.dimen.small_margin)
            );

            button.setLayoutParams(params);
            button.setText(type);
            button.setTextOn(type);
            button.setTextOff(type);
            button.setBackgroundResource(R.drawable.button_duration_selector);
            button.setTextAppearance(R.style.PainTypeButton);

            button.setOnCheckedChangeListener((buttonView, isChecked) -> {
                String selectedType = buttonView.getText().toString();
                if (isChecked) {
                    note.getHeadacheType().add(selectedType);
                } else {
                    note.getHeadacheType().remove(selectedType);
                }
            });

            flexbox.addView(button);
            buttons.add(button);
        }
    }

    private void restoreSelection() {
        if (note.getHeadacheType() != null) {
            for (ToggleButton button : buttons) {
                if (note.getHeadacheType().contains(button.getText().toString())) {
                    button.setChecked(true);
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
        navController.navigate(R.id.action_painTypeFragment_to_previousFragment);
    }

    private void navigateNext() {
        // Проверка не требуется, выбор опционален
        viewModel.setDirection(true);
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_survey);
        navController.navigate(R.id.action_painTypeFragment_to_nextFragment);
    }
}
