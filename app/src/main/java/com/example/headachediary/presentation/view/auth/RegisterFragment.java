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
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.headachediary.R;
import com.example.headachediary.domain.auth.UserCreate;
import com.example.headachediary.presentation.viewmodel.auth.RegisterViewModel;

public class RegisterFragment extends Fragment {
    private EditText emailEditText, passwordEditText, confirmPasswordEditText, nameEditText;
    public RegisterViewModel registerViewModel;

    private ProgressDialog progressDialog;

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_register, container, false);

        emailEditText = view.findViewById(R.id.emailEditText);
        passwordEditText = view.findViewById(R.id.passwordEditText);
        confirmPasswordEditText = view.findViewById(R.id.confirmPasswordEditText);
        nameEditText = view.findViewById(R.id.nameEditText);
        Button registerButton = view.findViewById(R.id.registerButton);
        TextView loginText = view.findViewById(R.id.loginText);

        registerButton.setOnClickListener(v -> attemptRegister());
        loginText.setOnClickListener(v -> goToLogin());
        registerViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);
        setupObserver();
        return view;
    }


    private void attemptRegister() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();
        String name = nameEditText.getText().toString().trim();

        // Валидация
        if (email.isEmpty()) {
            emailEditText.setError("Введите email");
            return;
        }

        if (name.isEmpty()) {
            nameEditText.setError("Введите имя");
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

        registerUser(name, email, password);

    }

    public void setViewModel(RegisterViewModel viewModel) {
        this.registerViewModel = viewModel;
    }

    private void goToLogin() {
        // Возврат к экрану входа
        requireActivity().getSupportFragmentManager().popBackStack();
    }


    private void registerUser(String name, String email, String password) {
        UserCreate user = new UserCreate();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        registerViewModel.registerUser(user);
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
        registerViewModel.getAuthState().observe(getViewLifecycleOwner(), state -> {
            switch(state.getStatus()) {
                case ERROR:
                    hideLoading();
                    showError(state.getError());
                    break;
                case LOADING:
                    showLoading("Регистрируемся");
                    break;
                case SUCCESS:
                    hideLoading();
                    Bundle args = new Bundle();
                    args.putString("email", state.getData().getEmail());
                    Navigation.findNavController(view)
                            .navigate(R.id.action_registerFragment_to_emailVerificationFragment, args);
                    break;
            }
        });
    }

}
