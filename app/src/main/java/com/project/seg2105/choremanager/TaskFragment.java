package com.project.seg2105.choremanager;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.util.Date;

/**
 * Created by jalilcompaore on 05/11/17. */


public class TaskFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private SimpleCursorAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.task_fragment, container, false);

        Tool tool = new Tool("Sponge", "circle_sponge");
        DbHandler.getInstance(getActivity()).insertTool(tool);

        Button button = view.findViewById(R.id.btn);
        button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                DbHandler.getInstance(getActivity()).insertTask(new Task(1, 1, "IT IS WORKING!", "No desc", "Get the trash out", ""));
                ((MainActivity) getActivity()).notifyFragments();

                Toast.makeText(getActivity(), "Working!", Toast.LENGTH_LONG).show();
            }
        });

        //We create a SimpleCursorAdapter without a cursor for now.
        //The cursor will be provided in the onLoadFinished method below.
        adapter = new SimpleCursorAdapter(getActivity(),
                R.layout.task_row, null,
                new String[] {DbHandler.TASK_TITLE, DbHandler.TASK_NOTE},
                new int[] {R.id.title, R.id.note}, 0);

        ListView listview = view.findViewById(R.id.taskList);
        listview.setAdapter(adapter);

        //This will call the onCreateLoader method below
        getLoaderManager().initLoader(0, null, this);

        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                startActivityForResult(new Intent(getActivity(), CreateTask.class), 1);
            }
        });
        return view;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new MyCursorLoader(getActivity(), MyCursorLoader.ALL_TASKS);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //update UI
        ((MainActivity) getActivity()).notifyFragments();
        Toast.makeText(getActivity(), "New task added!", Toast.LENGTH_LONG).show();
    }
}

