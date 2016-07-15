package com.apps.cb.flashcards.addflashcard;

import com.apps.cb.flashcards.entities.FlashCard;

/**
 * Fashr
 * Created by cristianbehe on 7/14/2016.
 */
public interface AddFlashCardRepository {

    void logout();
    void saveCard(FlashCard card);

}
