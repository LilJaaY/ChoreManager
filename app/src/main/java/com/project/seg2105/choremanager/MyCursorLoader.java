package com.project.seg2105.choremanager;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.CursorLoader;

import java.lang.ref.WeakReference;

/**
 * Created by jalil on 27/11/17.
 */

public class MyCursorLoader extends CursorLoader {
    public static final int ALL_TASKS_AVAILABLE = 0;
    public static final int ALL_USERS = 1;
    public static final int ALL_TOOLS = 2;
    public static final int ALL_TASKS_FOR_PARTICULAR_CREATOR = 3;
    public static final int ALL_TASKS_FOR_PARTICULAR_ASSIGNEE = 4;
    public static final int ALL_TASKS_BACKLOG = 5;
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
            case ALL_TASKS_FOR_PARTICULAR_CREATOR:
                sql = "SELECT " + DbHandler.TASK_TABLE_NAME + "." + DbHandler.TASK_ID + ", "
                        + DbHandler.TASK_TITLE + ", " +DbHandler.TASK_NOTE + ", " + DbHandler.USER_AVATAR
                        + " FROM " + DbHandler.TASK_TABLE_NAME + ", " + DbHandler.USER_TABLE_NAME
                        + " WHERE " + DbHandler.TASK_TABLE_NAME + "." + DbHandler.TASK_CREATOR_ID + "=" + DbHandler.USER_TABLE_NAME + "." + DbHandler.USER_ID
                        + " AND " + DbHandler.TASK_CREATOR_ID + "=" + user.getId() + ";";
                cursor = DbHandler.getInstance(context.get()).getWritableDatabase().rawQuery(sql, null);
                cursor.getCount();
                return cursor;
            case ALL_TASKS_FOR_PARTICULAR_ASSIGNEE:
                sql = "SELECT " + DbHandler.TASK_TABLE_NAME + "." + DbHandler.TASK_ID + " AS _id, "
                        + DbHandler.TASK_TITLE + ", " +DbHandler.TASK_NOTE + ", " + DbHandler.USER_AVATAR
                        + " FROM (SELECT * FROM " + DbHandler.TASK_TABLE_NAME + " WHERE " + DbHandler.TASK_STATUS
                        + " IN ('" + Task.Status.ASSIGNED.toString() + "','" + Task.Status.UNASSIGNED.toString() + "')) AS " +  DbHandler.TASK_TABLE_NAME
                        + ", " + DbHandler.USER_TABLE_NAME
                        + " WHERE " + DbHandler.TASK_TABLE_NAME + "." + DbHandler.TASK_ASSIGNEE_ID + "=" + DbHandler.USER_TABLE_NAME + "." + DbHandler.USER_ID
                        + " AND " + DbHandler.TASK_ASSIGNEE_ID + "=" + user.getId() + ";";
                cursor = DbHandler.getInstance(context.get()).getWritableDatabase().rawQuery(sql, null);
                cursor.getCount();
                return cursor;
            case ALL_TASKS_AVAILABLE:
                sql = "SELECT " + DbHandler.TASK_TABLE_NAME + "." + DbHandler.TASK_ID + " AS _id" + ", " + DbHandler.TASK_ASSIGNEE_ID
                        + ", " + DbHandler.TASK_TITLE + ", " + DbHandler.TASK_NOTE + ", " + DbHandler.USER_AVATAR
                        + ", " + DbHandler.TASK_DEADLINE + ", " + DbHandler.TASK_STATUS + ", "
                        + DbHandler.TASK_DESC + ", " + DbHandler.TASK_CREATOR_ID + ", " + DbHandler.TASK_REWARD
                        + " FROM (SELECT * FROM " + DbHandler.TASK_TABLE_NAME + " WHERE " + DbHandler.TASK_STATUS
                        + " IN ('" + Task.Status.ASSIGNED.toString() + "','" + Task.Status.UNASSIGNED.toString() + "')) AS " +  DbHandler.TASK_TABLE_NAME
                        + " LEFT JOIN " + DbHandler.USER_TABLE_NAME
                        + " ON " + DbHandler.TASK_TABLE_NAME + "." + DbHandler.TASK_ASSIGNEE_ID + "="
                        + DbHandler.USER_TABLE_NAME + "." + DbHandler.USER_ID + ";";
                cursor = DbHandler.getInstance(context.get()).getWritableDatabase().rawQuery(sql, null);
                cursor.getCount();
                return cursor;
            case ALL_TASKS_BACKLOG:
                sql = "SELECT " + DbHandler.TASK_TABLE_NAME + "." + DbHandler.TASK_ID + " AS _id" + ", " + DbHandler.TASK_ASSIGNEE_ID
                        + ", " + DbHandler.TASK_TITLE + ", " + DbHandler.TASK_NOTE + ", " + DbHandler.USER_AVATAR
                        + ", " + DbHandler.TASK_DEADLINE + ", " + DbHandler.TASK_STATUS + ", "
                        + DbHandler.TASK_DESC + ", " + DbHandler.TASK_CREATOR_ID + ", " + DbHandler.TASK_REWARD
                        + " FROM (SELECT * FROM " + DbHandler.TASK_TABLE_NAME + " WHERE " + DbHandler.TASK_STATUS
                        + " IN ('" + Task.Status.COMPLETED.toString() + "','" + Task.Status.UNCOMPLETED.toString() + "')) AS " +  DbHandler.TASK_TABLE_NAME
                        + " LEFT JOIN " + DbHandler.USER_TABLE_NAME
                        + " ON " + DbHandler.TASK_TABLE_NAME + "." + DbHandler.TASK_ASSIGNEE_ID + "="
                        + DbHandler.USER_TABLE_NAME + "." + DbHandler.USER_ID + ";";
                cursor = DbHandler.getInstance(context.get()).getWritableDatabase().rawQuery(sql, null);
                cursor.getCount();
                return cursor;
            case ALL_USERS:
                sql = "SELECT COUNT(" + DbHandler.TASK_TABLE_NAME + "." + DbHandler.TASK_ASSIGNEE_ID + ") as TasksCount, "
                        + DbHandler.USER_TABLE_NAME + "." + DbHandler.USER_ID + ", " + DbHandler.USER_NAME
                        + ", " + DbHandler.USER_PASSWORD + ", " + DbHandler.USER_AVATAR + ", " + DbHandler.USER_POINTS
                        + ", " + DbHandler.TASK_TITLE
                        + " FROM " + DbHandler.USER_TABLE_NAME + " LEFT JOIN (SELECT * FROM " + DbHandler.TASK_TABLE_NAME
                        + " WHERE " + DbHandler.TASK_STATUS
                        + " IN ('" + Task.Status.ASSIGNED.toString() + "','" + Task.Status.UNASSIGNED.toString() + "')) AS " +  DbHandler.TASK_TABLE_NAME
                        + " ON " + DbHandler.USER_TABLE_NAME + "." + DbHandler.USER_ID + " = " + DbHandler.TASK_ASSIGNEE_ID
                        + " GROUP BY " + DbHandler.USER_TABLE_NAME + "." + DbHandler.USER_ID + ";";
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
