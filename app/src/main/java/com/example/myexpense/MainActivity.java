package com.example.myexpense;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private DataBaseHelper db = new DataBaseHelper(this);
    private String name = "";
    private String pass = "";
    private EditText username, password;
    private TextView login, newaccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        int i = preferences.getInt("numberoflaunches", 1);
        if (i < 2){
            alarmMethod();
            i++;
            editor.putInt("numberoflaunches", i);
            editor.commit();
        }
        username = findViewById(R.id.username_edit_text);
        password = findViewById(R.id.password_edit_text);
        login = findViewById(R.id.LoginButton);
        newaccount = findViewById(R.id.New_Account_Button);
        newaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), NewAccount.class);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = username.getText().toString();
                int c = 0;
                pass = password.getText().toString();
                int total = (int) DataBaseHelper.getProfilesCount(db.getReadableDatabase(), "LoginDetails");
                while (total != 0) {
                    if (DataBaseHelper.getLoginData(db.getReadableDatabase(), name, pass, total) == false) {
                        total--;
                        continue;
                    } else {
                        Toast.makeText(getApplicationContext(), "Successful Login", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(), Actual_Dash.class));
                        onPause();
                        c = 1;
                        break;
                    }
                }
                if (c == 0)
                    Toast.makeText(getApplicationContext(), "Wrong UserName/Password", Toast.LENGTH_LONG).show();
            }
        });

    }
    private void alarmMethod(){
        //NotifyService notifyService = new NotifyService(new DataBaseHelper(this));
        //notifyService.displayNotification(this);
        Intent myIntent = new Intent(this , NotifyService.class);
        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, myIntent, 0);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 30);
        calendar.set(Calendar.HOUR, 8);
        calendar.set(Calendar.AM_PM, Calendar.PM);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000 * 60 * 60 * 24, pendingIntent);
        Toast.makeText(MainActivity.this, "Start Alarm", Toast.LENGTH_LONG).show();
    }

}
