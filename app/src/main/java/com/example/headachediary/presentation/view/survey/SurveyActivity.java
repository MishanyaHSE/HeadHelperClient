package com.example.headachediary.presentation.view.survey;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.headachediary.R;
import com.example.headachediary.domain.headache.NoteCreate;
import com.example.headachediary.presentation.viewmodel.survey.SurveyViewModel;

public class SurveyActivity extends AppCompatActivity {
    private SurveyViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);
        // Инициализация ViewModel
        viewModel = new ViewModelProvider(this).get(SurveyViewModel.class);
        viewModel.getQuestionsFromServer();
        // Настройка навигации
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment_survey);
        NavController navController = navHostFragment.getNavController();
    }
}