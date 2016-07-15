package com.apps.cb.flashcards.addflashcard;

import com.apps.cb.flashcards.addflashcard.events.AddFlashCardEvent;
import com.apps.cb.flashcards.entities.FlashCard;

/**
 * Fashr
 * Created by cristianbehe on 7/14/2016.
 */
public interface AddFlashCardPresenter {

    void onCreate();
    void onDestroy();

    void saveCard(FlashCard card);
    void logout();

    void onMainThread(AddFlashCardEvent event);
}
