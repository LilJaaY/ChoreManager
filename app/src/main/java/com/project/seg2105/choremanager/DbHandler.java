package com.project.seg2105.choremanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by jalilcompaore on 06/11/17.
 */

public class DbHandler extends SQLiteOpenHelper {

    public static DbHandler singleInstance;

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "ChoreManager.db";

    /*User table*/
    public static final String USER_TABLE_NAME = "User";
    public static final String USER_ID = "_id";
    public static final String USER_NAME = "Name";
    public static final String USER_PASSWORD = "Password";
    public static final String USER_AVATAR = "Avatar_path";

    /*Task table*/
    public static final String TASK_TABLE_NAME = "Task";
    public static final String TASK_ID = "_id";
    public static final String TASK_CREATOR_ID = "Creator_id";
    public static final String TASK_ASSIGNEE_ID = "Assignee_id";
    public static final String TASK_TITLE = "Title";
    public static final String TASK_DESC = "Description";
    public static final String TASK_NOTE = "Note";
    public static final String TASK_DEADLINE = "Deadline";

    /*Tool table*/
    public static final String TOOL_TABLE_NAME = "Tool";
    public static final String TOOL_ID = "_id";
    public static final String TOOL_NAME = "Name";
    public static final String TOOL_ICON = "Icon_path";

    /*Usage table*/
    public static final String USAGE_TABLE_NAME = "Usage";
    public static final String USAGE_ID = "_id";
    public static final String USAGE_TOOL_ID = "tool_id";
    public static final String USAGE_TASK_ID = "task_id";

    public static synchronized DbHandler getInstance(Context context) {
        if (singleInstance == null) {
            singleInstance = new DbHandler(context.getApplicationContext());
        }
        return singleInstance;
    }


    private DbHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USER_TABLE = "CREATE TABLE " + USER_TABLE_NAME + "(" +
                USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                USER_NAME + " TEXT NOT NULL, " +
                USER_PASSWORD + " TEXT NOT NULL, " +
                USER_AVATAR + " TEXT);";

        String CREATE_TASK_TABLE = "CREATE TABLE " + TASK_TABLE_NAME + "(" +
                TASK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TASK_CREATOR_ID + " INTEGER REFERENCES User(Id) ON UPDATE CASCADE ON DELETE CASCADE, " +
                TASK_ASSIGNEE_ID + " INTEGER REFERENCES User(Id) ON UPDATE CASCADE ON DELETE SET NULL, " +
                TASK_TITLE + " TEXT NOT NULL, " +
                TASK_DESC + " TEXT NOT NULL, " +
                TASK_NOTE + " TEXT NOT NULL, " +
                TASK_DEADLINE + " TEXT);";

        String CREATE_TOOL_TABLE = "CREATE TABLE " + TOOL_TABLE_NAME + "(" +
                TOOL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TOOL_NAME + " TEXT NOT NULL, " +
                TOOL_ICON + " TEXT);";

        String CREATE_USAGE_TABLE = "CREATE TABLE " + USAGE_TABLE_NAME + "(" +
                USAGE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                USAGE_TOOL_ID + " INTEGER NOT NULL, " +
                USAGE_TASK_ID + " INTEGER NOT NULL);";

        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_TASK_TABLE);
        db.execSQL(CREATE_TOOL_TABLE);
        db.execSQL(CREATE_USAGE_TABLE);
    }

    public void insertUser(User user) {
        ContentValues content = new ContentValues();
        content.put(USER_NAME, user.getName());
        content.put(USER_PASSWORD, user.getPassword());
        content.put(USER_AVATAR, user.getAvatar());
        singleInstance.getWritableDatabase().insert(USER_TABLE_NAME, null, content);
    }

    public void insertTask(Task task) {
        ContentValues content = new ContentValues();
        content.put(TASK_CREATOR_ID, task.getAssignee_id());
        content.put(TASK_ASSIGNEE_ID, task.getAssignee_id());
        content.put(TASK_TITLE, task.getTitle());
        content.put(TASK_DESC, task.getDescription());
        content.put(TASK_NOTE, task.getNote());
        content.put(TASK_DEADLINE, task.getDeadline());
        singleInstance.getWritableDatabase().insert(TASK_TABLE_NAME, null, content);
    }

    public void insertTool(Tool tool) {
        ContentValues content = new ContentValues();
        content.put(TOOL_NAME, tool.getName());
        content.put(TOOL_ICON, tool.getIcon());
        singleInstance.getWritableDatabase().insert(TOOL_TABLE_NAME, null, content);
    }

    public void insertUsage(Usage usage) {
        ContentValues content = new ContentValues();
        content.put(USAGE_TOOL_ID, usage.getTool_id());
        content.put(USAGE_TASK_ID, usage.getTask_id());
        singleInstance.getWritableDatabase().insert(USAGE_TABLE_NAME, null, content);
    }

    public void updateUser(User user) {
        ContentValues content = new ContentValues();
        content.put(USER_NAME, user.getName());
        content.put(USER_PASSWORD, user.getPassword());
        content.put(USER_AVATAR, user.getAvatar());
        singleInstance.getWritableDatabase().update(
                USER_TABLE_NAME,
                content,
                USER_ID + "=" + user.getId(),
                null
        );
    }

    public void udpateTask(Task task) {
        ContentValues content = new ContentValues();
        content.put(TASK_CREATOR_ID, task.getAssignee_id());
        content.put(TASK_ASSIGNEE_ID, task.getAssignee_id());
        content.put(TASK_TITLE, task.getTitle());
        content.put(TASK_DESC, task.getDescription());
        content.put(TASK_NOTE, task.getNote());
        content.put(TASK_DEADLINE, task.getDeadline());
        singleInstance.getWritableDatabase().update(
                TASK_TABLE_NAME,
                content,
                TASK_ID + "=" + task.getId(),
                null
        );
    }

    public void updateTool(Tool tool) {
        ContentValues content = new ContentValues();
        content.put(TOOL_NAME, tool.getName());
        content.put(TOOL_ICON, tool.getIcon());
        singleInstance.getWritableDatabase().update(
                TOOL_TABLE_NAME,
                content,
                TOOL_ID + "=" + tool.getId(),
                null
        );
    }

    public void updateUsage(Usage usage) {
        ContentValues content = new ContentValues();
        content.put(USAGE_TOOL_ID, usage.getTool_id());
        content.put(USAGE_TASK_ID, usage.getTask_id());
        singleInstance.getWritableDatabase().update(
                USAGE_TABLE_NAME,
                content,
                USAGE_ID + "=" + usage.getId(),
                null
        );
    }

    public void deleteUser(User user) {
        singleInstance.getWritableDatabase().delete(
                USER_TABLE_NAME,
                USER_ID + "=" + user.getId(),
                null);
    }

    public void deleteTask(Task task) {
        singleInstance.getWritableDatabase().delete(
                TASK_TABLE_NAME,
                TASK_ID + "=" + task.getId(),
                null);
    }

    public void deleteTool(Tool tool) {
        singleInstance.getWritableDatabase().delete(
                TOOL_TABLE_NAME,
                TOOL_ID + "=" + tool.getId(),
                null);
    }

    public void deleteUsage(Usage usage) {
        singleInstance.getWritableDatabase().delete(
                USAGE_TABLE_NAME,
                USAGE_ID + "=" + usage.getId(),
                null
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TASK_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TOOL_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + USAGE_TABLE_NAME);
        onCreate(db);
    }
}
