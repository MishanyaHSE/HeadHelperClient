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

public class AreaFragment extends Fragment {

    private SurveyViewModel viewModel;
    private NoteCreate note;
    private FlexboxLayout flexbox;
    private List<ToggleButton> buttons = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(SurveyViewModel.class);
        note = viewModel.getSurvey().getValue();

        // Гарантируем инициализацию списка
        if (note.getArea() == null) {
            note.setArea(new ArrayList<>());
        }

        if (!Objects.requireNonNull(viewModel.getQuestions().getValue()).getAreaQuestion()) {
            if(viewModel.moveForward()) {
                navigateNext();
            } else {
                navigateBack();
            }
        }
        Log.d("NOTE IN AREA", note.toString());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_area, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        flexbox = view.findViewById(R.id.flexbox);
        setupAreaButtons();
        setupNavigationButtons(view);
        restoreSelection();
    }

    private void setupAreaButtons() {
        String[] areas = getResources().getStringArray(R.array.pain_areas);
        int minWidth = getResources().getDimensionPixelSize(R.dimen.pain_type_button_width);

        for (String area : areas) {
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
            button.setText(area);
            button.setTextOn(area);
            button.setTextOff(area);
            button.setBackgroundResource(R.drawable.button_duration_selector);
            button.setTextAppearance(R.style.PainTypeButton);

            // Настройки текста
            button.setSingleLine(true);
            button.setEllipsize(TextUtils.TruncateAt.END);

            button.setOnCheckedChangeListener((buttonView, isChecked) -> {
                String selectedType = buttonView.getText().toString();
                if (isChecked) {
                    note.getArea().add(selectedType);
                } else {
                    note.getArea().remove(selectedType);
                }
            });

            flexbox.addView(button);
            buttons.add(button);
        }
    }

    private void handleAreaSelection(ToggleButton button, boolean isChecked) {
        String selectedArea = button.getText().toString();
        if (isChecked) {
            if (!note.getArea().contains(selectedArea)) {
                note.getArea().add(selectedArea);
            }
        } else {
            note.getArea().remove(selectedArea);
        }
    }

    private void restoreSelection() {
        if (note.getArea() != null) {
            for (ToggleButton button : buttons) {
                button.setChecked(note.getArea().contains(button.getText().toString()));
            }
        }
    }

    private void setupNavigationButtons(View view) {
        view.findViewById(R.id.btnBack).setOnClickListener(v -> navigateBack());
        view.findViewById(R.id.btnNext).setOnClickListener(v -> navigateNext());
    }

    private void navigateBack() {
        viewModel.updateSurvey(note);
        viewModel.setDirection(false);
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_survey);
        navController.navigate(R.id.action_areaFragment_to_previousFragment);
    }

    private void navigateNext() {
        viewModel.updateSurvey(note);
        viewModel.setDirection(true);
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_survey);
        navController.navigate(R.id.action_areaFragment_to_nextFragment);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        viewModel.setDirection(false);
        viewModel.updateSurvey(note); // Сохраняем изменения
    }
}
