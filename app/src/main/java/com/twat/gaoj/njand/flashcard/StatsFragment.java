package com.twat.gaoj.njand.flashcard;

import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.twat.gaoj.njand.flashcard.Database.DatabaseContract;

public class StatsFragment extends Fragment {

    MainActivity mainActivity;
    TextView setName;
    int setID;
    TextView lastCorrect;
    TextView lastTime;
    TextView totalTime;
    TextView totalSessions;
    TextView averageTime;
    FloatingActionButton backButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.stats_layout, container, false);
        setName = (TextView) view.findViewById(R.id.setName);
        lastCorrect = (TextView) view.findViewById(R.id.percentCorrect);
        lastTime = (TextView) view.findViewById(R.id.time);
        totalTime = (TextView) view.findViewById(R.id.timeTotal);
        totalSessions = (TextView) view.findViewById(R.id.sessionsCompleted);
        averageTime = (TextView) view.findViewById(R.id.timeAverage);

        backButton = (FloatingActionButton) view.findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mainActivity.switchFragment("set layout", setName.getText() + "," + setID);
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
        setID = Integer.parseInt(split[0]);


        Cursor statCursor = mainActivity.mDbAccess.getStatsOnSet(setID);
        statCursor.moveToNext();
        setName.setText(statCursor.getString(statCursor.getColumnIndex(DatabaseContract.FlashcardTableEntries.COLUMN_NAME_TITLE)));
        lastCorrect.setText(statCursor.getString(statCursor.getColumnIndex(DatabaseContract.FlashcardTableEntries.COLUMN_NAME_PERCENT_CORRECT_THIS_SESSION)));
        lastTime.setText(statCursor.getString(statCursor.getColumnIndex(DatabaseContract.FlashcardTableEntries.COLUMN_NAME_TIME_THIS_SESSION)));
        totalTime.setText(statCursor.getString(statCursor.getColumnIndex(DatabaseContract.FlashcardTableEntries.COLUMN_NAME_TOTAL_TIME)));
        totalSessions.setText(statCursor.getString(statCursor.getColumnIndex(DatabaseContract.FlashcardTableEntries.COLUMN_NAME_SESSIONS_COMPLETED)));
        int totalTimeInt = Integer.parseInt(totalTime.getText().toString());
        int totalSessionsInt = Integer.parseInt(totalSessions.getText().toString());
        if (totalSessionsInt != 0) {
            averageTime.setText(String.format("%d", totalTimeInt / totalSessionsInt));
        }
        else {
            averageTime.setText("0");
        }

        super.onResume();
    }

}
