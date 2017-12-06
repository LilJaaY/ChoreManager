package com.project.seg2105.choremanager;

import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

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


    Task[] tasks;
    List<CalendarEvent> events;
    AgendaCalendarView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_view);

        view = findViewById(R.id.agendaCalendar);
        events = new ArrayList<>();

        String sql = "SELECT * FROM " + DbHandler.TASK_TABLE_NAME + ";";
        Cursor cursor = DbHandler.getInstance(this).getWritableDatabase().rawQuery(sql, null);
        tasks = new Task[500];
        for(int i = 0; i < 500; i++) {
            cursor.moveToNext();
            int creatorId = cursor.getInt(cursor.getColumnIndex(DbHandler.TASK_CREATOR_ID));
            int assigneeId = cursor.getInt(cursor.getColumnIndex(DbHandler.TASK_ASSIGNEE_ID));
            String title = cursor.getString(cursor.getColumnIndex(DbHandler.TASK_TITLE));
            String description = cursor.getString(cursor.getColumnIndex(DbHandler.TASK_DESC));
            String note = cursor.getString(cursor.getColumnIndex(DbHandler.TASK_STATUS));
            String status = cursor.getString(cursor.getColumnIndex(DbHandler.TASK_STATUS));
            String deadline = cursor.getString(cursor.getColumnIndex(DbHandler.TASK_DEADLINE));
            int reward = cursor.getInt(cursor.getColumnIndex(DbHandler.TASK_REWARD));
            tasks[i] = new Task(creatorId, assigneeId, title, description, note, status, deadline, reward);
            tasks[i].setId(cursor.getInt(cursor.getColumnIndex(DbHandler.TASK_ID)));
        }
        cursor.close();




        BaseCalendarEvent event;

        for (int i = 0; i < tasks.length; i++){
            Task task = tasks[i];
            String deadline = task.getDeadline()+"/";

            int counter = 0;

            int year = 0;
            int month = 0;
            int day = 0;

            for (int j = 0; j < deadline.length(); i++){
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
            time.add(Calendar.YEAR, year);
            time.add(Calendar.MONTH, month);
            time.add(Calendar.DAY_OF_MONTH, day);

            event = new BaseCalendarEvent(task.getTitle(), task.getDescription(), "Home", Color.CYAN, time, time,true);
            events.add(event);
        }

        Calendar lowerBound = Calendar.getInstance();
        lowerBound.add(Calendar.YEAR,2017);
        lowerBound.add(Calendar.MONTH,10);
        lowerBound.add(Calendar.DAY_OF_MONTH, 1);

        Calendar upperBound = Calendar.getInstance();
        upperBound.add(Calendar.YEAR,2018);
        upperBound.add(Calendar.MONTH, 4);
        upperBound.add(Calendar.DAY_OF_MONTH, 30);

        view.init(events,lowerBound,upperBound, Locale.getDefault(),this);
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
