package com.apps.cb.flashcards.addflashcard;

import com.apps.cb.flashcards.entities.FlashCard;

/**
 * Fashr
 * Created by cristianbehe on 7/14/2016.
 */
public class AddFlashCardInteractorImpl implements AddFlashCardInteractor {

    AddFlashCardRepository repository;

    public AddFlashCardInteractorImpl(AddFlashCardRepository repository) {
        this.repository = repository;
    }

    @Override
    public void executeCreate(FlashCard card) {
        repository.saveCard(card);
    }

}
