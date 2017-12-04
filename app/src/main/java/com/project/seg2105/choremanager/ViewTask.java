package com.project.seg2105.choremanager;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class ViewTask extends AppCompatActivity {
    private Cursor cursor;
    private Task task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_task);

        //setting up back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Retrieving the task's ID from the intent
        int taskId = getIntent().getIntExtra("TaskID", 0);

        //Retrieving the task from the db
        task = new Task();
        task.setId(taskId);
        task = DbHandler.getInstance(this).findTask(task);

        //Retrieving the users involved
        User creator = new User();
        creator.setId(task.getCreator_id());
        creator = DbHandler.getInstance(this).findUser(creator);

        User assignee = new User();
        assignee.setId(task.getAssignee_id());
        assignee = DbHandler.getInstance(this).findUser(assignee);

        //Filling the activity's fields
        getSupportActionBar().setTitle(task.getTitle());

        ImageView avatar = findViewById(R.id.avatar);
        if(assignee != null) {
            avatar.setImageResource(getResources().getIdentifier(assignee.getAvatar(), "drawable", getApplicationContext().getPackageName()));
            ((TextView)(findViewById(R.id.name))).setText(assignee.getName());
            ((TextView)(findViewById(R.id.status))).setText(Task.Status.ASSIGNED.toString());
        } else {
            avatar.setImageResource(getResources().getIdentifier("question_mark_button", "drawable", getApplicationContext().getPackageName()));
            ((TextView)(findViewById(R.id.name))).setText("Nobody");
            ((TextView)(findViewById(R.id.status))).setText(Task.Status.UNASSIGNED.toString());
        }


        ((TextView)(findViewById(R.id.creatorName))).setText(creator.getName());


        ((TextView)(findViewById(R.id.deadline))).setText(task.getDeadline());
        ((TextView)(findViewById(R.id.description))).setText(task.getDescription());
        ((TextView)(findViewById(R.id.note))).setText(task.getNote());

        //Filling the GridLayout
        String sql = "SELECT * FROM " + DbHandler.USAGE_TABLE_NAME + ", " + DbHandler.TOOL_TABLE_NAME
                + " WHERE " + DbHandler.USAGE_TASK_ID + "=" + task.getId() + " AND "
                + DbHandler.USAGE_TOOL_ID + "=" + DbHandler.TOOL_TABLE_NAME + "." + DbHandler.TOOL_ID + ";";
        cursor = DbHandler.getInstance(this).getReadableDatabase().rawQuery(sql, null);
        SimpleCursorAdapter tools = new SimpleCursorAdapter(this,
                R.layout.tool_row_2, cursor,
                new String[] {DbHandler.TOOL_ID, DbHandler.TOOL_NAME, DbHandler.TOOL_ICON},
                new int[] {R.id.toolId, R.id.name, R.id.icon}, 0){
            @Override
            public void setViewImage(ImageView v, String value) {
                //Setting the src attribute of the ImageView
                v.setImageResource(getResources().getIdentifier(value, "drawable", ViewTask.this.getPackageName()));
            }
        };
        GridView toolsGrid = findViewById(R.id.tools);
        toolsGrid.setAdapter(tools);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Requery the cursor
        String sql = "SELECT * FROM " + DbHandler.USAGE_TABLE_NAME + ", " + DbHandler.TOOL_TABLE_NAME
                + " WHERE " + DbHandler.USAGE_TASK_ID + "=" + task.getId() + " AND "
                + DbHandler.USAGE_TOOL_ID + "=" + DbHandler.TOOL_TABLE_NAME + "." + DbHandler.TOOL_ID + ";";
        cursor = DbHandler.getInstance(this).getReadableDatabase().rawQuery(sql, null);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Close the cursor
        cursor.close();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return true;
    }
}
