package com.project.seg2105.choremanager;

import android.app.DialogFragment;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Authentication extends AppCompatActivity {

    EditText usernameText;
    EditText passwordText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        usernameText = findViewById(R.id.UsernameInput);
        passwordText = findViewById((R.id.PasswordInput));
    }

    public void login(View view){
        String username = usernameText.getText().toString();
        String password = passwordText.getText().toString();
        boolean authenticated = false;

        //Todo: (Jalil) Query DB for username and password match

        if (authenticated) {
            Intent intent = new Intent(Authentication.this, MainActivity.class);
            intent.putExtra("Username", username);
            startActivity(intent);
        } else {
            usernameText.getText().clear();
            passwordText.getText().clear();

            Toast.makeText(getApplicationContext(), "Authentication Failed", Toast.LENGTH_SHORT).show();
        }
    }

    public void createUser(View view){
        startActivity(new Intent(Authentication.this, CreateUser.class));
    }

    public void recoverPassword(View view){
        DialogFragment recoveryDialog = new PassRecoveryFragment();
        recoveryDialog.show(getFragmentManager(), "recovery");
    }
}
