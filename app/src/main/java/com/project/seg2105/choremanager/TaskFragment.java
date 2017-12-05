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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
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

    private static final int CREATE_TASK_REQUEST = 1;
    private static final int VIEW_TASK_REQUEST = 2;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.task_fragment, container, false);

        //TODO: remove later
        Tool tool = new Tool("Hello", "circle_ladder");
        DbHandler.getInstance(getActivity()).insertTool(tool);

        Button button = view.findViewById(R.id.btn);
        button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                DbHandler.getInstance(getActivity()).insertTask(new Task(1, 1, "Task C", "No desc", "Test note","Assigned", "10/12/2017", 300));
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
                new String[] {DbHandler.TASK_ID, DbHandler.TASK_TITLE, DbHandler.TASK_NOTE, DbHandler.USER_AVATAR},
                new int[] {R.id.taskId, R.id.title, R.id.note, R.id.avatar}, 0){
            @Override
            public void setViewImage(ImageView v, String value) {
                //Setting the src attribute of the ImageView
                v.setImageResource(getResources().getIdentifier(value, "drawable", getActivity().getPackageName()));
            }
        };

        //This will set the task's icon to a default image when it is unassigned.
        adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int i) {
                boolean b = view.getId() == R.id.avatar;
                //If the view is the avatar's ImageView
                if(view.getId() == R.id.avatar) {
                    //If the task is unassigned
                    if(/*cursor.getString(cursor.getColumnIndex(DbHandler.TASK_STATUS)).equals(Task.Status.UNASSIGNED.toString())*/
                            cursor.isNull(i) || cursor.getInt(i) < 0) {
                        ((ImageView)(view)).setImageResource(R.drawable.question_mark_button);
                        return true;
                    }
                }
                return false;
            }
        });

        ListView listview = view.findViewById(R.id.taskList);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView id = view.findViewById(R.id.taskId);
                int taskId = Integer.parseInt(id.getText().toString());
                //TODO: CHANGE LATER
                Intent intent = new Intent(getActivity(), ViewTask.class);
                intent.putExtra("TaskID", taskId);
                startActivityForResult(intent, VIEW_TASK_REQUEST);
            }
        });

        //This will call the onCreateLoader method below
        getLoaderManager().initLoader(0, null, this);

        //Button add new task
        Button buttonAdd = view.findViewById(R.id.button);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                startActivityForResult(new Intent(getActivity(), CreateTask.class), CREATE_TASK_REQUEST);
            }
        });
        TextView textButton = view.findViewById(R.id.textButton);
        textButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                startActivityForResult(new Intent(getActivity(), CreateTask.class), CREATE_TASK_REQUEST);
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
            loader = new MyCursorLoader(getActivity(), MyCursorLoader.ALL_TASKS_AVAILABLE);
        } else {
            User currentUser = ((MainActivity)getActivity()).currentUser;
            loader = new MyCursorLoader(getActivity(), MyCursorLoader.ALL_TASKS_FOR_PARTICULAR_ASSIGNEE, currentUser);
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

