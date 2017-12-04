package com.project.seg2105.choremanager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class EditTask extends CreateTask {
    private Task task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Retrieving the task's ID from the intent
        int taskId = getIntent().getIntExtra("TaskID", 0);

        //Retrieving the task from the db
        task = new Task();
        task.setId(taskId);
        task = DbHandler.getInstance(this).findTask(task);
    }
}
