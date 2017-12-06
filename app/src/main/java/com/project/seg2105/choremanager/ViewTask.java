package com.project.seg2105.choremanager;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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
        ((TextView)(findViewById(R.id.status))).setText(task.getStatus());

        if(assignee != null) { //This warning is wrong. the dbhandler returns null when no user is found.
            avatar.setImageResource(getResources().getIdentifier(assignee.getAvatar(), "drawable", this.getPackageName()));
            ((TextView)(findViewById(R.id.name))).setText(assignee.getName());
        } else {
            avatar.setImageResource(getResources().getIdentifier("question_mark_button", "drawable", this.getPackageName()));
            ((TextView)(findViewById(R.id.name))).setText("Nobody");
        }


        ((TextView)(findViewById(R.id.creatorName))).setText(creator.getName());


        //Setting the deadline
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date = null;
        try {
            date = formatter.parse(task.getDeadline());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
        TextView deadline = findViewById(R.id.deadline);
        if(Calendar.getInstance().get(Calendar.YEAR) == calendar.get(Calendar.YEAR)
                && Calendar.getInstance().get(Calendar.MONTH) == calendar.get(Calendar.MONTH)
                && Calendar.getInstance().get(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH)) {
            deadline.setText("Today");
        } else if(Calendar.getInstance().get(Calendar.YEAR) == calendar.get(Calendar.YEAR)
                && Calendar.getInstance().get(Calendar.MONTH) == calendar.get(Calendar.MONTH)
                && Calendar.getInstance().get(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH)-1) {
            deadline.setText("Tomorrow");
        } else {
            deadline.setText(task.getDeadline());
        }


        ((TextView)(findViewById(R.id.description))).setText(task.getDescription());
        ((TextView)(findViewById(R.id.note))).setText(task.getNote());
        ((TextView)(findViewById(R.id.reward))).setText(task.getReward()+"");

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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if(!getIntent().getBooleanExtra("Backlog", false)) {
            getMenuInflater().inflate(R.menu.view_task_menu, menu);
            return true;
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_edit:
                Intent intent = new Intent(this, EditTask.class);
                intent.putExtra("TaskID", task.getId());
                startActivityForResult(intent, 1);
                break;
            case R.id.action_delete:
                String sql = "DELETE FROM " + DbHandler.TASK_TABLE_NAME + " WHERE "
                        + DbHandler.TASK_ID + "=" + task.getId() + ";";
                DbHandler.getInstance(this).getWritableDatabase().execSQL(sql);
                finish();
                break;
            default: break;
        }
        return true;
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
    protected void onDestroy() {
        super.onDestroy();
        //Close the cursor
        cursor.close();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK) {
            recreate();
            Toast.makeText(this, "Task updated!", Toast.LENGTH_LONG).show();
        }
    }
}
