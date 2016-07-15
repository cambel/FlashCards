package com.apps.cb.flashcards.main;

import com.apps.cb.flashcards.entities.FlashCard;

/**
 * Fashr
 * Created by cristianbehe on 7/13/2016.
 */
public interface MainRepository {
    void logout();

    void subscribe();
    void unsubscribe();

    void updateCard(FlashCard card);
    void removeCard(FlashCard card);
}
