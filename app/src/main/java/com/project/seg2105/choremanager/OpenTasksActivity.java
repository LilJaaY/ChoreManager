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

public class OpenTasksActivity extends AppCompatActivity {

    private SimpleCursorAdapter adapter;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_tasks);

        //setting up back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Open tasks");

        //Retrieving ListView
        ListView tasks = findViewById(R.id.tasksList);

        String sql = "SELECT * FROM " + DbHandler.TASK_TABLE_NAME + " WHERE "
                + DbHandler.TASK_STATUS + "='" + Task.Status.UNASSIGNED.toString() + "';";
        cursor = DbHandler.getInstance(this).getReadableDatabase().rawQuery(sql, null);

        adapter = new SimpleCursorAdapter(this,
                R.layout.task_row, cursor,
                new String[] {DbHandler.TASK_ID, DbHandler.TASK_TITLE, DbHandler.TASK_NOTE},
                new int[] {R.id.taskId, R.id.title, R.id.note}, 0);

        tasks.setAdapter(adapter);

        tasks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView id = view.findViewById(R.id.taskId);
                int taskId = Integer.parseInt(id.getText().toString());
                Intent intent = new Intent(OpenTasksActivity.this, ViewTask.class);
                intent.putExtra("TaskID", taskId);
                startActivityForResult(intent, 1);
            }
        });
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
        String sql = "SELECT * FROM " + DbHandler.TASK_TABLE_NAME + " WHERE "
                + DbHandler.TASK_STATUS + "='" + Task.Status.UNASSIGNED.toString() + "';";
        cursor = DbHandler.getInstance(this).getReadableDatabase().rawQuery(sql, null);
        adapter.changeCursor(cursor);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        updateUI();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cursor.close();
    }
}
