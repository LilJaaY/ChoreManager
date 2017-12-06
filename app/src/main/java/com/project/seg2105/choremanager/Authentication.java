package com.project.seg2105.choremanager;

import android.app.DialogFragment;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Authentication extends AppCompatActivity implements PassRecoveryFragment.RecoveryDialogListener {

    EditText usernameText;
    EditText passwordText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        usernameText = findViewById(R.id.UsernameInput);
        passwordText = findViewById(R.id.PasswordInput);
    }

    public void login(View view){
        int id = 0;
        String username = usernameText.getText().toString();
        String password = passwordText.getText().toString();

        //Todo: (Jalil) Query DB for username and password match
        String sql = "SELECT * FROM " + DbHandler.USAGE_TABLE_NAME + " WHERE "
                + DbHandler.USER_NAME + "=" + username + " AND " + DbHandler.USER_PASSWORD
                + "=" + password;
        Cursor cursor = DbHandler.getInstance(this).getWritableDatabase().rawQuery(sql, null);

        if(cursor.getCount()>0) {
            cursor.moveToNext();
            id = cursor.getInt(cursor.getColumnIndex(DbHandler.USER_ID));

            Intent intent = new Intent(Authentication.this, MainActivity.class);
            intent.putExtra("Id", id);
            startActivity(intent);
        } else {
            usernameText.getText().clear();
            passwordText.getText().clear();

            Toast.makeText(getApplicationContext(), "Authentication Failed", Toast.LENGTH_SHORT).show();
        }
        cursor.close();
    }

    public void createUser(View view){
        startActivity(new Intent(Authentication.this, CreateUser.class));
    }

    public void recoverPassword(View view){
        DialogFragment recoveryDialog = new PassRecoveryFragment();
        recoveryDialog.show(getFragmentManager(), "recovery");
    }

    public void onPosClick(DialogFragment dialog, String user, String recovery){
        String recoveredPassword = "";

        //Todo: (Jalil) Query DB for a user with matching username and recovery input (the ones in this function's listener) and fetch password as string.

        Toast.makeText(getApplicationContext(), recoveredPassword, Toast.LENGTH_LONG).show();
    }
}
