package com.twat.gaoj.njand.flashcard;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;

import com.twat.gaoj.njand.flashcard.Database.DbAccess;
import com.twat.gaoj.njand.flashcard.Database.MyDbAccess;

public class MainActivity extends FragmentActivity {
    EditSetFragment editSetFragment;
    SetLayoutFragment setLayoutFragment;
    MainMenuFragment mainMenuFragment;
    StudyFragment studyFragment;
    DbAccess mDbAccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the fragments if they do not exist
        if (editSetFragment == null) {
            editSetFragment = new EditSetFragment();
            editSetFragment.setActivity(this);
        }

        if (mainMenuFragment == null) {
            mainMenuFragment = new MainMenuFragment();
            mainMenuFragment.setActivity(this);
        }

        if (setLayoutFragment == null) {
            setLayoutFragment = new SetLayoutFragment();
            setLayoutFragment.setActivity(this);
        }

        if (studyFragment == null) {
            studyFragment = new StudyFragment();
            studyFragment.setActivity(this);
        }

        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().add(R.id.activity_main, mainMenuFragment).commit();

        if (mDbAccess == null) {
            mDbAccess = new MyDbAccess(getApplicationContext());
        }

    }

    public void switchFragment(String newFragment, String data) {
        Bundle bundle = new Bundle();
        bundle.putString("message", data);

        if (newFragment == "edit set") {
            FragmentManager fm = getSupportFragmentManager();
            editSetFragment.setArguments(bundle);
            fm.beginTransaction().replace(R.id.activity_main, editSetFragment).commit();
        }
        else if (newFragment == "set layout") {
            FragmentManager fm = getSupportFragmentManager();
            setLayoutFragment.setArguments(bundle);
            fm.beginTransaction().replace(R.id.activity_main, setLayoutFragment).commit();
        }
        else if (newFragment == "main menu") {
            FragmentManager fm = getSupportFragmentManager();
            mainMenuFragment.setArguments(bundle);
            fm.beginTransaction().replace(R.id.activity_main, mainMenuFragment).commit();
        }
        else if (newFragment == "study") {
            FragmentManager fm = getSupportFragmentManager();
            studyFragment.setArguments(bundle);
            fm.beginTransaction().replace(R.id.activity_main, studyFragment).commit();
        }
    }
}
