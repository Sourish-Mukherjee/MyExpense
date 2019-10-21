package com.example.myexpense;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
public class NewAccount extends AppCompatActivity {

    private DataBaseHelper db = new DataBaseHelper(this);
    private EditText user_name, pass_word, re_password;
    private TextView submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_account2);
        initialize();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pass_word.getText().toString().equals(re_password.getText().toString())) {
                    DataBaseHelper.writeNewAccountData(db.getWritableDatabase(), user_name.getText().toString(), pass_word.getText().toString());
                    Toast.makeText(getApplicationContext(), "Account Details Saved !", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Password Doesn't Match", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    private void initialize() {
        user_name = findViewById(R.id.User_Name_New_Account);
        pass_word = findViewById(R.id.Password_New_Account);
        re_password = findViewById(R.id.Re_Password_New_Account);
        submit = findViewById(R.id.Submit_New_Account);
    }
}
