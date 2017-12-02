package com.project.seg2105.choremanager;

import android.content.Intent;
import android.database.Cursor;
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
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Switch;
import android.widget.TextView;
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

        //TODO: remove later
        Tool tool = new Tool("Sponge", "circle_sponge");
        DbHandler.getInstance(getActivity()).insertTool(tool);

        Button button = view.findViewById(R.id.btn);
        button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                DbHandler.getInstance(getActivity()).insertTask(new Task(1, 1, "IT IS WORKING!", "No desc", "Get the trash out", ""));
                ((MainActivity)getActivity()).notifyFragments();
                Toast.makeText(getActivity(), "Working!", Toast.LENGTH_LONG).show();
            }
        });

        //Switch listener
        Switch mySwitch = (Switch) view.findViewById(R.id.userTasks);
        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                refreshUI();
            }
        });

        //We create a SimpleCursorAdapter without a cursor for now.
        //The cursor will be provided in the onLoadFinished method below.
        adapter = new SimpleCursorAdapter(getActivity(),
                R.layout.task_row, null,
                new String[] {DbHandler.TASK_TITLE, DbHandler.TASK_NOTE, /*DbHandler.USER_AVATAR*/},
                new int[] {R.id.title, R.id.note, /*R.id.avatar*/}, 0);

        ListView listview = view.findViewById(R.id.taskList);
        listview.setAdapter(adapter);

        //This will call the onCreateLoader method below
        getLoaderManager().initLoader(0, null, this);

        //Button add new task
        Button buttonAdd = view.findViewById(R.id.button);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                startActivityForResult(new Intent(getActivity(), CreateTask.class), 1);
            }
        });
        TextView textButton = view.findViewById(R.id.textButton);
        textButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                startActivityForResult(new Intent(getActivity(), CreateTask.class), 1);
            }
        });
        return view;
    }

    public void refreshUI() {
        Switch mySwitch = (Switch) getView().findViewById(R.id.userTasks);
        if(mySwitch.isChecked()) {
            getLoaderManager().restartLoader(1, null, TaskFragment.this);
        } else {
            getLoaderManager().restartLoader(0, null, TaskFragment.this);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Loader<Cursor> loader;
        if(id == 0) {
            loader = new MyCursorLoader(getActivity(), MyCursorLoader.ALL_TASKS);
        } else {
            User currentUser = ((MainActivity)getActivity()).currentUser;
            loader = new MyCursorLoader(getActivity(), MyCursorLoader.ALL_TASKS_FOR_PARTICULAR_USER, currentUser);
        }
        return loader;
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
        ((MainActivity)getActivity()).notifyFragments();
        Toast.makeText(getActivity(), "New task added!", Toast.LENGTH_LONG).show();
    }
}

