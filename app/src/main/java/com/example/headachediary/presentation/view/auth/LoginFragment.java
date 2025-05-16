package com.example.headachediary.presentation.view.auth;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.example.headachediary.data.source.util.managers.CredentialManager;
import com.example.headachediary.data.source.util.managers.TokenManager;
import com.example.headachediary.domain.auth.UserAuth;
import com.example.headachediary.presentation.view.mainmenu.MainActivity;
import com.example.headachediary.presentation.viewmodel.auth.LoginViewModel;

public class LoginFragment extends Fragment {

    private LoginViewModel loginViewModel;
    private ProgressDialog progressDialog;
    private EditText emailEditText, passwordEditText;
    private CredentialManager credentialManager;
    private UserAuth cred;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        emailEditText = view.findViewById(R.id.emailEditText);
        passwordEditText = view.findViewById(R.id.passwordEditText);
        Button loginButton = view.findViewById(R.id.loginButton);
        Button registerButton = view.findViewById(R.id.registerButton);
        TextView forgotPassword = view.findViewById(R.id.forgotPasswordText);

        loginButton.setOnClickListener(v -> attemptLogin());
        registerButton.setOnClickListener(v -> goToRegister());
        forgotPassword.setOnClickListener(v -> goToForgotPassword());
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        credentialManager = new CredentialManager(requireContext());
        setupObserver();
        cred = credentialManager.getCred();
        if (cred.getUsername() != null && cred.getPassword() != null) {
            Log.d("Logging from saved", "FOUND SAVED CRED");
            loginViewModel.login(cred);
        }
        return view;
    }

    private void attemptLogin() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        cred = new UserAuth();
        cred.setUsername(email);
        cred.setPassword(password);
        Log.d("GETTING TOKEN", cred.toString());
        loginViewModel.login(cred);
    }

    private void goToRegister() {
        Navigation.findNavController(getView())
                .navigate(R.id.action_loginFragment_to_registerFragment);
    }

    private void goToForgotPassword() {
        Navigation.findNavController(getView())
                .navigate(R.id.action_loginFragment_to_forgotPassword);
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

    private void setupObserver() {
        loginViewModel.getLoginState().observe(getViewLifecycleOwner(), state -> {
            switch(state.getStatus()) {
                case ERROR:
                    hideLoading();
                    credentialManager.deleteCred();
                    String error = state.getError();
                    if (error.equals("401")) {
                        showError("Неправильный email/пароль, попробуйте еще раз");
                    } else if (error.equals("422")) {
                        showError("Некорректный формат email");
                    } else {
                        showError(error);
                    }
                    break;
                case LOADING:
                    showLoading();
                    break;
                case SUCCESS:
                    hideLoading();
                    credentialManager.saveCred(cred);
                    TokenManager tokenManager = new TokenManager(requireContext());
                    tokenManager.saveToken(state.getData().getAccessToken(),
                            state.getData().getRefreshToken());
                    enterMainActivity();
                    break;
            }
        });
    }

    private void enterMainActivity() {
        Intent intent = new Intent(requireContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        requireContext().startActivity(intent);
        requireActivity().finish();
    }

}
