package com.project.seg2105.choremanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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
    public static final String TASK_STATUS = "Status";
    public static final String TASK_DEADLINE = "Deadline";

    /*Tool table*/
    public static final String TOOL_TABLE_NAME = "Tool";
    public static final String TOOL_ID = "_id";
    public static final String TOOL_NAME = "Name";
    public static final String TOOL_ICON = "Icon_path";
    //Tools count
    public static int TOOLS_COUNT = 0;

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
                TASK_STATUS + " TEXT, " +
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

    public int insertUser(User user) {
        String sql;
        int id;
        ContentValues content = new ContentValues();
        content.put(USER_NAME, user.getName());
        content.put(USER_PASSWORD, user.getPassword());
        content.put(USER_AVATAR, user.getAvatar());
        singleInstance.getWritableDatabase().insert(USER_TABLE_NAME, null, content);

        //return Id
        sql = "SELECT last_insert_rowid();";
        Cursor c = singleInstance.getWritableDatabase().rawQuery(sql, null);
        c.moveToFirst();
        id = c.getInt(0);
        c.close();
        Log.d(id + "", "test");
        return id;
    }

    public int insertTask(Task task) {
        String sql;
        int id;
        ContentValues content = new ContentValues();
        content.put(TASK_CREATOR_ID, task.getCreator_id());
        content.put(TASK_ASSIGNEE_ID, task.getAssignee_id());
        content.put(TASK_TITLE, task.getTitle());
        content.put(TASK_DESC, task.getDescription());
        content.put(TASK_NOTE, task.getNote());
        content.put(TASK_STATUS, task.getStatus());
        content.put(TASK_DEADLINE, task.getDeadline());
        singleInstance.getWritableDatabase().insert(TASK_TABLE_NAME, null, content);

        //return Id
        sql = "SELECT last_insert_rowid();";
        Cursor c = singleInstance.getWritableDatabase().rawQuery(sql, null);
        c.moveToFirst();
        id = c.getInt(0);
        c.close();
        Log.d(id + "", "test");
        return id;
    }

    public int insertTool(Tool tool) {
        TOOLS_COUNT++;
        String sql;
        int id;
        ContentValues content = new ContentValues();
        content.put(TOOL_NAME, tool.getName());
        content.put(TOOL_ICON, tool.getIcon());
        singleInstance.getWritableDatabase().insert(TOOL_TABLE_NAME, null, content);

        //return Id
        sql = "SELECT last_insert_rowid();";
        Cursor c = singleInstance.getWritableDatabase().rawQuery(sql, null);
        c.moveToFirst();
        id = c.getInt(0);
        c.close();
        Log.d(id + "", "test");
        return id;
    }

    public int insertUsage(Usage usage) {
        String sql;
        int id;
        ContentValues content = new ContentValues();
        content.put(USAGE_TOOL_ID, usage.getTool_id());
        content.put(USAGE_TASK_ID, usage.getTask_id());
        singleInstance.getWritableDatabase().insert(USAGE_TABLE_NAME, null, content);

        //return Id
        sql = "SELECT last_insert_rowid();";
        Cursor c = singleInstance.getWritableDatabase().rawQuery(sql, null);
        c.moveToFirst();
        id = c.getInt(0);
        c.close();
        Log.d(id + "", "test");
        return id;
    }

    public User findUser(User userToFind) {
        User userFound = null;
        Cursor cursor;
        cursor = singleInstance.getWritableDatabase().query(
                DbHandler.USER_TABLE_NAME,
                new String[] {DbHandler.USER_ID, DbHandler.USER_NAME, DbHandler.USER_PASSWORD, DbHandler.USER_AVATAR},
                DbHandler.USER_ID + "=" + userToFind.getId(), null, null, null, null);
        if(cursor.getCount() == 1) {
            cursor.moveToFirst();
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String password = cursor.getString(2);
            String avatar = cursor.getString(3);
            userFound = new User(name, password, avatar);
            userFound.setId(id);
        }
        cursor.close();
        return userFound;
    }

    public Task findTask(Task taskToFind) {
        Task taskFound = null;
        Cursor cursor;
        cursor = singleInstance.getWritableDatabase().query(
                DbHandler.TASK_TABLE_NAME,
                new String[] {DbHandler.TASK_ID, DbHandler.TASK_CREATOR_ID, DbHandler.TASK_ASSIGNEE_ID,
                        DbHandler.TASK_TITLE, DbHandler.TASK_DESC, DbHandler.TASK_NOTE,
                        DbHandler.TASK_STATUS, DbHandler.TASK_DEADLINE},
                DbHandler.TASK_ID + "=" + taskToFind.getId(), null, null, null, null);
        if(cursor.getCount() == 1) {
            cursor.moveToFirst();
            int id = cursor.getInt(0);
            int creatorId = cursor.getInt(1);
            int assigneeId = cursor.getInt(2);
            String title = cursor.getString(3);
            String description = cursor.getString(4);
            String note = cursor.getString(5);
            String status = cursor.getString(6);
            String deadline = cursor.getString(7);
            taskFound = new Task(creatorId, title, description, note, status, deadline);
            taskFound.setId(id);
            taskFound.setAssignee_id(assigneeId);
        }
        cursor.close();
        return taskFound;
    }

    public Tool findTool(Tool toolToFind) {
        Tool toolFound = null;
        Cursor cursor;
        cursor = singleInstance.getWritableDatabase().query(
                DbHandler.TOOL_TABLE_NAME,
                new String[] {DbHandler.TOOL_ID, DbHandler.TOOL_NAME, DbHandler.TOOL_ICON},
                DbHandler.TOOL_ID + "=" + toolToFind.getId(), null, null, null, null);
        if(cursor.getCount() == 1) {
            cursor.moveToFirst();
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String icon = cursor.getString(2);
            toolFound = new Tool(name, icon);
            toolFound.setId(id);
        }
        cursor.close();
        return toolFound;
    }

    public Usage findUsage(Usage usageToFind) {
        Usage usageFound = null;
        Cursor cursor;
        cursor = singleInstance.getWritableDatabase().query(
                DbHandler.USAGE_TABLE_NAME,
                new String[] {DbHandler.USAGE_ID, DbHandler.USAGE_TOOL_ID, DbHandler.USAGE_TASK_ID},
                DbHandler.USAGE_ID + "=" + usageToFind.getId(), null, null, null, null);
        if(cursor.getCount() == 1) {
            cursor.moveToFirst();
            int id = cursor.getInt(0);
            int tooId = cursor.getInt(1);
            int taskId = cursor.getInt(2);
            usageFound = new Usage(tooId, taskId);
            usageFound.setId(id);
        }
        cursor.close();
        return usageFound;
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
        content.put(TASK_CREATOR_ID, task.getCreator_id());
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
        TOOLS_COUNT--;
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
