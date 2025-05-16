package com.example.headachediary.presentation.view.survey;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.headachediary.R;
import com.example.headachediary.domain.headache.NoteCreate;
import com.example.headachediary.presentation.view.mainmenu.MainActivity;
import com.example.headachediary.presentation.viewmodel.survey.SurveyViewModel;
import com.google.android.material.textfield.TextInputEditText;

import java.time.LocalDate;
import java.util.Objects;

public class CommentFragment extends Fragment {

    private ProgressDialog progressDialog;
    private SurveyViewModel viewModel;
    private NoteCreate note;
    private TextInputEditText etComment;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(SurveyViewModel.class);
        note = viewModel.getSurvey().getValue();
        if (!Objects.requireNonNull(viewModel.getQuestions().getValue()).getCommentQuestion()) {
            if(viewModel.moveForward()) {
                navigateNext();
            } else {
                navigateBack();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_comment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etComment = view.findViewById(R.id.etComment);
        setupTextWatcher();
        setupNavigationButtons();
        restoreComment();
        setupObservers();
    }

    private void setupTextWatcher() {
        etComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                note.setComment(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void restoreComment() {
        if (note.getComment() != null) {
            etComment.setText(note.getComment());
        }
    }

    private void setupNavigationButtons() {
        requireView().findViewById(R.id.btnBack).setOnClickListener(v -> navigateBack());
        requireView().findViewById(R.id.btnNext).setOnClickListener(v -> navigateNext());
    }

    private void navigateBack() {
        viewModel.setDirection(false);
        saveData();
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_survey);
        navController.navigate(R.id.action_commentFragment_to_previousFragment);
    }

    private void navigateNext() {
        saveData();
        if (requireActivity().getIntent().getStringExtra("mode").equals("edit")) {
            String[] dateParts = note.getDate().split("-");
            viewModel.deleteNote(LocalDate.of(Integer.parseInt(dateParts[0]),
                    Integer.parseInt(dateParts[1]), Integer.parseInt(dateParts[2])));
        }
        viewModel.saveNote(note);
    }

    private void saveData() {
        viewModel.updateSurvey(note);
    }

    private void showLoading() {
        progressDialog = new ProgressDialog(requireContext());
        progressDialog.setMessage("Авторизуемся...");
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




    private void setupObservers() {
        viewModel.getSaveState().observe(getViewLifecycleOwner(), state -> {
            switch(state.getStatus()) {
                case ERROR:
                    hideLoading();
                    showError(state.getError());
                    break;
                case LOADING:
                    showLoading();
                    break;
                case SUCCESS:
                    Intent intent = new Intent(requireContext(), MainActivity.class);
                    startActivity(intent);
                    Toast.makeText(requireContext(), "Запись успешно сохранена",
                            Toast.LENGTH_SHORT).show();
                    hideLoading();
                    requireActivity().finish();
                    break;
            }
        });
    }
}
