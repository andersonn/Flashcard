package com.twat.gaoj.njand.flashcard;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.twat.gaoj.njand.flashcard.Database.MyDbAccess;

public class MainActivity extends AppCompatActivity {
    MyDbAccess mMyDbAccess;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMyDbAccess = new MyDbAccess(getApplicationContext());
    }
}
