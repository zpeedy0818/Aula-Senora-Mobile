package co.edu.aulasenora;

import android.graphics.Color;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

import co.edu.aulasenora.databinding.ActivityStatsBinding;
import co.edu.aulasenora.db.DatabaseHelper;

public class StatsActivity extends AppCompatActivity {

    private ActivityStatsBinding binding;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityStatsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dbHelper = new DatabaseHelper(this);

        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(0, systemBars.top, 0, systemBars.bottom);
            return insets;
        });

        setupBarChart();
        setupPieChart();

        binding.includeHeader.btnBack.setOnClickListener(v -> finish());
        binding.includeHeader.headerBar.setBackgroundColor(getColor(R.color.adminPrimary));
        binding.includeHeader.tvTitle.setText("Estadísticas del Sistema");
    }

    private void setupBarChart() {
        int studentCount = dbHelper.getRoleCount("Estudiante");
        int volunteerCount = dbHelper.getRoleCount("Voluntario");

        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0f, studentCount));
        entries.add(new BarEntry(1f, volunteerCount));

        BarDataSet dataSet = new BarDataSet(entries, "Usuarios por rol");
        int cyan = Color.parseColor("#00BBD4");
        int orange = Color.parseColor("#FFB74D");
        dataSet.setColors(new int[]{cyan, orange});
        dataSet.setValueTextSize(14f);
        dataSet.setValueTextColor(Color.DKGRAY);

        BarData barData = new BarData(dataSet);
        barData.setBarWidth(0.5f);

        BarChart barChart = binding.barChart;
        barChart.setData(barData);
        barChart.setFitBars(true);
        barChart.setDrawValueAboveBar(true);
        barChart.getDescription().setEnabled(false);
        barChart.getLegend().setEnabled(false);
        barChart.animateY(800);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(new String[]{"Estudiantes", "Voluntarios"}));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setDrawGridLines(false);
        xAxis.setTextSize(12f);

        barChart.getAxisLeft().setGranularity(1f);
        barChart.getAxisLeft().setAxisMinimum(0f);
        barChart.getAxisRight().setEnabled(false);
        barChart.invalidate();
    }

    private void setupPieChart() {
        long studentSeconds = dbHelper.getTotalTimeInAppByRole("Estudiante");
        long volunteerSeconds = dbHelper.getTotalTimeInAppByRole("Voluntario");

        long studentHours = studentSeconds / 3600;
        long volunteerHours = volunteerSeconds / 3600;

        ArrayList<PieEntry> entries = new ArrayList<>();
        if (studentHours > 0 || volunteerHours > 0) {
            entries.add(new PieEntry(studentHours, "Estudiantes (" + studentHours + "h)"));
            entries.add(new PieEntry(volunteerHours, "Voluntarios (" + volunteerHours + "h)"));
        }

        PieDataSet dataSet = new PieDataSet(entries, "");
        int cyan = Color.parseColor("#00BBD4");
        int orange = Color.parseColor("#FFB74D");
        dataSet.setColors(new int[]{cyan, orange});
        dataSet.setValueTextSize(14f);
        dataSet.setValueTextColor(Color.WHITE);
        dataSet.setSliceSpace(3f);

        PieData pieData = new PieData(dataSet);
        pieData.setValueFormatter(new PercentFormatter());

        PieChart pieChart = binding.pieChart;
        pieChart.setData(pieData);
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleRadius(40f);
        pieChart.setHoleColor(Color.TRANSPARENT);
        pieChart.setTransparentCircleRadius(45f);
        pieChart.setCenterText("Horas en App");
        pieChart.setCenterTextSize(14f);
        pieChart.setCenterTextColor(Color.DKGRAY);
        pieChart.getLegend().setTextSize(13f);
        pieChart.animateY(800);
        pieChart.invalidate();
    }
}
