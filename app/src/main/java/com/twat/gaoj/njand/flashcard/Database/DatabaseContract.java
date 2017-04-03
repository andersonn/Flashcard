package com.twat.gaoj.njand.flashcard.Database;

import android.provider.BaseColumns;
import android.provider.VoicemailContract;

/**
 * Created by njand on 4/3/2017.
 */

public class DatabaseContract {
    private DatabaseContract(){};

    public static class FlashcardTableEntries implements BaseColumns{
        public static final String TABLE_NAME = "flashcards";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_PERCENT_CORRECT_THIS_SESSION = "pcToday";
        public static final String COLUMN_NAME_TIME_THIS_SESSION = "tToday";
        public static final String COLUMN_NAME_PERCENT_CORRECT_ALL_TIME = "pcAll";
        public static final String COLUMN_NAME_TOTAL_TIME = "tTotal";
        public static final String COLUMN_NAME_SESSIONS_COMPLETED = "sCompleted";
        public static final String COLUMN_NAME_AVG_TIME = "aTime";
    }

    public static class CardEntries implements BaseColumns{
        public static final String TABLE_NAME = "cards";
        public static final String COLUMN_NAME_FRONT = "front";
        public static final String COLUMN_NAME_BACK = "back";
        public static final String COLUMN_NAME_SET = "flashSet";
    }

    public static class CreateQueries{

        public static final String SQL_CREATE_FLASHCARD_TABLE =
                "CREATE TABLE " + FlashcardTableEntries.TABLE_NAME + " (" +
                        FlashcardTableEntries._ID + " INTEGER PRIMARY KEY," +
                        FlashcardTableEntries.COLUMN_NAME_TITLE + " TEXT," +
                        FlashcardTableEntries.COLUMN_NAME_PERCENT_CORRECT_THIS_SESSION + " REAL DEFAULT 0," +
                        FlashcardTableEntries.COLUMN_NAME_TIME_THIS_SESSION + " INTEGER DEFAULT 0," +
                        FlashcardTableEntries.COLUMN_NAME_PERCENT_CORRECT_ALL_TIME + " REAL DEFAULT 0," +
                        FlashcardTableEntries.COLUMN_NAME_TOTAL_TIME + " INTEGER DEFAULT 0," +
                        FlashcardTableEntries.COLUMN_NAME_SESSIONS_COMPLETED + " INTEGER DEFAULT 0," +
                        FlashcardTableEntries.COLUMN_NAME_AVG_TIME + " REAL DEFAULT 0)";

        public static final String SQL_CREATE_CARD_TABLE =
                "CREATE TABLE " + CardEntries.TABLE_NAME + " (" +
                        CardEntries._ID + " INTEGER PRIMARY KEY," +
                        CardEntries.COLUMN_NAME_FRONT + " TEXT," +
                        CardEntries.COLUMN_NAME_BACK + " TEXT," +
                        CardEntries.COLUMN_NAME_SET + " INTEGER," +
                        " FOREIGN KEY(" + CardEntries.COLUMN_NAME_SET + ") REFERENCES " + FlashcardTableEntries.TABLE_NAME +"(" + FlashcardTableEntries._ID + "))";

    }

    public static class SelectQueries{
        public static final String SQL_FIND_SET =
                FlashcardTableEntries._ID + " = ?";

        public static final String SQL_FIND_CARD_IN_SET =
                CardEntries.COLUMN_NAME_SET + " = ?";
    }

    public static class UpdateQueries{
        public static final String SQL_UPDATE_CARD_IN_SET =
                CardEntries._ID + " = ?";
        public static final String SQL_UPDATE_STATS =
                FlashcardTableEntries._ID + " = ?";
    }

    public static class DeleteQueries{

        public static final String SQL_DELETE_CARD_TABLE =
                "DROP TABLE IF EXISTS " + CardEntries.TABLE_NAME;

        public static final String SQL_DELETE_FLASHCARD_TABLE =
                "DROP TABLE IF EXISTS " + FlashcardTableEntries.TABLE_NAME;

        public static final String SQL_DELETE_CARD_ENTRY =
                CardEntries._ID + " = ?";

    }
}
