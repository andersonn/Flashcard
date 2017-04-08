package com.twat.gaoj.njand.flashcard;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import com.twat.gaoj.njand.flashcard.Database.DatabaseContract;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditSetFragment extends Fragment {
    MainActivity mainActivity;
    TextView setName;
    FloatingActionButton newCard;
    FloatingActionButton back;
    int currentID;
    ArrayList<Integer> databaseIds = new ArrayList<Integer>();    // Ids for the names in the list view
    Map<Integer, Integer> map = new HashMap<Integer, Integer>();
    ListView cardsListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.edit_set, container, false);

        cardsListView = (ListView)view.findViewById(R.id.cards_list);
        setName = (TextView) view.findViewById(R.id.Name_Of_Set);
        back = (FloatingActionButton) view.findViewById(R.id.back_from_edit_set);
        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mainActivity.switchFragment("set layout", setName.getText() + "," + currentID);
            }
        });

        newCard = (FloatingActionButton)view.findViewById(R.id.add_new_card);
        newCard.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                LayoutInflater li = LayoutInflater.from(getContext());
                View promptsView = li.inflate(R.layout.prompts_card, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                alertDialogBuilder.setView(promptsView);
                final EditText userInputFront = (EditText) promptsView.findViewById(R.id.editTextDialogUserInputFront);
                final EditText userInputBack = (EditText) promptsView.findViewById(R.id.editTextDialogUserInputBack);
                alertDialogBuilder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        mainActivity.mDbAccess.addCardToSet(currentID, userInputFront.getText().toString(), userInputBack.getText().toString());
                        displayCards();
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

        cardsListView = (ListView) view.findViewById(R.id.cards_list);
        cardsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int databaseID = map.get(position);
                mainActivity.switchFragment("edit card", setName.getText() + "," + Integer.toString(currentID) + "," + Integer.toString(databaseID)); //send along set info too, so you can return to the right set afterward
            }
        });

        displayCards();

        return view;
    }

    public void setActivity(MainActivity activity) {
        mainActivity = activity;
    }

    @Override
    public void onResume() {
        final String data = getArguments().getString("message");

        String[] split = data.split(",");
        final String name = split[0];
        currentID = Integer.parseInt(split[1]);

        setName.setText(name);
        displayCards();

        super.onResume();
    }

    // Display the current list of cards
    public void displayCards() {
        Cursor cursor = mainActivity.mDbAccess.getCardsInSet(currentID);
        // Iterate through results and store them in a list
        List<String> names = new ArrayList<String>();
        databaseIds.clear();
        while (cursor.moveToNext()) {
            String front = cursor.getString(cursor.getColumnIndex(DatabaseContract.CardEntries.COLUMN_NAME_FRONT));
            names.add(front);
            databaseIds.add(cursor.getInt(cursor.getColumnIndex(DatabaseContract.CardEntries._ID)));
        }
        cursor.close();

        // Display the cards to the list view
        ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, names);
        cardsListView.setAdapter(adapter);
        int count = 0;
        for (int i : databaseIds) {
            map.put(count, i);
            count++;
        }
    }
}
