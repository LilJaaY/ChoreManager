package com.project.seg2105.choremanager;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class MyTasksActivity extends AppCompatActivity {
    private SimpleCursorAdapter adapter;
    private User currentUser = new User();
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_tasks);

        //Setting currentUser's id
        currentUser.setId(getIntent().getIntExtra("UserId", 1));
        currentUser = DbHandler.getInstance(this).findUser(currentUser);

        //setting up back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("My tasks");

        //Retrieving ListView
        ListView tasks = findViewById(R.id.tasksList);

        String sql = "SELECT " + DbHandler.TASK_TABLE_NAME + "." + DbHandler.TASK_ID + ", "
                + DbHandler.TASK_TITLE + ", " +DbHandler.TASK_NOTE + ", "
                + DbHandler.USER_AVATAR + ", " + DbHandler.TASK_REWARD + ", " + DbHandler.TASK_STATUS
                + " FROM " + DbHandler.TASK_TABLE_NAME + ", " + DbHandler.USER_TABLE_NAME
                + " WHERE " + DbHandler.TASK_TABLE_NAME + "." + DbHandler.TASK_ASSIGNEE_ID + "=" + DbHandler.USER_TABLE_NAME + "." + DbHandler.USER_ID
                + " AND " + DbHandler.TASK_ASSIGNEE_ID + "=" + currentUser.getId() + " AND " + DbHandler.TASK_STATUS
                + "='" + Task.Status.ASSIGNED.toString() + "';";
        cursor = DbHandler.getInstance(this).getReadableDatabase().rawQuery(sql, null);

        adapter = new SimpleCursorAdapter(this,
                R.layout.task_row_2, cursor,
                new String[] {DbHandler.TASK_ID, DbHandler.TASK_TITLE, DbHandler.TASK_NOTE, DbHandler.USER_AVATAR, DbHandler.TASK_REWARD},
                new int[] {R.id.taskId, R.id.title, R.id.note, R.id.avatar, R.id.reward}, 0){
            @Override
            public void setViewImage(ImageView v, String value) {
                //Setting the src attribute of the ImageView
                v.setImageResource(getResources().getIdentifier(value, "drawable", getPackageName()));
            }
        };

        tasks.setAdapter(adapter);
    }

    public void onCompletedClick(View view) {
        View taskRow = (View) view.getParent();
        int taskId = Integer.parseInt(((TextView)taskRow.findViewById(R.id.taskId)).getText().toString());
        int reward = Integer.parseInt(((TextView)taskRow.findViewById(R.id.reward)).getText().toString());

        //Give the points to the user
        currentUser.setPoints(currentUser.getPoints()+reward);
        DbHandler.getInstance(this).updateUser(currentUser);

        //Update task status
        Task task = new Task();
        task.setId(taskId);
        task = DbHandler.getInstance(this).findTask(task);
        task.setStatus(Task.Status.COMPLETED.toString());
        DbHandler.getInstance(this).updateTask(task);

        updateUI();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return true;
    }

    private void updateUI() {
        //Requery the cursor
        String sql = "SELECT " + DbHandler.TASK_TABLE_NAME + "." + DbHandler.TASK_ID + ", "
                + DbHandler.TASK_TITLE + ", " +DbHandler.TASK_NOTE + ", "
                + DbHandler.USER_AVATAR + ", " + DbHandler.TASK_REWARD + ", " + DbHandler.TASK_STATUS
                + " FROM " + DbHandler.TASK_TABLE_NAME + ", " + DbHandler.USER_TABLE_NAME
                + " WHERE " + DbHandler.TASK_TABLE_NAME + "." + DbHandler.TASK_ASSIGNEE_ID + "=" + DbHandler.USER_TABLE_NAME + "." + DbHandler.USER_ID
                + " AND " + DbHandler.TASK_ASSIGNEE_ID + "=" + currentUser.getId() + " AND " + DbHandler.TASK_STATUS
                + "='" + Task.Status.ASSIGNED.toString() + "';";
        cursor = DbHandler.getInstance(this).getReadableDatabase().rawQuery(sql, null);
        adapter.changeCursor(cursor);
        //adapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Close the cursor
        cursor.close();
    }
}
