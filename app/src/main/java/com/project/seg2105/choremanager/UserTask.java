package com.project.seg2105.choremanager;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class UserTask extends AppCompatActivity {
    private User user;
    private Cursor cursor;
    private SimpleCursorAdapter tasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_task);

        //Retrieving the user's ID from the intent
        int userId = getIntent().getIntExtra("UserID", 0);

        //Retrieving the user from the db
        user = new User();
        user.setId(userId);
        user = DbHandler.getInstance(this).findUser(user);

        //setting up back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(user.getName());

        //Setting the user's fields
        ImageView avatar = findViewById(R.id.avatar);
        avatar.setImageResource(getResources().getIdentifier(user.getAvatar(), "drawable", this.getPackageName()));
        ((TextView)(findViewById(R.id.name))).setText(user.getName());
        ((TextView)(findViewById(R.id.userId))).setText(user.getId() + "");
        ((TextView)(findViewById(R.id.points))).setText(user.getPoints() + "");

        //Displaying the user's tasks
        String sql = "SELECT " + DbHandler.TASK_TABLE_NAME + "." + DbHandler.TASK_ID + ", "
                + DbHandler.TASK_TITLE + ", " +DbHandler.TASK_NOTE + ", "
                + DbHandler.USER_AVATAR + ", " + DbHandler.TASK_REWARD + ", " + DbHandler.TASK_STATUS
                + " FROM " + DbHandler.TASK_TABLE_NAME + ", " + DbHandler.USER_TABLE_NAME
                + " WHERE " + DbHandler.TASK_TABLE_NAME + "." + DbHandler.TASK_ASSIGNEE_ID + "=" + DbHandler.USER_TABLE_NAME + "." + DbHandler.USER_ID
                + " AND " + DbHandler.TASK_ASSIGNEE_ID + "=" + user.getId() + " AND " + DbHandler.TASK_STATUS
                + "='" + Task.Status.ASSIGNED.toString() + "';";
        cursor = DbHandler.getInstance(this).getReadableDatabase().rawQuery(sql, null);
        tasks = new SimpleCursorAdapter(this,
                R.layout.task_row_2, cursor,
                new String[] {DbHandler.TASK_ID, DbHandler.TASK_TITLE, DbHandler.TASK_NOTE, DbHandler.USER_AVATAR, DbHandler.TASK_REWARD},
                new int[] {R.id.taskId, R.id.title, R.id.note, R.id.avatar, R.id.reward}, 0){
            @Override
            public void setViewImage(ImageView v, String value) {
                //Setting the src attribute of the ImageView
                v.setImageResource(getResources().getIdentifier(value, "drawable", UserTask.this.getPackageName()));
            }
        };
        ListView tasksList = findViewById(R.id.tasks);
        tasksList.setAdapter(tasks);
    }

    public void onCompletedClick(View view) {
        View taskRow = (View) view.getParent();
        int taskId = Integer.parseInt(((TextView)taskRow.findViewById(R.id.taskId)).getText().toString());
        int reward = Integer.parseInt(((TextView)taskRow.findViewById(R.id.reward)).getText().toString());

        //Give the points to the user
        user.setPoints(user.getPoints()+reward);
        DbHandler.getInstance(this).updateUser(user);

        //Update task status
        Task task = new Task();
        task.setId(taskId);
        task = DbHandler.getInstance(this).findTask(task);
        task.setStatus(Task.Status.COMPLETED.toString());
        DbHandler.getInstance(this).updateTask(task);

        //Update UI
        recreate();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Requery the cursor
        String sql = "SELECT " + DbHandler.TASK_TABLE_NAME + "." + DbHandler.TASK_ID + ", "
                + DbHandler.TASK_TITLE + ", " +DbHandler.TASK_NOTE + ", " + DbHandler.USER_AVATAR
                + " FROM " + DbHandler.TASK_TABLE_NAME + ", " + DbHandler.USER_TABLE_NAME
                + " WHERE " + DbHandler.TASK_TABLE_NAME + "." + DbHandler.TASK_ASSIGNEE_ID + "=" + DbHandler.USER_TABLE_NAME + "." + DbHandler.USER_ID
                + " AND " + DbHandler.TASK_ASSIGNEE_ID + "=" + user.getId() + ";";
        cursor = DbHandler.getInstance(this).getReadableDatabase().rawQuery(sql, null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Close the cursor
        cursor.close();
    }
}
