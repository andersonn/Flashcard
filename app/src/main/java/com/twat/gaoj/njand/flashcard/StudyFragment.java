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

import com.twat.gaoj.njand.flashcard.Database.DatabaseContract;

import java.util.ArrayList;

public class StudyFragment extends Fragment {
    MainActivity mainActivity;
    Button cancelButton;
    Button submitButton;
    TextView questionText;
    String currentID;
    String setTitle;

    ArrayList<String> questionsList;
    ArrayList<String> answersList;
    Integer studyIndex = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.study, container, false);

        cancelButton = (Button)view.findViewById(R.id.cancel_study);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mainActivity.switchFragment("set layout", setTitle + "," + currentID);
            }
        });

        questionText = (TextView)view.findViewById(R.id.question);

        submitButton = (Button)view.findViewById(R.id.submit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
               if (studyIndex >= questionsList.size() - 1) {
                   studyIndex = 0;
               } else {
                   studyIndex += 1;
               }
                questionText.setText(questionsList.get(studyIndex));
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
        setTitle = split[0];
        currentID = split[1];
        final int id = Integer.parseInt(split[1]);

        Cursor cursor = mainActivity.mDbAccess.getCardsInSet(id);

        questionsList = new ArrayList<String>();
        answersList = new ArrayList<String>();
        while (cursor.moveToNext()) {
            String front = cursor.getString(cursor.getColumnIndex(DatabaseContract.CardEntries.COLUMN_NAME_FRONT));
            questionsList.add(front);
            String back = cursor.getString(cursor.getColumnIndex(DatabaseContract.CardEntries.COLUMN_NAME_BACK));
            answersList.add(back);
        }

        questionText.setText(questionsList.get(studyIndex));
        super.onResume();
    }


}