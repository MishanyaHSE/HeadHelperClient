package com.example.headachediary.presentation.view.auth;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.headachediary.R;
import com.example.headachediary.domain.auth.PasswordReset;
import com.example.headachediary.domain.auth.UserCreate;
import com.example.headachediary.presentation.viewmodel.auth.RegisterViewModel;
import com.example.headachediary.presentation.viewmodel.auth.ResetViewModel;

public class ResetFragment extends Fragment {
        private EditText codeEditText, passwordEditText, confirmPasswordEditText;

        private ProgressDialog progressDialog;
        private ResetViewModel resetViewModel;

        private View view;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            view = inflater.inflate(R.layout.fragment_reset, container, false);

            codeEditText = view.findViewById(R.id.codeEditText);
            passwordEditText = view.findViewById(R.id.passwordEditText);
            confirmPasswordEditText = view.findViewById(R.id.confirmPasswordEditText);
            Button registerButton = view.findViewById(R.id.registerButton);

            resetViewModel = new ViewModelProvider(requireActivity()).get(ResetViewModel.class);

            registerButton.setOnClickListener(v -> attemptReset());
            setupObserver();
            return view;
        }


        private void attemptReset() {
            String code = codeEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            String confirmPassword = confirmPasswordEditText.getText().toString().trim();

            // Валидация
            if (code.isEmpty()) {
                codeEditText.setError("Введите код из письма");
                return;
            }


            if (password.isEmpty()) {
                passwordEditText.setError("Введите пароль");
                return;
            }

            if (!password.equals(confirmPassword)) {
                confirmPasswordEditText.setError("Пароли не совпадают");
                return;
            }

            resetPassword(code, password);

        }

    private void goToLogin() {
        // Возврат к экрану входа
        requireActivity().getSupportFragmentManager().popBackStack();
    }


    private void resetPassword(String code, String password) {
        PasswordReset passwordReset = new PasswordReset();
        passwordReset.setCode(code);
        passwordReset.setEmail(resetViewModel.getResetEmail().getValue());
        passwordReset.setPassword(password);
        resetViewModel.resetPassword(passwordReset);
    }

    private void showLoading(String message) {
        progressDialog = new ProgressDialog(requireContext());
        progressDialog.setMessage(message);
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
        resetViewModel.getResetState().observe(getViewLifecycleOwner(), state -> {
            switch (state.getStatus()) {
                case ERROR:
                    hideLoading();
                    showError(state.getError());
                    break;
                case LOADING:
                    showLoading("Меняем пароль...");
                    break;
                case SUCCESS:
                    hideLoading();

                    Navigation.findNavController(view)
                            .navigate(R.id.action_resetPassword_to_authFragment);
                    break;
            }
        });
    }


}
