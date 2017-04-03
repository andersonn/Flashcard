package com.twat.gaoj.njand.flashcard.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by njand on 4/3/2017.
 */

public class DbSetup extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Flashcard.db";

    public DbSetup(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //TODO fix to something better.
        sqLiteDatabase.execSQL(DatabaseContract.DeleteQueries.SQL_DELETE_CARD_TABLE);
        sqLiteDatabase.execSQL(DatabaseContract.DeleteQueries.SQL_DELETE_FLASHCARD_TABLE);
        onCreate(sqLiteDatabase);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DatabaseContract.CreateQueries.SQL_CREATE_FLASHCARD_TABLE);
        sqLiteDatabase.execSQL(DatabaseContract.CreateQueries.SQL_CREATE_CARD_TABLE);
    }
}
