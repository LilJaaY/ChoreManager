package com.project.seg2105.choremanager;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.CursorLoader;

import java.lang.ref.WeakReference;

/**
 * Created by jalil on 27/11/17.
 */

public class MyCursorLoader extends CursorLoader {
    public static final int ALL_TASKS = 0;
    public static final int ALL_USERS = 1;
    private int purpose;
    private WeakReference<Context> context;

    public MyCursorLoader(Context context, int purpose) {
        super(context);
        this.context = new WeakReference<>(context);
        this.purpose = purpose;
    }

    @Override
    public Cursor loadInBackground() {
        Cursor cursor;
        switch (purpose) {
            case ALL_TASKS:
                cursor = DbHandler.getInstance(context.get()).getWritableDatabase().query(
                        DbHandler.TASK_TABLE_NAME,
                        new String[] {DbHandler.TASK_ID, DbHandler.TASK_TITLE, DbHandler.TASK_NOTE},
                        null, null, null, null, null);
                cursor.getCount();
                return cursor;
            case ALL_USERS:
                String sql = "SELECT COUNT(" + DbHandler.TASK_TABLE_NAME + "." + DbHandler.TASK_ASSIGNEE_ID + ") as TasksCount, "
                        + DbHandler.USER_TABLE_NAME + "." + DbHandler.USER_ID + ", " + DbHandler.USER_NAME
                        + " FROM " + DbHandler.USER_TABLE_NAME + " LEFT JOIN " + DbHandler.TASK_TABLE_NAME
                        + " ON " + DbHandler.USER_TABLE_NAME + "." + DbHandler.USER_ID + " = " + DbHandler.TASK_ASSIGNEE_ID
                        + " GROUP BY " + DbHandler.TASK_TABLE_NAME + "." + DbHandler.TASK_ASSIGNEE_ID + ";";
                cursor = DbHandler.getInstance(context.get()).getWritableDatabase().rawQuery(sql, null);
                cursor.getCount();
                return cursor;
        }
        return null;
    }

}
