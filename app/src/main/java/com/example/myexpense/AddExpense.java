package com.example.myexpense;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class AddExpense extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    TextView date, save;
    private EditText amount, name;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private Spinner spinner;
    private static String chosen;
    private DataBaseHelper dataBaseHelper = new DataBaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);
        amount = findViewById(R.id.Amount);
        date = findViewById(R.id.Date);
        save = findViewById(R.id.save);
        name = findViewById(R.id.Name);
        spinner = findViewById(R.id.Spinner1);
        spinnerFunction();
        spinner.setOnItemSelectedListener(this);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DATE);
                date.setVisibility(View.GONE);
                DatePickerDialog dialog = new DatePickerDialog(
                        AddExpense.this,
                        R.style.my_dialog_theme, mDateSetListener, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                dialog.show();

            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                date.setText(day + "/" + (month + 1) + "/" + year);
                date.setVisibility(View.VISIBLE);
            }
        };
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataBaseHelper.writeData(dataBaseHelper.getWritableDatabase(), date.getText().toString(), amount.getText().toString(),
                        name.getText().toString(), getChosen());
                Toast.makeText(getApplicationContext(), "Data Stored!", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        chosen = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void spinnerFunction() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item,
                getResources().getStringArray(R.array.names));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    public static String getChosen() {
        return chosen;
    }
}

