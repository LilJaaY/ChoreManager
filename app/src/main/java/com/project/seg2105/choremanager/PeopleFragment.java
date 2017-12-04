package com.project.seg2105.choremanager;

import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.MergeCursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

/**
 * Created by jalilcompaore on 05/11/17.
 */

public class PeopleFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor> {

    //Change that later
    private SimpleCursorAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.people_fragment, container, false);

        Log.d("test", DbHandler.getInstance(getActivity()).insertUser(new User("Person 3", "password", "girl_1")) + "");

        //We create a SimpleCursorAdapter without a cursor for now.
        //The cursor will be provided in the onLoadFinished method below.
        ListView users = view.findViewById(R.id.userList);
        adapter = new SimpleCursorAdapter(getActivity(),
                R.layout.people_row, null,
                new String[] { DbHandler.USER_NAME, "TasksCount", DbHandler.USER_ID, DbHandler.USER_AVATAR },
                new int[] { R.id.name, R.id.tasksCount, R.id.userId, R.id.avatar }, 0){
            @Override
            public void setViewImage(ImageView v, String value) {
                //Setting the src attribute of the ImageView
                v.setImageResource(getResources().getIdentifier(value, "drawable", getActivity().getApplicationContext().getPackageName()));
            }
        };
        users.setAdapter(adapter);

        //This will call the onCreateLoader method below.
        getLoaderManager().initLoader(0, null, this);

        return view;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new MyCursorLoader(getActivity(), MyCursorLoader.ALL_USERS);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        MatrixCursor extra = new MatrixCursor(new String[] {"TasksCount",
                DbHandler.USER_ID,
                DbHandler.USER_NAME,
                DbHandler.USER_PASSWORD,
                DbHandler.USER_AVATAR});
        extra.addRow(new String[] {"0", "-1", "Nobody", "pass", "question_mark_button"});
        Cursor[] cursors = { extra, data };
        Cursor extendedCursor = new MergeCursor(cursors);
        adapter.swapCursor(extendedCursor);
        /*adapter.swapCursor(data);*/
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }
}
