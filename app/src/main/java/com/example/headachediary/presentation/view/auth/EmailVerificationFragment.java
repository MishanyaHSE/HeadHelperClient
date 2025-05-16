package com.example.headachediary.presentation.view.auth;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.headachediary.R;
import com.example.headachediary.presentation.view.mainmenu.MainActivity;
import com.example.headachediary.presentation.viewmodel.auth.VerifyViewModel;

public class EmailVerificationFragment extends Fragment {

    private VerifyViewModel verifyViewModel;
    private EditText codeEditText;
    private String email;

    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_email_verification, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Получение email из аргументов
        if (getArguments() != null) {
            email = getArguments().getString("email");
        }

        codeEditText = view.findViewById(R.id.codeEditText);
        Button verifyButton = view.findViewById(R.id.verifyButton);

        verifyViewModel = new ViewModelProvider(this).get(VerifyViewModel.class);
        setupObserver();

        verifyButton.setOnClickListener(v -> attemptVerification());
    }

    private void attemptVerification() {
        String code = codeEditText.getText().toString().trim();

        if (code.isEmpty()) {
            codeEditText.setError("Введите код");
            return;
        }

        if (code.length() != 6) {
            codeEditText.setError("Код должен содержать 6 цифр");
            return;
        }

        verifyViewModel.verifyCode(email, code);
    }

    private void showLoading() {
        progressDialog = new ProgressDialog(requireContext());
        progressDialog.setMessage("Регистрируемся...");
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

    private void setupObserver() {
        verifyViewModel.getVerificationState().observe(getViewLifecycleOwner(), state -> {
            switch (state.getStatus()) {
                case SUCCESS:
                    hideLoading();
                    // Переход в MainActivity
                    Navigation.findNavController(getView())
                            .navigate(R.id.action_verificationFragment_to_loginFragment);
                    break;
                case ERROR:
                    hideLoading();
                    showError(state.getError());
                    break;
                case LOADING:
                    showLoading();
                    break;
            }
        });
    }
}