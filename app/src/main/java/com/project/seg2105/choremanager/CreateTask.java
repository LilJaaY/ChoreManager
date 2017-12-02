package com.project.seg2105.choremanager;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.database.Cursor;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Locale;

public class CreateTask extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>{

    private static final Calendar CALENDAR = Calendar.getInstance();
    private SimpleCursorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);

        //Intialize equipments' listview
        ListView listView = findViewById(R.id.tools);
        adapter = new SimpleCursorAdapter(this,
                R.layout.tool_row, null,
                new String[] {DbHandler.TOOL_ID, DbHandler.TOOL_NAME, DbHandler.TOOL_ICON},
                new int[] { R.id.toolId, R.id.name, R.id.icon}, 0){
            @Override
            public void setViewImage(ImageView v, String value) {
                //Setting the src attribute of the ImageView
                v.setImageResource(getResources().getIdentifier(value, "drawable", getApplicationContext().getPackageName()));
            }
        };
        listView.setAdapter(adapter);
        //This will call the onCreateLoader method below
        getSupportLoaderManager().initLoader(0, null, this);

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
        /*Inserting the task*/
        int taskId;
        String title = ((EditText)findViewById(R.id.title)).getText().toString();
//        make sure it is not empty
//        int points = Integer.parseInt(((EditText)findViewById(R.id.points)).getText().toString());
        int assigneeId =  Integer.parseInt(((TextView)(((Spinner)findViewById(R.id.users)).getSelectedView().findViewById(R.id.userId))).getText().toString());
        String note = ((EditText)findViewById(R.id.note)).getText().toString();
        String description = ((EditText)findViewById(R.id.description)).getText().toString();
        String date = CALENDAR.get(Calendar.DAY_OF_MONTH) + "/" + CALENDAR.get(Calendar.MONTH) + "/" + CALENDAR.get(Calendar.YEAR);
        Task task = new Task(assigneeId, assigneeId, title, description, note, date);
        taskId = DbHandler.getInstance(this).insertTask(task);

        /*Linking the task to the tools needed for it*/
        ListView list = findViewById(R.id.tools);
        for(int i = 0; i < list.getCount(); i++) {
            View v = list.getChildAt(i);
            CheckBox checkBox = v.findViewById(R.id.checkbox);
            if(checkBox.isChecked()) {
                Usage usage;
                int toolId = Integer.parseInt(((TextView)(v.findViewById(R.id.toolId))).getText().toString());
                usage = new Usage(toolId, taskId);
                DbHandler.getInstance(this).insertUsage(usage);
            }
        }

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

            TextView textView = getActivity().findViewById(R.id.date);
            textView.setText(getString(R.string.deadline,
                    CALENDAR.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US),
                    CALENDAR.get(Calendar.DAY_OF_MONTH),
                    CALENDAR.get(Calendar.YEAR)));
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new MyCursorLoader(this, MyCursorLoader.ALL_TOOLS);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }
}
