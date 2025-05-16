package com.example.headachediary.presentation.view.survey;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.headachediary.R;
import com.example.headachediary.domain.headache.NoteCreate;
import com.example.headachediary.presentation.viewmodel.survey.SurveyViewModel;

import java.util.Objects;

public class IntensityFragment extends Fragment {
    private SurveyViewModel viewModel;
    private NoteCreate note;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(SurveyViewModel.class);
        note = viewModel.getSurvey().getValue();
        if (!Objects.requireNonNull(viewModel.getQuestions().getValue()).getIntensityQuestion()) {
            if(viewModel.moveForward()) {
                navigateNext();
            } else {
                navigateBack();
            }
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_intensity, container, false);
        SeekBar seekBar = view.findViewById(R.id.seekBar);
        TextView tvValue = view.findViewById(R.id.tvValue);

        // Восстановление предыдущего значения
        if (note.getIntensity() != null) {
            seekBar.setProgress(note.getIntensity());
            tvValue.setText(String.valueOf(note.getIntensity()));
            updateColor(note.getIntensity(), seekBar, tvValue);
        }


        // Обработка изменений
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Минимальное значение - 1
                int adjustedProgress = Math.max(1, progress);
                tvValue.setText(String.valueOf(adjustedProgress));
                note.setIntensity(adjustedProgress);
                updateColor(adjustedProgress, seekBar, tvValue);
            }

            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        setupNavigationButtons(view);
        return view;
    }


    private void updateColor(int progress, SeekBar seekBar, TextView tvValue) {
        int colorRes;
        if (progress <= 3) {
            colorRes = R.color.green;
        } else if (progress <= 7) {
            colorRes = R.color.yellow;
        } else {
            colorRes = R.color.red;
        }

        // Меняем цвет прогресса
        seekBar.getProgressDrawable().setColorFilter(
                ContextCompat.getColor(requireContext(), colorRes),
                PorterDuff.Mode.SRC_IN
        );


        // Меняем цвет текста
        tvValue.setTextColor(ContextCompat.getColor(requireContext(), colorRes));
    }

    private void setupNavigationButtons(View view) {
        view.findViewById(R.id.btnBack).setOnClickListener(v -> navigateBack());
        view.findViewById(R.id.btnNext).setOnClickListener(v -> navigateNext());
    }

    private void navigateBack() {
        viewModel.setDirection(false);
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_survey);
        navController.navigate(R.id.action_intensityFragment_to_previousFragment);
    }

    private void navigateNext() {
        viewModel.setDirection(true);
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_survey);
        navController.navigate(R.id.action_intensityFragment_to_nextFragment);
    }
}
