package com.apps.cb.flashcards.main;

import com.apps.cb.flashcards.domain.FirebaseAPI;
import com.apps.cb.flashcards.domain.FirebaseActionListenerCallback;
import com.apps.cb.flashcards.domain.FirebaseEventListenerCallback;
import com.apps.cb.flashcards.entities.FlashCard;
import com.apps.cb.flashcards.lib.base.EventBus;
import com.apps.cb.flashcards.main.events.MainEvent;
import com.facebook.login.LoginManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

/**
 * Fashr
 * Created by cristianbehe on 7/13/2016.
 */
public class MainRepositoryImpl implements MainRepository {

    private EventBus eventBus;
    private FirebaseAPI firebase;

    public MainRepositoryImpl(EventBus eventBus, FirebaseAPI firebaseAPI) {
        this.eventBus = eventBus;
        this.firebase = firebaseAPI;
    }

    @Override
    public void logout() {
        LoginManager.getInstance().logOut();
        firebase.logout();
    }

    @Override
    public void subscribe() {
        firebase.checkForData(new FirebaseActionListenerCallback() {
            @Override
            public void onSuccess() {
            }

            @Override
            public void onError(String error) {
                if (error != null) {
                    post(MainEvent.READ_EVENT, error);
                } else {
                    post(MainEvent.READ_EVENT, "");
                }
            }
        });
        firebase.subscribe(new FirebaseEventListenerCallback() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot) {
                FlashCard FlashCard = dataSnapshot.getValue(FlashCard.class);
                FlashCard.setId(dataSnapshot.getKey());
                post(MainEvent.READ_EVENT, FlashCard);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot) {
                FlashCard FlashCard = dataSnapshot.getValue(FlashCard.class);
                FlashCard.setId(dataSnapshot.getKey());
                post(MainEvent.UPDATE_EVENT, FlashCard);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                FlashCard FlashCard = dataSnapshot.getValue(FlashCard.class);
                FlashCard.setId(dataSnapshot.getKey());
                post(MainEvent.DELETE_EVENT, FlashCard);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                post(MainEvent.READ_EVENT, error.getMessage());
            }
        });
    }

    @Override
    public void unsubscribe() {
        firebase.unsubscribe();
    }

    @Override
    public void updateCard(FlashCard card) {
        firebase.update(card);
    }

    @Override
    public void removeCard(final FlashCard card) {
        firebase.remove(card, new FirebaseActionListenerCallback() {
            @Override
            public void onSuccess() {
                post(MainEvent.DELETE_EVENT, card);
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    private void post(int type, String message) {
        post(type, message, null);
    }

    private void post(int type, FlashCard card) {
        post(type, null, card);
    }

    private void post(int type, String message, FlashCard card) {
        MainEvent event = new MainEvent();
        event.setType(type);
        event.setCard(card);
        event.setMessage(message);
        eventBus.post(event);
    }


}
