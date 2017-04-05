package com.twat.gaoj.njand.flashcard;

import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class SetLayoutFragment extends Fragment {
    MainActivity mainActivity;
    TextView setTitle;
    TextView cardCount;
    Button editSetButton;
    FloatingActionButton backButton;
    String currentID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.set_layout, container, false);

        setTitle = (TextView) view.findViewById(R.id.set_title);
        cardCount = (TextView) view.findViewById(R.id.numCards);

        editSetButton = (Button)view.findViewById(R.id.editButton);
        editSetButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mainActivity.switchFragment("edit set", setTitle.getText() + "," + currentID);
            }
        });

        backButton = (FloatingActionButton) view.findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mainActivity.switchFragment("main menu", "");
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
        final String name = split[0];
        currentID = split[1];
        final int id = Integer.parseInt(split[1]);
        setTitle.setText(name);

        Cursor cursor = mainActivity.mDbAccess.getCardsInSet(id);

        int counter = 0;
        while (cursor.moveToNext()) {
            counter += 1;
        }
        cardCount.setText(Integer.toString(counter));

        super.onResume();

    }


}