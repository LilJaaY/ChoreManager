package com.project.seg2105.choremanager;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ShoppingFragment extends Fragment {
    private Cursor materialsCursor;
    private Cursor groceriesCursor;
    private Spinner category;
    private SimpleCursorAdapter groceriesAdapter;
    private SimpleCursorAdapter materialsAdapter;
    private SparseBooleanArray materialsCheckBoxStates = new SparseBooleanArray();
    private SparseBooleanArray groceriesCheckBoxStates = new SparseBooleanArray();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_shopping, container, false);

        //Populating the Gridviews
        GridView materialsGrid = v.findViewById(R.id.materials);
        materialsAdapter = new SimpleCursorAdapter(getActivity(),
                R.layout.shopping_row, null,
                new String[] {DbHandler.ITEM_NAME},
                new int[] {R.id.name}, 0) {
            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                CheckBox checkBox = view.findViewById(R.id.check);
                checkBox.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        if(((CheckBox)v).isChecked()){
                            materialsCheckBoxStates.append(position, true);
                        }else{
                            materialsCheckBoxStates.append(position, false);
                        }
                    }
                });
                checkBox.setChecked(materialsCheckBoxStates.get(position));
                return view;
            }
        };
        materialsGrid.setAdapter(materialsAdapter);

        GridView groceriesGrid = v.findViewById(R.id.groceries);
        groceriesAdapter = new SimpleCursorAdapter(getActivity(),
                R.layout.shopping_row, null,
                new String[] {DbHandler.ITEM_NAME},
                new int[] {R.id.name}, 0) {
            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                CheckBox checkBox = view.findViewById(R.id.check);
                checkBox.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        if(((CheckBox)v).isChecked()){
                            groceriesCheckBoxStates.append(position, true);
                        }else{
                            groceriesCheckBoxStates.append(position, false);
                        }
                    }
                });
                checkBox.setChecked(groceriesCheckBoxStates.get(position));
                return view;
            }
        };
        groceriesGrid.setAdapter(groceriesAdapter);

        updateUI();

        //Populating spinner
        category = v.findViewById(R.id.category);
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.categories)
        );
        category.setAdapter(categoryAdapter);

        //ImageView onClickListener
        ImageView add = v.findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Check editText is not empty
                EditText name = ShoppingFragment.this.getView().findViewById(R.id.addItem);
                if(!name.getText().toString().isEmpty()) {
                    //Add item to database based on spinner's value
                    Item item = new Item();
                    if(category.getSelectedItem().toString().equals("Materials")) {
                        item.setCategory("Materials");
                        item.setName(name.getText().toString());
                        DbHandler.getInstance(ShoppingFragment.this.getActivity()).insertItem(item);
                    } else {
                        item.setCategory("Groceries");
                        item.setName(name.getText().toString());
                        DbHandler.getInstance(ShoppingFragment.this.getActivity()).insertItem(item);
                    }

                    //Update UI
                    updateUI();
                }
            }
        });
        return  v;
    }

    public void updateUI() {
        String sql = "SELECT * FROM " + DbHandler.ITEM_TABLE_NAME + " WHERE "
                + DbHandler.ITEM_CATEGORY + "='Materials';";
        materialsCursor = DbHandler.getInstance(getActivity()).getWritableDatabase().rawQuery(sql, null);
        materialsAdapter.changeCursor(materialsCursor);

        sql = "SELECT * FROM " + DbHandler.ITEM_TABLE_NAME + " WHERE "
                + DbHandler.ITEM_CATEGORY + "='Groceries';";
        groceriesCursor = DbHandler.getInstance(getActivity()).getWritableDatabase().rawQuery(sql, null);
        groceriesAdapter.changeCursor(groceriesCursor);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        materialsCursor.close();
        groceriesCursor.close();
    }
}
