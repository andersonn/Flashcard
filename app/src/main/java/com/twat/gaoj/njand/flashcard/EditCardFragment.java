package com.twat.gaoj.njand.flashcard;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.twat.gaoj.njand.flashcard.Database.DatabaseContract;

public class EditCardFragment extends Fragment {
    MainActivity mainActivity;
    EditText cardFront;
    EditText cardBack;
    Button saveButton;
    Button deleteButton;
    Button cancelButton;
    String setName;
    int setID;
    int currentID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.edit_card, container, false);
        cardFront = (EditText) view.findViewById(R.id.front_edit);
        cardBack = (EditText) view.findViewById(R.id.back_edit);
        saveButton = (Button) view.findViewById(R.id.save_button);
        deleteButton = (Button) view.findViewById(R.id.delete_button);
        cancelButton = (Button) view.findViewById(R.id.cancel_button);

        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mainActivity.mDbAccess.updateCardInSet(currentID, cardFront.getText().toString(), cardBack.getText().toString());
                mainActivity.switchFragment("edit set", setName + "," + setID);
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mainActivity.mDbAccess.deleteCardFromSet(currentID);
                mainActivity.switchFragment("edit set", setName + "," + setID);
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mainActivity.switchFragment("edit set", setName + "," + setID);
            }
        });

        return view;
    }

    public void setActivity(MainActivity activity) {
        mainActivity = activity;
    }

    @Override
    public void onResume() {
        final String data = getArguments().getString("message");

        String[] split = data.split(",");

        setName = split[0];
        setID = Integer.parseInt(split[1]);
        currentID = Integer.parseInt(split[2]);
        Cursor cardCursor = mainActivity.mDbAccess.getCardById(currentID);
        cardCursor.moveToNext();
        cardFront.setText(cardCursor.getString(cardCursor.getColumnIndex(DatabaseContract.CardEntries.COLUMN_NAME_FRONT)));
        cardBack.setText(cardCursor.getString(cardCursor.getColumnIndex(DatabaseContract.CardEntries.COLUMN_NAME_BACK)));

        super.onResume();
    }

}