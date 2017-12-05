package com.project.seg2105.choremanager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;

import java.util.List;

public class Calendar extends AppCompatActivity {

    WeekView view;
    Task[] tasks;
    List<WeekViewEvent> events;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        view = findViewById(R.id.calendar);

        //Todo (Jalil) query DB for all tasks and store them in "tasks" array

        for (int i = 0; i < tasks.length; i++){
            Task task = tasks[i];
            String deadline = task.getDeadline()+"/";

            int counter = 0;

            int year = 0;
            int month = 0;
            int day = 0;

            WeekViewEvent event;

            for (int j = 0; j < deadline.length(); i++){
                if (deadline.charAt(j) == '/') {
                    if (year == 0) {
                        year = Integer.parseInt(deadline.substring(counter,j));
                        counter = j+1;
                    } else if (month == 0) {
                        month = Integer.parseInt(deadline.substring(counter,j));
                        counter = j+1;
                    } else if (day == 0) {
                        day = Integer.parseInt(deadline.substring(counter,j));
                        counter = j+1;
                    }
                }
            }

            event = new WeekViewEvent(task.getId(),task.getTitle(), year, month, day, 19,0,year,month,day,20,0);
            events.add(event);

        }

        MonthLoader.MonthChangeListener listener = new MonthLoader.MonthChangeListener() {
            @Override
            public List<WeekViewEvent> onMonthChange(int newYear, int newMonth) {
                return events;
            }
        };

        view.setMonthChangeListener(listener);
    }
}
