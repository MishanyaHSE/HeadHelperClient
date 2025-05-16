package com.example.headachediary.presentation.view.auth;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.headachediary.R;
import com.example.headachediary.presentation.viewmodel.auth.ResetViewModel;
import com.example.headachediary.presentation.viewmodel.auth.VerifyViewModel;

public class ForgotPasswordFragment extends Fragment {
    private EditText emailEditText;
    private ProgressDialog progressDialog;

    private ResetViewModel resetViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forgot_password, container, false);

        emailEditText = view.findViewById(R.id.emailEditText);
        Button resetButton = view.findViewById(R.id.resetButton);
        TextView backToLoginText = view.findViewById(R.id.backToLoginText);
        resetViewModel = new ViewModelProvider(requireActivity()).get(ResetViewModel.class);

        resetButton.setOnClickListener(v -> attemptReset());
        backToLoginText.setOnClickListener(v -> goBackToLogin());
        setupObservers();

        return view;
    }

    private void attemptReset() {
        String email = emailEditText.getText().toString().trim();

        if (email.isEmpty()) {
            emailEditText.setError("Введите email");
            return;
        }
        resetViewModel.forgotPassword(email);
        resetViewModel.setEmail(email);



    }

    private void setupObservers() {
        resetViewModel.getForgotState().observe(getViewLifecycleOwner(), state -> {
            switch(state.getStatus()) {
                case ERROR:
                    hideLoading();
                    showError(state.getError());
                    break;
                case LOADING:
                    showLoading("Отправляем письмо...");
                    break;
                case SUCCESS:
                    hideLoading();
                    Log.d("SUCCESS", "here");
                    NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_auth);
                    navController.navigate(R.id.action_forgotPassword_to_resetPassword);
                    Toast.makeText(getContext(), "Код доступа отправлен на почту " + resetViewModel.getResetEmail(),
                            Toast.LENGTH_SHORT).show();
                    break;
            }
        });
    }


    private void showLoading(String message) {
        progressDialog = new ProgressDialog(requireContext());
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void hideLoading() {

        Log.d("HIDE IN FORGOT", "here");
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

    private void goBackToLogin() {
        requireActivity().getSupportFragmentManager().popBackStack();
    }
}
