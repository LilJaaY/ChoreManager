package com.project.seg2105.choremanager;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.github.tibolte.agendacalendarview.AgendaCalendarView;
import com.github.tibolte.agendacalendarview.CalendarPickerController;
import com.github.tibolte.agendacalendarview.models.BaseCalendarEvent;
import com.github.tibolte.agendacalendarview.models.CalendarEvent;
import com.github.tibolte.agendacalendarview.models.DayItem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ScheduleView extends AppCompatActivity implements CalendarPickerController{


    List<Task> tasks;
    List<CalendarEvent> events;
    AgendaCalendarView view;
    Task t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_view);

        //setting up back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Schedule");

        view = findViewById(R.id.agendaCalendar);
        events = new ArrayList<>();
        tasks = new ArrayList<>();

        String sql = "SELECT * FROM " + DbHandler.TASK_TABLE_NAME + " WHERE " + DbHandler.TASK_STATUS
                + "='" + Task.Status.UNASSIGNED.toString() + "' OR " + DbHandler.TASK_STATUS
                + "='" + Task.Status.ASSIGNED.toString() + "';";
        Cursor cursor = DbHandler.getInstance(this).getWritableDatabase().rawQuery(sql, null);
        for(int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToNext();
            int creatorId = cursor.getInt(cursor.getColumnIndex(DbHandler.TASK_CREATOR_ID));
            int assigneeId = cursor.getInt(cursor.getColumnIndex(DbHandler.TASK_ASSIGNEE_ID));
            String title = cursor.getString(cursor.getColumnIndex(DbHandler.TASK_TITLE));
            String description = cursor.getString(cursor.getColumnIndex(DbHandler.TASK_DESC));
            String note = cursor.getString(cursor.getColumnIndex(DbHandler.TASK_STATUS));
            String status = cursor.getString(cursor.getColumnIndex(DbHandler.TASK_STATUS));
            String deadline = cursor.getString(cursor.getColumnIndex(DbHandler.TASK_DEADLINE));
            int reward = cursor.getInt(cursor.getColumnIndex(DbHandler.TASK_REWARD));
            t = new Task(creatorId, assigneeId, title, description, note, status, deadline, reward);
            t.setId(cursor.getInt(cursor.getColumnIndex(DbHandler.TASK_ID)));
            tasks.add(t);
        }
        cursor.close();

        BaseCalendarEvent event;

        for (int i = 0; i < tasks.size(); i++){
            Task task = tasks.get(i);
            String deadline = task.getDeadline()+"/";

            int counter = 0;

            int year = 0;
            int month = 0;
            int day = 0;

            for (int j = 0; j < deadline.length(); j++){
                if (deadline.charAt(j) == '/') {
                    if (day == 0) {
                        day = Integer.parseInt(deadline.substring(counter,j));
                        counter = j+1;
                    } else if (month == 0) {
                        month = Integer.parseInt(deadline.substring(counter,j));
                        counter = j+1;
                    } else if (year == 0) {
                        year = Integer.parseInt(deadline.substring(counter,j));
                        counter = j+1;
                    }
                }
            }

            Calendar time = Calendar.getInstance();
            time.set(Calendar.YEAR, year);
            time.set(Calendar.MONTH, month-1); //because months are zero based.
            time.set(Calendar.DAY_OF_MONTH, day);

            event = new BaseCalendarEvent(task.getTitle(), task.getDescription(), "Home", Color.CYAN, time, time,true);
            events.add(event);
        }

        Calendar lowerBound = Calendar.getInstance();
        lowerBound.set(Calendar.YEAR,2017);
        lowerBound.set(Calendar.MONTH,11);
        lowerBound.set(Calendar.DAY_OF_MONTH, 1);

        Calendar upperBound = Calendar.getInstance();
        upperBound.set(Calendar.YEAR,2018);
        upperBound.set(Calendar.MONTH, 4);
        upperBound.set(Calendar.DAY_OF_MONTH, 30);

        view.init(events,lowerBound,upperBound, Locale.getDefault(),this);
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
    public void onDaySelected(DayItem dayItem) {

    }

    @Override
    public void onEventSelected(CalendarEvent event) {

    }

    @Override
    public void onScrollToDate(Calendar calendar) {

    }
}
