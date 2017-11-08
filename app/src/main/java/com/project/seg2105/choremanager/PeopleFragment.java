package com.project.seg2105.choremanager;

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
    private SQLiteDatabase db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        dbHelper = new MyDbHandler(getActivity());
        db = dbHelper.getWritableDatabase();

        adapter = new SimpleCursorAdapter(getActivity(),
                R.layout.people_row, null,
                new String[] { MyDbHandler.USER_NAME },
                new int[] { R.id.name }, 0);
        setListAdapter(adapter);

        dbHelper.insertUser(db, new User("IT IS FUCKING WORKING!", "password", "avatar"));

        getLoaderManager().initLoader(0, null, this);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), null, new String[] {MyDbHandler.USER_ID, MyDbHandler.USER_NAME}, null, null, null) {
            @Override
            public Cursor loadInBackground() {
                return db.query(MyDbHandler.USER_TABLE_NAME, new String[] {MyDbHandler.USER_ID, MyDbHandler.USER_NAME}, null, null, null, null, null);
            }
        };
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
