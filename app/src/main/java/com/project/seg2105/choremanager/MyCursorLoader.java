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
    public static final int ALL_TOOLS = 2;
    public static final int ALL_TASKS_FOR_PARTICULAR_USER = 3;
    private User user;
    private int purpose;
    private WeakReference<Context> context;

    public MyCursorLoader(Context context, int purpose) {
        super(context);
        this.context = new WeakReference<>(context);
        this.purpose = purpose;
    }

    public MyCursorLoader(Context context, int purpose, User user) {
        super(context);
        this.context = new WeakReference<>(context);
        this.purpose = purpose;
        this.user = user;
    }

    @Override
    public Cursor loadInBackground() {
        Cursor cursor;
        String sql;
        switch (purpose) {
            case ALL_TASKS_FOR_PARTICULAR_USER:
                sql = "SELECT " + DbHandler.TASK_TABLE_NAME + "." + DbHandler.TASK_ID + ", "
                        + DbHandler.TASK_TITLE + ", " +DbHandler.TASK_NOTE + ", " + DbHandler.USER_AVATAR
                        + " FROM " + DbHandler.TASK_TABLE_NAME + ", " + DbHandler.USER_TABLE_NAME
                        + " WHERE " + DbHandler.TASK_TABLE_NAME + "." + DbHandler.TASK_ASSIGNEE_ID + "=" + DbHandler.USER_TABLE_NAME + "." + DbHandler.USER_ID
                        + " AND " + DbHandler.TASK_ASSIGNEE_ID + "=" + user.getId() + ";";
                cursor = DbHandler.getInstance(context.get()).getWritableDatabase().rawQuery(sql, null);
                cursor.getCount();
                return cursor;
            case ALL_TASKS:
                sql = "SELECT " + DbHandler.TASK_TABLE_NAME + "." + DbHandler.TASK_ID + ", "
                        + DbHandler.TASK_TITLE + ", " +DbHandler.TASK_NOTE + ", " + DbHandler.USER_AVATAR
                        + " FROM " + DbHandler.TASK_TABLE_NAME + ", " + DbHandler.USER_TABLE_NAME
                        + " WHERE " + DbHandler.TASK_TABLE_NAME + "." + DbHandler.TASK_ASSIGNEE_ID + "=" + DbHandler.USER_TABLE_NAME + "." + DbHandler.USER_ID + ";";
                cursor = DbHandler.getInstance(context.get()).getWritableDatabase().rawQuery(sql, null);
                cursor.getCount();
                return cursor;
            case ALL_USERS:
                sql = "SELECT COUNT(" + DbHandler.TASK_TABLE_NAME + "." + DbHandler.TASK_ASSIGNEE_ID + ") as TasksCount, "
                        + DbHandler.USER_TABLE_NAME + "." + DbHandler.USER_ID + ", " + DbHandler.USER_NAME
                        + " FROM " + DbHandler.USER_TABLE_NAME + " LEFT JOIN " + DbHandler.TASK_TABLE_NAME
                        + " ON " + DbHandler.USER_TABLE_NAME + "." + DbHandler.USER_ID + " = " + DbHandler.TASK_ASSIGNEE_ID
                        + " GROUP BY " + DbHandler.TASK_TABLE_NAME + "." + DbHandler.TASK_ASSIGNEE_ID + ";";
                cursor = DbHandler.getInstance(context.get()).getWritableDatabase().rawQuery(sql, null);
                cursor.getCount();
                return cursor;
            case ALL_TOOLS:
                cursor = DbHandler.getInstance(context.get()).getWritableDatabase().query(
                        DbHandler.TOOL_TABLE_NAME,
                        new String[] {DbHandler.TOOL_ID, DbHandler.TOOL_NAME, DbHandler.TOOL_ICON},
                        null, null, null, null, null);
                cursor.getCount();
                return cursor;
        }
        return null;
    }

}
