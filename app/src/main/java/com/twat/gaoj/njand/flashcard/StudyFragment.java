package com.twat.gaoj.njand.flashcard;

import android.database.Cursor;
import android.os.Bundle;

import java.util.Random;
import android.support.v4.app.Fragment;
import java.util.Collections;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.twat.gaoj.njand.flashcard.Database.DatabaseContract;

import java.util.ArrayList;

public class StudyFragment extends Fragment {
    MainActivity mainActivity;
    Button cancelButton;
    Button submitButton;
    TextView questionText;
    String currentID;
    String setTitle;
    RadioGroup radioGroup;

    ArrayList<String> questionsList;
    ArrayList<String> answersList;
    int studyIndex = 0;
    String selection = "";
    int correct = 0;
    int incorrect = 0;
    long startTime = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.study, container, false);

        cancelButton = (Button)view.findViewById(R.id.cancel_study);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int percentCorrect = 0;
                if (incorrect + correct != 0) {
                    percentCorrect = (correct * 100) / (correct + incorrect);
                }
                int sessionDuration = (int) ((System.currentTimeMillis() - startTime) / 1000);
                Cursor statCursor = mainActivity.mDbAccess.getStatsOnSet(Integer.parseInt(currentID));
                statCursor.moveToNext();
                int totalTime = Integer.parseInt(statCursor.getString(statCursor.getColumnIndex(DatabaseContract.FlashcardTableEntries.COLUMN_NAME_TOTAL_TIME)));
                int totalSessions = Integer.parseInt(statCursor.getString(statCursor.getColumnIndex(DatabaseContract.FlashcardTableEntries.COLUMN_NAME_SESSIONS_COMPLETED)));
                mainActivity.mDbAccess.updateStats(Integer.parseInt(currentID), percentCorrect, sessionDuration, 0, totalTime + sessionDuration, ++totalSessions);
                mainActivity.switchFragment("set layout", setTitle + "," + currentID);
            }
        });

        questionText = (TextView)view.findViewById(R.id.question);

        radioGroup = (RadioGroup)view.findViewById(R.id.choices);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton selected=(RadioButton)view.findViewById(checkedId);
                try {
                    selection = selected.getText().toString();
                } catch (Exception e) {
                    selection = "";
                }
            }
        });

        submitButton = (Button)view.findViewById(R.id.submit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Check if right
                if (selection == answersList.get(studyIndex)) {
                    Toast.makeText(getActivity(), "Correct!", Toast.LENGTH_LONG).show();
                    correct++;
                } else {
                    Toast.makeText(getActivity(), "Incorrect. Correct answer was " + answersList.get(studyIndex), Toast.LENGTH_LONG).show();
                    incorrect++;
                }

                // Move onto next card
               if (studyIndex >= questionsList.size() - 1) {
                   studyIndex = 0;
               } else {
                   studyIndex += 1;
                   generateChoices(studyIndex);
               }
                questionText.setText(questionsList.get(studyIndex));
                radioGroup.clearCheck();
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

        generateChoices(studyIndex);

        startTime = System.currentTimeMillis();
        correct = 0;
        incorrect = 0;

        super.onResume();
    }

    public void generateChoices(int cardIndex) {
        Random random = new Random();
        String correctString = answersList.get(cardIndex);

        ArrayList<String> choices = new ArrayList<String>(answersList.size());
        for (String choice : answersList) choices.add(choice); // Make choices, a copy of answersList
        choices.remove(cardIndex); // Remove correct answer from possible choices
        Collections.shuffle(choices); // Shuffle choices

        choices = new ArrayList<String>(choices.subList(0,3)); // Take first 3 from shuffled choices
        choices.add(correctString); // Add correct choice
        Collections.shuffle(choices); // Shuffle

        questionText.setText(questionsList.get(cardIndex));
        for (int i=0; i< radioGroup.getChildCount(); i++) {
            RadioButton rbutton = (RadioButton)radioGroup.getChildAt(i);
            rbutton.setText(choices.get(i));
        }

    }
}