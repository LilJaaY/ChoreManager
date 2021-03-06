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

        String sql = "SELECT * FROM " + DbHandler.USER_TABLE_NAME + " WHERE "
                + DbHandler.USER_NAME + "='" + username + "' AND " + DbHandler.USER_PASSWORD
                + "='" + password + "';";
        Cursor cursor = DbHandler.getInstance(this).getWritableDatabase().rawQuery(sql, null);

        if(cursor.getCount()>0) {
            cursor.moveToNext();
            id = cursor.getInt(cursor.getColumnIndex(DbHandler.USER_ID));

            Intent intent = new Intent(Authentication.this, MainActivity.class);
            intent.putExtra("Id", id);
            startActivityForResult(intent, 1);
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

        String sql = "SELECT * FROM " + DbHandler.USER_TABLE_NAME + " WHERE " + DbHandler.USER_NAME
                + "='" + user + "' AND " + DbHandler.USER_RECOVERY + "='" + recovery + "';";
        Cursor cursor = DbHandler.getInstance(this).getReadableDatabase().rawQuery(sql, null);
        if(cursor.moveToNext()) {
            recoveredPassword = cursor.getString(cursor.getColumnIndex(DbHandler.USER_PASSWORD));
            Toast.makeText(getApplicationContext(), recoveredPassword, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "No matching record.", Toast.LENGTH_LONG).show();
        }
        cursor.close();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != RESULT_OK) {
            finish();
        }
        usernameText.setText("");
        passwordText.setText("");
    }
}
