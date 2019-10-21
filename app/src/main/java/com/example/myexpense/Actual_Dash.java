package com.example.myexpense;

import androidx.appcompat.app.AppCompatActivity;

import java.util.*;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;

public class Actual_Dash extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private DataBaseHelper dataBaseHelper = new DataBaseHelper(this);
    private PieChartView pieChartView;
    private TextView more_details, add_expense;
    private Spinner actual_dash_spinner;
    private String chosen;
    private BarChart barChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actual__dash);
        NotifyService notifyService = new NotifyService(dataBaseHelper);
        notifyService.displayNotification(getApplicationContext());
        more_details = findViewById(R.id.More_Details);
        add_expense = findViewById(R.id.Add_Expense);
        actual_dash_spinner = findViewById(R.id.Actual_Dash_Spinner);
        pieFunction();
        spinnerFunction();
        actual_dash_spinner.setOnItemSelectedListener(this);
        more_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), DashBoard.class));
            }
        });
        add_expense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AddExpense.class));
            }
        });
    }

    private void spinnerFunction() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item,
                getResources().getStringArray(R.array.Months));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        actual_dash_spinner.setAdapter(adapter);
    }
    public void calculateEachCategory() {
        DataBaseHelper.setMonth_exp_food_total(0);
        DataBaseHelper.setMonth_exp_entertainment_total(0);
        DataBaseHelper.setMonth_exp_travel_total(0);
        DataBaseHelper.setMonth_exp_total(0);
        int num = (int) DataBaseHelper.getProfilesCount(dataBaseHelper.getReadableDatabase(), "TransDetails");
        while (num != 0) {
            DataBaseHelper.getMonthData(dataBaseHelper.getReadableDatabase(), chosen, num);
            num--;
        }
    }

    public void pieFunction() {
        pieChartView = findViewById(R.id.chart);
        List<SliceValue> pieData = new ArrayList<>();
        pieData.add(new SliceValue(DataBaseHelper.getMonth_exp_food_total(), Color.rgb(86, 252, 3)).setLabel("Food ₹"
                + DataBaseHelper.getMonth_exp_food_total()));
        pieData.add(new SliceValue(DataBaseHelper.getMonth_exp_travel_total(), Color.rgb(245, 94, 81)).setLabel("Travel ₹"
                + DataBaseHelper.getMonth_exp_travel_total()));
        pieData.add(new SliceValue(DataBaseHelper.getMonth_exp_entertainment_total(), Color.rgb(194, 105, 245)).setLabel("Entertainment ₹"
                + DataBaseHelper.getMonth_exp_entertainment_total()));
        PieChartData pieChartData = new PieChartData(pieData);
        pieChartData.setHasLabels(true).setValueLabelTextSize(12);
        pieChartData.setHasCenterCircle(true).setCenterText1("Expenditure: " + System.lineSeparator() + "₹" + DataBaseHelper.getMonthlyExpenses()).
                setCenterText1FontSize(20).setCenterText1Color(Color.parseColor("#F3E6E8"));
        pieChartView.setPieChartData(pieChartData);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        chosen = parent.getItemAtPosition(position).toString();
        switch (chosen) {
            case "January":
                chosen = "1";
                break;
            case "February":
                chosen = "2";
                break;
            case "March":
                chosen = "3";
                break;
            case "April":
                chosen = "4";
                break;
            case "May":
                chosen = "5";
                break;
            case "June":
                chosen = "6";
                break;
            case "July":
                chosen = "7";
                break;
            case "August":
                chosen = "8";
                break;
            case "September":
                chosen = "9";
                break;
            case "October":
                chosen = "10";
                break;
            case "November":
                chosen = "11";
                break;
            case "December":
                chosen = "12";
                break;

        }
        calculateEachCategory();
        pieFunction();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
