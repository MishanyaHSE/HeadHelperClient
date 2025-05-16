package com.example.headachediary.presentation.view.survey;

import static android.widget.Toast.LENGTH_SHORT;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.headachediary.R;
import com.example.headachediary.domain.headache.NoteCreate;
import com.example.headachediary.presentation.viewmodel.survey.SurveyViewModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class PressureFragment extends Fragment {

    private SurveyViewModel viewModel;
    private NoteCreate note;

    private TextInputEditText etMorningUp;
    private TextInputEditText etMorningDown;
    private TextInputEditText etEveningUp;
    private TextInputEditText etEveningDown;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(SurveyViewModel.class);
        note = viewModel.getSurvey().getValue();
        if (!Objects.requireNonNull(viewModel.getQuestions().getValue()).getPressureQuestion()) {
            if(viewModel.moveForward()) {
                navigateNext();
            } else {
                navigateBack();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pressure, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        setupTextWatchers();
        setupNavigationButtons();
        restoreData();
    }

    private void initViews(View view) {
        etMorningUp = view.findViewById(R.id.etMorningUp);
        etMorningDown = view.findViewById(R.id.etMorningDown);
        etEveningUp = view.findViewById(R.id.etEveningUp);
        etEveningDown = view.findViewById(R.id.etEveningDown);
    }

    private void setupTextWatchers() {
        etMorningUp.addTextChangedListener(new PressureTextWatcher(etMorningUp));
        etMorningDown.addTextChangedListener(new PressureTextWatcher(etMorningDown));
        etEveningUp.addTextChangedListener(new PressureTextWatcher(etEveningUp));
        etEveningDown.addTextChangedListener(new PressureTextWatcher(etEveningDown));
    }

    private void restoreData() {
        if (note.getPressureMorningUp() != null) {
            etMorningUp.setText(String.valueOf(note.getPressureMorningUp()));
        }
        if (note.getPressureMorningDown() != null) {
            etMorningDown.setText(String.valueOf(note.getPressureMorningDown()));
        }
        if (note.getPressureEveningUp() != null) {
            etEveningUp.setText(String.valueOf(note.getPressureEveningUp()));
        }
        if (note.getPressureEveningDown() != null) {
            etEveningDown.setText(String.valueOf(note.getPressureEveningDown()));
        }
    }

    private void setupNavigationButtons() {
        requireView().findViewById(R.id.btnBack).setOnClickListener(v -> navigateBack());
        requireView().findViewById(R.id.btnNext).setOnClickListener(v -> {
            if (validateInput()) {
                saveData();
                navigateNext();
            }
        });
    }

    private boolean validateInput() {
        boolean isValid = true;
        isValid &= validateGroup(etMorningUp, etMorningDown, "Утреннее");
        isValid &= validateGroup(etEveningUp, etEveningDown, "Вечернее");
        return isValid;
    }

    private boolean validateGroup(TextInputEditText upper, TextInputEditText lower, String groupName) {
        boolean upperFilled = !TextUtils.isEmpty(upper.getText());
        boolean lowerFilled = !TextUtils.isEmpty(lower.getText());

        if (upperFilled || lowerFilled) {
            boolean valid = true;
            if (!upperFilled) {
                setError(upper, groupName + " верхнее обязательно");
                valid = false;
            }
            if (!lowerFilled) {
                setError(lower, groupName + " нижнее обязательно");
                valid = false;
            }
            return valid && validateField(upper) && validateField(lower);
        }
        return true;
    }

    private boolean validateField(TextInputEditText field) {
        String value = field.getText().toString();
        if (value.isEmpty()) return true;

        try {
            int pressure = Integer.parseInt(value);
            if (pressure < 50 || pressure > 250) {
                setError(field, getString(R.string.error_pressure_range));
                return false;
            }
        } catch (NumberFormatException e) {
            setError(field, getString(R.string.error_number_format));
            return false;
        }
        return true;
    }

    private void setError(TextInputEditText field, String error) {
        TextInputLayout layout = findParentTextInputLayout(field);
        if (layout != null) {
            layout.setError(error);
        }
    }

    private void navigateBack() {
        viewModel.setDirection(false);
        saveData();
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_survey);
        navController.navigate(R.id.action_pressureFragment_to_previousFragment);
    }

    private void navigateNext() {
        viewModel.setDirection(true);
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_survey);
        navController.navigate(R.id.action_pressureFragment_to_nextFragment);
    }

    private void saveData() {
        note.setPressureMorningUp(parseInt(etMorningUp.getText()));
        note.setPressureMorningDown(parseInt(etMorningDown.getText()));
        note.setPressureEveningUp(parseInt(etEveningUp.getText()));
        note.setPressureEveningDown(parseInt(etEveningDown.getText()));
        viewModel.updateSurvey(note);
    }

    private Integer parseInt(Editable text) {
        try {
            return Integer.parseInt(text.toString());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private TextInputLayout findParentTextInputLayout(View view) {
        ViewParent parent = view.getParent();
        while (parent != null) {
            if (parent instanceof TextInputLayout) {
                return (TextInputLayout) parent;
            }
            parent = parent.getParent();
        }
        return null;
    }

    private class PressureTextWatcher implements TextWatcher {
        private final TextInputEditText field;

        PressureTextWatcher(TextInputEditText field) {
            this.field = field;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

        @Override
        public void afterTextChanged(Editable s) {
            TextInputLayout layout = findParentTextInputLayout(field);
            if (layout != null) {
                layout.setError(null);

                String value = s.toString();
                if (!value.isEmpty()) {
                    try {
                        int pressure = Integer.parseInt(value);
                        if (pressure < 50 || pressure > 250) {
                            layout.setError(getString(R.string.error_pressure_range));
                        }
                    } catch (NumberFormatException e) {
                        layout.setError(getString(R.string.error_number_format));
                    }
                }
            }
        }
    }
}
