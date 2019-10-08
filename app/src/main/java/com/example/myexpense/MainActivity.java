package com.example.myexpense;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
}
