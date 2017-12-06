package com.project.seg2105.choremanager;

import android.content.Context;
import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import static android.app.Activity.RESULT_FIRST_USER;
import static android.app.Activity.RESULT_OK;

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

        Log.d("test", DbHandler.getInstance(getActivity()).insertUser(new User("Person 4", "password", "girl_1", 0)) + "");

        //We create a SimpleCursorAdapter without a cursor for now.
        //The cursor will be provided in the onLoadFinished method below.
        ListView users = view.findViewById(R.id.userList);
        adapter = new SimpleCursorAdapter(getActivity(),
                R.layout.people_row, null,
                new String[] { DbHandler.USER_NAME, "TasksCount", DbHandler.USER_ID, DbHandler.USER_AVATAR, DbHandler.TASK_TITLE },
                new int[] { R.id.name, R.id.tasksCount, R.id.userId, R.id.avatar, R.id.nextTask }, 0){
            @Override
            public void setViewImage(ImageView v, String value) {
                //Setting the src attribute of the ImageView
                v.setImageResource(getResources().getIdentifier(value, "drawable", getActivity().getApplicationContext().getPackageName()));
            }
        };
        users.setAdapter(adapter);

        users.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView id = view.findViewById(R.id.userId);
                int userId = Integer.parseInt(id.getText().toString());
                Intent intent = new Intent(getActivity(), UserTask.class);
                intent.putExtra("UserID", userId);
                intent.putExtra("CurrentUser", ((MainActivity)getActivity()).currentUser.getId());
                startActivityForResult(intent, 1);
            }
        });

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
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case RESULT_OK:
                ((MainActivity)getActivity()).notifyFragments();
                break;
            case RESULT_FIRST_USER:
                User user = new User();
                user.setId(data.getIntExtra("Id", 1));
                ((MainActivity)getActivity()).setUpCurrentUser(user);
        }
    }
}
