package com.twat.gaoj.njand.flashcard.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.twat.gaoj.njand.flashcard.Database.DatabaseContract.SelectQueries;
import com.twat.gaoj.njand.flashcard.Database.DatabaseContract.FlashcardTableEntries;
import com.twat.gaoj.njand.flashcard.Database.DatabaseContract.CardEntries;
import com.twat.gaoj.njand.flashcard.Database.DatabaseContract.UpdateQueries;
import com.twat.gaoj.njand.flashcard.Database.DatabaseContract.DeleteQueries;




/**
 * Created by njand on 4/3/2017.
 */

public class MyDbAccess implements DbAccess {
    DbSetup mDbSetup;
    SQLiteDatabase mDb;

    public MyDbAccess(Context context){
        mDbSetup = new DbSetup(context);
        mDb = mDbSetup.getWritableDatabase();
    }
    @Override
    public Cursor getSets() {
        String columns[] = {FlashcardTableEntries._ID, FlashcardTableEntries.COLUMN_NAME_TITLE};
        return mDb.query(FlashcardTableEntries.TABLE_NAME, columns, null, null, null, null, null);
    }

    @Override
    public Cursor getCardsInSet(int setId) {
        String columns[] = {CardEntries._ID, CardEntries.COLUMN_NAME_FRONT, CardEntries.COLUMN_NAME_BACK, CardEntries.COLUMN_NAME_SET};
        String args[] = {Integer.toString(setId)};

        return mDb.query(CardEntries.TABLE_NAME, columns, SelectQueries.SQL_FIND_CARD_IN_SET, args, null, null, null);
    }

    @Override
    public void addCardToSet(int setId, String front, String back) {
        ContentValues values = new ContentValues();
        values.put(CardEntries.COLUMN_NAME_FRONT, front);
        values.put(CardEntries.COLUMN_NAME_BACK, back);
        values.put(CardEntries.COLUMN_NAME_SET, setId);
        mDb.insert(CardEntries.TABLE_NAME, null, values);
    }

    @Override
    public void deleteCardFromSet(int cardId) {
        String[] args = {Integer.toString(cardId)};
        mDb.delete(CardEntries.TABLE_NAME, DeleteQueries.SQL_DELETE_CARD_ENTRY, args);
    }

    @Override
    public void updateCardInSet(int cardId, String front, String back) {
        ContentValues values = new ContentValues();
        values.put(CardEntries.COLUMN_NAME_FRONT, CardEntries.COLUMN_NAME_BACK);
        String[] args = {Integer.toString(cardId)};
        mDb.update(CardEntries.TABLE_NAME, values, UpdateQueries.SQL_UPDATE_CARD_IN_SET, args);
    }

    @Override
    public void newSet(String setName) {
        ContentValues values = new ContentValues();
        values.put(FlashcardTableEntries.COLUMN_NAME_TITLE, setName);
        mDb.insert(FlashcardTableEntries.TABLE_NAME, null, values);
    }

    @Override
    public Cursor getStatsOnSet(int setId) {
        String columns[] = {FlashcardTableEntries._ID, FlashcardTableEntries.COLUMN_NAME_TITLE,
                            FlashcardTableEntries.COLUMN_NAME_PERCENT_CORRECT_THIS_SESSION,
                            FlashcardTableEntries.COLUMN_NAME_TIME_THIS_SESSION,
                            FlashcardTableEntries.COLUMN_NAME_PERCENT_CORRECT_ALL_TIME,
                            FlashcardTableEntries.COLUMN_NAME_TOTAL_TIME,
                            FlashcardTableEntries.COLUMN_NAME_SESSIONS_COMPLETED,
                            FlashcardTableEntries.COLUMN_NAME_AVG_TIME};

        String args[] = {Integer.toString(setId)};
        return mDb.query(FlashcardTableEntries.TABLE_NAME, columns, SelectQueries.SQL_FIND_SET,args, null, null, null);
    }

    @Override
    public void updateStats(int setId, float percentCorrectThisSession, int timeThisSession,
                            float percentCorrectAllTime, int totalTime, int sessionsCompleted,
                            float averageTime) {

        ContentValues values = new ContentValues();
        values.put(FlashcardTableEntries.COLUMN_NAME_PERCENT_CORRECT_THIS_SESSION, percentCorrectThisSession);
        values.put(FlashcardTableEntries.COLUMN_NAME_TIME_THIS_SESSION, timeThisSession);
        values.put(FlashcardTableEntries.COLUMN_NAME_PERCENT_CORRECT_ALL_TIME, percentCorrectAllTime);
        values.put(FlashcardTableEntries.COLUMN_NAME_TOTAL_TIME, totalTime);
        values.put(FlashcardTableEntries.COLUMN_NAME_SESSIONS_COMPLETED, sessionsCompleted);
        values.put(FlashcardTableEntries.COLUMN_NAME_AVG_TIME, averageTime);

        String[] args = {Integer.toString(setId)};
        mDb.update(FlashcardTableEntries.TABLE_NAME, values, UpdateQueries.SQL_UPDATE_STATS, args);

    }

    @Override
    public void close() {
        mDb.close();
    }
}
