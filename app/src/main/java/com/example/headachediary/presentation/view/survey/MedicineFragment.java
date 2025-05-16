package com.example.headachediary.presentation.view.survey;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.headachediary.R;
import com.example.headachediary.domain.headache.Medicine;
import com.example.headachediary.domain.headache.NoteCreate;
import com.example.headachediary.presentation.viewmodel.survey.SurveyViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MedicineFragment extends Fragment {

    private SurveyViewModel viewModel;
    private LinearLayout containerMedicines;
    private Button btnAdd;
    private List<Medicine> medicines = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(SurveyViewModel.class);
        initMedicinesList();
        if (!Objects.requireNonNull(viewModel.getQuestions().getValue()).getMedicineQuestion()) {
            if(viewModel.moveForward()) {
                navigateNext();
            } else {
                navigateBack();
            }
        }
    }

    private void initMedicinesList() {
        NoteCreate note = viewModel.getSurvey().getValue();
        if (note != null && note.getMedicine() != null) {
            medicines = note.getMedicine();
        } else {
            medicines = new ArrayList<>();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_medicine, container, false);
        containerMedicines = view.findViewById(R.id.containerMedicines);
        btnAdd = view.findViewById(R.id.btnAddMedicine);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupUI();
        setupNavigationButtons();
        updateAddButtonVisibility();
    }

    private void setupNavigationButtons() {
        requireView().findViewById(R.id.btnBack).setOnClickListener(v -> navigateBack());
        requireView().findViewById(R.id.btnNext).setOnClickListener(v -> navigateNext());
    }

    private void navigateBack() {
        saveDataToViewModel();
        viewModel.setDirection(false);

        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_survey);
        navController.navigate(R.id.action_medicineFragment_to_previousFragment); // Укажите правильный action
    }

    private void navigateNext() {
        if (validateInput()) {
            viewModel.setDirection(true);
            saveDataToViewModel();
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_survey);
            navController.navigate(R.id.action_medicineFragment_to_nextFragment); // Укажите правильный action
        } else {
            showValidationError();
        }
    }

    private boolean validateInput() {
        for (Medicine medicine : medicines) {
            if (TextUtils.isEmpty(medicine.getName()) || medicine.getWeight() == null) {
                return false;
            }
        }
        return true;
    }

    private void showValidationError() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Ошибка")
                .setMessage("Заполните все поля для лекарств")
                .setPositiveButton("OK", null)
                .show();
    }

    private void saveDataToViewModel() {
        NoteCreate note = viewModel.getSurvey().getValue();
        if (note != null) {
            note.setMedicine(medicines);
            viewModel.updateSurvey(note);
        }
    }

    private void setupUI() {
        // Восстановление существующих лекарств
        for (Medicine medicine : medicines) {
            addMedicineView(medicine);
        }

        btnAdd.setOnClickListener(v -> {
            Medicine newMedicine = new Medicine();
            medicines.add(newMedicine);
            addMedicineView(newMedicine);
            updateAddButtonVisibility();
        });
    }

    private void addMedicineView(Medicine medicine) {
        View medicineView = LayoutInflater.from(getContext())
                .inflate(R.layout.item_medicine, containerMedicines, false);

        EditText etName = medicineView.findViewById(R.id.etMedicineName);
        EditText etDose = medicineView.findViewById(R.id.etDose);
        ImageButton btnDelete = medicineView.findViewById(R.id.btnDelete);

        // Восстановление данных
        if (medicine.getName() != null) {
            etName.setText(medicine.getName());
        }
        if (medicine.getWeight() != null) {
            etDose.setText(String.valueOf(medicine.getWeight()));
        }

        // Слушатели изменений
        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                medicine.setName(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        etDose.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    medicine.setWeight(Integer.parseInt(s.toString()));
                } catch (NumberFormatException e) {
                    medicine.setWeight(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        btnDelete.setOnClickListener(v -> {
            containerMedicines.removeView(medicineView);
            medicines.remove(medicine);
            updateAddButtonVisibility();
        });

        containerMedicines.addView(medicineView);
    }

    private void updateAddButtonVisibility() {
        btnAdd.setVisibility(medicines.size() < 7 ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onPause() {
        super.onPause();
        // Сохранение данных в ViewModel
        viewModel.getSurvey().getValue().setMedicine(medicines);
    }
}
