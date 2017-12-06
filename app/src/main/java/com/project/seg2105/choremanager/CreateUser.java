package com.project.seg2105.choremanager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
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

        //setting up back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Register a user");

        usernameText = findViewById(R.id.UsernameInput);
        passwordText = findViewById(R.id.PasswordInput);
        confirmText = findViewById(R.id.PasswordConfirm);
        recoveryText = findViewById(R.id.RecoveryInput);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return true;
    }

    public void createUser(View view){
        String username = usernameText.getText().toString();
        String password = passwordText.getText().toString();
        String confirm = confirmText.getText().toString();
        String recovery = recoveryText.getText().toString();
        RadioGroup group = findViewById(R.id.avatars);


        boolean meetsCriteria = (!username.isEmpty() && !password.isEmpty()
                && !confirm.isEmpty() && !recovery.isEmpty()
                && group.getCheckedRadioButtonId() != -1 && (password.equals(confirm)));

        if (meetsCriteria) {
            String avatar;
            switch (group.getCheckedRadioButtonId()) {
                case R.id.avatar1:
                    avatar = getResources().getResourceEntryName(R.drawable.boy);
                    break;
                case R.id.avatar2:
                    avatar = getResources().getResourceEntryName(R.drawable.boy_1);
                    break;
                case R.id.avatar3:
                    avatar = getResources().getResourceEntryName(R.drawable.girl);
                    break;
                case R.id.avatar4:
                    avatar = getResources().getResourceEntryName(R.drawable.girl_1);
                    break;
                case R.id.avatar5:
                    avatar = getResources().getResourceEntryName(R.drawable.man);
                    break;
                case R.id.avatar6:
                    avatar = getResources().getResourceEntryName(R.drawable.man_1);
                    break;
                case R.id.avatar7:
                    avatar = getResources().getResourceEntryName(R.drawable.man_2);
                    break;
                case R.id.avatar8:
                    avatar = getResources().getResourceEntryName(R.drawable.man_3);
                    break;
                case R.id.avatar9:
                    avatar = getResources().getResourceEntryName(R.drawable.man_4);
                    break;
                default:
                    avatar = "";
                    break;
            }
            User user = new User(username, password, avatar, 0, recovery);
            DbHandler.getInstance(this).insertUser(user);
            finish();
        } else {
            Toast.makeText(getApplicationContext(), "Incomplete/Incorrect input(s)", Toast.LENGTH_LONG).show();
        }

    }
}
