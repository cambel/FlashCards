package com.apps.cb.flashcards.main.ui;

import com.apps.cb.flashcards.entities.FlashCard;

/**
 * Fashr
 * Created by cristianbehe on 7/13/2016.
 */
public interface MainView {

    void onFlashCardAdded(FlashCard card);
    void onFlashCardRemoved(FlashCard card);

    void onFlashCardUpdated(FlashCard card);
}
