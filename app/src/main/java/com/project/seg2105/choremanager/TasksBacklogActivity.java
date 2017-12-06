package com.project.seg2105.choremanager;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class TasksBacklogActivity extends AppCompatActivity {
    private Cursor cursor;
    private SimpleCursorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks_backlog);

        //setting up back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Backlog");

        //Retrieving ListView
        ListView tasks = findViewById(R.id.tasksList);

        String sql = "SELECT " + DbHandler.TASK_TABLE_NAME + "." + DbHandler.TASK_ID + " AS _id" + ", " + DbHandler.TASK_ASSIGNEE_ID
                + ", " + DbHandler.TASK_TITLE + ", " + DbHandler.TASK_NOTE + ", " + DbHandler.USER_AVATAR
                + ", " + DbHandler.TASK_DEADLINE + ", " + DbHandler.TASK_STATUS + ", "
                + DbHandler.TASK_DESC + ", " + DbHandler.TASK_CREATOR_ID + ", " + DbHandler.TASK_REWARD
                + " FROM (SELECT * FROM " + DbHandler.TASK_TABLE_NAME + " WHERE " + DbHandler.TASK_STATUS
                + " IN ('" + Task.Status.COMPLETED.toString() + "','" + Task.Status.UNCOMPLETED.toString() + "')) AS " +  DbHandler.TASK_TABLE_NAME
                + " LEFT JOIN " + DbHandler.USER_TABLE_NAME
                + " ON " + DbHandler.TASK_TABLE_NAME + "." + DbHandler.TASK_ASSIGNEE_ID + "="
                + DbHandler.USER_TABLE_NAME + "." + DbHandler.USER_ID + ";";
        cursor = DbHandler.getInstance(this).getReadableDatabase().rawQuery(sql, null);

        adapter = new SimpleCursorAdapter(this,
                R.layout.task_row, cursor,
                new String[] {DbHandler.TASK_ID, DbHandler.TASK_TITLE, DbHandler.TASK_NOTE, DbHandler.USER_AVATAR, DbHandler.TASK_REWARD},
                new int[] {R.id.taskId, R.id.title, R.id.note, R.id.avatar, R.id.reward}, 0){
            @Override
            public void setViewImage(ImageView v, String value) {
                //Setting the src attribute of the ImageView
                v.setImageResource(getResources().getIdentifier(value, "drawable", getPackageName()));
            }
        };

        tasks.setAdapter(adapter);

        tasks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView id = view.findViewById(R.id.taskId);
                int taskId = Integer.parseInt(id.getText().toString());
                Intent intent = new Intent(TasksBacklogActivity.this, ViewTask.class);
                intent.putExtra("TaskID", taskId);
                intent.putExtra("Backlog", true);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                break;
            default: break;
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Close the cursor
        cursor.close();
    }
}
