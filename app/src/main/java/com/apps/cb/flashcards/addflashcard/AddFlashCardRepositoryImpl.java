package com.apps.cb.flashcards.addflashcard;

import com.apps.cb.flashcards.addflashcard.events.AddFlashCardEvent;
import com.apps.cb.flashcards.domain.FirebaseAPI;
import com.apps.cb.flashcards.domain.FirebaseActionListenerCallback;
import com.apps.cb.flashcards.domain.FirebaseEventListenerCallback;
import com.apps.cb.flashcards.entities.FlashCard;
import com.apps.cb.flashcards.lib.base.EventBus;
import com.apps.cb.flashcards.lib.base.ImageStorage;
import com.apps.cb.flashcards.lib.base.ImageStorageFinishedListener;
import com.facebook.login.LoginManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.io.File;

/**
 * Fashr
 * Created by cristianbehe on 7/14/2016.
 */
public class AddFlashCardRepositoryImpl implements AddFlashCardRepository {

    private EventBus eventBus;
    private FirebaseAPI firebaseAPI;
    private ImageStorage imageStorage;

    public AddFlashCardRepositoryImpl(EventBus eventBus, FirebaseAPI firebaseAPI, ImageStorage imageStorage) {
        this.eventBus = eventBus;
        this.firebaseAPI = firebaseAPI;
        this.imageStorage = imageStorage;
    }

    @Override
    public void logout() {
        LoginManager.getInstance().logOut();
        firebaseAPI.logout();
    }

    @Override
    public void saveCard(FlashCard card) {
        if (card.getId() == null || card.getId().isEmpty())
            createCard(card);
        else
            firebaseAPI.update(card);
        post(AddFlashCardEvent.FLASHCARD_ADDED);
    }

    private void createCard(final FlashCard card) {
        final String id = firebaseAPI.create(card);
        card.setId(id);

        post(AddFlashCardEvent.UPLOAD_COMPLETE);
        imageStorage.upload(new File(card.getResourceUrl()), id, new ImageStorageFinishedListener() {

            @Override
            public void onSuccess() {
                String url = imageStorage.getImageUrl(id);
                card.setResourceUrl(url);
                firebaseAPI.update(card);

                post(AddFlashCardEvent.UPLOAD_COMPLETE);
            }

            @Override
            public void onError(String error) {
                post(AddFlashCardEvent.UPLOAD_ERROR, error);
            }
        });
    }

    private void post(int type) {
        post(type, null, null);
    }

    private void post(int type, String message) {
        post(type, message, null);
    }

    private void post(int type, String message, FlashCard card) {
        AddFlashCardEvent event = new AddFlashCardEvent();
        event.setType(type);
        event.setMessage(message);
        event.setCard(card);
        eventBus.post(event);
    }
}
