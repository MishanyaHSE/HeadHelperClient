package com.example.headachediary.presentation.view.mainmenu;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.headachediary.R;
import com.example.headachediary.domain.statistics.StatisticsCreate;
import com.example.headachediary.domain.statistics.StatisticsResponse;
import com.example.headachediary.domain.statistics.TimeStats;
import com.example.headachediary.domain.statistics.TopDuration;
import com.example.headachediary.presentation.viewmodel.mainmenu.StatisticsViewModel;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class StatisticsFragment extends Fragment {
    private ProgressDialog progressDialog;
    private BarChart timeBarChart;
    private PieChart pieChart;
    private PieChart triggersChart;
    private StatisticsViewModel statisticsViewModel;
    private LinearProgressIndicator progressIndicator;
    private TextView tvPercentage, tvFillStatus, tvMeanText;

    private TextInputEditText startDateEditText, endDateEditText;
    private TextInputLayout startDateContainer, endDateContainer;
    private Button updateButton;
    private HorizontalBarChart durationChart;

    private static final int COLOR_BLUE = Color.parseColor("#2196F3");
    private static final int COLOR_GREEN = Color.parseColor("#4CAF50");
    private static final int COLOR_YELLOW = Color.parseColor("#FFC107");


    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_statistics, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Инициализация элементов интерфейса
        timeBarChart = view.findViewById(R.id.timeBarChart);
        pieChart = view.findViewById(R.id.pieChart);
        triggersChart = view.findViewById(R.id.triggersChart);
        startDateEditText = view.findViewById(R.id.startDateEditText);
        endDateEditText = view.findViewById(R.id.endDateEditText);
        startDateContainer = view.findViewById(R.id.startDateContainer);
        endDateContainer = view.findViewById(R.id.endDateContainer);
        updateButton = view.findViewById(R.id.updateButton);
        progressIndicator = view.findViewById(R.id.progressIndicator);
        tvPercentage = view.findViewById(R.id.tvPercentage);
        tvFillStatus = view.findViewById(R.id.tvFillStatus);
        tvMeanText = view.findViewById(R.id.meanText);
        durationChart = view.findViewById(R.id.durationChart);


        // Установка начальных дат (последние 7 дней)
        Calendar calendar = Calendar.getInstance();
        String endDate = dateFormat.format(calendar.getTime());
        calendar.add(Calendar.DAY_OF_MONTH, -6);
        String startDate = dateFormat.format(calendar.getTime());
        startDateEditText.setText(startDate);
        endDateEditText.setText(endDate);

        // Настройка DatePicker
        setupDatePickers();

        // Инициализация ViewModel
        statisticsViewModel = new ViewModelProvider(requireActivity()).get(StatisticsViewModel.class);
        updateStatistics(startDate, endDate);
        setupObserver();

        // Обработчик кнопки обновления
        updateButton.setOnClickListener(v -> validateAndUpdate());
    }

    private void setupDatePickers() {
        startDateEditText.setOnClickListener(v -> showDatePicker(true));
        endDateEditText.setOnClickListener(v -> showDatePicker(false));
    }

    private void showDatePicker(boolean isStartDate) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(requireContext(),
                (view, year, month, day) -> {
                    String selectedDate = String.format(Locale.getDefault(),
                            "%04d-%02d-%02d", year, month + 1, day);
                    if (isStartDate) {
                        startDateEditText.setText(selectedDate);
                    } else {
                        endDateEditText.setText(selectedDate);
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    private void validateAndUpdate() {
        String start = startDateEditText.getText().toString();
        String end = endDateEditText.getText().toString();

        // Сброс предыдущих ошибок
        startDateContainer.setError(null);
        endDateContainer.setError(null);

        if (start.isEmpty() || end.isEmpty()) {
            Toast.makeText(requireContext(), "Заполните обе даты", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            Date startDate = dateFormat.parse(start);
            Date endDate = dateFormat.parse(end);

            // Проверка что startDate <= endDate
            if (startDate.after(endDate)) {
                startDateContainer.setError("Дата начала не может быть позже окончания");
                return;
            }

            // Проверка минимального периода (3 дня)
            long diff = endDate.getTime() - startDate.getTime();
            long diffDays = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);

            if (diffDays < 2) { // Разница в 2 дня = 3 дня включая границы
                endDateContainer.setError("Минимальный период - 3 дня");
                startDateContainer.setError("Минимальный период - 3 дня");
                return;
            }

            // Обновление данных
            updateStatistics(start, end);

        } catch (ParseException e) {
            Toast.makeText(requireContext(),
                    "Ошибка формата даты", Toast.LENGTH_SHORT).show();
        }
    }

    private void renderDurationChart(List<TopDuration> durations) {
        if (durations == null || durations.isEmpty()) {
            durationChart.setVisibility(View.GONE);
            return;
        }
        durationChart.setVisibility(View.VISIBLE);

        List<BarEntry> entries = new ArrayList<>();
        final List<String> labels = new ArrayList<>();

        // Берем первые 3 элемента
        int count = Math.min(durations.size(), 3);
        for (int i = 0; i < count; i++) {
            TopDuration item = durations.get(i);
            entries.add(new BarEntry(i, item.getCount()));
            labels.add(item.getDuration());
        }

        BarDataSet set = new BarDataSet(entries, "Продолжительность приступов");
        set.setColors(new int[]{R.color.blue_chart, R.color.green, R.color.yellow}, requireContext());
        set.setValueTextSize(12f);
        set.setValueTextColor(Color.BLACK);
        set.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                // Отображаем значение и продолжительность
                int index = (int) value;
                if (index >= 0 && index < labels.size()) {
                    return labels.get(index) + ": " + (int) value;
                }
                return String.valueOf((int) value);
            }
        });

        // Настройка осей
        YAxis yAxisLeft = durationChart.getAxisLeft();
        yAxisLeft.setEnabled(false); // Отключаем левую ось

        YAxis yAxisRight = durationChart.getAxisRight();
        yAxisRight.setEnabled(false); // Отключаем правую ось

        XAxis xAxis = durationChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setDrawLabels(false); // Скрываем подписи оси X

        // Настройка легенды
        Legend legend = durationChart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setDrawInside(false);
        legend.setTextSize(12f);

        // Создаем кастомные записи для легенды
        List<LegendEntry> legendEntries = new ArrayList<>();
        int[] colors = {
                ContextCompat.getColor(requireContext(), R.color.blue_chart),
                ContextCompat.getColor(requireContext(), R.color.green),
                ContextCompat.getColor(requireContext(), R.color.yellow)
        };

        for (int i = 0; i < Math.min(durations.size(), 3); i++) {
            TopDuration item = durations.get(i);
            legendEntries.add(new LegendEntry(
                    item.getDuration() + " (" + item.getCount() + ")", // label
                    Legend.LegendForm.SQUARE,                           // form
                    10f,                                                // formSize
                    2f,                                                 // formLineWidth
                    null,                                               // dashPathEffect
                    colors[i]                                           // color
            ));
        }

        legend.setCustom(legendEntries);

        durationChart.getDescription().setEnabled(false);
        durationChart.setDrawValueAboveBar(true);
        durationChart.setFitBars(true);

        // Анимация
        durationChart.animateY(1000);
        durationChart.setData(new BarData(set));
        durationChart.invalidate();
    }

    private void updateStatistics(String startDate, String endDate) {
        StatisticsCreate sc = new StatisticsCreate();
        sc.setDateStart(startDate);
        sc.setDateEnd(endDate);
        statisticsViewModel.getStatistics(sc);
    }

    private void setupObserver() {
        statisticsViewModel.getStatisticsState().observe(getViewLifecycleOwner(), state -> {
            switch(state.getStatus()) {
                case LOADING:
                    showLoading("Формируем статистику");
                    break;
                case SUCCESS:
                    updateCharts(state.getData());
                    hideLoading();
                    break;
                case ERROR:
                    hideLoading();
                    showError("Не удалось загрузить статистику, попробуйте позже");
                    Log.d("ERROR WHILE STATISTICS", "error");
                    break;
            }
        });
    }

    private void updateCharts(StatisticsResponse statistics) {
        renderHeadacheChart(statistics);
        renderTimeChart(statistics);
        renderTriggersChart(statistics);
        updateFillStatus(statistics.getFillPercentage());
        renderDurationChart(statistics.getTopDurations());
        tvMeanText.setText(String.valueOf(statistics.getMeanIntensity()));
    }


    private void updateFillStatus(int percentage) {
        progressIndicator.setProgressCompat(percentage, true);
        tvPercentage.setText(String.format(Locale.getDefault(), "%d%%", percentage));

        String statusText;
        if (percentage < 30) {
            statusText = "Низкая полнота записей - рекомендуем вести дневник регулярно";
            progressIndicator.setIndicatorColor(ContextCompat.getColor(requireContext(), R.color.red));
        } else if (percentage < 70) {
            statusText = "Средняя полнота записей - можно лучше";
            progressIndicator.setIndicatorColor(ContextCompat.getColor(requireContext(), R.color.yellow));
        } else {
            statusText = "Высокая полнота записей - отличный результат!";
            progressIndicator.setIndicatorColor(ContextCompat.getColor(requireContext(), R.color.green));
        }
        tvFillStatus.setText(statusText);
    }

    private void renderHeadacheChart(StatisticsResponse statistics) {
        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(statistics.getHeadacheDays().getWithPain(), "С болью"));
        entries.add(new PieEntry(statistics.getHeadacheDays().getWithoutPain(), "Без боли"));

        PieDataSet set = new PieDataSet(entries, "Дни с болью/без");
        set.setColors(new int[] {R.color.red, R.color.green}, requireContext());
        set.setValueTextSize(14f);

        pieChart.getDescription().setEnabled(false);
        pieChart.setEntryLabelTextSize(12f);

        Legend legend = pieChart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setTextSize(12f);

        pieChart.setData(new PieData(set));
        pieChart.invalidate();
    }

    private void renderTriggersChart(StatisticsResponse statistics) {
        List<PieEntry> entries = new ArrayList<>();
        for (var trigger: statistics.getTopTriggers()) {
            entries.add(new PieEntry(trigger.getCount(), trigger.getName()));
        }

        PieDataSet set = new PieDataSet(entries, "Самые частые причины");
        set.setColors(new int[] {R.color.red, R.color.green, R.color.blue_chart, R.color.yellow},
                requireContext());
        set.setValueTextSize(14f);

        triggersChart.getDescription().setEnabled(false);
        triggersChart.setEntryLabelTextSize(12f);

        Legend legend = triggersChart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setTextSize(12f);

        triggersChart.setData(new PieData(set));
        triggersChart.invalidate();
    }

    private void renderTimeChart(StatisticsResponse statistics) {
        List<BarEntry> entries = new ArrayList<>();
        TimeStats ts = statistics.getTimeStats();
        final String[] labels = {"Утро", "День", "Вечер", "Ночь"};

        entries.add(new BarEntry(0f, ts.getMorning()));
        entries.add(new BarEntry(1f, ts.getAfternoon()));
        entries.add(new BarEntry(2f, ts.getEvening()));
        entries.add(new BarEntry(3f, ts.getNight()));

        BarDataSet set = new BarDataSet(entries, "Время начала приступов");
        set.setColor(ContextCompat.getColor(requireContext(), R.color.blue_chart));

        XAxis xAxis = timeBarChart.getXAxis();
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                int index = (int) value;
                return (index >= 0 && index < labels.length) ? labels[index] : "";
            }
        });
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(labels.length);

        timeBarChart.getDescription().setEnabled(false);
        timeBarChart.getLegend().setEnabled(false);
        timeBarChart.getAxisRight().setEnabled(false);

        YAxis leftAxis = timeBarChart.getAxisLeft();
        leftAxis.setGranularity(1f);
        leftAxis.setAxisMinimum(0f);

        timeBarChart.setData(new BarData(set));
        timeBarChart.invalidate();
    }

