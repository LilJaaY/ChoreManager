package com.project.seg2105.choremanager;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.MergeCursor;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class CreateTask extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>{

    protected static final Calendar CALENDAR = Calendar.getInstance();
    protected SimpleCursorAdapter toolsAdapter;
    private SimpleCursorAdapter peopleAdapter;
    protected Spinner spinner;
    protected ArrayList<Boolean> checkBoxStates;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);

        //Retrieving user's id
        user = new User();
        user.setId(getIntent().getIntExtra("Id", 1));

        //Default reward value
        final TextView reward = findViewById(R.id.reward);
        reward.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.toString().isEmpty()) {
                    reward.setText(0+"");
                }
            }
        });

        //Intialize equipments' listview
        String sql = "SELECT * FROM " + DbHandler.TOOL_TABLE_NAME + ";";
        Cursor cursor = DbHandler.getInstance(this).getWritableDatabase().rawQuery(sql, null);
        int toolsCount = cursor.getCount();
        cursor.close();
        ListView listView = findViewById(R.id.tools);

        //Initializing checkboxes' states
        checkBoxStates = new ArrayList<>();
        for(int i = 0; i < toolsCount; i++) {
            checkBoxStates.add(i, false);
        }

        //Using custom adapter defined inside activity code
        toolsAdapter = new CheckBoxAdapter(this,
                R.layout.tool_row, null,
                new String[] {DbHandler.TOOL_ID, DbHandler.TOOL_NAME, DbHandler.TOOL_ICON},
                new int[] { R.id.toolId, R.id.name, R.id.icon}, 0) {

            @Override
            public void setViewImage(ImageView v, String value) {
                //Setting the src attribute of the ImageView
                v.setImageResource(getResources().getIdentifier(value, "drawable", CreateTask.this.getPackageName()));
            }
        };
        listView.setAdapter(toolsAdapter);

        //setting up back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Create a new task");

        //Initializing spinner
        spinner = findViewById(R.id.users);
        peopleAdapter = new SimpleCursorAdapter(this,
                R.layout.people_row_2, null,
                new String[] {DbHandler.USER_ID, DbHandler.USER_AVATAR, DbHandler.USER_NAME},
                new int[] { R.id.userId, R.id.avatar, R.id.name}, 0){
            @Override
            public void setViewImage(ImageView v, String value) {
                //Setting the src attribute of the ImageView
                v.setImageResource(getResources().getIdentifier(value, "drawable", getApplicationContext().getPackageName()));
            }
        };

        spinner.setAdapter(peopleAdapter);

        //This will call the onCreateLoader method below
        getSupportLoaderManager().initLoader(0, null, this);
        getSupportLoaderManager().initLoader(1, null, this);

    }

    protected class CheckBoxAdapter extends SimpleCursorAdapter {

        CheckBoxAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
            super(context, layout, c, from, to, flags);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view = super.getView(position, convertView, parent);
            CheckBox checkBox = view.findViewById(R.id.checkbox);
            checkBox.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if(((CheckBox)v).isChecked()){
                        checkBoxStates.set(position, true);
                    }else{
                        checkBoxStates.set(position, false);
                    }
                }
            });
            checkBox.setChecked(checkBoxStates.get(position));
            return view;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return true;
    }

    public void onSubmitClick(View view) {
        /*Inserting the task*/
        Task task;
        int taskId;
        String title = ((EditText)findViewById(R.id.title)).getText().toString();
//        make sure it is not empty
        int points = Integer.parseInt(((EditText)findViewById(R.id.reward)).getText().toString());
        int assigneeId =  Integer.parseInt(((TextView)(((Spinner)findViewById(R.id.users)).getSelectedView().findViewById(R.id.userId))).getText().toString());
        String note = ((EditText)findViewById(R.id.note)).getText().toString();
        String description = ((EditText)findViewById(R.id.description)).getText().toString();
        int month = CALENDAR.get(Calendar.MONTH)+1; //because months are zero based
        String date = CALENDAR.get(Calendar.DAY_OF_MONTH) + "/" + month + "/" + CALENDAR.get(Calendar.YEAR);
        if(assigneeId > 0) {
            task = new Task(user.getId(), assigneeId, title, description, note, Task.Status.ASSIGNED.toString(), date, points);
        } else {
            task = new Task(user.getId(), title, description, note, Task.Status.UNASSIGNED.toString(), date, points);
        }
        taskId = DbHandler.getInstance(this).insertTask(task);

        /*Linking the task to the tools needed for it*/
        ListView list = findViewById(R.id.tools);
        for(int i = 0; i < list.getCount(); i++) {
            View v = list.getAdapter().getView(i, null, list);
            CheckBox checkBox = v.findViewById(R.id.checkbox);
            if(checkBox.isChecked()) {
                Usage usage;
                int toolId = Integer.parseInt(((TextView)(v.findViewById(R.id.toolId))).getText().toString());
                usage = new Usage(toolId, taskId);
                DbHandler.getInstance(this).insertUsage(usage);
            }
        }

        setResult(RESULT_OK);
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
            Calendar today = Calendar.getInstance();
            int dateSet = year*10000 + month*100 + day;
            int currentDate = today.get(Calendar.YEAR)*10000 + today.get(Calendar.MONTH)+100
                    + today.get(Calendar.DAY_OF_MONTH);
            if(currentDate <= dateSet) {
                CALENDAR.set(Calendar.YEAR, year);
                CALENDAR.set(Calendar.MONTH, month);
                CALENDAR.set(Calendar.DAY_OF_MONTH, day);

                TextView textView = getActivity().findViewById(R.id.date);
                textView.setText(getString(R.string.deadline,
                        CALENDAR.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US),
                        CALENDAR.get(Calendar.DAY_OF_MONTH),
                        CALENDAR.get(Calendar.YEAR)));

            } else {
                Toast.makeText(getActivity(), "New task added!", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Loader<Cursor> loader;
        if(id == 0) {
            loader = new MyCursorLoader(this, MyCursorLoader.ALL_TOOLS);
        } else {
            loader = new MyCursorLoader(this, MyCursorLoader.ALL_USERS);
        }
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(loader.getId() == 0) {
            toolsAdapter.swapCursor(data);
        } else {
            MatrixCursor extra = new MatrixCursor(new String[] {"TasksCount",
                    DbHandler.USER_ID,
                    DbHandler.USER_NAME,
                    DbHandler.USER_PASSWORD,
                    DbHandler.USER_AVATAR});
            extra.addRow(new String[] {"0", "-1", "Nobody", "pass", "question_mark_button"});
            Cursor[] cursors = { extra, data };
            Cursor extendedCursor = new MergeCursor(cursors);
            peopleAdapter.swapCursor(extendedCursor);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if(loader.getId() == 0) {
            toolsAdapter.swapCursor(null);
        } else {
            peopleAdapter.swapCursor(null);
        }
    }
}
