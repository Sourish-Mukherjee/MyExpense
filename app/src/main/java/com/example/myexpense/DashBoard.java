package com.example.myexpense;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.example.myexpense.R.layout.activity_dash_board;

public class DashBoard extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    DataBaseHelper dataBaseHelper = new DataBaseHelper(this);
    private static TextView totalExp;
    private Spinner typeSelector;
    private static TextView dateForDashboard;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private ListView lv;
    private List<ExpenseDetails> listOfExpense;
    private MyCustomListAdapter mc;
    private LinearLayout imageLinearLayout;
    private ImageView dashboard_imageview_1;
    private String chosen;
    private int listposition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_dash_board);
        initialize();
        calender();
        spinnerFunction();
        typeSelector.setOnItemSelectedListener(this);
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long arg3) {
                imageLinearLayout.setVisibility(View.VISIBLE);
                listposition = position;
                return false;
            }
        });
        dashboard_imageview_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataBaseHelper.deleteDataFromTable(dataBaseHelper.getWritableDatabase(), listOfExpense.get(listposition).getText());
                listOfExpense.remove(listOfExpense.get(listposition));
                Toast.makeText(getApplicationContext(),"Item Deleted!",Toast.LENGTH_SHORT).show();
                mc.notifyDataSetChanged();
                lv.setAdapter(mc);
            }
        });
    }

    private void calender() {
        dateForDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DATE);
                dateForDashboard.setVisibility(View.GONE);
                DatePickerDialog dialog = new DatePickerDialog(
                        DashBoard.this,
                        R.style.my_dialog_theme, mDateSetListener, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                dialog.show();
            }
        });
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                dateForDashboard.setText(day + "/" + (month + 1) + "/" + year);
                dateForDashboard.setVisibility(View.VISIBLE);
                DataBaseHelper.setDay_exp_total(0);
                viewNewData();
            }
        };
    }

    private void initialize() {
        totalExp = findViewById(R.id.TotalExp);
        imageLinearLayout = findViewById(R.id.ImageLinearLayout);
        dashboard_imageview_1 = findViewById(R.id.Dashboard_imageview_1);
        dateForDashboard = findViewById(R.id.DateForDashBoard);
        typeSelector = findViewById(R.id.type_selector);
        lv = findViewById(R.id.ListView1);
        lv.setVisibility(View.INVISIBLE);
        listOfExpense = new ArrayList<>();

    }

    public void viewNewData() {
        listOfExpense.clear();
        mc = new MyCustomListAdapter(this, R.layout.row_layout, listOfExpense);
        lv.setAdapter(mc);
        int num = (int) DataBaseHelper.getProfilesCount(dataBaseHelper.getReadableDatabase(), "TransDetails");
        while (num != 0) {
            String total = DataBaseHelper.getData(dataBaseHelper.getReadableDatabase(), num);
            if (total == null) {
                num--;
                continue;
            }
            String category;
            int id;
            if (total.contains("Food")) {
                id = R.drawable.food_icon;
                category = "Food";
            } else if (total.contains("Travel")) {
                id = R.drawable.travel_icon;
                category = "Travel";
            } else {
                id = R.drawable.entertainment_icon;
                category = "Entertainment";
            }
            total = total.replaceAll(category, "");
            listOfExpense.add(new ExpenseDetails(id, total));
            num--;
            totalExp.setText("Expenditure:  ₹".concat(String.valueOf(DataBaseHelper.getTotalExpenses(dataBaseHelper.getReadableDatabase()))));
        }
        MyCustomListAdapter mc = new MyCustomListAdapter(this, R.layout.row_layout, listOfExpense);
        lv.setAdapter(mc);
        if (listOfExpense.isEmpty()) {
            lv.setVisibility(View.INVISIBLE);
            totalExp.setText("Expenditure:  ₹0");
        } else {
            lv.setVisibility(View.VISIBLE);
        }
    }

    public void viewMonthlyData() {
        DataBaseHelper.setMonth_exp_food_total(0);
        DataBaseHelper.setMonth_exp_entertainment_total(0);
        DataBaseHelper.setMonth_exp_travel_total(0);
        DataBaseHelper.setMonth_exp_total(0);
        listOfExpense.clear();
        mc = new MyCustomListAdapter(getApplicationContext(), R.layout.row_layout, listOfExpense);
        lv.setAdapter(mc);
        int num = (int) DataBaseHelper.getProfilesCount(dataBaseHelper.getReadableDatabase(), "TransDetails");
        while (num != 0) {
            String total = DataBaseHelper.getMonthData(dataBaseHelper.getReadableDatabase(), chosen, num);
            if (total == null) {
                num--;
                continue;
            }
            String category;
            int id;
            if (total.contains("Food")) {
                id = R.drawable.food_icon;
                category = "Food";
            } else if (total.contains("Travel")) {
                id = R.drawable.travel_icon;
                category = "Travel";
            } else {
                id = R.drawable.entertainment_icon;
                category = "Entertainment";
            }
            total = total.replaceAll(category, "");
            listOfExpense.add(new ExpenseDetails(id, total));
            num--;
            totalExp.setText("Expenditure:  ₹".concat(String.valueOf(DataBaseHelper.getMonthlyExpenses())));
        }
        MyCustomListAdapter mc = new MyCustomListAdapter(getApplicationContext(), R.layout.row_layout, listOfExpense);
        lv.setAdapter(mc);
        if (listOfExpense.isEmpty()) {
            lv.setVisibility(View.INVISIBLE);
            totalExp.setText("Expenditure:  ₹0");
        } else {
            lv.setVisibility(View.VISIBLE);
        }
    }

    public static String getDateForDashboard() {
        return dateForDashboard.getText().toString();
    }

    private void spinnerFunction() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item,
                getResources().getStringArray(R.array.Months));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSelector.setAdapter(adapter);
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
        viewMonthlyData();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}