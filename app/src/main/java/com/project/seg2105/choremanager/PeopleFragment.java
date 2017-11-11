package com.project.seg2105.choremanager;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;

/**
 * Created by jalilcompaore on 05/11/17.
 */

public class PeopleFragment extends ListFragment implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private SimpleCursorAdapter adapter;
    private MyDbHandler dbHelper;
    private static SQLiteDatabase db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        dbHelper = new MyDbHandler(getActivity());
        db = dbHelper.getReadableDatabase();

        //We create a SimpleCursorAdapter without a cursor for now.
        //The cursor will be provided in the onLoadFinished method below.
        adapter = new SimpleCursorAdapter(getActivity(),
                R.layout.people_row, null,
                new String[] { MyDbHandler.USER_NAME , "TasksCount" },
                new int[] { R.id.name ,R.id.tasksCount}, 0);
        setListAdapter(adapter);

        dbHelper.insertUser(db, new User("IT IS FUCKING WORKING!", "password", "avatar"));

        //This will call the onCreateLoader method below.
        getLoaderManager().initLoader(0, null, this);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private static class MyCursorLoader extends CursorLoader {

        private MyCursorLoader(Context context) {
            super(context);
        }

        @Override
        public Cursor loadInBackground() {
            /*Cursor cursor = db.query(MyDbHandler.USER_TABLE_NAME, new String[] {MyDbHandler.USER_ID, MyDbHandler.USER_NAME},
                    null, null, null, null, null);
            cursor.getCount();
            return cursor;*/
            String sql = "SELECT COUNT(" + MyDbHandler.TASK_TABLE_NAME + "." + MyDbHandler.TASK_ASSIGNEE_ID + ") as TasksCount, "
                    + MyDbHandler.USER_TABLE_NAME + "." + MyDbHandler.USER_ID + ", " + MyDbHandler.USER_NAME
                    + " FROM " + MyDbHandler.USER_TABLE_NAME + " LEFT JOIN " + MyDbHandler.TASK_TABLE_NAME
                    + " ON " + MyDbHandler.USER_TABLE_NAME + "." + MyDbHandler.USER_ID + " = " + MyDbHandler.TASK_ASSIGNEE_ID
//                    + " WHERE " + MyDbHandler.TASK_TABLE_NAME + "." + MyDbHandler.TASK_ID + " IS NOT NULL"
                    + " GROUP BY " + MyDbHandler.TASK_TABLE_NAME + "." + MyDbHandler.TASK_ASSIGNEE_ID + ";";
            Cursor cursor = db.rawQuery(sql, null);
            cursor.getCount();
            return cursor;
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new MyCursorLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }
}
