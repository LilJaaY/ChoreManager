package com.project.seg2105.choremanager;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Calendar;


public class EditTask extends CreateTask {
    private Task task;
    private User assignee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //App bar title
        getSupportActionBar().setTitle("Update the task");

        //Retrieving the task's ID from the intent
        int taskId = getIntent().getIntExtra("TaskID", 0);

        //Retrieving the task from the db
        task = new Task();
        task.setId(taskId);
        task = DbHandler.getInstance(this).findTask(task);

        //Retrieving the assignee's id
        assignee = new User();
        assignee.setId(task.getAssignee_id());
        assignee = DbHandler.getInstance(this).findUser(assignee);

        //Setting the task's fields
        ((TextView) findViewById(R.id.title)).setText(task.getTitle());
        ((TextView) findViewById(R.id.description)).setText(task.getDescription());
        ((TextView) findViewById(R.id.note)).setText(task.getNote());
        ((TextView) findViewById(R.id.date)).setText(task.getDeadline());
        final TextView reward = findViewById(R.id.reward);
        reward.setText(task.getReward()+"");
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

        //Setting the states of the checkboxes
        String sql = "SELECT * FROM " + DbHandler.USAGE_TABLE_NAME + " WHERE "
                + DbHandler.USAGE_TASK_ID + "=" + task.getId() + ";";
        Cursor c = DbHandler.getInstance(this).getWritableDatabase().rawQuery(sql,  null);
        ListView list = findViewById(R.id.tools);
        for(int i = 0; i < c.getCount(); i++) {
            c.moveToNext();
            int toolId = c.getInt(c.getColumnIndex(DbHandler.USAGE_TOOL_ID));
            checkBoxStates.set(toolId-1, true);
        }
        c.close();

        //Changing the text of the submit button
        ((Button)findViewById(R.id.submit)).setText("Update task");
    }

    @Override
    public void onSubmitClick(View view) {
        /*Updating the task*/
        String title = ((EditText)findViewById(R.id.title)).getText().toString();
//        make sure it is not empty
        int points = Integer.parseInt(((EditText)findViewById(R.id.reward)).getText().toString());
        int assigneeId =  Integer.parseInt(((TextView)(((Spinner)findViewById(R.id.users)).getSelectedView().findViewById(R.id.userId))).getText().toString());
        String note = ((EditText)findViewById(R.id.note)).getText().toString();
        String description = ((EditText)findViewById(R.id.description)).getText().toString();
        String date = CALENDAR.get(Calendar.DAY_OF_MONTH) + "/" + CALENDAR.get(Calendar.MONTH) + "/" + CALENDAR.get(Calendar.YEAR);
        if(assigneeId > 0) {
            //TODO: change later
            task.setCreator_id(1);
            task.setAssignee_id(assigneeId);
            task.setTitle(title);
            task.setDescription(description);
            task.setNote(note);
            task.setStatus(Task.Status.ASSIGNED.toString());
            task.setDeadline(date);
            task.setReward(points);
        } else {
            task.setCreator_id(1);
            task.setAssignee_id(assigneeId);
            task.setTitle(title);
            task.setDescription(description);
            task.setNote(note);
            task.setStatus(Task.Status.UNASSIGNED.toString());
            task.setDeadline(date);
            task.setReward(points);
        }
        DbHandler.getInstance(this).updateTask(task);

        /*Deleting all the usages associated with this task*/
        String sql = "DELETE FROM " + DbHandler.USAGE_TABLE_NAME + " WHERE "
                + DbHandler.USAGE_TASK_ID + "=" + task.getId() + ";";
        DbHandler.getInstance(this).getWritableDatabase().execSQL(sql);

        /*Linking the task to the tools needed for it*/
        ListView list = findViewById(R.id.tools);
        for(int i = 0; i < list.getCount(); i++) {
            View v = list.getAdapter().getView(i, null, list);
            CheckBox checkBox = v.findViewById(R.id.checkbox);
            if(checkBox.isChecked()) {
                Usage usage;
                int toolId = Integer.parseInt(((TextView)(v.findViewById(R.id.toolId))).getText().toString());
                usage = new Usage(toolId, task.getId());
                DbHandler.getInstance(this).insertUsage(usage);
            }
        }

        //go back
        finish();
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        super.onLoadFinished(loader, data);
        if(loader.getId() == 1 && assignee != null) {
            spinner.setSelection(assignee.getId());
        }
    }
}
