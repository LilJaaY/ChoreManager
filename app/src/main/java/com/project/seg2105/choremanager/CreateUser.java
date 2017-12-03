package com.project.seg2105.choremanager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class CreateUser extends AppCompatActivity {

    EditText usernameText;
    EditText passwordText;
    EditText confirmText;
    EditText recoveryText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_newuser);

        usernameText = findViewById(R.id.UsernameInput);
        passwordText = findViewById(R.id.PasswordInput);
        confirmText = findViewById(R.id.PasswordConfirm);
        recoveryText = findViewById(R.id.RecoveryInput);
    }

    public void createUser(View view){
        String username = usernameText.getText().toString();
        String password = passwordText.getText().toString();
        String confirm = confirmText.getText().toString();
        String recovery = recoveryText.getText().toString();

        boolean meetsCriteria = (username != "" && username != null && password != "" &&
                password != null && confirm != "" && confirm != null && recovery != "" && recovery != null );

        if (meetsCriteria) {
            //Todo: (Jalil) Store new user in DB

            startActivity(new Intent(CreateUser.this, Authentication.class));
        } else {
            Toast.makeText(getApplicationContext(), "Incomplete/Incorrect input(s)", Toast.LENGTH_LONG).show();
        }

    }
}
