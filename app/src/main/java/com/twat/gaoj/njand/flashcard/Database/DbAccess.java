package com.twat.gaoj.njand.flashcard.Database;

import android.database.Cursor;

/**
 * Created by njand on 4/3/2017.
 */

public interface DbAccess {

    //Returns all flashcard set names and id's
    public Cursor getSets();

    /**
     * gets cards in a specific set.
     * @param setId - primary key of set.
     * @return
     */
    public Cursor getCardsInSet(int setId);

    /**
     * gets a single card given its id and set
     * @param cardId - primary key of this card.
     */
    public Cursor getCardById(int cardId);

    /**
     *
     * @param setId - primary key of set
     * @param front - question on front of card.
     * @param back - answer to question.
     */
    void addCardToSet(int setId, String front, String back);

    /**
     *
     * @param cardId - primary key of this card.
     */
    void deleteCardFromSet(int cardId);

    /**
     *
     * @param cardId - primary key of this card.
     * @param front - question on the front.
     * @param back - question on back.
     */
    void updateCardInSet(int cardId, String front, String back);

    /**
     *
     * @param setName - name of new set.
     */
    void newSet(String setName);

    /**
     *
     * @param setId - get statistics on this set.
     * @return
     */
    Cursor getStatsOnSet(int setId);

    /**
     *
     * @param setId
     * @param percentCorrectThisSession
     * @param timeThisSession
     * @param totalTime
     * @param sessionsCompleted
     */
    void updateStats(int setId, float percentCorrectThisSession, int timeThisSession,
                     int totalTime, int sessionsCompleted);

    /**
     *
     * @param setId
     */
    void deleteSet(int setId);

    /**
     * Close the database.
     */
    void close();
}
