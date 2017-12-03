package com.project.seg2105.choremanager;

import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.MergeCursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;

/**
 * Created by jalilcompaore on 05/11/17.
 */

public class PeopleFragment extends ListFragment implements
        LoaderManager.LoaderCallbacks<Cursor> {

    //Change that later
    public static SimpleCursorAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.d("test", DbHandler.getInstance(getActivity()).insertUser(new User("IT IS FUCKING WORKING!", "password", "girl_1")) + "");

        //We create a SimpleCursorAdapter without a cursor for now.
        //The cursor will be provided in the onLoadFinished method below.
        adapter = new SimpleCursorAdapter(getActivity(),
                R.layout.people_row, null,
                new String[] { DbHandler.USER_NAME, "TasksCount", DbHandler.USER_TABLE_NAME + "." + DbHandler.USER_ID, /*DbHandler.USER_AVATAR*/ },
                new int[] { R.id.name, R.id.tasksCount, R.id.userId, /*R.id.avatar*/ }, 0){
            @Override
            public void setViewImage(ImageView v, String value) {
                //Setting the src attribute of the ImageView
                v.setImageResource(getResources().getIdentifier(value, "drawable", getActivity().getApplicationContext().getPackageName()));
            }
        };
        setListAdapter(adapter);

        //This will call the onCreateLoader method below.
        getLoaderManager().initLoader(0, null, this);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new MyCursorLoader(getActivity(), MyCursorLoader.ALL_USERS);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        /*MatrixCursor extra = new MatrixCursor(new String[] {DbHandler.USER_NAME, "TasksCount", DbHandler.USER_ID, DbHandler.USER_AVATAR});
        extra.addRow(new String[] {"Nobody", "0", "-1", "question_mark_button"});
        Cursor[] cursors = { extra, data };
        Cursor extendedCursor = new MergeCursor(cursors);
        adapter.swapCursor(extendedCursor);*/
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }
}
