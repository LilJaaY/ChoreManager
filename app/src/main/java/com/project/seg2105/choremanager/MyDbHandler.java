package com.project.seg2105.choremanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.SQLInput;

/**
 * Created by jalilcompaore on 06/11/17.
 */

public class MyDbHandler extends SQLiteOpenHelper {

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
    public static final String TASK_ASSIGNEE_ID = "Assignee_id";
    public static final String TASK_TITLE = "Title";
    public static final String TASK_DESC = "Description";
    public static final String TASK_NOTE = "Note";
    public static final String TASK_DEADLINE = "Deadline";


    public MyDbHandler(Context context) {
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
                TASK_ASSIGNEE_ID + " INTEGER REFERENCES User(Id) ON UPDATE CASCADE ON DELETE SET NULL, " +
                TASK_TITLE + " TEXT NOT NULL, " +
                TASK_DESC + " TEXT NOT NULL, " +
                TASK_NOTE + " TEXT NOT NULL, " +
                TASK_DEADLINE + " DATE);";

        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_TASK_TABLE);
    }

    public void insertUser(SQLiteDatabase db, User user) {
        ContentValues content = new ContentValues();
        content.put(USER_NAME, user.getName());
        content.put(USER_PASSWORD, user.getPassword());
        content.put(USER_AVATAR, user.getAvatar());
        db.insert(USER_TABLE_NAME, null, content);
    }

    public void insertTask(SQLiteDatabase db, Task task) {
        ContentValues content = new ContentValues();
        content.put(TASK_ASSIGNEE_ID, task.getAssignee_id());
        content.put(TASK_TITLE, task.getTitle());
        content.put(TASK_DESC, task.getDescription());
        content.put(TASK_NOTE, task.getNote());
        //TODO format the date before inserting
        content.put(TASK_DEADLINE, task.getDeadline().toString());
        db.insert(TASK_TABLE_NAME, null, content);
    }

    public void updateUser(SQLiteDatabase db, User user) {
        
    }

    public void udpateTask(SQLiteDatabase db, Task task) {

    }

    public void deleteUser(SQLiteDatabase db, User user) {

    }

    public void deleteTask(SQLiteDatabase db, Task task) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TASK_TABLE_NAME);
        onCreate(db);

    }
}
