package com.apps.cb.flashcards.main;

import com.apps.cb.flashcards.entities.FlashCard;

/**
 * Fashr
 * Created by cristianbehe on 7/13/2016.
 */
public class MainInteractorImpl implements MainInteractor {

    private MainRepository repository;

    public MainInteractorImpl(MainRepository repository) {
        this.repository = repository;
    }

    @Override
    public void logout() {
        repository.logout();
    }

    @Override
    public void subscribe() {
        repository.subscribe();
    }

    @Override
    public void unsubscribe() {
        repository.unsubscribe();
    }

    @Override
    public void executeRemove(FlashCard card) {
        repository.removeCard(card);
    }

    @Override
    public void executeUpdate(FlashCard card) {
        repository.updateCard(card);
    }


}
