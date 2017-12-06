package com.project.seg2105.choremanager;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
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
    private final static int MAX_ITEMS_COLLECTION_SIZE = 20;
    private ArrayList<Boolean> materialsCheckBoxStates = new ArrayList<>();
    private ArrayList<Boolean> groceriesCheckBoxStates = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_shopping, container, false);

        //Initializing checkboxes' states
        for(int i = 0; i < MAX_ITEMS_COLLECTION_SIZE; i++) {
            materialsCheckBoxStates.add(i, false);
            groceriesCheckBoxStates.add(i, false);
        }

        //Populating the Gridviews
        GridView materialsGrid = v.findViewById(R.id.materials);
        updateMaterialsCursor();
        materialsAdapter = new SimpleCursorAdapter(getActivity(),
                R.layout.shopping_row, materialsCursor,
                new String[] {DbHandler.ITEM_NAME},
                new int[] {R.id.name}, 0) {
            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                CheckBox checkBox = view.findViewById(R.id.check);
                checkBox.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        if(((CheckBox)v).isChecked()){
                            materialsCheckBoxStates.set(position, true);
                        }else{
                            materialsCheckBoxStates.set(position, false);
                        }
                    }
                });
                checkBox.setChecked(materialsCheckBoxStates.get(position));
                return view;
            }
        };
        materialsGrid.setAdapter(materialsAdapter);

        GridView groceriesGrid = v.findViewById(R.id.groceries);
        updateGroceriesCursor();
        groceriesAdapter = new SimpleCursorAdapter(getActivity(),
                R.layout.shopping_row, groceriesCursor,
                new String[] {DbHandler.ITEM_NAME},
                new int[] {R.id.name}, 0) {
            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                CheckBox checkBox = view.findViewById(R.id.check);
                checkBox.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        if(((CheckBox)v).isChecked()){
                            groceriesCheckBoxStates.set(position, true);
                        }else{
                            groceriesCheckBoxStates.set(position, false);
                        }
                    }
                });
                checkBox.setChecked(groceriesCheckBoxStates.get(position));
                return view;
            }
        };
        groceriesGrid.setAdapter(groceriesAdapter);

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
                    updateGroceriesCursor();
                    updateMaterialsCursor();
                    groceriesAdapter.changeCursor(groceriesCursor);
                   // groceriesAdapter.notifyDataSetChanged();
                    materialsAdapter.changeCursor(materialsCursor);
                    //materialsAdapter.notifyDataSetChanged();
                }
            }
        });
        return  v;
    }

    /*public void onAddClick(View v) {
        //Check editText is not empty
        EditText name = getView().findViewById(R.id.addItem);
        if(name.getText().toString().isEmpty()) {
            //Add item to database based on spinner's value
            Item item = new Item();
            if(category.getSelectedItem().toString().equals("Materials")) {
                item.setCategory("Materials");
                item.setName(name.getText().toString());
                DbHandler.getInstance(getActivity()).insertItem(item);
            } else {
                item.setCategory("Groceries");
                item.setName(name.getText().toString());
                DbHandler.getInstance(getActivity()).insertItem(item);
            }

            //Update UI
            updateGroceriesCursor();
            updateMaterialsCursor();
            groceriesAdapter.changeCursor(groceriesCursor);
            materialsAdapter.changeCursor(materialsCursor);
        }
    }*/

    private void updateMaterialsCursor() {
        String sql = "SELECT * FROM " + DbHandler.ITEM_TABLE_NAME + " WHERE "
                + DbHandler.ITEM_CATEGORY + "='Materials';";
        materialsCursor = DbHandler.getInstance(getActivity()).getWritableDatabase().rawQuery(sql, null);
    }

    private void updateGroceriesCursor() {
        String sql = "SELECT * FROM " + DbHandler.ITEM_TABLE_NAME + " WHERE "
                + DbHandler.ITEM_CATEGORY + "='Groceries';";
        groceriesCursor = DbHandler.getInstance(getActivity()).getWritableDatabase().rawQuery(sql, null);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        materialsCursor.close();
        groceriesCursor.close();
    }
}