//    private void renderTriggersChart(StatisticsResponse statistics) {
//        List<BarEntry> entries = new ArrayList<>();
//        List<String> labels = new ArrayList<>();
//
//        for (int i = 0; i < statistics.getTopTriggers().size(); i++) {
//            TopTrigger trigger = statistics.getTopTriggers().get(i);
//            entries.add(new BarEntry(i, trigger.getCount()));
//            labels.add(trigger.getName());
//        }
//
//        BarDataSet set = new BarDataSet(entries, "Топ триггеров");
//        set.setColors(new int[]{R.color.blue_chart, R.color.yellow, R.color.green},
//                requireContext());
//        set.setValueTextSize(12f);
//        set.setValueFormatter(new ValueFormatter() {
//            @Override
//            public String getFormattedValue(float value) {
//                int index = (int) value;
//                return (index >= 0 && index < labels.size()) ? labels.get(index) : "";
//            }
//        });
//
//        triggersChart.getDescription().setEnabled(false);
//        triggersChart.getLegend().setEnabled(false);
//        triggersChart.getXAxis().setEnabled(false);
//        triggersChart.getAxisLeft().setEnabled(false);
//        triggersChart.getAxisRight().setEnabled(false);
//
//        triggersChart.setData(new BarData(set));
//        triggersChart.invalidate();
//    }

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