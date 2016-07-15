package com.apps.cb.flashcards.addflashcard;

/**
 * Fashr
 * Created by cristianbehe on 7/14/2016.
 */
public class SessionInteractorImpl implements SessionInteractor{

    AddFlashCardRepository repository;

    public SessionInteractorImpl(AddFlashCardRepository repository) {
        this.repository = repository;
    }

    @Override
    public void executeLogout() {
        repository.logout();
    }
}
