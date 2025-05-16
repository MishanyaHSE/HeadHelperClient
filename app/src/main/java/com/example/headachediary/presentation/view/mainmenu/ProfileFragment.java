package com.example.headachediary.presentation.view.mainmenu;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.headachediary.R;
import com.example.headachediary.data.source.util.managers.CredentialManager;
import com.example.headachediary.data.source.util.managers.TokenManager;
import com.example.headachediary.presentation.view.auth.AuthActivity;
import com.example.headachediary.presentation.viewmodel.mainmenu.ProfileViewModel;

public class ProfileFragment extends Fragment {

    private TextView tvName, tvEmail;
    private Button btnEditSurvey, btnLogout;

    private ProgressDialog progressDialog;

    private ProfileViewModel profileViewModel;

    private TokenManager tokenManager;
    private CredentialManager credentialManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Инициализация элементов
        tvName = view.findViewById(R.id.tvName);
        tvEmail = view.findViewById(R.id.tvEmail);
        btnEditSurvey = view.findViewById(R.id.btnEditSurvey);
        btnLogout = view.findViewById(R.id.btnLogout);
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        tokenManager = new TokenManager(requireContext());
        // Загрузка данных пользователя
        profileViewModel.getProfileData();
        setupObservers();

        // Обработчики кликов
        btnEditSurvey.setOnClickListener(v -> navigateToSurvey());
        btnLogout.setOnClickListener(v -> showLogoutConfirmation());



    }

    private void navigateToSurvey() {
        Navigation.findNavController(requireView())
                .navigate(R.id.action_profileFragment_to_surveyFragment);
    }

    private void showLogoutConfirmation() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Подтверждение выхода")
                .setMessage("Вы уверены, что хотите выйти из аккаунта?")
                .setPositiveButton("Выйти", (dialog, which) -> logout())
                .setNegativeButton("Отмена", null)
                .show();
    }

    private void logout() {

        tokenManager.deleteTokens();

        if (getActivity() != null) {
            startActivity(new Intent(requireActivity(), AuthActivity.class));
            requireActivity().finish();
        }
    }

    private void setupObservers() {
        profileViewModel.getProfileState().observe(getViewLifecycleOwner(), state -> {
            switch(state.getStatus()) {
                case LOADING:
                    showLoading("Загружаем данные пользователя...");
                    break;
                case SUCCESS:
                    tvEmail.setText(state.getData().getEmail());
                    tvName.setText(state.getData().getName());
                    hideLoading();
                    break;
                case ERROR:
                    hideLoading();
                    showError(state.getError());
                    break;
            }
        });;
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
}
