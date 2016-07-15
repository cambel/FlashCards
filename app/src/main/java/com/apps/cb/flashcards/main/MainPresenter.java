package com.apps.cb.flashcards.main;

import com.apps.cb.flashcards.entities.FlashCard;
import com.apps.cb.flashcards.main.events.MainEvent;

/**
 * Fashr
 * Created by cristianbehe on 7/13/2016.
 */
public interface MainPresenter {
    void onCreate();

    void onDestroy();

    void logout();
    void subscribe();
    void unsubscribe();

    void toggleFavorite(FlashCard card);
    void removeCard(FlashCard card);

    void onEventMainThread(MainEvent event);
}
