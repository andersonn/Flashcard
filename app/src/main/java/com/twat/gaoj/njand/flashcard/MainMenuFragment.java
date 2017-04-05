package com.twat.gaoj.njand.flashcard;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.twat.gaoj.njand.flashcard.Database.DatabaseContract.FlashcardTableEntries;

public class MainMenuFragment extends Fragment {
    MainActivity mainActivity;
    ListView setsListView;
    FloatingActionButton newSetButton;
    ArrayList<Integer> databaseIds = new ArrayList<Integer>();    // Ids for the names in the list view
    Map<Integer, Integer> map = new HashMap<Integer, Integer>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.main_menu, container, false);

        newSetButton = (FloatingActionButton) view.findViewById(R.id.add_new_set);
        newSetButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                LayoutInflater li = LayoutInflater.from(getContext());
                View promptsView = li.inflate(R.layout.prompts, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                alertDialogBuilder.setView(promptsView);
                final EditText userInput = (EditText) promptsView.findViewById(R.id.editTextDialogUserInput);
                alertDialogBuilder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        mainActivity.mDbAccess.newSet(userInput.getText().toString());
                                        displaySets();
                                    }
                                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

        setsListView = (ListView) view.findViewById(R.id.sets_list);
        setsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int databaseID = map.get(position);
                String name = setsListView.getItemAtPosition(position).toString();
                String data = name + "," + Integer.toString(databaseID);
                mainActivity.switchFragment("set layout", data);
            }
        });

        displaySets();

        return view;
    }

    public void setActivity(MainActivity activity) {mainActivity = activity; }


    // Display the current list of contacts
    public void displaySets() {
        Cursor cursor = mainActivity.mDbAccess.getSets();

        // Iterate through results and store them in a list
        List names = new ArrayList<String>();
        databaseIds.clear();
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(FlashcardTableEntries.COLUMN_NAME_TITLE));
            names.add(name);
            databaseIds.add(cursor.getInt(cursor.getColumnIndex(FlashcardTableEntries._ID)));
        }
        cursor.close();

        // Display the sets to the list view
        ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, names);
        setsListView.setAdapter(adapter);
        int count = 0;
        for (int i : databaseIds) {
            map.put(count, i);
            count++;
        }
    }
}