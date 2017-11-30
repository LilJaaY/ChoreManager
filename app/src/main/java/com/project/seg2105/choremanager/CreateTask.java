package com.project.seg2105.choremanager;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Calendar;

public class CreateTask extends AppCompatActivity {
    private static final Calendar CALENDAR = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);

        //setting up back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Create a new task");

        //Initializing spinner
        Spinner spinner = findViewById(R.id.users);
        spinner.setAdapter(PeopleFragment.adapter);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return true;
    }

    public void onCreateTaskClick(View view) {
        String title = ((EditText)findViewById(R.id.title)).getText().toString();
        //make sure it is not empty
        int points = Integer.parseInt(((EditText)findViewById(R.id.points)).getText().toString());
        int assigneeId =  Integer.parseInt(((TextView)(((Spinner)findViewById(R.id.users)).getSelectedView().findViewById(R.id.userId))).getText().toString());
        String note = ((EditText)findViewById(R.id.note)).getText().toString();
        String description = ((EditText)findViewById(R.id.description)).getText().toString();
        String date = CALENDAR.get(Calendar.YEAR) + "-" + CALENDAR.get(Calendar.MONTH) + "-" + CALENDAR.get(Calendar.DAY_OF_MONTH);
        Task task = new Task(assigneeId, assigneeId, title, description, note, date);
        DbHandler.getInstance(this).insertTask(task);

        //go back
        finish();
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            int year = CALENDAR.get(Calendar.YEAR);
            int month = CALENDAR.get(Calendar.MONTH);
            int day = CALENDAR.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            CALENDAR.set(Calendar.YEAR, year);
            CALENDAR.set(Calendar.MONTH, month);
            CALENDAR.set(Calendar.DAY_OF_MONTH, day);
        }
    }
}
